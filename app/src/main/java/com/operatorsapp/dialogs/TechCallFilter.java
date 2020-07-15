package com.operatorsapp.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.common.department.MachineLineResponse;
import com.operatorsapp.R;
import com.operatorsapp.adapters.TechCallFilterAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class TechCallFilter extends DialogFragment {

    private MachineLineResponse mMachineLine;
    private HashMap<Integer, Boolean> mFilteredOutMachines;
    private TechCallFilterListener mListener;

    public static TechCallFilter newInstants(MachineLineResponse machineLine, HashMap<Integer, Boolean> filteredOutMachines, TechCallFilterListener listener){
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

        RecyclerView recyclerView = view.findViewById(R.id.DTCF_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        TechCallFilterAdapter adapter = new TechCallFilterAdapter(mMachineLine, mFilteredOutMachines, new TechCallFilterAdapter.TechCallFilterAdapterListener() {
            @Override
            public void onItemCheckChanged(int position, boolean isChecked) {
                mFilteredOutMachines.put(mMachineLine.getMachinesData().get(position).getMachineID(), isChecked);

            }
        });
        recyclerView.setAdapter(adapter);
        return view;
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
