package com.operatorsapp.server.callback;

import com.example.common.StandardResponse;

/**
 * Created by alex on 30/07/2018.
 */

public interface PostProductionModeCallback {


    void onPostProductionModeSuccess(StandardResponse response);

    void onPostProductionModeFailed(StandardResponse reason);
}
