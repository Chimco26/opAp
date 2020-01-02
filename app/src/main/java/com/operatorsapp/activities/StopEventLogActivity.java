package com.operatorsapp.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.View;

import com.example.common.ErrorResponse;
import com.example.common.StandardResponse;
import com.example.common.StopLogs.Event;
import com.example.oppapplog.OppAppLogger;
import com.operators.reportfieldsformachineinfra.ReportFieldsForMachine;
import com.operators.reportfieldsformachineinfra.SubReasons;
import com.operators.reportrejectcore.ReportCallbackListener;
import com.operators.reportrejectcore.ReportCore;
import com.operators.reportrejectnetworkbridge.ReportNetworkBridge;
import com.operatorsapp.R;
import com.operatorsapp.activities.interfaces.ShowDashboardCroutonListener;
import com.operatorsapp.dialogs.Alert2BtnDialog;
import com.operatorsapp.fragments.ReportStopReasonFragment;
import com.operatorsapp.fragments.SelectStopReasonFragment;
import com.operatorsapp.fragments.StopEventLogFragment;
import com.operatorsapp.fragments.interfaces.OnCroutonRequestListener;
import com.operatorsapp.interfaces.ReportFieldsFragmentCallbackListener;
import com.operatorsapp.managers.CroutonCreator;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.managers.ProgressDialogManager;
import com.operatorsapp.server.NetworkManager;
import com.operatorsapp.utils.DavidVardi;
import com.operatorsapp.utils.SendReportUtil;
import com.operatorsapp.utils.ShowCrouton;
import com.operatorsapp.utils.broadcast.SendBroadcast;

import java.util.ArrayList;
import java.util.List;

import static com.operatorsapp.fragments.ActionBarAndEventsFragment.EXTRA_FIELD_FOR_MACHINE;
import static com.operatorsapp.fragments.ReportStopReasonFragment.MINIMUM_VERSION_TO_NEW_API;
import static org.litepal.LitePalApplication.getContext;

