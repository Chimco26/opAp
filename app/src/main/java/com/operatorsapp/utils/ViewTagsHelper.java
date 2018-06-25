package com.operatorsapp.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.operators.reportrejectnetworkbridge.server.response.Recipe.ChannelSplits;
import com.operatorsapp.R;
import com.operatorsapp.adapters.No0ChanneAdapter;

import java.util.ArrayList;
import java.util.List;


public class ViewTagsHelper {

    private static final float TITLE_MARGIN_TOP = -5;
    private static final float TITLE_TEXT_SIZE = 25;
    private static final float RV_MARGIN_TOP = 10;
    private static final int SEPARATOR_HEIGHT = 2;
    private static final float SEPARATOR_MARGIN_TOP = 15;
    private static final int SEPARATOR_MARGIN_BOTTOM = 15;
    private static final float TITLE_HEIGHT = 35;
    private static float TITLE_MARGIN_LEFT = 0;

    public static void addTitle(Context context , String question , LinearLayout mainView) {

        float mDensity = context.getResources().getDisplayMetrics().density;

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, (int) (TITLE_HEIGHT * mDensity));

        layoutParams.setMargins((int) (TITLE_MARGIN_LEFT * mDensity), (int) (TITLE_MARGIN_TOP * mDensity),0, 0);

        TextView textView = new TextView(context);

        textView.setTextColor(context.getResources().getColor(R.color.blue1));

        textView.setGravity(Gravity.CENTER_VERTICAL);

        textView.setTextSize(TITLE_TEXT_SIZE);

        Typeface tf = Typeface.createFromAsset(context.getAssets(),
                "fonts/BreeSerif-Regular.ttf");

        textView.setTypeface(tf);

        textView.setText(question);

        textView.setLayoutParams(layoutParams);

        mainView.addView(textView);

    }

    public static void addRv(Context context, List<ChannelSplits> channelSplits, LinearLayout mainView, No0ChanneAdapter.Channel100AdapterListener listener) {

        float mDensity = context.getResources().getDisplayMetrics().density;

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(0, (int) (RV_MARGIN_TOP * mDensity), 0, 0);

        RecyclerView recyclerView = new RecyclerView(context);

        recyclerView.setLayoutParams(layoutParams);

        mainView.addView(recyclerView);

        recyclerView.setNestedScrollingEnabled(false);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);

        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(new No0ChanneAdapter(context, listener, (ArrayList<ChannelSplits>) channelSplits, No0ChanneAdapter.TYPE_CHANNEL_1_99));

    }

    public static void addSeparator(Context context , LinearLayout mainView) {

        float mDensity = context.getResources().getDisplayMetrics().density;

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, (int) (SEPARATOR_HEIGHT * mDensity));

        layoutParams.setMargins(0, (int) (SEPARATOR_MARGIN_TOP * mDensity),0, (int) (SEPARATOR_MARGIN_BOTTOM * mDensity));

        View view = new View(context);

        view.setLayoutParams(layoutParams);

        view.setBackgroundColor(context.getResources().getColor(R.color.default_gray));

        mainView.addView(view);

    }


}
