package com.operatorsapp.adapters;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.operatorsapp.R;

/**
 * Created by User on 17/07/2016.
 */
public class JobsSpinnerAdapter extends ArrayAdapter<String>
{
    private Activity mContext;
    private String[] mSpinnerItems = null;
    TextView mRowName;

    public JobsSpinnerAdapter(Activity context, int resource, String[] spinnerItems)
    {
        super(context, resource, spinnerItems);
        this.mContext = context;
        this.mSpinnerItems = spinnerItems;
    }

   @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        if (row == null)
        {
            LayoutInflater inflater = mContext.getLayoutInflater();
            row = inflater.inflate(R.layout.spinner_job_item, parent, false);
            TextView rowName = (TextView)row.findViewById(R.id.spinner_job_item_name);
            rowName.setTextSize(20);

        }
        return row;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        if (row == null)
        {
            LayoutInflater inflater = mContext.getLayoutInflater();
            row = inflater.inflate(R.layout.spinner_job_item, parent, false);
        }

        String item = mSpinnerItems[position];
        if (item != null)
        {
            mRowName = (TextView) row.findViewById(R.id.spinner_job_item_name);
            mRowName.setText(item);
            mRowName.setTextAppearance(getContext(),R.style.FontStyle_T10);
            mRowName.setTextSize(17);
            if (position == 0)
            {
                mRowName.setTextColor(Color.BLACK);
                mRowName.setText(item);
                row.setClickable(false);
            }
            else if (position > 0)
            {
                mRowName.setTextColor(Color.GRAY);
                mRowName.setText(item);
                row.setClickable(true);
            }
        }
        return row;
    }

}
