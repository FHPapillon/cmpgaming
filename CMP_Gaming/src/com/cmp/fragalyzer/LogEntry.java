package com.cmp.fragalyzer;

import java.time.LocalDateTime;
import java.util.Date;

import com.cmp.fragalyzer.types.CPEvents;
import com.cmp.fragalyzer.types.EventType;
import com.cmp.fragalyzer.types.KillType;
import com.cmp.fragalyzer.types.KitType;
import com.cmp.fragalyzer.types.VehicleType;
import com.cmp.fragalyzer.types.WeaponType;

public  class LogEntry {
	private EventType eventType;
    private Position playerPosition;
    private String playerKit;
	private String player;
	private String playerTeam;
	private String vehicle;
	private String weapon;
	private Date timestamp;
	private String weaponName;
	private KillType killType;
	private String victim;
	private String victimVehicle;
	private Position victimPosition;
	private String victimKit;
	private String victimTeam;
	private boolean teamkill;
	private WeaponType attackerWeaponType;
	private WeaponType victimWeaponType;
	private KitType victimKitType;
	private VehicleType attackerVehicleType;
	private VehicleType victimVehicleType;
	private String attackerVehicleName;
	private String victimVehicleName;
	private String mapname;
	private int roundNumber;
	private LocalDateTime datetime;
	private KitType attackerKitType;
	private CPEvents cpEvent;
	private String time;
	
	

	public String getMapname() {
		return mapname;
	}
	public void setMapname(String mapname) {
		this.mapname = mapname;
	}
	public int getRoundNumber() {
		return roundNumber;
	}
	public void setRoundNumber(int roundNumber) {
		this.roundNumber = roundNumber;
	}
	public LocalDateTime getDatetime() {
		return datetime;
	}
	public void setDatetime(LocalDateTime datetime) {
		this.datetime = datetime;	
	}
	
	public String getAttackerVehicleName() {
		return attackerVehicleName;
	}
	public void setAttackerVehicleName(String attackerVehicleName) {
		this.attackerVehicleName = attackerVehicleName;
	}
	public String getVictimVehicleName() {
		return victimVehicleName;
	}
	public void setVictimVehicleName(String victimVehicleName) {
		this.victimVehicleName = victimVehicleName;
	}


	public WeaponType getAttackerWeaponType() {
		return attackerWeaponType;
	}
	public void setAttackerWeaponType(WeaponType attackerWeaponType) {
		this.attackerWeaponType = attackerWeaponType;
	}
	public WeaponType getVictimWeaponType() {
		return victimWeaponType;
	}
	public void setVictimWeaponType(WeaponType victimWeaponType) {
		this.victimWeaponType = victimWeaponType;
	}
	public KitType getAttackerKitType() {
		return attackerKitType;
	}
	public void setAttackerKitType(KitType attackerKitType) {
		this.attackerKitType = attackerKitType;
	}
	public KitType getVictimKitType() {
		return victimKitType;
	}
	public void setVictimKitType(KitType victimKitType) {
		this.victimKitType = victimKitType;
	}
	public VehicleType getAttackerVehicleType() {
		return attackerVehicleType;
	}
	public void setAttackerVehicleType(VehicleType attackerVehicleType) {
		this.attackerVehicleType = attackerVehicleType;
	}
	public VehicleType getVictimVehicleType() {
		return victimVehicleType;
	}
	public void setVictimVehicleType(VehicleType victimVehicleType) {
		this.victimVehicleType = victimVehicleType;
	}

	public String getVictim() {
		return victim;
	}
	public void setVictim(String victim) {
		this.victim = victim;
	}
	public String getVictimVehicle() {
		return victimVehicle;
	}
	public void setVictimVehicle(String victimVehicle) {
		this.victimVehicle = victimVehicle;
	}
	public Position getVictimPosition() {
		return victimPosition;
	}
	public void setVictimPosition(Position victimPosition) {
		this.victimPosition = victimPosition;
	}
	public String getVictimKit() {
		return victimKit;
	}
	public void setVictimKit(String victimKit) {
		this.victimKit = victimKit;
	}
	public String getVictimTeam() {
		return victimTeam;
	}
	public void setVictimTeam(String victimTeam) {
		this.victimTeam = victimTeam;
	}
	public KillType getKillType() {
		return killType;
	}
	public void setKillType(KillType killType) {
		this.killType = killType;
	}
	
