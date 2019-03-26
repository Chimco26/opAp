package com.example.common.machineJoshDataResponse;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class JobDataItem implements Parcelable {

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

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.eName);
		dest.writeInt(this.machineID);
		dest.writeString(this.lName);
		dest.writeString(this.startTime);
		dest.writeInt(this.productID);
		dest.writeInt(this.departmentID);
		dest.writeInt(this.joshID);
		dest.writeInt(this.jobID);
	}

	public JobDataItem() {
	}

	protected JobDataItem(Parcel in) {
		this.eName = in.readString();
		this.machineID = in.readInt();
		this.lName = in.readString();
		this.startTime = in.readString();
		this.productID = in.readInt();
		this.departmentID = in.readInt();
		this.joshID = in.readInt();
		this.jobID = in.readInt();
	}

	public static final Parcelable.Creator<JobDataItem> CREATOR = new Parcelable.Creator<JobDataItem>() {
		@Override
		public JobDataItem createFromParcel(Parcel source) {
			return new JobDataItem(source);
		}

		@Override
		public JobDataItem[] newArray(int size) {
			return new JobDataItem[size];
		}
	};
}