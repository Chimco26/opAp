package com.operatorsapp.fragments;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.common.QCModels.SamplesDatum;
import com.example.common.QCModels.SaveTestDetailsRequest;
import com.example.common.QCModels.SaveTestDetailsResponse;
import com.example.common.QCModels.TestDetailsRequest;
import com.example.common.QCModels.TestDetailsResponse;
import com.example.common.QCModels.TestFieldsDatum;
import com.example.common.QCModels.TestFieldsGroup;
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
import com.operatorsapp.utils.GoogleAnalyticsHelper;
import com.operatorsapp.utils.QCRequests;
import com.operatorsapp.utils.ShowCrouton;
import com.operatorsapp.view.GridSpacingItemDecoration;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static android.support.annotation.Dimension.SP;
import static com.example.common.QCModels.TestDetailsResponse.FIELD_TYPE_LAST;

public class QCDetailsFragment extends Fragment implements CroutonRootProvider,
        QCSamplesMultiTypeAdapter.QCSamplesMultiTypeAdapterListener{
    public static final String TAG = QCDetailsFragment.class.getSimpleName();
    private static final String EXTRA_TEST_ID = "EXTRA_TEST_ID";
    private TestDetailsRequest mTestDetailsRequest;
    private QCRequests mQcRequests;
    private View mNoDataTv;
    private ProgressBar mProgressBar;
    private QCDetailsFragmentListener mListener;
    private TestDetailsResponse mTestOrderDetails;
    private RecyclerView mSamplesTestRV;
    private LinearLayout mTestContainer;
    private int mSamplesCount;
    private QCParametersHorizontalAdapter mSamplesAdapter;
    private TextView mSamplesNumberEt;
    private View mPassedLy;
    private ImageView mPassedIc;
    private TextView mPassedTv;
    private TextView mTitleTv;

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
        mTitleTv = view.findViewById(R.id.FQCD_title_tv);
        mQcRequests = new QCRequests();
//        initTestRvView();
        initSampleRvView();
        getTestOrderDetails();
    }

    private void initVars(View view) {
        mNoDataTv = view.findViewById(R.id.FQCD_no_data_tv);
        mProgressBar = view.findViewById(R.id.FQCD_progress);
        view.findViewById(R.id.FQCD_save_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isMandatoryFieldsCompleted()) {
                    saveTestOrderDetails(mTestOrderDetails);
                } else {
                    Toast.makeText(getActivity(), getString(R.string.you_need_to_complete_all_mandatory_fields), Toast.LENGTH_SHORT).show();
                }
            }
        });
        mSamplesTestRV = view.findViewById(R.id.FQCD_paramters_rv);
        mTestContainer = view.findViewById(R.id.FQCD_fields_ll);
        view.findViewById(R.id.FQCD_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        initIncrementSamplesView(view);
        initPassedVars(view);
    }

    private boolean isMandatoryFieldsCompleted() {
        List<TestFieldsDatum> testFields = mTestOrderDetails.getTestFieldsData();
        List<TestSampleFieldsDatum> testSamplesFields = mTestOrderDetails.getTestSampleFieldsData();

        for (TestFieldsDatum testFieldsDatum : testFields) {
            if (testFieldsDatum.getRequiredField() && testFieldsDatum.getAllowEntry()
                    &&  (testFieldsDatum.getCurrentValue() == null || testFieldsDatum.getCurrentValue().isEmpty())) {
                return false;
            }
        }

        for (TestSampleFieldsDatum testSampleFieldsDatum : testSamplesFields) {
            ArrayList<SamplesDatum> samplesDatum = (ArrayList<SamplesDatum>) testSampleFieldsDatum.getSamplesData();
            for (SamplesDatum samplesDatum1 : samplesDatum)
                if (testSampleFieldsDatum.getRequiredField() && testSampleFieldsDatum.getAllowEntry()
                        && (samplesDatum1.getValue() == null || samplesDatum1.getValue().isEmpty())) {
                    return false;
                }
        }
        return true;
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
                    updateSamples(false, mSamplesCount);
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
        for (TestSampleFieldsDatum testSampleFieldsDatum : samples) {
            if (testSampleFieldsDatum.getSamplesData() == null) {
                break;
            }
            if (isIncrement) {
                testSampleFieldsDatum.getSamplesData().add(new SamplesDatum(2, 0));
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

    private void updateSamplesRelativeToOriginal() {
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
        mTitleTv.setText(String.format(Locale.getDefault(),
                "%s - %s - %d", getString(R.string.quality_test), mTestOrderDetails.getTestName(),mTestDetailsRequest.getTestId()));
        mSamplesNumberEt.setText(mSamplesCount + "");
        initPassedView(mTestOrderDetails.getTestDetails().get(0).getPassed());
        initSamplesTestRv();
        initTestRv();
    }

    private void initPassedView(Boolean status) {
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
        if (getActivity() != null) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            mSamplesAdapter = new QCParametersHorizontalAdapter(mTestOrderDetails.getTestDetails().get(0).getSamples(),
                    mTestOrderDetails.getTestSampleFieldsData(), this, displayMetrics.widthPixels);
            mSamplesTestRV.setAdapter(mSamplesAdapter);
        }
    }

    private void initSampleRvView() {
        mSamplesTestRV.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false){
            @Override
            public boolean requestChildRectangleOnScreen(RecyclerView parent, View child, Rect rect, boolean immediate, boolean focusedChildVisible) {
                return false;
            }
        });
        mSamplesTestRV.setHasFixedSize(true);
    }

    private void initTestRv() {
        mTestContainer.removeAllViews();
        for (ArrayList<TestFieldsDatum> testFieldsData : mTestOrderDetails.getTestFieldsComplete()) {
            if (testFieldsData.size() > 0 && testFieldsData.get(0) != null) {
                Log.d(TAG, "initTestRv: " + testFieldsData.get(0).getGroupName());
                if (testFieldsData.get(0).getGroupId().equals(-1)) {
                    addTextViewTitleToSample(testFieldsData.get(0).getGroupName(), getContext().getResources().getColor(R.color.machine_blue));
                } else {
                    addTextViewTitleToSample(testFieldsData.get(0).getGroupName(), getContext().getResources().getColor(R.color.black));
                }
                QCMultiTypeAdapter testAdapter = new QCMultiTypeAdapter(testFieldsData);
                RecyclerView recyclerView = new RecyclerView(getActivity());
                recyclerView.setAdapter(testAdapter);
                recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                GridSpacingItemDecoration gridSpacingItemDecoration = new GridSpacingItemDecoration(2, 50, true, 0);
                recyclerView.addItemDecoration(gridSpacingItemDecoration);
                recyclerView.setHasFixedSize(false);
                mTestContainer.addView(recyclerView);
                View view = new View(getActivity());
                view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
                view.setBackgroundColor(getContext().getResources().getColor(R.color.divider_gray));
                mTestContainer.addView(view);
                if (testFieldsData.get(0).getGroupId().equals(-1)) {
                    addTextViewTitleToSample(getString(R.string.test_fields), getContext().getResources().getColor(R.color.machine_blue));
                    View view1 = new View(getActivity());
                    view1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 10));
                    mTestContainer.addView(view1);
                }
            }
        }
    }

    private void addTextViewTitleToSample(String string, int color) {
        TextView textView = new TextView(getActivity());
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setTextColor(color);
        textView.setTextSize(SP, 23);
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        textView.setText(string);
        mTestContainer.addView(textView);
    }

