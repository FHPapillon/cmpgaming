package com.cmp.base;

import com.cmp.fragalyzer.types.KitType;
import com.cmp.fragalyzer.types.VehicleType;
import com.cmp.fragalyzer.types.WeaponType;

public class ObjectSpawner implements Comparable<ObjectSpawner> {
	private String name;

	private String template1;

	private String template2;

	private String minSpawnDelay;
	private String maxSpawnDelay;
	private String timeToLive;
	private String cpID;
	private String teamlocked;
	private String maxNrSpawned;

	private VehicleType vehicleType;
	private WeaponType weaponType;
	private KitType kitType;
	private String HUDName1;
	private String HUDName2;
	private Fh2SpawnerType type;

	public ObjectSpawner(){
		setType(Fh2SpawnerType.TRANSPORT_AT_AA_ARTILLERY);
		setHUDName1("");
		setHUDName2("");
	}
	
	public Fh2SpawnerType getType() {
		return type;
	}

	public void setType(Fh2SpawnerType type) {
		this.type = type;
	}

	private String fh2ImageName1;
	private String fh2ImageName2;
	@Override
	public int compareTo(ObjectSpawner arg0) {
		if (Integer.parseInt(getCpID()) < Integer.parseInt(arg0.getCpID()))
			return -1;
		if (Integer.parseInt(getCpID()) > Integer.parseInt(arg0.getCpID()))
			return 1;

		return 0;
	}

	public String getCpID() {
		return cpID;
	}
	

	public String getFh2ImageName1() {
		return fh2ImageName1;
	}

	public String getFh2ImageName2() {
		return fh2ImageName2;
	}

	public String getHUDName1() {
		return HUDName1;
	}

	public String getHUDName2() {
		return HUDName2;
	}
	public KitType getKitType() {
		return kitType;
	}
	public String getMaxNrSpawned() {
		return maxNrSpawned;
	}

	public String getMaxSpawnDelay() {
		return maxSpawnDelay;
	}

	public String getMinSpawnDelay() {
		return minSpawnDelay;
	}



	public String getName() {
		return name;
	}

	private String getSpaces(final String check, int expected) {
		
		int diff = 0;
		String ret = new String("");
		if (check == null)
			return ret;
		diff = expected - check.length();
		if (diff == 0)
			return ret;
		else {				
			if (diff < 0)
				return ret;
			else {
				while (diff > 0){
					ret = ret + " ";
					diff--;
				}
				return ret;
			}
		}

	}

	public String getTeamlocked() {
		return teamlocked;
	}

	public String getTemplate1() {
		return template1;
	}
	public String getTemplate2() {
		return template2;
	}
	public String getTimeToLive() {
		return timeToLive;
	}
	public VehicleType getVehicleType() {
		return vehicleType;
	}

	public WeaponType getWeaponType() {
		return weaponType;
	}



	public void setCpID(String cpID) {
		this.cpID = cpID;
	}

	public void setFh2ImageName1(String fh2ImageName1) {
		this.fh2ImageName1 = fh2ImageName1;
	}

	public void setFh2ImageName2(String fh2ImageName2) {
		this.fh2ImageName2 = fh2ImageName2;
	}

	public void setHUDName1(String hUDName) {
		HUDName1 = hUDName;
	}

	public void setHUDName2(String hUDName) {
		HUDName2 = hUDName;
	}




	public void setKitType(KitType kitType) {
		this.kitType = kitType;
	}

	public void setMaxNrSpawned(String maxNrSpawned) {
		this.maxNrSpawned = maxNrSpawned;
	}

	public void setMaxSpawnDelay(String maxSpawnDelay) {
		this.maxSpawnDelay = maxSpawnDelay;
	}

	public void setMinSpawnDelay(String minSpawnDelay) {
		this.minSpawnDelay = minSpawnDelay;
	}

	public void setName(String name) {
		this.name = name;
	}


