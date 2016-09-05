package com.operators.operatornetworkbridge;

import android.util.Log;

import com.app.operatorinfra.GetOperatorByIdCallback;
import com.app.operatorinfra.OperatorNetworkBridgeInterface;
import com.app.operatorinfra.Operator;
import com.app.operatorinfra.SetOperatorForMachineCallback;
import com.operators.operatornetworkbridge.interfaces.GetOperatorByIdNetworkManagerInterface;
import com.operators.operatornetworkbridge.interfaces.SetOperatorForMachineNetworkManagerInterface;
import com.operators.operatornetworkbridge.server.ErrorObject;
import com.operators.operatornetworkbridge.server.requests.GetOperatorByIdRequest;
import com.operators.operatornetworkbridge.server.requests.SetOperatorForMachineRequest;
import com.operators.operatornetworkbridge.server.responses.ErrorResponse;
import com.operators.operatornetworkbridge.server.responses.OperatorDataResponse;
import com.operators.operatornetworkbridge.server.responses.SetOperatorForMachineResponse;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class OperatorNetworkBridge implements OperatorNetworkBridgeInterface {
    private static final String LOG_TAG = OperatorNetworkBridge.class.getSimpleName();
    private GetOperatorByIdNetworkManagerInterface mGetOperatorByIdNetworkManagerInterface;
    private SetOperatorForMachineNetworkManagerInterface mSetOperatorForMachineNetworkManagerInterface;
    private int mRetryCount = 0;

    public void inject(GetOperatorByIdNetworkManagerInterface getOperatorByIdNetworkManagerInterface, SetOperatorForMachineNetworkManagerInterface setOperatorForMachineNetworkManagerInterface) {
        mGetOperatorByIdNetworkManagerInterface = getOperatorByIdNetworkManagerInterface;
        mSetOperatorForMachineNetworkManagerInterface = setOperatorForMachineNetworkManagerInterface;
        Log.i(LOG_TAG, "OperatorNetworkBridge inject()");
    }

    @Override
    public void getOperator(String siteUrl, String sessionId, final String operatorId, final GetOperatorByIdCallback getOperatorByIdCallback, final int totalRetries, int specificRequestTimeout) {
        GetOperatorByIdRequest getOperatorByIdRequest = new GetOperatorByIdRequest(sessionId, operatorId);
        Call<OperatorDataResponse> call = mGetOperatorByIdNetworkManagerInterface.getOperatorByIdRetroFitServiceRequests(siteUrl, specificRequestTimeout, TimeUnit.SECONDS).getOperator(getOperatorByIdRequest);
        call.enqueue(new Callback<OperatorDataResponse>() {
            @Override
            public void onResponse(Call<OperatorDataResponse> call, Response<OperatorDataResponse> response) {
                if (response.isSuccessful()) {
                    Operator operator = null;
                    if (response.body() != null) {
                        operator = response.body().getOperator();
                        if (operator == null) {
                            ErrorObject errorObject = errorObjectWithErrorCode(response.body().getErrorResponse());
                            getOperatorByIdCallback.onGetOperatorFailed(errorObject);
                        } else {
                            getOperatorByIdCallback.onGetOperatorSucceeded(operator);
                        }
                    } else {
                        Log.w(LOG_TAG, "Response is null");
                    }
                } else {
                    if (response.body() != null) {

                        ErrorObject errorObject = errorObjectWithErrorCode(response.body().getErrorResponse());
                        getOperatorByIdCallback.onGetOperatorFailed(errorObject);
                    } else {
                        Log.w(LOG_TAG, "Response body is null");

                    }
                }
            }

            @Override
            public void onFailure(Call<OperatorDataResponse> call, Throwable t) {
                if (mRetryCount++ < totalRetries) {
                    Log.d(LOG_TAG, "Retrying... (" + mRetryCount + " out of " + totalRetries + ")");
                    call.clone().enqueue(this);
                } else {
                    mRetryCount = 0;
                    Log.d(LOG_TAG, "onRequestFailed(), " + t.getMessage());
                    ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Get_operator_failed, "Get_operator_failed Error");
                    getOperatorByIdCallback.onGetOperatorFailed(errorObject);
                }
            }
        });
    }

    @Override
    public void setOperatorForMachine(String siteUrl, String sessionId, String machineId, String operatorId, final SetOperatorForMachineCallback setOperatorForMachineCallback, final int totalRetries, int specificRequestTimeout) {
        SetOperatorForMachineRequest setOperatorForMachineRequest = new SetOperatorForMachineRequest(sessionId, machineId, operatorId);
        Call<SetOperatorForMachineResponse> call = mSetOperatorForMachineNetworkManagerInterface.setOperatorForMachineRetroFitServiceRequests(siteUrl, specificRequestTimeout, TimeUnit.SECONDS).setOperatorForMachine(setOperatorForMachineRequest);
        call.enqueue(new Callback<SetOperatorForMachineResponse>() {
            @Override
            public void onResponse(Call<SetOperatorForMachineResponse> call, Response<SetOperatorForMachineResponse> response) {
                if (response.isSuccessful()) {
                    setOperatorForMachineCallback.onSetOperatorForMachineSuccess();
                } else {
                    if (response.body() != null) {
                        ErrorObject errorObject = errorObjectWithErrorCode(response.body().getErrorResponse());
                        setOperatorForMachineCallback.onSetOperatorForMachineFailed(errorObject);
                    }
                }
            }

            @Override
            public void onFailure(Call<SetOperatorForMachineResponse> call, Throwable t) {
                if (mRetryCount++ < totalRetries) {
                    Log.d(LOG_TAG, "Retrying... (" + mRetryCount + " out of " + totalRetries + ")");
                    call.clone().enqueue(this);
                } else {
                    mRetryCount = 0;
                    Log.d(LOG_TAG, "onRequestFailed(), " + t.getMessage());
                    ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Set_operator_for_machine_failed, "Set_operator_for_machine_failed Error");
                    setOperatorForMachineCallback.onSetOperatorForMachineFailed(errorObject);
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
        }
        return ErrorObject.ErrorCode.Unknown;
    }
}
