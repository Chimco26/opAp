package com.operatorsapp.view.widgetViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.operators.machinedatainfra.models.Widget;
import com.operatorsapp.R;
import com.operatorsapp.application.OperatorApplication;
import com.operatorsapp.utils.StringUtil;
import com.operatorsapp.utils.TimeUtils;

import java.util.Date;
import java.util.Locale;

public class GenericTimeViewHolder extends RecyclerView.ViewHolder {
    private static final int END_LIMIT = 10;//in minute
    private final TextView m1EstimatedDate;
    private final int mHeight;
    private final int mWidth;
    private LinearLayout mParentLayout;
    private TextView mTitle;
    private TextView mSubTitle;
    private View m1Ly;
    private TextView m1TimeTv;
    private View m2CountDownLy;
    private CountDownView m3CountDownView;

    public GenericTimeViewHolder(View itemView, int height, int width) {
        super(itemView);

        mTitle = itemView.findViewById(R.id.HGT_title);
        mSubTitle = itemView.findViewById(R.id.HGT_subtitle);
        m1Ly = itemView.findViewById(R.id.HGT_time_ly);
        m1TimeTv = itemView.findViewById(R.id.HGT_time_tv);
        m1EstimatedDate = itemView.findViewById(R.id.HGT_estimated_tv);
        m2CountDownLy = itemView.findViewById(R.id.HGT_countdown_ly);
        m3CountDownView = itemView.findViewById(R.id.HGT_countdown);
        mHeight = height;
        mWidth = width;
        mParentLayout = itemView.findViewById(R.id.widget_parent_layout);
        setSizes(mParentLayout);
    }

    public void setData(Widget widget) {
        setView(widget);
    }

    private void setView(Widget widget) {
        String nameByLang2 = OperatorApplication.isEnglishLang() ? widget.getFieldEName() : widget.getFieldLName();
        mTitle.setText(nameByLang2);
        long time = Long.parseLong(widget.getCurrentValue());
        int endLimit = END_LIMIT;
//        time = 250;// test states
        if (time >= 60) {
            m1Ly.setVisibility(View.VISIBLE);
            m2CountDownLy.setVisibility(View.GONE);
            if (time > 24 * 60) {
                m1EstimatedDate.setVisibility(View.VISIBLE);
                m1EstimatedDate.setText(String.format("%s: %s", m1TimeTv.getContext().getString(R.string.estimated_date),
                        TimeUtils.convertMillisecondDateTo(new Date().getTime() + time * 60 * 1000)));
                m1TimeTv.setText(String.format(Locale.getDefault(), "%s %s", ((int) (time / 60)),
                        m1TimeTv.getContext().getString(R.string.hr2)));
//                m1TimeTv.setTextSize(2, 18);
            } else {
                m1EstimatedDate.setVisibility(View.GONE);
                m1TimeTv.setText(String.format(Locale.getDefault(), "%s %s %s %s", ((int) (time / 60)),
                        m1TimeTv.getContext().getString(R.string.hr2),
                        StringUtil.add0ToNumber((int) (time % 60)),
                        m1TimeTv.getContext().getString(R.string.min)));
//                m1TimeTv.setTextSize(2, 45);
            }
//            mSubTitle.setText(mSubTitle.getContext().getString(R.string.hr));
        } else {
            m1Ly.setVisibility(View.GONE);
            m2CountDownLy.setVisibility(View.VISIBLE);
            initCountDown((int) time, endLimit);
        }
    }

    private void initCountDown(int time, int endLimit) {
        m3CountDownView.setEndModeTimeInMinute(endLimit);
        m3CountDownView.update(time, m3CountDownView.getContext().getString(R.string.min));
    }

    private void setSizes(final LinearLayout parent) {
        ViewGroup.LayoutParams layoutParams;
        layoutParams = parent.getLayoutParams();
        layoutParams.height = (int) (mHeight * 0.5);
        layoutParams.width = (int) (mWidth * 0.325);
        parent.requestLayout();

    }

}
