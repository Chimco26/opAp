package com.operatorsapp.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.github.chrisbanes.photoview.OnScaleChangedListener;
import com.github.chrisbanes.photoview.PhotoView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.operatorsapp.R;
import com.operatorsapp.adapters.GalleryAdapter;
import com.operatorsapp.model.GalleryModel;
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
        OnErrorListener {

    private static final String TAG = GalleryActivity.class.getSimpleName();

    private static final String EXTRA_FILE_URL = "EXTRA_FILE_URL";
    private static final String EXTRA_RECIPE_FILES_TITLE = "EXTRA_RECIPE_FILES_TITLE";
    private static final String EXTRA_RECIPE_PDF_FILES = "EXTRA_RECIPE_PDF_FILES";

    private RecyclerView mRv;
    private ArrayList<String> mFileUrls;
    private GalleryAdapter mAdapter;
    private TextView mTitleTv;
    private PhotoView mImage;
    private ArrayList<GalleryModel> mGalleryModels;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        mFileUrls = getIntent().getStringArrayListExtra(EXTRA_FILE_URL);

        mTitle = getIntent().getStringExtra(EXTRA_RECIPE_FILES_TITLE);

        mPdfList = getIntent().getParcelableArrayListExtra(EXTRA_RECIPE_PDF_FILES);

        if (mPdfList == null){ mPdfList = new ArrayList<>();}

        initVars();

        initView();

        initListener();
    }

    private void initVars() {

        mRv = findViewById(R.id.AG_rv);

        mTitleTv = findViewById(R.id.AG_title);

        mImage = findViewById(R.id.AG_image);

        mPdfViewer = findViewById(R.id.AG_pdf);

        mScaleTv = findViewById(R.id.AG_scale_tv);

        mLoadingLy = findViewById(R.id.AG_loading_ly);

        mLoadingProgress = findViewById(R.id.AG_loading_progress);

        mLoadingTv = findViewById(R.id.AG_loading_tv);

    }

    private void initView() {

        mFileUrls.add("https://www.ets.org/Media/Tests/GRE/pdf/gre_research_validity_data.pdf");

        mFileUrls.add(mFileUrls.get(0));
        mFileUrls.add(mFileUrls.get(0));
        mFileUrls.add(mFileUrls.get(0));
        mFileUrls.add(mFileUrls.get(0));
        mFileUrls.add(mFileUrls.get(0));
        mFileUrls.add(mFileUrls.get(0));
        mFileUrls.add(mFileUrls.get(0));
        mFileUrls.add(mFileUrls.get(0));
        mFileUrls.add(mFileUrls.get(0));
        mFileUrls.add(mFileUrls.get(0));
        mFileUrls.add(mFileUrls.get(0));
        mFileUrls.add(mFileUrls.get(0));
        mFileUrls.add(mFileUrls.get(0));
        mFileUrls.add(mFileUrls.get(0));

        ImageLoader.getInstance().displayImage(mFileUrls.get(0), mImage);

        mTitleTv.setText(mTitle);

        initRv();

        mImage.getAttacher().setOnScaleChangeListener(this);

        Typeface tf = Typeface.createFromAsset(this.getAssets(),
                "fonts/BreeSerif-Regular.ttf");

        mLoadingTv.setTypeface(tf);

    }

    private void initRv() {

        mGalleryModels = new ArrayList<>();

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

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        if (isPdflastClick) {

            if (mPdfViewer.getZoom() > 1.1) {

                mScaleTv.setVisibility(View.VISIBLE);

                mScaleTv.setText("x: " + String.valueOf(mPdfViewer.getZoom()).subSequence(0, 3));

                if (mScaleTv.getText().charAt(5) == ".".charAt(0)){

                    mScaleTv.setText(mScaleTv.getText().toString().replace(".", ""));
                }

            }else {

                mScaleTv.setVisibility(View.GONE);
            }

        }

        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.AG_close_btn:

                Intent intent = getIntent();
                intent.putExtra(EXTRA_RECIPE_PDF_FILES, mPdfList);

                setResult(RESULT_OK, intent);

                finish();

                break;
        }
    }

    @Override
    public void onImageClick(GalleryModel galleryModel) {

        resetDialog();

        isPdflastClick = false;

        mPdfViewer.recycle();

        mImage.setVisibility(View.VISIBLE);
        mPdfViewer.setVisibility(View.GONE);
        mLoadingLy.setVisibility(View.GONE);
        mScaleTv.setVisibility(View.GONE);

        ImageLoader.getInstance().displayImage(galleryModel.getUrl(), mImage);

        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void onPdfClick(GalleryModel galleryModel) {

        resetDialog();

        isPdflastClick = true;

        mImage.setVisibility(View.GONE);

        mScaleTv.setVisibility(View.GONE);

        mAdapter.notifyDataSetChanged();

        openPdf(galleryModel);

    }

    private void resetDialog() {

        mLoadingLy.setVisibility(View.GONE);

        mLoadingProgress.setVisibility(View.VISIBLE);

        mLoadingTv.setText(getResources().getString(R.string.loading_file));
    }

    private void openPdf(GalleryModel galleryModel) {

        mSelectedPdf = new PdfObject(null, galleryModel.getUrl());

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

                downLoadFile(galleryModel.getUrl());

                isLoad = true;
            }
        }else {

            mPdfViewer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onScaleChange(float scaleFactor, float focusX, float focusY) {

        if (mImage.getScale() > 1.1) {

            mScaleTv.setVisibility(View.VISIBLE);

            mScaleTv.setText("x: " + String.valueOf(mImage.getScale()).subSequence(0, 3));

            if (mScaleTv.getText().charAt(5) == ".".charAt(0)){

                mScaleTv.setText(mScaleTv.getText().toString().replace(".", ""));
            }

        }else {

            mScaleTv.setVisibility(View.GONE);
        }
    }

    private void downLoadFile(String url) {

        mPdfViewer.setVisibility(View.VISIBLE);
        mLoadingLy.setVisibility(View.VISIBLE);

        DownloadHelper helper = new DownloadHelper(this, this);

        helper.downloadFile(url, "operatorAppPdf");

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

        mSelectedPdf.setUri(Uri.fromFile(file));

        mPdfList.add(mSelectedPdf);

        loadPdfView(mSelectedPdf.getUri());

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

        if (isPdflastClick) {

            isLoad = false;

            mLoadingLy.setVisibility(View.GONE);

            PdfDocument.Meta meta = mPdfViewer.getDocumentMeta();
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

                mLoadingProgress.setVisibility(View.GONE);

                mLoadingTv.setText(getResources().getString(R.string.loading_error));
            }
        });

    }

    @Override
    public void onLoadFileError() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                mLoadingProgress.setVisibility(View.GONE);

                mLoadingTv.setText(getResources().getString(R.string.loading_error));
            }
        });
    }

}
