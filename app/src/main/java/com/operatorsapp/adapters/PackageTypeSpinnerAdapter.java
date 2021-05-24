package com.operatorsapp.adapters;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.common.PackageTypesResponse;
import com.operators.reportfieldsformachineinfra.PackageTypes;
import com.operatorsapp.R;
import com.operatorsapp.application.OperatorApplication;

import java.util.List;

public class PackageTypeSpinnerAdapter extends ArrayAdapter<PackageTypesResponse.PackageType> {
    private List<PackageTypesResponse.PackageType> mSpinnerItems;
    private TextView mRowName;
    private View mView;

    public PackageTypeSpinnerAdapter(Context context,  int base_spinner_item, List<PackageTypesResponse.PackageType> packageTypes) {
        super(context, base_spinner_item, packageTypes);
        mSpinnerItems = packageTypes;
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
            String color = mSpinnerItems.get(position).getColor();
            if (color == null) {
                mRowName.setTextColor(ContextCompat.getColor(getContext(), R.color.status_bar));
            }else {
                mRowName.setTextColor(Color.parseColor(color));
            }
            String nameByLang = OperatorApplication.isEnglishLang() ? mSpinnerItems.get(position).getEName() : mSpinnerItems.get(position).getLName();
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
            String color = mSpinnerItems.get(position).getColor();
            if (color == null) {
                mRowName.setTextColor(ContextCompat.getColor(getContext(), R.color.status_bar));
            }else {
                mRowName.setTextColor(Color.parseColor(color));
            }
            name.setTextSize(22);
        }
        return row;
    }

    public void setTitle(int position) {

        mRowName = mView.findViewById(R.id.spinner_item_name);
        String color = mSpinnerItems.get(position).getColor();
        if (color == null) {
            mRowName.setTextColor(ContextCompat.getColor(getContext(), R.color.status_bar));
        }else {
            mRowName.setTextColor(Color.parseColor(color));
        }
        String nameByLang = OperatorApplication.isEnglishLang() ? mSpinnerItems.get(position).getEName() : mSpinnerItems.get(position).getLName();
        mRowName.setText(nameByLang);
        mRowName.setTextSize(24);
    }
}
