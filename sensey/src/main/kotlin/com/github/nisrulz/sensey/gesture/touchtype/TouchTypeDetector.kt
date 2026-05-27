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
package com.github.nisrulz.sensey.gesture.touchtype

import android.content.Context
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import androidx.core.view.GestureDetectorCompat

class TouchTypeDetector(
    context: Context,
    private val trigger: TouchTypeTrigger,
    private val dispatcher: (TouchTypeEvent) -> Unit,
) {
    private val gestureDetector = GestureDetectorCompat(
        context,
        object : SimpleOnGestureListener() {
            override fun onDoubleTap(e: MotionEvent): Boolean {
                e?.let { dispatcher(TouchTypeEvent.DoubleTap) }
                return super.onDoubleTap(e)
            }

            override fun onFling(
                e1: MotionEvent?,
                e2: MotionEvent,
                velocityX: Float,
                velocityY: Float,
            ): Boolean {
                val deltaX = e2.x - (e1?.x ?: 0f)
                val deltaY = e2.y - (e1?.y ?: 0f)
                val event = trigger.evaluate(
                    floatArrayOf(deltaX, deltaY, velocityX, velocityY),
                    System.currentTimeMillis(),
                )
                event?.let(dispatcher)
                return false
            }

            override fun onLongPress(e: MotionEvent) {
                e?.let { dispatcher(TouchTypeEvent.LongPress) }
            }

            override fun onScroll(
                e1: MotionEvent?,
                e2: MotionEvent,
                distanceX: Float,
                distanceY: Float,
            ): Boolean {
                val deltaX = e2.x - (e1?.x ?: 0f)
                val deltaY = e2.y - (e1?.y ?: 0f)
                val event = trigger.evaluate(
                    floatArrayOf(deltaX, deltaY, 0f, 0f),
                    System.currentTimeMillis(),
                )
                event?.let(dispatcher)
                return super.onScroll(e1, e2, distanceX, distanceY)
            }

            override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                e?.let { dispatcher(TouchTypeEvent.SingleTap) }
                return super.onSingleTapConfirmed(e)
            }
        },
    )

    fun onTouchEvent(event: MotionEvent): Boolean {
        if (event != null) {
            when (event.actionMasked) {
                MotionEvent.ACTION_POINTER_DOWN -> {
                    when (event.pointerCount) {
                        3 -> dispatcher(TouchTypeEvent.ThreeFingerSingleTap)
                        2 -> dispatcher(TouchTypeEvent.TwoFingerSingleTap)
                    }
                }
            }
            return gestureDetector.onTouchEvent(event)
        }
        return false
    }
}
