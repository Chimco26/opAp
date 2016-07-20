package com.operatorsapp.server.mocks;

import android.util.Log;

import com.operatorsapp.application.OperatorApplication;
import com.operatorsapp.utils.NetworkAvailable;

import java.io.IOException;
import java.util.Locale;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class RetrofitMockClient implements Interceptor {
    private static final String LOG_TAG = RetrofitMockClient.class.getSimpleName();
    private static final String MIME_TYPE = "application/json";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response;
        boolean isStatusMock = !NetworkAvailable.isNetworkAvailable(OperatorApplication.getAppContext());
        if (isStatusMock) {
            String responseString;

            final HttpUrl url = chain.request().url();

            // Parse the url String.
            final String[] parsedUrl = url.toString().split("/");
            // parsedUrl.length-1 = last split
            switch (parsedUrl[parsedUrl.length - 1]) {
                case "JGetUserSessionID":
                    responseString = "{\"JGetUserSessionIDResult\":{\"error\":null,\"session\":[{\"session\":\"42547.7052039699\"}]}}";
                    break;
                case "GetMachinesForFactory":
                    responseString = "{\n" +
                            "  \"error\": null,\n" +
                            "  \"machines\": [\n" +
                            "    {\n" +
                            "      \"DefaultControllerFieldName\": null,\n" +
                            "      \"Department\": 0,\n" +
                            "      \"DisplayOrder\": 0,\n" +
                            "      \"Id\": 123,\n" +
                            "      \"MachineLName\": \"0393\",\n" +
                            "      \"MachineName\": \"0393 - OREN\",\n" +
                            "      \"MachineStatus\": 0\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"DefaultControllerFieldName\": null,\n" +
                            "      \"Department\": 0,\n" +
                            "      \"DisplayOrder\": 0,\n" +
                            "      \"Id\": 1234,\n" +
                            "      \"MachineLName\": \"1587\",\n" +
                            "      \"MachineName\": \"1587 - YOSSI\",\n" +
                            "      \"MachineStatus\": 0\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"DefaultControllerFieldName\": null,\n" +
                            "      \"Department\": 0,\n" +
                            "      \"DisplayOrder\": 0,\n" +
                            "      \"Id\": 12345,\n" +
                            "      \"MachineLName\": \"1777\",\n" +
                            "      \"MachineName\": \"1777 - OPP\",\n" +
                            "      \"MachineStatus\": 0\n" +
                            "    }\n" +
                            "  ]\n" +
                            "}";
                    break;
                case "GetMachineData":
                    responseString = "{\"error\":null,\"machineStatus\":{\n" +
                            "    \"MachineID\": 29,\n" +
                            "    \"MachineLname\": \"Pokemon creator\",\n" +
                            "    \"MachineName\": \"65\",\n" +
                            "    \"MachineStatusEname\": \"Stop\",\n" +
                            "    \"MachineStatusID\": 3,\n" +
                            "    \"MachineStatusName\": \"6\",\t\n" +
                            "    \"MachineStatusEname\": \"Processing\",\n" +
                            "    \"OperatorID\": 234,\n" +
                            "    \"operatorName\": \"opi the operator\",\n" +
                            "    \"productName\": \"Pokemon\",\n" +
                            "    \"productId\": 1234,\n" +
                            "    \"jobId\": 35,\n" +
                            "    \"shiftId\": 45,\n" +
                            "    \"shiftEndingIn\": 10000 \n" +
                            "    }\n" +
                            "}";
                    break;
                case "getJobsListForMachine":
                    responseString = "{\"error\":null,\n" +
                            "\"titleFields\":\n" +
                            "[\"jobId\",\"productName\",\"ERP\",\"plannedStart\",\"numberOfUnits\"],\n" +
                            "\"jobs\":\n" +
                            "    [\n" +
                            "    {\n" +
                            "\"jobId\": 123,\n" +
                            "\"productName\": \"some name\",\n" +
                            "\"ERP\": 6547,\n" +
                            "\"plannedStart\": \"01/02/2016\",\n" +
                            "\"numberOfUnits\": 999     \n" +
                            "    },\n" +
                            "    {\n" +
                            "\"jbId\": 456,\n" +
                            "\"productName\":\"some other name\",\n" +
                            "\"ERP\": 3214,\n" +
                            "\"plannedStart\": \"01/05/2016\",\n" +
                            "\"numberOfUnits\": 7     \n" +
                            "    }\n" +
                            "    ]\n" +
                            "}\n";
                    break;
                case "StartJobForMachine":
                    Log.i(LOG_TAG,"StartJobForMachine request received");
                    responseString = "{\"error\":null}";
                    break;
                default:
                    responseString = "";
                    break;
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
