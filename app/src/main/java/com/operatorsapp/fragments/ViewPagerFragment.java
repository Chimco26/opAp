package com.operatorsapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.common.Event;
import com.example.common.StandardResponse;
import com.example.common.actualBarExtraResponse.ActualBarExtraResponse;
import com.example.common.machineJoshDataResponse.MachineJoshDataResponse;
import com.example.common.permissions.WidgetInfo;
import com.example.oppapplog.OppAppLogger;
import com.operators.activejobslistformachineinfra.ActiveJobsListForMachine;
import com.operators.machinedatainfra.models.Widget;
import com.operators.machinestatusinfra.models.MachineStatus;
import com.operatorsapp.BuildConfig;
import com.operatorsapp.R;
import com.operatorsapp.activities.interfaces.GoToScreenListener;
import com.operatorsapp.adapters.ScreenSlidePagerAdapter;
import com.operatorsapp.interfaces.DashboardUICallbackListener;
import com.operatorsapp.interfaces.OnActivityCallbackRegistered;
import com.operatorsapp.managers.PersistenceManager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import static com.operatorsapp.fragments.ActionBarAndEventsFragment.MINIMUM_VERSION_FOR_NEW_ACTIVATE_JOB;

public class ViewPagerFragment extends Fragment implements DashboardUICallbackListener {

    public static final String TAG = ViewPagerFragment.class.getSimpleName();
    private ViewPager mPager;
    private ScreenSlidePagerAdapter mPagerAdapter;
    private OnViewPagerListener mListener;
    private WeakReference<ArrayList<Fragment>> mFragmentList;
    //    private SwipeRefreshLayout mSwipeRefresh;
    private OnActivityCallbackRegistered mOnActivityCallbackRegistered;
    private View mCycleWarningView;
    private GoToScreenListener mOnGoToScreenListener;
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
        mFragmentList = new WeakReference<>(new ArrayList<Fragment>());
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
            mOnGoToScreenListener = (GoToScreenListener) getActivity();
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
        mOnGoToScreenListener = null;

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.frament_view_pager, container, false);

        if (getActivity() != null && mFragmentList.get() != null) {

            initCycleAlarmView(view);
            mPager = view.findViewById(R.id.FVP_view_pager);
            mPagerAdapter = new ScreenSlidePagerAdapter(getActivity().getSupportFragmentManager(), mFragmentList.get());
            mPager.setAdapter(mPagerAdapter);

            mListener.onViewPagerCreated();
            mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    if (mFragmentList != null && mFragmentList.get() != null) {
                        if (mFragmentList.get().get(position) instanceof RecipeFragment) {
                            mListener.onRecipeFragmentShown();
                        }
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

        }
        return view;
    }

    public boolean isRecipeShown() {
        try {
            if (mFragmentList != null && mFragmentList.get() != null) {
                return mFragmentList.get().get(mPager.getCurrentItem()) instanceof RecipeFragment;
            }else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    private void initCycleAlarmView(View view) {
        mCycleWarningView = view.findViewById(R.id.FAAE_cycle_alarm_view);
        view.findViewById(R.id.FAAE_cycle_alarm_view).findViewById(R.id.NPAD_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivateJobScreen();
            }
        });
    }

    public void setCycleWarningView(boolean show) {
        if (show && !BuildConfig.FLAVOR.equals(getString(R.string.lenox_flavor_name))) {
            mCycleWarningView.setVisibility(View.VISIBLE);
        } else {
            mCycleWarningView.setVisibility(View.GONE);
        }
    }

    public void openActivateJobScreen() {
        OppAppLogger.d(TAG, "New Job");
        if (PersistenceManager.getInstance().getVersion() >= MINIMUM_VERSION_FOR_NEW_ACTIVATE_JOB) {
            mListener.onJobActionItemClick();
        } else {
            mOnGoToScreenListener.goToFragment(new JobsFragment(), true, true);
        }
    }

    public ViewPager getPager() {
        return mPager;
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

        if (mPagerAdapter != null && mFragmentList != null && mFragmentList.get() != null) {

            mFragmentList.get().add(fragment);

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

    private void updateDirection() {

        if (getActivity() != null && getActivity().getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {

            mPager.setCurrentItem(mPagerAdapter.getCount() - 1);
        }

    }

    @Override
    public void onPermissionForMachinePolling(SparseArray<WidgetInfo> permissionResponse) {
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
    public void onShiftLogDataReceived(ArrayList<Event> events, ActualBarExtraResponse actualBarExtraResponse, MachineJoshDataResponse mMachineJoshDataResponse) {
//        if (mSwipeRefresh.isRefreshing()){
//            mSwipeRefresh.setRefreshing(false);
//        }
    }

    @Override
    public void onTimerChanged(String timeToEndInHours) {

    }

    @Override
    public void onDataFailure(StandardResponse reason, CallType callType) {
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

    public void resetCycleWarningView(boolean wasShow, boolean show) {
        if ((wasShow && mCycleWarningView.getVisibility() == View.GONE)) {
            setCycleWarningView(false);
        } else {
            setCycleWarningView(show);
        }
    }

    public interface OnViewPagerListener {


        void onViewPagerCreated();

        void onJobActionItemClick();

        void onRecipeFragmentShown();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPager.clearOnPageChangeListeners();
    }
}
