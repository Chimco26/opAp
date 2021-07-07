package com.operatorsapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
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
import java.util.concurrent.TimeUnit;

public class MachineNewDisplayAdapter extends RecyclerView.Adapter<MachineNewDisplayAdapter.ViewHolder> {

    private final ArrayList<String> mSelectedMachineList;
    private final boolean isMultiSelect;
    private final List<DepartmentMachineValue> mMachines;
    private final List<DepartmentMachineValue> mMachinesFil;
    private final MachineAdapter.MachineAdapterListener mListener;
    private final String mMachineParameterName;

    public MachineNewDisplayAdapter(List<DepartmentMachineValue> departmentResponse, ArrayList<String> selectedMachineList, boolean isMultiSelect, String machineParameterName, MachineAdapter.MachineAdapterListener machineAdapterListener) {

        mMachines = departmentResponse;
        mMachinesFil = new ArrayList<>();
        mMachinesFil.addAll(departmentResponse);
        mListener = machineAdapterListener;
        mSelectedMachineList = selectedMachineList;
        this.isMultiSelect = isMultiSelect;
        mMachineParameterName = machineParameterName;
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

        viewHolder.itemView.setBackgroundColor(Color.parseColor(mMachinesFil.get(position).getMachineStatusColor()));
        viewHolder.mTitle.setTextColor(viewHolder.itemView.getContext().getResources().getColor(R.color.white));
        viewHolder.mTitle.setText(machine.getMachineName());
        if (mMachineParameterName != null) {
            viewHolder.mParameter.setText(getMachineParameterName(mMachinesFil.get(position), mMachineParameterName));
        }
        viewHolder.mTime.setText(getTimeFormatFromMinute(mMachinesFil.get(position).getStatusTime(), viewHolder.itemView.getContext()));

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DepartmentMachineValue machineValue = mMachinesFil.get(position);
                if (isMultiSelect) {
                    if (mSelectedMachineList.contains(String.valueOf(machineValue.getId()))) {
                        mSelectedMachineList.remove(String.valueOf(machineValue.getId()));
                        viewHolder.mTitle.setTextColor(view.getContext().getResources().getColor(R.color.white));
                        viewHolder.itemView.setBackgroundColor(Color.parseColor(mMachinesFil.get(position).getMachineStatusColor()));
                    } else {
                        mSelectedMachineList.add(String.valueOf(machineValue.getId()));
                        viewHolder.mTitle.setTextColor(view.getContext().getResources().getColor(R.color.blue1));
                        viewHolder.itemView.setBackgroundColor(view.getContext().getResources().getColor(R.color.dialog_text_gray));
                    }

                }
                mListener.onMachineSelected(machineValue);
            }
        });
        if (isMultiSelect && mSelectedMachineList.contains(String.valueOf(machine.getId()))) {
            viewHolder.mTitle.setTextColor(Color.BLUE);
        } else {
            viewHolder.mTitle.setTextColor(Color.WHITE);
        }
    }

    private String getMachineParameterName(DepartmentMachineValue departmentMachineValue, String mMachineParameterName) {
        switch (mMachineParameterName) {
            case "Product":
               return departmentMachineValue.getProductName();
            case "ProductCatalogID":
               return departmentMachineValue.getProductCatalog();
            case "ERPJobID":
                return departmentMachineValue.getErpJobID();
            case "Customer":
                return departmentMachineValue.getClientName();
        }
        return "";
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

    public static String getTimeFormatFromMinute(long totalMinuteDuration, Context context) {

        long d = TimeUnit.DAYS.convert(totalMinuteDuration, TimeUnit.MINUTES);

        long h = TimeUnit.HOURS.convert(totalMinuteDuration, TimeUnit.MINUTES);

        String time;

        if (d > 0) {
            long hour = h % 24;
            if (hour != 0) {
                time = String.format("%s %s %s %s", d, context.getString(R.string.days2), hour, context.getString(R.string.hr2));
            } else {
                time = String.format("%s %s", d, context.getString(R.string.days2));
            }
        } else if (h > 0) {
            long minute = totalMinuteDuration % 60;
            if (minute != 0) {
                time = String.format("%s %s %s %s", h, context.getString(R.string.hr2), totalMinuteDuration % 60, context.getString(R.string.min));
            } else {
                time = String.format("%s %s", h, context.getString(R.string.hr2));
            }
        } else {
            time = String.format("%s %s", totalMinuteDuration, context.getString(R.string.min));
        }
        return time;
    }

}
