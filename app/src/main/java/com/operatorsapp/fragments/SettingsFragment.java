package com.operatorsapp.fragments;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.operatorsapp.R;
import com.operatorsapp.activities.interfaces.GoToScreenListener;
import com.operatorsapp.adapters.LanguagesSpinnerAdapter;
import com.operatorsapp.fragments.interfaces.OnReportFieldsUpdatedCallbackListener;
import com.operatorsapp.interfaces.CroutonRootProvider;
import com.operatorsapp.interfaces.SettingsInterface;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.managers.ProgressDialogManager;
import com.operatorsapp.utils.NetworkAvailable;
import com.ravtech.david.sqlcore.DatabaseHelper;
import com.zemingo.logrecorder.ZLogger;

import java.util.ArrayList;

public class SettingsFragment extends BackStackAwareFragment implements View.OnClickListener, OnReportFieldsUpdatedCallbackListener, CroutonRootProvider
{

    private static final String LOG_TAG = SettingsFragment.class.getSimpleName();
    private Spinner mLanguagesSpinner;
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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_settings, container, false);
        setActionBar();

        return view;
    }

    private boolean mIsFirst = true;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLanguagesSpinner = view.findViewById(R.id.languages_spinner);
        final LanguagesSpinnerAdapter spinnerArrayAdapter = new LanguagesSpinnerAdapter(getActivity(), R.layout.spinner_language_item, getResources().getStringArray(R.array.languages_spinner_array));
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mLanguagesSpinner.setAdapter(spinnerArrayAdapter);

        if (getActivity() != null) {
            mLanguagesSpinner.getBackground().setColorFilter(ContextCompat.getColor(getActivity(), R.color.T12_color), PorterDuff.Mode.SRC_ATOP);
        }

        mLanguagesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mIsFirst) {
                    mIsFirst = false;
                    for (int i = 0; i < getResources().getStringArray(R.array.language_codes_array).length; i++) {
                        if (getResources().getStringArray(R.array.language_codes_array)[i].equals(PersistenceManager.getInstance().getCurrentLang())) {

                            mLanguagesSpinner.setSelection(i);
                        }
                    }
                } else {
                    mSelectedLanguageCode = getResources().getStringArray(R.array.language_codes_array)[position];
                    mSelectedLanguageName = getResources().getStringArray(R.array.languages_spinner_array)[position];
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        TextView mFactoryUrlTextView = view.findViewById(R.id.url_text_view);
        String siteUrl = PersistenceManager.getInstance().getSiteUrl();
        if (siteUrl != null) {
            mFactoryUrlTextView.setText(PersistenceManager.getInstance().getSiteUrl());
        } else {
            mFactoryUrlTextView.setText(R.string.dashes);
        }

        mRefreshStatusTextView = view.findViewById(R.id.refresh_status_text_view);
        mRefreshStatusTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_checkmark, 0);
        mRefreshStatusTextView.setVisibility(View.GONE);

        mAdvancedSettingsButton = view.findViewById(R.id.advanced_settings_button);

        mRefreshButton = view.findViewById(R.id.refresh_button);
        Drawable drawable = getActivity().getDrawable(R.drawable.button_refresh_reportind_data_selector);
        if (drawable != null) {
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        }

        ScaleDrawable sd = new ScaleDrawable(drawable, 0, 1, 1);
        Configuration config = getResources().getConfiguration();
        if (config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
            mRefreshButton.setCompoundDrawables(sd.getDrawable(), null, null, null);
        } else {
            mRefreshButton.setCompoundDrawables(null, null, sd.getDrawable(), null);

        }

        mSaveButton = view.findViewById(R.id.button_save);
        mCancelButton = view.findViewById(R.id.button_cancel);
        mButtonChange = view.findViewById(R.id.button_change);
    }

    @Override
    public void onPause() {
        super.onPause();
        mAdvancedSettingsButton.setOnClickListener(null);
        mSaveButton.setOnClickListener(null);
        mCancelButton.setOnClickListener(null);
        mRefreshButton.setOnClickListener(null);
        mButtonChange.setOnClickListener(null);
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

    public void setActionBar() {

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
                @SuppressLint("InflateParams")
                View view = inflater.inflate(R.layout.settings_action_bar, null);

                LinearLayout buttonClose = view.findViewById(R.id.close_image);
                buttonClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentManager fragmentManager = getFragmentManager();
                        if (fragmentManager != null) {
                            fragmentManager.popBackStack();
                        }
                    }
                });
                actionBar.setCustomView(view);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.advanced_settings_button: {
                mGoToScreenListener.goToFragment(AdvancedSettingsFragment.newInstance(mSelectedLanguageCode), false, true);
                break;
            }
            case R.id.button_cancel: {
                if (getActivity() != null) {
                    getActivity().onBackPressed();
                }
                break;
            }
            case R.id.button_save: {
                if (mSelectedLanguageCode == null || mSelectedLanguageCode.equals(PersistenceManager.getInstance().getCurrentLang())) {
                    if (getActivity() != null) {
                        getActivity().onBackPressed();
                    }
                } else {
                    saveAlarmsCheckedLocaly();
                    PersistenceManager.getInstance().setCurrentLang(mSelectedLanguageCode);
                    PersistenceManager.getInstance().setCurrentLanguageName(mSelectedLanguageName);
                    mSettingsInterface.onRefreshApplicationRequest();
                }
                break;
            }
            case R.id.refresh_button: {
                if(NetworkAvailable.isNetworkAvailable(getActivity()))
                {
                    ProgressDialogManager.show(getActivity());
                    mSettingsInterface.onRefreshReportFieldsRequest(this);
                }
                break;
            }
            case R.id.button_change: {
                boolean isShowTutorial = PersistenceManager.getInstance().isDisplayToolbarTutorial();
                mSettingsInterface.onClearAppDataRequest();
                PersistenceManager.getInstance().setDisplayToolbarTutorial(isShowTutorial);
                break;
            }
        }
    }


    private void saveAlarmsCheckedLocaly() {
        //because alarms status not saved in sever side,
        // the goal is to clear the database on change language and load it completely on reopen (to get events true language)
        //and update the alarms if checked

        ArrayList<Integer> checkedAlarmList = new ArrayList<>();

        PersistenceManager.getInstance().setShiftLogStartingFrom(com.operatorsapp.utils.TimeUtils.getDate(System.currentTimeMillis() - (24 * 60 * 60 * 100), "yyyy-MM-dd HH:mm:ss.SSS"));

        DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());

        Cursor mTempCursor = databaseHelper.getCursorOrderByTime();

        while (mTempCursor.isLast()) {

            if (mTempCursor.getInt(mTempCursor.getColumnIndex(DatabaseHelper.KEY_TREATED)) == 1) {

                checkedAlarmList.add(mTempCursor.getInt(mTempCursor.getColumnIndex(DatabaseHelper.KEY_EVENT_ID)));
            }

            mTempCursor.moveToNext();
        }

        mTempCursor.close();

        PersistenceManager.getInstance().setCheckedAlarms(checkedAlarmList);
    }


    @Override
    public void onReportUpdatedSuccess() {
        OppAppLogger.getInstance().i(LOG_TAG, "onReportUpdatedSuccess()");
        mRefreshStatusTextView.setVisibility(View.VISIBLE);
        dismissProgressDialog();

    }

    @Override
    public void onReportUpdateFailure() {
        OppAppLogger.getInstance().i(LOG_TAG, "onReportUpdateFailure()");
        dismissProgressDialog();
    }

    private void dismissProgressDialog() {
        ProgressDialogManager.dismiss();
    }

    @Override
    public int getCroutonRoot()
    {
        return R.id.settings_crouton_root;
    }
}
