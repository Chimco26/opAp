package com.operatorsapp.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.operators.reportfieldsformachineinfra.Technician;
import com.operators.reportrejectnetworkbridge.server.response.ResponseStatus;
import com.operatorsapp.R;
import com.operatorsapp.adapters.NotificationHistoryAdapter;
import com.operatorsapp.adapters.TechCallAdapter;
import com.operatorsapp.adapters.TechnicianAdapter;
import com.operatorsapp.application.OperatorApplication;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.model.TechCallInfo;
import com.operatorsapp.server.NetworkManager;
import com.operatorsapp.server.requests.RespondToNotificationRequest;
import com.operatorsapp.server.responses.Notification;
import com.operatorsapp.utils.Consts;
import com.operatorsapp.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by alex on 07/01/2019.
 */

public class TechCallDialog extends Dialog implements View.OnClickListener, TechnicianAdapter.TechItemListener {

    private ArrayList<TechCallInfo> mTechList;
    private RecyclerView mRecyclerView;
    private TextView mSubtitleTv;
    private TextView mTitleTv;
    private ImageView mCloseIv;
    private TextView mRightTab;
    private TextView mLeftTab;
    private TechDialogListener mListener;
    private FrameLayout mProgressBarFl;
    private List<Technician> mTechniciansList;
    private TextView mNewCallTv;
    private LinearLayout mTabsLil;
    private View mRightTabUnderline;
    private View mLeftTabUnderline;

    public TechCallDialog(@NonNull Context context, ArrayList<TechCallInfo> techList, List<Technician> techniciansList, TechDialogListener listener) {
        super(context);
        mTechList = new ArrayList<>();
        for (TechCallInfo call : techList) {
            if (call.isOpenCall()){
                mTechList.add(call);
            }
        }
        mTechniciansList = techniciansList;

        mListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.tech_call_dialog);
        mRecyclerView = (RecyclerView) findViewById(R.id.tech_dialog_rv);
        mSubtitleTv = (TextView) findViewById(R.id.tech_dialog_subtitle_tv);
        mTitleTv = (TextView) findViewById(R.id.tech_dialog_title_tv);
        mCloseIv = (ImageView) findViewById(R.id.tech_dialog_x_iv);
        mRightTab = findViewById(R.id.tech_dialog_tv_right_tab);
        mLeftTab = findViewById(R.id.tech_dialog_tv_left_tab);
        mLeftTabUnderline = findViewById(R.id.tech_dialog_left_tab_underline);
        mRightTabUnderline = findViewById(R.id.tech_dialog_right_tab_underline);
        mProgressBarFl = (FrameLayout) findViewById(R.id.tech_dialog_progress_fl);
        mNewCallTv = (TextView) findViewById(R.id.tech_dialog_new_call_tv);
        mTabsLil = (LinearLayout) findViewById(R.id.tech_dialog_ly_tabs);

        mCloseIv.setOnClickListener(this);
        mNewCallTv.setOnClickListener(this);
        mRightTab.setOnClickListener(this);
        mLeftTab.setOnClickListener(this);
        setOpenCalls(0);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tech_dialog_new_call_tv:
                openTechList();
                //mListener.onNewCallPressed();
                break;
            case R.id.tech_dialog_x_iv:
                mListener.onCancelPressed();
                break;
            case R.id.tech_dialog_tv_left_tab:
                mLeftTab.setTextColor(getContext().getResources().getColor(R.color.tabNotificationColor));
                mRightTab.setTextColor(getContext().getResources().getColor(R.color.dark_indigo));
                mLeftTabUnderline.setVisibility(View.VISIBLE);
                mRightTabUnderline.setVisibility(View.INVISIBLE);
                if (mTechList.size() > 0){
                    mSubtitleTv.setVisibility(View.VISIBLE);
                }else {
                    mSubtitleTv.setVisibility(View.GONE);
                }
                setOpenCalls(0);

                break;
            case R.id.tech_dialog_tv_right_tab:
                mRightTab.setTextColor(getContext().getResources().getColor(R.color.tabNotificationColor));
                mLeftTab.setTextColor(getContext().getResources().getColor(R.color.dark_indigo));
                mLeftTabUnderline.setVisibility(View.INVISIBLE);
                mRightTabUnderline.setVisibility(View.VISIBLE);
                mSubtitleTv.setVisibility(View.GONE);
                setLast24hrsCalls();

