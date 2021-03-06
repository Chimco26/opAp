package com.operatorsapp.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.operatorsapp.BuildConfig;
import com.operatorsapp.R;
import com.operatorsapp.application.OperatorApplication;
import com.operatorsapp.fragments.interfaces.OnStopReasonSelectedCallbackListener;
import com.operatorsapp.server.responses.StopReasonsGroup;
import com.operatorsapp.utils.ReasonImage;
import com.operatorsapp.utils.ReasonImageLenox;

import java.util.ArrayList;

/**
 * Created by alex on 26/12/2018.
 */

public class NewStopReasonsAdapter extends RecyclerView.Adapter<NewStopReasonsAdapter.ViewHolder> {

    private ArrayList<StopReasonsGroup> mStopItemsList;
    private OnStopReasonSelectedCallbackListener mOnStopReasonSelectedCallbackListener;

    public NewStopReasonsAdapter(ArrayList<StopReasonsGroup> stopItemsList, OnStopReasonSelectedCallbackListener onStopReasonSelectedCallbackListener) {
        mStopItemsList = stopItemsList;
        mOnStopReasonSelectedCallbackListener = onStopReasonSelectedCallbackListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stop_report_horizontal_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String nameByLang = OperatorApplication.isEnglishLang() ? mStopItemsList.get(position).getEName() : mStopItemsList.get(position).getLName();
        holder.mStopTitle.setText(nameByLang);
        int imgId;
        if (BuildConfig.FLAVOR.equals(holder.itemView.getContext().getString(R.string.lenox_flavor_name))) {
            imgId = ReasonImageLenox.getImageForStopReason(mStopItemsList.get(holder.getAdapterPosition()).getId());
        } else {
            imgId = mStopItemsList.get(position).getGroupIcon(holder.itemView.getContext());
//            if (imgId == 0){
//                imgId = ReasonImage.getImageForNewStopReason(mStopItemsList.get(holder.getAdapterPosition()).getId());
//            }
        }
        int color = (mStopItemsList.get(position).getGroupColor());
        if (color == 0) {
            color = ReasonImage.getColorForStopReason(mStopItemsList.get(holder.getAdapterPosition()).getId());
        }
        holder.mTitleLil.setBackgroundColor(color);
//        holder.mTitleLil.setBackgroundColor(ReasonImage.getColorForStopReason(mStopItemsList.get(holder.getAdapterPosition()).getId()));
//        holder.mTitleLil.setBackgroundColor(ReasonImage.getColorForStopReason(mStopItemsList.get(holder.getAdapterPosition()).getEventGroupColorID()));

        if (imgId == 0) {
            holder.mReasonImage.setVisibility(View.INVISIBLE);
        } else {
            holder.mReasonImage.setVisibility(View.VISIBLE);
            holder.mReasonImage.setBackground(holder.itemView.getContext().getResources().getDrawable(imgId));
        }
        holder.mHorizontalRv.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext(), LinearLayoutManager.VERTICAL, false));
        holder.mHorizontalRv.setAdapter(new VerticalAdapter(position, mStopItemsList.get(position).getSubReasons(), imgId));
//        int color = new Random().nextInt(4);
//        Color.parseColor(context.getResources().getStringArray(R.array.color_array)[color]);
        // holder.mTitleLil.setBackgroundColor(Color.parseColor(context.getResources().getStringArray(R.array.color_array)[color]));
    }

    @Override
    public int getItemCount() {
        return mStopItemsList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private final LinearLayout mTitleLil;
        private RecyclerView mHorizontalRv;
        private TextView mStopTitle;
        private ImageView mReasonImage;

        public ViewHolder(View view) {
            super(view);
            mStopTitle = view.findViewById(R.id.report_horizontal_tv);
            mReasonImage = view.findViewById(R.id.report_horizontal_iv);
            mHorizontalRv = view.findViewById(R.id.report_vertical_rv);
            mTitleLil = view.findViewById(R.id.report_horizontal_lil);
        }
    }


    /****  Vertical Adapter  ****/

    private class VerticalAdapter extends RecyclerView.Adapter<VerticalAdapter.VerticalViewHolder> {
        private final ArrayList<StopReasonsGroup> mVerticalList;
        private final int mImgId;
        private final int mGroupReasonPosition;

        public VerticalAdapter(int groupReasonPosition, ArrayList<StopReasonsGroup> subReasons, int imgId) {
            mVerticalList = subReasons;
            mImgId = imgId;
            mGroupReasonPosition = groupReasonPosition;
        }

        @NonNull
        @Override
        public VerticalAdapter.VerticalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            final View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.stop_report_vertical_item, parent, false);

            return new VerticalViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull VerticalAdapter.VerticalViewHolder holder, final int position) {

            String color = mVerticalList.get(position).getmColorID();
            if (color == null || color.isEmpty()) {
                color = String.format("#%06x", holder.itemView.getContext().getResources().getColor(R.color.C2) & 0xffffff);
            }
            Configuration config = holder.itemView.getContext().getResources().getConfiguration();

            GradientDrawable.Orientation orientation = GradientDrawable.Orientation.RIGHT_LEFT;
            if (config.getLayoutDirection() != View.LAYOUT_DIRECTION_RTL) {
                orientation = GradientDrawable.Orientation.LEFT_RIGHT;
            }
            GradientDrawable gd = new GradientDrawable(
                    orientation, new int[] {adjustAlpha(Color.parseColor(color), 0.65f),Color.parseColor("#ffffff")
                    ,Color.parseColor("#ffffff"),Color.parseColor("#ffffff")});
            holder.itemView.setBackground(gd);
            String title = OperatorApplication.isEnglishLang() ? mVerticalList.get(position).getEName() : mVerticalList.get(position).getLName();
            holder.mVerticalTitle.setText(title);

            int icon = mVerticalList.get(position).getGroupIcon(holder.itemView.getContext());
            if (icon == 0) {
//                icon = mImgId;
                holder.mVerticalImage.setVisibility(View.INVISIBLE);
            } else {
                holder.mVerticalImage.setVisibility(View.VISIBLE);
                holder.mVerticalImage.setBackground(holder.itemView.getContext().getResources().getDrawable(icon));
            }
//            holder.mVerticalImage.setBackground(context.getResources().getDrawable(mVerticalList.get(position).getEventIcon(context)));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnStopReasonSelectedCallbackListener.onUpdateStopReasonSelected(mGroupReasonPosition);
                    mOnStopReasonSelectedCallbackListener.onSubReasonSelected(mVerticalList.get(position));
                }
            });
        }

        @Override
        public int getItemCount() {
            return mVerticalList == null ? 0 : mVerticalList.size();
        }

        public class VerticalViewHolder extends RecyclerView.ViewHolder {
            private final TextView mVerticalTitle;
            private final ImageView mVerticalImage;

            public VerticalViewHolder(View view) {
                super(view);
                mVerticalTitle = view.findViewById(R.id.report_vertical_tv);
                mVerticalImage = view.findViewById(R.id.report_vertical_iv);

            }
        }


    }

    @ColorInt
    public static int adjustAlpha(@ColorInt int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }
}
