package com.operatorsapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.LinearLayout;

import com.example.common.StandardResponse;
import com.example.common.callback.GetMachineLineCallback;
import com.example.common.department.MachineLineResponse;
import com.example.common.department.MachinesLineDetail;
import com.operators.reportfieldsformachineinfra.ReportFieldsForMachine;
import com.operators.reportfieldsformachineinfra.Technician;
import com.operatorsapp.R;
import com.operatorsapp.fragments.TechCallFragment;
import com.operatorsapp.fragments.interfaces.OnCroutonRequestListener;
import com.operatorsapp.managers.CroutonCreator;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.managers.ProgressDialogManager;
import com.operatorsapp.model.TechCallInfo;
import com.operatorsapp.server.NetworkManager;
import com.operatorsapp.server.requests.TechCall24HRequest;
import com.operatorsapp.server.responses.Notification;
import com.operatorsapp.server.responses.NotificationHistoryResponse;
import com.operatorsapp.server.responses.TechCall24HResponse;
import com.operatorsapp.utils.ChangeLang;
import com.operatorsapp.utils.Consts;
import com.operatorsapp.utils.ShowCrouton;
import com.operatorsapp.utils.SimpleRequests;
import com.operatorsapp.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TechCallActivity extends AppCompatActivity implements TechCallFragment.TechCallListener, OnCroutonRequestListener {

    public static final String EXTRA_REPORT_FIELD_FOR_MACHINE = "EXTRA_REPORT_FIELD_FOR_MACHINE";
    public static final String EXTRA_MANAGE_SERVICE_CALL_FOR_TECHNICIAN = "EXTRA_MANAGE_SERVICE_CALL_FOR_TECHNICIAN";
    public static final String EXTRA_IS_MACHINE_CHANGED = "EXTRA_IS_MACHINE_CHANGED";
    public static final String EXTRA_MACHINE_LINE = "EXTRA_MACHINE_LINE";

    private LinearLayout mBackBtn;
    private ReportFieldsForMachine mReportFieldsMachine;
    private List<Technician> mTechnicianList;
    private boolean isManageServiceCall;
    private ArrayList<MachinesLineDetail> machineLineItems = new ArrayList<>();
    private boolean isMachineChanged = false;
    private CroutonCreator mCroutonCreator;
    private MachineLineResponse mMachineLineResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tech_call);
        mCroutonCreator = new CroutonCreator();
        setToolbar();
        getExtras();
