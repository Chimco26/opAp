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

import com.operators.reportfieldsformachineinfra.RejectCauses;
import com.operatorsapp.R;
import com.operatorsapp.application.OperatorApplication;

import java.util.List;

public class RejectCauseSpinnerAdapter extends ArrayAdapter<RejectCauses> {
    private List<RejectCauses> mSpinnerItems;
    private TextView mRowName;
    private View mView;

    public RejectCauseSpinnerAdapter(Context context, int resource, List<RejectCauses> rejectCauses) {
        super(context, resource, rejectCauses);
        mSpinnerItems = rejectCauses;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            row = LayoutInflater.from(getContext()).inflate(R.layout.base_spinner_item, parent, false);
            mView = row;
            mRowName = row.findViewById(R.id.spinner_item_name);
            mRowName.setTextColor(ContextCompat.getColor(getContext(), R.color.status_bar));
            String nameByLang = OperatorApplication.isEnglishLang() ?mSpinnerItems.get(0).getEName() : mSpinnerItems.get(0).getLName();
            if(mSpinnerItems != null && mSpinnerItems.get(0) != null)
            {
                mRowName.setText(nameByLang);
            }
            else
            {
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
            row = inflater.inflate(R.layout.base_spinner_item, parent, false);
        }
        String item = OperatorApplication.isEnglishLang() ?mSpinnerItems.get(position).getEName() : mSpinnerItems.get(position).getLName();
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
        String nameByLang = OperatorApplication.isEnglishLang() ?mSpinnerItems.get(position).getEName() : mSpinnerItems.get(position).getLName();
        mRowName.setText(nameByLang);
        mRowName.setTextSize(24);
    }
}
