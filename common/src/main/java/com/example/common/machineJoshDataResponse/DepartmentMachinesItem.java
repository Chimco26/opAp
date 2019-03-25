package com.example.common.machineJoshDataResponse;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class DepartmentMachinesItem{

	@SerializedName("DisplayOrder")
	private int displayOrder;

	@SerializedName("MachineStatus")
	private int machineStatus;

	@SerializedName("MachineName")
	private Object machineName;

	@SerializedName("Id")
	private int id;

	@SerializedName("JobData")
	private List<JobDataItem> jobData;

	public void setDisplayOrder(int displayOrder){
		this.displayOrder = displayOrder;
	}

	public int getDisplayOrder(){
		return displayOrder;
	}

	public void setMachineStatus(int machineStatus){
		this.machineStatus = machineStatus;
	}

	public int getMachineStatus(){
		return machineStatus;
	}

	public void setMachineName(Object machineName){
		this.machineName = machineName;
	}

	public Object getMachineName(){
		return machineName;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setJobData(List<JobDataItem> jobData){
		this.jobData = jobData;
	}

	public List<JobDataItem> getJobData(){
		return jobData;
	}
}