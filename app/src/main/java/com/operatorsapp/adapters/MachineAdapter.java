package com.operatorsapp.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.common.department.DepartmentMachineValue;
import com.operatorsapp.R;

import java.util.ArrayList;
import java.util.List;

class MachineAdapter  extends RecyclerView.Adapter<MachineAdapter.ViewHolder>
        implements Filterable {


    private List<DepartmentMachineValue> mMachines;
    private List<DepartmentMachineValue> mMachinesFil;
    private MachineFilter mFilter = new MachineFilter();

    public MachineAdapter(List<DepartmentMachineValue> departmentResponse) {

        mMachines = departmentResponse;
        mMachinesFil = new ArrayList<>();
        mMachinesFil.addAll(departmentResponse);
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

    }

    @Override
    public int getItemCount() {
        if (mMachinesFil != null) {
            return mMachinesFil.size();
        }
        return 0;
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView mTitle;

        ViewHolder(View itemView) {
            super(itemView);

            mTitle = itemView.findViewById(R.id.IDM_name);
        }

    }

    private class MachineFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results = new FilterResults();
            if (constraint != null && constraint.length() > 1) {

                mMachinesFil.clear();
                ArrayList<DepartmentMachineValue> departmentsMachines = new ArrayList<>();
                for (DepartmentMachineValue departmentsMachine: mMachines){
                    if (departmentsMachine.getMachineName().contains(constraint)) {
                        departmentsMachines.add(departmentsMachine);
                    }
                }

                mMachinesFil.addAll(departmentsMachines);
                results.values = departmentsMachines;

                results.count = departmentsMachines.size();

            } else

            {
                mMachinesFil.addAll(mMachines);

                results.values = mMachinesFil;

                results.count = mMachinesFil.size();
            }
            return results;

        }


        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            mMachinesFil = (List<DepartmentMachineValue>) results.values;

            notifyDataSetChanged();
        }

    }

}
