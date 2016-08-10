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
    private String name;
    @SerializedName("SubReason")
    private List<SubReasons> subReasons = new ArrayList<SubReasons>();

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<SubReasons> getSubReasons() {
        return subReasons;
    }
}