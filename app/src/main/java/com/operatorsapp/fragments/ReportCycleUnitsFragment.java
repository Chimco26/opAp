package com.operatorsapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.operators.machinestatusinfra.models.MachineStatus;
import com.operatorsapp.R;

/**
 * Created by Sergey on 11/08/2016.
 */
public class ReportCycleUnitsFragment extends Fragment {


    private static final String CURRENT_PRODUCT_NAME = "current_product_name";
    private static final String CURRENT_PRODUCT_ID = "current_product_id";

    private String mCurrentProductName;
    private int mCurrentProductId;



    public static ReportCycleUnitsFragment newInstance(String currentProductName, int currentProductId) {
        ReportCycleUnitsFragment reportCycleUnitsFragment = new ReportCycleUnitsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CURRENT_PRODUCT_NAME, currentProductName);
        bundle.putInt(CURRENT_PRODUCT_ID, currentProductId);
        reportCycleUnitsFragment.setArguments(bundle);
        return reportCycleUnitsFragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_report_cycle_unit, container, false);
        if (getArguments() != null) {
            mCurrentProductName = getArguments().getString(CURRENT_PRODUCT_NAME);
            mCurrentProductId = getArguments().getInt(CURRENT_PRODUCT_ID);
        }

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


}
