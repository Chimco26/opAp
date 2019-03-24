package com.operators.alldashboarddatacore.interfaces;

import com.example.common.callback.ErrorObjectInterface;
import com.example.common.actualBarExtraResponse.ActualBarExtraResponse;

public interface ActualBarExtraDetailsUICallback {

    void onActualBarExtraDetailsSucceeded(ActualBarExtraResponse actualBarExtraResponse);

    void onActualBarExtraDetailsFailed(ErrorObjectInterface reason);
}
