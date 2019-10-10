package com.operatorsapp.fragments;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.common.QCModels.ResponseDictionnaryItemsBaseModel;
import com.example.common.QCModels.SubType;
import com.example.common.QCModels.TestOrderRequest;
import com.example.common.QCModels.TestOrderResponse;
import com.example.common.QCModels.TestOrderSendRequest;
import com.example.common.StandardResponse;
import com.operatorsapp.R;
import com.operatorsapp.activities.QCActivity;
import com.operatorsapp.adapters.DictionarySpinnerAdapter;
import com.operatorsapp.adapters.SubTypeAdapter;
import com.operatorsapp.interfaces.CroutonRootProvider;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.utils.GoogleAnalyticsHelper;
import com.operatorsapp.utils.QCRequests;
import com.operatorsapp.utils.ShowCrouton;

import java.util.List;


public class QCTestOrderFragment extends Fragment implements
        CroutonRootProvider {

    public static final String TAG = QCTestOrderFragment.class.getSimpleName();
    private static final int TYPE_JOSH = 0;
    private static final int TYPE_QUALITY = 1;
    private static final int TYPE_PRODUCT_GROUP = 2;
    private static final int TYPE_PRODUCTS = 3;
    private QCRequests mQcRequests;
    private TestOrderResponse mTestOrder;
    private TestOrderRequest mTestOrderRequest;
    private ProgressBar mProgressBar;
    private QCTestOrderFragmentListener mListener;
    private EditText mSamplesEt;
    private Spinner mJoshSpinner;
    private Spinner mQualitySpinner;
    private Spinner mProductGroupSpinner;
    private Spinner mProductsSpinner;
    private Spinner mTestSpinner;
    private View mSamplesLy;

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
        if (context instanceof QCTestOrderFragmentListener) {
            mListener = (QCTestOrderFragmentListener) context;
        } else {
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

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void initVars(View view) {
        mProgressBar = view.findViewById(R.id.FQCTO_progress);
        mSamplesEt = view.findViewById(R.id.FQCTO_samples_et);
        mSamplesLy = view.findViewById(R.id.FQCTO_samples_ly);
        view.findViewById(R.id.FQCTO_run_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTestOrderRequest.getSubType() != 0) {
                    int samples = 0;
                    try {
                        samples = Integer.parseInt(mSamplesEt.getText().toString() + "");
                    } catch (Exception ignored) {
                    }
                    sendTestOrder(new TestOrderSendRequest(mTestOrderRequest.getJobID(), mTestOrder.getJoshID(),
                            mTestOrder.getProductID(), mTestOrderRequest.getSubType(), samples));
                }else {
                    Toast.makeText(getActivity(), getString(R.string.you_need_to_select_the_test_field), Toast.LENGTH_SHORT).show();
                }
            }
        });
        mJoshSpinner = view.findViewById(R.id.FQCTO_spin_josh);
        mQualitySpinner = view.findViewById(R.id.FQCTO_spin_quality);
        mProductGroupSpinner = view.findViewById(R.id.FQCTO_spin_product_spin);
        mProductsSpinner = view.findViewById(R.id.FQCTO_spin_products);
        mTestSpinner = view.findViewById(R.id.FQCTO_spin_test);
    }

    private void getTestOrder(final TestOrderRequest testOrderRequest) {
        mProgressBar.setVisibility(View.VISIBLE);
        mQcRequests.getQCTestOrder(testOrderRequest, new QCRequests.QCTestOrderCallback() {
            @Override
            public void onSuccess(TestOrderResponse testOrderResponse) {
                mProgressBar.setVisibility(View.GONE);
                if (mTestOrder == null) {
                    testOrderResponse.getResponseDictionaryDT().getSubTypes().add(0, new SubType());
                    initTestSpinner(testOrderResponse);
                }
                mTestOrder = testOrderResponse;
                if (mTestOrderRequest.getSubType() == -1) {
                    mTestOrderRequest.setSubType(mTestOrder.getResponseDictionaryDT().getSubTypes().get(0).getId());
                }
                mTestOrderRequest.setJoshID(mTestOrder.getJoshID());
                mTestOrderRequest.setProductGroupID(mTestOrder.getProductGroupID());
                mTestOrderRequest.setQualityGroupID(mTestOrder.getQualityGroupID());
                mTestOrderRequest.setProductID(mTestOrder.getProductID());
                mTestOrder.getResponseDictionaryDT().getQualityGroups().add(0, new ResponseDictionnaryItemsBaseModel());
                mTestOrder.getResponseDictionaryDT().getProductGroups().add(0, new ResponseDictionnaryItemsBaseModel());
                initSpinner(mJoshSpinner, mTestOrder.getResponseDictionaryDT().getJoshIDs(), TYPE_JOSH);
                initSpinner(mQualitySpinner, mTestOrder.getResponseDictionaryDT().getQualityGroups(), TYPE_QUALITY);
                initSpinner(mProductGroupSpinner, mTestOrder.getResponseDictionaryDT().getProductGroups(), TYPE_PRODUCT_GROUP);
                initSpinner(mProductsSpinner, mTestOrder.getResponseDictionaryDT().getProducts(), TYPE_PRODUCTS);
            }

            @Override
            public void onFailure(StandardResponse standardResponse) {
                mProgressBar.setVisibility(View.GONE);
                if (mTestOrder != null) {
                    ShowCrouton.showSimpleCrouton((QCActivity) getActivity(), standardResponse);
                } else {
                }
            }
        });
    }

    private void initSpinner(final Spinner spinner, final List<ResponseDictionnaryItemsBaseModel> models, final int type) {

        if (models != null && getActivity() != null) {
            final DictionarySpinnerAdapter dictionarySpinnerAdapter = new DictionarySpinnerAdapter(getActivity(), R.layout.base_spinner_item, models);
            dictionarySpinnerAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item_custom);
            spinner.getBackground().setColorFilter(ContextCompat.getColor(getActivity(), R.color.T12_color), PorterDuff.Mode.SRC_ATOP);
            spinner.setAdapter(dictionarySpinnerAdapter);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    dictionarySpinnerAdapter.setTitle(position);

                    switch (type) {
                        case TYPE_JOSH:
                            if (mTestOrderRequest.getJoshID() != mTestOrder.getResponseDictionaryDT().getJoshIDs().get(position).getId()) {
                                mTestOrderRequest.setJoshID(mTestOrder.getResponseDictionaryDT().getJoshIDs().get(position).getId());
                                getTestOrder(mTestOrderRequest);
                            }
                            break;
                        case TYPE_QUALITY:
                            if (mTestOrderRequest.getQualityGroupID() != mTestOrder.getResponseDictionaryDT().getQualityGroups().get(position).getId()) {
                                mTestOrderRequest.setQualityGroupID(mTestOrder.getResponseDictionaryDT().getQualityGroups().get(position).getId());
                                getTestOrder(mTestOrderRequest);
                            }
                            break;
                        case TYPE_PRODUCT_GROUP:
                            if (mTestOrderRequest.getProductGroupID() != mTestOrder.getResponseDictionaryDT().getQualityGroups().get(position).getId()) {
                                mTestOrderRequest.setProductGroupID(mTestOrder.getResponseDictionaryDT().getProductGroups().get(position).getId());
                                getTestOrder(mTestOrderRequest);
                            }
                            break;
                        case TYPE_PRODUCTS:
                            if (mTestOrderRequest.getProductID() != mTestOrder.getResponseDictionaryDT().getProducts().get(position).getId()) {
                                mTestOrderRequest.setSubType(mTestOrder.getResponseDictionaryDT().getSubTypes().get(position).getId());
                                getTestOrder(mTestOrderRequest);
                            }
                            break;
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            spinner.post(new Runnable() {
                @Override
                public void run() {
                    spinner.setSelection(getPositionForSelectedItem(models, type), false);
                }
            });

        }
    }

    private int getPositionForSelectedItem(List<ResponseDictionnaryItemsBaseModel> models, int type) {
        for (int i = 0; i < models.size(); i++) {
            switch (type) {
                case TYPE_JOSH:
                    if (models.get(i).getId() == (mTestOrderRequest.getJoshID())) {
                        return i;
                    }
                    break;
                case TYPE_QUALITY:
                    if (models.get(i).getId() == (mTestOrderRequest.getQualityGroupID())) {
                        return i;
                    }
                    break;
                case TYPE_PRODUCT_GROUP:
                    if (models.get(i).getId() == (mTestOrderRequest.getProductGroupID())) {
                        return i;
                    }
                    break;
                case TYPE_PRODUCTS:
                    if (models.get(i).getId() == (mTestOrderRequest.getProductID())) {
                        return i;
                    }
                    break;
            }
        }
        return 0;
    }

    public void initTestSpinner(final TestOrderResponse testOrderResponse) {
        if (testOrderResponse.getResponseDictionaryDT().getSubTypes() != null && getActivity() != null) {
            final SubTypeAdapter subTypeAdapter = new SubTypeAdapter(getActivity(), R.layout.base_spinner_item, testOrderResponse.getResponseDictionaryDT().getSubTypes());
            subTypeAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item_custom);
            mTestSpinner.setAdapter(subTypeAdapter);
            mTestSpinner.getBackground().setColorFilter(ContextCompat.getColor(getActivity(), R.color.T12_color), PorterDuff.Mode.SRC_ATOP);

            mTestSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    subTypeAdapter.setTitle(position);
                    mTestOrderRequest.setSubType(testOrderResponse.getResponseDictionaryDT().getSubTypes().get(position).getId());
                    if (testOrderResponse.getResponseDictionaryDT().getSubTypes().get(position).getHasSamples() != null &&
                            testOrderResponse.getResponseDictionaryDT().getSubTypes().get(position).getHasSamples()){
                        mSamplesLy.setVisibility(View.VISIBLE);
                    }else {
                        mSamplesLy.setVisibility(View.GONE);
                        mSamplesEt.setHint("0");
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

    private void sendTestOrder(TestOrderSendRequest testOrderSendRequest) {
        mProgressBar.setVisibility(View.VISIBLE);
        mQcRequests.postQCSendTestOrder(testOrderSendRequest, new QCRequests.QCTestSendOrderCallback() {
            @Override
            public void onSuccess(StandardResponse standardResponse) {
                mProgressBar.setVisibility(View.GONE);
                if (standardResponse.getLeaderRecordID() != null && standardResponse.getLeaderRecordID() > 0) {
                    mListener.onSent(standardResponse.getLeaderRecordID());
                } else {
                    ShowCrouton.showSimpleCrouton((QCActivity) getActivity(), standardResponse);
                }
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

    public interface QCTestOrderFragmentListener {
        void onSent(int testId);
    }
}
