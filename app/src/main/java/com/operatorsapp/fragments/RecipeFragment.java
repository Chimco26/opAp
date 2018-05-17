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
import com.operators.reportrejectnetworkbridge.server.response.Recipe.RecipeData;
import com.operators.reportrejectnetworkbridge.server.response.Recipe.RecipeResponse;
import com.operatorsapp.R;

public class RecipeFragment extends Fragment implements View.OnClickListener {

    private static final String RECIPE_RESPONS_KEY = "RECIPE_RESPONS_KEY";
    private OnRecipeFragmentListener mListener;
    private RecipeResponse mRecipeResponse;
    private View mchannel0BootomView;
    private View mchannel1_99BootomView;
    private View mchannel100BootomView;
    private View mLayoutChannel0;
    private TextView mLayoutChannel0Title;
    private ImageView mLayoutChannel0Image;
    private View mLayoutChannel0Item;
    private TextView mLayoutChannel0ItemTitleTv;
    private TextView mLayoutChannel0ItemSubTitleTv;
    private RecyclerView mLayoutChannel0ItemRv;
    private LinearLayout mLayoutChannel0ItemSplitLy;
    private View mMainView;

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
        initView(view);
        initListener(view);
    }

    private void initVars(View view) {

        mchannel0BootomView = view.findViewById(R.id.FR_channel_0_btn_bottom);

        mchannel1_99BootomView = view.findViewById(R.id.FR_channel_1_99_btn_bottom);

        mchannel100BootomView = view.findViewById(R.id.FR_channel_100_btn_bottom);

        initChannel0Vars(view);

    }

    private void initChannel0Vars(View view) {

        mLayoutChannel0 = view.findViewById(R.id.FR_channel_0_ly);

        mLayoutChannel0Title = mLayoutChannel0.findViewById(R.id.C0L_title_tv);

        mLayoutChannel0Image = mLayoutChannel0.findViewById(R.id.C0L_img);

        mLayoutChannel0Item = mLayoutChannel0.findViewById(R.id.C0L_item);

        mLayoutChannel0ItemTitleTv = mLayoutChannel0Item.findViewById(R.id.IP_title);

        mLayoutChannel0ItemSubTitleTv = mLayoutChannel0Item.findViewById(R.id.IP_sub_title);

        mLayoutChannel0ItemSplitLy = mLayoutChannel0Item.findViewById(R.id.IP_split_ly);

    }

    private void initView(View view) {

        initChannel0View(view);
    }

    private void initChannel0View(View view) {

        RecipeData recipeChannel0 = mRecipeResponse.getRecipeData().get(0);

        mLayoutChannel0Title.setText(recipeChannel0.getName());

        ImageLoader.getInstance().displayImage(mRecipeResponse.getProductData().getFileUrl().get(0), mLayoutChannel0Image);

        mLayoutChannel0ItemTitleTv.setText(recipeChannel0.getChannelSplits().get(0).getName());

        if (recipeChannel0.getChannelSplits().get(0).getMaterialInformation() != null) {

            mLayoutChannel0ItemSubTitleTv.setText(recipeChannel0.getChannelSplits().get(0).getMaterialInformation().getCatalogID());

        }

        if (recipeChannel0.getChannelSplits().get(0).getBaseSplits() != null) {

            for (BaseSplits baseSplits: recipeChannel0.getChannelSplits().get(0).getBaseSplits()){

                LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                View itemView = layoutInflater.inflate((R.layout.item_split), (ViewGroup) mMainView, false);

                ((TextView)itemView.findViewById(R.id.IS_tv1)).setText(baseSplits.getPropertyName());

//                ((TextView)itemView.findViewById(R.id.IS_tv1_nmbr)).setText(baseSplits.getFValue());

                mLayoutChannel0ItemSplitLy.addView(itemView);

            }

        }


    }

    private void initListener(View view) {

        view.findViewById(R.id.FR_channel_0_btn).setOnClickListener(this);

        view.findViewById(R.id.FR_channel_1_99_btn).setOnClickListener(this);

        view.findViewById(R.id.FR_channel_100_btn).setOnClickListener(this);
    }

    private void initChanne1_1_99_View() {
//
//        if (mCoreTagsResponse.getName() != null) {
//
//            ((TextView) mView.findViewById(R.id.FSP_title)).setText(mCoreTagsResponse.getName());
//
//        }
//
//        if (mCoreTagsResponse.getQuestion() != null){
//
//            ViewTagsHelper.addTitle(getActivity() , mCoreTagsResponse.getQuestion() , mMainView);
//
//            ViewTagsHelper.addRv(getContext() , mCoreTagsResponse , mMainView , this, false);
//
//        }else {
//
//            if (mCoreTagsResponse.getOptions() != null){
//
//                for (Object coreExpertiseResponse: mCoreTagsResponse.getOptions()){
//
//                    ViewTagsHelper.addTitle(getContext() , ((CoreTagsResponse)coreExpertiseResponse).getQuestion() , mMainView);
//
//                    ViewTagsHelper.addRv(getContext()
//                            , ((CoreTagsResponse) coreExpertiseResponse), mMainView , this, false);
//                }
//            }
//        }

    }

    @Override
    public void onClick(View view) {

    }

    public interface OnRecipeFragmentListener {

    }
}
