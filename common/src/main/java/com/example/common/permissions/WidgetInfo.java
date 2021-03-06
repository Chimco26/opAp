package com.example.common.permissions;

import android.util.SparseArray;
import android.view.View;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WidgetInfo {

    public enum PermissionId{
        SERVICE_CALLS(1),PRODUCTION_STATUS(2),OPERATOR_SIGN_IN(3),SHIFT_REPORT(4),EVENT_LIST(5),
        SHIFT_LOG(6),ACTIVATE_JOB(7),ADD_REJECTS(8),CHANGE_UNITS_IN_CYCLE(9),END_SETUP(10),
        MESSAGES(11), REPORT_PRODUCTION(12), ENABLE_EDIT_JOB_RECIPE(13), QC(14), TASK(15);

        PermissionId(int id) {
            this.id = id;
        }

        private int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("menu")
    @Expose
    private String menu;
    @SerializedName("menulname")
    @Expose
    private String menulname;
    @SerializedName("menuename")
    @Expose
    private String menuename;
    @SerializedName("haspermission")
    @Expose
    private Boolean haspermission;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public String getMenulname() {
        return menulname;
    }

    public void setMenulname(String menulname) {
        this.menulname = menulname;
    }

    public String getMenuename() {
        return menuename;
    }

    public void setMenuename(String menuename) {
        this.menuename = menuename;
    }

    public int getHaspermission() {
        if (haspermission){
            return View.VISIBLE;
        }else {
            return View.GONE;
        }
    }

    public boolean getHaspermissionBoolean() {
        return haspermission;
    }

    public void setHaspermission(Boolean haspermission) {
        this.haspermission = haspermission;
    }

    public static WidgetInfo getWidgetInfo(SparseArray<WidgetInfo> permissionResponse, int typeId) {
        if (permissionResponse != null && permissionResponse.get(typeId) != null) {
            return permissionResponse.get(typeId);
        } else {
            WidgetInfo widgetInfo = new WidgetInfo();
            widgetInfo.setHaspermission(true);
            return widgetInfo;
        }

    }
}