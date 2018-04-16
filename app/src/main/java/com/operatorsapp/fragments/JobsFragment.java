package com.operatorsapp.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.operators.jobsinfra.Header;
import com.operators.jobsinfra.JobListForMachine;
import com.operatorsapp.R;
import com.operatorsapp.activities.interfaces.GoToScreenListener;
import com.operatorsapp.adapters.JobsRecyclerViewAdapter;
import com.operatorsapp.application.OperatorApplication;
import com.operatorsapp.fragments.interfaces.OnCroutonRequestListener;
import com.operatorsapp.fragments.interfaces.OnJobSelectedCallbackListener;
import com.operatorsapp.interfaces.CroutonRootProvider;
import com.operatorsapp.interfaces.DashboardActivityToJobsFragmentCallback;
import com.operatorsapp.interfaces.JobsFragmentToDashboardActivityCallback;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.managers.ProgressDialogManager;
import com.operatorsapp.model.CurrentJob;
import com.operatorsapp.utils.KeyboardUtils;
import com.operatorsapp.utils.ShowCrouton;
import com.zemingo.logrecorder.ZLogger;

import java.util.HashMap;
import java.util.List;


public class JobsFragment extends BackStackAwareFragment implements OnJobSelectedCallbackListener, DashboardActivityToJobsFragmentCallback, View.OnClickListener, CroutonRootProvider, TextWatcher {
    private static final String LOG_TAG = JobsFragment.class.getSimpleName();
    private static final String SELECTED_JOB = "selected_job";
    private RecyclerView mJobsRecyclerView;
    private FrameLayout mErrorFrameLayout;
    private Button mRetryButton;
    private List<Header> mHeaderList;

    private GoToScreenListener mOnGoToScreenListener;
    private OnCroutonRequestListener mOnCroutonRequestListener;
    private JobsFragmentToDashboardActivityCallback mJobsFragmentToDashboardActivityCallback;

    private TextView mFirstHeader;
    private TextView mSecondHeader;
    private TextView mThirdHeader;
    private TextView mFourthHeader;
    private TextView mFifthHeader;

    private EditText mFirstFilter;
    private EditText mSecondFilter;
    private EditText mThirdFilter;
    private EditText mFourthFilter;
    private EditText mFifthFilter;

