package com.operatorsapp.view.widgetViewHolders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.operatorsapp.R;
import com.operatorsapp.model.event.QCTestEvent;

import org.greenrobot.eventbus.EventBus;

public class NewWidgetViewHolder extends RecyclerView.ViewHolder {
    private TextView buttonOrderTestTV;


    public NewWidgetViewHolder(@NonNull View itemView) {
        super(itemView);

        buttonOrderTestTV = itemView.findViewById(R.id.TLWC_countdown_btn);
        buttonOrderTestTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new QCTestEvent(0));
            }
        });
    }
}
