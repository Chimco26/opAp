package com.operators.operatornetworkbridge.server.responses;

import com.app.operatorinfra.Operator;
import com.example.common.StandardResponse;
import com.google.gson.annotations.SerializedName;


public class OperatorDataResponse extends StandardResponse {

    @SerializedName("Operator")
    private Operator mOperator ;

    public Operator getOperator() {
        return mOperator;
    }

    public void setOperator(Operator operator) {
        this.mOperator = operator;
    }
}
