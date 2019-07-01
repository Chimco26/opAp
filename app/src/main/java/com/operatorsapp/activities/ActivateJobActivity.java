package com.operatorsapp.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.view.View;

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
import com.operators.reportrejectnetworkbridge.server.response.activateJob.PendingJob;
import com.operators.reportrejectnetworkbridge.server.response.activateJob.PendingJobResponse;
import com.operators.reportrejectnetworkbridge.server.response.activateJob.Property;
import com.operators.reportrejectnetworkbridge.server.response.activateJob.Response;
import com.operatorsapp.R;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ActivateJobActivity extends AppCompatActivity implements
        PendingJobsAdapter.PendingJobsAdapterListener,
        OnCroutonRequestListener,
        CroutonCreator.CroutonListener,
        JobDetailsFragment.JobDetailsFragmentListener,
        JobListFragment.JobListFragmentListener {

    private static final String TAG = ActivateJobActivity.class.getSimpleName();
    public static final int EXTRA_ACTIVATE_JOB_CODE = 111;
    public static final String EXTRA_ACTIVATE_JOB_RESPONSE = "EXTRA_ACTIVATE_JOB_RESPONSE";
    public static final String EXTRA_ACTIVATE_JOB_ID = "EXTRA_ACTIVATE_JOB_ID";
    public static final String EXTRA_LAST_JOB_ID = "EXTRA_LAST_JOB_ID";
    public static final String EXTRA_IS_NO_PRODUCTION = "EXTRA_IS_NO_PRODUCTION";
    public static final String EXTRA_LAST_ERP_JOB_ID = "EXTRA_LAST_ERP_JOB_ID";
    public static final String EXTRA_LAST_PRODUCT_NAME = "EXTRA_LAST_PRODUCT_NAME";


    private PendingJobResponse mPendingJobsResponse;
    private HashMap<String, Header> mHashMapHeaders;
    private ArrayList<Header> mHeaders = new ArrayList<>();
    private ArrayList<PendingJob> mPendingJobs = new ArrayList<>();
    private ArrayList<PendingJob> mPendingJobsNoHeadersFiltered = new ArrayList<>();
    private JobDetailsResponse mCurrentJobDetails;
    private PendingJob mCurrentPendingJob;

    private ArrayList<PdfObject> mPdfList = new ArrayList<>();
    private CroutonCreator mCroutonCreator;
    private ArrayList<Action> mUpdatedActions;
    private boolean mIsNoProduction;
    private String mLastJobId = "";
    private String mLastJobErpId = "";
    private String mLastProductName = "";
    private boolean isActive = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activate_job_activity);

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

        getPendingJobList();

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
                            postUpdateActions(mUpdatedActions, mLastJobId);
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
                            postUpdateActions(mUpdatedActions, mLastJobId);
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

    private void getPendingJobList() {

        SimpleRequests simpleRequests = new SimpleRequests();

        final PersistenceManager persistanceManager = PersistenceManager.getInstance();

        ProgressDialogManager.show(this);

        simpleRequests.getPendingJobList(persistanceManager.getSiteUrl(), new GetPendingJobListCallback() {

            @Override
            public void onGetPendingJobListSuccess(Object response) {
                if (!isActive){
                    return;
                }
                mPendingJobsResponse = ((PendingJobResponse) response);

                if (mPendingJobsResponse != null && mPendingJobsResponse.getPendingJobs() != null && mPendingJobsResponse.getPendingJobs().size() > 0) {

                    SimpleDateFormat actualFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());

                    for (PendingJob pendingJob : mPendingJobsResponse.getPendingJobs()) {
                        for (Property property : pendingJob.getProperties()) {

                            property.setValue(updateDateForRtl(property, actualFormat, dateFormat));

                        }
                    }

                    mHeaders.addAll(mPendingJobsResponse.getHeaders());

                    mPendingJobs.addAll(mPendingJobsResponse.getPendingJobs());

                    mPendingJobsNoHeadersFiltered.addAll(mPendingJobs);

//                    initLeftView();

                    mCurrentPendingJob = mPendingJobsResponse.getPendingJobs().get(0);

                    ArrayList<Integer> jobIds = new ArrayList<>();
                    jobIds.add(mPendingJobsResponse.getPendingJobs().get(0).getID());
//                    getJobDetails(jobIds);
                    showJobListFragment(mPendingJobsResponse, mPendingJobs, mHeaders);
                    //todo new open list
                    ProgressDialogManager.dismiss();

                } else {
                    ProgressDialogManager.dismiss();
                    final GenericDialog dialog = new GenericDialog(ActivateJobActivity.this, getString(R.string.empty_job_list_msg), getString(R.string.attention), getString(R.string.ok), false);
                    dialog.setListener(new GenericDialog.OnGenericDialogListener() {
                        @Override
                        public void onActionYes() {
                            finish();
                        }

                        @Override
                        public void onActionNo() {

                        }

                        @Override
                        public void onActionAnother() {

                        }
                    });
                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            finish();
                        }
                    });
                    dialog.show();
                }

            }

            @Override
            public void onGetPendingJobListFailed(ErrorObjectInterface reason) {
                if (!isActive){
                    return;
                }
                ProgressDialogManager.dismiss();
                ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Retrofit, getString(R.string.get_panding_jobs_failed_error));
                ShowCrouton.jobsLoadingErrorCrouton(ActivateJobActivity.this, errorObject);
