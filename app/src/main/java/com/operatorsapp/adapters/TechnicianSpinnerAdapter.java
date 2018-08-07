package com.operatorsapp.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
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
    private Activity mContext;
    private List<Technician> mSpinnerItems;
    private TextView mRowName;
    private View mView;

    public TechnicianSpinnerAdapter(Activity context, int resource, List<Technician> technicians) {
        super(context, resource, technicians);
        mSpinnerItems = technicians;
        mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = mContext.getLayoutInflater();
            row = inflater.inflate(R.layout.base_spinner_item, parent, false);
            mView = row;
            mRowName = row.findViewById(R.id.spinner_item_name);
            mRowName.setTextColor(ContextCompat.getColor(mContext, R.color.status_bar));
            if(mSpinnerItems != null && mSpinnerItems.get(0) != null)
            {
                String technicianName = OperatorApplication.isEnglishLang() ? mSpinnerItems.get(0).getEName() : mSpinnerItems.get(0).getLName();
                mRowName.setText(technicianName);
            }
            else
            {
                mRowName.setText(mContext.getString(R.string.dashes));
            }
            mRowName.setTextSize(22);
        }
        return row;
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = mContext.getLayoutInflater();
            row = inflater.inflate(R.layout.base_spinner_item, parent, false);
        }
        Technician item = mSpinnerItems.get(position);
        String technicianName = OperatorApplication.isEnglishLang() ? item.getEName() : item.getLName();
        if (technicianName != null) {
            TextView name = row.findViewById(R.id.spinner_item_name);
            name.setText(technicianName);
            name.setTextColor(ContextCompat.getColor(mContext, R.color.status_bar));
            name.setTextSize(22);
        }
        return row;
    }

    public void setTitle(int position) {

        mRowName = mView.findViewById(R.id.spinner_item_name);
        mRowName.setTextColor(ContextCompat.getColor(mContext, R.color.status_bar));
        Technician item = mSpinnerItems.get(position);
        String technicianName = OperatorApplication.isEnglishLang() ? item.getEName() : item.getLName();
        mRowName.setText(technicianName);
        mRowName.setTextSize(24);
    }
}
