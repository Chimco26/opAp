package com.operatorsapp.fragments;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.operators.errorobject.ErrorObjectInterface;
import com.operators.machinedatainfra.models.Widget;
import com.operators.machinestatusinfra.models.MachineStatus;
import com.operatorsapp.R;
import com.operatorsapp.activities.interfaces.GoToScreenListener;
import com.operatorsapp.adapters.WidgetAdapter;
import com.operatorsapp.interfaces.DashboardUICallbackListener;
import com.operatorsapp.interfaces.OnActivityCallbackRegistered;
import com.operatorsapp.interfaces.ReportFieldsFragmentCallbackListener;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.managers.ProgressDialogManager;
import com.operatorsapp.utils.SoftKeyboardUtil;
import com.operatorsapp.view.GridSpacingItemDecoration;
import com.ravtech.david.sqlcore.Event;
import com.zemingo.logrecorder.ZLogger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.acra.ACRA.LOG_TAG;

/**
 * Created by nathanb on 4/29/2018.
 */

public class WidgetFragment extends Fragment implements
        DashboardUICallbackListener {

    private ViewGroup mWidgetsLayout;
    private ViewGroup.MarginLayoutParams mWidgetsParams;
    private int mCloseWidth;
    private boolean mIsOpen;
    private boolean mIsNewShiftLogs;
    //    private int mOpenWidth;
    private int mWidgetsLayoutWidth;
    //    private int mTollBarsHeight;
    private int mRecyclersHeight;
    private int mMiddleWidth;
    private TextView mNoNotificationsText;
//    private LinearLayout mNoDataView;
    private TextView mLoadingDataText;
    private LinearLayout mLoadingDataView;
    private RecyclerView mWidgetRecycler;
    private GridLayoutManager mGridLayoutManager;
    private WidgetAdapter mWidgetAdapter;
    private List<Widget> mWidgets;
    private GoToScreenListener mOnGoToScreenListener;
    private OnActivityCallbackRegistered mOnActivityCallbackRegistered;
    private ReportFieldsFragmentCallbackListener mReportFieldsFragmentCallbackListener;
    private int mWidth;
    private int mHeight;

    public static WidgetFragment newInstance() {
        return new WidgetFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        ProgressDialogManager.show(getActivity());
        View inflate = inflater.inflate(R.layout.fragment_widgets, container, false);

//        if(getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
//            inflate.setRotationY(180);
//        }

        SoftKeyboardUtil.hideKeyboard(this);
        return inflate;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ZLogger.d(LOG_TAG, "onViewCreated(), start ");
        super.onViewCreated(view, savedInstanceState);

        mIsOpen = false;
        mIsNewShiftLogs = PersistenceManager.getInstance().isNewShiftLogs();
        // get screen parameters
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        mWidth = size.x;
        mHeight = size.y;
        mWidgetsLayoutWidth = (int) (mWidth * 0.8);
        mRecyclersHeight = (int) (mHeight * 0.75);
        mMiddleWidth = (int) (mWidth * 0.3);

        mWidgetsLayout = (ViewGroup) view.findViewById(R.id.fragment_dashboard_widgets_layout);
        mWidgetsParams = (ViewGroup.MarginLayoutParams) mWidgetsLayout.getLayoutParams();
        mWidgetsParams.setMarginStart(mCloseWidth);
        mWidgetsLayout.setLayoutParams(mWidgetsParams);

        mWidgetRecycler = (RecyclerView) view.findViewById(R.id.fragment_dashboard_widgets);
        mGridLayoutManager = new GridLayoutManager(getActivity(), 3);
        mWidgetRecycler.setLayoutManager(mGridLayoutManager);
        GridSpacingItemDecoration mGridSpacingItemDecoration = new GridSpacingItemDecoration(3, 14, true, 0);
        mWidgetRecycler.addItemDecoration(mGridSpacingItemDecoration);
        mWidgetAdapter = new WidgetAdapter(getActivity(), mWidgets, mOnGoToScreenListener, true, mRecyclersHeight, mWidgetsLayoutWidth);
        mWidgetRecycler.setAdapter(mWidgetAdapter);

        mNoNotificationsText = (TextView) view.findViewById(R.id.fragment_dashboard_no_notif);
//        mNoDataView = (LinearLayout) view.findViewById(R.id.fragment_dashboard_no_data);

        mLoadingDataText = (TextView) view.findViewById(R.id.fragment_dashboard_loading_data_shiftlog);
        mLoadingDataView = (LinearLayout) view.findViewById(R.id.fragment_dashboard_loading_data_widgets);
        mLoadingDataView.setVisibility(View.VISIBLE);

    }

    @Override
    public void onAttach(Context context) {
        ZLogger.d(LOG_TAG, "onAttach(), start ");
        super.onAttach(context);
        try {
            mReportFieldsFragmentCallbackListener = (ReportFieldsFragmentCallbackListener) getActivity();
            mOnActivityCallbackRegistered = (OnActivityCallbackRegistered) context;
            mOnActivityCallbackRegistered.onFragmentAttached(this);
            mOnGoToScreenListener = (GoToScreenListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling fragment must implement interface");
        }
        ZLogger.d(LOG_TAG, "onAttach(), end ");
    }

    @Override
    public void onDetach() {
        ZLogger.d(LOG_TAG, "onDetach(), start ");
        super.onDetach();
        mOnActivityCallbackRegistered.onFragmentDetached(this);
        mOnActivityCallbackRegistered = null;
        mOnGoToScreenListener = null;
        mReportFieldsFragmentCallbackListener = null;
        ZLogger.d(LOG_TAG, "onDetach(), end ");
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mWidgets == null || mWidgets.size() == 0) {
//            mNoDataView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDeviceStatusChanged(MachineStatus machineStatus) {

    }

    @Override
    public void onMachineDataReceived(ArrayList<Widget> widgetList) {

        // if we can't fill any reports, show no data, client defined this behavior.
        if (mReportFieldsFragmentCallbackListener != null && mReportFieldsFragmentCallbackListener.getReportForMachine() == null) {

//            mNoDataView.setVisibility(View.VISIBLE);

            return;

        }

        mWidgets = widgetList;
        if (widgetList != null && widgetList.size() > 0) {
            saveAndRestoreChartData(widgetList);
            PersistenceManager.getInstance().setMachineDataStartingFrom(com.operatorsapp.utils.TimeUtils.getDate(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss.SSS"));
//            mNoDataView.setVisibility(View.GONE);
            mLoadingDataView.setVisibility(View.GONE);


            if (mWidgetAdapter != null) {
                mWidgetAdapter.setNewData(widgetList);
            } else {
                mWidgetAdapter = new WidgetAdapter(getActivity(), widgetList, mOnGoToScreenListener, !mIsOpen, mRecyclersHeight, mWidgetsLayoutWidth);
                mWidgetRecycler.setAdapter(mWidgetAdapter);
            }
        } else {

            mLoadingDataView.setVisibility(View.GONE);

//            mNoDataView.setVisibility(View.VISIBLE);
        }
    }

    public void setSpanCount(int span) {
        mGridLayoutManager.setSpanCount(span);
    }

    public void setWidgetState(boolean state) {
        mWidgetAdapter.changeState(state);
    }

    @Override
    public void onShiftLogDataReceived(ArrayList<Event> events) {

    }

    @Override
    public void onTimerChanged(String timeToEndInHours) {

    }

    @Override
    public void onDataFailure(ErrorObjectInterface reason, CallType callType) {

        mLoadingDataView.setVisibility(View.GONE);

        if (callType == CallType.MachineData) {
            if (mWidgets == null || mWidgets.size() == 0) {
//                mNoDataView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onShiftForMachineEnded() {

    }

    @Override
    public void onApproveFirstItemEnabledChanged(boolean enabled) {

    }

    private void saveAndRestoreChartData(ArrayList<Widget> widgetList) {
        // calls to this function were removed as saving to prefs was not needed.
        //historic data from prefs
        HashMap<String, ArrayList<Widget.HistoricData>> prefsHistoricCopy = new HashMap<>();
        HashMap<String, ArrayList<Widget.HistoricData>> prefsHistoric = PersistenceManager.getInstance().getChartHistoricData();
        for (Widget widget : widgetList) {
            // if have chart widget (field type 3)
            if (widget.getFieldType() == 3) {
                // if have historic in prefs
                if (prefsHistoric != null && prefsHistoric.size() > 0) {
                    prefsHistoricCopy.putAll(prefsHistoric);

                    // if get new data
                    if (widget.getMachineParamHistoricData() != null && widget.getMachineParamHistoricData().size() > 0) {
                        // if is old widget
                        if (prefsHistoricCopy.containsKey(String.valueOf(widget.getID()))) {

                            prefsHistoricCopy.get(String.valueOf(widget.getID())).addAll(widget.getMachineParamHistoricData());
                            widget.getMachineParamHistoricData().clear();

                            //set all data (old + new) to widget
                            if (prefsHistoricCopy.get(String.valueOf(widget.getID())) != null) {
                                widget.getMachineParamHistoricData().addAll(prefsHistoricCopy.get(String.valueOf(widget.getID())));
                            }
                        } else {
                            // if is new widget,  save to prefs
                            prefsHistoricCopy.put(String.valueOf(widget.getID()), widget.getMachineParamHistoricData());
                        }
                    } else {
                        // if no new data,  set old data to widget
                        if (prefsHistoricCopy.get(String.valueOf(widget.getID())) != null) {
                            widget.getMachineParamHistoricData().addAll(prefsHistoricCopy.get(String.valueOf(widget.getID())));
                        }
                    }

                } else {
                    // if is the firs chart data,  save to prefs
                    prefsHistoricCopy.put(String.valueOf(widget.getID()), widget.getMachineParamHistoricData());
                }
            }
        }
        PersistenceManager.getInstance().saveChartHistoricData(prefsHistoricCopy);
    }
}
