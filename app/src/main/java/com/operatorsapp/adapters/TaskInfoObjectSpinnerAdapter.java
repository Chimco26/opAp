package com.operatorsapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.common.task.TaskInfoObject;
import com.operatorsapp.R;

import java.util.List;

public class TaskInfoObjectSpinnerAdapter extends ArrayAdapter<TaskInfoObject> {
    private final int color;
    private List<TaskInfoObject> mSpinnerItems;
    private TextView mRowName;
    private View mView;

    public TaskInfoObjectSpinnerAdapter(Context context, int resource, List<TaskInfoObject> list, int color) {
        super(context, resource, list);
        mSpinnerItems = list;
        if (color != 0){
            this.color = color;
        }else {
            this.color = ContextCompat.getColor(context, R.color.status_bar);
        }
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            row = inflater.inflate(R.layout.base_spinner_item, parent, false);
            mView = row;
            mRowName = row.findViewById(R.id.spinner_item_name);
            mRowName.setTextColor(color);
            if(mSpinnerItems != null && mSpinnerItems.get(0) != null) {
                mRowName.setText(mSpinnerItems.get(0).getDisplayName());
            } else {
                mRowName.setText(getContext().getString(R.string.dashes));
            }
            mRowName.setTextSize(22);
        }
        return row;
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            row = inflater.inflate(R.layout.base_spinner_item_dropdown, parent, false);
        }
        String item = mSpinnerItems.get(position).getDisplayName();
        if (item != null) {
            TextView name = row.findViewById(R.id.spinner_item_name);
            name.setText(mSpinnerItems.get(position).getDisplayName());
            name.setTextColor(ContextCompat.getColor(getContext(), R.color.status_bar));
            name.setTextSize(22);
        }
        return row;
    }

    public void setTitle(int position) {

        mRowName = mView.findViewById(R.id.spinner_item_name);
        mRowName.setTextColor(color);
        mRowName.setText(mSpinnerItems.get(position).getDisplayName());
        mRowName.setTextSize(24);
    }

}
