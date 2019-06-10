package com.example.common.reportShift;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item {

    @SerializedName("X")
    @Expose
    private String x;
    @SerializedName("Y")
    @Expose
    private Double y;

    public Item(String x, Double y) {
        this.x = x;
        this.y = y;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }


}
