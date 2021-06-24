package com.operatorsapp.view.widgetViewHolders;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.operators.machinedatainfra.models.Widget;
import com.operatorsapp.R;
import com.operatorsapp.application.OperatorApplication;
import com.operatorsapp.interfaces.DashboardCentralContainerListener;

public class CounterViewHolder extends RecyclerView.ViewHolder {

    private final int mHeight;
    private final int mWidth;
    private LinearLayout mParentLayout;
    private View mDivider;
    private TextView mTitle;
    private TextView mSubtitle;
    private ImageView mAdd;
    private TextView mValue1;
    private TextView mValue10;
    private TextView mValue100;
    private TextView mValue1000;
    private TextView mValue10000;
    private DashboardCentralContainerListener mDashboardCentralContainerListener;

    public CounterViewHolder(View itemView, DashboardCentralContainerListener listener, int height, int width) {
        super(itemView);

        mHeight = height;
        mWidth = width;
        mDashboardCentralContainerListener = listener;

        mParentLayout = itemView.findViewById(R.id.widget_parent_layout);
        mDivider = itemView.findViewById(R.id.divider);
        mTitle = itemView.findViewById(R.id.counter_widget_title);
        mValue1 = itemView.findViewById(R.id.counter_widget_value_tv1);
        mValue10 = itemView.findViewById(R.id.counter_widget_value_tv10);
        mValue100 = itemView.findViewById(R.id.counter_widget_value_tv100);
        mValue1000 = itemView.findViewById(R.id.counter_widget_value_tv1000);
        mValue10000 = itemView.findViewById(R.id.counter_widget_value_tv10000);
        mSubtitle = itemView.findViewById(R.id.counter_widget_subtitle);
        mAdd = itemView.findViewById(R.id.counter_widget_add_iv);

    }

    public void setCounterItem(Widget widget) {
//        mDivider.post(new Runnable() {
//            @Override
//            public void run() {
//                ViewGroup.MarginLayoutParams mItemViewParams4;
//                mItemViewParams4 = (ViewGroup.MarginLayoutParams) mDivider.getLayoutParams();
//                mItemViewParams4.setMargins(0, (int) (mParentLayout.getHeight() * 0.3), 0, 0);
//                mDivider.requestLayout();
//            }
//        });
//
//
        setSizes(mParentLayout);
        String nameByLang4 = OperatorApplication.isEnglishLang() ? widget.getFieldEName() : widget.getFieldLName();
        mTitle.setText(nameByLang4);

        final int[] value = {0};
        try {
            value[0] = Integer.parseInt(widget.getCurrentValue());
        } catch (NumberFormatException e) {
        }

        mValue1.setText(value[0] % 10 + "");
        mValue10.setText((value[0] / 10) % 10 + "");
        mValue100.setText((value[0] / 100) % 10 + "");
        mValue1000.setText((value[0] / 1000) % 10 + "");
        mValue10000.setText((value[0] / 10000) % 10 + "");

        if (value[0] > 99999) {
            mSubtitle.setVisibility(View.VISIBLE);
            mSubtitle.setText(mSubtitle.getContext().getResources().getString(R.string.total_amount) + " " + value[0]);
        } else {
            mSubtitle.setVisibility(View.INVISIBLE);
        }

        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDashboardCentralContainerListener.onCounterPressedInCentralDashboardContainer(value[0]);
            }
        });

        // TODO: 16/12/2018 complete logic
    }

    private void setSizes(final LinearLayout parent) {
        ViewGroup.LayoutParams layoutParams;
        layoutParams = parent.getLayoutParams();
        layoutParams.height = (int) (mHeight * 0.5);
        layoutParams.width = (int) (mWidth * 0.325);
        parent.requestLayout();

    }
}
