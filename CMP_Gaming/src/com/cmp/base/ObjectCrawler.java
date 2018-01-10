package com.cmp.base;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

import com.cmp.fragalyzer.FragalyzerConstants;
import com.cmp.fragalyzer.types.WeaponType;

public class ObjectCrawler {
	private static void printMap(Map mp) {
	    Iterator it = mp.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        System.out.println(pair.getKey() + " = " + pair.getValue());
	        it.remove(); // avoids a ConcurrentModificationException
	    }
	}
	FileWriter fw;
	ArrayList<String> temp_list;

	ArrayList<Kit> kits;

	ArrayList<String> vehiclesNonTeamlock;

	ArrayList<String> vehiclesWithTeamlock;
	ArrayList<String> allVehicles;
	String suffix;
	ArrayList<Material> materials;
	ArrayList<Vehicle> vehicles;

	public ArrayList<Vehicle> getVehicles() {
		return vehicles;
	}

	public void setVehicles(ArrayList<Vehicle> vehicles) {
		this.vehicles = vehicles;
	}
	ArrayList<ProjectileTemplate> projectiles;
	ArrayList<Weapon> weapons;
	public boolean onlyTeamlock;;

	public ObjectCrawler() {

		temp_list = new ArrayList<>();
		vehiclesNonTeamlock = new ArrayList<>();
		vehiclesWithTeamlock = new ArrayList<>();
		allVehicles = new ArrayList<>();
		setMaterials(new ArrayList<>());
		setProjectiles(new ArrayList<>());
		setWeapons(new ArrayList<>());
		setKits(new ArrayList<>());
		setVehicles(new ArrayList<>());
	}

	private void dumpAvailableVehicles(String basePath) {
		Path p = Paths.get(basePath);

		FileVisitor<Path> fv = new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

				int dot = file.toString().lastIndexOf(".");
				suffix = file.toString().substring(dot + 1);
				// if (file.toString().substring(dot + 1).equals(suffix)) {
				// System.out.println(file);
				// System.out.println();
				String fileName = file.getFileName().toString();
				switch (suffix) {
				case "tweak":
					Vehicle vehicle = new Vehicle();
					HashMap<String, String> tweakFile = getFileContent(file);
					String name = tweakFile.get("ObjectTemplate.vehicleHud.hudName");
					if (name == null)
						name = tweakFile.get("ObjectTemplate.weaponHud.hudName");
					if (name == null)
						name = "Unknown";
					vehicle.setName(name);
					vehicle.setTemplateName(fileName);
					if (!name.equals("Unknown")) {
						System.out.println("Name: " + name + " (" + fileName + ")");
						ArrayList<String> tweakList = getFileContentList(file);
						ArrayList<Gun> guns = getGunsFromVehicle(tweakList);
						Iterator<Gun> it = guns.iterator();
						while (it.hasNext())
							System.out.println(it.next().toString());

						vehicle.setMaterial(getArmorMaterial(tweakList));
						vehicle.setHP(getHP(tweakList));

					}
					getVehicles().add(vehicle);
				default:
					break;
				}

				// }

				return FileVisitResult.CONTINUE;
			}

		};

		try {
			Files.walkFileTree(p, fv);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private Weapon findWeapon(String name) {
		Iterator<Weapon> it = getWeapons().iterator();
		Weapon w = new Weapon();
		while (it.hasNext()) {
			w = it.next();
			if (w.getTemplateName() != null)
				if (w.getTemplateName().toLowerCase().equals(name.toLowerCase()))
					return w;
		}
		System.out.println("Did not find " + name);
		return new Weapon();
	}
	
	private Material findMaterial(String name) {
		Iterator<Material> it = getMaterials().iterator();
		Material w = new Material();
		while (it.hasNext()) {
			w = it.next();
			if (w.getNumber() != null)
				if (w.getNumber().toLowerCase().equals(name.toLowerCase()))
					return w;
		}
		System.out.println("Did not find " + name);
		return new Material();
	}	

	private ProjectileTemplate findProjectile(String name) {
		Iterator<ProjectileTemplate> it = getProjectiles().iterator();
		ProjectileTemplate w = new ProjectileTemplate();
		while (it.hasNext()) {
			w = it.next();
			if (w.getName() != null)
				if (w.getName().toLowerCase().equals(name.toLowerCase()))
					return w;
		}
		System.out.println("Did not find " + name);
		return new ProjectileTemplate();
	}
	
	
	private Material getArmorMaterial(ArrayList<String> tweak) {
	
		Iterator<String> it = tweak.iterator();
		String line;
		while (it.hasNext()) {
			line = it.next();
			
			if (line.startsWith("ObjectTemplate.armor.defaultMaterial")) {
				return findMaterial(line.substring(line.indexOf(" "), line.length()).trim().toLowerCase());
			}
		}
		return new Material();
	}
	
	private int getHP(ArrayList<String> tweak) {
		
		Iterator<String> it = tweak.iterator();
		String line;
		while (it.hasNext()) {
			line = it.next();
			if (line.startsWith("ObjectTemplate.armor.hitPoints")) {
				return new Integer(line.substring(line.indexOf(" "), line.length()).trim()).intValue();
			}
			
		}
		return 0;
	}	

	private double getDoubleValueForKeyFromTweakLine(String key, String line) {
		if (line.startsWith(key))
			return new Double(line.substring(key.length(), line.length()).trim()).doubleValue();
		else
			return -1;
	}

	private HashMap<String, String> getFileContent(Path file) {
		Scanner scanner;
		boolean found = false;
		// System.out.println(file.getFileName());

		HashMap<String, String> ret = new HashMap<>();
		try {
			scanner = new Scanner(file);
		} catch (Exception e) {
			return ret;
		}
		// now read the file line by line...

		while (scanner.hasNextLine() && !found) {

			String line = scanner.nextLine();
			// System.out.println(line);
			if (line.length() > 0 && line.contains(" ")) {
				String key = line.substring(0, line.indexOf(" "));
				String value = line.substring(line.indexOf(" "), line.length());
				ret.put(key, value);
			}

		}
		scanner.close();
		return ret;
	}

	private ArrayList<String> getFileContentList(Path file) {
		Scanner scanner;
		boolean found = false;

		ArrayList<String> ret = new ArrayList<>();
		try {
			scanner = new Scanner(file);
		} catch (Exception e) {
			return ret;
		}
		// now read the file line by line...

		while (scanner.hasNextLine() && !found) {

			String line = scanner.nextLine();
			// System.out.println(line);
			if (line.length() > 0 && !(line.equals(" ")) && !(line.equals("rem"))) {

				ret.add(line);
			}

		}
		scanner.close();
		return ret;
	}

	public void getGPODump(String basePath) {

	}

	private int getIntValueForKeyFromTweakLine(String key, String line) {

		int value;
		if (line.startsWith(key)) {

			try {
				value = new Integer(line.substring(key.length(), line.length()).trim()).intValue();
			} catch (NumberFormatException e) {
				return -1;
			} catch (StringIndexOutOfBoundsException e) {
				return -1;
			}
			return value;
		} else
			return -1;

	}

	public ArrayList<Kit> getKits() {
		return kits;
	}	
	
	public ArrayList<Material> getMaterials() {
		return materials;
	}

	private String getObjectNameFromLocalization(String name) {
		String localizationPath = "C://Program Files (x86)//EA Games//Battlefield 2//mods//fh2//localization//german//german_fh.txt";

		File file = new File(localizationPath);
		String realName = null;
		Scanner scanner;
		try {
			scanner = new Scanner(file);
		} catch (FileNotFoundException e) {
			return realName;
		}
		// now read the file line by line...

		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();

			if (line.contains(name)) {

				realName = line.substring(line.indexOf("/") + 1, line.lastIndexOf("/"));

			}
		}
		scanner.close();
		return realName;
	}

	public HashMap<String, String> getObjects(String basePath) {

		// Vehicles - Teamlocked
		onlyTeamlock = true;
		suffix = "con";
		readMaterialMetadata(basePath + "common//Material//");
		// printMaterials();
		
		readProjectileMetaData(basePath + "objects//weapons//Projectiles//");
		readProjectileMetaData(basePath + "objects//vehicles//sea//Common//Shipguns//");
		// printProjectiles();
		readWeapons(basePath + "objects//weapons//");
		// printWeapons();
		readKits(basePath + "objects//kits//");
		// printKits();
		dumpAvailableVehicles(basePath + "objects//Vehicles//land//");
		//printVehicles();
		return null;
	}

	public ArrayList<ProjectileTemplate> getProjectiles() {
		return projectiles;
	}

	private ArrayList<Gun> getGunsFromVehicle(ArrayList<String> tweak) {
		ArrayList<Gun> ret = new ArrayList<>();
		Iterator<String> it = tweak.iterator();
		String line;
		int count = 0;
		
		Gun gun = new Gun();
		
		while (it.hasNext()) {
			
			line = it.next().toLowerCase();
			
			if(line.startsWith("objecttemplate.create genericfirearm") || line.startsWith("objecttemplate.activesafe genericfirearm")) {
				count ++;
				if (count > 1)
					ret.add(gun);
				gun = new Gun();				
			}
			if(line.startsWith("objecttemplate.weaponhud.hudname")) 						
				gun.setName(line.substring(line.indexOf(" "), line.length()));
					
			if(line.startsWith("objecttemplate.velocity")) 								
				gun.setVelocity(new Integer(line.substring(line.indexOf(" "), line.length()).trim()).intValue());
									
			if (line.startsWith("objecttemplate.ammo.nrofmags"))
				gun.setMags(new Integer(line.substring(line.indexOf(" "), line.length()).trim()).intValue());
			
			if (line.startsWith("objecttemplate.ammo.magsizes"))
				gun.setRounds(new Integer(line.substring(line.indexOf(" "), line.length()).trim()).intValue());			
			
			if (line.startsWith("objecttemplate.projectiletemplate")) {			
				gun.setProjectile(findProjectile(line.substring(line.indexOf(" "), line.length()).trim()));
				
			}
		}
		ret.add(gun);
		return ret;
	}

	private String getValueForKeyFromTweakLine(String key, String line) {
		if (line.startsWith(key))
			return line.substring(key.length(), line.length()).trim();
		else
			return "";
	}

	public ArrayList<Weapon> getWeapons() {
		return weapons;
	}

	private void printKits() {
		Iterator<Kit> it = getKits().iterator();
		while (it.hasNext())
			System.out.println(it.next().toString());
	}

	private void printMaterials() {
		Iterator<Material> it = getMaterials().iterator();
		while (it.hasNext())
			System.out.println(it.next().toString());
	}

	private void printProjectiles() {
		Iterator<ProjectileTemplate> it = getProjectiles().iterator();
		while (it.hasNext())
			System.out.println(it.next().toString());
	}

	private void printWeapons() {
		Iterator<Weapon> it = getWeapons().iterator();
		while (it.hasNext())
			System.out.println(it.next().toString());
	}
	

	
	private void printVehicles() {
		Iterator<Vehicle> it = getVehicles().iterator();
		while (it.hasNext())
			System.out.println(it.next().toString());
	}	

	private void readKits(String basePath) {
		Path p = Paths.get(basePath);

		FileVisitor<Path> fv = new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

				int dot = file.toString().lastIndexOf(".");
				suffix = file.toString().substring(dot + 1);

				String line;
				String kitType;
				String weapon;
				WeaponType weaponType;
				switch (suffix) {
				case "inc":

					ArrayList<String> tweakList = getFileContentList(file);

					Kit kit = new Kit();
					Iterator<String> it = tweakList.iterator();
					while (it.hasNext()) {
						line = it.next();
						if (!getValueForKeyFromTweakLine("ObjectTemplate.create Kit", line).equals("")) {
							kit.setIngameName(
									getValueForKeyFromTweakLine("ObjectTemplate.create Kit", line).toLowerCase());
							kit.setTeam(
									kit.getIngameName().substring(0, kit.getIngameName().indexOf("_")).toLowerCase());
							// System.out.println(kit.getIngameName() + " " +
							// kit.getTeam());
						}

						if (!getValueForKeyFromTweakLine("ObjectTemplate.kitType", line).equals("")) {
							kitType = getValueForKeyFromTweakLine("ObjectTemplate.kitType", line);
							kit.setKitType(FragalyzerConstants.kitTypes.get(kit.getIngameName()));

						}

						if (!getValueForKeyFromTweakLine("ObjectTemplate.addTemplate", line).equals("")) {
							weapon = getValueForKeyFromTweakLine("ObjectTemplate.addTemplate", line);
							weaponType = FragalyzerConstants.weaponTypes.get(weapon.toLowerCase());
							// System.out.println("WeaponType: " + weaponType +
							// " for " + weapon);
							if (weaponType == null)
								weaponType = WeaponType.WEAPON_TYPE_UNKNOWN;
							Weapon w = findWeapon(weapon.toLowerCase());
							if (weapon.contains("wrench"))
								kit.setHasWrench(true);
							switch (weaponType) {
							case WEAPON_TYPE_CLOSE:
								kit.setCloseQuartersWeapon(w);

								break;
							case WEAPON_TYPE_RIFLE:
							case WEAPON_TYPE_SMG:
								kit.setPrimaryWeapon(w);
								break;
							case WEAPON_TYPE_PISTOL:
								kit.setSecondaryWeapon(w);
								break;
							case WEAPON_TYPE_SMOKE:
								kit.setHasSmoke(true);
								break;
							case WEAPON_TYPE_TARGETING:
								kit.setHasBinocs(true);
								break;
							case WEAPON_TYPE_GRENADE:
								if (kit.getSecondaryWeapon() == null)
									kit.setSecondaryWeapon(w);
								else
									kit.setGrenade(w);
								break;
							case WEAPON_TYPE_APMINE:
								kit.setApMine(w);
								break;
							case WEAPON_TYPE_ATGUN:
								kit.setPrimaryWeapon(w);
								break;
							case WEAPON_TYPE_ATMINE:
								kit.setSecondaryWeapon(w);
								break;
							case WEAPON_TYPE_EXPLOSIVE:
								kit.setGrenade(w);
								break;
							default:
								break;
							}

						}

					}
					if (kit.getIngameName() != null)
						getKits().add(kit);

				default:
					break;
				}

				// }

				return FileVisitResult.CONTINUE;
			}

		};

		try {
			Files.walkFileTree(p, fv);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void readMaterialMetadata(String basePath) {
		Path p = Paths.get(basePath);

		FileVisitor<Path> fv = new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				String line;

				if (file.getFileName().toString().startsWith("materialManagerDefine")) {
					ArrayList<String> materialList = getFileContentList(file);
					Iterator<String> it = materialList.iterator();
					Material material = new Material();
					while (it.hasNext()) {
						line = it.next();
						if (!getValueForKeyFromTweakLine("Material.active", line).equals("")) {
							// System.out.println(material.toString());

							material = new Material();
							material.setNumber(getValueForKeyFromTweakLine("Material.active", line));
						}
						if (!getValueForKeyFromTweakLine("Material.name", line).equals("")) {
							material.setName(getValueForKeyFromTweakLine("Material.name", line).replace("\"", ""));
							material.setStrength_in_mm(tryToGetStrengthFromName(material.getName()));
							material.setPenetration_in_mm(tryToGetPenetrationFromName(material.getName()));
							getMaterials().add(material);
						}

					}
				}

				// }

				return FileVisitResult.CONTINUE;
			}

		};

		try {
			Files.walkFileTree(p, fv);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void readProjectileMetaData(String basePath) {
		Path p = Paths.get(basePath);

		FileVisitor<Path> fv = new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

				int dot = file.toString().lastIndexOf(".");
				suffix = file.toString().substring(dot + 1);
				ProjectileTemplate projectile = new ProjectileTemplate();
				String line;
				switch (suffix) {
				case "tweak":

					ArrayList<String> tweakList = getFileContentList(file);

					Iterator<String> it = tweakList.iterator();
					while (it.hasNext()) {
						line = it.next();
						if (!getValueForKeyFromTweakLine("ObjectTemplate.activeSafe GenericProjectile", line)
								.equals("")) {
							// System.out.println(projectile.toString());
							if (projectile.getName() != null)
								getProjectiles().add(projectile);
							projectile = new ProjectileTemplate();
							projectile.setName(
									getValueForKeyFromTweakLine("ObjectTemplate.activeSafe GenericProjectile", line));
						}
						if (getDoubleValueForKeyFromTweakLine("ObjectTemplate.damage ", line) > -1)
							projectile.setDamage(getDoubleValueForKeyFromTweakLine("ObjectTemplate.damage", line));

						if (getDoubleValueForKeyFromTweakLine("ObjectTemplate.minDamage", line) > -1)
							projectile
									.setMinDamage(getDoubleValueForKeyFromTweakLine("ObjectTemplate.minDamage", line));

						if (getDoubleValueForKeyFromTweakLine("ObjectTemplate.distToStartLoseDamage", line) > -1)
							projectile.setDistToStartLoseDamage(
									getDoubleValueForKeyFromTweakLine("ObjectTemplate.distToStartLoseDamage", line));

						if (getDoubleValueForKeyFromTweakLine("ObjectTemplate.distToMinDamage", line) > -1)
							projectile.setDistToMinDamage(
									getDoubleValueForKeyFromTweakLine("ObjectTemplate.distToMinDamage", line));

						if (!getValueForKeyFromTweakLine("ObjectTemplate.material", line).equals(""))
							projectile.setMaterial(findMaterial(getValueForKeyFromTweakLine("ObjectTemplate.material", line).trim()));
					}

				default:
					break;
				}

				// }

				return FileVisitResult.CONTINUE;
			}

		};

		try {
			Files.walkFileTree(p, fv);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void readWeapons(String basePath) {
		Path p = Paths.get(basePath);

		FileVisitor<Path> fv = new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

				int dot = file.toString().lastIndexOf(".");
				suffix = file.toString().substring(dot + 1);

				switch (suffix) {
				case "tweak":
					Weapon weapon = new Weapon();
					System.out.println(file.toString());
					String line;

					ArrayList<String> tweakList = getFileContentList(file);

					Iterator<String> it = tweakList.iterator();
					while (it.hasNext()) {
						line = it.next();
						if (!getValueForKeyFromTweakLine("ObjectTemplate.activeSafe GenericFireArm", line).equals(""))
							weapon.setTemplateName(
									getValueForKeyFromTweakLine("ObjectTemplate.activeSafe GenericFireArm", line)
											.toLowerCase());
						if (!getValueForKeyFromTweakLine("ObjectTemplate.projectileTemplate", line).equals(""))
							weapon.setProjectile(
									getValueForKeyFromTweakLine("ObjectTemplate.projectileTemplate", line));
						if (getIntValueForKeyFromTweakLine("ObjectTemplate.ammo.nrOfMags", line) > -1)
							weapon.setMags(getIntValueForKeyFromTweakLine("ObjectTemplate.ammo.nrOfMags", line));
						if (getIntValueForKeyFromTweakLine("ObjectTemplate.ammo.magSize", line) > -1)
							weapon.setRounds(getIntValueForKeyFromTweakLine("ObjectTemplate.ammo.magSize", line));
						if (getIntValueForKeyFromTweakLine("ObjectTemplate.velocity", line) > -1)
							weapon.setVelocity(getIntValueForKeyFromTweakLine("ObjectTemplate.velocity", line));
					}
					weapon.setType(FragalyzerConstants.weaponTypes.get(weapon.getTemplateName()));
					weapon.setName(FragalyzerConstants.weaponNames.get(weapon.getTemplateName()));
					getWeapons().add(weapon);

				default:
					break;
				}

				// }

				return FileVisitResult.CONTINUE;
			}

		};

		try

		{
			Files.walkFileTree(p, fv);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void setKits(ArrayList<Kit> kits) {
		this.kits = kits;
	}

	public void setMaterials(ArrayList<Material> materials) {
		this.materials = materials;
	}

	public void setProjectiles(ArrayList<ProjectileTemplate> projectiles) {
		this.projectiles = projectiles;
	}

	public void setWeapons(ArrayList<Weapon> weapons) {
		this.weapons = weapons;
	}

	private int tryToGetPenetrationFromName(String name) {

		int penetration = -1;
		if (name.contains("Penetration")) {
			try {
				penetration = new Integer(name.substring(0, name.indexOf("m")).trim()).intValue();
			} catch (NumberFormatException e) {
				return -1;
			} catch (StringIndexOutOfBoundsException e) {
				return -1;
			}
			return penetration;
		} else
			return -1;
	}

	private int tryToGetStrengthFromName(String name) {

		if (name.endsWith("mm")) {
			return new Integer(name.substring(name.lastIndexOf("_") + 1, name.length() - 2)).intValue();
		} else
			return -1;
	}
}
