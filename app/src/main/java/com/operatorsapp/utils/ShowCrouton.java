package com.operatorsapp.utils;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.StyleSpan;

import com.example.common.callback.ErrorObjectInterface;
import com.operators.loginnetworkbridge.server.ErrorObject;
import com.operatorsapp.R;
import com.operatorsapp.application.OperatorApplication;
import com.operatorsapp.fragments.interfaces.OnCroutonRequestListener;
import com.operatorsapp.managers.CroutonCreator;

public class ShowCrouton {
    private static final int CROUTON_DURATION = 5000;

    public static void jobsLoadingErrorCrouton(OnCroutonRequestListener onCroutonRequestListener, ErrorObjectInterface reason) {
        if (reason == null) {
            String prefix = OperatorApplication.getAppContext().getString(R.string.could_not_get_data).concat(" ");
            String credentialsError = OperatorApplication.getAppContext().getString(R.string.could_not_reach_server_error);
            createCrouton(onCroutonRequestListener, CroutonCreator.CroutonType.NETWORK_ERROR,prefix,credentialsError);
        } else {
            if (ErrorObject.ErrorCode.Url_not_correct.equals(reason.getError())) {
                String prefix = OperatorApplication.getAppContext().getString(R.string.could_not_log_in).concat(" ");
                String credentialsError = OperatorApplication.getAppContext().getString(R.string.url_error);
                createCrouton(onCroutonRequestListener, CroutonCreator.CroutonType.URL_ERROR,prefix,credentialsError);
            } else if (ErrorObject.ErrorCode.Credentials_mismatch.equals(reason.getError())) {
                String prefix = OperatorApplication.getAppContext().getString(R.string.could_not_get_data).concat(" ");
                String credentialsError = OperatorApplication.getAppContext().getString(R.string.credentials_error);
                createCrouton(onCroutonRequestListener, CroutonCreator.CroutonType.CREDENTIALS_ERROR,prefix,credentialsError);
            } else if (ErrorObject.ErrorCode.No_data.equals(reason.getError())) {
                String prefix = OperatorApplication.getAppContext().getString(R.string.could_not_get_data).concat(" ");
                String credentialsError = OperatorApplication.getAppContext().getString(R.string.no_data);
                createCrouton(onCroutonRequestListener, CroutonCreator.CroutonType.CREDENTIALS_ERROR,prefix,credentialsError);
            } else if (ErrorObject.ErrorCode.Retrofit.equals(reason.getError())) {
                String prefix = OperatorApplication.getAppContext().getString(R.string.could_not_get_data).concat(" ");
                String credentialsError = OperatorApplication.getAppContext().getString(R.string.could_not_reach_server_error);
                createCrouton(onCroutonRequestListener, CroutonCreator.CroutonType.CREDENTIALS_ERROR,prefix,credentialsError);
            } else if (ErrorObject.ErrorCode.Server.equals(reason.getError())) {
                String prefix = OperatorApplication.getAppContext().getString(R.string.could_not_get_data).concat(" ");
                String credentialsError = OperatorApplication.getAppContext().getString(R.string.error_rest);
                createCrouton(onCroutonRequestListener, CroutonCreator.CroutonType.CREDENTIALS_ERROR,prefix,credentialsError);
            } else if (ErrorObject.ErrorCode.Missing_reports.equals(reason.getError())) {
                String prefix = OperatorApplication.getAppContext().getString(R.string.could_not_get_data).concat(" ");
                String credentialsError = OperatorApplication.getAppContext().getString(R.string.no_reasons);
                createCrouton(onCroutonRequestListener, CroutonCreator.CroutonType.CREDENTIALS_ERROR,prefix,credentialsError);

            /*} else if (ErrorObject.ErrorCode.Error_rest.equals(reason.getError())) {
                String prefix = OperatorApplication.getAppContext().getString(R.string.could_not_get_data).concat(" ");
                String credentialsError = OperatorApplication.getAppContext().getString(R.string.error_rest);
                createCrouton(onCroutonRequestListener, CroutonCreator.CroutonType.CREDENTIALS_ERROR,prefix,credentialsError,null);
            } else if (ErrorObject.ErrorCode.Get_machines_failed.equals(reason.getError())) {
                String prefix = OperatorApplication.getAppContext().getString(R.string.could_not_get_data).concat(" ");
                String credentialsError = OperatorApplication.getAppContext().getString(R.string.error_rest);
                createCrouton(onCroutonRequestListener, CroutonCreator.CroutonType.CREDENTIALS_ERROR,prefix,credentialsError,null);
            } else if (ErrorObject.ErrorCode.Fail_to_perform_GetMachineShiftLog.equals(reason.getError())) {
                String prefix = OperatorApplication.getAppContext().getString(R.string.could_not_get_data).concat(" ");
                String credentialsError = OperatorApplication.getAppContext().getString(R.string.error_shift_log);
                createCrouton(onCroutonRequestListener, CroutonCreator.CroutonType.CREDENTIALS_ERROR,prefix,credentialsError,null);
                */
            } else if (reason.getDetailedDescription() != null && reason.getDetailedDescription().length() > 0){
                createCrouton(onCroutonRequestListener, CroutonCreator.CroutonType.NETWORK_ERROR, reason.getDetailedDescription(),"");
            }else {
                String prefix = OperatorApplication.getAppContext().getString(R.string.could_not_log_in).concat(" ");
                String networkError = OperatorApplication.getAppContext().getString(R.string.no_communication);
                createCrouton(onCroutonRequestListener, CroutonCreator.CroutonType.NETWORK_ERROR,prefix,networkError);
            }
        }
    }

