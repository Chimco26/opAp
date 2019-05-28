package com.operatorsapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.operatorsapp.R;
import com.operatorsapp.application.OperatorApplication;
import com.operatorsapp.managers.PersistenceManager;

public class JobListFragment extends Fragment {


    private static final String TAG = JobListFragment.class.getSimpleName();

    public static JobListFragment newInstance() {

        JobListFragment jobListFragment = new JobListFragment();
        return jobListFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Analytics
        OperatorApplication application = (OperatorApplication) getActivity().getApplication();
        Tracker mTracker = application.getDefaultTracker();
        PersistenceManager pm = PersistenceManager.getInstance();
        mTracker.setClientId("machine id: " + pm.getMachineId());
        mTracker.setAppVersion(pm.getVersion() + "");
        mTracker.setHostname(pm.getSiteName());
        mTracker.setScreenName(TAG);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_job_list, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

}
