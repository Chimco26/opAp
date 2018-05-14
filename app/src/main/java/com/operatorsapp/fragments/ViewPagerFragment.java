package com.operatorsapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.operatorsapp.R;
import com.operatorsapp.adapters.ScreenSlidePagerAdapter;

import java.util.ArrayList;

public class ViewPagerFragment extends Fragment {

    private ViewPager mPager;
    private ScreenSlidePagerAdapter mPagerAdapter;
    private OnViewPagerListener mListener;
    private ArrayList<Fragment> mFragmentList = new ArrayList<>();

    public static ViewPagerFragment newInstance() {
        ViewPagerFragment viewPagerFragment = new ViewPagerFragment();
        Bundle bundle = new Bundle();
        viewPagerFragment.setArguments(bundle);
        return viewPagerFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public void onAttach(Context context) {
        if (context instanceof OnViewPagerListener) {
            mListener = (OnViewPagerListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnViewPagerListener");
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
        final View view = inflater.inflate(R.layout.frament_view_pager, container, false);

        mPager = (ViewPager) view.findViewById(R.id.FVP_view_pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getActivity().getSupportFragmentManager(), mFragmentList);
        mPager.setAdapter(mPagerAdapter);

        mListener.onViewPagerCreated();

        return view;
    }

    public void addFragment(Fragment fragment){

        if (mPagerAdapter != null){

            mFragmentList.add(fragment);

            mPagerAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public interface OnViewPagerListener {

        void onViewPagerCreated();

    }
}
