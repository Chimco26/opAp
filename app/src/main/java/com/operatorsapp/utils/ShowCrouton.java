package com.operatorsapp.utils;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;

import com.operators.infra.ErrorObjectInterface;
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
            final SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(prefix + credentialsError);
            spannableStringBuilder.setSpan(new StyleSpan(R.style.DroidSansBold), 0, prefix.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            showEventsCrouton(onCroutonRequestListener, spannableStringBuilder, CroutonCreator.CroutonType.NETWORK_ERROR);
        } else {
            if (ErrorObject.ErrorCode.Url_not_correct.equals(reason.getError())) {
                String prefix = OperatorApplication.getAppContext().getString(R.string.could_not_log_in).concat(" ");
                String credentialsError = OperatorApplication.getAppContext().getString(R.string.url_error);
                final SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(prefix + credentialsError);
                spannableStringBuilder.setSpan(new StyleSpan(R.style.DroidSansBold), 0, prefix.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                showCrouton(onCroutonRequestListener, spannableStringBuilder, CroutonCreator.CroutonType.URL_ERROR);
            } else if (ErrorObject.ErrorCode.Credentials_mismatch.equals(reason.getError())) {
                String prefix = OperatorApplication.getAppContext().getString(R.string.could_not_log_in).concat(" ");
                String credentialsError = OperatorApplication.getAppContext().getString(R.string.credentials_error);
                final SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(prefix + credentialsError);
                spannableStringBuilder.setSpan(new StyleSpan(R.style.DroidSansBold), 0, prefix.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                showEventsCrouton(onCroutonRequestListener, spannableStringBuilder, CroutonCreator.CroutonType.CREDENTIALS_ERROR);
            } else {
                String prefix = OperatorApplication.getAppContext().getString(R.string.could_not_log_in).concat(" ");
                String networkError = OperatorApplication.getAppContext().getString(R.string.no_communication);
                final SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(prefix + networkError);
                spannableStringBuilder.setSpan(new StyleSpan(R.style.DroidSansBold), 0, prefix.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                showCrouton(onCroutonRequestListener, spannableStringBuilder, CroutonCreator.CroutonType.NETWORK_ERROR);
            }
        }
    }

    public static void jobsLoadingErrorCrouton(OnCroutonRequestListener onCroutonRequestListener) {
        String error_text = OperatorApplication.getAppContext().getString(R.string.could_not_reach_server_error).concat(" ");
        final SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(error_text);
        spannableStringBuilder.setSpan(new StyleSpan(R.style.DroidSansBold), 0, error_text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        showJobsCrouton(onCroutonRequestListener, spannableStringBuilder, CroutonCreator.CroutonType.NETWORK_ERROR);
    }

    private static void showCrouton(final OnCroutonRequestListener onCroutonRequestListener, final SpannableStringBuilder str, final CroutonCreator.CroutonType credentialsError) {
        if (onCroutonRequestListener != null) {
            onCroutonRequestListener.onShowCroutonRequest(str, CROUTON_DURATION, R.id.login_fragment_crouton_anchor, credentialsError);
        }
    }


    private static void showJobsCrouton(final OnCroutonRequestListener onCroutonRequestListener, final SpannableStringBuilder str, final CroutonCreator.CroutonType credentialsError) {
        if (onCroutonRequestListener != null) {
            onCroutonRequestListener.onShowCroutonRequest(str, CROUTON_DURATION, R.id.error_job_frame_layout, credentialsError);
        }
    }

    private static void showEventsCrouton(final OnCroutonRequestListener onCroutonRequestListener, final SpannableStringBuilder str, final CroutonCreator.CroutonType credentialsError) {
        if (onCroutonRequestListener != null) {
            onCroutonRequestListener.onShowCroutonRequest(str, CROUTON_DURATION, R.id.fragment_dashboard_layouts, credentialsError);
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

    public static void reportRejectCrouton(OnCroutonRequestListener onCroutonRequestListener) {
        String error_text = OperatorApplication.getAppContext().getString((R.string.no_reasons)).concat(" ");
        final SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(error_text);
        spannableStringBuilder.setSpan(new StyleSpan(R.style.DroidSansBold), 0, error_text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        showReportCrouton(onCroutonRequestListener, spannableStringBuilder, CroutonCreator.CroutonType.NETWORK_ERROR);
    }

    private static void showReportCrouton(final OnCroutonRequestListener onCroutonRequestListener, final SpannableStringBuilder str, final CroutonCreator.CroutonType credentialsError) {
        if (onCroutonRequestListener != null) {
            onCroutonRequestListener.onShowCroutonRequest(str, CROUTON_DURATION, R.id.report_reject_screen, credentialsError);
        }
    }
}
