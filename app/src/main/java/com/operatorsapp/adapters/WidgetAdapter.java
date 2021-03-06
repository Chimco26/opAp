package com.operatorsapp.adapters;

import android.annotation.SuppressLint;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.common.permissions.WidgetInfo;
import com.github.mikephil.charting.jobs.MoveViewJob;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.operators.machinedatainfra.models.Widget;
import com.operators.machinestatusinfra.models.MachineStatus;
import com.operators.reportfieldsformachineinfra.ReportFieldsForMachine;
import com.operatorsapp.R;
import com.operatorsapp.activities.interfaces.GoToScreenListener;
import com.operatorsapp.activities.interfaces.ShowDashboardCroutonListener;
import com.operatorsapp.interfaces.DashboardCentralContainerListener;
import com.operatorsapp.interfaces.OnKeyboardManagerListener;
import com.operatorsapp.view.widgetViewHolders.ActivateJobViewHolder;
import com.operatorsapp.view.widgetViewHolders.CounterViewHolder;
import com.operatorsapp.view.widgetViewHolders.FreeTextViewHolder;
import com.operatorsapp.view.widgetViewHolders.GenericTimeViewHolder;
import com.operatorsapp.view.widgetViewHolders.NumericViewHolder;
import com.operatorsapp.view.widgetViewHolders.OrderTestViewHolder;
import com.operatorsapp.view.widgetViewHolders.ProjectionViewHolderNew;
import com.operatorsapp.view.widgetViewHolders.RangeViewHolder;
import com.operatorsapp.view.widgetViewHolders.ReportProductionViewHolder;
import com.operatorsapp.view.widgetViewHolders.ReportStopViewHolder;
import com.operatorsapp.view.widgetViewHolders.TimeLeftViewHolder;
import com.operatorsapp.view.widgetViewHolders.TimeViewHolder;
import com.operatorsapp.view.widgetViewHolders.TopStopEventsViewHolder;
import com.operatorsapp.view.widgetViewHolders.helper.MachineWorkBitViewHolder;

import java.util.ArrayList;
import java.util.List;

import static androidx.recyclerview.widget.RecyclerView.Adapter;
import static androidx.recyclerview.widget.RecyclerView.ViewHolder;
import static com.operators.machinedatainfra.models.Widget.FIELD_TYPE_ACTIVATE_JOB;

public class WidgetAdapter extends Adapter {
    private static final long TEN_HOURS = 60000L * 60 * 10;
    private final DashboardCentralContainerListener mDashboardCentralContainerListener;
    private final OnKeyboardManagerListener mOnKeyboardManagerListener;
    private List<Widget> mWidgets;
    private final int NUMERIC = 0;
    private final int RANGE = 1;
    private final int PROJECTION = 2;
    private final int TIME = 3;
    private final int COUNTER = 4;
    private final int IMAGE = 5;
    private final int TIME_LEFT = 6; // current value is the time left in minutes
    private static final int REPORT_PERCENT = 7;
    private static final int GENERIC_TIME = 8;
    private static final int FREE_TEXT = 9;
    private static final int ORDER_TEST = 10;
    private static final int REPORT_PRODUCTION = 11;
    private static final int MACHINE_WORK_BIT = 12;
    private static final int TOP_5_STOP_EVENTS = 13;
    private GoToScreenListener mGoToScreenListener;
    private int mRangeCapsuleWidth = 0;
    private int mProjectionCapsuleWidth = 0;
    private boolean mClosedState;
    private int mHeight;
    private int mWidth;
    private ReportFieldsForMachine mReportFieldsForMachine;
    private MachineStatus mMachineStatus;
    private int mSelectedReasonId;
    private int mSelectedCauseId;
    private boolean mEndSetupDisable;
    private SparseArray<WidgetInfo> mPermissionResponse;
    private Integer mJoshId;
    private ShowDashboardCroutonListener mShowDashboardCroutonListener;


