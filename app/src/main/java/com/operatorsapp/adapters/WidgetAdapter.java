package com.operatorsapp.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.data.Entry;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.operators.machinedatainfra.models.Widget;
import com.operators.reportfieldsformachineinfra.ReportFieldsForMachine;
import com.operatorsapp.R;
import com.operatorsapp.activities.interfaces.GoToScreenListener;
import com.operatorsapp.application.OperatorApplication;
import com.operatorsapp.fragments.ChartFragment;
import com.operatorsapp.interfaces.DashboardCentralContainerListener;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.utils.KeyboardUtils;
import com.operatorsapp.utils.TimeUtils;
import com.operatorsapp.view.LineChartTimeSmall;
import com.operatorsapp.view.ProjectionView;
import com.operatorsapp.view.ProjectionViewEnd;
import com.operatorsapp.view.ProjectionViewStart;
import com.operatorsapp.view.RangeView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.grantland.widget.AutofitTextView;

import static android.support.v7.widget.RecyclerView.Adapter;
import static android.support.v7.widget.RecyclerView.ViewHolder;
import static android.text.format.DateUtils.DAY_IN_MILLIS;
import static com.operatorsapp.activities.DashboardActivity.REPORT_REJECT_TAG;
import static com.operatorsapp.activities.DashboardActivity.REPORT_UNIT_CYCLE_TAG;

public class WidgetAdapter extends Adapter {
    private static final long FOUR_HOURS = 60000L * 60 * 4;
    private static final long TEN_HOURS = 60000L * 60 * 10;
    private final DashboardCentralContainerListener mDashboardCentralContainerListener;
    private Activity mContext;
    private List<Widget> mWidgets;
    private final int NUMERIC = 0;
    private final int RANGE = 1;
    private final int PROJECTION = 2;
    private final int TIME = 3;
    private final int COUNTER = 4;
    private final int IMAGE = 5;
    private GoToScreenListener mGoToScreenListener;
    private int mRangeCapsuleWidth = 0;
    private int mProjectionCapsuleWidth = 0;
    private boolean mClosedState;
    private int mHeight;
    private int mWidth;
    private ReportFieldsForMachine mReportFieldsForMachine;
    private int mSelectedReasonId;
    private int mSelectedCauseId;

    public WidgetAdapter(Activity context, List<Widget> widgets, GoToScreenListener goToScreenListener,
                         boolean closedState, int height, int width,
                         DashboardCentralContainerListener dashboardCentralContainerListener,
                         ReportFieldsForMachine reportFieldsForMachine) {
        mWidgets = widgets;
        mContext = context;
        mGoToScreenListener = goToScreenListener;
        mClosedState = closedState;
        mHeight = height;
        mWidth = width;
        mDashboardCentralContainerListener = dashboardCentralContainerListener;
        mReportFieldsForMachine = reportFieldsForMachine;
    }

    public void changeState(boolean closedState) {
        mClosedState = closedState;
        notifyDataSetChanged();
    }

    public void setNewData(List<Widget> widgets) {
        if (mWidgets != null) {
//            ArrayList<Widget> toUpdate = getWidgetIneditMode();
            mWidgets.clear();
            mWidgets.addAll(widgets);
//            updateNewWidgetWithEditMode(toUpdate);
        } else {
            mWidgets = widgets;
        }
        notifyDataSetChanged();
    }

    public void updateNewWidgetWithEditMode(ArrayList<Widget> toUpdate) {
        for (Widget widget1 : toUpdate) {
            for (Widget widget : mWidgets) {
                if (widget.getID() == widget1.getID()) {
                    widget.setEditStep(widget1.getEditStep());
                }
            }
        }
    }

    @NonNull
    public ArrayList<Widget> getWidgetIneditMode() {
        ArrayList<Widget> toUpdate = new ArrayList<>();
        for (Widget widget : mWidgets) {
            if (widget.getEditStep() > 0) {
                toUpdate.add(widget);
            }
        }
        return toUpdate;
    }

    private class NumericViewHolder extends ViewHolder {

        private final View mEditStep2Ly;
        private final Spinner mSpinner1;
        private final View mSpinner1BtnRv;
        private final Spinner mSpinner2;
        private final View mStep2CancelBtn;
        private final View mStep2ReportBtn;
        private final View mEditCycleLy;
        private final EditText mEditCycleEt;
        private final View mEditCycleCancelBtn;
        private final View mEditCycleReportBtn;
        private View mEditIc;
        private View mDisplayLy;
        private View mEditStep1Ly;
        private EditText mEditNumberEt;
        private RadioButton mUnitRadioBtn;
        private RadioButton mWeightRadioBtn;
        private View mStep1CancelBtn;
        private TextView mStep1NextBtn;
        private RelativeLayout mParentLayout;
        private View mDivider;
        private AutofitTextView mTitle;
        private AutofitTextView mSubtitle;
        private TextView mValue;
        private TextView mChangeMaterial;

