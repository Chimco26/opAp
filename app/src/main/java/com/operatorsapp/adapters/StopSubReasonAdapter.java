package com.operatorsapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.operatorsapp.BuildConfig;
import com.operatorsapp.R;
import com.operatorsapp.application.OperatorApplication;
import com.operatorsapp.fragments.interfaces.OnSelectedSubReasonListener;
import com.operatorsapp.server.responses.StopReasonsGroup;
import com.operatorsapp.utils.ReasonImageLenox;

import java.util.ArrayList;

public class StopSubReasonAdapter extends RecyclerView.Adapter<StopSubReasonAdapter.ViewHolder> {

    private static final String LOG_TAG = StopSubReasonAdapter.class.getSimpleName();
    private final StopReasonsGroup mStopReason;
    private ArrayList<StopReasonsGroup> mSubReasonsList;
    private int mSelectedPosition = -1;
    private OnSelectedSubReasonListener mOnSelectedSubReasonListener;

    public StopSubReasonAdapter(OnSelectedSubReasonListener onSelectedSubReasonListener, StopReasonsGroup stopReason) {
        mSubReasonsList = stopReason.getSubReasons();
        mStopReason = stopReason;
        mOnSelectedSubReasonListener = onSelectedSubReasonListener;
    }

    @NonNull
    @Override
    public StopSubReasonAdapter.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {

        View view;
        if (com.operatorsapp.BuildConfig.FLAVOR.equals(parent.getContext().getString(R.string.lenox_flavor_name))) {

            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.sub_stop_reason_grid_item_lenox, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.sub_stop_reason_grid_item, parent, false);
        }
        return new ViewHolder(view);
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        Context context = holder.itemView.getContext();
        String nameByLang = OperatorApplication.isEnglishLang() ? mSubReasonsList.get(position).getEName() : mSubReasonsList.get(position).getLName();
        holder.mStopTitle.setText(nameByLang);

        if (BuildConfig.FLAVOR.equals(context.getString(R.string.lenox_flavor_name))) {

            holder.mStopTitle.setTextColor(Color.WHITE);
            holder.mReasonImage.setImageDrawable(context.getDrawable(ReasonImageLenox.getSubReasonIc(mStopReason.getId())));
            holder.itemView.setBackground(context.getDrawable(ReasonImageLenox.getSubReasonBackgroundColor(mStopReason.getId())));

        } else {
            holder.mStopTitle.setTextColor(context.getResources().getColor(R.color.color_jobs_row));
            String color = mSubReasonsList.get(position).getmColorID();
            if (color == null || color.isEmpty()) {
                color = String.format("#%06x", context.getColor(R.color.C2) & 0xffffff);
            }
            if (mSelectedPosition == position) {
//                holder.mImageTitle.setTextColor(Color.WHITE);
                GradientDrawable selector = (GradientDrawable) context.getDrawable(R.drawable.circle_withe_stroke);
                selector.setColor(Color.parseColor(color));

                holder.mReasonImage.setBackground(selector);
            } else {
                Drawable selector = context.getDrawable(R.drawable.simple_circle);
                selector.setTint(Color.parseColor(color));

                holder.mReasonImage.setBackground(selector);
//                holder.mImageTitle.setTextColor(ContextCompat.getColorStateList(context, R.color.button_stop_text_selector));
            }
//            if (nameByLang != null) {
//                char firstLetter = nameByLang.charAt(0);
////                holder.mImageTitle.setText(String.valueOf(firstLetter));
//            }
        }
        try {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSelectedPosition = position;
                    mOnSelectedSubReasonListener.onSubReasonSelected(mSubReasonsList.get(position));


                }
            });

        } catch (ArrayIndexOutOfBoundsException e) {

            if (e.getMessage() != null)

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
//        private TextView mImageTitle;

        public ViewHolder(View view) {
            super(view);
            mStopTitle = view.findViewById(R.id.stop_reason_title);
            mReasonImage = view.findViewById(R.id.grid_reason_image_view);
//            mImageTitle = view.findViewById(R.id.stop_reason_image_title);
        }
    }

}
