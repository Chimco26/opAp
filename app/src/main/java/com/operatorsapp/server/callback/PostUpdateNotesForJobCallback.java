package com.operatorsapp.server.callback;

import com.example.common.StandardResponse;

/**
 * Created by alex on 01/08/2018.
 */

public interface PostUpdateNotesForJobCallback {
    void onUpdateNotesSuccess(StandardResponse responseNewVersion);
    void onUpdateNotesFailed(StandardResponse reason);
}
