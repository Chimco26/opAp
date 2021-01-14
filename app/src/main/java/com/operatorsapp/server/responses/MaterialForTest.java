package com.operatorsapp.server.responses;

import android.content.Context;

import com.google.gson.annotations.SerializedName;
import com.operators.reportrejectnetworkbridge.server.response.activateJob.Property;
import com.operatorsapp.R;

import java.util.ArrayList;
import java.util.List;

public class MaterialForTest {

    @SerializedName("ID")
    int id;

    @SerializedName("MaterialName")
    String materialName;

    @SerializedName("Description")
    String description;

    @SerializedName("MaterialGroup")
    String materialGroup;

    @SerializedName("CatalogID")
    String catalogID;

    @SerializedName("ERPID")
    String erpId;

    @SerializedName("ErpAmount")
    int erpAmount;

    public int getId() {
        return id;
    }

    public String getMaterialName() {
        return materialName;
    }

    public String getDescription() {
        return description;
    }

    public String getMaterialGroup() {
        return materialGroup;
    }

    public String getCatalogID() {
        return catalogID;
    }

    public String getErpId() {
        return erpId;
    }

    public int getErpAmount() {
        return erpAmount;
    }

    public String[] getHeaders(Context context) {
        return context.getResources().getStringArray(R.array.material_headers_for_qc_array);
    }

    // TODO: 12/01/2021 translate:
    public List<Property> getProperties(Context context) {
        ArrayList<Property> propertyList = new ArrayList<>();
        String[] headersStringArray = getHeaders(context);
        propertyList.add(0, new Property(headersStringArray[0], getId()+""));
        propertyList.add(1, new Property(headersStringArray[1], getMaterialName()));
        propertyList.add(2, new Property(headersStringArray[2], getDescription()));
        propertyList.add(3, new Property(headersStringArray[3], getMaterialGroup()));
        propertyList.add(4, new Property(headersStringArray[4], getCatalogID()));
        propertyList.add(5, new Property(headersStringArray[5], getErpId()));
        propertyList.add(6, new Property(headersStringArray[6], getErpAmount()+""));

        return propertyList;
    }
}
