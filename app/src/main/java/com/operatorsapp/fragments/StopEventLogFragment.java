package com.operatorsapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.operatorsapp.R;
import com.operatorsapp.utils.GoogleAnalyticsHelper;

public class StopEventLogFragment extends Fragment {

    private static final String TAG = StopEventLogFragment.class.getSimpleName();
    private View mMainView;
    private OnStopEventLogFragmentListener mListener;

    public static StopEventLogFragment newInstance() {
        StopEventLogFragment stopEventLogFragment = new StopEventLogFragment();
        Bundle bundle = new Bundle();
        stopEventLogFragment.setArguments(bundle);
        return stopEventLogFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

        // Analytics
        new GoogleAnalyticsHelper().trackScreen(getActivity(), TAG);

    }

    @Override
    public void onAttach(Context context) {
        if (context instanceof StopEventLogFragment.OnStopEventLogFragmentListener) {
            mListener = (StopEventLogFragment.OnStopEventLogFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnStopEventLogFragmentListener");
        }
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.frament_stop_event_log, container, false);

        return mMainView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initVars(view);
        initView();
        initListener(view);
    }

    private void initListener(View view) {

    }

    private void initView() {


    }

    private void initVars(View view) {

    }

    public interface OnStopEventLogFragmentListener{

    }

}
