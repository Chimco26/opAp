package com.operatorsapp.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.operatorsapp.R;

public class ProductionSpinnerAdapter extends ArrayAdapter<String> {
    private static final String PRODUCTION = "Production";
    private static final String NO_PRODUCTION = "No Production";
    private static final String PREVENTIVE_MAINTENANCE = "Preventive Maintenance";
    private static final String SHUTDOWN = "Shutdown";
    private static final String BREAKDOWN = "Breakdown";

    private Activity mContext;
    private String[] mSpinnerItems;
    private String mCurrentProduction;
    private ImageView spinnerImage;

    public ProductionSpinnerAdapter(Activity context, int resource, String[] operators, String currentProduction) {
        super(context, resource, operators);
        mSpinnerItems = operators;
        mContext = context;
        mCurrentProduction = currentProduction;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = mContext.getLayoutInflater();
            row = inflater.inflate(R.layout.spinner_production_item, parent, false);
            spinnerImage = row.findViewById(R.id.SPI_image);
            if (mCurrentProduction == null || mCurrentProduction.equals("")) {
                spinnerImage.setImageDrawable(getContext().getResources().getDrawable(R.drawable.production));
            } else {
                setIcon(mCurrentProduction, false, spinnerImage);
            }
        }
        return row;
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = mContext.getLayoutInflater();
            row = inflater.inflate(R.layout.spinner_production_item_dropdown, parent, false);
        }
        String item = mSpinnerItems[position];
        if (item != null) {
            TextView name = row.findViewById(R.id.SPID_texte);
            name.setText(item);
            setIcon(item, false, (ImageView) row.findViewById(R.id.SPID_image));
            if (mCurrentProduction.equals(item)) {
                name.setTextColor(mContext.getResources().getColor(R.color.blue1));
                setIcon(item, true, (ImageView) row.findViewById(R.id.SPID_image));
                ((ImageView) row.findViewById(R.id.SPID_image)).setBackground(getContext().getResources().getDrawable(R.drawable.circle_blue));
            } else {
                name.setTextColor(mContext.getResources().getColor(R.color.white));
                setIcon(item, false, (ImageView) row.findViewById(R.id.SPID_image));
                ((ImageView) row.findViewById(R.id.SPID_image)).setBackground(getContext().getResources().getDrawable(R.drawable.circle_white));
            }
        }
        return row;
    }

    private void setIcon(String item, boolean selected, ImageView imageView) {

        switch (item) {
            case PRODUCTION:
                if (selected) {
                    imageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.production_blue));
                } else {
                    imageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.production));
                }
                break;
            case NO_PRODUCTION:
                if (selected) {
                    imageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.no_production));
                } else {
                    imageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.no_production));
                }
                break;
            case PREVENTIVE_MAINTENANCE:
                if (selected) {
                    imageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.preventive_blue));
                } else {
                    imageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.preventive));
                }
                break;
            case SHUTDOWN:
                if (selected) {
                    imageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.shutdown_blue));
                } else {
                    imageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.shutdown));
                }
                break;
            case BREAKDOWN:
                if (selected) {
                    imageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.breakdown_blue));
                } else {
                    imageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.breakdown));
                }
                break;
        }
    }

    public void updateTitle(String newTitle) {
        mCurrentProduction = newTitle;
        notifyDataSetChanged();
    }
}