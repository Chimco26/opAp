package com.operatorsapp.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.operators.errorobject.ErrorObjectInterface;
import com.operators.getmachinesnetworkbridge.server.ErrorObject;
import com.operators.reportfieldsformachineinfra.ReportFieldsForMachine;
import com.operators.reportrejectcore.ReportCallbackListener;
import com.operators.reportrejectcore.ReportCore;
import com.operators.reportrejectnetworkbridge.ReportNetworkBridge;
import com.operatorsapp.R;
import com.operatorsapp.activities.DashboardActivity;
import com.operatorsapp.activities.interfaces.SilentLoginCallback;
import com.operatorsapp.adapters.StopSubReasonAdapter;
import com.operatorsapp.application.OperatorApplication;
import com.operatorsapp.fragments.interfaces.OnCroutonRequestListener;
import com.operatorsapp.fragments.interfaces.OnSelectedSubReasonListener;
import com.operatorsapp.interfaces.CroutonRootProvider;
import com.operatorsapp.interfaces.ReportFieldsFragmentCallbackListener;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.managers.ProgressDialogManager;
import com.operatorsapp.server.NetworkManager;
import com.operatorsapp.utils.DavidVardi;
import com.operatorsapp.utils.SendReportUtil;
import com.operatorsapp.utils.ShowCrouton;
import com.operatorsapp.utils.TimeUtils;
import com.operatorsapp.utils.broadcast.SendBroadcast;
import com.operatorsapp.view.GridSpacingItemDecoration;
import com.operatorsapp.view.GridSpacingItemDecorationRTL;
import com.ravtech.david.sqlcore.Event;
import com.zemingo.logrecorder.ZLogger;

import java.util.ArrayList;

import static com.operatorsapp.utils.broadcast.SelectStopReasonBroadcast.EN_NAME;
import static com.operatorsapp.utils.broadcast.SelectStopReasonBroadcast.IL_NAME;
import static com.operatorsapp.utils.broadcast.SelectStopReasonBroadcast.REASON_ID;

public class SelectStopReasonFragmentNew extends BackStackAwareFragment implements OnSelectedSubReasonListener, View.OnClickListener, CroutonRootProvider {

    public static final String LOG_TAG = SelectStopReasonFragmentNew.class.getSimpleName();
    private static final String SELECTED_STOP_REASON_POSITION = "selected_stop_reason_position";
    private static final String CURRENT_JOB_ID = "current_job_id";

    private static final int NUMBER_OF_COLUMNS = 5;
    public static final String SAMSUNG = "samsung";

    private ReportFieldsForMachine mReportFieldsForMachine;
    private Integer mJobId = 0;

    private int mSelectedSubreasonId = -1;
    private int mSelectedReason;

    private RecyclerView mRecyclerView;
    private StopSubReasonAdapter mStopReasonsAdapter;
    private OnCroutonRequestListener mOnCroutonRequestListener;
    private ReportCore mReportCore;

    private ArrayList<Event> mSelectedEvents;
    private int mSelectedPosition;
    private int mReasonId;
    private String mEnName;
    private String mILName;

    public static SelectStopReasonFragmentNew newInstance(int position, int jobId, int reasonId, String eName, String lName) {
        SelectStopReasonFragmentNew selectedStopReasonFragment = new SelectStopReasonFragmentNew();
        Bundle bundle = new Bundle();
        bundle.putInt(SELECTED_STOP_REASON_POSITION, position);
        bundle.putInt(CURRENT_JOB_ID, jobId);
        bundle.putString(EN_NAME, eName);
        bundle.putString(IL_NAME, lName);
        bundle.putInt(REASON_ID, reasonId);
        selectedStopReasonFragment.setArguments(bundle);
        return selectedStopReasonFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSelectedPosition = getArguments().getInt(SELECTED_STOP_REASON_POSITION);
            mJobId = getArguments().getInt(CURRENT_JOB_ID);
            mReasonId = getArguments().getInt(REASON_ID);
            mEnName = getArguments().getString(EN_NAME);
            mILName = getArguments().getString(IL_NAME);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ReportFieldsFragmentCallbackListener reportFieldsFragmentCallbackListener = (ReportFieldsFragmentCallbackListener) getActivity();
        mReportFieldsForMachine = reportFieldsFragmentCallbackListener.getReportForMachine();
        mOnCroutonRequestListener = (OnCroutonRequestListener) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mReportFieldsForMachine = null;

        mOnCroutonRequestListener =null;
    }

