package com.operatorsapp.utils;

import android.support.annotation.NonNull;

import com.example.common.Event;
import com.example.common.actualBarExtraResponse.ActualBarExtraResponse;
import com.example.common.actualBarExtraResponse.Inventory;
import com.example.common.actualBarExtraResponse.Reject;
import com.example.common.actualBarExtraResponse.WorkingEvent;
import com.example.common.machineJoshDataResponse.JobDataItem;
import com.operatorsapp.R;
import com.ravtech.david.sqlcore.DatabaseHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static android.text.format.DateUtils.DAY_IN_MILLIS;
import static com.operatorsapp.fragments.ActionBarAndEventsFragment.TYPE_ALERT;
import static com.operatorsapp.utils.TimeUtils.SIMPLE_FORMAT_FORMAT;
import static com.operatorsapp.utils.TimeUtils.SQL_T_FORMAT;
import static com.operatorsapp.utils.TimeUtils.convertDateToMillisecond;
import static com.operatorsapp.utils.TimeUtils.getDateFromFormat;

public class SaveHelperNew {

    private static final int MAX_ID = 1000000000;

    public ArrayList<Event> updateList(ArrayList<Event> events, ActualBarExtraResponse actualBarExtraResponse) {

        if (actualBarExtraResponse == null) {
            return events;
        }
        if (events == null) {
            events = new ArrayList<>();
        }

//        removeOldUpdatedExtras(events, actualBarExtraResponse);
        ArrayList<WorkingEvent> workingEvents = actualBarExtraResponse.getWorkingEvents();
        Collections.reverse(workingEvents);

        ArrayList<Event> toReturn = new ArrayList<>();

        Collections.reverse(events);

        Event firstEvent = new Event();
        Event lastEvent = new Event();

        firstEvent.setEventTime(TimeUtils.getDateFromFormat(new Date(new Date().getTime() - DAY_IN_MILLIS), SIMPLE_FORMAT_FORMAT));
//        if (events.size() > 0) {
//            firstEvent.setEventEndTime(events.get(0).getEventEndTime());
//        }else {
        firstEvent.setEventEndTime(TimeUtils.getDateFromFormat(new Date(new Date().getTime() - DAY_IN_MILLIS + 100), SIMPLE_FORMAT_FORMAT));
//        }
        firstEvent.setEventID(1);
        events.add(0, firstEvent);

        if (events.size() > 1 && events.get(events.size() - 1).getEventEndTime() == null || events.get(events.size() - 1).getEventEndTime().length() == 0) {
            events.get(events.size() - 1).setEventEndTime(TimeUtils.getDateFromFormat(new Date(new Date().getTime()), SIMPLE_FORMAT_FORMAT));
        } else {
            lastEvent.setEventTime(TimeUtils.getDateFromFormat(new Date(new Date().getTime() - 100), SIMPLE_FORMAT_FORMAT));
            lastEvent.setEventEndTime(TimeUtils.getDateFromFormat(new Date(new Date().getTime()), SIMPLE_FORMAT_FORMAT));
            lastEvent.setEventID(MAX_ID);
            events.add(events.size(), lastEvent);
        }

        for (int i = 0; i < events.size() - 1; i++) {
            Event event = events.get(i);
            Long eventStartMilli = convertDateToMillisecond(event.getEventEndTime());
            Long eventEndMilli = convertDateToMillisecond(events.get(i + 1).getEventTime());
            int counter = 0;

            if (event.getEventID() != 1 && event.getEventID() != MAX_ID) {
                addDetailsToEvents(event, actualBarExtraResponse);
                toReturn.add(event);
            }
            for (WorkingEvent workingEvent : workingEvents) {
                counter += 1;
                Long workingEventSentTime = convertDateToMillisecond(workingEvent.getStartTime(), SQL_T_FORMAT);
                Long workingEventEndTime = convertDateToMillisecond(workingEvent.getEndTime(), SQL_T_FORMAT);

                if (events.get(i).getEventGroupID() != TYPE_ALERT) {

                    if (eventStartMilli != 0 && eventEndMilli != 0 && workingEventSentTime != 0 && workingEventEndTime != 0
                            && workingEventEndTime <= eventEndMilli
                            && workingEventSentTime >= eventStartMilli) {

                        Event workingEvent1 = createIntermediateEvent(getDateFromFormat(new Date(workingEventSentTime), SIMPLE_FORMAT_FORMAT),
                                getDateFromFormat(new Date(workingEventEndTime), SIMPLE_FORMAT_FORMAT), event.getEventID(), workingEventSentTime, workingEventEndTime,
                                getWorkingEventName(workingEvent), getWorkingEventName(workingEvent),
                                counter, workingEvent.getColor(), 1);
                        workingEvent1.setDuration(workingEvent.getDuration());

                        switch (workingEvent.getEventDistributionID()) {
                            case 2:
                            case 4:
                            case 5:
                                workingEvent1.setEventReasonID(-2);
                                break;
                            default:
                                workingEvent1.setEventReasonID(0);
                                break;
                        }
//                        if (DataSupport.count(Event.class) == 0 || !DataSupport.isExist(Event.class, DatabaseHelper.KEY_EVENT_ID + " = ?", String.valueOf(workingEvent1.getEventID()))) {

                        addDetailsToEvents(workingEvent1, actualBarExtraResponse);

                        toReturn.add(workingEvent1);
//                        }
                    }
                }
            }

        }
        if (events.get(events.size() - 1).getEventID() != MAX_ID) {
            toReturn.add(events.get(events.size() - 1));
        }
        Collections.reverse(toReturn);

        return toReturn;
    }

