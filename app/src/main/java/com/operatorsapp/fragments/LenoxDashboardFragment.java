package com.operatorsapp.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.operatorsapp.BuildConfig;
import com.operatorsapp.R;

public class LenoxDashboardFragment extends Fragment {

    private ImageView mImageView;

    public static LenoxDashboardFragment newInstance() {
        LenoxDashboardFragment lenoxDashboardFragment = new LenoxDashboardFragment();
        Bundle bundle = new Bundle();
        lenoxDashboardFragment.setArguments(bundle);
        return lenoxDashboardFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragement_lenox_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initVars(view);
        initView(view);
    }

    private void initVars(View view) {

        mImageView = view.findViewById(R.id.FLD_image_view);
    }

    private void initView(View view) {
        if (BuildConfig.FLAVOR.equals(getString(R.string.lenox_flavor_name))){
            view.findViewById(R.id.powered_by_leadermess_txt).setVisibility(View.VISIBLE);
        }
    }
}
