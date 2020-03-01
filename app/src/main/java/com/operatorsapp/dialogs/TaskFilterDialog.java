package com.operatorsapp.dialogs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.common.SelectableString;
import com.operatorsapp.R;
import com.operatorsapp.adapters.CheckBoxFilterAdapter;
import com.operatorsapp.managers.PersistenceManager;

import java.util.ArrayList;

public class TaskFilterDialog {
    private Context mContext;
    private AlertDialog mAlarmAlertDialog;
    private CountDownTimer mCountDownTimer;


    public TaskFilterDialog(Context context) {
        mContext = context;
    }

    public AlertDialog showTaskFilterDialog() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        @SuppressLint("InflateParams") View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_task_filter, null);
        builder.setView(view);

        final Button applyBtn = view.findViewById(R.id.DTF_apply_btn);
        final ImageView closeBtn = view.findViewById(R.id.DTF_close_btn);
        final RecyclerView priorityRv = view.findViewById(R.id.DTF_priority_rv);
        final RecyclerView periodRv = view.findViewById(R.id.DTF_time_rv);

        final ArrayList<SelectableString> priorities = PersistenceManager.getInstance().getTaskFilterPriorityToShow();
        CheckBoxFilterAdapter prioritiesAdapter = new CheckBoxFilterAdapter(priorities, new CheckBoxFilterAdapter.CheckBoxFilterAdapterListener() {
            @Override
            public void onItemCheck(SelectableString selectableString) {
                for (SelectableString selectableString1 : priorities) {
                    if (selectableString1.getId().equals(selectableString.getId())) {
                        selectableString1.setSelected(selectableString.isSelected());
                        return;
                    }
                }
            }
        }, false);
        priorityRv.setAdapter(prioritiesAdapter);
        priorityRv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        priorityRv.setLayoutManager(llm);

        final ArrayList<SelectableString> periods = PersistenceManager.getInstance().getTaskFilterPeriodToShow();
        periodRv.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        periodRv.setHasFixedSize(true);
        CheckBoxFilterAdapter periodsAdapter = new CheckBoxFilterAdapter(periods, new CheckBoxFilterAdapter.CheckBoxFilterAdapterListener() {
            @Override
            public void onItemCheck(SelectableString selectableString) {
                for (SelectableString selectableString1 : periods) {
                    if (selectableString1.getId().equals(selectableString.getId())) {
                        selectableString1.setSelected(selectableString.isSelected());
                        return;
                    }
                }
            }
        }, false);
        periodRv.setAdapter(periodsAdapter);

        builder.setCancelable(true);
        mAlarmAlertDialog = builder.create();

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAlarmAlertDialog.dismiss();
            }
        });

        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PersistenceManager.getInstance().setTaskFilterPeriodToShow(periods);
                PersistenceManager.getInstance().setTaskFilterPriorityToShow(priorities);
                mAlarmAlertDialog.dismiss();
            }
        });

        return mAlarmAlertDialog;
    }

}
