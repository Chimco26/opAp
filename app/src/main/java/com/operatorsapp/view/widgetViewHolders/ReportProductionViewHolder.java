package com.operatorsapp.view.widgetViewHolders;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.operators.machinedatainfra.models.Widget;
import com.operatorsapp.R;
import com.operatorsapp.model.event.ReportProductionEvent;

import org.greenrobot.eventbus.EventBus;

public class ReportProductionViewHolder extends RecyclerView.ViewHolder {

    public static final String LOG_TAG = ReportProductionViewHolder.class.getSimpleName();
//    private final TextView mTitle;
    private final int mHeight;
    private final int mWidth;
    private TextView mButtonReportProductionTV;

//    private View step1LL;
//    private View step2LL;
//    private Spinner mRejectReasonSpinner;
//    private EditText mEditUnitsET;
//    private Button mButtonReportBUT;
//    private TextView mButtonCancelBUT;

//    private Activity context;
//    private ShowDashboardCroutonListener mShowDashboardCroutonListener;
//    private OnKeyboardManagerListener mOnKeyboardManagerListener;
//
//    private ReportFieldsForMachine mReportFieldsForMachine;
//    private Integer mJoshId;
//    private String mSelectedPackageTypeName;
//    private int mSelectedPackageTypeId;
//
//    private int mUnitsCounter;


    public ReportProductionViewHolder(@NonNull View itemView, int height, int width) {
        super(itemView);

        mHeight = height;
        mWidth = width;
        LinearLayout parentLayout = itemView.findViewById(R.id.widget_parent_layout);
        setSizes(parentLayout);

        mButtonReportProductionTV = itemView.findViewById(R.id.RPWC_button_report_production_TV);
        mButtonReportProductionTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new ReportProductionEvent());
            }
        });
    }

    public void setData(Widget widget){
//        String nameByLang2 = OperatorApplication.isEnglishLang() ? widget.getFieldEName() : widget.getFieldLName();
//        mTitle.setText(nameByLang2);
    }

    private void setSizes(final LinearLayout parent) {
        ViewGroup.LayoutParams layoutParams;
        layoutParams = parent.getLayoutParams();
        layoutParams.height = (int) (mHeight * 0.5);
        layoutParams.width = (int) (mWidth * 0.325);
        parent.requestLayout();

    }

