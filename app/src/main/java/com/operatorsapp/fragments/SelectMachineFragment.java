package com.operatorsapp.fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.common.StandardResponse;
import com.example.common.callback.GetDepartmentCallback;
import com.example.common.department.DepartmentMachineValue;
import com.example.common.department.DepartmentsMachinesResponse;
import com.operatorsapp.R;
import com.operatorsapp.activities.interfaces.GoToScreenListener;
import com.operatorsapp.adapters.AutoCompleteAdapter;
import com.operatorsapp.adapters.DepartmentAdapter;
import com.operatorsapp.application.OperatorApplication;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.managers.ProgressDialogManager;
import com.operatorsapp.server.NetworkManager;
import com.operatorsapp.utils.ClearData;
import com.operatorsapp.utils.GoogleAnalyticsHelper;
import com.operatorsapp.utils.KeyboardUtils;
import com.operatorsapp.utils.SimpleRequests;

import static com.operatorsapp.managers.PersistenceManager.setMachineData;

public class SelectMachineFragment extends BackStackAwareFragment implements AdapterView.OnItemClickListener, View.OnClickListener, DepartmentAdapter.DepartmentAdapterListener {
    public static final String LOG_TAG = SelectMachineFragment.class.getSimpleName();
    private static final String MACHINES_LIST = "machines_list";
//    private GoToScreenListener mNavigationCallback;
    private AppCompatAutoCompleteTextView mSearchField;
    private ImageView mGoButton;
    private DepartmentsMachinesResponse mDepartmentMachine;
    private int mMachineId;
    private AutoCompleteAdapter mAutoCompleteAdapter;
    private boolean canGoNext = false;
    private String mMachineName;
    private DepartmentAdapter mDepartmentAdapter;
    private RecyclerView departementRecyclerView;
    private TextView noDataTv;
    private SelectMachineFragmentListener mListener;
    private ProgressBar mProgress;
    private Button mQcTestBtn;
    private Button mSignInBtn;
    private Button mChangeStatusBtn;


    public static SelectMachineFragment newInstance() {
        return new SelectMachineFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (SelectMachineFragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling fragment must implement SelectMachineFragmentListener interface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Analytics
        new GoogleAnalyticsHelper().trackScreen(getActivity(), "Select machine");
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_select_machine, container, false);
        setActionBar();

        mChangeStatusBtn = rootView.findViewById(R.id.FSM_change_production_status_btn);
        mSignInBtn = rootView.findViewById(R.id.FSM_operator_login_btn);
        mQcTestBtn = rootView.findViewById(R.id.FSM_qc_test_btn);
        mQcTestBtn.setOnClickListener(this);
        mSignInBtn.setOnClickListener(this);
        mChangeStatusBtn.setOnClickListener(this);
//        rootView.findViewById(R.id.FSM_change_factory_btn).setOnClickListener(this);
        departementRecyclerView = rootView.findViewById(R.id.FSM_department_rv);
        mSearchField = rootView.findViewById(R.id.machine_id_name);
        mGoButton = rootView.findViewById(R.id.goBtn);
        noDataTv = rootView.findViewById(R.id.FSM_no_data_tv);
        mProgress = rootView.findViewById(R.id.FSM_progress);
        getDepartmentsMachines();
        return rootView;
    }


    private void getDepartmentsMachines() {
        mProgress.setVisibility(View.VISIBLE);
        SimpleRequests.getDepartmentsMachines(PersistenceManager.getInstance().getSiteUrl(), new GetDepartmentCallback() {
            @Override
            public void onGetDepartmentSuccess(DepartmentsMachinesResponse response) {
                mProgress.setVisibility(View.GONE);
                mDepartmentMachine = response;
                initView();
            }

            @Override
            public void onGetDepartmentFailed(StandardResponse reason) {
                mProgress.setVisibility(View.GONE);

            }
        }, NetworkManager.getInstance(), PersistenceManager.getInstance().getTotalRetries(), PersistenceManager.getInstance().getRequestTimeout());

    }

