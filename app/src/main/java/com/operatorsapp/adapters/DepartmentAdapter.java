package com.operatorsapp.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.common.department.DepartmentMachine;
import com.example.common.department.DepartmentMachineValue;
import com.operatorsapp.R;
import com.operatorsapp.application.OperatorApplication;

import java.util.ArrayList;
import java.util.List;

public class DepartmentAdapter extends RecyclerView.Adapter<DepartmentAdapter.ViewHolder>
        implements Filterable, MachineAdapter.MachineAdapterListener {


    private final List<DepartmentMachine> mDepartments;
    private List<DepartmentMachine> mDepartmentFil;
    private DepartmentFilter mFilter = new DepartmentFilter();
    private String mSearchFilter;
    private DepartmentAdapterListener mListener;
    private boolean isMultiSelect = false;
    private ArrayList<String> selectedMachineList = new ArrayList<>();
    private ArrayList<String> machineLineIdList = new ArrayList<>();

    public DepartmentAdapter(List<DepartmentMachine> departmentResponse, DepartmentAdapterListener departmentAdapterListener) {

        mDepartments = departmentResponse;
        mDepartmentFil = new ArrayList<>();
        mDepartmentFil.addAll(departmentResponse);
        mListener = departmentAdapterListener;
    }

    @NonNull
    @Override
    public DepartmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        return new DepartmentAdapter.ViewHolder(inflater.inflate(R.layout.item_department, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final DepartmentAdapter.ViewHolder viewHolder, final int position) {

        DepartmentMachine item = mDepartmentFil.get(position);
        String title = OperatorApplication.isEnglishLang() ? item.getDepartmentsMachinesKey().getEName() : item.getDepartmentsMachinesKey().getLName();
        viewHolder.mTitle.setText(title);

        initRv(position, viewHolder);
    }

    private void initRv(int position, ViewHolder viewHolder) {

        MachineAdapter machineAdapter = new MachineAdapter(mDepartmentFil.get(position).getDepartmentMachineValue(), selectedMachineList, isMultiSelect, this);

        RecyclerView recyclerView = viewHolder.mRv;

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(viewHolder.mRv.getContext(), LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(machineAdapter);

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

    @Override
    public void onMachineSelected(DepartmentMachineValue departmentMachineValue) {
        if (isMultiSelect){
        }else {
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
                for (DepartmentMachine department: mDepartments){
                    DepartmentMachine departmentMachine = new DepartmentMachine(department.getDepartmentsMachinesKey(), new ArrayList<DepartmentMachineValue>());
                    for (DepartmentMachineValue machine: department.getDepartmentMachineValue()){
                        if (machine.getMachineName().toLowerCase().contains(constraint.toString().toLowerCase())){
                            if (!allDepartments.contains(departmentMachine)){
                                allDepartments.add(departmentMachine);
                            }
                            departmentMachine.getDepartmentMachineValue().add(machine);
                        }
                    }
                }

                mDepartmentFil.addAll(allDepartments);
                results.values = allDepartments;

                results.count = allDepartments.size();

            } else

            {

                mDepartmentFil.clear();
                mDepartmentFil.addAll(mDepartments);
                results.values = mDepartmentFil;

                results.count = mDepartmentFil.size();
            }
            return results;

        }

        private boolean isContainLetters(String toCheck, String searchLetters) {
            int counter = 0;
            for (Character character: searchLetters.toCharArray()){
                if (toCheck.contains(character.toString())){
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

    public interface DepartmentAdapterListener{

        void onMachineSelected(DepartmentMachineValue departmentMachineValue);
    }
}
