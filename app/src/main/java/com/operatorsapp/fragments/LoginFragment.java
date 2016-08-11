package com.operatorsapp.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
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
import com.operatorsapp.R;
import com.operatorsapp.activities.interfaces.GoToScreenListener;
import com.operatorsapp.fragments.interfaces.OnCroutonRequestListener;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.managers.ProgressDialogManager;
import com.operatorsapp.utils.ShowCrouton;
import com.zemingo.logrecorder.ZLogger;

import java.util.ArrayList;

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

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        // For now, It is necessary Settings screen
        PersistenceManager.getInstance().setTotalRetries(3);
        PersistenceManager.getInstance().setRequestTimeOut(17);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        mSiteUrl = (EditText) rootView.findViewById(R.id.factory_url);
        mUserName = (EditText) rootView.findViewById(R.id.user_name);
        mPassword = (EditText) rootView.findViewById(R.id.password);
        mLoginBtnBackground = (ImageView) rootView.findViewById(R.id.loginBtn_background);

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
        mShowHidePass = (ImageView) rootView.findViewById(R.id.show_hide_pass);
        mShowHidePass.setOnClickListener(new View.OnClickListener() {
            private boolean isVisible = false;

            @Override
            public void onClick(View v) {
                if (mShowHidePass.isEnabled()) {
                    if (!isVisible) {
                        mPassword.setTransformationMethod(null);
                        mPassword.setSelection(mPassword.length());
                        mShowHidePass.setImageResource(R.drawable.icn_show_password);
                        isVisible = true;
                    } else {
                        mPassword.setTransformationMethod(new PasswordTransformationMethod());
                        mPassword.setSelection(mPassword.length());
                        mShowHidePass.setImageResource(R.drawable.icn_password_hidden);
                        isVisible = false;
                    }
                }
            }
        });

        mLoginButton = (RelativeLayout) rootView.findViewById(R.id.loginBtn);
        mLoginButton.setEnabled(false);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLoginButton.isEnabled()) {
                    tryToLogin();
                }
            }
        });

        setActionBar();

        if (PersistenceManager.getInstance().isSelectedMachine()) {
            doSilentLogin();
        }

        return rootView;
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
                    mLoginBtnBackground.setBackgroundResource(R.drawable.login_button_selector);
                } else {
                    mLoginBtnBackground.setBackgroundResource(R.drawable.button_bg_disabled);
                }

                if (!TextUtils.isEmpty(mPassword.getText().toString())) {
                    mShowHidePass.setImageResource(R.drawable.icn_password_hidden);
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
            // rootView null
            @SuppressLint("InflateParams") View view = inflator.inflate(R.layout.actionbar_title_view, null);
            ((TextView) view.findViewById(R.id.title)).setText(s);
            actionBar.setCustomView(view);
            actionBar.setIcon(R.drawable.logo);
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
        ProgressDialogManager.show(getActivity());
        String siteUrl = mSiteUrl.getText().toString();
        String userName = mUserName.getText().toString().toLowerCase();
        String password = mPassword.getText().toString().toLowerCase();
        LoginCore.getInstance().login(siteUrl, userName, password, new LoginUICallback<Machine>() {
            @Override
            public void onLoginSucceeded(ArrayList<Machine> machines) {
                ZLogger.d(LOG_TAG, "login, onGetMachinesSucceeded() ");
                dismissProgressDialog();
                if (mNavigationCallback != null) {
                    mNavigationCallback.goToFragment(SelectMachineFragment.newInstance(machines), true);
                }
            }

            @Override
            public void onLoginFailed(final ErrorObjectInterface reason) {
                dismissProgressDialog();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ShowCrouton.jobsLoadingErrorCrouton(mCroutonCallback, reason);
                    }
                });
            }
        });
    }

    // Silent - setUsername & password from preferences, It is only when preferences.isSelectedMachine().
    private void doSilentLogin() {
        ProgressDialogManager.show(getActivity());
        LoginCore.getInstance().login(PersistenceManager.getInstance().getSiteUrl(),
                PersistenceManager.getInstance().getUserName(),
                PersistenceManager.getInstance().getPassword(), new LoginUICallback<Machine>() {
                    @Override
                    public void onLoginSucceeded(ArrayList<Machine> machines) {
                        ZLogger.d(LOG_TAG, "login, onGetMachinesSucceeded(),  go Next");
                        dismissProgressDialog();
                        if (mNavigationCallback != null) {
                            mNavigationCallback.goToDashboardActivity(PersistenceManager.getInstance().getMachineId());
                        }
                    }

                    @Override
                    public void onLoginFailed(final ErrorObjectInterface reason) {
                        dismissProgressDialog();
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ShowCrouton.jobsLoadingErrorCrouton(mCroutonCallback, reason);
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
}
