package com.operatorsapp.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.common.ErrorResponse;
import com.example.common.StandardResponse;
import com.example.common.callback.ErrorObjectInterface;
import com.example.oppapplog.OppAppLogger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.operators.reportrejectcore.ReportCallbackListener;
import com.operators.reportrejectcore.ReportCore;
import com.operators.reportrejectnetworkbridge.ReportNetworkBridge;
import com.operatorsapp.BuildConfig;
import com.operatorsapp.R;
import com.operatorsapp.activities.interfaces.ShowDashboardCroutonListener;
import com.operatorsapp.adapters.StopSubReasonAdapter;
import com.operatorsapp.fragments.interfaces.OnCroutonRequestListener;
import com.operatorsapp.fragments.interfaces.OnSelectedSubReasonListener;
import com.operatorsapp.interfaces.ReportFieldsFragmentCallbackListener;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.managers.ProgressDialogManager;
import com.operatorsapp.server.NetworkManager;
import com.operatorsapp.server.responses.StopReasonsGroup;
import com.operatorsapp.utils.DavidVardi;
import com.operatorsapp.utils.GoogleAnalyticsHelper;
import com.operatorsapp.utils.SendReportUtil;
import com.operatorsapp.utils.ShowCrouton;
import com.operatorsapp.utils.broadcast.SendBroadcast;
import com.operatorsapp.view.GridSpacingItemDecoration;
import com.operatorsapp.view.GridSpacingItemDecorationRTL;

import java.util.ArrayList;

import static com.operatorsapp.utils.broadcast.SelectStopReasonBroadcast.EN_NAME;
import static com.operatorsapp.utils.broadcast.SelectStopReasonBroadcast.IL_NAME;
import static com.operatorsapp.utils.broadcast.SelectStopReasonBroadcast.REASON_ID;

public class SelectStopReasonFragment extends BackStackAwareFragment implements OnSelectedSubReasonListener, View.OnClickListener {

    public static final String TAG = SelectStopReasonFragment.class.getSimpleName();
    private static final String SELECTED_STOP_REASON_POSITION = "selected_stop_reason_position";
    private static final String CURRENT_JOSH_ID = "current_job_id";
    private static final String IS_OPEN = "IS_OPEN";

    private static final int NUMBER_OF_COLUMNS = 5;
    private static final float MINIMUM_VERSION_TO_NEW_API = 1.7f;
    private static final String EXTRA_VIEW_LOG_REPORT_IS_SUCCESS = "EXTRA_VIEW_LOG_REPORT_IS_SUCCESS";

    //    private ReportFieldsForMachine mReportFieldsForMachine;
    private Integer mJoshId = 0;

    private StopReasonsGroup mSelectedSubreason;
    private int mSelectedReason;

    private RecyclerView mRecyclerView;
    private StopSubReasonAdapter mStopReasonsAdapter;
    private OnCroutonRequestListener mOnCroutonRequestListener;
    private ReportCore mReportCore;

    private ArrayList<Float> mSelectedEvents;
    private int mSelectedPosition;
    private int mReasonId;
    private String mEnName;
    private String mILName;
    private GridLayoutManager mGridLayoutManager;
    private boolean mIsOpen;
    private ShowDashboardCroutonListener mDashboardCroutonListener;
    private int mFlavorSpanDif;
    private boolean isFromViewLog;
    private SelectStopReasonFragmentListener mListener;
    private boolean isFromViewLogRoot;
    private StopReasonsGroup mStopReason;


    public static SelectStopReasonFragment newInstance(int position, StopReasonsGroup stopReasonsGroup, int joshId, int reasonId, String eName, String lName, boolean isOpen) {
        SelectStopReasonFragment selectedStopReasonFragment = new SelectStopReasonFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(SELECTED_STOP_REASON_POSITION, position);
        bundle.putInt(CURRENT_JOSH_ID, joshId);
        bundle.putString(EN_NAME, eName);
        bundle.putString(IL_NAME, lName);
        bundle.putInt(REASON_ID, reasonId);
        bundle.putBoolean(IS_OPEN, isOpen);
        selectedStopReasonFragment.setArguments(bundle);
        selectedStopReasonFragment.setStopReasons(stopReasonsGroup);
        return selectedStopReasonFragment;
    }

