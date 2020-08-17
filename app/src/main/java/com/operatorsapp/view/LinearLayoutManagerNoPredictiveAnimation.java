package com.operatorsapp.view;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;

public class LinearLayoutManagerNoPredictiveAnimation extends LinearLayoutManager {
    public LinearLayoutManagerNoPredictiveAnimation(Context context) {
        super(context);
    }

    @Override
    public boolean supportsPredictiveItemAnimations() {
        return false;
    }
}
