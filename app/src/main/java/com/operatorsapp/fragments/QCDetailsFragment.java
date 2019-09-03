package com.operatorsapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.common.QCModels.SaveTestDetailsRequest;
import com.example.common.QCModels.SaveTestDetailsResponse;
import com.example.common.QCModels.TestDetailsRequest;
import com.example.common.QCModels.TestDetailsResponse;
import com.example.common.StandardResponse;
import com.operatorsapp.R;
import com.operatorsapp.activities.QCActivity;
import com.operatorsapp.adapters.QCParametersHorizontalAdapter;
import com.operatorsapp.interfaces.CroutonRootProvider;
import com.operatorsapp.managers.CroutonCreator;
import com.operatorsapp.utils.GoogleAnalyticsHelper;
import com.operatorsapp.utils.QCRequests;
import com.operatorsapp.utils.ShowCrouton;

public class QCDetailsFragment extends Fragment implements CroutonRootProvider {
    public static final String TAG = QCDetailsFragment.class.getSimpleName();
    private static final String EXTRA_TEST_ID = "EXTRA_TEST_ID";
    private TestDetailsRequest mTestDetailsRequest;
    private QCRequests mQcRequests;
    private View mNoDataTv;
    private ProgressBar mProgressBar;
    private QCDetailsFragmentListener mListener;
    private TestDetailsResponse mTestOrderDetails;
    private RecyclerView mSamplesTestRV;

    public static QCDetailsFragment newInstance(int testId) {

        QCDetailsFragment qcDetailsFragment = new QCDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_TEST_ID, testId);
        qcDetailsFragment.setArguments(bundle);
        return qcDetailsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Analytics
        new GoogleAnalyticsHelper().trackScreen(getActivity(), TAG);

        if (getArguments() != null && getArguments().containsKey(EXTRA_TEST_ID)) {
            mTestDetailsRequest = new TestDetailsRequest(getArguments().getInt(EXTRA_TEST_ID));
            mTestDetailsRequest.setTestId(494);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_qc_details, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initVars(view);
        mQcRequests = new QCRequests();
        getTestOrderDetails();
    }

    private void initVars(View view) {
        mNoDataTv = view.findViewById(R.id.FQCD_no_data_tv);
        mProgressBar = view.findViewById(R.id.FQCD_progress);
        view.findViewById(R.id.FQCD_save_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveTestOrderDetails(mTestOrderDetails);
            }
        });
        mSamplesTestRV = view.findViewById(R.id.FQCD_paramters_rv);
    }

    private void initView(){
        initSamplesTestRv();
    }
    private void initSamplesTestRv() {
        mSamplesTestRV.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mSamplesTestRV.setAdapter(new QCParametersHorizontalAdapter(mTestOrderDetails.getTestDetails().get(0).getSamples(), mTestOrderDetails.getTestSampleFieldsData()));
    }

    @Override
    public void onAttach(Context context) {
        if (context instanceof QCDetailsFragmentListener) {
            mListener = (QCDetailsFragmentListener) context;
        } else {
            Log.d(TAG, "onAttach: must override QCDetailsFragmentListener");
        }
        super.onAttach(context);
    }

    private void getTestOrderDetails() {
        mProgressBar.setVisibility(View.VISIBLE);
        mQcRequests.getQCTestDetails(mTestDetailsRequest, new QCRequests.getQCTestDetailsCallback() {
            @Override
            public void onSuccess(TestDetailsResponse testDetailsResponse) {
                mProgressBar.setVisibility(View.GONE);
                mTestOrderDetails = testDetailsResponse;
                //todo
                initView();
            }

            @Override
            public void onFailure(StandardResponse standardResponse) {
                mProgressBar.setVisibility(View.GONE);
                mNoDataTv.setVisibility(View.VISIBLE);
            }
        });
    }

    private void saveTestOrderDetails(TestDetailsResponse testDetailsResponse) {

        SaveTestDetailsRequest saveTestDetailsRequest = new SaveTestDetailsRequest(testDetailsResponse.getTestSampleFieldsData(),
                testDetailsResponse.getTestFieldsData(), testDetailsResponse.getTestDetails().get(0).getSamples(), mTestDetailsRequest.getTestId());
        mProgressBar.setVisibility(View.VISIBLE);
        mQcRequests.postQCSaveTestDetails(saveTestDetailsRequest, new QCRequests.postQCSaveTestDetailsCallback() {
            @Override
            public void onSuccess(SaveTestDetailsResponse saveTestDetailsResponse) {
                //todo
                ShowCrouton.showSimpleCrouton((QCActivity) getActivity(), null, CroutonCreator.CroutonType.SUCCESS);
            }

            @Override
            public void onFailure(StandardResponse standardResponse) {
                ShowCrouton.showSimpleCrouton((QCActivity) getActivity(), standardResponse);
            }
        });
    }

    @Override
    public int getCroutonRoot() {
        return R.id.parent_layouts;
    }

    public interface QCDetailsFragmentListener {
        void onSaved();
    }
}
