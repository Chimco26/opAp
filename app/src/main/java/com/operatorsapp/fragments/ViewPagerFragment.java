package com.operatorsapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.common.Event;
import com.example.common.actualBarExtraResponse.ActualBarExtraResponse;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.operators.activejobslistformachineinfra.ActiveJobsListForMachine;
import com.example.common.callback.ErrorObjectInterface;
import com.operators.machinedatainfra.models.Widget;
import com.operators.machinestatusinfra.models.MachineStatus;
import com.operatorsapp.R;
import com.operatorsapp.adapters.ScreenSlidePagerAdapter;
import com.operatorsapp.application.OperatorApplication;
import com.operatorsapp.interfaces.DashboardUICallbackListener;
import com.operatorsapp.interfaces.OnActivityCallbackRegistered;
import com.operatorsapp.managers.PersistenceManager;

import java.util.ArrayList;

public class ViewPagerFragment extends Fragment implements DashboardUICallbackListener {

    private ViewPager mPager;
    private ScreenSlidePagerAdapter mPagerAdapter;
    private OnViewPagerListener mListener;
    private ArrayList<Fragment> mFragmentList = new ArrayList<>();
//    private SwipeRefreshLayout mSwipeRefresh;
    private OnActivityCallbackRegistered mOnActivityCallbackRegistered;
//    private ImageView mNoteIv;
//    private TextView mNoteTv;
//    private LinearLayout mNoteLy;
//    private String noteStr = "";

    public static ViewPagerFragment newInstance() {
        ViewPagerFragment viewPagerFragment = new ViewPagerFragment();
        Bundle bundle = new Bundle();
        viewPagerFragment.setArguments(bundle);
        return viewPagerFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.frament_view_pager, container, false);

        if (getActivity() != null) {

            mPager = view.findViewById(R.id.FVP_view_pager);
            mPagerAdapter = new ScreenSlidePagerAdapter(getActivity().getSupportFragmentManager(), mFragmentList);
            mPager.setAdapter(mPagerAdapter);

            mListener.onViewPagerCreated();
            mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    // Analytics
                    OperatorApplication application = (OperatorApplication) getActivity().getApplication();
                    Tracker mTracker = application.getDefaultTracker();
                    PersistenceManager pm = PersistenceManager.getInstance();
                    mTracker.setClientId("machine id: " + pm.getMachineId());
                    mTracker.setAppVersion(pm.getVersion() + "");
                    mTracker.setHostname(pm.getSiteName());
                    mTracker.setScreenName("ViewPager new page Selected: " + mFragmentList.get(position).getTag());
                    mTracker.send(new HitBuilders.ScreenViewBuilder().build());

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//
//        mSwipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.fragment_viewpager_swipe_refresh);
//        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                SendBroadcast.refreshPolling(getActivity());
//            }
//        });

//        mPager.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//
//                mSwipeRefresh.setEnabled(false);
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_UP:
//                        mSwipeRefresh.setEnabled(true);
//                        break;
//                }
//                return false;
//            }
//        });
    }

    public void addFragment(Fragment fragment) {

        if (mPagerAdapter != null) {

            mFragmentList.add(fragment);

            mPagerAdapter.notifyDataSetChanged();

            updateDirection();
        }

    }

//    public void removeFragment(Fragment fragment) {
//
//        if (mPagerAdapter != null) {
//
//            mFragmentList.remove(fragment);
//
//            mPagerAdapter.notifyDataSetChanged();
//
//        }
//
//    }

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
//        if (mSwipeRefresh.isRefreshing()){
//            mSwipeRefresh.setRefreshing(false);
//        }
    }

    @Override
    public void onShiftLogDataReceived(ArrayList<Event> events, ActualBarExtraResponse actualBarExtraResponse) {
//        if (mSwipeRefresh.isRefreshing()){
//            mSwipeRefresh.setRefreshing(false);
//        }
    }

    @Override
    public void onTimerChanged(String timeToEndInHours) {

    }

    @Override
    public void onDataFailure(ErrorObjectInterface reason, CallType callType) {
//        if (mSwipeRefresh.isRefreshing()){
//            mSwipeRefresh.setRefreshing(false);
//        }
    }

    @Override
    public void onApproveFirstItemEnabledChanged(boolean enabled) {

    }

    @Override
    public void onActiveJobsListForMachineUICallbackListener(ActiveJobsListForMachine mActiveJobsListForMachine) {
//        if (mSwipeRefresh.isRefreshing()){
//            mSwipeRefresh.setRefreshing(false);
//        }
    }

    public interface OnViewPagerListener {


        void onViewPagerCreated();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPager.clearOnPageChangeListeners();
    }
}