        NumericViewHolder(View itemView) {
            super(itemView);

            mParentLayout = itemView.findViewById(R.id.widget_parent_layout);
            mDivider = itemView.findViewById(R.id.divider);
            mTitle = itemView.findViewById(R.id.numeric_widget_title);
            mSubtitle = itemView.findViewById(R.id.numeric_widget_subtitle);
            mValue = itemView.findViewById(R.id.numeric_widget_value);
            mChangeMaterial = itemView.findViewById(R.id.numeric_widget_change_material);
            mEditIc = itemView.findViewById(R.id.numeric_widget_edit_ic);
            mDisplayLy = itemView.findViewById(R.id.NWC_display_ly);

            mEditStep1Ly = itemView.findViewById(R.id.NWC_edit_step_1_ly);
            mEditNumberEt = itemView.findViewById(R.id.NWC_edit_number_et);
            mUnitRadioBtn = itemView.findViewById(R.id.NWC_edit_unit_btn);
            mWeightRadioBtn = itemView.findViewById(R.id.NWC_edit_weight_btn);
            mStep1CancelBtn = itemView.findViewById(R.id.NWC_edit_cancel_btn);
            mStep1NextBtn = itemView.findViewById(R.id.NWC_edit_next_btn);

            mEditStep2Ly = itemView.findViewById(R.id.NWC_edit_step_2_ly);
            mSpinner1 = itemView.findViewById(R.id.NWC_reason_spinner);
            mSpinner1BtnRv = itemView.findViewById(R.id.NWC_reason2_spinner_rl);
            mSpinner2 = itemView.findViewById(R.id.NWC_reason2_spinner);
            mStep2CancelBtn = itemView.findViewById(R.id.NWC_edit_step2_cancel_btn);
            mStep2ReportBtn = itemView.findViewById(R.id.NWC_edit_step2_next_btn);

            mEditCycleLy = itemView.findViewById(R.id.NWC_edit_quantity_ly);
            mEditCycleEt = itemView.findViewById(R.id.NWC_edit_quantity_value_et);
            mEditCycleCancelBtn = itemView.findViewById(R.id.NWC_edit_quantity_cancel_btn);
            mEditCycleReportBtn = itemView.findViewById(R.id.NWC_edit_quantity_next_btn);

        }
    }

    private class RangeViewHolder extends ViewHolder {

        private RelativeLayout mParentLayout;
        private View mDivider;
        private AutofitTextView mTitle;
        private AutofitTextView mSubtitle;
        private TextView mValue;
        private View mCapsule;
        private RangeView mRangeViewBlue;
        private TextView mCurrentValue;
        private RangeView mRangeViewRed;
        private ImageView mRedMark;
        private TextView mMin;
        private TextView mStandard;
        private TextView mMax;

        RangeViewHolder(View itemView) {
            super(itemView);

            mParentLayout = itemView.findViewById(R.id.widget_parent_layout);
            mDivider = itemView.findViewById(R.id.divider);
            mTitle = itemView.findViewById(R.id.range_widget_title);
            mSubtitle = itemView.findViewById(R.id.range_widget_subtitle);
            mValue = itemView.findViewById(R.id.range_widget_current_value);
            mCapsule = itemView.findViewById(R.id.range_widget_oval);
            mRangeViewBlue = itemView.findViewById(R.id.range_widget_range_view_blue);
            mCurrentValue = itemView.findViewById(R.id.range_widget_current_value_in_chart);
            mRangeViewRed = itemView.findViewById(R.id.range_widget_range_view_red);
            mRedMark = itemView.findViewById(R.id.range_widget_red_mark);
            mMin = itemView.findViewById(R.id.range_widget_min);
            mStandard = itemView.findViewById(R.id.range_widget_standard);
            mMax = itemView.findViewById(R.id.range_widget_max);

        }
    }

    private class ProjectionViewHolder extends ViewHolder {

        private RelativeLayout mParentLayout;
        private View mDivider;
        private AutofitTextView mTitle;
        private AutofitTextView mSubtitle;
        private TextView mValue;
        private View mCapsule;
        private View mEndDivider;
        private ProjectionView mProjectionView;
        private RangeView mRangeView;
        private ProjectionViewStart mProjectionViewStart;
        private ProjectionViewEnd mProjectionViewEnd;
        private TextView mCurrentValueInChart;
        private TextView mGrayValueInChart;
        private TextView mGrayValueInEndChart;
        private LinearLayout mBluePlus;
        private TextView mMin;
        private TextView mMax;

        ProjectionViewHolder(View itemView) {
            super(itemView);

            mParentLayout = itemView.findViewById(R.id.widget_parent_layout);
            mDivider = itemView.findViewById(R.id.divider);
            mTitle = itemView.findViewById(R.id.projection_widget_title);
            mSubtitle = itemView.findViewById(R.id.projection_widget_subtitle);
            mValue = itemView.findViewById(R.id.projection_widget_current_value);
            mCapsule = itemView.findViewById(R.id.projection_widget_oval);
            mEndDivider = itemView.findViewById(R.id.projection_widget_end_divider);
            mProjectionView = itemView.findViewById(R.id.projection_widget_projectionView);
            mRangeView = itemView.findViewById(R.id.projection_widget_range_view);
            mProjectionViewStart = itemView.findViewById(R.id.projection_widget_projectionView_start);
            mProjectionViewEnd = itemView.findViewById(R.id.projection_widget_projectionView_end);
            mCurrentValueInChart = itemView.findViewById(R.id.projection_widget_current_value_in_chart);
            mGrayValueInChart = itemView.findViewById(R.id.projection_widget_gray_value_in_chart);
            mGrayValueInEndChart = itemView.findViewById(R.id.projection_widget_gray_value_in_end_chart);
            mBluePlus = itemView.findViewById(R.id.projection_widget_blue_plus);
            mMin = itemView.findViewById(R.id.projection_widget_min);
            mMax = itemView.findViewById(R.id.projection_widget_max);
        }
    }

    private class TimeViewHolder extends ViewHolder {

        private RelativeLayout mParentLayout;
        private View mDivider;
        private LineChartTimeSmall mChart;
        private AutofitTextView mTitle;
        private AutofitTextView mSubtitle;
        private TextView mValue;

        TimeViewHolder(View itemView) {
            super(itemView);

            mParentLayout = itemView.findViewById(R.id.widget_parent_layout);
            mDivider = itemView.findViewById(R.id.divider);
            mTitle = itemView.findViewById(R.id.time_widget_title);
            mSubtitle = itemView.findViewById(R.id.time_widget_subtitle);
            mValue = itemView.findViewById(R.id.time_widget_current_value);
            mChart = itemView.findViewById(R.id.lineChart_time);
        }
    }

    private class ImageViewHolder extends ViewHolder {

        private ImageView mImageLayout;

