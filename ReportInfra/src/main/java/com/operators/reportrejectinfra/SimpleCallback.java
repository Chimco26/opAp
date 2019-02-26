package com.operators.reportrejectinfra;

import com.operators.errorobject.ErrorObjectInterface;

public interface SimpleCallback {


    void onRequestSuccess(Object response);

    void onRequestFailed(ErrorObjectInterface reason);
}
