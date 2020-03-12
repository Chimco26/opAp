package com.operatorsapp.activities;

import android.os.Build;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.view.View;

import com.operatorsapp.R;
import com.operatorsapp.fragments.QCDetailsFragment;
import com.operatorsapp.fragments.QCTestOrderFragment;
import com.operatorsapp.fragments.interfaces.OnCroutonRequestListener;
import com.operatorsapp.interfaces.CroutonRootProvider;
import com.operatorsapp.managers.CroutonCreator;
import com.operatorsapp.utils.KeyboardUtils;

import java.util.List;

public class QCActivity extends AppCompatActivity implements OnCroutonRequestListener,
        CroutonCreator.CroutonListener,
        QCTestOrderFragment.QCTestOrderFragmentListener,
        QCDetailsFragment.QCDetailsFragmentListener {

    private CroutonCreator mCroutonCreator;
    private QCDetailsFragment mQcDetailsFragment;
    private QCTestOrderFragment mQcTestOrderFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qc);
        this.configureToolbar();
        mCroutonCreator = new CroutonCreator();
        showQCTestOrderFragment();
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
                setResult(RESULT_CANCELED, getIntent());
                finish();            }
        });
        // Sets the Toolbar
    }

    private void showQCTestOrderFragment() {

        try {
            mQcTestOrderFragment = QCTestOrderFragment.newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.AQC_container, mQcTestOrderFragment).addToBackStack(QCTestOrderFragment.TAG).commit();
        } catch (Exception e) {
            //todo
        }
    }
    private void showQCDetailsFragment(int testId) {

        try {
            mQcDetailsFragment = QCDetailsFragment.newInstance(testId);
            getSupportFragmentManager().beginTransaction().add(R.id.AQC_container, mQcDetailsFragment).addToBackStack(QCDetailsFragment.TAG).commit();
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
            setResult(RESULT_CANCELED, getIntent());
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
        showQCDetailsFragment(testId);
    }

    @Override
    public void onSaved() {

    }
}
