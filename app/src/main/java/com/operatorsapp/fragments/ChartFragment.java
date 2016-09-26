package com.operatorsapp.fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.data.Entry;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.operatorsapp.R;
import com.operatorsapp.view.LineChartTimeLarge;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class ChartFragment extends Fragment {
    private static final String VALUES = "values";
    private static final String MIN = "mib";
    private static final String STANDARD = "standard";
    private static final String MAX = "max";
    private static final String X_VALUES = "xValues";
    private static final String FIELD_NAME = "fieldName";

    private ArrayList<Entry> mValues;
    private float mMinVal;
    private float mStandardVal;
    private float mMaxVal;
    private String[] mXValues;
    private String mFieldName;

    public static ChartFragment newInstance(ArrayList<Entry> values, float min, float standard, float max, String[] xValues,String fieldName) {
        Gson gson = new Gson();
        String valuesString = gson.toJson(values);
        Bundle args = new Bundle();
        args.putString(VALUES, valuesString);
        args.putFloat(MIN, min);
        args.putFloat(STANDARD, standard);
        args.putFloat(MAX, max);
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
            Type listType = new TypeToken<ArrayList<Entry>>() {
            }.getType();
            mValues = gson.fromJson(getArguments().getString(VALUES), listType);
            mMinVal = getArguments().getFloat(MIN);
            mStandardVal = getArguments().getFloat(STANDARD);
            mMaxVal = getArguments().getFloat(MAX);
            mXValues = getArguments().getStringArray(X_VALUES);
            mFieldName = getArguments().getString(FIELD_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chart, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Context context = getContext();
        if(context != null)
        {
            TextView mMin = (TextView) view.findViewById(R.id.fragment_chart_min);
            StringBuilder minText = new StringBuilder(context.getString(R.string.chart_min_)).append(context.getString(R.string.space)).append(String.valueOf((int) mMinVal));

            TextView mStandard = (TextView) view.findViewById(R.id.fragment_chart_standard);
            StringBuilder standardText = new StringBuilder(context.getString(R.string.chart_standard_)).append(context.getString(R.string.space)).append(String.valueOf((int) mStandardVal));

            TextView mMax = (TextView) view.findViewById(R.id.fragment_chart_max);
            StringBuilder maxText = new StringBuilder(context.getString(R.string.chart_max_)).append(context.getString(R.string.space)).append(String.valueOf((int) mMaxVal));

            LineChartTimeLarge mChart = (LineChartTimeLarge) view.findViewById(R.id.fragment_chart_chart);

            mMin.setText(minText);
            mStandard.setText(standardText);
            mMax.setText(maxText);

            mChart.setData(mValues, mXValues);
            mChart.setAxis(context, mMinVal, mStandardVal, mMaxVal);
        }

        setActionBar();
    }


    private void setActionBar() {
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
            TextView title = (TextView) view.findViewById(R.id.new_job_title);
            title.setText(mFieldName);
            ImageView buttonClose = (ImageView) view.findViewById(R.id.close_image);
            buttonClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getFragmentManager().popBackStack();
                }
            });
            actionBar.setCustomView(view);
        }
    }

}
