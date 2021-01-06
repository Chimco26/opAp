package com.operatorsapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.operatorsapp.R;
import com.example.common.task.TaskStep;

import java.util.ArrayList;

public class TaskStepsAdapter extends RecyclerView.Adapter<TaskStepsAdapter.ViewHolder> {

    private final ArrayList<TaskStep> mTaskSteps;
    private boolean isTaskCreator;

    public TaskStepsAdapter(boolean isTaskCreator, ArrayList<TaskStep> taskSteps) {
        mTaskSteps = taskSteps;
        this.isTaskCreator = isTaskCreator;
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
        public ImageView mEdit;
//        public TextView mTimeTv;

        public ViewHolder(View view) {
            super(view);
            mText = itemView.findViewById(R.id.task_step_adapter_note_tv);
            mCheckBox = itemView.findViewById(R.id.task_step_adapter_checkbox_cb);
            mEdit = itemView.findViewById(R.id.task_step_adapter_edit_iv);
//            mTimeTv = itemView.findViewById(R.id.task_step_adapter_time_tv);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCheckBox.setChecked(!mCheckBox.isChecked());
                    mTaskSteps.get(getAdapterPosition()).setOpen(!mCheckBox.isChecked());
                }
            });

            if (isTaskCreator) {
                mEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openEditDialog(v.getContext());
                    }
                });
            }else {
                mEdit.setVisibility(View.GONE);
            }
        }

        private void openEditDialog(Context context) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            LayoutInflater inflater = ((AppCompatActivity)context).getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_note, null);
            builder.setView(dialogView);

            ((TextView)dialogView.findViewById(R.id.DN_note_main_title)).setText(context.getString(R.string.sub_task));

            TextView noteTitleTv = dialogView.findViewById(R.id.DN_note_title);
            final EditText noteEt = dialogView.findViewById(R.id.DN_note);
            Button submitBtn = dialogView.findViewById(R.id.DN_btn);
            ImageButton closeButton = dialogView.findViewById(R.id.DN_close_btn);

            noteTitleTv.setText(context.getString(R.string.edit_sub_task));
            noteEt.setText(mText.getText().toString());

            final AlertDialog alert = builder.create();
            alert.show();

            submitBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String note = noteEt.getText().toString();
                    if (!note.isEmpty()){
                        mText.setText(note);
                        mTaskSteps.get(getAdapterPosition()).setText(note);
                    }
                    alert.dismiss();
                }
            });

            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert.dismiss();
                }
            });
        }
    }
}