package com.operatorsapp.server;

import android.util.Log;

import com.operators.activejobslistformachinenetworkbridge.interfaces.ActiveJobsListForMachineNetworkManagerInterface;
import com.operators.activejobslistformachinenetworkbridge.interfaces.EmeraldGetActiveJobsListForMachineServiceRequests;
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
import com.operators.machinedatanetworkbridge.interfaces.EmeraldGetMachinesDataServiceRequest;
import com.operators.machinedatanetworkbridge.interfaces.GetMachineDataNetworkManagerInterface;
import com.operators.operatornetworkbridge.interfaces.EmeraldGetOperatorById;
import com.operators.operatornetworkbridge.interfaces.EmeraldSetOperatorForMachine;
import com.operators.operatornetworkbridge.interfaces.GetOperatorByIdNetworkManagerInterface;
import com.operators.operatornetworkbridge.interfaces.SetOperatorForMachineNetworkManagerInterface;
import com.operators.reportfieldsformachinenetworkbridge.interfaces.EmeraldGetReportFieldsForMachineRequest;
import com.operators.reportfieldsformachinenetworkbridge.interfaces.GetReportFieldsForMachineNetworkManagerInterface;
import com.operators.reportrejectinfra.GetVersionCallback;
import com.operators.reportrejectnetworkbridge.interfaces.ApproveFirstItemNetworkManagerInterface;
import com.operators.reportrejectnetworkbridge.interfaces.EmeraldGetAllRecipe;
import com.operators.reportrejectnetworkbridge.interfaces.EmeraldGetJobDetails;
import com.operators.reportrejectnetworkbridge.interfaces.EmeraldGetPendingJobList;
import com.operators.reportrejectnetworkbridge.interfaces.EmeraldGetVersion;
import com.operators.reportrejectnetworkbridge.interfaces.EmeraldSendApproveFirstItem;
import com.operators.reportrejectnetworkbridge.interfaces.EmeraldSendReportCycleUnits;
import com.operators.reportrejectnetworkbridge.interfaces.EmeraldSendReportInventory;
import com.operators.reportrejectnetworkbridge.interfaces.EmeraldSendReportReject;
import com.operators.reportrejectnetworkbridge.interfaces.EmeraldSendStopReport;
import com.operators.reportrejectnetworkbridge.interfaces.GetAllRecipeNetworkManagerInterface;
import com.operators.reportrejectnetworkbridge.interfaces.GetJobDetailsNetworkManager;
import com.operators.reportrejectnetworkbridge.interfaces.GetPendingJobListNetworkManager;
import com.operators.reportrejectnetworkbridge.interfaces.GetVersionNetworkManager;
import com.operators.reportrejectnetworkbridge.interfaces.ReportCycleUnitsNetworkManagerInterface;
import com.operators.reportrejectnetworkbridge.interfaces.ReportInventoryNetworkManagerInterface;
import com.operators.reportrejectnetworkbridge.interfaces.ReportRejectNetworkManagerInterface;
import com.operators.reportrejectnetworkbridge.interfaces.ReportStopNetworkManagerInterface;
import com.operators.shiftlognetworkbridge.interfaces.EmeraldShiftForMachineServiceRequests;
import com.operators.shiftlognetworkbridge.interfaces.EmeraldShiftLogServiceRequests;
import com.operators.shiftlognetworkbridge.interfaces.ShiftLogNetworkManagerInterface;
import com.operatorsapp.utils.SendReportUtil;
import com.zemingo.logrecorder.ZLogger;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.ConnectionPool;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
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
        GetAllRecipeNetworkManagerInterface,
        GetVersionNetworkManager,
        GetPendingJobListNetworkManager,
        GetJobDetailsNetworkManager{
    private static final String LOG_TAG = NetworkManager.class.getSimpleName();
    private static NetworkManager msInstance;
    private HashMap<String, EmeraldLoginServiceRequests> mEmeraldServiceRequestsHashMap = new HashMap<>();
    private Retrofit mRetrofit;
    private OkHttpClient okHttpClient;

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

    private Retrofit getRetrofit(String siteUrl, int timeout, TimeUnit timeUnit) {
        ConnectionPool pool = new ConnectionPool(5, 10000, TimeUnit.MILLISECONDS);

        try {
            if (mRetrofit == null || !mRetrofit.baseUrl().toString().equals(siteUrl)) {
                Dispatcher dispatcher = new okhttp3.Dispatcher();
                dispatcher.setMaxRequests(1);
                HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                if (timeout >= 0 && timeUnit != null) {
                    okHttpClient = new OkHttpClient.Builder()
                            .connectionPool(pool)
                            .connectTimeout(timeout, timeUnit)
                            .writeTimeout(timeout, timeUnit)
                            .readTimeout(timeout, timeUnit)
                            .addInterceptor(loggingInterceptor)
                            .dispatcher(dispatcher)
                            .build();
                } else {
                    okHttpClient = new OkHttpClient.Builder()
                            .connectionPool(pool)
                            .dispatcher(dispatcher)
                            .addInterceptor(loggingInterceptor)
                            .build();
                }


                mRetrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(siteUrl).client(okHttpClient).build();


            }
        } catch (IllegalArgumentException e) {

            String s = "siteUrl" + siteUrl;

            if (siteUrl == null) {

                s += " null";
            }

            SendReportUtil.sendAcraExeption(e, "getRetrofit " + s);

            mRetrofit = new Retrofit.Builder().build();

        } catch (RuntimeException e) {
            if(e.getMessage()!=null)

                Log.e(LOG_TAG,e.getMessage());

            SendReportUtil.sendAcraExeption(e, "getRetrofit " + siteUrl);

            mRetrofit = new Retrofit.Builder().build();

        }
        return mRetrofit;

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
            if(e.getMessage()!=null)

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
}