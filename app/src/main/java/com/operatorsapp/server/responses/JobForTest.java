package com.operatorsapp.server.responses;

import android.content.Context;

import com.google.gson.annotations.SerializedName;
import com.operators.reportrejectnetworkbridge.server.response.activateJob.Property;
import com.operatorsapp.R;

import java.util.ArrayList;
import java.util.List;

public class JobForTest {

    @SerializedName("ID")
    int id;

    @SerializedName("Status")
    int status;

    @SerializedName("MachineName")
    String machineName;

    @SerializedName("ProductName")
    String productName;

    @SerializedName("CatalogID")
    String catalogID;

    @SerializedName("UnitsTarget")
    double unitsTarget;

    @SerializedName("UnitsProduced")
    double unitsProduced;

    @SerializedName("ERPJobID")
    String erpJobID;

    @SerializedName("Notes")
    String notes;

    @SerializedName("ERPJobIndexKey")
    String erpJobIndexKey;

    public int getId() {
        return id;
    }

    public int getStatus() {
        return status;
    }

    public String getMachineName() {
        return machineName;
    }

    public String getProductName() {
        return productName;
    }

    public String getCatalogID() {
        return catalogID;
    }

    public double getUnitsTarget() {
        return unitsTarget;
    }

    public double getUnitsProduced() {
        return unitsProduced;
    }

    public String getErpJobID() {
        return erpJobID;
    }

    public String getNotes() {
        return notes;
    }

    public String getErpJobIndexKey() {
        return erpJobIndexKey;
    }

    public String[] getHeaders(Context context) {
        return context.getResources().getStringArray(R.array.pending_job_headers_for_qc_array);
    }

    public List<Property> getProperties(Context context) {
        ArrayList<Property> propertyList = new ArrayList<>();
        String[] headersStringArray = getHeaders(context);
        propertyList.add(0, new Property(headersStringArray[0], getId()+""));
        propertyList.add(1, new Property(headersStringArray[1], getCatalogID()));
        propertyList.add(2, new Property(headersStringArray[2], getStatus()+""));
        propertyList.add(3, new Property(headersStringArray[3], getMachineName()));
        propertyList.add(4, new Property(headersStringArray[4], getProductName()));
        propertyList.add(5, new Property(headersStringArray[5], getUnitsTarget()+""));
        propertyList.add(6, new Property(headersStringArray[6], getUnitsProduced()+""));
        propertyList.add(7, new Property(headersStringArray[7], getErpJobID()));
        propertyList.add(8, new Property(headersStringArray[8], getNotes()));
        propertyList.add(9, new Property(headersStringArray[9], getErpJobIndexKey()));

        return propertyList;
    }
}
