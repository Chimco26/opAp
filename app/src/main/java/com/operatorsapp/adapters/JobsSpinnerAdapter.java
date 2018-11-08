package com.operatorsapp.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.operatorsapp.R;
import com.operatorsapp.model.JobActionsSpinnerItem;

import java.util.List;

public class JobsSpinnerAdapter extends ArrayAdapter<JobActionsSpinnerItem> {

    private Activity mContext;
    private List<JobActionsSpinnerItem> mSpinnerItems;
    private int mCurrentProductionId;

    public JobsSpinnerAdapter(Activity context, int resource, List<JobActionsSpinnerItem> operators) {
        super(context, resource, operators);
        mSpinnerItems = operators;
        mContext = context;
        mCurrentProductionId = mSpinnerItems.get(0).getUniqueID();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = mContext.getLayoutInflater();
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
            LayoutInflater inflater = mContext.getLayoutInflater();
            row = inflater.inflate(R.layout.spinner_jobs_item_dropdown, parent, false);
        }
        TextView name = row.findViewById(R.id.SJID_texte);
        name.setText(mSpinnerItems.get(position).getName());
        setIcon(mSpinnerItems.get(position).getUniqueID(), false, (ImageView) row.findViewById(R.id.SJID_image));
        if (mCurrentProductionId == mSpinnerItems.get(position).getUniqueID()) {
            name.setTextColor(mContext.getResources().getColor(R.color.blue1));
            setIcon(mSpinnerItems.get(position).getUniqueID(), true, (ImageView) row.findViewById(R.id.SJID_image));
            row.findViewById(R.id.SJID_image).setBackground(getContext().getResources().getDrawable(R.drawable.circle_blue));
        } else {
            name.setTextColor(mContext.getResources().getColor(R.color.white));
            setIcon(mSpinnerItems.get(position).getUniqueID(), false, (ImageView) row.findViewById(R.id.SJID_image));
            row.findViewById(R.id.SJID_image).setBackground(getContext().getResources().getDrawable(R.drawable.circle_white));
        }

        return row;
    }

    private void setIcon(int id, boolean selected, ImageView imageView) {

        switch (id) {
            case 4:
                if (selected) {
                    imageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.end_setup_copy));
                } else {
                    imageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.end_setup));
                }
                break;
            case 3:
                if (selected) {
                    imageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.production_blue));
                } else {
                    imageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.production));
                }
                break;
            case 2:
                if (selected) {
                    imageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.cycle_units_white));
                } else {
                    imageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.cycle_units));
                }
                break;
            case 1:
                if (selected) {
                    imageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.add_white));
                } else {
                    imageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.add));
                }
                break;
            case 0:
                if (selected) {
                    imageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.shutdown_blue));
                } else {
                    imageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.shutdown));
                }
                break;
            default:
                if (selected) {
                    imageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.production_blue));
                } else {
                    imageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.production));
                }
        }
    }

    public void updateSelectedId(int id) {
        mCurrentProductionId = id;
    }
}