package com.operatorsapp.view.widgetViewHolders;

import android.os.CountDownTimer;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.operators.machinedatainfra.models.Widget;
import com.operatorsapp.R;

public class TimeLeftViewHolder extends RecyclerView.ViewHolder {
    private final CountDownView mCountDown;

    public TimeLeftViewHolder(View itemView) {
        super(itemView);

        mCountDown = itemView.findViewById(R.id.TLWC_countdown);
    }

    public void setData(Widget widget) {

        new CountDownTimer(60000, 100) {

            public void onTick(long millisUntilFinished) {
                mCountDown.setEndModeTimeInMinute(10);
                mCountDown.update((int) ((60000 - millisUntilFinished) / 1000), ":00");
            }
            public void onFinish() {
            }
        }.start();
    }
}
