package com.operatorsapp.activities;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.operatorsapp.adapters.JobHeadersAdapter;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class JobActionActivity extends AppCompatActivity implements View.OnClickListener,
        JobHeadersAdapter.JobHeadersAdaperListener,
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
    private TextView mTitleLine1Tv1;
    private TextView mTitleLine1Tv2;
    private TextView mTitleLine1Tv3;
    private TextView mTitleLine1Tv4;
    private TextView mTit2Line1Tv1;
    private TextView mTit2Line1Tv2;
    private TextView mTit2Line1Tv3;
    private TextView mTit2Line1Tv4;
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
    private JobHeadersAdapter mHeadersAdapter;
    private PendingJobsAdapter mPendingJobsAdapter;
    private HashMap<String, Header> mHashMapHeaders;
    private ArrayList<Header> mHeaders = new ArrayList<>();
    private ArrayList<PandingJob> mPendingJobs = new ArrayList<>();
    private ArrayList<PandingJob> mPendingJobsNoHeadersFiltered = new ArrayList<>();
    private JobDetailsResponse mCurrentJobDetails;
    private PandingJob mCurrentPendingJob;
    private DownloadHelper mDownloadHelper;
    private String mFirstPdf;
    private TextView mMoldNameTv;
    private TextView mMoldMoldCatalogTv;
    private TextView mMoldCavitiesActualTv;
    private TextView mMoldCavitiesStandardTv;
    private TextView mActionsTitleTv;
    private ArrayList<PdfObject> mPdfList = new ArrayList<>();
    private CroutonCreator mCroutonCreator;
    private ArrayList<Action> mUpdatedActions;
    private TextView mProductPdfNoImageTv;

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

                if (mPendingJobsResponse != null && mPendingJobsResponse.getPandingJobs() != null && mPendingJobsResponse.getPandingJobs().size() > 0) {

                    SimpleDateFormat actualFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());

                    for (PandingJob pandingJob : mPendingJobsResponse.getPandingJobs()) {
                        for (Property property : pandingJob.getProperties()) {

                            property.setValue(updateDateForRtl(property, actualFormat, dateFormat));

                        }
                    }

                    mHeaders.addAll(mPendingJobsResponse.getHeaders());

                    mPendingJobs.addAll(mPendingJobsResponse.getPandingJobs());

                    headerListToHashMap(mPendingJobsResponse.getHeaders());

                    mPendingJobsNoHeadersFiltered.addAll(mPendingJobs);

                    sortHeaders();

                    initLeftView();

                    mCurrentPendingJob = mPendingJobsResponse.getPandingJobs().get(0);

                    ArrayList<Integer> jobIds = new ArrayList<>();
                    jobIds.add(mPendingJobsResponse.getPandingJobs().get(0).getID());
                    getJobDetails(jobIds);

                    ProgressDialogManager.dismiss();

                } else {
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

                if (response == null) {

                    ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Retrofit, "PostActivateJob Failed");
                    ShowCrouton.jobsLoadingErrorCrouton(JobActionActivity.this, errorObject);

                } else if (((Response) response).getError() != null) {

                    ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Retrofit, ((Response) response).getError().getErrorDesc());
                    ShowCrouton.showSimpleCrouton(JobActionActivity.this, errorObject);


                } else {

                    mCurrentJobDetails = (JobDetailsResponse) response;
                    initRightView();
                }
            }

            @Override
            public void onGetJobDetailsFailed(ErrorObjectInterface reason) {

                ProgressDialogManager.dismiss();

                ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Retrofit, getString(R.string.get_jobs_details_failed_error));
                ShowCrouton.jobsLoadingErrorCrouton(JobActionActivity.this, errorObject);
            }
        }, NetworkManager.getInstance(), new JobDetailsRequest(persistanceManager.getSessionId(), jobIds), persistanceManager.getTotalRetries(), persistanceManager.getRequestTimeout());


    }

    private void postUpdateActions(ArrayList<Action> actions) {

        if (mCurrentJobDetails == null) {

            return;
        }

        final PersistenceManager persistanceManager = PersistenceManager.getInstance();

        ActionsUpdateRequest actionsUpdateRequest = new ActionsUpdateRequest(persistanceManager.getSessionId(), null);

        actionsUpdateRequest.setActions(new ActionsByJob(String.valueOf(mCurrentJobDetails.getJobs().get(0).getID()), persistanceManager.getOperatorId(), null));

        actionsUpdateRequest.getActions().setActions(actions);

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

                    ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Retrofit, "PostActivateJob Failed");
                    ShowCrouton.jobsLoadingErrorCrouton(JobActionActivity.this, errorObject);

                } else if (((Response) response).getError() != null) {

                    ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Retrofit, ((Response) response).getError().getErrorDesc());
                    ShowCrouton.showSimpleCrouton(JobActionActivity.this, errorObject);


                } else {

                    finishActivity((Response) response);
                }
            }

            @Override
            public void onPostActivateJobFailed(ErrorObjectInterface reason) {


                ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Retrofit, reason.getDetailedDescription());
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

    private String updateDateForRtl(Property property, SimpleDateFormat actualFormat, SimpleDateFormat newFormat) {

        if (property.getKey().contains("Time") && property.getValue() != null && property.getValue().length() > 0) {

            try {
                Date date = actualFormat.parse(property.getValue());
                return newFormat.format(date);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return property.getValue();
    }


    private void initActionsItemView() {

        mActionRv.setNestedScrollingEnabled(false);

        mCurrentJobDetails.getJobs().get(0).setActions(sortActions(mCurrentJobDetails.getJobs().get(0).getActions()));

        if (mCurrentJobDetails.getJobs().get(0).getActions() != null &&
                mCurrentJobDetails.getJobs().get(0).getActions().size() > 0) {

            mActionItemLy.setVisibility(View.VISIBLE);

            mActionsTitleTv.setText(getString(R.string.actions));

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

            mActionRv.setLayoutManager(layoutManager);

            JobActionsAdapter mActionsAdapter = new JobActionsAdapter(mCurrentJobDetails.getJobs().get(0).getActions(), this, this);

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

            mMoldCavitiesActualTv.setText(String.valueOf(mCurrentJobDetails.getJobs().get(0).getMold().getCavitiesActual()));

            mMoldCavitiesStandardTv.setText(String.valueOf(mCurrentJobDetails.getJobs().get(0).getMold().getCavitiesStandard()));

        } else {

            mMoldItem.setVisibility(View.GONE);
        }
    }

    public void initImagesViews() {

        String firstImage = null;
        mFirstPdf = null;

        mProductPdfNoImageTv.setText(getString(R.string.no_available_pdf));

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

            JobMaterialsSplitAdapter mMaterialAdapter = new JobMaterialsSplitAdapter(mCurrentJobDetails.getJobs().get(0).getMaterials(), this, this);

            mMaterialItemRv.setAdapter(mMaterialAdapter);

        } else {

            mMaterialItem.setVisibility(View.GONE);
        }
    }

    private void initViewsTitleLine() {

        ArrayList<Property> properties = (ArrayList<Property>) mCurrentPendingJob.getProperties();

        SimpleDateFormat actualFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy", Locale.getDefault());

        if (properties.size() > 0) {
            mTitleLine1Tv1.setText(getResizedString(mHashMapHeaders.get(properties.get(0).getKey()).getDisplayName(), 1));
            if (getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
                mTitleLine1Tv2.setText(getResizedString(updateDateForRtl(properties.get(0), actualFormat, dateFormat), 2));
            } else {
                mTitleLine1Tv2.setText(getResizedString(properties.get(0).getValue(), 2));
            }
        }
        if (properties.size() > 1) {
            mTitleLine1Tv3.setText(getResizedString(mHashMapHeaders.get(properties.get(1).getKey()).getDisplayName(), 1));
            if (getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
                mTitleLine1Tv4.setText(getResizedString(updateDateForRtl(properties.get(1), actualFormat, dateFormat), 2));
            } else {
                mTitleLine1Tv4.setText(getResizedString(properties.get(1).getValue(), 2));
            }
        }

        if (properties.size() > 2) {
            mTit2Line1Tv1.setText(getResizedString(mHashMapHeaders.get(properties.get(2).getKey()).getDisplayName(), 1));
            if (getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
                mTit2Line1Tv2.setText(getResizedString(updateDateForRtl(properties.get(2), actualFormat, dateFormat), 2));
            } else {
                mTit2Line1Tv2.setText(getResizedString(properties.get(2).getValue(), 2));
            }
        }
        if (properties.size() > 3) {
            mTit2Line1Tv3.setText(getResizedString(mHashMapHeaders.get(properties.get(3).getKey()).getDisplayName(), 1));
            if (getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
                mTit2Line1Tv4.setText(getResizedString(updateDateForRtl(properties.get(3), actualFormat, dateFormat), 2));
            } else {
                mTit2Line1Tv4.setText(getResizedString(properties.get(3).getValue(), 2));
            }
        }

    }

    private String getResizedString(String s, int type) {

        if (s == null || s.length() <= 20) {
            return s;

        } else {

            switch (type) {

                case 1:

                    return s.substring(0, 30) + "...";

                case 2:

                    return s.substring(0, 20) + "...";

                default:
                    return s;
            }

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

        mProductPdfNoImageTv = findViewById(R.id.AJA_img1_no_image_tv);

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

        mMoldCavitiesActualTv = mMoldItem.findViewById(R.id.IJAM_mold3_tv2);

        mMoldCavitiesStandardTv = mMoldItem.findViewById(R.id.IJAM_mold4_tv2);
    }

    private void initVarsMaterialItem() {

        mMaterialItem = findViewById(R.id.AJA_item_material);

        mMaterialItemTitleTv = mMaterialItem.findViewById(R.id.JAMI_title);

        mMaterialItemRv = mMaterialItem.findViewById(R.id.JAMI_rv);
    }

    private void initVarsTitleLine() {
        mTitleLine1Tv1 = findViewById(R.id.AJA_title_line1_tv1);
        mTitleLine1Tv2 = findViewById(R.id.AJA_title_line1_tv2);
        mTitleLine1Tv3 = findViewById(R.id.AJA_title_line1_tv3);
        mTitleLine1Tv4 = findViewById(R.id.AJA_title_line1_tv4);

        mTit2Line1Tv1 = findViewById(R.id.AJA_title_line2_tv1);
        mTit2Line1Tv2 = findViewById(R.id.AJA_title_line2_tv2);
        mTit2Line1Tv3 = findViewById(R.id.AJA_title_line2_tv3);
        mTit2Line1Tv4 = findViewById(R.id.AJA_title_line2_tv4);

    }

    private void initVarsSearch() {
        mSearchViewEt = findViewById(R.id.AJA_search_et);
        mHeadersRv = findViewById(R.id.AJA_search_rv);
        mPendingJobsRv = findViewById(R.id.AJA_product_rv);

    }

    private void initLeftView() {

        initRecyclerViews();

        if (getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {

            findViewById(R.id.AJA_back_btn).setRotationY(180);
        }

    }

    private void initRecyclerViews() {

        if (mPendingJobs != null && mPendingJobs.size() > 0) {
            sortHeaders();
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            mHeadersAdapter = new JobHeadersAdapter(mHeaders, mHashMapHeaders, this, this);
            mHeadersRv.setLayoutManager(layoutManager);
            mHeadersRv.setAdapter(mHeadersAdapter);

            mPendingJobs.get(0).setSelected(true);
            RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            mPendingJobsAdapter = new PendingJobsAdapter(mPendingJobs, mHashMapHeaders, this, this);
            mPendingJobsRv.setLayoutManager(layoutManager2);
            mPendingJobsRv.setAdapter(mPendingJobsAdapter);
        }
    }

    private void initListener() {

        findViewById(R.id.AJA_back_btn).setOnClickListener(this);
        findViewById(R.id.AJA_title).setOnClickListener(this);
        findViewById(R.id.AJA_search_btn).setOnClickListener(this);
        findViewById(R.id.AJA_job_activate_btn).setOnClickListener(this);
        findViewById(R.id.AJA_item_material).setOnClickListener(this);
        mMoldItemImg.setOnClickListener(this);
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

                if (s.length() < 1) {
                    for (Map.Entry<String, Header> headerEntry : mHashMapHeaders.entrySet()) {
                        mHashMapHeaders.get(headerEntry.getValue().getName()).setSelected(false);
                    }
                }

                updateRvBySearchResult();

            }
        });
    }

    public void updateRvBySearchResult() {

        mPendingJobs.clear();

        mHeaders.clear();

        for (PandingJob pandingJob : mPendingJobsResponse.getPandingJobs()) {

            for (Property property : pandingJob.getProperties()) {

                if (property.getValue() != null && property.getValue().toLowerCase().contains(mSearchViewEt.getText().toString().toLowerCase())) {

                    if (!mPendingJobs.contains(pandingJob)) {
                        mPendingJobs.add(pandingJob);
                    }

                    if (!mHeaders.contains(mHashMapHeaders.get(property.getKey())))
                        mHeaders.add(mHashMapHeaders.get(property.getKey()));
                }
            }
        }

        mPendingJobsNoHeadersFiltered.clear();
        mPendingJobsNoHeadersFiltered.addAll(mPendingJobs);
        sortHeaders();
        mHeadersAdapter.notifyDataSetChanged();
        mPendingJobsAdapter.notifyDataSetChanged();

        filterPendingJobsByHeaders();
    }

    public void filterPendingJobsByHeaders() {

        boolean isSelectedByUser = false;

        for (Map.Entry<String, Header> headerEntry : mHashMapHeaders.entrySet()) {

            if (headerEntry.getValue().isSelected()) {

                isSelectedByUser = true;
            }
        }

        if (!isSelectedByUser) {

            mPendingJobs.clear();
            mPendingJobs.addAll(mPendingJobsNoHeadersFiltered);
            mPendingJobsAdapter.notifyDataSetChanged();

            return;
        }

        mPendingJobs.clear();

        for (PandingJob pandingJob : mPendingJobsNoHeadersFiltered) {

            for (Property property : pandingJob.getProperties()) {

                if (property.getKey() != null && mHashMapHeaders.get(property.getKey()).isSelected()
                        && property.getValue() != null && mSearchViewEt.getText() != null &&
                        property.getValue().toLowerCase().contains(mSearchViewEt.getText().toString().toLowerCase())) {

                    if (!mPendingJobs.contains(pandingJob)) {
                        mPendingJobs.add(pandingJob);
                    }
                }
            }
        }

        mPendingJobsAdapter.notifyDataSetChanged();
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

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
            case R.id.AJA_title:

                onBackPressed();

                break;

            case R.id.AJA_search_btn:

                break;

            case R.id.AJA_job_activate_btn:

                postUpdateActions(mUpdatedActions);

                if (mCurrentJobDetails != null) {
                    PersistenceManager persistenceManager = PersistenceManager.getInstance();
                    postActivateJob(new ActivateJobRequest(persistenceManager.getSessionId(),
                            String.valueOf(persistenceManager.getMachineId()),
                            String.valueOf(mCurrentJobDetails.getJobs().get(0).getID()),
                            persistenceManager.getOperatorId()));
                }
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

            case R.id.IJAM_img:

                startGalleryActivity(mCurrentJobDetails.getJobs().get(0).getMold().getFiles(),
                        String.valueOf(mCurrentJobDetails.getJobs().get(0).getMold().getName()));

                break;
        }
    }

    private void showRecipeFragment() {

        RecipeFragment mRecipefragment = RecipeFragment.newInstance(mCurrentJobDetails.getJobs().get(0).getRecipe());

        getSupportFragmentManager().beginTransaction().add(R.id.AJA_container, mRecipefragment).addToBackStack(JOB_ACTION_FRAGMENT).commit();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onHeaderSelected(HashMap<String, Header> hashMapHeader) {

        mHashMapHeaders = hashMapHeader;

        filterPendingJobsByHeaders();
    }

    @Override
    public void onPandingJobSelected(PandingJob pandingJob) {

//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
//
//        SimpleDateFormat actualFormat = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy", Locale.getDefault());
//
//        for (Property property : mCurrentPendingJob.getProperties()) {
//            updateDateForRtl(property, actualFormat, dateFormat);
//        }

        for (PandingJob pandingJob1 : mPendingJobsResponse.getPandingJobs()) {

            if (pandingJob.equals(pandingJob1)) {

                pandingJob1.setSelected(pandingJob.isSelected());

            } else {

                pandingJob1.setSelected(false);
            }
        }

        mCurrentPendingJob = pandingJob;
        ArrayList<Integer> jobIds = new ArrayList<>();
        jobIds.add(pandingJob.getID());
        getJobDetails(jobIds);
        mProductPdfView.recycle();
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

                        printBookmarksTree(mProductPdfView.getTableOfContents(), "-");

                    }
                })
                .onError(new OnErrorListener() {
                    @Override
                    public void onError(Throwable t) {

                        mProductPdfNoImageTv.setText(getString(R.string.loading_error));
                        mProductPdfNoImageLy.setVisibility(View.VISIBLE);
                        mProductPdfView.setVisibility(View.INVISIBLE);
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

        mProductPdfNoImageTv.setText(getString(R.string.loading_error));
        mProductPdfNoImageLy.setVisibility(View.VISIBLE);
        mProductPdfView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onLoadFileProgress() {

    }

    @Override
    public void onActionChecked(Action action) {

        updateActionList(action);

    }

    public void updateActionList(Action action) {
        if (mUpdatedActions == null) {

            mUpdatedActions = new ArrayList<>();
        }

        if (mUpdatedActions.contains(action)) {

            mUpdatedActions.remove(action);
        }

        mUpdatedActions.add(action);
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

            Intent mGalleryIntent = new Intent(JobActionActivity.this, GalleryActivity.class);

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

        LayoutInflater inflater = this.getLayoutInflater();
        @SuppressLint("InflateParams") View dialogView = inflater.inflate(R.layout.dialog_action, null);
        builder.setView(dialogView);

        TextView textView = dialogView.findViewById(R.id.DA_text);
        textView.setText(action.getText());

        final EditText editText = dialogView.findViewById(R.id.DA_note);

        Button button = dialogView.findViewById(R.id.DA_btn);
        ImageButton closeButton = dialogView.findViewById(R.id.DA_close_btn);


        final AlertDialog alert = builder.create();
        alert.show();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                action.setNotes(editText.getText().toString());
                updateActionList(action);
                alert.dismiss();
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alert.dismiss();
            }
        });

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    action.setNotes(editText.getText().toString());
                    updateActionList(action);

                    alert.dismiss();

                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onShowCroutonRequest(String croutonMessage, int croutonDurationInMilliseconds,
                                     int viewGroup, CroutonCreator.CroutonType croutonType) {

        mCroutonCreator.showCrouton(this, String.valueOf(croutonMessage), croutonDurationInMilliseconds, R.id.AJA_root_relative_ly, croutonType, this);

    }

    @Override
    public void onShowCroutonRequest(SpannableStringBuilder croutonMessage,
                                     int croutonDurationInMilliseconds, int viewGroup, CroutonCreator.CroutonType croutonType) {

        if (mCroutonCreator != null) {
            mCroutonCreator.showCrouton(this, String.valueOf(croutonMessage), croutonDurationInMilliseconds, R.id.AJA_root_relative_ly, croutonType, this);

        }
    }

    @Override
    public void onHideConnectivityCroutonRequest() {

    }

    @Override
    public void onCroutonDismiss() {

    }
}
