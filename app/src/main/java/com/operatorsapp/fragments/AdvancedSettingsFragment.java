package com.operatorsapp.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.operatorsapp.R;
import com.operatorsapp.interfaces.CroutonRootProvider;
import com.operatorsapp.interfaces.SettingsInterface;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.utils.broadcast.SendLogsBroadcast;


public class AdvancedSettingsFragment extends Fragment implements View.OnClickListener, CroutonRootProvider, SendLogsBroadcast.SendLogsListener {
    private static final String LOG_TAG = AdvancedSettingsFragment.class.getSimpleName();

    private static final String SELECTED_LANGUAGE = "selected_language";
    private static final int MIN_POLLING_FREQUENCY_VALUE = 20; // TODO: David  return to 20
    private static final int MAX_POLLING_FREQUENCY_VALUE = 60;
    private static final int MIN_POPUP_VALUE = 5;
    private static final int MAX_POPUP_VALUE = 30;
    private static final int MIN_TIMEOUT_VALUE = 20;
    private static final int MAX_TIMEOUT_VALUE = 120;
    private SettingsInterface mSettingsInterface;
    private String mSelectedLanguage;
    private EditText mPollingFrequencyEditText;
    private EditText mRequestTimeoutEditText;
    private EditText mPopupTimeoutEditText;
    private TextView mButtonSave;
    private TextView mPopupRangeErrorTextView;
    private TextView mPollingRangeErrorTextView;
    private TextView mTimeoutRangeErrorTextView;
    private boolean mPollingFrequencyIsValid = true;
    private boolean mTimeoutIsValid = true;
    private boolean mPopupIsValid = true;

    private TextView mSendLogButton;
    private SendLogsBroadcast mSendLogsBroadcast = null;
    private AdvancedSettingsListener mListener;


    public static AdvancedSettingsFragment newInstance(String selectedLanguage) {
        AdvancedSettingsFragment advancedSettingsFragment = new AdvancedSettingsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(SELECTED_LANGUAGE, selectedLanguage);
        advancedSettingsFragment.setArguments(bundle);
        return advancedSettingsFragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mSettingsInterface = (SettingsInterface) getActivity();

        if (context instanceof AdvancedSettingsListener){

            mListener = (AdvancedSettingsListener) context;

        }else {

            Log.d(LOG_TAG, "onAttach: Activity need to implement AdvancedSettingsListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSelectedLanguage = getArguments().getString(SELECTED_LANGUAGE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_advanced_settings, container, false);
        setActionBar();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPollingFrequencyEditText = view.findViewById(R.id.polling_frequency_edit_text);
        mPollingFrequencyEditText.setText(String.valueOf(PersistenceManager.getInstance().getPollingFrequency()));
        mRequestTimeoutEditText = view.findViewById(R.id.request_timeout_edit_text);
        mRequestTimeoutEditText.setText(String.valueOf(PersistenceManager.getInstance().getRequestTimeout()));
        mPopupTimeoutEditText = view.findViewById(R.id.popup_timeout_edit_text);
        mPopupTimeoutEditText.setText(String.valueOf(PersistenceManager.getInstance().getTimeToDownParameterDialog() / 1000));
        mButtonSave = view.findViewById(R.id.button_save);
        mPollingRangeErrorTextView = view.findViewById(R.id.polling_range_error_text_view);
        mTimeoutRangeErrorTextView = view.findViewById(R.id.timeout_range_error_text_view);
        mPopupRangeErrorTextView = view.findViewById(R.id.pop_up_range_error_text_view);
        mPollingFrequencyEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    int pollingFrequency = Integer.parseInt(mPollingFrequencyEditText.getText().toString());
                    validatePollingFrequency(pollingFrequency);
                }
            }
        });


        mRequestTimeoutEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    int requestTimeout = Integer.parseInt(mRequestTimeoutEditText.getText().toString());
                    validateTimeout(requestTimeout);
                }
            }
        });

        mPopupTimeoutEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {

                    validatePopupTimeout(Integer.parseInt(mPopupTimeoutEditText.getText().toString()));

                }
            }
        });

        mSendLogButton = view.findViewById(R.id.send_log_settings_button);
        mSendLogButton.setVisibility(View.INVISIBLE);
    }


    private void validatePollingFrequency(int pollingFrequency) {
        if (pollingFrequency >= MIN_POLLING_FREQUENCY_VALUE && pollingFrequency <= MAX_POLLING_FREQUENCY_VALUE) {
            mPollingFrequencyIsValid = true;
            mPollingRangeErrorTextView.setVisibility(View.INVISIBLE);
            mButtonSave.setEnabled(true);
        } else {
            mPollingFrequencyIsValid = false;
            mPollingRangeErrorTextView.setVisibility(View.VISIBLE);
            mButtonSave.setEnabled(false);
        }
    }

    private void validateTimeout(int requestTimeout) {
        if (requestTimeout >= MIN_TIMEOUT_VALUE && requestTimeout <= MAX_TIMEOUT_VALUE) {
            mTimeoutIsValid = true;
            mTimeoutRangeErrorTextView.setVisibility(View.INVISIBLE);
            mButtonSave.setEnabled(true);
        } else {
            mTimeoutIsValid = false;
            mTimeoutRangeErrorTextView.setVisibility(View.VISIBLE);
            mButtonSave.setEnabled(false);
        }
    }

    private void validatePopupTimeout(int popupTimeout) {
        if (popupTimeout >= MIN_POPUP_VALUE && popupTimeout <= MAX_POPUP_VALUE) {
            mPopupIsValid = true;
            mPopupRangeErrorTextView.setVisibility(View.INVISIBLE);
            mButtonSave.setEnabled(true);
        } else {
            mPopupIsValid = false;
            mPopupRangeErrorTextView.setVisibility(View.VISIBLE);
            mButtonSave.setEnabled(false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mButtonSave.setOnClickListener(this);
        mSendLogButton.setOnClickListener(this);
//        registerReceiver();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        removeBroadcasts();
    }

    private void removeBroadcasts() {

        if (getActivity() != null) {
            try {

                LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mSendLogsBroadcast);

            } catch (Exception e) {


                Log.e(LOG_TAG, e.getMessage());
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mButtonSave.setOnClickListener(null);
        mSendLogButton.setOnClickListener(null);

        if (getActivity() != null) {
            View view = getActivity().getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        }
    }

    private void setActionBar() {
        if (getActivity() != null) {
            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (actionBar != null) {
                actionBar.setHomeButtonEnabled(false);
                actionBar.setDisplayHomeAsUpEnabled(false);
                actionBar.setDisplayShowTitleEnabled(false);
                actionBar.setDisplayShowCustomEnabled(true);
                actionBar.setDisplayUseLogoEnabled(true);
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                // rootView null
                @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.settings_action_bar, null);
                LinearLayout buttonClose = view.findViewById(R.id.close_image);
                buttonClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getActivity().onBackPressed();
                    }
                });
                actionBar.setCustomView(view);
                ((TextView) view.findViewById(R.id.new_job_title)).setText(getString(R.string.advanced_settings));
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_save: {
                if (mPollingFrequencyEditText.getText() != null && !mPollingFrequencyEditText.getText().toString().equals("") && mRequestTimeoutEditText.getText() != null && !mRequestTimeoutEditText.getText().toString().equals("")) {
                    checkDataAndSave();
                }
                break;
            }
            case R.id.send_log_settings_button: {

//                if (isStoragePermissionGranted()) {
//
//                    sendLogToEmail();
//
//                }
            }
            break;
        }
    }

