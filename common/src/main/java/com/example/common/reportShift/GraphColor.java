package com.example.common.reportShift;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GraphColor implements Comparable<GraphColor>{
    @SerializedName("ColorName")
    @Expose
    private String colorName;
    @SerializedName("HexCode")
    @Expose
    private String hexCode;
    @SerializedName("RGB")
    @Expose
    private String rGB;
    @SerializedName("DisplayOrder")
    @Expose
    private Integer displayOrder;

    public GraphColor(String colorName, String hexCode, String rGB, Integer displayOrder) {
        this.colorName = colorName;
        this.hexCode = hexCode;
        this.rGB = rGB;
        this.displayOrder = displayOrder;
    }

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public String getHexCode() {
        return hexCode;
    }

    public void setHexCode(String hexCode) {
        this.hexCode = hexCode;
    }

    public String getRGB() {
        return rGB;
    }

    public void setRGB(String rGB) {
        this.rGB = rGB;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    @Override
    public int compareTo(GraphColor graphColor) {
        return displayOrder.compareTo(graphColor.displayOrder);
    }
}