    public WidgetAdapter(List<Widget> widgets, GoToScreenListener goToScreenListener,
                         Integer joshId, boolean closedState, int height, int width,
                         DashboardCentralContainerListener dashboardCentralContainerListener,
                         ReportFieldsForMachine reportFieldsForMachine, MachineStatus machineStatus,
                         ShowDashboardCroutonListener showDashboardCroutonListener, OnKeyboardManagerListener onKeyboardManagerListener, SparseArray<WidgetInfo> permissionResponse) {
        mWidgets = widgets;
        mGoToScreenListener = goToScreenListener;
        mJoshId = joshId;
        mClosedState = closedState;
        mHeight = height;
        mWidth = width;
        mDashboardCentralContainerListener = dashboardCentralContainerListener;
        mReportFieldsForMachine = reportFieldsForMachine;
        mMachineStatus = machineStatus;
        mOnKeyboardManagerListener = onKeyboardManagerListener;
        mPermissionResponse = permissionResponse;
        mShowDashboardCroutonListener = showDashboardCroutonListener;
    }

    public void changeState(boolean closedState) {
        mClosedState = closedState;
        notifyDataSetChanged();
    }

    public void setNewData(List<Widget> widgets, SparseArray<WidgetInfo> permissionResponse) {
        mPermissionResponse = permissionResponse;
        if (mWidgets != null) {
            ArrayList<Widget> toUpdate = getWidgetIneditMode();
            mWidgets.clear();
            mWidgets.addAll(widgets);
            updateNewWidgetWithEditMode(toUpdate);
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
                    break;
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

    private class ImageViewHolder extends ViewHolder {

        private ImageView mImageLayout;
        private RelativeLayout mParentLayout;

        public ImageViewHolder(View itemView) {
            super(itemView);

            mImageLayout = itemView.findViewById(R.id.image_widget_layout);
            mParentLayout = itemView.findViewById(R.id.image_widget_parent_layout);
            setSizes(mParentLayout);

        }
    }

    private void setSizes(final RelativeLayout parent) {
        ViewGroup.LayoutParams layoutParams;
        layoutParams = parent.getLayoutParams();
        layoutParams.height = (int) (mHeight * 0.5);
        layoutParams.width = (int) (mWidth * 0.325);
        parent.requestLayout();

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case NUMERIC: {

                return new NumericViewHolder(inflater.inflate(R.layout.numeric_widget_cardview, parent, false),
                        mDashboardCentralContainerListener, mOnKeyboardManagerListener, mReportFieldsForMachine, mHeight, mWidth,
                        WidgetInfo.getWidgetInfo(mPermissionResponse, WidgetInfo.PermissionId.CHANGE_UNITS_IN_CYCLE.getId()).getHaspermissionBoolean(),
                        WidgetInfo.getWidgetInfo(mPermissionResponse, WidgetInfo.PermissionId.ADD_REJECTS.getId()).getHaspermissionBoolean());
            }
            case RANGE: {
                return new RangeViewHolder(inflater.inflate(R.layout.range_widget_cardview, parent, false), mClosedState, mHeight, mWidth);
            }
            case PROJECTION: {
                return new ProjectionViewHolderNew(inflater.inflate(R.layout.projection_widget_cardview_new, parent, false), mHeight, mWidth);
            }
            case TIME: {
                return new TimeViewHolder(inflater.inflate(R.layout.time_widget_cardview, parent, false), mGoToScreenListener, mHeight, mWidth);
            }
            case COUNTER: {
                return new CounterViewHolder(inflater.inflate(R.layout.counter_widget_cardview, parent, false), mDashboardCentralContainerListener, mHeight, mWidth);
            }
            case IMAGE: {
                return new ImageViewHolder(inflater.inflate(R.layout.image_widget_cardview, parent, false));
            }
            case TIME_LEFT: {
                return new TimeLeftViewHolder(inflater.inflate(R.layout.time_left_widget_cardview, parent, false),
                        mDashboardCentralContainerListener, mMachineStatus, mEndSetupDisable, mHeight, mWidth,
                        WidgetInfo.getWidgetInfo(mPermissionResponse, WidgetInfo.PermissionId.END_SETUP.getId()).getHaspermissionBoolean(),
                        WidgetInfo.getWidgetInfo(mPermissionResponse, WidgetInfo.PermissionId.ACTIVATE_JOB.getId()).getHaspermissionBoolean());
            }
            case REPORT_PERCENT: {
                return new ReportStopViewHolder(inflater.inflate(R.layout.report_percent_widget_cardview, parent, false),
                        mDashboardCentralContainerListener, mHeight, mWidth);
            }
            case GENERIC_TIME: {
                return new GenericTimeViewHolder(inflater.inflate(R.layout.holder_generic_time, parent, false), mHeight, mWidth);
            }
            case FREE_TEXT: {
                return new FreeTextViewHolder(inflater.inflate(R.layout.holder_free_text, parent, false), mHeight, mWidth);
            }
            case ORDER_TEST: {
                return new OrderTestViewHolder(inflater.inflate(R.layout.order_test_widget_cardview, parent, false), mHeight, mWidth);
            }
            case FIELD_TYPE_ACTIVATE_JOB: {
                return new ActivateJobViewHolder(inflater.inflate(R.layout.activate_job_widget_cardview, parent, false), mHeight, mWidth);
            }
            case REPORT_PRODUCTION: {
                return new ReportProductionViewHolder(inflater.inflate(R.layout.report_production_widget_cardview, parent, false), mHeight, mWidth);
//                return new ReportProductionViewHolder(inflater.inflate(R.layout.report_production_widget_cardview, parent, false), mHeight, mWidth,
//                        mReportFieldsForMachine, mOnKeyboardManagerListener, mShowDashboardCroutonListener, mJoshId);
            }
            case MACHINE_WORK_BIT: {
                return new MachineWorkBitViewHolder(inflater.inflate(R.layout.machine_work_bit_cardview, parent, false), mHeight, mWidth, mShowDashboardCroutonListener);
            }
            case TOP_5_STOP_EVENTS: {
                return new TopStopEventsViewHolder(inflater.inflate(R.layout.top_stop_events_cardview, parent, false), mHeight, mWidth);
            }
        }
        return new NumericViewHolder(inflater.inflate(R.layout.numeric_widget_cardview, parent, false),
                mDashboardCentralContainerListener, mOnKeyboardManagerListener, mReportFieldsForMachine, mHeight, mWidth,
                WidgetInfo.getWidgetInfo(mPermissionResponse, WidgetInfo.PermissionId.CHANGE_UNITS_IN_CYCLE.getId()).getHaspermissionBoolean(),
                WidgetInfo.getWidgetInfo(mPermissionResponse, WidgetInfo.PermissionId.ADD_REJECTS.getId()).getHaspermissionBoolean());
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        detach(recyclerView);
    }

    public void detach(@NonNull RecyclerView recyclerView) {
        MoveViewJob.getInstance(null, 0, 0, null, null);
        int childCOunt = recyclerView.getChildCount();
        for (int i = 0; i < childCOunt; i++) {
            if (recyclerView.getChildViewHolder(recyclerView.getChildAt(i)) instanceof NumericViewHolder) {
                ((NumericViewHolder) recyclerView.getChildViewHolder(recyclerView.getChildAt(i))).clearTouchListener();
            }
        }
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
//                    setSizes(imageViewHolder.mParentLayout);
                    String path = mWidgets.get(position).getCurrentValue();
                    if (!path.contains("http")) {
                        path = null;
                    }
                    ImageLoader.getInstance().displayImage(path, imageViewHolder.mImageLayout);
                    break;
                case COUNTER:
                    final CounterViewHolder counterViewHolder = (CounterViewHolder) holder;
                    counterViewHolder.setCounterItem(widget);
                    break;
                case NUMERIC:
                    final NumericViewHolder numericViewHolder = (NumericViewHolder) holder;
                    numericViewHolder.setNumericItem(widget, WidgetInfo.getWidgetInfo(mPermissionResponse, WidgetInfo.PermissionId.CHANGE_UNITS_IN_CYCLE.getId()).getHaspermissionBoolean(),
                            WidgetInfo.getWidgetInfo(mPermissionResponse, WidgetInfo.PermissionId.ADD_REJECTS.getId()).getHaspermissionBoolean());
                    break;
                case TIME:
                    final TimeViewHolder timeViewHolder = (TimeViewHolder) holder;
                    timeViewHolder.setTimeItem(holder.itemView.getContext(), widget);
                    break;
                case RANGE:
                    final RangeViewHolder rangeViewHolder = (RangeViewHolder) holder;
                    rangeViewHolder.setRangeItem(rangeViewHolder.itemView.getContext(), widget);
                    break;
                case PROJECTION:
                    final ProjectionViewHolderNew projectionViewHolder = (ProjectionViewHolderNew) holder;
                    projectionViewHolder.setProjectionItem(widget);
                    break;
                case TIME_LEFT:
                    final TimeLeftViewHolder timeLeftViewHolder = (TimeLeftViewHolder) holder;
                    timeLeftViewHolder.setData(widget, mMachineStatus, mEndSetupDisable,
                            WidgetInfo.getWidgetInfo(mPermissionResponse, WidgetInfo.PermissionId.END_SETUP.getId()).getHaspermissionBoolean(),
                            WidgetInfo.getWidgetInfo(mPermissionResponse, WidgetInfo.PermissionId.ACTIVATE_JOB.getId()).getHaspermissionBoolean());
                    mEndSetupDisable = false;
                case REPORT_PERCENT:
                    final ReportStopViewHolder reportStopViewHolder = (ReportStopViewHolder) holder;
                    reportStopViewHolder.setData(widget);
                case GENERIC_TIME:
                    final GenericTimeViewHolder genericTimeViewHolder = (GenericTimeViewHolder) holder;
                    genericTimeViewHolder.setData(widget);
                case FREE_TEXT:
                    final FreeTextViewHolder freeTextViewHolder = (FreeTextViewHolder) holder;
                    freeTextViewHolder.setData(widget);
                case ORDER_TEST: {
                    final OrderTestViewHolder orderTestViewHolder = (OrderTestViewHolder) holder;
                    orderTestViewHolder.setData(widget);
                }
                case REPORT_PRODUCTION: {
                    final ReportProductionViewHolder reportProductionViewHolder = (ReportProductionViewHolder) holder;
                    reportProductionViewHolder.setData(widget);
                }
                case MACHINE_WORK_BIT: {
                    final MachineWorkBitViewHolder machineWorkBitViewHolder = (MachineWorkBitViewHolder) holder;
                    machineWorkBitViewHolder.setData(widget);
                }
                case TOP_5_STOP_EVENTS: {
                    final TopStopEventsViewHolder topStopEventsViewHolder = (TopStopEventsViewHolder) holder;
//                   Nathat missing
                }
                case FIELD_TYPE_ACTIVATE_JOB: {
                    final ActivateJobViewHolder activateJobViewHolder = (ActivateJobViewHolder) holder;
                    activateJobViewHolder.setData(widget);
//                   Nathat missing
                }
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

    public void setReportFieldsForMachine(ReportFieldsForMachine reportFieldsForMachine) {
        mReportFieldsForMachine = reportFieldsForMachine;
        if (!mEndSetupDisable) {
            mEndSetupDisable = false;
        }
    }

    public void setMachineStatus(MachineStatus machineStatus) {
        this.mMachineStatus = machineStatus;
//        {mEndSetupDisable = false;}
    }

    public void setApproveFirstItemFeedBack() {
        mEndSetupDisable = true;
        notifyDataSetChanged();
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
//                type = REPORT_PRODUCTION;
//                type = ORDER_TEST;
//                type = TOP_5_STOP_EVENTS;
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
            case 6:
                type = TIME_LEFT;
                break;
            case 7:
                type = REPORT_PERCENT;
                break;
            case 8:
                type = GENERIC_TIME;
                break;
            case 9:
                type = FREE_TEXT;
                break;
            case 10:
                type = ORDER_TEST;
                break;
            case 11:
                type = REPORT_PRODUCTION;
                break;
            case 12:
                type = MACHINE_WORK_BIT;
                break;
            case 13:
                type = TOP_5_STOP_EVENTS;
                break;
            case FIELD_TYPE_ACTIVATE_JOB:
                type = FIELD_TYPE_ACTIVATE_JOB;
                break;

            default:
                type = NUMERIC;
                break;
        }
        return type;
    }

}
