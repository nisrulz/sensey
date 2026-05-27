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

internal class TouchTypeTrigger(
    private val swipeMinDistance: Float = 120f,
    private val swipeThresholdVelocity: Float = 200f,
) : GestureTrigger<TouchTypeEvent> {
    override fun evaluate(
        values: FloatArray,
        timestamp: Long,
    ): TouchTypeEvent? {
        if (values.size < 2) return null

        val deltaX = values[0]
        val deltaY = values[1]
        val distance = sqrt(deltaX * deltaX + deltaY * deltaY)
        if (distance < swipeMinDistance) return null

        val velocityX = values.getOrNull(2) ?: 0f
        val velocityY = values.getOrNull(3) ?: 0f
        val isSwipe = isAboveVelocityThreshold(velocityX, velocityY)

        val direction = classifyDirection(deltaX, deltaY, isSwipe)
        return if (isSwipe) TouchTypeEvent.Swipe(direction) else TouchTypeEvent.Scroll(direction)
    }

    private fun isAboveVelocityThreshold(
        vx: Float,
        vy: Float,
    ): Boolean = abs(vx) > swipeThresholdVelocity || abs(vy) > swipeThresholdVelocity

    private fun classifyDirection(
        deltaX: Float,
        deltaY: Float,
        isSwipe: Boolean,
    ): TouchTypeEvent.Direction {
        val degrees = Math.toDegrees(atan2(deltaY.toDouble(), deltaX.toDouble()))
        val quadrant = classifyQuadrant(degrees)
        return if (isSwipe) quadrant.swipeDirection else quadrant.scrollDirection
    }

    private fun classifyQuadrant(degrees: Double): Quadrant =
        when {
            degrees in -22.5..22.5 -> Quadrant.RIGHT
            degrees in 22.5..67.5 -> Quadrant.DOWN_RIGHT
            degrees in 67.5..112.5 -> Quadrant.DOWN
            degrees in 112.5..157.5 -> Quadrant.DOWN_LEFT
            degrees > 157.5 || degrees < -157.5 -> Quadrant.LEFT
            degrees in -157.5..-112.5 -> Quadrant.UP_LEFT
            degrees in -112.5..-67.5 -> Quadrant.UP
            degrees in -67.5..-22.5 -> Quadrant.UP_RIGHT
            else -> Quadrant.DOWN
        }

    private enum class Quadrant(
        val scrollDirection: TouchTypeEvent.Direction,
        val swipeDirection: TouchTypeEvent.Direction,
    ) {
        RIGHT(TouchTypeEvent.Direction.RIGHT, TouchTypeEvent.Direction.RIGHT),
        DOWN_RIGHT(TouchTypeEvent.Direction.DOWN, TouchTypeEvent.Direction.DOWN_RIGHT),
        DOWN(TouchTypeEvent.Direction.DOWN, TouchTypeEvent.Direction.DOWN),
        DOWN_LEFT(TouchTypeEvent.Direction.DOWN, TouchTypeEvent.Direction.DOWN_LEFT),
        LEFT(TouchTypeEvent.Direction.LEFT, TouchTypeEvent.Direction.LEFT),
        UP_LEFT(TouchTypeEvent.Direction.UP, TouchTypeEvent.Direction.UP_LEFT),
        UP(TouchTypeEvent.Direction.UP, TouchTypeEvent.Direction.UP),
        UP_RIGHT(TouchTypeEvent.Direction.UP, TouchTypeEvent.Direction.UP_RIGHT),
    }
}
