package com.example.common.reportShift;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Graph {

    @SerializedName("GraphSeries")
    @Expose
    private List<GraphSeries> graphSeries = null;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("AxisX")
    @Expose
    private String axisX;
    @SerializedName("AxisY")
    @Expose
    private String axisY;
    @SerializedName("GraphType")
    @Expose
    private String graphType;
    @SerializedName("DisplayOrder")
    @Expose
    private Integer displayOrder;
    @SerializedName("GraphTarget")
    @Expose
    private Integer graphTarget;
    @SerializedName("DisplayName")
    @Expose
    private String displayName;

    public List<GraphSeries> getGraphSeries() {
        return graphSeries;
    }

    public void setGraphSeries(List<GraphSeries> graphSeries) {
        this.graphSeries = graphSeries;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAxisX() {
        return axisX;
    }

    public void setAxisX(String axisX) {
        this.axisX = axisX;
    }

    public String getAxisY() {
        return axisY;
    }

    public void setAxisY(String axisY) {
        this.axisY = axisY;
    }

    public String getGraphType() {
        return graphType;
    }

    public void setGraphType(String graphType) {
        this.graphType = graphType;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Integer getGraphTarget() {
        return graphTarget;
    }

    public void setGraphTarget(Integer graphTarget) {
        this.graphTarget = graphTarget;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

}
