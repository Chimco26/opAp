package com.example.common.machineJoshDataResponse;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class DepMachineItem implements Parcelable {

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
		if (departmentMachines == null){
			departmentMachines = new ArrayList<>();
		}
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

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.eName);
		dest.writeList(this.departmentMachines);
		dest.writeString(this.lName);
		dest.writeInt(this.id);
	}

	public DepMachineItem() {
	}

	protected DepMachineItem(Parcel in) {
		this.eName = in.readString();
		this.departmentMachines = new ArrayList<DepartmentMachinesItem>();
		in.readList(this.departmentMachines, DepartmentMachinesItem.class.getClassLoader());
		this.lName = in.readString();
		this.id = in.readInt();
	}

	public static final Parcelable.Creator<DepMachineItem> CREATOR = new Parcelable.Creator<DepMachineItem>() {
		@Override
		public DepMachineItem createFromParcel(Parcel source) {
			return new DepMachineItem(source);
		}

		@Override
		public DepMachineItem[] newArray(int size) {
			return new DepMachineItem[size];
		}
	};
}