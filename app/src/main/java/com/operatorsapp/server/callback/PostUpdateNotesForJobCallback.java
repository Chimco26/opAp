package com.operatorsapp.server.callback;

import com.example.common.callback.ErrorObjectInterface;
import com.operators.reportrejectnetworkbridge.server.response.ResponseStatus;

/**
 * Created by alex on 01/08/2018.
 */

public interface PostUpdateNotesForJobCallback {
    void onUpdateNotesSuccess(ResponseStatus responseNewVersion);
    void onUpdateNotesFailed(ErrorObjectInterface reason);
}
