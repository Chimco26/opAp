package com.operatorsapp.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;

import com.operatorsapp.R;
import com.operatorsapp.fragments.QCDetailsFragment;
import com.operatorsapp.fragments.QCTestOrderFragment;
import com.operatorsapp.fragments.interfaces.OnCroutonRequestListener;
import com.operatorsapp.interfaces.CroutonRootProvider;
import com.operatorsapp.managers.CroutonCreator;

import java.util.List;

public class QCActivity extends AppCompatActivity implements OnCroutonRequestListener,
        CroutonCreator.CroutonListener,
        QCTestOrderFragment.QCTestOrderFragmentListener,
        QCDetailsFragment.QCDetailsFragmentListener {

    private CroutonCreator mCroutonCreator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qc);
        mCroutonCreator = new CroutonCreator();
        showQCTestOrderFragment();
    }

    private void showQCTestOrderFragment() {

        try {
            QCTestOrderFragment qcTestOrderFragment = QCTestOrderFragment.newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.AQC_container, qcTestOrderFragment).addToBackStack(QCTestOrderFragment.TAG).commit();
        } catch (Exception e) {
            //todo
        }
    }
    private void showQCDetailsFragment(int testId) {

        try {
            QCDetailsFragment qcDetailsFragment = QCDetailsFragment.newInstance(testId);
            getSupportFragmentManager().beginTransaction().add(R.id.AQC_container, qcDetailsFragment).addToBackStack(QCDetailsFragment.TAG).commit();
        } catch (Exception e) {
            //todo
        }
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count <= 1) {
            finish();
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
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
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
        showQCDetailsFragment(testId);
    }

    @Override
    public void onSaved() {

    }
}