                break;
        }
    }

    private void openTechList() {
        mRightTab.setTextColor(getContext().getResources().getColor(R.color.dark_indigo));
        mLeftTab.setTextColor(getContext().getResources().getColor(R.color.dark_indigo));
        mSubtitleTv.setVisibility(View.GONE);
        mTabsLil.setVisibility(View.GONE);

        if (mTechniciansList != null && mTechniciansList.size() > 0) {
            Collections.sort(mTechniciansList, new Comparator<Technician>() {
                @Override
                public int compare(Technician o1, Technician o2) {
                    if (OperatorApplication.isEnglishLang()) {
                        return o1.getEName().compareTo(o2.getEName());
                    } else {
                        return o1.getLName().compareTo(o2.getLName());
                    }
                }
            });

        }
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(new TechnicianAdapter(getContext(), mTechniciansList, this));
    }

    private void setOpenCalls(int position) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(new TechCallAdapter(getContext(), mTechList, new TechCallAdapter.TechCallItemListener() {
            @Override
            public void onRemoveCallPressed(final TechCallInfo techCallInfo) {
                Notification notificationToRemove = null;
                mProgressBarFl.setVisibility(View.VISIBLE);
                final PersistenceManager pm = PersistenceManager.getInstance();
                for (Notification not : pm.getNotificationHistory()) {
                    if (not.getmNotificationID() == techCallInfo.getmNotificationId()){
                        notificationToRemove = not;
                    }
                }

                if (notificationToRemove != null) {

                    int responseType = Consts.NOTIFICATION_RESPONSE_TYPE_CANCELLED;
                    if (notificationToRemove.getmResponseType() == Consts.NOTIFICATION_RESPONSE_TYPE_START_SERVICE) {
                        responseType = Consts.NOTIFICATION_RESPONSE_TYPE_END_SERVICE;
                    }

                    String srcId = pm.getOperatorId();
                    if (!(srcId != null && srcId.length() > 0)){
                        srcId = pm.getUserId() + "";
                    }
                    RespondToNotificationRequest request = new RespondToNotificationRequest(pm.getSessionId(),
                            notificationToRemove.getmTitle(),
                            notificationToRemove.getmBody(getContext()),
                            pm.getMachineId() + "",
                            notificationToRemove.getmNotificationID() + "",
                            responseType,
                            notificationToRemove.getmNotificationType(),
                            Consts.NOTIFICATION_RESPONSE_TARGET_TECHNICIAN,
                            pm.getUserId() + "",
                            pm.getOperatorName(),
                            techCallInfo.getmName(),
                            srcId,
                            techCallInfo.getmTechnicianId() +"");

                    NetworkManager.getInstance().postResponseToNotification(request, new Callback<ResponseStatus>() {
                        @Override
                        public void onResponse(@NonNull Call<ResponseStatus> call, @NonNull Response<ResponseStatus> response) {

                            if (response != null && response.body() != null && response.body().getmError() == null){

                                mTechList.remove(techCallInfo);
                                pm.setCalledTechnicianList(mTechList);
                                if (mTechList.size() > 0 && techCallInfo.getmNotificationId() == PersistenceManager.getInstance().getRecentTechCallId()){
                                    PersistenceManager.getInstance().setRecentTechCallId(mTechList.get(0).getmNotificationId());
                                }else if (mTechList.size() == 0){
                                    PersistenceManager.getInstance().setRecentTechCallId(0);
                                    mSubtitleTv.setVisibility(View.INVISIBLE);
                                }
                                mListener.onCleanTech();
                                setOpenCalls(((LinearLayoutManager) mRecyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition());


                            }else {
                                onFailure(call, new Throwable());
                            }
                            mProgressBarFl.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFailure(@NonNull Call<ResponseStatus> call, @NonNull Throwable t) {
                            mProgressBarFl.setVisibility(View.GONE);
                        }
                    });
                }else {
                    mProgressBarFl.setVisibility(View.GONE);
                }
            }
        }));
        ((LinearLayoutManager) mRecyclerView.getLayoutManager()).scrollToPosition(position);
    }


    private void setLast24hrsCalls() {
        ArrayList<Notification> notList = new ArrayList<>();

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR,-1);
        Date date24hrs = cal.getTime();

        for (Notification notification : PersistenceManager.getInstance().getNotificationHistory()) {
            if (notification.getmNotificationType() == Consts.NOTIFICATION_TYPE_TECHNICIAN && notification.getmResponseType() != Consts.NOTIFICATION_RESPONSE_TYPE_CANCELLED
                && TimeUtils.getDateForNotification(notification.getmResponseDate()).after(date24hrs)) {
                notList.add(notification);
            }
        }
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(new NotificationHistoryAdapter(getContext(), notList, null));
    }

    @Override
    public void onTechnicianSelected(Technician technician) {
        mListener.onTechnicianSelected(technician);
    }

    public interface TechDialogListener{
        void onNewCallPressed();
        void onCancelPressed();
        void onCleanTech();
        void onTechnicianSelected(Technician technician);
    }

}
