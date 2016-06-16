package com.operatorsapp.server.mocks;

import com.operatorsapp.BuildConfig;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class RetrofitMockClient implements Interceptor {
    private static final String MIME_TYPE = "application/json";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response;
        if (BuildConfig.DEBUG) {
            String responseString;

            if (chain.request().url().toString().contains("/LeaderMESApi/JGetUserSessionID")) {
                responseString = "{\"JGetUserSessionIDResult\":{\"error\":null,\"session\":[{\"session\":\"42409.48046875\"}]}}";
            } else {
                responseString = "";
            }

            response = getResponse(chain, responseString);
        } else {
            response = chain.proceed(chain.request());
        }

        return response;
    }

    private Response getResponse(Chain chain, String responseString) {
        Response response;
        response = new Response.Builder()
                .code(200)
                .message(responseString)
                .request(chain.request())
                .protocol(Protocol.HTTP_1_0)
                .body(ResponseBody.create(MediaType.parse(MIME_TYPE), responseString.getBytes()))
                .addHeader("content-type", "application/json")
                .build();
        return response;
    }
}
