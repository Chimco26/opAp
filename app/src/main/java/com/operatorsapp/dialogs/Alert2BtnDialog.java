package com.operatorsapp.dialogs;

import android.annotation.SuppressLint;
import android.app.Activity;
import androidx.appcompat.app.AlertDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.operatorsapp.R;
import com.operatorsapp.activities.QCActivity;

public class Alert2BtnDialog implements View.OnClickListener {

    private final String mPositiveBtnTxt;
    private final String mNegativeBtnTxt;
    private final Alert2BtnDialogListener mListener;
    private final String mMessage;
    private AlertDialog mAlarmAlertDialog;
    private boolean isSelected = false;


    public Alert2BtnDialog(final Alert2BtnDialogListener listener
            , String msg, String positiveBtn, String negativeBtn) {
        mMessage = msg;
        mPositiveBtnTxt = positiveBtn;
        mNegativeBtnTxt = negativeBtn;
        mListener = listener;
    }

    public AlertDialog showAlert2BtnDialog(final Activity context, boolean isFullScreen) {

        AlertDialog.Builder builder;
        LayoutInflater inflater = LayoutInflater.from(context);
        @SuppressLint("InflateParams") View view;

        if (isFullScreen){
            builder = new AlertDialog.Builder(context, R.style.Dialog_full_screen);
            view = inflater.inflate(R.layout.alert_2_btn_dialog_full_screen, null);
        }else {
            builder = new AlertDialog.Builder(context);
            view = inflater.inflate(R.layout.alert_2_btn_dialog, null);
        }


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
        if (context instanceof QCActivity){
            negativeBtn.setBackgroundColor(context.getResources().getColor(R.color.blue1));
            negativeBtn.setTextColor(context.getResources().getColor(R.color.white));
        }

        builder.setView(view);
        builder.setCancelable(true);
        mAlarmAlertDialog = builder.create();

        positiveBtn.setOnClickListener(this);
        negativeBtn.setOnClickListener(this);
        view.findViewById(R.id.button_cancel).setOnClickListener(this);

        mAlarmAlertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (!isSelected && context instanceof QCActivity)
                    context.finish();
            }
        });

        return mAlarmAlertDialog;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.button_cancel:
                mAlarmAlertDialog.dismiss();
                break;
            case R.id.DALJ_negative_btn:
                mListener.onClickNegativeBtn();
                isSelected = true;
                mAlarmAlertDialog.dismiss();
                break;
            case R.id.DALJ_positive_btn:
                mListener.onClickPositiveBtn();
                isSelected = true;
                mAlarmAlertDialog.dismiss();
                break;
        }
    }

    public interface Alert2BtnDialogListener {

        void onClickPositiveBtn();

        void onClickNegativeBtn();
    }
}