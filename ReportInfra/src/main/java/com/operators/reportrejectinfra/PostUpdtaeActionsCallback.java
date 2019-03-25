package com.operators.reportrejectinfra;

import com.example.common.callback.ErrorObjectInterface;

public interface PostUpdtaeActionsCallback {

    void onPostUpdtaeActionsSuccess(Object response);

    void onPostUpdtaeActionsFailed(ErrorObjectInterface reason);
}
