package com.operatorsapp.view;

import android.content.Context;
import androidx.core.widget.TextViewCompat;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


public class SingleLineKeyboard implements View.OnClickListener {

    private LinearLayout mLinearLayout;
    private String[] mChars;
    private String mText = "";
    public static boolean isKeyBoardOpen = false;
    private final static int DELETE = 123;

    private OnKeyboardClickListener mListener;


    public SingleLineKeyboard(LinearLayout linearLayout, Context context) {
        mLinearLayout = linearLayout;
    }

    public OnKeyboardClickListener getmListener() {
        return mListener;
    }

    public void setListener(OnKeyboardClickListener mListener) {
        this.mListener = mListener;
    }

    public void openKeyBoard(Context context, String text) {

        mText = text;

        closeKeyBoard();

        if (!isKeyBoardOpen)
            initViews(context);

        isKeyBoardOpen = true;
    }

    public void closeKeyBoard() {

        if (isKeyBoardOpen) {

            if (mLinearLayout.getChildCount() > 0) {
                mLinearLayout.removeAllViews();
            }
        }

        isKeyBoardOpen = false;
    }


    private void initViews(Context context) {

        for (int i = 0; i < 10; i++) {
            crateNewTextView(context, "" + i, i, mLinearLayout);
        }

        if (mChars != null)
            for (String chars : mChars) {

                crateNewTextView(context, chars, 0, mLinearLayout);

            }

        crateNewTextView(context, "⌫", DELETE, mLinearLayout);

    }

    private void crateNewTextView(Context context, String text, int id, LinearLayout parent) {

        Button button = new Button(context);
        button.setText(text);
        button.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, 1f));
        button.setGravity(Gravity.CENTER);
        button.setId(id);
        button.setTextSize(23);
        TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(button,
                        14, 23, 1, TypedValue.COMPLEX_UNIT_SP);

        button.setOnClickListener(this);
        parent.addView(button);

    }

    private void addText(String text) {

        switch (text) {
            case "-":
                if (mText.length() == 0)
                    mText += text;

                break;
            case ".":
                if (mText.length() > 0 && !mText.contains("."))
                    mText += text;
                break;
            default:
                mText += text;
                break;
        }

        if (mListener != null) {
            mListener.onKeyboardClick(mText);
        }
    }

    private void delete() {

        if (mText.length() > 0 && mListener != null) {

            mText = mText.substring(0, mText.length() - 1);

            mListener.onKeyboardClick(mText);
        }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case DELETE:
                delete();
                break;

            default:
                String text = (String) ((TextView) v).getText();
                addText(text);
                break;
        }

    }

    public void setChars(String[] mChars) {
        this.mChars = mChars;
    }

    public interface OnKeyboardClickListener {
        void onKeyboardClick(String text);
    }
}
