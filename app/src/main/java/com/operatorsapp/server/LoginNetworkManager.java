package com.operatorsapp.server;

import com.operators.networkbridge.interfaces.EmeraldServiceRequests;
import com.operators.networkbridge.interfaces.LoginNetworkManagerInterface;
import com.operatorsapp.server.mocks.RetrofitMockClient;
import com.zemingo.logrecorder.ZLogger;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class LoginNetworkManager implements LoginNetworkManagerInterface {
    private static final String LOG_TAG = LoginNetworkManager.class.getSimpleName();
    private static LoginNetworkManager msInstance;
    private HashMap<String, EmeraldServiceRequests> mEmeraldServiceRequestsHashMap = new HashMap<>();

    public static LoginNetworkManager initInstance() {
        if (msInstance == null) {
            msInstance = new LoginNetworkManager();
        }

        return msInstance;
    }

    public static LoginNetworkManager getInstance() {
        if (msInstance == null) {
            ZLogger.e(LOG_TAG, "getInstance(), fail, LoginNetworkManager is not init");
        }
        return msInstance;
    }

    public LoginNetworkManager() {
    }

    @Override
    public EmeraldServiceRequests getRetroFitServiceRequests(String siteUrl) {
// -1  to get default timeout
        return getRetroFitServiceRequests(siteUrl, -1, null);
    }

    private EmeraldServiceRequests getRetroFitServiceRequests(String siteUrl, int timeout, TimeUnit timeUnit) {
        if (mEmeraldServiceRequestsHashMap.containsKey(siteUrl)) {
            return mEmeraldServiceRequestsHashMap.get(siteUrl);
        } else {
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
            Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(siteUrl)
                    .client(okHttpClient)
                    .build();

            EmeraldServiceRequests emeraldServiceRequests = retrofit.create(EmeraldServiceRequests.class);
            mEmeraldServiceRequestsHashMap.put(siteUrl, emeraldServiceRequests);
            return emeraldServiceRequests;
        }
    }

}