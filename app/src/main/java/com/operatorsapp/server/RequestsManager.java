package com.operatorsapp.server;

import com.operatorsapp.server.interfaces.EmeraldServiceRequests;
import com.operatorsapp.server.requests.LoginRequest;
import com.operatorsapp.server.responses.ErrorResponse;
import com.operatorsapp.server.responses.SessionResponse;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RequestsManager {
    private static final String LOG_TAG = RequestsManager.class.getSimpleName();
    private static RequestsManager msInstance;
    private final ExecutorService mBackThreadExecutor;
    private HashMap<String, EmeraldServiceRequests> mEmeraldServiceRequestsHashMap;

    /*public enum MachineServerStatus {
        NO_JOB(0), WORKING_OK(1), PARAMETER_DEVIATION(2), STOPPED(3), COMMUNICATION_FAILURE(4), SETUP_WORKING(5), SETUP_STOPPED(6), SETUP_COMMUNICATION_FAILURE(7);

        private int mId;

        MachineServerStatus(int id) {
            mId = id;
        }

        public int getId() {
            return mId;
        }
    }*/

    public static RequestsManager getInstance() {
        if (msInstance == null) {
            msInstance = new RequestsManager();
        }
        return msInstance;
    }

    private RequestsManager() {
        mEmeraldServiceRequestsHashMap = new HashMap<>();
        mBackThreadExecutor = Executors.newSingleThreadExecutor();
    }

    private EmeraldServiceRequests getRetroFitServiceRequests(String siteUrl) {
        return getRetroFitServiceRequests(siteUrl, -1, null);
    }

    private EmeraldServiceRequests getRetroFitServiceRequests(String siteUrl, int timeout, TimeUnit timeUnit) {
        if (mEmeraldServiceRequestsHashMap.containsKey(siteUrl)) {
            return mEmeraldServiceRequestsHashMap.get(siteUrl);
        } else {
            OkHttpClient okHttpClient;
            if (timeout >= 0 && timeUnit != null) {
                okHttpClient = new OkHttpClient.Builder().writeTimeout(timeout, timeUnit).connectTimeout(timeout, timeUnit).readTimeout(timeout, timeUnit).build();
            } else {
                okHttpClient = new OkHttpClient();
            }
            Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(siteUrl).client(okHttpClient).build();

            EmeraldServiceRequests emeraldServiceRequests = retrofit.create(EmeraldServiceRequests.class);
            mEmeraldServiceRequestsHashMap.put(siteUrl, emeraldServiceRequests);
            return emeraldServiceRequests;
        }
    }

    // In use
    public void getUserSessionId(String siteUrl, String userName, String password, final OnRequestManagerResponseListener<String> listener) {
        LoginRequest loginRequest = new LoginRequest(userName, password);
        Call<SessionResponse> call = getRetroFitServiceRequests(siteUrl).getUserSessionId(loginRequest);
        call.enqueue(new retrofit2.Callback<SessionResponse>() {
            @Override
            public void onResponse(Call<SessionResponse> call, retrofit2.Response<SessionResponse> response) {
                SessionResponse.GetUserSessionIDResult sessionResult = response.body().getGetUserSessionIDResult();
                if (sessionResult.getErrorResponse() == null) {
                    if (response.body().getGetUserSessionIDResult().getSessionIds() != null && response.body().getGetUserSessionIDResult().getSessionIds().get(0) != null) {
                        listener.onRequestSucceed(response.body().getGetUserSessionIDResult().getSessionIds().get(0).getSessionId());
                    }
                } else {
                    ErrorObject errorObject = translateToErrorCode(sessionResult.getErrorResponse());
                    listener.onRequestFailed(errorObject);
                }
            }

            @Override
            public void onFailure(Call<SessionResponse> call, Throwable t) {
                ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Retrofit, "General Error");
                listener.onRequestFailed(errorObject);
            }
        });
    }

    /*// In use
    public void getSiteData(String siteUrl, String sessionId, final OnRequestManagerResponseListener<SitePieAndPeeData> callback) {
        BaseRequest baseRequest = new BaseRequest(sessionId);
        int specificRequestTimeout = 13;
        getRetroFitServiceRequests(siteUrl, specificRequestTimeout, TimeUnit.SECONDS).getFactoryData(baseRequest, new retrofit2.Callback<FactoryServerDataResponse>() {
            @Override
            public void onResponse(Call<FactoryServerDataResponse> call, retrofit2.Response<FactoryServerDataResponse> response) {
                ZLogger.v(LOG_TAG, "onRequestSucceed(), getSiteData request got data from server");

                SitePieAndPeeData sitePieAndPeeData = new SitePieAndPeeData();
                sitePieAndPeeData.setPEE(response.body().getPEE());
                statusAggregation(response.body().getMachineDepartmentStatus(), sitePieAndPeeData);
                if (callback != null) {
                    callback.onRequestSucceed(sitePieAndPeeData);
                }
            }

            @Override
            public void onFailure(Call<FactoryServerDataResponse> call, Throwable t) {
                ZLogger.w(LOG_TAG, "onRequestFailed(), getSiteData request failed to get data from server reason: " + t.getMessage());
                if (callback != null) {
                    callback.onRequestFailed(new ErrorObject(ErrorObject.ErrorCode.Retrofit, t.getMessage()));
                }
            }
        });
    }*/

    /*private void statusAggregation(ArrayList<MachineDepartmentStatus> machineDepartmentStatuses, SitePieAndPeeData sitePieAndPeeData) {
        for (MachineDepartmentStatus machineDepartmentStatus : machineDepartmentStatuses) {
            if (machineDepartmentStatus.getMachineStatusId() == MachineServerStatus.WORKING_OK.getId()) {
                sitePieAndPeeData.setWorkingPercentage(machineDepartmentStatus.getMachinePC());
            } else if (machineDepartmentStatus.getMachineStatusId() == MachineServerStatus.STOPPED.getId()) {
                sitePieAndPeeData.setStoppedPercentage(machineDepartmentStatus.getMachinePC());
            } else if (machineDepartmentStatus.getMachineStatusId() == MachineServerStatus.NO_JOB.getId() || machineDepartmentStatus.getMachineStatusId() == MachineServerStatus.COMMUNICATION_FAILURE.getId() || machineDepartmentStatus.getMachineStatusId() == MachineServerStatus.SETUP_COMMUNICATION_FAILURE.getId()) {
                sitePieAndPeeData.setNoDataPercentage(sitePieAndPeeData.getNoDataPercentage() + machineDepartmentStatus.getMachinePC());
            } else if (machineDepartmentStatus.getMachineStatusId() == MachineServerStatus.PARAMETER_DEVIATION.getId()) {
                sitePieAndPeeData.setParameterDeviationPercentage(machineDepartmentStatus.getMachinePC());
            } else if (machineDepartmentStatus.getMachineStatusId() == MachineServerStatus.SETUP_WORKING.getId() || machineDepartmentStatus.getMachineStatusId() == MachineServerStatus.SETUP_STOPPED.getId()) {
                sitePieAndPeeData.setSetupPercentage(sitePieAndPeeData.getSetupPercentage() + machineDepartmentStatus.getMachinePC());
            }
        }
    }*/

    /*// In use
    public void getFactoryOeePee(final String siteUrl, String sessionId, final OnRequestManagerResponseListener<ArrayList<Float>> callback) {
        GetRangeOEEPEE getRangeOEEPEE = new GetRangeOEEPEE(sessionId, DateUtils.getFormattedToYesterday(), 3);
        getRetroFitServiceRequests(siteUrl).getFactoryHistoryPEE(getRangeOEEPEE, new retrofit2.Callback<GetOeePeeDataResponse>() {
            @Override
            public void onResponse(Call<GetOeePeeDataResponse> call, retrofit2.Response<GetOeePeeDataResponse> response) {
                ZLogger.v(LOG_TAG, "onRequestSucceed(), getFactoryOeePee request got data from server");

                ArrayList<Float> mHistoryPee = new ArrayList<Float>();
                ArrayList<OeePeeData> mPeeFromServer = response.body().getOeePeeData();

                sortHistoryPeeByDate(mPeeFromServer);

                for (OeePeeData oeePeeData : mPeeFromServer) {
                    mHistoryPee.add(oeePeeData.getPEE());
                }
                if (callback != null) {
                    callback.onRequestSucceed(mHistoryPee);
                }
            }

            @Override
            public void onFailure(Call<GetOeePeeDataResponse> call, Throwable t) {
                ZLogger.w(LOG_TAG, "onRequestFailed(), getFactoryOeePee request failed to get data from server reason: " + t.getMessage());
                if (callback != null) {
                    callback.onRequestFailed(new ErrorObject(ErrorObject.ErrorCode.Retrofit, t.getMessage()));
                }
            }
        });
    }*/

    /*private void sortHistoryPeeByDate(ArrayList<OeePeeData> mPeeFromServer) {
        Collections.sort(mPeeFromServer, new Comparator<OeePeeData>() {
            @Override
            public int compare(OeePeeData lhs, OeePeeData rhs) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date leftDate = null;
                Date rightDate = null;
                try {
                    leftDate = format.parse(lhs.getDate());
                } catch (ParseException e) {
                    leftDate = new Date(System.currentTimeMillis());
                    e.printStackTrace();
                }

                try {
                    rightDate = format.parse(rhs.getDate());
                } catch (ParseException e) {
                    rightDate = new Date(System.currentTimeMillis());
                    e.printStackTrace();
                }
                return rightDate.compareTo(leftDate);
            }
        });
    }*/

    /*public void getSiteGeneralData(String siteUrl, String sessionId, final OnRequestManagerResponseListener<ApiAllDepartmentsResponse> callback) {
        BaseRequest baseRequest = new BaseRequest(sessionId);
        getRetroFitServiceRequests(siteUrl).getSiteGeneralData(baseRequest, new retrofit2.Callback<ApiAllDepartmentsResponse>() {
            @Override
            public void onResponse(Call<ApiAllDepartmentsResponse> call, retrofit2.Response<ApiAllDepartmentsResponse> response) {
                if (callback != null) {
                    callback.onRequestSucceed(response.body());
                }
            }

            @Override
            public void onFailure(Call<ApiAllDepartmentsResponse> call, Throwable t) {
                if (callback != null) {
                    callback.onRequestFailed(new ErrorObject(ErrorObject.ErrorCode.Retrofit, t.getMessage()));
                }
            }
        });
    }*/

    /*public static List<String> splitEqually(String text, int size) {
        // Give the list the right capacity to start with. You could use an array
        // instead if you wanted.
        List<String> ret = new ArrayList<String>((text.length() + size - 1) / size);

        for (int start = 0; start < text.length(); start += size) {
            ret.add(text.substring(start, Math.min(text.length(), start + size)));
        }
        return ret;
    }*/

    /*private class RequestCallbackWithResultArray<T> implements Callback<ServerResponse<T>> {
        private OnRequestManagerResponseListener<T> mOnRequestManagerResponseListener;

        private RequestCallbackWithResultArray(OnRequestManagerResponseListener<T> onRequestManagerResponseListener) {
            mOnRequestManagerResponseListener = onRequestManagerResponseListener;
        }

        @Override
        public void onResponse(Call<ServerResponse<T>> call, Response<ServerResponse<T>> response) {
            if (response.body().getErrorResponse() == null) {
                mOnRequestManagerResponseListener.onRequestSucceed(response.body().getResult());
                return;
            }

            // 104 SessionInvalid
            if (response.body().getErrorResponse().getErrorCode() == 104) {
                mOnRequestManagerResponseListener.onRequestSucceed(response.body().getResult());
            } else {
                ErrorObject errorObject = translateToErrorCode(response.body().getErrorResponse());
                mOnRequestManagerResponseListener.onRequestFailed(errorObject);
            }
        }

        @Override
        public void onFailure(Call<ServerResponse<T>> call, Throwable t) {
            ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Retrofit, t.getMessage());
            mOnRequestManagerResponseListener.onRequestFailed(errorObject);
        }

        private ErrorObject translateToErrorCode(ErrorResponse errorResponse) {
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
    }*/

    public interface OnRequestManagerResponseListener<T> {
        void onRequestSucceed(T result);

        void onRequestFailed(ErrorObject reason);
    }

   /* private class RequestCallback<T extends ErrorBaseRespone> implements Callback<T> {
        private OnRequestManagerResponseListener<T> mOnRequestManagerResponseListener;

        private RequestCallback(OnRequestManagerResponseListener<T> onRequestManagerResponseListener) {
            mOnRequestManagerResponseListener = onRequestManagerResponseListener;
        }

        @Override
        public void onResponse(Call<T> call, Response<T> response) {
            if (response.body().getErrorResponse() == null) {
                mOnRequestManagerResponseListener.onRequestSucceed(response.body());
                return;
            }
            // 104 SessionInvalid
            if (response.body().getErrorResponse().getErrorCode() == 104) {
                mOnRequestManagerResponseListener.onRequestSucceed(response.body());
            } else {
                ErrorObject errorObject = translateToErrorCode(response.body().getErrorResponse());
                mOnRequestManagerResponseListener.onRequestFailed(errorObject);
            }
        }

        @Override
        public void onFailure(Call<T> call, Throwable t) {
            ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Retrofit, t.getMessage());
            mOnRequestManagerResponseListener.onRequestFailed(errorObject);
        }
    }*/

    private ErrorObject translateToErrorCode(ErrorResponse errorResponse) {
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
