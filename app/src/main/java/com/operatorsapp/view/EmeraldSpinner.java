package com.operatorsapp.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Display;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Spinner;


public class EmeraldSpinner extends android.support.v7.widget.AppCompatSpinner {
    AdapterView.OnItemSelectedListener listener;

    private OnSpinnerEventsListener mListener;
    private boolean mOpenInitiated = false;

    public EmeraldSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EmeraldSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public EmeraldSpinner(Context context, AttributeSet attrs, int defStyleAttr, int mode) {
        super(context, attrs, defStyleAttr, mode);
    }


    public EmeraldSpinner(Context context) {
        super(context);
    }

    public EmeraldSpinner(Context context, int mode) {
        super(context, mode);
    }

    @Override
    public void setSelection(int position) {
        super.setSelection(position);

        if (position == getSelectedItemPosition() && listener != null) {
            listener.onItemSelected(null, null, position, 0);
        }
    }

    public void setOnItemSelectedListener(AdapterView.OnItemSelectedListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean performClick() {
        // register that the Spinner was opened so we have a status
        // indicator for when the container holding this Spinner may lose focus
        mOpenInitiated = true;
        if (mListener != null) {
            mListener.onSpinnerOpened(this);
        }
        return super.performClick();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasBeenOpened() && hasFocus) {
            performClosedEvent();
        }
    }

    /**
     * Register the listener which will listen for events.
     */
    public void setSpinnerEventsListener(
            OnSpinnerEventsListener onSpinnerEventsListener) {
        mListener = onSpinnerEventsListener;
    }

    /**
     * Propagate the closed Spinner event to the listener from outside if needed.
     */
    public void performClosedEvent() {
        mOpenInitiated = false;
        if (mListener != null) {
            mListener.onSpinnerClosed(this);
        }
    }

    /**
     * A boolean flag indicating that the Spinner triggered an open event.
     *
     * @return true for opened Spinner
     */
    public boolean hasBeenOpened() {
        return mOpenInitiated;
    }

    @Override
    public void getWindowVisibleDisplayFrame(Rect outRect) {
        if (android.os.Build.VERSION.SDK_INT != android.os.Build.VERSION_CODES.LOLLIPOP
                && android.os.Build.VERSION.SDK_INT != android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
            WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
            Display d = wm.getDefaultDisplay();
            d.getRectSize(outRect);
            outRect.set(outRect.left, outRect.top + 200, outRect.right, outRect.bottom - 100);
        } else {
            super.getWindowVisibleDisplayFrame(outRect);
        }
    }

    public interface OnSpinnerEventsListener {

        /**
         * Callback triggered when the spinner was opened.
         */
        void onSpinnerOpened(Spinner spinner);

        /**
         * Callback triggered when the spinner was closed.
         */
        void onSpinnerClosed(Spinner spinner);

    }
}
