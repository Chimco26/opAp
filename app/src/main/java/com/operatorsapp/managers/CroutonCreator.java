package com.operatorsapp.managers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.TextView;

import com.operatorsapp.R;
import com.zemingo.logrecorder.ZLogger;

import de.keyboardsurfer.android.widget.crouton.Configuration;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.LifecycleCallback;

public class CroutonCreator {
    private static final String LOG_TAG = CroutonCreator.class.getSimpleName();
    private final EmeraldCrouton mCurrentCrouton = new EmeraldCrouton();
    private final int DEFAULT_CROUTON_TIME = 5000;

    public void showCrouton(Activity activity, String croutonMessage, int croutonDurationInMilliseconds, int viewGroup, CroutonType croutonType) {
        showCrouton(activity, SpannableStringBuilder.valueOf(croutonMessage), croutonDurationInMilliseconds, viewGroup, croutonType);
    }

    public void showCrouton(Activity activity, SpannableStringBuilder croutonMessage, int croutonDurationInMilliseconds, int viewGroup, CroutonType croutonType) {
        if (checkIfConnectivityCroutonIsDisplayed()) {
            return;
        }
        if (!mCurrentCrouton.isEmpty() && mCurrentCrouton.getCroutonType().equals(croutonType)) {
            ZLogger.v(LOG_TAG, "showCrouton(), trying to show same crouton type twice");
            return;
        }
        Crouton crouton;
        switch (croutonType) {
            case CREDENTIALS_ERROR:
            case URL_ERROR:
                crouton = createCrouton(activity, croutonMessage, croutonDurationInMilliseconds, viewGroup, croutonType);
                break;
            case CONNECTIVITY:
                if (!mCurrentCrouton.isEmpty()) {
                    mCurrentCrouton.getCrouton().hide();
                    mCurrentCrouton.removeCrouton();
                }
                crouton = createCrouton(activity, SpannableStringBuilder.valueOf(croutonMessage), Configuration.DURATION_INFINITE, viewGroup, croutonType);
                break;
            case NETWORK_ERROR:
                crouton = createCrouton(activity, SpannableStringBuilder.valueOf(croutonMessage), croutonDurationInMilliseconds, viewGroup, croutonType);
                break;
            default:
                ZLogger.e(LOG_TAG, "showCrouton(), no crouton type");
                return;
        }
        crouton.setLifecycleCallback(new LifecycleCallback() {
            @Override
            public void onDisplayed() {

            }

            @Override
            public void onRemoved() {
                mCurrentCrouton.removeCrouton();
            }
        });
        crouton.show();
        mCurrentCrouton.setCrouton(crouton);
        mCurrentCrouton.setCroutonType(croutonType);
    }

//    public void cancelAllCroutons() {
//        if (mCurrentCrouton != null) {
//            mCurrentCrouton.removeCrouton();
//        }
//        Crouton.cancelAllCroutons();
//    }

    public void hideConnectivityCrouton() {
        if (!mCurrentCrouton.isEmpty() && mCurrentCrouton.getCroutonType().equals(CroutonType.CONNECTIVITY)) {
            mCurrentCrouton.getCrouton().hide();
        }
    }

    private boolean checkIfConnectivityCroutonIsDisplayed() {
        if (!mCurrentCrouton.isEmpty() && mCurrentCrouton.getCroutonType().equals(CroutonType.CONNECTIVITY)) {
            ZLogger.v(LOG_TAG, "showErrorCrouton(), connectivity crouton is displayed");
            return true;
        }
        return false;
    }


    @SuppressLint("InflateParams")
    @NonNull
    private Crouton createCrouton(Activity activity, SpannableStringBuilder croutonMessage, int croutonDurationInMilliseconds, int viewGroup, CroutonType croutonType) {
        if (croutonDurationInMilliseconds == 0) {
            croutonDurationInMilliseconds = DEFAULT_CROUTON_TIME;
        }
        Configuration configuration = new Configuration.Builder().setDuration(croutonDurationInMilliseconds).build();
        View croutonView = null;
        switch (croutonType) {
            case CREDENTIALS_ERROR:
            case URL_ERROR:
                croutonView = activity.getLayoutInflater().inflate(R.layout.crouton_error_view, null);
                break;
            case CONNECTIVITY:
            case NETWORK_ERROR:
                croutonView = activity.getLayoutInflater().inflate(R.layout.cruton_network_error_view, null);
                break;
        }
        TextView croutonText = (TextView) croutonView.findViewById(R.id.crouton_text);
        croutonText.setText(croutonMessage);
        return Crouton.make(activity, croutonView, viewGroup, configuration);
    }

    public enum CroutonType {
        CONNECTIVITY, NETWORK_ERROR, CREDENTIALS_ERROR, URL_ERROR
    }

    public class EmeraldCrouton {


        private Crouton mCrouton;

        private CroutonType mCroutonType;

        public boolean isEmpty() {
            return mCrouton == null;
        }

        public Crouton getCrouton() {
            return mCrouton;
        }

        public void setCrouton(Crouton crouton) {
            mCrouton = crouton;
        }

        public void removeCrouton() {
            mCrouton = null;
            mCroutonType = null;
        }

        public CroutonType getCroutonType() {
            return mCroutonType;
        }

        public void setCroutonType(CroutonType croutonType) {
            mCroutonType = croutonType;
        }
    }
}
