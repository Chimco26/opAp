package com.example.common.reportShift;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GraphSeries {
    @SerializedName("Items")
    @Expose
    private List<Item> items = null;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Color")
    @Expose
    private String color;
    @SerializedName("MaxValue")
    @Expose
    private Float maxValue;
    @SerializedName("MinValue")
    @Expose
    private Float minValue;

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Float getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(Float maxValue) {
        this.maxValue = maxValue;
    }

    public Float getMinValue() {
        return minValue;
    }

    public void setMinValue(Float minValue) {
        this.minValue = minValue;
    }}
