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
import android.widget.ProgressBar;

import com.example.common.StandardResponse;
import com.example.common.StopLogs.Event;
import com.example.common.StopLogs.StopLogsResponse;
import com.example.common.callback.GetStopLogCallback;
import com.operatorsapp.R;
import com.operatorsapp.adapters.StopEventLogAdapter;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.server.NetworkManager;
import com.operatorsapp.utils.GoogleAnalyticsHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.operatorsapp.utils.SimpleRequests.GetLineShiftLog;

public class StopEventLogFragment extends Fragment implements StopEventLogAdapter.StopEventLogAdapterListener {

    public static final String TAG = StopEventLogFragment.class.getSimpleName();
    private View mMainView;
    private OnStopEventLogFragmentListener mListener;
    private RecyclerView mRv;
    private StopEventLogAdapter mAdapter;
    private StopLogsResponse mStopLogsResponse;
    private ArrayList<Event> mStopLogsItems;
    private ProgressBar mProgressBar;
    private View mNoDataTv;
    private HashMap<Integer, ArrayList<Event>> rootMap = new HashMap<Integer, ArrayList<Event>>();

    public static StopEventLogFragment newInstance() {
        StopEventLogFragment stopEventLogFragment = new StopEventLogFragment();
        Bundle bundle = new Bundle();
        stopEventLogFragment.setArguments(bundle);
        return stopEventLogFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

        // Analytics
        new GoogleAnalyticsHelper().trackScreen(getActivity(), TAG);

    }

    @Override
    public void onAttach(Context context) {
        if (context instanceof StopEventLogFragment.OnStopEventLogFragmentListener) {
            mListener = (StopEventLogFragment.OnStopEventLogFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnStopEventLogFragmentListener");
        }
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.frament_stop_event_log, container, false);

        return mMainView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    public void refresh() {
        initVars(getView());
        initView();
        initListener(getView());
        getMachinesLineData();
    }

    private void getMachinesLineData() {
        mProgressBar.setVisibility(View.VISIBLE);
        mNoDataTv.setVisibility(View.GONE);
        PersistenceManager pm = PersistenceManager.getInstance();
        GetLineShiftLog(pm.getSiteUrl(), new GetStopLogCallback() {
            @Override
            public void onGetStopLogSuccess(StopLogsResponse response) {
                mProgressBar.setVisibility(View.GONE);
                mStopLogsResponse = response;
                mStopLogsItems.clear();
                mStopLogsItems.addAll(reorderEvents(mStopLogsResponse.getEvents()));
                mAdapter.getFilter().filter("");
                if (mStopLogsItems.size() == 0) {
                    mNoDataTv.setVisibility(View.VISIBLE);
                } else {
                    mNoDataTv.setVisibility(View.GONE);
                }
            }

            @Override
            public void onGetStopLogFailed(StandardResponse reason) {
                mProgressBar.setVisibility(View.GONE);
                mNoDataTv.setVisibility(View.VISIBLE);
            }
        }, NetworkManager.getInstance(), pm.getTotalRetries(), pm.getRequestTimeout());
    }

    private ArrayList<Event> reorderEvents(List<Event> events) {
        ArrayList<Event> toRemove = new ArrayList<>();
        ArrayList<Event> list = new ArrayList<>();
        ArrayList<Event> rootList = new ArrayList<>();
        for (Event event : events) {
            if (event.getRootEventID() == 0) {
                rootList.add(event);
                toRemove.add(event);
                rootMap.put(event.getEventID(), new ArrayList<Event>());
            }
        }
        events.removeAll(toRemove);

        for (Event event : rootList) {
            list.add(event);
            for (Event eventSub : events) {
                if (eventSub.getRootEventID().equals(event.getEventID())) {
                    list.add(eventSub);
                    toRemove.add(eventSub);
                    event.setHaveChild(true);
                    rootMap.get(event.getEventID()).add(eventSub);
                }
            }
            events.removeAll(toRemove);
        }

        for (Event event : events) {
            event.setRootEventID(0);
            list.add(event);
            rootMap.put(event.getEventID(), new ArrayList<Event>());
        }
        return list;
    }

    private void initVars(View view) {
        mProgressBar = view.findViewById(R.id.FSEL_progressBar);
        mNoDataTv = view.findViewById(R.id.FSEL_no_data_tv);
        mRv = view.findViewById(R.id.FSEL_rv);
        mRv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mStopLogsItems = new ArrayList<>();
        mAdapter = new StopEventLogAdapter(mStopLogsItems, StopEventLogFragment.this);
        mRv.setAdapter(mAdapter);
    }

    private void initView() {


    }

    private void initListener(View view) {

    }

    @Override
    public void onLogSelected(Event item) {
        ArrayList<Float> list = new ArrayList<>();
        list.add(item.getEventID() * 1f);
        if (rootMap.containsKey(item.getEventID()) && rootMap.get(item.getEventID()).size() > 0) {
            mListener.onReportEvents(rootMap.get(item.getEventID()), list, true);
        } else {
            mListener.onReportEvents(mStopLogsItems, list, false);
        }
    }

    public interface OnStopEventLogFragmentListener {

        void onReportEvents(ArrayList<Event> subEvents, ArrayList<Float> eventsIds, boolean isRoot);
    }

}
