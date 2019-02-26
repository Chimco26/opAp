package com.operators.alldashboarddatacore.interfaces;

import com.operators.errorobject.ErrorObjectInterface;
import com.example.common.actualBarExtraResponse.ActualBarExtraResponse;

public interface ActualBarExtraDetailsUICallback {

    void onActualBarExtraDetailsSucceeded(ActualBarExtraResponse actualBarExtraResponse);

    void onActualBarExtraDetailsFailed(ErrorObjectInterface reason);
}