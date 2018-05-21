package com.operatorsapp.utils;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.operatorsapp.R;


public class ViewTagsHelper {

    private static final float TITLE_MARGIN_TOP = 41;
    private static final float TITLE_TEXT_SIZE = 35;
    private static final float RV_MARGIN_TOP = 28;
    private static final int SEPARATOR_HEIGHT = 2;
    private static final float SEPARATOR_MARGIN_TOP = 50;

    public static void addTitle(Context context , String question , LinearLayout mainView) {

        float mDensity = context.getResources().getDisplayMetrics().density;

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(0, (int) (TITLE_MARGIN_TOP * mDensity),0, 0);

        TextView mTv = new TextView(context);

        mTv.setTextColor(context.getResources().getColor(R.color.dismiss_dialog));

        mTv.setGravity(Gravity.CENTER_VERTICAL);

        mTv.setTextSize(TITLE_TEXT_SIZE);

        mTv.setText(question);

        mTv.setLayoutParams(layoutParams);

        mainView.addView(mTv);

    }
//TODO replace Object
    public static void addRv(Context context, Object object, LinearLayout mainView, Object listener) {

        float mDensity = context.getResources().getDisplayMetrics().density;

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(0, (int) (RV_MARGIN_TOP * mDensity), 0, 0);

        RecyclerView mRecyclerView = new RecyclerView(context);

        mRecyclerView.setLayoutParams(layoutParams);

        mainView.addView(mRecyclerView);

        mRecyclerView.setNestedScrollingEnabled(false);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));

//      TODO  mRecyclerView.setAdapter(new Adapter);
    }

    public static void addSeparator(Context context , String question , LinearLayout mainView) {

        float mDensity = context.getResources().getDisplayMetrics().density;

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, (int) (SEPARATOR_HEIGHT * mDensity));

        layoutParams.setMargins(0, (int) (SEPARATOR_MARGIN_TOP * mDensity),0, 0);

        View view = new View(context);

        view.setLayoutParams(layoutParams);

        mainView.addView(view);

    }


}
