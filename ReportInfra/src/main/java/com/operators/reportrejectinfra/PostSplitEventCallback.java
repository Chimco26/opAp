package com.operators.reportrejectinfra;

import com.example.common.callback.ErrorObjectInterface;

/**
 * Created by alex on 05/07/2018.
 */

public interface PostSplitEventCallback {


    void onPostSplitEventSuccess(Object response);

    void onPostSplitEventFailed(ErrorObjectInterface reason);
}
