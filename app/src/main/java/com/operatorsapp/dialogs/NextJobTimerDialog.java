package com.operatorsapp.dialogs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.os.CountDownTimer;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.operatorsapp.R;
import com.operatorsapp.utils.TimeUtils;

import static android.text.format.DateUtils.SECOND_IN_MILLIS;

public class NextJobTimerDialog implements View.OnClickListener {
    private final Activity mContext;
    private final String mTitle;
    private final String mSubtitle;
    private final String mPositiveBtnTxt;
    private final String mNegativeBtnTxt;
    private final NextJobTimerDialogListener mListener;
    private final String mMessage;
    private final int mCounter;
    private final boolean mAutoActivateNextJobTimer;
    private AlertDialog mAlarmAlertDialog;
    private CountDownTimer mCountDownTimer;


    public NextJobTimerDialog(Activity activity, final NextJobTimerDialogListener listener,
                              String title, String subTitle, String msg, String positiveBtn, String negativeBtn, int counter, boolean autoActivateNextJobTimer) {
        mContext = activity;
        mTitle = title;
        mSubtitle = subTitle;
        mMessage = msg;
        mPositiveBtnTxt = positiveBtn;
        mNegativeBtnTxt = negativeBtn;
        mListener = listener;
        mCounter = counter;
        mAutoActivateNextJobTimer = autoActivateNextJobTimer;
    }

    public AlertDialog showNextJobTimerDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        LayoutInflater inflater = mContext.getLayoutInflater();
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.dialog_target_reached, null);
        builder.setView(view);

        TextView title = view.findViewById(R.id.DALJ_title_tv);
        TextView subTitle = view.findViewById(R.id.DALJ_sub_title_tv);
        final TextView message = view.findViewById(R.id.DALJ_text_tv);
        TextView positiveBtn = view.findViewById(R.id.DALJ_positive_btn);
        TextView negativeBtn = view.findViewById(R.id.DALJ_negative_btn);
        ImageView imageView = view.findViewById(R.id.DALJ_title_ic);

        if (mAutoActivateNextJobTimer) {
            mCountDownTimer = new CountDownTimer(mCounter * SECOND_IN_MILLIS, SECOND_IN_MILLIS) {
                public void onTick(long millisUntilFinished) {
                    message.setText(TimeUtils.getHMSFromMillis(millisUntilFinished));
                }

                public void onFinish() {
                    mListener.onClickPositiveBtn();
                    mAlarmAlertDialog.dismiss();
                }

            }.start();
        }
        imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_production_blue1));
        title.setText(mTitle);
        subTitle.setText(String.format("%s : ERPJobId: %s", mSubtitle, mMessage));
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

        builder.setCancelable(false);
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
                if (mCountDownTimer != null){
                    mCountDownTimer.cancel();
                }
                break;
            case R.id.DALJ_positive_btn:
                mListener.onClickPositiveBtn();
                mAlarmAlertDialog.dismiss();
                break;
        }
    }

    public interface NextJobTimerDialogListener {

        void onClickPositiveBtn();

        void onClickNegativeBtn();
    }

}
