package com.example.common.reportShift;

import com.example.common.StandardResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DepartmentShiftGraphResponse extends StandardResponse {

    @SerializedName("Departments")
    @Expose
    private List<Department> departments = null;
    @SerializedName("GraphColors")
    @Expose
    private List<GraphColor> graphColors = null;




    public List<Department> getDepartments() {
        return departments;
    }

    public void setDepartments(List<Department> departments) {
        this.departments = departments;
    }

    public List<GraphColor> getGraphColors() {
        return graphColors;
    }

    public void setGraphColors(List<GraphColor> graphColors) {
        this.graphColors = graphColors;
    }


}
