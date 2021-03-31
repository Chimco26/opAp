package com.operatorsapp.fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.common.QCModels.TestDetailsForm;
import com.example.common.QCModels.ValueList;
import com.example.common.SelectableString;
import com.example.common.StandardResponse;
import com.example.common.callback.GetDepartmentCallback;
import com.example.common.department.DepartmentMachine;
import com.example.common.department.DepartmentMachineValue;
import com.example.common.department.DepartmentsMachinesResponse;
import com.example.common.department.ProductionStatus;
import com.operatorsapp.R;
import com.operatorsapp.adapters.AutoCompleteAdapter;
import com.operatorsapp.adapters.DepartmentAdapter;
import com.operatorsapp.adapters.SimpleSpinnerAdapter;
import com.operatorsapp.application.OperatorApplication;
import com.operatorsapp.dialogs.ProgressDialogFragment;
import com.operatorsapp.managers.CroutonCreator;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.managers.ProgressDialogManager;
import com.operatorsapp.server.NetworkManager;
import com.operatorsapp.server.requests.ProductionModeForMachineRequest;
import com.operatorsapp.server.requests.UpdateWorkerRequest;
import com.operatorsapp.utils.ClearData;
import com.operatorsapp.utils.GoogleAnalyticsHelper;
import com.operatorsapp.utils.KeyboardUtils;
import com.operatorsapp.utils.SimpleRequests;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    private RelativeLayout mLoginLayout;
    private EditText mLoginIdEt;
    private RelativeLayout mMainLayoutTitle;
    private LinearLayout mBtnLayout;
    private Button mApplyMultiSelectBtn;
    private Spinner mStatusSpinner;
    private RelativeLayout mStatusLayout;


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
//        rootView.findViewById(R.id.FSM_change_factory_btn).setOnClickListener(this);
        departementRecyclerView = rootView.findViewById(R.id.FSM_department_rv);
        mSearchField = rootView.findViewById(R.id.machine_id_name);
        mGoButton = rootView.findViewById(R.id.goBtn);
        noDataTv = rootView.findViewById(R.id.FSM_no_data_tv);
        mProgress = rootView.findViewById(R.id.FSM_progress);
        mApplyMultiSelectBtn = rootView.findViewById(R.id.FSM_multi_select_apply_btn);
        mBtnLayout = rootView.findViewById(R.id.FSM_top_buttons_layout_lil);
        mLoginLayout = rootView.findViewById(R.id.FSM_operator_login_rl);
        mLoginIdEt = rootView.findViewById(R.id.FSM_operator_login_input_et);
        mMainLayoutTitle = rootView.findViewById(R.id.main);
        mStatusSpinner = rootView.findViewById(R.id.FSM_production_status_spn);
        mStatusLayout = rootView.findViewById(R.id.FSM_production_status_rl);

        mApplyMultiSelectBtn.setOnClickListener(this);
        mQcTestBtn.setOnClickListener(this);
        mSignInBtn.setOnClickListener(this);
        mChangeStatusBtn.setOnClickListener(this);

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

    public void initView() {
        if (mDepartmentMachine != null && mDepartmentMachine.getDepartmentMachine() != null && mDepartmentMachine.getDepartmentMachine().size() > 0) {
            mChangeStatusBtn.setVisibility(mDepartmentMachine.getUserGroupPermission().isChangeProductionStatus() ? View.VISIBLE : View.GONE);
            mSignInBtn.setVisibility(mDepartmentMachine.getUserGroupPermission().isOperatorLogin() ? View.VISIBLE : View.GONE);
            mQcTestBtn.setVisibility(mDepartmentMachine.getUserGroupPermission().isQualityTest() ? View.VISIBLE : View.GONE);

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

    }

    private void initDepartmentRv() {

        if (mDepartmentMachine.getDepartmentMachine() != null && mDepartmentMachine.getDepartmentMachine().size() > 0) {
            mDepartmentAdapter = new DepartmentAdapter(mDepartmentMachine.getDepartmentMachine(), this);

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

            departementRecyclerView.setLayoutManager(layoutManager);

            departementRecyclerView.setAdapter(mDepartmentAdapter);

            mDepartmentAdapter.setMultiSelection(false);
            mApplyMultiSelectBtn.setVisibility(View.GONE);
            mLoginLayout.setVisibility(View.GONE);
            mStatusLayout.setVisibility(View.GONE);
            mMainLayoutTitle.setVisibility(View.VISIBLE);
            mBtnLayout.setVisibility(View.VISIBLE);
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
                        getActivity().onBackPressed();
//                        if (mListener != null) {
//                            mListener.onCloseSelectMachine();
//                        }
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
            case R.id.FSM_multi_select_apply_btn:
                openConfirmationDialog();
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

    private ArrayList<String> getSelectedMachines() {
        return mDepartmentAdapter.getSelectedMachineList();
    }

    private ArrayList<String> getSelectedMachinesLineId() {
        return mDepartmentAdapter.getSelectedMachineLineIdList();
    }

    private void openConfirmationDialog() {
        final boolean isLogIn = mLoginLayout.getVisibility() == View.VISIBLE;
        if (isLogIn && getSelectedMachines().size() == 0 && mLoginIdEt.getText().toString().isEmpty()){
            Toast.makeText(getActivity(), getString(R.string.select_machines_and_worker_id), Toast.LENGTH_SHORT).show();
            return;
        }else if (!isLogIn && getSelectedMachines().size() == 0){
            Toast.makeText(getActivity(), getString(R.string.select_machines), Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getLayoutInflater().inflate(R.layout.title_text_btn_dialog, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();

        final ProgressBar progressBar = view.findViewById(R.id.DALJ_progress_pb);
        view.findViewById(R.id.DALJ_title_ic).setVisibility(View.GONE);
        view.findViewById(R.id.DALJ_title_tv).setVisibility(View.GONE);
        ((TextView)view.findViewById(R.id.DALJ_sub_title_tv)).setText(getResources().getString(R.string.confirm_machine_selection));
        view.findViewById(R.id.DALJ_positive_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {/// Button Yes
                if (isLogIn) {
                    signInSelectedMachines(dialog);
                }else {
                    setProductionModeForMachine(dialog);
                }
                progressBar.setVisibility(View.VISIBLE);
            }
        });
        view.findViewById(R.id.DALJ_negative_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {/// Button Cancel
                dialog.dismiss();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.show();
    }

    private void signInSelectedMachines(final AlertDialog dialog) {

        NetworkManager.getInstance().UpdateWorkerToJosh(getUpdateWorkerRequest(), new Callback<StandardResponse>() {
            @Override
            public void onResponse(Call<StandardResponse> call, Response<StandardResponse> response) {
                if (response.body() != null && response.body().isNoError()) {
                    initView();
                    if (dialog != null)
                        dialog.dismiss();
                    Toast.makeText(getActivity(), getString(R.string.signed_in_successfully), Toast.LENGTH_SHORT).show();
                }else {
                    if (response.body() != null) {
                        onFailure(call, new Throwable(response.body().getError().getErrorDesc()));
                    }else {
                        onFailure(call, new Throwable(""));
                    }
                }
            }

            @Override
            public void onFailure(Call<StandardResponse> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private UpdateWorkerRequest getUpdateWorkerRequest() {
        ArrayList<String> selectedMachines = new ArrayList<>();
        ArrayList<String> selectedLines = new ArrayList<>();
        for (DepartmentMachine department : mDepartmentMachine.getDepartmentMachine()) {
            for (DepartmentMachineValue machine : department.getDepartmentMachineValue()) {
                for (String machineId : getSelectedMachines()) {
                    if (machineId.equals(machine.getId())){
                        if (machine.getLineId() > 0) {
                            boolean addLine = true;
                            for (String lineId : selectedLines) {
                                if (lineId.equals(machine.getLineId())) {
                                    addLine = false;
                                    break;
                                }
                            }
                            if (addLine)
                                selectedLines.add(String.valueOf(machine.getLineId()));
                        }else {
                            selectedMachines.add(machineId);
                        }
                        break;
                    }
                }
            }
        }
        return new UpdateWorkerRequest(PersistenceManager.getInstance().getSessionId(), mLoginIdEt.getText().toString(), selectedMachines, selectedLines);
    }

    private ProductionModeForMachineRequest getProductionModeForMachineRequest() {
        ArrayList<String> selectedMachines = new ArrayList<>();
        ArrayList<String> selectedLines = new ArrayList<>();
        for (DepartmentMachine department : mDepartmentMachine.getDepartmentMachine()) {
            for (DepartmentMachineValue machine : department.getDepartmentMachineValue()) {
                for (String machineId : getSelectedMachines()) {
                    if (machineId.equals(machine.getId())){
                        if (machine.getLineId() > 0) {
                            boolean addLine = true;
                            for (String lineId : selectedLines) {
                                if (lineId.equals(machine.getLineId())) {
                                    addLine = false;
                                    break;
                                }
                            }
                            if (addLine)
                                selectedLines.add(String.valueOf(machine.getLineId()));
                        }else {
                            selectedMachines.add(machineId);
                        }
                        break;
                    }
                }
            }
        }
        int productionModeId = Integer.parseInt(((SimpleSpinnerAdapter)mStatusSpinner.getAdapter()).getItem(mStatusSpinner.getSelectedItemPosition()).getId());
        return new ProductionModeForMachineRequest(PersistenceManager.getInstance().getSessionId(), productionModeId, selectedMachines, selectedLines);
    }

    private void setProductionModeForMachine(final AlertDialog dialog) {
        NetworkManager.getInstance().postProductionModeForMachine(getProductionModeForMachineRequest(), new Callback<StandardResponse>() {
            @Override
            public void onResponse(Call<StandardResponse> call, Response<StandardResponse> response) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                if (response.body() != null && response.body().isNoError()) {
                    initView();
                    Toast.makeText(getActivity(), getString(R.string.success), Toast.LENGTH_SHORT).show();
                }else {
                    if (response.body() != null) {
                        onFailure(call, new Throwable(response.body().getError().getErrorDesc()));
                    }else {
                        onFailure(call, new Throwable(""));
                    }
                }
            }

            @Override
            public void onFailure(Call<StandardResponse> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setLoginMode() {
        mDepartmentAdapter.setMultiSelection(true);
        mApplyMultiSelectBtn.setVisibility(View.VISIBLE);
        mLoginLayout.setVisibility(View.VISIBLE);
        mMainLayoutTitle.setVisibility(View.GONE);
        mBtnLayout.setVisibility(View.GONE);
        mStatusLayout.setVisibility(View.GONE);
    }

    private void setStatusMode() {
        mDepartmentAdapter.setMultiSelection(true);
        mApplyMultiSelectBtn.setVisibility(View.VISIBLE);
        mStatusLayout.setVisibility(View.VISIBLE);
        mLoginLayout.setVisibility(View.GONE);
        mMainLayoutTitle.setVisibility(View.GONE);
        mBtnLayout.setVisibility(View.GONE);
        initSpinner();
    }

    private void initSpinner() {

        final ArrayList<SelectableString> statusList = new ArrayList<>();
        for (ProductionStatus status : mDepartmentMachine.getProductionStatuses()) {
            statusList.add(new SelectableString(status.getStatusName(), false, String.valueOf(status.getId())));
        }

        final SimpleSpinnerAdapter dataAdapter = new SimpleSpinnerAdapter(getActivity(), R.layout.base_spinner_item, statusList, true);
        dataAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item_custom);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mStatusSpinner.setAdapter(dataAdapter);
        mStatusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                dataAdapter.setTitle(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {            }
        });
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

    public boolean isMultiSelectMode() {
        return mApplyMultiSelectBtn.getVisibility() == View.VISIBLE;
    }

    public interface SelectMachineFragmentListener {
        void onChangeFactory();
        void onCloseSelectMachine();
        void onMachineSelected();
        void onQCTestSelected();
    }
}
