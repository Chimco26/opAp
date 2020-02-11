package com.operatorsapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.common.task.TaskProgress;
import com.operatorsapp.R;
import com.woxthebox.draglistview.DragItemAdapter;

import java.util.List;

public class TaskColumnAdapter extends DragItemAdapter<TaskProgress, TaskColumnAdapter.ViewHolder> {

    private final List<TaskProgress> list;

    public TaskColumnAdapter(List<TaskProgress> tasks) {
        list = tasks;
        setItemList(list);
    }

    @Override
    public long getUniqueItemId(int position) {
        return Long.parseLong(String.valueOf(list.get(position).getTaskID()));
    }

    @NonNull
    @Override
    public TaskColumnAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        return new TaskColumnAdapter.ViewHolder(inflater.inflate(R.layout.item_task, parent, false), R.id.ITask_title, true);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        holder.mTitle.setText(String.valueOf(list.get(holder.getAdapterPosition()).getTaskID()));
        holder.mText.setText(list.get(holder.getAdapterPosition()).getText());
        holder.mAuthor.setText(list.get(holder.getAdapterPosition()).getHistoryUser());
//        setPriorityViews(holder.mPriorityIc, holder.mPriorityMarge, list.get(holder.getAdapterPosition()));
    }

    private void setPriorityViews(ImageView mPriorityIc, View mPriorityMarge, TaskProgress taskProgress) {
        //todo logic
        int color = 0;
        switch (taskProgress.getTaskLevel()) {
            case 0:
                color = 0;
                break;
        }
        mPriorityIc.setColorFilter(color);
        mPriorityMarge.setVisibility(View.VISIBLE);
    }

    public class ViewHolder extends DragItemAdapter.ViewHolder {
        private TextView mTitle;
        private TextView mText;
        private TextView mAuthor;
        private ImageView mPriorityIc;
        private View mPriorityMarge;

        public ViewHolder(View itemView, int handleResId, boolean dragOnLongPress) {
            super(itemView, handleResId, dragOnLongPress);
            mTitle = itemView.findViewById(R.id.ITask_title);
            mText = itemView.findViewById(R.id.ITask_text);
            mAuthor = itemView.findViewById(R.id.ITask_author);
            mPriorityIc = itemView.findViewById(R.id.ITask_priority_ic);
            mPriorityMarge = itemView.findViewById(R.id.ITask_priority_view);
        }

    }
}
