package com.operatorsapp.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.common.StandardResponse;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.operators.reportrejectnetworkbridge.server.request.PostUpdateNotesForJobRequest;
import com.operators.reportrejectnetworkbridge.server.response.activateJob.Action;
import com.operators.reportrejectnetworkbridge.server.response.activateJob.ActivateJobRequest;
import com.operators.reportrejectnetworkbridge.server.response.activateJob.Header;
import com.operators.reportrejectnetworkbridge.server.response.activateJob.Job;
import com.operators.reportrejectnetworkbridge.server.response.activateJob.JobDetailsStandardResponse;
import com.operators.reportrejectnetworkbridge.server.response.activateJob.PendingJob;
import com.operators.reportrejectnetworkbridge.server.response.activateJob.Property;
import com.operatorsapp.R;
import com.operatorsapp.adapters.JobActionsAdapter;
import com.operatorsapp.adapters.JobMaterialsSplitAdapter;
import com.operatorsapp.adapters.PendingJobPropsAdapter;
import com.operatorsapp.dialogs.BasicTitleTextSingleBtnDialog;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.managers.ProgressDialogManager;
import com.operatorsapp.server.NetworkManager;
import com.operatorsapp.server.callback.PostUpdateNotesForJobCallback;
import com.operatorsapp.utils.DownloadHelper;
import com.operatorsapp.utils.GoogleAnalyticsHelper;
import com.operatorsapp.utils.SimpleRequests;
import com.operatorsapp.view.GridSpacingItemDecoration;
import com.operatorsapp.view.GridSpacingItemDecorationRTL;
import com.shockwave.pdfium.PdfDocument;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class JobDetailsFragment extends Fragment implements JobActionsAdapter.JobActionsAdapterListener,
        View.OnClickListener,
        DownloadHelper.DownloadFileListener,
        RecipeFragment.OnRecipeFragmentListener {

    private static final String TAG = JobDetailsFragment.class.getSimpleName();

    private static final int NUMBER_OF_COLUMNS = 3;
    private static final int PROPS_RV_HEIGHT = 87;

    private TextView mMaterialItemTitleTv;
    private RecyclerView mMaterialItemRv;
    private TextView mMoldItemTitleTv;
    private ImageView mMoldItemImg;
    private RecyclerView mActionRv;
    private View mActionItemLy;
    private View mMaterialItem;
    private View mMoldItem;
    private TextView mMoldNameTv;
    private TextView mMoldMoldCatalogTv;
    private TextView mMoldCavitiesActualTv;
    private TextView mMoldCavitiesStandardTv;
    private TextView mActionsTitleTv;
    private TextView mTitleTv;
    private PDFView mProductPdfView;
    private ImageView mProductImage;
    private View mProductPdfNoImageLy;
    private View mProductImageNoImageLy;
    private TextView mProductNoteTv;
    private TextView mProductPdfNoImageTv;
    private RecyclerView mPropsRv;
    private View mPropsRvOpenButton;

    private JobDetailsStandardResponse mCurrentJobDetails;
    private PendingJob mCurrentPendingJob;
    private String mFirstPdf;
    private DownloadHelper mDownloadHelper;
    private HashMap<String, Header> mHashMapHeaders;
    private JobDetailsFragmentListener mListener;
    private ArrayList<Action> mUpdatedActions;
    private ArrayList<Header> mHeaders;

    public static JobDetailsFragment newInstance(JobDetailsStandardResponse jobDetailsResponse, ArrayList<Header> headers, PendingJob pendingJob) {

        JobDetailsFragment jobDetailsFragment = new JobDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(JobDetailsStandardResponse.TAG, jobDetailsResponse);
        bundle.putParcelable(PendingJob.TAG, pendingJob);
        bundle.putParcelableArrayList(Header.TAG, headers);
        jobDetailsFragment.setArguments(bundle);
        return jobDetailsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Analytics
        new GoogleAnalyticsHelper().trackScreen(getActivity(), "Pending job details");

        if (getArguments() != null) {
            if (getArguments().containsKey(JobDetailsStandardResponse.TAG)) {
                mCurrentJobDetails = getArguments().getParcelable(JobDetailsStandardResponse.TAG);

            }
            if (getArguments().containsKey(PendingJob.TAG)) {
                mCurrentPendingJob = getArguments().getParcelable(PendingJob.TAG);

            }
            if (getArguments().containsKey(Header.TAG)) {
                mHeaders = getArguments().getParcelableArrayList(Header.TAG);
                sortHeaders();
                mHashMapHeaders = headerListToHashMap(mHeaders);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_actionnable, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initVars(view);

        initListener(view);

        initView(view);
    }

    private void initListener(View view) {
        view.findViewById(R.id.AJA_back_btn).setOnClickListener(this);
        view.findViewById(R.id.AJA_title).setOnClickListener(this);
        view.findViewById(R.id.AJA_job_activate_btn).setOnClickListener(this);
        view.findViewById(R.id.AJA_item_material).setOnClickListener(this);
        mMoldItemImg.setOnClickListener(this);
        view.findViewById(R.id.AJA_img1).setOnClickListener(this);
        view.findViewById(R.id.AJA_img2).setOnClickListener(this);
        view.findViewById(R.id.AJA_open_edit_text_btn).setOnClickListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof JobDetailsFragmentListener) {
            mListener = (JobDetailsFragmentListener) context;
        }
    }

    private void initVars(View view) {

        mDownloadHelper = new DownloadHelper(getActivity(), this);

        mTitleTv = view.findViewById(R.id.AJA_job_id_tv);

        mProductPdfView = view.findViewById(R.id.AJA_img1);

        mProductImage = view.findViewById(R.id.AJA_img2);

        mProductPdfNoImageLy = view.findViewById(R.id.AJA_img1_no_image);

        mProductPdfNoImageTv = view.findViewById(R.id.AJA_img1_no_image_tv);

        mProductImageNoImageLy = view.findViewById(R.id.AJA_img2_no_image);

        mProductNoteTv = view.findViewById(R.id.AJA_notes_tv);

        mPropsRv = view.findViewById(R.id.AJA_top_prop_rv);

        mPropsRvOpenButton = view.findViewById(R.id.AJA_props_top_rv_button);

        initVarsMaterialItem(view);

        initVarsMoldItem(view);

        initActionsItem(view);

    }

    private void initActionsItem(View view) {

        mActionItemLy = view.findViewById(R.id.AJA_actions_ly);

        mActionRv = view.findViewById(R.id.AJA_actions_rv);

        mActionsTitleTv = view.findViewById(R.id.AJA_actions_tv);
    }

    private void initVarsMoldItem(View view) {

        mMoldItem = view.findViewById(R.id.AJA_item_mold);

        mMoldItemTitleTv = mMoldItem.findViewById(R.id.IJAM_title);

        mMoldItemImg = mMoldItem.findViewById(R.id.IJAM_img);

        mMoldNameTv = mMoldItem.findViewById(R.id.IJAM_mold1_tv2);

        mMoldMoldCatalogTv = mMoldItem.findViewById(R.id.IJAM_mold2_tv2);

        mMoldCavitiesActualTv = mMoldItem.findViewById(R.id.IJAM_mold3_tv2);

        mMoldCavitiesStandardTv = mMoldItem.findViewById(R.id.IJAM_mold4_tv2);
    }

    private void initVarsMaterialItem(View view) {

        mMaterialItem = view.findViewById(R.id.AJA_item_material);

        mMaterialItemTitleTv = mMaterialItem.findViewById(R.id.JAMI_title);

        mMaterialItemRv = mMaterialItem.findViewById(R.id.JAMI_rv);
    }

    public void initView(View view) {

        initTitleView(view);

        initImagesViews();

        initTopPropsRv();

        initViewsMaterialItem();

        initViewsMoldItem();

        initActionsItemView();

        initNotesView();
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

    private HashMap<String, Header> headerListToHashMap(List<Header> headers) {

        HashMap<String, Header> hashMapHeaders = new HashMap<>();

        for (Header header : headers) {

            hashMapHeaders.put(header.getName(), header);
        }

        return hashMapHeaders;
    }

    private void initNotesView() {
        mProductNoteTv.setText("");
        for (Job job : mCurrentJobDetails.getJobs()) {
            if (mCurrentPendingJob.getID().equals(job.getID())) {

                mProductNoteTv.setText(job.getNotes());
            }
        }
    }

    public void initTitleView(View view) {

        mTitleTv.setText(String.valueOf(mCurrentJobDetails.getJobs().get(0).getID()));

        for (Property property : mCurrentPendingJob.getProperties()) {

            if (property.getKey().equals("ERPJobID") && property.getValue() != null && property.getValue().length() > 0) {

                mTitleTv.setText(String.format("%s (%s)", property.getValue(), String.valueOf(mCurrentJobDetails.getJobs().get(0).getID())));

            }
        }

        if (getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
            view.findViewById(R.id.AJA_back_btn).setRotationY(180);
        }
    }


    private void initActionsItemView() {

        mActionRv.setNestedScrollingEnabled(false);

        mCurrentJobDetails.getJobs().get(0).setActions(sortActions(mCurrentJobDetails.getJobs().get(0).getActions()));

        if (mCurrentJobDetails.getJobs().get(0).getActions() != null &&
                mCurrentJobDetails.getJobs().get(0).getActions().size() > 0) {

            mActionItemLy.setVisibility(View.VISIBLE);

            mActionsTitleTv.setText(getString(R.string.actions));

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

            mActionRv.setLayoutManager(layoutManager);

            JobActionsAdapter mActionsAdapter = new JobActionsAdapter(mCurrentJobDetails.getJobs().get(0).getActions(), this);

            mActionRv.setAdapter(mActionsAdapter);

        }
    }

    private void validateDialog(final Integer jobId, final boolean isToSetupEnd) {

        BasicTitleTextSingleBtnDialog basicTitleTextBtnDialog = new BasicTitleTextSingleBtnDialog(getActivity(),
                new BasicTitleTextSingleBtnDialog.BasicTitleTextBtnDialogListener() {
                    @Override
                    public void onClickPositiveBtn() {
                        mListener.onPostUpdateActions(mUpdatedActions);
                        if (jobId != null) {
                            PersistenceManager persistenceManager = PersistenceManager.getInstance();
                            mListener.onPostActivateJob(new ActivateJobRequest(persistenceManager.getSessionId(),
                                    String.valueOf(persistenceManager.getMachineId()),
                                    String.valueOf(jobId),
                                    persistenceManager.getOperatorId(),
                                    isToSetupEnd));
                        }
                    }

                    @Override
                    public void onClickNegativeBtn() {
                    }
                }, getString(R.string.activate_job_dialog_title), null
                , getString(R.string.activate_job_dialog_message), getString(R.string.activate)
        );

        basicTitleTextBtnDialog.showBasicTitleTextBtnDialog().show();

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

    public boolean isStoragePermissionGranted() {

        if (Build.VERSION.SDK_INT >= 23) {

            if (getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                return true;

            } else {

                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

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

    private void initViewsMaterialItem() {

        if (mCurrentJobDetails.getJobs().get(0).getMaterials() != null) {

            mMaterialItemTitleTv.setText(R.string.materials);

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

            mMaterialItemRv.setLayoutManager(layoutManager);

            JobMaterialsSplitAdapter mMaterialAdapter = new JobMaterialsSplitAdapter(mCurrentJobDetails.getJobs().get(0).getMaterials(), getActivity());

            mMaterialItemRv.setAdapter(mMaterialAdapter);

        } else {

            mMaterialItem.setVisibility(View.GONE);
        }
    }

    private void initTopPropsRv() {

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), NUMBER_OF_COLUMNS);
        mPropsRv.setLayoutManager(gridLayoutManager);
        int spacing = 0;//40

        Configuration config = getResources().getConfiguration();
        if (config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
            mPropsRv.addItemDecoration(new GridSpacingItemDecorationRTL(NUMBER_OF_COLUMNS, spacing, true, 0));
        } else {
            mPropsRv.addItemDecoration(new GridSpacingItemDecoration(NUMBER_OF_COLUMNS, spacing, true, 0));
        }

        mPropsRv.setAdapter(new PendingJobPropsAdapter(getActivity(), mCurrentPendingJob.getProperties(), mHashMapHeaders));

        mPropsRvOpenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = mPropsRv.getLayoutParams();
                if (params.height == (int) (PROPS_RV_HEIGHT * getResources().getDisplayMetrics().density)) {
                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    mPropsRv.setLayoutParams(params);
                } else {
                    params.height = (int) (PROPS_RV_HEIGHT * getResources().getDisplayMetrics().density);
                    mPropsRv.setLayoutParams(params);
                }
            }
        });

    }

    @Override
    public void onActionChecked(Action action) {

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.AJA_back_btn:
            case R.id.AJA_title:

                getActivity().onBackPressed();

                break;

            case R.id.AJA_job_activate_btn:

                if (mCurrentJobDetails != null && mCurrentJobDetails.getJobs() != null
                        && mCurrentJobDetails.getJobs().size() > 0) {
                    validateDialog(mCurrentJobDetails.getJobs().get(0).getID(), false);
                }
                break;

            case R.id.AJA_item_material:

                mListener.onShowRecipeFragment();

                break;

            case R.id.AJA_img1:

                if (mCurrentJobDetails != null && mCurrentJobDetails.getJobs() != null
                        && mCurrentJobDetails.getJobs().size() > 0) {
                    mListener.onStartGalleryActivity(mCurrentJobDetails.getJobs().get(0).getProductFiles(),
                            String.valueOf(mCurrentJobDetails.getJobs().get(0).getID()));
                }
                break;

            case R.id.AJA_img2:

                if (mCurrentJobDetails != null && mCurrentJobDetails.getJobs() != null
                        && mCurrentJobDetails.getJobs().size() > 0) {
                    mListener.onStartGalleryActivity(mCurrentJobDetails.getJobs().get(0).getProductFiles(),
                            String.valueOf(mCurrentJobDetails.getJobs().get(0).getID()));
                }
                break;

            case R.id.IJAM_img:

                if (mCurrentJobDetails != null && mCurrentJobDetails.getJobs() != null
                        && mCurrentJobDetails.getJobs().size() > 0) {
                    mListener.onStartGalleryActivity(mCurrentJobDetails.getJobs().get(0).getMold().getFiles(),
                            String.valueOf(mCurrentJobDetails.getJobs().get(0).getMold().getName()));
                }
                break;

            case R.id.AJA_open_edit_text_btn:

                openNotesDialog();
                break;
        }
    }

    private void openNotesDialog() {


        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = this.getLayoutInflater();
        @SuppressLint("InflateParams") View dialogView = inflater.inflate(R.layout.dialog_notes, null);
        builder.setView(dialogView);

        TextView noteTitleTv = dialogView.findViewById(R.id.DN_note_title);
        final EditText noteEt = dialogView.findViewById(R.id.DN_note);
        Button submitBtn = dialogView.findViewById(R.id.DN_btn);
        ImageButton closeButton = dialogView.findViewById(R.id.DN_close_btn);

        if (mProductNoteTv.getText().length() > 0) {

            noteTitleTv.setText(getString(R.string.edit_note));
            noteEt.setText(mProductNoteTv.getText());
        } else {
            noteTitleTv.setText(getString(R.string.add_note));
            noteEt.setHint(getString(R.string.enter_note_here));
        }

        final AlertDialog alert = builder.create();
        alert.show();

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateNotes(alert, noteEt.getText().toString());
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alert.dismiss();
            }
        });

    }

    private void updateNotes(final AlertDialog alert, final String note) {

        ProgressDialogManager.show(getActivity());

        SimpleRequests simpleRequests = new SimpleRequests();
        PersistenceManager pm = PersistenceManager.getInstance();
        PostUpdateNotesForJobRequest updateNotesRequest = new PostUpdateNotesForJobRequest(pm.getSessionId(), mCurrentPendingJob.getID(), note);
        simpleRequests.postUpdateNotesForJob(pm.getSiteUrl(), new PostUpdateNotesForJobCallback() {
            @Override
            public void onUpdateNotesSuccess(StandardResponse responseNewVersion) {
                mProductNoteTv.setText(note);
                ProgressDialogManager.dismiss();
                alert.dismiss();
            }

            @Override
            public void onUpdateNotesFailed(StandardResponse reason) {
                ProgressDialogManager.dismiss();
            }
        }, NetworkManager.getInstance(), updateNotesRequest, pm.getTotalRetries(), pm.getRequestTimeout());


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
                .scrollHandle(new DefaultScrollHandle(getActivity()))
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
    public void onPostExecute(File file) {

        loadPdfView(Uri.fromFile(file));
    }

    @Override
    public void onLoadFileError() {

        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    mProductPdfNoImageTv.setText(getString(R.string.loading_error));
                    mProductPdfNoImageLy.setVisibility(View.VISIBLE);
                    mProductPdfView.setVisibility(View.INVISIBLE);
                }
            });
        }
    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onLoadFileProgress() {

    }

    @Override
    public void onOpenActionDetails(Action action) {

        showActionDialog(action);
    }

    private void showActionDialog(final Action action) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

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
    public void onImageProductClick(List<String> fileUrl, String name) {
        mListener.onStartGalleryActivity(fileUrl, name);
    }

    public interface JobDetailsFragmentListener {

        void onShowRecipeFragment();

        void onStartGalleryActivity(List<String> productFiles, String name);

        void onPostActivateJob(ActivateJobRequest activateJobRequest);

        void onPostUpdateActions(ArrayList<Action> mUpdatedActions);
    }

}
