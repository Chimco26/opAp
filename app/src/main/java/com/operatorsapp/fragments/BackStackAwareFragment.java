package com.operatorsapp.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

/**
 * Created by Oren on 2/7/2017.
 * used to allow fragment to update the action bar when coming back from back stack because android is fun like that
 */
public abstract class BackStackAwareFragment extends Fragment implements FragmentManager.OnBackStackChangedListener
{

    protected int m_initialBackStackCount = 0;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        if (getFragmentManager() != null) {
            getFragmentManager().addOnBackStackChangedListener(this);
        }
        m_initialBackStackCount = getFragmentManager().getBackStackEntryCount() + 1;
    }

    @Override
    public void onBackStackChanged()
    {
        int count = 0;
        if (getFragmentManager() != null) {
            count = getFragmentManager().getBackStackEntryCount();
        }
        if(count == m_initialBackStackCount)
        {
            setActionBar();
        }
    }

    protected abstract void setActionBar();

    protected void removeBackStackListener()
    {
        FragmentManager fragmentManager = getFragmentManager();
        if(fragmentManager != null)
        {
            fragmentManager.removeOnBackStackChangedListener(this);
        }
    }

    @Override
    public void onStop()
    {
        super.onStop();
        removeBackStackListener();
    }

}
