package com.operatorsapp.server.responses;

import com.example.common.StandardResponse;
import com.google.gson.annotations.SerializedName;
import com.operatorsapp.utils.ChangeLang;

import java.util.ArrayList;
import java.util.HashMap;

public class ResponseKPIS extends StandardResponse {

    @SerializedName("ResponseList")
    ArrayList<HashMap<String, String>> mTranslationList;

    public ArrayList<HashMap<String, String>> getTranslationList() {
        return mTranslationList;
    }

    public String getKPIByName(String name){
        if (mTranslationList!= null && !mTranslationList.isEmpty()) {
            for (HashMap item : mTranslationList) {
                if (item != null && item.get("Name") != null && item.get("Name").toString().toLowerCase().equals(name.toLowerCase())) {
                    String txt = String.valueOf(item.get(ChangeLang.languagesCode3Letters[ChangeLang.getPositionByLanguageCode()]));
                    txt = !txt.isEmpty() ? txt :  String.valueOf(item.get(ChangeLang.languagesCode3Letters[0]));
                    return txt;
                }
            }
        }
        return "";
    }
}
