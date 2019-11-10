package com.operatorsapp.utils;

import android.content.Context;
import android.os.Bundle;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.operatorsapp.application.OperatorApplication;
import com.operatorsapp.managers.PersistenceManager;

public class GoogleAnalyticsHelper {


    public FirebaseAnalytics getTracker(Context context){

        try {
            return ((OperatorApplication)context.getApplicationContext()).getDefaultTracker();
        }catch (NullPointerException e){
            return null;
        }

    }

    public void trackScreen(Context context, String screenName){

        FirebaseAnalytics firebaseAnalytics = getTracker(context);
        PersistenceManager pm = PersistenceManager.getInstance();

        Bundle params = new Bundle();
        params.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "screen");
        params.putString(FirebaseAnalytics.Param.ITEM_NAME, screenName);
        params.putString(FirebaseAnalytics.Param.ITEM_ID, "machine name + id: " + pm.getMachineName() + ", " + pm.getMachineId());
        params.putString(FirebaseAnalytics.Param.ITEM_VARIANT, "version num: " + pm.getVersion());
        params.putString(FirebaseAnalytics.Param.AFFILIATION, "site name: " + pm.getSiteName());
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, params);


//        Tracker tracker = getTracker(context);
//        if (tracker != null) {
//            PersistenceManager pm = PersistenceManager.getInstance();
//
//            tracker.setClientId("machine name + id: " + pm.getMachineName() + ", " + pm.getMachineId());
//            tracker.setAppVersion(pm.getVersion() + "");
//            tracker.setHostname(pm.getSiteName());
//            tracker.setScreenName(screenName);
//            tracker.set("Machine Name + Id", pm.getMachineName() + ", " + pm.getMachineId());
//            tracker.send(new HitBuilders.ScreenViewBuilder().build());
//        }
    }

    public void trackEvent(Context context, EventCategory category, boolean isSucceed , String label){

        FirebaseAnalytics firebaseAnalytics = getTracker(context);
        PersistenceManager pm = PersistenceManager.getInstance();

        Bundle params = new Bundle();
        params.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, getCategory(category));
        params.putLong(FirebaseAnalytics.Param.SUCCESS, isSucceed ? 1 : 0);
        params.putString(FirebaseAnalytics.Param.ITEM_ID, "machine name + id: " + pm.getMachineName() + ", " + pm.getMachineId());
        params.putString(FirebaseAnalytics.Param.ITEM_VARIANT, "version num: " + pm.getVersion());
        params.putString(FirebaseAnalytics.Param.AFFILIATION, "site name: " + pm.getSiteName());
        params.putString(FirebaseAnalytics.Param.CONTENT, label);
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, params);




//
//        Tracker tracker = getTracker(context);
//        if (tracker != null) {
//            PersistenceManager pm = PersistenceManager.getInstance();
//
//            tracker.setClientId("machine name + id: " + pm.getMachineName() + ", " + pm.getMachineId());
//            tracker.setAppVersion(pm.getVersion() + "");
//            tracker.setHostname(pm.getSiteName());
//            tracker.set("Machine Name + Id", pm.getMachineName() + ", " + pm.getMachineId());
//            tracker.send(new HitBuilders.EventBuilder()
//                    .setCategory(getCategory(category))
//                    .setAction(isSucceed ? "Action was preformed successfully" : "Action has failed")
//                    .setLabel(label)
//                    .build());
//        }

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
