package com.operatorsapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.operatorsapp.R;
import com.operatorsapp.model.TopFiveItem;
import com.operatorsapp.utils.TimeUtils;

import java.util.ArrayList;

/**
 * Created by alex on 19/12/2018.
 */

public class TopFiveAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_STOP = 0;
    public static final int TYPE_REJECT = 1;
    private final int mListType;

    private int mParentWidth;
    private double mTotalSum;
    private ArrayList<TopFiveItem> mTopList;
    private final Context mContext;

    public TopFiveAdapter(Context context, ArrayList<TopFiveItem> topFiveList, int type){
        mContext = context;
        mListType = type;
        setmTopList(topFiveList);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.top_five_recycler_item, parent, false);
        mParentWidth = parent.getWidth() - 100;

        switch (mListType){
            case TYPE_REJECT:
                return new RejectViewHolder(v);
            case TYPE_STOP:
                return new StopEventViewHolder(v);
            default: return null;

        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        double amount = Math.abs(Double.parseDouble(mTopList.get(position).getmAmount()));
        int width;
        if (amount < 1 || mTotalSum == 0){
            width = 50;
        }else {
            width = (int) ((mParentWidth -50) * (Math.abs(amount) / mTotalSum)) + 50;
        }

        switch (mListType){
            case TYPE_REJECT:

                final RejectViewHolder rejectViewHolder = (RejectViewHolder) holder;
                rejectViewHolder.topTv.setText(mTopList.get(position).getmAmount());
                rejectViewHolder.topText.setText(mTopList.get(position).getmText());
                rejectViewHolder.topLil.setLayoutParams(new LinearLayout.LayoutParams(width,ViewGroup.LayoutParams.WRAP_CONTENT));

                break;

            case TYPE_STOP:

                final StopEventViewHolder stopEventViewHolder = (StopEventViewHolder) holder;
                stopEventViewHolder.topTv.setText(TimeUtils.getHMSFromMillis(Double.parseDouble(mTopList.get(position).getmAmount())));
                stopEventViewHolder.topText.setText(mTopList.get(position).getmText());
                stopEventViewHolder.topLil.setLayoutParams(new LinearLayout.LayoutParams(width,ViewGroup.LayoutParams.WRAP_CONTENT));
                try{
                    stopEventViewHolder.topView.setBackgroundColor(Color.parseColor(mTopList.get(position).getmColor()));
                }catch (IllegalArgumentException e){
                    stopEventViewHolder.topView.setBackgroundColor(mContext.getResources().getColor(R.color.dialog_text_gray));
                }

                break;
        }

    }

    @Override
    public int getItemCount() {
        return mTopList.size();
    }

    public class StopEventViewHolder extends RecyclerView.ViewHolder {
        private View topView;
        private TextView topTv;
        private TextView topText;
        private LinearLayout topLil;

        public StopEventViewHolder(View itemView) {
            super(itemView);

            topLil = itemView.findViewById(R.id.item_top_five_lil);
            topTv = itemView.findViewById(R.id.item_top_five_tv);
            topText = itemView.findViewById(R.id.item_top_five_text_tv);
            topView= itemView.findViewById(R.id.item_top_five_view);
        }
    }

    public class RejectViewHolder extends RecyclerView.ViewHolder {
        private View topView;
        private TextView topTv;
        private TextView topText;
        private LinearLayout topLil;

        public RejectViewHolder(View itemView) {
            super(itemView);

            topLil = itemView.findViewById(R.id.item_top_five_lil);
            topTv = itemView.findViewById(R.id.item_top_five_tv);
            topText = itemView.findViewById(R.id.item_top_five_text_tv);
            topView= itemView.findViewById(R.id.item_top_five_view);

        }
    }


    public ArrayList<TopFiveItem> getmTopList() {
        return mTopList;
    }

    public void setmTopList(ArrayList<TopFiveItem> mTopList) {
        this.mTopList = mTopList;
        mTotalSum = 0;
        for (TopFiveItem item : mTopList) {
            mTotalSum += Math.abs(Double.parseDouble(item.getmAmount()));
        }
        notifyDataSetChanged();
    }
}
