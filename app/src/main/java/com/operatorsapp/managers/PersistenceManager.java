package com.operatorsapp.managers;

import android.annotation.SuppressLint;
import android.content.Context;

import com.app.operatorinfra.OperatorPersistenceManagerInterface;
import com.example.common.Event;
import com.example.common.SelectableString;
import com.example.oppapplog.OppAppLogger;
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
import com.operators.shiftloginfra.ShiftLogPersistenceManagerInterface;
import com.operatorsapp.model.TechCallInfo;
import com.operatorsapp.server.responses.Notification;
import com.operatorsapp.utils.Consts;
import com.operatorsapp.utils.SecurePreferences;
import com.operatorsapp.utils.SendReportUtil;
import com.operatorsapp.utils.TimeUtils;

import org.acra.ACRA;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

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
    private static final String PREF_ARRAY_CHECKED_SHIFT_GRAPH_CATEGORIES = "pref.PREF_ARRAY_CHECKED_SHIFT_GRAPH_CATEGORIES";
    private static final String PREF_ARRAY_CHART_HISTORIC_DATA = "pref.PREF_ARRAY_CHART_HISTORIC_DATA";
    private static final String PREF_JOB_ID = "pref.PREF_JOB_ID";
    private static final String PREF_OPERATOR_ID = "pref.PREF_OPERATOR_ID";
    private static final String PREF_OPERATOR_NAME = "pref.PREF_OPERATOR_NAME";
    private static final String PREF_SHIFT_LOG_STARTING_FROM = "pref.PREF_SHIFT_LOG_STARTING_FROM";
    private static final String PREF_MACHINE_DATA_STARTING_FROM = "pref.PREF_MACHINE_DATA_STARTING_FROM";
    private static final String PREF_FORCE_LOCAL = "pref.PREF_FORCE_LOCAL";
    private static final String PREF_USERID = "pref.PREF_USERID";
    private static final String PREF_FORCE_LOCAL_NAME = "pref.PREF_FORCE_LOCAL_NAME";
    private static final String PREF_POLLING_FREQUENCY = "pref.PREF_POLLING_FREQUENCY";
    private static final String PREF_NEW_SHIFTLOG = "pref.PREF_NEW_SHIFTLOG";
    private static final String PREF_TOOLBAR_TUTORIAL = "pref.PREF_TOOLBAR_TUTORIAL";
    private static final String PREF_TIME_PARAMETER_DIALOG = "pref.PREF_TIME_PARAMETER_DIALOG";
    private static final String DEFAULT_LANGUAGE_VALUE = "en";
    private static final String DEFAULT_LANGUAGE_NAME_VALUE = "English";
    private static final int DEFAULT_POLLING_VALUE = 60;
    private static final int DEFAULT_TIMEOUT_VALUE = 60;
    private static final int DEFAULT_POPUP_VALUE = 5;
    private static final int DEFAULT_TOTAL_RETRIE_VALUE = 3;
    private static final int MAX_EVENT_SIZE = 200;
    private static final String PREFS_VERSION = "PREFS_VERSION";
    private static final float DEFAULT_VERSION = 1.6f;
    private static final String PREF_CHECKED_ALARM_IDS = "PREF_CHECKED_ALARM_IDS";
    private static final String PREF_STORAGE_PERMISSION_GRANTED = "PREF_STORAGE_PERMISSION_GRANTED";
    private static final String PREF_NOTIFICATION_TOKEN = "pref.PREF_NOTIFICATION_TOKEN";
    private static final String PREF_UPDATE_NOTIFICATION_TOKEN = "pref.PREF_UPDATE_NOTIFICATION_TOKEN";
    private static final String PREF_NOTIFICATION_HISTORY = "pref.PREF_NOTIFICATION_HISTORY";
    private static final String PREF_TECHNICIAN_CALL_TIME = "pref.PREF_TECHNICIAN_CALL_TIME";
    private static final String PREF_SITE_NAME = "pref.PREF_SITE_NAME";
    private static final String PREF_MACHINE_NAME = "pref.PREF_MACHINE_NAME";
    private static final String CALLED_TECHNICIAN_NAME = "pref.PREF_CALLED_TECHNICIAN_NAME";
    private static final String CALLED_TECHNICIAN = "pref.PREF_CALLED_TECHNICIAN";
    private static final String PREF_SHIFT_START = "pref.PREF_SHIFT_START";
    private static final String PREF_SHIFT_END = "pref.PREF_SHIFT_END";
    private static final String PREF_RECENT_TECH_CALL = "pref.RECENT_TECH_CALL";
    private static final String PREF_STOP_REASON_DESIGN = "pref.STOP_REASON_DESIGN";
    private static final String PREF_RECENT_MAX_UNIT_REPORT = "pref.PREF_RECENT_MAX_UNIT_REPORT";
    private static final String PREF_IS_NEW_SHIFTLOG_DISPLAY = "pref.PREF_IS_NEW_SHIFTLOG_DISPLAY";
    private static final String PREF_ARRAY_ALARAM_CURRENT_EVENT = "pref.PREF_ARRAY_ALARAM_CURRENT_EVENT";
    private static final String PREF_DEPARTMENT_ID = "PREF_DEPARTMENT_ID";
    private static final String PREF_REPORT_SHIFT_BTN_X = "PREF_REPORT_SHIFT_BTN_X";
    private static final String PREF_REPORT_SHIFT_BTN_Y = "PREF_REPORT_SHIFT_BTN_Y";
    private static final String PREF_SELF_NOTIFICATION_ID_MAP = "PREF_SELF_NOTIFICATION_ID_MAP";
    private static final long ONE_DAY = 1000 * 60 * 60 * 24;
    private static final String PREF_IS_STATUS_BAR_LOCKED = "PREF_IS_STATUS_BAR_LOCKED";
    private static final String PREF_UNITS_IN_CYCLE_TYPE = "PREF_UNITS_IN_CYCLE_TYPE";


    private static PersistenceManager msInstance;
    private Gson mGson;
    @SuppressLint("UseSparseArrays")
    public HashMap<Float, Event> items = new HashMap<>();

    public static void initInstance(Context context) {
        if (msInstance == null) {
            msInstance = new PersistenceManager(context);
        }
    }

    public static PersistenceManager getInstance() {
        if (msInstance == null) {
            OppAppLogger.getInstance().e(LOG_TAG, "getInstance(), fail, PersistenceManager is not init");
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

    @SuppressWarnings("deprecation")
    @Override
    public void setSiteUrl(String siteUrl) {

        if (ACRA.getConfig() != null) {
            ACRA.getConfig().setFormUri(siteUrl + "/LeaderMESApi/ReportApplicationCrash");
        }

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
        //return SecurePreferences.getInstance().getString(PREF_OPERATOR_ID, "0");
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


        OppAppLogger.getInstance().d(LOG_TAG, "saveShiftLogs(), jsonEvents: " + mGson.toJson(events));
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

        int count = 0;

        for (Event item : events) {

            count++;

            if (count < MAX_EVENT_SIZE)

                if (!items.containsKey(item.getEventID())) {

                    items.put(item.getEventID(), item);
                }
        }

        return new ArrayList<>(items.values());
    }

    @Override
    public ArrayList<Event> getShiftLogs() {
        String shiftLogsJsonString = SecurePreferences.getInstance().getString(PREF_ARRAY_SHIFT_LOGS, mGson.toJson(new ArrayList<>()));
        Type listType = new TypeToken<ArrayList<Event>>() {
        }.getType();

        //noinspection unchecked
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

    @Override
    public void setUserId(int userId) {
        SecurePreferences.getInstance().setInt(PREF_USERID, userId);
    }


    public int getUserId(){
        return SecurePreferences.getInstance().getInt(PREF_USERID);
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

    public void setShiftStart(String shiftStart) {
        SecurePreferences.getInstance().setString(PREF_SHIFT_START, shiftStart);
    }

    public void setShiftEnd(String shiftEnd) {
        if (shiftEnd != null && !shiftEnd.isEmpty() && shiftEnd.length() > 0) {
            SecurePreferences.getInstance().setString(PREF_SHIFT_END, shiftEnd);
        }
    }


    public String getShiftStart() {
        return SecurePreferences.getInstance().getString(PREF_SHIFT_START);
    }

    public String getShiftEnd() {
        String shiftEnd = SecurePreferences.getInstance().getString(PREF_SHIFT_END);
        if (shiftEnd != null && !shiftEnd.isEmpty() && shiftEnd.length() > 0) {
            return shiftEnd;
        }else {
            return TimeUtils.getDateFromFormat(new Date(), TimeUtils.SQL_NO_T_FORMAT);
        }
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
        SecurePreferences.getInstance().setInt(PREF_TIME_PARAMETER_DIALOG, time * 1000);
    }

    public void setVersion(float version) {

        SecurePreferences.getInstance().setFloat(PREFS_VERSION, version);
    }

    public float getVersion() {

        float version = SecurePreferences.getInstance().getFloat(PREFS_VERSION);

        if (version == 0) {

            version = DEFAULT_VERSION;
        }
        return version;
    }


    public void setCheckedAlarms(HashMap<Integer, ArrayList<Integer>> checkedAlarmHashMap) {

        SecurePreferences.getInstance().setString(PREF_CHECKED_ALARM_IDS, mGson.toJson(checkedAlarmHashMap));
    }

    public HashMap<Integer, ArrayList<Integer>> getCheckedAlarms() {

        String ids = SecurePreferences.getInstance().getString(PREF_CHECKED_ALARM_IDS, "");
        Type listType = new TypeToken<HashMap<Integer, ArrayList<Integer>>>() {
        }.getType();

        if (ids != null && ids.length() > 0 && !ids.equals("null")) {
            return mGson.fromJson(ids, listType);
        }else {
            return new HashMap<>();
        }
    }

    public void setMinEventDuration(int minEventDuration) {
        SecurePreferences.getInstance().setInt(PREF_MIN_EVENT_DURATION, minEventDuration);
    }

    public int getMinEventDuration() {
//        return 0;
        return SecurePreferences.getInstance().getInt(PREF_MIN_EVENT_DURATION, 1);
    }

    public boolean isDisplayToolbarTutorial() {
        return SecurePreferences.getInstance().getBoolean(PREF_TOOLBAR_TUTORIAL, true);
    }

    public void setDisplayToolbarTutorial(boolean isNew) {
        SecurePreferences.getInstance().setBoolean(PREF_TOOLBAR_TUTORIAL, isNew);
    }

    public void setStorageGranted(boolean granted) {
        SecurePreferences.getInstance().setBoolean(PREF_STORAGE_PERMISSION_GRANTED, granted);
    }

    public boolean isStorageGranted() {
        return SecurePreferences.getInstance().getBoolean(PREF_STORAGE_PERMISSION_GRANTED, false);
    }

    public void setNotificationToken(String token) {
        SecurePreferences.getInstance().setString(PREF_NOTIFICATION_TOKEN, token);
    }

    public String getNotificationToken() {
        return SecurePreferences.getInstance().getString(PREF_NOTIFICATION_TOKEN);
    }

//    public boolean isNeedUpdateToken() {
//        String str = getNotificationToken();
//        return SecurePreferences.getInstance().getBoolean(PREF_UPDATE_NOTIFICATION_TOKEN, true);
//    }

    public void setNeedUpdateToken(boolean isUpdateToken) {
        SecurePreferences.getInstance().setBoolean(PREF_UPDATE_NOTIFICATION_TOKEN, isUpdateToken);

    }

    public void setNotificationHistory(final ArrayList<Notification> notificationList) {

        final ArrayList<Notification> copyList = new ArrayList<>();
        if (notificationList != null && notificationList.size() > 0) {
            Collections.sort(notificationList, new Comparator<Notification>() {
                @Override
                public int compare(Notification o1, Notification o2) {

                    if (o1.getmNotificationID() > o2.getmNotificationID()) {
                        return -1;
                    } else if (o1.getmNotificationID() < o2.getmNotificationID()) {
                        return 1;
                    } else {
                        copyList.add(o2);
                        return 0;
                    }
                }
            });
        }

        if (copyList.size() > 0) {
            for (Notification not : copyList) {
                for (Notification not2 : notificationList) {
                    if (not.getmNotificationID() == not2.getmNotificationID()) {
                        notificationList.remove(not2);
                        break;
                    }
                }
            }
        }

        SecurePreferences.getInstance().setString(PREF_NOTIFICATION_HISTORY, mGson.toJson(notificationList));
    }

    public ArrayList<Notification> getNotificationHistoryNoTech() {
        String str = SecurePreferences.getInstance().getString(PREF_NOTIFICATION_HISTORY, "");
        Type listType = new TypeToken<ArrayList<Notification>>(){}.getType();
        ArrayList<Notification> notificationsList = mGson.fromJson(str, listType);
        ArrayList<Notification> filteredList = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -1);
        Date date;
        if (notificationsList != null) {
            for (int i = 0; i < notificationsList.size(); i++) {
                Notification not = notificationsList.get(i);
                date = TimeUtils.getDateForNotification(not.getmSentTime());
                if (not.getmNotificationType() != Consts.NOTIFICATION_TYPE_TECHNICIAN && date != null && date.after(cal.getTime())) {
                    filteredList.add(not);
                }
            }
        }

        return filteredList;

    }

    public ArrayList<Notification> getNotificationHistory() {
        String str = SecurePreferences.getInstance().getString(PREF_NOTIFICATION_HISTORY, "");
        Type listType = new TypeToken<ArrayList<Notification>>(){}.getType();
        ArrayList<Notification> notificationsList = mGson.fromJson(str, listType);
        ArrayList<Notification> filteredList = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -1);
        Date date;
        if (notificationsList != null) {
            for (int i = 0; i < notificationsList.size(); i++) {
                Notification not = notificationsList.get(i);
                date = TimeUtils.getDateForNotification(not.getmSentTime());
                if (date != null && date.after(cal.getTime())) {
                    filteredList.add(not);
                }else if (not.getmNotificationType() == Consts.NOTIFICATION_TYPE_TECHNICIAN &&
                        (not.getmResponseType() != Consts.NOTIFICATION_RESPONSE_TYPE_DECLINE || not.getmResponseType() != Consts.NOTIFICATION_RESPONSE_TYPE_END_SERVICE)){
                    filteredList.add(not);
                }
            }

            if (notificationsList.size() > filteredList.size()) {
                setNotificationHistory(filteredList);
            }
        }

        return filteredList;

    }

    public void setTechnicianCallTime(long technicianCallTime) {
        SecurePreferences.getInstance().setString(PREF_TECHNICIAN_CALL_TIME, String.valueOf(technicianCallTime));
    }

    public long getTechnicianCallTime() {
        String time = SecurePreferences.getInstance().getString(PREF_TECHNICIAN_CALL_TIME, "0");
        if (time != null && time.length() > 0 ){
            try {
                return Long.parseLong(time);
            }catch (NumberFormatException e){
                return 0;
            }
        }else {
            return 0;
        }

    }

    public void setSiteName(String siteName) {
        SecurePreferences.getInstance().setString(PREF_SITE_NAME, siteName);
    }

    public String getSiteName() {
        return SecurePreferences.getInstance().getString(PREF_SITE_NAME, "");
    }

//    public void tryToUpdateToken(String status) {
//        SecurePreferences.getInstance().setString("tryToUpdateToken", status + "  time: " + new Date().getTime());
//    }
//
//    public String getTimeOfUpdateToken() {
//        return SecurePreferences.getInstance().getString("tryToUpdateToken", "?");
//    }

    public void setMachineName(String mMachineName) {
        SecurePreferences.getInstance().setString(PREF_MACHINE_NAME, mMachineName);
    }

    public String getMachineName(){
        return SecurePreferences.getInstance().getString(PREF_MACHINE_NAME, "");
    }

    public void setCalledTechnicianName(String techName) {
        SecurePreferences.getInstance().setString(CALLED_TECHNICIAN_NAME, techName);
    }

    public String getCalledTechnicianName(){
        return SecurePreferences.getInstance().getString(CALLED_TECHNICIAN_NAME, "");
    }

    public void setCalledTechnician(TechCallInfo techCallInfo) {
        ArrayList<TechCallInfo> list = getCalledTechnician();
        ArrayList<TechCallInfo> listCopy = new ArrayList<>();
        listCopy.addAll(list);

        for (TechCallInfo call : listCopy) {
            if (call.getmTechnicianId() == techCallInfo.getmTechnicianId()){
                list.remove(call);
            }
        }

        list.add(techCallInfo);
        SecurePreferences.getInstance().setString(CALLED_TECHNICIAN, mGson.toJson(list));
    }

    public void clearCalledTechnician(){
        SecurePreferences.getInstance().setString(CALLED_TECHNICIAN, "");
    }

    public void setCalledTechnicianList(ArrayList<TechCallInfo> techCallInfoList) {
        if (techCallInfoList == null){
            SecurePreferences.getInstance().setString(CALLED_TECHNICIAN, null);
        }else {
            SecurePreferences.getInstance().setString(CALLED_TECHNICIAN, mGson.toJson(techCallInfoList));
        }
    }

    public ArrayList<TechCallInfo> getCalledTechnician(){

        String str = SecurePreferences.getInstance().getString(CALLED_TECHNICIAN, "");
        Type listType = new TypeToken<ArrayList<TechCallInfo>>() {
        }.getType();

        ArrayList<TechCallInfo> techCallInfoList = new ArrayList<TechCallInfo>();
        if (str != null && str.length() > 0){
            try {
                techCallInfoList = mGson.fromJson(str, listType);
            }catch (Exception e){

            }
        }

//        TechCallInfo t = mGson.fromJson(str, TechCallInfo.class);
//        if (t == null){
//            t = new TechCallInfo(-1, "", "", 0);
//        }
        if(techCallInfoList != null && techCallInfoList.size() > 0) {
                Collections.sort(techCallInfoList, new Comparator<TechCallInfo>() {
                    @Override
                    public int compare(TechCallInfo o1, TechCallInfo o2) {

                        if (o1.getmCallTime() > o2.getmCallTime()) {
                            return -1;
                        } else if (o1.getmCallTime() < o2.getmCallTime()) {
                            return 1;
                        } else {
                            return 0;
                        }
                    }
                });
        }
        return techCallInfoList;
    }

    public void setRecentTechCallId(int notificationId) {
        SecurePreferences.getInstance().setInt(PREF_RECENT_TECH_CALL, notificationId);
    }

    public int getRecentTechCallId() {
        return SecurePreferences.getInstance().getInt(PREF_RECENT_TECH_CALL);
    }

    public boolean isNewStopReasonDesign() {
        return SecurePreferences.getInstance().getBoolean(PREF_STOP_REASON_DESIGN, true);
    }

    public void setisNewStopReasonDesign(boolean selected) {
        SecurePreferences.getInstance().setBoolean(PREF_STOP_REASON_DESIGN, selected);
    }
    public void setMaxUnitReport(float maxUnitReport) {
        SecurePreferences.getInstance().setFloat(PREF_RECENT_MAX_UNIT_REPORT, maxUnitReport);
    }

    public float getMaxUnitReport() {
        return SecurePreferences.getInstance().getFloat(PREF_RECENT_MAX_UNIT_REPORT);
    }

    public void setIsNewShiftLog(boolean isNewShiftLog) {
        SecurePreferences.getInstance().setBoolean(PREF_IS_NEW_SHIFTLOG_DISPLAY, isNewShiftLog);
    }
    public boolean getIsNewShiftLog() {
        return SecurePreferences.getInstance().getBoolean(PREF_IS_NEW_SHIFTLOG_DISPLAY, false);
    }

    public void setDepartmentId(int departmentID) {
        SecurePreferences.getInstance().setInt(PREF_DEPARTMENT_ID, departmentID);
    }
    public int getDepartmentId() {
        return SecurePreferences.getInstance().getInt(PREF_DEPARTMENT_ID);
    }

    public void setReportShiftBtnPositionX(float moveToX) {
        SecurePreferences.getInstance().setFloat(PREF_REPORT_SHIFT_BTN_X, moveToX);
    }
    public float getReportShiftBtnPositionX() {
        return SecurePreferences.getInstance().getFloat(PREF_REPORT_SHIFT_BTN_X);
    }

    public void setReportShiftBtnPositionY(float moveToY) {
        SecurePreferences.getInstance().setFloat(PREF_REPORT_SHIFT_BTN_Y, moveToY);
    }
    public float getReportShiftBtnPositionY() {
        return SecurePreferences.getInstance().getFloat(PREF_REPORT_SHIFT_BTN_Y);
    }

    public void setSelfNotificationsId(String id) {

        String selfIdMap = SecurePreferences.getInstance().getString(PREF_SELF_NOTIFICATION_ID_MAP, mGson.toJson(new ArrayList<>()));
        Type listType = new TypeToken<HashMap<String, String>>() {
        }.getType();

        HashMap map = mGson.fromJson(selfIdMap, listType);
        map.put(id, new Date().getTime());
        SecurePreferences.getInstance().setString(PREF_SELF_NOTIFICATION_ID_MAP, mGson.toJson(map));
    }

    public boolean isSelfNotification(String id){


        String selfIdMap = SecurePreferences.getInstance().getString(PREF_SELF_NOTIFICATION_ID_MAP, mGson.toJson(new ArrayList<>()));
        Type listType = new TypeToken<HashMap<String, String>>() {
        }.getType();

        HashMap map = mGson.fromJson(selfIdMap, listType);

        Iterator<String> iter = map.keySet().iterator();
        while (iter.hasNext()){
            String str = iter.next();
            if (Long.valueOf((String) map.get(str)) + ONE_DAY < new Date().getTime()){
                iter.remove();
            }
        }

        return map.containsKey(id);
    }

    public boolean isStatusBarLocked() {
        return SecurePreferences.getInstance().getBoolean(PREF_IS_STATUS_BAR_LOCKED, true);
    }

    public void setStatusBarLocked(boolean isLocked) {
        SecurePreferences.getInstance().setBoolean(PREF_IS_STATUS_BAR_LOCKED, isLocked);
    }

    public ArrayList<SelectableString> getShiftGraphCategories() {
        String selectableString = SecurePreferences.getInstance()
                .getString(PREF_ARRAY_CHECKED_SHIFT_GRAPH_CATEGORIES, mGson.toJson(new ArrayList<>()));
        Type listType = new TypeToken<ArrayList<SelectableString>>() {
        }.getType();

        return ((ArrayList<SelectableString>) mGson.fromJson(selectableString, listType));

    }

    public void setShiftGraphCategories(ArrayList<SelectableString> selectableStrings) {
        SecurePreferences.getInstance().setString(PREF_ARRAY_CHECKED_SHIFT_GRAPH_CATEGORIES, mGson.toJson(selectableStrings));
    }

    public int getUnitsInCycleType() {
        return SecurePreferences.getInstance().getInt(PREF_UNITS_IN_CYCLE_TYPE, 1);
    }

    public void setUnitsInCycleType(int unitsInCyleType) {
        SecurePreferences.getInstance().setInt(PREF_UNITS_IN_CYCLE_TYPE, unitsInCyleType);
    }
}
