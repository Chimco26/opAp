package com.operatorsapp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AdapterView;
import android.widget.Spinner;


public class EmeraldSpinner extends Spinner {
   AdapterView.OnItemSelectedListener listener;

    public EmeraldSpinner(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }


    @Override
    public void setSelection(int position)
    {
        super.setSelection(position);

        if (position == getSelectedItemPosition())
        {
            listener.onItemSelected(null, null, position, 0);
        }
    }

    public void setOnItemSelectedListener(AdapterView.OnItemSelectedListener listener)
    {
        this.listener = listener;
    }
}