    private String getWorkingEventName(WorkingEvent workingEvent){
        if (workingEvent.getEventReason() != null && workingEvent.getEventReason().length() > 0) {
            return workingEvent.getEventReason();
        }else if (workingEvent.getName() != null && workingEvent.getName().length() > 0){
            return workingEvent.getName();
        }else {
            return "working";
        }

    }
    private void removeOldUpdatedExtras(ArrayList<Event> events, ActualBarExtraResponse actualBarExtraResponse) {

        if (actualBarExtraResponse == null) {
            return;
        }

        for (Event event : events) {

            ArrayList<Inventory> eventInventories = event.getInventories();
            ArrayList<Inventory> inventories = actualBarExtraResponse.getInventory();
            if (eventInventories != null && eventInventories.size() > 0
                    && inventories != null && inventories.size() > 0) {
                ArrayList<Inventory> toDelete = new ArrayList<>();
                for (Inventory inventory : inventories) {
                    for (Inventory eventInventory : eventInventories) {
                        if (inventory.getID().equals(eventInventory.getID())) {
                            toDelete.add(eventInventory);
                        }
                    }
                }
                eventInventories.removeAll(toDelete);
                if (eventInventories.size() == 0) {
                    eventInventories = null;
                }
                event.setInventories(eventInventories);
            }
            ArrayList<Reject> eventRejects = event.getRejects();
            ArrayList<Reject> rejects = actualBarExtraResponse.getRejects();
            if (eventRejects != null && eventRejects.size() > 0
                    && rejects != null && rejects.size() > 0) {
                ArrayList<Reject> toDelete = new ArrayList<>();
                for (Reject reject : rejects) {
                    for (Reject eventReject : eventRejects) {
                        if (reject.getID().equals(eventReject.getID())) {
                            toDelete.add(eventReject);
                        }
                    }
                }
                eventRejects.removeAll(toDelete);
                if (eventRejects.size() == 0) {
                    eventRejects = null;
                }
                event.setRejects(eventRejects);
            }
            //update old call
//            ArrayList<com.example.common.actualBarExtraResponse.Notification> eventNotifications = event.getNotifications();
//            ArrayList<com.example.common.actualBarExtraResponse.Notification> notifications = actualBarExtraResponse.getNotification();
//            if (eventNotifications != null && eventNotifications.size() > 0
//                    && notifications != null && notifications.size() > 0) {
//                ArrayList<com.example.common.actualBarExtraResponse.Notification> toDelete = new ArrayList<>();
//                for (com.example.common.actualBarExtraResponse.Notification notification : notifications) {
//                    for (com.example.common.actualBarExtraResponse.Notification eventsNotification : eventNotifications) {
//                        if (notification.getID().equals(eventsNotification.getID())) {
//                            toDelete.add(eventsNotification);
//                        }
//                    }
//                }
//                eventNotifications.removeAll(toDelete);
//                if (eventNotifications.size() == 0) {
//                    eventNotifications = null;
//                }
//                event.setNotifications(eventNotifications);
//            }
            event.setHaveExtra(event.getNotifications() != null || event.getInventories() != null || event.getRejects() != null);

            event.updateAll(DatabaseHelper.KEY_EVENT_ID + " = ?", String.valueOf(event.getEventID()));
        }
    }


