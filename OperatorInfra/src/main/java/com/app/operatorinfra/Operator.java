package com.app.operatorinfra;


import com.google.gson.annotations.SerializedName;

public class Operator{

    @SerializedName("OperatorID")
    private String mOperatorId;
    @SerializedName("OperatorName")
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
