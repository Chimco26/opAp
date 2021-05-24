package com.operatorsapp.fragments;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.SparseArray;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.common.Event;
import com.example.common.StandardResponse;
import com.example.common.actualBarExtraResponse.ActualBarExtraResponse;
import com.example.common.machineJoshDataResponse.MachineJoshDataResponse;
import com.example.common.permissions.WidgetInfo;
import com.example.oppapplog.OppAppLogger;
import com.github.mikephil.charting.jobs.MoveViewJob;
import com.operators.activejobslistformachineinfra.ActiveJobsListForMachine;
import com.operators.machinedatainfra.models.Widget;
import com.operators.machinestatusinfra.models.MachineStatus;
import com.operators.reportfieldsformachineinfra.ReportFieldsForMachine;
import com.operatorsapp.R;
import com.operatorsapp.activities.interfaces.GoToScreenListener;
import com.operatorsapp.activities.interfaces.ShowDashboardCroutonListener;
import com.operatorsapp.adapters.WidgetAdapter;
import com.operatorsapp.interfaces.DashboardCentralContainerListener;
import com.operatorsapp.interfaces.DashboardUICallbackListener;
import com.operatorsapp.interfaces.OnActivityCallbackRegistered;
import com.operatorsapp.interfaces.OnKeyboardManagerListener;
import com.operatorsapp.interfaces.ReportFieldsFragmentCallbackListener;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.utils.SoftKeyboardUtil;
import com.operatorsapp.utils.TimeUtils;
import com.operatorsapp.view.GridSpacingItemDecoration;
import com.operatorsapp.view.SingleLineKeyboard;
import com.operatorsapp.view.widgetViewHolders.NumericViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.text.format.DateUtils.DAY_IN_MILLIS;
import static org.acra.ACRA.LOG_TAG;

/**
 * Created by nathanb on 4/29/2018.
 */

public class WidgetFragment extends Fragment implements
        DashboardUICallbackListener, OnKeyboardManagerListener {

    public static final String KEY_JOSH_ID = "JOSHID";
    private static final int CHART_POINTS_LIST_LIMIT_SIZE = 1500;
    private boolean mIsOpen;
    private int mWidgetsLayoutWidth;
    private int mRecyclersHeight;
    private LinearLayout mLoadingDataView;
    private RecyclerView mWidgetRecycler;
    private GridLayoutManager mGridLayoutManager;
    private WidgetAdapter mWidgetAdapter;
    private List<Widget> mWidgets;
    private GoToScreenListener mOnGoToScreenListener;
    private OnActivityCallbackRegistered mOnActivityCallbackRegistered;
    private ReportFieldsFragmentCallbackListener mReportFieldsFragmentCallbackListener;
    private DashboardCentralContainerListener mDashboardCentralContainerListener;
    private ReportFieldsForMachine mReportFieldForMachine;
    private MachineStatus mMachineStatus;


    private LinearLayout mKeyBoardLayout;
    private SingleLineKeyboard mKeyBoard;
    private int mSpanCount;
    private SparseArray<WidgetInfo> mPermissionResponse;
    private Integer mJoshId = null;
    private ShowDashboardCroutonListener mShowDashboardCroutonListener;

    public static WidgetFragment newInstance(ReportFieldsForMachine reportFieldsForMachine, int joshId) {
        WidgetFragment widgetFragment = new WidgetFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ReportFieldsForMachine.TAG, reportFieldsForMachine);
        bundle.putInt(KEY_JOSH_ID,joshId);
        return widgetFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mReportFieldForMachine = getArguments().getParcelable(ReportFieldsForMachine.TAG);
            mJoshId = getArguments().getInt(KEY_JOSH_ID);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_widgets, container, false);

        SoftKeyboardUtil.hideKeyboard(this);
        return inflate;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        OppAppLogger.d(LOG_TAG, "onViewCreated(), start ");
        super.onViewCreated(view, savedInstanceState);

        mIsOpen = false;
