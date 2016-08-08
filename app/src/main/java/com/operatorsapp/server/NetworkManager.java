package com.operatorsapp.server;

import com.operators.getmachinesnetworkbridge.interfaces.EmeraldGetMachinesServiceRequests;
import com.operators.getmachinesnetworkbridge.interfaces.GetMachineNetworkManagerInterface;
import com.operators.getmachinesstatusnetworkbridge.interfaces.EmeraldGetMachinesStatusServiceRequest;
import com.operators.getmachinesstatusnetworkbridge.interfaces.GetMachineStatusNetworkManagerInterface;
import com.operators.jobsnetworkbridge.interfaces.EmeraldGetJobsListServiceRequests;
import com.operators.jobsnetworkbridge.interfaces.EmeraldStartJobServiceRequests;
import com.operators.jobsnetworkbridge.interfaces.GetJobsListForMachineNetworkManagerInterface;
import com.operators.jobsnetworkbridge.interfaces.StartJobForMachineNetworkManagerInterface;
import com.operators.loginnetworkbridge.interfaces.EmeraldLoginServiceRequests;
import com.operators.loginnetworkbridge.interfaces.LoginNetworkManagerInterface;
import com.operators.operatornetworkbridge.interfaces.EmeraldGetOperatorById;
import com.operators.operatornetworkbridge.interfaces.EmeraldSetOperatorForMachine;
import com.operators.operatornetworkbridge.interfaces.GetOperatorByIdNetworkManagerInterface;
import com.operators.operatornetworkbridge.interfaces.SetOperatorForMachineNetworkManagerInterface;
import com.operators.reportfieldsformachineinfra.ReportFieldsForMachinePersistenceManagerInterface;
import com.operators.reportfieldsformachinenetworkbridge.interfaces.EmeraldGetReportFieldsForMachineRequest;
import com.operators.reportfieldsformachinenetworkbridge.interfaces.GetReportFieldsForMachineNetworkManagerInterface;
import com.operators.reportrejectnetworkbridge.interfaces.EmeraldSendReportReject;
import com.operators.reportrejectnetworkbridge.interfaces.ReportRejectNetworkManagerInterface;
import com.operators.shiftlognetworkbridge.interfaces.EmeraldShiftLogServiceRequests;
import com.operators.shiftlognetworkbridge.interfaces.ShiftLogNetworkManagerInterface;
import com.operatorsapp.server.mocks.RetrofitMockClient;
import com.zemingo.logrecorder.ZLogger;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class NetworkManager implements LoginNetworkManagerInterface, GetMachineNetworkManagerInterface, GetMachineStatusNetworkManagerInterface, GetJobsListForMachineNetworkManagerInterface, StartJobForMachineNetworkManagerInterface, GetOperatorByIdNetworkManagerInterface,
        SetOperatorForMachineNetworkManagerInterface, ShiftLogNetworkManagerInterface, GetReportFieldsForMachineNetworkManagerInterface, ReportRejectNetworkManagerInterface {
    private static final String LOG_TAG = NetworkManager.class.getSimpleName();
    private static NetworkManager msInstance;
    private HashMap<String, EmeraldLoginServiceRequests> mEmeraldServiceRequestsHashMap = new HashMap<>();

    public static NetworkManager initInstance() {
        if (msInstance == null) {
            msInstance = new NetworkManager();
        }

        return msInstance;
    }

    public static NetworkManager getInstance() {
        if (msInstance == null) {
            ZLogger.e(LOG_TAG, "getInstance(), fail, NetworkManager is not init");
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
        }
        else {
            Retrofit retrofit = getRetrofit(siteUrl, timeout, timeUnit);

            EmeraldLoginServiceRequests emeraldLoginServiceRequests = retrofit.create(EmeraldLoginServiceRequests.class);
            mEmeraldServiceRequestsHashMap.put(siteUrl, emeraldLoginServiceRequests);
            return emeraldLoginServiceRequests;
        }
    }

    @Override
    public EmeraldGetMachinesServiceRequests getMachinesRetroFitServiceRequests(String siteUrl) {
        return getMachinesRetroFitServiceRequests(siteUrl, -1, null);
    }

    @Override
    public EmeraldGetMachinesServiceRequests getMachinesRetroFitServiceRequests(String siteUrl, int timeout, TimeUnit timeUnit) {
        Retrofit retrofit = getRetrofit(siteUrl, timeout, timeUnit);
        return retrofit.create(EmeraldGetMachinesServiceRequests.class);

    }

    @Override
    public EmeraldShiftLogServiceRequests getShiftLogRetroFitServiceRequests(String siteUrl) {
        return getShiftLogRetroFitServiceRequests(siteUrl, -1, null);
    }

    @Override
    public EmeraldShiftLogServiceRequests getShiftLogRetroFitServiceRequests(String siteUrl, int timeout, TimeUnit timeUnit) {
        Retrofit retrofit = getRetrofit(siteUrl, timeout, timeUnit);
        return retrofit.create(EmeraldShiftLogServiceRequests.class);
    }

    private Retrofit getRetrofit(String siteUrl, int timeout, TimeUnit timeUnit) {
        OkHttpClient okHttpClient;
        if (timeout >= 0 && timeUnit != null) {
            okHttpClient = new OkHttpClient.Builder()
                    //add mock
                    .addInterceptor(new RetrofitMockClient())
                    .connectTimeout(timeout, timeUnit)
                    .writeTimeout(timeout, timeUnit)
                    .readTimeout(timeout, timeUnit)
//                    .sslSocketFactory(sslContext.getSocketFactory())
                    .build();
        }
        else {
            okHttpClient = new OkHttpClient.Builder()
                    //add mock
                    .addInterceptor(new RetrofitMockClient())
                    .build();
        }
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(siteUrl)
                .client(okHttpClient)
                .build();
    }

    @Override
    public EmeraldGetMachinesStatusServiceRequest getMachineStatusRetroFitServiceRequests(String siteUrl) {
        return getMachineStatusRetroFitServiceRequests(siteUrl, -1, null);
    }

    @Override
    public EmeraldGetMachinesStatusServiceRequest getMachineStatusRetroFitServiceRequests(String siteUrl, int timeout, TimeUnit timeUnit) {
        Retrofit retrofit = getRetrofit(siteUrl, timeout, timeUnit);
        return retrofit.create(EmeraldGetMachinesStatusServiceRequest.class);
    }

    @Override
    public EmeraldGetJobsListServiceRequests getJobListForMachineStatusRetroFitServiceRequests(String siteUrl) {
        return getJobListForMachineStatusRetroFitServiceRequests(siteUrl, -1, null);
    }

    @Override
    public EmeraldGetJobsListServiceRequests getJobListForMachineStatusRetroFitServiceRequests(String siteUrl, int timeout, TimeUnit timeUnit) {
        Retrofit retrofit = getRetrofit(siteUrl, timeout, timeUnit);
        return retrofit.create(EmeraldGetJobsListServiceRequests.class);
    }

    @Override
    public EmeraldStartJobServiceRequests startJobForMachineStatusRetroFitServiceRequests(String siteUrl) {
        return startJobForMachineStatusRetroFitServiceRequests(siteUrl, -1, null);
    }

    @Override
    public EmeraldStartJobServiceRequests startJobForMachineStatusRetroFitServiceRequests(String siteUrl, int timeout, TimeUnit timeUnit) {
        Retrofit retrofit = getRetrofit(siteUrl, timeout, timeUnit);
        return retrofit.create(EmeraldStartJobServiceRequests.class);
    }

    @Override
    public EmeraldGetOperatorById getOperatorByIdRetroFitServiceRequests(String siteUrl) {
        return getOperatorByIdRetroFitServiceRequests(siteUrl, -1, null);
    }

    @Override
    public EmeraldGetOperatorById getOperatorByIdRetroFitServiceRequests(String siteUrl, int timeout, TimeUnit timeUnit) {
        Retrofit retrofit = getRetrofit(siteUrl, timeout, timeUnit);
        return retrofit.create(EmeraldGetOperatorById.class);
    }

    @Override
    public EmeraldSetOperatorForMachine setOperatorForMachineRetroFitServiceRequests(String siteUrl) {
        return setOperatorForMachineRetroFitServiceRequests(siteUrl, -1, null);
    }

    @Override
    public EmeraldSetOperatorForMachine setOperatorForMachineRetroFitServiceRequests(String siteUrl, int timeout, TimeUnit timeUnit) {
        Retrofit retrofit = getRetrofit(siteUrl, timeout, timeUnit);
        return retrofit.create(EmeraldSetOperatorForMachine.class);
    }

    @Override
    public EmeraldGetReportFieldsForMachineRequest getReportFieldsForMachineStatusRetroFitServiceRequests(String siteUrl) {
        return getReportFieldsForMachineStatusRetroFitServiceRequests(siteUrl, -1, null);
    }

    @Override
    public EmeraldGetReportFieldsForMachineRequest getReportFieldsForMachineStatusRetroFitServiceRequests(String siteUrl, int timeout, TimeUnit timeUnit) {
        Retrofit retrofit = getRetrofit(siteUrl, timeout, timeUnit);
        return retrofit.create(EmeraldGetReportFieldsForMachineRequest.class);
    }

    @Override
    public EmeraldSendReportReject reportRejectRetroFitServiceRequests(String siteUrl) {
        return reportRejectRetroFitServiceRequests(siteUrl, -1, null);

    }

    @Override
    public EmeraldSendReportReject reportRejectRetroFitServiceRequests(String siteUrl, int timeout, TimeUnit timeUnit) {
        Retrofit retrofit = getRetrofit(siteUrl, timeout, timeUnit);
        return retrofit.create(EmeraldSendReportReject.class);
    }
}