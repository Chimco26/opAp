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
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.common.Event;
import com.example.common.actualBarExtraResponse.ActualBarExtraResponse;
import com.example.common.callback.ErrorObjectInterface;
import com.example.common.machineJoshDataResponse.MachineJoshDataResponse;
import com.example.common.reportShift.DepartmentShiftGraphRequest;
import com.example.common.reportShift.DepartmentShiftGraphResponse;
import com.example.common.reportShift.Graph;
import com.example.common.reportShift.GraphSeries;
import com.example.common.reportShift.Item;
import com.example.common.reportShift.NotificationByType;
import com.example.common.reportShift.ReqDepartment;
import com.example.common.reportShift.ServiceCallsResponse;
import com.example.common.reportShift.ShiftByTime;
import com.example.common.request.BaseTimeRequest;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.operators.activejobslistformachineinfra.ActiveJobsListForMachine;
import com.operators.machinedatainfra.models.Widget;
import com.operators.machinestatusinfra.models.MachineStatus;
import com.operatorsapp.R;
import com.operatorsapp.adapters.GrapheSeriesSpinnerAdapter;
import com.operatorsapp.adapters.TopFiveAdapter;
import com.operatorsapp.interfaces.DashboardUICallbackListener;
import com.operatorsapp.interfaces.OnActivityCallbackRegistered;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.model.TopFiveItem;
import com.operatorsapp.server.NetworkManager;
import com.operatorsapp.server.requests.GetTopRejectsAndEventsRequest;
import com.operatorsapp.server.responses.StopAndCriticalEventsResponse;
import com.operatorsapp.server.responses.TopRejectResponse;
import com.operatorsapp.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.operatorsapp.utils.LineChartHelper.setLimitLine;
import static com.operatorsapp.utils.LineChartHelper.setXAxisStyle;
import static com.operatorsapp.utils.LineChartHelper.setYAxisStyle;
import static com.operatorsapp.utils.LineChartHelper.splitItemsByNull;

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
    private LineChart mCycleTime;
    private TextView mCycleTimeTitle;
    private TextView mServiceCallsTitle;
    private Spinner mCycleTimeSpinner;
    private int mGraphPosition;
    private GrapheSeriesSpinnerAdapter mCycleTimeSpinnerAdapter;

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
        ((TextView) view.findViewById(R.id.FRS_title_tv)).setText(getResources().getString(R.string.shift_report));
        ((TextView) view.findViewById(R.id.FRS_machine_name_tv)).setText(PersistenceManager.getInstance().getMachineName());
        initServiceCallsVars(view);
        initCycleTimeVars(view);
        initTopFiveStopsVars(view);
        initTopFiveRejects(view);
        getData();
    }

    private void initCycleTimeVars(View view) {
        mCycleTime = view.findViewById(R.id.FRS_cycle_time_view).findViewById(R.id.CTV_cycle_time_chart);
        mCycleTimeTitle = view.findViewById(R.id.FRS_cycle_time_view).findViewById(R.id.CTV_tv);
        mCycleTimeSpinner = view.findViewById(R.id.FRS_cycle_time_view).findViewById(R.id.CTV_spinner);
    }

    private void initCycleTimeSpinner(final ArrayList<Graph> graphs) {
        if (mCycleTimeSpinnerAdapter == null) {
            mCycleTimeSpinnerAdapter = new GrapheSeriesSpinnerAdapter(getActivity(), R.layout.spinner_language_item, graphs, graphs.get(mGraphPosition).getDisplayName());
            mCycleTimeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mCycleTimeSpinner.setAdapter(mCycleTimeSpinnerAdapter);
            mCycleTimeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                    mGraphPosition = position;
                    setGraphData(null, graphs.get(mGraphPosition).getGraphSeries().get(0));
                    mCycleTimeSpinnerAdapter.setTitle(graphs.get(mGraphPosition).getDisplayName());
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }

            });
        }
    }

    private void initServiceCallsVars(View view) {
        mPieChart = view.findViewById(R.id.FRS_service_calls_view).findViewById(R.id.SCV_pie_chart);
        mServiceCallsTitle = view.findViewById(R.id.FRS_service_calls_view).findViewById(R.id.SCV_tv);
    }

    private void initTopFiveStopsVars(View view) {
        mTopFiveStopsView = view.findViewById(R.id.fragment_dashboard_top_five_1);
        ((TextView) mTopFiveStopsView.findViewById(R.id.TF_tv)).setText(getString(R.string.top_five_stop_events));
        mTopStops_rv = mTopFiveStopsView.findViewById(R.id.top_five_recyclerView);
        mTopStops_rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        mStopsAdapter = new TopFiveAdapter(getActivity(), new ArrayList<TopFiveItem>(), TopFiveAdapter.TYPE_STOP);
        mTopStops_rv.setAdapter(mStopsAdapter);
    }

    private void initTopFiveRejects(View view) {
        mTopFiveRejectsView = view.findViewById(R.id.fragment_dashboard_top_five_2);
        ((TextView) mTopFiveRejectsView.findViewById(R.id.TF_tv)).setText(getString(R.string.top_five_reject_reasons));
        mTopRejects_rv = mTopFiveRejectsView.findViewById(R.id.top_five_recyclerView);
        mTopRejects_rv.setLayoutManager(new LinearLayoutManager(getActivity()));
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
                textView.setText(events);
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
                mPieValues.add(new PieEntry(notificationByType.getPC(), notificationByType.getEName()));//todo lname
            }

            mPieChart.setEntryLabelColor(Color.BLACK);

            PieDataSet pieDataSet = new PieDataSet(mPieValues, "");

            ArrayList<Integer> colors = new ArrayList<>();
            for (int c : ColorTemplate.JOYFUL_COLORS)
                colors.add(c);
            pieDataSet.setColors(colors);
            pieDataSet.setSliceSpace(4f);

            PieData data = new PieData(pieDataSet);

            data.setValueTextSize(20f);
            data.setValueTextColor(Color.BLACK);

            mPieChart.setData(data);
            mPieChart.setDescription(null);
            mPieChart.getLegend().setEnabled(false);
            mPieChart.setUsePercentValues(true);
            mPieChart.getLegend().setWordWrapEnabled(true);
            mPieChart.setDrawEntryLabels(false);
            mPieChart.setExtraOffsets(0, 5, 0, 5);

            pieDataSet.setValueLinePart1OffsetPercentage(80.f);
            pieDataSet.setValueLinePart1Length(0.5f);
            pieDataSet.setValueLinePart2Length(0.5f);
            pieDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
            pieDataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

            mPieChart.invalidate();
        }
    }

    private void setGraphData(String displayName, GraphSeries graphSeries) {

        LineChart graph = mCycleTime;

        graph.setBackgroundColor(Color.TRANSPARENT);
        graph.setTouchEnabled(false);
        graph.setDrawGridBackground(false);

        ArrayList<ILineDataSet> allDataSets = new ArrayList<>();

        ArrayList<List<Item>> listArrayList = splitItemsByNull(graphSeries.getItems());

        if (listArrayList.size() > 0) {

            for (List<Item> items : listArrayList) {

                if (items.size() > 0) {

                    ArrayList<Entry> values = new ArrayList<>();

                    for (int i = 0; i < items.size(); i++) {

                        Date date = TimeUtils.getDateForNotification(items.get(i).getX());

                        if (date != null) {

                            values.add(new Entry((float) date.getTime(), items.get(i).getY().floatValue()));
                        }


                        LineDataSet set = new LineDataSet(values, "");

                        set.setDrawCircles(false);

                        set.setDrawValues(false);

                        set.setMode(LineDataSet.Mode.LINEAR);
//                        graphSeries.setMinValue(1039700f);
//                        graphSeries.setMaxValue(1039900f);
                        if ((graphSeries.getMinValue() > 0 && items.get(i).getY().floatValue() < graphSeries.getMinValue()) ||
                                (graphSeries.getMaxValue() > 0 && items.get(i).getY().floatValue() > graphSeries.getMaxValue())) {
                            set.setColor(getContext().getResources().getColor(R.color.red_line));
                        } else {
                            set.setColor(getContext().getResources().getColor(R.color.blue1));
                        }

                        allDataSets.add(set);
                    }
                }
            }


        }
        LineData data = new LineData(allDataSets);

        graph.setDescription(null);
        graph.setExtraOffsets(5, 5, 5, 5);
        graph.setData(data);
        graph.getLegend().setEnabled(false);
        setXAxisStyle(graph);
        setYAxisStyle(graph);
        setLimitLine(graph, graphSeries.getMinValue(), graphSeries.getMaxValue());
        graph.notifyDataSetChanged();
        graph.invalidate();
    }

    @Override
    public void onDeviceStatusChanged(MachineStatus machineStatus) {

    }

    @Override
    public void onMachineDataReceived(ArrayList<Widget> widgetList) {
        getData();
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

    private void getData() {

        PersistenceManager pm = PersistenceManager.getInstance();
        String[] machineId = {String.valueOf(pm.getMachineId())};
        GetTopRejectsAndEventsRequest request = new GetTopRejectsAndEventsRequest(machineId, pm.getSessionId(), pm.getShiftStart(), TimeUtils.getDateFromFormat(new Date(), TimeUtils.SQL_NO_T_FORMAT));
//        GetTopRejectsAndEventsRequest request = new GetTopRejectsAndEventsRequest(machineId, pm.getSessionId(),"2019-05-06 07:00:46", "2019-06-06 15:47:59");

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

        BaseTimeRequest baseTimeRequest = new BaseTimeRequest(pm.getSessionId(), pm.getShiftStart(), pm.getShiftEnd(), pm.getMachineId());
//        BaseTimeRequest baseTimeRequest = new BaseTimeRequest(pm.getSessionId(), "2019-05-06 07:00:46", "2019-06-06 15:47:59", pm.getMachineId());

        NetworkManager.getInstance().getServiceCalls(baseTimeRequest, new Callback<ServiceCallsResponse>() {
            @Override
            public void onResponse(Call<ServiceCallsResponse> call, Response<ServiceCallsResponse> response) {
                if (isAdded() && response.body() != null && response.body().getError() == null) {

                    if (response.body().getNotificationByType() != null && response.body().getNotificationByType().size() > 0) {
                        mServiceCallsTitle.setText(getContext().getResources().getString(R.string.service_calls_distribution));
                        initServiceCallsView(response.body());
                    }

                }
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
//        ReqDepartment reqDepartment = new ReqDepartment(pm.getDepartmentId(), machineIds, new ShiftByTime("2018-05-10 07:00:46", "2019-06-10 15:47:59"));
        ArrayList<ReqDepartment> reqDepartments = new ArrayList<>();
        reqDepartments.add(reqDepartment);
        DepartmentShiftGraphRequest departmentShiftGraphRequest = new DepartmentShiftGraphRequest(pm.getSessionId(), reqDepartments, 0);
//        Log.d(TAG, "getData: " + GsonHelper.toJson(departmentShiftGraphRequest));
        NetworkManager.getInstance().getDepartmentShiftGraph(departmentShiftGraphRequest, new Callback<DepartmentShiftGraphResponse>() {
            @Override
            public void onResponse(Call<DepartmentShiftGraphResponse> call, Response<DepartmentShiftGraphResponse> response) {
                if (isAdded() && response.body() != null && response.body().getError() == null) {

                    if (response.body().getDepartments() != null && response.body().getDepartments().size() > 0) {
                        setGraphData(null, response.body().getDepartments().get(0).getCurrentShift().
                                get(0).getMachines().get(0).getGraphs().get(mGraphPosition).getGraphSeries().get(0));
                        initCycleTimeSpinner((ArrayList<Graph>) response.body().getDepartments().get(0).getCurrentShift().
                                get(0).getMachines().get(0).getGraphs());
                        mCycleTimeTitle.setText(getContext().getResources().getString(R.string.cycle_time));
                    }

                }

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
