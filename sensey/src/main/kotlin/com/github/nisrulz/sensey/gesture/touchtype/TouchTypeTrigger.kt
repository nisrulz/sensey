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
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.sqrt

class TouchTypeTrigger(
    private val swipeMinDistance: Float = 120f,
    private val swipeThresholdVelocity: Float = 200f,
) : GestureTrigger<TouchTypeEvent> {

    override fun evaluate(values: FloatArray, timestamp: Long): TouchTypeEvent? {
        if (values.size < 2) return null

        val deltaX = values[0]
        val deltaY = values[1]
        val velocityX = values.getOrNull(2) ?: 0f
        val velocityY = values.getOrNull(3) ?: 0f
        val distance = sqrt(deltaX * deltaX + deltaY * deltaY)

        if (distance < swipeMinDistance) return null

        val isSwipe = abs(velocityX) > swipeThresholdVelocity ||
            abs(velocityY) > swipeThresholdVelocity
        val maxVelocity = maxOf(abs(velocityX), abs(velocityY))

        val angle = atan2(deltaY.toDouble(), deltaX.toDouble())
        val degrees = Math.toDegrees(angle)

        val direction = when {
            degrees in -22.5..22.5 -> if (isSwipe) SWIPE_DIR_RIGHT else SCROLL_DIR_RIGHT
            degrees in 22.5..67.5 -> if (isSwipe) SWIPE_DIR_DOWN_RIGHT else SCROLL_DIR_DOWN
            degrees in 67.5..112.5 -> if (isSwipe) SWIPE_DIR_DOWN else SCROLL_DIR_DOWN
            degrees in 112.5..157.5 -> if (isSwipe) SWIPE_DIR_DOWN_LEFT else SCROLL_DIR_DOWN
            degrees > 157.5 || degrees < -157.5 -> if (isSwipe) SWIPE_DIR_LEFT else SCROLL_DIR_LEFT
            degrees in -157.5..-112.5 -> if (isSwipe) SWIPE_DIR_UP_LEFT else SCROLL_DIR_UP
            degrees in -112.5..-67.5 -> if (isSwipe) SWIPE_DIR_UP else SCROLL_DIR_UP
            degrees in -67.5..-22.5 -> if (isSwipe) SWIPE_DIR_UP_RIGHT else SCROLL_DIR_UP
            else -> return null
        }

        return if (isSwipe) TouchTypeEvent.Swipe(direction) else TouchTypeEvent.Scroll(direction)
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
        const val SWIPE_DIR_UP_RIGHT = 9
        const val SWIPE_DIR_UP_LEFT = 10
        const val SWIPE_DIR_DOWN_RIGHT = 11
        const val SWIPE_DIR_DOWN_LEFT = 12
    }
}
