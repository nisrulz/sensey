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
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

/**
 * The type Pinch scale detector.
 */
public class PinchScaleDetector {

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
            } else {
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
         * @param scaleGestureDetector the scale gesture detector
         * @param isScalingOut         the is scaling out
         */
        void onScale(ScaleGestureDetector scaleGestureDetector, boolean isScalingOut);

        /**
         * On scale end.
         *
         * @param scaleGestureDetector the scale gesture detector
         */
        void onScaleEnd(ScaleGestureDetector scaleGestureDetector);

        /**
         * On scale start.
         *
         * @param scaleGestureDetector the scale gesture detector
         */
        void onScaleStart(ScaleGestureDetector scaleGestureDetector);
    }

    private int countOfScaleIn;

    private int countOfScaleOut;

    private int eventOccurred;

    private final PinchScaleListener pinchScaleListener;

    private final ScaleGestureDetector scaleGestureDetector;

    /**
     * Instantiates a new Pinch scale detector.
     *
     * @param context            the context
     * @param pinchScaleListener the pinch scale listener
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
     * @param e the e
     * @return the boolean
     */
    boolean onTouchEvent(MotionEvent e) {
        return scaleGestureDetector.onTouchEvent(e);
    }
}
