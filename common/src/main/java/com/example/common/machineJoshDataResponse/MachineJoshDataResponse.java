package com.example.common.machineJoshDataResponse;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MachineJoshDataResponse implements Parcelable {

	@SerializedName("DepMachine")
	private List<DepMachineItem> depMachine;

	@SerializedName("LeaderRecordID")
	private int leaderRecordID;

	@SerializedName("error")
	private String error;

	@SerializedName("FunctionSucceed")
	private boolean functionSucceed;

	public void setDepMachine(List<DepMachineItem> depMachine){
		this.depMachine = depMachine;
	}

	public List<DepMachineItem> getDepMachine(){
		if (depMachine == null){
			depMachine = new ArrayList<>();
		}
		return depMachine;
	}

	public void setLeaderRecordID(int leaderRecordID){
		this.leaderRecordID = leaderRecordID;
	}

	public int getLeaderRecordID(){
		return leaderRecordID;
	}

	public void setError(String error){
		this.error = error;
	}

	public String getError(){
		return error;
	}

	public void setFunctionSucceed(boolean functionSucceed){
		this.functionSucceed = functionSucceed;
	}

	public boolean isFunctionSucceed(){
		return functionSucceed;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeList(this.depMachine);
		dest.writeInt(this.leaderRecordID);
		dest.writeString(this.error);
		dest.writeByte(this.functionSucceed ? (byte) 1 : (byte) 0);
	}

	public MachineJoshDataResponse() {
	}

	protected MachineJoshDataResponse(Parcel in) {
		this.depMachine = new ArrayList<DepMachineItem>();
		in.readList(this.depMachine, DepMachineItem.class.getClassLoader());
		this.leaderRecordID = in.readInt();
		this.error = in.readString();
		this.functionSucceed = in.readByte() != 0;
	}

	public static final Parcelable.Creator<MachineJoshDataResponse> CREATOR = new Parcelable.Creator<MachineJoshDataResponse>() {
		@Override
		public MachineJoshDataResponse createFromParcel(Parcel source) {
			return new MachineJoshDataResponse(source);
		}

		@Override
		public MachineJoshDataResponse[] newArray(int size) {
			return new MachineJoshDataResponse[size];
		}
	};
}