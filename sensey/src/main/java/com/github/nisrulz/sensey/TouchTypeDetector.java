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

/**
 * The type Touch type detector.
 */
public class TouchTypeDetector {

    /**
     * The type Gesture listener.
     */
    class GestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_MIN_DISTANCE = 120;

        private static final int SWIPE_THRESHOLD_VELOCITY = 200;

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            touchTypListener.onDoubleTap();
            return super.onDoubleTap(e);
        }

        @Override
        public boolean onFling(MotionEvent startevent, MotionEvent finishevent, float velocityX,
                float velocityY) {

            final float deltaX = finishevent.getX() - startevent.getX();
            final float deltaY = finishevent.getY() - startevent.getY();

            if (Math.abs(deltaX) > Math.abs(deltaY)) {
                if (Math.abs(deltaX) > SWIPE_MIN_DISTANCE
                        && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    if (deltaX > 0) {
                        touchTypListener.onSwipe(SWIPE_DIR_RIGHT);
                    } else {
                        touchTypListener.onSwipe(SWIPE_DIR_LEFT);
                    }
                }
            } else {
                if (Math.abs(deltaY) > SWIPE_MIN_DISTANCE
                        && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                    if (deltaY > 0) {
                        touchTypListener.onSwipe(SWIPE_DIR_DOWN);
                    } else {
                        touchTypListener.onSwipe(SWIPE_DIR_UP);
                    }
                }
            }

            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            touchTypListener.onLongPress();
            super.onLongPress(e);
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
                        touchTypListener.onScroll(SCROLL_DIR_RIGHT);
                    } else {
                        touchTypListener.onScroll(SCROLL_DIR_LEFT);
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

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            touchTypListener.onSingleTap();
            return super.onSingleTapConfirmed(e);
        }
    }

    /**
     * The interface Touch typ listener.
     */
    public interface TouchTypListener {

        /**
         * On double tap.
         */
        void onDoubleTap();

        /**
         * On long press.
         */
        void onLongPress();

        /**
         * On scroll.
         *
         * @param scrollDirection the scroll direction
         */
        void onScroll(int scrollDirection);

        /**
         * On single tap.
         */
        void onSingleTap();

        /**
         * On swipe.
         *
         * @param swipeDirection the swipe direction
         */
        void onSwipe(int swipeDirection);

        /**
         * On three finger single tap.
         */
        void onThreeFingerSingleTap();

        /**
         * On two finger single tap.
         */
        void onTwoFingerSingleTap();
    }

    /**
     * The constant SCROLL_DIR_UP.
     */
    public static final int SCROLL_DIR_UP = 1;

    /**
     * The constant SCROLL_DIR_RIGHT.
     */
    public static final int SCROLL_DIR_RIGHT = 2;

    /**
     * The constant SCROLL_DIR_DOWN.
     */
    public static final int SCROLL_DIR_DOWN = 3;

    /**
     * The constant SCROLL_DIR_LEFT.
     */
    public static final int SCROLL_DIR_LEFT = 4;

    /**
     * The constant SWIPE_DIR_UP.
     */
    public static final int SWIPE_DIR_UP = 5;

    /**
     * The constant SWIPE_DIR_RIGHT.
     */
    public static final int SWIPE_DIR_RIGHT = 6;

    /**
     * The constant SWIPE_DIR_DOWN.
     */
    public static final int SWIPE_DIR_DOWN = 7;

    /**
     * The constant SWIPE_DIR_LEFT.
     */
    public static final int SWIPE_DIR_LEFT = 8;

    /**
     * The Gesture listener.
     */
    final GestureListener gestureListener; // it's needed for TouchTypeDetectorTest, don't remove

    //gesture detector
    private final GestureDetectorCompat gDetect;

    private final TouchTypListener touchTypListener;

    /**
     * Instantiates a new Touch type detector.
     *
     * @param context          the context
     * @param touchTypListener the touch typ listener
     */
    public TouchTypeDetector(Context context, TouchTypListener touchTypListener) {
        gestureListener = new GestureListener();
        gDetect = new GestureDetectorCompat(context, gestureListener);
        this.touchTypListener = touchTypListener;
    }

    /**
     * On touch event boolean.
     *
     * @param event the event
     * @return the boolean
     */
    boolean onTouchEvent(MotionEvent event) {

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_POINTER_DOWN:
                if (event.getPointerCount() == 3) {
                    touchTypListener.onThreeFingerSingleTap();
                } else if (event.getPointerCount() == 2) {
                    touchTypListener.onTwoFingerSingleTap();
                }
        }
        return gDetect.onTouchEvent(event);
    }
}
