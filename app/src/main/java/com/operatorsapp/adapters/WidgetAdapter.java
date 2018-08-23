package com.operatorsapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.data.Entry;
import com.operators.machinedatainfra.models.Widget;
import com.operatorsapp.R;
import com.operatorsapp.activities.interfaces.GoToScreenListener;
import com.operatorsapp.application.OperatorApplication;
import com.operatorsapp.fragments.ChartFragment;
import com.operatorsapp.interfaces.DashboardCentralContainerListener;
import com.operatorsapp.utils.TimeUtils;
import com.operatorsapp.view.LineChartTimeSmall;
import com.operatorsapp.view.ProjectionView;
import com.operatorsapp.view.ProjectionViewEnd;
import com.operatorsapp.view.ProjectionViewStart;
import com.operatorsapp.view.RangeView;

import java.util.ArrayList;
import java.util.List;

import me.grantland.widget.AutofitTextView;

import static android.support.v7.widget.RecyclerView.Adapter;
import static android.support.v7.widget.RecyclerView.ViewHolder;

public class WidgetAdapter extends Adapter {
    private static final long FOUR_HOURS = 60000L * 60 * 4;
    private static final long TEN_HOURS = 60000L * 60 * 10;
    private final DashboardCentralContainerListener mDashboardCentralContainerListener;
    private Context mContext;
    private List<Widget> mWidgets;
    private final int NUMERIC = 0;
    private final int RANGE = 1;
    private final int PROJECTION = 2;
    private final int TIME = 3;
    private GoToScreenListener mGoToScreenListener;
    private int mRangeCapsuleWidth = 0;
    private int mProjectionCapsuleWidth = 0;
    private boolean mClosedState;
    private int mHeight;
    private int mWidth;

    public WidgetAdapter(Context context, List<Widget> widgets, GoToScreenListener goToScreenListener, boolean closedState, int height, int width, DashboardCentralContainerListener dashboardCentralContainerListener) {
        mWidgets = widgets;
        mContext = context;
        mGoToScreenListener = goToScreenListener;
        mClosedState = closedState;
        mHeight = height;
        mWidth = width;
        mDashboardCentralContainerListener = dashboardCentralContainerListener;
    }

    public void changeState(boolean closedState) {
        mClosedState = closedState;
        notifyDataSetChanged();
    }

    public void setNewData(List<Widget> widgets) {
        if (mWidgets != null) {
            mWidgets.clear();
            mWidgets.addAll(widgets);
        } else {

            mWidgets = widgets;

        }
        notifyDataSetChanged();
    }

    private class NumericViewHolder extends ViewHolder {

