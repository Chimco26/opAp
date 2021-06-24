package com.operatorsapp.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.operatorsapp.R;
import com.operatorsapp.model.GalleryModel;

import java.util.ArrayList;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    private final ArrayList<GalleryModel> mGalleryModels;

    private GalleryAdapterListener mListener;
    private int mItemLayoutId;


    public GalleryAdapter(ArrayList<GalleryModel> fileUrls, GalleryAdapterListener listener, int itemLayoutId) {

        mGalleryModels = fileUrls;

        mListener = listener;

        mItemLayoutId = itemLayoutId;
    }


    @NonNull
    @Override
    public GalleryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        return new GalleryAdapter.ViewHolder(inflater.inflate(mItemLayoutId, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final GalleryAdapter.ViewHolder viewHolder, final int position) {

        if (mItemLayoutId == R.layout.item_gallery) {
            viewHolder.mImgLy.post(new Runnable() {
                @Override
                public void run() {
                    if (viewHolder.itemView.getContext() != null
                            && viewHolder.itemView.getContext() instanceof Activity
                            && !((Activity) viewHolder.itemView.getContext()).isDestroyed()) {
                        ViewGroup.MarginLayoutParams mItemViewParams4;
                        mItemViewParams4 = (ViewGroup.MarginLayoutParams) viewHolder.mImgLy.getLayoutParams();
                        mItemViewParams4.width = viewHolder.itemView.getResources().getDisplayMetrics().widthPixels / 6;
                        viewHolder.mImgLy.requestLayout();
                    }
                }
            });
        }

        if (mGalleryModels.get(position).isSelected()) {

            viewHolder.mImgLy.setBackground(viewHolder.itemView.getResources().getDrawable(R.drawable.background_item_gallery_selected));

        } else {

            viewHolder.mImgLy.setBackground(viewHolder.itemView.getResources().getDrawable(R.drawable.background_item_gallery));
        }

        if (mGalleryModels.get(position).getUrl().endsWith("pdf")) {

            viewHolder.mImg.setImageResource(R.drawable.ic_pdf);

        } else {

            ImageLoader.getInstance().displayImage(mGalleryModels.get(position).getUrl(), viewHolder.mImg);

        }

        viewHolder.mImgLy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mItemLayoutId == R.layout.item_gallery) {

                    viewHolder.mImgLy.setBackground(viewHolder.itemView.getResources().getDrawable(R.drawable.background_item_gallery_selected));
                    resetSelectedItems();
                    mGalleryModels.get(position).setSelected(true);
                }

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
