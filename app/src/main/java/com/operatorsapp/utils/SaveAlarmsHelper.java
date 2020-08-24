package com.operatorsapp.utils;

import android.content.Context;
import android.database.Cursor;

import com.operatorsapp.managers.PersistenceManager;
import com.ravtech.david.sqlcore.DatabaseHelper;

import java.util.ArrayList;
import java.util.HashMap;

import static android.text.format.DateUtils.DAY_IN_MILLIS;

public class SaveAlarmsHelper {

    public static void saveAlarmsCheckedLocaly(Context context) {
        //because alarms status not saved in sever side,
        // the goal is to clear the database on change language and load it completely on reopen (to get events true language)
        //and update the alarms if checked

        PersistenceManager persistenceManager = PersistenceManager.getInstance();

        HashMap<Integer, ArrayList<Integer>> checkedAlarmHashMap = persistenceManager.getCheckedAlarms();

        PersistenceManager.getInstance().setShiftLogStartingFrom(com.operatorsapp.utils.TimeUtils.getDate(System.currentTimeMillis() - DAY_IN_MILLIS, "yyyy-MM-dd HH:mm:ss.SSS"));

        DatabaseHelper databaseHelper =DatabaseHelper.getInstance(context);

        Cursor tempCursor = databaseHelper.getCursorOrderByTime();
        if (tempCursor == null){
            return;
        }
        ArrayList<Integer> alarmList = checkedAlarmHashMap.get(persistenceManager.getMachineId());

        if (alarmList == null){
            alarmList = new ArrayList<>();
        }

        alarmList.clear();

        for (tempCursor.moveToFirst(); !tempCursor.isAfterLast(); tempCursor.moveToNext()) {

            if (tempCursor.getInt(tempCursor.getColumnIndex(DatabaseHelper.KEY_TREATED)) == 1) {

                alarmList.add(tempCursor.getInt(tempCursor.getColumnIndex(DatabaseHelper.KEY_EVENT_ID)));
            }

        }

        tempCursor.close();

        checkedAlarmHashMap.remove(persistenceManager.getMachineId());

        checkedAlarmHashMap.put(persistenceManager.getMachineId(), alarmList);

        PersistenceManager.getInstance().setCheckedAlarms(checkedAlarmHashMap);
    }
}
