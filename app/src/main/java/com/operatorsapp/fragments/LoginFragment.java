package com.operatorsapp.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.operators.infra.ErrorObjectInterface;
import com.operators.logincore.LoginCore;
import com.operators.logincore.LoginUICallback;
import com.operators.networkbridge.server.ErrorObject;
import com.operatorsapp.R;
import com.operatorsapp.fragments.interfaces.OnCroutonRequestListener;
import com.operatorsapp.managers.CroutonCreator;
import com.operatorsapp.managers.ProgressDialogManager;
import com.operatorsapp.utils.IdUtils;
import com.zemingo.logrecorder.ZLogger;

public class LoginFragment extends Fragment {
    private static final String LOG_TAG = LoginFragment.class.getSimpleName();
    private static final int CROUTON_DURATION = 5000;
    private OnCroutonRequestListener mCroutonCallback;
    private EditText mSiteUrl;
    private EditText mUserName;
    private EditText mPassword;
    private TextView mLoginButton;

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (mLoginButton != null) {
                mLoginButton.setEnabled(isAllFieldsAreValid());
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        mSiteUrl = (EditText) rootView.findViewById(R.id.factory_url);
        mUserName = (EditText) rootView.findViewById(R.id.user_name);
        mPassword = (EditText) rootView.findViewById(R.id.password);

        mSiteUrl.addTextChangedListener(mTextWatcher);
        mUserName.addTextChangedListener(mTextWatcher);
        mPassword.addTextChangedListener(mTextWatcher);

        setActionBar();

        doSilentLogin();

        return rootView;
    }

    private void doSilentLogin() {
        ProgressDialogManager.show(getActivity());
        LoginCore.getInstance().silentLogin(new LoginUICallback() {
            @Override
            public void onLoginSucceeded() {
                Log.d(LOG_TAG, "login, onLoginSucceeded(),  go Next");
                dismissProgressDialog();
                MyDialogFragment myDialog = new MyDialogFragment();
                myDialog.show(getFragmentManager(), LOG_TAG);
                mLoginButton.setText("");
            }

            @Override
            public void onLoginFailed(ErrorObjectInterface reason) {
                dismissProgressDialog();
                croutonError(reason);
            }
        });
    }

    private void croutonError(ErrorObjectInterface reason) {
        if (ErrorObject.ErrorCode.Credentials_mismatch.equals(reason.getError())) {
            String prefix = getString(R.string.could_not_log_in).concat(" ");
            String credentialsError = getString(R.string.credentials_error);
            final SpannableStringBuilder str = new SpannableStringBuilder(prefix + credentialsError);
            str.setSpan(new StyleSpan(R.style.DroidSansBold), 0, prefix.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            showCrouton(str, CroutonCreator.CroutonType.CREDENTIALS_ERROR);
        } else {
            String prefix = getString(R.string.could_not_log_in).concat(" ");
            String networkError = getString(R.string.no_communication);
            final SpannableStringBuilder str = new SpannableStringBuilder(prefix + networkError);
            str.setSpan(new StyleSpan(R.style.DroidSansBold), 0, prefix.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            showCrouton(str, CroutonCreator.CroutonType.NETWORK_ERROR);
        }
    }

    private void setActionBar() {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayUseLogoEnabled(false);
            actionBar.setIcon(R.drawable.logo);
            LayoutInflater inflator = LayoutInflater.from(getActivity());
            @SuppressLint("InflateParams") View view = inflator.inflate(R.layout.actionbar_title_and_button_view, null);
            ((TextView) view.findViewById(R.id.title)).setText("");
            mLoginButton = (TextView) view.findViewById(R.id.right_action_bar_button);
            mLoginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tryToLogin();
                    ZLogger.v(LOG_TAG, "onClick(), login button clicked");
                }
            });

            mLoginButton.setEnabled(isAllFieldsAreValid());
            actionBar.setCustomView(view);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCroutonCallback = (OnCroutonRequestListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling fragment must implement OnCroutonRequestListener interface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCroutonCallback = null;
    }

    private boolean isAllFieldsAreValid() {
        String factoryUrl = mSiteUrl.getText().toString();
        String userName = mUserName.getText().toString();
        String password = mPassword.toString();
        return !TextUtils.isEmpty(factoryUrl) && !TextUtils.isEmpty(userName) && !TextUtils.isEmpty(password);
    }

    private void tryToLogin() {
        ProgressDialogManager.show(getActivity());
        String siteUrl = mSiteUrl.getText().toString();
        String userName = mUserName.getText().toString();
        String password = mPassword.getText().toString();
        LoginCore.getInstance().login(siteUrl, userName, password, new LoginUICallback() {
            @Override
            public void onLoginSucceeded() {
                Log.d(LOG_TAG, "login, onLoginSucceeded() ");
                dismissProgressDialog();
                MyDialogFragment myDialog = new MyDialogFragment();
                myDialog.show(getFragmentManager(), LOG_TAG);
                mLoginButton.setText("");

            }

            @Override
            public void onLoginFailed(ErrorObjectInterface reason) {
                dismissProgressDialog();
                croutonError(reason);

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

    private void showCrouton(final SpannableStringBuilder str, final CroutonCreator.CroutonType credentialsError) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mCroutonCallback != null) {
                    mCroutonCallback.onShowCroutonRequest(getActivity(), str, CROUTON_DURATION, R.id.login_fragment_crouton_anchor, credentialsError);
                }
            }
        });
    }

    public static class MyDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.done)
                    .setPositiveButton(getResources().getString(R.string.ok), null)
                    .create();
        }
    }
}
