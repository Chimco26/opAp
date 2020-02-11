package com.operatorsapp.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.common.StandardResponse;
import com.example.common.callback.CreateTaskCallback;
import com.example.common.task.Task;
import com.operatorsapp.R;
import com.operatorsapp.fragments.TaskBoardFragment;
import com.operatorsapp.managers.CroutonCreator;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.server.NetworkManager;
import com.operatorsapp.utils.SimpleRequests;

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
}
