package com.operatorsapp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;

import com.operatorsapp.R;
import com.operatorsapp.fragments.QCDetailsFragment;
import com.operatorsapp.fragments.QCTestOrderFragment;
import com.operatorsapp.fragments.interfaces.OnCroutonRequestListener;
import com.operatorsapp.managers.CroutonCreator;

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
        if (getSupportFragmentManager().getFragments().size() < 2){
            finish();
        }
        super.onBackPressed();
    }

    @Override
    public void onShowCroutonRequest(String croutonMessage, int croutonDurationInMilliseconds, int viewGroup, CroutonCreator.CroutonType croutonType) {

    }

    @Override
    public void onShowCroutonRequest(SpannableStringBuilder croutonMessage, int croutonDurationInMilliseconds, int viewGroup, CroutonCreator.CroutonType croutonType) {
        if (mCroutonCreator != null) {
            mCroutonCreator.showCrouton(this, String.valueOf(croutonMessage), croutonDurationInMilliseconds, R.id.AQC_main_ly, croutonType, this);

        }
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
