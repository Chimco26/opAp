package com.operatorsapp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AdapterView;


public class EmeraldSpinner extends android.support.v7.widget.AppCompatSpinner {
   AdapterView.OnItemSelectedListener listener;

    public EmeraldSpinner(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public EmeraldSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public EmeraldSpinner(Context context, AttributeSet attrs, int defStyleAttr, int mode) {
        super(context, attrs, defStyleAttr, mode);
    }


    public EmeraldSpinner(Context context) {
        super(context);
    }

    public EmeraldSpinner(Context context, int mode) {
        super(context, mode);
    }

    @Override
    public void setSelection(int position)
    {
        super.setSelection(position);

        if (position == getSelectedItemPosition() && listener != null)
        {
            listener.onItemSelected(null, null, position, 0);
        }
    }

    public void setOnItemSelectedListener(AdapterView.OnItemSelectedListener listener)
    {
        this.listener = listener;
    }
}
