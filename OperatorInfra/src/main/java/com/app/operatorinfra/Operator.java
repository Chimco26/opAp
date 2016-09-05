package com.app.operatorinfra;


import com.google.gson.annotations.SerializedName;

public class Operator{

    @SerializedName("OperatorID")
    private String mOperatorId;
    @SerializedName("OperatorName")
    private String mOperatorName;

    public Operator(String operatorId, String operatorName) {
        mOperatorId = operatorId;
        mOperatorName = operatorName;
    }

    public String getOperatorId() {
        return mOperatorId;
    }

    public String getOperatorName() {
        return mOperatorName;
    }

}
