package com.operatorsapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.operators.reportfieldsformachineinfra.SubReasons;
import com.operatorsapp.R;
import com.operatorsapp.application.OperatorApplication;
import com.operatorsapp.fragments.interfaces.OnSelectedSubReasonListener;

import java.util.List;

public class StopSubReasonAdapter extends RecyclerView.Adapter<StopSubReasonAdapter.ViewHolder> {

    private static final String LOG_TAG = StopSubReasonAdapter.class.getSimpleName();
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

        return new ViewHolder(view);
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        if (mSelectedPosition == position) {
            holder.mReasonImage.setBackground(mContext.getDrawable(R.drawable.btn_pressed));
            holder.mImageTitle.setTextColor(Color.WHITE);
        } else {
            holder.mReasonImage.setBackground(mContext.getDrawable(R.drawable.stop_sub_selector));
            holder.mImageTitle.setTextColor(ContextCompat.getColorStateList(mContext, R.color.button_stop_text_selector));
        }

        String nameByLang = OperatorApplication.isEnglishLang() ? mSubReasonsList.get(position).getEName() : mSubReasonsList.get(position).getLName();
        holder.mStopTitle.setText(nameByLang);
        char firstLetter = nameByLang.charAt(0);
        holder.mImageTitle.setText(String.valueOf(firstLetter));

        try {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSelectedPosition = position;
                    mOnSelectedSubReasonListener.onSubReasonSelected(mSubReasonsList.get(position).getId());


                }
            });

        } catch (ArrayIndexOutOfBoundsException e) {

            if(e.getMessage()!=null)

            Log.e(LOG_TAG, e.getMessage());

        }
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
