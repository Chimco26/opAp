package com.operatorsapp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.view.View;

import com.example.common.ErrorResponse;
import com.example.common.StandardResponse;
import com.operators.reportfieldsformachineinfra.ReportFieldsForMachine;
import com.operatorsapp.R;
import com.operatorsapp.activities.interfaces.ShowDashboardCroutonListener;
import com.operatorsapp.fragments.ReportStopReasonFragment;
import com.operatorsapp.fragments.SelectStopReasonFragment;
import com.operatorsapp.fragments.StopEventLogFragment;
import com.operatorsapp.fragments.interfaces.OnCroutonRequestListener;
import com.operatorsapp.interfaces.ReportFieldsFragmentCallbackListener;
import com.operatorsapp.managers.CroutonCreator;
import com.operatorsapp.utils.ShowCrouton;

import java.util.ArrayList;

import static com.operatorsapp.fragments.ActionBarAndEventsFragment.EXTRA_FIELD_FOR_MACHINE;

public class StopEventLogActivity extends AppCompatActivity
        implements StopEventLogFragment.OnStopEventLogFragmentListener,
        ReportFieldsFragmentCallbackListener,
        ReportStopReasonFragment.ReportStopReasonFragmentListener,
        OnCroutonRequestListener,
        ShowDashboardCroutonListener, CroutonCreator.CroutonListener,
        SelectStopReasonFragment.SelectStopReasonFragmentListener {

    public static final int VIEW_LOG_ACTIVITY_CODE = 45258;
    private ReportFieldsForMachine mFieldForMachine;
    private SelectStopReasonFragment mSelectStopReasonFragment;
    private ArrayList<Float> mSelectedEvents;
    private CroutonCreator mCroutonCreator;

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
        getSupportFragmentManager().beginTransaction().add(R.id.ASE_container, StopEventLogFragment.newInstance()).addToBackStack(StopEventLogFragment.TAG).commit();
    }

    @Override
    public void onReportEvents(ArrayList<Float> eventsIds) {
        ReportStopReasonFragment fragment = ReportStopReasonFragment.newInstance(true, null, 0);
        getSupportFragmentManager().beginTransaction().add(R.id.ASE_container, fragment).addToBackStack(ReportStopReasonFragment.TAG).commit();
        fragment.setSelectedEvents(eventsIds);
        mSelectedEvents = eventsIds;
    }

    @Override
    public ReportFieldsForMachine getReportForMachine() {
        return mFieldForMachine;
    }

    @Override
    public void onOpenSelectStopReasonFragmentNew(SelectStopReasonFragment selectStopReasonFragment) {
        mSelectStopReasonFragment = selectStopReasonFragment;

        try {

            getSupportFragmentManager().beginTransaction().add(R.id.ASE_container, selectStopReasonFragment).addToBackStack(SelectStopReasonFragment.TAG).commit();
        } catch (IllegalStateException ignored) {
        }

        if (mSelectStopReasonFragment != null) {

            mSelectStopReasonFragment.setSelectedEvents(mSelectedEvents);

            mSelectStopReasonFragment.setFromViewLog(true);
        }
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
        }else {
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
    }
}
