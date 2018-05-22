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
import java.util.prefs.NodeChangeEvent;

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
    private RecyclerView mLayoutChannel0ItemRv;
    private LinearLayout mLayoutChannel0ItemSplitLy;
    private View mMainView;
    private View mLayoutChannel100;
    private TextView mLayoutChannel100Title;
    private RecyclerView mLayoutChannel100Rv;
    private No0ChanneAdapter mNo0ChanneAdapter;
    private LinearLayout mlayoutChannel1_99;

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

        mchannel0BotomView = view.findViewById(R.id.FR_channel_0_btn_bottom);

        mchannel1_99BotomView = view.findViewById(R.id.FR_channel_1_99_btn_bottom);

        mchannel100BotomView = view.findViewById(R.id.FR_channel_100_btn_bottom);

        initChannel0Vars(view);

        initChannel1_99Vars(view);

        initChannel100Vars(view);

    }

    private void initChannel1_99Vars(View view) {

        mlayoutChannel1_99 = view.findViewById(R.id.FR_channel_1_99_ly);
    }

    private void initChannel100Vars(View view) {

        mLayoutChannel100 = view.findViewById(R.id.FR_channel_100_ly);

        mLayoutChannel100Title = mLayoutChannel100.findViewById(R.id.C100_tv);

        mLayoutChannel100Rv = mLayoutChannel100.findViewById(R.id.channel_100_rv);
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

        initChannel100View();

        initChanne1_1_99_View();
    }

    private void initChannel100View() {

       if (mRecipeResponse.getRecipeData() != null &&
               mRecipeResponse.getRecipeData().get(mRecipeResponse.getRecipeData().size() - 1).getChannelNumber() == 100){

        mLayoutChannel100Title.setText(mRecipeResponse.getRecipeData().get(mRecipeResponse.getRecipeData().size() - 1).getName());

        mNo0ChanneAdapter = new No0ChanneAdapter(getActivity(), this,
                (ArrayList<ChannelSplits>) mRecipeResponse.getRecipeData().get(mRecipeResponse.getRecipeData().size() - 1).getChannelSplits(), No0ChanneAdapter.TYPE_CHANNEL_100);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        mLayoutChannel100Rv.setLayoutManager(layoutManager);

        mLayoutChannel100Rv.setAdapter(mNo0ChanneAdapter);
        }
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

            for (BaseSplits baseSplits : recipeChannel0.getChannelSplits().get(0).getBaseSplits()) {

                LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                View itemView = layoutInflater.inflate((R.layout.item_split), (ViewGroup) mMainView, false);

                ((TextView) itemView.findViewById(R.id.IS_tv)).setText(baseSplits.getPropertyName());

                ((TextView) itemView.findViewById(R.id.IS_tv_2)).setText(baseSplits.getFValue() + "");

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

        if (mRecipeResponse.getRecipeData() != null ) {

            List<RecipeData> recipeResponse_1_99 = mRecipeResponse.getRecipeData();

            if ( mRecipeResponse.getRecipeData().get(mRecipeResponse.getRecipeData().size() - 1).getChannelNumber() == 100){

                recipeResponse_1_99.remove( mRecipeResponse.getRecipeData().get(mRecipeResponse.getRecipeData().size() - 1));
            }
            recipeResponse_1_99.remove(0);

            for (RecipeData recipeData: recipeResponse_1_99) {

                ViewTagsHelper.addTitle(getActivity(), recipeData.getName(), mlayoutChannel1_99);

                ViewTagsHelper.addRv(getContext(), recipeData.getChannelSplits(), mlayoutChannel1_99, this);

                ViewTagsHelper.addSeparator(getContext(), mlayoutChannel1_99);
            }
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

                mlayoutChannel1_99.setVisibility(View.GONE);
                mLayoutChannel100.setVisibility(View.GONE);
                mLayoutChannel0.setVisibility(View.VISIBLE);
                break;

            case R.id.FR_channel_1_99_btn:

                mchannel0BotomView.setVisibility(View.INVISIBLE);
                mchannel100BotomView.setVisibility(View.INVISIBLE);
                mchannel1_99BotomView.setVisibility(View.VISIBLE);

                mLayoutChannel0.setVisibility(View.GONE);
                mLayoutChannel100.setVisibility(View.GONE);
                mlayoutChannel1_99.setVisibility(View.VISIBLE);

                break;

            case R.id.FR_channel_100_btn:

                mchannel0BotomView.setVisibility(View.INVISIBLE);
                mchannel1_99BotomView.setVisibility(View.INVISIBLE);
                mchannel100BotomView.setVisibility(View.VISIBLE);

                mLayoutChannel0.setVisibility(View.GONE);
                mlayoutChannel1_99.setVisibility(View.GONE);
                mLayoutChannel100.setVisibility(View.VISIBLE);

                break;

        }

    }

    public interface OnRecipeFragmentListener {

    }
}
