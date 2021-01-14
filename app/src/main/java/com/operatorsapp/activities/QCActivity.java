package com.operatorsapp.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.ProgressBar;

import com.operators.reportrejectnetworkbridge.server.response.activateJob.PendingJob;
import com.operatorsapp.R;
import com.operatorsapp.application.OperatorApplication;
import com.operatorsapp.dialogs.Alert2BtnDialog;
import com.operatorsapp.fragments.JobListFragment;
import com.operatorsapp.fragments.QCDetailsFragment;
import com.operatorsapp.fragments.QCTestOrderFragment;
import com.operatorsapp.fragments.interfaces.OnCroutonRequestListener;
import com.operatorsapp.interfaces.CroutonRootProvider;
import com.operatorsapp.managers.CroutonCreator;
import com.operatorsapp.server.NetworkManager;
import com.operatorsapp.server.responses.JobListForMaterialResponse;
import com.operatorsapp.server.responses.JobListForTestResponse;
import com.operatorsapp.utils.KeyboardUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QCActivity extends AppCompatActivity implements OnCroutonRequestListener,
        CroutonCreator.CroutonListener, JobListFragment.JobListFragmentListener,
        QCTestOrderFragment.QCTestOrderFragmentListener,
        QCDetailsFragment.QCDetailsFragmentListener,
        JobOrMaterialFragment.JobOrMaterialFragmentListener,
        Thread.UncaughtExceptionHandler{

    public static final String QC_IS_FROM_SELECT_MACHINE_SCREEN = "QC_IS_FROM_SELECT_MACHINE_SCREEN";
    public static final String QC_TEST_ID = "QC_TEST_ID";
    public static final String QC_EDIT_MODE = "QC_EDIT_MODE";

    private CroutonCreator mCroutonCreator;
    private QCDetailsFragment mQcDetailsFragment;
    private QCTestOrderFragment mQcTestOrderFragment;
    private boolean isOnlyQC;
    private boolean isOnlyQCMaterial = false;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qc);
        this.configureToolbar();
        mCroutonCreator = new CroutonCreator();
        mProgressBar = findViewById(R.id.AQA_progress);
        isOnlyQC = getIntent().getBooleanExtra(QC_IS_FROM_SELECT_MACHINE_SCREEN, false);
        if (isOnlyQC){
            openJobOrMaterialDialog();
        }else {
            int testId = getIntent().getIntExtra(QC_TEST_ID, 0);
            if (testId != 0) {
                showQCDetailsFragment(testId, getIntent().getBooleanExtra(QC_EDIT_MODE, true));
            } else {
                showQCTestOrderFragment(0);
            }
        }
    }

    private void openJobOrMaterialDialog() {

        JobOrMaterialFragment jobOrMaterialFragment = JobOrMaterialFragment.newInstance();
        getSupportFragmentManager().beginTransaction().add(R.id.AQC_container, jobOrMaterialFragment).addToBackStack(JobOrMaterialFragment.class.getSimpleName()).commit();



//        Alert2BtnDialog dialog = new Alert2BtnDialog(this, new Alert2BtnDialog.Alert2BtnDialogListener() {
//            @Override
//            public void onClickPositiveBtn() {
//                isOnlyQCMaterial = true;
//                chooseMaterial();
//            }
//
//            @Override
//            public void onClickNegativeBtn() {
//                chooseJob();
//            }
//        }, getString(R.string.run_test_for), getString(R.string.material), getString(R.string.job));
//
//        dialog.showAlert2BtnDialog(true).show();
    }

    private void chooseJob() {
        mProgressBar.setVisibility(View.VISIBLE);
        NetworkManager.getInstance().getJobsForTest(new Callback<JobListForTestResponse>() {
            @Override
            public void onResponse(Call<JobListForTestResponse> call, Response<JobListForTestResponse> response) {

                if (response.body() != null && response.body().isNoError()){
                    JobListFragment jobListFragment = JobListFragment.newInstance(response.body());
                    getSupportFragmentManager().beginTransaction().add(R.id.AQC_container, jobListFragment).addToBackStack(JobListFragment.class.getSimpleName()).commit();
                }
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<JobListForTestResponse> call, Throwable t) {
                mProgressBar.setVisibility(View.GONE);
            }
        });
    }

    private void chooseMaterial() {
        mProgressBar.setVisibility(View.VISIBLE);
        NetworkManager.getInstance().getMaterialsForTestOrder(new Callback<JobListForMaterialResponse>() {
            @Override
            public void onResponse(Call<JobListForMaterialResponse> call, Response<JobListForMaterialResponse> response) {

                if (response.body() != null && response.body().isNoError()){
                    JobListFragment jobListFragment = JobListFragment.newInstance(response.body(), 0);
                    getSupportFragmentManager().beginTransaction().add(R.id.AQC_container, jobListFragment).addToBackStack(JobListFragment.class.getSimpleName()).commit();
                }
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<JobListForMaterialResponse> call, Throwable t) {
                mProgressBar.setVisibility(View.GONE);
            }
        });
    }

    private void configureToolbar(){
        // Get the toolbar view inside the activity layout
        Toolbar toolbar = findViewById(R.id.AQC_toolbar);
        toolbar.setTitle(R.string.order_test);
        toolbar.setNavigationIcon(R.drawable.arrow_back);
        setSupportActionBar(toolbar);
        if (getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                toolbar.getNavigationIcon().setAutoMirrored(true);
            }
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
//                setResult(RESULT_CANCELED, getIntent());
//                finish();
            }
        });
        // Sets the Toolbar
    }

    private void showQCTestOrderFragment(int idForTests) {

        try {
            mQcTestOrderFragment = QCTestOrderFragment.newInstance(idForTests, isOnlyQCMaterial);
            getSupportFragmentManager().beginTransaction().replace(R.id.AQC_container, mQcTestOrderFragment, mQcTestOrderFragment.getTag()).addToBackStack(QCTestOrderFragment.TAG).commit();
        } catch (Exception e) {
            //todo
        }
    }

    private void showQCDetailsFragment(int testId, boolean editMode) {
        try {
            mQcDetailsFragment = QCDetailsFragment.newInstance(testId, editMode);
            getSupportFragmentManager().beginTransaction().replace(R.id.AQC_container, mQcDetailsFragment, mQcDetailsFragment.getTag()).addToBackStack(QCDetailsFragment.TAG).commit();
        } catch (Exception e) {
            //todo
        }
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();

//        if (SingleLineKeyboard.isKeyBoardOpen) {
//            if (mQcDetailsFragment != null) {
//                mQcDetailsFragment.onCloseKeyboard();
//            }
//            SingleLineKeyboard.isKeyBoardOpen = false;
//            return;
//        }
        if (count <= 1) {
            if (isOnlyQC){
                if (getSupportFragmentManager().getFragments().size() > 1){
                    getSupportFragmentManager().popBackStack();
                }else {
                    finish();
//                    super.onBackPressed();
//                    openJobOrMaterialDialog();
                }
            }else {
                setResult(RESULT_CANCELED, getIntent());
                finish();
            }
            //additional code
        } else {
            getSupportFragmentManager().popBackStack();
        }

    }

    @Override
    public void onShowCroutonRequest(String croutonMessage, int croutonDurationInMilliseconds, int viewGroup, CroutonCreator.CroutonType croutonType) {

    }

    @Override
    public void onShowCroutonRequest(SpannableStringBuilder croutonMessage, int croutonDurationInMilliseconds, int viewGroup, CroutonCreator.CroutonType croutonType) {
        if (mCroutonCreator != null) {
            mCroutonCreator.showCrouton(this, String.valueOf(croutonMessage), croutonDurationInMilliseconds, getCroutonRoot(), croutonType, this);

        }
    }

    public int getCroutonRoot() {
        Fragment currentFragment = getVisibleFragment();
        if (currentFragment != null && currentFragment instanceof CroutonRootProvider) {
            return ((CroutonRootProvider) currentFragment).getCroutonRoot();
        }
        return R.id.parent_layouts;
    }

    public Fragment getVisibleFragment() {
        Fragment f = null;
        FragmentManager fragmentManager = getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment != null && fragment.isVisible()) {
                    f = fragment;
                }
            }
        }
        return f;
    }

    @Override
    public void onHideConnectivityCroutonRequest() {

    }

    @Override
    public void onCroutonDismiss() {

    }

    @Override
    public void onSent(int testId) {
        KeyboardUtils.closeKeyboard(this);
        showQCDetailsFragment(testId, true);
    }

    @Override
    public void onSaved() {

    }

    @Override
    public void onPendingJobSelected(PendingJob pendingJob) {
//        getSupportFragmentManager().popBackStack();
        showQCTestOrderFragment(pendingJob.getID());
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("crash", true);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(OperatorApplication.getAppContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager mgr = (AlarmManager) OperatorApplication.getAppContext().getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, pendingIntent);
        finish();
        System.exit(2);
    }

    @Override
    public void onJobOrMaterialSelected(boolean isJob) {
        if (isJob){
            chooseJob();
        }else {
            isOnlyQCMaterial = true;
            chooseMaterial();
        }
    }
}