    public static void showSimpleCrouton(OnCroutonRequestListener onCroutonRequestListener, ErrorObjectInterface reason) {
        if (reason != null && reason.getDetailedDescription() != null && reason.getDetailedDescription().length() > 0) {
            createCrouton(onCroutonRequestListener, CroutonCreator.CroutonType.CREDENTIALS_ERROR, null, reason.getDetailedDescription());
        }
    }

    public static void showSimpleCrouton(OnCroutonRequestListener onCroutonRequestListener, String reason, CroutonCreator.CroutonType croutonType) {
        if (reason != null && reason.length() > 0) {
            createCrouton(onCroutonRequestListener, croutonType, null, reason);
        }
    }

    private static void createCrouton(OnCroutonRequestListener onCroutonRequestListener, CroutonCreator.CroutonType croutonType, String prefix, String message)
    {
        if(TextUtils.isEmpty(prefix))
        {
            prefix = "";
        }
        if(TextUtils.isEmpty(message))
        {
            message = "";
        }

        final SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(prefix + message);
        spannableStringBuilder.setSpan(new StyleSpan(R.style.DroidSansBold), 0, prefix.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.setSpan(new StyleSpan(R.style.DroidSansBold), spannableStringBuilder.length(), spannableStringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        showCrouton(onCroutonRequestListener, spannableStringBuilder, croutonType);
    }

    public static void jobsLoadingErrorCrouton(OnCroutonRequestListener onCroutonRequestListener) {
        String error_text = OperatorApplication.getAppContext().getString(R.string.could_not_reach_server_error).concat(" ");
        final SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(error_text);
        spannableStringBuilder.setSpan(new StyleSpan(R.style.DroidSansBold), 0, error_text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        showJobsCrouton(onCroutonRequestListener, spannableStringBuilder, CroutonCreator.CroutonType.NETWORK_ERROR);
    }

    public static void jobsLoadingAlertCrouton(OnCroutonRequestListener onCroutonRequestListener, String msg) {
        final SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(msg);
        spannableStringBuilder.setSpan(new StyleSpan(R.style.DroidSansBold), 0, msg.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        showJobsCrouton(onCroutonRequestListener, spannableStringBuilder, CroutonCreator.CroutonType.ALERT_DIALOG);
    }

    public static void jobsLoadingSuccessCrouton(OnCroutonRequestListener onCroutonRequestListener, String msg) {
        final SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(msg);
        spannableStringBuilder.setSpan(new StyleSpan(R.style.DroidSansBold), 0, msg.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        showJobsCrouton(onCroutonRequestListener, spannableStringBuilder, CroutonCreator.CroutonType.SUCCESS);
    }

    private static void showCrouton(final OnCroutonRequestListener onCroutonRequestListener, final SpannableStringBuilder str, final CroutonCreator.CroutonType credentialsError) {
        if (onCroutonRequestListener != null) {
            onCroutonRequestListener.onShowCroutonRequest(str, CROUTON_DURATION, R.id.parent_layouts, credentialsError);
        }
    }


    private static void showJobsCrouton(final OnCroutonRequestListener onCroutonRequestListener, final SpannableStringBuilder str, final CroutonCreator.CroutonType credentialsError) {
        if (onCroutonRequestListener != null) {
            onCroutonRequestListener.onShowCroutonRequest(str, CROUTON_DURATION, R.id.error_job_frame_layout, credentialsError);
        }
    }

    @SuppressWarnings("unused")
    private static void showEventsCrouton(final OnCroutonRequestListener onCroutonRequestListener, final SpannableStringBuilder str, final CroutonCreator.CroutonType credentialsError) {
        if (onCroutonRequestListener != null) {
            onCroutonRequestListener.onShowCroutonRequest(str, CROUTON_DURATION, R.id.parent_layouts, credentialsError);
        }
    }


    public static void operatorLoadingErrorCrouton(OnCroutonRequestListener onCroutonRequestListener, String reason) {
        String error_text = OperatorApplication.getAppContext().getString(R.string.unrecognized_operator).concat(" ");
        final SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(error_text);
        spannableStringBuilder.setSpan(new StyleSpan(R.style.DroidSansBold), 0, error_text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        showOperatorsCrouton(onCroutonRequestListener, spannableStringBuilder, CroutonCreator.CroutonType.NETWORK_ERROR);
    }

    private static void showOperatorsCrouton(final OnCroutonRequestListener onCroutonRequestListener, final SpannableStringBuilder str, final CroutonCreator.CroutonType credentialsError) {
        if (onCroutonRequestListener != null) {
            onCroutonRequestListener.onShowCroutonRequest(str, CROUTON_DURATION, R.id.operator_screen, credentialsError);
        }
    }

    /*
    public static void reportRejectCrouton(OnCroutonRequestListener onCroutonRequestListener) {
        String error_text = OperatorApplication.getAppContext().getString((R.string.no_reasons)).concat(" ");
        final SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(error_text);
        spannableStringBuilder.setSpan(new StyleSpan(R.style.DroidSansBold), 0, error_text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        showReportCrouton(onCroutonRequestListener, spannableStringBuilder, CroutonCreator.CroutonType.NETWORK_ERROR);
    }
    */

    @SuppressWarnings("unused")
    private static void showReportCrouton(final OnCroutonRequestListener onCroutonRequestListener, final SpannableStringBuilder str, final CroutonCreator.CroutonType credentialsError) {
        if (onCroutonRequestListener != null) {
            onCroutonRequestListener.onShowCroutonRequest(str, CROUTON_DURATION, R.id.report_reject_screen, credentialsError);
        }
    }

    @SuppressWarnings("unused")
    private static void showStopCrouton(final OnCroutonRequestListener onCroutonRequestListener, final SpannableStringBuilder str, final CroutonCreator.CroutonType credentialsError) {
        if (onCroutonRequestListener != null) {
            onCroutonRequestListener.onShowCroutonRequest(str, CROUTON_DURATION, R.id.report_stop_screen, credentialsError);
        }
    }

    /*
    public static void noDataCrouton(OnCroutonRequestListener onCroutonRequestListener, int viewId) {
        String error_text = OperatorApplication.getAppContext().getString((R.string.no_reasons)).concat(" ");
        final SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(error_text);
        spannableStringBuilder.setSpan(new StyleSpan(R.style.DroidSansBold), 0, error_text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        showNoDataCrouton(onCroutonRequestListener, spannableStringBuilder, CroutonCreator.CroutonType.NETWORK_ERROR, viewId);
    }

    private static void showNoDataCrouton(final OnCroutonRequestListener onCroutonRequestListener, final SpannableStringBuilder str, final CroutonCreator.CroutonType credentialsError, int viewId) {
        if (onCroutonRequestListener != null) {
            onCroutonRequestListener.onShowCroutonRequest(str, Integer.MAX_VALUE, viewId, credentialsError); //R.id.fragment_report_inventory
        }
    }
    */
}
