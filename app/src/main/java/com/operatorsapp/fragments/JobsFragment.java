package com.operatorsapp.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.operators.machinestatuscore.timecounter.TimeToEndCounter;
import com.operatorsapp.R;
import com.operatorsapp.adapters.JobsRecyclerViewAdapter;
import com.operatorsapp.adapters.JobsSpinnerAdapter;
import com.operatorsapp.adapters.OperatorSpinnerAdapter;


public class JobsFragment extends Fragment {
    private RecyclerView mJobsRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private JobsRecyclerViewAdapter mJobsRecyclerViewAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_jobs, container, false);
        setActionBar();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mJobsRecyclerView = (RecyclerView) view.findViewById(R.id.job_recycler_view);
        mLayoutManager = new LinearLayoutManager(getContext());
        mJobsRecyclerView.setLayoutManager(mLayoutManager);

        mJobsRecyclerViewAdapter = new JobsRecyclerViewAdapter();
        mJobsRecyclerView.setAdapter(mJobsRecyclerViewAdapter);
    }

    private void setActionBar() {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayUseLogoEnabled(true);
            SpannableString spannableString = new SpannableString(getString(R.string.screen_title));
            spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getActivity(), R.color.white)), 0, spannableString.length() - 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getActivity(), R.color.T12_color)), spannableString.length() - 3, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            LayoutInflater inflator = LayoutInflater.from(getActivity());
            // rootView null
            @SuppressLint("InflateParams") View view = inflator.inflate(R.layout.actionbar_title_and_tools_view, null);
            TextView title = ((TextView) view.findViewById(R.id.toolbar_title));
            title.setText(spannableString);
            title.setVisibility(View.GONE);

            Spinner operatorsSpinner = (Spinner) view.findViewById(R.id.toolbar_operator_spinner);
            operatorsSpinner.setVisibility(View.GONE);
            final Spinner jobsSpinner = (Spinner) view.findViewById(R.id.toolbar_job_spinner);
            jobsSpinner.setVisibility(View.GONE);
            TextView machineNameStatusBarTextView = (TextView) view.findViewById(R.id.text_view_machine_id_name);
            TextView machineStatus = (TextView) view.findViewById(R.id.text_view_machine_status);

            machineNameStatusBarTextView.setVisibility(View.GONE);
            machineStatus.setVisibility(View.GONE);

            LinearLayout spinnerLayout = (LinearLayout) view.findViewById(R.id.spinner_layout);
            spinnerLayout.setVisibility(View.GONE);

            ImageView indicator = (ImageView) view.findViewById(R.id.job_indicator);
            indicator.setVisibility(View.GONE);


            actionBar.setCustomView(view);
//            actionBar.setIcon(R.drawable.logo);
        }
    }
}
