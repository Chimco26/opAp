package com.operatorsapp.server.responses.GetMachineJoshData;

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

	public List<DepMachineItem> getDepMachine(){
		return depMachine;
	}

	public int getLeaderRecordID(){
		return leaderRecordID;
	}

	public Object getError(){
		return error;
	}

	public boolean isFunctionSucceed(){
		return functionSucceed;
	}
}