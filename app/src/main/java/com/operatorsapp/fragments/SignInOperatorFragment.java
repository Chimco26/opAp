package com.operatorsapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.app.operatorinfra.Operator;
import com.example.oppapplog.OppAppLogger;
import com.google.gson.Gson;
import com.operators.errorobject.ErrorObjectInterface;
import com.operators.operatorcore.OperatorCore;
import com.operators.operatorcore.interfaces.OperatorForMachineUICallbackListener;
import com.operatorsapp.R;
import com.operatorsapp.activities.DashboardActivity;
import com.operatorsapp.activities.interfaces.GoToScreenListener;
import com.operatorsapp.activities.interfaces.SilentLoginCallback;
import com.operatorsapp.fragments.interfaces.OnCroutonRequestListener;
import com.operatorsapp.interfaces.CroutonRootProvider;
import com.operatorsapp.interfaces.OperatorCoreToDashboardActivityCallback;
import com.operatorsapp.managers.ProgressDialogManager;
import com.operatorsapp.utils.ShowCrouton;
import com.operatorsapp.utils.SoftKeyboardUtil;

public class SignInOperatorFragment extends Fragment implements View.OnClickListener, CroutonRootProvider {

    private static final String LOG_TAG = SignInOperatorFragment.class.getSimpleName();
    private static final String SELECTED_OPERATOR = "selected_operator";
    private EditText mOperatorIdEditText;
    private Button mSignInButton;

    private OperatorCore mOperatorCore;
    private OperatorCoreToDashboardActivityCallback mOperatorCoreToDashboardActivityCallback;
    private GoToScreenListener mOnGoToScreenListener;
    private OnCroutonRequestListener mOnCroutonRequestListener;
    private View view;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mOperatorCoreToDashboardActivityCallback = (OperatorCoreToDashboardActivityCallback) getActivity();
        if (mOperatorCoreToDashboardActivityCallback != null) {
            mOperatorCore = mOperatorCoreToDashboardActivityCallback.onSignInOperatorFragmentAttached();
        }
        mOnGoToScreenListener = (GoToScreenListener) getActivity();
        mOnCroutonRequestListener = (OnCroutonRequestListener) getActivity();
        mOperatorCore.registerListener(mOperatorForMachineUICallbackListener);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_operator_sign_in, container, false);
        view = rootView;
