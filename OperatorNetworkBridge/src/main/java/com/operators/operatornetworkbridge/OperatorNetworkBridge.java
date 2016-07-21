package com.operators.operatornetworkbridge;

import android.util.Log;

import com.app.operatorinfra.GetOperatorByIdCallback;
import com.app.operatorinfra.GetOperatorNetworkBridgeInterface;
import com.app.operatorinfra.Operator;
import com.operators.operatornetworkbridge.interfaces.GetOperatorByIdNetworkManagerInterface;
import com.operators.operatornetworkbridge.server.requests.GetOperatorByIdRequest;
import com.operators.operatornetworkbridge.server.responses.ErrorResponse;
import com.operators.operatornetworkbridge.server.responses.OperatorDataResponse;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class OperatorNetworkBridge implements GetOperatorNetworkBridgeInterface {
    private static final String LOG_TAG = OperatorNetworkBridge.class.getSimpleName();

    private GetOperatorByIdNetworkManagerInterface mGetOperatorByIdNetworkManagerInterface;

    public void inject(GetOperatorByIdNetworkManagerInterface getOperatorByIdNetworkManagerInterface) {
        mGetOperatorByIdNetworkManagerInterface = getOperatorByIdNetworkManagerInterface;
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
