package com.operatorsapp.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.operatorsapp.R;
import com.operatorsapp.interfaces.SettingsInterface;
import com.operatorsapp.managers.PersistenceManager;

/**
 * Created by Sergey on 17/08/2016.
 */
public class AdvancedSettingsFragment extends Fragment implements View.OnClickListener {

    private static final String SELECTED_LANGUAGE = "selected_language";
    private SettingsInterface mSettingsInterface;
    private String mSelectedLanguage;
    private EditText mPollingFrequencyEditText;
    private EditText mRequestTimeoutEditText;
    private Button mButtonSave;

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
    }

    @Override
    public void onResume() {
        super.onResume();
        mButtonSave.setOnClickListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
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


                break;
            }
        }
    }
}
