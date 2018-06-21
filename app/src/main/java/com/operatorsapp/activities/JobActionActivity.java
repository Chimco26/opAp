package com.operatorsapp.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
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
import com.operators.reportrejectnetworkbridge.server.response.activateJob.Action;
import com.operators.reportrejectnetworkbridge.server.response.activateJob.ActivateJobRequest;
import com.operators.reportrejectnetworkbridge.server.response.activateJob.Header;
import com.operators.reportrejectnetworkbridge.server.response.activateJob.JobDetailsRequest;
import com.operators.reportrejectnetworkbridge.server.response.activateJob.JobDetailsResponse;
import com.operators.reportrejectnetworkbridge.server.response.activateJob.PandingJob;
import com.operators.reportrejectnetworkbridge.server.response.activateJob.PendingJobResponse;
import com.operators.reportrejectnetworkbridge.server.response.activateJob.Property;
import com.operatorsapp.R;
import com.operatorsapp.adapters.JobActionsAdapter;
import com.operatorsapp.adapters.JobHeadersAdaper;
import com.operatorsapp.adapters.JobMaterialsSplitAdapter;
import com.operatorsapp.adapters.PendingJobsAdapter;
import com.operatorsapp.fragments.RecipeFragment;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.model.PdfObject;
import com.operatorsapp.server.NetworkManager;
import com.operatorsapp.utils.DownloadHelper;
import com.operatorsapp.utils.SimpleRequests;
import com.operatorsapp.utils.SoftKeyboardUtil;
import com.shockwave.pdfium.PdfDocument;

import java.io.File;
import java.util.ArrayList;
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
        RecipeFragment.OnRecipeFragmentListener {

    private static final String TAG = JobActionActivity.class.getSimpleName();
    private static final String JOB_ACTION_FRAGMENT = "JOB_ACTION_FRAGMENT";
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_action);

        initVars();

        initListener();

        mDownloadHelper = new DownloadHelper(this, this);

        getPendingJoblist();

        SoftKeyboardUtil.hideKeyboard(this);

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

                    initLeftView();

                    mCurrentPendingJob = mPendingJobsResponse.getPandingJobs().get(0);

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

                mCurrentJobDetails = (JobDetailsResponse) response;
                initRightView();
            }

            @Override
            public void onGetJobDetailsFailed(ErrorObjectInterface reason) {

            }
        }, NetworkManager.getInstance(), new JobDetailsRequest(persistanceManager.getSessionId(), jobIds), persistanceManager.getTotalRetries(), persistanceManager.getRequestTimeout());


    }

    public void initRightView() {

        mTitleTv.setText(String.valueOf(mCurrentJobDetails.getJobs().get(0).getID()));

        initImagesViews();

        initViewsTitleLine();

        initViewsMaterialItem();

        initViewsMoldItem();

        initActionsItemView();
    }

    private void initActionsItemView() {

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

        if (mCurrentPendingJob.getProperties() != null) {
            if (mCurrentPendingJob.getProperties().size() > 0) {
                mTitlLine1Tv1.setText(mCurrentPendingJob.getProperties().get(0).getKey());
                mTitlLine1Tv2.setText(mCurrentPendingJob.getProperties().get(0).getValue());
            }
            if (mCurrentPendingJob.getProperties().size() > 1) {
                mTitlLine1Tv3.setText(mCurrentPendingJob.getProperties().get(1).getKey());
                mTitlLine1Tv4.setText(mCurrentPendingJob.getProperties().get(1).getValue());
            }
            if (mCurrentPendingJob.getProperties().size() > 2) {
                mTitlLine1Tv5.setText(mCurrentPendingJob.getProperties().get(2).getKey());
                mTitlLine1Tv6.setText(mCurrentPendingJob.getProperties().get(2).getValue());
            }
            if (mCurrentPendingJob.getProperties().size() > 3) {
                mTit2Line1Tv1.setText(mCurrentPendingJob.getProperties().get(3).getKey());
                mTit2Line1Tv2.setText(mCurrentPendingJob.getProperties().get(3).getValue());
            }
            if (mCurrentPendingJob.getProperties().size() > 4) {
                mTit2Line1Tv3.setText(mCurrentPendingJob.getProperties().get(4).getKey());
                mTit2Line1Tv4.setText(mCurrentPendingJob.getProperties().get(4).getValue());
            }
            if (mCurrentPendingJob.getProperties().size() > 5) {
                mTit2Line1Tv5.setText(mCurrentPendingJob.getProperties().get(5).getKey());
                mTit2Line1Tv6.setText(mCurrentPendingJob.getProperties().get(5).getValue());
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
                .enableSwipe(true)
                .swipeHorizontal(false)
                .enableAnnotationRendering(true)
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
}
