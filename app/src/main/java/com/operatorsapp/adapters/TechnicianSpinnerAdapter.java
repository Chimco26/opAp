package com.operatorsapp.adapters;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.operators.reportfieldsformachineinfra.Technician;
import com.operatorsapp.R;
import com.operatorsapp.application.OperatorApplication;

import java.util.List;

public class TechnicianSpinnerAdapter extends ArrayAdapter<Technician> {
    private List<Technician> mSpinnerItems;
    private TextView mRowName;
    private View mView;

    public TechnicianSpinnerAdapter(Context context, int resource, List<Technician> technicians) {
        super(context, resource, technicians);
        mSpinnerItems = technicians;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            row = inflater.inflate(R.layout.base_spinner_item, parent, false);
        }

        mView = row;
        mRowName = row.findViewById(R.id.spinner_item_name);
        mRowName.setTextColor(ContextCompat.getColor(getContext(), R.color.status_bar));
        if(mSpinnerItems != null && mSpinnerItems.get(position) != null)
        {
            String technicianName = OperatorApplication.isEnglishLang() ? mSpinnerItems.get(position).getEName() : mSpinnerItems.get(position).getLName();
            mRowName.setText(technicianName);
        }
        else
        {
            mRowName.setText(getContext().getString(R.string.dashes));
        }
        mRowName.setTextSize(22);

        return row;
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            row = inflater.inflate(R.layout.base_spinner_item_dropdown, parent, false);
        }
        Technician item = mSpinnerItems.get(position);
        String technicianName = OperatorApplication.isEnglishLang() ? item.getEName() : item.getLName();
        if (technicianName != null) {
            TextView name = row.findViewById(R.id.spinner_item_name);
            name.setText(technicianName);
            name.setTextColor(ContextCompat.getColor(getContext(), R.color.status_bar));
            name.setTextSize(22);
        }
        return row;
    }

    public void setTitle(int position) {

        mRowName = mView.findViewById(R.id.spinner_item_name);
        mRowName.setTextColor(ContextCompat.getColor(getContext(), R.color.status_bar));
        Technician item = mSpinnerItems.get(position);
        String technicianName = OperatorApplication.isEnglishLang() ? item.getEName() : item.getLName();
        mRowName.setText(technicianName);
        mRowName.setTextSize(24);
    }
}
