package com.operators.shiftloginfra;

import com.operators.errorobject.ErrorObjectInterface;
import com.example.common.actualBarExtraResponse.ActualBarExtraResponse;

public interface ActualBarExtraDetailsCallback<T> {
    void onActualBarExtraDetailsSucceeded(ActualBarExtraResponse actualBarExtraResponse);

    void onActualBarExtraDetailsFailed(ErrorObjectInterface reason);

}
