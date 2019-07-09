package com.operatorsapp.server.pulling.interfaces;

import com.example.common.callback.ErrorObjectInterface;
import com.example.common.permissions.PermissionResponse;

public interface MachinePermissionCallback {
    void onMachinePermissionCallbackSucceeded(PermissionResponse permissionResponse);

    void onMachinePermissionCallbackFailed(ErrorObjectInterface reason);
}
