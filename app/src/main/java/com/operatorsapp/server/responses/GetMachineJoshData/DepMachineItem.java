package com.operatorsapp.server.responses.GetMachineJoshData;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class DepMachineItem{

	@SerializedName("EName")
	private Object eName;

	@SerializedName("DepartmentMachines")
	private List<DepartmentMachinesItem> departmentMachines;

	@SerializedName("LName")
	private Object lName;

	@SerializedName("Id")
	private int id;

	public Object getEName(){
		return eName;
	}

	public List<DepartmentMachinesItem> getDepartmentMachines(){
		return departmentMachines;
	}

	public Object getLName(){
		return lName;
	}

	public int getId(){
		return id;
	}
}