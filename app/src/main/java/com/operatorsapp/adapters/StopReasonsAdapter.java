package com.operatorsapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.operators.reportfieldsformachineinfra.StopReasons;
import com.operatorsapp.R;
import com.operatorsapp.fragments.interfaces.OnStopReasonSelectedCallbackListener;

import java.util.List;

/**
 * Created by Sergey on 09/08/2016.
 */
public class StopReasonsAdapter extends RecyclerView.Adapter<StopReasonsAdapter.ViewHolder> {

    private List<StopReasons> mStopItemsList;
    private Context mContext;
    private OnStopReasonSelectedCallbackListener mOnStopReasonSelectedCallbackListener;

    public StopReasonsAdapter(Context context, List<StopReasons> stopItemsList, OnStopReasonSelectedCallbackListener onStopReasonSelectedCallbackListener) {
        mStopItemsList = stopItemsList;
        mContext = context;
        mOnStopReasonSelectedCallbackListener = onStopReasonSelectedCallbackListener;
    }

    @Override
    public StopReasonsAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stop_report_grid_item, parent, false);

        final ViewHolder holder = new ViewHolder(view);


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnStopReasonSelectedCallbackListener.onStopReasonSelected(holder.getAdapterPosition());
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mStopTitle.setText(mStopItemsList.get(position).getName());
        holder.mReasonImage.setBackground(mContext.getResources().getDrawable(getImageForStopReason(mStopItemsList.get(position).getId())));
    }

    @Override
    public int getItemCount() {
        return mStopItemsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mStopTitle;
        private ImageView mReasonImage;

        public ViewHolder(View view) {
            super(view);
            mStopTitle = (TextView) view.findViewById(R.id.stop_reason_title);
            mReasonImage = (ImageView) view.findViewById(R.id.grid_reason_image_view);
        }
    }

    private int getImageForStopReason(int stopReasonId) {
        int imageId = R.drawable.stop_general_selector;
        switch (stopReasonId) {
            case 1: {
                imageId = R.drawable.stop_settings_selector;
                break;
            }
            case 2: {
                imageId = R.drawable.stop_machinestop_selector;
                break;
            }
            case 3: {
                imageId = R.drawable.stop_oparations_selector;
                break;
            }
            case 4: {
                imageId = R.drawable.stop_planning_selector;
                break;
            }
            case 5: {
                imageId = R.drawable.stop_qa_selector;
                break;
            }
            case 6: {
                imageId = R.drawable.stop_materials_selector;
                break;
            }
            case 7: {
                imageId = R.drawable.stop_malfunction_selector;
                break;
            }
            case 8: {
                imageId = R.drawable.stop_maitenance_selector;
                break;
            }
            case 9: {
                imageId = R.drawable.stop_general_selector;
                break;
            }
        }
        return imageId;
    }
}
