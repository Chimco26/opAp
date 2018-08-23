package com.operatorsapp.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.operators.errorobject.ErrorObjectInterface;
import com.operators.infra.Machine;
import com.operators.logincore.LoginCore;
import com.operators.logincore.interfaces.LoginUICallback;
import com.operators.reportrejectinfra.GetVersionCallback;
import com.operators.reportrejectnetworkbridge.server.response.Recipe.VersionResponse;
import com.operatorsapp.R;
import com.operatorsapp.activities.interfaces.GoToScreenListener;
import com.operatorsapp.fragments.interfaces.OnCroutonRequestListener;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.managers.ProgressDialogManager;
import com.operatorsapp.server.NetworkManager;
import com.operatorsapp.utils.ShowCrouton;
import com.operatorsapp.utils.SimpleRequests;
import com.zemingo.logrecorder.ZLogger;

import java.util.ArrayList;
import java.util.List;

public class LoginFragment extends Fragment {
    private static final String LOG_TAG = LoginFragment.class.getSimpleName();
    private GoToScreenListener mNavigationCallback;
    private OnCroutonRequestListener mCroutonCallback;
    private EditText mSiteUrl;
    private EditText mUserName;
    private EditText mPassword;
    private RelativeLayout mLoginButton;
    private ImageView mLoginBtnBackground;
    private ImageView mShowHidePass;
    private boolean mPasswordIsVisible = false;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
        mLoginBtnBackground = rootView.findViewById(R.id.loginBtn_background);

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
                    mLoginBtnBackground.setImageResource(R.drawable.login_button_selector);
                } else {
                    mLoginBtnBackground.setImageResource(R.drawable.button_bg_disabled);
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
                actionBar.setHomeButtonEnabled(false);
                actionBar.setDisplayHomeAsUpEnabled(false);
                actionBar.setDisplayShowTitleEnabled(false);
                actionBar.setDisplayShowCustomEnabled(true);
                actionBar.setDisplayUseLogoEnabled(true);
                SpannableString s = new SpannableString(getString(R.string.screen_title));
                s.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getActivity(), R.color.white)), 0, s.length() - 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                s.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getActivity(), R.color.T12_color)), s.length() - 3, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                LayoutInflater inflator = LayoutInflater.from(getActivity());
                /* rootView null*/
                @SuppressLint("InflateParams") View view = inflator.inflate(R.layout.actionbar_title_view, null);
                ((TextView) view.findViewById(R.id.title)).setText(s);
                actionBar.setCustomView(view);
                actionBar.setIcon(R.drawable.logo);
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
        ProgressDialogManager.show(getActivity());
        String siteUrl = mSiteUrl.getText().toString();
        String userName = mUserName.getText().toString().toLowerCase();
        String password = mPassword.getText().toString().toLowerCase();

        // Ohad change 30/4/17
        if (!siteUrl.toLowerCase().startsWith("https://")) {
            siteUrl = String.format("https://api%1$s.my.leadermes.com", siteUrl);
        }

        LoginCore.getInstance().login(siteUrl, userName, password, new LoginUICallback<Machine>() {
            @Override
            public void onLoginSucceeded(ArrayList<Machine> machines) {
                ZLogger.d(LOG_TAG, "login, onGetMachinesSucceeded() ");

                getVersion(machines, true);
            }

            @Override
            public void onLoginFailed(final ErrorObjectInterface reason) {
                dismissProgressDialog();
                mCroutonCallback.onHideConnectivityCroutonRequest();
                ShowCrouton.jobsLoadingErrorCrouton(mCroutonCallback, reason);
                mNavigationCallback.isTryToLogin(false);
            }
        });
    }

    private void tryToLoginSuccess(ArrayList<Machine> machines) {
        dismissProgressDialog();
        if (mNavigationCallback != null) {

            mNavigationCallback.goToFragment(SelectMachineFragment.newInstance(machines), true, true);

            mNavigationCallback.isTryToLogin(false);
        }
    }

    // Silent - setUsername & password from preferences, It is only when preferences.isSelectedMachine().
    private void doSilentLogin() {
        mNavigationCallback.isTryToLogin(true);
        ProgressDialogManager.show(getActivity());
        LoginCore.getInstance().login(PersistenceManager.getInstance().getSiteUrl(), PersistenceManager.getInstance().getUserName(), PersistenceManager.getInstance().getPassword(), new LoginUICallback<Machine>() {
            @Override
            public void onLoginSucceeded(ArrayList<Machine> machines) {
                ZLogger.d(LOG_TAG, "login, onGetMachinesSucceeded(),  go Next");

                getVersion(machines, false);
            }

            @Override
            public void onLoginFailed(final ErrorObjectInterface reason) {
                dismissProgressDialog();
                mCroutonCallback.onHideConnectivityCroutonRequest();
                ShowCrouton.jobsLoadingErrorCrouton(mCroutonCallback, reason);
                mNavigationCallback.isTryToLogin(false);
            }
        });
    }

    private void loginSuccess() {
        dismissProgressDialog();
        if (mNavigationCallback != null) {
            mNavigationCallback.goToDashboardActivity(PersistenceManager.getInstance().getMachineId());

            mNavigationCallback.isTryToLogin(false);
        }
    }

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

                    loginSuccess();

                }

            }

            @Override
            public void onGetVersionFailed(ErrorObjectInterface reason) {

                if (isTryTologin) {

                    tryToLoginSuccess(machines);

                } else {

                    loginSuccess();

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
