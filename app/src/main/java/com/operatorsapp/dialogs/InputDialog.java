package com.operatorsapp.dialogs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.operatorsapp.R;

public class InputDialog {

    public static AlertDialog getInputDialog(Context context, String title, String value, final InputDialogListener listener){
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = LayoutInflater.from(context);
        @SuppressLint("InflateParams") View dialogView = inflater.inflate(R.layout.dialog_input, null);
        builder.setView(dialogView);

        TextView titleTv = dialogView.findViewById(R.id.DI_main_title);
        final EditText editEtText = dialogView.findViewById(R.id.DI_et_text);
        Button submitBtn = dialogView.findViewById(R.id.DI_btn);
        ImageButton closeButton = dialogView.findViewById(R.id.DI_close_btn);

        titleTv.setText(title);
        editEtText.setText(value);

        final AlertDialog alert = builder.create();

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSubmit(editEtText.getText().toString());
                alert.dismiss();

            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCancel();
                alert.dismiss();
            }
        });

        return alert;

    }

    public interface InputDialogListener {
        void onSubmit(String text);
        void onCancel();
    }

}
