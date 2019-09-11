package com.operatorsapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.common.QCModels.TestOrderRequest;
import com.example.common.QCModels.TestOrderResponse;
import com.example.common.QCModels.TestOrderSendRequest;
import com.example.common.StandardResponse;
import com.operatorsapp.R;
import com.operatorsapp.activities.QCActivity;
import com.operatorsapp.interfaces.CroutonRootProvider;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.utils.GoogleAnalyticsHelper;
import com.operatorsapp.utils.QCRequests;
import com.operatorsapp.utils.ShowCrouton;


public class QCTestOrderFragment extends Fragment implements
        CroutonRootProvider {

    public static final String TAG = QCTestOrderFragment.class.getSimpleName();
    private QCRequests mQcRequests;
    private TestOrderResponse mTestOrder;
    private TestOrderRequest mTestOrderRequest;
    private TextView mTestTv;
    private View mNoDataTv;
    private ProgressBar mProgressBar;
    private QCTestOrderFragmentListener mListener;
    private EditText mTestEt;

    public static QCTestOrderFragment newInstance() {

        QCTestOrderFragment qcTestOrderFragment = new QCTestOrderFragment();
        Bundle bundle = new Bundle();
//        qcTestOrderFragment.setArguments(bundle);
        return qcTestOrderFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Analytics
        new GoogleAnalyticsHelper().trackScreen(getActivity(), TAG);

        if (getArguments() != null) {
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_qc_test_order, container, false);

        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        if (context instanceof QCTestOrderFragmentListener){
            mListener = (QCTestOrderFragmentListener) context;
        }else {
            Log.d(TAG, "onAttach: must override QCTestOrderFragmentListener");
        }
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initVars(view);
        mQcRequests = new QCRequests();
        mTestOrderRequest = new TestOrderRequest(PersistenceManager.getInstance().getJobId());
        getTestOrder(mTestOrderRequest);
    }

    private void initVars(View view) {
        mNoDataTv = view.findViewById(R.id.FQCTO_no_data_tv);
        mProgressBar = view.findViewById(R.id.FQCTO_progress);
        mTestTv = view.findViewById(R.id.FQCTO_test_tv);
        mTestEt = view.findViewById(R.id.FQCTO_test_samples_et);
        view.findViewById(R.id.FQCTO_test_refresh_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTestOrderRequest.setQualityGroupID(mTestOrder.getResponseDictionaryDT().getQualityGroups().get(0).getId());
                mTestOrderRequest.setSubType(mTestOrder.getResponseDictionaryDT().getSubTypes().get(0).getId());
                getTestOrder(mTestOrderRequest);
            }
        });
        view.findViewById(R.id.FQCTO_test_run_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendTestOrder(new TestOrderSendRequest(mTestOrderRequest.getJobID(), mTestOrder.getJoshID(),
                        mTestOrder.getProductID(), mTestOrder.getSubType(),
                        Integer.parseInt(mTestEt.getText().toString() + "")));
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void getTestOrder(TestOrderRequest testOrderRequest) {
        mProgressBar.setVisibility(View.VISIBLE);
        mQcRequests.getQCTestOrder(testOrderRequest, new QCRequests.QCTestOrderCallback() {
            @Override
            public void onSuccess(TestOrderResponse testOrderResponse) {
                mProgressBar.setVisibility(View.GONE);
                mNoDataTv.setVisibility(View.GONE);
                mTestOrder = testOrderResponse;
                mTestTv.setText(String.valueOf(mTestOrder.getQualityGroupID()));
            }

            @Override
            public void onFailure(StandardResponse standardResponse) {
                mProgressBar.setVisibility(View.GONE);
                if (mTestOrder != null){
                    mNoDataTv.setVisibility(View.GONE);
                    ShowCrouton.showSimpleCrouton((QCActivity)getActivity(), standardResponse);
                }else {
                    mNoDataTv.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void sendTestOrder(TestOrderSendRequest testOrderSendRequest){
        mProgressBar.setVisibility(View.VISIBLE);
        mQcRequests.postQCSendTestOrder(testOrderSendRequest, new QCRequests.QCTestSendOrderCallback() {
            @Override
            public void onSuccess(StandardResponse standardResponse) {
                mProgressBar.setVisibility(View.GONE);
                if (standardResponse.getLeaderRecordID() != null && standardResponse.getLeaderRecordID() > 0){
                    mListener.onSent(standardResponse.getLeaderRecordID());
                }else {
                    ShowCrouton.showSimpleCrouton((QCActivity)getActivity(), standardResponse);
                }
            }

            @Override
            public void onFailure(StandardResponse standardResponse) {
                mProgressBar.setVisibility(View.GONE);
                ShowCrouton.showSimpleCrouton((QCActivity)getActivity(), standardResponse);
            }
        });
    }

    @Override
    public int getCroutonRoot() {
        return R.id.parent_layouts;
    }

    public interface QCTestOrderFragmentListener{
        void onSent(int testId);
    }
}
