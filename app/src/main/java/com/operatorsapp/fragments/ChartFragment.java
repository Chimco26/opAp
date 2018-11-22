package com.operatorsapp.fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.data.Entry;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.operatorsapp.R;
import com.operatorsapp.application.OperatorApplication;
import com.operatorsapp.view.LineChartTimeLarge;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class ChartFragment extends BackStackAwareFragment {
    private static final String VALUES = "values";
    private static final String MIN = "mib";
    private static final String STANDARD = "standard";
    private static final String MAX = "max";
    private static final String X_VALUES = "xValues";
    private static final String FIELD_NAME = "fieldName";
    private static final String MIDNIGHT = "MIDNIGHT";

    private static final String LOG_TAG = ChartFragment.class.getSimpleName();

    private ArrayList<ArrayList<Entry>> mValues;
    private float mMinVal;
    private float mStandardVal;
    private float mMaxVal;
    private String[] mXValues;
    private String mFieldName;
    private float mMidnightLimit;

    public static ChartFragment newInstance(ArrayList<ArrayList<Entry>> values, float min, float standard, float max, String[] xValues, String fieldName, float midnightLimit) {
        Gson gson = new Gson();
        String valuesString = gson.toJson(values);
        Bundle args = new Bundle();
        args.putString(VALUES, valuesString);
        args.putFloat(MIN, min);
        args.putFloat(STANDARD, standard);
        args.putFloat(MAX, max);
        args.putFloat(MIDNIGHT, midnightLimit);
        args.putStringArray(X_VALUES, xValues);
        args.putString(FIELD_NAME, fieldName);

        ChartFragment fragment = new ChartFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<ArrayList<Entry>>>() {
            }.getType();
            mValues = gson.fromJson(getArguments().getString(VALUES), listType);
            mMinVal = getArguments().getFloat(MIN);
            mStandardVal = getArguments().getFloat(STANDARD);
            mMaxVal = getArguments().getFloat(MAX);
            mXValues = getArguments().getStringArray(X_VALUES);
            mFieldName = getArguments().getString(FIELD_NAME);
            mMidnightLimit = getArguments().getFloat(MIDNIGHT);
        }

        // Analytics
        if (getActivity() != null && getActivity().getApplication() != null) {
            OperatorApplication application = (OperatorApplication) getActivity().getApplication();
            Tracker mTracker = application.getDefaultTracker();
            mTracker.setScreenName(LOG_TAG);
            mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Context context = getContext();
        if(context != null)
        {
            TextView mMin = view.findViewById(R.id.fragment_chart_min);
            StringBuilder minText = new StringBuilder(context.getString(R.string.chart_min_)).append(context.getString(R.string.space)).append(String.format(java.util.Locale.US,"%.2f", mMinVal));

            TextView mStandard = view.findViewById(R.id.fragment_chart_standard);
            StringBuilder standardText = new StringBuilder(context.getString(R.string.chart_standard_)).append(context.getString(R.string.space)).append(String.format(java.util.Locale.US,"%.2f", mStandardVal));

            TextView mMax = view.findViewById(R.id.fragment_chart_max);
            StringBuilder maxText = new StringBuilder(context.getString(R.string.chart_max_)).append(context.getString(R.string.space)).append(String.format(java.util.Locale.US,"%.2f", mMaxVal));

            LineChartTimeLarge mChart = view.findViewById(R.id.fragment_chart_chart);

            mMin.setText(minText);
            mStandard.setText(standardText);
            mMax.setText(maxText);

            mChart.setData(mValues, mXValues,mMinVal, mMaxVal);
            mChart.setAxis(context, mMinVal, mStandardVal, mMaxVal, mMidnightLimit);
        }

//        setActionBar();
    }


    protected void setActionBar() {
        if (getActivity() != null) {
            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (actionBar != null) {
                actionBar.setHomeButtonEnabled(false);
                actionBar.setDisplayHomeAsUpEnabled(false);
                actionBar.setDisplayShowTitleEnabled(false);
                actionBar.setDisplayShowCustomEnabled(true);
                actionBar.setDisplayUseLogoEnabled(true);
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                // rootView null
                @SuppressLint("InflateParams")
                View view = inflater.inflate(R.layout.jobs_fragment_action_bar, null);
                TextView title = view.findViewById(R.id.new_job_title);
                title.setText(mFieldName);
                LinearLayout buttonClose = view.findViewById(R.id.close_image);
                buttonClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentManager fragmentManager = getFragmentManager();
                        if (fragmentManager != null) {
                            fragmentManager.popBackStack();
                        }
                    }
                });
                actionBar.setCustomView(view);
            }
        }
    }

}
