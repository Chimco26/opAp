package com.operatorsapp.fragments;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.common.StandardResponse;
import com.example.oppapplog.OppAppLogger;
import com.operators.infra.Machine;
import com.operators.logincore.LoginCore;
import com.operators.logincore.interfaces.LoginUICallback;
import com.operators.reportrejectinfra.GetVersionCallback;
import com.operators.reportrejectnetworkbridge.server.response.Recipe.VersionResponse;
import com.operatorsapp.BuildConfig;
import com.operatorsapp.R;
import com.operatorsapp.activities.DashboardActivity;
import com.operatorsapp.activities.interfaces.GoToScreenListener;
import com.operatorsapp.fragments.interfaces.OnCroutonRequestListener;
import com.operatorsapp.managers.CroutonCreator;
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
    private GoToScreenListener mNavigationCallback;
    private OnCroutonRequestListener mCroutonCallback;
    private EditText mSiteUrl;
    private EditText mUserName;
    private EditText mPassword;
    private TextView mLoginButton;
    private TextView mLoginBtnBackground;
    private ImageView mShowHidePass;
    private boolean mPasswordIsVisible = false;
    private IntentFilter mIntentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
    private BroadcastReceiver broadcastReceiver;

    public static LoginFragment newInstance() {

        return new LoginFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        // Analytics
        new GoogleAnalyticsHelper().trackScreen(getActivity(), "Login screen");

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

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (isNetworkAvailable(getActivity())) {
                    Log.d("Network Available ", "Network Available");
                    doSilentLogin();
                }
            }
        };

        return rootView;
    }

    @Override
    public void onDestroyView() {
        mSiteUrl.addTextChangedListener(null);
        mUserName.addTextChangedListener(null);
        mPassword.addTextChangedListener(null);
        mPassword.setOnEditorActionListener(null);
        super.onDestroyView();
    }

    private Boolean isNetworkAvailable(Context application) {
        ConnectivityManager connectivityManager = (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Network nw = connectivityManager.getActiveNetwork();
            if (nw == null) return false;
            NetworkCapabilities actNw = connectivityManager.getNetworkCapabilities(nw);
            return actNw != null && (actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH));
        } else {
            NetworkInfo nwInfo = connectivityManager.getActiveNetworkInfo();
            return nwInfo != null && nwInfo.isConnected();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setActionBar();
        getActivity().registerReceiver(broadcastReceiver, mIntentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(broadcastReceiver);
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
            if (isAdded() && mLoginButton != null) {
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
        String checkUrl = factoryUrl.replaceAll("[^a-zA-Z0-9_/,:.-]", "");

        if (!checkUrl.equals(factoryUrl) && mCroutonCallback != null) {
            mCroutonCallback.onShowCroutonRequest(getResources().getString(R.string.illegal_chars), 5000, R.id.parent_layouts, CroutonCreator.CroutonType.URL_ERROR);
        }
        return !TextUtils.isEmpty(factoryUrl) && !TextUtils.isEmpty(userName) && !TextUtils.isEmpty(password) && checkUrl.equals(factoryUrl);
    }

    private void tryToLogin() {
        if (mNavigationCallback != null) {
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
            LoginCore.getInstance().login(siteUrl, userName, password, new LoginUICallback<Machine>() {
                @Override
                public void onLoginSucceeded(ArrayList<Machine> machines, String siteName) {
                    OppAppLogger.d(LOG_TAG, "login, onGetMachinesSucceeded() ");

                    //getNotifications();
                    PersistenceManager.getInstance().setSiteName(siteName);
                    getVersion(machines, true);
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
    }

    private void tryToLoginSuccess(ArrayList<Machine> machines) {
        dismissProgressDialog();
        if (mNavigationCallback != null) {

//            if (BuildConfig.FLAVOR.equals(getString(R.string.emerald_flavor_name))) {

//            mNavigationCallback.goToFragment(SelectMachineFragment.newInstance(), true, true);

            mNavigationCallback.goToDashboardActivity(machines);
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
        Log.d("Network Available ", "do Silent Login");
        if (mNavigationCallback != null) {
            mNavigationCallback.isTryToLogin(true);
            ProgressDialogManager.show(getActivity());//todo kuti
            LoginCore.getInstance().login(PersistenceManager.getInstance().getSiteUrl(), PersistenceManager.getInstance().getUserName(), PersistenceManager.getInstance().getPassword(), new LoginUICallback<Machine>() {
                @Override
                public void onLoginSucceeded(ArrayList<Machine> machines, String siteName) {
                    Log.d("Network Available ", "on Login Succeeded");
                    OppAppLogger.d(LOG_TAG, "login, onGetMachinesSucceeded(),  go Next");
                    PersistenceManager.getInstance().setSiteName(siteName);
                    getVersion(machines, false);
                }

                @Override
                public void onLoginFailed(final StandardResponse reason) {
                    Log.d("Network Available ", "on Login Failed");
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
    }

    private void loginSuccess(ArrayList<Machine> machines) {
        dismissProgressDialog();
        if (mNavigationCallback != null) {
            mNavigationCallback.goToDashboardActivity(machines);
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

    private void getVersion(final ArrayList<Machine> machines, final boolean isTryTologin) {

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

                    tryToLoginSuccess(machines);

                } else {

                    loginSuccess(machines);

                }

            }

            @Override
            public void onGetVersionFailed(StandardResponse reason) {

                if (isTryTologin) {

                    tryToLoginSuccess(machines);

                } else {

                    loginSuccess(machines);

                }

            }

        }, NetworkManager.getInstance(), persistanceManager.getTotalRetries(), persistanceManager.getRequestTimeout());

    }

    private void dismissProgressDialog() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (getActivity() != null && !getActivity().isDestroyed()) {
                        ProgressDialogManager.dismiss();
                    }
                }
            });
        }
    }
}