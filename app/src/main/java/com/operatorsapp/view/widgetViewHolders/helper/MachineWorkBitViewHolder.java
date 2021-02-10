package com.operatorsapp.view.widgetViewHolders.helper;

import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.operators.machinedatainfra.models.Widget;
import com.operatorsapp.R;
import com.operatorsapp.application.OperatorApplication;

public class MachineWorkBitViewHolder extends RecyclerView.ViewHolder {

    private final int mHeight;
    private final int mWidth;
    private final TextView mTitle;
    private Switch mSwitch;

    public MachineWorkBitViewHolder(@NonNull View itemView, int height, int width) {
        super(itemView);

        mHeight = height;
        mWidth = width;
        mTitle = itemView.findViewById(R.id.MWBC_title);
        mSwitch = itemView.findViewById(R.id.MWBC_switch_SW);
        LinearLayout parentLayout = itemView.findViewById(R.id.widget_parent_layout);
        setSizes(parentLayout);

        initListeners();
    }

    private void initListeners() {
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                
            }
        });
    }

    public void setData(Widget widget){
        String nameByLang2 = OperatorApplication.isEnglishLang() ? widget.getFieldEName() : widget.getFieldLName();
        mTitle.setText(nameByLang2);
        initSpinnerMode(widget.getCurrentValue());
    }

    private void initSpinnerMode(String currentValue) {
        if (currentValue.equals("1")){
            mSwitch.setChecked(true);
        }else {
            mSwitch.setChecked(false);
        }
    }


    private void setSizes(final LinearLayout parent) {
        ViewGroup.LayoutParams layoutParams;
        layoutParams = parent.getLayoutParams();
        layoutParams.height = (int) (mHeight * 0.5);
        layoutParams.width = (int) (mWidth * 0.325);
        parent.requestLayout();

    }
}
