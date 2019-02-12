package com.operatorsapp.server.responses.GetMachineJoshData;

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

	public int getDisplayOrder(){
		return displayOrder;
	}

	public int getMachineStatus(){
		return machineStatus;
	}

	public Object getMachineName(){
		return machineName;
	}

	public int getId(){
		return id;
	}

	public List<JobDataItem> getJobData(){
		return jobData;
	}
}