//        // get screen parameters
        if (getActivity() != null) {
//            view.post(new Runnable() {
//                @Override
//                public void run() {
//                    if (getActivity() == null) {
//                        return;
//                    }
                    Display display = getActivity().getWindowManager().getDefaultDisplay();
                    Point size = new Point();
                    display.getSize(size);
                    int mWidth = size.x;
                    int mHeight = size.y;

                    mWidgetsLayoutWidth = (int) (mWidth * 0.84);
                    mRecyclersHeight = (int) (mHeight * 0.67);

                    mSpanCount = 3;//(int) ((view.getWidth()) / (getActivity().getResources().getDimension(R.dimen.widget_width)));
                    mSpanCount = Math.max(mSpanCount, 1);
                    ViewGroup mWidgetsLayout = view.findViewById(R.id.fragment_dashboard_widgets_layout);
                    ViewGroup.MarginLayoutParams mWidgetsParams = (ViewGroup.MarginLayoutParams) mWidgetsLayout.getLayoutParams();
                    mWidgetsLayout.setLayoutParams(mWidgetsParams);

                    mWidgetRecycler = view.findViewById(R.id.fragment_dashboard_widgets);
                    mGridLayoutManager = new GridLayoutManager(getActivity(), mSpanCount);
                    mWidgetRecycler.setLayoutManager(mGridLayoutManager);
                    GridSpacingItemDecoration mGridSpacingItemDecoration = new GridSpacingItemDecoration(mSpanCount, 14, true, 0);
                    mGridSpacingItemDecoration.setSpacingTop(0);
                    mGridSpacingItemDecoration.setSpacingBottom(8);
                    mWidgetRecycler.addItemDecoration(mGridSpacingItemDecoration);
                    mWidgetAdapter = new WidgetAdapter(mWidgets, mOnGoToScreenListener,mJoshId,
                            true, mRecyclersHeight, mWidgetsLayoutWidth,
                            mDashboardCentralContainerListener, mReportFieldForMachine, mMachineStatus,mShowDashboardCroutonListener, new OnKeyboardManagerListener() {
                        @Override
                        public void onOpenKeyboard(Context context, SingleLineKeyboard.OnKeyboardClickListener listener, String text, String[] complementChars) {
                            if (mKeyBoardLayout != null) {
                                mKeyBoardLayout.setVisibility(View.VISIBLE);
                                if (mKeyBoard == null)
                                    mKeyBoard = new SingleLineKeyboard(mKeyBoardLayout, getContext());

                                mKeyBoard.setChars(complementChars);
                                mKeyBoard.openKeyBoard(context, text);
                                mKeyBoard.setListener(listener);
                                if (mDashboardCentralContainerListener != null) {
                                    mDashboardCentralContainerListener.onKeyboardEvent(true);
                                }
                            }
                        }

                        @Override
                        public void onCloseKeyboard() {
                            if (mKeyBoardLayout != null) {
                                mKeyBoardLayout.setVisibility(View.GONE);
                            }
                            if (mKeyBoard != null) {
                                mKeyBoard.setListener(null);
                            }
                            if (mDashboardCentralContainerListener != null) {
                                mDashboardCentralContainerListener.onKeyboardEvent(false);
                            }
                        }

                    }, mPermissionResponse);
                    mWidgetRecycler.setAdapter(mWidgetAdapter);

                    mLoadingDataView = view.findViewById(R.id.fragment_dashboard_loading_data_widgets);
                    mLoadingDataView.setVisibility(View.VISIBLE);
                    mKeyBoardLayout = view.findViewById(R.id.FW_keyboard);

                }
