package com.operatorsapp.server.responses;

import com.example.common.StandardResponse;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class JobListForMaterialResponse extends StandardResponse {

    @SerializedName("ResponseDictionaryDT")
    MaterialListForTest material;


    public ArrayList<MaterialForTest> getMaterialForTestList() {
        return material != null ? material.materialList : new ArrayList<MaterialForTest>();
    }

    public class MaterialListForTest {

        @SerializedName("Materials")
        ArrayList<MaterialForTest> materialList;
    }
}
