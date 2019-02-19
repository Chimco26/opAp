package com.operators.shiftloginfra;

import com.operators.errorobject.ErrorObjectInterface;
import com.operators.shiftloginfra.model.ActualBarExtraResponse;

public interface ActualBarExtraDetailsCallback<T> {
    void onActualBarExtraDetailsSucceeded(ActualBarExtraResponse actualBarExtraResponse);

    void onActualBarExtraDetailsFailed(ErrorObjectInterface reason);

}
