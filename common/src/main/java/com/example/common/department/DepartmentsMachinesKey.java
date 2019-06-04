package com.example.common.department;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DepartmentsMachinesKey {

    @SerializedName("EName")
    @Expose
    private String eName;
    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("LName")
    @Expose
    private String lName;

    public String getEName() {
        return eName;
    }

    public void setEName(String eName) {
        this.eName = eName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLName() {
        return lName;
    }

    public void setLName(String lName) {
        this.lName = lName;
    }

}
