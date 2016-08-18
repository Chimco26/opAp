package com.operatorsapp.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.operatorsapp.R;
import com.operatorsapp.managers.PersistenceManager;

/**
 * Created by User on 17/07/2016.
 */
public class LanguagesSpinnerAdapter extends ArrayAdapter<String> {
    private Activity mContext;
    private String[] mSpinnerItems = null;
    private TextView mRowName;
    private View mView;

    public LanguagesSpinnerAdapter(Activity context, int resource, String[] spinnerItems) {
        super(context, resource, spinnerItems);
        this.mContext = context;
        this.mSpinnerItems = spinnerItems;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = mContext.getLayoutInflater();
            row = inflater.inflate(R.layout.spinner_language_item, parent, false);
            mView = row;
            mRowName = (TextView) row.findViewById(R.id.spinner_language_item_name);
            mRowName.setTextSize(20);
            mRowName.setText(PersistenceManager.getInstance().getCurrentLanguageName());

        }
        return row;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = mContext.getLayoutInflater();
            row = inflater.inflate(R.layout.spinner_language_item, parent, false);
        }

        String item = mSpinnerItems[position];
        if (item != null) {
            mRowName = (TextView) row.findViewById(R.id.spinner_language_item_name);
            mRowName.setText(item);
            mRowName.setTextColor(ContextCompat.getColor(mContext, R.color.status_bar));
            mRowName.setTextSize(17);
        }
        return row;
    }

    public void setTitle(int position) {
        mRowName = (TextView) mView.findViewById(R.id.spinner_language_item_name);
        mRowName.setTextColor(ContextCompat.getColor(mContext, R.color.status_bar));
        mRowName.setText(mSpinnerItems[position]);
        mRowName.setTextSize(20);
    }
    public void setTitle(String language){
        mRowName = (TextView) mView.findViewById(R.id.spinner_language_item_name);
        mRowName.setTextColor(ContextCompat.getColor(mContext, R.color.status_bar));
        mRowName.setText(language);
        mRowName.setTextSize(20);
    }
}