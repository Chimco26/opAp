package com.operatorsapp.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.common.department.DepartmentMachine;
import com.operatorsapp.R;

import java.util.ArrayList;
import java.util.List;

public class DepartmentAdapter extends RecyclerView.Adapter<DepartmentAdapter.ViewHolder>
        implements Filterable {


    private final List<DepartmentMachine> mDepartments;
    private List<DepartmentMachine> mDepartmentFil;
    private DepartmentFilter mFilter = new DepartmentFilter();
    private String mSearchFilter;

    public DepartmentAdapter(List<DepartmentMachine> departmentResponse) {

        mDepartments = departmentResponse;
        mDepartmentFil = new ArrayList<>();
        mDepartmentFil.addAll(departmentResponse);
    }

    @NonNull
    @Override
    public DepartmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        return new DepartmentAdapter.ViewHolder(inflater.inflate(R.layout.item_department, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final DepartmentAdapter.ViewHolder viewHolder, final int position) {

        viewHolder.mTitle.setText(mDepartmentFil.get(position).getDepartmentsMachinesKey().getLName());

        initRv(position, viewHolder);
    }

    private void initRv(int position, ViewHolder viewHolder) {

        MachineAdapter machineAdapter = new MachineAdapter(mDepartmentFil.get(position).getDepartmentMachineValue());

        RecyclerView recyclerView = viewHolder.mRv;

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(viewHolder.mRv.getContext(), LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(machineAdapter);

        machineAdapter.getFilter().filter(mSearchFilter);
    }

    public void setSearchFilter(String searchFilter){
        mSearchFilter = searchFilter;
    }

    @Override
    public int getItemCount() {
        if (mDepartmentFil != null) {
            return mDepartmentFil.size();
        }
        return 0;
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView mTitle;
        private final RecyclerView mRv;

        ViewHolder(View itemView) {
            super(itemView);

            mTitle = itemView.findViewById(R.id.ID_title);
            mRv = itemView.findViewById(R.id.ID_rv);
        }

    }

    private class DepartmentFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results = new FilterResults();
            if (constraint.length() > 1) {

                mDepartmentFil.clear();
                ArrayList<DepartmentMachine> allDepartments = new ArrayList<>();
                for (DepartmentMachine allDepartment: mDepartments){
                    if (allDepartment.getDepartmentsMachinesKey().getLName().contains(constraint)) {
                        allDepartments.add(allDepartment);
                    }
                }

                mDepartmentFil.addAll(allDepartments);
                results.values = allDepartments;

                results.count = allDepartments.size();

            } else

            {

                mDepartmentFil.addAll(mDepartments);
                results.values = mDepartmentFil;

                results.count = mDepartmentFil.size();
            }
            return results;

        }


        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            mDepartmentFil = (List<DepartmentMachine>) results.values;

            notifyDataSetChanged();
        }

    }

}
