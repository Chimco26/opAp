package com.operatorsapp.view.widgetViewHolders;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.view.View;

public class ProjectionDrawablesHelper {

    public Drawable createRangeShape(String color) {
        Drawable drawable = new GradientDrawable();
        ((GradientDrawable) drawable).setShape(GradientDrawable.RECTANGLE);
        ((GradientDrawable) drawable).setCornerRadii(new float[]{0, 0, 0, 0, 60, 60, 60, 60});
        ((GradientDrawable) drawable).setSize(8, 48);
        ((GradientDrawable) drawable).setColor(Color.parseColor(color));
        return drawable;
    }

    public Drawable createStartProjectionShape(String color, Context context) {
        Configuration config = context.getResources().getConfiguration();
        if (config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
            return createEndProjectionShapeDrawable(color);
        }
        return createStartProjectionShapeDrawable(color);
    }

    @NonNull
    public Drawable createStartProjectionShapeDrawable(String color) {
        Drawable drawable = new GradientDrawable();
        ((GradientDrawable) drawable).setShape(GradientDrawable.RECTANGLE);
        ((GradientDrawable) drawable).setCornerRadii(new float[]{60, 60, 0, 0, 0, 0, 60, 60});
        ((GradientDrawable) drawable).setSize(30, 43);
        ((GradientDrawable) drawable).setColor(Color.parseColor(color));
        return drawable;
    }

    public Drawable createEndProjectionShape(String color, Context context) {
        Configuration config = context.getResources().getConfiguration();
        if (config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
            return createStartProjectionShapeDrawable(color);
        }
        return createEndProjectionShapeDrawable(color);
    }

    @NonNull
    public Drawable createEndProjectionShapeDrawable(String color) {
        Drawable drawable = new GradientDrawable();
        ((GradientDrawable) drawable).setShape(GradientDrawable.RECTANGLE);
        ((GradientDrawable) drawable).setCornerRadii(new float[]{0, 0, 60, 60, 60, 60, 0, 0});
        ((GradientDrawable) drawable).setSize(30, 43);
        ((GradientDrawable) drawable).setColor(Color.parseColor(color));
        return drawable;
    }

    public Drawable createProjectionShape(String color) {
        Drawable drawable = new GradientDrawable();
        ((GradientDrawable) drawable).setShape(GradientDrawable.RECTANGLE);
        ((GradientDrawable) drawable).setSize(30, 43);
        ((GradientDrawable) drawable).setColor(Color.parseColor(color));
        return drawable;
    }

    public Drawable createCapsuleDrawable(String color) {
        Drawable drawable = new GradientDrawable();
        ((GradientDrawable) drawable).setShape(GradientDrawable.RECTANGLE);
        ((GradientDrawable) drawable).setCornerRadii(new float[]{60, 60, 60, 60, 60, 60, 60, 60});
        ((GradientDrawable) drawable).setSize(270, 60);
        ((GradientDrawable) drawable).setColor(Color.parseColor(color));
        return drawable;
    }
}
