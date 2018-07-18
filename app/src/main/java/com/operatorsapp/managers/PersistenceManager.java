package com.operatorsapp.managers;

import android.content.Context;

import com.app.operatorinfra.OperatorPersistenceManagerInterface;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.operators.activejobslistformachineinfra.ActiveJobsListForMachinePersistenceManagerInterface;
import com.operators.infra.PersistenceManagerInterface;
import com.operators.jobsinfra.JobsPersistenceManagerInterface;
import com.operators.logincore.interfaces.LoginPersistenceManagerInterface;
import com.operators.machinedatainfra.interfaces.MachineDataPersistenceManagerInterface;
import com.operators.machinedatainfra.models.Widget;
import com.operators.machinestatusinfra.interfaces.MachineStatusPersistenceManagerInterface;
import com.operators.reportfieldsformachineinfra.ReportFieldsForMachinePersistenceManagerInterface;
import com.operators.reportrejectinfra.ReportPersistenceManagerInterface;
import com.ravtech.david.sqlcore.Event;
import com.operators.shiftloginfra.ShiftLogPersistenceManagerInterface;
import com.operatorsapp.utils.SecurePreferences;
import com.operatorsapp.utils.SendReportUtil;
import com.operatorsapp.utils.TimeUtils;
import com.zemingo.logrecorder.ZLogger;

