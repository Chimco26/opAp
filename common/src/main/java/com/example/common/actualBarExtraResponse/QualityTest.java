package com.example.common.actualBarExtraResponse;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class QualityTest implements Parcelable {

    @SerializedName("ID")
    @Expose
    private Integer iD;
    @SerializedName("ShiftID")
    @Expose
    private Integer shiftID;
    @SerializedName("LName")
    @Expose
    private String lName;
    @SerializedName("EName")
    @Expose
    private String eName;
    @SerializedName("ReportTime")
    @Expose
    private String reportTime;
    @SerializedName("TestTime")
    @Expose
    private String testTime;
    @SerializedName("Passed")
    @Expose
    private Boolean passed;

    /**
     * No args constructor for use in serialization
     */
    public QualityTest() {
    }

    /**
     * @param shiftID
     * @param lName
     * @param eName
     * @param testTime
     * @param iD
     * @param passed
     * @param reportTime
     */
    public QualityTest(Integer iD, Integer shiftID, String lName, String eName, String reportTime, String testTime, Boolean passed) {
        super();
        this.iD = iD;
        this.shiftID = shiftID;
        this.lName = lName;
        this.eName = eName;
        this.reportTime = reportTime;
        this.testTime = testTime;
        this.passed = passed;
    }

    public Integer getID() {
        return iD;
    }

    public void setID(Integer iD) {
        this.iD = iD;
    }

    public Integer getShiftID() {
        return shiftID;
    }

    public void setShiftID(Integer shiftID) {
        this.shiftID = shiftID;
    }

    public String getLName() {
        return lName;
    }

    public void setLName(String lName) {
        this.lName = lName;
    }

    public String getEName() {
        return eName;
    }

    public void setEName(String eName) {
        this.eName = eName;
    }

    public String getReportTime() {
        return reportTime;
    }

    public void setReportTime(String reportTime) {
        this.reportTime = reportTime;
    }

    public String getTestTime() {
        return testTime;
    }

    public void setTestTime(String testTime) {
        this.testTime = testTime;
    }

    public Boolean getPassed() {
        if (passed == null){
            return false;
        }
        return passed;
    }

    public void setPassed(Boolean passed) {
        this.passed = passed;
    }


    protected QualityTest(Parcel in) {
        iD = in.readByte() == 0x00 ? null : in.readInt();
        shiftID = in.readByte() == 0x00 ? null : in.readInt();
        lName = in.readString();
        eName = in.readString();
        reportTime = in.readString();
        testTime = in.readString();
        byte passedVal = in.readByte();
        passed = passedVal == 0x02 ? null : passedVal != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (iD == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(iD);
        }
        if (shiftID == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(shiftID);
        }
        dest.writeString(lName);
        dest.writeString(eName);
        dest.writeString(reportTime);
        dest.writeString(testTime);
        if (passed == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (passed ? 0x01 : 0x00));
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<QualityTest> CREATOR = new Parcelable.Creator<QualityTest>() {
        @Override
        public QualityTest createFromParcel(Parcel in) {
            return new QualityTest(in);
        }

        @Override
        public QualityTest[] newArray(int size) {
            return new QualityTest[size];
        }
    };
}