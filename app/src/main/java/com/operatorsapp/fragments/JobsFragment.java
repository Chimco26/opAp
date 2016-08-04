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
import com.operators.jobsinfra.Job;
import com.operators.jobsinfra.JobListForMachine;
import com.operatorsapp.R;
import com.operatorsapp.activities.interfaces.GoToScreenListener;
import com.operatorsapp.adapters.JobsRecyclerViewAdapter;
import com.operatorsapp.fragments.interfaces.OnCroutonRequestListener;
import com.operatorsapp.fragments.interfaces.OnJobSelectedCallbackListener;
import com.operatorsapp.interfaces.DashboardActivityToJobsFragmentCallback;
import com.operatorsapp.interfaces.JobsFragmentToDashboardActivityCallback;
import com.operatorsapp.managers.ProgressDialogManager;
import com.operatorsapp.utils.ShowCrouton;

import java.util.HashMap;
import java.util.List;


public class JobsFragment extends Fragment implements OnJobSelectedCallbackListener, DashboardActivityToJobsFragmentCallback, View.OnClickListener {
    private static final String LOG_TAG = JobsFragment.class.getSimpleName();

    public static final String SELECTED_JOB = "selected_job";
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
    public void onJobSelected(int position) {
        //TODO
//
//        Log.i(LOG_TAG, "onJobSelected(), Selected Job ID: " + mHeaderList.get(position).getJobId());
//        SelectedJobFragment selectedJobFragment = new SelectedJobFragment();
//        Bundle bundle = new Bundle();
//        Gson gson = new Gson();
//        Job job = mHeaderList.get(position);
//        String jobString = gson.toJson(job, Job.class);
//        bundle.putString(SELECTED_JOB, jobString);
//
//        selectedJobFragment.setArguments(bundle);
//        mOnGoToScreenListener.goToFragment(selectedJobFragment, true);
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
    }

    private void initHeaders() {
        int size = mHeaderList.size();
        if (size > 0) {

            switch (size) {
                case 1: {

                    mFirstHeader.setText(mHeaderList.get(0).getFieldName());
                    mSecondHeader.setText(R.string.dashes);
                    mThirdHeader.setText(R.string.dashes);
                    mFourthHeader.setText(R.string.dashes);
                    mFifthHeader.setText(R.string.dashes);
                    break;
                }
                case 2: {
                    mFirstHeader.setText(mHeaderList.get(0).getFieldName());
                    mSecondHeader.setText(mHeaderList.get(1).getFieldName());
                    mThirdHeader.setText(R.string.dashes);
                    mFourthHeader.setText(R.string.dashes);
                    mFifthHeader.setText(R.string.dashes);
                    break;
                }
                case 3: {
                    mFirstHeader.setText(mHeaderList.get(0).getFieldName());
                    mSecondHeader.setText(mHeaderList.get(1).getFieldName());
                    mThirdHeader.setText(mHeaderList.get(2).getFieldName());
                    mFourthHeader.setText(R.string.dashes);
                    mFifthHeader.setText(R.string.dashes);
                    break;
                }
                case 4: {
                    mFirstHeader.setText(mHeaderList.get(0).getFieldName());
                    mSecondHeader.setText(mHeaderList.get(1).getFieldName());
                    mThirdHeader.setText(mHeaderList.get(2).getFieldName());
                    mFourthHeader.setText(mHeaderList.get(3).getFieldName());
                    mFifthHeader.setText(R.string.dashes);
                    break;
                }
                case 5: {
                    mFirstHeader.setText(mHeaderList.get(0).getFieldName());
                    mSecondHeader.setText(mHeaderList.get(1).getFieldName());
                    mThirdHeader.setText(mHeaderList.get(2).getFieldName());
                    mFourthHeader.setText(mHeaderList.get(3).getFieldName());
                    mFifthHeader.setText(mHeaderList.get(4).getFieldName());
                    break;
                }
                default: {
                    mFirstHeader.setText(mHeaderList.get(0).getFieldName());
                    mSecondHeader.setText(mHeaderList.get(1).getFieldName());
                    mThirdHeader.setText(mHeaderList.get(2).getFieldName());
                    mFourthHeader.setText(mHeaderList.get(3).getFieldName());
                    mFifthHeader.setText(mHeaderList.get(4).getFieldName());
                }
            }
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
