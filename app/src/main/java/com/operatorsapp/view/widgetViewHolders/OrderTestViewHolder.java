package com.operatorsapp.view.widgetViewHolders;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.operators.machinedatainfra.models.Widget;
import com.operatorsapp.R;
import com.operatorsapp.application.OperatorApplication;
import com.operatorsapp.model.event.QCTestEvent;

import org.greenrobot.eventbus.EventBus;

public class OrderTestViewHolder extends RecyclerView.ViewHolder {
    private TextView buttonOrderTestTV;
//    private final TextView mTitle;
    private final int mHeight;
    private final int mWidth;


    public OrderTestViewHolder(@NonNull View itemView, int height, int width) {
        super(itemView);

        mHeight = height;
        mWidth = width;
//        mTitle = itemView.findViewById(R.id.OTWC_title);
        LinearLayout parentLayout = itemView.findViewById(R.id.widget_parent_layout);
        setSizes(parentLayout);

        buttonOrderTestTV = itemView.findViewById(R.id.OTWC_countdown_btn);
        buttonOrderTestTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new QCTestEvent(0));
            }
        });
    }

    public void setData(Widget widget){
//        String nameByLang2 = OperatorApplication.isEnglishLang() ? widget.getFieldEName() : widget.getFieldLName();
//        mTitle.setText(nameByLang2);
    }

    private void setSizes(final LinearLayout parent) {
        ViewGroup.LayoutParams layoutParams;
        layoutParams = parent.getLayoutParams();
        layoutParams.height = (int) (mHeight * 0.5);
        layoutParams.width = (int) (mWidth * 0.325);
        parent.requestLayout();

    }
}
