package com.operatorsapp.view.widgetViewHolders;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.operatorsapp.R;

public class TopStopEventsViewHolder extends RecyclerView.ViewHolder{

    private final int mHeight;
    private final int mWidth;

    public TopStopEventsViewHolder(@NonNull View itemView, int height, int width) {
        super(itemView);

        mHeight = height;
        mWidth = width;
        LinearLayout parentLayout = itemView.findViewById(R.id.widget_parent_layout);
        setSizes(parentLayout);
    }

    private void setSizes(final LinearLayout parent) {
        ViewGroup.LayoutParams layoutParams;
        layoutParams = parent.getLayoutParams();
        layoutParams.height = (int) (mHeight * 0.5);
        layoutParams.width = (int) (mWidth * 0.325);
        parent.requestLayout();

    }
}
