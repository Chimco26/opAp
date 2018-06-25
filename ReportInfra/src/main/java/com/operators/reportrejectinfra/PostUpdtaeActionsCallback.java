package com.operators.reportrejectinfra;

import com.operators.errorobject.ErrorObjectInterface;

public interface PostUpdtaeActionsCallback {

    void onPostUpdtaeActionsSuccess(Object response);

    void onPostUpdtaeActionsFailed(ErrorObjectInterface reason);
}
