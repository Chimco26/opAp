package com.operatorsapp.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.operatorsapp.R;
import com.zemingo.logrecorder.ZLogger;

/**
 * Show/Hide password in an EditText
 */
public class ShowHidePasswordEditText extends AppCompatEditText implements GestureDetector.OnGestureListener {
    private static final String LOG_TAG = ShowHidePasswordEditText.class.getSimpleName();
    private boolean mShowPassword = false;
    private GestureDetectorCompat mDetector;

    private int mShowResId;
    private int mHideResId;

    public ShowHidePasswordEditText(Context context) {
        super(context);
        init(context, null);
    }

    public ShowHidePasswordEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ShowHidePasswordEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //intercept hide/show drawable clicks
        mDetector.onTouchEvent(event);

        if (hasTouchedDrawable(event)) {
            /* This prevents the keyboard from opening if we touched the show/hide password */
            return true;
        }

        return super.onTouchEvent(event);

    }

    private boolean hasTouchedDrawable(MotionEvent e) {
        final int DRAWABLE_RIGHT = 2;
        Drawable drawable = getCompoundDrawables()[DRAWABLE_RIGHT];
        return drawable != null && e.getX() >= (getWidth() - drawable.getBounds().width());
    }

    private void init(Context context, AttributeSet attrs) {
        initDrawables(context, attrs);
        setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        setTransformationMethod(PasswordTransformationMethod.getInstance());
        mDetector = new GestureDetectorCompat(getContext(), this);
        addTextListener();
    }

    private void initDrawables(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ShowHidePasswordEditText, 0, 0);
            if (array != null) {
                mShowResId = array.getResourceId(R.styleable.ShowHidePasswordEditText_icn_password_disabled, 0);
                mHideResId = array.getResourceId(R.styleable.ShowHidePasswordEditText_icn_password_hidden, 0);
                array.recycle();
            }
        }
    }

    private void addTextListener() {
        super.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    removePasswordDrawable();
                } else {
                    toggleShowHide(mShowPassword);
                }
            }
        });
    }

    private void toggleShowHide(boolean show) {
        if (show) {
            //show the password
            setTransformationMethod(null);
            setCompoundDrawablesWithIntrinsicBounds(0, 0, mShowResId, 0);
            setSelection(getText().length());
        } else {
            //hide the password
            setTransformationMethod(PasswordTransformationMethod.getInstance());
            setCompoundDrawablesWithIntrinsicBounds(0, 0, mHideResId, 0);
            setSelection(getText().length());
        }
    }

    private void removePasswordDrawable() {
        setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        //gotta return true for the gesture detector
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        //don't need this
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {

        ZLogger.d(LOG_TAG, "onSingleTapUp(), pos: " + e.getX() + ", Right: " + getRight());
        if (hasTouchedDrawable(e)) {
            // your action here
            mShowPassword = !mShowPassword;
            toggleShowHide(mShowPassword);
        }
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        //don't need this
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        //don't need this
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        //don't need this
        return false;
    }
}
