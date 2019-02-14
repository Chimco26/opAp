package com.operatorsapp.dialogs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.operators.reportfieldsformachineinfra.RejectReasons;
import com.operators.reportfieldsformachineinfra.ReportFieldsForMachine;
import com.operatorsapp.R;
import com.operatorsapp.adapters.RejectReasonSpinnerAdapter;
import com.operatorsapp.adapters.TechnicianSpinnerAdapter;
import com.operatorsapp.application.OperatorApplication;
import com.operatorsapp.managers.PersistenceManager;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SetupEndDialog {

    private final Activity mContext;
    private AlertDialog mAlaramAlertDialog;
    private ReportFieldsForMachine mReportFieldsForMachine;
    private int mSelectedReasonId;
    private int mSelectedTechnicianId;


    public SetupEndDialog(Activity activity, ReportFieldsForMachine reportFieldsForMachine) {
        mContext = activity;
        mReportFieldsForMachine = reportFieldsForMachine;
    }

    public AlertDialog showNoProductionAlarm(final SetupEndDialogListener listener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        LayoutInflater inflater = mContext.getLayoutInflater();
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.fragment_approve_first_item, null);
        builder.setView(view);
        if (!PersistenceManager.getInstance().getAddRejectsOnSetupEnd()) {
            view.findViewById(R.id.reject_reason_tv).setVisibility(View.GONE);
            view.findViewById(R.id.reject_reason_spinner).setVisibility(View.GONE);
            view.findViewById(R.id.reject_reason_rl).setVisibility(View.GONE);
        }

        TextView setupText = view.findViewById(R.id.FAFI_setup_end_text);
        if (true){
            setupText.setText(R.string.setup_rejects_dialog_text);
        }else {
            setupText.setText(R.string.setup_good_units_dialog_text);
        }

        builder.setCancelable(true);
        mAlaramAlertDialog = builder.create();
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
                listener.sendReport(mSelectedReasonId, mSelectedTechnicianId);
            }
        });
        setUpSpinnerReason(view);
        setUpTechnicianSpinner(view);
        mAlaramAlertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                listener.onDismissSetupEndDialog();
            }
        });
        mAlaramAlertDialog.getWindow().getDecorView().setTop(100);

        return mAlaramAlertDialog;
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

    private void setUpSpinnerReason(View view) {

        sortRejectReasons();
        if (PersistenceManager.getInstance().getAddRejectsOnSetupEnd()) {
            Spinner rejectReasonSpinner = view.findViewById(R.id.reject_reason_spinner);

            final RejectReasonSpinnerAdapter reasonSpinnerArrayAdapter = new RejectReasonSpinnerAdapter(mContext, R.layout.base_spinner_item, mReportFieldsForMachine.getRejectReasons());
            reasonSpinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            rejectReasonSpinner.setAdapter(reasonSpinnerArrayAdapter);
            rejectReasonSpinner.getBackground().setColorFilter(ContextCompat.getColor(mContext, R.color.T12_color), PorterDuff.Mode.SRC_ATOP);

            rejectReasonSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    mSelectedReasonId = mReportFieldsForMachine.getRejectReasons().get(position).getId();
                    reasonSpinnerArrayAdapter.setTitle(position);

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        } else {
            for (RejectReasons rejectReason : mReportFieldsForMachine.getRejectReasons()) {
                String nameByLang = OperatorApplication.isEnglishLang() ? rejectReason.getEName() : rejectReason.getLName();
                if (nameByLang.equals(mContext.getString(R.string.reject_reason_setup))) {
                    mSelectedReasonId = rejectReason.getId();
                    break;
                }
            }
        }
    }

    private void sortRejectReasons() {
        List<RejectReasons> list = mReportFieldsForMachine.getRejectReasons();
        Collections.sort(list, new Comparator<RejectReasons>() {
            public int compare(RejectReasons o1, RejectReasons o2) {
                if (OperatorApplication.isEnglishLang()) {
                    return o1.getEName().compareTo(o2.getEName());
                } else {
                    return o1.getLName().compareTo(o2.getLName());
                }
            }
        });
        for (RejectReasons rr : list) {
            if (rr.getEName().equals("Setup")) {
                list.remove(rr);
                list.add(0, rr);
                break;
            }
        }
        mReportFieldsForMachine.setRejectReasons(list);
    }

    public interface SetupEndDialogListener{

        void sendReport(int mSelectedReasonId, int mSelectedTechnicianId);

        void onDismissSetupEndDialog();
    }
}