package com.example.common.machineJoshDataResponse;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class MachineJoshDataResponse{

	@SerializedName("DepMachine")
	private List<DepMachineItem> depMachine;

	@SerializedName("LeaderRecordID")
	private int leaderRecordID;

	@SerializedName("error")
	private Object error;

	@SerializedName("FunctionSucceed")
	private boolean functionSucceed;

	public void setDepMachine(List<DepMachineItem> depMachine){
		this.depMachine = depMachine;
	}

	public List<DepMachineItem> getDepMachine(){
		return depMachine;
	}

	public void setLeaderRecordID(int leaderRecordID){
		this.leaderRecordID = leaderRecordID;
	}

	public int getLeaderRecordID(){
		return leaderRecordID;
	}

	public void setError(Object error){
		this.error = error;
	}

	public Object getError(){
		return error;
	}

	public void setFunctionSucceed(boolean functionSucceed){
		this.functionSucceed = functionSucceed;
	}

	public boolean isFunctionSucceed(){
		return functionSucceed;
	}
}