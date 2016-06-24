/*
 * Copyright (C) 2016 Nishant Srivastava
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.nisrulz.sensey;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class TouchTypeDetector {

  //gesture detector
  private GestureDetectorCompat gDetect;

  private TouchTypListener touchTypListener;
  final GestureListener listener = new GestureListener();

  public static final int SCROLL_DIR_UP = 1;
  public static final int SCROLL_DIR_RIGHT = 2;
  public static final int SCROLL_DIR_DOWN = 3;
  public static final int SCROLL_DIR_LEFT = 4;

  public TouchTypeDetector(Context context, TouchTypListener touchTypListener) {
    gDetect = new GestureDetectorCompat(context, listener);
    gDetect.setOnDoubleTapListener(listener);
    this.touchTypListener = touchTypListener;
  }

  void onTouchEvent(MotionEvent event) {
    gDetect.onTouchEvent(event);
  }

  class GestureListener extends GestureDetector.SimpleOnGestureListener {
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    @Override public boolean onDoubleTap(MotionEvent e) {
      touchTypListener.onDoubleTap();
      return super.onDoubleTap(e);
    }

    @Override public boolean onDown(MotionEvent e) {
      return super.onDown(e);
    }

    @Override public void onLongPress(MotionEvent e) {
      touchTypListener.onLongPress();
      super.onLongPress(e);
    }

    @Override
    public boolean onFling(MotionEvent startevent, MotionEvent finishevent, float velocityX,
        float velocityY) {

      final float deltaX = startevent.getX() - finishevent.getX();
      final float deltaY = startevent.getY() - finishevent.getY();

      if (Math.abs(deltaY) > SWIPE_MAX_OFF_PATH) return false;
      // right to left swipe
      if (deltaX > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
        touchTypListener.onSwipeLeft();
      } else if (finishevent.getX() - startevent.getX() > SWIPE_MIN_DISTANCE
          && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
        touchTypListener.onSwipeRight();
      }
      return false;
    }

    @Override
    public boolean onScroll(MotionEvent startevent, MotionEvent finishevent, float distanceX,
        float distanceY) {

      float deltaX = finishevent.getX() - startevent.getX();
      float deltaY = finishevent.getY() - startevent.getY();

      if (Math.abs(deltaX) > Math.abs(deltaY)) {
        //Scrolling Horizontal
        if (Math.abs(deltaX) > SWIPE_MIN_DISTANCE) {
          if (deltaX > 0) {
            touchTypListener.onScroll(SCROLL_DIR_LEFT);
          } else {
            touchTypListener.onScroll(SCROLL_DIR_RIGHT);
          }
        }
      } else {
        //Scrolling Vertical
        if (Math.abs(deltaY) > SWIPE_MIN_DISTANCE) {
          if (deltaY > 0) {
            touchTypListener.onScroll(SCROLL_DIR_DOWN);
          } else {
            touchTypListener.onScroll(SCROLL_DIR_UP);
          }
        }
      }

      return super.onScroll(startevent, finishevent, distanceX, distanceY);
    }

    @Override public boolean onSingleTapConfirmed(MotionEvent e) {
      touchTypListener.onSingleTap();
      return super.onSingleTapConfirmed(e);
    }

    @Override public boolean onSingleTapUp(MotionEvent e) {
      return super.onSingleTapUp(e);
    }
  }

  public interface TouchTypListener {
    void onDoubleTap();

    void onScroll(int scroll_dir);

    void onSingleTap();

    void onSwipeLeft();

    void onSwipeRight();

    void onLongPress();
  }
}
