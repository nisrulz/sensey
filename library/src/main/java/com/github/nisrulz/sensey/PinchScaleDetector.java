package com.github.nisrulz.sensey;

import android.content.Context;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

public class PinchScaleDetector {

  private final ScaleGestureDetector scaleGestureDetector;
  private final PinchScaleListener pinchScaleListener;

  public PinchScaleDetector(Context context, PinchScaleListener pinchScaleListener) {
    scaleGestureDetector = new ScaleGestureDetector(context, new ScaleGestureListener());
    this.pinchScaleListener = pinchScaleListener;
  }

  boolean onTouchEvent(MotionEvent e) {
    return scaleGestureDetector.onTouchEvent(e);
  }

  public interface PinchScaleListener {
    void onScale(ScaleGestureDetector scaleGestureDetector, boolean isScalingOut);

    void onScaleStart(ScaleGestureDetector scaleGestureDetector);

    void onScaleEnd(ScaleGestureDetector scaleGestureDetector);
  }

  private class ScaleGestureListener implements ScaleGestureDetector.OnScaleGestureListener {

    @Override public boolean onScale(ScaleGestureDetector scaleGestureDetector) {

      float scaleFactor = scaleGestureDetector.getScaleFactor();
      if (scaleFactor > 1) {
        pinchScaleListener.onScale(scaleGestureDetector, true);
      } else {
        pinchScaleListener.onScale(scaleGestureDetector, false);
      }
      return true;
    }

    @Override public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
      pinchScaleListener.onScaleStart(scaleGestureDetector);
      return true;
    }

    @Override public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {
      pinchScaleListener.onScaleEnd(scaleGestureDetector);
    }
  }
}
