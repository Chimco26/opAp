package com.operatorsapp.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompatBase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.operatorsapp.R;
import com.operatorsapp.interfaces.SettingsInterface;
import com.operatorsapp.managers.PersistenceManager;

/**
 * Created by Sergey on 17/08/2016.
 */
public class AdvancedSettingsFragment extends Fragment implements View.OnClickListener {

    private static final String SELECTED_LANGUAGE = "selected_language";
    public static final String DASHBOARD_FRAGMENT = "dashboard_fragment";
    private static final int MIN_POLLING_FREQUENCY_VALUE = 20;
    private static final int MAX_POLLING_FREQUENCY_VALUE = 60;
    private static final int MIN_TIMEOUT_VALUE = 20;
    private static final int MAX_TIMEOUT_VALUE = 120;
    private SettingsInterface mSettingsInterface;
    private String mSelectedLanguage;
    private EditText mPollingFrequencyEditText;
    private EditText mRequestTimeoutEditText;
    private Button mButtonSave;
    private TextView mPollingRangeErrorTextView;
    private TextView mTimeoutRangeErrorTextView;
    private boolean mPollingFrequencyIsValid = true;
    private boolean mTimeoutIsValid = true;

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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_advanced_settings, container, false);
        setActionBar();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPollingFrequencyEditText = (EditText) view.findViewById(R.id.polling_frequency_edit_text);
        mPollingFrequencyEditText.setText(String.valueOf(PersistenceManager.getInstance().getPollingFrequency()));
        mRequestTimeoutEditText = (EditText) view.findViewById(R.id.request_timeout_edit_text);
        mRequestTimeoutEditText.setText(String.valueOf(PersistenceManager.getInstance().getRequestTimeout()));
        mButtonSave = (Button) view.findViewById(R.id.button_save);
        mPollingRangeErrorTextView = (TextView) view.findViewById(R.id.polling_range_error_text_view);
        mTimeoutRangeErrorTextView = (TextView) view.findViewById(R.id.timeout_range_error_text_view);
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

    @Override
    public void onResume() {
        super.onResume();
        mButtonSave.setOnClickListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void setActionBar() {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayUseLogoEnabled(true);
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            // rootView null
            @SuppressLint("InflateParams")
            View view = inflater.inflate(R.layout.advanced_settings_action_bar, null);
            ImageView buttonClose = (ImageView) view.findViewById(R.id.close_image);
            buttonClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getFragmentManager().popBackStack();
                }
            });
            actionBar.setCustomView(view);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_save: {
                if (mPollingFrequencyEditText.getText().toString() != null && !mPollingFrequencyEditText.getText().toString().equals("") && mRequestTimeoutEditText.getText().toString() != null && !mRequestTimeoutEditText.getText().toString().equals("")) {
                    checkDataAndSave();
                }
                break;
            }
        }
    }

    private void checkDataAndSave() {
        int pollingFrequency = Integer.parseInt(mPollingFrequencyEditText.getText().toString());
        int requestTimeout = Integer.parseInt(mRequestTimeoutEditText.getText().toString());


        if (mPollingFrequencyIsValid && mTimeoutIsValid) {
            if (pollingFrequency != PersistenceManager.getInstance().getPollingFrequency()) {
                PersistenceManager.getInstance().setPolingFrequency(pollingFrequency);
                mSettingsInterface.onRefreshPollingRequest();
            }

            if (requestTimeout != PersistenceManager.getInstance().getRequestTimeout()) {
                PersistenceManager.getInstance().setRequestTimeOut(requestTimeout);
            }
            if (mSelectedLanguage != null) {

                if (!mSelectedLanguage.equals(PersistenceManager.getInstance().getCurrentLang())) {
                    PersistenceManager.getInstance().setCurrentLang(mSelectedLanguage);
                    mSettingsInterface.onRefreshApplicationRequest();
                } else {
                    getFragmentManager().popBackStack(DASHBOARD_FRAGMENT, android.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
            } else {
                getFragmentManager().popBackStack(DASHBOARD_FRAGMENT, android.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        }


    }
}
