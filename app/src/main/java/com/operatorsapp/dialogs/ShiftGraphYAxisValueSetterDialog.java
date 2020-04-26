package com.operatorsapp.dialogs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.operatorsapp.R;
import com.operatorsapp.managers.PersistenceManager;

public class ShiftGraphYAxisValueSetterDialog implements View.OnClickListener {
    private final ShiftGraphYAxisValueSetterDialogListener mListener;
    private AlertDialog mAlarmAlertDialog;
    private EditText minEt;
    private EditText maxEt;
    private Switch activateSwitchBtn;


    public ShiftGraphYAxisValueSetterDialog(final ShiftGraphYAxisValueSetterDialogListener listener) {
        mListener = listener;
    }

    public AlertDialog showDialog(Activity context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = context.getLayoutInflater();
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.dialog_shift_graph_y_axis_value_setter, null);
        builder.setView(view);

        minEt = view.findViewById(R.id.SGY_min_et);
        maxEt = view.findViewById(R.id.SGY_max_et);
        activateSwitchBtn = view.findViewById(R.id.SGY_activate_switch);
        activateSwitchBtn.setChecked(PersistenceManager.getInstance().isShiftCustomY());
        initText(activateSwitchBtn.isChecked(), String.valueOf(PersistenceManager.getInstance().getShiftGraphMinY()),
                String.valueOf(PersistenceManager.getInstance().getShiftGraphMaxY()));
        TextView positiveBtn = view.findViewById(R.id.DALJ_positive_btn);

        builder.setCancelable(true);
        mAlarmAlertDialog = builder.create();

        positiveBtn.setOnClickListener(this);
        view.findViewById(R.id.button_cancel).setOnClickListener(this);

        mAlarmAlertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
            }
        });

        activateSwitchBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b && minEt.getText().length() == 0 && maxEt.getText().length() == 0) {
                    initText(b, minEt.getHint().toString(), maxEt.getHint().toString());
                } else if (b && minEt.getText().length() == 0 && maxEt.getText().toString().length() > 0) {
                    initText(b, minEt.getHint().toString(), maxEt.getText().toString());
                } else if (b && minEt.getText().length() > 0 && maxEt.getText().toString().length() == 0) {
                    initText(b, minEt.getText().toString(), maxEt.getHint().toString());
                } else {
                    initText(b, minEt.getText().toString(), maxEt.getText().toString());
                }
            }
        });

        return mAlarmAlertDialog;
    }

    private void initText(boolean b, String min, String max) {
        if (b) {
            if (!(min.equals("-1") && max.equals("-1"))) {
                minEt.setText(min);
                minEt.setHint(null);
                minEt.setSelection(minEt.getText().toString().length());

                maxEt.setText(String.valueOf(max));
                maxEt.setHint(null);
                maxEt.setSelection(maxEt.getText().toString().length());
            }
        } else {
            if (!(min.equals("-1") && max.equals("-1"))) {
                minEt.setText(null);
                minEt.setHint(min);

                maxEt.setText(null);
                maxEt.setHint(String.valueOf(max));
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.button_cancel:
                mAlarmAlertDialog.dismiss();
                break;
            case R.id.DALJ_positive_btn:
                PersistenceManager.getInstance().setShiftCustomY(activateSwitchBtn.isChecked());
                if (activateSwitchBtn.isChecked()) {
                    try {
                        float min = Float.parseFloat(minEt.getText().toString());
                        float max = Float.parseFloat(maxEt.getText().toString());
                        if (max > min) {
                            PersistenceManager.getInstance().setShiftGraphMinY(min);
                            PersistenceManager.getInstance().setShiftGraphMaxY(max);
                            mListener.onSaveYAxisValues();
                            mAlarmAlertDialog.dismiss();
                        } else {
                            Toast.makeText(v.getContext(), v.getContext().getText(R.string.max_value_must_be_greater_than_min), Toast.LENGTH_SHORT).show();
                        }
                    } catch (NumberFormatException e) {
                        Toast.makeText(v.getContext(), v.getContext().getText(R.string.invalid_value), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    mListener.onSaveYAxisValues();
                    mAlarmAlertDialog.dismiss();
                }
                break;
        }
    }

    public interface ShiftGraphYAxisValueSetterDialogListener {

        void onSaveYAxisValues();
    }
}
