package com.operatorsapp.interfaces;

import android.view.View;
import android.widget.FrameLayout;

import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;

public interface DashboardChartCallbackListener {

    void onChartStart(View container, ArrayList<Entry> values);
}
