package com.operatorsapp.view.widgetViewHolders;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.common.ErrorResponse;
import com.example.common.StandardResponse;
import com.example.oppapplog.OppAppLogger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.operators.reportfieldsformachineinfra.ReportFieldsForMachine;
import com.operators.reportrejectcore.ReportCallbackListener;
import com.operators.reportrejectcore.ReportCore;
import com.operators.reportrejectnetworkbridge.ReportNetworkBridge;
import com.operatorsapp.R;
import com.operatorsapp.activities.interfaces.ShowDashboardCroutonListener;
import com.operatorsapp.adapters.RejectProductionSpinnerAdapter;
import com.operatorsapp.interfaces.OnKeyboardManagerListener;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.managers.ProgressDialogManager;
import com.operatorsapp.server.NetworkManager;
import com.operatorsapp.utils.GoogleAnalyticsHelper;
import com.operatorsapp.utils.broadcast.SendBroadcast;
import com.operatorsapp.view.SingleLineKeyboard;
import com.example.oppapplog.OppAppLogger;

import static com.operatorsapp.application.OperatorApplication.isEnglishLang;

public class NewWidget2ViewHolder extends RecyclerView.ViewHolder implements OnClickListener{

    public static final String LOG_TAG = NewWidget2ViewHolder.class.getSimpleName();
    private View screen1;
    private View screen2;
    private TextView buttonReportProductionTV;
    private Spinner rejectReasonSpinner;
    private EditText mEditUnitsET;
    private Button buttonReportBUT;
    private TextView mButtonCancelBUT;

    private Activity mContext;
    private ShowDashboardCroutonListener mShowDashboardCroutonListener;
    private OnKeyboardManagerListener mOnKeyboardManagerListener;

    private ReportFieldsForMachine mReportFieldsForMachine;
    private Integer mJoshId;
    private String mSelectedPackageTypeName;
    private int mSelectedPackageTypeId;

    private int mUnitsCounter;



    public NewWidget2ViewHolder(@NonNull View itemView, ReportFieldsForMachine mReportFieldsForMachine, OnKeyboardManagerListener mOnKeyboardManagerListener,
                                ShowDashboardCroutonListener mShowDashboardCroutonListener, Integer mJoshId, Activity context) {
        super(itemView);
        this.mContext = context;
        this.mReportFieldsForMachine = mReportFieldsForMachine;
        this.mOnKeyboardManagerListener = mOnKeyboardManagerListener;
        this.mJoshId = mJoshId;
        this.mShowDashboardCroutonListener = mShowDashboardCroutonListener;

        screen1 = itemView.findViewById(R.id.NWC2_screen1);
        screen2 = itemView.findViewById(R.id.NWC2_screen2);
        buttonReportProductionTV = itemView.findViewById(R.id.TLWC_report_production_btn);
        rejectReasonSpinner = itemView.findViewById(R.id.package_type_spinner);
        mEditUnitsET = itemView.findViewById(R.id.NWC2_units_ET);
        buttonReportBUT = itemView.findViewById(R.id.NWC2_button_report);
        mButtonCancelBUT = itemView.findViewById(R.id.NWC2_button_cancel);

        initListeners();
        initSpinner();

    }


