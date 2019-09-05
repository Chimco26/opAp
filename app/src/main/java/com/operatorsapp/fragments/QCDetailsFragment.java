package com.operatorsapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.common.QCModels.SamplesDatum;
import com.example.common.QCModels.SaveTestDetailsRequest;
import com.example.common.QCModels.SaveTestDetailsResponse;
import com.example.common.QCModels.TestDetailsRequest;
import com.example.common.QCModels.TestDetailsResponse;
import com.example.common.QCModels.TestSampleFieldsDatum;
import com.example.common.StandardResponse;
import com.example.common.utils.GsonHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.operatorsapp.R;
import com.operatorsapp.activities.QCActivity;
import com.operatorsapp.adapters.QCMultiTypeAdapter;
import com.operatorsapp.adapters.QCParametersHorizontalAdapter;
import com.operatorsapp.adapters.QCSamplesMultiTypeAdapter;
import com.operatorsapp.interfaces.CroutonRootProvider;
import com.operatorsapp.interfaces.OnKeyboardManagerListener;
import com.operatorsapp.managers.CroutonCreator;
import com.operatorsapp.utils.GoogleAnalyticsHelper;
import com.operatorsapp.utils.QCRequests;
import com.operatorsapp.utils.ShowCrouton;
import com.operatorsapp.view.SingleLineKeyboard;

import java.lang.reflect.Type;
import java.util.List;

public class QCDetailsFragment extends Fragment implements CroutonRootProvider,
        QCSamplesMultiTypeAdapter.QCSamplesMultiTypeAdapterListener,
        OnKeyboardManagerListener {
    public static final String TAG = QCDetailsFragment.class.getSimpleName();
    private static final String EXTRA_TEST_ID = "EXTRA_TEST_ID";
    private TestDetailsRequest mTestDetailsRequest;
    private QCRequests mQcRequests;
    private View mNoDataTv;
    private ProgressBar mProgressBar;
    private QCDetailsFragmentListener mListener;
    private TestDetailsResponse mTestOrderDetails;
    private RecyclerView mSamplesTestRV;
    private RecyclerView mTestRV;
    private SingleLineKeyboard mKeyBoard;
    private LinearLayout mKeyBoardLayout;
    private int mSamplesCount;
    private QCParametersHorizontalAdapter mSamplesAdapter;
    private QCMultiTypeAdapter mTestAdapter;
    private TextView mSamplesNumerEt;

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
        mKeyBoardLayout = view.findViewById(R.id.FW_keyboard);
        mNoDataTv = view.findViewById(R.id.FQCD_no_data_tv);
        mProgressBar = view.findViewById(R.id.FQCD_progress);
        view.findViewById(R.id.FQCD_save_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveTestOrderDetails(mTestOrderDetails);
            }
        });
        mSamplesTestRV = view.findViewById(R.id.FQCD_paramters_rv);
        mTestRV = view.findViewById(R.id.FQCD_fields_rv);
        initIncrementSamplesView(view);
    }

    private void initIncrementSamplesView(View view) {
        mSamplesNumerEt = view.findViewById(R.id.FQCD_units_text_view);
        view.findViewById(R.id.FQCD_button_minus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.parseInt(mSamplesNumerEt.getText().toString()) > 0) {
                    mSamplesCount = Integer.parseInt(mSamplesNumerEt.getText().toString()) - 1;
                    updateSamples(false, mSamplesCount - 1);
                }
            }
        });
        view.findViewById(R.id.FQCD_button_plus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSamplesCount = Integer.parseInt(mSamplesNumerEt.getText().toString()) + 1;
                updateSamples(true, mSamplesCount - 1);
            }
        });
    }

    private void updateSamples(boolean isIncrement, int position) {
        mSamplesNumerEt.setText(mSamplesCount + "");
        List<TestSampleFieldsDatum> samples = mTestOrderDetails.getTestSampleFieldsData();
        for (TestSampleFieldsDatum testSampleFieldsDatum: samples) {
            if (testSampleFieldsDatum.getSamplesData() == null){
                break;
            }
            if (isIncrement) {
                testSampleFieldsDatum.getSamplesData().add(new SamplesDatum());
            }else if (testSampleFieldsDatum.getSamplesData().size() > 0){
                testSampleFieldsDatum.getSamplesData().remove(position);
            }
        }
        mSamplesAdapter.notifyDataSetChanged();
    }

    private void initView() {
        mSamplesNumerEt.setText(mSamplesCount + "");
        initSamplesTestRv();
        initTestRv();
    }

    private void initSamplesTestRv() {
        mSamplesTestRV.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mSamplesAdapter = new QCParametersHorizontalAdapter(mTestOrderDetails.getTestDetails().get(0).getSamples(),
                mTestOrderDetails.getTestSampleFieldsData(), this, this);
        mSamplesTestRV.setAdapter(mSamplesAdapter);
    }

    private void initTestRv() {
        mTestRV.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mTestRV.setHasFixedSize(false);
        mTestAdapter = new QCMultiTypeAdapter(mTestOrderDetails.getTestFieldsData(), this);
        mTestRV.setAdapter(mTestAdapter);
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
                initSamplesData();
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

    public void initSamplesData() {
        try {
            Type listType = new TypeToken<List<TestSampleFieldsDatum>>() {
            }.getType();
            mTestOrderDetails.setOriginalSampleFields((List<TestSampleFieldsDatum>) new Gson().fromJson(GsonHelper.toJson(mTestOrderDetails.getTestSampleFieldsData()), listType));
        }catch (Exception ignored){}
        mSamplesCount = mTestOrderDetails.getTestDetails().get(0).getSamples();
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

    public void closeKeyBoard() {
        if (mKeyBoardLayout != null) {
            mKeyBoardLayout.setVisibility(View.GONE);
        }
        if (mKeyBoard != null) {
            mKeyBoard.setListener(null);
            mKeyBoard.closeKeyBoard();
        }
    }

    public void openKeyboard(SingleLineKeyboard.OnKeyboardClickListener listener, String text, String[] complementChars) {
        if (mKeyBoardLayout != null) {
            mKeyBoardLayout.setVisibility(View.VISIBLE);
            if (mKeyBoard == null) {
                mKeyBoard = new SingleLineKeyboard(mKeyBoardLayout, getContext());
            }

            mKeyBoard.setChars(complementChars);
            mKeyBoard.openKeyBoard(text);
            mKeyBoard.setListener(listener);
        }
    }

    @Override
    public int getCroutonRoot() {
        return R.id.parent_layouts;
    }

    @Override
    public void onOpenKeyboard(SingleLineKeyboard.OnKeyboardClickListener listener, String text, String[] complementChars) {
        openKeyboard(listener, text, complementChars);
    }

    @Override
    public void onCloseKeyboard() {
        closeKeyBoard();
    }

    @Override
    public void onDeleteLine(int position) {
        mSamplesCount = Integer.parseInt(mSamplesNumerEt.getText().toString()) - 1;
        updateSamples(false, position);
    }

    public interface QCDetailsFragmentListener {
        void onSaved();
    }
}
