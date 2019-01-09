package com.operatorsapp.utils;

import android.graphics.Color;
import android.support.v4.graphics.ColorUtils;

import com.operatorsapp.R;

/**
 * Created by david on 12 יולי 2017.
 */

public class ReasonImage {

    public static int getColorForStopReason(int stopReasonId){
        int color;

        switch (stopReasonId) {
            case 1: {
                color = Color.parseColor("#5ACA96");
                break;
            }
            case 2: {
                color = Color.parseColor("#FF6B6B");
                break;
            }
            case 3: {
                color = Color.parseColor("#736FAA");
                break;
            }
            case 4: {
                color = Color.parseColor("#954B7D");
                break;
            }
            case 5: {
                color = Color.parseColor("#599ABE");
                break;
            }
            case 6: {
                color = Color.parseColor("#FF6B6B");
                break;
            }
            case 7: {
                color = Color.parseColor("#FFC23A");
                break;
            }
            case 8: {
                color = Color.parseColor("#78B4FF");
                // labor
                break;
            }
            case 9: {
                color = Color.parseColor("#78B4FF");
                // mold
                break;
            }
            case 10: {
                color = Color.parseColor("#954B7D");

                break;
            }
            case 12: {
                // expected stops
                color = Color.parseColor("#599ABE");

                break;
            }
            default:{
                color = Color.parseColor("#78B4FF");

            }
        }
        return color;
    }

    public static int getImageForStopReason(int stopReasonId) {

        int imageId;

        switch (stopReasonId) {
            case 1: {
                imageId = R.drawable.stop_oparations_selector;
                break;
            }
            case 2: {
                imageId = R.drawable.stop_maitenance_selector;
                break;
            }
            case 3: {
                imageId = R.drawable.stop_qa_selector;
                break;
            }
            case 4: {
                imageId = R.drawable.stop_malfunction_selector;
                break;
            }
            case 5: {
                imageId = R.drawable.stop_planning_selector;
                break;
            }
            case 6: {
                imageId = R.drawable.stop_machinestop_selector;
                break;
            }
            case 7: {
                imageId = R.drawable.stop_materials_selector;
                break;
            }
            case 8: {
                imageId = R.drawable.stop_labor_selector;
                // labor
                break;
            }
            case 9: {
                imageId = R.drawable.stop_mold_selector;
                // mold
                break;
            }
            case 10: {
                imageId = R.drawable.stop_settings_selector;

                break;
            }
            case 12: {
                // expected stops
                imageId = R.drawable.stop_planning_selector;

                break;
            }
            default:{
                imageId = R.drawable.stop_general_selector;

            }
        }
        return imageId;
    }

    public static int getImageForStopReasonShiftLog(int stopReasonId) {

        int imageId;

        switch (stopReasonId) {
            case 1: {
                imageId = R.drawable.ic_oparations_pressed_v;
                break;
            }
            case 2: {
                imageId = R.drawable.ic_maitenance_pressed_v;
                break;
            }
            case 3: {
                imageId = R.drawable.ic_q_a_pressed_v;
                break;
            }
            case 4: {
                imageId = R.drawable.ic_malfunction_oparations_v;
                break;
            }
            case 5: {
                imageId = R.drawable.ic_planning_pressed_v;
                break;
            }
            case 6: {
                imageId = R.drawable.ic_hand_pressed_v;
                break;
            }
            case 7: {
                imageId = R.drawable.ic_material_pressed_v;
                break;
            }
            case 8: {
                imageId = R.drawable.ic_mold_pressed_v;
                // labor
                break;
            }
            case 9: {
                imageId = R.drawable.ic_mold_pressed_v;
                // mold
                break;
            }
            case 10: {
                imageId = R.drawable.ic_setup_pressed_v;

                break;
            }
            case 12: {
                // expected stops
                imageId = R.drawable.ic_planning_pressed_v;

                break;
            }
            default:{
                imageId = R.drawable.ic_mold_pressed_v;

            }
        }
        return imageId;
    }

}
