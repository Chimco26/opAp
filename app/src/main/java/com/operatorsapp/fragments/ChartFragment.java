package com.operatorsapp.fragments;


import android.annotation.SuppressLint;
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
import com.operators.infra.Machine;
import com.operatorsapp.R;
import com.operatorsapp.view.LineChartTimeLarge;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ChartFragment extends Fragment {
    private static final String VALUES = "values";

    private TextView mInfo;
    private TextView mMin;
    private TextView mStandard;
    private TextView mMax;
    private LineChartTimeLarge mChart;
    private ArrayList<Entry> mValues;

    public static ChartFragment newInstance(ArrayList<Entry> values) {
        Gson gson = new Gson();
        String valuesString = gson.toJson(values);
        Bundle args = new Bundle();
        args.putString(VALUES, valuesString);

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

        mInfo = (TextView) view.findViewById(R.id.fragment_chart_info);
        mMin = (TextView) view.findViewById(R.id.fragment_chart_min);
        mStandard = (TextView) view.findViewById(R.id.fragment_chart_standard);
        mMax = (TextView) view.findViewById(R.id.fragment_chart_max);
        mChart = (LineChartTimeLarge) view.findViewById(R.id.fragment_chart_chart);

        mChart.setData(mValues);

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
            title.setText("Cycle time");
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
