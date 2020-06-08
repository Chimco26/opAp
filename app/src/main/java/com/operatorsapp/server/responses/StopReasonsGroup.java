package com.operatorsapp.server.responses;

import android.content.Context;
import android.graphics.Color;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class StopReasonsGroup {

    @SerializedName("ColorID")
    private String mColorID;

    @SerializedName("DepartmentID")
    private int mDepartmentID;

    @SerializedName("DictionaryID")
    private int mDictionaryID;

    @SerializedName("DisplayInOpApp")
    private boolean isDisplayInOpApp;

    @SerializedName("DisplayOrder")
    private int mDisplayOrder;

    @SerializedName("ID")
    private int id;
    @SerializedName("EName")
    private String EName;
    @SerializedName("LName")
    private String LName;

    @SerializedName("IconID")
    private String mIconID;

    @SerializedName("IsActive")
    private boolean isActive;

    @SerializedName("IsSystem")
    private boolean isSystem;

    @SerializedName("MachineID")
    private int mMachineID;

//    @SerializedName("Machines")
//    private ??? mMachines;

    @SerializedName("NumOfMachines")
    private int mNumOfMachines;

    @SerializedName("Reasons")
    private ArrayList<StopReasonsGroup> subReasons = new ArrayList<StopReasonsGroup>();

    public String getmColorID() {
        return mColorID;
    }

    public int getmDepartmentID() {
        return mDepartmentID;
    }

    public int getmDictionaryID() {
        return mDictionaryID;
    }

    public boolean isDisplayInOpApp() {
        return isDisplayInOpApp;
    }

    public int getmDisplayOrder() {
        return mDisplayOrder;
    }

    public int getId() {
        return id;
    }

    public String getEName() {
        return EName;
    }

    public String getLName() {
        return LName;
    }

    public String getmIconID() {

        if (mIconID != null) {
            String icon = mIconID.replace("-", "_");
            return icon.replace(".svg", "");
        }
        return "";
    }

    public boolean isActive() {
        return isActive;
    }

    public boolean isSystem() {
        return isSystem;
    }

    public int getmMachineID() {
        return mMachineID;
    }

    public int getmNumOfMachines() {
        return mNumOfMachines;
    }

    public ArrayList<StopReasonsGroup> getSubReasons() {
        return subReasons;
    }

    public int getGroupIcon(Context context){

        String icon = getmIconID();
        if (icon != null && !icon.equals("")) {
            return context.getResources().getIdentifier(icon, "drawable", context.getPackageName());
        }

        return 0;
    }

    public int getGroupColor(){
        try {
            return Color.parseColor(getmColorID());
        }catch (IllegalArgumentException e){
            return Color.parseColor("#bababa");
        }catch (StringIndexOutOfBoundsException e){
            return Color.parseColor("#bababa");
        }catch (NullPointerException e){
            return Color.parseColor("#bababa");
        }
    }
}
