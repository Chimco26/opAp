package com.operatorsapp.view.widgetViewHolders;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;

class ProjectionDrawablesHelper {

    Drawable createRangeShape(String color) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setCornerRadii(new float[]{0, 0, 0, 0, 60, 60, 60, 60});
        drawable.setSize(8, 48);
        drawable.setColor(Color.parseColor(color));
        return drawable;
    }

    @NonNull
    private Drawable createStartProjectionShapeDrawable(String color) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setCornerRadii(new float[]{60, 60, 0, 0, 0, 0, 60, 60});
        drawable.setSize(30, 43);
        drawable.setColor(Color.parseColor(color));
        return drawable;
    }

    @NonNull
    private Drawable createEndProjectionShapeDrawable(String color) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setCornerRadii(new float[]{0, 0, 60, 60, 60, 60, 0, 0});
        drawable.setSize(30, 43);
        drawable.setColor(Color.parseColor(color));
        return drawable;
    }

    Drawable createProjectionShape(String color) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setSize(30, 43);
        drawable.setColor(Color.parseColor(color));
        return drawable;
    }

    Drawable createCapsuleDrawable(String color) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setCornerRadii(new float[]{60, 60, 60, 60, 60, 60, 60, 60});
        drawable.setSize(270, 60);
        drawable.setColor(Color.parseColor(color));
        return drawable;
    }

    Drawable createEndProjectionShape(String color, Context context) {
//        Configuration config = context.getResources().getConfiguration();
//        if (config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
//            return createStartProjectionShapeDrawable(color);
//        }
        return createEndProjectionShapeDrawable(color);
    }

    Drawable createStartProjectionShape(String color, Context context) {
//        Configuration config = context.getResources().getConfiguration();
//        if (config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
//            return createEndProjectionShapeDrawable(color);
//        }
        return createStartProjectionShapeDrawable(color);
    }
}
