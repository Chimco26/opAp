package com.operatorsapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.common.task.TaskProgress;
import com.operatorsapp.R;

public class TaskDetailsFragment extends Fragment {

    private TaskProgress mTask;

    public static TaskDetailsFragment newInstance(TaskProgress taskProgress) {
        TaskDetailsFragment taskDetailsFragment = new TaskDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(TaskProgress.TAG, taskProgress);
        taskDetailsFragment.setArguments(bundle);
        return new TaskDetailsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (getArguments() != null && getArguments().containsKey(TaskProgress.TAG)) {
            mTask = (TaskProgress) getArguments().get(TaskProgress.TAG);
        }
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_task_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initVars(view);
        initView(view);
        initListener(view);
    }

    private void initVars(View view) {

    }

    private void initView(View view) {

    }

    private void initListener(View view) {

    }
}
