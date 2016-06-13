package com.operatorsapp.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.StyleSpan;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.operatorsapp.R;
import com.operatorsapp.dialogs.TwoButtonDialogFragment;
import com.operatorsapp.fragments.interfaces.OnBackPressedListener;
import com.operatorsapp.fragments.interfaces.OnCroutonRequestListener;
import com.operatorsapp.fragments.interfaces.OnLoginListener;
import com.operatorsapp.managers.AccountManager;
import com.operatorsapp.managers.CroutonCreator;
import com.operatorsapp.managers.ProgressDialogManager;
import com.operatorsapp.models.Site;
import com.operatorsapp.server.ErrorObject;
import com.operatorsapp.utils.SoftKeyboardUtil;
import com.zemingo.logrecorder.ZLogger;

import java.io.UnsupportedEncodingException;

public class LoginFragment extends Fragment implements TwoButtonDialogFragment.OnDialogButtonsListener, OnBackPressedListener, AccountManager.OnLoginListener {
    private static final String LOG_TAG = LoginFragment.class.getSimpleName();
    private static final String SITE_ID_ARGUMENT = "com.leadermes.emerald.fragments.site_argument";
    private static final int DISCARD_CHANGES_DIALOG_FRAGMENT = 1003;
    private static final int TRYING_TO_OVERRIDE_EXISTING_SITE_REQUEST_CODE = 1004;
    private static final int CROUTON_DURATION = 5000;
    private EditText mSiteName;
    private EditText mSiteUrl;
    private EditText mUserName;
    private EditText mPassword;
    private OnLoginListener mCallback;
    private OnCroutonRequestListener mCroutonCallback;
    private TextView mLoginButton;
    private ScreenMode mCurrentScreenMode;
    private Site mSiteForEdit;
    private String mUrlBeforeEdit;

    private enum ScreenMode {
        ADD_NEW_SITE, UPDATE_SITE
    }

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

