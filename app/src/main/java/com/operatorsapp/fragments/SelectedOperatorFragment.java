package com.operatorsapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.operatorinfra.Operator;
import com.example.common.StandardResponse;
import com.example.oppapplog.OppAppLogger;
import com.google.gson.Gson;
import com.operators.operatorcore.OperatorCore;
import com.operators.operatorcore.interfaces.OperatorForMachineUICallbackListener;
import com.operatorsapp.R;
import com.operatorsapp.fragments.interfaces.OnCroutonRequestListener;
import com.operatorsapp.interfaces.CroutonRootProvider;
import com.operatorsapp.interfaces.OperatorCoreToDashboardActivityCallback;
import com.operatorsapp.managers.ProgressDialogManager;
import com.operatorsapp.utils.GoogleAnalyticsHelper;
import com.operatorsapp.utils.ShowCrouton;
import com.operatorsapp.utils.broadcast.SendBroadcast;

public class SelectedOperatorFragment extends Fragment implements View.OnClickListener, CroutonRootProvider
{
    public static final String LOG_TAG = SelectedOperatorFragment.class.getSimpleName();
    private static final String SELECTED_OPERATOR = "selected_operator";
    private TextView mSignInButton;
    private Operator mSelectedOperator;
    private OperatorCore mOperatorCore;
    private OperatorCoreToDashboardActivityCallback mOperatorCoreToDashboardActivityCallback;
    private OnCroutonRequestListener mOnCroutonRequestListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mOperatorCoreToDashboardActivityCallback = (OperatorCoreToDashboardActivityCallback) getActivity();
        if (mOperatorCoreToDashboardActivityCallback != null) {
            mOperatorCore = mOperatorCoreToDashboardActivityCallback.onSignInOperatorFragmentAttached();
        }
        mOnCroutonRequestListener = (OnCroutonRequestListener) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_selected_operator, container, false);
        Bundle bundle = this.getArguments();

        Gson gson = new Gson();
        if (bundle != null) {
            mSelectedOperator = gson.fromJson(bundle.getString(SELECTED_OPERATOR), Operator.class);
        }

        mSignInButton = view.findViewById(R.id.button_selected_operator_sign_in);

        TextView selectedOperatorID = view.findViewById(R.id.operator_id_text_view);
        selectedOperatorID.setText(mSelectedOperator.getOperatorId());

        TextView selectedOperatorName = view.findViewById(R.id.operator_name_text_view);
        selectedOperatorName.setText(mSelectedOperator.getOperatorName());

//        setActionBar();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        mOperatorCore.registerListener(new OperatorForMachineUICallbackListener() {
            @Override
            public void onOperatorDataReceived(Operator operator) {

            }

            @Override
            public void onOperatorDataReceiveFailure(StandardResponse reason) {
                updateFailed(reason);
            }

            @Override
            public void onSetOperatorSuccess(String operatorId) {
                SendBroadcast.refreshPolling(getContext());
                OppAppLogger.getInstance().i(LOG_TAG, "onSetOperatorSuccess()");
                mOperatorCoreToDashboardActivityCallback.onSetOperatorForMachineSuccess(mSelectedOperator.getOperatorId(), mSelectedOperator.getOperatorName());

                //Analytics
                new GoogleAnalyticsHelper().trackEvent(getActivity(), GoogleAnalyticsHelper.EventCategory.OPERATOR_SIGN_IN, true,
                        "Operator name: " + mSelectedOperator.getOperatorName() + ", ID: " + mSelectedOperator.getOperatorId());

            }

            @Override
            public void onSetOperatorFailed(StandardResponse reason) {
                updateFailed(reason);

            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    public void updateFailed(StandardResponse reason) {
        ShowCrouton.operatorLoadingErrorCrouton(mOnCroutonRequestListener, "Set operator failed. Reason : " + reason.getError().toString());
        OppAppLogger.getInstance().w(LOG_TAG, "Set operator failed. Reason : " + reason.getError().toString());
        //Analytics
        new GoogleAnalyticsHelper().trackEvent(getActivity(), GoogleAnalyticsHelper.EventCategory.OPERATOR_SIGN_IN, false,
                "Failed Reason: " + reason.getError().toString());

        getActivity().onBackPressed();
    }

    @Override
    public void onResume() {
        super.onResume();
        mSignInButton.setOnClickListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        dismissProgressDialog();
        mSignInButton.setOnClickListener(null);
    }

//    protected void setActionBar() {
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
//                View view = inflater.inflate(R.layout.operator_fragment_action_bar, null);
//
//                ImageView buttonClose = view.findViewById(R.id.arrow_back);
//                buttonClose.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        getActivity().onBackPressed();
//                    }
//                });
//                actionBar.setCustomView(view);
//            }
//        }
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_selected_operator_sign_in: {
                ProgressDialogManager.show(getActivity());
                mOperatorCore.setOperatorForMachine(mSelectedOperator.getOperatorId());
//                SendBroadcast.refreshPolling(getContext());
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

    @Override
    public int getCroutonRoot()
    {
        return R.id.parent_layouts;
    }

    private void dismissProgressDialog()
    {
        if (getActivity() != null)
        {
            getActivity().runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    ProgressDialogManager.dismiss();
                }
            });
        }
    }
}
