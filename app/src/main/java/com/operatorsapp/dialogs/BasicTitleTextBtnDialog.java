package com.operatorsapp.dialogs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.operators.reportfieldsformachineinfra.ReportFieldsForMachine;
import com.operatorsapp.R;

public class BasicTitleTextBtnDialog implements View.OnClickListener {
    private final Activity mContext;
    private final String mTitle;
    private final String mMsg;
    private final String mPositiveBtnTxt;
    private final String mNetaiveBtnTxt;
    private final BasicTitleTextBtnDialogListener mListener;
    private final String mSubTitle;
    private AlertDialog mAlarmAlertDialog;
    private ReportFieldsForMachine mReportFieldsForMachine;
    private int mSelectedReasonId;
    private int mSelectedTechnicianId;


    public BasicTitleTextBtnDialog(Activity activity, final BasicTitleTextBtnDialogListener listener,
                                   String title, String subTitle, String msg, String positiveBtn, String negativeBtn) {
        mContext = activity;
        mTitle = title;
        mSubTitle = subTitle;
        mMsg = msg;
        mPositiveBtnTxt = positiveBtn;
        mNetaiveBtnTxt = negativeBtn;
        mListener = listener;
    }

    public AlertDialog showBasicTitleTextBtnDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        LayoutInflater inflater = mContext.getLayoutInflater();
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.title_text_btn_dialog, null);
        builder.setView(view);

        TextView title = view.findViewById(R.id.DALJ_title_tv);
        TextView subTitle = view.findViewById(R.id.DALJ_sub_text_tv);
        TextView msg = view.findViewById(R.id.DALJ_text_tv);
        TextView positiveBtn = view.findViewById(R.id.DALJ_positive_btn);
        TextView negativeBtn = view.findViewById(R.id.DALJ_negative_btn);

        title.setText(mTitle);
        subTitle.setText(mSubTitle);
        msg.setText(mMsg);
        positiveBtn.setText(mPositiveBtnTxt);
        negativeBtn.setText(mNetaiveBtnTxt);

        builder.setCancelable(true);
        mAlarmAlertDialog = builder.create();

        positiveBtn.setOnClickListener(this);
        negativeBtn.setOnClickListener(this);
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

            case R.id.DALJ_negative_btn:
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
