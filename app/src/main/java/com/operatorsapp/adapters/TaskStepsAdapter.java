package com.operatorsapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.operatorsapp.R;
import com.example.common.task.TaskStep;

import java.util.ArrayList;

public class TaskStepsAdapter extends RecyclerView.Adapter<TaskStepsAdapter.ViewHolder> {

    private final ArrayList<TaskStep> mTaskSteps;

    public TaskStepsAdapter(ArrayList<TaskStep> taskSteps) {
        mTaskSteps = taskSteps;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_step_adapter, parent, false);
        return new TaskStepsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TaskStep step = mTaskSteps.get(position);
        holder.mText.setText(step.getText());
        holder.mCheckBox.setChecked(!step.isOpen());
//        holder.mTimeTv.setText(TimeUtils.getShortDateWithTimeByFormat(step.mCreateDate, TimeUtils.SQL_NO_T_FORMAT));
    }

    @Override
    public int getItemCount() {
        return mTaskSteps.size();
    }

    public ArrayList<TaskStep> getTaskSteps() {
        return mTaskSteps == null ? new ArrayList<TaskStep>() : mTaskSteps;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CheckBox mCheckBox;
        public TextView mText;
//        public TextView mTimeTv;

        public ViewHolder(View view) {
            super(view);
            mText = itemView.findViewById(R.id.task_step_adapter_note_tv);
            mCheckBox = itemView.findViewById(R.id.task_step_adapter_checkbox_cb);
//            mTimeTv = itemView.findViewById(R.id.task_step_adapter_time_tv);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCheckBox.setChecked(!mCheckBox.isChecked());
                    mTaskSteps.get(getAdapterPosition()).setOpen(!mCheckBox.isChecked());
                }
            });
        }
    }
}