    private void initListeners() {
        buttonReportProductionTV.setOnClickListener(this);
        mButtonCancelBUT.setOnClickListener(this);
        buttonReportBUT.setOnClickListener(this);

        mEditUnitsET.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int inType = mEditUnitsET.getInputType(); // backup the input type
                mEditUnitsET.setInputType(InputType.TYPE_NULL); // disable soft input
                mEditUnitsET.onTouchEvent(event); // call native handler
                mEditUnitsET.setInputType(inType); // restore input type
                setKeyBoard(mEditUnitsET, null, buttonReportBUT);
                return false; // consume touch event
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.TLWC_report_production_btn: {
                initScreenReportProduction();
                break;
            }
            case R.id.NWC2_button_cancel: {
                cancelScreenReportProduction();
                break;
            }
            case R.id.NWC2_button_report: {
                initNumberUnits();
                sendReport();
                break;
            }
        }
    }

    private void cancelScreenReportProduction() {
        screen1.setVisibility(View.VISIBLE);
        screen2.setVisibility(View.GONE);
        buttonReportBUT.setBackground(mContext.getResources().getDrawable(R.drawable.button_bg_disabled));
        buttonReportBUT.setEnabled(false);
    }

    private void initScreenReportProduction() {
        screen1.setVisibility(View.GONE);
        screen2.setVisibility(View.VISIBLE);
        mEditUnitsET.getText().clear();
    }

    private void initNumberUnits() {
        if (!mEditUnitsET.getText().toString().isEmpty()) {
            mUnitsCounter = Integer.parseInt(mEditUnitsET.getText().toString());
        }
    }

    private void sendReport() {
        ProgressDialogManager.show(mContext);
        ReportNetworkBridge reportNetworkBridge = new ReportNetworkBridge();
        reportNetworkBridge.injectInventory(NetworkManager.getInstance());
        ReportCore mReportCore = new ReportCore(reportNetworkBridge, PersistenceManager.getInstance());
        mReportCore.registerListener(mReportCallbackListener);
        OppAppLogger.i(LOG_TAG, "sendReport units value is: " + String.valueOf(mUnitsCounter) + " type value: " + mSelectedPackageTypeId + " type name: " + mSelectedPackageTypeName + " JobId: " + mJoshId);

        mReportCore.sendInventoryReport(mSelectedPackageTypeId, mUnitsCounter, mJoshId);

//        SendBroadcast.refreshPolling(getContext());
    }

     private ReportCallbackListener mReportCallbackListener = new ReportCallbackListener() {
        @Override//TODO crouton error
        public void sendReportSuccess(StandardResponse o) {

            StandardResponse response = objectToNewError(o);
            dismissProgressDialog();

            if (response.getFunctionSucceed()) {
                new GoogleAnalyticsHelper().trackEvent(mContext, GoogleAnalyticsHelper.EventCategory.PRODUCTION_REPORT, true, "Report Production- units: " + mUnitsCounter + ", type: " + mSelectedPackageTypeName);
//                ShowCrouton.showSimpleCrouton(mOnCroutonRequestListener, response.getError().getErrorDesc(), CroutonCreator.CroutonType.SUCCESS);
                mShowDashboardCroutonListener.onShowCrouton(o.getError().getErrorDesc(), false);

            } else {
                new GoogleAnalyticsHelper().trackEvent(mContext, GoogleAnalyticsHelper.EventCategory.PRODUCTION_REPORT, false, "Report Production- units: " + mUnitsCounter + ", type: " + mSelectedPackageTypeName);
//                ShowCrouton.showSimpleCrouton(mOnCroutonRequestListener, response.getError().getErrorDesc(), CroutonCreator.CroutonType.NETWORK_ERROR);
                mShowDashboardCroutonListener.onShowCrouton("sendReportFailure() reason: " + o.getError().getErrorDesc(), true);
            }
            SendBroadcast.refreshPolling(mContext);

            cancelScreenReportProduction();


        }
         @Override
        public void sendReportFailure(StandardResponse reason) {
            dismissProgressDialog();
            new GoogleAnalyticsHelper().trackEvent(mContext, GoogleAnalyticsHelper.EventCategory.PRODUCTION_REPORT, false, "Report Production- " + reason.getError().getErrorDesc());
            OppAppLogger.i(LOG_TAG, "sendReportFailure() reason: " + reason.getError().getErrorDesc());
             mShowDashboardCroutonListener.onShowCrouton("sendReportFailure() reason: " + reason.getError().getErrorDesc(), true);
            SendBroadcast.refreshPolling(mContext);

             cancelScreenReportProduction();
        }
    };

    private void dismissProgressDialog() {
        if (mContext != null) {
            mContext.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ProgressDialogManager.dismiss();
                }
            });
        }
    }

    private StandardResponse objectToNewError(Object o) {
        StandardResponse responseNewVersion;
        if (o instanceof StandardResponse) {
            responseNewVersion = (StandardResponse) o;
        } else {
            Gson gson = new GsonBuilder().create();

            ErrorResponse er = gson.fromJson(new Gson().toJson(o), ErrorResponse.class);

            responseNewVersion = new StandardResponse(true, 0, er);
            if (responseNewVersion.getError().getErrorCode() != 0) {
                responseNewVersion.setFunctionSucceed(false);
            }
        }
        return responseNewVersion;
    }



    private void setKeyBoard(final EditText editText, String[] complementChars, final View nextBtn) {
        if (mOnKeyboardManagerListener != null) {
            mOnKeyboardManagerListener.onOpenKeyboard(new SingleLineKeyboard.OnKeyboardClickListener() {
                @Override
                public void onKeyboardClick(String text) {
                    editText.setText(text);
                    enableNextBtn(text, nextBtn, editText);
                }
            }, editText.getText().toString(), complementChars);
        }
    }

    public void enableNextBtn(String text, View nextBtn, EditText editText) {
        if (!text.isEmpty()) {
            nextBtn.setBackground(editText.getContext().getResources().getDrawable(R.drawable.buttons_selector));
            nextBtn.setEnabled(true);
        } else {
            nextBtn.setBackground(editText.getContext().getResources().getDrawable(R.drawable.button_bg_disabled));
            nextBtn.setEnabled(false);
        }
    }

    private void initSpinner() {
        if (mContext != null) {
            final RejectProductionSpinnerAdapter reasonSpinnerArrayAdapter = new RejectProductionSpinnerAdapter(mContext, R.layout.base_spinner_item, mReportFieldsForMachine.getPackageTypes());
            reasonSpinnerArrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item_custom);
            rejectReasonSpinner.setAdapter(reasonSpinnerArrayAdapter);
            rejectReasonSpinner.getBackground().setColorFilter(ContextCompat.getColor(mContext, R.color.T12_color), PorterDuff.Mode.SRC_ATOP);

            rejectReasonSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    reasonSpinnerArrayAdapter.setTitle(position);
                    mSelectedPackageTypeName = isEnglishLang() ? mReportFieldsForMachine.getPackageTypes().get(position).getEName() : mReportFieldsForMachine.getPackageTypes().get(position).getLName();
                    mSelectedPackageTypeId = mReportFieldsForMachine.getPackageTypes().get(position).getId();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }
}
