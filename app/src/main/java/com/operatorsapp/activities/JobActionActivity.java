package com.operatorsapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.operatorsapp.R;

import java.util.ArrayList;

public class JobActionActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = JobActionActivity.class.getSimpleName();
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
    private EditText mSearchViewEt;
    private RecyclerView mSearchViewRv;
    private RecyclerView mResultProductRv;
    private ImageView mProductShema;
    private ImageView mProductImage;
    private View mProductShemaNoImageLy;
    private View mProductImageNoImageLy;
    private View mMaterialItem;
    private TextView mMaterialItemTitleTv;
    private RecyclerView mMaterialItemRv;
    private View mMoldItem;
    private TextView mMoldItemTitleTv;
    private ImageView mMoldItemImg;
    private RecyclerView mMoldItemRv;
    private RecyclerView mActionRv;
    private View mActionItemLy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_action);

        initVars();

        initView();

        initListener();
    }

    private void initVars() {

        mTitleTv = findViewById(R.id.AJA_job_id_tv);

        mProductShema = findViewById(R.id.AJA_img1);

        mProductImage = findViewById(R.id.AJA_img2);

        mProductShemaNoImageLy = findViewById(R.id.AJA_img1_no_image);

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
    }

    private void initVarsMoldItem() {

        mMoldItem = findViewById(R.id.AJA_item_mold);

        mMoldItemTitleTv = mMoldItem.findViewById(R.id.IJAM_title);

        mMoldItemImg = mMoldItem.findViewById(R.id.IJAM_img);

        mMoldItemRv = mMoldItem.findViewById(R.id.IJAM_rv);
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
        mSearchViewRv = findViewById(R.id.AJA_search_rv);
        mResultProductRv = findViewById(R.id.AJA_product_rv);
    }

    private void initView() {

    }

    private void initListener() {

        findViewById(R.id.AJA_back_btn).setOnClickListener(this);
        findViewById(R.id.AJA_search_btn).setOnClickListener(this);
        findViewById(R.id.AJA_job_activate_btn).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }

    private String searchInList(String search, ArrayList<String> arrayList){

        for (String string: arrayList){

            if (string.toLowerCase().contains(search.toLowerCase())){

                return string;
            }
        }

        return "";
    }
}
