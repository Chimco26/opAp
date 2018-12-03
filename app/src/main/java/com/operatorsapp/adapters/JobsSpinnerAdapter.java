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
        setIcon(mSpinnerItems.get(position).getUniqueID(), (ImageView) row.findViewById(R.id.SJID_image));
        ((ImageView) row.findViewById(R.id.SJID_image)).setBackground(getContext().getResources().getDrawable(R.drawable.circle_white));

        if (mSpinnerItems.get(position).isEnabled()){
            name.setTextColor(mContext.getResources().getColor(R.color.white));
        }else {
            name.setTextColor(mContext.getResources().getColor(R.color.dialog_text_gray));
        }

        return row;
    }

    private void setIcon(int id, ImageView imageView) {

        switch (id) {
            case 4:
                imageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.end_setup));
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
}