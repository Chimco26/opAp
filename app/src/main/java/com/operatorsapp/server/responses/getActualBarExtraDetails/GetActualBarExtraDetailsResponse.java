package com.operatorsapp.server.responses.getActualBarExtraDetails;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class GetActualBarExtraDetailsResponse{

	@SerializedName("Rejects")
	private List<RejectsItem> rejects;

	@SerializedName("FunctionSucceed")
	private boolean functionSucceed;

	@SerializedName("Inventory")
	private List<InventoryItem> inventory;

	@SerializedName("Notification")
	private List<NotificationItem> notification;

	public List<RejectsItem> getRejects(){
		return rejects;
	}

	public boolean isFunctionSucceed(){
		return functionSucceed;
	}

	public List<InventoryItem> getInventory(){
		return inventory;
	}

	public List<NotificationItem> getNotification(){
		return notification;
	}
}