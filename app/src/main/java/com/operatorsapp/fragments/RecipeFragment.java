package com.operatorsapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.operators.reportrejectnetworkbridge.server.response.Recipe.BaseSplits;
import com.operators.reportrejectnetworkbridge.server.response.Recipe.ChannelSplits;
import com.operators.reportrejectnetworkbridge.server.response.Recipe.RecipeData;
import com.operators.reportrejectnetworkbridge.server.response.Recipe.RecipeResponse;
import com.operatorsapp.R;
import com.operatorsapp.adapters.No0ChanneAdapter;
import com.operatorsapp.utils.ViewTagsHelper;

import java.util.ArrayList;
import java.util.List;

public class RecipeFragment extends Fragment implements View.OnClickListener, No0ChanneAdapter.Channel100AdapterListener {

    private static final String RECIPE_RESPONS_KEY = "RECIPE_RESPONS_KEY";
    private OnRecipeFragmentListener mListener;
    private RecipeResponse mRecipeResponse;
    private View mchannel0BotomView;
    private View mchannel1_99BotomView;
    private View mchannel100BotomView;
    private View mLayoutChannel0;
    private TextView mLayoutChannel0Title;
    private ImageView mLayoutChannel0Image;
    private View mLayoutChannel0Item;
    private TextView mLayoutChannel0ItemTitleTv;
    private TextView mLayoutChannel0ItemSubTitleTv;
    private LinearLayout mLayoutChannel0ItemSplitLy;
    private View mMainView;
    private View mLayoutChannel100;
    private TextView mLayoutChannel100Title;
    private RecyclerView mLayoutChannel100Rv;
    private No0ChanneAdapter mNo0ChanneAdapter;
    private LinearLayout mlayoutChannel1_99;
    private View mLayoutChannel0NoDataImage;
    private View mLayoutChannel100NoDataImage;
    private View mChannel1_99NoDataImage;
    private View mLayoutChannel0Mainlayout;
    private View mChannel1_99NoDataTv;
    private View mLayoutChannel100NoDataTv;
    private View mLayoutChannel0NoDataTv;
    private View mlayoutChannel1_99NoDataLayout;
    private View mlayoutChannel1_99MainLy;
    private View mLayoutChannel100RvLy;

