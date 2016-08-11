package com.operators.shiftlognetworkbridge;

import android.util.Log;

import com.operators.errorobject.ErrorObjectInterface;
import com.operators.shiftloginfra.Event;
import com.operators.shiftloginfra.ShiftLogCoreCallback;
import com.operators.shiftloginfra.ShiftLogNetworkBridgeInterface;
import com.operators.shiftlognetworkbridge.interfaces.ShiftLogNetworkManagerInterface;
import com.operators.shiftlognetworkbridge.server.ErrorObject;
import com.operators.shiftlognetworkbridge.server.requests.GetShiftLogRequest;
import com.operators.shiftlognetworkbridge.server.responses.ErrorResponse;
import com.operators.shiftlognetworkbridge.server.responses.ShiftLogResponse;
import com.zemingo.logrecorder.ZLogger;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShiftLogNetworkBridge implements ShiftLogNetworkBridgeInterface {
    private static final String LOG_TAG = ShiftLogNetworkBridge.class.getSimpleName();

    private int retryCount = 0;

    private ShiftLogNetworkManagerInterface mShiftLogNetworkManagerInterface;

    public void inject(ShiftLogNetworkManagerInterface shiftLogNetworkManagerInterface) {
        mShiftLogNetworkManagerInterface = shiftLogNetworkManagerInterface;
    }

    @Override
    public void getShiftLog(String siteUrl, String sessionId, int machineId, String startingFrom, final ShiftLogCoreCallback shiftLogCoreCallback, final int totalRetries, int specificRequestTimeout) {
        GetShiftLogRequest getShiftLogRequest = new GetShiftLogRequest(sessionId, machineId, startingFrom);
        Call<ShiftLogResponse> call = mShiftLogNetworkManagerInterface.getShiftLogRetroFitServiceRequests(siteUrl, specificRequestTimeout, TimeUnit.SECONDS).getMachineShiftLog(getShiftLogRequest);
        call.enqueue(new Callback<ShiftLogResponse>() {

            @Override
            public void onResponse(Call<ShiftLogResponse> call, Response<ShiftLogResponse> response) {
                ArrayList<Event> events = response.body().getShiftLogs();
                if (response.body().getErrorResponse() == null) {
                    if (events != null && events.size() > 0) {
                        ZLogger.d(LOG_TAG, "onRequestSucceed(), " + events.size() + " events");
                        shiftLogCoreCallback.onShiftLogSucceeded(events);
                    } else {
                        ZLogger.d(LOG_TAG, "onRequestFailed(), list null or empty");
                        ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Get_machines_failed, "list null or empty");
                        shiftLogCoreCallback.onShiftLogFailed(errorObject);
                    }
                } else {
                    ZLogger.d(LOG_TAG, "onRequest(), getShiftLog failed");
                    ErrorObject errorObject = errorObjectWithErrorCode(response.body().getErrorResponse());
                    shiftLogCoreCallback.onShiftLogFailed(errorObject);
                }
            }

            @Override
            public void onFailure(Call<ShiftLogResponse> call, Throwable t) {
                if (retryCount++ < totalRetries) {
                    Log.d(LOG_TAG, "Retrying... (" + retryCount + " out of " + totalRetries + ")");
                    call.clone().enqueue(this);
                } else {
                    retryCount = 0;
                    ZLogger.d(LOG_TAG, "onRequestFailed(), " + t.getMessage());
                    ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Retrofit, "General Error");
                    shiftLogCoreCallback.onShiftLogFailed(errorObject);
                }
            }
        });
    }

    private ErrorObject errorObjectWithErrorCode(ErrorResponse errorResponse) {
        ErrorObject.ErrorCode code = toCode(errorResponse.getErrorCode());
        return new ErrorObject(code, errorResponse.getErrorDesc());
    }

    private ErrorObject.ErrorCode toCode(int errorCode) {
        switch (errorCode) {
            case 101:
                return ErrorObject.ErrorCode.Credentials_mismatch;
            case 0:
                return  ErrorObject.ErrorCode.No_data;
        }
        return ErrorObject.ErrorCode.Unknown;
    }
}