    @NonNull
    private Event createIntermediateEvent(String eventTime, String eventEndTime, float id, Long eventStartMilli,
                                          Long eventEndMilli, String lName, String eName,
                                          float differenceForNewId, String color, int type) {
        Event workingEvent = new Event();
        workingEvent.setEventTime(eventTime);
        workingEvent.setEventEndTime(eventEndTime);

        workingEvent.setEventSubTitleLname(lName);
        workingEvent.setEventSubTitleEname(eName);
        workingEvent.setColor(color);

        workingEvent.setEventID(id + differenceForNewId);
        workingEvent.setType(type);

        long duration = eventEndMilli - eventStartMilli;
        if (duration > DAY_IN_MILLIS) {
            duration = DAY_IN_MILLIS;
        }
        long minute = TimeUnit.MILLISECONDS.toMinutes(duration);

        workingEvent.setDuration(minute);
        return workingEvent;
    }

    private boolean addDetailsToEvents(Event event, ActualBarExtraResponse actualBarExtraResponse) {

        if (actualBarExtraResponse != null) {
            Long eventStart = convertDateToMillisecond(event.getEventTime(), SIMPLE_FORMAT_FORMAT);
            Long eventEnd = new Date().getTime();
            if (event.getEventEndTime() != null && event.getEventEndTime().length() > 0) {
                eventEnd = convertDateToMillisecond(event.getEventEndTime(), SIMPLE_FORMAT_FORMAT);
            }

            boolean haveRejects = addRejectsToEvents(eventStart, eventEnd, event, actualBarExtraResponse);
            boolean haveInventory = addInventoryToEvents(eventStart, eventEnd, event, actualBarExtraResponse);
            if (haveRejects || haveInventory) {
                event.setHaveExtra(true);
            }
            if (addNotificationsToEvents(eventStart, eventEnd, event, actualBarExtraResponse)//addAlarmEvents(eventStart, eventEnd, event, actualBarExtraResponse)|| for add alarms in shiftlog
                    | haveRejects
                    | haveInventory//| addDetailsToWorking(eventStart, eventEnd, event, actualBarExtraResponse)
                    | addStartProductToEvents(eventStart, eventEnd, event, actualBarExtraResponse)) {
                return true;
            }
        }
        return false;
    }

    private boolean addRejectsToEvents(Long eventStart, Long eventEnd, Event event, ActualBarExtraResponse actualBarExtraResponse) {
        if (actualBarExtraResponse == null) {
            return false;
        }
        ArrayList<Reject> rejects = actualBarExtraResponse.getRejects();
        ArrayList<Reject> toDelete = new ArrayList<>();
        if (rejects != null && rejects.size() > 0) {

            ArrayList<Reject> rejectArrayList = event.getRejects();

            for (Reject reject : rejects) {
                Long rejectTime = convertDateToMillisecond(reject.getTime(), SIMPLE_FORMAT_FORMAT);

                if (eventStart <= rejectTime && rejectTime <= eventEnd) {
                    if (rejectArrayList == null) {
                        rejectArrayList = new ArrayList<>();
                    }
                    rejectArrayList.add(reject);
                    toDelete.add(reject);
                }
            }
            if (rejectArrayList != null) {
                event.setRejects(rejectArrayList);
                rejects.removeAll(toDelete);
                if (rejects.size() == 0) {
                    rejects = null;
                }
                return true;
            }
        }
        return false;
    }

