package com.operatorsapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.common.department.MachineLineResponse;
import com.operatorsapp.R;

import java.util.ArrayList;
import java.util.HashMap;

public class TechCallFilterAdapter extends RecyclerView.Adapter<TechCallFilterAdapter.ViewHolder> {

    private final MachineLineResponse mMachineLine;
    private HashMap<Integer, Boolean> mFilteredOutMachines;
    private final TechCallFilterAdapterListener mListener;

    public TechCallFilterAdapter(MachineLineResponse machineLine, HashMap<Integer, Boolean> filteredOutMachines, TechCallFilterAdapterListener listener) {
        mMachineLine = machineLine;
        mFilteredOutMachines = filteredOutMachines;
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tech_call_filter_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.chkBox.setText(mMachineLine.getMachinesData().get(position).getMachineName());
        if (mFilteredOutMachines.containsKey(mMachineLine.getMachinesData().get(position).getMachineID())) {
            holder.chkBox.setChecked(mFilteredOutMachines.get(mMachineLine.getMachinesData().get(position).getMachineID()));
        }
    }

    @Override
    public int getItemCount() {
        return mMachineLine.getMachinesData().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CheckBox chkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            chkBox = itemView.findViewById(R.id.tech_call_filter_chkbx);
            chkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mFilteredOutMachines.put(mMachineLine.getMachinesData().get(getAdapterPosition()).getMachineID(), isChecked);
                    mListener.onItemCheckChanged(getAdapterPosition(), isChecked);
                }
            });
        }
    }

    public void updateFilterList(HashMap<Integer, Boolean> filteredList){
        mFilteredOutMachines = filteredList;
    }

    public interface TechCallFilterAdapterListener {
        void onItemCheckChanged(int position, boolean isChecked);
    }
}
