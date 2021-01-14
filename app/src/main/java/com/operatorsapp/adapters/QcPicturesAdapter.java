package com.operatorsapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.operatorsapp.R;

import java.util.List;

public class QcPicturesAdapter extends RecyclerView.Adapter<QcPicturesAdapter.ViewHolder> {

    private final List<String> mFileList;

    public QcPicturesAdapter(List<String> files) {
        mFileList = files;
//        mFileList.add("https://s3-eu-west-1.amazonaws.com/amg0/upload/612_Screenshot_20180503-16003.jpg");
//        mFileList.add("https://s3-eu-west-1.amazonaws.com/amg0/upload/577_dev_154288259757.jpg");
//        mFileList.add("https://s3-eu-west-1.amazonaws.com/amg0/upload/576_dev_154288236047.jpg");
//        mFileList.add("https://s3-eu-west-1.amazonaws.com/amg0/upload/612_Screenshot_20180503-16003.jpg");
//        mFileList.add("https://s3-eu-west-1.amazonaws.com/amg0/upload/577_dev_154288259757.jpg");
//        mFileList.add("https://s3-eu-west-1.amazonaws.com/amg0/upload/576_dev_154288236047.jpg");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.picture_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ImageLoader.getInstance().displayImage(mFileList.get(position), holder.pictureIv);
    }

    @Override
    public int getItemCount() {
        return mFileList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView pictureIv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pictureIv = itemView.findViewById(R.id.picture_adapter_container_fl);
        }
    }
}
