package com.operatorsapp.activities;

import android.os.Bundle;
import android.text.SpannableStringBuilder;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.common.task.TaskProgress;
import com.operatorsapp.BuildConfig;
import com.operatorsapp.R;
import com.operatorsapp.fragments.TaskBoardFragment;
import com.operatorsapp.fragments.TaskDetailsFragment;
import com.operatorsapp.fragments.interfaces.OnCroutonRequestListener;
import com.operatorsapp.managers.CroutonCreator;
import com.operatorsapp.utils.MyExceptionHandler;

import java.util.List;

public class TaskActivity extends AppCompatActivity
        implements TaskBoardFragment.TaskBoardFragmentListener,
        OnCroutonRequestListener,
        CroutonCreator.CroutonListener,
        TaskDetailsFragment.TaskDetailsFragmentListener {

    private CroutonCreator mCroutonCreator;
    private TaskBoardFragment taskBoardFragment;
    private TaskDetailsFragment taskDetailsFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!BuildConfig.DEBUG){Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this));}

        setContentView(R.layout.task_activity);
        mCroutonCreator = new CroutonCreator();
        showTaskBoardFragment();

    }

    private void showTaskBoardFragment() {
        if (taskBoardFragment == null) {
            try {
                taskBoardFragment = TaskBoardFragment.newInstance();
                getSupportFragmentManager().beginTransaction().add(R.id.TA_container, taskBoardFragment).addToBackStack(TaskBoardFragment.class.getSimpleName()).commit();
            } catch (Exception ignored) {
            }
        } else {
            taskBoardFragment.refresh();
        }
    }

    private void showTaskDetailsFragment(TaskProgress taskProgress) {
        try {
            taskDetailsFragment = TaskDetailsFragment.newInstance(taskProgress);
            getSupportFragmentManager().beginTransaction().add(R.id.TA_container, taskDetailsFragment).addToBackStack(TaskDetailsFragment.class.getSimpleName()).commit();
        } catch (Exception e) {
        }
    }

    @Override
    public void onBackPressed() {
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        if (fragmentList.get(fragmentList.size() - 1) instanceof TaskBoardFragment) {
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

    @Override
    public void onShowCroutonRequest(String croutonMessage, int croutonDurationInMilliseconds,
                                     int viewGroup, CroutonCreator.CroutonType croutonType) {

        mCroutonCreator.showCrouton(this, String.valueOf(croutonMessage), croutonDurationInMilliseconds, R.id.TA_container, croutonType, this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCroutonCreator != null) {
            mCroutonCreator.cancel();
            mCroutonCreator = null;
        }
    }

    @Override
    public void onShowCroutonRequest(SpannableStringBuilder croutonMessage,
                                     int croutonDurationInMilliseconds, int viewGroup, CroutonCreator.CroutonType croutonType) {

        if (mCroutonCreator != null) {
            mCroutonCreator.showCrouton(this, String.valueOf(croutonMessage), croutonDurationInMilliseconds, R.id.TA_container, croutonType, this);
        }
    }


    @Override
    public void onHideConnectivityCroutonRequest() {

    }

    @Override
    public void onCroutonDismiss() {

    }

    @Override
    public void onUpdate() {
        getSupportFragmentManager().beginTransaction().remove(taskDetailsFragment).commit();
        showTaskBoardFragment();
    }
}
