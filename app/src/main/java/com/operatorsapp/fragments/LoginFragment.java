package com.operatorsapp.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.common.StandardResponse;
import com.example.common.callback.GetDepartmentCallback;
import com.example.common.department.DepartmentsMachinesResponse;
import com.example.oppapplog.OppAppLogger;
import com.operators.infra.Machine;
import com.operators.logincore.LoginCore;
import com.operators.logincore.interfaces.LoginUICallback;
import com.operators.reportrejectinfra.GetVersionCallback;
import com.operators.reportrejectnetworkbridge.server.response.Recipe.VersionResponse;
import com.operatorsapp.BuildConfig;
import com.operatorsapp.R;
import com.operatorsapp.activities.interfaces.GoToScreenListener;
import com.operatorsapp.fragments.interfaces.OnCroutonRequestListener;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.managers.ProgressDialogManager;
import com.operatorsapp.server.NetworkManager;
import com.operatorsapp.utils.GoogleAnalyticsHelper;
import com.operatorsapp.utils.ShowCrouton;
import com.operatorsapp.utils.SimpleRequests;

import java.util.ArrayList;
import java.util.List;

import static android.text.format.DateUtils.DAY_IN_MILLIS;

public class LoginFragment extends Fragment {
    private static final String LOG_TAG = LoginFragment.class.getSimpleName();
    private final static String GO_TO_SELECT_MACHINE = "GO_TO_SELECT_MACHINE";
    private GoToScreenListener mNavigationCallback;
    private OnCroutonRequestListener mCroutonCallback;
    private EditText mSiteUrl;
    private EditText mUserName;
    private EditText mPassword;
    private TextView mLoginButton;
    private TextView mLoginBtnBackground;
    private ImageView mShowHidePass;
    private boolean mPasswordIsVisible = false;
    private boolean mGoToSelectMachine;

    public static LoginFragment newInstance(boolean goToSelectMachine) {

        LoginFragment loginFragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putBoolean(GO_TO_SELECT_MACHINE, goToSelectMachine);
        loginFragment.setArguments(args);

        return loginFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        // Analytics
        new GoogleAnalyticsHelper().trackScreen(getActivity(), "Login screen");

        if (getArguments() != null) {
            mGoToSelectMachine = getArguments().getBoolean(GO_TO_SELECT_MACHINE);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mNavigationCallback = (GoToScreenListener) context;
            mCroutonCallback = (OnCroutonRequestListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling fragment must implement interface");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        mSiteUrl = rootView.findViewById(R.id.factory_url);
        mSiteUrl.setText(PersistenceManager.getInstance().getSiteUrl());
        mUserName = rootView.findViewById(R.id.user_name);
        mPassword = rootView.findViewById(R.id.password);
//        mLoginBtnBackground = rootView.findViewById(R.id.loginBtn_background);

        mPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (isAllFieldsAreValid()) {
                    tryToLogin();
                }
                return true;
            }

        });

