package com.operatorsapp.dialogs;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.common.department.MachineLineResponse;
import com.example.common.department.MachinesLineDetail;
import com.operatorsapp.R;
import com.operatorsapp.adapters.TechCallFilterAdapter;

import java.util.HashMap;
import java.util.Set;

public class TechCallFilter extends DialogFragment {

    private MachineLineResponse mMachineLine;
    private HashMap<Integer, Boolean> mFilteredOutMachines;
    private TechCallFilterListener mListener;
    private CheckBox mSelectAllCB;
    private TechCallFilterAdapter mAdapter;
    private RecyclerView mRecyclerView;

    public static TechCallFilter newInstants(MachineLineResponse machineLine, HashMap<Integer, Boolean> filteredOutMachines, TechCallFilterListener listener) {
        TechCallFilter filter = new TechCallFilter();
        filter.setMachineLine(machineLine);
        filter.setFilteredOutMachines(filteredOutMachines);
        filter.setListener(listener);
        return filter;
    }

    private void setListener(TechCallFilterListener listener) {
        mListener = listener;
    }

    private void setFilteredOutMachines(HashMap<Integer, Boolean> filteredOutMachines) {
        mFilteredOutMachines = filteredOutMachines;
    }

    private void setMachineLine(MachineLineResponse machineLine) {
        mMachineLine = machineLine;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(androidx.fragment.app.DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_DeviceDefault_Light_Dialog_MinWidth);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_tech_call_filter, container, false);

        view.findViewById(R.id.DTCF_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mRecyclerView = view.findViewById(R.id.DTCF_recycler);
        mSelectAllCB = view.findViewById(R.id.DTCF_select_all);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new TechCallFilterAdapter(mMachineLine, mFilteredOutMachines, new TechCallFilterAdapter.TechCallFilterAdapterListener() {
            @Override
            public void onItemCheckChanged(int position, boolean isChecked) {
                mFilteredOutMachines.put(mMachineLine.getMachinesData().get(position).getMachineID(), isChecked);
                setSelectAll();
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        mSelectAllCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mSelectAllCB.isChecked()) {
                    for (MachinesLineDetail machine : mMachineLine.getMachinesData()) {
                        mFilteredOutMachines.put(machine.getMachineID(), false);
                    }
                } else {
                    for (MachinesLineDetail machine : mMachineLine.getMachinesData()) {
                        mFilteredOutMachines.put(machine.getMachineID(), true);
                    }
                }
                setSelectAll();
            }
        });
        setSelectAll();
        return view;
    }

    private void setSelectAll() {

        boolean isCheck = true;
        if (mFilteredOutMachines != null && !mFilteredOutMachines.isEmpty()) {
            Set<Integer> keys = mFilteredOutMachines.keySet();
            for (int key : keys) {
                if (!mFilteredOutMachines.get(key)) {
                    isCheck = false;
                    break;
                }
            }
        }
        mSelectAllCB.setChecked(isCheck);
        mAdapter.updateFilterList(mFilteredOutMachines);

        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                if (getContext() != null
                        && getContext() instanceof Activity
                        && !((Activity) getContext()).isDestroyed()) {
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        mListener.onFilterItems(mFilteredOutMachines);
        mListener = null;
        super.onDestroy();

    }

    public interface TechCallFilterListener {
        void onFilterItems(HashMap<Integer, Boolean> filteredOutMachines);
    }
}
