package com.operatorsapp.view.widgetViewHolders;

import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.operators.machinedatainfra.models.Widget;
import com.operatorsapp.R;
import com.operatorsapp.application.OperatorApplication;

public class FreeTextViewHolder extends RecyclerView.ViewHolder {

    private final TextView mTitle;
    private final TextView mTextView;
    private final int mHeight;
    private final int mWidth;


    public FreeTextViewHolder(@NonNull View itemView, int height, int width) {
        super(itemView);

        mTitle = itemView.findViewById(R.id.HFT_title);
        mTextView = itemView.findViewById(R.id.HFT_textView);
        mHeight = height;
        mWidth = width;
        LinearLayout parentLayout = itemView.findViewById(R.id.widget_parent_layout);
        setSizes(parentLayout);
    }

    public void setData(Widget widget){
        String nameByLang2 = OperatorApplication.isEnglishLang() ? widget.getFieldEName() : widget.getFieldLName();
        mTitle.setText(nameByLang2);
        mTextView.setText(widget.getCurrentValue());
    }

    private void setSizes(final LinearLayout parent) {
        ViewGroup.LayoutParams layoutParams;
        layoutParams = parent.getLayoutParams();
        layoutParams.height = (int) (mHeight * 0.5);
        layoutParams.width = (int) (mWidth * 0.325);
        parent.requestLayout();

    }
}
