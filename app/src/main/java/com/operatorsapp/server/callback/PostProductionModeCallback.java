package com.operatorsapp.server.callback;

import com.operators.errorobject.ErrorObjectInterface;

/**
 * Created by alex on 30/07/2018.
 */

public interface PostProductionModeCallback {


    void onPostProductionModeSuccess(Object response);

    void onPostProductionModeFailed(ErrorObjectInterface reason);
}
