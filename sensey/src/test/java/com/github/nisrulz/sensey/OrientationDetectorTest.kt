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

import android.hardware.Sensor
import android.hardware.SensorEvent
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.robolectric.RobolectricTestRunner

// TODO add tests for smoothness > 1
@RunWith(RobolectricTestRunner::class)
class OrientationDetectorTest {
    private var mockListener: OrientationDetector.OrientationListener? = null

    private var testOrientationDetector: OrientationDetector? = null

    @Test
    fun detectBottomSideUp() {
        testOrientationDetector?.onSensorChanged(
            SensorUtils.testAccelerometerEvent(floatArrayOf(-9.81f, -9.81f, -9.81f)),
        )
        testOrientationDetector?.onSensorChanged(testMagneticEvent(floatArrayOf(0f, 0f, 1f)))
        Mockito.verify(mockListener, Mockito.only())?.onBottomSideUp()
    }

    @Test
    fun detectLeftSideUp() {
        testOrientationDetector?.onSensorChanged(
            SensorUtils.testAccelerometerEvent(floatArrayOf(-9.81f, 0f, -9.81f)),
        )
        testOrientationDetector?.onSensorChanged(testMagneticEvent(floatArrayOf(0f, 0f, 1f)))
        Mockito.verify(mockListener, Mockito.only())?.onLeftSideUp()
    }

    @Test
    fun detectNothingForOnlyAccelerometerEvent() {
        testOrientationDetector?.onSensorChanged(
            SensorUtils.testAccelerometerEvent(
                floatArrayOf(
                    1f,
                    2f,
                    3f,
                ),
            ),
        )
        Mockito.verifyNoMoreInteractions(mockListener)
    }

    @Test
    fun detectNothingForOnlyMagneticEvent() {
        testOrientationDetector?.onSensorChanged(testMagneticEvent(floatArrayOf(1f, 2f, 3f)))
        Mockito.verifyNoMoreInteractions(mockListener)
    }

    @Test
    fun detectRightSideUp() {
        testOrientationDetector?.onSensorChanged(
            SensorUtils.testAccelerometerEvent(floatArrayOf(9.81f, 0f, 9.81f)),
        )
        testOrientationDetector?.onSensorChanged(testMagneticEvent(floatArrayOf(0f, 0f, 1f)))
        Mockito.verify(mockListener, Mockito.only())?.onRightSideUp()
    }

    @Test
    fun detectTopSideUp() {
        testOrientationDetector?.onSensorChanged(
            SensorUtils.testAccelerometerEvent(floatArrayOf(9.81f, 9.81f, 9.81f)),
        )
        testOrientationDetector?.onSensorChanged(testMagneticEvent(floatArrayOf(0f, 0f, 1f)))
        Mockito.verify(mockListener, Mockito.only())?.onTopSideUp()
    }

    @Before
    fun setUp() {
        mockListener = Mockito.mock(OrientationDetector.OrientationListener::class.java)
        testOrientationDetector = OrientationDetector(mockListener)
    }

    private fun testMagneticEvent(values: FloatArray): SensorEvent = SensorUtils.testSensorEvent(values, Sensor.TYPE_MAGNETIC_FIELD)
}
