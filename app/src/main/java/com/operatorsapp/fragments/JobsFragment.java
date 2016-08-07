package com.operatorsapp.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.operators.jobsinfra.Header;
import com.operators.jobsinfra.JobListForMachine;
import com.operatorsapp.R;
import com.operatorsapp.activities.interfaces.GoToScreenListener;
import com.operatorsapp.adapters.JobsRecyclerViewAdapter;
import com.operatorsapp.fragments.interfaces.OnCroutonRequestListener;
import com.operatorsapp.fragments.interfaces.OnJobSelectedCallbackListener;
import com.operatorsapp.interfaces.DashboardActivityToJobsFragmentCallback;
import com.operatorsapp.interfaces.JobsFragmentToDashboardActivityCallback;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.managers.ProgressDialogManager;
import com.operatorsapp.utils.ShowCrouton;

import java.util.HashMap;
import java.util.List;


public class JobsFragment extends Fragment implements OnJobSelectedCallbackListener, DashboardActivityToJobsFragmentCallback, View.OnClickListener {
    private static final String LOG_TAG = JobsFragment.class.getSimpleName();


    private static final String SELECTED_JOB_TITLES = "selected_job_titles";
    private static final String SELECTED_JOB_DATA_ARRAY = "selected_job_data_array";
    private static final String SELECTED_JOB_ID = "selected_job_id";
    private RecyclerView mJobsRecyclerView;
    private FrameLayout mErrorFrameLayout;
    private Button mRetryButton;
    private RecyclerView.LayoutManager mLayoutManager;
    private JobsRecyclerViewAdapter mJobsRecyclerViewAdapter;
    private List<Header> mHeaderList;
    private List<HashMap<String, Object>> mJobsDataList;

    private GoToScreenListener mOnGoToScreenListener;
    private OnCroutonRequestListener mOnCroutonRequestListener;
    private JobsFragmentToDashboardActivityCallback mJobsFragmentToDashboardActivityCallback;

    private TextView mFirstHeader;
    private TextView mSecondHeader;
    private TextView mThirdHeader;
    private TextView mFourthHeader;
    private TextView mFifthHeader;

    private String[] mFieldsValues = new String[5];

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mOnGoToScreenListener = (GoToScreenListener) getActivity();
            mOnCroutonRequestListener = (OnCroutonRequestListener) getActivity();
            mJobsFragmentToDashboardActivityCallback = (JobsFragmentToDashboardActivityCallback) getActivity();
            mJobsFragmentToDashboardActivityCallback.onJobFragmentAttached(this);
            mJobsFragmentToDashboardActivityCallback.initJobsCore();
        }
        catch (ClassCastException e) {
            throw new ClassCastException("Calling fragment must implement required interface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mJobsFragmentToDashboardActivityCallback.unregisterListeners();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_jobs, container, false);
        setActionBar();
        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        mRetryButton.setOnClickListener(null);
    }

    @Override
    public void onResume() {
        super.onResume();
        mRetryButton.setOnClickListener(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mJobsRecyclerView = (RecyclerView) view.findViewById(R.id.job_recycler_view);
        mErrorFrameLayout = (FrameLayout) view.findViewById(R.id.error_job_frame_layout);

        mFirstHeader = (TextView) view.findViewById(R.id.first_header_text_view);
        mSecondHeader = (TextView) view.findViewById(R.id.second_header_text_view);
        mThirdHeader = (TextView) view.findViewById(R.id.third_header_text_view);
        mFourthHeader = (TextView) view.findViewById(R.id.fourth_header_text_view);
        mFifthHeader = (TextView) view.findViewById(R.id.fifth_header_text_view);

        mRetryButton = (Button) view.findViewById(R.id.button_retry);
        mLayoutManager = new LinearLayoutManager(getContext());
        mJobsRecyclerView.setLayoutManager(mLayoutManager);
        mJobsFragmentToDashboardActivityCallback.getJobsForMachineList();


    }

    private void setActionBar() {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayUseLogoEnabled(true);
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            // rootView null
            @SuppressLint("InflateParams")
            View view = inflater.inflate(R.layout.jobs_fragment_action_bar, null);

            ImageView buttonClose = (ImageView) view.findViewById(R.id.close_image);
            buttonClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getFragmentManager().popBackStack();
                }
            });
            actionBar.setCustomView(view);
        }
    }

    @Override
    public void onJobSelected(String[] jobDataArray, int jobId) {
        SelectedJobFragment selectedJobFragment = new SelectedJobFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArray(SELECTED_JOB_TITLES, mFieldsValues);
        bundle.putStringArray(SELECTED_JOB_DATA_ARRAY, jobDataArray);
        bundle.putInt(SELECTED_JOB_ID, jobId);

        selectedJobFragment.setArguments(bundle);
        mOnGoToScreenListener.goToFragment(selectedJobFragment, true);
    }

    @Override
    public void onJobReceiveFailed() {
        dismissProgressDialog();
        Log.i(LOG_TAG, "onJobReceiveFailed()");
        mErrorFrameLayout.setVisibility(View.VISIBLE);
        ShowCrouton.jobsLoadingErrorCrouton(mOnCroutonRequestListener);
    }

    @Override
    public void onJobReceived(JobListForMachine jobListForMachine) {
        dismissProgressDialog();
        mErrorFrameLayout.setVisibility(View.GONE);
        mHeaderList = jobListForMachine.getHeaders();
        mJobsDataList = jobListForMachine.getData();
        jobListForMachine.getData();
        initHeaders();
        mJobsRecyclerViewAdapter = new JobsRecyclerViewAdapter(this, mHeaderList, mJobsDataList);
        mJobsRecyclerView.setAdapter(mJobsRecyclerViewAdapter);

        Log.i(LOG_TAG, "Session id" + " = " + PersistenceManager.getInstance().getSessionId() + " list size " + " = " + mJobsDataList.size() + " machine id = " + PersistenceManager.getInstance().getMachineId());

    }

    private void initTitles(String text1, String text2, String text3, String text4, String text5) {
        mFirstHeader.setText(mHeaderList.get(0).getFieldName());
        mFirstHeader.setText(text1);
        mSecondHeader.setText(text2);
        mThirdHeader.setText(text3);
        mFourthHeader.setText(text4);
        mFifthHeader.setText(text5);
    }

    private void initHeaders() {
        if (mHeaderList != null) {
            if (mHeaderList.size() > 0) {

                for (int i = 0; i < 5; i++) {
                    if (i < mHeaderList.size()) {
                        if (mHeaderList.get(i).getFieldName() == null || mHeaderList.get(i) == null || TextUtils.isEmpty(mHeaderList.get(i).getFieldName())) {
                            mFieldsValues[i] = "- -";
                        }
                        else {
                            mFieldsValues[i] = mHeaderList.get(i).getFieldName();
                        }
                    }
                    else {
                        mFieldsValues[i] = "- -";
                    }
                }
                initTitles(mFieldsValues[0], mFieldsValues[1], mFieldsValues[2], mFieldsValues[3], mFieldsValues[4]);
            }
        }
        else {
            Log.w(LOG_TAG, "mHeaderList is null");
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_retry: {
                //Start Progress
                ProgressDialogManager.show(getActivity());
                mJobsFragmentToDashboardActivityCallback.getJobsForMachineList();
                break;
            }
        }
    }

    private void dismissProgressDialog() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ProgressDialogManager.dismiss();
            }
        });
    }
}