	public void setTeamlocked(String teamlocked) {
		if (teamlocked == null)
			this.teamlocked = "";
		else
			this.teamlocked = teamlocked;
	}

	public void setTemplate1(String template1) {
		this.template1 = template1;
	}

	public void setTemplate2(String template2) {
		this.template2 = template2;
	}

	public void setTimeToLive(String timeToLive) {
		this.timeToLive = timeToLive;
	}


	public void setVehicleType(VehicleType vehicleType) {
		this.vehicleType = vehicleType;
	}

	public void setWeaponType(WeaponType weaponType) {
		this.weaponType = weaponType;
	}

	public String toString(String name1, String name2) {
		String ret = new String();
		String ret1 = new String();
		String ret2 = new String();
		String name1_ret = new String();
		String name2_ret = new String();		
		boolean spawnDelayError = false;
		boolean ttlError = false;		
		boolean teamlockError = false;	
		int i = 0;
			
		
		/*Rule:
		 * Min delay must be > max delay
		 */
		if (getMaxSpawnDelay() != null && getMinSpawnDelay() != null){
			if (Integer.parseInt(getMinSpawnDelay()) > Integer.parseInt(getMaxSpawnDelay())){
				spawnDelayError = true;
			}
		} else
			spawnDelayError = true;
				
		/*Rule:
		 * ttl must not be null
		 */
		if (getTimeToLive()== null)
				ttlError = true;		
		
		/*Rule:
		 * teamlock must be null if the word spawn is not contained in the template name
		 * teamlock must be set if the word spawn is contained in the template name
		 */
		if (getTeamlocked()== null){
			if (getTemplate1()!= null && getTemplate1().contains("spawn")){
				teamlockError = true;
				System.out.println("x");
			}
			if (getTemplate2()!= null && getTemplate2().contains("spawn")){
				teamlockError = true;
				System.out.println("y");
			}			
		} else {
			if (getTemplate1()!= null && !getTemplate1().contains("spawn")){
				teamlockError = true;
			}
			if (getTemplate2()!= null && !getTemplate2().contains("spawn")){
				teamlockError = true;
			}			
		}
					
		
		
		if (name1 == null)
			name1_ret = "";
		else
			name1_ret = " (" + name1 + ")";
		
		if (name2 == null)
			name2_ret = "";
		else
			name2_ret = " (" + name2 + ")";		
		
		if (getTemplate1() != null) {
			ret1 = ret1 + " Axis:  " + getTemplate1() + name1_ret;
			i = ret1.length();

			while (i < 60) {
				ret1 = ret1 + " ";
				i++;
			}
			ret1 = ret1 + " min: " + getMinSpawnDelay()
					+ getSpaces(getMinSpawnDelay(), 3) + " max: "
					+ getMaxSpawnDelay() +  getSpaces(getMaxSpawnDelay(), 3) +" ttl: " + getTimeToLive()
					+ " teamlock:" + getTeamlocked();
			ret = ret1;
		}
		if (getTemplate2() != null) {
			ret2 = ret2 + " Allied:  " + getTemplate2() + name1_ret;

			i = ret2.length();

			while (i < 60) {
				ret2 = ret2 + " ";
				i++;
			}
			ret2 = ret2 + " min: " + getMinSpawnDelay()
					+ getSpaces(getMinSpawnDelay(), 3) + " max: " + getMaxSpawnDelay() +  getSpaces(getMaxSpawnDelay(), 3)+ " ttl: "
					+ getTimeToLive() + " teamlock:" + getTeamlocked();
			if (getTemplate1() != null)
				ret = ret1 + "\n" + ret2;
			else
				ret = ret2;
		}
		if (spawnDelayError || ttlError || teamlockError)
		
		 return "Check! " + ret + " " + spawnDelayError + " " + ttlError + " " + teamlockError + " " +getName();
		else
			return ret + " " +getName();
					
	}

}