//    private void initTestRvView() {
//        mTestContainer.setLayoutManager(new GridLayoutManager(getActivity(), 2));
//        GridSpacingItemDecoration gridSpacingItemDecoration = new GridSpacingItemDecoration(2, 30, true, 0);
//        mTestContainer.addItemDecoration(gridSpacingItemDecoration);
//        mTestContainer.setHasFixedSize(false);
//    }

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
                if (getActivity() != null) {
                    mProgressBar.setVisibility(View.GONE);
                    mTestOrderDetails = testDetailsResponse;
                    initSamplesData();
                    initFieldsData();
                    initView();
                }
            }

            @Override
            public void onFailure(StandardResponse standardResponse) {
                mProgressBar.setVisibility(View.GONE);
                mNoDataTv.setVisibility(View.VISIBLE);
            }
        });
    }

    private void initFieldsData() {
        ArrayList<ArrayList<TestFieldsDatum>> completList = new ArrayList<>();
        ArrayList<TestFieldsDatum> recipeList = new ArrayList<>();
        HashMap<Integer, ArrayList<TestFieldsDatum>> testFieldsDatumHashMap = new HashMap<>();
        int counter = 0;
        for (TestFieldsDatum testFieldsDatum : mTestOrderDetails.getTestFieldsData()) {
            if (testFieldsDatum.getInputType() == 2 || testFieldsDatum.getInputType() == 6) {
                testFieldsDatum.setGroupId(-1);
                testFieldsDatum.setGroupName(getString(R.string.recipe_fields));
                recipeList.add(testFieldsDatum);
            } else {
                if (testFieldsDatum.getGroupId() == null) {
                    testFieldsDatum.setGroupId(0);
                }
                ArrayList<TestFieldsDatum> list = new ArrayList<>();
                if (testFieldsDatumHashMap.containsKey(testFieldsDatum.getGroupId())
                        && testFieldsDatumHashMap.get(testFieldsDatum.getGroupId()) != null) {
                    list = testFieldsDatumHashMap.get(testFieldsDatum.getGroupId());
                }
                list.add(testFieldsDatum);
                testFieldsDatumHashMap.put(testFieldsDatum.getGroupId(), list);

                //for test
//                ArrayList<TestFieldsGroup> testFieldsGroups = new ArrayList<>();
//                testFieldsGroups.add(new TestFieldsGroup(1, "1", 0));
//                testFieldsGroups.add(new TestFieldsGroup(2, "2", 0));
//                mTestOrderDetails.setTestFieldsGroups(testFieldsGroups);
                for (TestFieldsGroup testFieldsGroup : mTestOrderDetails.getTestFieldsGroups()) {
                    if (testFieldsGroup.getId() == testFieldsDatum.getGroupId()) {
                        testFieldsDatum.setGroupName(testFieldsGroup.getName());
                    }
                }
            }
        }
        completList.add(recipeList);
        for (HashMap.Entry<Integer, ArrayList<TestFieldsDatum>> arrayListEntry : testFieldsDatumHashMap.entrySet()) {
            completList.add(arrayListEntry.getValue());
        }
        mTestOrderDetails.setCompleteTestList(completList);
    }

    public void initSamplesData() {
        int counter = 1000;
        for (TestSampleFieldsDatum testSampleFieldsDatum : mTestOrderDetails.getTestSampleFieldsData()) {
            for (SamplesDatum samplesDatum : testSampleFieldsDatum.getSamplesData()) {
                if (samplesDatum.getID() == null || samplesDatum.getID().equals(0)) {
                    samplesDatum.setID(counter++);
                }
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

        updateSamplesRelativeToOriginal();
        final SaveTestDetailsRequest saveTestDetailsRequest = new SaveTestDetailsRequest(testDetailsResponse.getTestSampleFieldsData(),
                testDetailsResponse.getTestFieldsData(), mSamplesCount, mTestDetailsRequest.getTestId());
        mProgressBar.setVisibility(View.VISIBLE);
        mQcRequests.postQCSaveTestDetails(saveTestDetailsRequest, new QCRequests.postQCSaveTestDetailsCallback() {
            @Override
            public void onSuccess(SaveTestDetailsResponse saveTestDetailsResponse) {
                mProgressBar.setVisibility(View.GONE);
                    initPassedView(saveTestDetailsResponse.isPassed());
                    getTestOrderDetails();
            }

            @Override
            public void onFailure(StandardResponse standardResponse) {
                mProgressBar.setVisibility(View.GONE);
                ShowCrouton.showSimpleCrouton((QCActivity) getActivity(), standardResponse);
            }
        });
    }

    @Override
    public int getCroutonRoot() {
        return R.id.parent_layouts;
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
