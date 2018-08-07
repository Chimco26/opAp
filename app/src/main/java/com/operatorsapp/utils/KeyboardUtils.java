package com.operatorsapp.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by david vardi
 */

public class KeyboardUtils {

    public static void closeKeyboard(Activity activity) {

        if (activity != null && activity.getCurrentFocus() != null) {

            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);

            if (imm != null)

                imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }


    @SuppressWarnings("unused")
    public static void closeKeyboard(Context context) {

        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

        if (imm != null) {

            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
    }

    @SuppressWarnings("unused")
    public static void keyboardIsShownA(Activity activity, final KeyboardListener listener) {

        final View decorView = activity.getWindow().getDecorView();

        final int MIN_KEYBOARD_HEIGHT_PX = 150;

        // Register global layout listener.
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            private final Rect windowVisibleDisplayFrame = new Rect();

            private int lastVisibleDecorViewHeight;

            @Override
            public void onGlobalLayout() {

                // Retrieve visible rectangle inside window.
                decorView.getWindowVisibleDisplayFrame(windowVisibleDisplayFrame);

                final int visibleDecorViewHeight = windowVisibleDisplayFrame.height();

                // Decide whether keyboard is visible from changing decor view height.
                if (lastVisibleDecorViewHeight != 0) {

                    Log.d(DavidVardi.DAVID_TAG_SPRINT_1_5, "lastVisibleDecorViewHeight: " + lastVisibleDecorViewHeight);

                    Log.d(DavidVardi.DAVID_TAG_SPRINT_1_5, "visibleDecorViewHeight: " + visibleDecorViewHeight);


                    if (lastVisibleDecorViewHeight > visibleDecorViewHeight + MIN_KEYBOARD_HEIGHT_PX) {

                        // Calculate current keyboard height (this includes also navigation bar height when in fullscreen mode).
                        int currentKeyboardHeight = decorView.getHeight() - windowVisibleDisplayFrame.bottom;

                        // Notify listener about keyboard being shown.
                        listener.onKeyboardShown();

                    } else if (lastVisibleDecorViewHeight + MIN_KEYBOARD_HEIGHT_PX < visibleDecorViewHeight) {
                        // Notify listener about keyboard being hidden.
                        listener.onKeyboardHidden();
                    }
                }

                // Save current decor view height for the next call.
                lastVisibleDecorViewHeight = visibleDecorViewHeight;
            }
        });
    }


    @SuppressWarnings("unused")
    public static void keyboardIsShownB(final Context context, final View view, final KeyboardListener listener) {

        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = view.getRootView().getHeight() - view.getHeight();


                Log.d(DavidVardi.DAVID_TAG_SPRINT_1_5, "view.getRootView().getHeight(): " + view.getRootView().getHeight());

                Log.d(DavidVardi.DAVID_TAG_SPRINT_1_5, "dpToPx(context): " + dpToPx(context));

                Log.d(DavidVardi.DAVID_TAG_SPRINT_1_5, "heightDiff" + heightDiff);

                if (heightDiff > dpToPx(context)) { // if more than 200 dp, it's probably a keyboard...
                    listener.onKeyboardShown();

                } else {

                    listener.onKeyboardHidden();

                }
            }
        });


    }

    public static void keyboardIsShownC( Activity activity,  final KeyboardListener listener) {

        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);

        if (imm != null) {

            if (imm.isAcceptingText()) {
                Log.d(DavidVardi.DAVID_TAG_SPRINT_1_5, "Software Keyboard was shown");
                listener.onKeyboardShown();

            } else {
                Log.d(DavidVardi.DAVID_TAG_SPRINT_1_5, "Software Keyboard was not shown");
                listener.onKeyboardHidden();



            }
        }
    }
    private static float dpToPx(Context context) {

        DisplayMetrics metrics = context.getResources().getDisplayMetrics();

        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) 200, metrics);
    }

    public interface KeyboardListener {

        void onKeyboardShown();

        void onKeyboardHidden();
    }
}
