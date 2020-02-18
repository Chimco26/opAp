package com.operatorsapp.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.common.StandardResponse;
import com.example.common.callback.CreateTaskCallback;
import com.example.common.task.Task;
import com.example.common.task.TaskProgress;
import com.operatorsapp.R;
import com.operatorsapp.fragments.TaskBoardFragment;
import com.operatorsapp.fragments.TaskDetailsFragment;
import com.operatorsapp.managers.CroutonCreator;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.server.NetworkManager;
import com.operatorsapp.utils.SimpleRequests;

import java.util.List;

public class TaskActivity extends AppCompatActivity {

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

    private void createTask() {
        PersistenceManager pm = PersistenceManager.getInstance();
        SimpleRequests.createOrUpdateTask(new Task("a"), pm.getSiteUrl(), new CreateTaskCallback() {
            @Override
            public void onCreateTaskCallbackSuccess(StandardResponse response) {

            }

            @Override
            public void onCreateTaskCallbackFailed(StandardResponse reason) {

            }
        }, NetworkManager.getInstance(), pm.getTotalRetries(), pm.getRequestTimeout());
    }

    @Override
    public void onBackPressed() {
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        if (fragmentList.get(fragmentList.size() - 1) instanceof TaskBoardFragment){
            finish();
        }
        super.onBackPressed();
    }
}
