package com.operatorsapp.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.github.barteksc.pdfviewer.util.Constants;
import com.github.chrisbanes.photoview.OnScaleChangedListener;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.halilibo.bettervideoplayer.BetterVideoCallback;
import com.halilibo.bettervideoplayer.BetterVideoPlayer;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.operatorsapp.R;
import com.operatorsapp.adapters.GalleryAdapter;
import com.operatorsapp.application.OperatorApplication;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.model.GalleryModel;
import com.operatorsapp.model.PdfObject;
import com.operatorsapp.utils.DownloadHelper;
import com.shockwave.pdfium.PdfDocument;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GalleryActivity extends AppCompatActivity implements View.OnClickListener,
        GalleryAdapter.GalleryAdapterListener,
        OnScaleChangedListener,
        DownloadHelper.DownloadFileListener,
        OnPageChangeListener,
        OnLoadCompleteListener,
        OnErrorListener, BetterVideoCallback {

    private static final String TAG = GalleryActivity.class.getSimpleName();

    public static final String EXTRA_FILE_URL = "EXTRA_FILE_URL";
    public static final String EXTRA_RECIPE_FILES_TITLE = "EXTRA_RECIPE_FILES_TITLE";
    public static final String EXTRA_RECIPE_PDF_FILES = "EXTRA_RECIPE_PDF_FILES";
    public static final int EXTRA_GALLERY_CODE = 123;
    private static final String TEST_URL = "https://ia800201.us.archive.org/22/items/ksnn_compilation_master_the_internet/ksnn_compilation_master_the_internet_512kb.mp4";

    private RecyclerView mRv;
    private ArrayList<String> mFileUrls;
    private GalleryAdapter mAdapter;
    private TextView mTitleTv;
    private PhotoView mImage;
    private String mTitle;
    private TextView mScaleTv;
    private PDFView mPdfViewer;
    private PdfObject mSelectedPdf;
    private View mLoadingLy;
    private boolean isPdflastClick;
    private ArrayList<PdfObject> mPdfList;
    private View mLoadingProgress;
    private TextView mLoadingTv;
    private boolean isLoad;
    private DownloadHelper mDownloadHelper;
    private BetterVideoPlayer mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        // Analytics
        OperatorApplication application = (OperatorApplication) getApplication();
        Tracker mTracker = application.getDefaultTracker();
        mTracker.setHostname(PersistenceManager.getInstance().getSiteName());
        mTracker.setScreenName(this.getLocalClassName());
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        mFileUrls = getIntent().getStringArrayListExtra(EXTRA_FILE_URL);

        mTitle = getIntent().getStringExtra(EXTRA_RECIPE_FILES_TITLE);

        mPdfList = getIntent().getParcelableArrayListExtra(EXTRA_RECIPE_PDF_FILES);

        if (mPdfList == null) {
            mPdfList = new ArrayList<>();
        }

        initVars();

        initView();

        initListener();
    }

    private void initVars() {

        mRv = findViewById(R.id.AG_rv);

        mTitleTv = findViewById(R.id.AG_title);

        mImage = findViewById(R.id.AG_image);

        mPdfViewer = findViewById(R.id.AG_pdf);

        mPlayer = findViewById(R.id.AG_player);

        mScaleTv = findViewById(R.id.AG_scale_tv);

        mLoadingLy = findViewById(R.id.AG_loading_ly);

        mLoadingProgress = findViewById(R.id.AG_loading_progress);

        mLoadingTv = findViewById(R.id.AG_loading_tv);

    }

    private void initView() {

//        initTest();

        mImage.setMaximumScale((float) 9.75);

        mImage.setMinimumScale((float) 0.25);

        mPdfViewer.setMaxZoom((float) 9.75);

        mPdfViewer.setMinZoom((float) 0.25);

        Constants.Pinch.MINIMUM_ZOOM = 0.25f;

//        mPlayer.setCallback(this);
//
//        mPlayer.enableControls();
//
//        mPlayer.enableSwipeGestures();
//
//        mPlayer.setSource(Uri.parse(TEST_URL));

        //todo update Visibility of AG_scale_ly when is player or not
        //todo call reset when close player or open new video

        mTitleTv.setText(mTitle);

        initRv();

        mImage.getAttacher().setOnScaleChangeListener(this);

        Typeface tf = Typeface.createFromAsset(this.getAssets(),
                "fonts/BreeSerif-Regular.ttf");

        mLoadingTv.setTypeface(tf);

        initShowedFile();
    }

    private void initShowedFile() {

        if (!mFileUrls.get(0).endsWith("pdf")) {

            ImageLoader.getInstance().displayImage(mFileUrls.get(0), mImage);

        } else {

            openPdf(new GalleryModel(mFileUrls.get(0), true));

        }

    }

