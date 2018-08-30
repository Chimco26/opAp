package com.operatorsapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.operatorsapp.R;
import com.operatorsapp.model.GalleryModel;

import java.util.ArrayList;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    private final ArrayList<GalleryModel> mGalleryModels;

    private final Context mContext;

    private GalleryAdapterListener mListener;


    public GalleryAdapter(ArrayList<GalleryModel> fileUrls, GalleryAdapterListener listener, Context context) {

        mGalleryModels = fileUrls;

        mListener = listener;

        mContext = context;
    }


    @NonNull
    @Override
    public GalleryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        return new GalleryAdapter.ViewHolder(inflater.inflate(R.layout.item_gallery, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final GalleryAdapter.ViewHolder viewHolder, final int position) {

        if (mGalleryModels.get(position).isSelected()) {

            viewHolder.mImgLy.setBackground(mContext.getResources().getDrawable(R.drawable.background_item_gallery_selected));

        } else {

            viewHolder.mImgLy.setBackground(mContext.getResources().getDrawable(R.drawable.background_item_gallery));
        }

        if (mGalleryModels.get(position).getUrl().endsWith("pdf")) {

            viewHolder.mImg.setImageResource(R.drawable.ic_pdf);

        }else {

            ImageLoader.getInstance().displayImage(mGalleryModels.get(position).getUrl(), viewHolder.mImg);

        }

        viewHolder.mImgLy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                viewHolder.mImgLy.setBackground(mContext.getResources().getDrawable(R.drawable.background_item_gallery_selected));

                resetSelectedItems();

                mGalleryModels.get(position).setSelected(true);

                if (!mGalleryModels.get(position).getUrl().endsWith("pdf")) {

                    mListener.onImageClick(mGalleryModels.get(position));

                } else {

                    mListener.onPdfClick(mGalleryModels.get(position));

                }
            }
        });


    }

    private void resetSelectedItems() {

        for (GalleryModel galleryModel : mGalleryModels) {

            galleryModel.setSelected(false);
        }
    }

    @Override
    public int getItemCount() {
        if (mGalleryModels != null) {
            return mGalleryModels.size();
        } else return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView mImg;

        private final View mImgLy;

        ViewHolder(View itemView) {
            super(itemView);

            mImg = itemView.findViewById(R.id.IG_img);

            mImgLy = itemView.findViewById(R.id.IG_image_ly);

        }

    }

    public interface GalleryAdapterListener {

        void onImageClick(GalleryModel galleryModel);

        void onPdfClick(GalleryModel galleryModel);
    }
}
