package com.operatorsapp.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.common.Event;
import com.example.common.SelectableString;
import com.example.common.StandardResponse;
import com.example.common.actualBarExtraResponse.ActualBarExtraResponse;
import com.example.common.machineJoshDataResponse.MachineJoshDataResponse;
import com.example.common.permissions.WidgetInfo;
import com.example.common.reportShift.DepartmentShiftGraphRequest;
import com.example.common.reportShift.DepartmentShiftGraphResponse;
import com.example.common.reportShift.Graph;
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
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.operators.activejobslistformachineinfra.ActiveJobsListForMachine;
import com.operators.machinedatainfra.models.Widget;
import com.operators.machinestatusinfra.models.MachineStatus;
import com.operatorsapp.R;
import com.operatorsapp.adapters.CheckBoxFilterAdapter;
import com.operatorsapp.adapters.TopFiveAdapter;
import com.operatorsapp.application.OperatorApplication;
import com.operatorsapp.dialogs.ShiftGraphYAxisValueSetterDialog;
import com.operatorsapp.interfaces.DashboardUICallbackListener;
import com.operatorsapp.interfaces.OnActivityCallbackRegistered;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.model.TopFiveItem;
import com.operatorsapp.server.NetworkManager;
import com.operatorsapp.server.requests.GetTopRejectsAndEventsRequest;
import com.operatorsapp.server.responses.StopAndCriticalEventsResponse;
import com.operatorsapp.server.responses.TopRejectResponse;
import com.operatorsapp.utils.GoogleAnalyticsHelper;
import com.operatorsapp.utils.TimeUtils;

import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.common.SelectableString.STANDARD_VARIATION_ID;
import static com.example.common.reportShift.GraphSeries.addAveragedPoints;
import static com.operatorsapp.utils.LineChartHelper.setLimitLine;
import static com.operatorsapp.utils.LineChartHelper.setXAxisStyle;
import static com.operatorsapp.utils.LineChartHelper.setYAxisStyle;
import static com.operatorsapp.utils.LineChartHelper.splitItemsByNull;
import static com.operatorsapp.utils.TimeUtils.ONE_MINUTE_IN_SECONDS;

public class ReportShiftFragment extends Fragment implements DashboardUICallbackListener {

