package com.operatorsapp.adapters;

import android.content.Context;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.operatorsapp.R;
import com.operatorsapp.model.TopFiveItem;
import com.operatorsapp.utils.TimeUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by alex on 19/12/2018.
 */

public class TopFiveAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_STOP = 0;
    public static final int TYPE_REJECT = 1;
    private final int mListType;

    private double mTotalSum;
    private ArrayList<TopFiveItem> mTopList;
    private final Context mContext;
    private ViewGroup mParent;

    public TopFiveAdapter(Context context, ArrayList<TopFiveItem> topFiveList, int type) {
        mContext = context;
        mListType = type;
        setmTopList(topFiveList);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.top_five_recycler_item, parent, false);
        mParent = parent;
        switch (mListType) {
            case TYPE_REJECT:
                return new RejectViewHolder(v);
            case TYPE_STOP:
                return new StopEventViewHolder(v);
            default:
                return null;

        }
    }

    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

        switch (mListType) {
            case TYPE_REJECT:

                final RejectViewHolder rejectViewHolder = (RejectViewHolder) holder;
                rejectViewHolder.topTv.setText(new DecimalFormat("#.#").format(Double.parseDouble(mTopList.get(position).getmAmount())));
                rejectViewHolder.topText.setText(mTopList.get(position).getmText());
                setBarSize(position, rejectViewHolder.topView);
                if (mTopList.get(position).getmColor() != null && !mTopList.get(position).getmColor().isEmpty()) {
                    rejectViewHolder.topView.setBackgroundColor(Color.parseColor(mTopList.get(position).getmColor()));
                    rejectViewHolder.topTv.setTextColor(Color.parseColor(mTopList.get(position).getmColor()));
                }
                break;

            case TYPE_STOP:

                final StopEventViewHolder stopEventViewHolder = (StopEventViewHolder) holder;
                stopEventViewHolder.topTv.setText(TimeUtils.getDecimalHourFromMillis(Double.parseDouble(mTopList.get(position).getmAmount())
                        , stopEventViewHolder.topTv.getContext().getString(R.string.hr2)
                        , stopEventViewHolder.topTv.getContext().getString(R.string.min)));
                stopEventViewHolder.topText.setText(mTopList.get(position).getmText());
                setBarSize(position, stopEventViewHolder.topView);
                try {
                    stopEventViewHolder.topView.setBackgroundColor(Color.parseColor(mTopList.get(position).getmColor()));
                    stopEventViewHolder.topTv.setTextColor(Color.parseColor(mTopList.get(position).getmColor()));
                } catch (IllegalArgumentException e) {
                    stopEventViewHolder.topView.setBackgroundColor(mContext.getResources().getColor(R.color.dialog_text_gray));
                }

                break;
        }

    }

    public void setBarSize(final int position, final View view) {
        mParent.post(new Runnable() {
            @Override
            public void run() {
                int parentWidth = mParent.getWidth() - 100;
                double amount = Math.abs(Double.parseDouble(mTopList.get(position).getmAmount()));
                int width;
                if (amount < 1 || mTotalSum == 0) {
                    width = 10;
                } else {
                    width = (int) ((parentWidth) * (Math.abs(amount) / mTotalSum));
                    if (width > (parentWidth * 80 / 100)) {
                        width = (parentWidth * 80 / 100);
                    }
                }
                ViewGroup.MarginLayoutParams mItemViewParams4;
                mItemViewParams4 = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
                mItemViewParams4.width = (int) width;
                view.requestLayout();
            }

        });
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
            topView = itemView.findViewById(R.id.item_top_five_view);
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
            topView = itemView.findViewById(R.id.item_top_five_view);

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
