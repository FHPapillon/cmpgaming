package com.cmp.base;

import com.cmp.fragalyzer.types.WeaponType;

public class Weapon {
private WeaponType type;
private String templateName;
private String name;
private String projectile;
private int velocity;
private int rounds; 
private int mags;
public WeaponType getType() {
	return type;
}
public void setType(WeaponType type) {
	this.type = type;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getProjectile() {
	return projectile;
}
public void setProjectile(String projectile) {
	this.projectile = projectile;
}
public int getVelocity() {
	return velocity;
}
public void setVelocity(int velocity) {
	this.velocity = velocity;
}
public int getRounds() {
	return rounds;
}
public void setRounds(int rounds) {
	this.rounds = rounds;
}
public int getMags() {
	return mags;
}
public void setMags(int mags) {
	this.mags = mags;
}
public String getTemplateName() {
	return templateName;
}
public void setTemplateName(String templateName) {
	this.templateName = templateName;
}
@Override
public String toString() {
	return "Weapon [type=" + type + ", templateName=" + templateName + ", name=" + name + ", projectile=" + projectile
			+ ", velocity=" + velocity + ", rounds=" + rounds + ", mags=" + mags + "]";
}

}
