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
package com.github.nisrulz.sensey.gesture.pinchscale

import android.content.Context
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.OnScaleGestureListener

class PinchScaleDetector(
    context: Context,
    private val trigger: PinchScaleTrigger,
    private val dispatcher: (PinchScaleEvent) -> Unit,
    private val onScaleStart: (() -> Unit)? = null,
    private val onScaleEnd: (() -> Unit)? = null,
) {
    private val scaleGestureDetector = ScaleGestureDetector(
        context,
        object : OnScaleGestureListener {
            override fun onScale(detector: ScaleGestureDetector): Boolean {
                val event = trigger.evaluate(
                    floatArrayOf(detector.scaleFactor),
                    System.currentTimeMillis(),
                )
                event?.let(dispatcher)
                return true
            }

            override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
                trigger.reset()
                onScaleStart?.invoke()
                return true
            }

            override fun onScaleEnd(detector: ScaleGestureDetector) {
                onScaleEnd?.invoke()
            }
        },
    )

    fun onTouchEvent(event: MotionEvent): Boolean {
        return scaleGestureDetector.onTouchEvent(event)
    }
}
