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

import com.github.nisrulz.sensey.contract.GestureTrigger

class TouchTypeTrigger(
    private val swipeMinDistance: Float = 120f,
    private val swipeThresholdVelocity: Float = 200f,
) : GestureTrigger<TouchTypeEvent> {

    override fun evaluate(values: FloatArray, timestamp: Long): TouchTypeEvent? {
        return when {
            values.size < 2 -> null
            else -> {
                val deltaX = values[0]
                val deltaY = values[1]
                val velocityX = values.getOrNull(2) ?: 0f
                val velocityY = values.getOrNull(3) ?: 0f

                if (kotlin.math.abs(deltaX) > kotlin.math.abs(deltaY)) {
                    if (kotlin.math.abs(deltaX) > swipeMinDistance
                        && kotlin.math.abs(velocityX) > swipeThresholdVelocity
                    ) {
                        if (deltaX > 0) TouchTypeEvent.Swipe(SWIPE_DIR_RIGHT)
                        else TouchTypeEvent.Swipe(SWIPE_DIR_LEFT)
                    } else if (kotlin.math.abs(deltaX) > swipeMinDistance) {
                        if (deltaX > 0) TouchTypeEvent.Scroll(SCROLL_DIR_RIGHT)
                        else TouchTypeEvent.Scroll(SCROLL_DIR_LEFT)
                    } else null
                } else {
                    if (kotlin.math.abs(deltaY) > swipeMinDistance
                        && kotlin.math.abs(velocityY) > swipeThresholdVelocity
                    ) {
                        if (deltaY > 0) TouchTypeEvent.Swipe(SWIPE_DIR_DOWN)
                        else TouchTypeEvent.Swipe(SWIPE_DIR_UP)
                    } else if (kotlin.math.abs(deltaY) > swipeMinDistance) {
                        if (deltaY > 0) TouchTypeEvent.Scroll(SCROLL_DIR_DOWN)
                        else TouchTypeEvent.Scroll(SCROLL_DIR_UP)
                    } else null
                }
            }
        }
    }

    companion object {
        const val SCROLL_DIR_UP = 1
        const val SCROLL_DIR_RIGHT = 2
        const val SCROLL_DIR_DOWN = 3
        const val SCROLL_DIR_LEFT = 4
        const val SWIPE_DIR_UP = 5
        const val SWIPE_DIR_RIGHT = 6
        const val SWIPE_DIR_DOWN = 7
        const val SWIPE_DIR_LEFT = 8
    }
}
