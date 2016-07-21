package com.app.operatorinfra;


import com.google.gson.annotations.SerializedName;

public class Operator{

    @SerializedName("operatorId")
    private String mOperatorId;
    @SerializedName("0peratorName")
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
