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

public class No0ChanneAdapter extends RecyclerView.Adapter<No0ChanneAdapter.ViewHolder> {

    public static final int TYPE_CHANNEL_100 = 100;
    public static final int TYPE_CHANNEL_1_99 = 99;
    private final Context mContext;
    private final Channel100AdapterListener mListener;
    private final ArrayList<ChannelSplits> mChannelSplits;
    private final int mType;
    private View mMainView;

    public No0ChanneAdapter(Context context, Channel100AdapterListener listener, ArrayList<ChannelSplits> channelSplits, int type) {

        mContext = context;
        mListener = listener;
        mChannelSplits = channelSplits;
        mType = type;
    }


    @NonNull
    @Override
    public No0ChanneAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (mType == TYPE_CHANNEL_100){

            mMainView = inflater.inflate(R.layout.item_product_100, parent, false);

        }else if (mType == TYPE_CHANNEL_1_99){

            mMainView = inflater.inflate(R.layout.item_product_1_99, parent, false);

        }

        return new No0ChanneAdapter.ViewHolder(mMainView);
    }

    @Override
    public void onBindViewHolder(@NonNull final No0ChanneAdapter.ViewHolder viewHolder, final int position) {

        initSplits(viewHolder, position);

        viewHolder.mTitle.setText(mChannelSplits.get(position).getName());

        if (mChannelSplits.get(position).getMaterialInformation() != null ) {
            viewHolder.mSubTitle.setText(mChannelSplits.get(position).getMaterialInformation().getName() + "");

            if (mType == TYPE_CHANNEL_100) {
                ((TextView)viewHolder.itemView.findViewById(R.id.IP_sub_title_id)).setText(mChannelSplits.get(position).getMaterialInformation().getCatalogID());
            }

            if (mChannelSplits.get(position).getMaterialInformation().getFileUrl() != null && mChannelSplits.get(position).getMaterialInformation().getFileUrl().get(0) != null) {

                ImageLoader.getInstance().displayImage(mChannelSplits.get(position).getMaterialInformation().getFileUrl().get(0), viewHolder.mImage);

                if (mType == TYPE_CHANNEL_100){

                    (viewHolder.itemView.findViewById(R.id.IP_vertical_separator)).setVisibility(View.VISIBLE);
                    (viewHolder.itemView.findViewById(R.id.IP_img)).setVisibility(View.VISIBLE);

                }

            }else if (mType == TYPE_CHANNEL_100){

                (viewHolder.itemView.findViewById(R.id.IP_vertical_separator)).setVisibility(View.GONE);
                (viewHolder.itemView.findViewById(R.id.IP_img)).setVisibility(View.GONE);
            }
        }
    }

    private void initSplits(ViewHolder viewHolder, int position) {
        if (mChannelSplits.get(position).getBaseSplits() != null) {

            for (BaseSplits baseSplits : mChannelSplits.get(position).getBaseSplits()) {

                LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                View itemView = layoutInflater.inflate((R.layout.item_split), (ViewGroup) mMainView, false);

                ((TextView) itemView.findViewById(R.id.IS_tv)).setText(baseSplits.getPropertyName());

                ((TextView)itemView.findViewById(R.id.IS_tv_2)).setText(baseSplits.getFValue() + "");

                if (baseSplits.getRange() != null && baseSplits.getRange().length() > 0){

                    ((TextView)itemView.findViewById(R.id.IS_range_tv)).setVisibility(View.VISIBLE);

                    ((TextView)itemView.findViewById(R.id.IS_range_tv)).setText(baseSplits.getRange());

                }else{

                    ((TextView)itemView.findViewById(R.id.IS_range_tv)).setVisibility(View.GONE);
                }

                viewHolder.mSplitLy.addView(itemView);

            }

        }
    }

    @Override
    public int getItemCount() {
        if (mChannelSplits != null) {
            return mChannelSplits.size();
        } else return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final LinearLayout mSplitLy;
        private final TextView mTitle;
        private final TextView mSubTitle;
        private final ImageView mImage;

        ViewHolder(View itemView) {
            super(itemView);

            mSplitLy = itemView.findViewById(R.id.IP_split_ly);
            mTitle = itemView.findViewById(R.id.IP_title);
            mSubTitle = itemView.findViewById(R.id.IP_sub_title);
            mImage = itemView.findViewById(R.id.IP_img);
        }

    }

    public interface Channel100AdapterListener {


    }
}
