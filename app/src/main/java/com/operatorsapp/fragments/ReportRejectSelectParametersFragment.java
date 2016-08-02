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

import com.google.gson.Gson;
import com.operators.jobsinfra.Job;
import com.operators.machinestatusinfra.MachineStatus;
import com.operatorsapp.R;

/**
 * Created by Sergey on 31/07/2016.
 */
public class ReportRejectSelectParametersFragment extends Fragment {

    private static final String CURRENT_MACHINE_STATUS = "current_machine_status";
    public static final String REJECT_REASON = "reject_reason";
    public static final String REJECT_CAUSE = "reject_cause";
    private MachineStatus mMachineStatus;
    private String mSelectedReason;
    private String mSelectedCause;

    private TextView mProductNameTextView;
    private TextView mProductIdTextView;
    private TextView mJobIdTextView;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_report_rejects_selected_parameters, container, false);
        Bundle bundle = this.getArguments();
        Gson gson = new Gson();
        mMachineStatus = gson.fromJson(bundle.getString(CURRENT_MACHINE_STATUS), MachineStatus.class);
        mSelectedReason = bundle.getString(REJECT_REASON);
        mSelectedCause = bundle.getString(REJECT_CAUSE);

        setActionBar();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mProductNameTextView = (TextView) view.findViewById(R.id.report_rejects_product_name_text_view);
        mProductIdTextView = (TextView) view.findViewById(R.id.report_rejects_product_id_text_view);
        mJobIdTextView = (TextView) view.findViewById(R.id.report_rejects_selected_job_id_text_view);

        mProductNameTextView.setText(new StringBuilder(mMachineStatus.getAllMachinesData().get(0).getCurrentProductName() + ","));
        mProductIdTextView.setText(String.valueOf(mMachineStatus.getAllMachinesData().get(0).getCurrentProductID()));
        mJobIdTextView.setText((String.valueOf(mMachineStatus.getAllMachinesData().get(0).getCurrentJobID())));
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
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
            View view = inflater.inflate(R.layout.report_reject_parameters_action_bar, null);

            ImageView buttonClose = (ImageView) view.findViewById(R.id.close_image);
            buttonClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getFragmentManager().popBackStack();
                }
            });

            TextView reasonTextView = (TextView) view.findViewById(R.id.reason_text_view);
            reasonTextView.setText(mSelectedReason);
            actionBar.setCustomView(view);

        }
    }
}
