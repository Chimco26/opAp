package com.operatorsapp.fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.common.StandardResponse;
import com.example.common.department.MachineLineResponse;
import com.example.common.department.MachinesLineDetail;
import com.operators.reportfieldsformachineinfra.Technician;
import com.operatorsapp.R;
import com.operatorsapp.adapters.NotificationHistoryAdapter;
import com.operatorsapp.adapters.TechCallAdapter;
import com.operatorsapp.application.OperatorApplication;
import com.operatorsapp.dialogs.GenericDialog;
import com.operatorsapp.dialogs.NotificationsDialog;
import com.operatorsapp.dialogs.TechCallFilter;
import com.operatorsapp.managers.CroutonCreator;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.managers.ProgressDialogManager;
import com.operatorsapp.model.TechCallInfo;
import com.operatorsapp.server.NetworkManager;
import com.operatorsapp.server.requests.PostTechnicianCallRequest;
import com.operatorsapp.server.requests.RespondToNotificationRequest;
import com.operatorsapp.server.responses.Notification;
import com.operatorsapp.server.responses.NotificationHistoryResponse;
import com.operatorsapp.utils.ChangeLang;
import com.operatorsapp.utils.Consts;
import com.operatorsapp.utils.FilterStatus;
import com.operatorsapp.utils.GoogleAnalyticsHelper;
import com.operatorsapp.utils.ShowCrouton;
import com.operatorsapp.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.operatorsapp.activities.TechCallActivity.EXTRA_MACHINE_LINE;
import static com.operatorsapp.activities.TechCallActivity.EXTRA_MANAGE_SERVICE_CALL_FOR_TECHNICIAN;
import static com.operatorsapp.activities.TechCallActivity.EXTRA_REPORT_FIELD_FOR_MACHINE;

public class TechCallFragment extends Fragment implements View.OnClickListener {

    public static final String LOG_TAG = TechCallFragment.class.getSimpleName();
    private TechCallListener mListener;
    private TextView mApplyBtn;
    private TextView mOpenTab;
    private TextView mLast24Tab;
    private EditText mDescriptionEt;
    private View mOpenUnderline;
    private View mLast24Underline;
    private ImageView mFilterIv;
    private RecyclerView mRecycler;
    private Spinner mSortSpnr;
    private Spinner mSelectMachineSpnr;
    private Spinner mSelectTechSpnr;
    private List<Technician> mTechnicianList;
    private boolean isManageServiceCall;
    private ArrayList<TechCallInfo> mTechCallList;
    private LinearLayout mSelectMachineFrame;
    private LinearLayout mSelectTechFrame;
    private int mSelectedMachine = -1;
    private int mSelectedTech = -1;
    private MachineLineResponse mMachineLine;
    private TextView mSortTv;
    private boolean isOpenCalls = true;
    private HashMap<Integer, Boolean> mFilteredOutMachines;

    public TechCallFragment() {
    }


