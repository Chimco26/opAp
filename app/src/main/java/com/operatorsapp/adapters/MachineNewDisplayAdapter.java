package com.operatorsapp.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.common.department.DepartmentMachineValue;
import com.operatorsapp.R;

import java.util.ArrayList;
import java.util.List;

public class MachineNewDisplayAdapter extends RecyclerView.Adapter<MachineNewDisplayAdapter.ViewHolder> {

    private ArrayList<String> mSelectedMachineList;
    private boolean isMultiSelect;
    private List<DepartmentMachineValue> mMachines;
    private List<DepartmentMachineValue> mMachinesFil;
    private MachineAdapter.MachineAdapterListener mListener;

    public MachineNewDisplayAdapter(List<DepartmentMachineValue> departmentResponse, ArrayList<String> selectedMachineList, boolean isMultiSelect, MachineAdapter.MachineAdapterListener machineAdapterListener) {

        mMachines = departmentResponse;
        mMachinesFil = new ArrayList<>();
        mMachinesFil.addAll(departmentResponse);
        mListener = machineAdapterListener;
        mSelectedMachineList = selectedMachineList;
        this.isMultiSelect = isMultiSelect;
    }

    @NonNull
    @Override
    public MachineNewDisplayAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        return new MachineNewDisplayAdapter.ViewHolder(inflater.inflate(R.layout.item_department_machine_new_display, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MachineNewDisplayAdapter.ViewHolder viewHolder, final int position) {
        DepartmentMachineValue machine = mMachinesFil.get(position);
        viewHolder.mTitle.setText(machine.getMachineName());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DepartmentMachineValue machineValue = mMachinesFil.get(position);
                if (isMultiSelect) {
                    if (mSelectedMachineList.contains(String.valueOf(machineValue.getId()))) {
                        mSelectedMachineList.remove(String.valueOf(machineValue.getId()));
                        viewHolder.mTitle.setTextColor(view.getContext().getResources().getColor(R.color.black));
                        viewHolder.itemView.setBackgroundColor(view.getContext().getResources().getColor(R.color.white_five));
                    } else {
                        mSelectedMachineList.add(String.valueOf(machineValue.getId()));
                        viewHolder.mTitle.setTextColor(view.getContext().getResources().getColor(R.color.blue1));
                        viewHolder.itemView.setBackgroundColor(view.getContext().getResources().getColor(R.color.grey_lite));
                    }

                }
                mListener.onMachineSelected(machineValue);
            }
        });
//        if (isMultiSelect && mSelectedMachineList.contains(String.valueOf(machine.getId()))) {
//            viewHolder.mTitle.setTextColor(Color.BLUE);
//        } else {
//            viewHolder.mTitle.setTextColor(Color.BLACK);
//        }
    }

    @Override
    public int getItemCount() {
        if (mMachinesFil != null) {
            return mMachinesFil.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView mTitle;
        private final TextView mParameter;
        private final TextView mTime;

        ViewHolder(View itemView) {
            super(itemView);

            mTitle = itemView.findViewById(R.id.IDMND_title_TV);
            mParameter = itemView.findViewById(R.id.IDMND_parameter_TV);
            mTime = itemView.findViewById(R.id.IDMND_time_TV);
        }

    }
}