//                finish();
                findViewById(R.id.AJA_no_data_tv).setVisibility(View.VISIBLE);
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
                if (!isActive){
                    return;
                }
                ProgressDialogManager.dismiss();

                if (response == null) {

                    ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Retrofit, "PostActivateJob Failed");
                    ShowCrouton.jobsLoadingErrorCrouton(ActivateJobActivity.this, errorObject);

                } else if (((Response) response).getError() != null) {

                    ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Retrofit, ((Response) response).getError().getErrorDesc());
                    ShowCrouton.jobsLoadingErrorCrouton(ActivateJobActivity.this, errorObject);


                } else {

                    mCurrentJobDetails = (JobDetailsResponse) response;
                    //todo new open details
                    showJobDetailsFragment(mCurrentJobDetails);
                }
            }

            @Override
            public void onGetJobDetailsFailed(ErrorObjectInterface reason) {
                if (!isActive){
                    return;
                }
                ProgressDialogManager.dismiss();

                ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Retrofit, getString(R.string.get_jobs_details_failed_error));
                ShowCrouton.jobsLoadingErrorCrouton(ActivateJobActivity.this, errorObject);
            }
        }, NetworkManager.getInstance(), new JobDetailsRequest(persistanceManager.getSessionId(), jobIds), persistanceManager.getTotalRetries(), persistanceManager.getRequestTimeout());


    }

    private void postUpdateActions(ArrayList<Action> actions, String currentJobId) {

        if (currentJobId == null) {

            return;
        }

        final PersistenceManager persistanceManager = PersistenceManager.getInstance();

        ActionsUpdateRequest actionsUpdateRequest = new ActionsUpdateRequest(persistanceManager.getSessionId(), null);

        String operatorId = persistanceManager.getOperatorId() != null? persistanceManager.getOperatorId() : "0";
        actionsUpdateRequest.setActions(new ActionsByJob(currentJobId, operatorId, null));

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

    private void postActivateJob(final ActivateJobRequest activateJobRequest) {

        final PersistenceManager persistanceManager = PersistenceManager.getInstance();

        SimpleRequests simpleRequests = new SimpleRequests();

        ProgressDialogManager.show(this);

        simpleRequests.postActivateJob(persistanceManager.getSiteUrl(), new PostActivateJobCallback() {

            @Override
            public void onPostActivateJobSuccess(Object response) {
                if (!isActive){
                    return;
                }
                ProgressDialogManager.dismiss();

                if (response == null) {

                    ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Retrofit, "PostActivateJob Failed");
                    ShowCrouton.jobsLoadingErrorCrouton(ActivateJobActivity.this, errorObject);

                } else if (((Response) response).getError() != null) {

                    ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Retrofit, ((Response) response).getError().getErrorDesc());
                    ShowCrouton.showSimpleCrouton(ActivateJobActivity.this, errorObject);

                } else {

                    finishActivity((Response) response, activateJobRequest.getJobID());
                }
            }

            @Override
            public void onPostActivateJobFailed(ErrorObjectInterface reason) {
                if (!isActive){
                    return;
                }
                ProgressDialogManager.dismiss();

                ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Retrofit, reason.getDetailedDescription());
                ShowCrouton.jobsLoadingErrorCrouton(ActivateJobActivity.this, errorObject);
            }
        }, NetworkManager.getInstance(), activateJobRequest, persistanceManager.getTotalRetries(), persistanceManager.getRequestTimeout());

    }

    public void finishActivity(Response response, String jobID) {
        isActive = false;
        Intent intent = getIntent();
        intent.putExtra(EXTRA_ACTIVATE_JOB_RESPONSE, response);
        intent.putExtra(EXTRA_ACTIVATE_JOB_ID, jobID);

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


    private void showRecipeFragment() {

        RecipeFragment mRecipefragment = RecipeFragment.newInstance(mCurrentJobDetails.getJobs().get(0).getRecipe());

        getSupportFragmentManager().beginTransaction().add(R.id.AJA_container, mRecipefragment).addToBackStack(RecipeFragment.TAG).commit();
    }

    private void showJobListFragment(PendingJobResponse mPendingJobsResponse, ArrayList<PendingJob> mPendingJobs, ArrayList<Header> headers) {

        JobListFragment jobListFragment = JobListFragment.newInstance(mPendingJobsResponse, mPendingJobs, headers);

        getSupportFragmentManager().beginTransaction().add(R.id.AJA_container, jobListFragment).addToBackStack(JobListFragment.class.getSimpleName()).commit();
    }

    private void showJobDetailsFragment(JobDetailsResponse jobDetailsResponse) {

        JobDetailsFragment jobDetailsFragment = JobDetailsFragment.newInstance(jobDetailsResponse, (ArrayList<Header>) mPendingJobsResponse.getHeaders(), mCurrentPendingJob);

        getSupportFragmentManager().beginTransaction().add(R.id.AJA_container, jobDetailsFragment).addToBackStack(JobDetailsFragment.class.getSimpleName()).commit();
    }


    @Override
    public void onPendingJobSelected(PendingJob pendingJob) {

        for (PendingJob pendingJob1 : mPendingJobsResponse.getPendingJobs()) {

            if (pendingJob.equals(pendingJob1)) {

                pendingJob1.setSelected(pendingJob.isSelected());

            } else {

                pendingJob1.setSelected(false);
            }
        }

        mCurrentPendingJob = pendingJob;
        ArrayList<Integer> jobIds = new ArrayList<>();
        jobIds.add(pendingJob.getID());
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

        if (count <= 1) {
            finish();
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
        postUpdateActions(updatedActions, String.valueOf(mCurrentJobDetails.getJobs().get(0).getID()));
    }
}
