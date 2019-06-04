package com.operatorsapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.common.Event;
import com.example.common.actualBarExtraResponse.ActualBarExtraResponse;
import com.example.common.callback.ErrorObjectInterface;
import com.example.common.machineJoshDataResponse.MachineJoshDataResponse;
import com.example.common.reportShift.DepartmentShiftGraphRequest;
import com.example.common.reportShift.DepartmentShiftGraphResponse;
import com.example.common.reportShift.ReqDepartment;
import com.example.common.reportShift.ServiceCallsResponse;
import com.example.common.reportShift.ShiftByTime;
import com.example.common.request.BaseTimeRequest;
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
import com.operatorsapp.server.responses.CriticalEvent;
import com.operatorsapp.server.responses.StopAndCriticalEventsResponse;
import com.operatorsapp.server.responses.TopRejectResponse;

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
    TextView include1_title;
    TextView include2_title;
//    TextView row1_title;
    TextView include1_row1_stat1_num;
//    TextView include1_row1_stat2_num;
    TextView include1_row1_stat3_num;
    private LinearLayout row1_lil;
    private OnActivityCallbackRegistered mOnActivityCallbackRegistered;
    private View mProgressBar;
    private RecyclerView.Adapter mStopsAdapter;
    private TopFiveAdapter mRejectsAdapter;
    private boolean isOpen;
    private View include1_title_ly;
    private View include2_title_ly;

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
        if (getArguments() != null && getArguments().containsKey(IS_TIME_LINE_OPEN)){
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
        initTopFive(view);
        getTopRejectsAndStops();
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

    private void initTopFive(View view) {

        mProgressBar = view.findViewById(R.id.FTF_progress);

        View includeTopFive_1 = view.findViewById(R.id.fragment_dashboard_top_five_1);
        View includeTopFive_2 = view.findViewById(R.id.fragment_dashboard_top_five_2);

        row1_lil = view.findViewById(R.id.row1_top_five_lil);
        include1_title = includeTopFive_1.findViewById(R.id.title_top_five_tv);
        include1_title_ly = includeTopFive_1.findViewById(R.id.TP_title_ly);
        include2_title_ly = includeTopFive_2.findViewById(R.id.TP_title_ly);
        include2_title = includeTopFive_2.findViewById(R.id.title_top_five_tv);

        include1_row1_stat1_num = includeTopFive_1.findViewById(R.id.row1_title_stat1_top_five_number_tv);
        include1_row1_stat3_num = includeTopFive_1.findViewById(R.id.row1_title_stat3_top_five_number_tv);

        mTopStops_rv = includeTopFive_1.findViewById(R.id.top_five_recyclerView);
        mTopStops_rv.setLayoutManager(new LinearLayoutManager(getActivity()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        mStopsAdapter = new TopFiveAdapter(getActivity(), new ArrayList<TopFiveItem>(), TopFiveAdapter.TYPE_STOP);
        mTopStops_rv.setAdapter(mStopsAdapter);

        mTopRejects_rv = includeTopFive_2.findViewById(R.id.top_five_recyclerView);
        mTopRejects_rv.setLayoutManager(new LinearLayoutManager(getActivity()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        mRejectsAdapter = new TopFiveAdapter(getActivity(), new ArrayList<TopFiveItem>(), TopFiveAdapter.TYPE_REJECT);
        mTopRejects_rv.setAdapter(mRejectsAdapter);

        include1_title.setText(getString(R.string.top_five_stop_events));
        include2_title.setText(getString(R.string.top_five_reject_reasons));
    }

    private void getTopRejectsAndStops() {

        PersistenceManager pm = PersistenceManager.getInstance();
        String[] machineId = {String.valueOf(pm.getMachineId())};
        GetTopRejectsAndEventsRequest request = new GetTopRejectsAndEventsRequest(machineId, pm.getSessionId(), pm.getShiftStart(), pm.getShiftEnd());

        NetworkManager.getInstance().getTopRejects(request, new Callback<TopRejectResponse>() {
            @Override
            public void onResponse(Call<TopRejectResponse> call, Response<TopRejectResponse> response) {
                if (response.body() != null && response.body().getmError() == null && response.body().getmRejectsList() != null) {
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

                    if (response.body().getmCriticalEvents() != null && response.body().getmCriticalEvents().size() > 0) {
                        initCritical(response.body());
                    } else {
                        row1_lil.setVisibility(View.GONE);
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
                if (response.body() != null && response.body().getError() == null) {

//                    if (response.body().getmStopEvents() != null && response.body().getmStopEvents().size() > 0) {
//                        ((TopFiveAdapter) mTopStops_rv.getAdapter()).setmTopList(response.body().getStopsAsTopFive());
//                    }
//
//                    if (response.body().getmCriticalEvents() != null && response.body().getmCriticalEvents().size() > 0) {
//                        initCritical(response.body());
//                    } else {
//                        row1_lil.setVisibility(View.GONE);
//                    }
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
        ArrayList<ReqDepartment> reqDepartments = new ArrayList<>();
        reqDepartments.add(reqDepartment);
        DepartmentShiftGraphRequest departmentShiftGraphRequest = new DepartmentShiftGraphRequest(pm.getSessionId(), reqDepartments, 0);
//        Log.d(TAG, "getTopRejectsAndStops: " + GsonHelper.toJson(departmentShiftGraphRequest));
        NetworkManager.getInstance().getDepartmentShiftGraph(departmentShiftGraphRequest, new Callback<DepartmentShiftGraphResponse>() {
            @Override
            public void onResponse(Call<DepartmentShiftGraphResponse> call, Response<DepartmentShiftGraphResponse> response) {
                if (response.body() != null && response.body().getError() == null) {

//                    if (response.body().getmStopEvents() != null && response.body().getmStopEvents().size() > 0) {
//                        ((TopFiveAdapter) mTopStops_rv.getAdapter()).setmTopList(response.body().getStopsAsTopFive());
//                    }
//
//                    if (response.body().getmCriticalEvents() != null && response.body().getmCriticalEvents().size() > 0) {
//                        initCritical(response.body());
//                    } else {
//                        row1_lil.setVisibility(View.GONE);
//                    }
                }
                mProgressBar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<DepartmentShiftGraphResponse> call, Throwable t) {
                mProgressBar.setVisibility(View.GONE);

            }
        });
    }

    private void initCritical(StopAndCriticalEventsResponse response) {
        ArrayList<CriticalEvent> critList = response.getmCriticalEvents();

        if (critList != null && critList.size() >= 1) {
            CriticalEvent crit1 = critList.get(0);
            row1_lil.setVisibility(View.VISIBLE);
//            row1_title.setText(crit1.getmName());
            include1_row1_stat1_num.setText(crit1.getmDuration());
//            include1_row1_stat2_num.setText(String.format("%s%%", crit1.getmPercentageDuration()));
            include1_row1_stat3_num.setText(crit1.getmEventsCount());
        }else {
            include1_title_ly.setVisibility(View.GONE);
            include2_title_ly.setVisibility(View.GONE);
        }

    }

    public void setIsOpenState(boolean open) {
        isOpen = open;
        mStopsAdapter.notifyDataSetChanged();
        mRejectsAdapter.notifyDataSetChanged();
    }
}
