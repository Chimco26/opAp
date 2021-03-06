package com.operatorsapp.utils;

import com.operatorsapp.managers.PersistenceManager;

import org.acra.ACRA;

/**
 * Created by david vardi
 */

public class SendReportUtil {

    public static final String SESSION_ID = "SESSION_ID";

    public static final String IS_APP_CRASH = "IS_APP_CRASH";

    public static final String MACHINE_ID  = "MACHINE_ID";

    @SuppressWarnings("unused")
    public static final String CURRENT_STORAGE_SIZE  = "CURRENT_STORAGE_SIZE ";

    public static final String METHOD_NAME = "METHOD_NAME";

    public static void sendAcraExeption(Throwable throwable, String method) {

      ACRA.getErrorReporter().putCustomData(IS_APP_CRASH, "false");

        ACRA.getErrorReporter().putCustomData(METHOD_NAME, method);

        ACRA.getErrorReporter().putCustomData(MACHINE_ID  , String.valueOf(PersistenceManager.getInstance().getMachineId()));

        ACRA.getErrorReporter().putCustomData(SESSION_ID, String.valueOf(PersistenceManager.getInstance().getSessionId()));

        ACRA.getErrorReporter().handleException(throwable);

    }
}
