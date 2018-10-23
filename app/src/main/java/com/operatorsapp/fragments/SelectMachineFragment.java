package com.operatorsapp.fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.operators.infra.Machine;
import com.operatorsapp.R;
import com.operatorsapp.activities.interfaces.GoToScreenListener;
import com.operatorsapp.adapters.AutoCompleteAdapter;
import com.operatorsapp.application.OperatorApplication;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.utils.SoftKeyboardUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SelectMachineFragment extends BackStackAwareFragment implements AdapterView.OnItemClickListener
{
    public static final String LOG_TAG = SelectMachineFragment.class.getSimpleName();
    private static final String MACHINES_LIST = "machines_list";
    private GoToScreenListener mNavigationCallback;
    private AppCompatAutoCompleteTextView mSearchField;
    private RelativeLayout mGoButton;
    private ImageView mGoButtonBackground;
    private ArrayList<Machine> mMachinesList = new ArrayList<>();
    private int mMachineId;
    private AutoCompleteAdapter mAutoCompleteAdapter;
    private boolean canGoNext = false;

    public static SelectMachineFragment newInstance(ArrayList machinesList)
    {
        Gson gson = new Gson();
        String machinesListString = gson.toJson(machinesList);
        Bundle args = new Bundle();
        args.putString(MACHINES_LIST, machinesListString);

        SelectMachineFragment fragment = new SelectMachineFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        try
        {
            mNavigationCallback = (GoToScreenListener) context;
        } catch(ClassCastException e)
        {
            throw new ClassCastException("Calling fragment must implement OnCroutonRequestListener interface");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mNavigationCallback = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if(getArguments() != null)
        {
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<Machine>>()
            {}.getType();
            mMachinesList = gson.fromJson(getArguments().getString(MACHINES_LIST), listType);
        }

        // Analytics
        OperatorApplication application = (OperatorApplication) getActivity().getApplication();
        Tracker mTracker = application.getDefaultTracker();
        mTracker.setScreenName(LOG_TAG);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final View rootView = inflater.inflate(R.layout.fragment_select_machine, container, false);

        Collections.sort(mMachinesList, new Comparator<Machine>() {
            @Override
            public int compare(Machine o1, Machine o2) {
                if (OperatorApplication.isEnglishLang()){
                    return o1.getMachineEName().compareTo(o2.getMachineEName());
                }else {
                    return o1.getMachineLName().compareTo(o2.getMachineLName());
                }
            }
        });


        mSearchField = rootView.findViewById(R.id.machine_id_name);
        mAutoCompleteAdapter = new AutoCompleteAdapter(getActivity(), mMachinesList);
        mSearchField.setAdapter(mAutoCompleteAdapter);
        mSearchField.setOnItemClickListener(this);
        mSearchField.addTextChangedListener(mTextWatcher);
        mSearchField.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if(canGoNext)
                {
                    PersistenceManager.getInstance().setMachineId(mMachineId);
                    mNavigationCallback.goToDashboardActivity(mMachineId, null);
                }
                return true;
            }

        });
        mSearchField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchField.showDropDown();
            }
        });

        mGoButton = rootView.findViewById(R.id.goBtn);
        mGoButton.setEnabled(false);
        mGoButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(mGoButton.isEnabled())
                {
                    PersistenceManager.getInstance().setMachineId(mMachineId);
                    PersistenceManager.getInstance().setSelectedMachine(true);
                    mNavigationCallback.goToDashboardActivity(mMachineId, null);
                }
            }
        });
        mGoButtonBackground = rootView.findViewById(R.id.goBtn_background);

        setActionBar();
        if (getActivity() != null) {
            SoftKeyboardUtil.showKeyboard(getActivity());
        }

        return rootView;
    }

    protected void setActionBar()
    {
        if (getActivity() != null) {

            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (actionBar != null) {
                actionBar.setHomeButtonEnabled(false);
                actionBar.setDisplayHomeAsUpEnabled(false);
                actionBar.setDisplayShowTitleEnabled(false);
                actionBar.setDisplayShowCustomEnabled(true);
                actionBar.setDisplayUseLogoEnabled(true);
                LayoutInflater inflator = LayoutInflater.from(getActivity());
                // rootView null
                @SuppressLint("InflateParams") View view = inflator.inflate(R.layout.actionbar_title_view1, null);
                ImageView backButton = view.findViewById(R.id.action_bar_back_btn);
                backButton.setVisibility(View.VISIBLE);
                backButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getActivity().onBackPressed();
                    }
                });
                ((TextView) view.findViewById(R.id.title)).setText(getString(R.string.link_machine));
                actionBar.setCustomView(view);
                actionBar.setIcon(null);

            }
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mAutoCompleteAdapter != null && mAutoCompleteAdapter.getItem(position) != null) {
            mMachineId = mAutoCompleteAdapter.getItem(position).getId();
            String nameByLang = OperatorApplication.isEnglishLang() ? mAutoCompleteAdapter.getItem(position).getMachineEName() : mAutoCompleteAdapter.getItem(position).getMachineLName();
            String machineName = nameByLang == null ? "" : nameByLang;
            mSearchField.setText(new StringBuilder(mMachineId).append(" - ").append(machineName));
            mSearchField.setSelection(mSearchField.length());

            canGoNext = true;
            mGoButton.setEnabled(true);
            mGoButtonBackground.setImageResource(R.drawable.login_button_selector);
            mSearchField.dismissDropDown();
        }
    }

    private TextWatcher mTextWatcher = new TextWatcher()
    {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {
            canGoNext = false;
            mGoButton.setEnabled(false);
            mGoButtonBackground.setImageResource(R.drawable.button_bg_disabled);

        }

        @Override
        public void afterTextChanged(Editable s)
        {

        }
    };
}
