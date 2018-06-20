package com.operatorsapp.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.operators.errorobject.ErrorObjectInterface;
import com.operators.reportrejectinfra.GetJobDetailsCallback;
import com.operators.reportrejectinfra.GetPendingJobListCallback;
import com.operators.reportrejectnetworkbridge.server.response.activateJob.ActivateJobRequest;
import com.operators.reportrejectnetworkbridge.server.response.activateJob.Header;
import com.operators.reportrejectnetworkbridge.server.response.activateJob.JobDetailsRequest;
import com.operators.reportrejectnetworkbridge.server.response.activateJob.JobDetailsResponse;
import com.operators.reportrejectnetworkbridge.server.response.activateJob.PandingJob;
import com.operators.reportrejectnetworkbridge.server.response.activateJob.PendingJobResponse;
import com.operators.reportrejectnetworkbridge.server.response.activateJob.Property;
import com.operatorsapp.R;
import com.operatorsapp.adapters.JobHeadersAdaper;
import com.operatorsapp.adapters.PendingJobsAdapter;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.server.NetworkManager;
import com.operatorsapp.utils.SimpleRequests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JobActionActivity extends AppCompatActivity implements View.OnClickListener, JobHeadersAdaper.JobHeadersAdaperListener, PendingJobsAdapter.PendingJobsAdapterListener {

    private static final String TAG = JobActionActivity.class.getSimpleName();
    private TextView mTitleTv;
    private TextView mTitlLine1Tv1;
    private TextView mTitlLine1Tv2;
    private TextView mTitlLine1Tv3;
    private TextView mTitlLine1Tv4;
    private TextView mTitlLine1Tv5;
    private TextView mTitlLine1Tv6;
    private TextView mTit2Line1Tv1;
    private TextView mTit2Line1Tv2;
    private TextView mTit2Line1Tv3;
    private TextView mTit2Line1Tv4;
    private TextView mTit2Line1Tv5;
    private TextView mTit2Line1Tv6;
    private ImageView mProductShema;
    private ImageView mProductImage;
    private View mProductShemaNoImageLy;
    private View mProductImageNoImageLy;
    private TextView mMaterialItemTitleTv;
    private RecyclerView mMaterialItemRv;
    private TextView mMoldItemTitleTv;
    private ImageView mMoldItemImg;
    private RecyclerView mMoldItemRv;
    private RecyclerView mActionRv;
    private View mActionItemLy;
    private EditText mSearchViewEt;
    private RecyclerView mHeadersRv;
    private RecyclerView mPendingJobsRv;
    private View mMaterialItem;
    private View mMoldItem;
    private PendingJobResponse mPendingJobsResponse;
    private JobHeadersAdaper mHeadersAdapter;
    private PendingJobsAdapter mPandingJobsAdaper;
    private HashMap<String, Header> mHashMapHeaders;
    private ArrayList<Header> mHeaders = new ArrayList<>();
    private ArrayList<PandingJob> mPandingJobs = new ArrayList<>();
    private ArrayList<PandingJob> mPendingJobsNoHeadersFiltered = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_action);

        initVars();

        initListener();

        getPendingJoblist();
    }

    private void getPendingJoblist() {

        SimpleRequests simpleRequests = new SimpleRequests();

        final PersistenceManager persistanceManager = PersistenceManager.getInstance();

        simpleRequests.getPendingJobList(persistanceManager.getSiteUrl(), new GetPendingJobListCallback() {

            @Override
            public void onGetPendingJobListSuccess(Object response) {

                mPendingJobsResponse = ((PendingJobResponse) response);

                if (mPendingJobsResponse != null) {

                    mHeaders.addAll(mPendingJobsResponse.getHeaders());

                    mPandingJobs.addAll(mPendingJobsResponse.getPandingJobs());

                    headerListToHashMap(mPendingJobsResponse.getHeaders());

                    mPendingJobsNoHeadersFiltered.addAll(mPandingJobs);

                    sortHeaders();

                    initView();

                    ArrayList<Integer> jobIds = new ArrayList<>();
                    jobIds.add(mPendingJobsResponse.getPandingJobs().get(0).getID());
                    getJobDetails(jobIds);

                }

            }

            @Override
            public void onGetPendingJobListFailed(ErrorObjectInterface reason) {

            }
        }, NetworkManager.getInstance(), new ActivateJobRequest(persistanceManager.getSessionId(), persistanceManager.getMachineId()), persistanceManager.getTotalRetries(), persistanceManager.getRequestTimeout());


    }

    private void getJobDetails(ArrayList<Integer> jobIds) {

        SimpleRequests simpleRequests = new SimpleRequests();

        final PersistenceManager persistanceManager = PersistenceManager.getInstance();

        simpleRequests.getJobDetails(persistanceManager.getSiteUrl(), new GetJobDetailsCallback() {

            @Override
            public void onGetJobDetailsSuccess(Object response) {

                mTitleTv.setText(String.valueOf(((JobDetailsResponse) response).getJobs().get(0).getID()));
            }

            @Override
            public void onGetJobDetailsFailed(ErrorObjectInterface reason) {

            }
        }, NetworkManager.getInstance(), new JobDetailsRequest(persistanceManager.getSessionId(), jobIds), persistanceManager.getTotalRetries(), persistanceManager.getRequestTimeout());


    }

    private void headerListToHashMap(List<Header> headers) {

        mHashMapHeaders = new HashMap<>();

        for (Header header : headers) {

            mHashMapHeaders.put(header.getName(), header);
        }

    }

    private void initVars() {

        mTitleTv = findViewById(R.id.AJA_job_id_tv);

        mProductShema = findViewById(R.id.AJA_img1);

        mProductImage = findViewById(R.id.AJA_img2);

        mProductShemaNoImageLy = findViewById(R.id.AJA_img1_no_image);

        mProductImageNoImageLy = findViewById(R.id.AJA_img2_no_image);

        initVarsSearch();

        initVarsTitleLine();

        initVarsMaterialItem();

        initVarsMoldItem();

        initActionsItem();

    }

    private void initActionsItem() {

        mActionItemLy = findViewById(R.id.AJA_actions_ly);

        mActionRv = findViewById(R.id.AJA_actions_rv);
    }

    private void initVarsMoldItem() {

        mMoldItem = findViewById(R.id.AJA_item_mold);

        mMoldItemTitleTv = mMoldItem.findViewById(R.id.IJAM_title);

        mMoldItemImg = mMoldItem.findViewById(R.id.IJAM_img);

        mMoldItemRv = mMoldItem.findViewById(R.id.IJAM_rv);
    }

    private void initVarsMaterialItem() {

        mMaterialItem = findViewById(R.id.AJA_item_material);

        mMaterialItemTitleTv = mMaterialItem.findViewById(R.id.JAMI_title);

        mMaterialItemRv = mMaterialItem.findViewById(R.id.JAMI_rv);
    }

    private void initVarsTitleLine() {
        mTitlLine1Tv1 = findViewById(R.id.AJA_title_line1_tv1);
        mTitlLine1Tv2 = findViewById(R.id.AJA_title_line1_tv2);
        mTitlLine1Tv3 = findViewById(R.id.AJA_title_line1_tv3);
        mTitlLine1Tv4 = findViewById(R.id.AJA_title_line1_tv4);
        mTitlLine1Tv5 = findViewById(R.id.AJA_title_line1_tv5);
        mTitlLine1Tv6 = findViewById(R.id.AJA_title_line1_tv6);

        mTit2Line1Tv1 = findViewById(R.id.AJA_title_line2_tv1);
        mTit2Line1Tv2 = findViewById(R.id.AJA_title_line2_tv2);
        mTit2Line1Tv3 = findViewById(R.id.AJA_title_line2_tv3);
        mTit2Line1Tv4 = findViewById(R.id.AJA_title_line2_tv4);
        mTit2Line1Tv5 = findViewById(R.id.AJA_title_line2_tv5);
        mTit2Line1Tv6 = findViewById(R.id.AJA_title_line2_tv6);
    }

    private void initVarsSearch() {
        mSearchViewEt = findViewById(R.id.AJA_search_et);
        mHeadersRv = findViewById(R.id.AJA_search_rv);
        mPendingJobsRv = findViewById(R.id.AJA_product_rv);

    }

    private void initView() {

        initRecyclerViews();

    }

    private void initRecyclerViews() {

        sortHeaders();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mHeadersAdapter = new JobHeadersAdaper(mHeaders, mHashMapHeaders, this, this);
        mHeadersRv.setLayoutManager(layoutManager);
        mHeadersRv.setAdapter(mHeadersAdapter);

        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mPandingJobsAdaper = new PendingJobsAdapter(mPandingJobs, mHashMapHeaders, this, this);
        mPendingJobsRv.setLayoutManager(layoutManager2);
        mPendingJobsRv.setAdapter(mPandingJobsAdaper);
    }

    private void initListener() {

        findViewById(R.id.AJA_back_btn).setOnClickListener(this);
        findViewById(R.id.AJA_search_btn).setOnClickListener(this);
        findViewById(R.id.AJA_job_activate_btn).setOnClickListener(this);

        mSearchViewEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                updateRvBySearchResult();
            }
        });
    }

    public void updateRvBySearchResult() {

        mPandingJobs.clear();

        mHeaders.clear();

        for (PandingJob pandingJob : mPendingJobsResponse.getPandingJobs()) {

            for (Property property : pandingJob.getProperties()) {

                if (property.getValue() != null && property.getValue().contains(mSearchViewEt.getText())) {

                    if (!mPandingJobs.contains(pandingJob)) {
                        mPandingJobs.add(pandingJob);
                    }

                    if (!mHeaders.contains(mHashMapHeaders.get(property.getKey())))
                        mHeaders.add(mHashMapHeaders.get(property.getKey()));
                }
            }
        }

        mPendingJobsNoHeadersFiltered.clear();
        mPendingJobsNoHeadersFiltered.addAll(mPandingJobs);
        sortHeaders();
        mHeadersAdapter.notifyDataSetChanged();
        mPandingJobsAdaper.notifyDataSetChanged();
    }

    private void sortHeaders() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mHeaders.sort(new Comparator<Header>() {
                @Override
                public int compare(Header o1, Header o2) {
                    if (o1.getOrder().equals(o2.getOrder())) {
                        return 0;
                    } else if (o1.getOrder() <
                            o2.getOrder()) {
                        return -1;
                    }
                    return 1;
                }
            });
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onHeaderSelected(HashMap<String, Header> hashMapHeader) {

        filterPandingJobsByHeaders(hashMapHeader);
    }

    public void filterPandingJobsByHeaders(HashMap<String, Header> hashMapHeader) {

        mHashMapHeaders = hashMapHeader;

        boolean isSelectedByUser = false;

        for (Map.Entry<String, Header> headerEntry : mHashMapHeaders.entrySet()) {

            if (headerEntry.getValue().isSelected()) {

                isSelectedByUser = true;
            }
        }

        if (!isSelectedByUser) {

            mPandingJobs.clear();
            mPandingJobs.addAll(mPendingJobsNoHeadersFiltered);
            mPandingJobsAdaper.notifyDataSetChanged();

            return;
        }

        mPandingJobs.clear();

        for (PandingJob pandingJob : mPendingJobsNoHeadersFiltered) {

            for (Property property : pandingJob.getProperties()) {

                if (property.getKey() != null && mHashMapHeaders.get(property.getKey()).isSelected()) {

                    if (!mPandingJobs.contains(pandingJob)) {
                        mPandingJobs.add(pandingJob);
                    }
                }
            }
        }

        mPandingJobsAdaper.notifyDataSetChanged();
    }

    @Override
    public void onPandingJobSelected(PandingJob pandingJob) {

        ArrayList<Integer> jobIds = new ArrayList<>();
        jobIds.add(mPendingJobsResponse.getPandingJobs().get(0).getID());
        getJobDetails(jobIds);
    }
}
