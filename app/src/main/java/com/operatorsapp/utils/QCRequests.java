package com.operatorsapp.utils;

import com.example.common.ErrorResponse;
import com.example.common.QCModels.SaveTestDetailsResponse;
import com.example.common.QCModels.TestDetailsRequest;
import com.example.common.QCModels.TestDetailsResponse;
import com.example.common.QCModels.TestOrderRequest;
import com.example.common.QCModels.TestOrderResponse;
import com.example.common.QCModels.TestOrderSendRequest;
import com.example.common.StandardResponse;
import com.example.oppapplog.OppAppLogger;
import com.operatorsapp.server.NetworkManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QCRequests {
    private static final String TAG = QCRequests.class.getSimpleName();

    private void getQCTestOrder(TestOrderRequest request, final QCTestOrderCallback callback) {
        NetworkManager.getInstance().getQCTestOrder(request, new Callback<TestOrderResponse>() {
            @Override
            public void onResponse(Call<TestOrderResponse> call, Response<TestOrderResponse> response) {

                if (callback != null) {
                    if (response.body() != null) {
                        callback.onSuccess(response.body());
                    } else {
                        onFailure(call, new Throwable("getQCTestOrder() failed"));
                    }
                }else {
                    OppAppLogger.getInstance().w(TAG, "getQCTestOrder() failed");
                }
            }

            @Override
            public void onFailure(Call<TestOrderResponse> call, Throwable t) {
                OppAppLogger.getInstance().w(TAG, t.getMessage());
                StandardResponse errorResponse = new StandardResponse(ErrorResponse.ErrorCode.No_data, t.getMessage());
                callback.onFailure(errorResponse);
            }
        });
    }

    private interface QCTestOrderCallback {
        void onSuccess(TestOrderResponse testOrderRequest);
        void onFailure(StandardResponse standardResponse);
    }

    private void postQCSendTestOrder(TestOrderSendRequest request, final QCTestSendOrderCallback callback) {
        NetworkManager.getInstance().postQCSendTestOrder(request, new Callback<StandardResponse>() {
            @Override
            public void onResponse(Call<StandardResponse> call, Response<StandardResponse> response) {

                if (callback != null) {
                    if (response.body() != null && response.body().getLeaderRecordID() != null && response.body().getLeaderRecordID() > 0) {
                        callback.onSuccess(response.body());
                    } else {
                        onFailure(call, new Throwable("postQCSendTestOrder() failed"));
                    }
                }else {
                    OppAppLogger.getInstance().w(TAG, "postQCSendTestOrder() failed");
                }
            }

            @Override
            public void onFailure(Call<StandardResponse> call, Throwable t) {
                OppAppLogger.getInstance().w(TAG, t.getMessage());
                StandardResponse errorResponse = new StandardResponse(ErrorResponse.ErrorCode.No_data, t.getMessage());
                callback.onFailure(errorResponse);
            }
        });
    }

    private interface QCTestSendOrderCallback {
        void onSuccess(StandardResponse standardResponse);
        void onFailure(StandardResponse standardResponse);
    }

    private void getQCTestDetails(TestDetailsRequest request, final getQCTestDetailsCallback callback) {
        NetworkManager.getInstance().getQCTestDetails(request, new Callback<TestDetailsResponse>() {
            @Override
            public void onResponse(Call<TestDetailsResponse> call, Response<TestDetailsResponse> response) {

                if (callback != null) {
                    if (response.body() != null) {
                        callback.onSuccess(response.body());
                    } else {
                        onFailure(call, new Throwable("getQCTestDetails() failed"));
                    }
                }else {
                    OppAppLogger.getInstance().w(TAG, "getQCTestDetails() failed");
                }
            }

            @Override
            public void onFailure(Call<TestDetailsResponse> call, Throwable t) {
                OppAppLogger.getInstance().w(TAG, t.getMessage());
                StandardResponse errorResponse = new StandardResponse(ErrorResponse.ErrorCode.No_data, t.getMessage());
                callback.onFailure(errorResponse);
            }
        });
    }

    private interface getQCTestDetailsCallback {
        void onSuccess(TestDetailsResponse standardResponse);
        void onFailure(StandardResponse standardResponse);
    }

    private void postQCSaveTestDetails(TestDetailsResponse request, final postQCSaveTestDetailsCallback callback) {
        NetworkManager.getInstance().postQCSaveTestDetails(request, new Callback<SaveTestDetailsResponse>() {
            @Override
            public void onResponse(Call<SaveTestDetailsResponse> call, Response<SaveTestDetailsResponse> response) {

                if (callback != null) {
                    if (response.body() != null) {
                        callback.onSuccess(response.body());
                    } else {
                        onFailure(call, new Throwable("postQCSaveTestDetails() failed"));
                    }
                }else {
                    OppAppLogger.getInstance().w(TAG, "postQCSaveTestDetails() failed");
                }
            }

            @Override
            public void onFailure(Call<SaveTestDetailsResponse> call, Throwable t) {
                OppAppLogger.getInstance().w(TAG, t.getMessage());
                StandardResponse errorResponse = new StandardResponse(ErrorResponse.ErrorCode.No_data, t.getMessage());
                callback.onFailure(errorResponse);
            }
        });
    }

    private interface postQCSaveTestDetailsCallback {
        void onSuccess(SaveTestDetailsResponse standardResponse);
        void onFailure(StandardResponse standardResponse);
    }
}
