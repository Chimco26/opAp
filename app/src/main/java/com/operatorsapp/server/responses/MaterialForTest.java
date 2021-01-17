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
                case "MaterialName":
                    fieldName = getMaterialName();
                    break;
                case "Description":
                    fieldName = getDescription();
                    break;
                case "ERPID":
                    fieldName = getErpId();
                    break;
                case "MaterialGroup":
                    fieldName = getMaterialGroup();
                    break;
                case "ErpAmount":
                    fieldName = getErpAmount()+"";
                    break;
            }
            propertyList.add(i, new Property(headersStringArray[i], fieldName));
        }
        return propertyList;
    }
}