        public ImageViewHolder(View itemView) {
            super(itemView);

            mImageLayout = itemView.findViewById(R.id.image_widget_parent_layout);

        }
    }

    private class CounterViewHolder extends ViewHolder {

        private RelativeLayout mParentLayout;
        private View mDivider;
        private AutofitTextView mTitle;
        private AutofitTextView mSubtitle;
        private ImageView mAdd;
        private TextView mValue1;
        private TextView mValue10;
        private TextView mValue100;
        private TextView mValue1000;
        private TextView mValue10000;

        CounterViewHolder(View itemView) {
            super(itemView);

            mParentLayout = itemView.findViewById(R.id.counter_parent_layout);
            mDivider = itemView.findViewById(R.id.divider);
            mTitle = itemView.findViewById(R.id.counter_widget_title);
            mValue1 = itemView.findViewById(R.id.counter_widget_value_tv1);
            mValue10 = itemView.findViewById(R.id.counter_widget_value_tv10);
            mValue100 = itemView.findViewById(R.id.counter_widget_value_tv100);
            mValue1000 = itemView.findViewById(R.id.counter_widget_value_tv1000);
            mValue10000 = itemView.findViewById(R.id.counter_widget_value_tv10000);
            mSubtitle = itemView.findViewById(R.id.counter_widget_subtitle);
            mAdd = itemView.findViewById(R.id.counter_widget_add_iv);

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case NUMERIC: {
                return new NumericViewHolder(inflater.inflate(R.layout.numeric_widget_cardview, parent, false));
            }
            case RANGE: {
                return new RangeViewHolder(inflater.inflate(R.layout.range_widget_cardview, parent, false));
            }
            case PROJECTION: {
                return new ProjectionViewHolder(inflater.inflate(R.layout.projection_widget_cardview, parent, false));
            }
            case TIME: {
                return new TimeViewHolder(inflater.inflate(R.layout.time_widget_cardview, parent, false));
            }
            case COUNTER: {
                return new CounterViewHolder(inflater.inflate(R.layout.counter_widget_cardview, parent, false));
            }
            case IMAGE: {
                return new ImageViewHolder(inflater.inflate(R.layout.image_widget_cardview, parent, false));
            }
        }
        return new NumericViewHolder(inflater.inflate(R.layout.numeric_widget_cardview, parent, false));
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        try {
            int type = getItemViewType(position);
            final Widget widget = mWidgets.get(position);
            switch (type) {

                case IMAGE:
                    final ImageViewHolder imageViewHolder = (ImageViewHolder) holder;
                    ImageLoader.getInstance().displayImage(mWidgets.get(position).getCurrentValue(), imageViewHolder.mImageLayout);

                    break;
                case COUNTER:
                    final CounterViewHolder counterViewHolder = (CounterViewHolder) holder;

                    counterViewHolder.mDivider.post(new Runnable() {
                        @Override
                        public void run() {
                            ViewGroup.MarginLayoutParams mItemViewParams4;
                            mItemViewParams4 = (ViewGroup.MarginLayoutParams) counterViewHolder.mDivider.getLayoutParams();
                            mItemViewParams4.setMargins(0, (int) (counterViewHolder.mParentLayout.getHeight() * 0.4), 0, 0);
                            counterViewHolder.mDivider.requestLayout();
                        }
                    });


                    setSizes(counterViewHolder.mParentLayout);
                    String nameByLang4 = OperatorApplication.isEnglishLang() ? widget.getFieldEName() : widget.getFieldLName();
                    counterViewHolder.mTitle.setText(nameByLang4);

                    final int[] value = {0};
                    try {
                        value[0] = Integer.parseInt(widget.getCurrentValue());
                    } catch (NumberFormatException e) {
                    }

                    counterViewHolder.mValue1.setText(value[0] % 10 + "");
                    counterViewHolder.mValue10.setText((value[0] / 10) % 10 + "");
                    counterViewHolder.mValue100.setText((value[0] / 100) % 10 + "");
                    counterViewHolder.mValue1000.setText((value[0] / 1000) % 10 + "");
                    counterViewHolder.mValue10000.setText((value[0] / 10000) % 10 + "");

                    if (value[0] > 99999) {
                        counterViewHolder.mSubtitle.setVisibility(View.VISIBLE);
                        counterViewHolder.mSubtitle.setText(mContext.getResources().getString(R.string.total_amount) + " " + value[0]);
                    } else {
                        counterViewHolder.mSubtitle.setVisibility(View.INVISIBLE);
                    }

                    counterViewHolder.mAdd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mDashboardCentralContainerListener.onCounterPressedInCentralDashboardContainer(value[0]);
                        }
                    });

                    // TODO: 16/12/2018 complete logic

                    break;
                case NUMERIC:
                    final NumericViewHolder numericViewHolder = (NumericViewHolder) holder;
                    if (widget.getTargetScreen().equals(REPORT_REJECT_TAG)) {
                        setupNumericRejectItem(widget, numericViewHolder);
                    } else if (widget.getTargetScreen().equals(REPORT_REJECT_TAG)) {
                        setupNumericCycleItem(widget, numericViewHolder);
                    } else {
                        initNumericDiplayLy(widget, numericViewHolder);
                    }
                    break;
                case TIME:

                    final TimeViewHolder timeViewHolder = (TimeViewHolder) holder;

                    timeViewHolder.mDivider.post(new Runnable() {
                        @Override
                        public void run() {
                            ViewGroup.MarginLayoutParams mItemViewParams2;
                            mItemViewParams2 = (ViewGroup.MarginLayoutParams) timeViewHolder.mDivider.getLayoutParams();
                            mItemViewParams2.setMargins(0, (int) (timeViewHolder.mParentLayout.getHeight() * 0.4), 0, 0);
                            timeViewHolder.mDivider.requestLayout();
                        }
                    });


