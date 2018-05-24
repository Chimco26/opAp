
package com.operatorsapp.activities;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.operatorsapp.R;
import com.operatorsapp.adapters.GalleryAdapter;

import java.util.ArrayList;

public class GalleryActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String EXTRA_FILE_URL = "EXTRA_FILE_URL";
    private RecyclerView mRv;
    private ArrayList<String> mFileUrls;
    private GalleryAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        mFileUrls = getIntent().getStringArrayListExtra(EXTRA_FILE_URL);

        initVars();

        initView();

        initListener();
    }

    private void initVars() {

        mRv = findViewById(R.id.AG_rv);
    }

    private void initView() {

        mAdapter = new GalleryAdapter(mFileUrls);

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

        switch (v.getId()){

            case R.id.AG_close_btn:

                setResult(RESULT_OK);

                finish();

                break;
        }
    }
}
