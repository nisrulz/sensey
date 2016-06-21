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
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class TouchTypeDetector {

  //gesture detector
  private GestureDetectorCompat gDetect;

  private TouchTypListener touchTypListener;

  public TouchTypeDetector(Context context, TouchTypListener touchTypListener) {
    gDetect = new GestureDetectorCompat(context, new GestureListener());
    this.touchTypListener = touchTypListener;
  }

  void onTouchEvent(MotionEvent event) {
    gDetect.onTouchEvent(event);
  }

  private class GestureListener extends GestureDetector.SimpleOnGestureListener {
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    //user will move forward through messages on fling up or left
    boolean forward = false;
    //user will move backward through messages on fling down or right
    boolean backward = false;

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

      if (Math.abs(startevent.getY() - finishevent.getY()) > SWIPE_MAX_OFF_PATH) return false;
      // right to left swipe
      if (startevent.getX() - finishevent.getX() > SWIPE_MIN_DISTANCE
          && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
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

      boolean isScrollingTowardsTop;
      float deltaX = startevent.getX() - finishevent.getX();
      float deltaY = startevent.getY() - finishevent.getY();

      Log.i("Touch", "Delta X =" + deltaX + " | Delta Y : " + deltaY);

      if (Math.abs(deltaX) < 0) {
        Log.i("Touch1", "Delta X =" + deltaX + " | Scrolling Left");
      } else if (Math.abs(deltaX) > 0) {
        Log.i("Touch1", "Delta X =" + deltaX + " | Scrolling Right");
      }

      if (Math.abs(deltaY) < 0) {
        Log.i("Touch1", "Delta Y =" + deltaY + " | Scrolling Top");
      } else if (Math.abs(deltaY) > 0) {
        Log.i("Touch1", "Delta Y =" + deltaY + " | Scrolling Bottom");
      }

      if (deltaY > 120 && deltaX < 5) {
        isScrollingTowardsTop = true;
      } else {
        isScrollingTowardsTop = false;
      }

      touchTypListener.onScroll(isScrollingTowardsTop);

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

    void onScroll(boolean scrollingTowardsTop);

    void onSingleTap();

    void onSwipeLeft();

    void onSwipeRight();

    void onLongPress();
  }
}
