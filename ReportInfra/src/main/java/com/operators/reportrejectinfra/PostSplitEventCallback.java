package com.operators.reportrejectinfra;

import com.example.common.StandardResponse;

/**
 * Created by alex on 05/07/2018.
 */

public interface PostSplitEventCallback {


    void onPostSplitEventSuccess(StandardResponse response);

    void onPostSplitEventFailed(StandardResponse reason);
}
