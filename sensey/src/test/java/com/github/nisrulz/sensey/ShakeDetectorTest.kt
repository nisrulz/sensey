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

import com.github.nisrulz.sensey.ShakeDetector.ShakeListener
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ShakeDetectorTest {
    @Mock
    private val mockListener: ShakeListener? = null

    @Test
    fun detectNothingWithZeroGravity() {
        this.testDetector().onSensorChanged(
            SensorUtils.testAccelerometerEvent(
                floatArrayOf(0f, 0f, 0f),
            ),
        )
        Mockito.verifyNoMoreInteractions(this.mockListener)
    }

    @Test
    fun detectNothingWithZeroGravityForCustomThreshold() {
        this.testDetector(10f, 1000).onSensorChanged(
            SensorUtils.testAccelerometerEvent(
                floatArrayOf(0f, 0f, 0f),
            ),
        )
        Mockito.verifyNoMoreInteractions(this.mockListener)
    }

    @Test
    fun detectShakeWithDoubleGravity() {
        this.testDetector().onSensorChanged(
            SensorUtils.testAccelerometerEvent(
                floatArrayOf(0f, 0f, 2 * 9.81f),
            ),
        )
        Mockito.verify(this.mockListener, Mockito.only())?.onShakeDetected()
    }

    @Test
    fun detectShakeWithDoubleGravityForCustomThreshold() {
        this.testDetector(9f, 1000).onSensorChanged(
            SensorUtils.testAccelerometerEvent(
                floatArrayOf(0f, 0f, 2 * 9.81f),
            ),
        )
        Mockito.verify(this.mockListener, Mockito.only())?.onShakeDetected()
    }

    @Test
    fun detectShakeWithSeveralGravitySensors() {
        val testDetector = this.testDetector()
        testDetector.onSensorChanged(
            SensorUtils.testAccelerometerEvent(
                floatArrayOf(
                    0f,
                    0f,
                    2 * 9.81f,
                ),
            ),
        )
        testDetector.onSensorChanged(
            SensorUtils.testAccelerometerEvent(
                floatArrayOf(
                    0f,
                    0f,
                    2 * -9.81f,
                ),
            ),
        )
        Mockito.verify(this.mockListener, Mockito.times(2))?.onShakeDetected()
    }

    @Test
    fun detectShakeWithSeveralGravitySensorsForCustomThreshold() {
        val testDetector = this.testDetector(9f, 1000)
        testDetector.onSensorChanged(
            SensorUtils.testAccelerometerEvent(
                floatArrayOf(
                    0f,
                    0f,
                    2 * 9.81f,
                ),
            ),
        )
        testDetector.onSensorChanged(
            SensorUtils.testAccelerometerEvent(
                floatArrayOf(
                    0f,
                    0f,
                    2 * -9.81f,
                ),
            ),
        )
        Mockito.verify(this.mockListener, Mockito.times(1))?.onShakeDetected()
    }

    @Test
    fun detectShakeWithSeveralStrongGravitySensorsForCustomThreshold() {
        val testDetector = this.testDetector(9f, 1000)
        testDetector.onSensorChanged(
            SensorUtils.testAccelerometerEvent(
                floatArrayOf(
                    0f,
                    0f,
                    2 * 9.81f,
                ),
            ),
        )
        testDetector.onSensorChanged(
            SensorUtils.testAccelerometerEvent(
                floatArrayOf(
                    0f,
                    0f,
                    3 * -9.81f,
                ),
            ),
        )
        Mockito.verify(this.mockListener, Mockito.times(2))?.onShakeDetected()
    }

    @Test(expected = ArrayIndexOutOfBoundsException::class)
    fun exceptionWithLessThanThreeElements() {
        this.testDetector().onSensorChanged(
            SensorUtils.testAccelerometerEvent(
                floatArrayOf(2f, 3f),
            ),
        )
    }

    private fun testDetector(): ShakeDetector = ShakeDetector(this.mockListener)

    private fun testDetector(
        threshold: Float,
        timeBeforeDeclaringShakeStopped: Long,
    ): ShakeDetector = ShakeDetector(threshold, timeBeforeDeclaringShakeStopped, this.mockListener)
}