        mSiteUrl.addTextChangedListener(mTextWatcher);
        mUserName.addTextChangedListener(mTextWatcher);
        mPassword.addTextChangedListener(mTextWatcher);
        mShowHidePass = rootView.findViewById(R.id.show_hide_pass);
        mShowHidePass.setEnabled(false);
        mShowHidePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mShowHidePass.isEnabled()) {
                    if (!mPasswordIsVisible) {
                        mPassword.setTransformationMethod(null);
                        mPassword.setSelection(mPassword.length());
                        mShowHidePass.setImageResource(R.drawable.icn_show_password);
                        mPasswordIsVisible = true;
                    } else {
                        mPassword.setTransformationMethod(new PasswordTransformationMethod());
                        mPassword.setSelection(mPassword.length());
                        mShowHidePass.setImageResource(R.drawable.icn_password_hidden);
                        mPasswordIsVisible = false;
                    }
                }
            }
        });


        setActionBar();

        if (PersistenceManager.getInstance().isSelectedMachine()) {
            doSilentLogin();
        }

        ravtechTest(rootView);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        setActionBar();
    }

    private void ravtechTest(View rootView) {

        rootView.findViewById(R.id.ravtech_test).setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                mSiteUrl.setText("dev");
                mUserName.setText("ravtech");
                mPassword.setText("ravTech@1");
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLoginButton = view.findViewById(R.id.loginBtn);
        mLoginButton.setEnabled(false);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLoginButton.isEnabled()) {
                    tryToLogin();
                }
            }
        });

    }

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (mLoginButton != null) {
                mLoginButton.setEnabled(isAllFieldsAreValid());
                if (isAllFieldsAreValid()) {
                    mLoginButton.setBackground(getResources().getDrawable(R.drawable.login_button_selector));
                } else {
                    mLoginButton.setBackground(getResources().getDrawable(R.drawable.button_bg_disabled));
                }

                if (!TextUtils.isEmpty(mPassword.getText().toString())) {
                    if (!mPasswordIsVisible) {
                        mShowHidePass.setImageResource(R.drawable.icn_password_hidden);
                    } else {
                        mShowHidePass.setImageResource(R.drawable.icn_show_password);
                    }
                    mShowHidePass.setEnabled(true);
                } else {
                    mShowHidePass.setImageResource(R.drawable.icn_password_disabled);
                    mShowHidePass.setEnabled(false);
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void setActionBar() {
        if (getActivity() != null) {
            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (actionBar != null) {
                LayoutInflater inflator = LayoutInflater.from(getActivity());
                @SuppressLint("InflateParams") View view = inflator.inflate(R.layout.actionbar_title_view1, null);
                ImageView backButton = view.findViewById(R.id.action_bar_back_btn);
                backButton.setVisibility(View.GONE);
                ((TextView) view.findViewById(R.id.title)).setText("");
                actionBar.setCustomView(view);
                actionBar.setHomeButtonEnabled(false);
                actionBar.setDisplayHomeAsUpEnabled(false);
                actionBar.setDisplayShowTitleEnabled(false);
                actionBar.setDisplayShowCustomEnabled(true);
                actionBar.setDisplayUseLogoEnabled(true);
                if (BuildConfig.FLAVOR.equals(getString(R.string.lenox_flavor_name))) {
                    actionBar.setIcon(R.drawable.lenox_logo_new_medium);
                } else {
                    actionBar.setIcon(R.drawable.logo_new_medium);
                }
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCroutonCallback = null;
        mNavigationCallback = null;
    }

    private boolean isAllFieldsAreValid() {
        String factoryUrl = mSiteUrl.getText().toString();
        String userName = mUserName.getText().toString();
        String password = mPassword.getText().toString();
        return !TextUtils.isEmpty(factoryUrl) && !TextUtils.isEmpty(userName) && !TextUtils.isEmpty(password);
    }

    private void tryToLogin() {
        mNavigationCallback.isTryToLogin(true);
        PersistenceManager.getInstance().setShiftLogStartingFrom(com.operatorsapp.utils.TimeUtils.getDate(System.currentTimeMillis() - DAY_IN_MILLIS, "yyyy-MM-dd HH:mm:ss.SSS"));

        ProgressDialogManager.show(getActivity());//todo kuti
        String siteUrl = mSiteUrl.getText().toString();
        String userName = mUserName.getText().toString().toLowerCase();
        String password = mPassword.getText().toString().toLowerCase();

        // Ohad change 30/4/17
        if (!siteUrl.toLowerCase().startsWith("https://")) {
            siteUrl = String.format("https://api%1$s.my.leadermes.com", siteUrl);
        }

        final String finalSiteUrl = siteUrl;
        LoginCore.getInstance().login(siteUrl, userName, password, new LoginUICallback<Machine>() {
            @Override
            public void onLoginSucceeded(ArrayList<Machine> machines, String siteName) {
                OppAppLogger.getInstance().d(LOG_TAG, "login, onGetMachinesSucceeded() ");

//                getVersion(machines, true);
                //getNotifications();
                PersistenceManager.getInstance().setSiteName(siteName);

                getDepartmentsMachines(machines, finalSiteUrl, true);

            }

            @Override
            public void onLoginFailed(final StandardResponse reason) {
                dismissProgressDialog();
                mCroutonCallback.onHideConnectivityCroutonRequest();
                ShowCrouton.jobsLoadingErrorCrouton(mCroutonCallback, reason);
                mNavigationCallback.isTryToLogin(false);
            }
        });
    }

    private void getDepartmentsMachines(final ArrayList<Machine> machines, String finalSiteUrl, final boolean isTryToLogin) {

        SimpleRequests.getDepartmentsMachines(finalSiteUrl, new GetDepartmentCallback() {
            @Override
            public void onGetDepartmentSuccess(DepartmentsMachinesResponse response) {
                getVersion(machines, isTryToLogin, response);
            }

            @Override
            public void onGetDepartmentFailed(StandardResponse reason) {

            }
        }, NetworkManager.getInstance(), PersistenceManager.getInstance().getTotalRetries(), PersistenceManager.getInstance().getRequestTimeout());

    }

    private void tryToLoginSuccess(DepartmentsMachinesResponse machines) {
        dismissProgressDialog();
        if (mNavigationCallback != null) {

//            if (BuildConfig.FLAVOR.equals(getString(R.string.emerald_flavor_name))) {

            mNavigationCallback.goToFragment(SelectMachineFragment.newInstance(machines), true, true);

//            } else if (BuildConfig.FLAVOR.equals(getString(R.string.lenox_flavor_name))) {
//
//                PersistenceManager.getInstance().setSelectedMachine(true);
//
//                loginSuccess(machines);
//            }
            //TODO Lenox uncomment

            mNavigationCallback.isTryToLogin(false);
        }
    }

    // Silent - setUsername & password from preferences, It is only when preferences.isSelectedMachine().
    private void doSilentLogin() {
        mNavigationCallback.isTryToLogin(true);
        ProgressDialogManager.show(getActivity());//todo kuti
        LoginCore.getInstance().login(PersistenceManager.getInstance().getSiteUrl(), PersistenceManager.getInstance().getUserName(), PersistenceManager.getInstance().getPassword(), new LoginUICallback<Machine>() {
            @Override
            public void onLoginSucceeded(ArrayList<Machine> machines, String siteName) {
                OppAppLogger.getInstance().d(LOG_TAG, "login, onGetMachinesSucceeded(),  go Next");
                // getNotifications();

//                if (mGoToSelectMachine) {
//
//                    getVersion(machines, true);
//                } else {
//
//                }
                getDepartmentsMachines(machines, PersistenceManager.getInstance().getSiteUrl(), false);
                PersistenceManager.getInstance().setSiteName(siteName);
            }

            @Override
            public void onLoginFailed(final StandardResponse reason) {
                dismissProgressDialog();
                if (mCroutonCallback != null) {
                    mCroutonCallback.onHideConnectivityCroutonRequest();
                    ShowCrouton.jobsLoadingErrorCrouton(mCroutonCallback, reason);
                }
                if (mNavigationCallback != null) {
                    mNavigationCallback.isTryToLogin(false);
                }
            }
        });
    }

    private void loginSuccess(ArrayList<Machine> machines, DepartmentsMachinesResponse departmentsMachinesResponse) {
        dismissProgressDialog();
        if (mNavigationCallback != null) {
            if (mGoToSelectMachine) {
                mNavigationCallback.goToFragment(SelectMachineFragment.newInstance(departmentsMachinesResponse), false, false);
            } else {
                mNavigationCallback.goToDashboardActivity(PersistenceManager.getInstance().getMachineId(), machines);
            }
            mNavigationCallback.isTryToLogin(false);
        }
    }

//    private void getNotifications(){
//
//        NetworkManager.getInstance().getNotificationHistory(new Callback<NotificationHistoryResponse>() {
//            @Override
//            public void onResponse(Call<NotificationHistoryResponse> call, Response<NotificationHistoryResponse> response) {
//
//                if (response != null && response.body() != null && response.body().getError() == null) {
//
//                    for (Notification not : response.body().getmNotificationsList()) {
//                        not.setmSentTime(TimeUtils.getStringNoTFormatForNotification(not.getmSentTime()));
//                        not.setmResponseDate(TimeUtils.getStringNoTFormatForNotification(not.getmResponseDate()));
//                    }
//
//                    PersistenceManager.getInstance().setNotificationHistory(response.body().getmNotificationsList());
//                }else {
//                    PersistenceManager.getInstance().setNotificationHistory(null);
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<NotificationHistoryResponse> call, Throwable t) {
//
//                PersistenceManager.getInstance().setNotificationHistory(null);
//
//            }
//        });
//
//    }

    private void getVersion(final ArrayList<Machine> machines, final boolean isTryTologin, final DepartmentsMachinesResponse departmentsMachinesResponse) {

        SimpleRequests simpleRequests = new SimpleRequests();

        final PersistenceManager persistanceManager = PersistenceManager.getInstance();

        simpleRequests.getVersion(persistanceManager.getSiteUrl(), new GetVersionCallback() {
            @Override
            public void onGetVersionSuccess(Object response) {

                List<VersionResponse> versionResponses = (List<VersionResponse>) response;

                if (versionResponses != null && versionResponses.get(0) != null) {

                    persistanceManager.setVersion(Float.parseFloat((String) (versionResponses.get(0).getVersion().subSequence(0, 3))));
                }

                if (isTryTologin) {

                    tryToLoginSuccess(departmentsMachinesResponse);

                } else {

                    loginSuccess(machines, departmentsMachinesResponse);

                }

            }

            @Override
            public void onGetVersionFailed(StandardResponse reason) {

                if (isTryTologin) {

                    tryToLoginSuccess(departmentsMachinesResponse);

                } else {

                    loginSuccess(machines, departmentsMachinesResponse);

                }

            }

        }, NetworkManager.getInstance(), persistanceManager.getTotalRetries(), persistanceManager.getRequestTimeout());

    }

    private void dismissProgressDialog() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ProgressDialogManager.dismiss();
                }
            });
        }
    }
}