package com.operatorsapp.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.operators.errorobject.ErrorObjectInterface;
import com.operators.reportrejectinfra.GetJobDetailsCallback;
import com.operators.reportrejectinfra.GetPendingJobListCallback;
import com.operators.reportrejectinfra.PostActivateJobCallback;
import com.operators.reportrejectinfra.PostUpdtaeActionsCallback;
import com.operators.reportrejectnetworkbridge.server.ErrorObject;
import com.operators.reportrejectnetworkbridge.server.response.activateJob.Action;
import com.operators.reportrejectnetworkbridge.server.response.activateJob.ActionsByJob;
import com.operators.reportrejectnetworkbridge.server.response.activateJob.ActionsUpdateRequest;
import com.operators.reportrejectnetworkbridge.server.response.activateJob.ActivateJobRequest;
import com.operators.reportrejectnetworkbridge.server.response.activateJob.GetPendingJobListRequest;
import com.operators.reportrejectnetworkbridge.server.response.activateJob.Header;
import com.operators.reportrejectnetworkbridge.server.response.activateJob.JobDetailsRequest;
import com.operators.reportrejectnetworkbridge.server.response.activateJob.JobDetailsResponse;
import com.operators.reportrejectnetworkbridge.server.response.activateJob.PandingJob;
import com.operators.reportrejectnetworkbridge.server.response.activateJob.PendingJobResponse;
import com.operators.reportrejectnetworkbridge.server.response.activateJob.Property;
import com.operators.reportrejectnetworkbridge.server.response.activateJob.Response;
import com.operatorsapp.R;
import com.operatorsapp.adapters.JobActionsAdapter;
import com.operatorsapp.adapters.JobHeadersAdaper;
import com.operatorsapp.adapters.JobMaterialsSplitAdapter;
import com.operatorsapp.adapters.PendingJobsAdapter;
import com.operatorsapp.fragments.RecipeFragment;
import com.operatorsapp.fragments.interfaces.OnCroutonRequestListener;
import com.operatorsapp.managers.CroutonCreator;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.managers.ProgressDialogManager;
import com.operatorsapp.model.PdfObject;
import com.operatorsapp.server.NetworkManager;
import com.operatorsapp.utils.DownloadHelper;
import com.operatorsapp.utils.ShowCrouton;
import com.operatorsapp.utils.SimpleRequests;
import com.shockwave.pdfium.PdfDocument;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JobActionActivity extends AppCompatActivity implements View.OnClickListener,
        JobHeadersAdaper.JobHeadersAdaperListener,
        PendingJobsAdapter.PendingJobsAdapterListener,
        DownloadHelper.DownloadFileListener,
        JobMaterialsSplitAdapter.JobMaterialsSplitAdapterListener,
        JobActionsAdapter.JobActionsAdapterListener,
        RecipeFragment.OnRecipeFragmentListener,
        OnCroutonRequestListener,
        CroutonCreator.CroutonListener {

    private static final String TAG = JobActionActivity.class.getSimpleName();
    private static final String JOB_ACTION_FRAGMENT = "JOB_ACTION_FRAGMENT";
    public static final String EXTRA_ACTIVATE_JOB_RESPONSE = "EXTRA_ACTIVATE_JOB_RESPONSE";
    public static final int EXTRA_ACTIVATE_JOB_CODE = 111;
    public static final String EXTRA_ACTIVATE_JOB_ID = "EXTRA_ACTIVATE_JOB_ID";
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
    private PDFView mProductPdfView;
    private ImageView mProductImage;
    private View mProductPdfNoImageLy;
    private View mProductImageNoImageLy;
    private TextView mMaterialItemTitleTv;
    private RecyclerView mMaterialItemRv;
    private TextView mMoldItemTitleTv;
    private ImageView mMoldItemImg;
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
    private JobDetailsResponse mCurrentJobDetails;
    private PandingJob mCurrentPendingJob;
    private DownloadHelper mDownloadHelper;
    private String mFirstPdf;
    private JobMaterialsSplitAdapter mMaterialAdapter;
    private TextView mMoldNameTv;
    private TextView mMoldMoldCatalogTv;
    private TextView mMoldcavitiesActualTv;
    private TextView mMoldCavitiesStandardTv;
    private JobActionsAdapter mActionsAdapter;
    private TextView mActionsTitleTv;
    private RecipeFragment mRecipefragment;
    private Intent mGalleryIntent;
    private ArrayList<PdfObject> mPdfList = new ArrayList<>();
    private CroutonCreator mCroutonCreator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_action);

        mCroutonCreator = new CroutonCreator();

        initVars();

        initListener();

        mDownloadHelper = new DownloadHelper(this, this);

        getPendingJoblist();

    }

    private void getPendingJoblist() {

        SimpleRequests simpleRequests = new SimpleRequests();

        final PersistenceManager persistanceManager = PersistenceManager.getInstance();

        ProgressDialogManager.show(this);

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

                    initLeftView();

                    mCurrentPendingJob = mPendingJobsResponse.getPandingJobs().get(0);

                    ArrayList<Integer> jobIds = new ArrayList<>();
                    jobIds.add(mPendingJobsResponse.getPandingJobs().get(0).getID());
                    getJobDetails(jobIds);

                    ProgressDialogManager.dismiss();

                }

            }

            @Override
            public void onGetPendingJobListFailed(ErrorObjectInterface reason) {

                ProgressDialogManager.dismiss();
                ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Retrofit, getString(R.string.get_panding_jobs_failed_error));
                ShowCrouton.jobsLoadingErrorCrouton(JobActionActivity.this, errorObject);

            }
        }, NetworkManager.getInstance(), new GetPendingJobListRequest(persistanceManager.getSessionId(), persistanceManager.getMachineId()), persistanceManager.getTotalRetries(), persistanceManager.getRequestTimeout());


    }

    private void getJobDetails(ArrayList<Integer> jobIds) {

        SimpleRequests simpleRequests = new SimpleRequests();

        final PersistenceManager persistanceManager = PersistenceManager.getInstance();

        ProgressDialogManager.show(this);

        simpleRequests.getJobDetails(persistanceManager.getSiteUrl(), new GetJobDetailsCallback() {

            @Override
            public void onGetJobDetailsSuccess(Object response) {

                ProgressDialogManager.dismiss();
                mCurrentJobDetails = (JobDetailsResponse) response;
                initRightView();
            }

            @Override
            public void onGetJobDetailsFailed(ErrorObjectInterface reason) {

                ProgressDialogManager.dismiss();

                ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Retrofit, getString(R.string.get_jobs_details_failed_error));
                ShowCrouton.jobsLoadingErrorCrouton(JobActionActivity.this, errorObject);
            }
        }, NetworkManager.getInstance(), new JobDetailsRequest(persistanceManager.getSessionId(), jobIds), persistanceManager.getTotalRetries(), persistanceManager.getRequestTimeout());


    }

    private void postUpdateActions(Action action) {

        final PersistenceManager persistanceManager = PersistenceManager.getInstance();

        ActionsUpdateRequest actionsUpdateRequest = new ActionsUpdateRequest(persistanceManager.getSessionId(), null);

        actionsUpdateRequest.setActions(new ActionsByJob(String.valueOf(mCurrentJobDetails.getJobs().get(0).getID()), persistanceManager.getOperatorId(), null));

        actionsUpdateRequest.getActions().setActions(Collections.singletonList(action));

        SimpleRequests simpleRequests = new SimpleRequests();

        simpleRequests.postUpdtaeActions(persistanceManager.getSiteUrl(), new PostUpdtaeActionsCallback() {

            @Override
            public void onPostUpdtaeActionsSuccess(Object response) {

            }

            @Override
            public void onPostUpdtaeActionsFailed(ErrorObjectInterface reason) {

            }
        }, NetworkManager.getInstance(), actionsUpdateRequest, persistanceManager.getTotalRetries(), persistanceManager.getRequestTimeout());


    }

    private void postActivateJob(ActivateJobRequest activateJobRequest) {

        final PersistenceManager persistanceManager = PersistenceManager.getInstance();

        SimpleRequests simpleRequests = new SimpleRequests();

        ProgressDialogManager.show(this);

        simpleRequests.postActivateJob(persistanceManager.getSiteUrl(), new PostActivateJobCallback() {

            @Override
            public void onPostActivateJobSuccess(Object response) {

                ProgressDialogManager.dismiss();

                if (response == null) {

                    ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Retrofit, "PostActivateJob_Failed Error");
                    ShowCrouton.jobsLoadingErrorCrouton(JobActionActivity.this, errorObject);

                } else if (((Response) response).getError() != null) {

                    ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Retrofit, ((Response) response).getError().getErrorDesc());
                    ShowCrouton.jobsLoadingErrorCrouton(JobActionActivity.this, errorObject);


                } else {

                    finishActivity((Response) response);
                }
            }

            @Override
            public void onPostActivateJobFailed(ErrorObjectInterface reason) {


                ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Retrofit, "PostActivateJob_Failed Error");
                ShowCrouton.jobsLoadingErrorCrouton(JobActionActivity.this, errorObject);
            }
        }, NetworkManager.getInstance(), activateJobRequest, persistanceManager.getTotalRetries(), persistanceManager.getRequestTimeout());


    }

    public void finishActivity(Response response) {
        Intent intent = getIntent();
        intent.putExtra(EXTRA_ACTIVATE_JOB_RESPONSE, response);
        intent.putExtra(EXTRA_ACTIVATE_JOB_ID, mCurrentJobDetails.getJobs().get(0).getID());

        setResult(RESULT_OK, intent);

        ProgressDialogManager.dismiss();

        finish();
    }

    public void initRightView() {

        initTitleView();

        initImagesViews();

        initViewsTitleLine();

        initViewsMaterialItem();

        initViewsMoldItem();

        initActionsItemView();
    }

    public void initTitleView() {

        mTitleTv.setText(String.valueOf(mCurrentJobDetails.getJobs().get(0).getID()));

        for (Property property : mCurrentPendingJob.getProperties()) {

            if (property.getKey().equals("ERPJobID") && property.getValue() != null && property.getValue().length() > 0) {

                mTitleTv.setText(String.format("%s (%s)", property.getValue(), String.valueOf(mCurrentJobDetails.getJobs().get(0).getID())));

            }
        }
    }

    private void initActionsItemView() {

        mCurrentJobDetails.getJobs().get(0).setActions(sortActions(mCurrentJobDetails.getJobs().get(0).getActions()));

        if (mCurrentJobDetails.getJobs().get(0).getActions() != null) {

            mActionsTitleTv.setText(getString(R.string.actions));

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

            mActionRv.setLayoutManager(layoutManager);

            mActionsAdapter = new JobActionsAdapter(mCurrentJobDetails.getJobs().get(0).getActions(), this, this);

            mActionRv.setAdapter(mActionsAdapter);

        } else {

            mActionItemLy.setVisibility(View.GONE);
        }
    }

    private List<Action> sortActions(List<Action> actions) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            actions.sort(new Comparator<Action>() {
                @Override
                public int compare(Action o1, Action o2) {
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

        return actions;
    }

    private void initViewsMoldItem() {

        if (mCurrentJobDetails.getJobs().get(0).getMold() != null) {


            mMoldItemTitleTv.setText(R.string.mold);

            for (String url : mCurrentJobDetails.getJobs().get(0).getMold().getFiles()) {

                if (!url.endsWith("pdf")) {

                    ImageLoader.getInstance().displayImage(url, mMoldItemImg);

                    break;
                }
            }

            mMoldNameTv.setText(mCurrentJobDetails.getJobs().get(0).getMold().getName());

            mMoldMoldCatalogTv.setText(mCurrentJobDetails.getJobs().get(0).getMold().getCatalog());

            mMoldcavitiesActualTv.setText(String.valueOf(mCurrentJobDetails.getJobs().get(0).getMold().getCavitiesActual()));

            mMoldCavitiesStandardTv.setText(String.valueOf(mCurrentJobDetails.getJobs().get(0).getMold().getCavitiesStandard()));

        } else {

            mMoldItem.setVisibility(View.GONE);
        }
    }

    public void initImagesViews() {

        String firstImage = null;
        mFirstPdf = null;

        for (String url : mCurrentJobDetails.getJobs().get(0).getProductFiles()) {

            if (url.endsWith("pdf")) {

                if (mFirstPdf == null) {

                    mFirstPdf = url;

                }
            } else {

                if (firstImage == null) {

                    firstImage = url;
                }
            }
        }

        if (mFirstPdf != null) {

            if (isStoragePermissionGranted()) {

                mDownloadHelper.downloadFileFromUrl(mFirstPdf);
            }

        } else {

            mProductPdfNoImageLy.setVisibility(View.VISIBLE);
            mProductPdfView.setVisibility(View.INVISIBLE);

        }

        if (firstImage != null) {

            ImageLoader.getInstance().displayImage(firstImage, mProductImage);

            mProductImageNoImageLy.setVisibility(View.INVISIBLE);
            mProductImage.setVisibility(View.VISIBLE);

        } else {

            mProductImageNoImageLy.setVisibility(View.VISIBLE);
            mProductImage.setVisibility(View.INVISIBLE);

        }

    }

    private void initViewsMaterialItem() {

        if (mCurrentJobDetails.getJobs().get(0).getMaterials() != null) {

            mMaterialItemTitleTv.setText(R.string.materials);

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

            mMaterialItemRv.setLayoutManager(layoutManager);

            mMaterialAdapter = new JobMaterialsSplitAdapter(mCurrentJobDetails.getJobs().get(0).getMaterials(), this, this);

            mMaterialItemRv.setAdapter(mMaterialAdapter);

        } else {

            mMaterialItem.setVisibility(View.GONE);
        }
    }

    private void initViewsTitleLine() {

        ArrayList<Property> properties = new ArrayList<>();

        for (Property property : mCurrentPendingJob.getProperties()) {

            if (mHashMapHeaders.get(property.getKey()).getShowOnHeader()) {

                properties.add(property);
            }
        }

        if (properties.size() > 0) {
            mTitlLine1Tv1.setText(getResizedString(mHashMapHeaders.get(properties.get(0).getKey()).getDisplayName()));
            mTitlLine1Tv2.setText(getResizedString(properties.get(0).getValue()));
        }
        if (properties.size() > 1) {
            mTitlLine1Tv3.setText(getResizedString(mHashMapHeaders.get(properties.get(1).getKey()).getDisplayName()));
            mTitlLine1Tv4.setText(getResizedString(properties.get(1).getValue()));
        }
        if (properties.size() > 2) {
            mTitlLine1Tv5.setText(getResizedString(mHashMapHeaders.get(properties.get(2).getKey()).getDisplayName()));
            mTitlLine1Tv6.setText(getResizedString(properties.get(2).getValue()));
        }
        if (properties.size() > 3) {
            mTit2Line1Tv1.setText(getResizedString(mHashMapHeaders.get(properties.get(3).getKey()).getDisplayName()));
            mTit2Line1Tv2.setText(getResizedString(properties.get(3).getValue()));
        }
        if (properties.size() > 4) {
            mTit2Line1Tv3.setText(getResizedString(mHashMapHeaders.get(properties.get(4).getKey()).getDisplayName()));
            mTit2Line1Tv4.setText(getResizedString(properties.get(4).getValue()));
        }
        if (properties.size() > 5) {
            mTit2Line1Tv5.setText(getResizedString(mHashMapHeaders.get(properties.get(5).getKey()).getDisplayName()));
            mTit2Line1Tv6.setText(getResizedString(properties.get(5).getValue()));
        }

    }

    private String getResizedString(String s) {

        if (s != null && s.length() > 10) {
            return s.substring(0, 10) + "...";
        } else {
            return s;
        }
    }

    private void headerListToHashMap(List<Header> headers) {

        mHashMapHeaders = new HashMap<>();

        for (Header header : headers) {

            mHashMapHeaders.put(header.getName(), header);
        }

    }

    private void initVars() {

        mTitleTv = findViewById(R.id.AJA_job_id_tv);

        mProductPdfView = findViewById(R.id.AJA_img1);

        mProductImage = findViewById(R.id.AJA_img2);

        mProductPdfNoImageLy = findViewById(R.id.AJA_img1_no_image);

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

        mActionsTitleTv = findViewById(R.id.AJA_actions_tv);
    }

    private void initVarsMoldItem() {

        mMoldItem = findViewById(R.id.AJA_item_mold);

        mMoldItemTitleTv = mMoldItem.findViewById(R.id.IJAM_title);

        mMoldItemImg = mMoldItem.findViewById(R.id.IJAM_img);

        mMoldNameTv = mMoldItem.findViewById(R.id.IJAM_mold1_tv2);

        mMoldMoldCatalogTv = mMoldItem.findViewById(R.id.IJAM_mold2_tv2);

        mMoldcavitiesActualTv = mMoldItem.findViewById(R.id.IJAM_mold3_tv2);

        mMoldCavitiesStandardTv = mMoldItem.findViewById(R.id.IJAM_mold4_tv2);
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

    private void initLeftView() {

        initRecyclerViews();

    }

    private void initRecyclerViews() {

        sortHeaders();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mHeadersAdapter = new JobHeadersAdaper(mHeaders, mHashMapHeaders, this, this);
        mHeadersRv.setLayoutManager(layoutManager);
        mHeadersRv.setAdapter(mHeadersAdapter);

        mPandingJobs.get(0).setSelected(true);
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mPandingJobsAdaper = new PendingJobsAdapter(mPandingJobs, mHashMapHeaders, this, this);
        mPendingJobsRv.setLayoutManager(layoutManager2);
        mPendingJobsRv.setAdapter(mPandingJobsAdaper);
    }

    private void initListener() {

        findViewById(R.id.AJA_back_btn).setOnClickListener(this);
        findViewById(R.id.AJA_search_btn).setOnClickListener(this);
        findViewById(R.id.AJA_job_activate_btn).setOnClickListener(this);
        findViewById(R.id.AJA_item_material).setOnClickListener(this);
        findViewById(R.id.AJA_img1).setOnClickListener(this);
        findViewById(R.id.AJA_img2).setOnClickListener(this);

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

                if (property.getValue() != null && property.getValue().toLowerCase().contains(mSearchViewEt.getText().toString().toLowerCase())) {

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

    public boolean isStoragePermissionGranted() {

        if (Build.VERSION.SDK_INT >= 23) {

            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                return true;

            } else {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

                return false;
            }
        } else {

            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            mDownloadHelper.downloadFileFromUrl(mFirstPdf);

        } else {

            mProductPdfNoImageLy.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.AJA_back_btn:

                onBackPressed();

                break;

            case R.id.AJA_search_btn:

                break;

            case R.id.AJA_job_activate_btn:

                PersistenceManager persistenceManager = PersistenceManager.getInstance();
                postActivateJob(new ActivateJobRequest(persistenceManager.getSessionId(),
                        String.valueOf(persistenceManager.getMachineId()),
                        String.valueOf(mCurrentJobDetails.getJobs().get(0).getID()),
                        persistenceManager.getOperatorId()));

                break;

            case R.id.AJA_item_material:

                showRecipeFragment();

                break;

            case R.id.AJA_img1:

                startGalleryActivity(mCurrentJobDetails.getJobs().get(0).getProductFiles(),
                        String.valueOf(mCurrentJobDetails.getJobs().get(0).getID()));

                break;

            case R.id.AJA_img2:

                startGalleryActivity(mCurrentJobDetails.getJobs().get(0).getProductFiles(),
                        String.valueOf(mCurrentJobDetails.getJobs().get(0).getID()));

                break;
        }
    }

    private void showRecipeFragment() {

        mRecipefragment = RecipeFragment.newInstance(mCurrentJobDetails.getJobs().get(0).getRecipe());

        getSupportFragmentManager().beginTransaction().add(R.id.AJA_container, mRecipefragment).addToBackStack(JOB_ACTION_FRAGMENT).commit();
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

                if (property.getKey() != null && mHashMapHeaders.get(property.getKey()).isSelected()
                        && property.getValue() != null && mSearchViewEt.getText() != null &&
                        property.getValue().toLowerCase().contains(mSearchViewEt.getText().toString().toLowerCase())) {

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

        mCurrentPendingJob = pandingJob;
        ArrayList<Integer> jobIds = new ArrayList<>();
        jobIds.add(pandingJob.getID());
        getJobDetails(jobIds);
    }

    @Override
    public void onPostExecute(File file) {

        loadPdfView(Uri.fromFile(file));
    }

    private void loadPdfView(Uri uri) {

        mProductPdfView.fromUri(uri)
                .defaultPage(0)
                .enableSwipe(false)
                .swipeHorizontal(false)
                .enableAnnotationRendering(false)
                .onLoad(new OnLoadCompleteListener() {
                    @Override
                    public void loadComplete(int nbPages) {

                        mProductPdfNoImageLy.setVisibility(View.INVISIBLE);
                        mProductPdfView.setVisibility(View.VISIBLE);

                        PdfDocument.Meta meta = mProductPdfView.getDocumentMeta();
                        printBookmarksTree(mProductPdfView.getTableOfContents(), "-");

                    }
                })
                .onError(new OnErrorListener() {
                    @Override
                    public void onError(Throwable t) {


                    }
                })
                .scrollHandle(new DefaultScrollHandle(this))
                .load();
    }

    public void printBookmarksTree(List<PdfDocument.Bookmark> tree, String sep) {
        for (PdfDocument.Bookmark b : tree) {

            Log.e(TAG, String.format("%s %s, p %d", sep, b.getTitle(), b.getPageIdx()));

            if (b.hasChildren()) {
                printBookmarksTree(b.getChildren(), sep + "-");
            }
        }
    }

    @Override
    public void onLoadFileError() {

    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onActionChecked(Action action) {

        postUpdateActions(action);
    }

    @Override
    public void onOpenActionDetails(Action action) {

        showActionDialog(action);
    }

    @Override
    public void onImageProductClick(List<String> fileUrl, String name) {

        startGalleryActivity(fileUrl, name);
    }

    private void startGalleryActivity(List<String> fileUrl, String name) {

        if (fileUrl != null && fileUrl.size() > 0) {

            mGalleryIntent = new Intent(JobActionActivity.this, GalleryActivity.class);

            mGalleryIntent.putExtra(GalleryActivity.EXTRA_FILE_URL, (ArrayList<String>) fileUrl);

            mGalleryIntent.putExtra(GalleryActivity.EXTRA_RECIPE_FILES_TITLE, name);

            mGalleryIntent.putExtra(GalleryActivity.EXTRA_RECIPE_PDF_FILES, mPdfList);

            startActivityForResult(mGalleryIntent, GalleryActivity.EXTRA_GALLERY_CODE);

        }
    }

    @Override
    public void onBackPressed() {
        int count = getFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            getFragmentManager().popBackStack();
        }
    }

    private void showActionDialog(final Action action) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final EditText editText = new EditText(this);
        editText.setHint(R.string.you_can_add_a_note);
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        editText.setImeOptions(EditorInfo.IME_ACTION_DONE);

        builder.setTitle(R.string.action_details);
        builder.setMessage(action.getText());

        builder.setView(editText);

        builder.setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                action.setNotes(editText.getText().toString());
                postUpdateActions(action);

            }
        });

        builder.setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                dialog.dismiss();

            }
        });

        final AlertDialog alert = builder.create();
        alert.show();

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    action.setNotes(editText.getText().toString());
                    postUpdateActions(action);

                    alert.dismiss();

                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onShowCroutonRequest(String croutonMessage, int croutonDurationInMilliseconds, int viewGroup, CroutonCreator.CroutonType croutonType) {

        mCroutonCreator.showCrouton(this, String.valueOf(croutonMessage), croutonDurationInMilliseconds, R.id.AJA_root_relative_ly, croutonType, this);

    }

    @Override
    public void onShowCroutonRequest(SpannableStringBuilder croutonMessage, int croutonDurationInMilliseconds, int viewGroup, CroutonCreator.CroutonType croutonType) {

        if (mCroutonCreator != null) {
            mCroutonCreator.showCrouton(this, String.valueOf(croutonMessage), croutonDurationInMilliseconds,  R.id.AJA_root_relative_ly, croutonType, this);

        }
    }

    @Override
    public void onHideConnectivityCroutonRequest() {

    }

    @Override
    public void onCroutonDismiss() {

    }
}