public class StopEventLogActivity extends AppCompatActivity
        implements StopEventLogFragment.OnStopEventLogFragmentListener,
        ReportFieldsFragmentCallbackListener,
        ReportStopReasonFragment.ReportStopReasonFragmentListener,
        OnCroutonRequestListener,
        ShowDashboardCroutonListener, CroutonCreator.CroutonListener,
        SelectStopReasonFragment.SelectStopReasonFragmentListener {

    public static final int VIEW_LOG_ACTIVITY_CODE = 45258;
    private static final String TAG = StopEventLogActivity.class.getSimpleName();
    private ReportFieldsForMachine mFieldForMachine;
    private SelectStopReasonFragment mSelectStopReasonFragment;
    private ArrayList<Float> mSelectedEvents;
    private CroutonCreator mCroutonCreator;
    private ReportCore mReportCore;
    private int position;
    private SubReasons subReason;
    private ArrayList<Event> mSubEvents;
    private StopEventLogFragment mStopEventLogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_event);

        mCroutonCreator = new CroutonCreator();

        if (getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
            findViewById(R.id.ASE_back_btn).setRotationY(180);
        }

        findViewById(R.id.ASE_back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        if (getIntent().hasExtra(EXTRA_FIELD_FOR_MACHINE)) {
            mFieldForMachine = getIntent().getParcelableExtra(EXTRA_FIELD_FOR_MACHINE);
        }
        mStopEventLogFragment = StopEventLogFragment.newInstance();
        getSupportFragmentManager().beginTransaction().add(R.id.ASE_container, mStopEventLogFragment).addToBackStack(StopEventLogFragment.TAG).commit();
    }

    @Override
    public void onReportEvents(ArrayList<Event> subEvents, ArrayList<Float> eventsIds, boolean b) {
        ReportStopReasonFragment fragment = ReportStopReasonFragment.newInstance(true, null, 0);
        getSupportFragmentManager().beginTransaction().add(R.id.ASE_container, fragment).addToBackStack(ReportStopReasonFragment.TAG).commit();
        fragment.setSelectedEvents(eventsIds);
        mSelectedEvents = eventsIds;
        fragment.setFromViewLog(true);
        fragment.setFromViewLogRoot(b);
        mSubEvents = subEvents;
    }

    @Override
    public ReportFieldsForMachine getReportForMachine() {
        return mFieldForMachine;
    }

    @Override
    public void onOpenSelectStopReasonFragmentNew(SelectStopReasonFragment selectStopReasonFragment, boolean isFromViewLogRoot) {
        mSelectStopReasonFragment = selectStopReasonFragment;

        try {

            getSupportFragmentManager().beginTransaction().add(R.id.ASE_container, selectStopReasonFragment).addToBackStack(SelectStopReasonFragment.TAG).commit();
        } catch (IllegalStateException ignored) {
        }

        if (mSelectStopReasonFragment != null) {

            mSelectStopReasonFragment.setSelectedEvents(mSelectedEvents);

            mSelectStopReasonFragment.setFromViewLog(true);
            mSelectStopReasonFragment.setFromViewLogRoot(isFromViewLogRoot);
        }
    }

    @Override
    public void onReport(final int position, final SubReasons mSelectedSubreason) {
        if (getVisibleFragment() instanceof ReportStopReasonFragment) {
            onBackPressed();
        }
        if (getVisibleFragment() instanceof SelectStopReasonFragment) {
            onBackPressed();
            onBackPressed();
        }

        final ArrayList<Float> events = new ArrayList<>();
        for (Event event : mSubEvents) {
            if (event.getEventGroupID() == 6) {
                events.add(event.getEventID() * 1.0f);
            }
        }
        if (mSubEvents != null && mSubEvents.size() > 0) {
            Alert2BtnDialog alert2BtnDialog = new Alert2BtnDialog(this, new Alert2BtnDialog.Alert2BtnDialogListener() {
                @Override
                public void onClickPositiveBtn() {
//                    sendReport(position, mSelectedSubreason);
                    ArrayList<Float> list = new ArrayList<>();
                    for (Event event: mSubEvents){
                        list.add(event.getEventID() * 1.0f);
                    }
                    mSelectedEvents.addAll(list);
                    sendReport(position, mSelectedSubreason);
                }

                @Override
                public void onClickNegativeBtn() {
                    mSelectedEvents.addAll(events);
                    sendReport(position, mSelectedSubreason);
                }
            }, getString(R.string.update_this_event_to_all_linked), getString(R.string.yes), getString(R.string.only_to_unreported));

            alert2BtnDialog.showAlert2BtnDialog().show();
        }else {
            sendReport(position, mSelectedSubreason);
        }
    }

    @Override
    public void onSuccess() {
        mStopEventLogFragment.onResume();
    }

    @Override
    public void onShowCroutonRequest(String croutonMessage, int croutonDurationInMilliseconds, int viewGroup, CroutonCreator.CroutonType croutonType) {

    }

    @Override
    public void onShowCroutonRequest(SpannableStringBuilder croutonMessage, int croutonDurationInMilliseconds, int viewGroup, CroutonCreator.CroutonType croutonType) {
        if (mCroutonCreator != null) {
            mCroutonCreator.showCrouton(this, String.valueOf(croutonMessage), croutonDurationInMilliseconds, R.id.parent_layouts, croutonType, this);

        }
    }

    @Override
    public void onHideConnectivityCroutonRequest() {

    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getFragments().size() > 1) {
            super.onBackPressed();
        } else {
            finish();
        }
    }

    @Override
    public void onShowCrouton(String errorResponse, boolean isError) {
        if (isError) {
            if (errorResponse == null || errorResponse.length() == 0) {
                errorResponse = " ";
            }
            StandardResponse errorObject = new StandardResponse(ErrorResponse.ErrorCode.Retrofit, errorResponse);
            ShowCrouton.showSimpleCrouton(this, errorObject);
        } else {
            if (errorResponse == null || errorResponse.length() == 0) {
                errorResponse = getString(R.string.success);
            }
            ShowCrouton.showSimpleCrouton(this, errorResponse, CroutonCreator.CroutonType.SUCCESS);
        }

    }

    @Override
    public void onCroutonDismiss() {

    }

    @Override
    public void onSuccess(StandardResponse standardResponse) {
        onBackPressed();
        onBackPressed();
        mStopEventLogFragment.onResume();
    }


    public Fragment getVisibleFragment() {
        Fragment f = null;
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment != null && fragment.isVisible()) {
                    f = fragment;
                }
            }
        }
        return f;
    }

    //SEND REPORT

    private void sendReport(int position, SubReasons subReasons) {

        this.position = position;
        this.subReason = subReasons;

        ProgressDialogManager.show(this);

        ReportNetworkBridge reportNetworkBridge = new ReportNetworkBridge();

        reportNetworkBridge.inject(NetworkManager.getInstance(), NetworkManager.getInstance());

        mReportCore = new ReportCore(reportNetworkBridge, PersistenceManager.getInstance());

        mReportCore.registerListener(mReportCallbackListener);

        long[] eventsId = new long[mSelectedEvents.size()];

        for (int i = 0; i < mSelectedEvents.size(); i++) {

            eventsId[i] = mSelectedEvents.get(i).intValue();

//            SendBroadcast.sendReason(getContext(), mSelectedEvents.get(i), mReportFieldsForMachine.getStopReasons().get(mSelectedPosition).getId(),
//                    mReportFieldsForMachine.getStopReasons().get(mSelectedPosition).getEName(),
//                    mReportFieldsForMachine.getStopReasons().get(mSelectedPosition).getLName(),
//                    mSelectedSubreason.getEName(), mSelectedSubreason.getLName());

        }

        if (PersistenceManager.getInstance().getVersion() < MINIMUM_VERSION_TO_NEW_API) {

            for (int i = 0; i < mSelectedEvents.size(); i++) {

                mReportCore.sendStopReport(getReportForMachine().getStopReasons().get(position).getId()
                        , subReasons.getId(), mSelectedEvents.get(i).intValue(), PersistenceManager.getInstance().getJoshId());

            }

        } else {

            mReportCore.sendMultipleStopReport(getReportForMachine().getStopReasons().get(position).getId(),
                    subReasons.getId(), eventsId, PersistenceManager.getInstance().getJoshId());

        }


    }


    ReportCallbackListener mReportCallbackListener = new ReportCallbackListener() {
        @Override
        public void sendReportSuccess(StandardResponse response) {
//            StandardResponse response = objectToNewError(response);
            SendBroadcast.refreshPolling(getParent());
            ProgressDialogManager.dismiss();

            if (response.getFunctionSucceed()) {
                // TODO: 17/07/2018 add crouton for success
                if (response.getError().getErrorDesc() == null || response.getError().getErrorDesc().length() < 1) {
                    response.getError().setErrorDesc(getString(R.string.success));
                }
                // ShowCrouton.showSimpleCrouton(mOnCroutonRequestListener, response.getError().getErrorDesc(), CroutonCreator.CroutonType.SUCCESS);
                onShowCrouton(response.getError().getErrorDesc(), false);
                OppAppLogger.getInstance().i(TAG, "sendReportSuccess()");
                Log.d(DavidVardi.DAVID_TAG_SPRINT_1_5, "sendReportSuccess");
                mStopEventLogFragment.onResume();
                for (int i = 0; i < mSelectedEvents.size(); i++) {

                    SendBroadcast.sendReason(getContext(), mSelectedEvents.get(i).intValue(), getReportForMachine().getStopReasons().get(position).getId(),
                            getReportForMachine().getStopReasons().get(position).getEName(),
                            getReportForMachine().getStopReasons().get(position).getLName(),
                            subReason.getEName(), subReason.getLName());

                }


            } else {
                onShowCrouton(response.getError().getErrorDesc(), true);
            }

            try {

                mReportCore.unregisterListener();

            } catch (NullPointerException e) {

                if (getFragmentManager() == null)
                    SendReportUtil.sendAcraExeption(e, "mReportCallbackListener getFragmentManager = null");

                if (mReportCore == null)
                    SendReportUtil.sendAcraExeption(e, "mReportCallbackListener mReportCore = null");
            }
        }

        @Override
        public void sendReportFailure(StandardResponse reason) {
            ProgressDialogManager.dismiss();
            OppAppLogger.getInstance().w(TAG, "sendReportFailure()");
            String msg = "missing reports";
            if (reason != null && reason.getError().getErrorDesc() != null) {
                msg = reason.getError().getErrorDesc();
            }
            StandardResponse errorObject = new StandardResponse(ErrorResponse.ErrorCode.Missing_reports, msg);
            ShowCrouton.jobsLoadingErrorCrouton(StopEventLogActivity.this, errorObject);

        }
    };

}
