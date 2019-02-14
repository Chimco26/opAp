package com.operatorsapp.server.responses.getActualBarExtraDetails;

import com.google.gson.annotations.SerializedName;

public class RejectsItem{

	@SerializedName("EName")
	private String eName;

	@SerializedName("LName")
	private String lName;

	@SerializedName("Amount")
	private int amount;

	@SerializedName("ShiftID")
	private int shiftID;

	@SerializedName("Time")
	private String time;

	@SerializedName("ID")
	private int iD;

	public String getEName(){
		return eName;
	}

	public String getLName(){
		return lName;
	}

	public int getAmount(){
		return amount;
	}

	public int getShiftID(){
		return shiftID;
	}

	public String getTime(){
		return time;
	}

	public int getID(){
		return iD;
	}
}