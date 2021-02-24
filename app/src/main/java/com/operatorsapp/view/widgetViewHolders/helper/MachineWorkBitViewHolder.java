package com.operatorsapp.view.widgetViewHolders.helper;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.common.StandardResponse;
import com.example.common.task.TaskFilesResponse;
import com.operators.machinedatainfra.models.Widget;
import com.operators.reportrejectinfra.SimpleCallback;
import com.operatorsapp.R;
import com.operatorsapp.activities.interfaces.ShowDashboardCroutonListener;
import com.operatorsapp.application.OperatorApplication;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.managers.ProgressDialogManager;
import com.operatorsapp.server.NetworkManager;
import com.operatorsapp.utils.SimpleRequests;
import com.operatorsapp.utils.broadcast.SendBroadcast;

public class MachineWorkBitViewHolder extends RecyclerView.ViewHolder {

    private final int mHeight;
    private final int mWidth;
    private final LinearLayout mPowerBtn;
    private final ImageView mPowerImg;
    private final TextView mPowerText;
    //    private final TextView mTitle;
//    private Switch mSwitch;
//    private CompoundButton.OnCheckedChangeListener mSwitchListener;
    private ShowDashboardCroutonListener mShowDashboardCroutonListener;
    private Activity mContext;
    private Widget mWidget;

    public MachineWorkBitViewHolder(@NonNull View itemView, int height, int width, Activity context, ShowDashboardCroutonListener showDashboardCroutonListener) {
        super(itemView);

        mHeight = height;
        mWidth = width;
        mContext = context;
        mShowDashboardCroutonListener = showDashboardCroutonListener;
//        mTitle = itemView.findViewById(R.id.MWBC_title);
//        mSwitch = itemView.findViewById(R.id.MWBC_switch_SW);
        LinearLayout parentLayout = itemView.findViewById(R.id.widget_parent_layout);
        mPowerBtn = itemView.findViewById(R.id.MWBC_power_BTN);
        mPowerImg = itemView.findViewById(R.id.MWBC_power_IMG);
        mPowerText = itemView.findViewById(R.id.MWBC_power_TEXT);
        setSizes(parentLayout);

        initListeners();
    }

    private void initListeners() {

        mPowerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initPowerBtn(!mWidget.getCurrentValue().equals("1"));
                changeMachineModeRequest();


            }
        });

//        mSwitchListener = new CompoundButton.OnCheckedChangeListener() {
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                changeMachineModeRequest();
//            }
//        };
//        mSwitch.setOnCheckedChangeListener(mSwitchListener);
    }

    private void changeMachineModeRequest() {
        ProgressDialogManager.show(mContext);
        PersistenceManager pm = PersistenceManager.getInstance();
        SimpleRequests.updateMachineStopBit(pm.getMachineId(), pm.getSiteUrl(), new SimpleCallback() {
            @Override
            public void onRequestSuccess(StandardResponse response) {
                dismissProgressDialog();
                if (response.getFunctionSucceed()) {
                    mShowDashboardCroutonListener.onShowCrouton(response.getError().getErrorDesc(), false);
                } else {
                    mShowDashboardCroutonListener.onShowCrouton("sendReportFailure() reason: " + response.getError().getErrorDesc(), true);
                }
                SendBroadcast.refreshPolling(mContext);
            }

            @Override
            public void onRequestFailed(StandardResponse reason) {
                dismissProgressDialog();
                mShowDashboardCroutonListener.onShowCrouton("sendReportFailure() reason: " + reason.getError().getErrorDesc(), true);
                SendBroadcast.refreshPolling(mContext);
            }
        }, NetworkManager.getInstance(), pm.getTotalRetries(), pm.getRequestTimeout());
    }

    private void dismissProgressDialog() {
        if (mContext != null) {
            mContext.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ProgressDialogManager.dismiss();
                }
            });
        }
    }


    public void setData(Widget widget) {
        mWidget = widget;
//        String nameByLang2 = OperatorApplication.isEnglishLang() ? widget.getFieldEName() : widget.getFieldLName();
//        mTitle.setText(nameByLang2);
        initSpinnerMode(widget.getCurrentValue());
    }

    private void initSpinnerMode(String currentValue) {
//        mSwitch.setOnCheckedChangeListener(null);
        initPowerBtn(currentValue.equals("1"));
//        if (currentValue.equals("1")) {
//            mSwitch.setChecked(true);
//        } else {
//            mSwitch.setChecked(false);
//        }
//        mSwitch.setOnCheckedChangeListener(mSwitchListener);
    }

    private void initPowerBtn(boolean isOn) {
        mPowerBtn.setBackgroundColor(mContext.getResources().getColor(isOn ? R.color.red_light :  R.color.green_dark_2));
        mPowerText.setText(mContext.getResources().getText(isOn ?  R.string.stop : R.string.start));
        mPowerImg.setImageDrawable(mContext.getResources().getDrawable(isOn ? R.drawable.ic_outline_stop_24 : R.drawable.ic_baseline_power_settings_new_24 ));
    }


    private void setSizes(final LinearLayout parent) {
        ViewGroup.LayoutParams layoutParams;
        layoutParams = parent.getLayoutParams();
        layoutParams.height = (int) (mHeight * 0.5);
        layoutParams.width = (int) (mWidth * 0.325);
        parent.requestLayout();

    }
}