//            });
//
//        }

    }

    @Override
    public void onAttach(Context context) {
        OppAppLogger.d(LOG_TAG, "onAttach(), start ");
        super.onAttach(context);
        try {
            mReportFieldsFragmentCallbackListener = (ReportFieldsFragmentCallbackListener) getActivity();
            mOnActivityCallbackRegistered = (OnActivityCallbackRegistered) context;
            mOnActivityCallbackRegistered.onFragmentAttached(WidgetFragment.class.getSimpleName(),this);
            mOnGoToScreenListener = (GoToScreenListener) getActivity();
            mDashboardCentralContainerListener = (DashboardCentralContainerListener) getActivity();
            mShowDashboardCroutonListener = (ShowDashboardCroutonListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling fragment must implement interface");
        }
        OppAppLogger.d(LOG_TAG, "onAttach(), end ");
    }

    @Override
    public void onDetach() {
        OppAppLogger.d(LOG_TAG, "onDetach(), start ");
        super.onDetach();
        mOnActivityCallbackRegistered.onFragmentDetached(WidgetFragment.class.getSimpleName(),this);
        mOnActivityCallbackRegistered = null;
        mOnGoToScreenListener = null;
        mReportFieldsFragmentCallbackListener = null;
        mDashboardCentralContainerListener = null;
        mShowDashboardCroutonListener = null;
        OppAppLogger.d(LOG_TAG, "onDetach(), end ");
        MoveViewJob.getInstance(null, 0, 0, null, null);

    }

    @Override
    public void onDestroyView() {
        if (mWidgetAdapter != null && mWidgetRecycler != null){
            mWidgetAdapter.detach(mWidgetRecycler);
        }
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPermissionForMachinePolling(SparseArray<WidgetInfo> permissionResponse) {
        mPermissionResponse = permissionResponse;
    }

    @Override
    public void onDeviceStatusChanged(MachineStatus machineStatus) {
        this.mMachineStatus = machineStatus;
        if (mWidgetAdapter != null) {
            mWidgetAdapter.setMachineStatus(mMachineStatus);
        }
    }

    @Override
    public void onMachineDataReceived(ArrayList<Widget> widgetList) {

        if (mReportFieldsFragmentCallbackListener == null || mReportFieldsFragmentCallbackListener.getReportForMachine() == null
                || mLoadingDataView == null) {

            return;

        }

        mWidgets = widgetList;
        if (widgetList != null && widgetList.size() > 0) {
            saveAndRestoreChartData(widgetList);
            PersistenceManager.getInstance().setMachineDataStartingFrom(TimeUtils.getDate(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss.SSS"));
//            mNoDataView.setVisibility(View.GONE);
            mLoadingDataView.setVisibility(View.GONE);

            if (mWidgetAdapter != null) {
                mWidgetAdapter.setNewData(widgetList, mPermissionResponse);
            } else {
                mWidgetAdapter = new WidgetAdapter(widgetList, mOnGoToScreenListener,
                        mJoshId, !mIsOpen, mRecyclersHeight, mWidgetsLayoutWidth,
                        mDashboardCentralContainerListener, mReportFieldForMachine, mMachineStatus, mShowDashboardCroutonListener, this, mPermissionResponse);
                mWidgetRecycler.setAdapter(mWidgetAdapter);
            }
        } else {

            mLoadingDataView.setVisibility(View.GONE);

//            mNoDataView.setVisibility(View.VISIBLE);
        }

    }

    public void setSpanCount(boolean open) {
        if (mGridLayoutManager != null) {
            if (open) {
                mGridLayoutManager.setSpanCount(Math.max(mSpanCount, 1));
            } else {
                mGridLayoutManager.setSpanCount(Math.max(mSpanCount - 1, 1));
            }
        }
    }

    public void setWidgetState(boolean state) {
        if (mWidgetAdapter != null) {
            mWidgetAdapter.changeState(state);
        }
    }

    @Override
    public void onShiftLogDataReceived(ArrayList<Event> events, ActualBarExtraResponse actualBarExtraResponse, MachineJoshDataResponse mMachineJoshDataResponse) {

    }

    @Override
    public void onTimerChanged(String timeToEndInHours) {

    }

    @Override
    public void onDataFailure(StandardResponse reason, CallType callType) {

        mLoadingDataView.setVisibility(View.GONE);

    }

    @Override
    public void onApproveFirstItemEnabledChanged(boolean enabled) {
        if (mWidgetAdapter != null) {
            mWidgetAdapter.setApproveFirstItemFeedBack();
        }
    }

    private void saveAndRestoreChartData(ArrayList<Widget> widgetList) {
        // calls to this function were removed as saving to prefs was not needed.
        //historic data from prefs

        HashMap<String, ArrayList<Widget.HistoricData>> prefsHistoricCopy = new HashMap<>();
        HashMap<String, ArrayList<Widget.HistoricData>> prefsHistoric = PersistenceManager.getInstance().getChartHistoricData();

        if (prefsHistoric != null && prefsHistoric.size() > 0) {
            prefsHistoricCopy.putAll(prefsHistoric);
        }

        for (Widget widget : widgetList) {
            // if have chart widget (field type 3)
            if (widget.getFieldType() == 3) {

                removeOver1500Points(widget);

                // if have historic in prefs
                if (prefsHistoricCopy.size() > 0) {

                    if (prefsHistoricCopy.containsKey(String.valueOf(widget.getID()))) {
                        cleanOver24HoursData(prefsHistoricCopy, String.valueOf(widget.getID()));
                    }
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
                        if (prefsHistoricCopy.containsKey(String.valueOf(widget.getID())) &&
                                prefsHistoricCopy.get(String.valueOf(widget.getID())) != null) {
                            widget.getMachineParamHistoricData().addAll(prefsHistoricCopy.get(String.valueOf(widget.getID())));
                        }
                    }

                } else {
                    // if is the first chart data,  save to prefs
                    prefsHistoricCopy.put(String.valueOf(widget.getID()), widget.getMachineParamHistoricData());
                }
            }
        }
        PersistenceManager.getInstance().saveChartHistoricData(prefsHistoricCopy);
    }

    private void removeOver1500Points(Widget widget) {
        int widgetParamHistoDataSize = widget.getMachineParamHistoricData().size();
        ArrayList<Widget.HistoricData> toRemove = new ArrayList<>();

        if (widgetParamHistoDataSize > CHART_POINTS_LIST_LIMIT_SIZE) {
            for (int i = CHART_POINTS_LIST_LIMIT_SIZE + 1; i < widgetParamHistoDataSize; i++) {

                toRemove.add(widget.getMachineParamHistoricData().get(i));
            }
        }

        for (Widget.HistoricData historicDataToRemove : toRemove) {

            widget.getMachineParamHistoricData().remove(historicDataToRemove);
        }
    }

    private void cleanOver24HoursData(HashMap<String, ArrayList<Widget.HistoricData>> prefsHistoricCopy, String widgetId) {

        ArrayList<Widget.HistoricData> toClean = new ArrayList<>();

        for (int i = 0; i < prefsHistoricCopy.get(widgetId).size(); i++) {
            if (TimeUtils.getLongFromDateString(prefsHistoricCopy.get(widgetId).get(i).getTime(), "dd/MM/yyyy HH:mm:ss") <
                    (TimeUtils.getLongFromDateString(prefsHistoricCopy.get(widgetId).get(prefsHistoricCopy.get(widgetId).size() - 1).getTime(), "dd/MM/yyyy HH:mm:ss") - DAY_IN_MILLIS)
                    || i > CHART_POINTS_LIST_LIMIT_SIZE) {
                toClean.add(prefsHistoricCopy.get(widgetId).get(i));
            }
        }
        for (Widget.HistoricData toCleanItem : toClean) {

            prefsHistoricCopy.get(widgetId).remove(toCleanItem);
        }

        if (prefsHistoricCopy.get(widgetId).size() < 1) {

            prefsHistoricCopy.remove(widgetId);
        }

    }

    @Override
    public void onActiveJobsListForMachineUICallbackListener(ActiveJobsListForMachine activeJobsListForMachine) {
    }

    public void setReportFieldForMachine(ReportFieldsForMachine reportFieldsForMachine) {
        mReportFieldForMachine = reportFieldsForMachine;
        if (mWidgetAdapter != null) {
            mWidgetAdapter.setReportFieldsForMachine(reportFieldsForMachine);
        }
    }

    public void scrollToPosition(int position) {
        mWidgetRecycler.scrollToPosition(position);
    }


    @Override
    public void onOpenKeyboard(Context context, SingleLineKeyboard.OnKeyboardClickListener listener, String text, String[] complementChars) {
        if (mKeyBoardLayout != null) {
            mKeyBoardLayout.setVisibility(View.VISIBLE);
            if (mKeyBoard == null)
                mKeyBoard = new SingleLineKeyboard(mKeyBoardLayout, getContext());

            mKeyBoard.setChars(complementChars);
            mKeyBoard.openKeyBoard(context, text);
            mKeyBoard.setListener(listener);
            if (mDashboardCentralContainerListener != null) {
                mDashboardCentralContainerListener.onKeyboardEvent(true);
            }
        }
    }

    @Override
    public void onCloseKeyboard() {
        if (mKeyBoardLayout != null) {
            mKeyBoardLayout.setVisibility(View.GONE);
        }
        if (mKeyBoard != null) {
            mKeyBoard.setListener(null);
            mKeyBoard.closeKeyBoard();
        }
        if (mDashboardCentralContainerListener != null) {
            mDashboardCentralContainerListener.onKeyboardEvent(false);
        }
    }

}

