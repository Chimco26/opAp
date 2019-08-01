package com.operatorsapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.operators.reportrejectnetworkbridge.server.response.Recipe.BaseSplits;
import com.operatorsapp.R;

import java.util.List;

public class ChannelItemsAdapters extends RecyclerView.Adapter<ChannelItemsAdapters.ViewHolder> {

    public static final int TYPE_CHANNEL_100 = 100;
    public static final int TYPE_CHANNEL_1_99 = 99;
    private final Context mContext;
    private final ChannelItemsAdaptersListener mListener;
    private final List<BaseSplits> baseSplits;
    private View mMainView;

    public ChannelItemsAdapters(Context context, ChannelItemsAdaptersListener listener, List<BaseSplits> channelSplits) {

        mContext = context;
        mListener = listener;
        baseSplits = channelSplits;
    }


    @NonNull
    @Override
    public ChannelItemsAdapters.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        mMainView = inflater.inflate(R.layout.item_split, parent, false);

        return new ChannelItemsAdapters.ViewHolder(mMainView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ChannelItemsAdapters.ViewHolder viewHolder, int position) {

        viewHolder.mTitle.setText(baseSplits.get(position).getPropertyName());
        viewHolder.mNumber.setText(baseSplits.get(position).getFValue());
        viewHolder.mRange.setText(baseSplits.get(position).getRange());

    }

    @Override
    public int getItemCount() {
        if (baseSplits != null) {
            return baseSplits.size();
        } else return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mTitle;
        private TextView mNumber;
        private TextView mRange;

        ViewHolder(View itemView) {
            super(itemView);

            mTitle = itemView.findViewById(R.id.IS_tv);
            mNumber = itemView.findViewById(R.id.IS_tv_2);
            mRange = itemView.findViewById(R.id.IS_range_tv);
        }

    }

    public interface ChannelItemsAdaptersListener {

    }
}
