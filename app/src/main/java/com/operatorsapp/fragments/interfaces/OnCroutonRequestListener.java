package com.operatorsapp.fragments.interfaces;

import android.app.Activity;
import android.text.SpannableStringBuilder;

import com.operatorsapp.managers.CroutonCreator;

public interface OnCroutonRequestListener {
    void onShowCroutonRequest(Activity activity, String croutonMessage, int croutonDurationInMilliseconds, int viewGroup, CroutonCreator.CroutonType croutonType);

    void onShowCroutonRequest(Activity activity, SpannableStringBuilder croutonMessage, int croutonDurationInMilliseconds, int viewGroup, CroutonCreator.CroutonType croutonType);

    void onHideConnectivityCroutonRequest();
}
