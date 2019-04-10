package com.operatorsapp.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;


public class PinchRecyclerView extends RecyclerView {
    private ScaleGestureDetector scaleGestureDetector;
    private static final String TAG = PinchRecyclerView.class.getSimpleName();

    private float scaleFactor = 1.f;
    private static final float minScale = 1.0f;
    private static final float maxScale = 3.0f;
    private boolean scalling;

    public PinchRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        scaleGestureDetector = new ScaleGestureDetector(getContext(), new ScaleGestureDetector.OnScaleGestureListener() {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                scaleFactor *= detector.getScaleFactor();

                //makes sure user does not zoom in or out past a certain amount
                scaleFactor = Math.max(minScale, Math.min(scaleFactor, maxScale));

                //refresh the view and compute the size of the view in the screen
                if (mListener != null) {
                    mListener.onScale(scaleFactor);
                }
                Log.d(TAG, "onScale: ");
                return true;
            }

            @Override
            public boolean onScaleBegin(ScaleGestureDetector detector) {
                scalling = false;
                Log.d(TAG, "onScaleBegin: ");
                mListener.onScaleBegin();
                return true;
            }

            @Override
            public void onScaleEnd(ScaleGestureDetector detector) {
                Log.d(TAG, "onScaleEnd: ");
                mListener.onScaleEnd();
                scalling = true;
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        //notify the scaleGestureDetector that an event has happened
        scaleGestureDetector.onTouchEvent(event);

        if (event.getAction() == MotionEvent.ACTION_MOVE){
            if (scalling){
                return false;
            }
        }
        return true;
    }

    //    @Override
//    protected void dispatchDraw(@NonNull Canvas canvas) {
//        //scales the display, centered on where the user is touching the display
//        canvas.scale(scaleFactor, scaleFactor, scaleGestureDetector.getFocusX(), scaleGestureDetector.getFocusY());
//
//        super.dispatchDraw(canvas);
//    }
    private PinchRecyclerViewListener mListener;

    public void addListener(PinchRecyclerViewListener pinchRecyclerViewListener) {
        mListener = pinchRecyclerViewListener;
    }

    public void removeListener() {
        mListener = null;
    }

    public interface PinchRecyclerViewListener {

        void onScale(float factor);

        void onScaleEnd();

        void onScaleBegin();
    }
}