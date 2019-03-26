package com.operatorsapp.dialogs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.example.common.RejectForMultipleRequest;
import com.operators.activejobslistformachineinfra.ActiveJob;
import com.operators.activejobslistformachineinfra.ActiveJobsListForMachine;
import com.operators.reportfieldsformachineinfra.ReportFieldsForMachine;
import com.operatorsapp.R;
import com.operatorsapp.adapters.ReportNumericAdapter;
import com.operatorsapp.adapters.TechnicianSpinnerAdapter;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.view.SingleLineKeyboard;
import com.operatorsapp.view.widgetViewHolders.NumericViewHolder;

import java.util.ArrayList;
import java.util.List;

public class SetupEndDialog implements NumericViewHolder.OnKeyboardManagerListener {

    private static final int STATIC_REASON_ID = 100;
    private final Activity mContext;
    private final List<ActiveJob> mActiveJobs;
    private AlertDialog mAlaramAlertDialog;
    private ReportFieldsForMachine mReportFieldsForMachine;
    //    private int mSelectedReasonId;
    private int mSelectedTechnicianId;
    private boolean isRejects = true;
    private ViewPager mReportRv;
    private LinearLayout mKeyBoardLayout;
    private SingleLineKeyboard mKeyBoard;


    public SetupEndDialog(Activity activity, ReportFieldsForMachine reportFieldsForMachine, ActiveJobsListForMachine activeJobsListForMachine) {
        mContext = activity;
        mReportFieldsForMachine = reportFieldsForMachine;
        mActiveJobs = activeJobsListForMachine.getActiveJobs();
//        filter0Units();

        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }

    private void filter0Units() {
        ArrayList<ActiveJob> toDelete = new ArrayList<>();
        for (ActiveJob activeJob: mActiveJobs){
            if (activeJob.getJobUnitsProducedOK().equals("0") || activeJob.getJobUnitsProducedOK().isEmpty()){
                toDelete.add(activeJob);
            }
        }
        mActiveJobs.removeAll(toDelete);
    }

    public AlertDialog showNoProductionAlarm(final SetupEndDialogListener listener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        LayoutInflater inflater = mContext.getLayoutInflater();
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.fragment_approve_first_item, null);
        builder.setView(view);
        isRejects = PersistenceManager.getInstance().getAddRejectsOnSetupEnd();

        mKeyBoardLayout = view.findViewById(R.id.FAFI_keyboard);
        mKeyBoardLayout.setVisibility(View.VISIBLE);
        initRv(view);
        builder.setCancelable(true);
        mAlaramAlertDialog = builder.create();

        setListeners(listener, view);
        setUpTechnicianSpinner(view);

        return mAlaramAlertDialog;
    }

    private void initRv(View view) {

        if (mActiveJobs != null &&
                mActiveJobs.size() > 0) {
            view.findViewById(R.id.FAFI_back_btn).setVisibility(View.VISIBLE);
            view.findViewById(R.id.FAFI_next_btn).setVisibility(View.VISIBLE);
            mReportRv = view.findViewById(R.id.FAFI_report_rv);
            mReportRv.setOffscreenPageLimit(mActiveJobs.size());
            ReportNumericAdapter reportNumericAdapter = new ReportNumericAdapter(mActiveJobs, isRejects, this);
            mReportRv.setAdapter(reportNumericAdapter);
            mReportRv.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    onCloseKeyboard();
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            if (mActiveJobs.size() > 1) {

                view.findViewById(R.id.FAFI_back_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mReportRv.getCurrentItem() > 0) {
                            mReportRv.setCurrentItem(mReportRv.getCurrentItem() - 1);
                        }
                    }
                });
                view.findViewById(R.id.FAFI_next_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mReportRv.getCurrentItem() < mActiveJobs.size() - 1) {
                            mReportRv.setCurrentItem(mReportRv.getCurrentItem() + 1);
                        }
                    }
                });
            }else {
                view.findViewById(R.id.FAFI_back_btn).setVisibility(View.GONE);
                view.findViewById(R.id.FAFI_next_btn).setVisibility(View.GONE);
            }
        }else {
            view.findViewById(R.id.FAFI_back_btn).setVisibility(View.GONE);
            view.findViewById(R.id.FAFI_next_btn).setVisibility(View.GONE);
        }
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
        for (ActiveJob activeJob : mActiveJobs) {
            if (activeJob.isEdited()) {
                if (activeJob.isUnit()) {
                    rejectForMultipleRequests.add(new RejectForMultipleRequest(persistenceManager.getMachineId(),
                            persistenceManager.getOperatorId(), STATIC_REASON_ID, activeJob.getReportValue(),
                            0, 0f, activeJob.getJoshID()));
                } else {
                    rejectForMultipleRequests.add(new RejectForMultipleRequest(persistenceManager.getMachineId(),
                            persistenceManager.getOperatorId(), STATIC_REASON_ID, 0f,
                            0, activeJob.getReportValue(), activeJob.getJoshID()));
                }
            }
        }
        return rejectForMultipleRequests;
    }

    private void setUpTechnicianSpinner(View view) {
        Spinner technicianSpinner = view.findViewById(R.id.technician_spinner);
        final TechnicianSpinnerAdapter technicianSpinnerAdapter = new TechnicianSpinnerAdapter(mContext, R.layout.base_spinner_item, mReportFieldsForMachine.getTechnicians());
        technicianSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        technicianSpinner.setAdapter(technicianSpinnerAdapter);

        technicianSpinner.getBackground().setColorFilter(ContextCompat.getColor(mContext, R.color.T12_color), PorterDuff.Mode.SRC_ATOP);

        technicianSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mReportFieldsForMachine != null && mReportFieldsForMachine.getRejectCauses().size() > 0) {
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
//            mKeyBoardLayout.setVisibility(View.VISIBLE);
            if (mKeyBoard == null)
                mKeyBoard = new SingleLineKeyboard(mKeyBoardLayout, mContext);

            mKeyBoard.setChars(complementChars);
            mKeyBoard.openKeyBoard(text);
            mKeyBoard.setListener(listener);
        }
    }

    @Override
    public void onCloseKeyboard() {
//        if (mKeyBoardLayout != null) {
//            mKeyBoardLayout.setVisibility(View.GONE);
//        }
        if (mKeyBoard != null) {
            mKeyBoard.setListener(null);
        }
    }

    public interface SetupEndDialogListener {

        void sendReport(int selectedReasonId, int selectedTechnicianId, ArrayList<RejectForMultipleRequest> rejectForMultipleRequests);

        void onDismissSetupEndDialog();
    }
}