//    public ReportProductionViewHolder(@NonNull View itemView, int height, int width, ReportFieldsForMachine mReportFieldsForMachine, OnKeyboardManagerListener mOnKeyboardManagerListener,
//                                      ShowDashboardCroutonListener mShowDashboardCroutonListener, Integer mJoshId, Activity context) {
//        super(itemView);
//        mHeight = height;
//        mWidth = width;
//
//        this.context = context;
//        this.mReportFieldsForMachine = mReportFieldsForMachine;
//        this.mOnKeyboardManagerListener = mOnKeyboardManagerListener;
//        this.mJoshId = mJoshId;
//        this.mShowDashboardCroutonListener = mShowDashboardCroutonListener;
//
//        mTitle = itemView.findViewById(R.id.RPWC_title);
//        step1LL = itemView.findViewById(R.id.RPWC_step1_LL);
//        step2LL = itemView.findViewById(R.id.RPWC_step2_LL);
//        mButtonReportProductionTV = itemView.findViewById(R.id.RPWC_button_report_production_TV);
//        mRejectReasonSpinner = itemView.findViewById(R.id.RPWC_package_type_spinner_SP);
//        mEditUnitsET = itemView.findViewById(R.id.RPWC_units_ET);
//        mButtonReportBUT = itemView.findViewById(R.id.RPWC_button_report_BUT);
//        mButtonCancelBUT = itemView.findViewById(R.id.RPWC_button_cancel_TV);
//        LinearLayout parentLayout = itemView.findViewById(R.id.widget_parent_layout);
//        setSizes(parentLayout);
//
//        initListeners();
//        initSpinner();
//
//    }
//
//
//    private void initListeners() {
//        mButtonReportProductionTV.setOnClickListener(this);
//        mButtonCancelBUT.setOnClickListener(this);
//        mButtonReportBUT.setOnClickListener(this);
//
//        mEditUnitsET.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                int inType = mEditUnitsET.getInputType(); // backup the input type
//                mEditUnitsET.setInputType(InputType.TYPE_NULL); // disable soft input
//                mEditUnitsET.onTouchEvent(event); // call native handler
//                mEditUnitsET.setInputType(inType); // restore input type
//                setKeyBoard(mEditUnitsET, null, mButtonReportBUT);
//                return false; // consume touch event
//            }
//        });
//    }
//
//    public void setData(Widget widget){
//        String nameByLang2 = OperatorApplication.isEnglishLang() ? widget.getFieldEName() : widget.getFieldLName();
//        mTitle.setText(nameByLang2);
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.RPWC_button_report_production_TV: {
//                initScreenReportProduction();
//                break;
//            }
//            case R.id.RPWC_button_cancel_TV: {
//                cancelScreenReportProduction();
//                break;
//            }
//            case R.id.RPWC_button_report_BUT: {
//                initNumberUnits();
//                sendReport();
//                break;
//            }
//        }
//    }
//
//    private void cancelScreenReportProduction() {
//        step1LL.setVisibility(View.VISIBLE);
//        step2LL.setVisibility(View.GONE);
//        mButtonReportBUT.setBackground(context.getResources().getDrawable(R.drawable.button_bg_disabled));
//        mButtonReportBUT.setEnabled(false);
//    }
//
//    private void initScreenReportProduction() {
//        step1LL.setVisibility(View.GONE);
//        step2LL.setVisibility(View.VISIBLE);
//        mEditUnitsET.getText().clear();
//    }
//
//    private void initNumberUnits() {
//        if (!mEditUnitsET.getText().toString().isEmpty()) {
//            mUnitsCounter = Integer.parseInt(mEditUnitsET.getText().toString());
//        }
//    }
//
//    private void sendReport() {
//        ProgressDialogManager.show(context);
//        ReportNetworkBridge reportNetworkBridge = new ReportNetworkBridge();
//        reportNetworkBridge.injectInventory(NetworkManager.getInstance());
//        ReportCore mReportCore = new ReportCore(reportNetworkBridge, PersistenceManager.getInstance());
//        mReportCore.registerListener(mReportCallbackListener);
//        OppAppLogger.i(LOG_TAG, "sendReport units value is: " + String.valueOf(mUnitsCounter) + " type value: " + mSelectedPackageTypeId + " type name: " + mSelectedPackageTypeName + " JobId: " + mJoshId);
//
//        mReportCore.sendInventoryReport(mSelectedPackageTypeId, mUnitsCounter, mJoshId);
//
////        SendBroadcast.refreshPolling(getContext());
//    }
//
//    private ReportCallbackListener mReportCallbackListener = new ReportCallbackListener() {
//        @Override//TODO crouton error
//        public void sendReportSuccess(StandardResponse o) {
//
//            StandardResponse response = objectToNewError(o);
//            dismissProgressDialog();
//
//            if (response.getFunctionSucceed()) {
//                new GoogleAnalyticsHelper().trackEvent(context, GoogleAnalyticsHelper.EventCategory.PRODUCTION_REPORT, true, "Report Production- units: " + mUnitsCounter + ", type: " + mSelectedPackageTypeName);
////                ShowCrouton.showSimpleCrouton(mOnCroutonRequestListener, response.getError().getErrorDesc(), CroutonCreator.CroutonType.SUCCESS);
//                mShowDashboardCroutonListener.onShowCrouton(o.getError().getErrorDesc(), false);
//
//            } else {
//                new GoogleAnalyticsHelper().trackEvent(context, GoogleAnalyticsHelper.EventCategory.PRODUCTION_REPORT, false, "Report Production- units: " + mUnitsCounter + ", type: " + mSelectedPackageTypeName);
////                ShowCrouton.showSimpleCrouton(mOnCroutonRequestListener, response.getError().getErrorDesc(), CroutonCreator.CroutonType.NETWORK_ERROR);
//                mShowDashboardCroutonListener.onShowCrouton("sendReportFailure() reason: " + o.getError().getErrorDesc(), true);
//            }
//            SendBroadcast.refreshPolling(context);
//
//            cancelScreenReportProduction();
//
//
//        }
//
//        @Override
//        public void sendReportFailure(StandardResponse reason) {
//            dismissProgressDialog();
//            new GoogleAnalyticsHelper().trackEvent(context, GoogleAnalyticsHelper.EventCategory.PRODUCTION_REPORT, false, "Report Production- " + reason.getError().getErrorDesc());
//            OppAppLogger.i(LOG_TAG, "sendReportFailure() reason: " + reason.getError().getErrorDesc());
//            mShowDashboardCroutonListener.onShowCrouton("sendReportFailure() reason: " + reason.getError().getErrorDesc(), true);
//            SendBroadcast.refreshPolling(context);
//
//            cancelScreenReportProduction();
//        }
//    };
//
//    private void dismissProgressDialog() {
//        if (context != null) {
//            context.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    ProgressDialogManager.dismiss();
//                }
//            });
//        }
//    }
//
//    private StandardResponse objectToNewError(Object o) {
//        StandardResponse responseNewVersion;
//        if (o instanceof StandardResponse) {
//            responseNewVersion = (StandardResponse) o;
//        } else {
//            Gson gson = new GsonBuilder().create();
//
//            ErrorResponse er = gson.fromJson(new Gson().toJson(o), ErrorResponse.class);
//
//            responseNewVersion = new StandardResponse(true, 0, er);
//            if (responseNewVersion.getError().getErrorCode() != 0) {
//                responseNewVersion.setFunctionSucceed(false);
//            }
//        }
//        return responseNewVersion;
//    }
//
//
//    private void setKeyBoard(final EditText editText, String[] complementChars, final View nextBtn) {
//        if (mOnKeyboardManagerListener != null) {
//            mOnKeyboardManagerListener.onOpenKeyboard(new SingleLineKeyboard.OnKeyboardClickListener() {
//                @Override
//                public void onKeyboardClick(String text) {
//                    editText.setText(text);
//                    enableNextBtn(text, nextBtn, editText);
//                }
//            }, editText.getText().toString(), complementChars);
//        }
//    }
//
//    public void enableNextBtn(String text, View nextBtn, EditText editText) {
//        if (!text.isEmpty()) {
//            nextBtn.setBackground(editText.getContext().getResources().getDrawable(R.drawable.buttons_selector));
//            nextBtn.setEnabled(true);
//        } else {
//            nextBtn.setBackground(editText.getContext().getResources().getDrawable(R.drawable.button_bg_disabled));
//            nextBtn.setEnabled(false);
//        }
//    }
//
//    private void initSpinner() {
//        if (context != null) {
//            final RejectProductionSpinnerAdapter reasonSpinnerArrayAdapter = new RejectProductionSpinnerAdapter(context, R.layout.base_spinner_item, mReportFieldsForMachine.getPackageTypes());
//            reasonSpinnerArrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item_custom);
//            mRejectReasonSpinner.setAdapter(reasonSpinnerArrayAdapter);
//            mRejectReasonSpinner.getBackground().setColorFilter(ContextCompat.getColor(context, R.color.T12_color), PorterDuff.Mode.SRC_ATOP);
//
//            mRejectReasonSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                    reasonSpinnerArrayAdapter.setTitle(position);
//                    mSelectedPackageTypeName = isEnglishLang() ? mReportFieldsForMachine.getPackageTypes().get(position).getEName() : mReportFieldsForMachine.getPackageTypes().get(position).getLName();
//                    mSelectedPackageTypeId = mReportFieldsForMachine.getPackageTypes().get(position).getId();
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> parent) {
//
//                }
//            });
//        }
//    }
//
//    private void setSizes(final LinearLayout parent) {
//        ViewGroup.LayoutParams layoutParams;
//        layoutParams = parent.getLayoutParams();
//        layoutParams.height = (int) (mHeight * 0.5);
//        layoutParams.width = (int) (mWidth * 0.325);
//        parent.requestLayout();
//
//    }
}
