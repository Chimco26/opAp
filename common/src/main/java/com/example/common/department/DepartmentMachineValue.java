package com.example.common.department;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DepartmentMachineValue implements Cloneable {

    @SerializedName("DisplayOrder")
    @Expose
    private Integer displayOrder;
    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("MachineName")
    @Expose
    private String machineName;
    @SerializedName("MachineStatus")
    @Expose
    private Integer machineStatus;
    @SerializedName("LineID")
    @Expose
    private Integer lineId;
    @SerializedName("StatusTime")
    @Expose
    private Integer statusTime;
    @SerializedName("IsEndOfLine")
    @Expose
    private boolean isEndOfLine;
    @SerializedName("MachineStatusColor")
    @Expose
    private String machineStatusColor;
    @SerializedName("ProductName")
    @Expose
    private String productName;
    @SerializedName("ProductCatalog")
    @Expose
    private String productCatalog;
    @SerializedName("ErpJobID")
    @Expose
    private String erpJobID;
    @SerializedName("ClientName")
    @Expose
    private String clientName;


    public Integer getLineId() {
        return lineId;
    }

    public void setLineId(Integer lineId) {
        this.lineId = lineId;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public Integer getMachineStatus() {
        return machineStatus;
    }

    public void setMachineStatus(Integer machineStatus) {
        this.machineStatus = machineStatus;
    }

    public Integer getStatusTime() { return statusTime; }

    public boolean isEndOfLine() { return isEndOfLine; }

    public String getMachineStatusColor() { return machineStatusColor; }

    public String getProductName() { return productName; }

    public String getProductCatalog() { return productCatalog; }

    public String getErpJobID() { return erpJobID; }

    public String getClientName() { return clientName; }
}