    private void initView() {
        if (mDepartmentMachine != null && mDepartmentMachine.getDepartmentMachine() != null && mDepartmentMachine.getDepartmentMachine().size() > 0) {
            initDepartmentRv();
            mSearchField.addTextChangedListener(mTextWatcher);
            mGoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    KeyboardUtils.closeKeyboard(getActivity());
                }
            });
        } else {
            mSearchField.setVisibility(View.GONE);
            mGoButton.setVisibility(View.GONE);
            noDataTv.setVisibility(View.VISIBLE);
        }

//        if (mDepartmentMachine != null ){
//            mQcTestBtn.setVisibility(mDepartmentMachine.getUserGroupPermission().isQualityTest() ? View.VISIBLE : View.INVISIBLE);
//            mQcTestBtn.setVisibility(mDepartmentMachine.getUserGroupPermission().isChangeProductionStatus() ? View.VISIBLE : View.INVISIBLE);
//            mQcTestBtn.setVisibility(mDepartmentMachine.getUserGroupPermission().isOperatorLogin() ? View.VISIBLE : View.INVISIBLE);
//        }
    }

    private void initDepartmentRv() {

        if (mDepartmentMachine.getDepartmentMachine() != null && mDepartmentMachine.getDepartmentMachine().size() > 0) {
            mDepartmentAdapter = new DepartmentAdapter(mDepartmentMachine.getDepartmentMachine(), this);

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

            departementRecyclerView.setLayoutManager(layoutManager);

            departementRecyclerView.setAdapter(mDepartmentAdapter);
        }
    }

    protected void setActionBar() {
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
                if (getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
                    backButton.setRotationY(180);
                }
                backButton.setVisibility(View.VISIBLE);
                backButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener != null) {
                            mListener.onCloseSelectMachine();
                        }
                    }
                });
                ((TextView) view.findViewById(R.id.title)).setText(getString(R.string.link_machine));
                actionBar.setCustomView(view);
                actionBar.setIcon(null);

                TextView changeFactoryTv = view.findViewById(R.id.change_factory);
                changeFactoryTv.setVisibility(View.VISIBLE);
                changeFactoryTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ClearData.clearData();
                        mListener.onChangeFactory();
                    }
                });

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

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            canGoNext = false;
//            mGoButton.setEnabled(false);
            mDepartmentAdapter.setSearchFilter(s.toString());
            mDepartmentAdapter.getFilter().filter(s);
            //mGoButtonBackground.setImageResource(R.drawable.button_bg_disabled);

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.FSM_qc_test_btn:
                mListener.onQCTestSelected();
                break;
            case R.id.FSM_operator_login_btn:
                setLoginMode();
                break;
            case R.id.FSM_change_production_status_btn:
                setStatusMode();
                break;
            case R.id.FSM_change_factory_btn:

//                if (mNavigationCallback != null){
                ClearData.clearData();
//                    mNavigationCallback.goToFragment(LoginFragment.newInstance(false), false, false);
                if (mListener != null) {
                    mListener.onChangeFactory();
                }
//                }
                break;
        }

    }

    private void setLoginMode() {

    }

    private void setStatusMode() {

    }

    @Override
    public void onMachineSelected(DepartmentMachineValue departmentMachineValue) {
        ClearData.clearMachineData();
        mMachineId = departmentMachineValue.getId();
        mMachineName = departmentMachineValue.getMachineName();
        setMachineData(departmentMachineValue.getId(), departmentMachineValue.getMachineName());
        PersistenceManager.getInstance().setMachineLineId(departmentMachineValue.getLineId());
//        mNavigationCallback.goToDashboardActivity(mMachineId, null);

        if (mListener != null) {
            mListener.onMachineSelected();
        }
    }

    public interface SelectMachineFragmentListener {
        void onChangeFactory();
        void onCloseSelectMachine();
        void onMachineSelected();
        void onQCTestSelected();
    }
}
