package com.operatorsapp.fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.common.StandardResponse;
import com.example.common.StopLogs.Event;
import com.example.common.StopLogs.StopLogsResponse;
import com.example.common.callback.GetStopLogCallback;
import com.operators.reportfieldsformachineinfra.Technician;
import com.operatorsapp.R;
import com.operatorsapp.adapters.StopEventLogAdapter;
import com.operatorsapp.application.OperatorApplication;
import com.operatorsapp.dialogs.EventTechCallDialog;
import com.operatorsapp.dialogs.GenericDialog;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.managers.ProgressDialogManager;
import com.operatorsapp.model.TechCallInfo;
import com.operatorsapp.server.NetworkManager;
import com.operatorsapp.server.requests.PostTechnicianCallRequest;
import com.operatorsapp.utils.GoogleAnalyticsHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.operatorsapp.utils.SimpleRequests.getLineShiftLog;

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
    private EventTechCallDialog mTechCalldialog;
    private TextView mUnreportedTv;
    private boolean isUnreportedOnly = false;
    private ArrayList<Event> mSelectedItems;
    private View mReportMultipleBtn;

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
        initVars(getView());
        initView();
        initListener(getView());
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    public void refresh() {
        getMachinesLineData();
    }

    private void getMachinesLineData() {
        mProgressBar.setVisibility(View.VISIBLE);
        mNoDataTv.setVisibility(View.GONE);
        PersistenceManager pm = PersistenceManager.getInstance();
        getLineShiftLog(pm.getSiteUrl(), new GetStopLogCallback() {
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
                initAdapter();
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
        mUnreportedTv = view.findViewById(R.id.FSEL_unreported_tv);
        mReportMultipleBtn = view.findViewById(R.id.FSEL_report_mutiple_btn);
        mRv = view.findViewById(R.id.FSEL_rv);
        mRv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mStopLogsItems = new ArrayList<>();
        initAdapter();
    }

    private void initView() {


    }

    private void initListener(View view) {
        mUnreportedTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isUnreportedOnly = !isUnreportedOnly;
                if (isUnreportedOnly){
                    mUnreportedTv.setBackgroundColor(getResources().getColor(R.color.blue1));
                }else {
                    mUnreportedTv.setBackgroundColor(getResources().getColor(R.color.white_five));
                }
                initAdapter();
            }
        });
        mReportMultipleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Event item = mSelectedItems.get(0);
                ArrayList<Float> list = new ArrayList<>();
                for (Event event: mSelectedItems) {
                    list.add(event.getEventID() * 1f);
                }
                mListener.onReportEvents(item.getMachineId(), mStopLogsItems, list, item.getRootEventID() == 0, getRootMachineName(item), item.getDescr());
            }
        });
    }

    private void initAdapter(){
        ArrayList<Event> stopLogTempList = new ArrayList<>();
        ArrayList<Integer> rootIdList = new ArrayList<>();
        if (isUnreportedOnly){

            for (Event event : mStopLogsItems) {
                if (event.getEventGroupID() == 6 && !rootIdList.contains(event.getRootEventID())){
                    rootIdList.add(event.getRootEventID());
                }
            }

            for (Event event : mStopLogsItems) {
                if (rootIdList.contains(event.getEventID()) || (rootIdList.contains(event.getRootEventID()) && (event.getRootEventID() != 0 || event.getEventGroupID() == 6))){
                    stopLogTempList.add(event);
                }
            }
        }else {
            stopLogTempList = mStopLogsItems;
        }
        mAdapter = new StopEventLogAdapter(stopLogTempList, StopEventLogFragment.this);
        mRv.setAdapter(mAdapter);
    }

    @Override
    public void onLogSelected(Event item) {
        ArrayList<Float> list = new ArrayList<>();
        list.add(item.getEventID() * 1f);
        if (rootMap.containsKey(item.getRootEventID()) && rootMap.get(item.getRootEventID()) != null && rootMap.get(item.getRootEventID()).size() > 0) {
            mListener.onReportEvents(item.getMachineId(), rootMap.get(item.getRootEventID()), list, item.getRootEventID() == 0, getRootMachineName(item), item.getDescr());
        } else {
            mListener.onReportEvents(item.getMachineId(), mStopLogsItems, list, item.getRootEventID() == 0, getRootMachineName(item), item.getDescr());
        }
    }

    @Override
    public void onUpdateLogSelection(ArrayList<Event> selectedItems) {
        mSelectedItems = selectedItems;
        if (mSelectedItems.size() > 0){
            mReportMultipleBtn.setVisibility(View.VISIBLE);
        }else {
            mReportMultipleBtn.setVisibility(View.GONE);
        }
    }

    @Override
    public void onTechCallSelected(Event item) {
        openTechCallPopUp(item);
    }

    private void openTechCallPopUp(Event item) {
        mTechCalldialog = new EventTechCallDialog(getActivity(), item, new EventTechCallDialog.EventTechCallDialogListener() {
            @Override
            public void onNewCallPressed(Technician technician, Event event, String additionalText) {
                sendTechCall(technician, event, additionalText);
            }
        });
        mTechCalldialog.show();
    }

    private void sendTechCall(final Technician technician, Event event, final String additionalText) {

        mTechCalldialog.dismiss();
        PersistenceManager pm = PersistenceManager.getInstance();
        ProgressDialogManager.show(getActivity());

        String body;
        String operatorName = "";
        String title = getString(R.string.operator) + " ";
        if (pm.getOperatorName() != null && pm.getOperatorName().length() > 0) {
            operatorName = pm.getOperatorName();
        } else {
            operatorName = pm.getUserName();
        }

        body = getActivity().getResources().getString(R.string.service_call_made_new).replace(getActivity().getResources().getString(R.string.placeholder1), operatorName);
        body += " " + pm.getMachineName();
        title += operatorName;

        final String techName = OperatorApplication.isEnglishLang() ? technician.getEName() : technician.getLName();
        String sourceUserId = PersistenceManager.getInstance().getOperatorId();
        final int machineId = event.getMachineId();

        PostTechnicianCallRequest request = new PostTechnicianCallRequest(pm.getSessionId(), machineId, title, technician.getID(), body, additionalText, operatorName, technician.getEName(), sourceUserId, event.getEventID());
        NetworkManager.getInstance().postTechnicianCall(request, new Callback<StandardResponse>() {
            @Override
            public void onResponse(@NonNull Call<StandardResponse> call, @NonNull Response<StandardResponse> response) {
                if (response.body() != null && response.body().getError().getErrorDesc() == null) {

                    PersistenceManager.getInstance().setTechnicianCallTime(Calendar.getInstance().getTimeInMillis());
                    PersistenceManager.getInstance().setCalledTechnicianName(techName);

                    TechCallInfo techCall = new TechCallInfo(machineId, 0, techName, getString(R.string.called_technician) + "\n" + techName,
                            additionalText, Calendar.getInstance().getTimeInMillis(), response.body().getLeaderRecordID(), technician.getID(), 0, null);
                    PersistenceManager.getInstance().setCalledTechnician(techCall);
                    PersistenceManager.getInstance().setRecentTechCallId(techCall.getmNotificationId());
                    ProgressDialogManager.dismiss();

                    new GoogleAnalyticsHelper().trackEvent(getActivity(), GoogleAnalyticsHelper.EventCategory.TECH_CALL, true, "technician name: " + techName);

                } else {
                    String msg = "failed";
                    if (response.body() != null && response.body().getError() != null) {
                        msg = response.body().getError().getErrorDesc();
                    }
                    onFailure(call, new Throwable(msg));
                }
            }

            @Override
            public void onFailure(@NonNull Call<StandardResponse> call, @NonNull Throwable t) {
                ProgressDialogManager.dismiss();
                PersistenceManager.getInstance().setCalledTechnicianName("");

                String m = "";
                if (t.getMessage() != null) {
                    m = t.getMessage();
                }

                new GoogleAnalyticsHelper().trackEvent(getActivity(), GoogleAnalyticsHelper.EventCategory.TECH_CALL, false, "reason: " + m);

                final GenericDialog dialog = new GenericDialog(getActivity(), t.getMessage(), getString(R.string.call_technician_title), getString(R.string.ok), true);
                final AlertDialog alertDialog = dialog.showNoProductionAlarm(getContext());
                dialog.setListener(new GenericDialog.OnGenericDialogListener() {
                    @Override
                    public void onActionYes() {
                        alertDialog.cancel();
                    }
                    @Override
                    public void onActionNo() { }
                    @Override
                    public void onActionAnother() {}
                });
            }
        });
    }

    private String getRootMachineName(Event event) {
        if (event.getRootEventID() == 0){
            return event.getMachineName() + "\n"+ getString(R.string.start) + ": " + event.getEventTime() + "     " + getString(R.string.end) + ": " + event.getEventEndTime();
        }

        ArrayList<Event> root = rootMap.get(event.getRootEventID());

        return root != null ? root.get(0).getMachineName() : "";
    }

    public interface OnStopEventLogFragmentListener {

        void onReportEvents(int machineId, ArrayList<Event> subEvents, ArrayList<Float> eventsIds, boolean isRoot, String machineName, String rootMachineName);
    }

}
