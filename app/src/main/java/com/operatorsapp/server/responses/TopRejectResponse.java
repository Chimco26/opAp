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

    public TopRejectResponse(ArrayList<TopRejectReason> mRejectsList) {
        this.mRejectsList = mRejectsList;
    }

    public ArrayList<TopRejectReason> getmRejectsList() {
        return mRejectsList;
    }

    public void setmRejectsList(ArrayList<TopRejectReason> mRejectsList) {
        this.mRejectsList = mRejectsList;
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
