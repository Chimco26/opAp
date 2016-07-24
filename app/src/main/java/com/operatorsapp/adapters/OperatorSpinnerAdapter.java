package com.operatorsapp.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.operatorsapp.R;
import com.operatorsapp.view.RoundedImageView;

public class OperatorSpinnerAdapter extends ArrayAdapter<String>
{

    private Activity context;
    private String[] data = null;

    public OperatorSpinnerAdapter(Activity context, int resource, String[] data2)
    {
        super(context, resource, data2);
        this.context = context;
        this.data = data2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        if (row == null)
        {
            LayoutInflater inflater = context.getLayoutInflater();
            row = inflater.inflate(R.layout.spinner_operator_item, parent, false);
        }
        String item = data[position];
        if (item != null)
        {
            TextView name = (TextView) row.findViewById(R.id.spinner_operator_item_name);
            name.setTextColor(Color.WHITE);
            name.setText(item);

            RoundedImageView image = (RoundedImageView) row.findViewById(R.id.spinner_operator_item_image);
        }

        return row;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        if (row == null)
        {
            LayoutInflater inflater = context.getLayoutInflater();
            row = inflater.inflate(R.layout.spinner_operator_item, parent, false);
        }
        String item = data[position];
        if (item != null)
        {
            TextView name = (TextView) row.findViewById(R.id.spinner_operator_item_name);
            name.setTextColor(Color.BLACK);
            name.setText(item);

            RoundedImageView image = (RoundedImageView) row.findViewById(R.id.spinner_operator_item_image);
        }

        return row;
    }

}