                    setSizes(timeViewHolder.mParentLayout);
                    String nameByLang2 = OperatorApplication.isEnglishLang() ? widget.getFieldEName() : widget.getFieldLName();
                    timeViewHolder.mTitle.setText(nameByLang2);
                    timeViewHolder.mSubtitle.setText(new StringBuilder(mContext.getString(R.string.standard)).append(widget.getStandardValue()));
                    timeViewHolder.mValue.setText(widget.getCurrentValue());
                    try {
                        if (Float.valueOf(widget.getCurrentValue()) < widget.getLowLimit() || Float.valueOf(widget.getCurrentValue()) > widget.getHighLimit()) {
                            timeViewHolder.mValue.setTextColor(mContext.getResources().getColor(R.color.red_line));
                        } else {
                            timeViewHolder.mValue.setTextColor(mContext.getResources().getColor(R.color.C16));
                        }
                    } catch (NumberFormatException e) {
                        timeViewHolder.mValue.setTextColor(mContext.getResources().getColor(R.color.C16));
                    }
                    int xValuesIncreaseIndex = 0;
                    ArrayList<Entry> tenHoursValues = new ArrayList<>();
                    ArrayList<Entry> fourHoursValues = new ArrayList<>();
                    ArrayList<ArrayList<Entry>> fourHoursList = new ArrayList<>();
                    final ArrayList<ArrayList<Entry>> tenHoursList = new ArrayList<>();
                    float midnightLimit = 0;
                    if (widget.getMachineParamHistoricData() != null && widget.getMachineParamHistoricData().size() > 0) {
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
                        timeViewHolder.mChart.setData(fourHoursList, xValues, widget.getLowLimit(), widget.getHighLimit());
                        timeViewHolder.mChart.setLimitLines(widget.getLowLimit(), widget.getHighLimit(), widget.getStandardValue(), midnightLimit);
                        final float midnightFinal = midnightLimit;
                        timeViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (tenHoursList.size() > 0) {
                                    String nameByLang = OperatorApplication.isEnglishLang() ? widget.getFieldEName() : widget.getFieldLName();
                                    mGoToScreenListener.goToFragment(ChartFragment.newInstance(tenHoursList, widget.getLowLimit(), widget.getStandardValue(), widget.getHighLimit(), xValues, nameByLang, midnightFinal), true, false);
                                }
                            }
                        });
                    }

                    break;

                case RANGE:
                    final RangeViewHolder rangeViewHolder = (RangeViewHolder) holder;

                    rangeViewHolder.mDivider.post(new Runnable() {
                        @Override
                        public void run() {
                            ViewGroup.MarginLayoutParams mItemViewParams3;
                            mItemViewParams3 = (ViewGroup.MarginLayoutParams) rangeViewHolder.mDivider.getLayoutParams();
                            mItemViewParams3.setMargins(0, (int) (rangeViewHolder.mParentLayout.getHeight() * 0.4), 0, 0);
                            rangeViewHolder.mDivider.requestLayout();
                        }
                    });

                    setSizes(rangeViewHolder.mParentLayout);
                    String nameByLang3 = OperatorApplication.isEnglishLang() ? widget.getFieldEName() : widget.getFieldLName();
                    rangeViewHolder.mTitle.setText(nameByLang3);
                    rangeViewHolder.mSubtitle.setText(new StringBuilder(mContext.getString(R.string.standard)).append(widget.getStandardValue()));
                    rangeViewHolder.mValue.setText(widget.getCurrentValue());
                    rangeViewHolder.mCurrentValue.setText(widget.getCurrentValue());
                    if (widget.isOutOfRange()) {
                        rangeViewHolder.mValue.setTextColor(ContextCompat.getColor(mContext, R.color.red_line));
                    } else {
                        rangeViewHolder.mValue.setTextColor(ContextCompat.getColor(mContext, R.color.C16));
                    }

                    rangeViewHolder.mRangeViewRed.setCurrentLine(true);
                    rangeViewHolder.mRangeViewBlue.setCurrentLine(false);
//                    if (mRangeCapsuleWidth == 0) {
                    rangeViewHolder.mCapsule.post(new Runnable() {
                        @Override
                        public void run() {
                            mRangeCapsuleWidth = rangeViewHolder.mCapsule.getWidth();
                            setRangeData(widget, rangeViewHolder);
                        }
                    });
