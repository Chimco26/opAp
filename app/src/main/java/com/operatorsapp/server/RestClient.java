package com.operatorsapp.server;

import com.operatorsapp.mocks.RetrofitMockClient;
import com.operatorsapp.server.interfaces.EmeraldServiceRequests;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient {
    private static EmeraldServiceRequests mEmeraldServiceRequests = null;

    public static EmeraldServiceRequests getClient() {
        if (mEmeraldServiceRequests == null) {
            final OkHttpClient client = new OkHttpClient();
            // ***YOUR CUSTOM INTERCEPTOR GOES HERE***
            client.interceptors().add(new RetrofitMockClient());

            final Retrofit retrofit = new Retrofit.Builder()
                    // Using custom Jackson Converter to parse JSON
                    // Add dependencies:
                    // com.squareup.retrofit:converter-jackson:2.0.0-beta2
                    .addConverterFactory(GsonConverterFactory.create())
                    // Endpoint
                    .baseUrl(EmeraldServiceRequests.ENDPOINT)
                    .client(client)
                    .build();

            mEmeraldServiceRequests = retrofit.create(EmeraldServiceRequests.class);
        }
        return mEmeraldServiceRequests;
    }
}
