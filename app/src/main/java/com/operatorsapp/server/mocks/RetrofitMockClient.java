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
                            "      \"priority\": 1,\n" +
                            "      \"EventDuration\": 102960000,\n" +
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
                            "      \"priority\": 2,\n" +
                            "      \"EventDuration\": 4032000,\n" +
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
                            "      \"priority\": 2,\n" +
                            "      \"EventDuration\": 1296000,\n" +
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
                            "      \"priority\": 2,\n" +
                            "      \"EventDuration\": 3168000,\n" +
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
                            "      \"priority\": 1,\n" +
                            "      \"EventDuration\": 0,\n" +
                            "      \"EventEndTime\": \"12/07/2016\n17:36:00\",\n" +
                            "      \"EventGroupID\": 20,\n" +
                            "      \"EventGroupLname\": \"Alarms\",\n" +
                            "      \"EventID\": 1020,\n" +
                            "      \"EventSubTitleEname\": \"Alarms Activated\",\n" +
                            "      \"EventSubTitleLname\": \"Alarms Activated\",\n" +
                            "      \"EventTime\": \"12/07/2016 17:36:00\",\n" +
                            "      \"EventTitle\": \"0998: Alarms Active on:\nNoProgressCount\"\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"priority\": 2,\n" +
                            "      \"EventDuration\": 0,\n" +
                            "      \"EventEndTime\": \"12/07/2016\n17:55:00\",\n" +
                            "      \"EventGroupID\": 20,\n" +
                            "      \"EventGroupLname\": \"Alarms\",\n" +
                            "      \"EventID\": 1044,\n" +
                            "      \"EventSubTitleEname\": \"Alarms Activated\",\n" +
                            "      \"EventSubTitleLname\": \"Alarms Activated\",\n" +
                            "      \"EventTime\": \"12/07/2016 17:55:00\",\n" +
                            "      \"EventTitle\": \"0998: Alarms Active on:\nNoProgressCount\"\n" +
                            "    },\n" +
                            "    {\n" +
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
                case "GetMachineData":
                    responseString = /*"{\n" +
                            "  \"MachineID\": 9,\n" +
                            "  \"MachineParams\": [\n" +
                            "    {\n" +
                            "      \"CurrentValue\": \"2\",\n" +
                            "      \"FieldEName\": \"CycleTime\",\n" +
                            "      \"FieldLName\": \"זמן מחזור (שנ)\",\n" +
                            "      \"FieldName\": \"CycleTime\",\n" +
                            "      \"HighLimit\": 110,\n" +
                            "      \"ID\": 1,\n" +
                            "      \"LowLimit\": 30,\n" +
                            "      \"MachineParamHistoricData\": [{\n" +
                            "            \"Time\": 1470801600000,\n" +
                            "            \"Value\": 70\n" +
                            "      \t},\n" +
                            "      \t{\n" +
                            "            \"Time\": 1470805200000,\n" +
                            "            \"Value\": 90\n" +
                            "      \t},\n" +
                            "      \t{\n" +
                            "            \"Time\": 1470808800000,\n" +
                            "            \"Value\": 20\n" +
                            "      \t},\n" +
                            "      \t{\n" +
                            "            \"Time\": 1470812400000,\n" +
                            "            \"Value\": 82\n" +
                            "      \t},\n" +
                            "      \t{\n" +
                            "            \"Time\": 1470816000000,\n" +
                            "            \"Value\": 30\n" +
                            "      \t},\n" +
                            "      \t{\n" +
                            "            \"Time\": 1470819600000,\n" +
                            "            \"Value\": 60\n" +
                            "      \t},\n" +
                            "      \t{\n" +
                            "            \"Time\": 1470823200000,\n" +
                            "            \"Value\": 30\n" +
                            "      \t},\n" +
                            "      \t{\n" +
                            "            \"Time\": 1470826800000,\n" +
                            "            \"Value\": 20\n" +
                            "      \t},\n" +
                            "      \t{\n" +
                            "            \"Time\": 1470830400000,\n" +
                            "            \"Value\": 40\n" +
                            "      \t},\n" +
                            "      \t{\n" +
                            "            \"Time\": 1470834000000,\n" +
                            "            \"Value\": 50\n" +
                            "      \t}],\n" +
                            "      \"StandardValue\": 70,\n" +
                            "      \"fieldType\": 3,\n" +
                            "      \"isOutOfRange\": null\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"CurrentValue\": \"8\",\n" +
                            "      \"FieldEName\": \"CycleTime\",\n" +
                            "      \"FieldLName\": \"זמן מחזור (שנ)\",\n" +
                            "      \"FieldName\": \"CycleTime\",\n" +
                            "      \"HighLimit\": 110,\n" +
                            "      \"ID\": 1,\n" +
                            "      \"LowLimit\": 30,\n" +
                            "      \"MachineParamHistoricData\": [{\n" +
                            "            \"Time\": 1470639600000,\n" +
                            "            \"Value\": 50\n" +
                            "      \t},\n" +
                            "      \t{\n" +
                            "            \"Time\": 1470643200000,\n" +
                            "            \"Value\": 10\n" +
                            "      \t},\n" +
                            "      \t{\n" +
                            "            \"Time\": 1470646800000,\n" +
                            "            \"Value\": 80\n" +
                            "      \t},\n" +
                            "      \t{\n" +
                            "            \"Time\": 1470650400000,\n" +
                            "            \"Value\": 36\n" +
                            "      \t},\n" +
                            "      \t{\n" +
                            "            \"Time\": 1470654000000,\n" +
                            "            \"Value\": 12\n" +
                            "      \t}],\n" +
                            "      \"StandardValue\": 70,\n" +
                            "      \"fieldType\": 3,\n" +
                            "      \"isOutOfRange\": null\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"CurrentValue\": \"2.04\",\n" +
                            "      \"FieldEName\": \"AverageCycle\",\n" +
                            "      \"FieldLName\": \"זמן מחזור ממוצע (שנ)\",\n" +
                            "      \"FieldName\": \"AverageCycle\",\n" +
                            "      \"HighLimit\": null,\n" +
                            "      \"ID\": 2,\n" +
                            "      \"LowLimit\": null,\n" +
                            "      \"MachineParamHistoricData\": [],\n" +
                            "      \"StandardValue\": 30,\n" +
                            "      \"fieldType\": 0,\n" +
                            "      \"isOutOfRange\": null\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"CurrentValue\": \"39\",\n" +
                            "      \"FieldEName\": \"TotalCycles\",\n" +
                            "      \"FieldLName\": \"מספר מחזורים\",\n" +
                            "      \"FieldName\": \"TotalCycles\",\n" +
                            "      \"HighLimit\": null,\n" +
                            "      \"ID\": 8,\n" +
                            "      \"LowLimit\": null,\n" +
                            "      \"MachineParamHistoricData\": [],\n" +
                            "      \"StandardValue\": 59,\n" +
                            "      \"fieldType\": 0,\n" +
                            "      \"isOutOfRange\": null\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"CurrentValue\": \"2\",\n" +
                            "      \"FieldEName\": \"Time Left Hr\",\n" +
                            "      \"FieldLName\": \"זמן לסיום (דק)\",\n" +
                            "      \"FieldName\": \"TimeLeftHr\",\n" +
                            "      \"HighLimit\": 35,\n" +
                            "      \"ID\": 12,\n" +
                            "      \"LowLimit\": 16,\n" +
                            "      \"MachineParamHistoricData\": [],\n" +
                            "      \"StandardValue\": 25,\n" +
                            "      \"fieldType\": 1,\n" +
                            "      \"isOutOfRange\": true\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"CurrentValue\": \"230\",\n" +
                            "      \"FieldEName\": \"CavitiesPC\",\n" +
                            "      \"FieldLName\": \"CavitiesPC\",\n" +
                            "      \"FieldName\": \"CavitiesPC\",\n" +
                            "      \"HighLimit\": 220.8,\n" +
                            "      \"ID\": 31,\n" +
                            "      \"LowLimit\": 22.5,\n" +
                            "      \"MachineParamHistoricData\": [],\n" +
                            "      \"StandardValue\": 110,\n" +
                            "      \"fieldType\": 1,\n" +
                            "      \"isOutOfRange\": true\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"CurrentValue\": \"26\",\n" +
                            "      \"FieldEName\": \"NoProgressCount\",\n" +
                            "      \"FieldLName\": \"NoProgressCount\",\n" +
                            "      \"FieldName\": \"NoProgressCount\",\n" +
                            "      \"HighLimit\": 35,\n" +
                            "      \"ID\": 3583,\n" +
                            "      \"LowLimit\": 25,\n" +
                            "      \"MachineParamHistoricData\": [],\n" +
                            "      \"StandardValue\": 30,\n" +
                            "      \"fieldType\": 1,\n" +
                            "      \"isOutOfRange\": false\n" +
                            "    }\n" +
//                            "    {\n" +
//                            "     \"CurrentValue\": \"0.26\",\n" +
//                            "     \"FieldEName\": \"PEE Projection Testing\",\n" +
//                            "     \"FieldLName\": \"PEE Projection Testing\",\n" +
//                            "     \"FieldName\": \"PEE Projection Testing\",\n" +
//                            "     \"HighLimit\": null,\n" +
//                            "     \"ID\":999999999,\n" +
//                            "     \"LowLimit\":null,\n" +
//                            "     \"MachineParamHistoricData\":[],\n" +
//                            "     \"Projection\":30,\n" +
//                            "     \"StandardValue\":24,\n" +
//                            "     \"Target\":50,\n" +
//                            "     \"fieldType\":2,\n" +
//                            "     \"isOutOfRange\":null\n" +
//                            "     }\n" +
                            "  ],\n" +
                            "  \"error\": null\n" +
                            "}*/
                            "{\n" +
                            "  \"MachineID\": 9,\n" +
                            "  \"MachineParams\": [\n" +
                            "    {\n" +
                            "      \"CurrentValue\": \"19\",\n" +
                            "      \"FieldEName\": \"CycleTime\",\n" +
                            "      \"FieldLName\": \"זמן מחזור (שנ)\",\n" +
                            "      \"FieldName\": \"CycleTime\",\n" +
                            "      \"HighLimit\": 1100,\n" +
                            "      \"ID\": 1,\n" +
                            "      \"LowLimit\": 900,\n" +
                                    "      \"MachineParamHistoricData\": [{\n" +
                                    "            \"Time\": 1470801600000,\n" +
                                    "            \"Value\": 70\n" +
                                    "      \t},\n" +
                                    "      \t{\n" +
                                    "            \"Time\": 1470805200000,\n" +
                                    "            \"Value\": 90\n" +
                                    "      \t},\n" +
                                    "      \t{\n" +
                                    "            \"Time\": 1470808800000,\n" +
                                    "            \"Value\": 20\n" +
                                    "      \t},\n" +
                                    "      \t{\n" +
                                    "            \"Time\": 1470812400000,\n" +
                                    "            \"Value\": 82\n" +
                                    "      \t},\n" +
                                    "      \t{\n" +
                                    "            \"Time\": 1470816000000,\n" +
                                    "            \"Value\": 30\n" +
                                    "      \t},\n" +
                                    "      \t{\n" +
                                    "            \"Time\": 1470819600000,\n" +
                                    "            \"Value\": 60\n" +
                                    "      \t},\n" +
                                    "      \t{\n" +
                                    "            \"Time\": 1470823200000,\n" +
                                    "            \"Value\": 30\n" +
                                    "      \t},\n" +
                                    "      \t{\n" +
                                    "            \"Time\": 1470826800000,\n" +
                                    "            \"Value\": 20\n" +
                                    "      \t},\n" +
                                    "      \t{\n" +
                                    "            \"Time\": 1470830400000,\n" +
                                    "            \"Value\": 40\n" +
                                    "      \t},\n" +
                                    "      \t{\n" +
                                    "            \"Time\": 1470834000000,\n" +
                                    "            \"Value\": 50\n" +
                                    "      \t}],\n" +
                            "      \"Projection\": null,\n" +
                            "      \"StandardValue\": 19,\n" +
                            "      \"Target\": null,\n" +
                            "      \"fieldType\": 3,\n" +
                            "      \"isOutOfRange\": null\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"CurrentValue\": \"18.95\",\n" +
                            "      \"FieldEName\": \"AverageCycle\",\n" +
                            "      \"FieldLName\": \"זמן מחזור ממוצע (שנ)\",\n" +
                            "      \"FieldName\": \"AverageCycle\",\n" +
                            "      \"HighLimit\": null,\n" +
                            "      \"ID\": 2,\n" +
                            "      \"LowLimit\": null,\n" +
                            "      \"MachineParamHistoricData\": [],\n" +
                            "      \"Projection\": null,\n" +
                            "      \"StandardValue\": null,\n" +
                            "      \"Target\": null,\n" +
                            "      \"fieldType\": 0,\n" +
                            "      \"isOutOfRange\": null\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"CurrentValue\": \"58\",\n" +
                            "      \"FieldEName\": \"TotalCycles\",\n" +
                            "      \"FieldLName\": \"מספר מחזורים\",\n" +
                            "      \"FieldName\": \"TotalCycles\",\n" +
                            "      \"HighLimit\": null,\n" +
                            "      \"ID\": 8,\n" +
                            "      \"LowLimit\": null,\n" +
                            "      \"MachineParamHistoricData\": [],\n" +
                            "      \"Projection\": null,\n" +
                            "      \"StandardValue\": null,\n" +
                            "      \"Target\": null,\n" +
                            "      \"fieldType\": 0,\n" +
                            "      \"isOutOfRange\": null\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"CurrentValue\": \"422\",\n" +
                            "      \"FieldEName\": \"Time Left Hr\",\n" +
                            "      \"FieldLName\": \"זמן לסיום (דק)\",\n" +
                            "      \"FieldName\": \"TimeLeftHr\",\n" +
                            "      \"HighLimit\": 142560,\n" +
                            "      \"ID\": 12,\n" +
                            "      \"LowLimit\": 16,\n" +
                            "      \"MachineParamHistoricData\": [],\n" +
                            "      \"Projection\": null,\n" +
                            "      \"StandardValue\": null,\n" +
                            "      \"Target\": null,\n" +
                            "      \"fieldType\": 1,\n" +
                            "      \"isOutOfRange\": false\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"CurrentValue\": \"230\",\n" +
                            "      \"FieldEName\": \"CavitiesPC\",\n" +
                            "      \"FieldLName\": \"CavitiesPC\",\n" +
                            "      \"FieldName\": \"CavitiesPC\",\n" +
                            "      \"HighLimit\": 27.5,\n" +
                            "      \"ID\": 31,\n" +
                            "      \"LowLimit\": 22.5,\n" +
                            "      \"MachineParamHistoricData\": [],\n" +
                            "      \"Projection\": null,\n" +
                            "      \"StandardValue\": 230,\n" +
                            "      \"Target\": null,\n" +
                            "      \"fieldType\": 1,\n" +
                            "      \"isOutOfRange\": true\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"CurrentValue\": \"532\",\n" +
                            "      \"FieldEName\": \"NoProgressCount\",\n" +
                            "      \"FieldLName\": \"NoProgressCount\",\n" +
                            "      \"FieldName\": \"NoProgressCount\",\n" +
                            "      \"HighLimit\": 1100,\n" +
                            "      \"ID\": 3583,\n" +
                            "      \"LowLimit\": 900,\n" +
                            "      \"MachineParamHistoricData\": [],\n" +
                            "      \"Projection\": null,\n" +
                            "      \"StandardValue\": 532,\n" +
                            "      \"Target\": null,\n" +
                            "      \"fieldType\": 1,\n" +
                            "      \"isOutOfRange\": true\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"CurrentValue\": \"150\",\n" +
                            "      \"FieldEName\": \"PEE Projection Testing\",\n" +
                            "      \"FieldLName\": \"PEE Projection Testing\",\n" +
                            "      \"FieldName\": \"PEE Projection Testing\",\n" +
                            "      \"HighLimit\": 2000,\n" +
                            "      \"ID\": 999999999,\n" +
                            "      \"LowLimit\": 0,\n" +
                            "      \"MachineParamHistoricData\": [],\n" +
                            "      \"Projection\": 250,\n" +
                            "      \"StandardValue\": 1500,\n" +
                            "      \"Target\": 2000,\n" +
                            "      \"fieldType\": 2,\n" +
                            "      \"isOutOfRange\": null\n" +
                            "    }\n" +
                            "  ],\n" +
                            "  \"error\": null\n" +
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
                            "      \"shiftEndingIn\": 6510002\n" +
                            "    }\n" +
                            "  ],\n" +
                            "  \"error\": null\n" +
                            "}";
                    break;
                case "getJobsListForMachineGeneric":
