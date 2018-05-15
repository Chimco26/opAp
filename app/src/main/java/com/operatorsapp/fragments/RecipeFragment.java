package com.operatorsapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.operatorsapp.R;
import com.operatorsapp.utils.ViewTagsHelper;

public class RecipeFragment extends Fragment {

    private OnRecipeFragmentListener mListener;

    public static RecipeFragment newInstance() {
        RecipeFragment recipeFragment = new RecipeFragment();
        Bundle bundle = new Bundle();
        recipeFragment.setArguments(bundle);
        return recipeFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

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
        final View view = inflater.inflate(R.layout.frament_recipe, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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

    public interface OnRecipeFragmentListener {

    }
}
