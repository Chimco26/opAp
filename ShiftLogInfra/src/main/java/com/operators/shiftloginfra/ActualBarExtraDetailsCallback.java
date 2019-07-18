package com.operators.shiftloginfra;

import com.example.common.StandardResponse;
import com.example.common.actualBarExtraResponse.ActualBarExtraResponse;

public interface ActualBarExtraDetailsCallback<T> {
    void onActualBarExtraDetailsSucceeded(ActualBarExtraResponse actualBarExtraResponse);

    void onActualBarExtraDetailsFailed(StandardResponse reason);

}
