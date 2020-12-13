package com.operatorsapp.utils;

import com.example.common.ErrorResponse;
import com.example.common.QCModels.SaveTestDetailsRequest;
import com.example.common.QCModels.SaveTestDetailsResponse;
import com.example.common.QCModels.TestDetailsRequest;
import com.example.common.QCModels.TestDetailsResponse;
import com.example.common.QCModels.TestOrderRequest;
import com.example.common.QCModels.TestOrderResponse;
import com.example.common.QCModels.TestOrderSendRequest;
import com.example.common.StandardResponse;
import com.example.oppapplog.OppAppLogger;
import com.operatorsapp.server.NetworkManager;
import com.operatorsapp.server.requests.TestOrderMaterialRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QCRequests {
    private static final String TAG = QCRequests.class.getSimpleName();

    public void getQCTestOrder(final TestOrderRequest request, final QCTestOrderCallback callback) {
        NetworkManager.getInstance().getQCTestOrder(request, new Callback<TestOrderResponse>() {
            @Override
            public void onResponse(Call<TestOrderResponse> call, Response<TestOrderResponse> response) {

                if (callback != null) {
                    if (response.body() != null && (response.body().getError() == null || response.body().getError().getErrorDesc() == null)) {
                        callback.onSuccess(response.body());
                    } else {
                        onFailure(call, new Throwable(getErrorMessage(response.body(), "getQCTestOrder() failed")));
                    }
                }else {
                    OppAppLogger.w(TAG, "getQCTestOrder() failed");
                }
            }

            @Override
            public void onFailure(Call<TestOrderResponse> call, Throwable t) {
                OppAppLogger.w(TAG, t.getMessage());
                StandardResponse errorResponse = new StandardResponse(ErrorResponse.ErrorCode.No_data, t.getMessage());
                callback.onFailure(errorResponse);
            }
        });
    }

    public void getQCTestOrderMaterial(TestOrderMaterialRequest request, final QCTestOrderCallback callback) {
        NetworkManager.getInstance().getMaterialTestOrder(request, new Callback<TestOrderResponse>() {
            @Override
            public void onResponse(Call<TestOrderResponse> call, Response<TestOrderResponse> response) {

                if (callback != null) {
                    if (response.body() != null && (response.body().getError() == null || response.body().getError().getErrorDesc() == null)) {
                        callback.onSuccess(response.body());
                    } else {
                        onFailure(call, new Throwable(getErrorMessage(response.body(), "getQCTestOrder() failed")));
                    }
                }else {
                    OppAppLogger.w(TAG, "getQCTestOrder() failed");
                }
            }

            @Override
            public void onFailure(Call<TestOrderResponse> call, Throwable t) {
                OppAppLogger.w(TAG, t.getMessage());
                StandardResponse errorResponse = new StandardResponse(ErrorResponse.ErrorCode.No_data, t.getMessage());
                callback.onFailure(errorResponse);
            }
        });
    }

    public interface QCTestOrderCallback {
        void onSuccess(TestOrderResponse testOrderResponse);
        void onFailure(StandardResponse standardResponse);
    }

    public void postQCSendTestOrder(TestOrderSendRequest request, final QCTestSendOrderCallback callback) {
        NetworkManager.getInstance().postQCSendTestOrder(request, new Callback<StandardResponse>() {
            @Override
            public void onResponse(Call<StandardResponse> call, Response<StandardResponse> response) {

                if (callback != null) {
                    if (response.body() != null && (response.body().getError() == null || response.body().getError().getErrorDesc() == null)
                && response.body().getLeaderRecordID() != null && response.body().getLeaderRecordID() > 0) {
                        callback.onSuccess(response.body());
                    } else {
                        onFailure(call, new Throwable(getErrorMessage(response.body(),"postQCSendTestOrder() failed")));
                    }
                }else {
                    OppAppLogger.w(TAG, "postQCSendTestOrder() failed");
                }
            }

            @Override
            public void onFailure(Call<StandardResponse> call, Throwable t) {
                OppAppLogger.w(TAG, t.getMessage());
                StandardResponse errorResponse = new StandardResponse(ErrorResponse.ErrorCode.No_data, t.getMessage());
                callback.onFailure(errorResponse);
            }
        });
    }

    public interface QCTestSendOrderCallback {
        void onSuccess(StandardResponse standardResponse);
        void onFailure(StandardResponse standardResponse);
    }

    public void getQCTestDetails(TestDetailsRequest request, final getQCTestDetailsCallback callback) {
        NetworkManager.getInstance().getQCTestDetails(request, new Callback<TestDetailsResponse>() {
            @Override
            public void onResponse(Call<TestDetailsResponse> call, Response<TestDetailsResponse> response) {

                if (callback != null) {
                    if (response.body() != null && (response.body().getError() == null || response.body().getError().getErrorDesc() == null)) {
                        callback.onSuccess(response.body());
                    } else {
                        onFailure(call, new Throwable(getErrorMessage(response.body(),"getQCTestDetails() failed")));
                    }
                }else {
                    OppAppLogger.w(TAG, "getQCTestDetails() failed");
                }
            }

            @Override
            public void onFailure(Call<TestDetailsResponse> call, Throwable t) {
                OppAppLogger.w(TAG, t.getMessage());
                StandardResponse errorResponse = new StandardResponse(ErrorResponse.ErrorCode.No_data, t.getMessage());
                callback.onFailure(errorResponse);
            }
        });
    }

    public interface getQCTestDetailsCallback {
        void onSuccess(TestDetailsResponse testDetailsResponse);
        void onFailure(StandardResponse standardResponse);
    }

    public void postQCSaveTestDetails(SaveTestDetailsRequest request, final postQCSaveTestDetailsCallback callback) {
        NetworkManager.getInstance().postQCSaveTestDetails(request, new Callback<SaveTestDetailsResponse>() {
            @Override
            public void onResponse(Call<SaveTestDetailsResponse> call, Response<SaveTestDetailsResponse> response) {

                if (callback != null) {
                    if (response.body() != null && (response.body().getError() == null || response.body().getError().getErrorDesc() == null)) {
                        callback.onSuccess(response.body());
                    } else {
                        onFailure(call, new Throwable(getErrorMessage(response.body(), "postQCSaveTestDetails() failed")));
                    }
                }else {
                    OppAppLogger.w(TAG, "postQCSaveTestDetails() failed");
                }
            }

            @Override
            public void onFailure(Call<SaveTestDetailsResponse> call, Throwable t) {
                OppAppLogger.w(TAG, t.getMessage());
                StandardResponse errorResponse = new StandardResponse(ErrorResponse.ErrorCode.No_data, t.getMessage());
                callback.onFailure(errorResponse);
            }
        });
    }

    private String getErrorMessage(StandardResponse response, String s) {
        if (response != null && response.getError() != null && response.getError().getErrorDesc() != null && !response.getError().getErrorDesc().isEmpty()){
            return response.getError().getErrorDesc();
        }
        return s;
    }

    public interface postQCSaveTestDetailsCallback {
        void onSuccess(SaveTestDetailsResponse saveTestDetailsResponse);
        void onFailure(StandardResponse standardResponse);
    }
}
