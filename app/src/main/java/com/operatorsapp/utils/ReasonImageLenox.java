package com.operatorsapp.utils;

import com.operatorsapp.R;

public class ReasonImageLenox {

    public static int getImageForStopReason(int stopReasonId) {

        int imageId;

        switch (stopReasonId) {
            case 2: {
                imageId = R.drawable.maintenence;
                break;
            }
            case 4: {
                imageId = R.drawable.stop_malfunction_selector;
                break;
            }
            case 6: {
                imageId = R.drawable.stop_qa_selector;
                break;
            }
            case 7: {
                imageId = R.drawable.material;
                break;
            }
            case 9: {
                imageId = R.drawable.blade_change;
                break;
            }
            case 12: {
                imageId = R.drawable.stops_shift_seletor_lenox;

                break;
            }
            default:{
                imageId = R.drawable.stop_qa_selector;

            }
        }
        return imageId;
    }

    public static int getImageForStopReasonShiftLog(int stopReasonId) {

        int imageId;

        switch (stopReasonId) {
            case 2: {
                imageId = R.drawable.maintenance_side_lenox;
                break;
            }
            case 4: {
                imageId = R.drawable.malfunction_side_lenox;
                break;
            }
            case 6: {
                imageId = R.drawable.other_side;
                break;
            }
            case 7: {
                imageId = R.drawable.material_side;
                break;
            }
            case 9: {
                imageId = R.drawable.blade_side_lenox;
                // mold
                break;
            }
            case 12: {
                // expected stops
                imageId = R.drawable.shift_side;

                break;
            }
            default:{
                imageId = R.drawable.other_side;

            }
        }
        return imageId;
    }

    public static int getSubReasonIc(int subReasons) {

        int imageId;

        switch (subReasons) {
            case 2: {
                imageId = R.drawable.maintenance_subreason;
                break;
            }
            case 4: {
                imageId = R.drawable.malfunction_subreason;
                break;
            }
            case 6: {
                imageId = R.drawable.other_subreason;
                break;
            }
            case 7: {
                imageId = R.drawable.material_subreason;
                break;
            }
            case 9: {
                imageId = R.drawable.blade_subreason;
                break;
            }
            case 12: {
                imageId = R.drawable.shift_subreason;

                break;
            }
            default:{
                imageId = R.drawable.other_subreason;

            }
        }
        return imageId;

    }public static int getSubReasonBackgroundColor(int subReasons) {

        int imageId;

        switch (subReasons) {
            case 2: {
                imageId = R.drawable.circle_yellow;
                break;
            }
            case 4: {
                imageId = R.drawable.circle_purple;
                break;
            }
            case 6: {
                imageId = R.drawable.circle_purple_other;
                break;
            }
            case 7: {
                imageId = R.drawable.circle_green;
                break;
            }
            case 9: {
                imageId = R.drawable.circle_blue_blade;
                break;
            }
            case 12: {
                imageId = R.drawable.circle_blue_light;

                break;
            }
            default:{
                imageId = R.drawable.circle_purple_other;

            }
        }
        return imageId;

    }
}