//                    } else {
//                        mRangeCapsuleWidth = rangeViewHolder.mCapsule.getWidth();
//                        setRangeData(widget, rangeViewHolder);
//                    }
                    rangeViewHolder.mMin.setText(String.valueOf(widget.getLowLimit()));
                    rangeViewHolder.mStandard.setText(String.valueOf(widget.getStandardValue()));
                    rangeViewHolder.mMax.setText(String.valueOf(widget.getHighLimit()));
                    break;

                case PROJECTION:
                    final ProjectionViewHolder projectionViewHolder = (ProjectionViewHolder) holder;

                    projectionViewHolder.mDivider.post(new Runnable() {
                        @Override
                        public void run() {
                            ViewGroup.MarginLayoutParams mItemViewParams4;
                            mItemViewParams4 = (ViewGroup.MarginLayoutParams) projectionViewHolder.mDivider.getLayoutParams();
                            mItemViewParams4.setMargins(0, (int) (projectionViewHolder.mParentLayout.getHeight() * 0.4), 0, 0);
                            projectionViewHolder.mDivider.requestLayout();
                        }
                    });
                    projectionViewHolder.mRangeView.init(mContext);
                    projectionViewHolder.mProjectionView.init(mContext);
                    projectionViewHolder.mProjectionViewEnd.init(mContext);
                    setSizes(projectionViewHolder.mParentLayout);
                    projectionViewHolder.mRangeView.setCurrentLine(false);
                    float currentFloat = tryParse(widget.getCurrentValue(), StringParse.FLOAT);
                    projectionViewHolder.mCurrentValueInChart.setText(valueInK(currentFloat));
                    if (currentFloat >= widget.getTarget()) {
                        projectionViewHolder.mBluePlus.setVisibility(View.VISIBLE);
                        projectionViewHolder.mProjectionViewEnd.setCurrentView(true);
                        projectionViewHolder.mProjectionViewStart.setCurrentView(true);
//                    projectionViewHolder.mCurrentValueInChart.setText("");
                        projectionViewHolder.mRangeView.hideView();
                    } else if (widget.getProjection() >= widget.getTarget()) {
                        projectionViewHolder.mBluePlus.setVisibility(View.GONE);
                        projectionViewHolder.mProjectionViewEnd.setCurrentView(false);
                        projectionViewHolder.mProjectionViewStart.setCurrentView(true);
                        if (isNotNearestTexts(widget)) {
                            projectionViewHolder.mGrayValueInEndChart.setText(valueInK(widget.getProjection()));
                        } else {
                            projectionViewHolder.mGrayValueInEndChart.setText("");
                        }
                    } else {
                        projectionViewHolder.mBluePlus.setVisibility(View.GONE);
                        projectionViewHolder.mProjectionViewEnd.setCurrentView(false);
                        projectionViewHolder.mProjectionViewStart.setCurrentView(true);
                        projectionViewHolder.mProjectionViewEnd.hideView();
                        if (isNotNearestTexts(widget)) {
                            projectionViewHolder.mGrayValueInChart.setText(valueInK(widget.getProjection()));
                        } else {
                            projectionViewHolder.mGrayValueInChart.setText("");
                        }
                    }
                    if (currentFloat <= widget.getLowLimit() && currentFloat != widget.getTarget()) {
                        projectionViewHolder.mProjectionViewEnd.setCurrentView(false);
                        projectionViewHolder.mRangeView.hideView();
                        projectionViewHolder.mProjectionView.hideViews();
                        projectionViewHolder.mProjectionViewStart.setCurrentView(false);
                        projectionViewHolder.mGrayValueInEndChart.setText("");
                        projectionViewHolder.mGrayValueInChart.setText("");
                        projectionViewHolder.mCurrentValueInChart.setText("");
                    }
                    final float finalCurrentFloat = currentFloat;
//                    if (mProjectionCapsuleWidth == 0) {
                    projectionViewHolder.mCapsule.post(new Runnable() {
                        @Override
                        public void run() {
                            mProjectionCapsuleWidth = projectionViewHolder.mRangeView.getWidth();
                            setProjectionData(projectionViewHolder, widget, finalCurrentFloat);
                        }
                    });