    public static LoginFragment newInstance(String siteId) {
        LoginFragment fragment = new LoginFragment();
        Bundle bundle = new Bundle();
        bundle.putString(SITE_ID_ARGUMENT, siteId);
        fragment.setArguments(bundle);
        return fragment;
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
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        mSiteName = (EditText) rootView.findViewById(R.id.factory_name);
        mSiteUrl = (EditText) rootView.findViewById(R.id.factory_url);
        mUserName = (EditText) rootView.findViewById(R.id.user_name);
        mPassword = (EditText) rootView.findViewById(R.id.password);
        if (getArguments() != null && getArguments().containsKey(SITE_ID_ARGUMENT)) {
            String siteId = getArguments().getString(SITE_ID_ARGUMENT);
            if (!TextUtils.isEmpty(siteId)) {
                mSiteForEdit = AccountManager.getInstance().getSiteById(siteId);
                mSiteName.setText(mSiteForEdit.getSiteName());
                mSiteUrl.setText(mSiteForEdit.getSiteUrl());
                mUserName.setText(mSiteForEdit.getUserName());
                mUrlBeforeEdit = mSiteForEdit.getSiteUrl();
                byte[] decode = Base64.decode(mSiteForEdit.getPassword(), Base64.NO_WRAP);
                String password = "";
                try {
                    password = new String(decode, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    ZLogger.w(LOG_TAG, "onCreateView(), can't decode password");
                    e.printStackTrace();
                }
                mPassword.setText(password);
                mCurrentScreenMode = ScreenMode.UPDATE_SITE;
            }
        } else {
            mCurrentScreenMode = ScreenMode.ADD_NEW_SITE;
            mUrlBeforeEdit = "";
        }

        mSiteName.addTextChangedListener(mTextWatcher);
        mSiteUrl.addTextChangedListener(mTextWatcher);
        mUserName.addTextChangedListener(mTextWatcher);
        mPassword.addTextChangedListener(mTextWatcher);

        mPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (isAllFieldsAreValid()) {
                        tryToLogin();
                    }
                }
                return false;
            }
        });
        setActionBar();
        return rootView;
    }

    private void setActionBar() {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayUseLogoEnabled(false);
            actionBar.setIcon(null);
            actionBar.setHomeAsUpIndicator(R.drawable.x_button_selector);
            LayoutInflater inflator = LayoutInflater.from(getActivity());
            View view = inflator.inflate(R.layout.actionbar_title_and_button_view, null);
            ((TextView) view.findViewById(R.id.title)).setText(getString(R.string.login_screen_title));
            mLoginButton = (TextView) view.findViewById(R.id.right_action_bar_button);
            if (mCurrentScreenMode == ScreenMode.UPDATE_SITE) {
                mLoginButton.setText(getString(R.string.login_screen_save));
            }
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
            mCallback = (OnLoginListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling fragment must implement OnSessionListener interface");
        }
        try {
            mCroutonCallback = (OnCroutonRequestListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling fragment must implement OnCroutonRequestListener interface");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        AccountManager.getInstance().registerLoginListener(this);
        mSiteName.requestFocus();
        SoftKeyboardUtil.showKeyboard(getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
        SoftKeyboardUtil.hideKeyboard(this);
        AccountManager.getInstance().unRegisterLoginListener();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                if (!onBackPressed()) {
                    getFragmentManager().popBackStack();
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean fieldsContainsData() {
        String factoryName = mSiteName.getText().toString();
        String factoryUrl = mSiteUrl.getText().toString();
        String userName = mUserName.getText().toString();
        String password = mPassword.getText().toString();
        return !TextUtils.isEmpty(factoryName) || !TextUtils.isEmpty(factoryUrl) || !TextUtils.isEmpty(userName) || !TextUtils.isEmpty(password);
    }

    private boolean isAllFieldsAreValid() {
        String factoryName = mSiteName.getText().toString();
        String factoryUrl = mSiteUrl.getText().toString();
        String userName = mUserName.getText().toString();
        String password = mPassword.getText().toString();
        return !TextUtils.isEmpty(factoryName) && !TextUtils.isEmpty(factoryUrl) && !TextUtils.isEmpty(userName) && !TextUtils.isEmpty(password);
    }

    private void tryToLogin() {
        String alias = "";

        if (!mUrlBeforeEdit.equals(mSiteUrl.getText().toString())) {
            if (mSiteForEdit != null) {
                alias = AccountManager.getInstance().getAliasForSiteUrlExcludingSiteId(mSiteUrl.getText().toString(), mSiteForEdit.getSiteId());
            } else {
                alias = AccountManager.getInstance().getAliasForSiteUrlExcludingSiteId(mSiteUrl.getText().toString(), null);
            }
        }

        //todo explain the flow here..
        if (!TextUtils.isEmpty(alias)) {
            TwoButtonDialogFragment twoButtonDialogFragment = TwoButtonDialogFragment.newInstance(alias + "\n" + getString(R.string.login_dialog_alias_already_exists_with_the_same_url_add) + " " + mSiteName.getText() + " ?", R.string.login_dialog_add, R.string.login_dialog_cancel);
            twoButtonDialogFragment.setTargetFragment(this, TRYING_TO_OVERRIDE_EXISTING_SITE_REQUEST_CODE);
            twoButtonDialogFragment.show(getChildFragmentManager(), TwoButtonDialogFragment.DIALOG);
        } else {
            performLogin();
        }
    }

    public void performLogin() {
        ProgressDialogManager.show(getActivity());
        String siteName = mSiteName.getText().toString();
        String siteUrl = mSiteUrl.getText().toString();
        String userName = mUserName.getText().toString();
        String password = mPassword.getText().toString();
        String previousSiteId = "";
        if (mSiteForEdit != null) {
            previousSiteId = mSiteForEdit.getSiteId();
        }

        AccountManager.getInstance().performLogin(siteName, siteUrl, userName, password, previousSiteId);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    @Override
    public void onTwoButtonDialogPositiveButtonClick(DialogInterface dialog, int requestCode) {
        dialog.dismiss();

        switch (requestCode) {
            case TRYING_TO_OVERRIDE_EXISTING_SITE_REQUEST_CODE:
                performLogin();
                break;
        }
    }

    @Override
    public void onTwoButtonDialogNegativeButtonClick(DialogInterface dialog, int requestCode) {
        dialog.dismiss();

        if (requestCode == DISCARD_CHANGES_DIALOG_FRAGMENT) {
            getFragmentManager().popBackStack();
        }
    }


    @Override
    public boolean onBackPressed() {
        if (!dataWasChanged()) {
            return false;
        }
        if (fieldsContainsData()) {
            TwoButtonDialogFragment twoButtonDialogFragment = TwoButtonDialogFragment.newInstance(getString(R.string.login_dialog_message), R.string.login_dialog_keep_editing, R.string.login_dialog_discard);
            twoButtonDialogFragment.setTargetFragment(this, DISCARD_CHANGES_DIALOG_FRAGMENT);
            twoButtonDialogFragment.show(getChildFragmentManager(), TwoButtonDialogFragment.DIALOG);
            return true;
        }
        return false;
    }

    @Override
    public void onLoginSucceeded(String data) {
        dismissProgressDialog();
        if (mCurrentScreenMode.equals(ScreenMode.ADD_NEW_SITE)) {
            if (mCallback != null) {
                mCallback.onLoginSuccess(data);
            }
        } else if (mCurrentScreenMode.equals(ScreenMode.UPDATE_SITE)) {
            if (mCallback != null) {
                mCallback.onUpdatedSuccessfully(data);
            }
        }
        getFragmentManager().popBackStack();
    }

    private boolean dataWasChanged() {
        if (mSiteForEdit == null) {
            return true;
        }
        String siteUrl = mSiteUrl.getText().toString();
        String siteName = mSiteName.getText().toString();
        String userName = mUserName.getText().toString();
        String password = Base64.encodeToString(mPassword.getText().toString().getBytes(), Base64.NO_WRAP);
        return !(siteUrl.equals(mSiteForEdit.getSiteUrl()) && siteName.equals(mSiteForEdit.getSiteName()) && userName.equals(mSiteForEdit.getUserName()) && password.equals(mSiteForEdit.getPassword()));
    }

    private void dismissProgressDialog() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ProgressDialogManager.dismiss();
            }
        });
    }

    @Override
    public void onLoginFailed(ErrorObject.ErrorCode errorCode) {
        dismissProgressDialog();
        if (ErrorObject.ErrorCode.Credentials_mismatch.equals(errorCode)) {
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
}
