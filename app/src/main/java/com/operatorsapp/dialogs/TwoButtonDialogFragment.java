package com.operatorsapp.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

public class TwoButtonDialogFragment extends DialogFragment {
    public final static String DIALOG = "dialog";
    private final static String DIALOG_MESSAGE = "AlertDialogFragment.dialogMessage";
    private final static String POSITIVE_BUTTON_TEXT = "AlertDialogFragment.positiveButtonText";
    private final static String NEGATIVE_BUTTON_TEXT = "AlertDialogFragment.negativeButtonString";
    private OnDialogButtonsListener mListener;
    private String mMessageText;
    private String mPositiveButtonText;
    private String mNegativeButtonText;

    public static TwoButtonDialogFragment newInstance(String message, int positiveButtonText, int negativeButtonString) {
        TwoButtonDialogFragment twoButtonDialogFragment = new TwoButtonDialogFragment();
        Bundle args = new Bundle();
        args.putString(DIALOG_MESSAGE, message);
        args.putInt(POSITIVE_BUTTON_TEXT, positiveButtonText);
        args.putInt(NEGATIVE_BUTTON_TEXT, negativeButtonString);
        twoButtonDialogFragment.setArguments(args);
        return twoButtonDialogFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mMessageText = getArguments().getString(DIALOG_MESSAGE);
            mPositiveButtonText = getResources().getString(getArguments().getInt(POSITIVE_BUTTON_TEXT));
            mNegativeButtonText = getResources().getString(getArguments().getInt(NEGATIVE_BUTTON_TEXT));
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity()).setMessage(mMessageText).setNegativeButton(mNegativeButtonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mListener != null) {
                    mListener.onTwoButtonDialogNegativeButtonClick(dialog, getTargetRequestCode());
                }
            }
        }).setPositiveButton(mPositiveButtonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mListener != null) {
                    mListener.onTwoButtonDialogPositiveButtonClick(dialog, getTargetRequestCode());
                }
            }
        }).create();
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnDialogButtonsListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling fragment must implement OnDialogButtonsListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnDialogButtonsListener {
        void onTwoButtonDialogPositiveButtonClick(DialogInterface dialog, int requestCode);

        void onTwoButtonDialogNegativeButtonClick(DialogInterface dialog, int requestCode);
    }
}
