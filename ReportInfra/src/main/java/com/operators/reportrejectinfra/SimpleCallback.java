package com.operators.reportrejectinfra;

import com.example.common.callback.ErrorObjectInterface;

public interface SimpleCallback {


    void onRequestSuccess(Object response);

    void onRequestFailed(ErrorObjectInterface reason);
}
