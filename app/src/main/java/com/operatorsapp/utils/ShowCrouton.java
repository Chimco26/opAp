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

    public static void croutonError(OnCroutonRequestListener onCroutonRequestListener, ErrorObjectInterface reason) {
        if (ErrorObject.ErrorCode.Credentials_mismatch.equals(reason.getError())) {
            String prefix = OperatorApplication.getAppContext().getString(R.string.could_not_log_in).concat(" ");
            String credentialsError = OperatorApplication.getAppContext().getString(R.string.credentials_error);
            final SpannableStringBuilder str = new SpannableStringBuilder(prefix + credentialsError);
            str.setSpan(new StyleSpan(R.style.DroidSansBold), 0, prefix.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            showCrouton(onCroutonRequestListener, str, CroutonCreator.CroutonType.CREDENTIALS_ERROR);
        } else {
            String prefix = OperatorApplication.getAppContext().getString(R.string.could_not_log_in).concat(" ");
            String networkError = OperatorApplication.getAppContext().getString(R.string.no_communication);
            final SpannableStringBuilder str = new SpannableStringBuilder(prefix + networkError);
            str.setSpan(new StyleSpan(R.style.DroidSansBold), 0, prefix.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            showCrouton(onCroutonRequestListener, str, CroutonCreator.CroutonType.NETWORK_ERROR);
        }
    }

    private static void showCrouton(final OnCroutonRequestListener onCroutonRequestListener, final SpannableStringBuilder str, final CroutonCreator.CroutonType credentialsError) {
        if (onCroutonRequestListener != null) {
            onCroutonRequestListener.onShowCroutonRequest(str, CROUTON_DURATION, R.id.login_fragment_crouton_anchor, credentialsError);
        }
    }
}
