package com.operatorsapp.managers;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.operatorsapp.R;
import com.zemingo.logrecorder.ZLogger;

import de.keyboardsurfer.android.widget.crouton.Configuration;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.LifecycleCallback;

public class CroutonCreator {
    private static final String LOG_TAG = CroutonCreator.class.getSimpleName();
    private final EmeraldCrouton mCurrentCrouton = new EmeraldCrouton();
    private static final int DEFAULT_CROUTON_TIME = 5000;
    private CroutonListener mListener;

    public void showCrouton(Activity activity, String croutonMessage, int croutonDurationInMilliseconds, int viewGroup, CroutonType croutonType) {
        showCrouton(activity, SpannableStringBuilder.valueOf(croutonMessage), croutonDurationInMilliseconds, viewGroup, croutonType);
    }

    public void showCrouton(Activity activity, String croutonMessage, int croutonDurationInMilliseconds, int viewGroup, CroutonType croutonType, CroutonListener listener) {
        mListener = listener;
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
            case ALERT_DIALOG:
                crouton = createCrouton(activity, croutonMessage, PersistenceManager.getInstance().getTimeToDownParameterDialog(), viewGroup, croutonType);
                break;
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


    public void hideConnectivityCrouton() {

        if (!mCurrentCrouton.isEmpty()) {

            if (mCurrentCrouton.getCroutonType().equals(CroutonType.CONNECTIVITY)) {

                mCurrentCrouton.getCrouton().hide();

            } else if (mCurrentCrouton.getCroutonType().equals(CroutonType.NETWORK_ERROR)) {

                mCurrentCrouton.getCrouton().hide();
            }
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
            case ALERT_DIALOG:
                croutonView = activity.getLayoutInflater().inflate(R.layout.crouton_alert_view, null);
                setOnClickListener(croutonView);
                setProgressCountDown(croutonView,PersistenceManager.getInstance().getTimeToDownParameterDialog());
                break;

            case CREDENTIALS_ERROR:
            case URL_ERROR:
                croutonView = activity.getLayoutInflater().inflate(R.layout.crouton_error_view, null);
                setProgressCountDown(croutonView,DEFAULT_CROUTON_TIME);
                break;
            case CONNECTIVITY:
            case NETWORK_ERROR:
                croutonView = activity.getLayoutInflater().inflate(R.layout.cruton_network_error_view, null);
                setProgressCountDown(croutonView,DEFAULT_CROUTON_TIME);
                break;
            default:
                croutonView = activity.getLayoutInflater().inflate(R.layout.cruton_network_error_view, null);
                setProgressCountDown(croutonView,DEFAULT_CROUTON_TIME);
        }
        TextView croutonText = (TextView) croutonView.findViewById(R.id.crouton_text);
        croutonText.setText(croutonMessage);

        setProgressCountDown(croutonView,DEFAULT_CROUTON_TIME);

        return Crouton.make(activity, croutonView, viewGroup, configuration);
    }

    private void setOnClickListener(View croutonView) {

        croutonView.findViewById(R.id.dialog_dismiss).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                hideConnectivityCrouton();

                if (mListener != null) {

                    mListener.onCroutonDismiss();
                }
            }
        });

    }

    public enum CroutonType {
        CONNECTIVITY, NETWORK_ERROR, CREDENTIALS_ERROR, URL_ERROR, ALERT_DIALOG
    }

    private void setProgressCountDown(View view, int croutonTime) {

        ProgressBar mProgressBar = view.findViewById(R.id.PT_progressbar_time_left);

        ObjectAnimator animation = ObjectAnimator.ofInt(mProgressBar, "progress", 0, 100);

        animation.setDuration(croutonTime);

        animation.setInterpolator(new DecelerateInterpolator());

        animation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {


            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });

        animation.start();
    }


    public class EmeraldCrouton {

        private Crouton mCrouton;

        private CroutonType mCroutonType;

        boolean isEmpty() {
            return mCrouton == null;
        }

        Crouton getCrouton() {
            return mCrouton;
        }

        void setCrouton(Crouton crouton) {
            mCrouton = crouton;
        }

        void removeCrouton() {
            mCrouton = null;
            mCroutonType = null;
        }

        CroutonType getCroutonType() {
            return mCroutonType;
        }

        void setCroutonType(CroutonType croutonType) {
            mCroutonType = croutonType;
        }
    }


    public interface CroutonListener {

        void onCroutonDismiss();
    }
}
