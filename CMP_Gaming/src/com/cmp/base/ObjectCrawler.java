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
import java.util.Arrays;
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
	ArrayList<Projectile> projectiles;
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

	private void readVehicles(String basePath) {
		Path p = Paths.get(basePath);

		FileVisitor<Path> fv = new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

				int dot = file.toString().lastIndexOf(".");
				suffix = file.toString().substring(dot + 1);
				String line;

				String fileName = file.getFileName().toString();
				switch (suffix) {
				case "tweak":
					Vehicle vehicle = new Vehicle();
					ArrayList<String> tweakFile = getFileContentList(file);
					
					Iterator<String> itf = tweakFile.iterator();
					while (itf.hasNext()) {
						line = itf.next();
						if (!getValueForKeyFromTweakLine("ObjectTemplate.vehicleHud.hudName", line).equals(""))
							vehicle.setName(
									getValueForKeyFromTweakLine("ObjectTemplate.vehicleHud.hudName", line).replace("\"", ""));
						
					}
					if (vehicle.getName()==null)
						vehicle.setName("Unknown");
		
					vehicle.setTemplateName(fileName.substring(0, fileName.indexOf(".")));
					
					vehicle.setTeam(basePath.substring(basePath.length()-4, (basePath.length()-2)));
				
					
					vehicle.setVehicleType(FragalyzerConstants.vehicleTypes.get(vehicle.getTemplateName()));
					if (!vehicle.getName().equals("Unknown")) {
						System.out.println("Name: " + vehicle.getName() + " (" + fileName + ")");
						ArrayList<String> tweakList = getFileContentList(file);
						ArrayList<Gun> guns = getGunsFromVehicle(tweakList);
						if (!guns.isEmpty()) {
							Iterator<Gun> it = guns.iterator();
						
							while (it.hasNext()) {
								if(vehicle.getPrimaryGun() == null)
									vehicle.setPrimaryGun(it.next());
								if(vehicle.getSecondaryGun() == null && it.hasNext())
									vehicle.setSecondaryGun(it.next());
								if(vehicle.getTertiaryGun() == null && it.hasNext())
									vehicle.setTertiaryGun(it.next());		
								if(it.hasNext())
									System.out.println(it.next().toString());
							}
						}
						vehicle.setMaterial(getArmorMaterial(tweakList));
						vehicle.setHP(getHP(tweakList));

					}
					if (vehicle.getName()!= "Unknown")
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

	private Projectile findProjectile(String name) {
		Iterator<Projectile> it = getProjectiles().iterator();
		Projectile w = new Projectile();
		while (it.hasNext()) {
			w = it.next();
			if (w.getName() != null)
				if (w.getName().toLowerCase().equals(name.toLowerCase()))
					return w;
		}
		System.out.println("Did not find " + name);
		return new Projectile();
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
		readKits(basePath + "objects//kits//ba");
		readKits(basePath + "objects//kits//bw");
		readKits(basePath + "objects//kits//be");
		readKits(basePath + "objects//kits//spawnable", "b");
		writeKitsToCSV(basePath, "kit_gb.csv");
		// printKits();
		setKits(new ArrayList<>());
		
		readKits(basePath + "objects//kits//cw");
		readKits(basePath + "objects//kits//spawnable", "c");
		writeKitsToCSV(basePath, "kit_ca.csv");
		// printKits();
		setKits(new ArrayList<>());		
		
		readKits(basePath + "objects//kits//ga");
		readKits(basePath + "objects//kits//gc");
		readKits(basePath + "objects//kits//gm");
		readKits(basePath + "objects//kits//gs");
		readKits(basePath + "objects//kits//gw");	
		readKits(basePath + "objects//kits//spawnable", "g");
		writeKitsToCSV(basePath, "kit_de.csv");
		// printKits();
		setKits(new ArrayList<>());		
		
		readKits(basePath + "objects//kits//ia");
		readKits(basePath + "objects//kits//spawnable", "i");
		writeKitsToCSV(basePath, "kit_it.csv");
		// printKits();
		setKits(new ArrayList<>());
		
		readKits(basePath + "objects//kits//jp");
		readKits(basePath + "objects//kits//spawnable", "j");
		writeKitsToCSV(basePath, "kit_jp.csv");
		// printKits();
		setKits(new ArrayList<>());				
		
		readKits(basePath + "objects//kits//re");
		readKits(basePath + "objects//kits//spawnable", "r");
		writeKitsToCSV(basePath, "kit_ru.csv");	
		// printKits();
		setKits(new ArrayList<>());		
		
		readKits(basePath + "objects//kits//se");
		readKits(basePath + "objects//kits//spawnable", "s");
		writeKitsToCSV(basePath, "kit_fi.csv");
		// printKits();
		setKits(new ArrayList<>());		
		
		
		readKits(basePath + "objects//kits//ua");
		readKits(basePath + "objects//kits//uc");
		readKits(basePath + "objects//kits//uw");
		readKits(basePath + "objects//kits//up");	
		readKits(basePath + "objects//kits//spawnable", "u");
		writeKitsToCSV(basePath, "kit_us.csv");
		// printKits();
		setKits(new ArrayList<>());			
		readKits(basePath + "objects//kits");
		writeKitsToCSV(basePath, "kits_all.csv");
		
		//writeKitsToCSV(basePath);
		writeWeaponsToCSV(basePath);
		
		readVehicles(basePath + "objects//Vehicles//land//de//");
		writeVehiclesToCSV(basePath, "land_de");
		writeVehiclesToCSV(basePath, "land_all");
		
		setVehicles(new ArrayList<>());
		readVehicles(basePath + "objects//Vehicles//land//jp//");
		writeVehiclesToCSV(basePath, "land_jp");
		writeVehiclesToCSV(basePath, "land_all");
		
		setVehicles(new ArrayList<>());
		readVehicles(basePath + "objects//Vehicles//land//us//");
		writeVehiclesToCSV(basePath, "land_us");	
		writeVehiclesToCSV(basePath, "land_all");
		
		setVehicles(new ArrayList<>());
		readVehicles(basePath + "objects//Vehicles//land//gb//");
		writeVehiclesToCSV(basePath, "land_gb");		
		writeVehiclesToCSV(basePath, "land_all");
		
		setVehicles(new ArrayList<>());
		readVehicles(basePath + "objects//Vehicles//land//se//");
		writeVehiclesToCSV(basePath, "land_se");		
		writeVehiclesToCSV(basePath, "land_all");
		
		setVehicles(new ArrayList<>());
		readVehicles(basePath + "objects//Vehicles//land//ru//");
		writeVehiclesToCSV(basePath, "land_ru");
		writeVehiclesToCSV(basePath, "land_all");
		
		setVehicles(new ArrayList<>());
		readVehicles(basePath + "objects//Vehicles//land//it//");
		writeVehiclesToCSV(basePath, "land_it");
		writeVehiclesToCSV(basePath, "land_all");
		
		setVehicles(new ArrayList<>());
		readVehicles(basePath + "objects//Vehicles//land//au//");
		writeVehiclesToCSV(basePath, "land_au");
		writeVehiclesToCSV(basePath, "land_all");
		
		readVehicles(basePath + "objects//Vehicles//air//de//");
		writeVehiclesToCSV(basePath, "air_de");
		writeVehiclesToCSV(basePath, "air_all");
		
		setVehicles(new ArrayList<>());
		readVehicles(basePath + "objects//Vehicles//air//jp//");
		writeVehiclesToCSV(basePath, "air_jp");
		writeVehiclesToCSV(basePath, "air_all");
		
		setVehicles(new ArrayList<>());
		readVehicles(basePath + "objects//Vehicles//air//us//");
		writeVehiclesToCSV(basePath, "air_us");	
		writeVehiclesToCSV(basePath, "air_all");
		
		setVehicles(new ArrayList<>());
		readVehicles(basePath + "objects//Vehicles//air//gb//");
		writeVehiclesToCSV(basePath, "air_gb");	
		writeVehiclesToCSV(basePath, "air_all");	
		
		setVehicles(new ArrayList<>());
		readVehicles(basePath + "objects//Vehicles//air//se//");
		writeVehiclesToCSV(basePath, "air_se");	
		writeVehiclesToCSV(basePath, "air_all");
		
		setVehicles(new ArrayList<>());
		readVehicles(basePath + "objects//Vehicles//air//ru//");
		writeVehiclesToCSV(basePath, "air_ru");	
		writeVehiclesToCSV(basePath, "air_all");
		
		setVehicles(new ArrayList<>());
		readVehicles(basePath + "objects//Vehicles//air//it//");
		writeVehiclesToCSV(basePath, "air_it");	
		writeVehiclesToCSV(basePath, "air_all");
	
		setVehicles(new ArrayList<>());
		readVehicles(basePath + "objects//Vehicles//land//civ//");
		writeVehiclesToCSV(basePath, "land_civ");	
		
		writeMaterialsToCSV(basePath);
		writeProjectilesToCSV(basePath);
				
		return null;
	}
private void writeMaterialsToCSV(String basePath){
		
        try {
			String csvFile = basePath + "materials.csv";
			FileWriter writer = new FileWriter(csvFile);
			Iterator<Material> it = getMaterials().iterator();
			Material material;
			CSVUtils.writeLine(writer, Arrays.asList(
					"Name", 
					"Number", 
					"Penetration in mm",
					"Strength in mm")			
					);
			
			while(it.hasNext()){
				material = it.next();
				CSVUtils.writeLine(writer,material.getMaterialOutput());
			}

	        writer.flush();
	        writer.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	

private void writeProjectilesToCSV(String basePath){
	
    try {
		String csvFile = basePath + "projectiles.csv";
		FileWriter writer = new FileWriter(csvFile);
		Iterator<Projectile> it = getProjectiles().iterator();
		Projectile projectile;
		CSVUtils.writeLine(writer, Arrays.asList(
				"Name", 
				"Damage", 
				"Min Damage",
				"Distance to Min Damage",
				"Distance to start loosing damage",
				"Material")			
				);
		
		while(it.hasNext()){
			projectile = it.next();
			CSVUtils.writeLine(writer,projectile.getProjectilelOutput(),';');
		}

        writer.flush();
        writer.close();
		
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}	
	private void writeKitsToCSV(String basePath, String filename){
		
        try {
			String csvFile = basePath + filename;
			FileWriter writer = new FileWriter(csvFile);
			Iterator<Kit> it = getKits().iterator();
			Kit kit;
			CSVUtils.writeLine(writer, Arrays.asList(
					"Team", 
					"Name", 
					"Primary Weapon", 
					"Primary Template",
					"Secondary Weapon", 
					"Secondary Template",	
					"CQ Weapon", 
					"CQ Template",
					"AP", 
					"AP Template",		
					"Grenade", 
					"Grenade Template",	
					"Binocs",		
					"Wrench", 
					"Smoke"		)			
					);
			
			while(it.hasNext()){
				kit = it.next();
				CSVUtils.writeLine(writer,kit.getKitOutput());
			}

	        writer.flush();
	        writer.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
private void writeWeaponsToCSV(String basePath){
		
        try {
			String csvFile = basePath + "weapons.csv";
			FileWriter writer = new FileWriter(csvFile);
			Iterator<Weapon> it = getWeapons().iterator();
			Weapon weapon;
			CSVUtils.writeLine(writer, Arrays.asList(
					"Name", 
					"Template", 
					"Type", 
					"HP", 
					"1st Gun",
					"1st Projectile",
					"1st Projectile Velocity",		
					"1st Rounds per Mag",					
					"1st Magazines"
					),';'			
					);
			
			while(it.hasNext()){
				weapon = it.next();
				CSVUtils.writeLine(writer,weapon.getWeaponOutput(),';');
			}

	        writer.flush();
	        writer.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	

private void writeVehiclesToCSV(String basePath, String team){
	
    try {
		String csvFile = basePath + "vehicles_" + team + ".csv";
		FileWriter writer = new FileWriter(csvFile, true);
		
		Iterator<Vehicle> it = getVehicles().iterator();
		Vehicle vehicle;
		
		CSVUtils.writeLine(writer, Arrays.asList(
				"Name", 
				"Template", 
				"Team",
				"Type", 
				"HP",
				"1st Gun",
				"Projectile",
				"Projectile Velocity",					
				"Penetration",
				"Magazines", 
				"Rounds per Mag",
				"2nd Gun",
				"Projectile",
				"Projectile Velocity",	
				"Penetration",
				"Magazines", 
				"Rounds per Mag",
				"3rd Gun",
				"Projectile",
				"Projectile Velocity",		
				"Penetration",
				"Magazines", 
				"Rounds per Mag"	
				),';'			
				);
		
		while(it.hasNext()){
			vehicle = it.next();
			CSVUtils.writeLine(writer,vehicle.getVehicleOutput(),';');
		}

        writer.flush();
        writer.close();
		
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}	


	public ArrayList<Projectile> getProjectiles() {
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
				if (count > 1 && gun.getName()!=null)
					ret.add(gun);
				if (count > 1 && gun.getName()==null)
					count--;
				gun = new Gun();				
			}
			if(line.startsWith("objecttemplate.weaponhud.hudname")) 	 {					
				gun.setName(line.substring(line.indexOf(" "), line.length()));
				gun.setName(gun.getName().substring(0, gun.getName().length()).replace("\"", ""));
			}
					
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
		if(gun.getName()!=null)
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
		Iterator<Projectile> it = getProjectiles().iterator();
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
	
	private String getTeamFromKitPrefix(String prefix) {
		switch (prefix) {
		case "ga":
		case "gs":
		case "gw":
		case "gm":
		case "gc":
			return "de";
			
		case "ia":
			return "it";
			
		case "aa":
			return "au";
			
		case "ba":
		case "bw":
		case "be":
		case "bj":
			return "gb";
			
		case "cw":
			return "ca";
			
		case "jp":
			return "jp";
				
		case "ua":
		case "uc":
		case "up":
		case "uw":
		case "us":			
			return "us";
		case "re":
			return "ru";
		case "se":
			return "fi";			
			
		default:
			return "??";
		}
	}
	
	private void readKits(String basePath) {
		readKits(basePath, "");
	}
	private void readKits(String basePath, String prefix_filter) {
		Path p = Paths.get(basePath);

		FileVisitor<Path> fv = new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				String prefix = file.getFileName().toFile().getName().substring(0, 1);
				int dot = file.toString().lastIndexOf(".");
				suffix = file.toString().substring(dot + 1);
				

				String line;
				String kitType;
				String weapon;
				WeaponType weaponType;
				if (prefix_filter == "" || prefix.toLowerCase().equals(prefix_filter.toLowerCase())) {
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
							kit.setTeam(getTeamFromKitPrefix(
									kit.getIngameName().substring(0, kit.getIngameName().indexOf("_")).toLowerCase()));
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
							case WEAPON_TYPE_LMG:
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
					if (kit.getIngameName() != null && !kit.getIngameName().toLowerCase().startsWith("fa"))
						getKits().add(kit);
				default:
					break;
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
				Projectile projectile = new Projectile();
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
							projectile = new Projectile();
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
					if (weapon.getTemplateName() != null)
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

	public void setProjectiles(ArrayList<Projectile> projectiles) {
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
