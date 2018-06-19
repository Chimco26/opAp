package com.operatorsapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.operators.errorobject.ErrorObjectInterface;
import com.operators.reportrejectinfra.GetPendingJobListCallback;
import com.operators.reportrejectnetworkbridge.server.response.activateJob.ActivateJobRequest;
import com.operators.reportrejectnetworkbridge.server.response.activateJob.Header;
import com.operators.reportrejectnetworkbridge.server.response.activateJob.PandingJob;
import com.operators.reportrejectnetworkbridge.server.response.activateJob.PendingJobResponse;
import com.operatorsapp.R;
import com.operatorsapp.adapters.JobHeadersAdaper;
import com.operatorsapp.adapters.PendingJobsAdapter;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.server.NetworkManager;
import com.operatorsapp.utils.SimpleRequests;

import java.util.ArrayList;

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
    private EditText mSearchViewEt;
    private RecyclerView mHeadersRv;
    private RecyclerView mPendingJobsRv;
    private ImageView mProductShema;
    private ImageView mProductImage;
    private View mProductShemaNoImageLy;
    private View mProductImageNoImageLy;
    private View mMaterialItem;
    private TextView mMaterialItemTitleTv;
    private RecyclerView mMaterialItemRv;
    private View mMoldItem;
    private TextView mMoldItemTitleTv;
    private ImageView mMoldItemImg;
    private RecyclerView mMoldItemRv;
    private RecyclerView mActionRv;
    private View mActionItemLy;
    private PendingJobResponse mPendingJobs;
    private JobHeadersAdaper mHeadersAdapter;
    private PendingJobsAdapter mPendingJobsAdaper;

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

                mPendingJobs = (PendingJobResponse) response;

                initView();

            }

            @Override
            public void onGetPendingJobListFailed(ErrorObjectInterface reason) {

            }
        }, NetworkManager.getInstance(), new ActivateJobRequest(persistanceManager.getSessionId(), persistanceManager.getMachineId()), persistanceManager.getTotalRetries(), persistanceManager.getRequestTimeout());


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

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mHeadersAdapter = new JobHeadersAdaper((ArrayList<Header>) mPendingJobs.getHeaders(), this, this);
        mHeadersRv.setLayoutManager(layoutManager);
        mHeadersRv.setAdapter(mHeadersAdapter);

        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mPendingJobsAdaper = new PendingJobsAdapter((ArrayList<PandingJob>) mPendingJobs.getPandingJobs(), this, this);
        mPendingJobsRv.setLayoutManager(layoutManager2);
        mPendingJobsRv.setAdapter(mPendingJobsAdaper);

    }

    private void initListener() {

        findViewById(R.id.AJA_back_btn).setOnClickListener(this);
        findViewById(R.id.AJA_search_btn).setOnClickListener(this);
        findViewById(R.id.AJA_job_activate_btn).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }

    private String searchInList(String search, ArrayList<String> arrayList){

        for (String string: arrayList){

            if (string.toLowerCase().contains(search.toLowerCase())){

                return string;
            }
        }

        return "";
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
