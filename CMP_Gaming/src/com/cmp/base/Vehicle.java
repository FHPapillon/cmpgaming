package com.cmp.base;

public class Vehicle {
private String name;
private String templateName;
private Gun primaryGun;
private Gun secondaryGun;
private Gun tertiaryGun;
private int HP;
private Material material;
public String getName() {
	return name;
}
@Override
public String toString() {
	return "Vehicle [name=" + name + ", templateName=" + templateName + ", primaryGun=" + primaryGun + ", secondaryGun="
			+ secondaryGun + ", tertiaryGun=" + tertiaryGun + ", HP=" + HP + ", material=" + material + "]";
}
public void setName(String name) {
	this.name = name;
}
public String getTemplateName() {
	return templateName;
}
public void setTemplateName(String templateName) {
	this.templateName = templateName;
}
public Gun getPrimaryGun() {
	return primaryGun;
}
public void setPrimaryGun(Gun primaryGun) {
	this.primaryGun = primaryGun;
}
public Gun getSecondaryGun() {
	return secondaryGun;
}
public void setSecondaryGun(Gun secondaryGun) {
	this.secondaryGun = secondaryGun;
}
public Gun getTertiaryGun() {
	return tertiaryGun;
}
public void setTertiaryGun(Gun tertiaryGun) {
	this.tertiaryGun = tertiaryGun;
}
public int getHP() {
	return HP;
}
public void setHP(int hP) {
	HP = hP;
}
public Material getMaterial() {
	return material;
}
public void setMaterial(Material material) {
	this.material = material;
}
}
