package com.operatorsapp.server.callback;

import com.operators.errorobject.ErrorObjectInterface;
import com.operators.reportrejectnetworkbridge.server.response.ErrorResponseNewVersion;

/**
 * Created by alex on 30/07/2018.
 */

public interface PostProductionModeCallback {


    void onPostProductionModeSuccess(ErrorResponseNewVersion response);

    void onPostProductionModeFailed(ErrorObjectInterface reason);
}