//    private void initTest() {
//        mFileUrls.add("https://www.ets.org/Media/Tests/GRE/pdf/gre_research_validity_data.pdf");
//
//        mFileUrls.add(mFileUrls.get(0));
//        mFileUrls.add(mFileUrls.get(0));
//        mFileUrls.add(mFileUrls.get(0));
//
//        mFileUrls.add("https://s1.q4cdn.com/806093406/files/doc_downloads/test.pdf");
//
//        mFileUrls.add(mFileUrls.get(0));
//        mFileUrls.add(mFileUrls.get(0));
//        mFileUrls.add(mFileUrls.get(0));
//        mFileUrls.add(mFileUrls.get(0));
//        mFileUrls.add(mFileUrls.get(0));
//        mFileUrls.add(mFileUrls.get(0));
//        mFileUrls.add(mFileUrls.get(0));
//        mFileUrls.add(mFileUrls.get(0));
//        mFileUrls.add(mFileUrls.get(0));
//        mFileUrls.add(mFileUrls.get(0));
//        mFileUrls.add(mFileUrls.get(0));
//    }

    private void initRv() {

        ArrayList<GalleryModel> mGalleryModels = new ArrayList<>();

        for (String s : mFileUrls) {

            mGalleryModels.add(new GalleryModel(s, false));
        }

        mGalleryModels.get(0).setSelected(true);

        mAdapter = new GalleryAdapter(mGalleryModels, this, this);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        mRv.setLayoutManager(layoutManager);

        mRv.setAdapter(mAdapter);
    }

    private void initListener() {

        findViewById(R.id.AG_close_btn).setOnClickListener(this);

        findViewById(R.id.AG_scale_minus).setOnClickListener(this);

        findViewById(R.id.AG_scale_plus).setOnClickListener(this);

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        updatePdfViewer(mPdfViewer.getZoom());

        return super.dispatchTouchEvent(ev);
    }

    private void updatePdfViewer(float zoom) {

        if (isPdflastClick) {

            if (zoom * 100 != 100) {

                mScaleTv.setText(String.format("%s%%", String.valueOf((int) (zoom * 100))));

            } else {

                mScaleTv.setText(R.string.one_hundred_percent);
            }

        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.AG_close_btn:

                finishActivity();

                break;

            case R.id.AG_scale_minus:

                updateScale(false);

                break;

            case R.id.AG_scale_plus:

                updateScale(true);

                break;
        }
    }

    @Override
    public void onBackPressed() {
        finishActivity();
    }

    public void finishActivity() {
        Intent intent = getIntent();
        intent.putExtra(EXTRA_RECIPE_PDF_FILES, mPdfList);

        setResult(RESULT_OK, intent);

        finish();
    }

    private void updateScale(boolean isPositive) {

        if (!isPdflastClick) {

            int scaleBase25 = ((int) (mImage.getScale() * 100) / 25) * 25;

            if (isPositive) {

                float scale = (scaleBase25 + 25) / 100f;

                scaleImage(scale);

            } else {

                float scale;

                if ((((mImage.getScale() * 100) / 25) * 25) % 25 == 0) {

                    scale = ((scaleBase25) - 25) / 100f;

                } else {

                    scale = ((scaleBase25)) / 100f;
                }

                scaleImage(scale);

            }

        } else {

            int scaleBase25 = ((int) (mPdfViewer.getZoom() * 100) / 25) * 25;

            if (isPositive) {

                float scale = (scaleBase25 + 25) / 100f;

                scalePdf(scale);

            } else {

                float scale;

                if ((((mPdfViewer.getZoom() * 100) / 25) * 25) % 25 == 0) {

                    scale = ((scaleBase25) - 25) / 100f;

                } else {

                    scale = ((scaleBase25)) / 100f;
                }

                scalePdf(scale);
            }

        }
    }

    private void scalePdf(float scale) {

        if (scale <= mPdfViewer.getMaxZoom() && scale >= mPdfViewer.getMinZoom()) {

            if (!isLoad) {

                mPdfViewer.zoomWithAnimation(scale);

                updatePdfViewer(scale);

            }
        }
    }

    private void scaleImage(float scale) {
        if (scale <= mImage.getMaximumScale() && scale >= mImage.getMinimumScale()) {

            mImage.setScale(scale);

            updateImageScale(scale);

        }
    }

    @Override
    public void onImageClick(GalleryModel galleryModel) {

        resetDialog();

        mScaleTv.setText(String.format("%s%%", String.valueOf(100)));

        isPdflastClick = false;

        mPdfViewer.recycle();

        mImage.setVisibility(View.VISIBLE);
        mPdfViewer.setVisibility(View.GONE);
        mLoadingLy.setVisibility(View.GONE);

        ImageLoader.getInstance().displayImage(galleryModel.getUrl(), mImage);

        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void onPdfClick(GalleryModel galleryModel) {

        resetDialog();

        mPdfViewer.recycle();

        mScaleTv.setText(String.format("%s%%", String.valueOf(100)));

        isPdflastClick = true;

        mImage.setVisibility(View.GONE);

        mAdapter.notifyDataSetChanged();

        openPdf(galleryModel);

    }

    private void resetDialog() {

        mLoadingLy.setVisibility(View.GONE);

        mLoadingProgress.setVisibility(View.VISIBLE);

        mLoadingTv.setText(getResources().getString(R.string.loading_file));
    }

    private void openPdf(GalleryModel galleryModel) {

        if (mSelectedPdf != null && mDownloadHelper != null) {

            mSelectedPdf = new PdfObject(null, galleryModel.getUrl());

            mDownloadHelper.cancelDownloadFileFromUrl();

        } else {

            mSelectedPdf = new PdfObject(null, galleryModel.getUrl());

            initLoading(galleryModel.getUrl());

        }

    }

    private void initLoading(String url) {

        if (!isLoad) {

            for (PdfObject pdfObject : mPdfList) {

                if (pdfObject.getUrl().equals(mSelectedPdf.getUrl())) {

                    mPdfViewer.setVisibility(View.VISIBLE);

                    isLoad = true;

                    loadPdfView(pdfObject.getUri());

                    return;
                }
            }

            if (isStoragePermissionGranted()) {

                isLoad = true;

                downLoadFile(url);

            }
        } else {

            mPdfViewer.setVisibility(View.VISIBLE);

            mLoadingLy.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onScaleChange(float scaleFactor, float focusX, float focusY) {

        updateImageScale(mImage.getScale());
    }

    private void updateImageScale(float scale) {

        if (mImage.getScale() * 100 != 100) {

            mScaleTv.setText(String.format("%s%%", String.valueOf((int) (scale * 100))));

        } else {

            mScaleTv.setText(getString(R.string.one_hundred_percent));
        }
    }

    private void downLoadFile(String url) {

        mPdfViewer.setVisibility(View.VISIBLE);
        mLoadingLy.setVisibility(View.VISIBLE);

        mDownloadHelper = new DownloadHelper(this, this);
        mDownloadHelper.downloadFileFromUrl(url);

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

            downLoadFile(mSelectedPdf.getUrl());

            isLoad = true;
        }
    }


    @Override
    public void onPostExecute(File file) {

        if (file.exists()) {
            mSelectedPdf.setUri(Uri.fromFile(file));

            mPdfList.add(mSelectedPdf);

            loadPdfView(mSelectedPdf.getUri());

        } else {

            showLoadingError();
        }
    }

    private void loadPdfView(Uri uri) {

        mLoadingLy.setVisibility(View.VISIBLE);

        mPdfViewer.fromUri(uri)
                .defaultPage(0)
                .enableSwipe(true)
                .swipeHorizontal(false)
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .onError(this)
                .scrollHandle(new DefaultScrollHandle(this))
                .load();
    }

    @Override
    public void onPageChanged(int page, int pageCount) {
//        pageNumber = page;
//        setTitle(String.format("%s %s / %s", pdfFileName, page + 1, pageCount));
    }


    @Override
    public void loadComplete(int nbPages) {

        mSelectedPdf = null;

        mLoadingLy.setVisibility(View.GONE);

        isLoad = false;

        if (isPdflastClick) {

//            PdfDocument.Meta meta = mPdfViewer.getDocumentMeta();
            printBookmarksTree(mPdfViewer.getTableOfContents(), "-");
        }
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
    public void onError(Throwable t) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                showLoadingError();
            }
        });

    }

    @Override
    public void onLoadFileError() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                showLoadingError();
            }
        });
    }

    @Override
    public void onCancel() {

        isLoad = false;

        if (mSelectedPdf != null) {

            mPdfViewer.recycle();

            initLoading(mSelectedPdf.getUrl());

        } else if (isPdflastClick) {

            showLoadingError();
        }

    }

    @Override
    public void onLoadFileProgress() {

        if (mLoadingTv.getText() != getString(R.string.loading_file)) {
            mLoadingTv.setText(getResources().getString(R.string.loading_file));
        }
    }

    private void showLoadingError() {

        mLoadingProgress.setVisibility(View.GONE);

        mLoadingTv.setText(getResources().getString(R.string.loading_error));
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPlayer.pause();
    }

    @Override
    public void onStarted(BetterVideoPlayer player) {

    }

    @Override
    public void onPaused(BetterVideoPlayer player) {

    }

    @Override
    public void onPreparing(BetterVideoPlayer player) {

    }

    @Override
    public void onPrepared(BetterVideoPlayer player) {

    }

    @Override
    public void onBuffering(int percent) {

    }

    @Override
    public void onError(BetterVideoPlayer player, Exception e) {

    }

    @Override
    public void onCompletion(BetterVideoPlayer player) {

    }

    @Override
    public void onToggleControls(BetterVideoPlayer player, boolean isShowing) {

    }
}
