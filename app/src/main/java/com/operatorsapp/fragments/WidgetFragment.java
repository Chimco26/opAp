package com.operatorsapp.fragments;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.oppapplog.OppAppLogger;
import com.operators.activejobslistformachineinfra.ActiveJobsListForMachine;
import com.operators.errorobject.ErrorObjectInterface;
import com.operators.machinedatainfra.models.Widget;
import com.operators.machinestatusinfra.models.MachineStatus;
import com.operatorsapp.R;
import com.operatorsapp.activities.interfaces.GoToScreenListener;
import com.operatorsapp.adapters.TopFiveAdapter;
import com.operatorsapp.adapters.WidgetAdapter;
import com.operatorsapp.interfaces.DashboardCentralContainerListener;
import com.operatorsapp.interfaces.DashboardUICallbackListener;
import com.operatorsapp.interfaces.OnActivityCallbackRegistered;
import com.operatorsapp.interfaces.ReportFieldsFragmentCallbackListener;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.model.TopFiveItem;
import com.operatorsapp.server.NetworkManager;
import com.operatorsapp.server.requests.GetTopRejectsAndEventsRequest;
import com.operatorsapp.server.responses.CriticalEvent;
import com.operatorsapp.server.responses.StopAndCriticalEventsResponse;
import com.operatorsapp.server.responses.TopRejectResponse;
import com.operatorsapp.utils.SoftKeyboardUtil;
import com.operatorsapp.utils.TimeUtils;
import com.operatorsapp.view.GridSpacingItemDecoration;
import com.ravtech.david.sqlcore.Event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.text.format.DateUtils.DAY_IN_MILLIS;
import static org.acra.ACRA.LOG_TAG;

/**
 * Created by nathanb on 4/29/2018.
 */