//        setActionBar();
        return rootView;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mOperatorIdEditText = view.findViewById(R.id.operator_id_edit_text);
        mSignInButton = view.findViewById(R.id.button_operator_signIn);
        mOperatorIdEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                OppAppLogger.getInstance().i(LOG_TAG, "S " + s + " , start " + start + " before, " + before + " count " + count);
                if (getActivity() != null) {

                    if (start + count > 0) {
                        mSignInButton.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.buttons_selector));
                        mSignInButton.setClickable(true);
                    } else {
                        mSignInButton.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.button_bg_disabled));
                        mSignInButton.setClickable(false);

                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    OperatorForMachineUICallbackListener mOperatorForMachineUICallbackListener = new OperatorForMachineUICallbackListener() {
        @Override
        public void onOperatorDataReceived(Operator operator) {
            removePhoneKeypad();
            if (operator != null) {
                if (operator.getOperatorName().equals("")) {
                    OppAppLogger.getInstance().d(LOG_TAG, "Operator data receive failed. Reason : Empty operator name ");
                    removePhoneKeypad();
                    ShowCrouton.operatorLoadingErrorCrouton(mOnCroutonRequestListener, "No operator found");
                } else {
                    OppAppLogger.getInstance().d(LOG_TAG, "Operator data received: Operator Id is:" + operator.getOperatorId() + " Operator Name Is: " + operator.getOperatorName());

                    SelectedOperatorFragment selectedOperatorFragment = new SelectedOperatorFragment();
                    Bundle bundle = new Bundle();
                    Gson gson = new Gson();
                    String jobString = gson.toJson(operator, Operator.class);
                    bundle.putString(SELECTED_OPERATOR, jobString);

                    selectedOperatorFragment.setArguments(bundle);
                    mOnGoToScreenListener.goToFragment(selectedOperatorFragment, true, true);
                }
            } else {
                OppAppLogger.getInstance().d(LOG_TAG, "Operator data receive failed. Reason : ");
                removePhoneKeypad();
                ShowCrouton.operatorLoadingErrorCrouton(mOnCroutonRequestListener, "No operator found");
            }
            dismissProgressDialog();
        }

        @Override
        public void onOperatorDataReceiveFailure(ErrorObjectInterface reason) {
            OppAppLogger.getInstance().d(LOG_TAG, "Operator data receive failed. Reason : " + reason.getError().toString());
            if (reason.getError() == ErrorObjectInterface.ErrorCode.Credentials_mismatch && getActivity() != null) {
                ((DashboardActivity) getActivity()).silentLoginFromDashBoard(mOnCroutonRequestListener, new SilentLoginCallback() {
                    @Override
                    public void onSilentLoginSucceeded() {
                        String id = mOperatorIdEditText.getText().toString();
                        OppAppLogger.getInstance().i(LOG_TAG, "Operator id: " + id);
                        mOperatorCore.getOperatorById(id);
                    }

                    @Override
                    public void onSilentLoginFailed(ErrorObjectInterface reason) {
                        ShowCrouton.operatorLoadingErrorCrouton(mOnCroutonRequestListener, reason.getError().toString());
                    }
                });
            } else {
                removePhoneKeypad();
                ShowCrouton.operatorLoadingErrorCrouton(mOnCroutonRequestListener, reason.getError().toString());
            }
            dismissProgressDialog();
        }

        @Override
        public void onSetOperatorSuccess(String operatorId) {
            dismissProgressDialog();
        }

        @Override
        public void onSetOperatorFailed(ErrorObjectInterface reason) {
            dismissProgressDialog();
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        mSignInButton.setOnClickListener(null);
        SoftKeyboardUtil.hideKeyboard(this);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
//            setActionBar();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mSignInButton.setOnClickListener(this);
    }


//    protected void setActionBar() {
//
//        if (getActivity() != null) {
//
//            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
//            if (actionBar != null) {
//                actionBar.setHomeButtonEnabled(false);
//                actionBar.setDisplayHomeAsUpEnabled(false);
//                actionBar.setDisplayShowTitleEnabled(false);
//                actionBar.setDisplayShowCustomEnabled(true);
//                actionBar.setDisplayUseLogoEnabled(true);
//                LayoutInflater inflater = LayoutInflater.from(getActivity());
//                // rootView null
//                @SuppressLint("InflateParams")
//                View view = inflater.inflate(R.layout.sign_in_operator_action_bar, null);
//
//                LinearLayout buttonClose = view.findViewById(R.id.close_image);
//                buttonClose.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
////                    FragmentManager fragmentManager = getFragmentManager();
////                    if (fragmentManager != null) {
////                        fragmentManager.popBackStack();
////                    }
//
//                        getActivity().onBackPressed();
//                    }
//                });
//                actionBar.setCustomView(view);
//            }
//
//        }
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_operator_signIn: {
                ProgressDialogManager.show(getActivity());
                String id = mOperatorIdEditText.getText().toString();
                OppAppLogger.getInstance().i(LOG_TAG, "Operator id: " + id);
                mOperatorCore.getOperatorById(id);
                break;
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mOperatorCoreToDashboardActivityCallback != null) {
            mOperatorCoreToDashboardActivityCallback = null;
        }
        mOperatorCore.unregisterListener();
    }

    public void removePhoneKeypad() {
        InputMethodManager inputManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        IBinder binder = view.getWindowToken();
        if (inputManager != null) {
            inputManager.hideSoftInputFromWindow(binder,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public int getCroutonRoot() {
        return R.id.parent_layouts;
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
