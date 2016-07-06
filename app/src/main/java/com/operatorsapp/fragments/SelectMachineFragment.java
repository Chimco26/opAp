package com.operatorsapp.fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.operators.getmachinesnetworkbridge.GetMachinesNetworkBridge;
import com.operators.getmachinesnetworkbridge.server.ErrorObject;
import com.operators.getmachinesnetworkbridge.server.requests.GetMachinesRequest;
import com.operators.getmachinesnetworkbridge.server.responses.ErrorResponse;
import com.operators.getmachinesnetworkbridge.server.responses.Machine;
import com.operators.getmachinesnetworkbridge.server.responses.MachinesResponse;
import com.operators.infra.ErrorObjectInterface;
import com.operators.infra.GetMachinesCallback;
import com.operators.logincore.LoginCore;
import com.operators.logincore.LoginUICallback;
import com.operatorsapp.R;
import com.operatorsapp.activities.interfaces.OnNavigationListener;
import com.operatorsapp.adapters.AutoCompleteAdapter;
import com.operatorsapp.application.OperatorApplication;
import com.operatorsapp.fragments.interfaces.OnCroutonRequestListener;
import com.operatorsapp.managers.LoginPersistenceManager;
import com.operatorsapp.managers.ProgressDialogManager;
import com.operatorsapp.server.NetworkManager;
import com.operatorsapp.utils.ShowCrouton;
import com.operatorsapp.utils.SoftKeyboardUtil;
import com.zemingo.logrecorder.ZLogger;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectMachineFragment extends Fragment implements AdapterView.OnItemClickListener {
    private static final String LOG_TAG = SelectMachineFragment.class.getSimpleName();
    private static final String MACHINES_LIST = "machines_list";
    private OnNavigationListener mNavigationCallback;
    private OnCroutonRequestListener mCroutonCallback;
    private AppCompatAutoCompleteTextView mSearchField;
    private RelativeLayout mGoButton;
    private ImageView mGoButtonBackground;
    private ArrayList<Machine> mMachinesList = new ArrayList<>();
    private int mMachineId;
    private Machine mMachine;
    private AutoCompleteAdapter mAutoCompleteAdapter;

    public static SelectMachineFragment newInstance(ArrayList machinesList) {
        Gson gson = new Gson();
        String machinesListString = gson.toJson(machinesList);
        Bundle args = new Bundle();
        args.putString(MACHINES_LIST, machinesListString);

        SelectMachineFragment fragment = new SelectMachineFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCroutonCallback = (OnCroutonRequestListener) getActivity();
            mNavigationCallback = (OnNavigationListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling fragment must implement OnCroutonRequestListener interface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCroutonCallback = null;
        mNavigationCallback = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<Machine>>() {
            }.getType();
            mMachinesList = gson.fromJson(getArguments().getString(MACHINES_LIST), listType);
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_select_machine, container, false);

        mSearchField = (AppCompatAutoCompleteTextView) rootView.findViewById(R.id.machine_id_name);
        mAutoCompleteAdapter = new AutoCompleteAdapter(getActivity(), mMachinesList);
        mSearchField.setAdapter(mAutoCompleteAdapter);
        mSearchField.setOnItemClickListener(this);
        mSearchField.addTextChangedListener(mTextWatcher);

        mGoButton = (RelativeLayout) rootView.findViewById(R.id.goBtn);
        mGoButton.setEnabled(false);
        mGoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mGoButton.isEnabled()) {
                    LoginPersistenceManager.getInstance().setMachineId(mMachineId);
                    mNavigationCallback.goToDashboardActivity(mMachine);
                }
            }
        });
        mGoButtonBackground = (ImageView) rootView.findViewById(R.id.goBtn_background);

        TextView title = (TextView) rootView.findViewById(R.id.select_machine_title);
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSum();
            }
        });
        setActionBar();
        SoftKeyboardUtil.showKeyboard(getActivity());
        return rootView;
    }

    private void setActionBar() {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayUseLogoEnabled(true);
            SpannableString s = new SpannableString(getString(R.string.login_screen_title));
            LayoutInflater inflator = LayoutInflater.from(getActivity());
            // rootView null
            @SuppressLint("InflateParams") View view = inflator.inflate(R.layout.actionbar_title_view, null);
            ((TextView) view.findViewById(R.id.title)).setText(s);
            actionBar.setCustomView(view);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mMachine = mAutoCompleteAdapter.getItem(position);
        mMachineId = mMachine.getId();
        String machineName = mMachine.getMachineName() == null ? "" : mMachine.getMachineName();
        mSearchField.setText(new StringBuilder(mMachineId + " - " + machineName));

        mGoButton.setEnabled(true);
        mGoButtonBackground.setImageResource(R.drawable.button_bg);
        mSearchField.dismissDropDown();
    }

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (before > count) {
                mGoButton.setEnabled(false);
                mGoButtonBackground.setImageResource(R.drawable.button_bg_disabled);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public void doSum() {
        GetMachinesRequest getMachinesRequest = new GetMachinesRequest(LoginPersistenceManager.getInstance().getSessionId());
        Call<MachinesResponse> call = NetworkManager.getInstance().getMachinesRetroFitServiceRequests(LoginPersistenceManager.getInstance().getSiteUrl(), 17, TimeUnit.SECONDS).getMachinesForFactory(getMachinesRequest);
        call.enqueue(new Callback<MachinesResponse>() {
            @Override
            public void onResponse(Call<MachinesResponse> call, Response<MachinesResponse> response) {
                if (response.body().getErrorResponse() == null) {
                    ArrayList<Machine> machines = response.body().getMachines();
                    if (machines != null && machines.size() > 0) {
                        ZLogger.d(LOG_TAG, "onRequestSucceed(), " + machines.size() + " machines");
                    } else {
                        ZLogger.d(LOG_TAG, "onRequest(), machines = 0");
                    }
                } else if (errorObjectWithErrorCode(response.body().getErrorResponse()).getError() == ErrorObjectInterface.ErrorCode.Credentials_mismatch || errorObjectWithErrorCode(response.body().getErrorResponse()).getError() == ErrorObjectInterface.ErrorCode.SessionInvalid) {
                    ZLogger.d(LOG_TAG, "onRequestFailed(), " + response.body().getErrorResponse().getErrorDesc());
                    //re-login if sessionId loss
                    doSilentLogin();
                } else {
                    ZLogger.d(LOG_TAG, "onRequestFailed(), " + response.body().getErrorResponse().getErrorDesc());
                }
            }

            @Override
            public void onFailure(Call<MachinesResponse> call, Throwable t) {
                ZLogger.d(LOG_TAG, "onRequestFailed(), " + t.getMessage());
            }
        });
    }

    private void doSilentLogin() {
        ProgressDialogManager.show(getActivity());
        OperatorApplication.silentLogin(new LoginUICallback() {
            @Override
            public void onLoginSucceeded(ArrayList machines) {
                Log.d(LOG_TAG, "login, onGetMachinesSucceeded(),  go Next");
                dismissProgressDialog();
                doSum();
            }

            @Override
            public void onLoginFailed(final ErrorObjectInterface reason) {
                dismissProgressDialog();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ShowCrouton.croutonError(mCroutonCallback, reason);
                    }
                });
            }
        });
    }

    private void dismissProgressDialog() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ProgressDialogManager.dismiss();
            }
        });
    }

    private ErrorObject errorObjectWithErrorCode(ErrorResponse errorResponse) {
        ErrorObject.ErrorCode code = toCode(errorResponse.getErrorCode());
        return new ErrorObject(code, errorResponse.getErrorDesc());
    }

    private ErrorObject.ErrorCode toCode(int errorCode) {
        switch (errorCode) {
            case 101:
                return ErrorObject.ErrorCode.Credentials_mismatch;
        }
        return ErrorObject.ErrorCode.Unknown;
    }

}