    public static TechCallFragment newInstance() {
        TechCallFragment fragment = new TechCallFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tech_call, container, false);
        if (getArguments() != null) {
            isManageServiceCall = getArguments().getBoolean(EXTRA_MANAGE_SERVICE_CALL_FOR_TECHNICIAN, false);
            mTechnicianList = getArguments().getParcelableArrayList(EXTRA_REPORT_FIELD_FOR_MACHINE);
            mMachineLine = getArguments().getParcelable(EXTRA_MACHINE_LINE);
            if (mTechnicianList == null) mTechnicianList = new ArrayList<>();
        }
        mTechCallList = PersistenceManager.getInstance().getCalledTechnician();
        initViews(view);
        setListeners();
        return view;
    }

    private void setListeners() {
        mApplyBtn.setOnClickListener(this);
        mOpenTab.setOnClickListener(this);
        mLast24Tab.setOnClickListener(this);
        mFilterIv.setOnClickListener(this);
        mSelectTechFrame.setOnClickListener(this);
        mSelectMachineFrame.setOnClickListener(this);
    }

    private void initViews(View view) {
        mApplyBtn = view.findViewById(R.id.tech_fragment_apply_tv);
        mOpenTab = view.findViewById(R.id.tech_fragment_tv_left_tab);
        mOpenUnderline = view.findViewById(R.id.tech_fragment_left_tab_underline);
        mLast24Tab = view.findViewById(R.id.tech_fragment_tv_right_tab);
        mLast24Underline = view.findViewById(R.id.tech_fragment_right_tab_underline);
        mSortSpnr = view.findViewById(R.id.tech_fragment_sort_by_spnr);
        mSortTv = view.findViewById(R.id.tech_fragment_sort_by_tv);
        mFilterIv = view.findViewById(R.id.tech_fragment_filter_iv);
        mRecycler = view.findViewById(R.id.tech_fragment_rv);
        mSelectMachineFrame = view.findViewById(R.id.tech_fragment_select_machine_frame);
        mSelectMachineSpnr = view.findViewById(R.id.tech_fragment_select_machine_spnr);
        mSelectTechFrame = view.findViewById(R.id.tech_fragment_select_tech_frame);
        mSelectTechSpnr = view.findViewById(R.id.tech_fragment_select_tech_spnr);
        mDescriptionEt = view.findViewById(R.id.tech_fragment_description_et);

        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        setOpenCalls(0);
        setTechniciansSpinner();
        setMachineSpinner();
        setLineLayout(mMachineLine);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.tech_fragment_apply_tv:
                checkFieldsForNewCall();
                break;
            case R.id.tech_fragment_tv_left_tab:
                setOpenCalls(0);
                break;
            case R.id.tech_fragment_tv_right_tab:
                setLast24Hrs();
                break;
            case R.id.tech_fragment_filter_iv:
                openFilter();
                break;
            case R.id.tech_fragment_select_tech_frame:
                mSelectTechSpnr.performClick();
                break;
            case R.id.tech_fragment_select_machine_frame:
                mSelectMachineSpnr.performClick();
                break;
        }
    }

    private void openFilter() {
        if (mFilteredOutMachines == null || mFilteredOutMachines.isEmpty()){
            mFilteredOutMachines = new HashMap<>();
            for (MachinesLineDetail machine : mMachineLine.getMachinesData()) {
                mFilteredOutMachines.put(machine.getMachineID(), true);
            }
        }

        TechCallFilter filter = TechCallFilter.newInstants(mMachineLine, mFilteredOutMachines, new TechCallFilter.TechCallFilterListener() {
            @Override
            public void onFilterItems(HashMap<Integer, Boolean> filteredOutMachines) {
                mFilteredOutMachines = filteredOutMachines;
                filterCalls();
                if(isOpenCalls ){
                    setOpenCalls(0);
                } else {
                    setLast24Hrs();
                }
            }
        });
        filter.show(getChildFragmentManager(), null);
    }

    private void filterCalls() {
        mTechCallList = PersistenceManager.getInstance().getCalledTechnician();
        if (mFilteredOutMachines != null && mFilteredOutMachines.size() > 0) {

            Iterator<TechCallInfo> iterator = mTechCallList.iterator();
            while (iterator.hasNext()) {
                TechCallInfo techCall = iterator.next();
                if (mFilteredOutMachines.containsKey(techCall.getmMachineId()) && !mFilteredOutMachines.get(techCall.getmMachineId())){
                    iterator.remove();
                }
            }
        }
    }

    private void setTechniciansSpinner() {
        ArrayList<String> technicianArray = new ArrayList<>();
        if (mTechnicianList != null) {
            technicianArray.add(getResources().getString(R.string.select_technician));
            for (Technician technician : mTechnicianList) {
                technicianArray.add(ChangeLang.defaultIsEng() ? technician.getEName() : technician.getLName());
            }
        }
        mSelectTechSpnr.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, technicianArray));
        mSelectTechSpnr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSelectedTech = position -1;
                mSelectTechFrame.setBackgroundColor(getResources().getColor(R.color.grey_lite));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    private void setSortSpinner() {
        ArrayList<String> sortArray = new ArrayList<>();
        sortArray.add(getResources().getString(R.string.date));
        sortArray.add(getResources().getString(R.string.machine));
        sortArray.add(getResources().getString(R.string.status));

        mSortSpnr.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, sortArray));
        mSortSpnr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(isOpenCalls ){
                    setOpenCalls(0);
                } else {
                    setLast24Hrs();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    private void sortCalls(int position) {

        switch (position){
            case 0:  // sort by date
                Collections.sort(mTechCallList, new Comparator<TechCallInfo>() {
                    @Override
                    public int compare(TechCallInfo o1, TechCallInfo o2) {
                        if (o1.getmCallTime() > o2.getmCallTime()){
                            return -1;
                        }else if (o1.getmCallTime() < o2.getmCallTime()) {
                            return 1;
                        }
                        return 0;
                    }
                });
                break;

            case 1:  // sort by machine
                Collections.sort(mTechCallList, new Comparator<TechCallInfo>() {
                    @Override
                    public int compare(TechCallInfo o1, TechCallInfo o2) {
                        if (o1.getmMachineId() > o2.getmMachineId()){
                            return 1;
                        }else if (o1.getmMachineId() < o2.getmMachineId()) {
                            return -1;
                        }
                        return 0;
                    }
                });
                break;

            case 2:   // sort by status
                Collections.sort(mTechCallList, new Comparator<TechCallInfo>() {
                    @Override
                    public int compare(TechCallInfo o1, TechCallInfo o2) {
                        if (o1.getmResponseType() > o2.getmResponseType()){
                            return 1;
                        }else if (o1.getmResponseType() < o2.getmResponseType()) {
                            return -1;
                        }
                        return 0;
                    }
                });
                break;
        }
    }

    private void setMachineSpinner() {
        ArrayList<String> machineArray = new ArrayList<>();
        if (mMachineLine != null) {
            machineArray.add(getResources().getString(R.string.select_machine_screen_title));
            for (MachinesLineDetail machine : mMachineLine.getMachinesData()) {
                machineArray.add(machine.getMachineName());
            }
        }
        mSelectMachineSpnr.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, machineArray));
        mSelectMachineSpnr.setSelection(mSelectedMachine);
        mSelectMachineSpnr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSelectedMachine = position -1;
                mSelectMachineFrame.setBackgroundColor(getResources().getColor(R.color.grey_lite));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    private void checkFieldsForNewCall() {
        boolean isFieldsOk = true;
        if (mMachineLine != null && mMachineLine.getLineID() != 0 && mSelectedMachine < 0){
            mSelectMachineFrame.setBackgroundColor(Color.RED);
            isFieldsOk = false;
        }

        if (mSelectedTech < 0){
            mSelectTechFrame.setBackgroundColor(Color.RED);
            isFieldsOk = false;
        }

        if (isFieldsOk)
            createNewTechCall();
    }

    private void setLast24Hrs() {
        isOpenCalls = false;
        mLast24Tab.setTextColor(getResources().getColor(R.color.tabNotificationColor));
        mOpenTab.setTextColor(getResources().getColor(R.color.dark_indigo));
        mLast24Underline.setVisibility(View.VISIBLE);
        mOpenUnderline.setVisibility(View.INVISIBLE);

        ArrayList<TechCallInfo> techCallList = new ArrayList<>();

        long millis24h = 1000 * 60 * 60 * 24;
        long millisBefore24h = Calendar.getInstance().getTimeInMillis() - millis24h;

        for (TechCallInfo techCall : mTechCallList) {
            if (techCall.getmCallTime() > millisBefore24h) {
                techCallList.add(techCall);
            }
        }

        mRecycler.setAdapter(new TechCallAdapter(getContext(), techCallList, isManageServiceCall, mMachineLine, new TechCallAdapter.TechCallItemListener() {
            @Override
            public void onRemoveCallPressed(TechCallInfo techCallInfo) {
                removeCall(techCallInfo);
            }

            @Override
            public void onManageCall(TechCallInfo techCallInfo) {
                manageCall(techCallInfo);
            }
        }));

//        for (Notification notification : PersistenceManager.getInstance().getNotificationHistory()) {
//            Date date = TimeUtils.getDateForNotification(notification.getmResponseDate());
//            if (date != null && notification.getmNotificationType() == Consts.NOTIFICATION_TYPE_TECHNICIAN && date.after(date24hrs)) {
//                notList.add(notification);
//            }
//        }
//        mRecycler.setAdapter(new NotificationHistoryAdapter(getContext(), notList, null));
    }

    private void setOpenCalls(int position) {
        isOpenCalls = true;
        mOpenTab.setTextColor(getResources().getColor(R.color.tabNotificationColor));
        mLast24Tab.setTextColor(getResources().getColor(R.color.dark_indigo));
        mOpenUnderline.setVisibility(View.VISIBLE);
        mLast24Underline.setVisibility(View.INVISIBLE);
//
//        String machineName = "";
//        if (mMachineLine != null && mMachineLine.getLineID() > 0){
//            machineName = PersistenceManager.getInstance().getMachineName();
//        }

        sortCalls(mSortSpnr.getSelectedItemPosition());
        mRecycler.setAdapter(new TechCallAdapter(getContext(), mTechCallList, isManageServiceCall, mMachineLine, new TechCallAdapter.TechCallItemListener() {
            @Override
            public void onRemoveCallPressed(TechCallInfo techCallInfo) {
                removeCall(techCallInfo);
            }

            @Override
            public void onManageCall(TechCallInfo techCallInfo) {
                manageCall(techCallInfo);
            }
        }));
        ((LinearLayoutManager) mRecycler.getLayoutManager()).scrollToPosition(position);
    }

    private void manageCall(final TechCallInfo techCallInfo) {
        Notification notification = null;
        final PersistenceManager pm = PersistenceManager.getInstance();
        for (Notification not : pm.getNotificationHistory()) {
            if (not.getmNotificationID() == techCallInfo.getmNotificationId()){
                notification = not;
            }
        }

        if (notification == null) return;
        String srcId = pm.getOperatorId();
        if (!(srcId != null && srcId.length() > 0)){
            srcId = pm.getUserId() + "";
        }
        final int machineId = mMachineLine != null ? pm.getMachineId() : mMachineLine.getMachinesData().get(mSelectMachineSpnr.getSelectedItemPosition() -1).getMachineID();

        final RespondToNotificationRequest request = new RespondToNotificationRequest(pm.getSessionId(),
                notification.getmTitle(),
                notification.getmBody(getContext()),
                machineId + "",
                notification.getmNotificationID() + "",
                Consts.NOTIFICATION_RESPONSE_TYPE_APPROVE,
                notification.getmNotificationType(),
                Consts.NOTIFICATION_RESPONSE_TARGET_TECHNICIAN,
                pm.getUserId() + "",
                pm.getOperatorName(),
                techCallInfo.getmName(),
                srcId,
                techCallInfo.getmTechnicianId() +"");

        if (techCallInfo.getmResponseType() == Consts.NOTIFICATION_RESPONSE_TYPE_UNSET){
            NetworkManager.getInstance().postResponseToNotification(request, new Callback<StandardResponse>() {
                @Override
                public void onResponse(@NonNull Call<StandardResponse> call, @NonNull Response<StandardResponse> response) {
                }

                @Override
                public void onFailure(@NonNull Call<StandardResponse> call, @NonNull Throwable t) {
                }
            });
        }

        if (techCallInfo.getmResponseType() == Consts.NOTIFICATION_RESPONSE_TYPE_START_SERVICE){
            request.setResponseType(Consts.NOTIFICATION_RESPONSE_TYPE_END_SERVICE);
        }else {
            request.setResponseType(Consts.NOTIFICATION_RESPONSE_TYPE_START_SERVICE);
        }

        NetworkManager.getInstance().postResponseToNotification(request, new Callback<StandardResponse>() {
            @Override
            public void onResponse(@NonNull Call<StandardResponse> call, @NonNull Response<StandardResponse> response) {
                if (response.body() != null && response.body().getError().getErrorDesc() == null){

                    if (request.getmResponseType() == Consts.NOTIFICATION_RESPONSE_TYPE_END_SERVICE){
                        mTechCallList.remove(techCallInfo);
                    }else {
                        techCallInfo.setmResponseType(request.getmResponseType());
                    }
                    pm.setCalledTechnicianList(mTechCallList);
                    mListener.onGetNotificationsFromServer();
                    setOpenCalls(((LinearLayoutManager) mRecycler.getLayoutManager()).findFirstCompletelyVisibleItemPosition());


                }else {
                    onFailure(call, new Throwable());
                }
            }

            @Override
            public void onFailure(@NonNull Call<StandardResponse> call, @NonNull Throwable t) {
            }
        });
    }

    private void removeCall(final TechCallInfo techCallInfo) {
        Notification notificationToRemove = null;
        final PersistenceManager pm = PersistenceManager.getInstance();
        for (Notification not : pm.getNotificationHistory()) {
            if (not.getmNotificationID() == techCallInfo.getmNotificationId()){
                notificationToRemove = not;
            }
        }

        if (notificationToRemove != null) {

            int responseType = Consts.NOTIFICATION_RESPONSE_TYPE_CANCELLED;
            if (notificationToRemove.getmResponseType() == Consts.NOTIFICATION_RESPONSE_TYPE_START_SERVICE) {
                responseType = Consts.NOTIFICATION_RESPONSE_TYPE_END_SERVICE;
            }

            String srcId = pm.getOperatorId();
            if (!(srcId != null && srcId.length() > 0)){
                srcId = pm.getUserId() + "";
            }
            int machineId = mMachineLine != null ? pm.getMachineId() : mMachineLine.getMachinesData().get(mSelectMachineSpnr.getSelectedItemPosition() -1).getMachineID();

            RespondToNotificationRequest request = new RespondToNotificationRequest(pm.getSessionId(),
                    notificationToRemove.getmTitle(),
                    notificationToRemove.getmBody(getContext()),
                    machineId + "",
                    notificationToRemove.getmNotificationID() + "",
                    responseType,
                    notificationToRemove.getmNotificationType(),
                    Consts.NOTIFICATION_RESPONSE_TARGET_TECHNICIAN,
                    pm.getUserId() + "",
                    pm.getOperatorName(),
                    techCallInfo.getmName(),
                    srcId,
                    techCallInfo.getmTechnicianId() +"");

            NetworkManager.getInstance().postResponseToNotification(request, new Callback<StandardResponse>() {
                @Override
                public void onResponse(@NonNull Call<StandardResponse> call, @NonNull Response<StandardResponse> response) {

                    if (response.body() != null && response.body().getError().getErrorDesc() == null){
                        mTechCallList.remove(techCallInfo);
                        pm.setCalledTechnicianList(mTechCallList);
                        if (mTechCallList.size() > 0 && techCallInfo.getmNotificationId() == PersistenceManager.getInstance().getRecentTechCallId()) {
                            PersistenceManager.getInstance().setRecentTechCallId(mTechCallList.get(0).getmNotificationId());
                        }
                        mListener.onGetNotificationsFromServer();
                        if (mRecycler != null && mRecycler.getLayoutManager() != null)
                            setOpenCalls(((LinearLayoutManager) mRecycler.getLayoutManager()).findFirstCompletelyVisibleItemPosition());
                    }else {
                        onFailure(call, new Throwable());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<StandardResponse> call, @NonNull Throwable t) {
                }
            });
        }else {
        }
    }

    private void createNewTechCall() {
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

        final Technician technician = mTechnicianList.get(mSelectedTech);
        final String techName = OperatorApplication.isEnglishLang() ? technician.getEName() : technician.getLName();
        String sourceUserId = PersistenceManager.getInstance().getOperatorId();
        final int machineId = mMachineLine == null ? pm.getMachineId() : mMachineLine.getMachinesData().get(mSelectMachineSpnr.getSelectedItemPosition() -1).getMachineID();

        PostTechnicianCallRequest request = new PostTechnicianCallRequest(pm.getSessionId(), machineId, title, technician.getID(), body, mDescriptionEt.getText().toString(), operatorName, technician.getEName(), sourceUserId);
        NetworkManager.getInstance().postTechnicianCall(request, new Callback<StandardResponse>() {
            @Override
            public void onResponse(@NonNull Call<StandardResponse> call, @NonNull Response<StandardResponse> response) {
                if (response.body() != null && response.body().getError().getErrorDesc() == null) {

                    PersistenceManager.getInstance().setTechnicianCallTime(Calendar.getInstance().getTimeInMillis());
                    PersistenceManager.getInstance().setCalledTechnicianName(techName);

                    TechCallInfo techCall = new TechCallInfo(machineId, 0, techName, getString(R.string.called_technician) + "\n" + techName,
                            mDescriptionEt.getText().toString(), Calendar.getInstance().getTimeInMillis(), response.body().getLeaderRecordID(), technician.getID(), 0);
                    PersistenceManager.getInstance().setCalledTechnician(techCall);
                    PersistenceManager.getInstance().setRecentTechCallId(techCall.getmNotificationId());
                    mListener.onGetNotificationsFromServer();
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
                final AlertDialog alertDialog = dialog.showNoProductionAlarm();
                dialog.setListener(new GenericDialog.OnGenericDialogListener() {
                    @Override
                    public void onActionYes() {
                        alertDialog.cancel();
                    }

                    @Override
                    public void onActionNo() {
                    }

                    @Override
                    public void onActionAnother() {
                    }
                });
            }
        });
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onTechCallListener(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TechCallListener) {
            mListener = (TechCallListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void setLineLayout(MachineLineResponse machineLineResponse) {
        mMachineLine = machineLineResponse;
        if (mMachineLine != null && mMachineLine.getLineID() != 0) {
            mSelectMachineFrame.setVisibility(View.VISIBLE);
            mFilterIv.setVisibility(View.VISIBLE);
            mSortSpnr.setVisibility(View.VISIBLE);
            mSortTv.setVisibility(View.VISIBLE);
            setListeners();
            setMachineSpinner();
            setSortSpinner();
            if (isOpenCalls) {
                setOpenCalls(mRecycler.getChildAdapterPosition(mRecycler.getLayoutManager().getFocusedChild()));
            }else {
                setLast24Hrs();
            }
        }else {
            mSelectMachineFrame.setVisibility(View.GONE);
            mFilterIv.setVisibility(View.GONE);
            mSortSpnr.setVisibility(View.GONE);
            mSortTv.setVisibility(View.GONE);
        }
    }

    public interface TechCallListener {
        void onTechCallListener(Uri uri);

        void onGetNotificationsFromServer();
    }
}
