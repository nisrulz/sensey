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
package com.github.nisrulz.sensey

import com.github.nisrulz.sensey.FlipDetector.FlipListener
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class FlipDetectorTest {
    @Mock
    private val mockListener: FlipListener? = null

    private var testFlipDetector: FlipDetector? = null

    @Test
    fun detectFlipWithMiddleFaceDownValue() {
        testFlipDetector?.onSensorChanged(
            SensorUtils.testAccelerometerEvent(
                floatArrayOf(
                    0f,
                    0f,
                    -9.5f,
                ),
            ),
        )
        Mockito.verify(this.mockListener, Mockito.only())?.onFaceDown()
    }

    @Test
    fun detectFlipWithMiddleFaceUpValue() {
        testFlipDetector?.onSensorChanged(
            SensorUtils.testAccelerometerEvent(
                floatArrayOf(
                    0f,
                    0f,
                    9.5f,
                ),
            ),
        )
        Mockito.verify(this.mockListener, Mockito.only())?.onFaceUp()
    }

    @Test(expected = ArrayIndexOutOfBoundsException::class)
    fun exceptionWithArrayLessThenThreeElements() {
        testFlipDetector?.onSensorChanged(
            SensorUtils.testAccelerometerEvent(
                floatArrayOf(
                    0f,
                    0f,
                ),
            ),
        )
        Mockito.verifyNoMoreInteractions(this.mockListener)
    }

    @Test
    fun notDetectFlipWithMaxFaceDownValue() {
        testFlipDetector?.onSensorChanged(
            SensorUtils.testAccelerometerEvent(
                floatArrayOf(
                    0f,
                    0f,
                    -9f,
                ),
            ),
        )
        Mockito.verifyNoMoreInteractions(this.mockListener)
    }

    @Test
    fun notDetectFlipWithMaxFaceUpValue() {
        testFlipDetector?.onSensorChanged(
            SensorUtils.testAccelerometerEvent(
                floatArrayOf(
                    0f,
                    0f,
                    10f,
                ),
            ),
        )
        Mockito.verifyNoMoreInteractions(this.mockListener)
    }

    @Test
    fun notDetectFlipWithMinFaceDownValue() {
        testFlipDetector?.onSensorChanged(
            SensorUtils.testAccelerometerEvent(
                floatArrayOf(
                    0f,
                    0f,
                    -10f,
                ),
            ),
        )
        Mockito.verifyNoMoreInteractions(this.mockListener)
    }

    @Test
    fun notDetectFlipWithMinFaceUpValue() {
        testFlipDetector?.onSensorChanged(
            SensorUtils.testAccelerometerEvent(
                floatArrayOf(
                    0f,
                    0f,
                    9f,
                ),
            ),
        )
        Mockito.verifyNoMoreInteractions(this.mockListener)
    }

    @Test
    fun notDetectFlipWithOtherValue() {
        testFlipDetector?.onSensorChanged(
            SensorUtils.testAccelerometerEvent(
                floatArrayOf(0f, 0f, 0f),
            ),
        )
        Mockito.verifyNoMoreInteractions(this.mockListener)
    }

    @Before
    fun setUp() {
        this.testFlipDetector = FlipDetector(this.mockListener)
    }
}
