package com.operators.reportfieldsformachineinfra;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Sergey on 02/08/2016.
 */
public class RejectReasons {

    @SerializedName("ID")
    private int id;
    @SerializedName("EName")
    private String name;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public RejectReasons(int id, String name) {
        this.id = id;
        this.name = name;
    }
}

