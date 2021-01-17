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

    public List<Property> getProperties(String[] headersStringArray) {
        ArrayList<Property> propertyList = new ArrayList<>();
        for (int i = 0; i < headersStringArray.length; i++) {
            String fieldName = "";
            switch (headersStringArray[i]){
                case "ID":
                    fieldName = getId()+"";
                    break;
                case "CatalogID":
                    fieldName = getCatalogID();
                    break;
                case "Status":
                    fieldName = getStatus()+"";
                    break;
                case "MachineName":
                    fieldName = getMachineName();
                    break;
                case "ProductName":
                    fieldName = getProductName();
                    break;
                case "UnitsTarget":
                    fieldName = getUnitsTarget()+"";
                    break;
                case "UnitsProduced":
                    fieldName = getUnitsProduced()+"";
                    break;
                case "ERPJobID":
                    fieldName = getErpJobID();
                    break;
                case "Notes":
                    fieldName = getNotes();
                    break;
                case "ERPJobIndexKey":
                    fieldName = getErpJobIndexKey();
                    break;
            }
            propertyList.add(i, new Property(headersStringArray[i], fieldName));
        }

        return propertyList;
    }
}
