package com.operatorsapp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.operatorsapp.R;
import com.operatorsapp.fragments.QCDetailsFragment;
import com.operatorsapp.fragments.QCTestOrderFragment;

public class QCActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qc);
    }

    private void showQCTestOrderFragment() {

        try {
            QCTestOrderFragment qcTestOrderFragment = QCTestOrderFragment.newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.AQC_container, qcTestOrderFragment).addToBackStack(QCTestOrderFragment.TAG).commit();
        } catch (Exception e) {
            //todo
        }
    }
    private void showJobListFragment() {

        try {
            QCDetailsFragment qcDetailsFragment = QCDetailsFragment.newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.AQC_container, qcDetailsFragment).addToBackStack(QCDetailsFragment.TAG).commit();
        } catch (Exception e) {
            //todo
        }
    }
}