public class WidgetFragment extends Fragment implements
        DashboardUICallbackListener {

    private static final int CHART_POINTS_LIST_LIMIT_SIZE = 1500;
    private boolean mIsOpen;
    //    private int mOpenWidth;
    private int mWidgetsLayoutWidth;
    //    private int mTollBarsHeight;
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
    private RecyclerView mTopStops_rv;
    private RecyclerView mTopRejects_rv;
    TextView include1_title;
    TextView include2_title;
    TextView row1_title;
    TextView row2_title;
    TextView include1_row1_stat1_num;
    TextView include1_row1_stat2_num;
    TextView include1_row1_stat3_num;
    TextView include1_row2_stat1_num;
    TextView include1_row2_stat2_num;
    TextView include1_row2_stat3_num;
    TextView include2_stat1_num;
    TextView include2_stat2_num;
    TextView include2_stat3_num;
    private LinearLayout row1_lil;
    private LinearLayout row2_lil;

    public static WidgetFragment newInstance() {
        return new WidgetFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        ProgressDialogManager.show(getActivity());
        View inflate = inflater.inflate(R.layout.fragment_widgets, container, false);

//        if(getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
//            inflate.setRotationY(180);
//        }

        SoftKeyboardUtil.hideKeyboard(this);
        return inflate;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        OppAppLogger.getInstance().d(LOG_TAG, "onViewCreated(), start ");
        super.onViewCreated(view, savedInstanceState);

        mIsOpen = false;
        // get screen parameters
        if (getActivity() != null) {
            Display display = getActivity().getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int mWidth = size.x;
            int mHeight = size.y;

            mWidgetsLayoutWidth = (int) (mWidth * 0.8);
            mRecyclersHeight = (int) (mHeight * 0.75);

            ViewGroup mWidgetsLayout = view.findViewById(R.id.fragment_dashboard_widgets_layout);
            ViewGroup.MarginLayoutParams mWidgetsParams = (ViewGroup.MarginLayoutParams) mWidgetsLayout.getLayoutParams();
            mWidgetsLayout.setLayoutParams(mWidgetsParams);

            mWidgetRecycler = view.findViewById(R.id.fragment_dashboard_widgets);
            mGridLayoutManager = new GridLayoutManager(getActivity(), 3);
            mWidgetRecycler.setLayoutManager(mGridLayoutManager);
            GridSpacingItemDecoration mGridSpacingItemDecoration = new GridSpacingItemDecoration(3, 14, true, 0);
            mWidgetRecycler.addItemDecoration(mGridSpacingItemDecoration);
            mWidgetAdapter = new WidgetAdapter(getActivity(), mWidgets, mOnGoToScreenListener, true, mRecyclersHeight, mWidgetsLayoutWidth, mDashboardCentralContainerListener);
            mWidgetRecycler.setAdapter(mWidgetAdapter);

            mLoadingDataView = view.findViewById(R.id.fragment_dashboard_loading_data_widgets);
            mLoadingDataView.setVisibility(View.VISIBLE);

            initTopFive(view);
        }

    }

    private void initTopFive(View view) {

        View includeTopFive_1 = view.findViewById(R.id.fragment_dashboard_top_five_1);
        View includeTopFive_2 = view.findViewById(R.id.fragment_dashboard_top_five_2);


        row1_lil = view.findViewById(R.id.row1_top_five_lil);
        row2_lil = view.findViewById(R.id.row2_top_five_lil);
        include1_title = includeTopFive_1.findViewById(R.id.title_top_five_tv);
        include2_title = includeTopFive_2.findViewById(R.id.title_top_five_tv);

        row1_title = includeTopFive_1.findViewById(R.id.title_row1_top_five_tv);
        include1_row1_stat1_num = includeTopFive_1.findViewById(R.id.row1_title_stat1_top_five_number_tv);
        include1_row1_stat2_num = includeTopFive_1.findViewById(R.id.row1_title_stat2_top_five_number_tv);
        include1_row1_stat3_num = includeTopFive_1.findViewById(R.id.row1_title_stat3_top_five_number_tv);

        row2_title = includeTopFive_1.findViewById(R.id.title_row2_top_five_tv);
        include1_row2_stat1_num = includeTopFive_1.findViewById(R.id.row2_title_stat1_top_five_number_tv);
        include1_row2_stat2_num = includeTopFive_1.findViewById(R.id.row2_title_stat2_top_five_number_tv);
        include1_row2_stat3_num = includeTopFive_1.findViewById(R.id.row2_title_stat3_top_five_number_tv);



        mTopStops_rv = includeTopFive_1.findViewById(R.id.top_five_recyclerView);
        mTopStops_rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        mTopStops_rv.setAdapter(new TopFiveAdapter(getActivity(), new ArrayList<TopFiveItem>(), TopFiveAdapter.TYPE_STOP));

        mTopRejects_rv = includeTopFive_2.findViewById(R.id.top_five_recyclerView);
        mTopRejects_rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        mTopRejects_rv.setAdapter(new TopFiveAdapter(getActivity(), new ArrayList<TopFiveItem>(), TopFiveAdapter.TYPE_REJECT));

        include1_title.setText(getString(R.string.top_five_stop_events));
        include2_title.setText(getString(R.string.top_five_reject_reasons));
    }

    @Override
    public void onAttach(Context context) {
        OppAppLogger.getInstance().d(LOG_TAG, "onAttach(), start ");
        super.onAttach(context);
        try {
            mReportFieldsFragmentCallbackListener = (ReportFieldsFragmentCallbackListener) getActivity();
            mOnActivityCallbackRegistered = (OnActivityCallbackRegistered) context;
            mOnActivityCallbackRegistered.onFragmentAttached(this);
            mOnGoToScreenListener = (GoToScreenListener) getActivity();
            mDashboardCentralContainerListener = (DashboardCentralContainerListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling fragment must implement interface");
        }
        OppAppLogger.getInstance().d(LOG_TAG, "onAttach(), end ");
    }

    @Override
    public void onDetach() {
        OppAppLogger.getInstance().d(LOG_TAG, "onDetach(), start ");
        super.onDetach();
        mOnActivityCallbackRegistered.onFragmentDetached(this);
        mOnActivityCallbackRegistered = null;
        mOnGoToScreenListener = null;
        mReportFieldsFragmentCallbackListener = null;
        mDashboardCentralContainerListener = null;
        OppAppLogger.getInstance().d(LOG_TAG, "onDetach(), end ");
    }

    @Override
    public void onResume() {
        super.onResume();

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
                mWidgetAdapter = new WidgetAdapter(getActivity(), widgetList, mOnGoToScreenListener, !mIsOpen, mRecyclersHeight, mWidgetsLayoutWidth, mDashboardCentralContainerListener);
                mWidgetRecycler.setAdapter(mWidgetAdapter);
            }
        } else {

            mLoadingDataView.setVisibility(View.GONE);

//            mNoDataView.setVisibility(View.VISIBLE);
        }

        getTopRejectsAndStops();
    }

    private void getTopRejectsAndStops() {

        PersistenceManager pm = PersistenceManager.getInstance();
        String[] machineId = {String.valueOf(pm.getMachineId())};
        GetTopRejectsAndEventsRequest request = new GetTopRejectsAndEventsRequest(machineId, pm.getSessionId(), pm.getShiftStart(), pm.getShiftEnd());

        NetworkManager.getInstance().getTopRejects(request, new Callback<TopRejectResponse>() {
            @Override
            public void onResponse(Call<TopRejectResponse> call, Response<TopRejectResponse> response) {
                if (response != null && response.body().getmError() == null && response.body().getmRejectsList() != null && response.body().getmRejectsList().size() > 0){
                    ((TopFiveAdapter) mTopRejects_rv.getAdapter()).setmTopList(response.body().getRejectsAsTopFive());
                }
            }

            @Override
            public void onFailure(Call<TopRejectResponse> call, Throwable t) {

            }
        });


        NetworkManager.getInstance().getTopStopAndCriticalEvents(request, new Callback<StopAndCriticalEventsResponse>() {
            @Override
            public void onResponse(Call<StopAndCriticalEventsResponse> call, Response<StopAndCriticalEventsResponse> response) {
                if (response != null && response.body().getmError() == null){

                    if (response.body().getmStopEvents() != null && response.body().getmStopEvents().size() > 0) {
                        ((TopFiveAdapter) mTopStops_rv.getAdapter()).setmTopList(response.body().getStopsAsTopFive());
                    }

                    if (response.body().getmCriticalEvents() != null && response.body().getmCriticalEvents().size() > 0) {
                        initCritical(response.body());
                    }else {
                        row1_lil.setVisibility(View.GONE);
                        row2_lil.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<StopAndCriticalEventsResponse> call, Throwable t) {

            }
        });
    }

    private void initCritical(StopAndCriticalEventsResponse response) {
        ArrayList<CriticalEvent> critList = response.getmCriticalEvents();

        if (critList != null && critList.size() >= 1){
            CriticalEvent crit1 = critList.get(0);
            row1_lil.setVisibility(View.VISIBLE);
            row1_title.setText(crit1.getmName());
            include1_row1_stat1_num.setText(crit1.getmDuration());
            include1_row1_stat2_num.setText(crit1.getmPercentageDuration() + "%");
            include1_row1_stat3_num.setText(crit1.getmEventsCount());
        }else {
            row1_lil.setVisibility(View.GONE);
        }

        if (critList != null && critList.size() >= 2){
            CriticalEvent crit2 = critList.get(1);
            row2_lil.setVisibility(View.VISIBLE);
            row2_title.setText(crit2.getmName());
            include1_row2_stat1_num.setText(crit2.getmDuration());
            include1_row2_stat2_num.setText(crit2.getmPercentageDuration() + "%");
            include1_row2_stat3_num.setText(crit2.getmEventsCount());
        }else {
            row2_lil.setVisibility(View.GONE);
        }

    }

    public void setSpanCount(int span) {
        if (mGridLayoutManager != null) {
            mGridLayoutManager.setSpanCount(span);
        }
    }

    public void setWidgetState(boolean state) {
        if (mWidgetAdapter != null) {
            mWidgetAdapter.changeState(state);
        }
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

    }

    @Override
    public void onApproveFirstItemEnabledChanged(boolean enabled) {

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

        for (Widget.HistoricData historicDataToRemove: toRemove){

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
    public void onActiveJobsListForMachineUICallbackListener(ActiveJobsListForMachine mActiveJobsListForMachine) {

    }
}
