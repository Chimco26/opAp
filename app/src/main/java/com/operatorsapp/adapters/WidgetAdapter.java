package com.operatorsapp.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.operators.machinedatainfra.models.Widget;
import com.operators.machinestatusinfra.models.MachineStatus;
import com.operators.reportfieldsformachineinfra.ReportFieldsForMachine;
import com.operatorsapp.R;
import com.operatorsapp.activities.interfaces.GoToScreenListener;
import com.operatorsapp.interfaces.DashboardCentralContainerListener;
import com.operatorsapp.view.widgetViewHolders.CounterViewHolder;
import com.operatorsapp.view.widgetViewHolders.NumericViewHolder;
import com.operatorsapp.view.widgetViewHolders.ProjectionViewHolder;
import com.operatorsapp.view.widgetViewHolders.RangeViewHolder;
import com.operatorsapp.view.widgetViewHolders.TimeLeftViewHolder;
import com.operatorsapp.view.widgetViewHolders.TimeViewHolder;

import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.RecyclerView.Adapter;
import static android.support.v7.widget.RecyclerView.ViewHolder;

public class WidgetAdapter extends Adapter {
    private static final long TEN_HOURS = 60000L * 60 * 10;
    private final DashboardCentralContainerListener mDashboardCentralContainerListener;
    private final NumericViewHolder.OnKeyboardManagerListener mOnKeyboardManagerListener;
    private Activity mContext;
    private List<Widget> mWidgets;
    private final int NUMERIC = 0;
    private final int RANGE = 1;
    private final int PROJECTION = 2;
    private final int TIME = 3;
    private final int COUNTER = 4;
    private final int IMAGE = 5;
    private final int TIME_LEFT = 6; // current value is the time left in minutes
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

    public WidgetAdapter(Activity context, List<Widget> widgets, GoToScreenListener goToScreenListener,
                         boolean closedState, int height, int width,
                         DashboardCentralContainerListener dashboardCentralContainerListener,
                         ReportFieldsForMachine reportFieldsForMachine, MachineStatus machineStatus,
                         NumericViewHolder.OnKeyboardManagerListener onKeyboardManagerListener) {
        mWidgets = widgets;
        mContext = context;
        mGoToScreenListener = goToScreenListener;
        mClosedState = closedState;
        mHeight = height;
        mWidth = width;
        mDashboardCentralContainerListener = dashboardCentralContainerListener;
        mReportFieldsForMachine = reportFieldsForMachine;
        mMachineStatus = machineStatus;
        mOnKeyboardManagerListener = onKeyboardManagerListener;
    }

    public void changeState(boolean closedState) {
        mClosedState = closedState;
        notifyDataSetChanged();
    }

    public void setNewData(List<Widget> widgets) {
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

        }
    }

    private void setSizes(final RelativeLayout parent) {
        ViewGroup.LayoutParams layoutParams;
        layoutParams = parent.getLayoutParams();
        layoutParams.height = (int) (mHeight * 0.45);
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
                        mContext, mDashboardCentralContainerListener, mOnKeyboardManagerListener, mReportFieldsForMachine, mHeight, mWidth);
            }
            case RANGE: {
                return new RangeViewHolder(inflater.inflate(R.layout.range_widget_cardview, parent, false),
                        mContext, mClosedState, mHeight, mWidth);
            }
            case PROJECTION: {
                return new ProjectionViewHolder(inflater.inflate(R.layout.projection_widget_cardview, parent, false),
                        mContext, mHeight, mWidth);
            }
            case TIME: {
                return new TimeViewHolder(inflater.inflate(R.layout.time_widget_cardview, parent, false), mContext, mGoToScreenListener, mHeight, mWidth);
            }
            case COUNTER: {
                return new CounterViewHolder(inflater.inflate(R.layout.counter_widget_cardview, parent, false), mContext, mDashboardCentralContainerListener, mHeight, mWidth);
            }
            case IMAGE: {
                return new ImageViewHolder(inflater.inflate(R.layout.image_widget_cardview, parent, false));
            }
            case TIME_LEFT: {
                return new TimeLeftViewHolder(inflater.inflate(R.layout.time_left_widget_cardview, parent, false),
                        mDashboardCentralContainerListener, mMachineStatus, mEndSetupDisable, mHeight, mWidth);
            }
        }
        return new NumericViewHolder(inflater.inflate(R.layout.numeric_widget_cardview, parent, false),
                mContext, mDashboardCentralContainerListener, mOnKeyboardManagerListener, mReportFieldsForMachine, mHeight, mWidth);
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
                    setSizes(imageViewHolder.mParentLayout);
                    ImageLoader.getInstance().displayImage(mWidgets.get(position).getCurrentValue(), imageViewHolder.mImageLayout);
                    break;
                case COUNTER:
                    final CounterViewHolder counterViewHolder = (CounterViewHolder) holder;
                    counterViewHolder.setCounterItem(widget);
                    break;
                case NUMERIC:
                    final NumericViewHolder numericViewHolder = (NumericViewHolder) holder;
                    numericViewHolder.setNumericItem(widget);
                    break;
                case TIME:
                    final TimeViewHolder timeViewHolder = (TimeViewHolder) holder;
                    timeViewHolder.setTimeItem(widget);
                    break;
                case RANGE:
                    final RangeViewHolder rangeViewHolder = (RangeViewHolder) holder;
                    rangeViewHolder.setRangeItem(widget);
                    break;
                case PROJECTION:
                    final ProjectionViewHolder projectionViewHolder = (ProjectionViewHolder) holder;
                    projectionViewHolder.setProjectionItem(widget);
                    break;
                case TIME_LEFT:
                    final TimeLeftViewHolder timeLeftViewHolder = (TimeLeftViewHolder) holder;
                    timeLeftViewHolder.setData(widget, mMachineStatus, mEndSetupDisable);
                    mEndSetupDisable = false;
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
        if (!mEndSetupDisable) {mEndSetupDisable = false;}
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
            default:
                type = NUMERIC;
                break;
        }
        return type;
    }

}
