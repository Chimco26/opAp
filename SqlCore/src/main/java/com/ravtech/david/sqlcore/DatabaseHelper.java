package com.ravtech.david.sqlcore;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.common.Event;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by david vardi on Ravtech
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = DatabaseHelper.class.getSimpleName();


    //Database instance
    private static DatabaseHelper mInstance = null;

    // Database Version
    private static final int DATABASE_VERSION = 19;

    // Database Name
    private static final String DATABASE_NAME = "events.db";

    // Table Names
    public static final String TABLE_EVENT = "Event";

    // column names
    public static final String KEY_EVENT_ID = "meventid";
    private static final String KEY_ID = "id";
    public static final String KEY_PRIORITY = "mpriority";
    public static final String KEY_TIME = "meventtime";
    public static final String KEY_TITLE = "meventtitle";
    public static final String KEY_E_TITLE = "meventetitle";
    public static final String KEY_L_TITLE = "meventltitle";
    public static final String KEY_SUB_TITLE_E_NAME = "meventsubtitleename";
    public static final String KEY_END_TIME = "meventendtime";
    public static final String KEY_GROUP_ID = "meventgroupid";
    public static final String KEY_DURATION = "meventduration";
    public static final String KEY_GROUP_L_NAME = "meventgrouplname";
    public static final String KEY_GROUP_E_NAME = "meventgroupename";
    public static final String KEY_ALARM_H_VALUE = "malarmhvalue";
    public static final String KEY_ALARM_L_VALUE = "malarmlvalue";
    public static final String KEY_ALARM_STANDARD_VALUE = "malarmstandardvalue";
    public static final String KEY_ALARM_VALUE = "malarmvalue";
    public static final String KEY_TREATED = "mtreated";
    private static final String KEY_SUB_TITLE_L_NAME = "meventsubtitlelname";
    private static final String KEY_ALARM_DISMISSED = "malarmdismissed";
    public static final String KEY_REASON_ID = "meventreasonid";
    private static final String KEY_TIME_OF_ADDED = "mtimeofadded";
    public static final String KEY_IS_DISMISS = "misdismiss";
    private static final String KEY_CREATED_AT = "created_at";
    public static final String KEY_CHECKED = "mchecked";
    public static final String KEY_TIME_MILLIS = "meventtimeinmillis";
    public static final String KEY_COLOR = "color";
    public static final String KEY_TYPE = "type";
    private static final String KEY_NOTIFICATIONS = "notifications";
    private static final String KEY_INVENTORY = "inventories";
    private static final String KEY_REJECTS = "rejects";
    private static final String KEY_ALARMS_EVENTS = "alarmEvents";
    private static final String KEY_JOB_DATA_ITEMS = "jobdataitems";
    private static final String KEY_QUALITY_TEST_ITEMS = "qualitytestitems";
    private static final String KEY_HAVE_EXTRA = "extra";
    private static final String KEY_DESCR = "descr";


    // Table Create Statements
    // Event table create statement
    private static final String CREATE_TABLE_EVENTS = "CREATE TABLE " + TABLE_EVENT +
            "(" +
            KEY_EVENT_ID + " FLOAT PRIMARY KEY," +
            KEY_ID + " INTEGER," +
            KEY_PRIORITY + " INTEGER," +
            KEY_TIME + " TEXT," +
            KEY_TITLE + " TEXT," +
            KEY_E_TITLE + " TEXT," +
            KEY_L_TITLE + " TEXT," +
            KEY_SUB_TITLE_E_NAME + " TEXT," +
            KEY_SUB_TITLE_L_NAME + " TEXT," +
            KEY_END_TIME + " TEXT," +
            KEY_DURATION + " BIGINT," +
            KEY_GROUP_ID + " INTEGER," +
            KEY_GROUP_L_NAME + " TEXT," +
            KEY_GROUP_E_NAME + " TEXT," +
            KEY_ALARM_DISMISSED + " BOOLEAN," +
            KEY_ALARM_H_VALUE + " FLOAT," +
            KEY_ALARM_L_VALUE + " FLOAT," +
            KEY_ALARM_STANDARD_VALUE + " FLOAT," +
            KEY_ALARM_VALUE + " FLOAT," +
            KEY_REASON_ID + " INTEGER," +
            KEY_TIME_OF_ADDED + " BIGINT," +
            KEY_TREATED + " BOOLEAN," +
            KEY_CHECKED + " BOOLEAN," +
            KEY_IS_DISMISS + " BOOLEAN," +
            KEY_HAVE_EXTRA + " BOOLEAN," +
            KEY_CREATED_AT + " DATETIME," +
            KEY_TIME_MILLIS + " BIGINT," +
            KEY_TYPE + " BIGINT," +
            KEY_COLOR + " TEXT," +
            KEY_NOTIFICATIONS + " TEXT," +
            KEY_REJECTS + " TEXT," +
            KEY_ALARMS_EVENTS + " TEXT," +
            KEY_JOB_DATA_ITEMS + " TEXT," +
            KEY_QUALITY_TEST_ITEMS + " TEXT," +
            KEY_DESCR + " TEXT," +
            KEY_INVENTORY + " TEXT" +
            ")";


    public static DatabaseHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return mInstance;
    }

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        if (db.isOpen())
            db.execSQL(CREATE_TABLE_EVENTS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (db.isOpen()) {
            // on upgrade drop older tables
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENT);

            // create new tables
            onCreate(db);
        }
    }


    // ------------------------ "Events" table methods ----------------//

    /**
     * Creating a Event
     */
    public long createEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = convertEventToContentValues(event);


        values.put(KEY_CREATED_AT, getDateTime());


        // insert row
        return db.insert(TABLE_EVENT, null, values);
    }


    /**
     * get single event
     */
    public Event getEvent(float eventId) {

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_EVENT + " WHERE "
                + KEY_EVENT_ID + " = " + eventId;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();


        return convertRawToEvent(c);
    }


    /**
     * getting all events
     */
    public ArrayList<Event> getAlEvents() {
        ArrayList<Event> events = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_EVENT;

        Log.d(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();

        if (db.isOpen()) {
            Cursor c = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (c.moveToFirst()) {
                do {
                    Event event = convertRawToEvent(c);

                    events.add(event);
                } while (c.moveToNext());
            }
            c.close();
            return events;
        }else {
            return new ArrayList<>();
        }
    }

    public ArrayList<Event> getListFromCursor(Cursor cursor) {
        Log.d(LOG, "getListFromCursor() ");
        ArrayList<Event> events = new ArrayList<>();

        if (cursor == null){
            return new ArrayList<>();
        }

        if (cursor.moveToFirst()) {
            do {
                Event event = convertRawToEvent(cursor);

                events.add(event);
            } while (cursor.moveToNext());
        }

        return events;
    }


    /**
     * getting event count
     */
    public int getEventsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_EVENT;

        SQLiteDatabase db = this.getReadableDatabase();

        int count = 0;
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery(countQuery, null);

            count = cursor.getCount();

            cursor.close();
        }

        // return count
        return count;
    }

    /**
     * getting cursor
     */
    public Cursor getCursorOrderByTime() {
        String countQuery = "SELECT  * FROM " + TABLE_EVENT + " ORDER BY " + KEY_EVENT_ID + " DESC";
        Log.d(LOG, countQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        if (db.isOpen()) {
            Cursor c = db.rawQuery(countQuery, null);
            return c;
        }else {
            return null;
        }

    }

    public Cursor getCursorIfHaveExtra() {
        String countQuery = "SELECT  * FROM " + TABLE_EVENT +
                " WHERE (" + KEY_HAVE_EXTRA + " AND " +  KEY_GROUP_ID + " != 20 )";

        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery(countQuery, null);

    }

    public Cursor getCursorOrderByTimeFilterByDurationStartFromOneEvent(int minEventDuration, float eventId) {
        String countQuery = "SELECT  * FROM " + TABLE_EVENT +
                " WHERE (" + KEY_DURATION + " > " + minEventDuration +
                " AND " + KEY_EVENT_ID + " >= " + eventId +
                " AND " + KEY_GROUP_ID + " != 20 " +
                " OR (" + KEY_EVENT_ID + " >= " + eventId +
                " AND " + KEY_GROUP_ID + " != 20 " +
                " AND " + KEY_END_TIME + " IS NULL OR " + KEY_END_TIME + "= ''))" +
                " ORDER BY " + KEY_EVENT_ID + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery(countQuery, null);

    }

    public Cursor getCursorOrderByTimeFilterByDurationWithoutWork(int minEventDuration) {
        String countQuery = "SELECT  * FROM " + TABLE_EVENT +
                " WHERE (" + KEY_TYPE + " = 0 AND " + KEY_DURATION + " > " + minEventDuration +
                " OR (" + KEY_GROUP_ID + " = 20)" +
                " OR (" + KEY_TYPE + " = 0 AND " + KEY_END_TIME + " IS NULL OR " + KEY_END_TIME + "= ''))" +
                " ORDER BY " + KEY_EVENT_ID + " DESC";

        Log.d(LOG, countQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        if (db.isOpen()) {
            return db.rawQuery(countQuery, null);
        }
        return null;
    }
    public Cursor getStopTypeShiftOrderByTimeFilterByDurationWithoutWork(int minEventDuration) {
        String countQuery = "SELECT  * FROM " + TABLE_EVENT +
                " WHERE (" + KEY_TYPE + " = 0 AND " + KEY_GROUP_ID + " != 20 AND " +
                KEY_DURATION + " > " + minEventDuration + ")" +
                " OR (" + KEY_TYPE + " = 0 AND " + KEY_GROUP_ID + " != 20 AND (" + KEY_END_TIME + " IS NULL OR " + KEY_END_TIME + " = ''))" +
                " ORDER BY " + KEY_EVENT_ID + " DESC";

        Log.d(LOG, countQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        if (db.isOpen()) {
            return db.rawQuery(countQuery, null);
        }
        return null;
    }

    public Cursor getStopTypeShiftOrderByTimeFilterByDuration(int minEventDuration) {
        String countQuery = "SELECT  * FROM " + TABLE_EVENT +
                " WHERE (" + KEY_GROUP_ID + " != 20 AND " +
                KEY_DURATION + " > " + minEventDuration + ")" +
                " OR (" + KEY_GROUP_ID + " != 20 AND (" + KEY_END_TIME + " IS NULL OR " + KEY_END_TIME + " = ''))" +
                " ORDER BY " + KEY_EVENT_ID + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery(countQuery, null);

    }

    public Cursor getStopTypeShiftOrderByTimeFilterByDurationAndStartTime(int minEventDuration, String newEventsStartTime) {
        String countQuery = "SELECT  * FROM " + TABLE_EVENT +
                " WHERE (" + KEY_GROUP_ID + " != 20 AND " +
                KEY_TIME + " >= " + newEventsStartTime + " AND " +
                KEY_DURATION + " > " + minEventDuration + ")" +
                " OR (" + KEY_TIME + " >= " + newEventsStartTime + " AND "
                + KEY_GROUP_ID + " != 20 AND (" + KEY_END_TIME + " IS NULL OR " + KEY_END_TIME + " = ''))" +
                " ORDER BY " + KEY_EVENT_ID + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery(countQuery, null);

    }

    public Cursor getStopByReasonIdShiftOrderByTimeFilterByDuration(int minEventDuration, int reasonId) {
        String countQuery = "SELECT  * FROM " + TABLE_EVENT +
                " WHERE (" + KEY_TYPE + " = 0 AND " + KEY_REASON_ID + " = " + reasonId + " AND " +
                KEY_DURATION + " > " + minEventDuration +
                ") OR (" + KEY_TYPE + " = 0 AND " + KEY_REASON_ID + " = " + reasonId + " AND (" + KEY_END_TIME + " IS NULL OR " + KEY_END_TIME + " = ''))" +
                " ORDER BY " + KEY_EVENT_ID + " DESC";

        Log.d(LOG, countQuery);
        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery(countQuery, null);

    }

    public Cursor getCursorOrderByTimeFilterByDuration(int minEventDuration) {
        String countQuery = "SELECT  * FROM " + TABLE_EVENT +
                " WHERE (" + KEY_DURATION + " > " + minEventDuration +
                " AND " +  KEY_GROUP_ID + " != 20 " +
                " OR (" + KEY_END_TIME + " IS NULL OR " + KEY_END_TIME + "= ''" +
                " AND " +  KEY_GROUP_ID + " != 20 ))" +
                " ORDER BY " + KEY_EVENT_ID + " DESC";

        Log.d(LOG, countQuery);
        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery(countQuery, null);

    }
    //    private String getMinimumDurationLimit() {
//        return String.valueOf(MIN_EVENT_DURATION_MILLIS / (60 * 1000));
//    }

    public Cursor getAlarmTypeShiftOrderByTime() {
        String countQuery = "SELECT  * FROM " + TABLE_EVENT + " WHERE " + KEY_GROUP_ID + " = 20" +
                " ORDER BY " + KEY_EVENT_ID + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.rawQuery(countQuery, null);

        return c;

    }

    /**
     * Updating a event
     */
    public int updateEvent(Event event) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = convertEventToContentValues(event);


        // updating row
        return db.update(TABLE_EVENT, values, KEY_EVENT_ID + " = ?",
                new String[]{String.valueOf(event.getEventID())});
    }

    /**
     * Deleting a event
     */
    public void deleteEvent(float event_id) {
        Log.d(LOG, "delete event id:" + event_id);
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_EVENT, KEY_EVENT_ID + " = ?",
                new String[]{String.valueOf(event_id)});
    }


    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

    /**
     * get datetime
     */
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd/MM/YYYY HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    private ContentValues convertEventToContentValues(Event event) {

        ContentValues values = new ContentValues();

        values.put(KEY_EVENT_ID, event.getEventID());
        values.put(KEY_ID, event.getEventID());
        values.put(KEY_PRIORITY, event.getPriority());
        values.put(KEY_TIME, event.getTime());
        values.put(KEY_TIME_MILLIS, getLongFromDateString(event.getTime(), "dd/MM/yyyy HH:mm:ss"));
        values.put(KEY_TITLE, event.getTitle());
        values.put(KEY_E_TITLE, event.getEventETitle());
        values.put(KEY_L_TITLE, event.getEventLTitle());
        values.put(KEY_SUB_TITLE_E_NAME, event.getSubtitleEname());
        values.put(KEY_SUB_TITLE_L_NAME, event.getEventSubTitleLname());
        values.put(KEY_END_TIME, event.getEventEndTime());
        values.put(KEY_GROUP_ID, event.getEventGroupID());
        values.put(KEY_DURATION, event.getDuration());
        values.put(KEY_GROUP_E_NAME, event.getEventGroupEname());
        values.put(KEY_GROUP_L_NAME, event.getEventGroupLname());
        values.put(KEY_ALARM_DISMISSED, event.isAlarmDismissed());
        values.put(KEY_ALARM_H_VALUE, event.getAlarmHValue());
        values.put(KEY_ALARM_L_VALUE, event.getAlarmLValue());
        values.put(KEY_ALARM_STANDARD_VALUE, event.getAlarmStandardValue());
        values.put(KEY_ALARM_VALUE, event.getAlarmValue());
        values.put(KEY_REASON_ID, event.getEventReasonID());
        values.put(KEY_TIME_OF_ADDED, event.getTimeOfAdded());
        values.put(KEY_TREATED, event.isTreated());
        values.put(KEY_IS_DISMISS, event.isIsDismiss());
        values.put(KEY_COLOR, event.getColor());
        values.put(KEY_TYPE, event.getType());
        values.put(KEY_NOTIFICATIONS, event.getNotificationsJson());
        values.put(KEY_REJECTS, event.getRejectsJson());
        values.put(KEY_INVENTORY, event.getInventoriesJson());
        values.put(KEY_ALARMS_EVENTS, event.getAlarmsEventsJson());
        values.put(KEY_JOB_DATA_ITEMS, event.getJobDataItemsJson());
        values.put(KEY_QUALITY_TEST_ITEMS, event.getQualityTestsJson());
        values.put(KEY_HAVE_EXTRA, event.haveExtra());
        values.put(KEY_DESCR, event.getDescr());

        return values;
    }

    @SuppressLint("SimpleDateFormat")
    private static long getLongFromDateString(String date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        long timeInMilliseconds = 0;
        try {
            Date mDate = sdf.parse(date);
            timeInMilliseconds = mDate.getTime();
        } catch (java.text.ParseException e) {
            if (e.getMessage() != null) {
            }

//                Log.e(LOG_TAG,e.getMessage());
        }
        return timeInMilliseconds;
    }

    private Event convertRawToEvent(Cursor c) {

        Event event = new Event();

        if (c != null && c.getCount() > 0) {

            event.setEventID(c.getFloat(c.getColumnIndex(KEY_EVENT_ID)));
            event.setPriority(c.getInt(c.getColumnIndex(KEY_PRIORITY)));
            event.setEventTime(c.getString(c.getColumnIndex(KEY_TIME)));
            event.setEventTimeInMillis(c.getLong(c.getColumnIndex(KEY_TIME_MILLIS)));
            event.setEventTitle(c.getString(c.getColumnIndex(KEY_TITLE)));
            event.setmEventETitle(c.getString(c.getColumnIndex(KEY_E_TITLE)));
            event.setmEventLTitle(c.getString(c.getColumnIndex(KEY_L_TITLE)));
            event.setEventSubTitleEname(c.getString(c.getColumnIndex(KEY_SUB_TITLE_E_NAME)));
            event.setEventSubTitleLname(c.getString(c.getColumnIndex(KEY_SUB_TITLE_L_NAME)));
            event.setEventEndTime(c.getString(c.getColumnIndex(KEY_END_TIME)));
            event.setEventGroupID(c.getInt(c.getColumnIndex(KEY_GROUP_ID)));
            event.setDuration(c.getInt(c.getColumnIndex(KEY_DURATION)));
            event.setmEventGroupEname(c.getString(c.getColumnIndex(KEY_GROUP_E_NAME)));
            event.setEventGroupLname(c.getString(c.getColumnIndex(KEY_GROUP_L_NAME)));
            event.setAlarmDismissed(c.getInt(c.getColumnIndex(KEY_ALARM_DISMISSED)) > 0);
            event.setAlarmHValue(c.getInt(c.getColumnIndex(KEY_ALARM_H_VALUE)));
            event.setAlarmLValue(c.getInt(c.getColumnIndex(KEY_ALARM_L_VALUE)));
            event.setAlarmStandardValue(c.getInt(c.getColumnIndex(KEY_ALARM_STANDARD_VALUE)));
            event.setAlarmLValue(c.getInt(c.getColumnIndex(KEY_ALARM_VALUE)));
            event.setEventReasonID(c.getInt(c.getColumnIndex(KEY_REASON_ID)));
            event.setTimeOfAdded(c.getInt(c.getColumnIndex(KEY_TIME_OF_ADDED)));
            event.setTreated(c.getInt(c.getColumnIndex(KEY_TREATED)) > 0);
            event.setIsDismiss(c.getInt(c.getColumnIndex(KEY_IS_DISMISS)) > 0);
            event.setColor(c.getString(c.getColumnIndex(KEY_COLOR)));
            event.setType(c.getInt(c.getColumnIndex(KEY_TYPE)));
            event.setNotificationsJson(c.getString(c.getColumnIndex(KEY_NOTIFICATIONS)));
            event.setRejectsJson(c.getString(c.getColumnIndex(KEY_REJECTS)));
            event.setInventoriesJson(c.getString(c.getColumnIndex(KEY_INVENTORY)));
            event.setAlarmsEventsJson(c.getString(c.getColumnIndex(KEY_ALARMS_EVENTS)));
            event.setJobDataItemsJson(c.getString(c.getColumnIndex(KEY_JOB_DATA_ITEMS)));
            event.setQualityTestsJson(c.getString(c.getColumnIndex(KEY_QUALITY_TEST_ITEMS)));
            event.setHaveExtra(c.getInt(c.getColumnIndex(KEY_HAVE_EXTRA)) > 0);
            event.setDescr(c.getString(c.getColumnIndex(KEY_DESCR)));


        }
        return event;
    }
}
