package com.operators.machinestatusinfra;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TimeWidget extends BaseWidget {

    @SerializedName("HighLimit")
    private int mHighLimit;

    @SerializedName("LowLimit")
    private int mLowLimit;

    @SerializedName("MachineParamHistoricData")
    private List<HistoricData> mMachineParamHistoricData;

    @SerializedName("StandardValue")
    private int mStandardValue;


    private class HistoricData {
        @SerializedName("Time")
        private String mTime;

        @SerializedName("Value")
        private int mValue;

        public String getTime() {
            return mTime;
        }

        public void setTime(String time) {
            this.mTime = time;
        }

        public int getValue() {
            return mValue;
        }

        public void setValue(int value) {
            this.mValue = value;
        }
    }

    public int getHighLimit() {
        return mHighLimit;
    }

    public void setHighLimit(int highLimit) {
        this.mHighLimit = highLimit;
    }

    public int getLowLimit() {
        return mLowLimit;
    }

    public void setLowLimit(int lowLimit) {
        this.mLowLimit = lowLimit;
    }

    public List<HistoricData> getMachineParamHistoricData() {
        return mMachineParamHistoricData;
    }

    public void setMachineParamHistoricData(List<HistoricData> machineParamHistoricData) {
        this.mMachineParamHistoricData = machineParamHistoricData;
    }

    public int getStandardValue() {
        return mStandardValue;
    }

    public void setStandardValue(int standardValue) {
        this.mStandardValue = standardValue;
    }
}