//                    } else {
//                        mProjectionCapsuleWidth = projectionViewHolder.mRangeView.getWidth();
//                        setProjectionData(projectionViewHolder, widget, finalCurrentFloat);
//                    }
                    projectionViewHolder.mMin.setText(valueInK(widget.getLowLimit()));
                    //                projectionViewHolder.mStandard.setText(valueInK((int) widget.getStandardValue()));
                    projectionViewHolder.mMax.setText(valueInK(widget.getTarget()));

                    if (widget.getTarget() == 0 && currentFloat > 0) {
                        projectionViewHolder.mCurrentValueInChart.setText("");
                    }
                    break;

            }
            //        final View itemview= holder.itemView;
            //        Log.clearPollingRequest("moo", "onDraw: " + itemview.getWidth() + " " + itemview.getHeight());
            //        holder.itemView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            //            @Override
            //            public boolean onPreDraw() {
            //                Log.clearPollingRequest("moo", "onDraw: inner: " + itemview.getWidth() + " " + itemview.getHeight());
            //                return true;
            //            }
            //        });


        } catch (Exception ignored) {

        }
    }

    private void setupNumericCycleItem(Widget widget, NumericViewHolder numericViewHolder) {
        if (widget.getEditStep() == 0) {
            initNumericDiplayLy(widget, numericViewHolder);
        } else if (widget.getEditStep() == 1 && widget.getTargetScreen().equals(REPORT_UNIT_CYCLE_TAG)) {
            initEditCycleLy(widget, numericViewHolder);
        }
    }

    public void setupNumericRejectItem(Widget widget, NumericViewHolder numericViewHolder) {
        if (widget.getEditStep() == 0) {
            initNumericDiplayLy(widget, numericViewHolder);
        } else if (widget.getEditStep() == 1 && widget.getTargetScreen().equals(REPORT_REJECT_TAG)) {
            initEditNumericStep1(widget, numericViewHolder);
        } else if (widget.getEditStep() == 2 && widget.getTargetScreen().equals(REPORT_REJECT_TAG)) {
            initEditNumericStep2(widget, numericViewHolder);
        }
    }

    private void initEditCycleLy(final Widget widget, final NumericViewHolder numericViewHolder) {
        numericViewHolder.mDisplayLy.setVisibility(View.GONE);
        numericViewHolder.mEditStep1Ly.setVisibility(View.GONE);
        numericViewHolder.mEditStep2Ly.setVisibility(View.GONE);
        numericViewHolder.mEditCycleLy.setVisibility(View.VISIBLE);
        numericViewHolder.mEditCycleEt.requestFocus();
        KeyboardUtils.showKeyboard(mContext);

        numericViewHolder.mEditCycleCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                widget.setEditStep(0);
                setupNumericCycleItem(widget, numericViewHolder);
                numericViewHolder.mEditCycleEt.clearFocus();
                mDashboardCentralContainerListener.onScrollToPosition(numericViewHolder.getAdapterPosition());
            }
        });
        numericViewHolder.mEditCycleReportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                widget.setEditStep(0);
                mDashboardCentralContainerListener.onReportCycleUnit(
                        numericViewHolder.mEditCycleEt.getText().toString());
                setupNumericCycleItem(widget, numericViewHolder);
                numericViewHolder.mEditCycleEt.clearFocus();
                mDashboardCentralContainerListener.onScrollToPosition(numericViewHolder.getAdapterPosition());

            }
        });
    }

    private void initEditNumericStep2(final Widget widget, final NumericViewHolder numericViewHolder) {
        numericViewHolder.mDisplayLy.setVisibility(View.GONE);
        numericViewHolder.mEditStep1Ly.setVisibility(View.GONE);
        numericViewHolder.mEditCycleLy.setVisibility(View.GONE);
        numericViewHolder.mEditStep2Ly.setVisibility(View.VISIBLE);

        if (!PersistenceManager.getInstance().getDisplayRejectFactor()) {
            numericViewHolder.mSpinner1BtnRv.setVisibility(View.GONE);
        }
        setNumericReportRejctStep2Spinners(widget, numericViewHolder);

        numericViewHolder.mStep2CancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                widget.setEditStep(1);
                setupNumericRejectItem(widget, numericViewHolder);
                mDashboardCentralContainerListener.onScrollToPosition(numericViewHolder.getAdapterPosition());
            }
        });
        numericViewHolder.mStep2ReportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                widget.setEditStep(0);
                mDashboardCentralContainerListener.onReportReject(
                        numericViewHolder.mEditNumberEt.getText().toString(),
                        numericViewHolder.mUnitRadioBtn.isChecked(),
                        mSelectedCauseId,
                        mSelectedReasonId);
                setupNumericRejectItem(widget, numericViewHolder);
                mDashboardCentralContainerListener.onScrollToPosition(numericViewHolder.getAdapterPosition());

            }
        });
    }

    private void setNumericReportRejctStep2Spinners(Widget widget, NumericViewHolder numericViewHolder) {
        if (mReportFieldsForMachine != null) {

            final RejectReasonSpinnerAdapter reasonSpinnerArrayAdapter = new RejectReasonSpinnerAdapter(mContext, R.layout.base_spinner_item, mReportFieldsForMachine.getRejectReasons());
            reasonSpinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            numericViewHolder.mSpinner1.setAdapter(reasonSpinnerArrayAdapter);
            numericViewHolder.mSpinner1.getBackground().setColorFilter(ContextCompat.getColor(mContext, R.color.T12_color), PorterDuff.Mode.SRC_ATOP);

            numericViewHolder.mSpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    mSelectedReasonId = mReportFieldsForMachine.getRejectReasons().get(position).getId();
                    reasonSpinnerArrayAdapter.setTitle(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            if (PersistenceManager.getInstance().getDisplayRejectFactor()) {
                final RejectCauseSpinnerAdapter causeSpinnerArrayAdapter = new RejectCauseSpinnerAdapter(mContext, R.layout.base_spinner_item, mReportFieldsForMachine.getRejectCauses());
                causeSpinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                numericViewHolder.mSpinner2.setAdapter(causeSpinnerArrayAdapter);
                numericViewHolder.mSpinner2.getBackground().setColorFilter(ContextCompat.getColor(mContext, R.color.T12_color), PorterDuff.Mode.SRC_ATOP);

                numericViewHolder.mSpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        if (mReportFieldsForMachine.getRejectCauses().size() > 0) {
                            mSelectedCauseId = mReportFieldsForMachine.getRejectCauses().get(position).getId();
                        }
                        causeSpinnerArrayAdapter.setTitle(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            } else {
                mSelectedCauseId = 0;
            }
        }
    }

    public void setReportFieldsForMachine(ReportFieldsForMachine reportFieldsForMachine) {
        mReportFieldsForMachine = reportFieldsForMachine;
    }

    private void initEditNumericStep1(final Widget widget,
                                      final NumericViewHolder numericViewHolder) {

        numericViewHolder.mDisplayLy.setVisibility(View.GONE);
        numericViewHolder.mEditStep1Ly.setVisibility(View.VISIBLE);
        numericViewHolder.mEditStep2Ly.setVisibility(View.GONE);
        numericViewHolder.mEditCycleLy.setVisibility(View.GONE);
        numericViewHolder.mEditNumberEt.requestFocus();
        KeyboardUtils.showKeyboard(mContext);

        numericViewHolder.mStep1CancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                widget.setEditStep(0);
                setupNumericRejectItem(widget, numericViewHolder);
                numericViewHolder.mEditNumberEt.clearFocus();
                mDashboardCentralContainerListener.onScrollToPosition(numericViewHolder.getAdapterPosition());
            }
        });
        numericViewHolder.mStep1NextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                widget.setEditStep(2);
                setupNumericRejectItem(widget, numericViewHolder);
                numericViewHolder.mEditNumberEt.clearFocus();
                mDashboardCentralContainerListener.onScrollToPosition(numericViewHolder.getAdapterPosition());
            }
        });
    }

    public void initNumericDiplayLy(final Widget widget,
                                    final NumericViewHolder numericViewHolder) {
        numericViewHolder.mDisplayLy.setVisibility(View.VISIBLE);
        numericViewHolder.mEditStep1Ly.setVisibility(View.GONE);
        numericViewHolder.mEditStep2Ly.setVisibility(View.GONE);
        numericViewHolder.mEditCycleLy.setVisibility(View.GONE);
        numericViewHolder.mEditNumberEt.setText("");
        numericViewHolder.mEditCycleEt.setText("");
        numericViewHolder.mDivider.post(new Runnable() {
            @Override
            public void run() {
                ViewGroup.MarginLayoutParams mItemViewParams1;
                mItemViewParams1 = (ViewGroup.MarginLayoutParams) numericViewHolder.mDivider.getLayoutParams();
                mItemViewParams1.setMargins(0, (int) (numericViewHolder.mParentLayout.getHeight() * 0.4), 0, 0);
                numericViewHolder.mDivider.requestLayout();
            }
        });

        setSizes(numericViewHolder.mParentLayout);
        String nameByLang1 = OperatorApplication.isEnglishLang() ? widget.getFieldEName() : widget.getFieldLName();
        numericViewHolder.mTitle.setText(nameByLang1);
        numericViewHolder.mSubtitle.setVisibility(View.INVISIBLE);
        numericViewHolder.mValue.setText(widget.getCurrentValue());
        numericViewHolder.mValue.setSelected(true);

        numericViewHolder.mChangeMaterial.setVisibility(View.INVISIBLE);

        if (widget.getTargetScreen() != null && widget.getTargetScreen().length() > 0) {

            numericViewHolder.mEditIc.setVisibility(View.VISIBLE);

            numericViewHolder.mEditIc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                    if (widget.getTargetScreen().equals(REPORT_REJECT_TAG)) {
//                        widget.setEditStep(1);
//                        setupNumericRejectItem(widget, numericViewHolder);
//                        mDashboardCentralContainerListener.onScrollToPosition(numericViewHolder.getAdapterPosition());
//                    } else if (widget.getTargetScreen().equals(REPORT_UNIT_CYCLE_TAG)) {
//                        widget.setEditStep(1);
//                        setupNumericCycleItem(widget, numericViewHolder);
//                        mDashboardCentralContainerListener.onScrollToPosition(numericViewHolder.getAdapterPosition());
//                    } else {
                    mDashboardCentralContainerListener.onOpenNewFragmentInCentralDashboardContainer(widget.getTargetScreen());
//                    }
                }
            });

        } else {

            numericViewHolder.mEditIc.setVisibility(View.GONE);
        }
    }

    private boolean isNotNearestTexts(Widget widget) {
        float size = widget.getStandardValue() - widget.getLowLimit();
        try {
            return ((widget.getProjection() - Float.valueOf(widget.getCurrentValue())) / size > 0.15);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void setSizes(final RelativeLayout parent) {
        ViewGroup.LayoutParams layoutParams;
        layoutParams = parent.getLayoutParams();
        layoutParams.height = (int) (mHeight * 0.45);
        layoutParams.width = (int) (mWidth * 0.325);
        parent.requestLayout();

    }

    private void setProjectionData(ProjectionViewHolder projectionViewHolder, Widget widget,
                                   float finalCurrentFloat) {
        String nameByLang4 = OperatorApplication.isEnglishLang() ? widget.getFieldEName() : widget.getFieldLName();
        projectionViewHolder.mTitle.setText(nameByLang4);
        projectionViewHolder.mSubtitle.setText(new StringBuilder(mContext.getString(R.string.total_required)).append(valueInK(widget.getTarget())));
        projectionViewHolder.mValue.setText(valueInK(finalCurrentFloat));
        if (widget.getTarget() >= widget.getLowLimit()) {
            float scaleValue = (widget.getTarget() - widget.getLowLimit());
            float currentValue = finalCurrentFloat - widget.getLowLimit();
            float projectionValue = (widget.getProjection() - widget.getLowLimit());
            float convertCurrentValue = 1;
            float convertProjectionValue = 1;
            if (scaleValue != 0) {
                convertCurrentValue = currentValue / scaleValue;
                convertProjectionValue = projectionValue / scaleValue;
            }
            float currentWidth = mProjectionCapsuleWidth * convertCurrentValue;
            float projectionWidth = projectionViewHolder.mProjectionView.getWidth() * convertProjectionValue;
            if (currentWidth > 1000) {
                currentWidth = 1000;
            }
            if (projectionWidth > 1000) {
                projectionWidth = 1000;
            }
            if (convertCurrentValue > 0.9) {
                projectionViewHolder.mRangeView.updateX(currentWidth - 8/* half of the line*/);
                projectionViewHolder.mCurrentValueInChart.setX(currentWidth - 20/* half of the line*/);
                projectionViewHolder.mGrayValueInChart.setX(mProjectionCapsuleWidth * convertProjectionValue - 8/* half of the line*/);
            } else {
                projectionViewHolder.mRangeView.updateX(currentWidth/* half of the line*/);
                projectionViewHolder.mCurrentValueInChart.setX(currentWidth - 2/* half of the line*/);
                projectionViewHolder.mGrayValueInChart.setX(mProjectionCapsuleWidth * convertProjectionValue - 2/* half of the line*/);
            }
            projectionViewHolder.mProjectionView.updateWidth(currentWidth, projectionWidth);
        }
        if (projectionViewHolder.mEndDivider.getX() - projectionViewHolder.mRangeView.getX() < 150 && projectionViewHolder.mBluePlus.getVisibility() != View.VISIBLE && finalCurrentFloat != 0) {
            projectionViewHolder.mEndDivider.setVisibility(View.INVISIBLE);
        } else {
            projectionViewHolder.mEndDivider.setVisibility(View.VISIBLE);
        }
    }

    private Drawable createRangeShape(String color) {
        Drawable drawable = new GradientDrawable();
        ((GradientDrawable) drawable).setShape(GradientDrawable.RECTANGLE);
        ((GradientDrawable) drawable).setCornerRadii(new float[]{0, 0, 0, 0, 60, 60, 60, 60});
        ((GradientDrawable) drawable).setSize(8, 48);
        ((GradientDrawable) drawable).setColor(Color.parseColor(color));
        return drawable;
    }

    private Drawable createStartProjectionShape(String color) {
        Configuration config = mContext.getResources().getConfiguration();
        if (config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
            return createEndProjectionShapeDrawable(color);
        }
        return createStartProjectionShapeDrawable(color);
    }

    @NonNull
    private Drawable createStartProjectionShapeDrawable(String color) {
        Drawable drawable = new GradientDrawable();
        ((GradientDrawable) drawable).setShape(GradientDrawable.RECTANGLE);
        ((GradientDrawable) drawable).setCornerRadii(new float[]{60, 60, 0, 0, 0, 0, 60, 60});
        ((GradientDrawable) drawable).setSize(30, 43);
        ((GradientDrawable) drawable).setColor(Color.parseColor(color));
        return drawable;
    }

    private Drawable createEndProjectionShape(String color) {
        Configuration config = mContext.getResources().getConfiguration();
        if (config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
            return createStartProjectionShapeDrawable(color);
        }
        return createEndProjectionShapeDrawable(color);
    }

    @NonNull
    private Drawable createEndProjectionShapeDrawable(String color) {
        Drawable drawable = new GradientDrawable();
        ((GradientDrawable) drawable).setShape(GradientDrawable.RECTANGLE);
        ((GradientDrawable) drawable).setCornerRadii(new float[]{0, 0, 60, 60, 60, 60, 0, 0});
        ((GradientDrawable) drawable).setSize(30, 43);
        ((GradientDrawable) drawable).setColor(Color.parseColor(color));
        return drawable;
    }

    private Drawable createProjectionShape(String color) {
        Drawable drawable = new GradientDrawable();
        ((GradientDrawable) drawable).setShape(GradientDrawable.RECTANGLE);
        ((GradientDrawable) drawable).setSize(30, 43);
        ((GradientDrawable) drawable).setColor(Color.parseColor(color));
        return drawable;
    }

    private Drawable createCapsuleDrawable(String color) {
        Drawable drawable = new GradientDrawable();
        ((GradientDrawable) drawable).setShape(GradientDrawable.RECTANGLE);
        ((GradientDrawable) drawable).setCornerRadii(new float[]{60, 60, 60, 60, 60, 60, 60, 60});
        ((GradientDrawable) drawable).setSize(270, 60);
        ((GradientDrawable) drawable).setColor(Color.parseColor(color));
        return drawable;
    }

    private void setRangeData(Widget widget, RangeViewHolder rangeViewHolder) {
        float currentValue = tryParse(widget.getCurrentValue(), StringParse.FLOAT);
        if (widget.isOutOfRange() && currentValue > widget.getHighLimit()) {
            rangeViewHolder.mRangeViewBlue.setVisibility(View.INVISIBLE);
            rangeViewHolder.mCurrentValue.setVisibility(View.INVISIBLE);
            rangeViewHolder.mRangeViewRed.setVisibility(View.VISIBLE);
            rangeViewHolder.mRedMark.setVisibility(View.VISIBLE);
            if (mClosedState) {
                rangeViewHolder.mRangeViewRed.updateX((float) (mRangeCapsuleWidth * 0.89)/*max location*/);
            }  //todo

            rangeViewHolder.mRedMark.setX(rangeViewHolder.mRangeViewRed.getX());
        } else if (widget.isOutOfRange() && currentValue < widget.getLowLimit()) {
            rangeViewHolder.mRangeViewBlue.setVisibility(View.INVISIBLE);
            rangeViewHolder.mCurrentValue.setVisibility(View.INVISIBLE);
            rangeViewHolder.mRangeViewRed.setVisibility(View.VISIBLE);
            rangeViewHolder.mRedMark.setVisibility(View.VISIBLE);
            if (mClosedState) {
                rangeViewHolder.mRangeViewRed.updateX((float) (mRangeCapsuleWidth * 0.001)/*min location*/);
            }  //todo

            rangeViewHolder.mRedMark.setX(rangeViewHolder.mRangeViewRed.getX());
        } else {
            rangeViewHolder.mRangeViewRed.setVisibility(View.INVISIBLE);
            rangeViewHolder.mRedMark.setVisibility(View.INVISIBLE);
            rangeViewHolder.mRangeViewBlue.setVisibility(View.VISIBLE);
            rangeViewHolder.mCurrentValue.setVisibility(View.VISIBLE);
            if (widget.getHighLimit() > widget.getLowLimit()) {
                float scaleValue = (widget.getHighLimit() - widget.getLowLimit());
                float currentFloatValue = currentValue - widget.getLowLimit();
                final float convertCurrentValue = currentFloatValue / scaleValue;
                if (convertCurrentValue > 0.5) {
                    rangeViewHolder.mRangeViewBlue.updateX((rangeViewHolder.mRangeViewBlue.getWidth() * convertCurrentValue - 7)/* half of the line*/);
                    rangeViewHolder.mCurrentValue.setX(rangeViewHolder.mRangeViewBlue.getX() - 7);
                } else {
                    rangeViewHolder.mRangeViewBlue.updateX((rangeViewHolder.mRangeViewBlue.getWidth() * convertCurrentValue)/* half of the line*/);
                    rangeViewHolder.mCurrentValue.setX(rangeViewHolder.mRangeViewBlue.getX());
                }
            }
        }
    }

    @SuppressLint("DefaultLocale")
    private String valueInK(float value) {
        String valueString;
        if (value >= 1000) {
            float valueFloat = value / 1000;
            if (value % 100 == 0) {
                valueString = String.format("%.1f", valueFloat);
            } else {
                valueString = String.format("%.1f", valueFloat);
            }
            if (value % 1000 == 0) {
                valueString = String.valueOf(value / 1000);
            }
            return valueString + "k";
        } else {
            return String.valueOf((int) value);
        }
    }

    @Override
    public int getItemCount() {
        if (mWidgets != null) {
            return mWidgets.size();
        } else return 0;
    }

    @Override
    public int getItemViewType(int position) {
        int type;
        switch (mWidgets.get(position).getFieldType()) {
            case 0:
                type = NUMERIC;
                break;
            case 1:
                type = RANGE;
                break;
            case 2:
                type = PROJECTION;
                break;
            case 3:
                type = TIME;
                break;
            case 4:
                type = COUNTER;
                break;
            case 5:
                type = IMAGE;
                break;
            default:
                type = NUMERIC;
                break;
        }
        return type;
    }

    enum StringParse {
        INT, FLOAT
    }

    private float tryParse(String value, StringParse stringParse) {
        try {
            if (stringParse == StringParse.INT) {
                return Integer.parseInt(value);
            }
            if (stringParse == StringParse.FLOAT) {
                return Float.parseFloat(value);
            }
        } catch (NumberFormatException nfe) {
            // Log exception.
            return 0;
        }
        return 0;
    }
}
