package com.operatorsapp.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.common.department.DepartmentMachineValue;
import com.operatorsapp.R;

import java.util.ArrayList;
import java.util.List;

class MachineAdapter  extends RecyclerView.Adapter<MachineAdapter.ViewHolder> {

    private List<DepartmentMachineValue> mMachines;
    private List<DepartmentMachineValue> mMachinesFil;
    private MachineAdapterListener mListener;

    public MachineAdapter(List<DepartmentMachineValue> departmentResponse, MachineAdapterListener machineAdapterListener) {

        mMachines = departmentResponse;
        mMachinesFil = new ArrayList<>();
        mMachinesFil.addAll(departmentResponse);
        mListener = machineAdapterListener;
    }

    @NonNull
    @Override
    public MachineAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        return new MachineAdapter.ViewHolder(inflater.inflate(R.layout.item_department_machine, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MachineAdapter.ViewHolder viewHolder, final int position) {

        viewHolder.mTitle.setText(mMachinesFil.get(position).getMachineName());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onMachineSelected(mMachinesFil.get(position));
            }
        });
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
