package com.operatorsapp.utils;

import android.content.Context;

import androidx.core.content.ContextCompat;

import com.operatorsapp.R;

public class TaskUtil {

    public static int getPriorityColor(int priorityId, Context context) {
        int color;
        switch (priorityId) {
            case 1:
                color = ContextCompat.getColor(context, R.color.green_light);
                break;
//            case 2:
//                color = ContextCompat.getColor(context, R.color.alert);
//                break;
            case 3:
                color = ContextCompat.getColor(context, R.color.red_dark);
                break;
            case 4:
                color = ContextCompat.getColor(context, R.color.red_line);
                break;
            default:
                color = ContextCompat.getColor(context, R.color.alert);
                break;
        }
        return color;

    }

    public static String getPriorityName(int priorityId, Context context) {
        String name = "";
        switch (priorityId) {
            case 1:
                name = context.getString(R.string.low);
                break;
            case 2:
                name = context.getString(R.string.medium);
                break;
            case 3:
                name = context.getString(R.string.high);
                break;
            case 4:
                name = context.getString(R.string.very_high);
                break;
        }
        return name;

    }


}
