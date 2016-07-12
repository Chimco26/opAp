package com.operatorsapp.model;

public class ShiftLog {

    private boolean mPriority;
    private String mTitle;
    private String mSubtitle;
    private int mType;
    private int mIcon;
    private String mTime;

    public boolean isPriority() {
        return mPriority;
    }

    public void setPriority(boolean mPriority) {
        this.mPriority = mPriority;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getSubtitle() {
        return mSubtitle;
    }

    public void setSubtitle(String mSubtitle) {
        this.mSubtitle = mSubtitle;
    }

    public int getType() {
        return mType;
    }

    public void setType(int mType) {
        this.mType = mType;
    }

    public int getIcon() {
        return mIcon;
    }

    public void setIcon(int mIcon) {
        this.mIcon = mIcon;
    }

    public String getTime() {
        return mTime;
    }

    public void setTime(String time) {
        this.mTime = time;
    }
}
