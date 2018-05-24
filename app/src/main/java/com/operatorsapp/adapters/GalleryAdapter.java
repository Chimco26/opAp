package com.operatorsapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.operators.reportrejectnetworkbridge.server.response.Recipe.BaseSplits;
import com.operators.reportrejectnetworkbridge.server.response.Recipe.ChannelSplits;
import com.operatorsapp.R;

import java.util.ArrayList;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    private final ArrayList<String> mFileUrls;

    public GalleryAdapter(ArrayList<String> fileUrls) {

        mFileUrls = fileUrls;
    }


    @NonNull
    @Override
    public GalleryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        return new GalleryAdapter.ViewHolder(inflater.inflate(R.layout.item_gallery, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final GalleryAdapter.ViewHolder viewHolder, final int position) {

        ImageLoader.getInstance().displayImage(mFileUrls.get(position), viewHolder.mImg);
    }

    @Override
    public int getItemCount() {
        if (mFileUrls != null) {
            return mFileUrls.size();
        } else return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView mImg;

        ViewHolder(View itemView) {
            super(itemView);

            mImg = itemView.findViewById(R.id.IG_img);
        }

    }
}
