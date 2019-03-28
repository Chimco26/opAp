package com.example.common.machineJoshDataResponse;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class DepartmentMachinesItem implements Parcelable {

	@SerializedName("DisplayOrder")
	private int displayOrder;

	@SerializedName("MachineStatus")
	private int machineStatus;

	@SerializedName("MachineName")
	private String machineName;

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

	public void setMachineName(String machineName){
		this.machineName = machineName;
	}

	public String getMachineName(){
		return machineName;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setJobData(List<JobDataItem> jobData){
		if (jobData == null){
			jobData = new ArrayList<>();
		}
		this.jobData = jobData;
	}

	public List<JobDataItem> getJobData(){
		return jobData;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.displayOrder);
		dest.writeInt(this.machineStatus);
		dest.writeString(this.machineName);
		dest.writeInt(this.id);
		dest.writeList(this.jobData);
	}

	public DepartmentMachinesItem() {
	}

	protected DepartmentMachinesItem(Parcel in) {
		this.displayOrder = in.readInt();
		this.machineStatus = in.readInt();
		this.machineName = in.readString();
		this.id = in.readInt();
		this.jobData = new ArrayList<JobDataItem>();
		in.readList(this.jobData, JobDataItem.class.getClassLoader());
	}

	public static final Parcelable.Creator<DepartmentMachinesItem> CREATOR = new Parcelable.Creator<DepartmentMachinesItem>() {
		@Override
		public DepartmentMachinesItem createFromParcel(Parcel source) {
			return new DepartmentMachinesItem(source);
		}

		@Override
		public DepartmentMachinesItem[] newArray(int size) {
			return new DepartmentMachinesItem[size];
		}
	};
}