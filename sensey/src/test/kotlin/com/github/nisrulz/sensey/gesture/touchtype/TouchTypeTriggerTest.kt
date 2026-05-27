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

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class TouchTypeTriggerTest {

    private val trigger = TouchTypeTrigger(
        swipeMinDistance = 120f,
        swipeThresholdVelocity = 200f,
    )

    @Test
    fun swipeRight() {
        assertEquals(
            TouchTypeEvent.Swipe(TouchTypeTrigger.SWIPE_DIR_RIGHT),
            trigger.evaluate(floatArrayOf(200f, 0f, 300f, 0f), 0L),
        )
    }

    @Test
    fun swipeLeft() {
        assertEquals(
            TouchTypeEvent.Swipe(TouchTypeTrigger.SWIPE_DIR_LEFT),
            trigger.evaluate(floatArrayOf(-200f, 0f, -300f, 0f), 0L),
        )
    }

    @Test
    fun swipeDown() {
        assertEquals(
            TouchTypeEvent.Swipe(TouchTypeTrigger.SWIPE_DIR_DOWN),
            trigger.evaluate(floatArrayOf(0f, 200f, 0f, 300f), 0L),
        )
    }

    @Test
    fun swipeUp() {
        assertEquals(
            TouchTypeEvent.Swipe(TouchTypeTrigger.SWIPE_DIR_UP),
            trigger.evaluate(floatArrayOf(0f, -200f, 0f, -300f), 0L),
        )
    }

    @Test
    fun scrollRight() {
        assertEquals(
            TouchTypeEvent.Scroll(TouchTypeTrigger.SCROLL_DIR_RIGHT),
            trigger.evaluate(floatArrayOf(200f, 0f, 0f, 0f), 0L),
        )
    }

    @Test
    fun noEventBelowMinDistance() {
        assertNull(trigger.evaluate(floatArrayOf(10f, 0f, 0f, 0f), 0L))
    }

    @Test
    fun nullForEmptyValues() {
        assertNull(trigger.evaluate(floatArrayOf(), 0L))
    }
}