    private String[] mFieldsValues = new String[5];
    public JobsRecyclerViewAdapter mJobsRecyclerViewAdapter;

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
        } catch (ClassCastException e) {
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
        ProgressDialogManager.show(getActivity());
        super.onViewCreated(view, savedInstanceState);
        mJobsRecyclerView = (RecyclerView) view.findViewById(R.id.job_recycler_view);
        mErrorFrameLayout = (FrameLayout) view.findViewById(R.id.error_job_frame_layout);

        mFirstHeader = (TextView) view.findViewById(R.id.first_header_text_view);
        mSecondHeader = (TextView) view.findViewById(R.id.second_header_text_view);
        mThirdHeader = (TextView) view.findViewById(R.id.third_header_text_view);
        mFourthHeader = (TextView) view.findViewById(R.id.fourth_header_text_view);
        mFifthHeader = (TextView) view.findViewById(R.id.fifth_header_text_view);

        mFirstFilter = (EditText) view.findViewById(R.id.FJ_ET_first_header);
        mSecondFilter = (EditText) view.findViewById(R.id.FJ_ET_second_header);
        mThirdFilter = (EditText) view.findViewById(R.id.FJ_ET_third_header);
        mFourthFilter = (EditText) view.findViewById(R.id.FJ_ET_fourth_header);
        mFifthFilter = (EditText) view.findViewById(R.id.FJ_ET_fifth_header);

        mRetryButton = (Button) view.findViewById(R.id.button_retry);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mJobsRecyclerView.setLayoutManager(mLayoutManager);
        mJobsFragmentToDashboardActivityCallback.getJobsForMachineList();
        mJobsFragmentToDashboardActivityCallback.updateReportRejectFields();

        view.findViewById(R.id.FJ_clear_filters).setOnClickListener(this);


        ZLogger.i(LOG_TAG, "SessionId : " + PersistenceManager.getInstance().getSessionId() + " machineId: " + PersistenceManager.getInstance().getMachineId());


    }

    protected void setActionBar() {
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

            LinearLayout buttonClose = (LinearLayout) view.findViewById(R.id.close_image);
            buttonClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    KeyboardUtils.closeKeyboard(getContext());
                    FragmentManager fragmentManager = getFragmentManager();
                    if (fragmentManager != null) {
                        fragmentManager.popBackStack();
                    }
                }
            });
            actionBar.setCustomView(view);
        }
    }

    @Override
    public void onJobSelected(CurrentJob currentJob) {

        KeyboardUtils.closeKeyboard(getActivity());
        SelectedJobFragment selectedJobFragment = new SelectedJobFragment();
        Bundle bundle = new Bundle();
        Gson gson = new Gson();
        String jobString = gson.toJson(currentJob, CurrentJob.class);
        bundle.putString(SELECTED_JOB, jobString);
        selectedJobFragment.setArguments(bundle);
        mOnGoToScreenListener.goToFragment(selectedJobFragment, true);
    }

    @Override
    public void onJobsListReceiveFailed() {
        dismissProgressDialog();
        ZLogger.i(LOG_TAG, "onJobsListReceiveFailed()");
        mErrorFrameLayout.setVisibility(View.VISIBLE);
        ShowCrouton.jobsLoadingErrorCrouton(mOnCroutonRequestListener);
    }

    @Override
    public void onJobsListReceived(JobListForMachine jobListForMachine) {
        dismissProgressDialog();
        mErrorFrameLayout.setVisibility(View.GONE);
        mHeaderList = jobListForMachine.getHeaders();
        List<HashMap<String, Object>> mJobsDataList = jobListForMachine.getData();
        jobListForMachine.getData();
        initHeaders();
        mJobsRecyclerViewAdapter = new JobsRecyclerViewAdapter(this, mHeaderList, mJobsDataList);
        mJobsRecyclerView.setAdapter(mJobsRecyclerViewAdapter);
        initTextWatcherListener();

        ZLogger.i(LOG_TAG, "Session id" + " = " + PersistenceManager.getInstance().getSessionId() + " list size " + " = " + mJobsDataList.size() + " machine id = " + PersistenceManager.getInstance().getMachineId());

    }

    private void initTextWatcherListener() {

        mFirstFilter.addTextChangedListener(this);

        mSecondFilter.addTextChangedListener(this);

        mThirdFilter.addTextChangedListener(this);

        mFourthFilter.addTextChangedListener(this);

        mFifthFilter.addTextChangedListener(this);

    }

    private void initTitles(String text1, String text2, String text3, String text4, String text5) {
        mFirstHeader.setText(text1);
        mFirstFilter.setHint(text1);
        mSecondHeader.setText(text2);
        mSecondFilter.setHint(text2);
        mThirdHeader.setText(text3);
        mThirdFilter.setHint(text3);
        mFourthHeader.setText(text4);
        mFourthFilter.setHint(text4);
        mFifthHeader.setText(text5);
        mFifthFilter.setHint(text5);
    }

    private void initHeaders() {
        if (mHeaderList != null) {
            if (mHeaderList.size() > 0) {

                for (int i = 0; i < 5; i++) {
                    if (i < mHeaderList.size()) {

                        String headerName = null;
                        if (mHeaderList != null && mHeaderList.get(i) != null) {
                            headerName = OperatorApplication.isEnglishLang() ? mHeaderList.get(i).getDisplayEName() : mHeaderList.get(i).getDisplayHName();
                        }
                        if (TextUtils.isEmpty(headerName)) {
                            mFieldsValues[i] = "- -";
                        } else {
                            mFieldsValues[i] = headerName;
                        }
                    } else {
                        mFieldsValues[i] = "- -";
                    }
                }
                initTitles(mFieldsValues[0], mFieldsValues[1], mFieldsValues[2], mFieldsValues[3], mFieldsValues[4]);
            }
        } else {
            ZLogger.w(LOG_TAG, "mHeaderList is null");
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_retry:
                //Start Progress
                ProgressDialogManager.show(getActivity());
                mJobsFragmentToDashboardActivityCallback.getJobsForMachineList();
                break;


            case R.id.FJ_clear_filters:

                clearTextFromFilters();

                break;
        }
    }

    private void clearTextFromFilters() {

        mFirstFilter.setText("");
        mSecondFilter.setText("");
        mThirdFilter.setText("");
        mFourthFilter.setText("");
        mFifthFilter.setText("");


    }

    private void dismissProgressDialog() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ProgressDialogManager.dismiss();
            }
        });
    }

    @Override
    public int getCroutonRoot() {
        return R.id.jobs_list_fragment;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mJobsRecyclerViewAdapter.getFilter().filter(mFirstFilter.getText().toString()
                + ",#@#" + mSecondFilter.getText().toString()
                + ",#@#" + mThirdFilter.getText().toString()
                + ",#@#" + mFourthFilter.getText().toString()
                + ",#@#" + mFifthFilter.getText().toString() + ","
        );
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
