package com.operatorsapp.adapters;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
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
    private Activity mContext;
    private List<RejectCauses> mSpinnerItems;
    TextView mRowName;
    View mView;

    public RejectCauseSpinnerAdapter(Activity context, int resource, List<RejectCauses> rejectCauses) {
        super(context, resource, rejectCauses);
        mSpinnerItems = rejectCauses;
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = mContext.getLayoutInflater();
            row = inflater.inflate(R.layout.base_spinner_item, parent, false);
            mView = row;
            mRowName = (TextView) row.findViewById(R.id.spinner_item_name);
            mRowName.setTextColor(ContextCompat.getColor(mContext, R.color.status_bar));
            String nameByLang = OperatorApplication.isEnglishLang() ?mSpinnerItems.get(0).getEName() : mSpinnerItems.get(0).getLName();
            if(mSpinnerItems != null && mSpinnerItems.get(0) != null)
            {
                mRowName.setText(nameByLang);
            }
            else
            {
                mRowName.setText(mContext.getString(R.string.dashes));
            }
            mRowName.setTextSize(18);
        }
        return row;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = mContext.getLayoutInflater();
            row = inflater.inflate(R.layout.base_spinner_item, parent, false);
        }
        String nameByLang = OperatorApplication.isEnglishLang() ?mSpinnerItems.get(position).getEName() : mSpinnerItems.get(position).getLName();
        String item = nameByLang;
        if (item != null) {
            TextView name = (TextView) row.findViewById(R.id.spinner_item_name);
            name.setText(item);
            name.setTextColor(ContextCompat.getColor(mContext, R.color.status_bar));
            name.setTextSize(17);
        }
        return row;
    }

    public void setTitle(int position) {

        mRowName = (TextView) mView.findViewById(R.id.spinner_item_name);
        mRowName.setTextColor(ContextCompat.getColor(mContext, R.color.status_bar));
        String nameByLang = OperatorApplication.isEnglishLang() ?mSpinnerItems.get(position).getEName() : mSpinnerItems.get(position).getLName();
        mRowName.setText(nameByLang);
        mRowName.setTextSize(20);
    }
}
