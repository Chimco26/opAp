package com.operatorsapp.fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.common.department.DepartmentMachineValue;
import com.example.common.department.DepartmentsMachinesResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.operatorsapp.R;
import com.operatorsapp.activities.interfaces.GoToScreenListener;
import com.operatorsapp.adapters.AutoCompleteAdapter;
import com.operatorsapp.adapters.DepartmentAdapter;
import com.operatorsapp.application.OperatorApplication;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.utils.ClearData;
import com.operatorsapp.utils.GoogleAnalyticsHelper;
import com.operatorsapp.utils.KeyboardUtils;

import java.lang.reflect.Type;

public class SelectMachineFragment extends BackStackAwareFragment implements AdapterView.OnItemClickListener, View.OnClickListener, DepartmentAdapter.DepartmentAdapterListener {
    public static final String LOG_TAG = SelectMachineFragment.class.getSimpleName();
    private static final String MACHINES_LIST = "machines_list";
    private GoToScreenListener mNavigationCallback;
    private AppCompatAutoCompleteTextView mSearchField;
    private ImageView mGoButton;
    private DepartmentsMachinesResponse mDepartmentMachine;
    private int mMachineId;
    private AutoCompleteAdapter mAutoCompleteAdapter;
    private boolean canGoNext = false;
    private String mMachineName;
    private DepartmentAdapter mDepartmentAdapter;


    public static SelectMachineFragment newInstance(DepartmentsMachinesResponse machinesList)
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
            Type listType = new TypeToken<DepartmentsMachinesResponse>()
            {}.getType();
            mDepartmentMachine = gson.fromJson(getArguments().getString(MACHINES_LIST), listType);
        }

        //Analytics
        new GoogleAnalyticsHelper().trackScreen(getActivity(), "Select machine");
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final View rootView = inflater.inflate(R.layout.fragment_select_machine, container, false);
        setActionBar();

//        Collections.sort(mDepartmentMachine, new Comparator<Machine>() {
//            @Override
//            public int compare(Machine o1, Machine o2) {
//                if (OperatorApplication.isEnglishLang()){
//                    return o1.getMachineEName().compareTo(o2.getMachineEName());
//                }else {
//                    return o1.getMachineLName().compareTo(o2.getMachineLName());
//                }
//            }
//        });

        rootView.findViewById(R.id.FSM_change_factory_btn).setOnClickListener(this);

        mSearchField = rootView.findViewById(R.id.machine_id_name);
        mGoButton = rootView.findViewById(R.id.goBtn);
        if (mDepartmentMachine != null && mDepartmentMachine.getDepartmentMachine() != null && mDepartmentMachine.getDepartmentMachine().size() > 0) {
            initDepartmentRv(rootView);
            mSearchField.addTextChangedListener(mTextWatcher);
            mGoButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
//                if(mGoButton.isEnabled())
//                {
//                    setMachineData();
//                }
                    KeyboardUtils.closeKeyboard(getActivity());
                }
            });
        }else {
            mSearchField.setVisibility(View.GONE);
            mGoButton.setVisibility(View.GONE);
            rootView.findViewById(R.id.FSM_no_data_tv).setVisibility(View.VISIBLE);
        }
//        mAutoCompleteAdapter = new AutoCompleteAdapter(getActivity(), mDepartmentMachine);
//        mSearchField.setAdapter(mAutoCompleteAdapter);
//        mSearchField.setOnItemClickListener(this);
//        mSearchField.addTextChangedListener(mTextWatcher);
//        mSearchField.setOnEditorActionListener(new TextView.OnEditorActionListener()
//        {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
//            {
//                if(canGoNext)
//                {
//
//                    PersistenceManager.getInstance().setMachineId(mMachineId);
//                    PersistenceManager.getInstance().setMachineName(mMachineName);
//                    PersistenceManager.getInstance().setSelectedMachine(true);
//                    PersistenceManager.getInstance().setNeedUpdateToken(true);
//                    mNavigationCallback.goToDashboardActivity(mMachineId, null);
//                }
//                return true;
//            }
//
//        });
//        mSearchField.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mSearchField.showDropDown();
//            }
//        });

//        mGoButton.setEnabled(false);

//        if (getActivity() != null) {
//            SoftKeyboardUtil.showKeyboard(getActivity());
//        }

        return rootView;
    }

    public void setMachineData() {
        PersistenceManager.getInstance().setMachineId(mMachineId);
        PersistenceManager.getInstance().setMachineName(mMachineName);
        PersistenceManager.getInstance().setSelectedMachine(true);
        PersistenceManager.getInstance().setNeedUpdateToken(true);
        mNavigationCallback.goToDashboardActivity(mMachineId, null);
    }

    private void initDepartmentRv(View rootView) {

        if (mDepartmentMachine.getDepartmentMachine() != null && mDepartmentMachine.getDepartmentMachine().size() > 0) {
            mDepartmentAdapter = new DepartmentAdapter(mDepartmentMachine.getDepartmentMachine(), this);

            RecyclerView recyclerView = rootView.findViewById(R.id.FSM_department_rv);

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

            recyclerView.setLayoutManager(layoutManager);

            recyclerView.setAdapter(mDepartmentAdapter);
        }
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
            mMachineName = nameByLang == null ? "" : nameByLang;
            mSearchField.setText(new StringBuilder(mMachineId).append(" - ").append(mMachineName));
            mSearchField.setSelection(mSearchField.length());

            canGoNext = true;
            mGoButton.setEnabled(true);

//            mGoButtonBackground.setImageResource(R.drawable.login_button_selector);
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
//            mGoButton.setEnabled(false);
            mDepartmentAdapter.setSearchFilter(s.toString());
            mDepartmentAdapter.getFilter().filter(s);
            //mGoButtonBackground.setImageResource(R.drawable.button_bg_disabled);

        }

        @Override
        public void afterTextChanged(Editable s)
        {

        }
    };

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.FSM_change_factory_btn:

                if (mNavigationCallback != null){
                    ClearData.clearData();
                    mNavigationCallback.goToFragment(LoginFragment.newInstance(false), false, false);

                }
                break;
        }

    }

    @Override
    public void onMachineSelected(DepartmentMachineValue departmentMachineValue) {
        mMachineId = departmentMachineValue.getId();
        mMachineName = departmentMachineValue.getMachineName();
        setMachineData();
    }
}
