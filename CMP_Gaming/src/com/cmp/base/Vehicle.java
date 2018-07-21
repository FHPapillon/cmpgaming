package com.cmp.base;

import java.util.ArrayList;
import java.util.List;

import com.cmp.fragalyzer.types.VehicleType;

public class Vehicle {
private String team;
public String getTeam() {
	return team;
}
public void setTeam(String team) {
	this.team = team;
}
private String name;
private String templateName;
private VehicleType vehicleType;
public VehicleType getVehicleType() {
	return vehicleType;
}
public void setVehicleType(VehicleType vehicleType) {
	this.vehicleType = vehicleType;
}
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
	return "Vehicle [team=" + team + ", name=" + name + ", templateName=" + templateName + ", vehicleType="
			+ vehicleType + ", primaryGun=" + primaryGun + ", secondaryGun=" + secondaryGun + ", tertiaryGun="
			+ tertiaryGun + ", HP=" + HP + ", material=" + material + "]";
}



public List<String> getVehicleOutput() {
	List<String> ret = new ArrayList<>();
	if(getName() != null)
		ret.add(getName());
	else
		ret.add(getTemplateName());
	
	ret.add(getTemplateName());
	
	if(getVehicleType() != null)
		ret.add(getVehicleType().name());
	else
		ret.add("Unknown");
	
	ret.add(getTeam());
	
	ret.add(Integer.toString(getHP()));
	
	if (getPrimaryGun() != null) {
		
		ret.add(getPrimaryGun().getName());
		if (getPrimaryGun().getProjectile().getName() != null) {
			ret.add(getPrimaryGun().getProjectile().getName());
			ret.add(Integer.toString(getPrimaryGun().getVelocity()));
			ret.add(Integer.toString(getPrimaryGun().getProjectile().getMaterial().getPenetration_in_mm()));
			ret.add(Integer.toString(getPrimaryGun().getRounds()));
			ret.add(Integer.toString(getPrimaryGun().getMags()));
		}
		else {
		    ret.add(" ");
			ret.add(" ");
			ret.add(" ");
		    ret.add(" ");
		    ret.add(" ");
		}
	}
	else {
		ret.add(" ");
	    ret.add(" ");
	    ret.add(" ");
		ret.add(" ");
	    ret.add(" ");
	    ret.add(" ");
	}
	
	if (getSecondaryGun() != null) {
		
		ret.add(getSecondaryGun().getName());
		if (getSecondaryGun().getProjectile().getName() != null) {
			ret.add(getSecondaryGun().getProjectile().getName());
			ret.add(Integer.toString(getSecondaryGun().getVelocity()));		
			ret.add(Integer.toString(getSecondaryGun().getProjectile().getMaterial().getPenetration_in_mm()));
			ret.add(Integer.toString(getSecondaryGun().getRounds()));
			ret.add(Integer.toString(getSecondaryGun().getMags()));
		}
		else {
		    ret.add(" ");
			ret.add(" ");
			ret.add(" ");
		    ret.add(" ");
		    ret.add(" ");
		}
	}
	else {
		ret.add(" ");
		ret.add(" ");
	    ret.add(" ");
		ret.add(" ");
	    ret.add(" ");
	    ret.add(" ");
	}	
	
    if (getTertiaryGun() != null) {
		
		ret.add(getTertiaryGun().getName());
		if (getTertiaryGun().getProjectile().getName() != null) {
			ret.add(getTertiaryGun().getProjectile().getName());
			ret.add(Integer.toString(getTertiaryGun().getVelocity()));		
			ret.add(Integer.toString(getTertiaryGun().getProjectile().getMaterial().getPenetration_in_mm()));
			ret.add(Integer.toString(getTertiaryGun().getRounds()));
			ret.add(Integer.toString(getTertiaryGun().getMags()));
		}
		else {
		    ret.add(" ");
			ret.add(" ");
			ret.add(" ");
		    ret.add(" ");
		    ret.add(" ");
		}
	}
	else {
		ret.add(" ");
	    ret.add(" ");
	    ret.add(" ");
		ret.add(" ");
	    ret.add(" ");
	    ret.add(" ");
	}		
	

	return ret;
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
