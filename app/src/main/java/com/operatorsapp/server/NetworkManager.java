package com.operatorsapp.server;

import android.util.Base64;
import android.util.Log;

import com.example.common.QCModels.SaveTestDetailsResponse;
import com.example.common.QCModels.TestDetailsRequest;
import com.example.common.QCModels.TestDetailsResponse;
import com.example.common.QCModels.TestOrderRequest;
import com.example.common.QCModels.TestOrderResponse;
import com.example.common.QCModels.TestOrderSendRequest;
import com.example.common.StandardResponse;
import com.example.common.permissions.PermissionResponse;
import com.example.common.reportShift.DepartmentShiftGraphRequest;
import com.example.common.reportShift.DepartmentShiftGraphResponse;
import com.example.common.reportShift.ServiceCallsResponse;
import com.example.common.request.BaseTimeRequest;
import com.example.common.request.MachineIdRequest;
import com.example.oppapplog.OppAppLogger;
import com.operators.activejobslistformachinenetworkbridge.interfaces.ActiveJobsListForMachineNetworkManagerInterface;
import com.operators.activejobslistformachinenetworkbridge.interfaces.EmeraldGetActiveJobsListForMachineServiceRequests;
import com.operators.getmachinesnetworkbridge.interfaces.EmeraldGetMachinesServiceRequests;
import com.operators.getmachinesnetworkbridge.interfaces.GetMachineNetworkManagerInterface;
import com.operators.getmachinesstatusnetworkbridge.interfaces.EmeraldGetMachineJoshDataRequest;
import com.operators.getmachinesstatusnetworkbridge.interfaces.EmeraldGetMachinesStatusServiceRequest;
import com.operators.getmachinesstatusnetworkbridge.interfaces.EmeraldSetProductionModeForMachineRequest;
import com.operators.getmachinesstatusnetworkbridge.interfaces.GetMachineStatusNetworkManagerInterface;
import com.operators.jobsnetworkbridge.interfaces.EmeraldGetJobsListServiceRequests;
import com.operators.jobsnetworkbridge.interfaces.EmeraldStartJobServiceRequests;
import com.operators.jobsnetworkbridge.interfaces.GetJobsListForMachineNetworkManagerInterface;
import com.operators.jobsnetworkbridge.interfaces.StartJobForMachineNetworkManagerInterface;
import com.operators.loginnetworkbridge.interfaces.EmeraldLoginServiceRequests;
import com.operators.loginnetworkbridge.interfaces.LoginNetworkManagerInterface;
import com.operators.loginnetworkbridge.server.requests.LoginRequest;
import com.operators.machinedatanetworkbridge.interfaces.EmeraldGetMachinesDataServiceRequest;
import com.operators.machinedatanetworkbridge.interfaces.GetMachineDataNetworkManagerInterface;
import com.operators.operatornetworkbridge.interfaces.EmeraldGetOperatorById;
import com.operators.operatornetworkbridge.interfaces.EmeraldSetOperatorForMachine;
import com.operators.operatornetworkbridge.interfaces.GetOperatorByIdNetworkManagerInterface;
import com.operators.operatornetworkbridge.interfaces.SetOperatorForMachineNetworkManagerInterface;
import com.operators.reportfieldsformachinenetworkbridge.interfaces.EmeraldGetReportFieldsForMachineRequest;
import com.operators.reportfieldsformachinenetworkbridge.interfaces.GetReportFieldsForMachineNetworkManagerInterface;
import com.operators.reportrejectnetworkbridge.interfaces.ApproveFirstItemNetworkManagerInterface;
import com.operators.reportrejectnetworkbridge.interfaces.EmeraldGetAllRecipe;
import com.operators.reportrejectnetworkbridge.interfaces.EmeraldGetDepartment;
import com.operators.reportrejectnetworkbridge.interfaces.EmeraldGetIntervalAndTimeOut;
import com.operators.reportrejectnetworkbridge.interfaces.EmeraldGetJobDetails;
import com.operators.reportrejectnetworkbridge.interfaces.EmeraldGetPendingJobList;
import com.operators.reportrejectnetworkbridge.interfaces.EmeraldGetVersion;
import com.operators.reportrejectnetworkbridge.interfaces.EmeraldPostActivateJob;
import com.operators.reportrejectnetworkbridge.interfaces.EmeraldPostSplitEvent;
import com.operators.reportrejectnetworkbridge.interfaces.EmeraldPostUpdateActions;
import com.operators.reportrejectnetworkbridge.interfaces.EmeraldPostUpdateNotesForJob;
import com.operators.reportrejectnetworkbridge.interfaces.EmeraldReportMultipleRejects;
import com.operators.reportrejectnetworkbridge.interfaces.EmeraldSendApproveFirstItem;
import com.operators.reportrejectnetworkbridge.interfaces.EmeraldSendReportCycleUnits;
import com.operators.reportrejectnetworkbridge.interfaces.EmeraldSendReportInventory;
import com.operators.reportrejectnetworkbridge.interfaces.EmeraldSendReportReject;
import com.operators.reportrejectnetworkbridge.interfaces.EmeraldSendStopReport;
import com.operators.reportrejectnetworkbridge.interfaces.GetDepartmentNetworkManager;
import com.operators.reportrejectnetworkbridge.interfaces.GetIntervalAndTimeOutNetworkManager;
import com.operators.reportrejectnetworkbridge.interfaces.GetJobDetailsNetworkManager;
import com.operators.reportrejectnetworkbridge.interfaces.GetPendingJobListNetworkManager;
import com.operators.reportrejectnetworkbridge.interfaces.GetReportMultipleRequestNetworkManager;
import com.operators.reportrejectnetworkbridge.interfaces.GetVersionNetworkManager;
import com.operators.reportrejectnetworkbridge.interfaces.PostActivateJobNetworkManager;
import com.operators.reportrejectnetworkbridge.interfaces.PostSplitEventNetworkManager;
import com.operators.reportrejectnetworkbridge.interfaces.PostUpdateNotesForJobNetworkManager;
import com.operators.reportrejectnetworkbridge.interfaces.PostUpdtaeActionsNetworkManager;
import com.operators.reportrejectnetworkbridge.interfaces.RecipeNetworkManagerInterface;
import com.operators.reportrejectnetworkbridge.interfaces.ReportCycleUnitsNetworkManagerInterface;
import com.operators.reportrejectnetworkbridge.interfaces.ReportInventoryNetworkManagerInterface;
import com.operators.reportrejectnetworkbridge.interfaces.ReportRejectNetworkManagerInterface;
import com.operators.reportrejectnetworkbridge.interfaces.ReportStopNetworkManagerInterface;
import com.operators.shiftlognetworkbridge.interfaces.EmeraldActualBarExtraDetailsServiceRequest;
import com.operators.shiftlognetworkbridge.interfaces.EmeraldShiftForMachineServiceRequests;
import com.operators.shiftlognetworkbridge.interfaces.EmeraldShiftLogServiceRequests;
import com.operators.shiftlognetworkbridge.interfaces.ShiftLogNetworkManagerInterface;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.server.interfaces.OpAppServiceRequests;
import com.operatorsapp.server.requests.GetTopRejectsAndEventsRequest;
import com.operatorsapp.server.requests.NotificationHistoryRequest;
import com.operatorsapp.server.requests.PostDeleteTokenRequest;
import com.operatorsapp.server.requests.PostIncrementCounterRequest;
import com.operatorsapp.server.requests.PostNotificationTokenRequest;
import com.operatorsapp.server.requests.PostTechnicianCallRequest;
import com.operatorsapp.server.requests.RespondToNotificationRequest;
import com.operatorsapp.server.requests.SendNotificationRequest;
import com.operatorsapp.server.requests.TopNotificationRequest;
import com.operatorsapp.server.responses.AppVersionResponse;
import com.operatorsapp.server.responses.NotificationHistoryResponse;
import com.operatorsapp.server.responses.StopAndCriticalEventsResponse;
import com.operatorsapp.server.responses.TopRejectResponse;
import com.operatorsapp.utils.SendReportUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import okhttp3.ConnectionPool;
import okhttp3.Dispatcher;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class NetworkManager implements LoginNetworkManagerInterface,
        GetMachineNetworkManagerInterface,
        GetMachineStatusNetworkManagerInterface,
        GetJobsListForMachineNetworkManagerInterface,
        StartJobForMachineNetworkManagerInterface,
        GetOperatorByIdNetworkManagerInterface,
        SetOperatorForMachineNetworkManagerInterface,
        ShiftLogNetworkManagerInterface,
        GetReportFieldsForMachineNetworkManagerInterface,
        ReportRejectNetworkManagerInterface,
        GetMachineDataNetworkManagerInterface,
        ReportStopNetworkManagerInterface,
        ReportCycleUnitsNetworkManagerInterface,
        ReportInventoryNetworkManagerInterface,
        ActiveJobsListForMachineNetworkManagerInterface,
        ApproveFirstItemNetworkManagerInterface,
        RecipeNetworkManagerInterface,
        GetVersionNetworkManager,
        GetPendingJobListNetworkManager,
        GetJobDetailsNetworkManager,
        PostUpdtaeActionsNetworkManager,
        PostActivateJobNetworkManager,
        PostUpdateNotesForJobNetworkManager,
        PostSplitEventNetworkManager,
        GetIntervalAndTimeOutNetworkManager,
        GetReportMultipleRequestNetworkManager,
        GetDepartmentNetworkManager {
    private static final String LOG_TAG = NetworkManager.class.getSimpleName();
    private static NetworkManager msInstance;
    private HashMap<String, EmeraldLoginServiceRequests> mEmeraldServiceRequestsHashMap = new HashMap<>();
    private Retrofit mRetrofit;
    private OkHttpClient okHttpClient;
    HeaderInterceptor headerInterceptor = new HeaderInterceptor();

    public static NetworkManager initInstance() {
        if (msInstance == null) {
            msInstance = new NetworkManager();
        }

        return msInstance;
    }

    public static NetworkManager getInstance() {
        if (msInstance == null) {
            OppAppLogger.getInstance().e(LOG_TAG, "getInstance(), fail, NetworkManager is not init");
        }
        return msInstance;
    }

    public NetworkManager() {
    }


    @Override
    public EmeraldLoginServiceRequests getLoginRetroFitServiceRequests(String siteUrl) {
        // -1  to get default timeout
        return getLoginRetroFitServiceRequests(siteUrl, -1, null);
    }

    @Override
    public EmeraldLoginServiceRequests getLoginRetroFitServiceRequests(String siteUrl, int timeout, TimeUnit timeUnit) {
        if (mEmeraldServiceRequestsHashMap.containsKey(siteUrl)) {
            return mEmeraldServiceRequestsHashMap.get(siteUrl);
        } else {
            mRetrofit = getRetrofit(siteUrl, timeout, timeUnit);

            EmeraldLoginServiceRequests emeraldLoginServiceRequests = mRetrofit.create(EmeraldLoginServiceRequests.class);


            mEmeraldServiceRequestsHashMap.put(siteUrl, emeraldLoginServiceRequests);


            return emeraldLoginServiceRequests;
        }
    }

    @Override
    public EmeraldGetMachinesServiceRequests getMachinesRetroFitServiceRequests(String siteUrl) {
        try {

            return getMachinesRetroFitServiceRequests(siteUrl, -1, null);

        } catch (RuntimeException e) {

            SendReportUtil.sendAcraExeption(e, "getMachinesRetroFitServiceRequests");
        }

        return getMachinesRetroFitServiceRequests(siteUrl, -1, null);
    }

    @Override
    public EmeraldGetMachinesServiceRequests getMachinesRetroFitServiceRequests(String siteUrl, int timeout, TimeUnit timeUnit) {
        mRetrofit = getRetrofit(siteUrl, timeout, timeUnit);
        try {

            return mRetrofit.create(EmeraldGetMachinesServiceRequests.class);

        } catch (RuntimeException e) {

            SendReportUtil.sendAcraExeption(e, "getMachinesRetroFitServiceRequests");
        }
        return mRetrofit.create(EmeraldGetMachinesServiceRequests.class);

    }

    @Override
    public EmeraldShiftLogServiceRequests getShiftLogRetroFitServiceRequests(String siteUrl) {
        try {

            return getShiftLogRetroFitServiceRequests(siteUrl, -1, null);

        } catch (RuntimeException e) {

            SendReportUtil.sendAcraExeption(e, "getShiftLogRetroFitServiceRequests");
        }
        return getShiftLogRetroFitServiceRequests(siteUrl, -1, null);
    }

    @Override
    public EmeraldShiftLogServiceRequests getShiftLogRetroFitServiceRequests(String siteUrl, int timeout, TimeUnit timeUnit) {
        mRetrofit = getRetrofit(siteUrl, timeout, timeUnit);
        try {

            return mRetrofit.create(EmeraldShiftLogServiceRequests.class);

        } catch (RuntimeException e) {

            SendReportUtil.sendAcraExeption(e, "getShiftLogRetroFitServiceRequests");
        }
        return mRetrofit.create(EmeraldShiftLogServiceRequests.class);
    }

    @Override
    public EmeraldGetMachinesDataServiceRequest getMachineDataRetroFitServiceRequests(String siteUrl) {
        try {

            return getMachineDataRetroFitServiceRequests(siteUrl, -1, null);

        } catch (RuntimeException e) {

            SendReportUtil.sendAcraExeption(e, "getMachineDataRetroFitServiceRequests");
        }
        return getMachineDataRetroFitServiceRequests(siteUrl, -1, null);
    }

    @Override
    public EmeraldGetMachinesDataServiceRequest getMachineDataRetroFitServiceRequests(String siteUrl, int timeout, TimeUnit timeUnit) {
        mRetrofit = getRetrofit(siteUrl, timeout, timeUnit);
        try {

            return mRetrofit.create(EmeraldGetMachinesDataServiceRequest.class);

        } catch (RuntimeException e) {

            SendReportUtil.sendAcraExeption(e, "getMachineDataRetroFitServiceRequests");
        }
        return mRetrofit.create(EmeraldGetMachinesDataServiceRequest.class);
    }

    @Override
    public EmeraldShiftForMachineServiceRequests getShiftForMachineServiceRequests(String siteUrl) {
        try {

            return getShiftForMachineServiceRequests(siteUrl, -1, null);

        } catch (RuntimeException e) {

            SendReportUtil.sendAcraExeption(e, "getShiftForMachineServiceRequests");
        }
        return getShiftForMachineServiceRequests(siteUrl, -1, null);
    }

    @Override
    public EmeraldShiftForMachineServiceRequests getShiftForMachineServiceRequests(String siteUrl, int timeout, TimeUnit timeUnit) {
        mRetrofit = getRetrofit(siteUrl, timeout, timeUnit);
        try {

            return mRetrofit.create(EmeraldShiftForMachineServiceRequests.class);

        } catch (RuntimeException e) {

            SendReportUtil.sendAcraExeption(e, "getShiftForMachineServiceRequests");
        }
        return mRetrofit.create(EmeraldShiftForMachineServiceRequests.class);
    }

    @Override
    public EmeraldActualBarExtraDetailsServiceRequest getActualBarExtraDetails(String siteUrl, int timeout, TimeUnit timeUnit) {
        mRetrofit = getRetrofit(siteUrl, timeout, timeUnit);
        try {

            return mRetrofit.create(EmeraldActualBarExtraDetailsServiceRequest.class);

        } catch (RuntimeException e) {

            SendReportUtil.sendAcraExeption(e, "getActualBarExtraDetails");
        }
        return mRetrofit.create(EmeraldActualBarExtraDetailsServiceRequest.class);
    }

    @Override
    public EmeraldGetMachineJoshDataRequest getMachineJoshDataServiceRequest(String siteUrl, int timeout, TimeUnit timeUnit) {
        mRetrofit = getRetrofit(siteUrl, timeout, timeUnit);
        try {

            return mRetrofit.create(EmeraldGetMachineJoshDataRequest.class);

        } catch (RuntimeException e) {

            SendReportUtil.sendAcraExeption(e, "GetMachineJoshData");
        }
        return mRetrofit.create(EmeraldGetMachineJoshDataRequest.class);
    }

    @Override
    public EmeraldReportMultipleRejects emeraldReportMultipleRejects(String siteUrl, int timeout, TimeUnit timeUnit) {
        mRetrofit = getRetrofit(siteUrl, timeout, timeUnit);
        try {

            return mRetrofit.create(EmeraldReportMultipleRejects.class);

        } catch (RuntimeException e) {

            SendReportUtil.sendAcraExeption(e, "getMultipleReportRejectsNetworkManager");
        }
        return mRetrofit.create(EmeraldReportMultipleRejects.class);
    }

    private Retrofit getRetrofit(String siteUrl, int timeout, TimeUnit timeUnit) {
        ConnectionPool pool = new ConnectionPool(5, 10000, TimeUnit.MILLISECONDS);

        try {
            if (mRetrofit == null || !mRetrofit.baseUrl().toString().equals(siteUrl)) {
                Dispatcher dispatcher = new okhttp3.Dispatcher();
                dispatcher.setMaxRequests(1);
                HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                if (okHttpClient == null) {
                    if (timeout >= 0 && timeUnit != null) {
                        okHttpClient = new OkHttpClient.Builder()
                                .connectionPool(pool)
                                .connectTimeout(timeout, timeUnit)
                                .writeTimeout(timeout, timeUnit)
                                .readTimeout(timeout, timeUnit)
                                .addInterceptor(headerInterceptor)
                                .addInterceptor(loggingInterceptor)
                                .dispatcher(dispatcher)
                                .build();
                    } else {
                        okHttpClient = new OkHttpClient.Builder()
                                .connectionPool(pool)
                                .dispatcher(dispatcher)
                                .addInterceptor(headerInterceptor)
                                .addInterceptor(loggingInterceptor)
                                .build();
                    }
                } else if (timeUnit != null) {

                    int millis = timeUnitToMillis(timeUnit, timeout);

                    if (millis != okHttpClient.connectTimeoutMillis()
                            || !okHttpClient.interceptors().contains(headerInterceptor)) {
                        okHttpClient = okHttpClient.newBuilder()
                                .connectTimeout(timeout, timeUnit)
                                .writeTimeout(timeout, timeUnit)
                                .addInterceptor(headerInterceptor)
                                .addInterceptor(loggingInterceptor)
                                .readTimeout(timeout, timeUnit)
                                .build();
                    }
                }
//                Gson builder = new GsonBuilder().disableHtmlEscaping().create(); to disable encoding if needed but need to check if can cause bug in one of the request in the app and in server side
                mRetrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(siteUrl).client(okHttpClient).build();

            }
        } catch (IllegalArgumentException | IllegalStateException e) {

            String s = "siteUrl" + siteUrl;

            if (siteUrl == null) {

                s += " null";
            }

            SendReportUtil.sendAcraExeption(e, "getRetrofit " + s);

            mRetrofit = new Retrofit.Builder().build();

        } catch (RuntimeException e) {
            if (e.getMessage() != null)

                Log.e(LOG_TAG, e.getMessage());

            SendReportUtil.sendAcraExeption(e, "getRetrofit " + siteUrl);

            mRetrofit = new Retrofit.Builder().build();

        }

        return mRetrofit;

    }

    public class HeaderInterceptor implements Interceptor {

        private static final String HEADER_TOKEN_KEY = "x-app-key";//"token";//
        private static final String HEADER_LANGUAGE_KEY = "Language";
        private static final String HEADER_PLATFORM_KEY = "Platform";
        private static final String TIME_ZONE = "TimeZone";

        @Override
        public Response intercept(Chain chain) throws IOException {
            PersistenceManager persistenceManager = PersistenceManager.getInstance();
            Request requestToReturn = chain.request().newBuilder().build();
            if (chain.request().headers().get(HEADER_TOKEN_KEY) == null && persistenceManager.getUserName() != null && persistenceManager.getPassword() != null) {
                requestToReturn = chain.request().newBuilder()
                        .addHeader(HEADER_TOKEN_KEY, Base64.encodeToString(String.format("%s;%s", persistenceManager.getUserName(), persistenceManager.getPassword()).getBytes(), Base64.NO_WRAP))
                        .addHeader(HEADER_LANGUAGE_KEY, persistenceManager.getCurrentLang())
                        .addHeader(HEADER_PLATFORM_KEY, LoginRequest.PLATFORM)
                        .addHeader(TIME_ZONE, TimeZone.getDefault().getID())
                        .build();
            }
            Log.d(LOG_TAG, "intercept: " + " : "
                    + Base64.encodeToString(String.format("%s;%s", persistenceManager.getUserName(), persistenceManager.getPassword()).getBytes(), Base64.NO_WRAP)
                    + String.format("%s;%s", persistenceManager.getUserName(), persistenceManager.getPassword())
                    + persistenceManager.getCurrentLang()
                    + LoginRequest.PLATFORM);
            return chain.proceed(requestToReturn);
        }
    }

    private int timeUnitToMillis(TimeUnit timeUnit, int timeout) {

        int millis = 0;
        if (timeout > 0) {

            switch (timeUnit) {

                case DAYS:
                    millis = timeout * 24 * 60 * 60 * 1000;
                    break;
                case HOURS:
                    millis = timeout * 60 * 60 * 1000;
                    break;
                case MINUTES:
                    millis = timeout * 60 * 1000;
                    break;
                case SECONDS:
                    millis = timeout * 1000;
                    break;
                case NANOSECONDS:
                    millis = timeout / 1000 / 1000;
                    break;
                case MICROSECONDS:
                    millis = timeout / 1000;
                    break;
                case MILLISECONDS:
                    millis = timeout;
                    break;
            }
        } else {
            millis = 0;
        }

        return millis;
    }


    @Override
    public EmeraldGetMachinesStatusServiceRequest getMachineStatusRetroFitServiceRequests(String siteUrl) {
        try {

            return getMachineStatusRetroFitServiceRequests(siteUrl, -1, null);

        } catch (RuntimeException e) {

            SendReportUtil.sendAcraExeption(e, "getMachineStatusRetroFitServiceRequests");
        }
        return getMachineStatusRetroFitServiceRequests(siteUrl, -1, null);
    }

    @Override
    public EmeraldGetMachinesStatusServiceRequest getMachineStatusRetroFitServiceRequests(String siteUrl, int timeout, TimeUnit timeUnit) {
        mRetrofit = getRetrofit(siteUrl, timeout, timeUnit);

        try {

            return mRetrofit.create(EmeraldGetMachinesStatusServiceRequest.class);

        } catch (RuntimeException e) {

            SendReportUtil.sendAcraExeption(e, "getMachineStatusRetroFitServiceRequests");
        }
        return mRetrofit.create(EmeraldGetMachinesStatusServiceRequest.class);
    }

    @Override
    public EmeraldSetProductionModeForMachineRequest postProductionModeForMachineRetroFitServiceRequests(String siteUrl) {
        try {

            return postProductionModeForMachineRetroFitServiceRequests(siteUrl, -1, null);

        } catch (RuntimeException e) {

            SendReportUtil.sendAcraExeption(e, "postProductionModeForMachineRetroFitServiceRequests");
        }
        return postProductionModeForMachineRetroFitServiceRequests(siteUrl, -1, null);
    }

    @Override
    public EmeraldSetProductionModeForMachineRequest postProductionModeForMachineRetroFitServiceRequests(String siteUrl, int timeout, TimeUnit timeUnit) {
        mRetrofit = getRetrofit(siteUrl, timeout, timeUnit);
        try {

            return mRetrofit.create(EmeraldSetProductionModeForMachineRequest.class);

        } catch (RuntimeException e) {

            SendReportUtil.sendAcraExeption(e, "postProductionModeForMachineRetroFitServiceRequests");
        }
        return mRetrofit.create(EmeraldSetProductionModeForMachineRequest.class);
    }

    @Override
    public EmeraldGetJobsListServiceRequests getJobListForMachineStatusRetroFitServiceRequests(String siteUrl) {
        try {

            return getJobListForMachineStatusRetroFitServiceRequests(siteUrl, -1, null);

        } catch (RuntimeException e) {

            SendReportUtil.sendAcraExeption(e, "getJobListForMachineStatusRetroFitServiceRequests");
        }
        return getJobListForMachineStatusRetroFitServiceRequests(siteUrl, -1, null);
    }

    @Override
    public EmeraldGetJobsListServiceRequests getJobListForMachineStatusRetroFitServiceRequests(String siteUrl, int timeout, TimeUnit timeUnit) {
        mRetrofit = getRetrofit(siteUrl, timeout, timeUnit);
        try {

            return mRetrofit.create(EmeraldGetJobsListServiceRequests.class);

        } catch (RuntimeException e) {

            SendReportUtil.sendAcraExeption(e, "getJobListForMachineStatusRetroFitServiceRequests");
        }
        return mRetrofit.create(EmeraldGetJobsListServiceRequests.class);
    }

    @Override
    public EmeraldStartJobServiceRequests startJobForMachineStatusRetroFitServiceRequests(String siteUrl) {
        try {

            return startJobForMachineStatusRetroFitServiceRequests(siteUrl, -1, null);

        } catch (RuntimeException e) {

            SendReportUtil.sendAcraExeption(e, "startJobForMachineStatusRetroFitServiceRequests");
        }
        return startJobForMachineStatusRetroFitServiceRequests(siteUrl, -1, null);
    }

    @Override
    public EmeraldStartJobServiceRequests startJobForMachineStatusRetroFitServiceRequests(String siteUrl, int timeout, TimeUnit timeUnit) {
        mRetrofit = getRetrofit(siteUrl, timeout, timeUnit);
        try {

            return mRetrofit.create(EmeraldStartJobServiceRequests.class);

        } catch (RuntimeException e) {

            SendReportUtil.sendAcraExeption(e, "startJobForMachineStatusRetroFitServiceRequests");
        }
        return mRetrofit.create(EmeraldStartJobServiceRequests.class);
    }

    @Override
    public EmeraldGetOperatorById getOperatorByIdRetroFitServiceRequests(String siteUrl) {
        try {

            return getOperatorByIdRetroFitServiceRequests(siteUrl, -1, null);

        } catch (RuntimeException e) {

            SendReportUtil.sendAcraExeption(e, "getOperatorByIdRetroFitServiceRequests");
        }
        return getOperatorByIdRetroFitServiceRequests(siteUrl, -1, null);
    }

    @Override
    public EmeraldGetOperatorById getOperatorByIdRetroFitServiceRequests(String siteUrl, int timeout, TimeUnit timeUnit) {

        mRetrofit = getRetrofit(siteUrl, timeout, timeUnit);
        try {

            return mRetrofit.create(EmeraldGetOperatorById.class);

        } catch (RuntimeException e) {

            SendReportUtil.sendAcraExeption(e, "getOperatorByIdRetroFitServiceRequests");
        }
        return mRetrofit.create(EmeraldGetOperatorById.class);
    }

    @Override
    public EmeraldSetOperatorForMachine setOperatorForMachineRetroFitServiceRequests(String siteUrl) {
        try {

            return setOperatorForMachineRetroFitServiceRequests(siteUrl, -1, null);

        } catch (RuntimeException e) {

            SendReportUtil.sendAcraExeption(e, "setOperatorForMachineRetroFitServiceRequests");
        }
        return setOperatorForMachineRetroFitServiceRequests(siteUrl, -1, null);
    }

    @Override
    public EmeraldSetOperatorForMachine setOperatorForMachineRetroFitServiceRequests(String siteUrl, int timeout, TimeUnit timeUnit) {
        mRetrofit = getRetrofit(siteUrl, timeout, timeUnit);
        try {

            return mRetrofit.create(EmeraldSetOperatorForMachine.class);

        } catch (RuntimeException e) {

            SendReportUtil.sendAcraExeption(e, "setOperatorForMachineRetroFitServiceRequests");
        }
        return mRetrofit.create(EmeraldSetOperatorForMachine.class);
    }

    @Override
    public EmeraldGetReportFieldsForMachineRequest getReportFieldsForMachineStatusRetroFitServiceRequests(String siteUrl) {
        try {

            return getReportFieldsForMachineStatusRetroFitServiceRequests(siteUrl, -1, null);

        } catch (RuntimeException e) {

            SendReportUtil.sendAcraExeption(e, "getReportFieldsForMachineStatusRetroFitServiceRequests");
        }
        return getReportFieldsForMachineStatusRetroFitServiceRequests(siteUrl, -1, null);
    }

    @Override
    public EmeraldGetReportFieldsForMachineRequest getReportFieldsForMachineStatusRetroFitServiceRequests(String siteUrl, int timeout, TimeUnit timeUnit) {
        mRetrofit = getRetrofit(siteUrl, timeout, timeUnit);
        try {

            return mRetrofit.create(EmeraldGetReportFieldsForMachineRequest.class);

        } catch (RuntimeException e) {

            SendReportUtil.sendAcraExeption(e, "getReportFieldsForMachineStatusRetroFitServiceRequests");
        }
        return mRetrofit.create(EmeraldGetReportFieldsForMachineRequest.class);
    }

    @Override
    public EmeraldSendReportReject reportRejectRetroFitServiceRequests(String siteUrl) {
        try {

            return reportRejectRetroFitServiceRequests(siteUrl, -1, null);

        } catch (RuntimeException e) {

            SendReportUtil.sendAcraExeption(e, "reportRejectRetroFitServiceRequests");
        }
        return reportRejectRetroFitServiceRequests(siteUrl, -1, null);

    }

    @Override
    public EmeraldSendReportReject reportRejectRetroFitServiceRequests(String siteUrl, int timeout, TimeUnit timeUnit) {
        mRetrofit = getRetrofit(siteUrl, timeout, timeUnit);
        try {

            return mRetrofit.create(EmeraldSendReportReject.class);

        } catch (RuntimeException e) {

            SendReportUtil.sendAcraExeption(e, "reportRejectRetroFitServiceRequests");
        }
        return mRetrofit.create(EmeraldSendReportReject.class);
    }

    @Override
    public EmeraldSendStopReport reportStopRetroFitServiceRequests(String siteUrl) {


        try {

            return reportStopRetroFitServiceRequests(siteUrl, -1, null);

        } catch (RuntimeException e) {


            SendReportUtil.sendAcraExeption(e, "reportStopRetroFitServiceRequests");
        }
        return reportStopRetroFitServiceRequests(siteUrl, -1, null);

    }

    @Override
    public EmeraldSendStopReport reportStopRetroFitServiceRequests(String siteUrl, int timeout, TimeUnit timeUnit) {

        mRetrofit = getRetrofit(siteUrl, timeout, timeUnit);

        try {

            return mRetrofit.create(EmeraldSendStopReport.class);

        } catch (RuntimeException e) {


            SendReportUtil.sendAcraExeption(e, "reportStopRetroFitServiceRequests");
        }
        return mRetrofit.create(EmeraldSendStopReport.class);
    }

    @Override
    public EmeraldSendReportCycleUnits reportCycleUnitsRetroFitServiceRequests(String siteUrl) {

        try {

            return reportCycleUnitsRetroFitServiceRequests(siteUrl, -1, null);

        } catch (RuntimeException e) {

            SendReportUtil.sendAcraExeption(e, "reportCycleUnitsRetroFitServiceRequests");
        }
        return reportCycleUnitsRetroFitServiceRequests(siteUrl, -1, null);
    }

    @Override
    public EmeraldSendReportCycleUnits reportCycleUnitsRetroFitServiceRequests(String siteUrl, int timeout, TimeUnit timeUnit) {
        mRetrofit = getRetrofit(siteUrl, timeout, timeUnit);
        try {

            return mRetrofit.create(EmeraldSendReportCycleUnits.class);

        } catch (RuntimeException e) {

            SendReportUtil.sendAcraExeption(e, "reportCycleUnitsRetroFitServiceRequests");
        }
        return mRetrofit.create(EmeraldSendReportCycleUnits.class);
    }

    @Override
    public EmeraldSendReportInventory reportInventoryRetroFitServiceRequests(String siteUrl) {
        try {

            return reportInventoryRetroFitServiceRequests(siteUrl, -1, null);

        } catch (RuntimeException e) {

            SendReportUtil.sendAcraExeption(e, "reportInventoryRetroFitServiceRequests");
        }
        return reportInventoryRetroFitServiceRequests(siteUrl, -1, null);
    }

    @Override
    public EmeraldSendReportInventory reportInventoryRetroFitServiceRequests(String siteUrl, int timeout, TimeUnit timeUnit) {
        mRetrofit = getRetrofit(siteUrl, timeout, timeUnit);
        try {

            return mRetrofit.create(EmeraldSendReportInventory.class);

        } catch (RuntimeException e) {

            SendReportUtil.sendAcraExeption(e, "reportInventoryRetroFitServiceRequests");
        }
        return mRetrofit.create(EmeraldSendReportInventory.class);
    }

    @Override
    public EmeraldGetActiveJobsListForMachineServiceRequests getActiveJobListForMachineStatusRetroFitServiceRequests(String siteUrl) {

        try {

            return getActiveJobListForMachineStatusRetroFitServiceRequests(siteUrl, -1, null);

        } catch (RuntimeException e) {

            SendReportUtil.sendAcraExeption(e, "getActiveJobListForMachineStatusRetroFitServiceRequests");
        }
        return getActiveJobListForMachineStatusRetroFitServiceRequests(siteUrl, -1, null);
    }

    @Override
    public EmeraldGetActiveJobsListForMachineServiceRequests getActiveJobListForMachineStatusRetroFitServiceRequests(String siteUrl, int timeout, TimeUnit timeUnit) {
        mRetrofit = getRetrofit(siteUrl, timeout, timeUnit);

        try {

            return mRetrofit.create(EmeraldGetActiveJobsListForMachineServiceRequests.class);

        } catch (RuntimeException e) {

            SendReportUtil.sendAcraExeption(e, "getActiveJobListForMachineStatusRetroFitServiceRequests");
        }
        return mRetrofit.create(EmeraldGetActiveJobsListForMachineServiceRequests.class);
    }

    @Override
    public EmeraldSendApproveFirstItem approveFirstItemRetroFitServiceRequests(String siteUrl, int timeout, TimeUnit timeUnit) {
        mRetrofit = getRetrofit(siteUrl, timeout, timeUnit);

        try {
            return mRetrofit.create(EmeraldSendApproveFirstItem.class);

        } catch (RuntimeException e) {

            SendReportUtil.sendAcraExeption(e, "approveFirstItemRetroFitServiceRequests");
        }
        return mRetrofit.create(EmeraldSendApproveFirstItem.class);
    }

    public void clearPollingRequest() {

        try {
            okHttpClient.connectionPool().evictAll();

        } catch (Exception e) {
            if (e.getMessage() != null)

                Log.e(LOG_TAG, e.getMessage());
        }
    }

    @Override
    public EmeraldGetAllRecipe emeraldGetAllRecipe(String siteUrl, int timeout, TimeUnit timeUnit) {
        mRetrofit = getRetrofit(siteUrl, timeout, timeUnit);

        try {
            return mRetrofit.create(EmeraldGetAllRecipe.class);

        } catch (RuntimeException e) {

            SendReportUtil.sendAcraExeption(e, "approveEmeraldGetAllRecipeRequests");
        }
        return mRetrofit.create(EmeraldGetAllRecipe.class);
    }

    @Override
    public EmeraldGetVersion emeraldGetVersion(String siteUrl, int timeout, TimeUnit timeUnit) {

        mRetrofit = getRetrofit(siteUrl, timeout, timeUnit);

        try {
            return mRetrofit.create(EmeraldGetVersion.class);

        } catch (RuntimeException e) {

            SendReportUtil.sendAcraExeption(e, "approveEmeraldGetVersionRequests");
        }
        return mRetrofit.create(EmeraldGetVersion.class);
    }

    @Override
    public EmeraldGetIntervalAndTimeOut emeraldGetIntervalAndTimeOut(String siteUrl, int timeout, TimeUnit timeUnit) {

        mRetrofit = getRetrofit(siteUrl, timeout, timeUnit);

        try {
            return mRetrofit.create(EmeraldGetIntervalAndTimeOut.class);

        } catch (RuntimeException e) {

            SendReportUtil.sendAcraExeption(e, "approveEmeraldGetIntervalAndTimeOutRequests");
        }
        return mRetrofit.create(EmeraldGetIntervalAndTimeOut.class);
    }

    @Override
    public EmeraldGetPendingJobList emeraldGetPendingJobList(String siteUrl, int timeout, TimeUnit timeUnit) {
        mRetrofit = getRetrofit(siteUrl, timeout, timeUnit);

        try {
            return mRetrofit.create(EmeraldGetPendingJobList.class);

        } catch (RuntimeException e) {

            SendReportUtil.sendAcraExeption(e, "approveEmeraldGetPendingJobListRequests");
        }
        return mRetrofit.create(EmeraldGetPendingJobList.class);
    }

    @Override
    public EmeraldGetJobDetails emeraldGetJobDetails(String siteUrl, int timeout, TimeUnit timeUnit) {
        mRetrofit = getRetrofit(siteUrl, timeout, timeUnit);

        try {
            return mRetrofit.create(EmeraldGetJobDetails.class);

        } catch (RuntimeException e) {

            SendReportUtil.sendAcraExeption(e, "approveEmeraldGetJobDetailsRequests");
        }
        return mRetrofit.create(EmeraldGetJobDetails.class);
    }

    @Override
    public EmeraldPostUpdateActions emeraldpostUpdateActions(String siteUrl, int timeout, TimeUnit timeUnit) {

        mRetrofit = getRetrofit(siteUrl, timeout, timeUnit);

        try {
            return mRetrofit.create(EmeraldPostUpdateActions.class);

        } catch (RuntimeException e) {

            SendReportUtil.sendAcraExeption(e, "approveEmeraldPostUpdateActions");
        }
        return mRetrofit.create(EmeraldPostUpdateActions.class);
    }

    @Override
    public EmeraldPostActivateJob emeraldPostActivateJob(String siteUrl, int timeout, TimeUnit timeUnit) {
        mRetrofit = getRetrofit(siteUrl, timeout, timeUnit);

        try {
            return mRetrofit.create(EmeraldPostActivateJob.class);

        } catch (RuntimeException e) {

            SendReportUtil.sendAcraExeption(e, "approveEmeraldPostActivateJob");
        }
        return mRetrofit.create(EmeraldPostActivateJob.class);
    }

    @Override
    public EmeraldPostSplitEvent emeraldPostSplitEvent(String siteUrl, int timeout, TimeUnit timeUnit) {
        mRetrofit = getRetrofit(siteUrl, timeout, timeUnit);

        try {
            return mRetrofit.create(EmeraldPostSplitEvent.class);

        } catch (RuntimeException e) {

            SendReportUtil.sendAcraExeption(e, "approveEmeraldPostSplitEvent");
        }
        return mRetrofit.create(EmeraldPostSplitEvent.class);
    }

    @Override
    public EmeraldGetDepartment emeraldGetDepartment(String siteUrl, int timeout, TimeUnit timeUnit) {
        mRetrofit = getRetrofit(siteUrl, timeout, timeUnit);

        try {
            return mRetrofit.create(EmeraldGetDepartment.class);

        } catch (RuntimeException e) {

            SendReportUtil.sendAcraExeption(e, "approveEmeraldGetDepartment");
        }
        return mRetrofit.create(EmeraldGetDepartment.class);
    }

    @Override
    public EmeraldPostUpdateNotesForJob emeraldPostUpdateNotesForJob(String siteUrl, int timeout, TimeUnit timeUnit) {
        mRetrofit = getRetrofit(siteUrl, timeout, timeUnit);

        try {
            return mRetrofit.create(EmeraldPostUpdateNotesForJob.class);

        } catch (RuntimeException e) {

            SendReportUtil.sendAcraExeption(e, "approveEmeraldPostUpdateNotesForJob");
        }
        return mRetrofit.create(EmeraldPostUpdateNotesForJob.class);
    }

    public void postNotificationToken(PostNotificationTokenRequest request, final Callback<StandardResponse> callback) {

        mRetrofit = getRetrofit(PersistenceManager.getInstance().getSiteUrl(), PersistenceManager.getInstance().getRequestTimeout(), TimeUnit.SECONDS);
        Call<StandardResponse> call = mRetrofit.create(OpAppServiceRequests.class).postNotificationTokenRequest(request);
        call.enqueue(callback);

    }

    public void getNotificationHistory(final Callback<NotificationHistoryResponse> callback) {

        PersistenceManager pm = PersistenceManager.getInstance();
        NotificationHistoryRequest request = new NotificationHistoryRequest(pm.getSessionId(), pm.getMachineId());
        mRetrofit = getRetrofit(pm.getSiteUrl(), pm.getRequestTimeout(), TimeUnit.SECONDS);

        Call<NotificationHistoryResponse> call = mRetrofit.create(OpAppServiceRequests.class).getNotificationHistoryRequest(request);
        call.enqueue(callback);
    }

    public void postTechnicianCall(PostTechnicianCallRequest request, final Callback<StandardResponse> callback) {

        mRetrofit = getRetrofit(PersistenceManager.getInstance().getSiteUrl(), PersistenceManager.getInstance().getRequestTimeout(), TimeUnit.SECONDS);
        Call<StandardResponse> call = mRetrofit.create(OpAppServiceRequests.class).postTechnicianCallRequest(request);
        call.enqueue(callback);

    }

    public void postResponseToNotification(RespondToNotificationRequest request, final Callback<StandardResponse> callback) {

        mRetrofit = getRetrofit(PersistenceManager.getInstance().getSiteUrl(), PersistenceManager.getInstance().getRequestTimeout(), TimeUnit.SECONDS);
        Call<StandardResponse> call = mRetrofit.create(OpAppServiceRequests.class).postNotificationResponse(request);
        call.enqueue(callback);
    }

    public void postIncrementCounter(PostIncrementCounterRequest request, final Callback<StandardResponse> callback) {
        mRetrofit = getRetrofit(PersistenceManager.getInstance().getSiteUrl(), PersistenceManager.getInstance().getRequestTimeout(), TimeUnit.SECONDS);
        Call<StandardResponse> call = mRetrofit.create(OpAppServiceRequests.class).postIncrementCounterRequest(request);
        call.enqueue(callback);
    }

    public void getTopStopAndCriticalEvents(GetTopRejectsAndEventsRequest request, final Callback<StopAndCriticalEventsResponse> callback) {
        mRetrofit = getRetrofit(PersistenceManager.getInstance().getSiteUrl(), PersistenceManager.getInstance().getRequestTimeout(), TimeUnit.SECONDS);
        Call<StopAndCriticalEventsResponse> call = mRetrofit.create(OpAppServiceRequests.class).getStopAndCriticalEventsRequest(request);
        call.enqueue(callback);
    }

    public void getTopRejects(GetTopRejectsAndEventsRequest request, final Callback<TopRejectResponse> callback) {
        mRetrofit = getRetrofit(PersistenceManager.getInstance().getSiteUrl(), PersistenceManager.getInstance().getRequestTimeout(), TimeUnit.SECONDS);
        Call<TopRejectResponse> call = mRetrofit.create(OpAppServiceRequests.class).getRejects(request);
        call.enqueue(callback);
    }

    public void getServiceCalls(BaseTimeRequest request, final Callback<ServiceCallsResponse> callback) {
        mRetrofit = getRetrofit(PersistenceManager.getInstance().getSiteUrl(), PersistenceManager.getInstance().getRequestTimeout(), TimeUnit.SECONDS);
        Call<ServiceCallsResponse> call = mRetrofit.create(OpAppServiceRequests.class).getServiceCalls(request);
        call.enqueue(callback);
    }

    public void getDepartmentShiftGraph(DepartmentShiftGraphRequest request, final Callback<DepartmentShiftGraphResponse> callback) {
        mRetrofit = getRetrofit(PersistenceManager.getInstance().getSiteUrl(), PersistenceManager.getInstance().getRequestTimeout(), TimeUnit.SECONDS);
        Call<DepartmentShiftGraphResponse> call = mRetrofit.create(OpAppServiceRequests.class).getDepartmentShiftGraph(request);
        call.enqueue(callback);
    }

    public void postDeleteToken(PostDeleteTokenRequest request, final Callback<StandardResponse> callback) {
        mRetrofit = getRetrofit(PersistenceManager.getInstance().getSiteUrl(), PersistenceManager.getInstance().getRequestTimeout(), TimeUnit.SECONDS);
        Call<StandardResponse> call = mRetrofit.create(OpAppServiceRequests.class).postDeleteToken(request);
        call.enqueue(callback);
    }

//    public void getMachineJoshData(MachineJoshDataRequest request, final Callback<MachineJoshDataResponse> callback) {
//        mRetrofit = getRetrofit(PersistenceManager.getInstance().getSiteUrl(), PersistenceManager.getInstance().getRequestTimeout(), TimeUnit.SECONDS);
//        Call<MachineJoshDataResponse> call = mRetrofit.create(OpAppServiceRequests.class).getMachineJoshData(request);
//        call.enqueue(callback);
//    }
//
//    public void getNewVersionFile(final Callback<ResponseBody> callback) {
//        mRetrofit = getRetrofit("http://www.ovh.net", PersistenceManager.getInstance().getRequestTimeout(), TimeUnit.SECONDS);
//        Call<ResponseBody> call = mRetrofit.create(OpAppServiceRequests.class).getNewVersionFile();
//        call.enqueue(callback);
//    }


    public void GetApplicationVersion(final Callback<AppVersionResponse> callback) {
        mRetrofit = getRetrofit(PersistenceManager.getInstance().getSiteUrl(), PersistenceManager.getInstance().getRequestTimeout(), TimeUnit.SECONDS);
        Call<AppVersionResponse> call = mRetrofit.create(OpAppServiceRequests.class).GetApplicationVersion();
        call.enqueue(callback);
    }

    public void postSendNotification(SendNotificationRequest request, final Callback<NotificationHistoryResponse> callback) {
        mRetrofit = getRetrofit(PersistenceManager.getInstance().getSiteUrl(), PersistenceManager.getInstance().getRequestTimeout(), TimeUnit.SECONDS);
        Call<NotificationHistoryResponse> call = mRetrofit.create(OpAppServiceRequests.class).sendNotification(request);
        call.enqueue(callback);
    }

    public void getTopNotification(TopNotificationRequest request, final Callback<NotificationHistoryResponse> callback) {
        mRetrofit = getRetrofit(PersistenceManager.getInstance().getSiteUrl(), PersistenceManager.getInstance().getRequestTimeout(), TimeUnit.SECONDS);
        Call<NotificationHistoryResponse> call = mRetrofit.create(OpAppServiceRequests.class).getTopNotifications(request);
        call.enqueue(callback);
    }

    public void getPermissionForMachine(MachineIdRequest request, final Callback<PermissionResponse> callback) {
        mRetrofit = getRetrofit(PersistenceManager.getInstance().getSiteUrl(), PersistenceManager.getInstance().getRequestTimeout(), TimeUnit.SECONDS);
        Call<PermissionResponse> call = mRetrofit.create(OpAppServiceRequests.class).getPermissionForMachine(request);
        call.enqueue(callback);
    }

    public void getQCTestOrder(TestOrderRequest request, final Callback<TestOrderResponse> callback) {
        mRetrofit = getRetrofit(PersistenceManager.getInstance().getSiteUrl(), PersistenceManager.getInstance().getRequestTimeout(), TimeUnit.SECONDS);
        Call<TestOrderResponse> call = mRetrofit.create(OpAppServiceRequests.class).getQCTestOrder(request);
        call.enqueue(callback);
    }

    public void postQCSendTestOrder(TestOrderSendRequest request, final Callback<StandardResponse> callback) {
        mRetrofit = getRetrofit(PersistenceManager.getInstance().getSiteUrl(), PersistenceManager.getInstance().getRequestTimeout(), TimeUnit.SECONDS);
        Call<StandardResponse> call = mRetrofit.create(OpAppServiceRequests.class).postQCSendTestOrder(request);
        call.enqueue(callback);
    }

    public void getQCTestDetails(TestDetailsRequest request, final Callback<TestDetailsResponse> callback) {
        mRetrofit = getRetrofit(PersistenceManager.getInstance().getSiteUrl(), PersistenceManager.getInstance().getRequestTimeout(), TimeUnit.SECONDS);
        Call<TestDetailsResponse> call = mRetrofit.create(OpAppServiceRequests.class).getQCTestDetails(request);
        call.enqueue(callback);
    }

    public void postQCSaveTestDetails(TestDetailsResponse request, final Callback<SaveTestDetailsResponse> callback) {
        mRetrofit = getRetrofit(PersistenceManager.getInstance().getSiteUrl(), PersistenceManager.getInstance().getRequestTimeout(), TimeUnit.SECONDS);
        Call<SaveTestDetailsResponse> call = mRetrofit.create(OpAppServiceRequests.class).postQCSaveTestDetails(request);
        call.enqueue(callback);
    }

}