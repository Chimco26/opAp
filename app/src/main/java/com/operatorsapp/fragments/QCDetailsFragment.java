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
import android.widget.ImageView;
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
import com.operatorsapp.utils.GoogleAnalyticsHelper;
import com.operatorsapp.utils.QCRequests;
import com.operatorsapp.utils.ShowCrouton;
import com.operatorsapp.view.GridSpacingItemDecoration;
import com.operatorsapp.view.SingleLineKeyboard;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;
import static com.example.common.QCModels.TestDetailsResponse.FIELD_TYPE_LAST;

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
    private TextView mSamplesNumberEt;
    private View mPassedLy;
    private ImageView mPassedIc;
    private TextView mPassedTv;

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
//            mTestDetailsRequest.setTestId(342); for test
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
        ((TextView) view.findViewById(R.id.FQCD_title_tv)).setText(String.format(Locale.getDefault(),
                "%s- %d", getString(R.string.quality_test), mTestDetailsRequest.getTestId()));
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
        initPassedVars(view);
    }

    private void initPassedVars(View view) {
        mPassedLy = view.findViewById(R.id.FQCD_passed_ly);
        mPassedIc = view.findViewById(R.id.FQCD_passed_ic);
        mPassedTv = view.findViewById(R.id.FQCD_passed_tv);
    }

    private void initIncrementSamplesView(View view) {
        mSamplesNumberEt = view.findViewById(R.id.FQCD_units_text_view);
        view.findViewById(R.id.FQCD_button_minus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.parseInt(mSamplesNumberEt.getText().toString()) > 0) {
                    mSamplesCount = Integer.parseInt(mSamplesNumberEt.getText().toString()) - 1;
                    updateSamples(false, mSamplesCount - 1);
                }
            }
        });
        view.findViewById(R.id.FQCD_button_plus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSamplesCount = Integer.parseInt(mSamplesNumberEt.getText().toString()) + 1;
                updateSamples(true, mSamplesCount - 1);
            }
        });
    }

    private void updateSamples(boolean isIncrement, int position) {
        mSamplesNumberEt.setText(mSamplesCount + "");
        List<TestSampleFieldsDatum> samples = mTestOrderDetails.getTestSampleFieldsData();
        int counter = 0;
        for (TestSampleFieldsDatum testSampleFieldsDatum : samples) {
            if (testSampleFieldsDatum.getSamplesData() == null) {
                break;
            }
            if (isIncrement) {
                testSampleFieldsDatum.getSamplesData().add(
                        new SamplesDatum(2, Integer.parseInt(String.valueOf(new Date().getTime())
                                .substring(String.valueOf(new Date().getTime()).length() - 5, String.valueOf(new Date().getTime()).length() - 1)) + counter));
            } else if (testSampleFieldsDatum.getSamplesData().size() > 0) {
                Integer id = testSampleFieldsDatum.getSamplesData().get(position).getID();
                testSampleFieldsDatum.getSamplesData().remove(position);
                List<TestSampleFieldsDatum> originalSamples = mTestOrderDetails.getOriginalSampleFields();
                for (TestSampleFieldsDatum original : originalSamples) {
                    for (SamplesDatum datum : original.getSamplesData()) {
                        if (datum.getID().equals(id)) {
                            datum.setUpsertType(1);
                        }
                    }
                }
            }
        }
        mSamplesAdapter.notifyDataSetChanged();
    }

    private void updateOriginalSamples() {
        List<TestSampleFieldsDatum> samples = mTestOrderDetails.getTestSampleFieldsData();
        List<TestSampleFieldsDatum> originalSamples = mTestOrderDetails.getOriginalSampleFields();
        if (samples.get(samples.size() - 1).getFieldType().equals(FIELD_TYPE_LAST)) {
            samples.remove(samples.get(samples.size() - 1));
        }
        for (int i = 0; i < originalSamples.size(); i++) {
            for (SamplesDatum datum : originalSamples.get(i).getSamplesData()) {
                if (datum.getUpsertType() == 1) {
                        samples.get(i).getSamplesData().add(datum);
                }
            }
        }
    }

    private void initView() {
        mSamplesNumberEt.setText(mSamplesCount + "");
        initPassedView();
        initSamplesTestRv();
        initTestRv();
    }

    private void initPassedView() {
        Boolean status = mTestOrderDetails.getTestDetails().get(0).getPassed();
        if (status == null) {
            mPassedLy.setVisibility(View.INVISIBLE);
        } else if (status) {
            mPassedLy.setVisibility(View.VISIBLE);
            mPassedLy.setBackgroundColor(getContext().getResources().getColor(R.color.new_green));
            mPassedIc.setImageDrawable(getContext().getResources().getDrawable(R.drawable.passed));
            mPassedTv.setText(getString(R.string.passed));
        } else {
            mPassedLy.setVisibility(View.VISIBLE);
            mPassedLy.setBackgroundColor(getContext().getResources().getColor(R.color.red_dark));
            mPassedIc.setImageDrawable(getContext().getResources().getDrawable(R.drawable.close_white));
            mPassedTv.setText(getString(R.string.failed));
        }
    }

    private void initSamplesTestRv() {
        mSamplesTestRV.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mSamplesTestRV.setHasFixedSize(false);
        mSamplesAdapter = new QCParametersHorizontalAdapter(mTestOrderDetails.getTestDetails().get(0).getSamples(),
                mTestOrderDetails.getTestSampleFieldsData(), this, this);
        mSamplesTestRV.setAdapter(mSamplesAdapter);
    }

    private void initTestRv() {
        mTestRV.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        GridSpacingItemDecoration gridSpacingItemDecoration = new GridSpacingItemDecoration(2, 15, true, 0);
        mTestRV.addItemDecoration(gridSpacingItemDecoration);
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
        int counter = 0;
        for (TestSampleFieldsDatum testSampleFieldsDatum : mTestOrderDetails.getTestSampleFieldsData()) {
            for (SamplesDatum samplesDatum : testSampleFieldsDatum.getSamplesData()) {
                samplesDatum.setID(counter++);
            }
        }
        try {
            Type listType = new TypeToken<List<TestSampleFieldsDatum>>() {
            }.getType();
            mTestOrderDetails.setOriginalSampleFields((List<TestSampleFieldsDatum>) new Gson().fromJson(GsonHelper.toJson(mTestOrderDetails.getTestSampleFieldsData()), listType));
        } catch (Exception ignored) {
        }
        mSamplesCount = mTestOrderDetails.getTestDetails().get(0).getSamples();
    }

    private void saveTestOrderDetails(TestDetailsResponse testDetailsResponse) {

        updateOriginalSamples();
        SaveTestDetailsRequest saveTestDetailsRequest = new SaveTestDetailsRequest(testDetailsResponse.getOriginalSampleFields(),
                testDetailsResponse.getTestFieldsData(), testDetailsResponse.getTestDetails().get(0).getSamples(), mTestDetailsRequest.getTestId());
        mProgressBar.setVisibility(View.VISIBLE);
        mQcRequests.postQCSaveTestDetails(saveTestDetailsRequest, new QCRequests.postQCSaveTestDetailsCallback() {
            @Override
            public void onSuccess(SaveTestDetailsResponse saveTestDetailsResponse) {
                mProgressBar.setVisibility(View.GONE);
                getActivity().setResult(RESULT_OK);
                getActivity().finish();
            }

            @Override
            public void onFailure(StandardResponse standardResponse) {
                mProgressBar.setVisibility(View.GONE);
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
        mSamplesCount = Integer.parseInt(mSamplesNumberEt.getText().toString()) - 1;
        updateSamples(false, position);
    }

    public interface QCDetailsFragmentListener {
        void onSaved();
    }
}
