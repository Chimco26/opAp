package com.operatorsapp.dialogs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.operatorsapp.R;

public class Alert2BtnDialog implements View.OnClickListener {
    private final Activity mContext;

    private final String mPositiveBtnTxt;
    private final String mNegativeBtnTxt;
    private final Alert2BtnDialogListener mListener;
    private final String mMessage;
    private AlertDialog mAlarmAlertDialog;


    public Alert2BtnDialog(Activity activity, final Alert2BtnDialogListener listener
            , String msg, String positiveBtn, String negativeBtn) {
        mContext = activity;
        mMessage = msg;
        mPositiveBtnTxt = positiveBtn;
        mNegativeBtnTxt = negativeBtn;
        mListener = listener;
    }

    public AlertDialog showAlert2BtnDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        LayoutInflater inflater = mContext.getLayoutInflater();
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.alert_2_btn_dialog, null);
        builder.setView(view);

        TextView message = view.findViewById(R.id.DALJ_text_tv);
        TextView positiveBtn = view.findViewById(R.id.DALJ_positive_btn);
        TextView negativeBtn = view.findViewById(R.id.DALJ_negative_btn);

        message.setText(mMessage);
        if (mPositiveBtnTxt != null) {
            positiveBtn.setText(mPositiveBtnTxt);
        } else {
            positiveBtn.setVisibility(View.GONE);
        }
        if (mNegativeBtnTxt != null) {
            negativeBtn.setText(mNegativeBtnTxt);
        } else {
            negativeBtn.setVisibility(View.GONE);
        }

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

    public interface Alert2BtnDialogListener {

        void onClickPositiveBtn();

        void onClickNegativeBtn();
    }
}