package com.operators.operatornetworkbridge;

import android.util.Log;

import com.app.operatorinfra.GetOperatorByIdCallback;
import com.app.operatorinfra.OperatorNetworkBridgeInterface;
import com.app.operatorinfra.Operator;
import com.app.operatorinfra.SetOperatorForMachineCallback;
import com.operators.operatornetworkbridge.interfaces.GetOperatorByIdNetworkManagerInterface;
import com.operators.operatornetworkbridge.interfaces.SetOperatorForMachineNetworkManagerInterface;
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

    public void inject(GetOperatorByIdNetworkManagerInterface getOperatorByIdNetworkManagerInterface, SetOperatorForMachineNetworkManagerInterface setOperatorForMachineNetworkManagerInterface) {
        mGetOperatorByIdNetworkManagerInterface = getOperatorByIdNetworkManagerInterface;
        mSetOperatorForMachineNetworkManagerInterface = setOperatorForMachineNetworkManagerInterface;
        Log.i(LOG_TAG, "OperatorNetworkBridge inject()");
    }

    @Override
    public void getOperator(String siteUrl, String sessionId, String operatorId, final GetOperatorByIdCallback getOperatorByIdCallback, int totalRetries, int specificRequestTimeout) {
        GetOperatorByIdRequest getOperatorByIdRequest = new GetOperatorByIdRequest(sessionId, operatorId);
        Call<OperatorDataResponse> call = mGetOperatorByIdNetworkManagerInterface.getOperatorByIdRetroFitServiceRequests(siteUrl, specificRequestTimeout, TimeUnit.SECONDS).getOperator(getOperatorByIdRequest);
        call.enqueue(new Callback<OperatorDataResponse>() {
            @Override
            public void onResponse(Call<OperatorDataResponse> call, Response<OperatorDataResponse> response) {
                if (response.isSuccessful()) {
                    Operator operator = response.body().getOperator();
                    getOperatorByIdCallback.onGetOperatorSucceeded(operator);
                }
                else {
                    ErrorObject errorObject = errorObjectWithErrorCode(response.body().getErrorResponse());
                    getOperatorByIdCallback.onGetOperatorFailed(errorObject);
                }
            }

            @Override
            public void onFailure(Call<OperatorDataResponse> call, Throwable t) {
                ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Get_operator_failed, "General Error");
                getOperatorByIdCallback.onGetOperatorFailed(errorObject);
            }
        });
    }

    @Override
    public void setOperatorForMachine(String siteUrl, String sessionId, String machineId, String operatorId, final SetOperatorForMachineCallback setOperatorForMachineCallback, int totalRetries, int specificRequestTimeout) {
        SetOperatorForMachineRequest setOperatorForMachineRequest = new SetOperatorForMachineRequest(sessionId, machineId, operatorId);
        Call<SetOperatorForMachineResponse> call = mSetOperatorForMachineNetworkManagerInterface.setOperatorForMachineRetroFitServiceRequests(siteUrl, specificRequestTimeout, TimeUnit.SECONDS).setOperatorForMachine(setOperatorForMachineRequest);
        call.enqueue(new Callback<SetOperatorForMachineResponse>() {
            @Override
            public void onResponse(Call<SetOperatorForMachineResponse> call, Response<SetOperatorForMachineResponse> response) {
                if (response.isSuccessful()) {
                    setOperatorForMachineCallback.onSetOperatorForMachineSuccess();
                }
                else {
                    ErrorObject errorObject = errorObjectWithErrorCode(response.body().getErrorResponse());
                    setOperatorForMachineCallback.onSetOperatorForMachineFailed(errorObject);
                }
            }

            @Override
            public void onFailure(Call<SetOperatorForMachineResponse> call, Throwable t) {
                ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Set_operator_for_machine_failed, "General Error");
                setOperatorForMachineCallback.onSetOperatorForMachineFailed(errorObject);
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
