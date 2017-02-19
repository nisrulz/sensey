package com.github.nisrulz.sensey;

import android.content.Context;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

/**
 * The type Pinch scale detector.
 */
public class PinchScaleDetector {

  private final ScaleGestureDetector scaleGestureDetector;
  private final PinchScaleListener pinchScaleListener;
  private int eventOccurred;
  private int countOfScaleIn;
  private int countOfScaleOut;

  /**
   * Instantiates a new Pinch scale detector.
   *
   * @param context
   *     the context
   * @param pinchScaleListener
   *     the pinch scale listener
   */
  public PinchScaleDetector(Context context, PinchScaleListener pinchScaleListener) {
    scaleGestureDetector = new ScaleGestureDetector(context, new ScaleGestureListener());
    this.pinchScaleListener = pinchScaleListener;
    this.eventOccurred = 0;
    this.countOfScaleIn = 0;
    this.countOfScaleOut = 0;
  }

  /**
   * On touch event boolean.
   *
   * @param e
   *     the e
   * @return the boolean
   */
  boolean onTouchEvent(MotionEvent e) {
    return scaleGestureDetector.onTouchEvent(e);
  }

  private class ScaleGestureListener implements ScaleGestureDetector.OnScaleGestureListener {

    @Override
    public boolean onScale(ScaleGestureDetector scaleGestureDetector) {

      float scaleFactor = scaleGestureDetector.getScaleFactor();
      if (scaleFactor > 1) {
        countOfScaleIn += 1;
        if (eventOccurred != 1 && countOfScaleIn > 2) {
          eventOccurred = 1;
          pinchScaleListener.onScale(scaleGestureDetector, true);
        }
      }
      else {
        countOfScaleOut += 1;
        if (eventOccurred != 2 && countOfScaleOut > 2) {
          eventOccurred = 2;
          pinchScaleListener.onScale(scaleGestureDetector, false);
        }
      }
      return true;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
      pinchScaleListener.onScaleStart(scaleGestureDetector);
      countOfScaleOut = 0;
      countOfScaleIn = 0;
      eventOccurred = 0;
      return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {
      pinchScaleListener.onScaleEnd(scaleGestureDetector);
    }
  }

  /**
   * The interface Pinch scale listener.
   */
  public interface PinchScaleListener {
    /**
     * On scale.
     *
     * @param scaleGestureDetector
     *     the scale gesture detector
     * @param isScalingOut
     *     the is scaling out
     */
    void onScale(ScaleGestureDetector scaleGestureDetector, boolean isScalingOut);

    /**
     * On scale start.
     *
     * @param scaleGestureDetector
     *     the scale gesture detector
     */
    void onScaleStart(ScaleGestureDetector scaleGestureDetector);

    /**
     * On scale end.
     *
     * @param scaleGestureDetector
     *     the scale gesture detector
     */
    void onScaleEnd(ScaleGestureDetector scaleGestureDetector);
  }
}
