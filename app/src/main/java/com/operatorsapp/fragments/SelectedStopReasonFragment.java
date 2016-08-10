package com.operatorsapp.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.operators.reportfieldsformachineinfra.ReportFieldsForMachine;
import com.operators.reportfieldsformachineinfra.SubReasons;
import com.operators.reportrejectcore.ReportCallbackListener;
import com.operators.reportrejectcore.ReportRejectCore;
import com.operators.reportrejectinfra.ErrorObjectInterface;
import com.operators.reportrejectnetworkbridge.ReportRejectNetworkBridge;
import com.operatorsapp.R;
import com.operatorsapp.activities.DashboardActivity;
import com.operatorsapp.activities.interfaces.SilentLoginCallback;
import com.operatorsapp.adapters.StopSubReasonAdapter;
import com.operatorsapp.fragments.interfaces.OnCroutonRequestListener;
import com.operatorsapp.fragments.interfaces.OnSelectedSubReasonListener;
import com.operatorsapp.interfaces.ReportFieldsFragmentCallbackListener;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.server.NetworkManager;
import com.operatorsapp.utils.ShowCrouton;
import com.operatorsapp.view.GridSpacingItemDecoration;

import java.util.List;

/**
 * Created by Sergey on 09/08/2016.
 */
public class SelectedStopReasonFragment extends Fragment implements OnSelectedSubReasonListener, View.OnClickListener {

    public static final String LOG_TAG = SelectedStopReasonFragment.class.getSimpleName();
    private static final String SELECTED_STOP_REASON_POSITION = "selected_stop_reason_position";
    private static final String CURRENT_JOB_ID = "current_job_id";
    private static final int NUMBER_OF_COLUMNS = 5;

    private int mSelectedPosition;
    private ReportFieldsFragmentCallbackListener mReportFieldsFragmentCallbackListener;
    private ReportFieldsForMachine mReportFieldsForMachine;
    private int mCurrentJobId;
    private List<SubReasons> mSubReasonsList;
    private int mSelectedSubreasonId = -1;
    private int mSelectedReason;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private StopSubReasonAdapter mStopReasonsAdapter;
    private OnCroutonRequestListener mOnCroutonRequestListener;
    private ReportRejectCore mReportRejectCore;


    private Button mButtonNext;
    private TextView mButtonCancel;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mReportFieldsFragmentCallbackListener = (ReportFieldsFragmentCallbackListener) getActivity();
        mReportFieldsForMachine = mReportFieldsFragmentCallbackListener.getReportForMachine();
        mOnCroutonRequestListener = (OnCroutonRequestListener) getActivity();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_selected_stop_report, container, false);
        Bundle bundle = this.getArguments();
        mSelectedPosition = bundle.getInt(SELECTED_STOP_REASON_POSITION);
        mCurrentJobId = bundle.getInt(CURRENT_JOB_ID);
        mSelectedReason = mReportFieldsForMachine.getStopReasons().get(mSelectedPosition).getId();
        setActionBar();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView jobIdTextView = (TextView) view.findViewById(R.id.report_rejects_job_id__text_view);
        jobIdTextView.setText((String.valueOf(mCurrentJobId)));

        mButtonNext = (Button) view.findViewById(R.id.button_next);

        mButtonCancel = (TextView) view.findViewById(R.id.button_cancel);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.selected_stop_recycler_view);
        mLayoutManager = new GridLayoutManager(getContext(), NUMBER_OF_COLUMNS);
        mRecyclerView.setLayoutManager(mLayoutManager);
        int spacing = 30;
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(NUMBER_OF_COLUMNS, spacing, true, 0));

        initSubReasons();
    }

    private void initSubReasons() {
        if (mReportFieldsForMachine != null) {
            mStopReasonsAdapter = new StopSubReasonAdapter(this, getContext(), mReportFieldsForMachine.getStopReasons().get(mSelectedPosition).getSubReasons());
            mRecyclerView.setAdapter(mStopReasonsAdapter);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mButtonCancel.setOnClickListener(null);
        mButtonNext.setOnClickListener(null);
    }

    @Override
    public void onResume() {
        super.onResume();
        mButtonCancel.setOnClickListener(this);
        mButtonNext.setOnClickListener(this);
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
    public void onSubReasonSelected(int subReasonId) {
        mButtonNext.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.buttons_selector));
        Log.i(LOG_TAG, "Selected sub reason id: " + subReasonId);
        mSelectedSubreasonId = subReasonId;
        mStopReasonsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_next: {
                if (mSelectedSubreasonId != -1) {
                    sendReport();
                }
                break;
            }
            case R.id.button_cancel: {
                getFragmentManager().popBackStack(null, android.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);
                break;
            }
        }
    }

    private void sendReport() {

        ReportRejectNetworkBridge reportRejectNetworkBridge = new ReportRejectNetworkBridge();
        reportRejectNetworkBridge.inject(NetworkManager.getInstance(), NetworkManager.getInstance());
        mReportRejectCore = new ReportRejectCore(reportRejectNetworkBridge, PersistenceManager.getInstance());
        mReportRejectCore.registerListener(mReportCallbackListener);
        mReportRejectCore.sendStopReport(mSelectedReason, mSelectedSubreasonId);
    }

    ReportCallbackListener mReportCallbackListener = new ReportCallbackListener() {
        @Override
        public void sendReportSuccess() {
            Log.i(LOG_TAG, "sendReportSuccess()");
            mReportRejectCore.unregisterListener();
            //TODO check
            getFragmentManager().popBackStack(null, android.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        @Override
        public void sendReportFailure(ErrorObjectInterface reason) {
            Log.w(LOG_TAG, "sendReportFailure()");
            if (reason.getError() == ErrorObjectInterface.ErrorCode.Credentials_mismatch) {
                ((DashboardActivity) getActivity()).silentLoginFromDashBoard(mOnCroutonRequestListener, new SilentLoginCallback() {
                    @Override
                    public void onSilentLoginSucceeded() {
                        sendReport();
                    }

                    @Override
                    public void onSilentLoginFailed(com.operators.infra.ErrorObjectInterface reason) {
                        Log.w(LOG_TAG, "Failed silent login");
                        ShowCrouton.reportRejectCrouton(mOnCroutonRequestListener);
                    }
                });
            }
            else {

                ShowCrouton.reportRejectCrouton(mOnCroutonRequestListener);
            }
        }
    };
}
