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
    private static final String END_SETUP = "End Setup";
    private static final String REPORT_PRODUCTION = "Report Production";
    private static final String CHANGE_UNIT_CYCLE = "Change Units in Cycle\n";
    private static final String ADD_REJECTS = "Add Rejects";
    private static final String ACTIVATE_JOB = "Activate Job";

    private Activity mContext;
    private List<JobActionsSpinnerItem> mSpinnerItems;
    private String mCurrentProduction;

    public JobsSpinnerAdapter(Activity context, int resource, List<JobActionsSpinnerItem> operators) {
        super(context, resource, operators);
        mSpinnerItems = operators;
        mContext = context;
        mCurrentProduction = mSpinnerItems.get(0).getName();
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
        String item = mSpinnerItems.get(position).getName();
        if (item != null) {
            TextView name = row.findViewById(R.id.SJID_texte);
            name.setText(item);
            setIcon(item, false, (ImageView) row.findViewById(R.id.SJID_image));
            if (mCurrentProduction.equals(item)) {
                name.setTextColor(mContext.getResources().getColor(R.color.blue1));
                setIcon(item, true, (ImageView) row.findViewById(R.id.SJID_image));
                ((ImageView) row.findViewById(R.id.SJID_image)).setBackground(getContext().getResources().getDrawable(R.drawable.circle_blue));
            } else {
                name.setTextColor(mContext.getResources().getColor(R.color.white));
                setIcon(item, false, (ImageView) row.findViewById(R.id.SJID_image));
                ((ImageView) row.findViewById(R.id.SJID_image)).setBackground(getContext().getResources().getDrawable(R.drawable.circle_white));
            }
        }
        return row;
    }

    private void setIcon(String item, boolean selected, ImageView imageView) {

        switch (item) {
            case END_SETUP:
                if (selected) {
                    imageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.end_setup_copy));
                } else {
                    imageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.end_setup));
                }
                break;
            case REPORT_PRODUCTION:
                if (selected) {
                    imageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.production_blue));
                } else {
                    imageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.production));
                }
                break;
            case CHANGE_UNIT_CYCLE:
                if (selected) {
                    imageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.units_blue));
                } else {
                    imageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.units));
                }
                break;
            case ADD_REJECTS:
                if (selected) {
                    imageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.rejects_blue));
                } else {
                    imageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.rejects));
                }
                break;
            case ACTIVATE_JOB:
                if (selected) {
                    imageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.shutdown_blue));
                } else {
                    imageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.shutdown));
                }
                break;
        }
    }

    public void updateTitle(String newTitle) {
        mCurrentProduction = newTitle;
//        notifyDataSetChanged();
    }
}