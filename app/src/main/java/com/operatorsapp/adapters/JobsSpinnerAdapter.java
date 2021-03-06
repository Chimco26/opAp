package com.operatorsapp.adapters;

import android.app.Activity;
import androidx.annotation.NonNull;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.operators.machinestatusinfra.models.MachineStatus;
import com.operatorsapp.R;
import com.operatorsapp.model.JobActionsSpinnerItem;

import java.util.List;

public class JobsSpinnerAdapter extends ArrayAdapter<JobActionsSpinnerItem> {

    private MachineStatus mCurrentMachineStatus;
    private List<JobActionsSpinnerItem> mSpinnerItems;
    private int mCurrentProductionId;

    public JobsSpinnerAdapter(Context context, int resource, List<JobActionsSpinnerItem> operators, MachineStatus currentMachineStatus) {
        super(context, resource, operators);
        mSpinnerItems = operators;
        mCurrentMachineStatus = currentMachineStatus;
        mCurrentProductionId = mSpinnerItems.get(0).getUniqueID();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            row = inflater.inflate(R.layout.spinner_job_item, parent, false);
            TextView rowName = row.findViewById(R.id.spinner_job_item_name);
            rowName.setTextSize(20);

        }
        return row;
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            row = inflater.inflate(R.layout.spinner_jobs_item_dropdown, parent, false);
        }
//        checkMachineStatusConfig();
        TextView name = row.findViewById(R.id.SJID_texte);
        name.setText(mSpinnerItems.get(position).getName());
        setIcon(mSpinnerItems.get(position).getUniqueID(), (ImageView) row.findViewById(R.id.SJID_image));
        ((ImageView) row.findViewById(R.id.SJID_image)).setBackground(getContext().getResources().getDrawable(R.drawable.circle_white));

        if (mSpinnerItems.get(position).isEnabled()) {
            name.setTextColor(getContext().getResources().getColor(R.color.white));
        } else {
            name.setTextColor(getContext().getResources().getColor(R.color.dialog_text_gray));
        }

        return row;
    }

    private void setIcon(int id, ImageView imageView) {

        switch (id) {
            case 5:
                imageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.end_setup));
                break;
            case 4:
                imageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.order_test));
                break;
            case 3:
                imageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.production_dark_blue));
                break;
            case 2:
                imageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.cycle_units));
                break;
            case 1:
                imageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.add));
                break;
            case 0:
                imageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.activate));
                break;
            default:
                imageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.production_dark_blue));

        }

    }

    public void updateSelectedId(int id) {
        mCurrentProductionId = id;
    }

    public void checkMachineStatusConfig() {
        if (mCurrentMachineStatus != null && mCurrentMachineStatus.getAllMachinesData() != null
                && mCurrentMachineStatus.getAllMachinesData().get(0).getConfigName() != null
                && mCurrentMachineStatus.getAllMachinesData().get(0).getConfigName().length() > 0){
            mSpinnerItems.get(2).setEnabled(false);
        }
    }

    public void updateMachineStatus(MachineStatus machineStatus) {
        mCurrentMachineStatus = machineStatus;
    }
}