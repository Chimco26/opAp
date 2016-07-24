package com.app.operatorinfra;


import com.google.gson.annotations.SerializedName;

public class Operator{

    @SerializedName("getOperatorId")
    private String mOperatorId;
    @SerializedName("operatorName")
    private String mOperatorName;


    public String getOperatorId() {
        return mOperatorId;
    }


    public void setOperatorId(String operatorId) {
        this.mOperatorId = operatorId;
    }


    public String getOperatorName() {
        return mOperatorName;
    }

    public void setOperatorName(String operatorName) {
        this.mOperatorName = operatorName;
    }
}
