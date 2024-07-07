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

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import androidx.test.core.app.ApplicationProvider
import com.github.nisrulz.sensey.FlipDetector.FlipListener
import com.github.nisrulz.sensey.LightDetector.LightListener
import com.github.nisrulz.sensey.ProximityDetector.ProximityListener
import com.github.nisrulz.sensey.ShakeDetector.ShakeListener
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.shadows.ShadowSensorManager

@RunWith(RobolectricTestRunner::class)
class SenseyTest {
    private companion object {
        var sensey: Sensey? = null
        var shadowSensorManager: ShadowSensorManager? = null

        fun SensorDetector.hasDetector() = shadowSensorManager?.hasListener(this) ?: false
    }

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        shadowSensorManager =
            Shadows.shadowOf(context.getSystemService(Context.SENSOR_SERVICE) as SensorManager)

        sensey = Sensey.getInstance()
        sensey?.init(context)
    }

    @After
    fun tearDown() {
        sensey = null
        shadowSensorManager = null
    }

    @Test
    fun detectListenerWithStartFlipDetection() {
        addSensor(Sensor.TYPE_ACCELEROMETER)
        val fakeListener = Mockito.mock(FlipListener::class.java)
        sensey?.startFlipDetection(fakeListener)
        val detector = getDetector(fakeListener, FlipDetector::class.java)
        if (detector != null) {
            assertTrue(
                "Sensor Manager must contain sensor event listener for flip",
                detector.hasDetector(),
            )
        } else {
            fail(
                "There should be flip detector in sensey. If not, please, check last version of class and update reflection accessing to it field",
            )
        }
    }

    @Test
    fun detectListenerWithStartLightDetection() {
        addSensor(Sensor.TYPE_LIGHT)
        val fakeListener = Mockito.mock(LightListener::class.java)
        sensey?.startLightDetection(fakeListener)
        val detector = getDetector(fakeListener, LightDetector::class.java)
        if (detector != null) {
            assertTrue(
                "Sensor Manager must contain sensor event listener for light",
                detector.hasDetector(),
            )
        } else {
            fail(
                "There should be light detector in sensey. If not, please, check last version of class and update reflection accessing to it field",
            )
        }
    }

    @Test
    fun detectListenerWithStartLightDetectionWithCustomThreshold() {
        addSensor(Sensor.TYPE_LIGHT)
        val fakeListener = Mockito.mock(LightListener::class.java)
        sensey?.startLightDetection(4f, fakeListener)
        val detector = getDetector(fakeListener, LightDetector::class.java)
        if (detector != null) {
            assertTrue(
                "Sensor Manager must contain sensor event listener for light",
                detector.hasDetector(),
            )
        } else {
            fail(
                "There should be light detector in sensey. If not, please, check last version of class and update reflection accessing to it field",
            )
        }
    }

    @Test
    fun detectListenerWithStartOrientationDetection() {
        addSensor(Sensor.TYPE_ACCELEROMETER)
        addSensor(Sensor.TYPE_MAGNETIC_FIELD)
        val fakeListener =
            Mockito.mock(
                OrientationDetector.OrientationListener::class.java,
            )
        sensey?.startOrientationDetection(fakeListener)
        val detector = getDetector(fakeListener, OrientationDetector::class.java)
        if (detector != null) {
            assertTrue(
                "Sensor Manager must contain sensor event listener for orientation",
                detector.hasDetector(),
            )
        } else {
            fail(
                "There should be orientation detector in sensey. If not, please, check last version of class and update reflection accessing to it field",
            )
        }
    }

    @Test
    fun detectListenerWithStartOrientationDetectionWithCustomSmoothness() {
        addSensor(Sensor.TYPE_ACCELEROMETER)
        addSensor(Sensor.TYPE_MAGNETIC_FIELD)
        val fakeListener =
            Mockito.mock(
                OrientationDetector.OrientationListener::class.java,
            )
        sensey?.startOrientationDetection(3, fakeListener)
        val detector = getDetector(fakeListener, OrientationDetector::class.java)
        if (detector != null) {
            assertTrue(
                "Sensor Manager must contain sensor event listener for orientation",
                detector.hasDetector(),
            )
        } else {
            fail(
                "There should be orientation detector in sensey. If not, please, check last version of class and update reflection accessing to it field",
            )
        }
    }

    @Test
    fun detectListenerWithStartProximityDetection() {
        addSensor(Sensor.TYPE_PROXIMITY)
        val fakeListener = Mockito.mock(ProximityListener::class.java)
        sensey?.startProximityDetection(fakeListener)
        val detector = getDetector(fakeListener, ProximityDetector::class.java)
        if (detector != null) {
            assertTrue(
                "Sensor Manager must contain sensor event listener for proximity",
                detector.hasDetector(),
            )
        } else {
            fail(
                "There should be proximity detector in sensey. If not, please, check last version of class and update reflection accessing to it field",
            )
        }
    }

    @Test
    fun detectListenerWithStartShakeDetection() {
        addSensor(Sensor.TYPE_ACCELEROMETER)
        val fakeListener = Mockito.mock(ShakeListener::class.java)
        sensey?.startShakeDetection(fakeListener)
        val detector = getDetector(fakeListener, ShakeDetector::class.java)
        if (detector != null) {
            assertTrue(
                "Sensor Manager must contain sensor event listener for shake",
                detector.hasDetector(),
            )
        } else {
            fail(
                "There should be shake detector in sensey. If not, please, check last version of class and update reflection accessing to it field",
            )
        }
    }

    @Test
    fun detectListenerWithStartShakeDetectionWithCustomThreshold() {
        addSensor(Sensor.TYPE_ACCELEROMETER)
        val fakeListener = Mockito.mock(ShakeListener::class.java)
        sensey?.startShakeDetection(4f, 1000, fakeListener)
        val detector = getDetector(fakeListener, ShakeDetector::class.java)
        if (detector != null) {
            assertTrue(
                "Sensor Manager must contain sensor event listener for shake",
                detector.hasDetector(),
            )
        } else {
            fail(
                "There should be shake detector in sensey. If not, please, check last version of class and update reflection accessing to it field",
            )
        }
    }

    @Test
    fun detectNoListenerWithStopFlipDetection() {
        addSensor(Sensor.TYPE_ACCELEROMETER)
        val fakeListener = Mockito.mock(FlipListener::class.java)
        sensey?.startFlipDetection(fakeListener)
        val detector = getDetector(fakeListener, FlipDetector::class.java)
        if (detector != null) {
            assertTrue(
                "Sensor Manager must contain sensor event listener for flip",
                detector.hasDetector(),
            )
            sensey?.stopFlipDetection(fakeListener)
            assertFalse(
                "There should be no more sensor event listener in sensor manager",
                detector.hasDetector(),
            )
        } else {
            fail(
                "There should be flip detector in sensey. If not, please, check last version of class and update reflection accessing to it field",
            )
        }
    }

    @Test
    fun detectNoListenerWithStopLightDetection() {
        addSensor(Sensor.TYPE_LIGHT)
        val fakeListener = Mockito.mock(LightListener::class.java)
        sensey?.startLightDetection(fakeListener)
        val detector = getDetector(fakeListener, LightDetector::class.java)
        if (detector != null) {
            assertTrue(
                "Sensor Manager must contain sensor event listener for light",
                detector.hasDetector(),
            )
            sensey?.stopLightDetection(fakeListener)
            assertFalse(
                "There should be no more sensor event listener in sensor manager",
                detector.hasDetector(),
            )
        } else {
            fail(
                "There should be light detector in sensey. If not, please, check last version of class and update reflection accessing to it field",
            )
        }
    }

    @Test
    fun detectNoListenerWithStopOrientationDetection() {
        addSensor(Sensor.TYPE_ACCELEROMETER)
        addSensor(Sensor.TYPE_MAGNETIC_FIELD)
        val fakeListener =
            Mockito.mock(
                OrientationDetector.OrientationListener::class.java,
            )
        sensey?.startOrientationDetection(fakeListener)
        val detector = getDetector(fakeListener, OrientationDetector::class.java)
        if (detector != null) {
            assertTrue(
                "Sensor Manager must contain sensor event listener for orientation",
                shadowSensorManager?.hasListener(detector) ?: false,
            )
            sensey?.stopOrientationDetection(fakeListener)
            assertFalse(
                "There should be no more sensor event listener in sensor manager",
                shadowSensorManager?.hasListener(detector) ?: false,
            )
        } else {
            fail(
                "There should be orientation detector in sensey. If not, please, check last version of class and update reflection accessing to it field",
            )
        }
    }

    @Test
    fun detectNoListenerWithStopProximityDetection() {
        addSensor(Sensor.TYPE_PROXIMITY)
        val fakeListener = Mockito.mock(ProximityListener::class.java)
        sensey?.startProximityDetection(fakeListener)
        val detector = getDetector(fakeListener, ProximityDetector::class.java)
        if (detector != null) {
            assertTrue(
                "Sensor Manager must contain sensor event listener for proximity",
                detector.hasDetector(),
            )
            sensey?.stopProximityDetection(fakeListener)
            assertFalse(
                "There should be no more sensor event listener in sensor manager",
                detector.hasDetector(),
            )
        } else {
            fail(
                "There should be proximity detector in sensey. If not, please, check last version of class and update reflection accessing to it field",
            )
        }
    }

    @Test
    fun detectNoListenerWithStopShakeDetection() {
        addSensor(Sensor.TYPE_ACCELEROMETER)
        val fakeListener = Mockito.mock(ShakeListener::class.java)
        sensey?.startShakeDetection(fakeListener)
        val detector = getDetector(fakeListener, ShakeDetector::class.java)
        if (detector != null) {
            assertTrue(
                "Sensor Manager must contain sensor event listener for shake",
                detector.hasDetector(),
            )
            sensey?.stopShakeDetection(fakeListener)
            assertFalse(
                "There should be no more sensor event listener in sensor manager",
                detector.hasDetector(),
            )
        } else {
            fail(
                "There should be shake detector in sensey. If not, please, check last version of class and update reflection accessing to it field",
            )
        }
    }

    @Test
    fun detectNoListenerWithStoppingTwoSameDetections() {
        addSensor(Sensor.TYPE_PROXIMITY)
        val fakeListener1 = Mockito.mock(ProximityListener::class.java)
        val fakeListener2 = Mockito.mock(ProximityListener::class.java)
        val detector1 = startProximityDetection(fakeListener1)
        val detector2 = startProximityDetection(fakeListener2)
        sensey?.stopProximityDetection(fakeListener1)
        sensey?.stopProximityDetection(fakeListener2)
        assertFalse(
            "Sensor manager need to contain no detectors",
            detector2.hasDetector(),
        )
        assertFalse(
            "Sensor manager need to contain no detectors",
            detector1.hasDetector(),
        )
    }

    private fun addSensor(type: Int) {
        shadowSensorManager?.addSensor(type, Mockito.mock(Sensor::class.java))
    }

    // Hardcode because of can not get appropriate detector from Sensey.class
    private fun <T> getDetector(
        listener: Any,
        aClass: Class<T>,
    ): T? {
        var result: T? = null

        try {
            val field = sensey?.javaClass?.getDeclaredField("defaultSensorsMap")
            field?.isAccessible = true
            val defaults = field?.get(sensey) as Map<Any, SensorDetector>
            result = aClass.cast(defaults[listener])
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }

        return result
    }

    private fun getFieldName(aClass: Class<*>): String? =
        if (aClass == ShakeDetector::class.java) {
            "shakeDetector"
        } else if (aClass == ProximityDetector::class.java) {
            "proximityDetector"
        } else if (aClass == OrientationDetector::class.java) {
            "orientationDetector"
        } else if (aClass == LightDetector::class.java) {
            "lightDetector"
        } else if (aClass == FlipDetector::class.java) {
            "flipDetector"
        } else if (aClass == WaveDetector::class.java) {
            "waveDetector"
        } else {
            null
        }

    private fun startProximityDetection(listener: ProximityListener): ProximityDetector {
        sensey?.startProximityDetection(listener)
        return getDetector(listener, ProximityDetector::class.java)!!
    }
}
