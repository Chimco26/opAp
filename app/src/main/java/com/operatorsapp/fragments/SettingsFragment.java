package com.operatorsapp.fragments;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.common.StandardResponse;
import com.example.oppapplog.OppAppLogger;
import com.operatorsapp.BuildConfig;
import com.operatorsapp.R;
import com.operatorsapp.activities.interfaces.GoToScreenListener;
import com.operatorsapp.adapters.LanguagesSpinnerAdapter;
import com.operatorsapp.fragments.interfaces.OnReportFieldsUpdatedCallbackListener;
import com.operatorsapp.interfaces.CroutonRootProvider;
import com.operatorsapp.interfaces.SettingsInterface;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.managers.ProgressDialogManager;
import com.operatorsapp.server.NetworkManager;
import com.operatorsapp.server.requests.PostNotificationTokenRequest;
import com.operatorsapp.utils.NetworkAvailable;
import com.operatorsapp.utils.SaveAlarmsHelper;

import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsFragment extends BackStackAwareFragment implements View.OnClickListener, OnReportFieldsUpdatedCallbackListener, CroutonRootProvider {

    private static final String LOG_TAG = SettingsFragment.class.getSimpleName();
    private Spinner mLanguagesSpinner;
    private TextView mRefreshStatusTextView;
    private TextView mAdvancedSettingsButton;
    private TextView mRefreshButton;
    private TextView mCancelButton;
    private TextView mSaveButton;
    private String mSelectedLanguageCode;
    private String mSelectedLanguageName;
//    private Button mButtonChange;
    private GoToScreenListener mGoToScreenListener;
    private SettingsInterface mSettingsInterface;
    private TextView mButtonChangeMachine;
    private TextView mUpdateBtn;

    public static SettingsFragment newInstance() {

        return new SettingsFragment();
    }

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
        ((TextView) view.findViewById(R.id.FS_version)).setText(String.valueOf(BuildConfig.VERSION_NAME));
        ((TextView) view.findViewById(R.id.FS_username)).setText(String.valueOf(PersistenceManager.getInstance().getUserName()));

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

        String machineName = String.valueOf(PersistenceManager.getInstance().getMachineName());
        if (machineName != null)
            ((TextView) view.findViewById(R.id.machine_name_text_view)).setText(machineName);


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
//        mButtonChange = view.findViewById(R.id.button_change);
        mButtonChangeMachine = view.findViewById(R.id.button_change_machine);
        mUpdateBtn = view.findViewById(R.id.update_app_button);
    }

    @Override
    public void onPause() {
        super.onPause();
        mAdvancedSettingsButton.setOnClickListener(null);
        mSaveButton.setOnClickListener(null);
        mCancelButton.setOnClickListener(null);
        mRefreshButton.setOnClickListener(null);
//        mButtonChange.setOnClickListener(null);
        mButtonChangeMachine.setOnClickListener(null);
        mUpdateBtn.setOnClickListener(null);
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdvancedSettingsButton.setOnClickListener(this);
        mSaveButton.setOnClickListener(this);
        mCancelButton.setOnClickListener(this);
        mRefreshButton.setOnClickListener(this);
//        mButtonChange.setOnClickListener(this);
        mButtonChangeMachine.setOnClickListener(this);
        mUpdateBtn.setOnClickListener(this);
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
                        if (getActivity() != null) {
                            getActivity().onBackPressed();
                        }
//                        getChildFragmentManager().popBackStack();
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
                    SaveAlarmsHelper.saveAlarmsCheckedLocaly(getActivity());
                    PersistenceManager.getInstance().setCurrentLang(mSelectedLanguageCode);
                    PersistenceManager.getInstance().setCurrentLanguageName(mSelectedLanguageName);
                    sendTokenWithSessionIdToServer();
                }
                break;
            }
            case R.id.refresh_button: {
                if (NetworkAvailable.isNetworkAvailable(getActivity())) {
                    ProgressDialogManager.show(getActivity());
                    mSettingsInterface.onRefreshReportFieldsRequest(this);
                }
                break;
            }
            case R.id.update_app_button:
                mSettingsInterface.onCheckForAppUpdates(true);
                getActivity().onBackPressed();
                break;
//            case R.id.button_change: {
//                boolean isShowTutorial = PersistenceManager.getInstance().isDisplayToolbarTutorial();
//                mSettingsInterface.onClearAppDataRequest();
//                PersistenceManager.getInstance().setDisplayToolbarTutorial(isShowTutorial);
//                break;
//            }

            case R.id.button_change_machine: {//todo kuti
                mButtonChangeMachine.setOnClickListener(null);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (getActivity() != null && !getActivity().isDestroyed()) {
                            mButtonChangeMachine.setOnClickListener(SettingsFragment.this);
                        }
                    }
                }, 2000);
                mSettingsInterface.onChangeMachineRequest();
                break;
            }
        }
    }

    private void sendTokenWithSessionIdToServer() {
        final PersistenceManager pm = PersistenceManager.getInstance();
        final String id = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
        PostNotificationTokenRequest request = new PostNotificationTokenRequest(pm.getSessionId(), pm.getMachineId(), pm.getNotificationToken(), id);
        NetworkManager.getInstance().postNotificationToken(request, new Callback<StandardResponse>() {
            @Override
            public void onResponse(Call<StandardResponse> call, Response<StandardResponse> response) {
                if (response != null && response.body() != null && response.isSuccessful()) {
                    Log.d(LOG_TAG, "token sent");
                    mSettingsInterface.onRefreshApplicationRequest();

                } else {
                    Log.d(LOG_TAG, "token failed");
                }
            }

            @Override
            public void onFailure(Call<StandardResponse> call, Throwable t) {
                Log.d(LOG_TAG, "token failed");
            }
        });

    }


    @Override
    public void onReportUpdatedSuccess() {
        OppAppLogger.i(LOG_TAG, "onReportUpdatedSuccess()");
        mRefreshStatusTextView.setVisibility(View.VISIBLE);
        dismissProgressDialog();

    }

    @Override
    public void onReportUpdateFailure() {
        OppAppLogger.i(LOG_TAG, "onReportUpdateFailure()");
        dismissProgressDialog();
    }

    private void dismissProgressDialog() {
        ProgressDialogManager.dismiss();
    }

    @Override
    public int getCroutonRoot() {
        return R.id.settings_crouton_root;
    }

    public void updateApp() {

    }

}
