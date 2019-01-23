package com.operatorsapp.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.operatorsapp.R;
import com.operatorsapp.model.TechCallInfo;
import com.operatorsapp.utils.Consts;

/**
 * Created by alex on 15/01/2019.
 */

public class GenericDialog extends Dialog implements View.OnClickListener {


    private static final String DIALOG_BODY = "body";
    private static final String DIALOG_TITLE = "title";
    private final Context mContext;
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

    public GenericDialog(Context context, String body, String title, String actionYes, boolean isError) {
        super(context);
        mContext = context;
        mBodyStr = body;
        mTitleStr = title;
        mActionYesStr = actionYes;
        mIsError = isError;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.basic_message_dialog);

        mCloseIv = findViewById(R.id.basic_dialog_close_iv);
        mHeaderIconIv = findViewById(R.id.basic_dialog_header_icon_iv);
        mTitleTv = findViewById(R.id.basic_dialog_header_tv);
        mBodyTv = findViewById(R.id.basic_dialog_body_tv);
        mActionButtonsLil = findViewById(R.id.basic_dialog_action_lil);
        mActionYesBtn = findViewById(R.id.basic_dialog_action_yes_btn);
        mActionNoBtn = findViewById(R.id.basic_dialog_action_no_btn);
        mActionAnotherBtn = findViewById(R.id.basic_dialog_action_another_btn);

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
            mHeaderIconIv.setBackground(mContext.getResources().getDrawable(R.drawable.attention_red));
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.basic_dialog_close_iv:
                this.dismiss();
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
