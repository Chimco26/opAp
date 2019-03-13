package com.operatorsapp.dialogs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.common.RejectForMultipleRequest;
import com.operators.activejobslistformachineinfra.ActiveJob;
import com.operators.activejobslistformachineinfra.ActiveJobsListForMachine;
import com.operators.reportfieldsformachineinfra.ReportFieldsForMachine;
import com.operatorsapp.R;
import com.operatorsapp.adapters.ReportNumericAdapter;
import com.operatorsapp.adapters.TechnicianSpinnerAdapter;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.view.FocusableRecycleView;
import com.operatorsapp.view.SingleLineKeyboard;
import com.operatorsapp.view.widgetViewHolders.NumericViewHolder;

import java.util.ArrayList;

public class SetupEndDialog implements NumericViewHolder.OnKeyboardManagerListener {

    private static final int STATIC_REASON_ID = 100;
    private final Activity mContext;
    private final ActiveJobsListForMachine mActiveJobListForMAchine;
    private AlertDialog mAlaramAlertDialog;
    private ReportFieldsForMachine mReportFieldsForMachine;
//    private int mSelectedReasonId;
    private int mSelectedTechnicianId;
    private boolean isRejects = true;
    private FocusableRecycleView mReportRv;
    private LinearLayout mKeyBoardLayout;
    private SingleLineKeyboard mKeyBoard;


    public SetupEndDialog(Activity activity, ReportFieldsForMachine reportFieldsForMachine, ActiveJobsListForMachine activeJobsListForMachine) {
        mContext = activity;
        mReportFieldsForMachine = reportFieldsForMachine;
        mActiveJobListForMAchine = activeJobsListForMachine;

        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }

    public AlertDialog showNoProductionAlarm(final SetupEndDialogListener listener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        LayoutInflater inflater = mContext.getLayoutInflater();
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.fragment_approve_first_item, null);
        builder.setView(view);
        isRejects = PersistenceManager.getInstance().getAddRejectsOnSetupEnd();

        mKeyBoardLayout = view.findViewById(R.id.FAFI_keyboard);
        TextView setupText = view.findViewById(R.id.FAFI_setup_end_text);
        if (isRejects) {
            setupText.setText(R.string.setup_rejects_dialog_text);
        } else {
            setupText.setText(R.string.setup_good_units_dialog_text);
        }
        initRv(view);
        builder.setCancelable(true);
        mAlaramAlertDialog = builder.create();

        setListeners(listener, view);
        setUpTechnicianSpinner(view);

        return mAlaramAlertDialog;
    }

    private void initRv(View view) {

        mReportRv = view.findViewById(R.id.FAFI_report_rv);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mReportRv.setLayoutManager(layoutManager);
        ReportNumericAdapter reportNumericAdapter = new ReportNumericAdapter(mActiveJobListForMAchine.getActiveJobs(), isRejects, this);
        mReportRv.setAdapter(reportNumericAdapter);
    }

    public void setListeners(final SetupEndDialogListener listener, View view) {
        view.findViewById(R.id.button_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAlaramAlertDialog.dismiss();
            }
        });
        view.findViewById(R.id.button_approve).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAlaramAlertDialog.dismiss();
                ArrayList<RejectForMultipleRequest> rejectForMultipleRequests = createReportRejectList();
                listener.sendReport(STATIC_REASON_ID, mSelectedTechnicianId, rejectForMultipleRequests);
            }
        });
        mAlaramAlertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mContext.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                listener.onDismissSetupEndDialog();
            }
        });
    }

    @NonNull
    public ArrayList<RejectForMultipleRequest> createReportRejectList() {
        PersistenceManager persistenceManager = PersistenceManager.getInstance();
        ArrayList<RejectForMultipleRequest> rejectForMultipleRequests = new ArrayList<>();
        for (ActiveJob activeJob : mActiveJobListForMAchine.getActiveJobs()) {
            if (activeJob.isEdited()) {
                if (activeJob.isUnit()) {
                    rejectForMultipleRequests.add(new RejectForMultipleRequest(persistenceManager.getMachineId(),
                            persistenceManager.getOperatorId(), STATIC_REASON_ID, activeJob.getReportValue(),
                            0, 0f, activeJob.getJoshID()));
                }else {
                    rejectForMultipleRequests.add(new RejectForMultipleRequest(persistenceManager.getMachineId(),
                            persistenceManager.getOperatorId(), STATIC_REASON_ID, 0f,
                            0, activeJob.getReportValue(), activeJob.getJoshID()));
                }
            }
        }
        return rejectForMultipleRequests;
    }

    //if (isUnit) {
//        mReportCore.sendReportReject(selectedReasonId, selectedCauseId, Double.parseDouble(value),
//                (double) 0, new MultipleRejectRequestModel(PersistenceManager.getInstance().getSessionId(), rejectRequests));
//    } else {
//        mReportCore.sendReportReject(selectedReasonId, selectedCauseId, (double) 0, Double.parseDouble(value), mSelectProductJobId);
//    }
    private void setUpTechnicianSpinner(View view) {
        Spinner technicianSpinner = view.findViewById(R.id.technician_spinner);
        final TechnicianSpinnerAdapter technicianSpinnerAdapter = new TechnicianSpinnerAdapter(mContext, R.layout.base_spinner_item, mReportFieldsForMachine.getTechnicians());
        technicianSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        technicianSpinner.setAdapter(technicianSpinnerAdapter);

        technicianSpinner.getBackground().setColorFilter(ContextCompat.getColor(mContext, R.color.T12_color), PorterDuff.Mode.SRC_ATOP);

        technicianSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mReportFieldsForMachine.getRejectCauses().size() > 0) {
                    mSelectedTechnicianId = mReportFieldsForMachine.getTechnicians().get(position).getID();
                }

                technicianSpinnerAdapter.setTitle(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onOpenKeyboard(SingleLineKeyboard.OnKeyboardClickListener listener, String text, String[] complementChars) {
        if (mKeyBoardLayout != null) {
            mKeyBoardLayout.setVisibility(View.VISIBLE);
            if (mKeyBoard == null)
                mKeyBoard = new SingleLineKeyboard(mKeyBoardLayout, mContext);

            mKeyBoard.setChars(complementChars);
            mKeyBoard.openKeyBoard(text);
            mKeyBoard.setListener(listener);
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
    }

    public interface SetupEndDialogListener {

        void sendReport(int selectedReasonId, int selectedTechnicianId, ArrayList<RejectForMultipleRequest> rejectForMultipleRequests);

        void onDismissSetupEndDialog();
    }
}