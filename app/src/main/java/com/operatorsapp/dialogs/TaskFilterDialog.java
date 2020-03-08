package com.operatorsapp.dialogs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.common.SelectableString;
import com.example.common.task.Task;
import com.operatorsapp.R;
import com.operatorsapp.adapters.CheckBoxFilterAdapter;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.utils.TaskUtil;

import java.util.ArrayList;

public class TaskFilterDialog {
    private Context mContext;
    private AlertDialog mAlarmAlertDialog;
    private CompoundButton.OnCheckedChangeListener selectAllListener;

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
        final CheckBox selectAllCb = view.findViewById(R.id.DTF_select_all_cb);

        final ArrayList<SelectableString> priorities = PersistenceManager.getInstance().getTaskFilterPriorityToShow();
        final CheckBoxFilterAdapter prioritiesAdapter = new CheckBoxFilterAdapter(priorities, new CheckBoxFilterAdapter.CheckBoxFilterAdapterListener() {
            @Override
            public void onItemCheck(SelectableString selectableString) {
                for (SelectableString selectableString1 : priorities) {
                    if (selectableString1.getId().equals(selectableString.getId())) {
                        selectableString1.setSelected(selectableString.isSelected());
                        updateSelectAll(selectAllCb);
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
        final CheckBoxFilterAdapter periodsAdapter = new CheckBoxFilterAdapter(periods, new CheckBoxFilterAdapter.CheckBoxFilterAdapterListener() {
            @Override
            public void onItemCheck(SelectableString selectableString) {
                for (SelectableString selectableString1 : periods) {
                    if (selectableString1.getId().equals(selectableString.getId())) {
                        selectableString1.setSelected(selectableString.isSelected());
                        updateSelectAll(selectAllCb);
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

        selectAllListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                for (SelectableString selectableString1 : periods) {
                    selectableString1.setSelected(b);
                }
                for (SelectableString selectableString1 : priorities) {
                    selectableString1.setSelected(b);
                }
                periodsAdapter.notifyDataSetChanged();
                prioritiesAdapter.notifyDataSetChanged();
            }
        };

        updateSelectAll(selectAllCb);

        return mAlarmAlertDialog;
    }

    private void updateSelectAll(CheckBox selectAllCb) {
        selectAllCb.setOnCheckedChangeListener(null);
        if (TaskUtil.isFiltered()){
            selectAllCb.setChecked(false);
        }else {
            selectAllCb.setChecked(true);
        }
        selectAllCb.setOnCheckedChangeListener(selectAllListener);
    }

}