import org.acra.ACRA;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class PersistenceManager implements LoginPersistenceManagerInterface,
        ShiftLogPersistenceManagerInterface, PersistenceManagerInterface, MachineStatusPersistenceManagerInterface,
        JobsPersistenceManagerInterface, OperatorPersistenceManagerInterface, ReportFieldsForMachinePersistenceManagerInterface, ReportPersistenceManagerInterface, MachineDataPersistenceManagerInterface, ActiveJobsListForMachinePersistenceManagerInterface {

    private static final String LOG_TAG = PersistenceManager.class.getSimpleName();

    private static final String PREF_SITE_URL = "pref.PREF_SITE_URL";
    private static final String PREF_USERNAME = "pref.PREF_USERNAME";
    private static final String PREF_PASSWORD = "pref.PREF_PASSWORD";
    private static final String PREF_SESSION_ID = "pref.PREF_SESSION_ID";
    private static final String PREF_MACHINE_ID = "pref.PREF_MACHINE_ID";
    private static final String PREF_SELECTED_MACHINE = "pref.PREF_SELECTED_MACHINE";
    private static final String PREF_DISPLAY_REJECTED_FACTOR = "pref.PREF_DISPLAY_REJECTED_FACTOR";
    private static final String PREF_ADD_REJECTS_ON_SETUP = "pref.PREF_ADD_REJECTS_ON_SETUP";
    private static final String PREF_MIN_EVENT_DURATION = "pref.PREF_MIN_EVENT_DURATION";
    private static final String PREF_TOTAL_RETRIES = "pref.PREF_TOTAL_RETRIES";
    private static final String PREF_REQUEST_TIMEOUT = "pref.PREF_REQUEST_TIMEOUT";
    private static final String PREF_ARRAY_SHIFT_LOGS = "pref.PREF_ARRAY_SHIFT_LOGS";
    private static final String PREF_ARRAY_CHART_HISTORIC_DATA = "pref.PREF_ARRAY_CHART_HISTORIC_DATA";
    private static final String PREF_JOB_ID = "pref.PREF_JOB_ID";
    private static final String PREF_OPERATOR_ID = "pref.PREF_OPERATOR_ID";
    private static final String PREF_OPERATOR_NAME = "pref.PREF_OPERATOR_NAME";
    private static final String PREF_SHIFT_LOG_STARTING_FROM = "pref.PREF_SHIFT_LOG_STARTING_FROM";
    private static final String PREF_MACHINE_DATA_STARTING_FROM = "pref.PREF_MACHINE_DATA_STARTING_FROM";
    private static final String PREF_FORCE_LOCAL = "pref.PREF_FORCE_LOCAL";
    private static final String PREF_FORCE_LOCAL_NAME = "pref.PREF_FORCE_LOCAL_NAME";
    private static final String PREF_POLLING_FREQUENCY = "pref.PREF_POLLING_FREQUENCY";
    private static final String PREF_NEW_SHIFTLOG = "pref.PREF_NEW_SHIFTLOG";
    private static final String PREF_TOOLBAR_TUTORIAL = "pref.PREF_TOOLBAR_TUTORIAL";
    private static final String PREF_TIME_PARAMETER_DIALOG = "pref.PREF_TIME_PARAMETER_DIALOG";
    private static final String DEFAULT_LANGUAGE_VALUE = "en";
    private static final String DEFAULT_LANGUAGE_NAME_VALUE = "English";
    private static final int DEFAULT_POLLING_VALUE = 30;
    private static final int DEFAULT_TIMEOUT_VALUE = 20;
    private static final int DEFAULT_POPUP_VALUE = 5;
    private static final int DEFAULT_TOTAL_RETRIE_VALUE = 3;
    private static final int MAX_EVENT_SIZE = 200;
    private static final String PREFS_VERSION = "PREFS_VERSION";
    private static final float DEFAULT_VERSION = 1.6f;
    private static final String PREF_CHECKED_ALARM_IDS = "PREF_CHECKED_ALARM_IDS";


    private static PersistenceManager msInstance;
    private Gson mGson;
    public HashMap<Integer, Event> items = new HashMap<>();
    ;

    public static void initInstance(Context context) {
        if (msInstance == null) {
            msInstance = new PersistenceManager(context);
        }
    }

    public static PersistenceManager getInstance() {
        if (msInstance == null) {
            ZLogger.e(LOG_TAG, "getInstance(), fail, PersistenceManager is not init");
        }
        return msInstance;
    }

    private PersistenceManager(Context context) {
        SecurePreferences.initInstance(context);
        mGson = new Gson();
    }

    @Override
    public String getSiteUrl() {
        return SecurePreferences.getInstance().getString(PREF_SITE_URL, /*"https://apinextgen.my.leadermes.com"*/"");
//        return SecurePreferences.getInstance().getString(PREF_SITE_URL, "https://apitest.my.leadermes.com");
    }

    @Override
    public void setSiteUrl(String siteUrl) {

       ACRA.getConfig().setFormUri(siteUrl + "/LeaderMESApi/ReportApplicationCrash");
       // ACRA.getConfig().setFormUri("http://207.154.207.162/test/");

        SecurePreferences.getInstance().setString(PREF_SITE_URL, siteUrl);
    }

    @Override
    public String getUserName() {
        return SecurePreferences.getInstance().getString(PREF_USERNAME);
    }

    @Override
    public void setUsername(String username) {
        SecurePreferences.getInstance().setString(PREF_USERNAME, username);
    }

    @Override
    public String getPassword() {
        return SecurePreferences.getInstance().getString(PREF_PASSWORD);
    }

    @Override
    public void setPassword(String password) {
        SecurePreferences.getInstance().setString(PREF_PASSWORD, password);
    }

    @Override
    public String getSessionId() {
        return SecurePreferences.getInstance().getString(PREF_SESSION_ID);
    }

    @Override
    public void setSessionId(String sessionId) {
        SecurePreferences.getInstance().setString(PREF_SESSION_ID, sessionId);
    }


    @Override
    public int getMachineId() {
        return SecurePreferences.getInstance().getInt(PREF_MACHINE_ID);
    }

    @Override
    public String getOperatorId() {
        return SecurePreferences.getInstance().getString(PREF_OPERATOR_ID);
    }

    @Override
    public String getOperatorName() {
        return SecurePreferences.getInstance().getString(PREF_OPERATOR_NAME);
    }

    @Override
    public int getJobId() {
        return SecurePreferences.getInstance().getInt(PREF_JOB_ID);
    }

    @Override
    public void setJobId(int jobId) {
        SecurePreferences.getInstance().setInt(PREF_JOB_ID, jobId);
    }

    @Override
    public void setMachineId(int machineId) {
        SecurePreferences.getInstance().setInt(PREF_MACHINE_ID, machineId);
    }

    @Override
    public void setOperatorId(String operatorId) {
        SecurePreferences.getInstance().setString(PREF_OPERATOR_ID, operatorId);
    }

    @Override
    public void setOperatorName(String operatorName) {
        SecurePreferences.getInstance().setString(PREF_OPERATOR_NAME, operatorName);
    }

    public boolean isSelectedMachine() {
        return SecurePreferences.getInstance().getBoolean(PREF_SELECTED_MACHINE, false);
    }

    public void setSelectedMachine(boolean selected) {
        SecurePreferences.getInstance().setBoolean(PREF_SELECTED_MACHINE, selected);
    }

    public void setDisplayRejectFactor(boolean displayRejectFactor) {
        SecurePreferences.getInstance().setBoolean(PREF_DISPLAY_REJECTED_FACTOR, displayRejectFactor);
    }

    public boolean getDisplayRejectFactor() {
        return SecurePreferences.getInstance().getBoolean(PREF_DISPLAY_REJECTED_FACTOR, false);
    }

    public void setAddRejectsOnSetupEnd(boolean addRejectsOnSetupEnd) {
        SecurePreferences.getInstance().setBoolean(PREF_ADD_REJECTS_ON_SETUP, addRejectsOnSetupEnd);
    }

    public boolean getAddRejectsOnSetupEnd() {
        return SecurePreferences.getInstance().getBoolean(PREF_ADD_REJECTS_ON_SETUP, true);
    }

    @Override
    public int getTotalRetries() {
        return SecurePreferences.getInstance().getInt(PREF_TOTAL_RETRIES, DEFAULT_TOTAL_RETRIE_VALUE);
    }

    @Override
    public void setTotalRetries(int totalRetries) {
        SecurePreferences.getInstance().setInt(PREF_TOTAL_RETRIES, totalRetries);
    }

    @Override
    public int getRequestTimeout() {
        return SecurePreferences.getInstance().getInt(PREF_REQUEST_TIMEOUT, DEFAULT_TIMEOUT_VALUE);
    }

    @Override
    public void setRequestTimeOut(int requestTimeOut) {
        SecurePreferences.getInstance().setInt(PREF_REQUEST_TIMEOUT, requestTimeOut);
    }

    @Override
    public void saveShiftLogs(ArrayList<Event> events) {

        try {

            SecurePreferences.getInstance().setString(PREF_ARRAY_SHIFT_LOGS, mGson.toJson(deletingDuplicate(events)));

        } catch (OutOfMemoryError e) {

            SendReportUtil.sendAcraExeption(e, "saveShiftLogs  events size = " + events.size());

            SecurePreferences.getInstance().setString(PREF_ARRAY_SHIFT_LOGS, mGson.toJson(deletingDuplicate(getEventsFirstHalf(events))));

        }


        ZLogger.d(LOG_TAG, "saveShiftLogs(), jsonEvents: " + mGson.toJson(events));
    }

    private ArrayList<Event> getEventsFirstHalf(ArrayList<Event> events) {

        ArrayList<Event> firstHalf = new ArrayList<>();

        int x = events.size() / 2 + (events.size() % 2) - 1;

        for (int i = 0; i < x; i++) {

            firstHalf.add(events.get(i));
        }

        return firstHalf;
    }

    private ArrayList<Event> deletingDuplicate(ArrayList<Event> events) {

        ArrayList<Event> duplicates = new ArrayList<Event>();

        int count = 0;

        for (Event item : events) {

            count++;

            if (count < MAX_EVENT_SIZE)

                if (!items.containsKey(item.getEventID())) {

                    items.put(item.getEventID(), item);
                }
        }

        duplicates.addAll(items.values());

        return duplicates;
    }

    @Override
    public ArrayList<Event> getShiftLogs() {
        String shiftLogsJsonString = SecurePreferences.getInstance().getString(PREF_ARRAY_SHIFT_LOGS, mGson.toJson(new ArrayList<>()));
        Type listType = new TypeToken<ArrayList<Event>>() {
        }.getType();

        return deletingDuplicate((ArrayList<Event>) mGson.fromJson(shiftLogsJsonString, listType));
    }

    public void saveChartHistoricData(HashMap<String, ArrayList<Widget.HistoricData>> historicDatas) {
        SecurePreferences.getInstance().setString(PREF_ARRAY_CHART_HISTORIC_DATA, mGson.toJson(historicDatas));
    }

    public HashMap<String, ArrayList<Widget.HistoricData>> getChartHistoricData() {
        String historicDatasString = SecurePreferences.getInstance().getString(PREF_ARRAY_CHART_HISTORIC_DATA, mGson.toJson(new ArrayList<>()));
        Type listType = new TypeToken<HashMap<String, ArrayList<Widget.HistoricData>>>() {
        }.getType();

        return mGson.fromJson(historicDatasString, listType);
    }

    @Override
    public String getCurrentLang() {
        return SecurePreferences.getInstance().getString(PREF_FORCE_LOCAL, DEFAULT_LANGUAGE_VALUE);
    }

    @Override
    public void setCurrentLang(String lang) {
        SecurePreferences.getInstance().setString(PREF_FORCE_LOCAL, lang);
    }


    public String getCurrentLanguageName() {
        return SecurePreferences.getInstance().getString(PREF_FORCE_LOCAL_NAME, DEFAULT_LANGUAGE_NAME_VALUE);
    }

    public void setCurrentLanguageName(String languageName) {
        SecurePreferences.getInstance().setString(PREF_FORCE_LOCAL_NAME, languageName);
    }

    public void clear() {
        SecurePreferences.getInstance().clear();
    }

    @Override
    public int getPollingFrequency() {
        return SecurePreferences.getInstance().getInt(PREF_POLLING_FREQUENCY, DEFAULT_POLLING_VALUE);
    }

    @Override
    public void setPolingFrequency(int polingFrequency) {
        SecurePreferences.getInstance().setInt(PREF_POLLING_FREQUENCY, polingFrequency);
    }

    @Override
    public String getShiftLogStartingFrom() {
        // TODO: 18-Oct-16 SERGEY 86400000
//        return SecurePreferences.getInstance().getString(PREF_SHIFT_LOG_STARTING_FROM, TimeUtils.getDate(System.currentTimeMillis() - 691200000, "dd.MM.yy"/*"yyyy-MM-dd HH:mm:ss.SSS"*/));
        return SecurePreferences.getInstance().getString(PREF_SHIFT_LOG_STARTING_FROM, TimeUtils.getDate(System.currentTimeMillis() - 86400000, /*"yyyy-MM-dd"*/"yyyy-MM-dd HH:mm:ss.SSS"));
    }

    @Override
    public void setShiftLogStartingFrom(String startingFrom) {
        SecurePreferences.getInstance().setString(PREF_SHIFT_LOG_STARTING_FROM, startingFrom);
    }

    @Override
    public String getMachineDataStartingFrom() {
        return SecurePreferences.getInstance().getString(PREF_MACHINE_DATA_STARTING_FROM, TimeUtils.getDate(System.currentTimeMillis() - (24 * 60 * 60 * 1000), "yyyy-MM-dd HH:mm:ss.SSS"));
    }

    @Override
    public void setMachineDataStartingFrom(String startingFrom) {
        SecurePreferences.getInstance().setString(PREF_MACHINE_DATA_STARTING_FROM, startingFrom);
    }

    public boolean isNewShiftLogs() {
        return SecurePreferences.getInstance().getBoolean(PREF_NEW_SHIFTLOG, false);
    }

    public void setIsNewShiftLogs(boolean isNew) {
        SecurePreferences.getInstance().setBoolean(PREF_NEW_SHIFTLOG, isNew);
    }

    public int getTimeToDownParameterDialog() {
        return SecurePreferences.getInstance().getInt(PREF_TIME_PARAMETER_DIALOG, DEFAULT_POPUP_VALUE * 1000);
    }

    public void setTimeToDownParameterDialog(int time) {
        SecurePreferences.getInstance().setInt(PREF_TIME_PARAMETER_DIALOG, time);
    }

    public void setVersion(float version) {

        SecurePreferences.getInstance().setFloat(PREFS_VERSION, version);
    }

    public float getVersion() {

        float version = SecurePreferences.getInstance().getFloat(PREFS_VERSION);

        if (version == 0){

            version = DEFAULT_VERSION;
        }
        return version;
    }


    public void setCheckedAlarms(ArrayList<Integer> checkedAlarmList) {

        SecurePreferences.getInstance().setString(PREF_CHECKED_ALARM_IDS, mGson.toJson(checkedAlarmList));
    }

    public ArrayList<Integer> getCheckedAlarms() {

        String ids = SecurePreferences.getInstance().getString(PREF_CHECKED_ALARM_IDS, mGson.toJson(new ArrayList<>()));
        Type listType = new TypeToken<ArrayList<Integer>>() {
        }.getType();

        return mGson.fromJson(ids, listType);
    }

    public void setMinEventDuration(int minEventDuration) {
        SecurePreferences.getInstance().setInt(PREF_MIN_EVENT_DURATION, minEventDuration);
    }

    public int getMinEventDuration(){
        return SecurePreferences.getInstance().getInt(PREF_MIN_EVENT_DURATION, 0);
    }

    public boolean isDisplayToolbarTutorial() {
        return SecurePreferences.getInstance().getBoolean(PREF_TOOLBAR_TUTORIAL, true);
    }

    public void setDisplayToolbarTutorial(boolean isNew) {
        SecurePreferences.getInstance().setBoolean(PREF_TOOLBAR_TUTORIAL, isNew);
    }
}
