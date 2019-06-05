package com.operatorsapp.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.common.Event;
import com.example.common.actualBarExtraResponse.ActualBarExtraResponse;
import com.example.common.callback.ErrorObjectInterface;
import com.example.common.machineJoshDataResponse.MachineJoshDataResponse;
import com.example.common.reportShift.Department;
import com.example.common.reportShift.DepartmentShiftGraphRequest;
import com.example.common.reportShift.DepartmentShiftGraphResponse;
import com.example.common.reportShift.GraphColor;
import com.example.common.reportShift.NotificationByType;
import com.example.common.reportShift.ReqDepartment;
import com.example.common.reportShift.ServiceCallsResponse;
import com.example.common.reportShift.ShiftByTime;
import com.example.common.request.BaseTimeRequest;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.operators.activejobslistformachineinfra.ActiveJobsListForMachine;
import com.operators.machinedatainfra.models.Widget;
import com.operators.machinestatusinfra.models.MachineStatus;
import com.operatorsapp.R;
import com.operatorsapp.adapters.TopFiveAdapter;
import com.operatorsapp.interfaces.DashboardUICallbackListener;
import com.operatorsapp.interfaces.OnActivityCallbackRegistered;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.model.TopFiveItem;
import com.operatorsapp.server.NetworkManager;
import com.operatorsapp.server.requests.GetTopRejectsAndEventsRequest;
import com.operatorsapp.server.responses.StopAndCriticalEventsResponse;
import com.operatorsapp.server.responses.TopRejectResponse;
import com.operatorsapp.view.LineChartTimeSmall;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportShiftFragment extends Fragment implements DashboardUICallbackListener {

    public static final String TAG = ReportShiftFragment.class.getSimpleName();
    private static final String IS_TIME_LINE_OPEN = "IS_TIME_LINE_OPEN";
    private RecyclerView mTopStops_rv;
    private RecyclerView mTopRejects_rv;
    private OnActivityCallbackRegistered mOnActivityCallbackRegistered;
    private View mProgressBar;
    private RecyclerView.Adapter mStopsAdapter;
    private TopFiveAdapter mRejectsAdapter;
    private boolean isOpen;
    private ServiceCallsResponse mServiceCallsResponse;
    private DepartmentShiftGraphResponse mDepartmentShiftGraphResponse;
    private View mTopFiveStopsView;
    private View mTopFiveRejectsView;
    private PieChart mPieChart;
    private LineChartTimeSmall mCycleTime;

    public static ReportShiftFragment newInstance(boolean isTimeLineOpen) {
        ReportShiftFragment reportShiftFragment = new ReportShiftFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(IS_TIME_LINE_OPEN, isTimeLineOpen);
        reportShiftFragment.setArguments(bundle);
        return reportShiftFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && getArguments().containsKey(IS_TIME_LINE_OPEN)) {
            isOpen = getArguments().getBoolean(IS_TIME_LINE_OPEN);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mOnActivityCallbackRegistered = (OnActivityCallbackRegistered) context;
        mOnActivityCallbackRegistered.onFragmentAttached(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mOnActivityCallbackRegistered.onFragmentDetached(this);
        mOnActivityCallbackRegistered = null;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.frament_report_shift, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mProgressBar = view.findViewById(R.id.FTF_progress);
        mPieChart = view.findViewById(R.id.FRS_service_calls_view).findViewById(R.id.SCV_pie_chart);
        mCycleTime = view.findViewById(R.id.FRS_cycle_time_view).findViewById(R.id.CTV_cycle_time_chart);
        initTopFiveStopsVars(view);
        initTopFiveRejects(view);
        getTopRejectsAndStops();
    }

    private void initTopFiveStopsVars(View view) {
        mTopFiveStopsView = view.findViewById(R.id.fragment_dashboard_top_five_1);
        ((TextView) mTopFiveStopsView.findViewById(R.id.TF_tv)).setText(getString(R.string.top_five_stop_events));
        mTopStops_rv = mTopFiveStopsView.findViewById(R.id.top_five_recyclerView);
        mTopStops_rv.setLayoutManager(new LinearLayoutManager(getActivity()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        mStopsAdapter = new TopFiveAdapter(getActivity(), new ArrayList<TopFiveItem>(), TopFiveAdapter.TYPE_STOP);
        mTopStops_rv.setAdapter(mStopsAdapter);
    }

    private void initTopFiveRejects(View view) {
        mTopFiveRejectsView = view.findViewById(R.id.fragment_dashboard_top_five_2);
        ((TextView) mTopFiveRejectsView.findViewById(R.id.TF_tv)).setText(getString(R.string.top_five_reject_reasons));
        mTopRejects_rv = mTopFiveRejectsView.findViewById(R.id.top_five_recyclerView);
        mTopRejects_rv.setLayoutManager(new LinearLayoutManager(getActivity()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        mRejectsAdapter = new TopFiveAdapter(getActivity(), new ArrayList<TopFiveItem>(), TopFiveAdapter.TYPE_REJECT);
        mTopRejects_rv.setAdapter(mRejectsAdapter);
    }

    private void initCritical(View viewItem, String value, String events, String valueTitle, String eventsTile) {

        if ((value != null && !value.isEmpty())
                || events != null && !events.isEmpty()) {
            viewItem.findViewById(R.id.TF_values_ly).setVisibility(View.VISIBLE);
            if (value != null && !value.isEmpty()) {
                viewItem.findViewById(R.id.TF_values).setVisibility(View.VISIBLE);
                TextView textView = viewItem.findViewById(R.id.TF_values_value);
                textView.setText(value);
                ((TextView) viewItem.findViewById(R.id.TF_values_title)).setText(valueTitle);
            } else {
                viewItem.findViewById(R.id.TF_values).setVisibility(View.INVISIBLE);
            }
            if (events != null && !events.isEmpty()) {
                viewItem.findViewById(R.id.TF_events).setVisibility(View.VISIBLE);
                TextView textView = viewItem.findViewById(R.id.TF_events_value);
                textView.setText(value);
                ((TextView) viewItem.findViewById(R.id.TF_events_title)).setText(eventsTile);
            } else {
                viewItem.findViewById(R.id.TF_events).setVisibility(View.INVISIBLE);
            }
        } else {
            viewItem.findViewById(R.id.TF_values_ly).setVisibility(View.GONE);
        }

    }

    private void initServiceCallsView(ServiceCallsResponse serviceCallsResponse) {
        mPieChart.setDrawHoleEnabled(false);

        List<NotificationByType> values = serviceCallsResponse.getNotificationByType();
        if (values.size() > 0) {
            ArrayList<PieEntry> mPieValues = new ArrayList<>();
            for (int i = 0; i < values.size(); i++) {
                NotificationByType notificationByType = values.get(i);
                mPieValues.add(new PieEntry(notificationByType.getNumOfResponse(), notificationByType.getEName()));
            }

            mPieChart.setEntryLabelColor(Color.BLACK);

            PieDataSet pieDataSet = new PieDataSet(mPieValues, "");

            ArrayList<Integer> colors = new ArrayList<>();
            for (int c : ColorTemplate.JOYFUL_COLORS)
                colors.add(c);
            pieDataSet.setColors(colors);
            pieDataSet.setSliceSpace(4f);

            PieData data = new PieData(pieDataSet);

//            data.setValueFormatter(new PercentFormatter(mPieChart));
            data.setValueTextSize(11f);
            data.setValueTextColor(Color.BLACK);

            mPieChart.setData(data);
            mPieChart.setUsePercentValues(true);
//            mPieChart.getDescription().setEnabled(false);
            mPieChart.getLegend().setWordWrapEnabled(true);
            mPieChart.setDrawEntryLabels(false);
            mPieChart.setExtraOffsets(0, 20, 0, 20);

            mPieChart.invalidate();

            pieDataSet.setValueLinePart1OffsetPercentage(80.f);
            pieDataSet.setValueLinePart1Length(0.2f);
            pieDataSet.setValueLinePart2Length(0.4f);
            pieDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        }
    }


    private void initCycleTime(DepartmentShiftGraphResponse departmentShiftGraphResponse) {
//        int xValuesIncreaseIndex = 0;
//        ArrayList<Entry> tenHoursValues = new ArrayList<>();
//        ArrayList<Entry> fourHoursValues = new ArrayList<>();
//        ArrayList<ArrayList<Entry>> fourHoursList = new ArrayList<>();
//        final ArrayList<ArrayList<Entry>> tenHoursList = new ArrayList<>();
//        float midnightLimit = 0;
//        if (departmentShiftGraphResponse.getDepartments() != null && departmentShiftGraphResponse.getGraphColors().size() > 0) {
//            Collections.sort(departmentShiftGraphResponse.getGraphColors());
//            final String[] xValues = new String[departmentShiftGraphResponse.getGraphColors().size() + 1];
//            for (int i = 0; i < departmentShiftGraphResponse.getDepartments().size(); i++) {
//                Department department = departmentShiftGraphResponse.getDepartments().get(i);
//
//                xValues[i + xValuesIncreaseIndex] = TimeUtils.getDateForChart(department.getTime());
//                Entry entry;
//                try {
//                    entry = new Entry(i, department.getValue());
//                } catch (Exception e) {
//                    entry = null;
//                }
//                if (entry == null) {
//                    if (fourHoursValues.size() > 0) {
//                        fourHoursList.add(fourHoursValues);
//                    }
//                    fourHoursValues = new ArrayList<>();
//                } else {
//                    if (TimeUtils.getLongFromDateString(department.getTime(), "dd/MM/yyyy HH:mm:ss") > (TimeUtils.getLongFromDateString(widget.getMachineParamHistoricData().get(widget.getMachineParamHistoricData().size() - 1).getTime(), "dd/MM/yyyy HH:mm:ss") - FOUR_HOURS)) {
//                        fourHoursValues.add(entry);
//                    }
//                }
//            }
//            if (fourHoursValues.size() > 0) {
//                fourHoursList.add(fourHoursValues);
//                tenHoursList.add(tenHoursValues);
//            }
//            mCycleTime.setData(fourHoursList, xValues, widget.getLowLimit(), widget.getHighLimit());
//            mCycleTime.setLimitLines(widget.getLowLimit(), widget.getHighLimit(), widget.getStandardValue(), midnightLimit);
//        }
    }

    @Override
    public void onDeviceStatusChanged(MachineStatus machineStatus) {

    }

    @Override
    public void onMachineDataReceived(ArrayList<Widget> widgetList) {
        getTopRejectsAndStops();
    }

    @Override
    public void onShiftLogDataReceived(ArrayList<Event> events, ActualBarExtraResponse actualBarExtraResponse, MachineJoshDataResponse mMachineJoshDataResponse) {

    }

    @Override
    public void onTimerChanged(String timeToEndInHours) {

    }

    @Override
    public void onDataFailure(ErrorObjectInterface reason, CallType callType) {

    }

    @Override
    public void onApproveFirstItemEnabledChanged(boolean enabled) {

    }

    @Override
    public void onActiveJobsListForMachineUICallbackListener(ActiveJobsListForMachine mActiveJobsListForMachine) {

    }

    private void getTopRejectsAndStops() {

        PersistenceManager pm = PersistenceManager.getInstance();
        String[] machineId = {String.valueOf(pm.getMachineId())};
        GetTopRejectsAndEventsRequest request = new GetTopRejectsAndEventsRequest(machineId, pm.getSessionId(), pm.getShiftStart(), pm.getShiftEnd());

        NetworkManager.getInstance().getTopRejects(request, new Callback<TopRejectResponse>() {
            @Override
            public void onResponse(Call<TopRejectResponse> call, Response<TopRejectResponse> response) {
                if (isAdded() && response.body() != null && response.body().getmError() == null && response.body().getmRejectsList() != null) {
                    ((TopFiveAdapter) mTopRejects_rv.getAdapter()).setmTopList(response.body().getRejectsAsTopFive());
                    mProgressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<TopRejectResponse> call, Throwable t) {
                mProgressBar.setVisibility(View.GONE);

            }
        });


        NetworkManager.getInstance().getTopStopAndCriticalEvents(request, new Callback<StopAndCriticalEventsResponse>() {
            @Override
            public void onResponse(Call<StopAndCriticalEventsResponse> call, Response<StopAndCriticalEventsResponse> response) {
                if (response.body() != null && response.body().getmError() == null) {

                    if (response.body().getmStopEvents() != null && response.body().getmStopEvents().size() > 0) {
                        ((TopFiveAdapter) mTopStops_rv.getAdapter()).setmTopList(response.body().getStopsAsTopFive());
                    }

                    if (isAdded() && response.body().getmCriticalEvents() != null && response.body().getmCriticalEvents().size() > 0) {
                        initCritical(mTopFiveStopsView, response.body().getmCriticalEvents().get(0).getmDuration(),
                                response.body().getmCriticalEvents().get(0).getmEventsCount(),
                                getContext().getString(R.string.minutes_long), getContext().getString(R.string.events));
                        if (response.body().getmCriticalEvents().size() > 1) {
                            initCritical(mTopFiveRejectsView, response.body().getmCriticalEvents().get(1).getmDuration(),
                                    response.body().getmCriticalEvents().get(1).getmEventsCount(),
                                    getContext().getString(R.string.rejects).toLowerCase(), getContext().getString(R.string.events));
                        }
                    }
                }
                mProgressBar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<StopAndCriticalEventsResponse> call, Throwable t) {
                mProgressBar.setVisibility(View.GONE);

            }
        });

        BaseTimeRequest baseTimeRequest = new BaseTimeRequest(pm.getSessionId(), pm.getShiftStart(), pm.getShiftEnd());

        NetworkManager.getInstance().getServiceCalls(baseTimeRequest, new Callback<ServiceCallsResponse>() {
            @Override
            public void onResponse(Call<ServiceCallsResponse> call, Response<ServiceCallsResponse> response) {
                if (isAdded() && response.body() != null && response.body().getError() == null) {

                    if (response.body().getNotificationByType() != null && response.body().getNotificationByType().size() > 0) {
                        initServiceCallsView(response.body());
                    }

                }
                mServiceCallsResponse = new ServiceCallsResponse();
                ArrayList<NotificationByType> notificationByTypes = new ArrayList<>();
                notificationByTypes.add(new NotificationByType("a", "b", 2, 3, 3, 4));
                notificationByTypes.add(new NotificationByType("a", "b", 2, 3, 3, 4));
                notificationByTypes.add(new NotificationByType("a", "b", 2, 3, 3, 4));
                mServiceCallsResponse.setNotificationByType(notificationByTypes);
                mProgressBar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<ServiceCallsResponse> call, Throwable t) {
                mProgressBar.setVisibility(View.GONE);

            }
        });

        List<Integer> machineIds = new ArrayList<>();
        machineIds.add(pm.getMachineId());
        ReqDepartment reqDepartment = new ReqDepartment(pm.getDepartmentId(), machineIds, new ShiftByTime(pm.getShiftStart(), pm.getShiftEnd()));
        ArrayList<ReqDepartment> reqDepartments = new ArrayList<>();
        reqDepartments.add(reqDepartment);
        DepartmentShiftGraphRequest departmentShiftGraphRequest = new DepartmentShiftGraphRequest(pm.getSessionId(), reqDepartments, 0);
//        Log.d(TAG, "getTopRejectsAndStops: " + GsonHelper.toJson(departmentShiftGraphRequest));
        NetworkManager.getInstance().getDepartmentShiftGraph(departmentShiftGraphRequest, new Callback<DepartmentShiftGraphResponse>() {
            @Override
            public void onResponse(Call<DepartmentShiftGraphResponse> call, Response<DepartmentShiftGraphResponse> response) {
                if (response.body() != null && response.body().getError() == null) {

                    if (response.body().getDepartments() != null && response.body().getDepartments().size() > 0) {
                        initCycleTime(response.body());
                    }

                }
                mDepartmentShiftGraphResponse = new DepartmentShiftGraphResponse();
                ArrayList<GraphColor> graphColors = new ArrayList<>();
                GraphColor graphColor = new GraphColor("blue", "a", "#007cff", 0);
                GraphColor graphColor2 = new GraphColor("red", "a", "#d0021b", 0);
                graphColors.add(graphColor);
                graphColors.add(graphColor2);
                mDepartmentShiftGraphResponse.setGraphColors(graphColors);
                ArrayList<Department> departments = new ArrayList<>();
//                Department department = new Department();
//                departmentShiftGraphResponse.setDepartments();
                mProgressBar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<DepartmentShiftGraphResponse> call, Throwable t) {
                mProgressBar.setVisibility(View.GONE);

            }
        });
    }


    public void setIsOpenState(boolean open) {
        isOpen = open;
        mStopsAdapter.notifyDataSetChanged();
        mRejectsAdapter.notifyDataSetChanged();
    }
}
