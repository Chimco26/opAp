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

import com.operatorsapp.R;
import com.operatorsapp.managers.PersistenceManager;

public class LanguagesSpinnerAdapterActionBar extends ArrayAdapter<String> {
    private String[] mSpinnerItems;
    private TextView mRowName;
    //    private View mView;
    private boolean mIsFirst = true;

    public LanguagesSpinnerAdapterActionBar(Context context, int resource, String[] spinnerItems) {
        super(context, resource, spinnerItems);
        this.mSpinnerItems = spinnerItems;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            row = inflater.inflate(R.layout.spinner_language_item, parent, false);
//            mView = row;
            mRowName = row.findViewById(R.id.spinner_language_item_name);
            mRowName.setTextSize(20);
            if (mIsFirst) {
                setTitle(PersistenceManager.getInstance().getCurrentLanguageName());
                mIsFirst = false;
            }
            else {
                setTitle(position);
            }


        }
        return row;
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            row = inflater.inflate(R.layout.spinner_language_item, parent, false);
        }

        String item = mSpinnerItems[position];
        if (item != null) {
            mRowName = row.findViewById(R.id.spinner_language_item_name);
            mRowName.setText(item);
            mRowName.setTextColor(ContextCompat.getColor(getContext(), R.color.status_bar));
            mRowName.setTextSize(17);
        }
        return row;
    }

    public void setTitle(int position) {
        mRowName.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        mRowName.setText(mSpinnerItems[position]);
        mRowName.setTextSize(20);
    }

    public void setTitle(String language) {
        mRowName.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        mRowName.setText(language);
        mRowName.setTextSize(20);
    }
}