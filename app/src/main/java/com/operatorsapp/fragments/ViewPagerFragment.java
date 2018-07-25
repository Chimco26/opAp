package com.operatorsapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.operators.errorobject.ErrorObjectInterface;
import com.operators.machinedatainfra.models.Widget;
import com.operators.machinestatusinfra.models.MachineStatus;
import com.operatorsapp.R;
import com.operatorsapp.adapters.ScreenSlidePagerAdapter;
import com.operatorsapp.interfaces.DashboardUICallbackListener;
import com.operatorsapp.interfaces.OnActivityCallbackRegistered;
import com.operatorsapp.utils.broadcast.SendBroadcast;
import com.ravtech.david.sqlcore.Event;

import java.util.ArrayList;

public class ViewPagerFragment extends Fragment implements DashboardUICallbackListener {

    private ViewPager mPager;
    private ScreenSlidePagerAdapter mPagerAdapter;
    private OnViewPagerListener mListener;
    private ArrayList<Fragment> mFragmentList = new ArrayList<>();
    private SwipeRefreshLayout mShiftLogSwipeRefresh;
    private OnActivityCallbackRegistered mOnActivityCallbackRegistered;

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

        try {
            mOnActivityCallbackRegistered = (OnActivityCallbackRegistered) context;
            mOnActivityCallbackRegistered.onFragmentAttached(this);
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling fragment must implement interface");
        }
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mOnActivityCallbackRegistered.onFragmentDetached(this);
        mOnActivityCallbackRegistered = null;

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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mShiftLogSwipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.fragment_viewpager_swipe_refresh);
        mShiftLogSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                SendBroadcast.refreshPolling(getActivity());
            }
        });
    }

    public void addFragment(Fragment fragment) {

        if (mPagerAdapter != null) {

            mFragmentList.add(fragment);

            mPagerAdapter.notifyDataSetChanged();

            updateDirection();
        }

    }

    public void removeFragment(Fragment fragment) {

        if (mPagerAdapter != null) {

            mFragmentList.remove(fragment);

            mPagerAdapter.notifyDataSetChanged();

        }

    }

    private void updateDirection(){

        if (getActivity() != null && getActivity().getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_RTL){

            mPager.setCurrentItem(mPagerAdapter.getCount() - 1);
        }

    }

    @Override
    public void onDeviceStatusChanged(MachineStatus machineStatus) {

    }

    @Override
    public void onMachineDataReceived(ArrayList<Widget> widgetList) {
        if (mShiftLogSwipeRefresh.isRefreshing()){
            mShiftLogSwipeRefresh.setRefreshing(false);
        }
    }

    @Override
    public void onShiftLogDataReceived(ArrayList<Event> events) {
        if (mShiftLogSwipeRefresh.isRefreshing()){
            mShiftLogSwipeRefresh.setRefreshing(false);
        }
    }

    @Override
    public void onTimerChanged(String timeToEndInHours) {

    }

    @Override
    public void onDataFailure(ErrorObjectInterface reason, CallType callType) {
        if (mShiftLogSwipeRefresh.isRefreshing()){
            mShiftLogSwipeRefresh.setRefreshing(false);
        }
    }

    @Override
    public void onShiftForMachineEnded() {

    }

    @Override
    public void onApproveFirstItemEnabledChanged(boolean enabled) {

    }

    public interface OnViewPagerListener {


        void onViewPagerCreated();

    }
}
