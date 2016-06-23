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

  public TouchTypeDetector(Context context, TouchTypListener touchTypListener) {
    gDetect = new GestureDetectorCompat(context, listener);
    gDetect.setOnDoubleTapListener(listener);
    this.touchTypListener = touchTypListener;
  }

  void onTouchEvent(MotionEvent event) {
    gDetect.onTouchEvent(event);
  }

  class GestureListener extends GestureDetector.SimpleOnGestureListener {
    private float flingMin = 100;
    private float velocityMin = 100;

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
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

      //calculate the change in X position within the fling gesture
      float horizontalDiff = e2.getX() - e1.getX();
      //calculate the change in Y position within the fling gesture
      float verticalDiff = e2.getY() - e1.getY();

      float absHDiff = Math.abs(horizontalDiff);
      float absVDiff = Math.abs(verticalDiff);
      float absVelocityX = Math.abs(velocityX);
      float absVelocityY = Math.abs(velocityY);

      if (absHDiff > absVDiff && absHDiff > flingMin && absVelocityX > velocityMin) {
        //move forward or backward
        if (horizontalDiff > 0) {
          backward = true;
        } else if (absVDiff > flingMin && absVelocityY > velocityMin) {
          if (verticalDiff > 0) {
            backward = true;
            forward = false;
          } else {
            forward = true;
            backward = false;
          }
        }
      }

      //user is cycling forward through messages
      if (forward) {
        touchTypListener.onSwipeRight();
      }
      //user is cycling backwards through messages
      else if (backward) {
        touchTypListener.onSwipeLeft();
      }

      return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

      boolean isScrollingTowardsTop;

      if (e1.getY() > e2.getY()) {
        isScrollingTowardsTop = true;
      } else {
        isScrollingTowardsTop = false;
      }

      touchTypListener.onScroll(isScrollingTowardsTop);

      return super.onScroll(e1, e2, distanceX, distanceY);
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
