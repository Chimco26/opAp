package com.operators.operatornetworkbridge;

import android.util.Log;

import com.app.operatorinfra.GetOperatorByIdCallback;
import com.app.operatorinfra.OperatorNetworkBridgeInterface;
import com.app.operatorinfra.Operator;
import com.app.operatorinfra.SetOperatorForMachineCallback;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.operators.errorobject.ErrorObjectInterface;
import com.operators.operatornetworkbridge.interfaces.GetOperatorByIdNetworkManagerInterface;
import com.operators.operatornetworkbridge.interfaces.SetOperatorForMachineNetworkManagerInterface;
import com.operators.operatornetworkbridge.server.ErrorObject;
import com.operators.operatornetworkbridge.server.requests.GetOperatorByIdRequest;
import com.operators.operatornetworkbridge.server.requests.SetOperatorForMachineRequest;
import com.operators.operatornetworkbridge.server.responses.ErrorResponse;
import com.operators.operatornetworkbridge.server.responses.OperatorDataResponse;
import com.operators.operatornetworkbridge.server.responses.SetOperatorForMachineResponse;
import com.operators.reportrejectnetworkbridge.server.response.ErrorResponseNewVersion;
import com.zemingo.logrecorder.ZLogger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.ResponseBody;
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
        ZLogger.i(LOG_TAG, "OperatorNetworkBridge inject()");
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
                        ZLogger.w(LOG_TAG, "Response is null");
                    }
                } else {
                    if (response.body() != null) {

                        ErrorObject errorObject = errorObjectWithErrorCode(response.body().getErrorResponse());
                        getOperatorByIdCallback.onGetOperatorFailed(errorObject);
                    } else {
                        ZLogger.w(LOG_TAG, "Response body is null");

                    }
                }
            }

            @Override
            public void onFailure(Call<OperatorDataResponse> call, Throwable t) {
                if (mRetryCount++ < totalRetries) {
                    ZLogger.d(LOG_TAG, "Retrying... (" + mRetryCount + " out of " + totalRetries + ")");
                    call.clone().enqueue(this);
                } else {
                    mRetryCount = 0;
                    ZLogger.d(LOG_TAG, "onRequestFailed(), " + t.getMessage());
                    ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Retrofit, "Get_operator_failed Error");
                    getOperatorByIdCallback.onGetOperatorFailed(errorObject);
                }
            }
        });
    }

    @Override
    public void setOperatorForMachine(String siteUrl, String sessionId, String machineId, String operatorId, final SetOperatorForMachineCallback setOperatorForMachineCallback, final int totalRetries, int specificRequestTimeout) {
        SetOperatorForMachineRequest setOperatorForMachineRequest = new SetOperatorForMachineRequest(sessionId, machineId, operatorId);
        Call<ResponseBody> call = mSetOperatorForMachineNetworkManagerInterface.setOperatorForMachineRetroFitServiceRequests(siteUrl, specificRequestTimeout, TimeUnit.SECONDS).setOperatorForMachine(setOperatorForMachineRequest);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                String str = "";
                try {
                    JSONObject jObject = new JSONObject(response.body().string());
                    str = jObject.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ErrorResponseNewVersion newResponse = objectToNewError(str);

                if (newResponse.isFunctionSucceed()){
                    setOperatorForMachineCallback.onSetOperatorForMachineSuccess();
                }else if (newResponse.getmError().getErrorDesc() != null) {
                        ErrorObject errorObject = errorObjectWithErrorCode(new ErrorResponse(newResponse.getmError().getErrorDesc(), newResponse.getmError().getmErrorMessage(),newResponse.getmError().getErrorCode()));
                        setOperatorForMachineCallback.onSetOperatorForMachineFailed(errorObject);
                    }

//                if (response.isSuccessful()) {
//                    setOperatorForMachineCallback.onSetOperatorForMachineSuccess();
//                } else {
//                    if (response.body() != null) {
//                        ErrorObject errorObject = errorObjectWithErrorCode(response.body().getErrorResponse());
//                        setOperatorForMachineCallback.onSetOperatorForMachineFailed(errorObject);
//                    }
//                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (mRetryCount++ < totalRetries) {
                    ZLogger.d(LOG_TAG, "Retrying... (" + mRetryCount + " out of " + totalRetries + ")");
                    call.clone().enqueue(this);
                } else {
                    mRetryCount = 0;
                    ZLogger.d(LOG_TAG, "onRequestFailed(), " + t.getMessage());
                    ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Retrofit, "Set_operator_for_machine_failed Error");
                    setOperatorForMachineCallback.onSetOperatorForMachineFailed(errorObject);
                }
            }
        });
    }

    private ErrorResponseNewVersion objectToNewError(String response) {

        Gson gson = new GsonBuilder().create();
        ErrorResponseNewVersion responseNewVersion;

        if (response.contains("FunctionSucceed")){
            responseNewVersion = gson.fromJson(response, ErrorResponseNewVersion.class);
        }else{
            com.operators.reportrejectnetworkbridge.server.response.ErrorResponse er = gson.fromJson(response, com.operators.reportrejectnetworkbridge.server.response.ErrorResponse.class);
            responseNewVersion = new ErrorResponseNewVersion(true, 0, er);

            if (responseNewVersion.getmError().getErrorCode() != 0){
                responseNewVersion.setFunctionSucceed(false);
            }
        }
        return responseNewVersion;
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
