package com.operatorsapp.server.mocks;

import android.util.Log;

import com.operatorsapp.application.OperatorApplication;
import com.operatorsapp.utils.NetworkAvailable;

import java.io.IOException;

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

                case "GetMachineShiftLog":
                    responseString = "{\n" +
                            "  \"error\": null,\n" +
                            "  \"events\": [\n" +
                            "    {\n" +
                            "      \"type\": 2,\n" +
                            "      \"priority\": 1,\n" +
                            "      \"EventDuration\": 577,\n" +
                            "      \"EventEndTime\": \"13/10/2015 16:39:00\",\n" +
                            "      \"EventGroupID\": 6,\n" +
                            "      \"EventGroupLname\": \"Machine Stop\",\n" +
                            "      \"EventID\": 291,\n" +
                            "      \"EventSubTitleEname\": \"Unreported Stop \",\n" +
                            "      \"EventSubTitleLname\": \"עצירה לא מדווח\",\n" +
                            "      \"EventTime\": \"13/10/2015 07:02:00\",\n" +
                            "      \"EventTitle\": \"HAAS998: \"\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"type\": 2,\n" +
                            "      \"priority\": 2,\n" +
                            "      \"EventDuration\": 3,\n" +
                            "      \"EventEndTime\": \"14/10/2015 11:12:00\",\n" +
                            "      \"EventGroupID\": 6,\n" +
                            "      \"EventGroupLname\": \"Machine Stop\",\n" +
                            "      \"EventID\": 365,\n" +
                            "      \"EventSubTitleEname\": \"Unreported Stop \",\n" +
                            "      \"EventSubTitleLname\": \"עצירה לא מדווח\",\n" +
                            "      \"EventTime\": \"14/10/2015 11:09:00\",\n" +
                            "      \"EventTitle\": \"HAAS998: \"\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"type\": 2,\n" +
                            "      \"priority\": 2,\n" +
                            "      \"EventDuration\": 379,\n" +
                            "      \"EventEndTime\": \"14/10/2015 19:03:00\",\n" +
                            "      \"EventGroupID\": 6,\n" +
                            "      \"EventGroupLname\": \"Machine Stop\",\n" +
                            "      \"EventID\": 376,\n" +
                            "      \"EventSubTitleEname\": \"Unreported Stop \",\n" +
                            "      \"EventSubTitleLname\": \"עצירה לא מדווח\",\n" +
                            "      \"EventTime\": \"14/10/2015 12:44:00\",\n" +
                            "      \"EventTitle\": \"HAAS998: \"\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"type\": 2,\n" +
                            "      \"priority\": 2,\n" +
                            "      \"EventDuration\": 718,\n" +
                            "      \"EventEndTime\": \"15/10/2015 07:01:00\",\n" +
                            "      \"EventGroupID\": 6,\n" +
                            "      \"EventGroupLname\": \"Machine Stop\",\n" +
                            "      \"EventID\": 388,\n" +
                            "      \"EventSubTitleEname\": \"Unreported Stop \",\n" +
                            "      \"EventSubTitleLname\": \"עצירה לא מדווח\",\n" +
                            "      \"EventTime\": \"14/10/2015 19:03:00\",\n" +
                            "      \"EventTitle\": \"HAAS998: \"\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"type\": 1,\n" +
                            "      \"priority\": 1,\n" +
                            "      \"EventDuration\": 0,\n" +
                            "      \"EventEndTime\": \"12/07/2016\n17:36:00\",\n" +
                            "      \"EventGroupID\": 20,\n" +
                            "      \"EventGroupLname\": \"Alarms\",\n" +
                            "      \"EventID\": 1020,\n" +
                            "      \"EventSubTitleEname\": \"Alarms Activated\",\n" +
                            "      \"EventSubTitleLname\": \"Alarms Activated\",\n" +
                            "      \"EventTime\": \"12/07/2016\n17:36:00\",\n" +
                            "      \"EventTitle\": \"0998: Alarms Active on:\nNoProgressCount\"\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"type\": 1,\n" +
                            "      \"priority\": 2,\n" +
                            "      \"EventDuration\": 0,\n" +
                            "      \"EventEndTime\": \"12/07/2016\n17:55:00\",\n" +
                            "      \"EventGroupID\": 20,\n" +
                            "      \"EventGroupLname\": \"Alarms\",\n" +
                            "      \"EventID\": 1044,\n" +
                            "      \"EventSubTitleEname\": \"Alarms Activated\",\n" +
                            "      \"EventSubTitleLname\": \"Alarms Activated\",\n" +
                            "      \"EventTime\": \"12/07/2016\n17:55:00\",\n" +
                            "      \"EventTitle\": \"0998: Alarms Active on:\nNoProgressCount\"\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"type\": 2,\n" +
                            "      \"priority\": 2,\n" +
                            "      \"EventDuration\": 145,\n" +
                            "      \"EventEndTime\": \"12/10/2015 11:55:00\",\n" +
                            "      \"EventGroupID\": 6,\n" +
                            "      \"EventGroupLname\": \"Machine Stop\",\n" +
                            "      \"EventID\": 233,\n" +
                            "      \"EventSubTitleEname\": \"Unreported Stop \",\n" +
                            "      \"EventSubTitleLname\": \"עצירה לא מדווח\",\n" +
                            "      \"EventTime\": \"12/10/2015 09:30:00\",\n" +
                            "      \"EventTitle\": \"HAAS998: \"\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"type\": 2,\n" +
                            "      \"priority\": 2,\n" +
                            "      \"EventDuration\": 24,\n" +
                            "      \"EventEndTime\": \"12/10/2015 13:44:00\",\n" +
                            "      \"EventGroupID\": 6,\n" +
                            "      \"EventGroupLname\": \"Machine Stop\",\n" +
                            "      \"EventID\": 248,\n" +
                            "      \"EventSubTitleEname\": \"Unreported Stop \",\n" +
                            "      \"EventSubTitleLname\": \"עצירה לא מדווח\",\n" +
                            "      \"EventTime\": \"12/10/2015 13:20:00\",\n" +
                            "      \"EventTitle\": \"HAAS998: \"\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"type\": 2,\n" +
                            "      \"priority\": 2,\n" +
                            "      \"EventDuration\": 236,\n" +
                            "      \"EventEndTime\": \"12/10/2015 19:04:00\",\n" +
                            "      \"EventGroupID\": 6,\n" +
                            "      \"EventGroupLname\": \"Machine Stop\",\n" +
                            "      \"EventID\": 249,\n" +
                            "      \"EventSubTitleEname\": \"Unreported Stop \",\n" +
                            "      \"EventSubTitleLname\": \"עצירה לא מדווח\",\n" +
                            "      \"EventTime\": \"12/10/2015 15:08:00\",\n" +
                            "      \"EventTitle\": \"HAAS998: \"\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"type\": 2,\n" +
                            "      \"priority\": 2,\n" +
                            "      \"EventDuration\": 718,\n" +
                            "      \"EventEndTime\": \"13/10/2015 07:02:00\",\n" +
                            "      \"EventGroupID\": 6,\n" +
                            "      \"EventGroupLname\": \"Machine Stop\",\n" +
                            "      \"EventID\": 262,\n" +
                            "      \"EventSubTitleEname\": \"Unreported Stop \",\n" +
                            "      \"EventSubTitleLname\": \"עצירה לא מדווח\",\n" +
                            "      \"EventTime\": \"12/10/2015 19:04:00\",\n" +
                            "      \"EventTitle\": \"HAAS998: \"\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"type\": 2,\n" +
                            "      \"priority\": 1,\n" +
                            "      \"EventDuration\": 464,\n" +
                            "      \"EventEndTime\": \"15/10/2015 14:45:00\",\n" +
                            "      \"EventGroupID\": 6,\n" +
                            "      \"EventGroupLname\": \"Machine Stop\",\n" +
                            "      \"EventID\": 407,\n" +
                            "      \"EventSubTitleEname\": \"Unreported Stop \",\n" +
                            "      \"EventSubTitleLname\": \"עצירה לא מדווח\",\n" +
                            "      \"EventTime\": \"15/10/2015 07:01:00\",\n" +
                            "      \"EventTitle\": \"HAAS998: \"\n" +
                            "    }\n" +
                            "  ]\n" +
                            "}";
                    break;
                case "GetCurrentMachineStatus":
                    responseString = "{\n" +
                            "  \"DepartmentMachinePC\": [],\n" +
                            "  \"DepartmentOeePee\": [],\n" +
                            "  \"MissingMachineIds\": null,\n" +
                            "  \"allMachinesData\": [\n" +
                            "    {\n" +
                            "      \"CurrentJobID\": 346,\n" +
                            "      \"CurrentProductEname\": \"0998plan124\",\n" +
                            "      \"CurrentProductID\": 75,\n" +
                            "      \"CurrentProductName\": \"0998plan124\",\n" +
                            "      \"CurrentValue\": 0,\n" +
                            "      \"DepartmentEname\": \"Milling\",\n" +
                            "      \"DepartmentID\": 3,\n" +
                            "      \"DepartmentLname\": \"Milling\",\n" +
                            "      \"FieldEName\": null,\n" +
                            "      \"FieldLName\": null,\n" +
                            "      \"FieldName\": null,\n" +
                            "      \"HighLimit\": 0,\n" +
                            "      \"IsMoreMachineToDisplay\": false,\n" +
                            "      \"LowLimit\": 0,\n" +
                            "      \"MachineID\": 7,\n" +
                            "      \"MachineLname\": \"0998\",\n" +
                            "      \"MachineName\": \"0998 - HAAS998\",\n" +
                            "      \"MachineStatusEname\": \"Working OK\",\n" +
                            "      \"MachineStatusID\": 1,\n" +
                            "      \"MachineStatusName\": \"פעיל ותקין\",\n" +
                            "      \"NoProgressCount\": 0,\n" +
                            "      \"Row_Counter\": 7,\n" +
                            "      \"ShiftID\": 258,\n" +
                            "      \"shiftEndingIn\": 7117\n" +
                            "    }\n" +
                            "  ],\n" +
                            "  \"error\": null\n" +
                            "}";
                    break;
                case "GetJobsListForMachine":
//                case "getJobsListForMachine":
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
                            "\"jobId\": 456,\n" +
                            "\"productName\":\"some other name\",\n" +
                            "\"ERP\": 7896,\n" +
                            "\"plannedStart\": \"01/05/2016\",\n" +
                            "\"numberOfUnits\": 7     \n" +
                            "    }\n" +
                            "    ]\n" +
                            "}\n";
                    break;
                case "StartJobListForMachine":
                    Log.i(LOG_TAG, "StartJobForMachine request received");
                    responseString = "{\"error\":null}";
                    break;
                case "GetOperatorById":
                    responseString = "{\n" +
                            "  \"Operator\": {\n" +
                            "    \"OperatorID\": 2222,\n" +
                            "    \"OperatorName\": \"דוד ארביטמן\"\n" +
                            "  },\n" +
                            "  \"error\": null\n" +
                            "}";
                    break;
                case "SetOperatorForMachine":
                    Log.i(LOG_TAG, "SetOperatorForMachine request received");
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
