package com.operatorsapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.operatorsapp.R;
import com.operatorsapp.server.responses.TaskNote;
import com.operatorsapp.utils.TimeUtils;

import java.util.ArrayList;

public class TaskNotesAdapter extends RecyclerView.Adapter<TaskNotesAdapter.ViewHolder> {
    private final ArrayList<TaskNote> mNoteList;

    public TaskNotesAdapter(ArrayList<TaskNote> taskNotes) {
        mNoteList = taskNotes;
    }

    @NonNull
    @Override
    public TaskNotesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_note_adapter, parent, false);
        return new TaskNotesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskNotesAdapter.ViewHolder holder, int position) {
        TaskNote note = mNoteList.get(position);
        holder.mNoteTv.setText(note.mNoteText);
        holder.mNameTv.setText(note.getCreatorName());
        holder.mTimeTv.setText(TimeUtils.getShortDateWithTimeByFormat(note.mCreateDate, TimeUtils.SQL_T_FORMAT));
    }

    @Override
    public int getItemCount() {
        return mNoteList.size();
    }

    public ArrayList<TaskNote> getNoteList() {
        return mNoteList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mNameTv;
        public TextView mNoteTv;
        public TextView mTimeTv;

        public ViewHolder(View view) {
            super(view);
            mNoteTv = itemView.findViewById(R.id.task_comment_adapter_note_tv);
            mTimeTv = itemView.findViewById(R.id.task_comment_adapter_time_tv);
            mNameTv = itemView.findViewById(R.id.task_comment_adapter_name_tv);
        }
    }
}