        private final View mEditIc;
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
        }
        return new NumericViewHolder(inflater.inflate(R.layout.numeric_widget_cardview, parent, false));
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        int type = getItemViewType(position);
        final Widget widget = mWidgets.get(position);
        switch (type) {
            case NUMERIC:
                final NumericViewHolder numericViewHolder = (NumericViewHolder) holder;

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

                            mDashboardCentralContainerListener.onOpenNewFragmentInCentralDashboardContainer(widget.getTargetScreen());
                        }
                    });

                } else {

                    numericViewHolder.mEditIc.setVisibility(View.GONE);
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
                final ArrayList<Entry> tenHoursValues = new ArrayList<>();
                final ArrayList<Entry> fourHoursValues = new ArrayList<>();
                if (widget.getMachineParamHistoricData() != null && widget.getMachineParamHistoricData().size() > 0) {
                    final String[] xValues = new String[widget.getMachineParamHistoricData().size()];
                    for (int i = 0; i < widget.getMachineParamHistoricData().size(); i++) {
                        xValues[i] = TimeUtils.getDateForChart(widget.getMachineParamHistoricData().get(i).getTime());/*new SimpleDateFormat("HH:mm").format(new Date(widget.getMachineParamHistoricData().get(i).getTime()));*/
                        Entry entry = new Entry(i, widget.getMachineParamHistoricData().get(i).getValue()/*, new SimpleDateFormat("HH:mm").format(new Date(widget.getMachineParamHistoricData().get(i).getTime())*/);
                        if (TimeUtils.getLongFromDateString(widget.getMachineParamHistoricData().get(i).getTime(), "yyyy/MM/dd HH:mm:ss") > (TimeUtils.getLongFromDateString(widget.getMachineParamHistoricData().get(widget.getMachineParamHistoricData().size() - 1).getTime(), "yyyy/MM/dd HH:mm:ss") - TEN_HOURS)) {
                            tenHoursValues.add(entry);
                        }
                        if (TimeUtils.getLongFromDateString(widget.getMachineParamHistoricData().get(i).getTime(), "yyyy/MM/dd HH:mm:ss") > (TimeUtils.getLongFromDateString(widget.getMachineParamHistoricData().get(widget.getMachineParamHistoricData().size() - 1).getTime(), "yyyy/MM/dd HH:mm:ss") - FOUR_HOURS)) {
                            fourHoursValues.add(entry);
                        }
                    }
                    timeViewHolder.mChart.setData(fourHoursValues, xValues, widget.getLowLimit(), widget.getHighLimit());

                    timeViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (tenHoursValues.size() > 0) {
                                String nameByLang = OperatorApplication.isEnglishLang() ? widget.getFieldEName() : widget.getFieldLName();
                                mGoToScreenListener.goToFragment(ChartFragment.newInstance(tenHoursValues, widget.getLowLimit(), widget.getStandardValue(), widget.getHighLimit(), xValues, nameByLang), true, false);
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
                if (mRangeCapsuleWidth == 0) {
                    rangeViewHolder.mCapsule.post(new Runnable() {
                        @Override
                        public void run() {
                            mRangeCapsuleWidth = rangeViewHolder.mCapsule.getWidth();
                            setRangeData(widget, rangeViewHolder);
                        }
                    });
                } else {
                    mRangeCapsuleWidth = rangeViewHolder.mCapsule.getWidth();
                    setRangeData(widget, rangeViewHolder);
                }
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

                setSizes(projectionViewHolder.mParentLayout);
                projectionViewHolder.mRangeView.setCurrentLine(false);
                float currentFloat = tryParse(widget.getCurrentValue(), StringParse.FLOAT);
                projectionViewHolder.mCurrentValueInChart.setText(valueInK(currentFloat));
                if (currentFloat >= widget.getTarget()) {
                    projectionViewHolder.mBluePlus.setVisibility(View.VISIBLE);
                    projectionViewHolder.mProjectionViewEnd.setCurrentView(true);
                    projectionViewHolder.mCurrentValueInChart.setText("");
                    projectionViewHolder.mRangeView.hideView();
                } else if (widget.getProjection() >= widget.getTarget()) {
                    projectionViewHolder.mBluePlus.setVisibility(View.GONE);
                    projectionViewHolder.mProjectionViewEnd.setCurrentView(false);
                    projectionViewHolder.mGrayValueInEndChart.setText(valueInK(widget.getProjection()));
                } else {
                    projectionViewHolder.mBluePlus.setVisibility(View.GONE);
                    projectionViewHolder.mProjectionViewEnd.hideView();
                    projectionViewHolder.mGrayValueInChart.setText(valueInK(widget.getProjection()));
                }
                if (currentFloat <= widget.getLowLimit()) {
                    projectionViewHolder.mRangeView.hideView();
                    projectionViewHolder.mProjectionView.hideViews();
                    projectionViewHolder.mProjectionViewStart.hideView();
                    projectionViewHolder.mGrayValueInEndChart.setText("");
                    projectionViewHolder.mGrayValueInChart.setText("");
                    projectionViewHolder.mCurrentValueInChart.setText("");
                }
                final float finalCurrentFloat = currentFloat;
                if (mProjectionCapsuleWidth == 0) {
                    projectionViewHolder.mCapsule.post(new Runnable() {
                        @Override
                        public void run() {
                            mProjectionCapsuleWidth = projectionViewHolder.mRangeView.getWidth();
                            setProjectionData(projectionViewHolder, widget, finalCurrentFloat);
                        }
                    });
                } else {
                    mProjectionCapsuleWidth = projectionViewHolder.mRangeView.getWidth();
                    setProjectionData(projectionViewHolder, widget, finalCurrentFloat);
                }
                projectionViewHolder.mMin.setText(valueInK(widget.getLowLimit()));
                //                projectionViewHolder.mStandard.setText(valueInK((int) widget.getStandardValue()));
                projectionViewHolder.mMax.setText(valueInK(widget.getTarget()));
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


    }

    private void setSizes(final RelativeLayout parent) {
        ViewGroup.LayoutParams layoutParams;
        layoutParams = parent.getLayoutParams();
        layoutParams.height = (int) (mHeight * 0.45);
        layoutParams.width = (int) (mWidth * 0.325);
        parent.requestLayout();

    }

    private void setProjectionData(ProjectionViewHolder projectionViewHolder, Widget widget, float finalCurrentFloat) {
        String nameByLang4 = OperatorApplication.isEnglishLang() ? widget.getFieldEName() : widget.getFieldLName();
        projectionViewHolder.mTitle.setText(nameByLang4);
        projectionViewHolder.mSubtitle.setText(new StringBuilder(mContext.getString(R.string.total_required)).append(valueInK(widget.getTarget())));
        projectionViewHolder.mValue.setText(valueInK(finalCurrentFloat));
        if (widget.getTarget() > widget.getLowLimit()) {
            float scaleValue = (widget.getTarget() - widget.getLowLimit());
            float currentValue = finalCurrentFloat - widget.getLowLimit();
            float projectionValue = (widget.getProjection() - widget.getLowLimit());
            final float convertCurrentValue = currentValue / scaleValue;
            final float convertProjectionValue = projectionValue / scaleValue;
            projectionViewHolder.mRangeView.updateX(mProjectionCapsuleWidth * convertCurrentValue/* half of the line*/);
            projectionViewHolder.mProjectionView.updateWidth((mProjectionCapsuleWidth * convertCurrentValue), (projectionViewHolder.mProjectionView.getWidth() * convertProjectionValue));
            projectionViewHolder.mCurrentValueInChart.setX(mProjectionCapsuleWidth * convertCurrentValue - 2/* half of the line*/);
            projectionViewHolder.mGrayValueInChart.setX(mProjectionCapsuleWidth * convertProjectionValue - 2/* half of the line*/);
        }
        if (projectionViewHolder.mEndDivider.getX() - projectionViewHolder.mRangeView.getX() < 150 && projectionViewHolder.mBluePlus.getVisibility() != View.VISIBLE && finalCurrentFloat != 0) {
            projectionViewHolder.mEndDivider.setVisibility(View.GONE);
        } else {
            projectionViewHolder.mEndDivider.setVisibility(View.VISIBLE);
        }
    }

    private void setRangeData(Widget widget, RangeViewHolder rangeViewHolder) {
        int currentValue = (int) tryParse(widget.getCurrentValue(), StringParse.INT);
        if (widget.isOutOfRange() && currentValue > widget.getHighLimit()) {
            rangeViewHolder.mRangeViewBlue.setVisibility(View.GONE);
            rangeViewHolder.mCurrentValue.setVisibility(View.GONE);
            rangeViewHolder.mRangeViewRed.setVisibility(View.VISIBLE);
            rangeViewHolder.mRedMark.setVisibility(View.VISIBLE);
            if (mClosedState) {
                rangeViewHolder.mRangeViewRed.updateX((float) (mRangeCapsuleWidth * 0.84)/*max location*/);
            }  //todo

            rangeViewHolder.mRedMark.setX(rangeViewHolder.mRangeViewRed.getX());
        } else if (widget.isOutOfRange() && currentValue < widget.getLowLimit()) {
            rangeViewHolder.mRangeViewBlue.setVisibility(View.GONE);
            rangeViewHolder.mCurrentValue.setVisibility(View.GONE);
            rangeViewHolder.mRangeViewRed.setVisibility(View.VISIBLE);
            rangeViewHolder.mRedMark.setVisibility(View.VISIBLE);
            if (mClosedState) {
                rangeViewHolder.mRangeViewRed.updateX((float) (mRangeCapsuleWidth * 0.001)/*min location*/);
            }  //todo

            rangeViewHolder.mRedMark.setX(rangeViewHolder.mRangeViewRed.getX());
        } else {
            rangeViewHolder.mRangeViewRed.setVisibility(View.GONE);
            rangeViewHolder.mRedMark.setVisibility(View.GONE);
            rangeViewHolder.mRangeViewBlue.setVisibility(View.VISIBLE);
            rangeViewHolder.mCurrentValue.setVisibility(View.VISIBLE);
            if (widget.getHighLimit() > widget.getLowLimit()) {
                float scaleValue = (widget.getHighLimit() - widget.getLowLimit());
                float currentFloatValue = currentValue - widget.getLowLimit();
                final float convertCurrentValue = currentFloatValue / scaleValue;
                rangeViewHolder.mRangeViewBlue.updateX((rangeViewHolder.mRangeViewBlue.getWidth() * convertCurrentValue)/* half of the line*/);
                rangeViewHolder.mCurrentValue.setX(rangeViewHolder.mRangeViewBlue.getX());
            }
        }
    }

    @SuppressLint("DefaultLocale")
    private String valueInK(float value) {
        String valueString = String.valueOf(value);
        if (value >= 1000) {
            float valueFloat = value / 1000;
            if (value % 100 == 0) {
                valueString = String.format("%.1f", valueFloat);
            } else {
                valueString = String.format("%.2f", valueFloat);
            }
            if (value % 1000 == 0) {
                valueString = String.valueOf(value / 1000);
            }
            return valueString + "k";
        } else {
            return valueString;
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
