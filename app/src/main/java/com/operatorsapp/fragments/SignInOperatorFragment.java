package com.operatorsapp.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.app.operatorinfra.ErrorObjectInterface;
import com.app.operatorinfra.Operator;
import com.google.gson.Gson;
import com.operators.operatorcore.OperatorCore;
import com.operators.operatorcore.interfaces.OperatorForMachineUICallbackListener;
import com.operatorsapp.R;
import com.operatorsapp.activities.interfaces.OnGoToScreenListener;
import com.operatorsapp.interfaces.SignInOperatorToDashboardActivityCallback;

public class SignInOperatorFragment extends Fragment implements View.OnClickListener {

    private static final String LOG_TAG = SignInOperatorFragment.class.getSimpleName();
    private static final String SELECTED_OPERATOR = "selected_operator";
    private EditText mOperatorIdEditText;
    private Button mSignInButton;

    private OperatorCore mOperatorCore;
    private SignInOperatorToDashboardActivityCallback mSignInOperatorToDashboardActivityCallback;
    private OnGoToScreenListener mOnGoToScreenListener;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mSignInOperatorToDashboardActivityCallback = (SignInOperatorToDashboardActivityCallback) getActivity();
        mOperatorCore = mSignInOperatorToDashboardActivityCallback.onSignInOperatorFragmentAttached();
        mOnGoToScreenListener = (OnGoToScreenListener) getActivity();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_operator_sign_in, container, false);
        setActionBar();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mOperatorIdEditText = (EditText) view.findViewById(R.id.operator_id_edit_text);
        mSignInButton = (Button) view.findViewById(R.id.button_operator_signIn);

        mOperatorCore.registerListener(new OperatorForMachineUICallbackListener() {
            @Override
            public void onOperatorDataReceived(Operator operator) {
                Log.d(LOG_TAG, "Operator data received: Operator Id is:" + operator.getOperatorId() + " Operator Name Is: " + operator.getOperatorName());

                SelectedOperatorFragment selectedOperatorFragment = new SelectedOperatorFragment();
                Bundle bundle = new Bundle();
                Gson gson = new Gson();
                String jobString = gson.toJson(operator, Operator.class);
                bundle.putString(SELECTED_OPERATOR, jobString);

                selectedOperatorFragment.setArguments(bundle);
                mOnGoToScreenListener.goToFragment(selectedOperatorFragment, true);
            }

            @Override
            public void onOperatorDataReceiveFailure(ErrorObjectInterface reason) {
                Log.d(LOG_TAG, "Operator data receive failed. Reason : " + reason.getError().toString());
            }

            @Override
            public void onSetOperatorSuccess() {

            }

            @Override
            public void onSetOperatorFailed(ErrorObjectInterface reason) {

            }
        });

    }

    @Override
    public void onPause() {
        super.onPause();
        mSignInButton.setOnClickListener(null);
    }

    @Override
    public void onResume() {
        super.onResume();
        mSignInButton.setOnClickListener(this);
    }

    private void setActionBar() {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayUseLogoEnabled(true);
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            // rootView null
            @SuppressLint("InflateParams")
            View view = inflater.inflate(R.layout.sign_in_operator_action_bar, null);

            ImageView buttonClose = (ImageView) view.findViewById(R.id.button_cancel);
            buttonClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getFragmentManager().popBackStack();
                }
            });
            actionBar.setCustomView(view);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_operator_signIn: {
                String id = mOperatorIdEditText.getText().toString();
                Log.i(LOG_TAG, "Operator id: " + id);
                mOperatorCore.getOperatorById(id);
                break;
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mSignInOperatorToDashboardActivityCallback != null) {
            mSignInOperatorToDashboardActivityCallback = null;
        }
        mOperatorCore.unregisterListener();
    }
}