//        openTechCallFragment();
        getMachinesLineData();
    }

    private void getExtras() {
        mReportFieldsMachine = getIntent().getParcelableExtra(EXTRA_REPORT_FIELD_FOR_MACHINE);
        mTechnicianList = mReportFieldsMachine.getTechnicians();
        isManageServiceCall = getIntent().getBooleanExtra(EXTRA_MANAGE_SERVICE_CALL_FOR_TECHNICIAN, false);
    }

    private void getMachinesLineData() {
        ProgressDialogManager.show(this);
        PersistenceManager pm = PersistenceManager.getInstance();
        SimpleRequests.getMachineLine(pm.getSiteUrl(), new GetMachineLineCallback() {
            @Override
            public void onGetMachineLineSuccess(MachineLineResponse response) {
                ProgressDialogManager.dismiss();
                mMachineLineResponse = response;
                setLineLayouts();
                getNotificationsFromServer(mMachineLineResponse != null && mMachineLineResponse.getLineID() != 0);
            }

            @Override
            public void onGetMachineLineFailed(StandardResponse reason) {
                ProgressDialogManager.dismiss();
                ShowCrouton.showSimpleCrouton(TechCallActivity.this, reason.getError().getErrorDesc(), CroutonCreator.CroutonType.NETWORK_ERROR);
            }
        }, NetworkManager.getInstance(), pm.getTotalRetries(), pm.getRequestTimeout());
    }

    @Override
    protected void onResume() {
        super.onResume();
        ChangeLang.initLanguage(this);
    }

    private void setLineLayouts() {
        if (mMachineLineResponse != null && mMachineLineResponse.getLineID() != 0){
            machineLineItems.clear();
            machineLineItems.addAll(mMachineLineResponse.getMachinesData());
            TechCallFragment fragment = (TechCallFragment) getSupportFragmentManager().findFragmentByTag(TechCallFragment.LOG_TAG);
            if (fragment != null && fragment.isVisible()){
                fragment.setLineLayout(mMachineLineResponse);
            }
        }
    }

    private void openTechCallFragment() {
        ProgressDialogManager.dismiss();
        TechCallFragment fragment = TechCallFragment.newInstance();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(TechCallActivity.EXTRA_REPORT_FIELD_FOR_MACHINE, new ArrayList<Parcelable>(mTechnicianList));
        bundle.putParcelable(TechCallActivity.EXTRA_MACHINE_LINE, mMachineLineResponse);
        bundle.putBoolean(TechCallActivity.EXTRA_MANAGE_SERVICE_CALL_FOR_TECHNICIAN, isManageServiceCall);

        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment, TechCallFragment.LOG_TAG).commit();
    }

    public void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        mBackBtn = toolbar.findViewById(R.id.toolbar_tech_call_back_lil);
        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getNotificationsFromServer(final boolean isLineMachine) {
        ProgressDialogManager.show(this);
        Callback<NotificationHistoryResponse> notificationCallback = new Callback<NotificationHistoryResponse>() {
            @Override
            public void onResponse(Call<NotificationHistoryResponse> call, Response<NotificationHistoryResponse> response) {

                if (response.body() != null && response.body().getError().getErrorDesc() == null) {

//                    ArrayList<TechCallInfo> techList = PersistenceManager.getInstance().getCalledTechnician();
                    ArrayList<TechCallInfo> techListCopy = new ArrayList<>();
//                    techListCopy = techList;
                    ArrayList<TechCallInfo> techList = new ArrayList<>();
                    for (Notification not : response.body().getmNotificationsList()) {

                            not.setmSentTime(TimeUtils.getStringNoTFormatForNotification(not.getmSentTime()));
                            not.setmResponseDate(TimeUtils.getStringNoTFormatForNotification(not.getmResponseDate()));

                            if (not.getmNotificationType() == Consts.NOTIFICATION_TYPE_TECHNICIAN) {
                                techList.add(new TechCallInfo(not.getMachineID(), not.getmResponseType(), not.getmTargetName(), not.getmTitle(),
                                        not.getmAdditionalText(), TimeUtils.getLongFromDateString(not.getmResponseDate(), TimeUtils.SIMPLE_FORMAT_FORMAT),
                                        not.getmNotificationID(), not.getmTargetUserId(), not.getmEventID(), not.getmEventName()));

//                                boolean isNew = true;
//                                if (techList != null && techListCopy.size() > 0) {
//                                    for (int i = 0; i < techListCopy.size(); i++) {
//                                        TechCallInfo tech = techListCopy.get(i);
//                                        if (tech.getmNotificationId() == not.getmNotificationID() && tech.getmResponseType() == not.getmResponseType()
//                                                && tech.getmTechnicianId() == not.getmTargetUserId()) {
//                                            isNew = false;
//                                            tech.setmCallTime(TimeUtils.getLongFromDateString(not.getmResponseDate(), TimeUtils.SIMPLE_FORMAT_FORMAT));
//                                            tech.setmResponseType(not.getmResponseType());
//                                            techList.set(i, tech);
//                                        } else if (tech.getmTechnicianId() == not.getmTargetUserId() && not.getmNotificationID() > tech.getmNotificationId()) {
//                                            techList.remove(i);
//                                        } else if (tech.getmNotificationId() == not.getmNotificationID()) {
//                                            techList.remove(i);
//                                        }
//                                    }
//                                }
//                                if (isNew) {
//                                }
                            }
                        }
                    PersistenceManager.getInstance().setNotificationHistory(response.body().getmNotificationsList());
                    PersistenceManager.getInstance().setCalledTechnicianList(techList);
                    openTechCallFragment();
                } else {
                    PersistenceManager.getInstance().setNotificationHistory(null);
                    if (response.body() != null) {
                        onFailure(call, new Throwable(response.body().getError().getErrorDesc()));
                    } else {
                        ShowCrouton.showSimpleCrouton(TechCallActivity.this, getString(R.string.credentials_error), CroutonCreator.CroutonType.CREDENTIALS_ERROR);
                    }
                }
                ProgressDialogManager.dismiss();
            }

            @Override
            public void onFailure(Call<NotificationHistoryResponse> call, Throwable t) {
                openTechCallFragment();
                ProgressDialogManager.dismiss();
                ShowCrouton.showSimpleCrouton(TechCallActivity.this, t.getMessage(), CroutonCreator.CroutonType.CREDENTIALS_ERROR);
            }
        };

        if (isLineMachine) {
            int[] machinesIdArray = new int[machineLineItems.size()];
            for (int i = 0; i < machineLineItems.size(); i++) {
                machinesIdArray[i] = machineLineItems.get(i).getMachineID();
            }
            NetworkManager.getInstance().getNotificationHistory(machinesIdArray, notificationCallback);
        }else {
            NetworkManager.getInstance().getNotificationHistory(notificationCallback);
        }

    }

    @Override
    public void onTechCallListener(Uri uri) {

    }

    @Override
    public void onGetNotificationsFromServer() {
        getNotificationsFromServer(mMachineLineResponse != null && mMachineLineResponse.getLineID() != 0);
    }


    @Override
    public void onGetLast24HCalls(){
        TechCall24HRequest request = new TechCall24HRequest(PersistenceManager.getInstance().getSessionId(), PersistenceManager.getInstance().getMachineId() + "");
        NetworkManager.getInstance().getTechCall24H(request, new Callback<TechCall24HResponse>() {
            @Override
            public void onResponse(Call<TechCall24HResponse> call, Response<TechCall24HResponse> response) {
                if (response.body() != null && response.body().getError() == null){
                    Fragment frag = getSupportFragmentManager().findFragmentByTag(TechCallFragment.LOG_TAG);
                    if (frag != null && frag.isVisible()) {
                        ((TechCallFragment)frag).setLast24hCallList(response.body().getCalls24Hours());
                    }
                }else {
                    String msg = "General Response Error";
                    if (response.body() != null){
                        msg = response.body().getError().getMessage();
                    }
                    onFailure(call, new Throwable(msg));
                }
            }

            @Override
            public void onFailure(Call<TechCall24HResponse> call, Throwable t) {
                onShowCroutonRequest(t.getMessage(), 5000, R.id.container, CroutonCreator.CroutonType.NETWORK_ERROR);
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (mCroutonCreator != null) {
            mCroutonCreator.cancel();
            mCroutonCreator = null;
        }

        Intent intent = getIntent();
        intent.putExtra(EXTRA_IS_MACHINE_CHANGED, isMachineChanged);
        setResult(RESULT_OK, intent);
        super.onDestroy();
    }

    @Override
    public void onShowCroutonRequest(String croutonMessage, int croutonDurationInMilliseconds, int viewGroup, CroutonCreator.CroutonType croutonType) {
        if (mCroutonCreator != null) {
            mCroutonCreator.showCrouton(this, croutonMessage, croutonDurationInMilliseconds, R.id.container, croutonType, null);
        }
    }

    @Override
    public void onShowCroutonRequest(SpannableStringBuilder croutonMessage, int croutonDurationInMilliseconds, int viewGroup, CroutonCreator.CroutonType croutonType) {
        if (mCroutonCreator != null) {
            mCroutonCreator.showCrouton(this, String.valueOf(croutonMessage), croutonDurationInMilliseconds, R.id.container, croutonType, null);
        }
    }

    @Override
    public void onHideConnectivityCroutonRequest() {
        if (mCroutonCreator != null) {
            mCroutonCreator.hideConnectivityCrouton();
        }
    }
}
