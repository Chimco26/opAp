package com.operatorsapp.adapters;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.operators.reportfieldsformachineinfra.PackageTypes;
import com.operatorsapp.R;
import com.operatorsapp.application.OperatorApplication;

import java.util.List;

public class ProductionSpinnerAdapter extends ArrayAdapter<PackageTypes> {

    private Activity mContext;
    private List<PackageTypes> mSpinnerItems;
    private int mCurrentProductionId;

    public ProductionSpinnerAdapter(Activity context, int resource, List<PackageTypes> operators, int currentProduction) {
        super(context, resource, operators);
        mSpinnerItems = operators;
        mContext = context;
        mCurrentProductionId = currentProduction;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = mContext.getLayoutInflater();
            row = inflater.inflate(R.layout.spinner_production_item, parent, false);
            ImageView spinnerImage = row.findViewById(R.id.SPI_image);
            if (mSpinnerItems.get(mCurrentProductionId).getEName() == null || mSpinnerItems.get(mCurrentProductionId).getEName().equals("")) {
                spinnerImage.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_production_white));
            } else {
                setIcon(mSpinnerItems.get(mCurrentProductionId).getId(), true, spinnerImage);
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
        if (mSpinnerItems.get(position) != null) {
            TextView name = row.findViewById(R.id.SPID_texte);
            name.setText(OperatorApplication.isEnglishLang() ? mSpinnerItems.get(position).getEName() : mSpinnerItems.get(position).getLName());
            setIcon(mSpinnerItems.get(position).getId(), false, (ImageView) row.findViewById(R.id.SPID_image));
            GradientDrawable textBackground = (GradientDrawable) (row.findViewById(R.id.SPID_image)).getBackground();

            if (mSpinnerItems.get(mCurrentProductionId).getId() == mSpinnerItems.get(position).getId()) {
                name.setTextColor(mContext.getResources().getColor(R.color.blue1));
                setIcon(mSpinnerItems.get(position).getId(), true, (ImageView) row.findViewById(R.id.SPID_image));
                textBackground.setColor(mContext.getResources().getColor(R.color.blue1));
//                ((row.findViewById(R.id.SPID_image))).setBackground(getContext().getResources().getDrawable(R.drawable.circle_blue));
            } else {
                name.setTextColor(mContext.getResources().getColor(R.color.white));
                setIcon(mSpinnerItems.get(position).getId(), false, (ImageView) row.findViewById(R.id.SPID_image));
                textBackground.setColor(mContext.getResources().getColor(R.color.white));
            }
        }
        return row;
    }

    private void setIcon(int item, boolean selected, ImageView imageView) {

        switch (item) {
            case 1:
                if (selected) {
                    imageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_production_white));
                } else {
                    imageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_production_blue));
                }
                break;
            default:
                if (selected) {
                    imageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_no_production_white));
                } else {
                    imageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_no_production_blue));
                }
        }
    }

}