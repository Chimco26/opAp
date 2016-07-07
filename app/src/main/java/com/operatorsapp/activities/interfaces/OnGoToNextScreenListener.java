package com.operatorsapp.activities.interfaces;

import android.support.v4.app.Fragment;

import com.operators.infra.Machine;

public interface OnGoToNextScreenListener {
    // responsibility of fragment transaction is of activity, so every fragment which wants to go to another one pass
    // wanted fragment instance to activity by using callback and the activity makes the switch in the way he wants.
    void goToSelectMachineFrafment(Fragment fragment, boolean addToBackStack);

    void goToDashboardActivity(int machine);
}
