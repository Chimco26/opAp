package com.operatorsapp.dialogs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AlertDialog;
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
import com.operatorsapp.interfaces.OnKeyboardManagerListener;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.view.SingleLineKeyboard;

import java.util.ArrayList;
import java.util.List;

public class SetupEndDialog implements OnKeyboardManagerListener {

    private static final int STATIC_REASON_ID = 100;
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
        mReportFieldsForMachine = reportFieldsForMachine;
        mActiveJobs = activeJobsListForMachine.getActiveJobs();
//        filter0Units();todo

        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }

    private void filter0Units() {
        ArrayList<ActiveJob> toDelete = new ArrayList<>();
        for (ActiveJob activeJob : mActiveJobs) {
            if (activeJob.getJobUnitsProducedOK().equals("0") || activeJob.getJobUnitsProducedOK().isEmpty()) {
                toDelete.add(activeJob);
            }
        }
        mActiveJobs.removeAll(toDelete);
    }

    public AlertDialog showNoProductionAlarm(Activity context, final SetupEndDialogListener listener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = context.getLayoutInflater();
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.dialog_setup_end, null);
        builder.setView(view);
        isRejects = PersistenceManager.getInstance().getAddRejectsOnSetupEnd();

        mKeyBoardLayout = view.findViewById(R.id.FAFI_keyboard);
        initRv(view);
        builder.setCancelable(true);
        mAlaramAlertDialog = builder.create();

        setListeners(context, listener, view);
        setUpTechnicianSpinner(context, view);

        mAlaramAlertDialog.show();
        return mAlaramAlertDialog;
    }

    private void initRv(View view) {
        if (mActiveJobs != null &&
                mActiveJobs.size() > 0) {
            showArrows(view, View.VISIBLE);
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
            } else {
                showArrows(view, View.GONE);
            }
        } else {
            showArrows(view, View.GONE);
        }
    }

    private void showArrows(View view, int gone) {
        view.findViewById(R.id.FAFI_back_btn).setVisibility(gone);
        view.findViewById(R.id.FAFI_next_btn).setVisibility(gone);
    }

    public void setListeners(final Activity context, final SetupEndDialogListener listener, View view) {
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
                context.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
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

    private void setUpTechnicianSpinner(Activity context, View view) {
        Spinner technicianSpinner = view.findViewById(R.id.technician_spinner);
        final TechnicianSpinnerAdapter technicianSpinnerAdapter = new TechnicianSpinnerAdapter(context, R.layout.base_spinner_item, mReportFieldsForMachine.getTechnicians());
        technicianSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        technicianSpinner.setAdapter(technicianSpinnerAdapter);

        technicianSpinner.getBackground().setColorFilter(ContextCompat.getColor(context, R.color.T12_color), PorterDuff.Mode.SRC_ATOP);

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
    public void onOpenKeyboard(Context context, SingleLineKeyboard.OnKeyboardClickListener listener, String text, String[] complementChars) {
        if (mKeyBoardLayout != null) {
            if (mKeyBoard == null)
                mKeyBoard = new SingleLineKeyboard(mKeyBoardLayout, context);

            mKeyBoard.setChars(complementChars);
            mKeyBoard.openKeyBoard(context, text);
            mKeyBoard.setListener(listener);
        }
    }

    @Override
    public void onCloseKeyboard() {
        if (mKeyBoard != null) {
            mKeyBoard.setListener(null);
        }
    }

    public interface SetupEndDialogListener {

        void sendReport(int selectedReasonId, int selectedTechnicianId, ArrayList<RejectForMultipleRequest> rejectForMultipleRequests);

        void onDismissSetupEndDialog();
    }
}