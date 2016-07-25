package com.operators.operatornetworkbridge.server.responses;

import com.app.operatorinfra.Operator;
import com.google.gson.annotations.SerializedName;


public class OperatorDataResponse extends ErrorBaseResponse {

    @SerializedName("Operator")
    private Operator mOperator = new Operator();

    public Operator getOperator() {
        return mOperator;
    }

    public void setOperator(Operator operator) {
        this.mOperator = operator;
    }
}
