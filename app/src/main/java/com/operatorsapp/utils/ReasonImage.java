package com.operatorsapp.utils;

import com.operatorsapp.R;

/**
 * Created by david on 12 יולי 2017.
 */

public class ReasonImage {

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
}
