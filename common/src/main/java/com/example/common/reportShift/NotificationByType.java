package com.example.common.reportShift;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationByType {

    @SerializedName("EName")
    @Expose
    private String eName;
    @SerializedName("LName")
    @Expose
    private String lName;
    @SerializedName("NumOfResponse")
    @Expose
    private Integer numOfResponse;
    @SerializedName("PC")
    @Expose
    private Float pC;
    @SerializedName("ResponseType")
    @Expose
    private Integer responseType;
    @SerializedName("TotalRespones")
    @Expose
    private Integer totalRespones;

    public NotificationByType(String eName, String lName, Integer numOfResponse, Float pC, Integer responseType, Integer totalRespones) {
        this.eName = eName;
        this.lName = lName;
        this.numOfResponse = numOfResponse;
        this.pC = pC;
        this.responseType = responseType;
        this.totalRespones = totalRespones;
    }

    public String getEName() {
        return eName;
    }

    public void setEName(String eName) {
        this.eName = eName;
    }

    public String getLName() {
        return lName;
    }

    public void setLName(String lName) {
        this.lName = lName;
    }

    public Integer getNumOfResponse() {
        return numOfResponse;
    }

    public void setNumOfResponse(Integer numOfResponse) {
        this.numOfResponse = numOfResponse;
    }

    public Float getPC() {
        return pC;
    }

    public void setPC(Float pC) {
        this.pC = pC;
    }

    public Integer getResponseType() {
        return responseType;
    }

    public void setResponseType(Integer responseType) {
        this.responseType = responseType;
    }

    public Integer getTotalRespones() {
        return totalRespones;
    }

    public void setTotalRespones(Integer totalRespones) {
        this.totalRespones = totalRespones;
    }
}
