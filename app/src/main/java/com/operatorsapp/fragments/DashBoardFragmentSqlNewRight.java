package com.operatorsapp.fragments;

import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.operators.machinedatainfra.models.Widget;
import com.operatorsapp.R;
import com.operatorsapp.activities.interfaces.GoToScreenListener;
import com.operatorsapp.adapters.WidgetAdapter;
import com.operatorsapp.dialogs.DialogFragment;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.managers.ProgressDialogManager;
import com.operatorsapp.utils.SoftKeyboardUtil;
import com.operatorsapp.view.GridSpacingItemDecoration;
import com.zemingo.logrecorder.ZLogger;

import java.util.List;

import static org.acra.ACRA.LOG_TAG;

/**
 * Created by nathanb on 4/29/2018.
 */

public class DashBoardFragmentSqlNewRight extends Fragment {

    private static final int ANIM_DURATION_MILLIS = 200;
    private LinearLayout mShiftLogLayout;
    private ViewGroup.LayoutParams mShiftLogParams;
    private int mOpenWidth;
    private int mCloseWidth;
    private int mWidgetsLayoutWidth;
    private int mTollBarsHeight;
    private int mRecyclersHeight;
    private boolean mIsOpen;
    private boolean mIsNewShiftLogs;
    private int mMiddleWidth;
    private View mStatusLayout;
    private View mProductNameTextView;
    private TextView mJobIdTextView;
    private TextView mShiftIdTextView;
    private TextView mTimerTextView;
    private TextView mMachineIdStatusBarTextView;
    private TextView mMachineStatusStatusBarTextView;
    private ImageView mStatusIndicatorImageView;
    private DialogFragment mDialogFragment;
    private ViewGroup.MarginLayoutParams mWidgetsParams;
    private View mConfigLayout;
    private View mConfigView;
    private TextView mConfigTextView;
    private ViewGroup mWidgetsLayout;
    private RecyclerView mWidgetRecycler;
    private GridLayoutManager mGridLayoutManager;
    private WidgetAdapter mWidgetAdapter;
    private TextView mNoNotificationsText;
    private LinearLayout mNoDataView;
    private TextView mLoadingDataText;
    private LinearLayout mLoadingDataView;
    private List<Widget> mWidgets;
    private GoToScreenListener mOnGoToScreenListener;

    public static DashBoardFragmentSqlNewRight newInstance() {
        return new DashBoardFragmentSqlNewRight();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ProgressDialogManager.show(getActivity());
        View inflate = inflater.inflate(R.layout.fragment_dashboard_new_right, container, false);
        SoftKeyboardUtil.hideKeyboard(this);
        return inflate;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ZLogger.d(LOG_TAG, "onViewCreated(), start ");
        super.onViewCreated(view, savedInstanceState);

        mIsOpen = false;
        mIsNewShiftLogs = PersistenceManager.getInstance().isNewShiftLogs();
        // get screen parameters
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        mOpenWidth = (int) (width * 0.4);
        mCloseWidth = (int) (width * 0.2);
        mWidgetsLayoutWidth = (int) (width * 0.8);
        mTollBarsHeight = (int) (height * 0.25);
        mRecyclersHeight = (int) (height * 0.75);
        mMiddleWidth = (int) (width * 0.3);

        mStatusLayout = view.findViewById(R.id.status_layout);
        ViewGroup.LayoutParams statusBarParams;
        statusBarParams = mStatusLayout.getLayoutParams();
        statusBarParams.height = (int) (mTollBarsHeight * 0.35);
        mStatusLayout.requestLayout();

        mProductNameTextView = view.findViewById(R.id.text_view_product_name_and_id);
        mJobIdTextView = (TextView) view.findViewById(R.id.text_view_job_id);
        mShiftIdTextView = (TextView) view.findViewById(R.id.text_view_shift_id);
        mTimerTextView = (TextView) view.findViewById(R.id.text_view_timer);

        mConfigLayout =  view.findViewById(R.id.pConfig_layout);
        mConfigView =  view.findViewById(R.id.pConfig_view);
        mConfigTextView = (TextView) view.findViewById(R.id.text_view_config);
        mConfigTextView.setSelected(true);

        mWidgetsLayout = (ViewGroup) view.findViewById(R.id.fragment_dashboard_widgets_layout);
        mWidgetsParams = (ViewGroup.MarginLayoutParams) mWidgetsLayout.getLayoutParams();
        mWidgetsParams.setMarginStart(mCloseWidth);
        mWidgetsLayout.setLayoutParams(mWidgetsParams);

        mWidgetRecycler = (RecyclerView) view.findViewById(R.id.fragment_dashboard_widgets);
        mGridLayoutManager = new GridLayoutManager(getActivity(), 3);
        mWidgetRecycler.setLayoutManager(mGridLayoutManager);
        GridSpacingItemDecoration mGridSpacingItemDecoration = new GridSpacingItemDecoration(3, 14, true, 0);
        mWidgetRecycler.addItemDecoration(mGridSpacingItemDecoration);
        mWidgetAdapter = new WidgetAdapter(getActivity(), mWidgets, mOnGoToScreenListener, true, mRecyclersHeight, mWidgetsLayoutWidth);
        mWidgetRecycler.setAdapter(mWidgetAdapter);

        mNoNotificationsText = (TextView) view.findViewById(R.id.fragment_dashboard_no_notif);
        mNoDataView = (LinearLayout) view.findViewById(R.id.fragment_dashboard_no_data);

        mLoadingDataText = (TextView) view.findViewById(R.id.fragment_dashboard_loading_data_shiftlog);
        mLoadingDataView = (LinearLayout) view.findViewById(R.id.fragment_dashboard_loading_data_widgets);
    }
}
