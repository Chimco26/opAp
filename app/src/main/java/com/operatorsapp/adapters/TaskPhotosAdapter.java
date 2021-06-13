package com.operatorsapp.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.common.task.TaskFileResponse;
import com.operatorsapp.R;
import com.operatorsapp.dialogs.ShowImageDialog;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class TaskPhotosAdapter extends RecyclerView.Adapter<TaskPhotosAdapter.ViewHolder> {
    private static final int TYPE_FILE = 1;
    private static final int TYPE_URL = 2;
    private final ArrayList<File> mFileList;
    private final ArrayList<TaskFileResponse.TaskFiles> mFilesUrlList;
    private final FragmentManager mFm;
    private boolean isModify = true;


    public TaskPhotosAdapter(FragmentManager fragmentManager, ArrayList<File> filesForUpload, ArrayList<TaskFileResponse.TaskFiles> mTaskFilesUrlList) {
        mFileList = filesForUpload == null ? new ArrayList<File>() : filesForUpload;
        mFilesUrlList = mTaskFilesUrlList == null ? new ArrayList<TaskFileResponse.TaskFiles>() : mTaskFilesUrlList;
        mFm = fragmentManager;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_photo_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        if (getItemViewType(position) == TYPE_FILE) {
            Picasso.with(holder.itemView.getContext()).load(mFileList.get(position)).centerCrop().resize(50, 50).into(holder.mPhotoIv);
        }else {
            Picasso.with(holder.itemView.getContext()).load(mFilesUrlList.get(position - mFileList.size()).getmFilePath()).centerCrop().resize(50, 50).into(holder.mPhotoIv);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isModify) {
                    ShowImageDialog dialog;
                    if (getItemViewType(position) == TYPE_FILE) {
                        dialog = ShowImageDialog.newInstance(mFileList.get(position));
                    } else {
                        dialog = ShowImageDialog.newInstance(mFilesUrlList.get(position - mFileList.size()).getmFilePath());
                    }
                    dialog.show(mFm, "");
                    dialog.setupImage(v.getContext());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFileList.size() + mFilesUrlList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position < mFileList.size() ? TYPE_FILE : TYPE_URL;
    }

    public void setIsModifyCheckBox(boolean isModify) {
        this.isModify = isModify;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView mPhotoIv;

        public ViewHolder(View itemView) {
            super(itemView);
            mPhotoIv = itemView.findViewById(R.id.task_photo_item_iv);
        }
    }
}
