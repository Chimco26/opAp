package com.example.common.machineJoshDataResponse;

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

	public void setEName(String eName){
		this.eName = eName;
	}

	public String getEName(){
		return eName;
	}

	public void setMachineID(int machineID){
		this.machineID = machineID;
	}

	public int getMachineID(){
		return machineID;
	}

	public void setLName(String lName){
		this.lName = lName;
	}

	public String getLName(){
		return lName;
	}

	public void setStartTime(String startTime){
		this.startTime = startTime;
	}

	public String getStartTime(){
		return startTime;
	}

	public void setProductID(int productID){
		this.productID = productID;
	}

	public int getProductID(){
		return productID;
	}

	public void setDepartmentID(int departmentID){
		this.departmentID = departmentID;
	}

	public int getDepartmentID(){
		return departmentID;
	}

	public void setJoshID(int joshID){
		this.joshID = joshID;
	}

	public int getJoshID(){
		return joshID;
	}

	public void setJobID(int jobID){
		this.jobID = jobID;
	}

	public int getJobID(){
		return jobID;
	}
}