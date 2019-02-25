package com.operatorsapp.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

public class FocusableRecycleView extends RecyclerView {

    public FocusableRecycleView(Context context) {
        super(context);
    }

    public FocusableRecycleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FocusableRecycleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onCheckIsTextEditor() {
        // this is where the magic happens
        return false;
    }
}