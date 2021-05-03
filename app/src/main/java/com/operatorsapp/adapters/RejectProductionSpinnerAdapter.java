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

import com.operators.reportfieldsformachineinfra.PackageTypes;
import com.operatorsapp.R;
import com.operatorsapp.application.OperatorApplication;

import java.util.List;

public class RejectProductionSpinnerAdapter extends ArrayAdapter<PackageTypes> {
    private List<PackageTypes> mSpinnerItems;
    private TextView mRowName;
    private View mView;

    public RejectProductionSpinnerAdapter(Context context, int resource, List<PackageTypes> packageTypesList) {
        super(context, resource, packageTypesList);
        mSpinnerItems = packageTypesList;
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
            mRowName.setTextColor(ContextCompat.getColor(getContext(), R.color.status_bar));
            String nameByLang = OperatorApplication.isEnglishLang() ? mSpinnerItems.get(0).getEName() : mSpinnerItems.get(0).getLName();
            mRowName.setText(nameByLang);
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
        String item = OperatorApplication.isEnglishLang() ? mSpinnerItems.get(position).getEName() : mSpinnerItems.get(position).getLName();
        if (item != null) {
            TextView name = row.findViewById(R.id.spinner_item_name);
            name.setText(item);
            name.setTextColor(ContextCompat.getColor(getContext(), R.color.status_bar));
            name.setTextSize(22);
        }
        return row;
    }

    public void setTitle(int position) {

        mRowName = mView.findViewById(R.id.spinner_item_name);
        mRowName.setTextColor(ContextCompat.getColor(getContext(), R.color.status_bar));
        String nameByLang = OperatorApplication.isEnglishLang() ? mSpinnerItems.get(position).getEName() : mSpinnerItems.get(position).getLName();
        mRowName.setText(nameByLang);
        mRowName.setTextSize(24);
    }
}
