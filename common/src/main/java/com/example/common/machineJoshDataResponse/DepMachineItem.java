package com.example.common.machineJoshDataResponse;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class DepMachineItem{

	@SerializedName("EName")
	private String eName;

	@SerializedName("DepartmentMachines")
	private List<DepartmentMachinesItem> departmentMachines;

	@SerializedName("LName")
	private String lName;

	@SerializedName("Id")
	private int id;

	public void setEName(String eName){
		this.eName = eName;
	}

	public String getEName(){
		return eName;
	}

	public void setDepartmentMachines(List<DepartmentMachinesItem> departmentMachines){
		this.departmentMachines = departmentMachines;
	}

	public List<DepartmentMachinesItem> getDepartmentMachines(){
		return departmentMachines;
	}

	public void setLName(String lName){
		this.lName = lName;
	}

	public Object getLName(){
		return lName;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}
}