    public static RecipeFragment newInstance(RecipeResponse recipeResponse) {
        RecipeFragment recipeFragment = new RecipeFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(RECIPE_RESPONS_KEY, recipeResponse);
        recipeFragment.setArguments(bundle);
        return recipeFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            if (getArguments().getParcelable(RECIPE_RESPONS_KEY) != null) {
                mRecipeResponse = getArguments().getParcelable(RECIPE_RESPONS_KEY);
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        if (context instanceof RecipeFragment.OnRecipeFragmentListener) {
            mListener = (RecipeFragment.OnRecipeFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnRecipeFragmentListener");
        }
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.frament_recipe, container, false);

        return mMainView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initVars(view);
        initView();
        initListener(view);
    }

    private void initVars(View view) {

        mchannel0BotomView = view.findViewById(R.id.FR_channel_0_btn_bottom);

        mchannel1_99BotomView = view.findViewById(R.id.FR_channel_1_99_btn_bottom);

        mchannel100BotomView = view.findViewById(R.id.FR_channel_100_btn_bottom);

        initChannel0Vars(view);

        initChannel1_99Vars(view);

        initChannel100Vars(view);

    }

    private void initChannel1_99Vars(View view) {

        mlayoutChannel1_99 = view.findViewById(R.id.FR_channel_1_99_ly);

        mlayoutChannel1_99MainLy = view.findViewById(R.id.FR_channel_1_99_main_ly);

        mlayoutChannel1_99NoDataLayout = view.findViewById(R.id.FR_channel_1_99_no_data_ly);

        mChannel1_99NoDataImage = view.findViewById(R.id.FR_C1_99_no_data_img);

        mChannel1_99NoDataTv = view.findViewById(R.id.FR_C1_99_no_data_tv);
    }

    private void initChannel100Vars(View view) {

        mLayoutChannel100 = view.findViewById(R.id.FR_channel_100_ly);

        mLayoutChannel100RvLy = view.findViewById(R.id.channel_100_rv_ly);

        mLayoutChannel100Title = mLayoutChannel100.findViewById(R.id.C100_tv);

        mLayoutChannel100Rv = mLayoutChannel100.findViewById(R.id.channel_100_rv);

        mLayoutChannel100NoDataImage = mLayoutChannel100.findViewById(R.id.C100_no_data_img);

        mLayoutChannel100NoDataTv = mLayoutChannel100.findViewById(R.id.C100_no_data_tv);
    }

    private void initChannel0Vars(View view) {

        mLayoutChannel0 = view.findViewById(R.id.FR_channel_0_ly);

        mLayoutChannel0Mainlayout = view.findViewById(R.id.C0L_layout);

        mLayoutChannel0Title = mLayoutChannel0.findViewById(R.id.C0L_title_tv);

        mLayoutChannel0Image = mLayoutChannel0.findViewById(R.id.C0L_img);

        mLayoutChannel0Item = mLayoutChannel0.findViewById(R.id.C0L_item);

        mLayoutChannel0NoDataImage = mLayoutChannel0.findViewById(R.id.C0L_no_data_img);

        mLayoutChannel0NoDataTv = mLayoutChannel0.findViewById(R.id.C0L_no_data_tv);

        mLayoutChannel0ItemTitleTv = mLayoutChannel0Item.findViewById(R.id.IP_title);

        mLayoutChannel0ItemSubTitleTv = mLayoutChannel0Item.findViewById(R.id.IP_sub_title);

        mLayoutChannel0ItemSplitLy = mLayoutChannel0Item.findViewById(R.id.IP_split_ly);

    }

    private void initView() {

        initChannel0View();

        initChannel100View();

        initChanne1_1_99_View();

    }

    private void initChannel100View() {

        if (mRecipeResponse != null && mRecipeResponse.getRecipeData() != null
                && mRecipeResponse.getRecipeData().size() > 0 && mRecipeResponse.getRecipeData().get(0) != null &&
                mRecipeResponse.getRecipeData().get(mRecipeResponse.getRecipeData().size() - 1).getChannelNumber() == 100) {

            mLayoutChannel100NoDataImage.setVisibility(View.GONE);
            mLayoutChannel100NoDataTv.setVisibility(View.GONE);
            mLayoutChannel100RvLy.setVisibility(View.VISIBLE);

            mLayoutChannel100Title.setText(mRecipeResponse.getRecipeData().get(mRecipeResponse.getRecipeData().size() - 1).getName());

            mNo0ChanneAdapter = new No0ChanneAdapter(getActivity(), this,
                    (ArrayList<ChannelSplits>) mRecipeResponse.getRecipeData().get(mRecipeResponse.getRecipeData().size() - 1).getChannelSplits(), No0ChanneAdapter.TYPE_CHANNEL_100);

            LinearLayoutManager layoutManager
                    = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

            mLayoutChannel100Rv.setLayoutManager(layoutManager);

            mLayoutChannel100Rv.setAdapter(mNo0ChanneAdapter);

        } else {

            mLayoutChannel100NoDataImage.setVisibility(View.VISIBLE);
            mLayoutChannel100NoDataTv.setVisibility(View.VISIBLE);
            mLayoutChannel100RvLy.setVisibility(View.GONE);
        }
    }

    private void initChannel0View() {

        if (mRecipeResponse != null && mRecipeResponse.getRecipeData() != null
                && mRecipeResponse.getRecipeData().size() > 0 && mRecipeResponse.getRecipeData().get(0) != null) {

            mLayoutChannel0Mainlayout.setVisibility(View.VISIBLE);
            mLayoutChannel0NoDataImage.setVisibility(View.GONE);
            mLayoutChannel0NoDataTv.setVisibility(View.GONE);

            RecipeData recipeChannel0 = mRecipeResponse.getRecipeData().get(0);

            mLayoutChannel0Title.setText(recipeChannel0.getName());

            if (mRecipeResponse.getProductData() != null && mRecipeResponse.getProductData().getFileUrl() != null &&
                    mRecipeResponse.getProductData().getFileUrl().size() > 0) {
                ImageLoader.getInstance().displayImage(mRecipeResponse.getProductData().getFileUrl().get(0), mLayoutChannel0Image);
            }

            mLayoutChannel0ItemTitleTv.setText(recipeChannel0.getChannelSplits().get(0).getName());

            if (recipeChannel0.getChannelSplits().get(0).getMaterialInformation() != null) {

                mLayoutChannel0ItemSubTitleTv.setText(recipeChannel0.getChannelSplits().get(0).getMaterialInformation().getCatalogID());

            } else {

                mLayoutChannel0ItemSubTitleTv.setText("");

            }

            if (recipeChannel0.getChannelSplits().get(0).getBaseSplits() != null) {

                if (mLayoutChannel0ItemSplitLy.getChildAt(0) != null) {

                    mLayoutChannel0ItemSplitLy.removeAllViews();
                }

                for (BaseSplits baseSplits : recipeChannel0.getChannelSplits().get(0).getBaseSplits()) {

                    LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                    View itemView = layoutInflater.inflate((R.layout.item_split), (ViewGroup) mMainView, false);

                    ((TextView) itemView.findViewById(R.id.IS_tv)).setText(baseSplits.getPropertyName());

                    ((TextView) itemView.findViewById(R.id.IS_tv_2)).setText(baseSplits.getFValue() + "");

                    ((TextView) itemView.findViewById(R.id.IS_range_tv)).setText(baseSplits.getRange());

                    mLayoutChannel0ItemSplitLy.addView(itemView);

                }

            } else if (mLayoutChannel0ItemSplitLy.getChildAt(0) != null) {

                mLayoutChannel0ItemSplitLy.removeAllViews();
            }

        } else {

            mLayoutChannel0Mainlayout.setVisibility(View.GONE);
            mLayoutChannel0NoDataImage.setVisibility(View.VISIBLE);
            mLayoutChannel0NoDataTv.setVisibility(View.VISIBLE);

            if (mLayoutChannel0ItemSplitLy.getChildAt(0) != null) {

                mLayoutChannel0ItemSplitLy.removeAllViews();
            }
        }

    }

    private void initListener(View view) {

        view.findViewById(R.id.FR_channel_0_btn).setOnClickListener(this);

        view.findViewById(R.id.FR_channel_1_99_btn).setOnClickListener(this);

        view.findViewById(R.id.FR_channel_100_btn).setOnClickListener(this);

        mLayoutChannel0Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mRecipeResponse.getProductData() != null && mRecipeResponse.getProductData().getFileUrl() != null) {
                    mListener.onImageProductClick(mRecipeResponse.getProductData().getFileUrl());
                }
            }
        });

    }

    private void initChanne1_1_99_View() {

        if (mRecipeResponse != null) {
            List<RecipeData> recipeResponse_1_99 = mRecipeResponse.getRecipeData();

            if (mRecipeResponse != null && mRecipeResponse.getRecipeData() != null && mRecipeResponse.getRecipeData().size() > 0 &&
                    mRecipeResponse.getRecipeData().get(mRecipeResponse.getRecipeData().size() - 1).getChannelNumber() == 100) {

                recipeResponse_1_99.remove(mRecipeResponse.getRecipeData().get(mRecipeResponse.getRecipeData().size() - 1));
            }

            if (mRecipeResponse != null && mRecipeResponse.getRecipeData() != null && mRecipeResponse.getRecipeData().size() > 0) {
                recipeResponse_1_99.remove(0);
            }

            if (recipeResponse_1_99 != null && recipeResponse_1_99.size() > 0) {

                mChannel1_99NoDataImage.setVisibility(View.GONE);
                mChannel1_99NoDataTv.setVisibility(View.GONE);
                mlayoutChannel1_99NoDataLayout.setVisibility(View.GONE);

                if (mlayoutChannel1_99.getChildAt(0) != null) {

                    mlayoutChannel1_99.removeAllViews();
                }

                for (RecipeData recipeData : recipeResponse_1_99) {

                    ViewTagsHelper.addTitle(getActivity(), recipeData.getName(), mlayoutChannel1_99);

                    ViewTagsHelper.addRv(getActivity(), recipeData.getChannelSplits(), mlayoutChannel1_99, this);

                    ViewTagsHelper.addSeparator(getActivity(), mlayoutChannel1_99);
                }

            } else {

                mlayoutChannel1_99.removeAllViews();

                mChannel1_99NoDataImage.setVisibility(View.VISIBLE);

                mChannel1_99NoDataTv.setVisibility(View.VISIBLE);

                mlayoutChannel1_99NoDataLayout.setVisibility(View.VISIBLE);
            }
        }else {

            mlayoutChannel1_99.removeAllViews();

            mChannel1_99NoDataImage.setVisibility(View.VISIBLE);

            mChannel1_99NoDataTv.setVisibility(View.VISIBLE);

            mlayoutChannel1_99NoDataLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.FR_channel_0_btn:

                updateBar(view.getId());

                break;

            case R.id.FR_channel_1_99_btn:

                updateBar(view.getId());

                break;

            case R.id.FR_channel_100_btn:

                updateBar(view.getId());

                break;

        }
    }

    private void updateBar(int id) {

        mchannel0BotomView.setVisibility(View.INVISIBLE);
        mchannel1_99BotomView.setVisibility(View.INVISIBLE);
        mchannel100BotomView.setVisibility(View.INVISIBLE);

        switch (id) {

            case R.id.FR_channel_0_btn:

                mchannel1_99BotomView.setVisibility(View.INVISIBLE);
                mchannel100BotomView.setVisibility(View.INVISIBLE);
                mchannel0BotomView.setVisibility(View.VISIBLE);

                mlayoutChannel1_99MainLy.setVisibility(View.GONE);
                mLayoutChannel100.setVisibility(View.GONE);
                mLayoutChannel0.setVisibility(View.VISIBLE);
                break;

            case R.id.FR_channel_1_99_btn:

                mchannel0BotomView.setVisibility(View.INVISIBLE);
                mchannel100BotomView.setVisibility(View.INVISIBLE);
                mchannel1_99BotomView.setVisibility(View.VISIBLE);

                mLayoutChannel0.setVisibility(View.GONE);
                mLayoutChannel100.setVisibility(View.GONE);
                mlayoutChannel1_99MainLy.setVisibility(View.VISIBLE);

                break;

            case R.id.FR_channel_100_btn:

                mchannel0BotomView.setVisibility(View.INVISIBLE);
                mchannel1_99BotomView.setVisibility(View.INVISIBLE);
                mchannel100BotomView.setVisibility(View.VISIBLE);

                mLayoutChannel0.setVisibility(View.GONE);
                mlayoutChannel1_99MainLy.setVisibility(View.GONE);
                mLayoutChannel100.setVisibility(View.VISIBLE);

                break;

        }

    }

    public void updateRecipeResponse(RecipeResponse recipeResponse) {

        mRecipeResponse = recipeResponse;

        initView();
    }

    @Override
    public void onImageProductClick(List<String> fileUrls) {

        mListener.onImageProductClick(fileUrls);
    }

    public interface OnRecipeFragmentListener {

        void onImageProductClick(List<String> fileUrl);
    }
}