//    private void sendLogToEmail() {
//
//        ProgressDialogManager.show(getActivity());
//        OppAppLogger.d(LOG_TAG, "start sendLogToEmail(), ");
//        LogRecorder.getInstance().setEmailInfo("support@matics.live", "Operator app logs", null);
//        try {
//            LogRecorder.getInstance().requestSendLogsIntent(false, new LogRecorder.SendLogsListener() {
//                @Override
//                public void onCompleted(final Intent intent) {
//                    ProgressDialogManager.dismiss();
//                    try {
//                        mListener.onIgnoreOnPauseFromAdvancedSettings();
//                        startActivity(intent);
//                    } catch (Exception e) {
//                        OppAppLogger.e(LOG_TAG, "requestSendLogsIntent(), failed.", e);
//                    }
//                }
//            });
//            OppAppLogger.d(LOG_TAG, "end sendLogToEmail(), ");
//        } catch (Exception e) {
//            ProgressDialogManager.dismiss();
//            e.printStackTrace();
//            if (e.getMessage() != null)
//
//                Log.e(LOG_TAG, e.getMessage());
//        }
//    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (getActivity() != null && getActivity().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(LOG_TAG, "Permission is granted");
                return true;
            } else {

                Log.v(LOG_TAG, "Permission is revoked");

                mSettingsInterface.onIgnoreFromOnPause(true);
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else {
            Log.v(LOG_TAG, "Permission is granted");
            return true;
        }
    }


    private void checkDataAndSave() {
        int pollingFrequency = Integer.parseInt(mPollingFrequencyEditText.getText().toString());
        int requestTimeout = Integer.parseInt(mRequestTimeoutEditText.getText().toString());
        int popupTimeout = Integer.parseInt(mPopupTimeoutEditText.getText().toString());


        if (getActivity() != null && mPollingFrequencyIsValid && mTimeoutIsValid && mPopupIsValid) {
            if (pollingFrequency != PersistenceManager.getInstance().getPollingFrequency()) {
                PersistenceManager.getInstance().setPolingFrequency(pollingFrequency);
                mSettingsInterface.onRefreshPollingRequest();
            }

            if (requestTimeout != PersistenceManager.getInstance().getRequestTimeout()) {
                PersistenceManager.getInstance().setRequestTimeOut(requestTimeout);
            }

            if (popupTimeout != PersistenceManager.getInstance().getTimeToDownParameterDialog() / 1000) {

                PersistenceManager.getInstance().setTimeToDownParameterDialog(popupTimeout);
            }

            if (mSelectedLanguage != null) {

                if (!mSelectedLanguage.equals(PersistenceManager.getInstance().getCurrentLang())) {
                    PersistenceManager.getInstance().setCurrentLang(mSelectedLanguage);
                    mSettingsInterface.onRefreshApplicationRequest();
                } else {
//                    getFragmentManager().popBackStack(DASHBOARD_FRAGMENT, android.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    getActivity().onBackPressed();
                }
            } else {
                getActivity().onBackPressed();
//                getFragmentManager().popBackStack(DASHBOARD_FRAGMENT, android.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        }


    }

    @Override
    public int getCroutonRoot() {
        return R.id.advanced_settings_crouton_root;
    }

    private void registerReceiver() {

        if (mSendLogsBroadcast == null && getActivity() != null) {

            mSendLogsBroadcast = new SendLogsBroadcast(this);

            LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mSendLogsBroadcast, new IntentFilter(SendLogsBroadcast.ACTION_SEND_LOGS));

        }

    }


    @Override
    public void onPermissionGranted() {

//        sendLogToEmail();
    }

    public interface AdvancedSettingsListener{

        void onIgnoreOnPauseFromAdvancedSettings();
    }
}
