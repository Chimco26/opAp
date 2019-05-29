package com.operatorsapp.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.example.common.callback.ErrorObjectInterface;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
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
import com.operatorsapp.adapters.JobHeadersAdapter;
import com.operatorsapp.adapters.PendingJobsAdapter;
import com.operatorsapp.application.OperatorApplication;
import com.operatorsapp.dialogs.BasicTitleTextBtnDialog;
import com.operatorsapp.dialogs.GenericDialog;
import com.operatorsapp.fragments.JobDetailsFragment;
import com.operatorsapp.fragments.JobListFragment;
import com.operatorsapp.fragments.RecipeFragment;
import com.operatorsapp.fragments.interfaces.OnCroutonRequestListener;
import com.operatorsapp.managers.CroutonCreator;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.managers.ProgressDialogManager;
import com.operatorsapp.model.PdfObject;
import com.operatorsapp.server.NetworkManager;
import com.operatorsapp.utils.ShowCrouton;
import com.operatorsapp.utils.SimpleRequests;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ActivateJobActivity extends AppCompatActivity implements View.OnClickListener,
        JobHeadersAdapter.JobHeadersAdaperListener,
        PendingJobsAdapter.PendingJobsAdapterListener,
        OnCroutonRequestListener,
        CroutonCreator.CroutonListener,
        JobDetailsFragment.JobDetailsFragmentListener {

    private static final String TAG = ActivateJobActivity.class.getSimpleName();
    private static final String JOB_ACTION_FRAGMENT = "JOB_ACTION_FRAGMENT";
    public static final String EXTRA_ACTIVATE_JOB_RESPONSE = "EXTRA_ACTIVATE_JOB_RESPONSE";
    public static final String EXTRA_ACTIVATE_JOB_ID = "EXTRA_ACTIVATE_JOB_ID";
    public static final String EXTRA_LAST_JOB_ID = "EXTRA_LAST_JOB_ID";
    public static final String EXTRA_IS_NO_PRODUCTION = "EXTRA_IS_NO_PRODUCTION";
    public static final String EXTRA_LAST_ERP_JOB_ID = "EXTRA_LAST_ERP_JOB_ID";
    public static final String EXTRA_LAST_PRODUCT_NAME = "EXTRA_LAST_PRODUCT_NAME";

    private EditText mSearchViewEt;
    private RecyclerView mHeadersRv;
    private RecyclerView mPendingJobsRv;

    private PendingJobResponse mPendingJobsResponse;
    private JobHeadersAdapter mHeadersAdapter;
    private PendingJobsAdapter mPendingJobsAdapter;
    private HashMap<String, Header> mHashMapHeaders;
    private ArrayList<Header> mHeaders = new ArrayList<>();
    private ArrayList<PandingJob> mPendingJobs = new ArrayList<>();
    private ArrayList<PandingJob> mPendingJobsNoHeadersFiltered = new ArrayList<>();
    private JobDetailsResponse mCurrentJobDetails;
    private PandingJob mCurrentPendingJob;

    private ArrayList<PdfObject> mPdfList = new ArrayList<>();
    private CroutonCreator mCroutonCreator;
    private ArrayList<Action> mUpdatedActions;
    private boolean mIsNoProduction;
    private String mLastJobId = "";
    private String mLastJobErpId = "";
    private String mLastProductName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_action);

        // Analytics
        OperatorApplication application = (OperatorApplication) getApplication();
        Tracker mTracker = application.getDefaultTracker();
        PersistenceManager pm = PersistenceManager.getInstance();
        mTracker.setClientId("machine name + id: " + pm.getMachineName() + ", " + pm.getMachineId());
        mTracker.setAppVersion(pm.getVersion() + "");
        mTracker.setHostname(pm.getSiteName());
        mTracker.setScreenName("Activate Job Screen");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        mCroutonCreator = new CroutonCreator();

        getExtras();
        initVars();
        initListener();

        getPendingJoblist();

        initLastJobDialog();
    }

    private void initLastJobDialog() {
        if (mIsNoProduction && mLastJobId != null && mLastJobId.length() > 0 && !mLastJobId.equals("0")) {
            showLastJobDialog(getString(R.string.activate_last_job),
                    getString(R.string.do_you_want_to_activate_the_last_job_you_work_on_job),
                    String.format(Locale.getDefault(),
                            "%s: %s | %s: %s | %s: %s", getString(R.string.job),
                            mLastJobId, getString(R.string.erp_job_id), mLastJobErpId,
                            getString(R.string.product), mLastProductName),
                    getString(R.string.activate_job), getString(R.string.dont_activate), true);
        }
    }

    private void getExtras() {
        if (getIntent().getExtras() != null) {
            if (getIntent().getExtras().containsKey(EXTRA_LAST_JOB_ID)) {
                mLastJobId = getNotNullText(getIntent().getExtras().getString(EXTRA_LAST_JOB_ID));
            }
            if (getIntent().getExtras().containsKey(EXTRA_LAST_ERP_JOB_ID)) {
                mLastJobErpId = getNotNullText(getIntent().getExtras().getString(EXTRA_LAST_ERP_JOB_ID));
            }
            if (getIntent().getExtras().containsKey(EXTRA_LAST_PRODUCT_NAME)) {
                mLastProductName = getNotNullText(getIntent().getExtras().getString(EXTRA_LAST_PRODUCT_NAME));
            }

            if (getIntent().getExtras().containsKey(EXTRA_IS_NO_PRODUCTION)) {
                mIsNoProduction = getIntent().getExtras().getBoolean(EXTRA_IS_NO_PRODUCTION);
            }
        }
    }

    private String getNotNullText(String string) {
        if (string == null) {
            return "";
        }
        return string;
    }

    private void showLastJobDialog(String title, String subTitle, String msg, String positiveBtn, String negativeBtn, final boolean firstSteps) {
        BasicTitleTextBtnDialog basicTitleTextBtnDialog = new BasicTitleTextBtnDialog(this,
                new BasicTitleTextBtnDialog.BasicTitleTextBtnDialogListener() {
                    @Override
                    public void onClickPositiveBtn() {
                        if (firstSteps) {
                            showLastJobDialog(getString(R.string.activate_last_job),
                                    getString(R.string.required_setup),
                                    null,
                                    getString(R.string.go_to_setup), getString(R.string.no_setup), false);
                        } else {
                            postUpdateActions(mUpdatedActions);
                            if (mLastJobId != null) {
                                PersistenceManager persistenceManager = PersistenceManager.getInstance();
                                postActivateJob(new ActivateJobRequest(persistenceManager.getSessionId(),
                                        String.valueOf(persistenceManager.getMachineId()),
                                        String.valueOf(mLastJobId),
                                        persistenceManager.getOperatorId(),
                                        false));
                            }
                        }
                    }

                    @Override
                    public void onClickNegativeBtn() {
                        if (!firstSteps) {
                            postUpdateActions(mUpdatedActions);
                            if (mLastJobId != null) {
                                PersistenceManager persistenceManager = PersistenceManager.getInstance();
                                postActivateJob(new ActivateJobRequest(persistenceManager.getSessionId(),
                                        String.valueOf(persistenceManager.getMachineId()),
                                        String.valueOf(mLastJobId),
                                        persistenceManager.getOperatorId(),
                                        true));
                            }
                        }
                    }
                }, title, subTitle, msg, positiveBtn, negativeBtn
        );

        basicTitleTextBtnDialog.showBasicTitleTextBtnDialog().show();
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

//                    initLeftView();

                    mCurrentPendingJob = mPendingJobsResponse.getPandingJobs().get(0);

                    ArrayList<Integer> jobIds = new ArrayList<>();
                    jobIds.add(mPendingJobsResponse.getPandingJobs().get(0).getID());
//                    getJobDetails(jobIds);

                    //todo new open list
                    ProgressDialogManager.dismiss();

                } else {
                    ProgressDialogManager.dismiss();
                    final GenericDialog dialog = new GenericDialog(ActivateJobActivity.this, getString(R.string.empty_job_list_msg), getString(R.string.attention), getString(R.string.ok), false);
                    dialog.setListener(new GenericDialog.OnGenericDialogListener() {
                        @Override
                        public void onActionYes() {
                            dialog.dismiss();
                        }

                        @Override
                        public void onActionNo() {

                        }

                        @Override
                        public void onActionAnother() {

                        }
                    });
                    dialog.show();
                }

            }

            @Override
            public void onGetPendingJobListFailed(ErrorObjectInterface reason) {

                ProgressDialogManager.dismiss();
                ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Retrofit, getString(R.string.get_panding_jobs_failed_error));
                ShowCrouton.jobsLoadingErrorCrouton(ActivateJobActivity.this, errorObject);

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
                    ShowCrouton.jobsLoadingErrorCrouton(ActivateJobActivity.this, errorObject);

                } else if (((Response) response).getError() != null) {

                    ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Retrofit, ((Response) response).getError().getErrorDesc());
                    ShowCrouton.showSimpleCrouton(ActivateJobActivity.this, errorObject);


                } else {

                    mCurrentJobDetails = (JobDetailsResponse) response;
                    //todo new open details
                }
            }

            @Override
            public void onGetJobDetailsFailed(ErrorObjectInterface reason) {

                ProgressDialogManager.dismiss();

                ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Retrofit, getString(R.string.get_jobs_details_failed_error));
                ShowCrouton.jobsLoadingErrorCrouton(ActivateJobActivity.this, errorObject);
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

        simpleRequests.postUpdateActions(persistanceManager.getSiteUrl(), new PostUpdtaeActionsCallback() {

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
                    ShowCrouton.jobsLoadingErrorCrouton(ActivateJobActivity.this, errorObject);

                } else if (((Response) response).getError() != null) {

                    ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Retrofit, ((Response) response).getError().getErrorDesc());
                    ShowCrouton.showSimpleCrouton(ActivateJobActivity.this, errorObject);


                } else {

                    finishActivity((Response) response);
                }
            }

            @Override
            public void onPostActivateJobFailed(ErrorObjectInterface reason) {

                ProgressDialogManager.dismiss();

                ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Retrofit, reason.getDetailedDescription());
                ShowCrouton.jobsLoadingErrorCrouton(ActivateJobActivity.this, errorObject);
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


    private void headerListToHashMap(List<Header> headers) {

        mHashMapHeaders = new HashMap<>();

        for (Header header : headers) {

            mHashMapHeaders.put(header.getName(), header);
        }

    }

    private void initVars() {


        initVarsSearch();


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

        findViewById(R.id.AJA_search_btn).setOnClickListener(this);


        mSearchViewEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() < 1 && mHashMapHeaders != null) {
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

        if (mPendingJobsResponse != null) {
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
        }

        mPendingJobsNoHeadersFiltered.clear();
        mPendingJobsNoHeadersFiltered.addAll(mPendingJobs);
        sortHeaders();
        if (mHeadersAdapter != null) {
            mHeadersAdapter.notifyDataSetChanged();
        }
        if (mPendingJobsAdapter != null) {
            mPendingJobsAdapter.notifyDataSetChanged();
            filterPendingJobsByHeaders();
        }
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

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.AJA_search_btn:

                break;

        }
    }

    private void showRecipeFragment() {

        RecipeFragment mRecipefragment = RecipeFragment.newInstance(mCurrentJobDetails.getJobs().get(0).getRecipe());

        getSupportFragmentManager().beginTransaction().add(R.id.AJA_container, mRecipefragment).addToBackStack(RecipeFragment.TAG).commit();
    }

    private void showJobListFragment() {

        JobListFragment jobListFragment = JobListFragment.newInstance();

        getSupportFragmentManager().beginTransaction().add(R.id.AJA_container, jobListFragment).addToBackStack(JobListFragment.class.getSimpleName()).commit();
    }

    private void showJobDetailsFragment(JobDetailsResponse jobDetailsResponse) {

        JobDetailsFragment jobDetailsFragment = JobDetailsFragment.newInstance(jobDetailsResponse, mHashMapHeaders);

        getSupportFragmentManager().beginTransaction().add(R.id.AJA_container, jobDetailsFragment).addToBackStack(JobDetailsFragment.class.getSimpleName()).commit();
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
    }


    private void startGalleryActivity(List<String> fileUrl, String name) {

        if (fileUrl != null && fileUrl.size() > 0) {

            Intent mGalleryIntent = new Intent(ActivateJobActivity.this, GalleryActivity.class);

            mGalleryIntent.putExtra(GalleryActivity.EXTRA_FILE_URL, (ArrayList<String>) fileUrl);

            mGalleryIntent.putExtra(GalleryActivity.EXTRA_RECIPE_FILES_TITLE, name);

            mGalleryIntent.putExtra(GalleryActivity.EXTRA_RECIPE_PDF_FILES, mPdfList);

            startActivityForResult(mGalleryIntent, GalleryActivity.EXTRA_GALLERY_CODE);

        }
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            getSupportFragmentManager().popBackStack();
        }
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

    @Override
    public void onShowRecipeFragment() {
        showRecipeFragment();
    }

    @Override
    public void onStartGalleryActivity(List<String> productFiles, String name) {
        startGalleryActivity(productFiles, name);
    }

    @Override
    public void onPostActivateJob(ActivateJobRequest activateJobRequest) {
        postActivateJob(activateJobRequest);
    }

    @Override
    public void onPostUpdateActions(ArrayList<Action> updatedActions) {
        postUpdateActions(updatedActions);
    }
}