    public void setSelectedEvents(ArrayList<Event> selectedEvents) {

        mSelectedEvents = selectedEvents;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_select_stop_reason_new, container, false);
        mSelectedReason = mReportFieldsForMachine.getStopReasons().get(mSelectedPosition).getId();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.selected_stop_recycler_view);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), NUMBER_OF_COLUMNS);
        mRecyclerView.setLayoutManager(layoutManager);
        int spacing;
        String strManufacturer = android.os.Build.MANUFACTURER;

        if (strManufacturer.equals(SAMSUNG)) {
            spacing = 80;
        } else {
            spacing = 40;
        }
        Configuration config = getResources().getConfiguration();
        if (config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
            mRecyclerView.addItemDecoration(new GridSpacingItemDecorationRTL(NUMBER_OF_COLUMNS, spacing, true, 0));
        } else {

            mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(NUMBER_OF_COLUMNS, spacing, true, 0));
        }


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
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    protected void setActionBar() {

    }

    @Override
    public void onSubReasonSelected(int subReasonId) {

        ZLogger.i(LOG_TAG, "Selected sub reason id: " + subReasonId);

        mSelectedSubreasonId = subReasonId;

        mStopReasonsAdapter.notifyDataSetChanged();

        sendReport();

        SendBroadcast.refreshPolling(getContext());
    }

    @Override
    public void onClick(View v) {

    }

    private void sendReport() {

        if (mSelectedEvents != null && mSelectedEvents.size() > 0) {

            ProgressDialogManager.show(getActivity());

            ReportNetworkBridge reportNetworkBridge = new ReportNetworkBridge();

            reportNetworkBridge.inject(NetworkManager.getInstance(), NetworkManager.getInstance());

            mReportCore = new ReportCore(reportNetworkBridge, PersistenceManager.getInstance());

            mReportCore.registerListener(mReportCallbackListener);

            for (Event event: mSelectedEvents) {

                mReportCore.sendStopReport(mSelectedReason, mSelectedSubreasonId, event.getEventID(), mJobId);

                SendBroadcast.sendReason(getContext(), event.getEventID(), mReasonId, mEnName, mILName);

            }
        }
    }

    ReportCallbackListener mReportCallbackListener = new ReportCallbackListener() {
        @Override
        public void sendReportSuccess() {
            dismissProgressDialog();
            ZLogger.i(LOG_TAG, "sendReportSuccess()");
            Log.d(DavidVardi.DAVID_TAG_SPRINT_1_5, "sendReportSuccess");

            try {

                mReportCore.unregisterListener();

                getFragmentManager().popBackStack(null, android.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);

                getActivity().onBackPressed();

            } catch (NullPointerException e) {

                if (getFragmentManager() == null)
                    SendReportUtil.sendAcraExeption(e,"mReportCallbackListener getFragmentManager = null");

                if (mReportCore == null)
                    SendReportUtil.sendAcraExeption(e,"mReportCallbackListener mReportCore = null");
            }
        }

        @Override
        public void sendReportFailure(ErrorObjectInterface reason) {
            dismissProgressDialog();
            ZLogger.w(LOG_TAG, "sendReportFailure()");
            if (reason.getError() == ErrorObjectInterface.ErrorCode.Credentials_mismatch) {
                ((DashboardActivity) getActivity()).silentLoginFromDashBoard(mOnCroutonRequestListener, new SilentLoginCallback() {
                    @Override
                    public void onSilentLoginSucceeded() {
                        sendReport();
                    }

                    @Override
                    public void onSilentLoginFailed(ErrorObjectInterface reason) {
                        ZLogger.w(LOG_TAG, "Failed silent login");
                        ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Missing_reports, "missing reports");
                        ShowCrouton.jobsLoadingErrorCrouton(mOnCroutonRequestListener, errorObject);
                    }
                });
            } else {

                ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Missing_reports, "missing reports");
                ShowCrouton.jobsLoadingErrorCrouton(mOnCroutonRequestListener, errorObject);
            }
        }
    };

    private void dismissProgressDialog() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ProgressDialogManager.dismiss();
                }
            });
        }
    }

    @Override
    public int getCroutonRoot() {
        return R.id.selected_stop_crouton_root;
    }
}
