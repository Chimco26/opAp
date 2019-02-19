package com.operatorsapp.utils;

import android.annotation.SuppressLint;

import com.operators.machinedatainfra.models.Widget;

public class WidgetAdapterUtils {

    public  enum StringParse {
        INT, FLOAT
    }

    public static float tryParse(String value, StringParse stringParse) {
        try {
            if (stringParse == StringParse.INT) {
                return Integer.parseInt(value);
            }
            if (stringParse == StringParse.FLOAT) {
                return Float.parseFloat(value);
            }
        } catch (NumberFormatException nfe) {
            // Log exception.
            return 0;
        }
        return 0;
    }

    @SuppressLint("DefaultLocale")
    public static String valueInK(float value) {
        String valueString;
        if (value >= 1000) {
            float valueFloat = value / 1000;
            if (value % 100 == 0) {
                valueString = String.format("%.1f", valueFloat);
            } else {
                valueString = String.format("%.1f", valueFloat);
            }
            if (value % 1000 == 0) {
                valueString = String.valueOf(value / 1000);
            }
            return valueString + "k";
        } else {
            return String.valueOf((int) value);
        }
    }

    public static boolean isNotNearestTexts(Widget widget) {
        float size = widget.getStandardValue() - widget.getLowLimit();
        try {
            return ((widget.getProjection() - Float.valueOf(widget.getCurrentValue())) / size > 0.15);
        } catch (NumberFormatException e) {
            return false;
        }
    }
    public static boolean isNotNearestTextsNew(Widget widget, float currentValue) {
        float size = Math.abs(widget.getProjection() - currentValue);
        return size * 100 / widget.getTarget() > 15;
//        return !(currentValue * 100 / widget.getProjection() > 90
//        && currentValue * 100 / widget.getProjection() < 110);
    }

}
