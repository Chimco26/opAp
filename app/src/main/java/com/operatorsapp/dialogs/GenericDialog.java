package com.operatorsapp.dialogs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.operatorsapp.R;

/**
 * Created by alex on 15/01/2019.
 */

public class GenericDialog implements View.OnClickListener {


    private static final String DIALOG_BODY = "body";
    private static final String DIALOG_TITLE = "title";
    private Activity mContext;
    private final String mActionYesStr;
    private final boolean mIsError;
    private String mBodyStr;
    private String mTitleStr;
    private ImageView mCloseIv;
    private ImageView mHeaderIconIv;
    private TextView mTitleTv;
    private TextView mBodyTv;
    private LinearLayout mActionButtonsLil;
    private Button mActionYesBtn;
    private Button mActionNoBtn;
    private Button mActionAnotherBtn;
    private OnGenericDialogListener mListener;
    private AlertDialog mAlertDialog;

    public GenericDialog(Activity context, String body, String title, String actionYes, boolean isError) {
        mContext = context;
        mBodyStr = body;
        mTitleStr = title;
        mActionYesStr = actionYes;
        mIsError = isError;
        mContext.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }

    public AlertDialog showNoProductionAlarm() {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        LayoutInflater inflater = mContext.getLayoutInflater();
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.basic_message_dialog, null);
        builder.setView(view);
        mCloseIv = view.findViewById(R.id.basic_dialog_close_iv);
        mHeaderIconIv = view.findViewById(R.id.basic_dialog_header_icon_iv);
        mTitleTv = view.findViewById(R.id.basic_dialog_header_tv);
        mBodyTv = view.findViewById(R.id.basic_dialog_body_tv);
        mActionButtonsLil = view.findViewById(R.id.basic_dialog_action_lil);
        mActionYesBtn = view.findViewById(R.id.basic_dialog_action_yes_btn);
        mActionNoBtn = view.findViewById(R.id.basic_dialog_action_no_btn);
        mActionAnotherBtn = view.findViewById(R.id.basic_dialog_action_another_btn);

        mCloseIv.setOnClickListener(this);
        mActionYesBtn.setOnClickListener(this);
        mActionNoBtn.setOnClickListener(this);
        mActionAnotherBtn.setOnClickListener(this);

        mBodyTv.setText(mBodyStr);
        mTitleTv.setText(mTitleStr);
        mActionYesBtn.setText(mActionYesStr);

        if (mIsError){
            mTitleTv.setTextColor(Color.RED);
            mActionYesBtn.setBackgroundColor(Color.RED);
            mHeaderIconIv.setImageResource(R.drawable.attention_red);
        }
        builder.setCancelable(true);
        mAlertDialog = builder.create();
        mAlertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mContext.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            }
        });
        mAlertDialog.show();
        return mAlertDialog;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.basic_dialog_close_iv:
                mAlertDialog.dismiss();
                break;
            case R.id.basic_dialog_action_no_btn:
                if (mListener != null){
                    mListener.onActionNo();
                }
                break;
            case R.id.basic_dialog_action_yes_btn:
                if (mListener != null){
                    mListener.onActionYes();
                }
                break;
            case R.id.basic_dialog_action_another_btn:
                if (mListener != null) {
                    mListener.onActionAnother();
                }
                break;
        }
    }

    public void setListener(OnGenericDialogListener listener) {
        mListener = listener;
    }

    public interface OnGenericDialogListener {

        void onActionYes();
        void onActionNo();
        void onActionAnother();

//        void onDismissClick(DialogInterface dialog, int requestCode);
//
//        void onDismissAllClick(DialogInterface dialog, int eventGroupId, int requestCode);
//
//        void onReportClick(Event event);
    }
}
