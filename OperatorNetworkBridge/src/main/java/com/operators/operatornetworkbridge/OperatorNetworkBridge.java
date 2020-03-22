package com.operators.operatornetworkbridge;

import androidx.annotation.NonNull;

import com.app.operatorinfra.GetOperatorByIdCallback;
import com.app.operatorinfra.Operator;
import com.app.operatorinfra.OperatorNetworkBridgeInterface;
import com.app.operatorinfra.SetOperatorForMachineCallback;
import com.example.common.ErrorResponse;
import com.example.common.StandardResponse;
import com.example.common.callback.ErrorObjectInterface;
import com.example.oppapplog.OppAppLogger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.operators.operatornetworkbridge.interfaces.GetOperatorByIdNetworkManagerInterface;
import com.operators.operatornetworkbridge.interfaces.SetOperatorForMachineNetworkManagerInterface;
import com.operators.operatornetworkbridge.server.requests.GetOperatorByIdRequest;
import com.operators.operatornetworkbridge.server.requests.SetOperatorForMachineRequest;
import com.operators.operatornetworkbridge.server.responses.OperatorDataResponse;

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
        OppAppLogger.i(LOG_TAG, "OperatorNetworkBridge inject()");
    }

    @Override
    public void getOperator(String siteUrl, String sessionId, final String operatorId, final GetOperatorByIdCallback getOperatorByIdCallback, final int totalRetries, int specificRequestTimeout) {
        GetOperatorByIdRequest getOperatorByIdRequest = new GetOperatorByIdRequest(sessionId, operatorId);
        Call<OperatorDataResponse> call = mGetOperatorByIdNetworkManagerInterface.getOperatorByIdRetroFitServiceRequests(siteUrl, specificRequestTimeout, TimeUnit.SECONDS).getOperator(getOperatorByIdRequest);
        call.enqueue(new Callback<OperatorDataResponse>() {
            @Override
            public void onResponse(@NonNull Call<OperatorDataResponse> call, @NonNull Response<OperatorDataResponse> response) {
                if (response.isSuccessful()) {
                    Operator operator;
                    if (response.body() != null) {
                        operator = response.body().getOperator();
                        if (operator == null) {
                            response.body().getError().setDefaultErrorCodeConstant(response.body().getError().getErrorCode());
                            getOperatorByIdCallback.onGetOperatorFailed(response.body());
                        } else {
                            getOperatorByIdCallback.onGetOperatorSucceeded(operator);
                        }
                    } else {
                        OppAppLogger.w(LOG_TAG, "Response is null");
                    }
                } else {
                    if (response.body() != null) {

                        response.body().getError().setDefaultErrorCodeConstant(response.body().getError().getErrorCode());
                        getOperatorByIdCallback.onGetOperatorFailed(response.body());
                    } else {
                        OppAppLogger.w(LOG_TAG, "Response body is null");

                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<OperatorDataResponse> call, @NonNull Throwable t) {
                if (mRetryCount++ < totalRetries) {
                    OppAppLogger.d(LOG_TAG, "Retrying... (" + mRetryCount + " out of " + totalRetries + ")");
                    call.clone().enqueue(this);
                } else {
                    mRetryCount = 0;
                    OppAppLogger.d(LOG_TAG, "onRequestFailed(), " + t.getMessage());
                    StandardResponse errorObject = new StandardResponse(ErrorObjectInterface.ErrorCode.Retrofit, "Get_operator_failed Error");
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
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

                String str = "";
                try {
                    JSONObject jObject = new JSONObject(response.body() != null ? response.body().string() : "");
                    str = jObject.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                StandardResponse newResponse = objectToNewError(str);

                if (newResponse.getFunctionSucceed()) {
                    setOperatorForMachineCallback.onSetOperatorForMachineSuccess();
                } else {
                    if (newResponse.getError() != null && newResponse.getError().getErrorDesc() != null && newResponse.getError().getErrorDesc() != null) {
                        newResponse.getError().setDefaultErrorCodeConstant(newResponse.getError().getErrorCode());
                    } else {
                        newResponse.getError().setDefaultErrorCodeConstant(500);
                    }
                    setOperatorForMachineCallback.onSetOperatorForMachineFailed(newResponse);
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
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                if (mRetryCount++ < totalRetries) {
                    OppAppLogger.d(LOG_TAG, "Retrying... (" + mRetryCount + " out of " + totalRetries + ")");
                    call.clone().enqueue(this);
                } else {
                    mRetryCount = 0;
                    OppAppLogger.d(LOG_TAG, "onRequestFailed(), " + t.getMessage());
                    StandardResponse errorObject = new StandardResponse(ErrorObjectInterface.ErrorCode.Retrofit, "Set_operator_for_machine_failed Error");
                    setOperatorForMachineCallback.onSetOperatorForMachineFailed(errorObject);
                }
            }
        });
    }

    private StandardResponse objectToNewError(String response) {

        Gson gson = new GsonBuilder().create();
        StandardResponse responseNewVersion;

        if (response.contains("FunctionSucceed")) {
            responseNewVersion = gson.fromJson(response, StandardResponse.class);
        } else {
            ErrorResponse er = gson.fromJson(response, ErrorResponse.class);
            responseNewVersion = new StandardResponse(true, 0, er);

            if (responseNewVersion.getError().getErrorCode() != 0) {
                responseNewVersion.setFunctionSucceed(false);
            }
        }
        return responseNewVersion;
    }

}
