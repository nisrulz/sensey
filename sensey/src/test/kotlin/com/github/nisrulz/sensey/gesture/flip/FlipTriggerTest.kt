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
package com.github.nisrulz.sensey.gesture.flip

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class FlipTriggerTest {

    private val trigger = FlipTrigger()

    @Test
    fun faceUpWithMiddleValue() {
        assertEquals(FlipEvent.FaceUp, trigger.evaluate(floatArrayOf(0f, 0f, 9.5f), 0L))
    }

    @Test
    fun faceDownWithMiddleValue() {
        assertEquals(FlipEvent.FaceDown, trigger.evaluate(floatArrayOf(0f, 0f, -9.5f), 0L))
    }

    @Test
    fun notDetectFlipWithOtherValue() {
        assertNull(trigger.evaluate(floatArrayOf(0f, 0f, 0f), 0L))
    }

    @Test
    fun notDetectFlipWithMaxFaceUp() {
        assertNull(trigger.evaluate(floatArrayOf(0f, 0f, 11f), 0L))
    }

    @Test
    fun notDetectFlipWithMinFaceUp() {
        assertNull(trigger.evaluate(floatArrayOf(0f, 0f, 7f), 0L))
    }

    @Test
    fun notDetectFlipWithMaxFaceDown() {
        assertNull(trigger.evaluate(floatArrayOf(0f, 0f, -11f), 0L))
    }

    @Test
    fun notDetectFlipWithMinFaceDown() {
        assertNull(trigger.evaluate(floatArrayOf(0f, 0f, -7f), 0L))
    }

    @Test
    fun faceUpAtWideBoundary() {
        assertEquals(FlipEvent.FaceUp, trigger.evaluate(floatArrayOf(0f, 0f, 8.5f), 0L))
    }

    @Test
    fun faceDownAtWideBoundary() {
        assertEquals(FlipEvent.FaceDown, trigger.evaluate(floatArrayOf(0f, 0f, -8.5f), 0L))
    }

    @Test
    fun faceUpOnlyOnceUntilReset() {
        assertEquals(FlipEvent.FaceUp, trigger.evaluate(floatArrayOf(0f, 0f, 9.5f), 0L))
        assertNull(trigger.evaluate(floatArrayOf(0f, 0f, 9.5f), 100L))
    }

    @Test
    fun faceDownOnlyOnceUntilNewOrientation() {
        assertEquals(FlipEvent.FaceDown, trigger.evaluate(floatArrayOf(0f, 0f, -9.5f), 0L))
        assertNull(trigger.evaluate(floatArrayOf(0f, 0f, -9.5f), 100L))
    }

    @Test
    fun faceUpThenFaceDownWorks() {
        assertEquals(FlipEvent.FaceUp, trigger.evaluate(floatArrayOf(0f, 0f, 9.5f), 0L))
        assertEquals(FlipEvent.FaceDown, trigger.evaluate(floatArrayOf(0f, 0f, -9.5f), 200L))
    }
}
