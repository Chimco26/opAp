package com.operatorsapp.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.operators.errorobject.ErrorObjectInterface;
import com.operators.getmachinesnetworkbridge.server.ErrorObject;
import com.operators.reportfieldsformachineinfra.ReportFieldsForMachine;
import com.operators.reportfieldsformachineinfra.SubReasons;
import com.operators.reportrejectcore.ReportCallbackListener;
import com.operators.reportrejectcore.ReportCore;
import com.operators.reportrejectnetworkbridge.ReportNetworkBridge;
import com.operatorsapp.R;
import com.operatorsapp.activities.DashboardActivity;
import com.operatorsapp.activities.interfaces.SilentLoginCallback;
import com.operatorsapp.adapters.StopSubReasonAdapter;
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
import com.operatorsapp.utils.broadcast.SendBroadcast;
import com.operatorsapp.view.GridSpacingItemDecoration;
import com.operatorsapp.view.GridSpacingItemDecorationRTL;
import com.zemingo.logrecorder.ZLogger;

import java.util.ArrayList;

import static com.operatorsapp.utils.broadcast.SelectStopReasonBroadcast.EN_NAME;
import static com.operatorsapp.utils.broadcast.SelectStopReasonBroadcast.IL_NAME;
import static com.operatorsapp.utils.broadcast.SelectStopReasonBroadcast.REASON_ID;

public class SelectStopReasonFragmentNew extends BackStackAwareFragment implements OnSelectedSubReasonListener, View.OnClickListener, CroutonRootProvider {

    public static final String LOG_TAG = SelectStopReasonFragmentNew.class.getSimpleName();
    private static final String SELECTED_STOP_REASON_POSITION = "selected_stop_reason_position";
    private static final String CURRENT_JOB_ID = "current_job_id";
    private static final String IS_OPEN = "IS_OPEN";

    private static final int NUMBER_OF_COLUMNS = 5;
    public static final String SAMSUNG = "samsung";

    private ReportFieldsForMachine mReportFieldsForMachine;
    private Integer mJobId = 0;

    private SubReasons mSelectedSubreason;
    private int mSelectedReason;

    private RecyclerView mRecyclerView;
    private StopSubReasonAdapter mStopReasonsAdapter;
    private OnCroutonRequestListener mOnCroutonRequestListener;
    private ReportCore mReportCore;

    private ArrayList<Integer> mSelectedEvents;
    private int mSelectedPosition;
    private int mReasonId;
    private String mEnName;
    private String mILName;
    private GridLayoutManager mGridLayoutManager;
    private boolean mIsOpen;

    public static SelectStopReasonFragmentNew newInstance(int position, int jobId, int reasonId, String eName, String lName, boolean isOpen) {
        SelectStopReasonFragmentNew selectedStopReasonFragment = new SelectStopReasonFragmentNew();
        Bundle bundle = new Bundle();
        bundle.putInt(SELECTED_STOP_REASON_POSITION, position);
        bundle.putInt(CURRENT_JOB_ID, jobId);
        bundle.putString(EN_NAME, eName);
        bundle.putString(IL_NAME, lName);
        bundle.putInt(REASON_ID, reasonId);
        bundle.putBoolean(IS_OPEN, isOpen);
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
            mIsOpen = getArguments().getBoolean(IS_OPEN, false);

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

        mOnCroutonRequestListener = null;
    }

    public void setSelectedEvents(ArrayList<Integer> selectedEvents) {

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
        mGridLayoutManager = new GridLayoutManager(getContext(), NUMBER_OF_COLUMNS);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        int spacing;
        String strManufacturer = android.os.Build.MANUFACTURER;

//        if (strManufacturer.equals(SAMSUNG)) {
//            spacing = 80;
//        } else {
//            spacing = 40;
//        }
        spacing = 0;
        Configuration config = getResources().getConfiguration();
        if (config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
            mRecyclerView.addItemDecoration(new GridSpacingItemDecorationRTL(NUMBER_OF_COLUMNS, spacing, true, 0));
        } else {

            mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(NUMBER_OF_COLUMNS, spacing, true, 0));
        }

        initSubReasons();

        setSpanCount(mIsOpen);
    }

    private void initSubReasons() {
        if (mReportFieldsForMachine != null) {
            mStopReasonsAdapter = new StopSubReasonAdapter(this, getContext(), mReportFieldsForMachine.getStopReasons().get(mSelectedPosition).getSubReasons());
            mRecyclerView.setAdapter(mStopReasonsAdapter);
        }
    }

    public void setSpanCount(boolean isOpen) {
        if (isOpen) {
            mGridLayoutManager.setSpanCount(NUMBER_OF_COLUMNS - 1);
        } else {
            mGridLayoutManager.setSpanCount(NUMBER_OF_COLUMNS);

        }
        mIsOpen = isOpen;
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
    public void onSubReasonSelected(SubReasons subReason) {

        if (mSelectedEvents != null && mSelectedEvents.size() > 0) {

            ZLogger.i(LOG_TAG, "Selected sub reason id: " + subReason.getId());

            mSelectedSubreason = subReason;

            mStopReasonsAdapter.notifyDataSetChanged();

            sendReport();

            SendBroadcast.refreshPolling(getContext());

        } else {

            Toast.makeText(getActivity(), "you need to choice at least one Event", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onClick(View v) {

    }

    private void sendReport() {

        ProgressDialogManager.show(getActivity());

        ReportNetworkBridge reportNetworkBridge = new ReportNetworkBridge();

        reportNetworkBridge.inject(NetworkManager.getInstance(), NetworkManager.getInstance());

        mReportCore = new ReportCore(reportNetworkBridge, PersistenceManager.getInstance());

        mReportCore.registerListener(mReportCallbackListener);

        long[] eventsId = new long[mSelectedEvents.size()];

        for (int i = 0; i < mSelectedEvents.size(); i++) {

            eventsId[0] = mSelectedEvents.get(i);

            SendBroadcast.sendReason(getContext(), mSelectedEvents.get(i), mReasonId, mEnName, mILName, mSelectedSubreason.getEName(), mSelectedSubreason.getLName());

        }

        mReportCore.sendMultipleStopReport(mSelectedReason, mSelectedSubreason.getId(), eventsId, mJobId);

     /*   if (!isOldVersion) {

            mReportCore.sendMultipleStopReport(mSelectedReason, mSelectedSubreason, eventsId, mJobId);

        } else {
            for (int i = 0; i < mSelectedEvents.size(); i++) {
                mReportCore.sendStopReport(mSelectedReason, mSelectedSubreason, mSelectedEvents.get(i), mJobId);

            }
        }*/


    }


    ReportCallbackListener mReportCallbackListener = new ReportCallbackListener() {
        @Override
        public void sendReportSuccess() {
            dismissProgressDialog();
            ZLogger.i(LOG_TAG, "sendReportSuccess()");
            Log.d(DavidVardi.DAVID_TAG_SPRINT_1_5, "sendReportSuccess");

            try {

                mReportCore.unregisterListener();

                getActivity().onBackPressed();
                getActivity().onBackPressed();

            } catch (NullPointerException e) {

                if (getFragmentManager() == null)
                    SendReportUtil.sendAcraExeption(e, "mReportCallbackListener getFragmentManager = null");

                if (mReportCore == null)
                    SendReportUtil.sendAcraExeption(e, "mReportCallbackListener mReportCore = null");
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
