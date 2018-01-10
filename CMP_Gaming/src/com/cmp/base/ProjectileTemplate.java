package com.cmp.base;

public class ProjectileTemplate {
private String name;
private double damage;
private double minDamage;
private double distToStartLoseDamage;
private double distToMinDamage;
private Material material;
public Material getMaterial() {
	return material;
}
public void setMaterial(Material material) {
	this.material = material;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public double getDamage() {
	return damage;
}
public void setDamage(double damage) {
	this.damage = damage;
}
public double getMinDamage() {
	return minDamage;
}
public void setMinDamage(double minDamage) {
	this.minDamage = minDamage;
}
public double getDistToStartLoseDamage() {
	return distToStartLoseDamage;
}
public void setDistToStartLoseDamage(double distToStartLoseDamage) {
	this.distToStartLoseDamage = distToStartLoseDamage;
}
public double getDistToMinDamage() {
	return distToMinDamage;
}
public void setDistToMinDamage(double distToMinDamage) {
	this.distToMinDamage = distToMinDamage;
}
@Override
public String toString() {
	return "ProjectileTemplate [name=" + name + ", damage=" + damage + ", minDamage=" + minDamage
			+ ", distToStartLoseDamage=" + distToStartLoseDamage + ", distToMinDamage=" + distToMinDamage
			+ ", material=" + material + "]";
}

}
