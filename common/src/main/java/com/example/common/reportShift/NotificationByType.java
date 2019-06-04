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
    private Integer pC;
    @SerializedName("ResponseType")
    @Expose
    private Integer responseType;
    @SerializedName("TotalRespones")
    @Expose
    private Integer totalRespones;

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

    public Integer getPC() {
        return pC;
    }

    public void setPC(Integer pC) {
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
