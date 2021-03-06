package com.operatorsapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.operators.reportrejectnetworkbridge.server.response.Recipe.BaseSplits;
import com.operators.reportrejectnetworkbridge.server.response.Recipe.ChannelSplitName;
import com.operators.reportrejectnetworkbridge.server.response.Recipe.ChannelSplits;
import com.operatorsapp.R;
import com.operatorsapp.application.OperatorApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class No0ChannelAdapter extends RecyclerView.Adapter<No0ChannelAdapter.ViewHolder> {

    public static final int TYPE_CHANNEL_100 = 100;
    public static final int TYPE_CHANNEL_1_99 = 99;
    private final Channel100AdapterListener mListener;
    private final ArrayList<ChannelSplits> mChannelSplits;
    private final int mType;
    private final List<ChannelSplitName> channelSplitNames;
    private View mMainView;

    public No0ChannelAdapter(Channel100AdapterListener listener, ArrayList<ChannelSplits> channelSplits, int type, List<ChannelSplitName> channelSplitName) {
        mListener = listener;
        mChannelSplits = channelSplits;
        mType = type;
        channelSplitNames = channelSplitName;
    }


    @NonNull
    @Override
    public No0ChannelAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (mType == TYPE_CHANNEL_100) {

            mMainView = inflater.inflate(R.layout.item_product_100, parent, false);

        } else if (mType == TYPE_CHANNEL_1_99) {

            mMainView = inflater.inflate(R.layout.item_product_1_99, parent, false);

        }

        return new No0ChannelAdapter.ViewHolder(mMainView);
    }

    @Override
    public void onBindViewHolder(@NonNull final No0ChannelAdapter.ViewHolder viewHolder, int position) {


        initSplits(viewHolder, position);

        viewHolder.mTitle.setText(String.format(Locale.getDefault(),
                "%s - %d", viewHolder.itemView.getContext().getResources()
                        .getString(R.string.source), mChannelSplits.get(position).getSplitNumber()));

        if (mChannelSplits.get(position).getBaseSplits() != null && mChannelSplits.get(position).getBaseSplits().size() > 0) {

            viewHolder.mSubTitle.setText(String.format("%s",
                    OperatorApplication.isEnglishLang() ?
                            mChannelSplits.get(viewHolder.getAdapterPosition()).getBaseSplits().get(0).getMaterialEName()
                            : mChannelSplits.get(viewHolder.getAdapterPosition()).getBaseSplits().get(0).getMaterialLName()));
            if (mType == TYPE_CHANNEL_100) {
                ((TextView) viewHolder.itemView.findViewById(R.id.IP_sub_title_id)).setText(mChannelSplits.get(position).getBaseSplits().get(0).getCatalogID());
            }

            if (mChannelSplits.get(position).getBaseSplits().get(0).getMaterialImageLink() != null
                    && !mChannelSplits.get(position).getBaseSplits().get(0).getMaterialImageLink().isEmpty()) {

                ImageLoader.getInstance().displayImage(mChannelSplits.get(position).getBaseSplits().get(0).getMaterialImageLink(), viewHolder.mImage);

                viewHolder.mImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String splitLName = mChannelSplits.get(viewHolder.getAdapterPosition()).getSplitLName(mChannelSplits.get(viewHolder.getAdapterPosition()).getSplitNumber(), channelSplitNames);
                        splitLName = splitLName != null ? splitLName : mChannelSplits.get(viewHolder.getAdapterPosition()).geteName();
                        String name = OperatorApplication.isEnglishLang() ? mChannelSplits.get(viewHolder.getAdapterPosition()).geteName() : splitLName;
                        mListener.onImageProductClick(mChannelSplits.get(viewHolder.getAdapterPosition()).getBaseSplits().get(0).getMaterialImageLink(), name);

                    }
                });

                if (mType == TYPE_CHANNEL_100) {

                    (viewHolder.itemView.findViewById(R.id.IP_vertical_separator)).setVisibility(View.VISIBLE);
                    viewHolder.mImage.setVisibility(View.VISIBLE);

                }

            } else if (mType == TYPE_CHANNEL_100) {

                (viewHolder.itemView.findViewById(R.id.IP_vertical_separator)).setVisibility(View.GONE);
                viewHolder.mImage.setVisibility(View.GONE);
            }

        } else if (mType == TYPE_CHANNEL_100) {

            (viewHolder.itemView.findViewById(R.id.IP_vertical_separator)).setVisibility(View.GONE);
            viewHolder.mImage.setVisibility(View.GONE);
        }
    }

    private void initSplits(ViewHolder viewHolder, int position) {

        if (mChannelSplits.get(position).getBaseSplits() != null) {

            ChannelItemsAdapters channelItemsAdapters = new ChannelItemsAdapters(
                    mChannelSplits.get(position).getBaseSplits(), new ChannelItemsAdapters.ChannelItemsAdaptersListener() {
                @Override
                public void onEditMode(BaseSplits item) {
                    mListener.onEditMode(item);
                }
            });

            LinearLayoutManager layoutManager
                    = new LinearLayoutManager(viewHolder.itemView.getContext(), LinearLayoutManager.VERTICAL, false);

            viewHolder.mSplitRv.setLayoutManager(layoutManager);

            viewHolder.mSplitRv.setAdapter(channelItemsAdapters);

        }

    }

    @Override
    public int getItemCount() {
        if (mChannelSplits != null) {
            return mChannelSplits.size();
        } else return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final RecyclerView mSplitRv;
        private final TextView mTitle;
        private final TextView mSubTitle;
        private final ImageView mImage;

        ViewHolder(View itemView) {
            super(itemView);

            mSplitRv = itemView.findViewById(R.id.IP_split_rv);
            mTitle = itemView.findViewById(R.id.IP_title);
            mSubTitle = itemView.findViewById(R.id.IP_sub_title);
            mImage = itemView.findViewById(R.id.IP_img);

        }

    }

    public interface Channel100AdapterListener {

        void onImageProductClick(String fileUrl, String s);

        void onEditMode(BaseSplits item);
    }
}
