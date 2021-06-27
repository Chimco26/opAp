package com.operatorsapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.common.department.DepartmentMachine;
import com.example.common.department.DepartmentMachineValue;
import com.operatorsapp.R;
import com.operatorsapp.application.OperatorApplication;

import java.util.ArrayList;
import java.util.List;

public class DepartmentNewDisplayAdapter extends RecyclerView.Adapter<DepartmentNewDisplayAdapter.ViewHolder>
        implements Filterable, MachineAdapter.MachineAdapterListener {


    private List<DepartmentMachine> mDepartments;
    private List<DepartmentMachine> mDepartmentFil;
    private final DepartmentFilter mFilter = new DepartmentFilter();
    private final DepartmentAdapter.DepartmentAdapterListener mListener;
    private boolean isMultiSelect = false;
    private final ArrayList<String> selectedMachineList = new ArrayList<>();
    private final String mMachineParameterName;

    public DepartmentNewDisplayAdapter(List<DepartmentMachine> departmentResponse, String machineParameterName ,DepartmentAdapter.DepartmentAdapterListener departmentAdapterListener) {

        mDepartments = departmentResponse;
        mDepartmentFil = new ArrayList<>();
        mDepartmentFil.addAll(departmentResponse);
        mListener = departmentAdapterListener;
        mMachineParameterName = machineParameterName;

    }

    @NonNull
    @Override
    public DepartmentNewDisplayAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        return new ViewHolder(inflater.inflate(R.layout.item_department_new_display, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final DepartmentNewDisplayAdapter.ViewHolder viewHolder, final int position) {

        DepartmentMachine item = mDepartmentFil.get(position);
        String title = OperatorApplication.isEnglishLang() ? item.getDepartmentsMachinesKey().getEName() : item.getDepartmentsMachinesKey().getLName();
        viewHolder.mTitle.setText(title);
        viewHolder.mAmountMachines.setText(String.valueOf(item.getDepartmentMachineValue().size()));

        initRv(position, viewHolder);
    }



    private void initRv(int position, ViewHolder viewHolder) {

        MachineNewDisplayAdapter machineAdapter = new MachineNewDisplayAdapter(mDepartmentFil.get(position).getDepartmentMachineValue(), selectedMachineList, isMultiSelect, mMachineParameterName,this);

        RecyclerView recyclerView = viewHolder.mRv;

        recyclerView.setLayoutManager(new GridLayoutManager(viewHolder.mRv.getContext(), 5));

        recyclerView.setNestedScrollingEnabled(false);

        recyclerView.setAdapter(machineAdapter);

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

    @Override
    public void onMachineSelected(DepartmentMachineValue departmentMachineValue) {
        if (isMultiSelect) {
        } else {
            mListener.onMachineSelected(departmentMachineValue);
        }
    }

    public void setMultiSelection(boolean isMultiSelect) {
        this.isMultiSelect = isMultiSelect;
        notifyDataSetChanged();
    }

    public ArrayList<String> getSelectedMachineList() {
        return selectedMachineList;
    }

    public void updateMachinesStatus(List<DepartmentMachine> mDepartmentMachine) {
        if (mDepartmentMachine != null){
            mDepartments = mDepartmentMachine;
            mDepartmentFil.clear();
            mDepartmentFil.addAll(mDepartmentMachine);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView mTitle;
        private final TextView mAmountMachines;
        private final RecyclerView mRv;

        ViewHolder(View itemView) {
            super(itemView);

            mTitle = itemView.findViewById(R.id.IDND_department_name_TV);
            mAmountMachines = itemView.findViewById(R.id.IDND_machines_amount_TV);
            mRv = itemView.findViewById(R.id.ID_rv_department_new_display_rv);
        }

    }

    private class DepartmentFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results = new FilterResults();
            if (constraint.length() > 1) {

                mDepartmentFil.clear();
                ArrayList<DepartmentMachine> allDepartments = new ArrayList<>();
                for (DepartmentMachine department : mDepartments) {
                    DepartmentMachine departmentMachine = new DepartmentMachine(department.getDepartmentsMachinesKey(), new ArrayList<DepartmentMachineValue>());
                    for (DepartmentMachineValue machine : department.getDepartmentMachineValue()) {
                        if (machine.getMachineName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                            if (!allDepartments.contains(departmentMachine)) {
                                allDepartments.add(departmentMachine);
                            }
                            departmentMachine.getDepartmentMachineValue().add(machine);
                        }
                    }
                }

                mDepartmentFil.addAll(allDepartments);
                results.values = allDepartments;

                results.count = allDepartments.size();

            } else {

                mDepartmentFil.clear();
                mDepartmentFil.addAll(mDepartments);
                results.values = mDepartmentFil;

                results.count = mDepartmentFil.size();
            }
            return results;

        }

        private boolean isContainLetters(String toCheck, String searchLetters) {
            int counter = 0;
            for (Character character : searchLetters.toCharArray()) {
                if (toCheck.contains(character.toString())) {
                    counter++;
                }
            }
            return counter == searchLetters.length();
        }


        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            mDepartmentFil = (List<DepartmentMachine>) results.values;

            notifyDataSetChanged();
        }

    }
}
