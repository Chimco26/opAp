package com.operatorsapp.managers;


import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.util.Log;

import com.app.operatorinfra.Operator;

import java.util.ArrayList;
import java.util.List;

public class SignedInOperatorsManager {
    public static final String LOG_TAG = SignedInOperatorsManager.class.getSimpleName();
    private List<Operator> mOperators;

    public SignedInOperatorsManager() {
        mOperators = new ArrayList<>();
    }

    public List<Operator> getOperators() {
        mOperators = PersistenceManager.getInstance().getSignedOperators();
        List<Operator> operators;
        operators = mOperators;
        if (mOperators != null) {
            if (mOperators.size() > 0) {
                operators.add(0, new Operator("000", "Switch operator"));
            }
            else if (mOperators.size() == 0) {
                operators.add(new Operator("000", "Sign in"));
            }
        }
        else {
            operators = new ArrayList<>();
            operators.add(new Operator("", "Sign In"));
        }
        return operators;
    }

    public void addOperator(Operator operator) {
        mOperators = PersistenceManager.getInstance().getSignedOperators();
        if (mOperators == null) {
            mOperators = new ArrayList<>();
            mOperators.add(operator);
        }
        else {
            if (!isOperatorInList(operator)) {
                mOperators.add(operator);
                Log.w(LOG_TAG, "New Operator saved!");
            }
        }
        PersistenceManager.getInstance().saveSignedOperators(mOperators);
    }

    private boolean isOperatorInList(Operator operator) {
        for (int i = 0; i < mOperators.size(); i++) {
            if (mOperators.get(i).getOperatorId().equals(operator.getOperatorId())) {
                Log.w(LOG_TAG, "Operator already exists");
                return true;
            }
        }
        return false;
    }
}
