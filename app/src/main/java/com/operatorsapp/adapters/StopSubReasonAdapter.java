package com.operatorsapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.operators.reportfieldsformachineinfra.SubReasons;
import com.operatorsapp.R;
import com.operatorsapp.fragments.interfaces.OnSelectedSubReasonListener;

import java.util.List;

/**
 * Created by Sergey on 09/08/2016.
 */
public class StopSubReasonAdapter extends RecyclerView.Adapter<StopSubReasonAdapter.ViewHolder> {

    private List<SubReasons> mSubReasonsList;
    private Context mContext;
    private int mSelectedPosition = -1;
    private OnSelectedSubReasonListener mOnSelectedSubReasonListener;

    public StopSubReasonAdapter(OnSelectedSubReasonListener onSelectedSubReasonListener, Context context, List<SubReasons> subReasonsList) {
        mSubReasonsList = subReasonsList;
        mContext = context;
        mOnSelectedSubReasonListener = onSelectedSubReasonListener;
    }

    @Override
    public StopSubReasonAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sub_stop_reason_grid_item, parent, false);

        final ViewHolder holder = new ViewHolder(view);


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectedPosition = holder.getAdapterPosition();
                mOnSelectedSubReasonListener.onSubReasonSelected(mSubReasonsList.get(holder.getAdapterPosition()).getId());


            }
        });


        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (mSelectedPosition == position) {
            holder.mReasonImage.setBackground(mContext.getResources().getDrawable(R.drawable.btn_pressed));
            holder.mImageTitle.setTextColor(Color.WHITE);
        }else {
            holder.mReasonImage.setBackground(mContext.getResources().getDrawable(R.drawable.stop_sub_selector));
            holder.mImageTitle.setTextColor(mContext.getResources().getColorStateList(R.color.button_stop_text_selector));
        }

        holder.mStopTitle.setText(mSubReasonsList.get(position).getName());
        char firstLetter = mSubReasonsList.get(position).getName().charAt(0);
        holder.mImageTitle.setText(String.valueOf(firstLetter));
    }

    @Override
    public int getItemCount() {
        return mSubReasonsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mStopTitle;
        private ImageView mReasonImage;
        private TextView mImageTitle;

        public ViewHolder(View view) {
            super(view);
            mStopTitle = (TextView) view.findViewById(R.id.stop_reason_title);
            mReasonImage = (ImageView) view.findViewById(R.id.grid_reason_image_view);
            mImageTitle = (TextView) view.findViewById(R.id.stop_reason_image_title);
        }
    }

}
