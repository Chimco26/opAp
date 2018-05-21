package com.operatorsapp.fragments;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.operators.reportrejectnetworkbridge.server.response.Recipe.BaseSplits;
import com.operators.reportrejectnetworkbridge.server.response.Recipe.ChannelSplits;
import com.operators.reportrejectnetworkbridge.server.response.Recipe.RecipeData;
import com.operatorsapp.R;
import com.operatorsapp.adapters.ShiftLogSqlAdapter;

import java.util.ArrayList;
import java.util.List;

public class Channel100Adapter extends RecyclerView.Adapter<Channel100Adapter.ViewHolder> {

    private final Context mContext;
    private final Channel100AdapterListener mListener;
    private final ArrayList<ChannelSplits> mChannelSplits;
    private View mMainView;

    public Channel100Adapter(Context context, Channel100AdapterListener listener, ArrayList<ChannelSplits> channelSplits) {

        mContext = context;
        mListener = listener;
        mChannelSplits = channelSplits;
    }


    @NonNull
    @Override
    public Channel100Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        mMainView = inflater.inflate(R.layout.item_product_100, parent, false);

        return new Channel100Adapter.ViewHolder(mMainView);
    }

    @Override
    public void onBindViewHolder(@NonNull final Channel100Adapter.ViewHolder viewHolder, final int position) {

        initSplits(viewHolder, position);

        viewHolder.mTitle.setText(mChannelSplits.get(position).getName());

        if (mChannelSplits.get(position).getMaterialInformation() != null) {
            viewHolder.mSubTitle.setText(mChannelSplits.get(position).getMaterialInformation().describeContents());
            viewHolder.mSubTitleId.setText(mChannelSplits.get(position).getMaterialInformation().getCatalogID());
        }
    }

    private void initSplits(ViewHolder viewHolder, int position) {
        if (mChannelSplits.get(position).getBaseSplits() != null) {

            for (BaseSplits baseSplits : mChannelSplits.get(position).getBaseSplits()) {

                LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                View itemView = layoutInflater.inflate((R.layout.item_split), (ViewGroup) mMainView, false);

                ((TextView) itemView.findViewById(R.id.IS_tv1)).setText(baseSplits.getPropertyName());

//           TODO     ((TextView)itemView.findViewById(R.id.IS_tv1_nmbr)).setText(baseSplits.getFValue());

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
        private final TextView mSubTitleId;

        ViewHolder(View itemView) {
            super(itemView);

            mSplitLy = itemView.findViewById(R.id.IP_split_ly);
            mTitle = itemView.findViewById(R.id.IP_title);
            mSubTitle = itemView.findViewById(R.id.IP_sub_title);
            mSubTitleId = itemView.findViewById(R.id.IP_sub_title_id);
        }

    }

    public interface Channel100AdapterListener {


    }
}
