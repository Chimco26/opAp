package com.operatorsapp.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.common.department.DepartmentMachineValue;
import com.operatorsapp.R;

import java.util.ArrayList;
import java.util.List;

class MachineAdapter  extends RecyclerView.Adapter<MachineAdapter.ViewHolder> {

    private ArrayList<String> mSelectedMachineList;
    private boolean isMultiSelect;
    private List<DepartmentMachineValue> mMachines;
    private List<DepartmentMachineValue> mMachinesFil;
    private MachineAdapterListener mListener;

    public MachineAdapter(List<DepartmentMachineValue> departmentResponse, ArrayList<String> selectedMachineList, boolean isMultiSelect, MachineAdapterListener machineAdapterListener) {

        mMachines = departmentResponse;
        mMachinesFil = new ArrayList<>();
        mMachinesFil.addAll(departmentResponse);
        mListener = machineAdapterListener;
        mSelectedMachineList = selectedMachineList;
        this.isMultiSelect = isMultiSelect;
    }

    @NonNull
    @Override
    public MachineAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        return new MachineAdapter.ViewHolder(inflater.inflate(R.layout.item_department_machine, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MachineAdapter.ViewHolder viewHolder, final int position) {
        DepartmentMachineValue machine = mMachinesFil.get(position);
        viewHolder.mTitle.setText(machine.getMachineName());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DepartmentMachineValue machineValue = mMachinesFil.get(position);
                if (isMultiSelect){
                    if (mSelectedMachineList.contains(String.valueOf(machineValue.getId()))){
                        mSelectedMachineList.remove(String.valueOf(machineValue.getId()));
                        viewHolder.mTitle.setTextColor(view.getContext().getResources().getColor(R.color.black));
                        viewHolder.itemView.setBackgroundColor(view.getContext().getResources().getColor(R.color.white_five));
                    }else {
                        mSelectedMachineList.add(String.valueOf(machineValue.getId()));
                        viewHolder.mTitle.setTextColor(view.getContext().getResources().getColor(R.color.blue1));
                        viewHolder.itemView.setBackgroundColor(view.getContext().getResources().getColor(R.color.grey_lite));
                    }

                }
                mListener.onMachineSelected(machineValue);
            }
        });
        if (isMultiSelect && mSelectedMachineList.contains(String.valueOf(machine.getId()))){
            viewHolder.mTitle.setTextColor(Color.BLUE);
        }else {
            viewHolder.mTitle.setTextColor(Color.BLACK);
        }
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

        ViewHolder(View itemView) {
            super(itemView);

            mTitle = itemView.findViewById(R.id.IDM_name);
        }

    }

    public interface MachineAdapterListener{

        void onMachineSelected(DepartmentMachineValue departmentMachineValue);
    }
}
