package com.operatorsapp.server.responses;

import com.google.gson.annotations.SerializedName;
import com.operatorsapp.model.TopFiveItem;

import java.util.ArrayList;

/**
 * Created by alex on 20/12/2018.
 */

public class TopRejectResponse extends PojoResponse{

    @SerializedName("RejectsList")
    ArrayList<TopRejectReason> mRejectsList;
    @SerializedName("GoodUnits")
    Double mGoodUnits;
    @SerializedName("RejectedUnits")
    Double mRejectedUnits;
    @SerializedName("RejectsPC")
    Double mRejectsPC;

    public TopRejectResponse(ArrayList<TopRejectReason> mRejectsList) {
        this.mRejectsList = mRejectsList;
    }

    public ArrayList<TopRejectReason> getmRejectsList() {
        return mRejectsList;
    }

    public void setmRejectsList(ArrayList<TopRejectReason> mRejectsList) {
        this.mRejectsList = mRejectsList;
    }

    public Double getmGoodUnits() {
        return mGoodUnits;
    }

    public void setmGoodUnits(Double mGoodUnits) {
        this.mGoodUnits = mGoodUnits;
    }

    public Double getmRejectedUnits() {
        return mRejectedUnits;
    }

    public void setmRejectedUnits(Double mRejectedUnits) {
        this.mRejectedUnits = mRejectedUnits;
    }

    public Double getmRejectsPC() {
        return mRejectsPC;
    }

    public void setmRejectsPC(Double mRejectsPC) {
        this.mRejectsPC = mRejectsPC;
    }

    public ArrayList<TopFiveItem> getRejectsAsTopFive(){
        ArrayList<TopFiveItem> list = new ArrayList<>();
        if (mRejectsList != null) {
            for (TopRejectReason reason : mRejectsList) {
                list.add(new TopFiveItem(reason.getmAmount(), reason.getmName(), ""));
            }
        }
        return list;
    }
}
