package com.operators.infra;

import com.google.gson.annotations.SerializedName;

public class MachineStatus
{
        @SerializedName("MachineID")
        private int machineID;
        @SerializedName("MachineLname")
        private String machineLname;
        @SerializedName("MachineName")
        private String machineName;
        @SerializedName("MachineStatusEname")
        private String machineStatusEname;
        @SerializedName("MachineStatusID")
        private int machineStatusID;
        @SerializedName("MachineStatusName")
        private String machineStatusName;
        @SerializedName("OperatorID")
        private int operatorID;
        @SerializedName("operatorName")
        private String operatorName;
        @SerializedName("productName")
        private String productName;

        @SerializedName("productId")
        private int productId;
        @SerializedName("jobId")
        private int jobId;
        @SerializedName("shiftId")
        private int shiftId;
        @SerializedName("shiftEndingIn")
        private int shiftEndingIn;


}
