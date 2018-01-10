package com.cmp.base;

public class Material {
private String name;
private int strength_in_mm;
private int penetration_in_mm;
public int getPenetration_in_mm() {
	return penetration_in_mm;
}
public void setPenetration_in_mm(int penetration_in_mm) {
	this.penetration_in_mm = penetration_in_mm;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public int getStrength_in_mm() {
	return strength_in_mm;
}
public void setStrength_in_mm(int strength_in_mm) {
	this.strength_in_mm = strength_in_mm;
}
public String getNumber() {
	return number;
}
public void setNumber(String number) {
	this.number = number;
}
private String number;
@Override
public String toString() {
	return "Material [name=" + name + ", strength_in_mm=" + strength_in_mm + ", penetration_in_mm=" + penetration_in_mm
			+ ", number=" + number + "]";
}



}