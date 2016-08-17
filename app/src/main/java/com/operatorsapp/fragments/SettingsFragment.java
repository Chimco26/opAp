package com.operatorsapp.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.operatorsapp.R;
import com.operatorsapp.activities.MainActivity;
import com.operatorsapp.activities.interfaces.GoToScreenListener;
import com.operatorsapp.adapters.LanguagesSpinnerAdapter;
import com.operatorsapp.fragments.interfaces.OnReportFieldsUpdatedCallbackListener;
import com.operatorsapp.interfaces.SettingsInterface;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.managers.ProgressDialogManager;

/**
 * Created by Sergey on 16/08/2016.
 */
public class SettingsFragment extends Fragment implements View.OnClickListener, OnReportFieldsUpdatedCallbackListener {

    private static final String LOG_TAG = SettingsFragment.class.getSimpleName();
    private Spinner mLanguagesSpinner;
    private TextView mFactoryUrlTextView;
    private TextView mRefreshStatusTextView;
    private TextView mAdvancedSettingsButton;
    private Button mRefreshButton;
    private TextView mCancelButton;
    private Button mSaveButton;
    private String mSelectedLanguageCode;
    private String mSelectedLanguageName;
    private Button mButtonChange;
    private GoToScreenListener mGoToScreenListener;
    private SettingsInterface mSettingsInterface;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mGoToScreenListener = (GoToScreenListener) getActivity();
        mSettingsInterface = (SettingsInterface) getActivity();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_settings, container, false);
        setActionBar();

        return view;
    }

    private boolean mIsFirst = true;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLanguagesSpinner = (Spinner) view.findViewById(R.id.languages_spinner);
        final LanguagesSpinnerAdapter spinnerArrayAdapter = new LanguagesSpinnerAdapter(getActivity(), R.layout.spinner_language_item, getResources().getStringArray(R.array.languages_spinner_array));
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mLanguagesSpinner.setAdapter(spinnerArrayAdapter);
        mLanguagesSpinner.getBackground().setColorFilter(ContextCompat.getColor(getContext(), R.color.T12_color), PorterDuff.Mode.SRC_ATOP);

        mLanguagesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mIsFirst) {
//                    spinnerArrayAdapter.setTitle(PersistenceManager.getInstance().getCurrentLanguageName());
                    mIsFirst = false;
                }
                else {
                    mSelectedLanguageCode = getResources().getStringArray(R.array.language_codes_array)[position];
                    mSelectedLanguageName = getResources().getStringArray(R.array.languages_spinner_array)[position];
                    spinnerArrayAdapter.setTitle(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        mFactoryUrlTextView = (TextView) view.findViewById(R.id.url_text_view);
        String siteUrl = PersistenceManager.getInstance().getSiteUrl();
        if (siteUrl != null) {
            mFactoryUrlTextView.setText(PersistenceManager.getInstance().getSiteUrl());
        }
        else {
            mFactoryUrlTextView.setText(R.string.dashes);
        }

        mRefreshStatusTextView = (TextView) view.findViewById(R.id.refresh_status_text_view);
        mRefreshStatusTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_checkmark, 0);
        mRefreshStatusTextView.setVisibility(View.GONE);

        mAdvancedSettingsButton = (TextView) view.findViewById(R.id.advanced_settings_button);

        mRefreshButton = (Button) view.findViewById(R.id.refresh_button);
        Drawable drawable = getResources().getDrawable(R.drawable.button_refresh_reportind_data_selector);
        drawable.setBounds(0, 0, (int) (drawable.getIntrinsicWidth() * 1.5),
                (int) (drawable.getIntrinsicHeight() * 1.5));
        ScaleDrawable sd = new ScaleDrawable(drawable, 0, 1, 1);

        mRefreshButton.setCompoundDrawables(null, null, sd.getDrawable(), null);

        mSaveButton = (Button) view.findViewById(R.id.button_save);
        mCancelButton = (TextView) view.findViewById(R.id.button_cancel);
        mButtonChange = (Button) view.findViewById(R.id.button_change);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdvancedSettingsButton.setOnClickListener(this);
        mSaveButton.setOnClickListener(this);
        mCancelButton.setOnClickListener(this);
        mRefreshButton.setOnClickListener(this);
        mButtonChange.setOnClickListener(this);
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
            View view = inflater.inflate(R.layout.settings_action_bar, null);

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
            case R.id.advanced_settings_button: {
                mGoToScreenListener.goToFragment(AdvancedSettingsFragment.newInstance(mSelectedLanguageCode), true);
                break;
            }
            case R.id.button_cancel: {
                getFragmentManager().popBackStack();
                break;
            }
            case R.id.button_save: {
                if (mSelectedLanguageCode.equals(PersistenceManager.getInstance().getCurrentLang())) {
                    getFragmentManager().popBackStack();
                }
                else {
                    PersistenceManager.getInstance().setCurrentLang(mSelectedLanguageCode);
                    PersistenceManager.getInstance().setCurrentLanguageName(mSelectedLanguageName);
                   mSettingsInterface.refreshApplication();
                }
                break;
            }
            case R.id.refresh_button: {
                ProgressDialogManager.show(getActivity());
                mSettingsInterface.refreshReportFields(this);
                break;
            }
            case R.id.button_change: {
                mSettingsInterface.clearAppData();
                break;
            }
        }
    }

    @Override
    public void onReportUpdatedSuccess() {
        Log.i(LOG_TAG, "onReportUpdatedSuccess()");
        mRefreshStatusTextView.setVisibility(View.VISIBLE);
        dismissProgressDialog();

    }

    @Override
    public void onReportUpdateFailure() {
        Log.i(LOG_TAG, "onReportUpdateFailure()");
        dismissProgressDialog();
    }

    private void dismissProgressDialog() {

        //TODO remove handler, only for test
        Handler mHandler = new Handler();

        mHandler.postDelayed(new Runnable() {
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ProgressDialogManager.dismiss();
                    }
                });
            }
        }, 2000);

    }
}
