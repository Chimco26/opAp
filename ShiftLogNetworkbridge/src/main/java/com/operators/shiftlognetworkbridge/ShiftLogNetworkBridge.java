package com.operators.shiftlognetworkbridge;

import android.util.Log;

import com.operators.shiftloginfra.Event;
import com.operators.shiftloginfra.ShiftForMachineResponse;
import com.operators.shiftloginfra.ShiftForMachineCoreCallback;
import com.operators.shiftloginfra.ShiftLogCoreCallback;
import com.operators.shiftloginfra.ShiftLogNetworkBridgeInterface;
import com.operators.shiftlognetworkbridge.interfaces.ShiftLogNetworkManagerInterface;
import com.operators.shiftlognetworkbridge.server.ErrorObject;
import com.operators.shiftlognetworkbridge.server.requests.GetShiftForMachineRequest;
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
    public void getShiftLog(String siteUrl, String sessionId, int machineId, String startingFrom, final ShiftLogCoreCallback<Event> shiftLogCoreCallback, final int totalRetries, int specificRequestTimeout) {
        GetShiftLogRequest getShiftLogRequest = new GetShiftLogRequest(sessionId, machineId, startingFrom);
        Call<ShiftLogResponse> call = mShiftLogNetworkManagerInterface.getShiftLogRetroFitServiceRequests(siteUrl, specificRequestTimeout, TimeUnit.SECONDS).getMachineShiftLog(getShiftLogRequest);
        call.enqueue(new Callback<ShiftLogResponse>() {

            @Override
            public void onResponse(Call<ShiftLogResponse> call, Response<ShiftLogResponse> response) {
                ArrayList<Event> events = response.body().getEvents();
                if (response.body().getErrorResponse() == null) {
                    if (events != null && events.size() > 0) {
                        ZLogger.d(LOG_TAG, "onRequestSucceed(), " + events.size() + " events");
                        shiftLogCoreCallback.onShiftLogSucceeded(events);
                    } else {
                        //todo not error
                        ZLogger.d(LOG_TAG, "onRequestFailed(), list null or empty");
                        ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Get_machines_failed, "list null or empty");
                  //      shiftLogCoreCallback.onShiftLogFailed(errorObject);
                    }
                } else {
                    ZLogger.d(LOG_TAG, "onRequest(), getShiftLog failed" + response.body().getErrorResponse().getErrorDesc() + " " + response.body().getErrorResponse().getErrorCode());
                    ErrorObject errorObject = errorObjectWithErrorCode(response.body().getErrorResponse());
              //      shiftLogCoreCallback.onShiftLogFailed(errorObject);
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

    @Override
    public void GetShiftForMachine(String siteUrl, String sessionId, int machineId, final ShiftForMachineCoreCallback<ShiftForMachineResponse> shiftForMachineCoreCallback, final int totalRetries, int specificRequestTimeout) {
        GetShiftForMachineRequest getShiftForMachineRequest = new GetShiftForMachineRequest(sessionId, machineId);
        Call<ShiftForMachineResponse> call = mShiftLogNetworkManagerInterface.getShiftForMachineServiceRequests(siteUrl, specificRequestTimeout, TimeUnit.SECONDS).getGetShiftForMachine(getShiftForMachineRequest);
        call.enqueue(new Callback<ShiftForMachineResponse>() {
            @Override
            public void onResponse(Call<ShiftForMachineResponse> call, Response<ShiftForMachineResponse> response) {
                if (response.body().getError() == null) {
                    shiftForMachineCoreCallback.onShiftForMachineSucceeded(response.body());
                } else {
                    ZLogger.d(LOG_TAG, "onResponse(), GetShiftForMachine failed");
                    ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Get_shift_for_machine_failed, "shift for machine failed");
                    shiftForMachineCoreCallback.onShiftForMachineFailed(errorObject);
                }
            }

            @Override
            public void onFailure(Call<ShiftForMachineResponse> call, Throwable t) {
                if (retryCount++ < totalRetries) {
                    Log.d(LOG_TAG, "Retrying... (" + retryCount + " out of " + totalRetries + ")");
                    call.clone().enqueue(this);
                } else {
                    retryCount = 0;
                    ZLogger.d(LOG_TAG, "onRequestFailed(), " + t.getMessage());
                    ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Retrofit, "General Error");
                    shiftForMachineCoreCallback.onShiftForMachineFailed(errorObject);
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
            case 500:
                return  ErrorObject.ErrorCode.Fail_to_perform_GetMachineShiftLog;
        }
        return ErrorObject.ErrorCode.Unknown;
    }
}
