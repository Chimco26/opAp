package com.operatorsapp.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.operatorsapp.R;
import com.operatorsapp.managers.PersistenceManager;

public class LockStatusBarDialog extends Dialog {

    private final Context mContext;
    private final LockStatusBarListener mListener;
    private ImageView mShowHidePassIv;
    private EditText mPassEt;
    private boolean mPasswordIsVisible = false;
    private TextView mUnlockBtn;


    public LockStatusBarDialog(Context context, LockStatusBarListener listener) {
        super(context);
        mContext = context;
        mListener = listener;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams wlp = getWindow().getAttributes();
        wlp.x = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        getWindow().setAttributes(wlp);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.lock_status_dialog);

        mPassEt = findViewById(R.id.lock_status_dialog_password);
        mShowHidePassIv = findViewById(R.id.lock_status_dialog_show_hide_pass);
        mUnlockBtn = findViewById(R.id.lock_status_dialog_unlock_btn);



        mUnlockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkPassword();
            }
        });

        mShowHidePassIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mPasswordIsVisible) {
                    mPassEt.setTransformationMethod(null);
                    mPassEt.setSelection(mPassEt.length());
                    mShowHidePassIv.setImageResource(R.drawable.icn_show_password);
                    mPasswordIsVisible = true;
                } else {
                    mPassEt.setTransformationMethod(new PasswordTransformationMethod());
                    mPassEt.setSelection(mPassEt.length());
                    mShowHidePassIv.setImageResource(R.drawable.icn_password_hidden);
                    mPasswordIsVisible = false;
                }
            }
        });

    }

    private void checkPassword(){

        if (mPassEt.getText().toString().equals(PersistenceManager.getInstance().getPassword())){
            mListener.unlockSuccess();
            this.dismiss();
        }else {
            Toast.makeText(mContext, R.string.password_incorrect, Toast.LENGTH_SHORT).show();
        }
    }

    public interface LockStatusBarListener{
        void unlockSuccess();
    }
}