//                case "getJobsListForMachine":
                    responseString = "{\n" +
                            "  \"error\": null,\n" +
                            "  \"Jobs\": [],\n" +
                            "  \"Headers\": [\n" +
                            "    {\n" +
                            "      \"FieldName\": \"ID\",\n" +
                            "      \"DisplayEName\": \"ID\",\n" +
                            "      \"FormID\": null,\n" +
                            "      \"linkitem\": \"Job\",\n" +
                            "      \"DisplayType\": \"num\"\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"FieldName\": \"ERPJobID\",\n" +
                            "      \"DisplayEName\": \"ERP Job ID\",\n" +
                            "      \"FormID\": null,\n" +
                            "      \"linkitem\": null,\n" +
                            "      \"DisplayType\": \"text\"\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"FieldName\": \"EndTimeTarget\",\n" +
                            "      \"DisplayEName\": \"End Time Target\",\n" +
                            "      \"FormID\": null,\n" +
                            "      \"linkitem\": \"\",\n" +
                            "      \"DisplayType\": \"date\"\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"FieldName\": \"EndTime\",\n" +
                            "      \"DisplayEName\": \"End Time Actual\",\n" +
                            "      \"FormID\": null,\n" +
                            "      \"linkitem\": \"\",\n" +
                            "      \"DisplayType\": \"date\"\n" +
                            "    }\n" +
                            "  ],\n" +
                            "  \"Data\": [\n" +
                            "    {\n" +
                            "      \"ID\": 346,\n" +
                            "      \"ERPJobID\": null,\n" +
                            "      \"ProductCatalogID\": \"0998plan-2016-03-28 12:41:44 PM-124\",\n" +
                            "      \"ProductID\": \"0998plan124\",\n" +
                            "      \"MoldID\": \"0998\",\n" +
                            "      \"UnitsTarget\": 100,\n" +
                            "      \"UnitsProduced\": 0,\n" +
                            "      \"UnitsProducedOK\": 0,\n" +
                            "      \"UnitsProducedLeft\": 100,\n" +
                            "      \"Status\": \"10 - Active\",\n" +
                            "      \"StartTimeTarget\": \"2015-10-21T22:22:00\",\n" +
                            "      \"EndTimeTarget\": \"2015-10-23T03:38:00\",\n" +
                            "      \"EndTime\": null,\n" +
                            "      \"TimeLeftHrHour\": 27.78,\n" +
                            "      \"MachineJobOrder\": 10000\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"ID\": 344,\n" +
                            "      \"ERPJobID\": \"O12-ERP B802-33.1\",\n" +
                            "      \"ProductCatalogID\": \"B802-33-4220-2016-03-28 12:41:44 PM-124\",\n" +
                            "      \"ProductID\": \"5373802 B 33124\",\n" +
                            "      \"MoldID\": \"MILLING FIXTURE\",\n" +
                            "      \"UnitsTarget\": 5,\n" +
                            "      \"UnitsProduced\": 1,\n" +
                            "      \"UnitsProducedOK\": 1,\n" +
                            "      \"UnitsProducedLeft\": 4,\n" +
                            "      \"Status\": \"10 - Active\",\n" +
                            "      \"StartTimeTarget\": \"2015-10-19T19:00:00\",\n" +
                            "      \"EndTimeTarget\": \"2016-06-26T19:07:17\",\n" +
                            "      \"EndTime\": \"2015-10-15T15:17:06\",\n" +
                            "      \"TimeLeftHrHour\": 3.48,\n" +
                            "      \"MachineJobOrder\": 1\n" +
                            "    }\n" +
                            "  ]\n" +
                            "}";
                    break;
                case "StartJobListForMachine":
                    Log.i(LOG_TAG, "StartJobForMachine request received");
                    responseString = "{\"error\":null}";
                    break;
                case "ReportInventory":
                    responseString = "{\"error\":null}";
                    break;
                case "GetOperatorById":
                    responseString = "{\n" +
                            "  \"Operator\": {\n" +
                            "    \"OperatorID\": 2222,\n" +
                            "    \"OperatorName\": \"Sergey \"\n" +
                            "  },\n" +
                            "  \"error\": null\n" +
                            "}";
                    break;
                case "SetOperatorForMachine":
                    Log.i(LOG_TAG, "SetOperatorForMachine request received");
                    responseString = "{\"error\":null}";
                    break;
                case "ReportCycleUnits":
                    responseString = "{\"error\":null}";
                    break;
                case "ReportReject":
                    Log.i(LOG_TAG, "ReportReject request received");
                    responseString = "{\"error\":null}";
                    break;
                case "ReportStop": {
                    Log.i(LOG_TAG, "ReportReject request received");
                    responseString = "{\"error\":null}";
                    break;
                }
                case "GetReportFieldsForMachine":
                    responseString = "{\n" +
                            "  \"RejectCauses\": [\n" +
                            "    {\n" +
                            "      \"EName\": \"Injection\",\n" +
                            "      \"ID\": 1,\n" +
                            "      \"SubReason\": []\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"EName\": \"Machine Tuning\",\n" +
                            "      \"ID\": 110,\n" +
                            "      \"SubReason\": []\n" +
                            "    }\n" +
                            "  ],\n" +
                            "  \"RejectReason\": [\n" +
                            "    {\n" +
                            "      \"EName\": \"Injection\",\n" +
                            "      \"ID\": 1,\n" +
                            "      \"SubReason\": []\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"EName\": \"Color\",\n" +
                            "      \"ID\": 2,\n" +
                            "      \"SubReason\": []\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"EName\": \"Quantity Adjust.\",\n" +
                            "      \"ID\": 3,\n" +
                            "      \"SubReason\": []\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"EName\": \"Black Points\",\n" +
                            "      \"ID\": 110,\n" +
                            "      \"SubReason\": []\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"EName\": \"Lack of Material\",\n" +
                            "      \"ID\": 120,\n" +
                            "      \"SubReason\": []\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"EName\": \"Material Error\",\n" +
                            "      \"ID\": 130,\n" +
                            "      \"SubReason\": []\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"EName\": \"Oil on Product\",\n" +
                            "      \"ID\": 140,\n" +
                            "      \"SubReason\": []\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"EName\": \"Flash\",\n" +
                            "      \"ID\": 520,\n" +
                            "      \"SubReason\": []\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"EName\": \"Visual\",\n" +
                            "      \"ID\": 530,\n" +
                            "      \"SubReason\": []\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"EName\": \"testing\",\n" +
                            "      \"ID\": 560,\n" +
                            "      \"SubReason\": []\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"EName\": \"Rework\",\n" +
                            "      \"ID\": 570,\n" +
                            "      \"SubReason\": []\n" +
                            "    }\n" +
                            "  ],\n" +
                            "  \"StopReason\": [\n" +
                            "    {\n" +
                            "      \"EName\": \"Operation\",\n" +
                            "      \"ID\": 1,\n" +
                            "      \"SubReason\": [\n" +
                            "        {\n" +
                            "          \"EName\": \"No Communication\",\n" +
                            "          \"ID\": 18,\n" +
                            "          \"SubReason\": []\n" +
                            "        },\n" +
                            "        {\n" +
                            "          \"EName\": \"Start\",\n" +
                            "          \"ID\": 54,\n" +
                            "          \"SubReason\": []\n" +
                            "        }\n" +
                            "      ]\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"EName\": \"Maintenance\",\n" +
                            "      \"ID\": 2,\n" +
                            "      \"SubReason\": [\n" +
                            "        {\n" +
                            "          \"EName\": \"Periodic Maintenance\",\n" +
                            "          \"ID\": 7,\n" +
                            "          \"SubReason\": []\n" +
                            "        }\n" +
                            "      ]\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"EName\": \"QA\",\n" +
                            "      \"ID\": 3,\n" +
                            "      \"SubReason\": [\n" +
                            "        {\n" +
                            "          \"EName\": \"Disqualification\",\n" +
                            "          \"ID\": 10,\n" +
                            "          \"SubReason\": []\n" +
                            "        },\n" +
                            "        {\n" +
                            "          \"EName\": \"Sample\",\n" +
                            "          \"ID\": 13,\n" +
                            "          \"SubReason\": []\n" +
                            "        },\n" +
                            "        {\n" +
                            "          \"EName\": \"No Quality Authorization\",\n" +
                            "          \"ID\": 50,\n" +
                            "          \"SubReason\": []\n" +
                            "        }\n" +
                            "      ]\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"EName\": \"Malfunction\",\n" +
                            "      \"ID\": 4,\n" +
                            "      \"SubReason\": [\n" +
                            "        {\n" +
                            "          \"EName\": \"Cycle Time\",\n" +
                            "          \"ID\": 14,\n" +
                            "          \"SubReason\": []\n" +
                            "        },\n" +
                            "        {\n" +
                            "          \"EName\": \"Broken Part\",\n" +
                            "          \"ID\": 16,\n" +
                            "          \"SubReason\": []\n" +
                            "        },\n" +
                            "        {\n" +
                            "          \"EName\": \"Over Heating\",\n" +
                            "          \"ID\": 17,\n" +
                            "          \"SubReason\": []\n" +
                            "        },\n" +
                            "        {\n" +
                            "          \"EName\": \"Oil Leak\",\n" +
                            "          \"ID\": 29,\n" +
                            "          \"SubReason\": []\n" +
                            "        },\n" +
                            "        {\n" +
                            "          \"EName\": \"Oil Temperature\",\n" +
                            "          \"ID\": 30,\n" +
                            "          \"SubReason\": []\n" +
                            "        },\n" +
                            "        {\n" +
                            "          \"EName\": \"Cylinder Temp\",\n" +
                            "          \"ID\": 31,\n" +
                            "          \"SubReason\": []\n" +
                            "        },\n" +
                            "        {\n" +
                            "          \"EName\": \"Broken Ring\",\n" +
                            "          \"ID\": 32,\n" +
                            "          \"SubReason\": []\n" +
                            "        },\n" +
                            "        {\n" +
                            "          \"EName\": \"Electricity Error\",\n" +
                            "          \"ID\": 33,\n" +
                            "          \"SubReason\": []\n" +
                            "        },\n" +
                            "        {\n" +
                            "          \"EName\": \"Dirty Cilinder\",\n" +
                            "          \"ID\": 34,\n" +
                            "          \"SubReason\": []\n" +
                            "        }\n" +
                            "      ]\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"EName\": \"Machine Stop\",\n" +
                            "      \"ID\": 6,\n" +
                            "      \"SubReason\": [\n" +
                            "        {\n" +
                            "          \"EName\": \"Unreported Stop \",\n" +
                            "          \"ID\": 0,\n" +
                            "          \"SubReason\": []\n" +
                            "        },\n" +
                            "        {\n" +
                            "          \"EName\": \"Stop - General\",\n" +
                            "          \"ID\": 2,\n" +
                            "          \"SubReason\": []\n" +
                            "        },\n" +
                            "        {\n" +
                            "          \"EName\": \"Offline InActiveTime Unreported\",\n" +
                            "          \"ID\": 318,\n" +
                            "          \"SubReason\": []\n" +
                            "        },\n" +
                            "        {\n" +
                            "          \"EName\": \"Offline DownTime Unreported\",\n" +
                            "          \"ID\": 319,\n" +
                            "          \"SubReason\": []\n" +
                            "        }\n" +
                            "      ]\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"EName\": \"Material\",\n" +
                            "      \"ID\": 7,\n" +
                            "      \"SubReason\": [\n" +
                            "        {\n" +
                            "          \"EName\": \"Material (%)\",\n" +
                            "          \"ID\": 15,\n" +
                            "          \"SubReason\": []\n" +
                            "        },\n" +
                            "        {\n" +
                            "          \"EName\": \"No Material\",\n" +
                            "          \"ID\": 22,\n" +
                            "          \"SubReason\": []\n" +
                            "        },\n" +
                            "        {\n" +
                            "          \"EName\": \"Material - Iron Found\",\n" +
                            "          \"ID\": 44,\n" +
                            "          \"SubReason\": []\n" +
                            "        },\n" +
                            "        {\n" +
                            "          \"EName\": \"Material - Error\",\n" +
                            "          \"ID\": 45,\n" +
                            "          \"SubReason\": []\n" +
                            "        },\n" +
                            "        {\n" +
                            "          \"EName\": \"Material - Shading Problem\",\n" +
                            "          \"ID\": 46,\n" +
                            "          \"SubReason\": []\n" +
                            "        },\n" +
                            "        {\n" +
                            "          \"EName\": \"Material - Black Points\",\n" +
                            "          \"ID\": 47,\n" +
                            "          \"SubReason\": []\n" +
                            "        }\n" +
                            "      ]\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"EName\": \"Labor\",\n" +
                            "      \"ID\": 8,\n" +
                            "      \"SubReason\": [\n" +
                            "        {\n" +
                            "          \"EName\": \"Lack of Personal\",\n" +
                            "          \"ID\": 51,\n" +
                            "          \"SubReason\": []\n" +
                            "        }\n" +
                            "      ]\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"EName\": \"Mold\",\n" +
                            "      \"ID\": 9,\n" +
                            "      \"SubReason\": [\n" +
                            "        {\n" +
                            "          \"EName\": \"Heating\",\n" +
                            "          \"ID\": 21,\n" +
                            "          \"SubReason\": []\n" +
                            "        },\n" +
                            "        {\n" +
                            "          \"EName\": \"Broken Pin\",\n" +
                            "          \"ID\": 23,\n" +
                            "          \"SubReason\": []\n" +
                            "        },\n" +
                            "        {\n" +
                            "          \"EName\": \"Mold Protection\",\n" +
                            "          \"ID\": 24,\n" +
                            "          \"SubReason\": []\n" +
                            "        },\n" +
                            "        {\n" +
                            "          \"EName\": \"Mold - Insert Change\",\n" +
                            "          \"ID\": 36,\n" +
                            "          \"SubReason\": []\n" +
                            "        },\n" +
                            "        {\n" +
                            "          \"EName\": \"Mold - Download/Assembly\",\n" +
                            "          \"ID\": 40,\n" +
                            "          \"SubReason\": []\n" +
                            "        },\n" +
                            "        {\n" +
                            "          \"EName\": \"Mold - Assignment\",\n" +
                            "          \"ID\": 41,\n" +
                            "          \"SubReason\": []\n" +
                            "        },\n" +
                            "        {\n" +
                            "          \"EName\": \"Mold - Water Leak\",\n" +
                            "          \"ID\": 42,\n" +
                            "          \"SubReason\": []\n" +
                            "        },\n" +
                            "        {\n" +
                            "          \"EName\": \"Mold - Heaters Change\",\n" +
                            "          \"ID\": 48,\n" +
                            "          \"SubReason\": []\n" +
                            "        }\n" +
                            "      ]\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"EName\": \"Setup\",\n" +
                            "      \"ID\": 10,\n" +
                            "      \"SubReason\": [\n" +
                            "        {\n" +
                            "          \"EName\": \"Setup\",\n" +
                            "          \"ID\": 100,\n" +
                            "          \"SubReason\": []\n" +
                            "        }\n" +
                            "      ]\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"EName\": \"Expected Stops\",\n" +
                            "      \"ID\": 12,\n" +
                            "      \"SubReason\": [\n" +
                            "        {\n" +
                            "          \"EName\": \"Holiday\",\n" +
                            "          \"ID\": 53,\n" +
                            "          \"SubReason\": []\n" +
                            "        },\n" +
                            "        {\n" +
                            "          \"EName\": \"Lack of Order\",\n" +
                            "          \"ID\": 310,\n" +
                            "          \"SubReason\": []\n" +
                            "        },\n" +
                            "        {\n" +
                            "          \"EName\": \"weekend\",\n" +
                            "          \"ID\": 314,\n" +
                            "          \"SubReason\": []\n" +
                            "        }\n" +
                            "      ]\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"EName\": \"Alarms\",\n" +
                            "      \"ID\": 20,\n" +
                            "      \"SubReason\": [\n" +
                            "        {\n" +
                            "          \"EName\": \"Alarms\",\n" +
                            "          \"ID\": 300,\n" +
                            "          \"SubReason\": []\n" +
                            "        },\n" +
                            "        {\n" +
                            "          \"EName\": \"Alarms Activated\",\n" +
                            "          \"ID\": 301,\n" +
                            "          \"SubReason\": []\n" +
                            "        },\n" +
                            "        {\n" +
                            "          \"EName\": \"Alarms Disabled\",\n" +
                            "          \"ID\": 302,\n" +
                            "          \"SubReason\": []\n" +
                            "        },\n" +
                            "        {\n" +
                            "          \"EName\": \"Cycle Time Alarm\",\n" +
                            "          \"ID\": 303,\n" +
                            "          \"SubReason\": []\n" +
                            "        },\n" +
                            "        {\n" +
                            "          \"EName\": \"Color Save PC Alarm\",\n" +
                            "          \"ID\": 304,\n" +
                            "          \"SubReason\": []\n" +
                            "        },\n" +
                            "        {\n" +
                            "          \"EName\": \"Total Distance Alarm\",\n" +
                            "          \"ID\": 305,\n" +
                            "          \"SubReason\": []\n" +
                            "        },\n" +
                            "        {\n" +
                            "          \"EName\": \"Product Weight Alarm\",\n" +
                            "          \"ID\": 306,\n" +
                            "          \"SubReason\": []\n" +
                            "        },\n" +
                            "        {\n" +
                            "          \"EName\": \"SetPoint Changed\",\n" +
                            "          \"ID\": 307,\n" +
                            "          \"SubReason\": []\n" +
                            "        }\n" +
                            "      ]\n" +
                            "    }\n" +
                            "  ],\n" +
                            "  \"PackageTypes\": [\n" +
                            "    {\n" +
                            "      \"EName\": \"Test1 Mock\",\n" +
                            "      \"ID\": 1\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"EName\": \"Test2 Mock\",\n" +
                            "      \"ID\": 110\n" +
                            "    }\n" +
                            "  ],\n" +
                            "  \"error\": null\n" +
                            "}";
                    break;
                case "GetActiveJobsListForMachine":{
                    responseString = "{\n" +
                            "  \"error\":null,\n" +
                            "  \"Jobs\":\n" +
                            "  [\n" +
                            "    {\n" +
                            "      \"JobID\": 29,\n" +
                            "      \"JobName\": \"some name1\"\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"JobID\": 31,\n" +
                            "      \"JobName\": \"some name2\"\n" +
                            "    },\n" +
                            "\t{\n" +
                            "      \"JobID\": 23,\n" +
                            "      \"JobName\": \"some name3\"\n" +
                            "    },\n" +
                            "\t{\n" +
                            "      \"JobID\": 28,\n" +
                            "      \"JobName\": \"some name4\"\n" +
                            "    }\n" +
                            "  ]\n" +
                            "}";
                    break;
                }
                default:
                    responseString = "{\n" +
                            "  \"error\": null,\n" +
                            "  \"stopReasons\": \n" +
                            "  [\n" +
                            "    {\n" +
                            "      \"Id\":1,\n" +
                            "      \"Name\":\"some reason 1\",\n" +
                            "      \"subReasons\":\n" +
                            "      [\n" +
                            "        {\n" +
                            "          \"Id\":1,\n" +
                            "          \"Name\":\"some sub reason 1\"\n" +
                            "        },\n" +
                            "        {\n" +
                            "          \"Id\":2,\n" +
                            "          \"Name\":\"some sub reason 2\"\n" +
                            "        }\n" +
                            "      ]\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"Id\":2,\n" +
                            "      \"Name\":\"some reason 2\",\n" +
                            "      \"subReasons\":\n" +
                            "      [\n" +
                            "        {\n" +
                            "          \"Id\":1,\n" +
                            "          \"Name\":\"some sub reason 1\"\n" +
                            "        },\n" +
                            "        {\n" +
                            "          \"Id\":2,\n" +
                            "          \"Name\":\"some sub reason 2\"\n" +
                            "        }\n" +
                            "      ]\n" +
                            "    }\n" +
                            "  ],\n" +
                            "  \"rejectReasons\":\n" +
                            "  [\n" +
                            "    {\n" +
                            "      \"Id\":1,\n" +
                            "      \"Name\":\"some reason 1\"\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"Id\":2,\n" +
                            "      \"Name\":\"some reason 2\"\n" +
                            "    }    \n" +
                            "  ],\n" +
                            "  \"rejectCauses\":\n" +
                            "  [\n" +
                            "    {\n" +
                            "      \"Id\":1,\n" +
                            "      \"Name\":\"some cause 1\"\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"Id\":2,\n" +
                            "      \"Name\":\"some cause 2\"\n" +
                            "    }    \n" +
                            "  ]\n" +
                            "}\n";
                    break;
            }

            response = getResponse(chain, responseString);
        } else

        {
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
