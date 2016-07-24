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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.operatorinfra.ErrorObjectInterface;
import com.app.operatorinfra.Operator;
import com.google.gson.Gson;

import com.operators.operatorcore.OperatorCore;
import com.operators.operatorcore.interfaces.OperatorForMachineUICallbackListener;
import com.operatorsapp.R;
import com.operatorsapp.activities.interfaces.OnGoToScreenListener;
import com.operatorsapp.interfaces.SignInOperatorToDashboardActivityCallback;

public class SelectedOperatorFragment extends Fragment implements View.OnClickListener {
    public static final String LOG_TAG = SelectedOperatorFragment.class.getSimpleName();
    private static final String SELECTED_OPERATOR = "selected_operator";
    private Button mSignInButton;
    private Operator mSelectedOperator;
    private OperatorCore mOperatorCore;
    private SignInOperatorToDashboardActivityCallback mSignInOperatorToDashboardActivityCallback;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mSignInOperatorToDashboardActivityCallback = (SignInOperatorToDashboardActivityCallback) getActivity();
        mOperatorCore = mSignInOperatorToDashboardActivityCallback.onSignInOperatorFragmentAttached();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_selected_operator, container, false);
        Bundle bundle = this.getArguments();
        Gson gson = new Gson();
        mSelectedOperator = gson.fromJson(bundle.getString(SELECTED_OPERATOR), Operator.class);

        mSignInButton = (Button) view.findViewById(R.id.button_selected_operator_sign_in);

        TextView selectedOperatorID = (TextView) view.findViewById(R.id.operator_id_text_view);
        selectedOperatorID.setText(mSelectedOperator.getOperatorId());

        TextView selectedOperatorName = (TextView) view.findViewById(R.id.operator_name_text_view);
        selectedOperatorName.setText(mSelectedOperator.getOperatorName());

        setActionBar();
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mOperatorCore.registerListener(new OperatorForMachineUICallbackListener() {
            @Override
            public void onOperatorDataReceived(Operator operator) {

            }

            @Override
            public void onOperatorDataReceiveFailure(ErrorObjectInterface reason) {

            }

            @Override
            public void onSetOperatorSuccess() {
                Log.i(LOG_TAG, "onSetOperatorSuccess()");
                mSignInOperatorToDashboardActivityCallback.onSetOperatorForMachineSuccess(mSelectedOperator.getOperatorId(), mSelectedOperator.getOperatorName());
            }

            @Override
            public void onSetOperatorFailed(ErrorObjectInterface reason) {
                Log.w(LOG_TAG, "Set operator failed. Reason : " + reason.getError().toString());

            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mSignInButton.setOnClickListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mSignInButton.setOnClickListener(null);
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
            View view = inflater.inflate(R.layout.operator_fragment_action_bar, null);

            ImageView buttonClose = (ImageView) view.findViewById(R.id.arrow_back);
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
            case R.id.button_selected_operator_sign_in: {
                mOperatorCore.setOperatorForMachine(mSelectedOperator.getOperatorId());
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
