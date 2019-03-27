package com.operatorsapp.view.widgetViewHolders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.data.Entry;
import com.operators.machinedatainfra.models.Widget;
import com.operatorsapp.R;
import com.operatorsapp.activities.interfaces.GoToScreenListener;
import com.operatorsapp.application.OperatorApplication;
import com.operatorsapp.fragments.ChartFragment;
import com.operatorsapp.utils.TimeUtils;
import com.operatorsapp.view.LineChartTimeSmall;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import me.grantland.widget.AutofitTextView;

import static android.text.format.DateUtils.DAY_IN_MILLIS;

public class TimeViewHolder extends RecyclerView.ViewHolder {

    private static final long FOUR_HOURS = 60000L * 60 * 4;

    private final Context mContext;
    private final int mHeight;
    private final int mWidth;
    private RelativeLayout mParentLayout;
    private View mDivider;
    private LineChartTimeSmall mChart;
    private AutofitTextView mTitle;
    private AutofitTextView mSubtitle;
    private TextView mValue;
    private GoToScreenListener mGoToScreenListener;


    public TimeViewHolder(View itemView, Context context, GoToScreenListener listener, int height, int width) {
        super(itemView);

        mContext = context;
        mHeight = height;
        mWidth = width;
        mGoToScreenListener = listener;

        mParentLayout = itemView.findViewById(R.id.widget_parent_layout);
        mDivider = itemView.findViewById(R.id.divider);
        mTitle = itemView.findViewById(R.id.time_widget_title);
        mSubtitle = itemView.findViewById(R.id.time_widget_subtitle);
        mValue = itemView.findViewById(R.id.time_widget_current_value);
        mChart = itemView.findViewById(R.id.lineChart_time);
    }
    
    public void setTimeItem(final Widget widget) {
//        mDivider.post(new Runnable() {
//            @Override
//            public void run() {
//                ViewGroup.MarginLayoutParams mItemViewParams2;
//                mItemViewParams2 = (ViewGroup.MarginLayoutParams) mDivider.getLayoutParams();
//                mItemViewParams2.setMargins(0, (int) (mParentLayout.getHeight() * 0.3), 0, 0);
//                mDivider.requestLayout();
//            }
//        });
//

//        setSizes(mParentLayout);
        String nameByLang2 = OperatorApplication.isEnglishLang() ? widget.getFieldEName() : widget.getFieldLName();
        mTitle.setText(nameByLang2);
        mSubtitle.setText(new StringBuilder(mContext.getString(R.string.standard)).append(widget.getStandardValue()));
        mValue.setText(widget.getCurrentValue());
        try {
            if (Float.valueOf(widget.getCurrentValue()) < widget.getLowLimit() || Float.valueOf(widget.getCurrentValue()) > widget.getHighLimit()) {
                mValue.setTextColor(mContext.getResources().getColor(R.color.red_line));
            } else {
                mValue.setTextColor(mContext.getResources().getColor(R.color.C16));
            }
        } catch (NumberFormatException e) {
            mValue.setTextColor(mContext.getResources().getColor(R.color.C16));
        }
        int xValuesIncreaseIndex = 0;
        ArrayList<Entry> tenHoursValues = new ArrayList<>();
        ArrayList<Entry> fourHoursValues = new ArrayList<>();
        ArrayList<ArrayList<Entry>> fourHoursList = new ArrayList<>();
        final ArrayList<ArrayList<Entry>> tenHoursList = new ArrayList<>();
        float midnightLimit = 0;
        if (widget.getMachineParamHistoricData() != null && widget.getMachineParamHistoricData().size() > 0) {
            Collections.sort(widget.getMachineParamHistoricData());
            final String[] xValues = new String[widget.getMachineParamHistoricData().size() + 1];
            for (int i = 0; i < widget.getMachineParamHistoricData().size(); i++) {
                try {
                    Date date = TimeUtils.getTodayMidnightDate();
                    if (i > 0 && xValues.length > 0 && date != null &&
                            TimeUtils.getDateForNotification(widget.getMachineParamHistoricData().get(i - 1).getTime()) != null &&
                            TimeUtils.getDateForNotification(widget.getMachineParamHistoricData().get(i).getTime()) != null &&
                            TimeUtils.getDateForNotification(widget.getMachineParamHistoricData().get(i - 1).getTime()).before(date) &&
                            TimeUtils.getDateForNotification(widget.getMachineParamHistoricData().get(i).getTime()).after(date)) {
                        xValues[i] = mContext.getResources().getString(R.string.midnight);
                        xValuesIncreaseIndex = 1;
                        midnightLimit = i;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                xValues[i + xValuesIncreaseIndex] = TimeUtils.getDateForChart(widget.getMachineParamHistoricData().get(i).getTime());
                Entry entry;
                try {
                    entry = new Entry(i, widget.getMachineParamHistoricData().get(i).getValue());
                } catch (Exception e) {
                    entry = null;
                }
                if (entry == null) {
                    if (fourHoursValues.size() > 0) {
                        fourHoursList.add(fourHoursValues);
                    }
                    if (tenHoursValues.size() > 0) {
                        tenHoursList.add(tenHoursValues);
                    }
                    tenHoursValues = new ArrayList<>();
                    fourHoursValues = new ArrayList<>();
                } else {
                    if (TimeUtils.getLongFromDateString(widget.getMachineParamHistoricData().get(i).getTime(), "dd/MM/yyyy HH:mm:ss") > (TimeUtils.getLongFromDateString(widget.getMachineParamHistoricData().get(widget.getMachineParamHistoricData().size() - 1).getTime(), "dd/MM/yyyy HH:mm:ss") - DAY_IN_MILLIS)) {
                        tenHoursValues.add(entry);
                    }
                    if (TimeUtils.getLongFromDateString(widget.getMachineParamHistoricData().get(i).getTime(), "dd/MM/yyyy HH:mm:ss") > (TimeUtils.getLongFromDateString(widget.getMachineParamHistoricData().get(widget.getMachineParamHistoricData().size() - 1).getTime(), "dd/MM/yyyy HH:mm:ss") - FOUR_HOURS)) {
                        fourHoursValues.add(entry);
                    }
                }
            }
            if (fourHoursValues.size() > 0) {
                fourHoursList.add(fourHoursValues);
                tenHoursList.add(tenHoursValues);
            }
            mChart.setData(fourHoursList, xValues, widget.getLowLimit(), widget.getHighLimit());
            mChart.setLimitLines(widget.getLowLimit(), widget.getHighLimit(), widget.getStandardValue(), midnightLimit);
            final float midnightFinal = midnightLimit;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tenHoursList.size() > 0) {
                        String nameByLang = OperatorApplication.isEnglishLang() ? widget.getFieldEName() : widget.getFieldLName();
                        mGoToScreenListener.goToFragment(ChartFragment.newInstance(tenHoursList, widget.getLowLimit(), widget.getStandardValue(), widget.getHighLimit(), xValues, nameByLang, midnightFinal), true, false);
                    }
                }
            });
        }
    }

    private void setSizes(final RelativeLayout parent) {
        ViewGroup.LayoutParams layoutParams;
        layoutParams = parent.getLayoutParams();
        layoutParams.height = (int) (mHeight * 0.45);
        layoutParams.width = (int) (mWidth * 0.325);
        parent.requestLayout();

    }
}
