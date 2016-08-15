package com.operatorsapp.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.operators.machinestatusinfra.models.MachineStatus;
import com.operators.reportfieldsformachineinfra.ReportFieldsForMachine;
import com.operatorsapp.R;
import com.operatorsapp.activities.interfaces.GoToScreenListener;
import com.operatorsapp.adapters.StopReasonsAdapter;
import com.operatorsapp.fragments.interfaces.OnCroutonRequestListener;
import com.operatorsapp.fragments.interfaces.OnStopReasonSelectedCallbackListener;
import com.operatorsapp.interfaces.ReportFieldsFragmentCallbackListener;
import com.operatorsapp.utils.TimeUtils;
import com.operatorsapp.view.GridSpacingItemDecoration;

/**
 * Created by Sergey on 08/08/2016.
 */
public class ReportStopReasonFragment extends Fragment implements OnStopReasonSelectedCallbackListener {
    private static final String LOG_TAG = ReportStopReasonFragment.class.getSimpleName();
    private static final String CURRENT_MACHINE_STATUS = "current_machine_status";
    private static final int NUMBER_OF_COLUMNS = 5;
    private static final String SELECTED_STOP_REASON_POSITION = "selected_stop_reason_position";
    private static final String CURRENT_JOB_ID = "current_job_id";
    private static final String END_TIME = "end_time";
    private static final String START_TIME = "start_time";
    private static final String DURATION = "duration";
    private static final String STOP_REPORT_EVENT_ID = "stop_report_event_id";

    private int mEventId;
    private String mStart;
    private String mEnd;
    private int mDuration;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private StopReasonsAdapter mStopReasonsAdapter;

    private MachineStatus mMachineStatus;
    private GoToScreenListener mGoToScreenListener;
    private OnCroutonRequestListener mOnCroutonRequestListener;
    private ReportFieldsFragmentCallbackListener mReportFieldsFragmentCallbackListener;
    private ReportFieldsForMachine mReportFieldsForMachine;

    private TextView mEventIdTextView;
    private TextView mProductTextView;
    private TextView mDurationTextView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mGoToScreenListener = (GoToScreenListener) getActivity();
        mReportFieldsFragmentCallbackListener = (ReportFieldsFragmentCallbackListener) getActivity();
        mReportFieldsForMachine = mReportFieldsFragmentCallbackListener.getReportForMachine();
        mOnCroutonRequestListener = (OnCroutonRequestListener) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_report_stop_reason, container, false);

        setActionBar();
        Bundle bundle = this.getArguments();
        Gson gson = new Gson();
        mMachineStatus = gson.fromJson(bundle.getString(CURRENT_MACHINE_STATUS), MachineStatus.class);

        mStart = bundle.getString(START_TIME);
        mEnd = bundle.getString(END_TIME);
        mDuration = bundle.getInt(DURATION);
        mEventId = bundle.getInt(STOP_REPORT_EVENT_ID);
        Log.i(LOG_TAG, "STart " + mStart + " end " + mEnd + " duration " + mDuration);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.stop_recycler_view);

        TextView jobIdTextView = (TextView) view.findViewById(R.id.report_job_spinner);
        if (mMachineStatus != null) {
            if (mMachineStatus.getAllMachinesData() != null) {
                jobIdTextView.setText((String.valueOf(mMachineStatus.getAllMachinesData().get(0).getCurrentJobID())));
            }
        }

        if (mReportFieldsForMachine == null || mReportFieldsForMachine.getStopReasons() == null || mReportFieldsForMachine.getStopReasons().size() == 0) {
            Log.i(LOG_TAG, "No Reasons in list");
//            ShowCrouton.reportStopCrouton(mOnCroutonRequestListener); //TODO Check place
        }
        else {
            mLayoutManager = new GridLayoutManager(getContext(), NUMBER_OF_COLUMNS);
            mRecyclerView.setLayoutManager(mLayoutManager);
            int spacing = 40;
            mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(NUMBER_OF_COLUMNS, spacing, true, 0));
            initStopReasons();
        }

        mEventIdTextView = (TextView) view.findViewById(R.id.date_text_view);
        mProductTextView = (TextView) view.findViewById(R.id.prodct_Text_View);
        mDurationTextView = (TextView) view.findViewById(R.id.duration_text_view);

        if (mStart == null || mEnd == null) {
            mProductTextView.setText("- -");
        }
        else {
            mProductTextView.setText(getActivity().getString(R.string.stop) + TimeUtils.getTimeFromString(mStart) + ", Resume " + TimeUtils.getTimeFromString(mEnd));
        }

        mDurationTextView.setText(TimeUtils.secondsToTimeFormat(mDuration));
        mEventIdTextView.setText(String.valueOf(mEventId));


    }

    private void initStopReasons() {
        if (mReportFieldsForMachine != null) {
            mStopReasonsAdapter = new StopReasonsAdapter(getContext(), mReportFieldsForMachine.getStopReasons(), this);
            mRecyclerView.setAdapter(mStopReasonsAdapter);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
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
            View view = inflater.inflate(R.layout.action_bar_report_stop, null);

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
    public void onStopReasonSelected(int position) {
        SelectedStopReasonFragment selectedStopReasonFragment = new SelectedStopReasonFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(SELECTED_STOP_REASON_POSITION, position);
        bundle.putInt(CURRENT_JOB_ID, mMachineStatus.getAllMachinesData().get(0).getCurrentJobID());
        bundle.putString(END_TIME, mEnd);
        bundle.putString(START_TIME, mStart);
        bundle.putInt(DURATION, mDuration);
        bundle.putInt(STOP_REPORT_EVENT_ID, mEventId);
        selectedStopReasonFragment.setArguments(bundle);
        mGoToScreenListener.goToFragment(selectedStopReasonFragment, true);

    }
}
