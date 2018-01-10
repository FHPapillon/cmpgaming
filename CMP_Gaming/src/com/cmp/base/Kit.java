package com.cmp.base;

import com.cmp.fragalyzer.types.KitType;

public class Kit {
private String name;
private String team;
private String ingameName;
private Weapon closeQuartersWeapon;
private boolean hasSmoke;
private Weapon primaryWeapon;
private Weapon secondaryWeapon;
private Weapon grenade;
private boolean hasWrench;
private KitType kitType;
private boolean hasBinocs;
private Weapon apMine;

public Weapon getApMine() {
	return apMine;
}
public void setApMine(Weapon apMine) {
	this.apMine = apMine;
}
public boolean isHasBinocs() {
	return hasBinocs;
}
public void setHasBinocs(boolean hasBinocs) {
	this.hasBinocs = hasBinocs;
}
public Kit() {
	super();
	setHasSmoke(false);
	setHasWrench(false);
	setHasBinocs(false);
}
public Weapon getCloseQuartersWeapon() {
	return closeQuartersWeapon;
}
public Weapon getGrenade() {
	return grenade;
}
public String getIngameName() {
	return ingameName;
}
public KitType getKitType() {
	return kitType;
}
public String getName() {
	return name;
}
public Weapon getPrimaryWeapon() {
	return primaryWeapon;
}
public Weapon getSecondaryWeapon() {
	return secondaryWeapon;
}
public String getTeam() {
	return team;
}
public boolean isHasSmoke() {
	return hasSmoke;
}
public boolean isHasWrench() {
	return hasWrench;
}
public void setCloseQuartersWeapon(Weapon closeQuartersWeapon) {
	this.closeQuartersWeapon = closeQuartersWeapon;
}
public void setGrenade(Weapon grenade) {
	this.grenade = grenade;
}
public void setHasSmoke(boolean hasSmoke) {
	this.hasSmoke = hasSmoke;
}
public void setHasWrench(boolean hasWrench) {
	this.hasWrench = hasWrench;
}
public void setIngameName(String ingameName) {
	this.ingameName = ingameName;
}
public void setKitType(KitType kitType) {
	this.kitType = kitType;
}
public void setName(String name) {
	this.name = name;
}
public void setPrimaryWeapon(Weapon primaryWeapon) {
	this.primaryWeapon = primaryWeapon;
}
public void setSecondaryWeapon(Weapon secondaryWeapon) {
	this.secondaryWeapon = secondaryWeapon;
}
public void setTeam(String team) {
	this.team = team;
}
@Override
public String toString() {
	return "Kit [name=" + name + ", team=" + team + ", ingameName=" + ingameName + ", closeQuartersWeapon="
			+ closeQuartersWeapon + ", hasSmoke=" + hasSmoke + ", primaryWeapon=" + primaryWeapon + ", secondaryWeapon="
			+ secondaryWeapon + ", grenade=" + grenade + ", hasWrench=" + hasWrench + ", kitType=" + kitType
			+ ", hasBinocs=" + hasBinocs + ", apMine=" + apMine + "]";
}
}
