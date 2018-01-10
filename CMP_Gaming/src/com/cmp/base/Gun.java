package com.cmp.base;

public class Gun {
private String name;
private int rounds; 
private int mags;
private int velocity;
public int getVelocity() {
	return velocity;
}
public void setVelocity(int velocity) {
	this.velocity = velocity;
}
private ProjectileTemplate projectile;
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
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
public ProjectileTemplate getProjectile() {
	return projectile;
}
public void setProjectile(ProjectileTemplate projectil) {
	this.projectile = projectil;
}
@Override
public String toString() {
	return "Gun [name=" + name + ", rounds=" + rounds + ", mags=" + mags + ", projectil=" + projectile +",  velocity=" + velocity  + "]";
}
}
