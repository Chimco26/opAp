package com.operatorsapp.utils;

import android.content.Context;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.operatorsapp.application.OperatorApplication;
import com.operatorsapp.managers.PersistenceManager;

public class GoogleAnalyticsHelper {


    public Tracker getTracker(Context context){

        try {
            return ((OperatorApplication)context.getApplicationContext()).getDefaultTracker();
        }catch (NullPointerException e){
            return null;
        }

    }

    public void trackScreen(Context context, String screenName){

        Tracker tracker = getTracker(context);
        if (tracker != null) {
            PersistenceManager pm = PersistenceManager.getInstance();

            tracker.setClientId("machine name + id: " + pm.getMachineName() + ", " + pm.getMachineId());
            tracker.setAppVersion(pm.getVersion() + "");
            tracker.setHostname(pm.getSiteName());
            tracker.setScreenName(screenName);
            tracker.set("Machine Name + Id", pm.getMachineName() + ", " + pm.getMachineId());
            tracker.send(new HitBuilders.ScreenViewBuilder().build());
        }
    }

    public void trackEvent(Context context, EventCategory category, boolean isSucceed , String label){

        Tracker tracker = getTracker(context);
        if (tracker != null) {
            PersistenceManager pm = PersistenceManager.getInstance();

            tracker.setClientId("machine name + id: " + pm.getMachineName() + ", " + pm.getMachineId());
            tracker.setAppVersion(pm.getVersion() + "");
            tracker.setHostname(pm.getSiteName());
            tracker.set("Machine Name + Id", pm.getMachineName() + ", " + pm.getMachineId());
            tracker.send(new HitBuilders.EventBuilder()
                    .setCategory(getCategory(category))
                    .setAction(isSucceed ? "Action was preformed successfully" : "Action has failed")
                    .setLabel(label)
                    .build());
        }

    }

    private String getCategory(EventCategory category) {
        switch (category){
            case TECH_CALL:
                return "Technician Call";
            case OPEN_NOTIFICATIONS:
                return "Open Notifications Dialog";
            case OPERATOR_SIGN_IN:
                return "Operator Sign In";
            case STOP_REASON_REPORT:
                return "Stop Reason Report";
            case REJECT_REPORT:
                return "Reject Report";
            case CHANGE_UNIT_IN_CYCLE:
                return "Change Unit in Cycle";
            case TOGGLE_SHIFT_LOG_VIEW:
                return "Toggle Shift Log Vew";
            case SPLIT_STOP_EVENT:
                return "Split Stop Event";
            case PRODUCTION_STATUS:
                return "Production Status";
            case END_SETUP:
                return "End Setup";
            case SEND_NOTIFICATION:
                return "Send New Notification";
            case RESPOND_TO_NOTIFICATION:
                return "Respond to Notification";
            case RECIPE_EDIT:
                return "Edit Recipe";
            case SHIFT_REPORT:
                return "Shift Report";
            default:return "";
        }
    }


    public enum EventCategory{
        TECH_CALL,
        OPEN_NOTIFICATIONS,
        OPERATOR_SIGN_IN,
        STOP_REASON_REPORT,
        REJECT_REPORT,
        CHANGE_UNIT_IN_CYCLE,
        TOGGLE_SHIFT_LOG_VIEW,
        SPLIT_STOP_EVENT,
        PRODUCTION_REPORT,
        PRODUCTION_STATUS,
        END_SETUP,
        SEND_NOTIFICATION,
        RESPOND_TO_NOTIFICATION,
        RECIPE_EDIT,
        SHIFT_REPORT
    }
}
