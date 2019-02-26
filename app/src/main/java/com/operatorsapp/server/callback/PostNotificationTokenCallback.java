package com.operatorsapp.server.callback;

import com.operators.errorobject.ErrorObjectInterface;
import com.operators.reportrejectnetworkbridge.server.response.ResponseStatus;

/**
 * Created by alex on 07/10/2018.
 */

public interface PostNotificationTokenCallback {
    void onPostTokenSuccess(ResponseStatus responseNewVersion);
    void onPostTokenFailed(ErrorObjectInterface reason);
}