	public String toString() {
		String ret = new String();
		switch (getEventType()) {
		
		case INIT:
			ret = getMapname() + " Round " + getRoundNumber() + " on " + getDatetime();
			break;
			
		case SCORE:
			ret = getPlayer() + " " + getCpEvent();
		
		case KILL:
			
			String killtext = new String();
			
			if (isTeamkill())
				killtext =  " teamkilled " ;
			else
				killtext = " killed ";				
			
			killtext = getPlayer() + killtext + getVictim();
			
			switch (getKillType()) {
			
			case SUICIDE:
				ret =  getPlayer() + " suicided " + getTime();
				break;
			case INF_INF:
				ret = getKillType() + " " +killtext + " with " + getWeaponName(getWeapon()) + "/" + getAttackerWeaponType()+ " (" + " " + FragalyzerConstants.kitNames.get(getAttackerKitType()) + " vs " +  FragalyzerConstants.kitNames.get(getVictimKitType()) +  ")"+ getTime();
				break;
			case INF_VEHICLE:
				ret = getKillType() + " " +killtext + " with " + getWeaponName(getWeapon()) + "/" + getAttackerWeaponType() + " ("  + " " + FragalyzerConstants.kitNames.get(getAttackerKitType()) + " vs " +  getVehicleName(getVictimVehicle()) + "/" + getVictimVehicleType() + ")"+ getTime();
				
				break;		
			case VEHICLE_INF:
				ret = getKillType() + " " +killtext + " with " + getVehicleName(getVehicle())+ " ("  + " " + FragalyzerConstants.names.get(getVehicle()) + "/" + getAttackerVehicleType() + " vs " +  FragalyzerConstants.kitNames.get(getVictimKitType()) +  ")"+ getTime();
				
				break;	
			case VEHICLE_VEHICLE:
				ret = getKillType() + " " +killtext + " with " + getVehicleName(getVehicle())+ " (" + " " + FragalyzerConstants.names.get(getVehicle()) + "/" + getAttackerVehicleType()  + " vs " +  FragalyzerConstants.names.get(getVictimVehicle()) + "/" + getVictimVehicleType()  +  ")"+ getTime();
				
				break;								
			default:
				break;
			}
			break;

		default:
			break;
		}
		
		
		
		return ret;
	}
	
	private String getWeaponName(String name){
		if (FragalyzerConstants.names.containsKey(name))
			return FragalyzerConstants.names.get(name);
		else
			return name;
	}
			
	
	
	private String getVehicleName(String name){
		if (FragalyzerConstants.names.containsKey(name))
			return FragalyzerConstants.names.get(name);
		else
			return name;
	}
	
	public boolean isTeamkill() {
		return teamkill;
	}
	public void setTeamkill(boolean teamkill) {
		this.teamkill = teamkill;
	}	
	
	public EventType getEventType() {
		return eventType;
	}
	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}
	public Position getPlayerPosition() {
		return playerPosition;
	}
	public void setPlayerPosition(Position playerPosition) {
		this.playerPosition = playerPosition;
	}
	public String getPlayerKit() {
		return playerKit;
	}
	public void setPlayerKit(String playerKit) {
		this.playerKit = playerKit;
	}
	public String getPlayer() {
		return player;
	}
	public void setPlayer(String player) {
		this.player = player;
	}
	public String getVehicle() {
		return vehicle;
	}
	public void setVehicle(String vehicle) {
		this.vehicle = vehicle;
	}
	public String getWeapon() {
		return weapon;
	}
	public void setWeapon(String weapon) {
		this.weapon = weapon;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public String getPlayerTeam() {
		return playerTeam;
	}
	public void setPlayerTeam(String playerTeam) {
		this.playerTeam = playerTeam;
	}
	public String getWeaponName() {
		return weaponName;
	}
	public void setWeaponName(String weaponName) {
		this.weaponName = weaponName;
	}
	public CPEvents getCpEvent() {
		return cpEvent;
	}
	public void setCpEvent(CPEvents cpEvent) {
		this.cpEvent = cpEvent;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
}
