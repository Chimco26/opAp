package com.operatorsapp.activities;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.operatorsapp.R;
import com.operatorsapp.fragments.QCTestOrderFragment;

public class JobOrMaterialFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = JobOrMaterialFragment.class.getSimpleName();

    private JobOrMaterialFragmentListener mListener;

    public JobOrMaterialFragment() {
    }

    public static JobOrMaterialFragment newInstance() {
        return new JobOrMaterialFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        if (context instanceof JobOrMaterialFragment.JobOrMaterialFragmentListener) {
            mListener = (JobOrMaterialFragment.JobOrMaterialFragmentListener) context;
        } else {
            Log.d(TAG, "onAttach: must override QCTestOrderFragmentListener");
        }
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        mListener = null;
        super.onDetach();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_job_or_material, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }

    private void initViews(View view) {
        view.findViewById(R.id.FJOM_job_btn).setOnClickListener(this);
        view.findViewById(R.id.FJOM_material_btn).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (mListener != null){
            mListener.onJobOrMaterialSelected(v.getId() == R.id.FJOM_job_btn);
        }
    }

    public interface JobOrMaterialFragmentListener {
        void onJobOrMaterialSelected(boolean isJob);
    }
}