    public static final String TAG = ReportShiftFragment.class.getSimpleName();
    private static final String IS_TIME_LINE_OPEN = "IS_TIME_LINE_OPEN";
    private static final String SHOW_LIMITS = "SHOW_LIMITS";
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
    private TextView mServiceCallsTitle;
    private int mGraphPosition;
    private CheckBoxFilterAdapter mCheckBoxFilterAdapter;
    public static final int[] BLUE_COLORS = {
            Color.rgb(0, 0, 153), Color.rgb(0, 0, 204), Color.rgb(0, 0, 255),
            Color.rgb(51, 51, 255), Color.rgb(0, 102, 255), Color.rgb(51, 102, 255)};
    private RecyclerView mGraphRv;
    private ArrayList<Graph> mGraphs;
    private ArrayList<SelectableString> mSelectableStrings;
    private TextView mTitle;
    private WeakReference<DashboardUICallbackListener> mUiCallbackListener;

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
        mUiCallbackListener = new WeakReference<DashboardUICallbackListener>((DashboardUICallbackListener)this);
        mOnActivityCallbackRegistered.onFragmentAttached(mUiCallbackListener);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mOnActivityCallbackRegistered.onFragmentDetached(mUiCallbackListener);
        mUiCallbackListener = null;
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
        mTitle = view.findViewById(R.id.FRS_machine_name_tv);
        initServiceCallsVars(view);
        initCycleTimeVars(view);
        initTopFiveStopsVars(view);
        initTopFiveRejects(view);
        getData();
        view.findViewById(R.id.FRS_close_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() != null) {
                    getActivity().onBackPressed();
                }
            }
        });
        new GoogleAnalyticsHelper().trackScreen(getActivity(), "Shift Report");
    }

    private void initCycleTimeVars(View view) {
        mCycleTime = view.findViewById(R.id.FRS_cycle_time_view).findViewById(R.id.CTV_cycle_time_chart);
        mGraphRv = view.findViewById(R.id.FRS_cycle_time_view).findViewById(R.id.CTV_filter_rv);
        view.findViewById(R.id.FRS_cycle_time_view).findViewById(R.id.CTV_custom_y_axis_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ShiftGraphYAxisValueSetterDialog(new ShiftGraphYAxisValueSetterDialog.ShiftGraphYAxisValueSetterDialogListener() {
                    @Override
                    public void onSaveYAxisValues() {
                        setGraphData(mSelectableStrings, mGraphs);
                    }
                }).showDialog(getActivity()).show();
            }
        });
    }

    private void initGraphFilterRv(final ArrayList<Graph> graphs, final ArrayList<SelectableString> selectableStrings) {
        if (mCheckBoxFilterAdapter == null) {
            mCheckBoxFilterAdapter = new CheckBoxFilterAdapter(selectableStrings, new CheckBoxFilterAdapter.CheckBoxFilterAdapterListener() {
                @Override
                public void onItemCheck(SelectableString selectableString) {
                    if (selectableString.getId().equals(SelectableString.SELECT_ALL_ID)) {
                        for (SelectableString selectableString1 : selectableStrings) {
                            selectableString1.setSelected(selectableString.isSelected());
                        }
                    }
                    PersistenceManager.getInstance().setShiftGraphCategories(selectableStrings);
                    mGraphRv.post(new Runnable() {
                        @Override
                        public void run() {
                            mCheckBoxFilterAdapter.notifyDataSetChanged();
                        }
                    });

                    setGraphData(selectableStrings, graphs);
                }
            }, true, false);
        } else {
            mCheckBoxFilterAdapter.notifyDataSetChanged();
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        mGraphRv.setLayoutManager(layoutManager);

        mGraphRv.setAdapter(mCheckBoxFilterAdapter);
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
        mStopsAdapter = new TopFiveAdapter(new ArrayList<TopFiveItem>(), TopFiveAdapter.TYPE_STOP);
        mTopStops_rv.setAdapter(mStopsAdapter);
    }

    private void initTopFiveRejects(View view) {
        mTopFiveRejectsView = view.findViewById(R.id.fragment_dashboard_top_five_2);
        ((TextView) mTopFiveRejectsView.findViewById(R.id.TF_tv)).setText(getString(R.string.top_five_reject_reasons));
        mTopRejects_rv = mTopFiveRejectsView.findViewById(R.id.top_five_recyclerView);
        mTopRejects_rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRejectsAdapter = new TopFiveAdapter(new ArrayList<TopFiveItem>(), TopFiveAdapter.TYPE_REJECT);
        mTopRejects_rv.setAdapter(mRejectsAdapter);
    }

    private void initCritical(ViewGroup view, String name, String value, String percentage, String events, String valueTitle, String eventsTile) {

        LayoutInflater inflater = getLayoutInflater();
        View childLayout = inflater.inflate(R.layout.critical_layout, null);

        if ((value != null && !value.isEmpty())
                || events != null && !events.isEmpty()
                || percentage != null && !percentage.isEmpty()) {
            view.addView(childLayout);
            if (name != null && !name.isEmpty()) {
                childLayout.findViewById(R.id.TF_name).setVisibility(View.VISIBLE);
                ((TextView) childLayout.findViewById(R.id.TF_name)).setText(name);
            } else {
                childLayout.findViewById(R.id.TF_name).setVisibility(View.GONE);
            }
            if (percentage != null && !percentage.isEmpty()) {
                childLayout.findViewById(R.id.TF_percentage_ly).setVisibility(View.VISIBLE);
                ((TextView) childLayout.findViewById(R.id.TF_percentage)).setText(percentage);
            } else {
                childLayout.findViewById(R.id.TF_percentage_ly).setVisibility(View.GONE);
            }
            if (value != null && !value.isEmpty()) {
                childLayout.findViewById(R.id.TF_values).setVisibility(View.VISIBLE);
                TextView textView = childLayout.findViewById(R.id.TF_values_value);
                textView.setText(value);
                ((TextView) childLayout.findViewById(R.id.TF_values_title)).setText(valueTitle);
            } else {
                childLayout.findViewById(R.id.TF_values).setVisibility(View.GONE);
            }
            if (events != null && !events.isEmpty()) {
                childLayout.findViewById(R.id.TF_events).setVisibility(View.VISIBLE);
                TextView textView = childLayout.findViewById(R.id.TF_events_value);
                textView.setText(events);
                ((TextView) childLayout.findViewById(R.id.TF_events_title)).setText(eventsTile);
            } else {
                childLayout.findViewById(R.id.TF_events).setVisibility(View.GONE);
            }
        } else {
            childLayout.findViewById(R.id.TF_values_ly).setVisibility(View.GONE);
        }

    }

    private void initServiceCallsView(ServiceCallsResponse serviceCallsResponse) {

        List<NotificationByType> values = serviceCallsResponse.getNotificationByType();
        if (values.size() > 0) {
            ArrayList<PieEntry> mPieValues = new ArrayList<>();
            for (int i = 0; i < values.size(); i++) {
                NotificationByType notificationByType = values.get(i);
                String titleByLang = OperatorApplication.isEnglishLang() ? notificationByType.getEName() : notificationByType.getLName();
                mPieValues.add(new PieEntry(notificationByType.getPC(), String.format(Locale.getDefault(), "%s (%d)", titleByLang, notificationByType.getNumOfResponse())));
            }
            mPieChart.setDrawHoleEnabled(false);
            mPieChart.setEntryLabelColor(Color.BLACK);

            PieDataSet pieDataSet = new PieDataSet(mPieValues, "");
            pieDataSet.setValueLinePart1OffsetPercentage(80.f);
            pieDataSet.setValueLinePart1Length(0.5f);
            pieDataSet.setValueLinePart2Length(0.5f);
            pieDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
            pieDataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

            ArrayList<Integer> colors = new ArrayList<>();
            for (int c : BLUE_COLORS) {
                colors.add(c);
            }
            pieDataSet.setColors(colors);
            pieDataSet.setSliceSpace(4f);

            PieData data = new PieData(pieDataSet);
            data.setValueFormatter(new PercentFormatter());
            data.setValueTextSize(20f);
            data.setValueTextColor(Color.BLACK);


            mPieChart.setUsePercentValues(true);
            mPieChart.getLegend().setEnabled(true);
            mPieChart.setDrawEntryLabels(false);
            mPieChart.getLegend().setWordWrapEnabled(true);
            mPieChart.setTouchEnabled(false);
            mPieChart.getDescription().setEnabled(false);
            mPieChart.getLegend().setTextSize(14f);
            mPieChart.setExtraOffsets(15, 5, 15, 5);

            mPieChart.setData(data);
            mPieChart.invalidate();

        }
    }

    public class FloatCouple {
        float a;
        float b;

        public FloatCouple(float a, float b) {
            this.a = a;
            this.b = b;
        }
    }

    private void setGraphData(ArrayList<SelectableString> selectableStrings, List<Graph> graphs) {

        LineChart graph = mCycleTime;
        graph.removeAllViews();
        graph.setBackgroundColor(Color.TRANSPARENT);
        graph.setTouchEnabled(false);
        graph.setDrawGridBackground(false);
        Float min = null;
        Float max = null;

        ArrayList<ILineDataSet> allDataSets = new ArrayList<>();
        ArrayList<FloatCouple> limits = new ArrayList<>();

        boolean showLimits = false;
        for (Graph graphItem : graphs) {
            boolean isSelected = false;
            boolean isStandardV = false;
            for (SelectableString selectableString : selectableStrings) {
                if (selectableString.getId().equals(graphItem.getId())) {
                    isSelected = selectableString.isSelected();
                }
                if (selectableString.getId().equals(STANDARD_VARIATION_ID)) {
                    isStandardV = selectableString.isSelected();
                }
                if (selectableString.getId().equals(SHOW_LIMITS)) {
                    showLimits = selectableString.isSelected();
                }
            }
            if (!isSelected) {
                continue;
            }
            limits.add(new FloatCouple(graphItem.getGraphSeries().get(0).getMinValue(), graphItem.getGraphSeries().get(0).getMaxValue()));

            ArrayList<List<Item>> listArrayList;
            if (isStandardV){
                listArrayList = splitItemsByNull(graphItem.getGraphSeries().get(0).getStandardItems());
            }else {
                listArrayList = splitItemsByNull(graphItem.getGraphSeries().get(0).getItems());
            }
            if (listArrayList.size() > 0) {

                for (List<Item> items : listArrayList) {

                    if (items.size() > 0) {

                        ArrayList<Entry> values = new ArrayList<>();

                        for (int i = 0; i < items.size(); i++) {

                            Date date = TimeUtils.getDateForNotification(items.get(i).getX());

                            if (date != null) {

                                float value = (float) (items.get(i).getY().floatValue());
                                values.add(new Entry((float) date.getTime(), value));
                                if (max == null || value > max){
                                    max = value;
                                }
                                if (min == null || value < min){
                                    min = value;
                                }

                            }


                        }
                        LineDataSet set = new LineDataSet(values, "");

                        set.setDrawCircles(false);

                        set.setDrawValues(false);

                        set.setMode(LineDataSet.Mode.LINEAR);

                        set.setColor(graphItem.getColor());

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
        graph.getAxisLeft().removeAllLimitLines();
        if (showLimits) {
            for (FloatCouple floatcouple : limits) {
                setLimitLine(graph, floatcouple.a, floatcouple.b);
                if (min == null || floatcouple.a < min){
                    min = floatcouple.a;
                }
                if (max == null || floatcouple.b > max){
                    max = floatcouple.b;
                }
            }
        }
        PersistenceManager persistenceManager = PersistenceManager.getInstance();
        if (persistenceManager.isShiftCustomY()) {
            graph.getAxisLeft().setAxisMinimum(persistenceManager.getShiftGraphMinY());
            graph.getAxisLeft().setAxisMaximum(persistenceManager.getShiftGraphMaxY());
        }else {
            graph.getAxisLeft().resetAxisMinimum();
            graph.getAxisLeft().resetAxisMaximum();
            if (min != null && min == 0){min = -0.1f;}
            if (max != null && max == 0){max = 0.1f;}
            if (min != null) {
                graph.getAxisLeft().setAxisMinimum(min - (max - min) / 10);
            }
            if (max != null) {
                graph.getAxisLeft().setAxisMaximum(max + (max - min) / 10);
            }
        }
        graph.notifyDataSetChanged();
        graph.invalidate();
    }



    @Override
    public void onPermissionForMachinePolling(SparseArray<WidgetInfo> permissionResponse) {
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
    public void onDataFailure(StandardResponse reason, CallType callType) {

    }

    @Override
    public void onApproveFirstItemEnabledChanged(boolean enabled) {

    }

    @Override
    public void onActiveJobsListForMachineUICallbackListener(ActiveJobsListForMachine mActiveJobsListForMachine) {

    }

    private void getData() {

        PersistenceManager pm = PersistenceManager.getInstance();
        mTitle.setText(pm.getMachineName());
        String shiftEnd = TimeUtils.getDate(TimeUtils.getLongFromDateString(pm.getShiftEnd(), TimeUtils.SQL_NO_T_FORMAT) + 5 * ONE_MINUTE_IN_SECONDS * 1000, TimeUtils.SQL_NO_T_FORMAT);
        String[] machineId = {String.valueOf(pm.getMachineId())};
        final GetTopRejectsAndEventsRequest request = new GetTopRejectsAndEventsRequest(machineId, pm.getSessionId(), pm.getShiftStart(), TimeUtils.getDateFromFormat(new Date(), TimeUtils.SQL_NO_T_FORMAT));
//        GetTopRejectsAndEventsRequest request = new GetTopRejectsAndEventsRequest(machineId, pm.getSessionId(),"2019-05-06 07:00:46", "2019-06-06 15:47:59");

        NetworkManager.getInstance().getTopRejects(request, new Callback<TopRejectResponse>() {
            @Override
            public void onResponse(Call<TopRejectResponse> call, Response<TopRejectResponse> response) {
                if (isAdded() && response.body() != null && response.body().getError().getErrorDesc() == null && response.body().getmRejectsList() != null) {
                    ((TopFiveAdapter) mTopRejects_rv.getAdapter()).setmTopList(response.body().getRejectsAsTopFive());
                    String goodUnits = "";
                    String rejectsPc = "";
                    String rejects = "";
                    if (response.body().getmGoodUnits() != null) {
                        goodUnits = new DecimalFormat("##.##").format(response.body().getmGoodUnits());
                    }
                    if (response.body().getmRejectsPC() != null) {
                        rejectsPc = String.format("%s%%", new DecimalFormat("##.##").format(response.body().getmRejectsPC()));
                    }
                    if (response.body().getmRejectedUnits() != null) {
                        rejects = new DecimalFormat("##.##").format(response.body().getmRejectedUnits());
                    }
                    ((LinearLayout) mTopFiveRejectsView.findViewById(R.id.TF_top_container)).removeAllViews();
                    initCritical((LinearLayout) mTopFiveRejectsView.findViewById(R.id.TF_top_container),
                            null,
                            goodUnits,
                            rejectsPc,
                            rejects,
                            getString(R.string.good_units),
                            getString(R.string.rejects).toLowerCase());
                }
                mProgressBar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<TopRejectResponse> call, Throwable t) {
                mProgressBar.setVisibility(View.GONE);

            }
        });


        NetworkManager.getInstance().getTopStopAndCriticalEvents(request, new Callback<StopAndCriticalEventsResponse>() {
            @Override
            public void onResponse(Call<StopAndCriticalEventsResponse> call, Response<StopAndCriticalEventsResponse> response) {
                if (response.body() != null && response.body().getError().getErrorDesc() == null) {

                    if (response.body().getmStopEvents() != null && response.body().getmStopEvents().size() > 0) {
                        ((TopFiveAdapter) mTopStops_rv.getAdapter()).setmTopList(response.body().getStopsAsTopFive());
                    }
                    ((LinearLayout) mTopFiveStopsView.findViewById(R.id.TF_top_container)).removeAllViews();
                    if (isAdded() && response.body().getmCriticalEvents() != null && response.body().getmCriticalEvents().size() > 0) {
                        initCritical((LinearLayout) mTopFiveStopsView.findViewById(R.id.TF_top_container), response.body().getmCriticalEvents().get(0).getmName(),
                                response.body().getmCriticalEvents().get(0).getmDuration(),
                                String.format("%s%%", response.body().getmCriticalEvents().get(0).getmPercentageDuration()),
                                response.body().getmCriticalEvents().get(0).getmEventsCount(),
                                getContext().getString(R.string.minutes_long), getContext().getString(R.string.events));
                    }
                    if (isAdded() && response.body().getmCriticalEvents() != null && response.body().getmCriticalEvents().size() > 1) {
                        initCritical((LinearLayout) mTopFiveStopsView.findViewById(R.id.TF_top_container), response.body().getmCriticalEvents().get(1).getmName(),
                                response.body().getmCriticalEvents().get(1).getmDuration(),
                                String.format("%s%%", response.body().getmCriticalEvents().get(1).getmPercentageDuration()),
                                response.body().getmCriticalEvents().get(1).getmEventsCount(),
                                getContext().getString(R.string.minutes_long), getContext().getString(R.string.events));
                    }

                }
                mProgressBar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<StopAndCriticalEventsResponse> call, Throwable t) {
                mProgressBar.setVisibility(View.GONE);

            }
        });

        List<Integer> machineIds = new ArrayList<>();
        machineIds.add(pm.getMachineId());
        ReqDepartment reqDepartment = new ReqDepartment(pm.getDepartmentId(), machineIds, new ShiftByTime(pm.getShiftStart(), shiftEnd));
//        ReqDepartment reqDepartment = new ReqDepartment(pm.getDepartmentId(), machineIds, new ShiftByTime("2018-05-10 07:00:46", "2019-06-10 15:47:59"));
        ArrayList<ReqDepartment> reqDepartments = new ArrayList<>();
        reqDepartments.add(reqDepartment);
        DepartmentShiftGraphRequest departmentShiftGraphRequest = new DepartmentShiftGraphRequest(pm.getSessionId(), reqDepartments, 0);
//        Log.d(TAG, "getData: " + GsonHelper.toJson(departmentShiftGraphRequest));
        NetworkManager.getInstance().getDepartmentShiftGraph(departmentShiftGraphRequest, new Callback<DepartmentShiftGraphResponse>() {
            @Override
            public void onResponse(Call<DepartmentShiftGraphResponse> call, Response<DepartmentShiftGraphResponse> response) {
                if (isAdded() && response.body() != null && response.body().getError().getErrorDesc() == null) {

                    if (response.body().getDepartments() != null && response.body().getDepartments().size() > 0
                            && response.body().getDepartments().get(0).getCurrentShift().size() > 0 && response.body().getDepartments().get(0).getCurrentShift().
                            get(0).getMachines().size() > 0 && response.body().getDepartments().get(0).getCurrentShift().
                            get(0).getMachines().get(0).getGraphs().size() > mGraphPosition) {
                        ArrayList<SelectableString> selectableStrings = new ArrayList<>();
                        selectableStrings.add(new SelectableString(getString(R.string.select_all), true, SelectableString.SELECT_ALL_ID, getActivity().getResources().getColor(R.color.black)));
                        selectableStrings.add(new SelectableString(getString(R.string.standard), true, STANDARD_VARIATION_ID, getActivity().getResources().getColor(R.color.grey_lite)));
                        selectableStrings.add(new SelectableString(getString(R.string.limits), true, SHOW_LIMITS, getActivity().getResources().getColor(R.color.green_light)));
                        if (response.body().getDepartments().get(0).getCurrentShift().
                                get(0).getMachines().get(0).getGraphs() != null) {
                            int[] colors = getActivity().getResources().getIntArray(R.array.colorList);
                            for (int i = 0; i < response.body().getDepartments().get(0).getCurrentShift().
                                    get(0).getMachines().get(0).getGraphs().size(); i++) {
                                Graph graph = response.body().getDepartments().get(0).getCurrentShift().
                                        get(0).getMachines().get(0).getGraphs().get(i);
                                graph.setId(graph.getName());
                                SelectableString selectableString = new SelectableString(graph.getDisplayName(), true, String.valueOf(graph.getId()));
                                if (colors.length > i) {
                                    graph.setColor(colors[i]);
                                    selectableString.setColor(colors[i]);
                                }
                                selectableStrings.add(selectableString);
                            }
                        }
                        if (mSelectableStrings == null) {
                            mSelectableStrings = selectableStrings;
                            boolean isSameCategories = SelectableString.setPrefCheck(mSelectableStrings, PersistenceManager.getInstance().getShiftGraphCategories());
                            if (!isSameCategories) {
                                PersistenceManager.getInstance().setShiftGraphCategories(new ArrayList<SelectableString>());
                            }
                        } else {
                            ArrayList<SelectableString> arrayList = SelectableString.updateList(mSelectableStrings, selectableStrings);
                            mSelectableStrings.clear();
                            mSelectableStrings.addAll(arrayList);
                        }
                        if (mGraphs == null) {
                            mGraphs = new ArrayList<>();
                        }
                        mGraphs.clear();
                        List<Graph> graphs = response.body().getDepartments().get(0).getCurrentShift().
                                get(0).getMachines().get(0).getGraphs();
                        addAveragedPoints(graphs);
                        mGraphs.addAll(graphs);
                        initGraphFilterRv(mGraphs, mSelectableStrings);
                        setGraphData(mSelectableStrings, mGraphs);
                    }

                }

            }

            @Override
            public void onFailure(Call<DepartmentShiftGraphResponse> call, Throwable t) {
                mProgressBar.setVisibility(View.GONE);

            }
        });


        BaseTimeRequest baseTimeRequest = new BaseTimeRequest(pm.getSessionId(), pm.getShiftStart(), shiftEnd, pm.getMachineId());
//        BaseTimeRequest baseTimeRequest = new BaseTimeRequest(pm.getSessionId(), "2019-05-06 07:00:46", "2019-06-06 15:47:59", pm.getMachineId());

        NetworkManager.getInstance().getServiceCalls(baseTimeRequest, new Callback<ServiceCallsResponse>() {
            @Override
            public void onResponse(Call<ServiceCallsResponse> call, Response<ServiceCallsResponse> response) {
                if (isAdded() && response.body() != null && response.body().getError().getErrorDesc() == null) {

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
    }

    private Float getTotalAmount(ArrayList<TopFiveItem> rejectsAsTopFive) {
        Float amount = 0f;
        for (TopFiveItem topFiveItem : rejectsAsTopFive) {
            amount += Float.valueOf(topFiveItem.getmAmount());
        }
        return amount;
    }


    public void setIsOpenState(boolean open) {
        isOpen = open;
        mStopsAdapter.notifyDataSetChanged();
        mRejectsAdapter.notifyDataSetChanged();
    }

}
