package com.cmp.fragalyzer.types;

import java.util.ArrayList;
import java.util.HashMap;

import com.cmp.fragalyzer.LogEntry;

public class PlayerStats {
private String playerName;
private ArrayList<LogEntry> events;
private ArrayList<LogEntry> flagCaptures;
private HashMap<WeaponType, Integer> weaponTypeKills;
private HashMap<VehicleType, Integer> vehicleTypeKills;
private HashMap<String, Integer> enemies;
private HashMap<String, Integer> victims;
private HashMap<String, Integer> weaponNameKills;
private HashMap<String, Integer> vehicleNameKills;
private StatScope scope;
private int kills;
private int deaths;
private int flagCaps;
private int teamKills;
private int teamKilled;
public int getTeamKilled() {
	return teamKilled;
}

public void setTeamKilled(int teamKilled) {
	this.teamKilled = teamKilled;
}

private int flagDefends;
private int flagCapAssists;
private int flagNeutralizes;
private int flagNeutralizeAssist;
private double kdrRatio;
public PlayerStats() {
		super();
		
		events = new ArrayList<>();
		flagCaptures = new ArrayList<>();
		weaponTypeKills = new HashMap<>();
		weaponNameKills = new HashMap<>();
		vehicleTypeKills = new HashMap<>();
		vehicleNameKills = new HashMap<>();
		enemies = new HashMap<>();
		victims = new HashMap<>();
		setKdrRatio(0.0);
		setScope(scope);
		// TODO Auto-generated constructor stub
	}

public int getDeaths() {
	return deaths;
}
public HashMap<String, Integer> getEnemies() {
	return enemies;
}
public ArrayList<LogEntry> getEvents() {
	return events;
}
public int getFlagCapAssists() {
	return flagCapAssists;
}
public int getFlagCaps() {
	return flagCaps;
}
public ArrayList<LogEntry> getFlagCaptures() {
	return flagCaptures;
}
public int getFlagDefends() {
	return flagDefends;
}
public int getFlagNeutralizes() {
	return flagNeutralizes;
}
public int getKills() {
	return kills;
}
public String getPlayerName() {
	return playerName;
}
public StatScope getScope() {
	return scope;
}
public int getTeamKills() {
	return teamKills;
}
public HashMap<String, Integer> getVehicleNameKills() {
	return vehicleNameKills;
}
public HashMap<VehicleType, Integer> getVehicleTypeKills() {
	return vehicleTypeKills;
}
public HashMap<String, Integer> getVictims() {
	return victims;
}
public HashMap<String, Integer> getWeaponNameKills() {
	return weaponNameKills;
}
public HashMap<WeaponType, Integer> getWeaponTypeKills() {
	return weaponTypeKills;
}
public void setDeaths(int deaths) {
	this.deaths = deaths;
}
public void setEnemies(HashMap<String, Integer> enemies) {
	this.enemies = enemies;
}
public void setEvents(ArrayList<LogEntry> events) {
	this.events = events;
}
public void setFlagCapAssists(int flagCapAssists) {
	this.flagCapAssists = flagCapAssists;
}
public void setFlagCaps(int flagCaps) {
	this.flagCaps = flagCaps;
}

public void setFlagCaptures(ArrayList<LogEntry> flagCaptures) {
	this.flagCaptures = flagCaptures;
}
public void setFlagDefends(int flagDefends) {
	this.flagDefends = flagDefends;
}
public void setFlagNeutralizes(int flagNeutralizes) {
	this.flagNeutralizes = flagNeutralizes;
}
public void setKills(int kills) {
	this.kills = kills;
}
public void setPlayerName(String playerName) {
	this.playerName = playerName;
}
public void setScope(StatScope scope) {
	this.scope = scope;
}
public void setTeamKills(int teamKills) {
	this.teamKills = teamKills;
}
public void setVehicleNameKills(HashMap<String, Integer> vehicleNameKills) {
	this.vehicleNameKills = vehicleNameKills;
}
public void setVehicleTypeKills(HashMap<VehicleType, Integer> vehicleTypeKills) {
	this.vehicleTypeKills = vehicleTypeKills;
}
public void setVictims(HashMap<String, Integer> victims) {
	this.victims = victims;
}
public void setWeaponNameKills(HashMap<String, Integer> weaponNameKills) {
	this.weaponNameKills = weaponNameKills;
}
public void setWeaponTypeKills(HashMap<WeaponType, Integer> weaponTypeKills) {
	this.weaponTypeKills = weaponTypeKills;
}

public double getKdrRatio() {
	return kdrRatio;
}

public void setKdrRatio(double kdrRatio) {
	this.kdrRatio = kdrRatio;
}

public int getFlagNeutralizeAssist() {
	return flagNeutralizeAssist;
}

public void setFlagNeutralizeAssist(int flagNeutralizeAssist) {
	this.flagNeutralizeAssist = flagNeutralizeAssist;
}

}
