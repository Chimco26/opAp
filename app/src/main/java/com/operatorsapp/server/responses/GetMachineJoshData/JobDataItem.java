package com.operatorsapp.server.responses.GetMachineJoshData;

import com.google.gson.annotations.SerializedName;

public class JobDataItem{

	@SerializedName("EName")
	private String eName;

	@SerializedName("MachineID")
	private int machineID;

	@SerializedName("LName")
	private String lName;

	@SerializedName("StartTime")
	private String startTime;

	@SerializedName("ProductID")
	private int productID;

	@SerializedName("DepartmentID")
	private int departmentID;

	@SerializedName("JoshID")
	private int joshID;

	@SerializedName("JobID")
	private int jobID;

	public String getEName(){
		return eName;
	}

	public int getMachineID(){
		return machineID;
	}

	public String getLName(){
		return lName;
	}

	public String getStartTime(){
		return startTime;
	}

	public int getProductID(){
		return productID;
	}

	public int getDepartmentID(){
		return departmentID;
	}

	public int getJoshID(){
		return joshID;
	}

	public int getJobID(){
		return jobID;
	}
}