    private boolean addStartProductToEvents(Long eventStart, Long eventEnd, Event event, ActualBarExtraResponse actualBarExtraResponse) {

        if (actualBarExtraResponse == null) {
            return false;
        }
        ArrayList<JobDataItem> jobDataItems = (ArrayList<JobDataItem>) actualBarExtraResponse.getJobData();
        ArrayList<JobDataItem> toDelete = new ArrayList<>();
        if (jobDataItems != null && jobDataItems.size() > 0) {

            ArrayList<JobDataItem> jobDataItemArrayList = event.getJobDataItems();

            for (JobDataItem jobDataItem : jobDataItems) {
                Long jobDataItemSentTime = convertDateToMillisecond(jobDataItem.getStartTime(), SIMPLE_FORMAT_FORMAT);

                if (eventStart <= jobDataItemSentTime && jobDataItemSentTime <= eventEnd) {

                    if (jobDataItemArrayList == null) {
                        jobDataItemArrayList = new ArrayList<>();
                    }

                    jobDataItemArrayList.add(jobDataItem);
                    toDelete.add(jobDataItem);
                }
            }
            if (jobDataItemArrayList != null) {
                event.setJobDataItems(jobDataItemArrayList);
                jobDataItems.removeAll(toDelete);
                if (jobDataItems.size() == 0) {
                    jobDataItems = null;
                }
                return true;
            }
        }
        return false;
    }

    private boolean addNotificationsToEvents(Long eventStart, Long eventEnd, Event event, ActualBarExtraResponse actualBarExtraResponse) {
        if (actualBarExtraResponse == null) {
            return false;
        }
        ArrayList<com.example.common.actualBarExtraResponse.Notification> notifications = actualBarExtraResponse.getNotification();
        ArrayList<com.example.common.actualBarExtraResponse.Notification> toDelete = new ArrayList<>();
        if (notifications != null && notifications.size() > 0) {

            ArrayList<com.example.common.actualBarExtraResponse.Notification> notificationArrayList = event.getNotifications();

            for (com.example.common.actualBarExtraResponse.Notification notification : notifications) {
                Long notificationSentTime = convertDateToMillisecond(notification.getResponseDate(), SQL_T_FORMAT);

                if (eventStart <= notificationSentTime && notificationSentTime <= eventEnd) {

                    if (notificationArrayList == null) {
                        notificationArrayList = new ArrayList<>();
                    }

                    notificationArrayList.add(notification);
                    toDelete.add(notification);
                }
            }
            if (notificationArrayList != null) {
                event.setNotifications(notificationArrayList);
                notifications.removeAll(toDelete);
                if (notifications.size() == 0) {
                    notifications = null;
                }
                return true;
            }
        }
        return false;
    }

    private boolean addInventoryToEvents(Long eventStart, Long eventEnd, Event event, ActualBarExtraResponse actualBarExtraResponse) {
        if (actualBarExtraResponse == null) {
            return false;
        }
        ArrayList<Inventory> inventories = actualBarExtraResponse.getInventory();
        ArrayList<Inventory> toDelete = new ArrayList<>();

        if (inventories != null && inventories.size() > 0) {

            ArrayList<Inventory> inventoriesArrayList = event.getInventories();

            for (Inventory inventory : inventories) {
                Long inventoryTime = convertDateToMillisecond(inventory.getTime(), SIMPLE_FORMAT_FORMAT);

                if (eventStart <= inventoryTime && inventoryTime <= eventEnd) {
                    if (inventoriesArrayList == null) {
                        inventoriesArrayList = new ArrayList<>();
                    }
                    inventoriesArrayList.add(inventory);
                    toDelete.add(inventory);
                }
            }
            if (inventoriesArrayList != null) {
                event.setInventories(inventoriesArrayList);
                inventories.removeAll(toDelete);
                if (inventories.size() == 0) {
                    inventories = null;
                }
                return true;
            }
        }
        return false;
    }


    private void addNotificationsToPasteEvents(ArrayList<Event> events, ActualBarExtraResponse actualBarExtraResponse) {

        if (actualBarExtraResponse == null) {
            return;
        }
        if (actualBarExtraResponse.getInventory() != null || actualBarExtraResponse.getNotification() != null ||
                actualBarExtraResponse.getRejects() != null) {

            for (Event event : events) {
                if (event.getEventGroupID() != TYPE_ALERT && (actualBarExtraResponse.getInventory() != null || actualBarExtraResponse.getNotification() != null ||
                        actualBarExtraResponse.getRejects() != null)) {
                    if (addDetailsToEvents(event, actualBarExtraResponse)) {
                        event.updateAll(DatabaseHelper.KEY_EVENT_ID + " = ?", String.valueOf(event.getEventID()));
                    }
                }
            }
        }
    }
}
