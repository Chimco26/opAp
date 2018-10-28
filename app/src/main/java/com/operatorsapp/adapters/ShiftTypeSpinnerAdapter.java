package com.operatorsapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.operatorsapp.R;

import java.util.List;

public class ShiftTypeSpinnerAdapter extends ArrayAdapter<String> {
    private final LayoutInflater mInflater;
    private List<String> members;
    private TextView mRowName;
    private View mView;
    private String mRowText;

    public ShiftTypeSpinnerAdapter(Context context, int resource, List<String> members, LayoutInflater inflater) {
        super(context, resource, members);
        this.members = members;
        mInflater = inflater;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            row = mInflater.inflate(R.layout.item_product_spinner, parent, false);
            mView = row;
            mRowName = row.findViewById(R.id.IPSL_name);
            mRowName.setText(members.get(position));
        }
        return row;
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            row = mInflater.inflate(R.layout.item_product_spinner_list, parent, false);
        }
        TextView name = row.findViewById(R.id.IPSL_name);
        name.setText(members.get(position));

        return row;
    }

    public void setTitle(int position) {
        if (mView != null) {
            mRowName = mView.findViewById(R.id.IPSL_name);
            mRowText = members.get(position);
            mRowName.setText(mRowText);
        }
    }

    public String getRowText() {
        return mRowText;
    }
}
