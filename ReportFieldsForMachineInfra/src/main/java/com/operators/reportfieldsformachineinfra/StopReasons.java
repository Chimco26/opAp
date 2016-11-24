package com.operators.reportfieldsformachineinfra;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sergey on 02/08/2016.
 */
public class StopReasons {

    @SerializedName("ID")
    private int id;
    @SerializedName("EName")
    private String EName;
    @SerializedName("LName")
    private String LName;
    @SerializedName("SubReason")
    private List<SubReasons> subReasons = new ArrayList<SubReasons>();

    public int getId() {
        return id;
    }

    public String getEName() {
        return EName;
    }

    public List<SubReasons> getSubReasons() {
        return subReasons;
    }

    public String getLName()
    {
        return LName;
    }
}