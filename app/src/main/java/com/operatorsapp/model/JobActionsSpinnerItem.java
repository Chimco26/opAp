package com.operatorsapp.model;

/**
 * Created by Oren on 1/30/2017.
 */
public class JobActionsSpinnerItem
{

    int mUniqueID;
    String mName;
    boolean mEnabled = true;
    // in the future icon will go here
    // TODO actually use this


    public JobActionsSpinnerItem(int uniqueID, String name)
    {
        mUniqueID = uniqueID;
        mName = name;
    }

    public int getUniqueID()
    {
        return mUniqueID;
    }

    public String getName()
    {
        return mName;
    }

    public boolean isEnabled()
    {
        return mEnabled;
    }

    public void setEnabled(boolean mEnabled)
    {
        this.mEnabled = mEnabled;
    }
}
