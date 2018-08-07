package com.operatorsapp.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.operators.reportfieldsformachineinfra.PackageTypes;
import com.operatorsapp.R;
import com.operatorsapp.application.OperatorApplication;

import java.util.List;

public class RejectInventorySpinnerAdapter extends ArrayAdapter<PackageTypes> {
    private Activity mContext;
    private List<PackageTypes> mSpinnerItems;
    private TextView mRowName;
    private View mView;

    public RejectInventorySpinnerAdapter(Activity context, int resource, List<PackageTypes> packageTypesList) {
        super(context, resource, packageTypesList);
        mSpinnerItems = packageTypesList;
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
            LayoutInflater inflater = mContext.getLayoutInflater();
            row = inflater.inflate(R.layout.base_spinner_item, parent, false);
        }
        String item = OperatorApplication.isEnglishLang() ? mSpinnerItems.get(position).getEName() : mSpinnerItems.get(position).getLName();
        if (item != null) {
            TextView name = row.findViewById(R.id.spinner_item_name);
            name.setText(item);
            name.setTextColor(ContextCompat.getColor(mContext, R.color.status_bar));
            name.setTextSize(22);
        }
        return row;
    }

    public void setTitle(int position) {

        mRowName = mView.findViewById(R.id.spinner_item_name);
        mRowName.setTextColor(ContextCompat.getColor(mContext, R.color.status_bar));
        String nameByLang = OperatorApplication.isEnglishLang() ? mSpinnerItems.get(position).getEName() : mSpinnerItems.get(position).getLName();
        mRowName.setText(nameByLang);
        mRowName.setTextSize(24);
    }
}
