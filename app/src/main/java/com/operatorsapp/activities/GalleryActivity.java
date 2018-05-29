package com.operatorsapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.github.chrisbanes.photoview.OnScaleChangedListener;
import com.github.chrisbanes.photoview.PhotoView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.operatorsapp.R;
import com.operatorsapp.adapters.GalleryAdapter;
import com.operatorsapp.model.GalleryModel;

import java.util.ArrayList;

public class GalleryActivity extends AppCompatActivity implements View.OnClickListener, GalleryAdapter.GalleryAdapterListener, OnScaleChangedListener {

    private static final String EXTRA_FILE_URL = "EXTRA_FILE_URL";
    private static final String EXTRA_RECIPE_FILES_TITLE = "EXTRA_RECIPE_FILES_TITLE";

    private RecyclerView mRv;
    private ArrayList<String> mFileUrls;
    private GalleryAdapter mAdapter;
    private TextView mTitleTv;
    private PhotoView mImage;
    private ArrayList<GalleryModel> mGalleryModels;
    private String mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        mFileUrls = getIntent().getStringArrayListExtra(EXTRA_FILE_URL);

        mTitle = getIntent().getStringExtra(EXTRA_RECIPE_FILES_TITLE);

        initVars();

        initView();

        initListener();
    }

    private void initVars() {

        mRv = findViewById(R.id.AG_rv);

        mTitleTv = findViewById(R.id.AG_title);

        mImage = findViewById(R.id.AG_image);

    }

    private void initView() {

        mFileUrls.add("http://www.orimi.com/pdf-test.pdf");

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

    }

    private void initRv() {

        mGalleryModels = new ArrayList<>();

        for (String s: mFileUrls){

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
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.AG_close_btn:

                setResult(RESULT_OK);

                finish();

                break;
        }
    }

    @Override
    public void onImageClick(GalleryModel galleryModel) {

        ImageLoader.getInstance().displayImage(galleryModel.getUrl(), mImage);

        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void onPdfClick(GalleryModel galleryModel) {

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onScaleChange(float scaleFactor, float focusX, float focusY) {

//        (scaleFactor * 100) / mImage.getMaximumScale();
    }
}
