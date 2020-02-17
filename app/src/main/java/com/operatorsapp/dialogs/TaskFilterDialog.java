package com.operatorsapp.dialogs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
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

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        @SuppressLint("InflateParams") View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_task_filter, null);
        builder.setView(view);

        final ImageView priorityExpand = view.findViewById(R.id.DTF_priority_arrow);
        final ImageView timeExpand = view.findViewById(R.id.DTF_time_arrow);
        final RecyclerView priorityRv = view.findViewById(R.id.DTF_priority_rv);
        final RecyclerView periodRv = view.findViewById(R.id.DTF_time_rv);

        priorityExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateView(priorityRv, priorityExpand);
            }
        });
        timeExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateView(periodRv, timeExpand);
            }
        });

        final ArrayList<SelectableString> priorities = PersistenceManager.getInstance().getTaskFilterPriorityToShow();
        priorityRv.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        priorityRv.setHasFixedSize(true);
        CheckBoxFilterAdapter prioritiesAdapter = new CheckBoxFilterAdapter(priorities, new CheckBoxFilterAdapter.CheckBoxFilterAdapterListener() {
            @Override
            public void onItemCheck(SelectableString selectableString) {
                for (SelectableString selectableString1 : priorities) {
                    if (selectableString1.getId().equals(selectableString.getId())) {
                        selectableString1.setSelected(selectableString.isSelected());
                        PersistenceManager.getInstance().setTaskFilterPriorityToShow(priorities);
                        return;
                    }
                }
            }
        }, false);
        priorityRv.setAdapter(prioritiesAdapter);

        final ArrayList<SelectableString> periods = PersistenceManager.getInstance().getTaskFilterPeriodToShow();
        periodRv.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        periodRv.setHasFixedSize(true);
        CheckBoxFilterAdapter periodsAdapter = new CheckBoxFilterAdapter(periods, new CheckBoxFilterAdapter.CheckBoxFilterAdapterListener() {
            @Override
            public void onItemCheck(SelectableString selectableString) {
                for (SelectableString selectableString1 : periods) {
                    if (selectableString1.getId().equals(selectableString.getId())) {
                        selectableString1.setSelected(selectableString.isSelected());
                        PersistenceManager.getInstance().setTaskFilterPeriodToShow(periods);
                        return;
                    }
                }
            }
        }, false);
        periodRv.setAdapter(periodsAdapter);

        builder.setCancelable(true);
        mAlarmAlertDialog = builder.create();

        return mAlarmAlertDialog;
    }

    private void updateView(RecyclerView priorityRv, ImageView priorityExpand) {
        if (priorityRv.getVisibility() == View.GONE) {
            priorityExpand.setRotation(90);
            priorityRv.setVisibility(View.VISIBLE);
        } else {
            priorityExpand.setRotation(0);
            priorityRv.setVisibility(View.GONE);
        }
    }
}
