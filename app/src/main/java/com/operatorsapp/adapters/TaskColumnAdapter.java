package com.operatorsapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.common.task.Task;
import com.operatorsapp.R;
import com.woxthebox.draglistview.DragItemAdapter;

import java.util.ArrayList;

public class TaskColumnAdapter extends DragItemAdapter<Task, TaskColumnAdapter.ViewHolder> {

    private final ArrayList<Task> list;

    public TaskColumnAdapter(ArrayList<Task> tasks) {
        list = tasks;
        setItemList(list);
    }

    @Override
    public long getUniqueItemId(int position) {
        return Long.parseLong(list.get(position).getText());
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
        holder.mText.setText(list.get(holder.getAdapterPosition()).getText());
    }

    public class ViewHolder extends DragItemAdapter.ViewHolder {
        private final TextView mText;

        public ViewHolder(View itemView, int handleResId, boolean dragOnLongPress) {
            super(itemView, handleResId, dragOnLongPress);
            mText = itemView.findViewById(R.id.ITask_title);
        }

    }
}
