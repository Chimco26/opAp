package com.operatorsapp.server.callback;

import com.operators.errorobject.ErrorObjectInterface;
import com.operators.reportrejectnetworkbridge.server.response.ErrorResponseNewVersion;

/**
 * Created by alex on 01/08/2018.
 */

public interface PostUpdateNotesForJobCallback {
    void onUpdateNotesSuccess(ErrorResponseNewVersion responseNewVersion);
    void onUpdateNotesFailed(ErrorObjectInterface reason);
}
