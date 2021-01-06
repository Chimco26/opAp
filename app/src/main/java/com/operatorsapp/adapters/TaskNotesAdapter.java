package com.operatorsapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.operatorsapp.R;
import com.operatorsapp.server.responses.TaskNote;
import com.operatorsapp.utils.TimeUtils;

import java.util.ArrayList;

public class TaskNotesAdapter extends RecyclerView.Adapter<TaskNotesAdapter.ViewHolder> {
    private final ArrayList<TaskNote> mNoteList;
    private boolean isTaskCreator;

    public TaskNotesAdapter(boolean isTaskCreator, ArrayList<TaskNote> taskNotes) {
        mNoteList = taskNotes;
        this.isTaskCreator = isTaskCreator;
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

            if (isTaskCreator) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openEditDialog(v.getContext());
                    }
                });
            }
        }

        private void openEditDialog(Context context) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            LayoutInflater inflater = ((AppCompatActivity)context).getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_note, null);
            builder.setView(dialogView);

            TextView noteTitleTv = dialogView.findViewById(R.id.DN_note_title);
            final EditText noteEt = dialogView.findViewById(R.id.DN_note);
            Button submitBtn = dialogView.findViewById(R.id.DN_btn);
            ImageButton closeButton = dialogView.findViewById(R.id.DN_close_btn);

            noteTitleTv.setText(context.getString(R.string.edit_note));
            noteEt.setText(mNoteTv.getText().toString());

            final AlertDialog alert = builder.create();
            alert.show();

            submitBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String note = noteEt.getText().toString();
                    if (!note.isEmpty()){
                        mNoteTv.setText(note);
                        mNoteList.get(getAdapterPosition()).setNoteText(note);
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
