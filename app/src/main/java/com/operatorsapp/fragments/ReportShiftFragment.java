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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportShiftFragment extends Fragment implements DashboardUICallbackListener {

    public static final String TAG = ReportShiftFragment.class.getSimpleName();
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
    private LinearLayout row1_lil;
    private LinearLayout row2_lil;
    private OnActivityCallbackRegistered mOnActivityCallbackRegistered;
    private View mProgressBar;

    public static ReportShiftFragment newInstance() {
        ReportShiftFragment reportShiftFragment = new ReportShiftFragment();
        Bundle bundle = new Bundle();
        reportShiftFragment.setArguments(bundle);
        return reportShiftFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        return inflater.inflate(R.layout.frament_top_five, container, false);
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
        mTopStops_rv.setLayoutManager(new LinearLayoutManager(getActivity()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        mTopStops_rv.setAdapter(new TopFiveAdapter(getActivity(), new ArrayList<TopFiveItem>(), TopFiveAdapter.TYPE_STOP));

        mTopRejects_rv = includeTopFive_2.findViewById(R.id.top_five_recyclerView);
        mTopRejects_rv.setLayoutManager(new LinearLayoutManager(getActivity()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        mTopRejects_rv.setAdapter(new TopFiveAdapter(getActivity(), new ArrayList<TopFiveItem>(), TopFiveAdapter.TYPE_REJECT));

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
                        row2_lil.setVisibility(View.GONE);
                    }
                }
                mProgressBar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<StopAndCriticalEventsResponse> call, Throwable t) {
                mProgressBar.setVisibility(View.GONE);

            }
        });
    }

    private void initCritical(StopAndCriticalEventsResponse response) {
        ArrayList<CriticalEvent> critList = response.getmCriticalEvents();

        if (critList != null && critList.size() >= 1) {
            CriticalEvent crit1 = critList.get(0);
            row1_lil.setVisibility(View.VISIBLE);
            row1_title.setText(crit1.getmName());
            include1_row1_stat1_num.setText(crit1.getmDuration());
            include1_row1_stat2_num.setText(crit1.getmPercentageDuration() + "%");
            include1_row1_stat3_num.setText(crit1.getmEventsCount());
        } else {
            row1_lil.setVisibility(View.GONE);
        }

        if (critList != null && critList.size() >= 2) {
            CriticalEvent crit2 = critList.get(1);
            row2_lil.setVisibility(View.VISIBLE);
            row2_title.setText(crit2.getmName());
            include1_row2_stat1_num.setText(crit2.getmDuration());
            include1_row2_stat2_num.setText(crit2.getmPercentageDuration() + "%");
            include1_row2_stat3_num.setText(crit2.getmEventsCount());
        } else {
            row2_lil.setVisibility(View.GONE);
        }

    }

}