    private void setStopReasons(StopReasonsGroup stopReasonsGroup) {
        mStopReason = stopReasonsGroup;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSelectedPosition = getArguments().getInt(SELECTED_STOP_REASON_POSITION);
            mJoshId = getArguments().getInt(CURRENT_JOSH_ID);
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
//        if (reportFieldsFragmentCallbackListener != null) {
//            mReportFieldsForMachine = reportFieldsFragmentCallbackListener.getReportForMachine();
//        }
        mOnCroutonRequestListener = (OnCroutonRequestListener) getActivity();
        if (context instanceof ShowDashboardCroutonListener) {
            mDashboardCroutonListener = (ShowDashboardCroutonListener) getActivity();
        }
        if (context instanceof SelectStopReasonFragmentListener) {
            mListener = (SelectStopReasonFragmentListener) getActivity();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

//        mReportFieldsForMachine = null;

        mOnCroutonRequestListener = null;
    }

    public void setSelectedEvents(ArrayList<Float> selectedEvents) {

        mSelectedEvents = selectedEvents;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_select_stop_reason_new, container, false);

        mSelectedReason = mStopReason.getId();
        if (BuildConfig.FLAVOR.equals(getString(R.string.lenox_flavor_name))) {
            view.findViewById(R.id.powered_by_leadermess_txt).setVisibility(View.VISIBLE);
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Analytics
        new GoogleAnalyticsHelper().trackScreen(getActivity(), "Report Stop Reason- circle level 2");

//        if (BuildConfig.FLAVOR.equals(getString(R.string.lenox_flavor_name))){
//
//            mFlavorSpanDif = -1;
//        }
        //TODO Lenox uncomment

        mRecyclerView = view.findViewById(R.id.selected_stop_recycler_view);
        mGridLayoutManager = new GridLayoutManager(getContext(), NUMBER_OF_COLUMNS + mFlavorSpanDif);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        int spacing;

        spacing = 0;
        Configuration config = getResources().getConfiguration();
        if (config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
            mRecyclerView.addItemDecoration(new GridSpacingItemDecorationRTL(NUMBER_OF_COLUMNS + mFlavorSpanDif, spacing, true, 0));
        } else {

            mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(NUMBER_OF_COLUMNS + mFlavorSpanDif, spacing, true, 0));
        }

        initSubReasons();

        setSpanCount(mIsOpen);

        view.findViewById(R.id.FSSRN_close_select_events).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                close();
            }
        });

        View backBtn = view.findViewById(R.id.FSSRN_back_btn);
        if (config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
            backBtn.setRotation(180);
        }
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (getActivity() != null) {
                    getActivity().onBackPressed();
                }
            }
        });
    }

    private void initSubReasons() {
        if (mStopReason != null) {
            mStopReasonsAdapter = new StopSubReasonAdapter(this, getContext(), mStopReason);
            mRecyclerView.setAdapter(mStopReasonsAdapter);
        }
    }

    public void setSpanCount(boolean isOpen) {
        if (isOpen) {
            mGridLayoutManager.setSpanCount(NUMBER_OF_COLUMNS - 1 + mFlavorSpanDif);
        } else {
            mGridLayoutManager.setSpanCount(NUMBER_OF_COLUMNS + mFlavorSpanDif);

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
    public void onSubReasonSelected(StopReasonsGroup subReason) {

        if (mSelectedEvents != null && mSelectedEvents.size() > 0) {

            OppAppLogger.i(TAG, "Selected sub reason id: " + subReason.getId());

            mSelectedSubreason = subReason;

            mStopReasonsAdapter.notifyDataSetChanged();

            if (isFromViewLogRoot) {
                mListener.onReport(mSelectedPosition, mSelectedSubreason);
            } else {
                sendReport();
            }
//            SendBroadcast.refreshPolling(getContext());

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

            eventsId[i] = mSelectedEvents.get(i).intValue();

            SendBroadcast.sendReason(getContext(), mSelectedEvents.get(i).intValue(), mReasonId, mEnName, mILName, mSelectedSubreason.getEName(), mSelectedSubreason.getLName());

        }

        if (PersistenceManager.getInstance().getVersion() < MINIMUM_VERSION_TO_NEW_API) {

            for (int i = 0; i < mSelectedEvents.size(); i++) {

                mReportCore.sendStopReport(mSelectedReason, mSelectedSubreason.getId(), mSelectedEvents.get(i).intValue(), mJoshId);

            }

        } else {

            mReportCore.sendMultipleStopReport(mSelectedReason, mSelectedSubreason.getId(), eventsId, mJoshId, false);

        }


    }


    ReportCallbackListener mReportCallbackListener = new ReportCallbackListener() {
        @Override
        public void sendReportSuccess(StandardResponse o) {
            StandardResponse response = objectToNewError(o);
            SendBroadcast.refreshPolling(getContext());
            dismissProgressDialog();

            if (response.getFunctionSucceed()) {

                try {
                    // TODO: 17/07/2018 add crouton for success
                    // ShowCrouton.showSimpleCrouton(mOnCroutonRequestListener, response.getError().getErrorDesc(), CroutonCreator.CroutonType.SUCCESS);
                    mDashboardCroutonListener.onShowCrouton(response.getError().getErrorDesc(), false);
                    OppAppLogger.i(TAG, "sendReportSuccess()");
                    Log.d(DavidVardi.DAVID_TAG_SPRINT_1_5, "sendReportSuccess");
                    //Analytics
                    new GoogleAnalyticsHelper().trackEvent(getActivity(), GoogleAnalyticsHelper.EventCategory.STOP_REASON_REPORT, true,
                            "Stop Reason: " + mStopReason.getEName() + ", Subreason: " + mSelectedSubreason.getEName());
                } catch (NullPointerException e) {

                }

            } else {
                try {
                    mDashboardCroutonListener.onShowCrouton(response.getError().getErrorDesc(), true);
                    //Analytics
                    new GoogleAnalyticsHelper().trackEvent(getActivity(), GoogleAnalyticsHelper.EventCategory.STOP_REASON_REPORT, false, "Error: " + response.getError().getErrorDesc());
                } catch (NullPointerException e) {

                }
            }

            try {

                mReportCore.unregisterListener();

                if (isFromViewLog) {
                    mListener.onSuccess(response);
                    return;
                }
                close();

            } catch (NullPointerException e) {

                if (getFragmentManager() == null)
                    SendReportUtil.sendAcraExeption(e, "mReportCallbackListener getFragmentManager = null");

                if (mReportCore == null)
                    SendReportUtil.sendAcraExeption(e, "mReportCallbackListener mReportCore = null");
            }
        }

        @Override
        public void sendReportFailure(StandardResponse reason) {
            dismissProgressDialog();
            OppAppLogger.w(TAG, "sendReportFailure()");

            //Analytics
            new GoogleAnalyticsHelper().trackEvent(getActivity(), GoogleAnalyticsHelper.EventCategory.STOP_REASON_REPORT, false, "Error: " + reason.getError().getErrorDesc());

            if (reason.getError().getErrorCodeConstant() == ErrorObjectInterface.ErrorCode.Credentials_mismatch && getActivity() != null) {
//                ((DashboardActivity) getActivity()).silentLoginFromDashBoard(mOnCroutonRequestListener, new SilentLoginCallback() {
//                    @Override
//                    public void onSilentLoginSucceeded() {
//                        sendReport();
//                    }
//
//                    @Override
//                    public void onSilentLoginFailed(StandardResponse reason) {
//                        OppAppLogger.w(TAG, "Failed silent login");
//                        StandardResponse errorObject = new StandardResponse(ErrorResponse.ErrorCode.Missing_reports, "missing reports");
//                        ShowCrouton.jobsLoadingErrorCrouton(mOnCroutonRequestListener, errorObject);
//                    }
//                });
                //todo check
            } else {

                StandardResponse errorObject = new StandardResponse(ErrorResponse.ErrorCode.Missing_reports, "missing reports");
                ShowCrouton.jobsLoadingErrorCrouton(mOnCroutonRequestListener, errorObject);
            }

        }
    };

    public void close() {
        if (getActivity() != null) {
            getActivity().onBackPressed();
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }
        }
    }

    private StandardResponse objectToNewError(Object o) {
        StandardResponse responseNewVersion;
        if (o instanceof StandardResponse) {
            responseNewVersion = (StandardResponse) o;
        } else {
            Gson gson = new GsonBuilder().create();

            ErrorResponse er = gson.fromJson(new Gson().toJson(o), ErrorResponse.class);

            responseNewVersion = new StandardResponse(true, 0, er);
            if (responseNewVersion.getError().getErrorCode() != 0) {
                responseNewVersion.setFunctionSucceed(false);
            }
        }
        return responseNewVersion;
    }

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

    public void setFromViewLog(boolean b) {
        isFromViewLog = b;
    }

    public void setFromViewLogRoot(boolean isFromViewLogRoot) {
        this.isFromViewLogRoot = isFromViewLogRoot;
    }
//
//    @Override
//    public int getCroutonRoot() {
//        //return R.id.parent_layouts;
//        return R.id.selected_stop_crouton_root;
//    }

    public interface SelectStopReasonFragmentListener {
        void onSuccess(StandardResponse standardResponse);

        void onReport(int mSelectedPosition, StopReasonsGroup mSelectedSubreason);
    }
}
