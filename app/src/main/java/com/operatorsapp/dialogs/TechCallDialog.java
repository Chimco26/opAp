package com.operatorsapp.dialogs;

import android.app.Activity;
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
import android.widget.TextView;

import com.operators.reportrejectnetworkbridge.server.response.ErrorResponseNewVersion;
import com.operatorsapp.R;
import com.operatorsapp.adapters.TechCallAdapter;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.model.TechCallInfo;
import com.operatorsapp.server.NetworkManager;
import com.operatorsapp.server.requests.RespondToNotificationRequest;
import com.operatorsapp.server.responses.Notification;
import com.operatorsapp.utils.Consts;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by alex on 07/01/2019.
 */

public class TechCallDialog extends Dialog implements View.OnClickListener {

    private ArrayList<TechCallInfo> mTechList;
    private RecyclerView mRecyclerView;
    private TextView mSubtitleTv;
    private TextView mTitleTv;
    private ImageView mCloseIv;
    private TextView mNewCall;
    private TechDialogListener mListener;
    private FrameLayout mProgressBarFl;

    public TechCallDialog(@NonNull Context context, ArrayList<TechCallInfo> techList, TechDialogListener listener) {
        super(context);
        mTechList = new ArrayList<>();
        for (TechCallInfo call : techList) {
            if (call.isOpenCall()){
                mTechList.add(call);
            }
        }

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
        mNewCall = (TextView) findViewById(R.id.tech_dialog_new_call);
        mProgressBarFl = (FrameLayout) findViewById(R.id.tech_dialog_progress_fl);

        mCloseIv.setOnClickListener(this);
        mNewCall.setOnClickListener(this);
        setRecyclerVew();
    }

    private void setRecyclerVew() {
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

                    RespondToNotificationRequest request = new RespondToNotificationRequest(pm.getSessionId(),
                            notificationToRemove.getmTitle(),
                            notificationToRemove.getmBody(),
                            pm.getMachineId() + "",
                            notificationToRemove.getmNotificationID() + "",
                            Consts.NOTIFICATION_RESPONSE_TYPE_CANCELLED,
                            notificationToRemove.getmNotificationType(),
                            Consts.NOTIFICATION_RESPONSE_TARGET_TECHNICIAN,
                            techCallInfo.getmTechnicianId()+"",
                            pm.getOperatorName(),
                            techCallInfo.getmName());

                    NetworkManager.getInstance().postResponseToNotification(request, new Callback<ErrorResponseNewVersion>() {
                        @Override
                        public void onResponse(@NonNull Call<ErrorResponseNewVersion> call, @NonNull Response<ErrorResponseNewVersion> response) {

                            if (response != null && response.body() != null && response.body().getmError() == null){

                                mTechList.remove(techCallInfo);
                                pm.setCalledTechnicianList(mTechList);
                                if (mTechList.size() > 0 && techCallInfo.getmNotificationId() == PersistenceManager.getInstance().getRecentTechCallId()){
                                    PersistenceManager.getInstance().setRecentTechCallId(mTechList.get(0).getmNotificationId());
                                }else if (mTechList.size() == 0){
                                    PersistenceManager.getInstance().setRecentTechCallId(0);
                                }
                                mListener.onCleanTech(techCallInfo);
                                setRecyclerVew();


                            }else {
                                onFailure(call, new Throwable());
                            }
                            mProgressBarFl.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFailure(@NonNull Call<ErrorResponseNewVersion> call, @NonNull Throwable t) {
                            mProgressBarFl.setVisibility(View.GONE);
                        }
                    });
                }else {
                    mProgressBarFl.setVisibility(View.GONE);
                }
            }
        }));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tech_dialog_new_call:
                mListener.onNewCallPressed();
                break;
            case R.id.tech_dialog_x_iv:
                mListener.onCancelPressed();
                break;
        }
    }

    public interface TechDialogListener{
        void onNewCallPressed();
        void onCancelPressed();
        void onCleanTech(TechCallInfo techCallInfo);
    }

}