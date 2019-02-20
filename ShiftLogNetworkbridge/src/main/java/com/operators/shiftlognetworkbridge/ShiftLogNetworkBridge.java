package com.operators.shiftlognetworkbridge;

import com.example.common.Event;
import com.example.oppapplog.OppAppLogger;
import com.operators.shiftloginfra.ActualBarExtraDetailsCallback;
import com.operators.shiftloginfra.ShiftForMachineCoreCallback;
import com.operators.shiftloginfra.ShiftLogCoreCallback;
import com.operators.shiftloginfra.ShiftLogNetworkBridgeInterface;
import com.example.common.actualBarExtraResponse.ActualBarExtraResponse;
import com.operators.shiftloginfra.model.ShiftForMachineResponse;
import com.operators.shiftlognetworkbridge.interfaces.ShiftLogNetworkManagerInterface;
import com.operators.shiftlognetworkbridge.server.ErrorObject;
import com.operators.shiftlognetworkbridge.server.requests.ActualBarExtraDetailsRequest;
import com.operators.shiftlognetworkbridge.server.requests.GetShiftForMachineRequest;
import com.operators.shiftlognetworkbridge.server.requests.GetShiftLogRequest;
import com.operators.shiftlognetworkbridge.server.responses.ErrorResponse;
import com.operators.shiftlognetworkbridge.server.responses.ShiftLogResponse;

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
//        startingFrom = "2019-02-14 14:00:00.000";
        GetShiftLogRequest getShiftLogRequest = new GetShiftLogRequest(sessionId, machineId, startingFrom);
        Call<ShiftLogResponse> call = mShiftLogNetworkManagerInterface.getShiftLogRetroFitServiceRequests(siteUrl, specificRequestTimeout, TimeUnit.SECONDS).getMachineShiftLog(getShiftLogRequest);
        call.enqueue(new Callback<ShiftLogResponse>() {

            @Override
            public void onResponse(Call<ShiftLogResponse> call, Response<ShiftLogResponse> response) {


                if (response.body() != null && response.body().getEvents() != null && (response.body().getErrorResponse() == null || response.body().getErrorResponse().getErrorCode() == 1967)) {
                    ArrayList<Event> events = response.body().getEvents();

                    OppAppLogger.getInstance().d(LOG_TAG, "getShiftLog , onResponse " + events.size() + " events");


                    shiftLogCoreCallback.onShiftLogSucceeded(events);
                } else {

                    OppAppLogger.getInstance().d(LOG_TAG, "getShiftLog , onResponse - getShiftLog failed Error");
                    ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Retrofit, "getShiftLog failed Error");
                    shiftLogCoreCallback.onShiftLogFailed(errorObject);
                }
            }

            @Override
            public void onFailure(Call<ShiftLogResponse> call, Throwable t) {
                if (retryCount++ < totalRetries) {
                    OppAppLogger.getInstance().d(LOG_TAG, "Retrying... (" + retryCount + " out of " + totalRetries + ")");

                    call.clone().enqueue(this);
                } else {
                    retryCount = 0;
                    OppAppLogger.getInstance().d(LOG_TAG, "getShiftLog, onFailure " + t.getMessage());

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
                if (response.body() != null && response.body().getError() == null) {
                    shiftForMachineCoreCallback.onShiftForMachineSucceeded(response.body());
                } else if (response.body() != null) {
                    OppAppLogger.getInstance().e(LOG_TAG, "GetShiftForMachine onResponse, " + response.body().getError());
                    ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Server, "shift for machine failed");
                    shiftForMachineCoreCallback.onShiftForMachineFailed(errorObject);
                } else {
                    OppAppLogger.getInstance().d(LOG_TAG, "getShiftLog , onResponse - GetShiftForMachine failed Error");
                    ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Retrofit, "GetShiftForMachine failed Error");
                    shiftForMachineCoreCallback.onShiftForMachineFailed(errorObject);
                }
            }

            @Override
            public void onFailure(Call<ShiftForMachineResponse> call, Throwable t) {
                if (retryCount++ < totalRetries) {
                    OppAppLogger.getInstance().d(LOG_TAG, "Retrying... (" + retryCount + " out of " + totalRetries + ")");
                    call.clone().enqueue(this);
                } else {
                    retryCount = 0;
                    OppAppLogger.getInstance().d(LOG_TAG, "GetShiftForMachine, onFailure " + t.getMessage());
                    ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Retrofit, "General Error");
                    shiftForMachineCoreCallback.onShiftForMachineFailed(errorObject);
                }

            }
        });
    }

    @Override
    public void GetActualBarExtraDetails(String siteUrl, String sessionId, String startTime, String endTime, final ActualBarExtraDetailsCallback<ActualBarExtraResponse> actualBarExtraResponseActualBarExtraDetailsCallback, final int totalRetries, int specificRequestTimeout) {
//        startTime = "2019-02-14 14:00:00.000";
        ActualBarExtraDetailsRequest actualBarExtraDetailsRequest = new ActualBarExtraDetailsRequest(sessionId, startTime, endTime);
        Call<ActualBarExtraResponse> call = mShiftLogNetworkManagerInterface.getActualBarExtraDetails(siteUrl, specificRequestTimeout, TimeUnit.SECONDS).getActualBarExtra(actualBarExtraDetailsRequest);
        call.enqueue(new Callback<ActualBarExtraResponse>() {

            @Override
            public void onResponse(Call<ActualBarExtraResponse> call, Response<ActualBarExtraResponse> response) {


                if (response.body() != null && response.body().getFunctionSucceed()) {
                    OppAppLogger.getInstance().d(LOG_TAG, "GetActualBarExtraDetails , onResponse ");

                    actualBarExtraResponseActualBarExtraDetailsCallback.onActualBarExtraDetailsSucceeded(response.body());
                } else {
                    OppAppLogger.getInstance().d(LOG_TAG, "GetActualBarExtraDetails , onResponse - GetActualBarExtraDetails failed Error");
                    ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Retrofit, "GetActualBarExtraDetails failed Error");
                    actualBarExtraResponseActualBarExtraDetailsCallback.onActualBarExtraDetailsFailed(errorObject);
                }
            }

            @Override
            public void onFailure(Call<ActualBarExtraResponse> call, Throwable t) {
                if (retryCount++ < totalRetries) {
                    OppAppLogger.getInstance().d(LOG_TAG, "Retrying... (" + retryCount + " out of " + totalRetries + ")");

                    call.clone().enqueue(this);
                } else {
                    retryCount = 0;
                    OppAppLogger.getInstance().d(LOG_TAG, "GetActualBarExtraDetails, onFailure " + t.getMessage());

                    ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Retrofit, "General Error");
                    actualBarExtraResponseActualBarExtraDetailsCallback.onActualBarExtraDetailsFailed(errorObject);


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
                return ErrorObject.ErrorCode.No_data;
            case 500:
                return ErrorObject.ErrorCode.Server;
        }
        return ErrorObject.ErrorCode.Unknown;
    }
}
