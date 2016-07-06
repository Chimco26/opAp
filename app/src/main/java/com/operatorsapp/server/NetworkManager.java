package com.operatorsapp.server;

import android.support.annotation.NonNull;

import com.operators.getmachinesnetworkbridge.interfaces.EmeraldGetMachinesServiceRequests;
import com.operators.getmachinesnetworkbridge.interfaces.GetMachineNetworkManagerInterface;
import com.operators.loginnetworkbridge.interfaces.EmeraldLoginServiceRequests;
import com.operators.loginnetworkbridge.interfaces.LoginNetworkManagerInterface;
import com.operatorsapp.server.mocks.RetrofitMockClient;
import com.zemingo.logrecorder.ZLogger;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class NetworkManager implements LoginNetworkManagerInterface, GetMachineNetworkManagerInterface {
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
        } else {
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

    private Retrofit getRetrofit(String siteUrl, int timeout, TimeUnit timeUnit) {
        OkHttpClient okHttpClient;
        if (timeout >= 0 && timeUnit != null) {
            okHttpClient = new OkHttpClient.Builder()
                    //add mock
                    .addInterceptor(new RetrofitMockClient())
                    .connectTimeout(timeout, timeUnit)
                    .writeTimeout(timeout, timeUnit)
                    .readTimeout(timeout, timeUnit)
                    .build();
        } else {
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
}