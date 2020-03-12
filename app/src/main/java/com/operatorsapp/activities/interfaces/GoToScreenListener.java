package com.operatorsapp.activities.interfaces;

import androidx.fragment.app.Fragment;

import com.operators.infra.Machine;

import java.util.ArrayList;

public interface GoToScreenListener {
    // responsibility of fragment transaction is of activity, so every fragment which wants to go to another one pass
    // wanted fragment instance to activity by using callback and the activity makes the switch in the way he wants.
    void goToFragment(Fragment fragment, boolean isCentralContainer, boolean addToBackStack);

    void goToDashboardActivity(int machine, ArrayList<Machine> machines);

    void isTryToLogin(boolean isTryToLogin);
}
