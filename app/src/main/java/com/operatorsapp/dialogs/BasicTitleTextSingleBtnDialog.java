package com.operatorsapp.dialogs;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.operators.reportfieldsformachineinfra.ReportFieldsForMachine;
import com.operatorsapp.R;

public class BasicTitleTextSingleBtnDialog implements View.OnClickListener {
    private final String mTitle;
    private final String mSubtitle;
    private final String mPositiveBtnTxt;
    private final BasicTitleTextBtnDialogListener mListener;
    private final String mMessage;
    private AlertDialog mAlarmAlertDialog;
    private ReportFieldsForMachine mReportFieldsForMachine;
    private int mSelectedReasonId;
    private int mSelectedTechnicianId;


    public BasicTitleTextSingleBtnDialog(final BasicTitleTextBtnDialogListener listener,
                                   String title, String subTitle, String msg, String positiveBtn) {
        mTitle = title;
        mSubtitle = subTitle;
        mMessage = msg;
        mPositiveBtnTxt = positiveBtn;
        mListener = listener;
    }

    public AlertDialog showBasicTitleTextBtnDialog(Activity activity) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.title_txt_single_btn_dialog, null);
        builder.setView(view);

        TextView title = view.findViewById(R.id.DALJ_title_tv);
        TextView subTitle = view.findViewById(R.id.DALJ_sub_title_tv);
        TextView message = view.findViewById(R.id.DALJ_text_tv);
        TextView positiveBtn = view.findViewById(R.id.DALJ_positive_btn);

        title.setText(mTitle);
        message.setText(mMessage);
        subTitle.setText(mSubtitle);
        if (mPositiveBtnTxt != null) {
            positiveBtn.setText(mPositiveBtnTxt);
        } else {
            positiveBtn.setVisibility(View.GONE);
        }

        builder.setCancelable(true);
        mAlarmAlertDialog = builder.create();

        positiveBtn.setOnClickListener(this);
        view.findViewById(R.id.button_cancel).setOnClickListener(this);

        mAlarmAlertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
            }
        });

        return mAlarmAlertDialog;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.button_cancel:
                mListener.onClickNegativeBtn();
                mAlarmAlertDialog.dismiss();
                break;
            case R.id.DALJ_positive_btn:
                mListener.onClickPositiveBtn();
                mAlarmAlertDialog.dismiss();
                break;
        }
    }

    public interface BasicTitleTextBtnDialogListener {

        void onClickPositiveBtn();

        void onClickNegativeBtn();
    }
}
