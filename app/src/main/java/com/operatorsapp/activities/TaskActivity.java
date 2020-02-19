package com.operatorsapp.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.common.task.TaskProgress;
import com.operatorsapp.R;
import com.operatorsapp.fragments.TaskBoardFragment;
import com.operatorsapp.fragments.TaskDetailsFragment;
import com.operatorsapp.managers.CroutonCreator;

import java.util.List;

public class TaskActivity extends AppCompatActivity implements TaskBoardFragment.TaskBoardFragmentListener {

    private CroutonCreator mCroutonCreator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_activity);
        mCroutonCreator = new CroutonCreator();
        showTaskBoardFragment();

    }

    private void showTaskBoardFragment() {
        try {
            TaskBoardFragment taskBoardFragment = TaskBoardFragment.newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.TA_container, taskBoardFragment).addToBackStack(TaskBoardFragment.class.getSimpleName()).commit();
        } catch (Exception e) {
        }
    }

    private void showTaskDetailsFragment(TaskProgress taskProgress) {
        try {
            TaskDetailsFragment taskDetailsFragment = TaskDetailsFragment.newInstance(taskProgress);
            getSupportFragmentManager().beginTransaction().add(R.id.TA_container, taskDetailsFragment).addToBackStack(TaskDetailsFragment.class.getSimpleName()).commit();
        } catch (Exception e) {
        }
    }

    @Override
    public void onBackPressed() {
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        if (fragmentList.get(fragmentList.size() - 1) instanceof TaskBoardFragment){
            finish();
        }
        super.onBackPressed();
    }

    @Override
    public void onTaskClicked(TaskProgress taskProgress) {
        showTaskDetailsFragment(taskProgress);
    }

    @Override
    public void onCreateTask() {
        showTaskDetailsFragment(null);
    }
}
