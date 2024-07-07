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
import com.github.nisrulz.sensey.LightDetector.LightListener
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LightDetectorTest {
    @Mock
    private val mockListener: LightListener? = null

    @Test
    fun detectOnDarkWithLuxLessThanCustomThreshold() {
        this.testDetector(9f).onSensorChanged(this.testLightEvent(floatArrayOf(3f)))
        Mockito.verify(this.mockListener, Mockito.only())?.onDark()
    }

    @Test
    fun detectOnDarkWithLuxLessThanDefaultThreshold() {
        this.testDetector().onSensorChanged(this.testLightEvent(floatArrayOf(1f)))
        Mockito.verify(this.mockListener, Mockito.only())?.onDark()
    }

    @Test
    fun detectOnLightWithExtraValues() {
        this
            .testDetector()
            .onSensorChanged(this.testLightEvent(floatArrayOf(10f, 0f, 43f, 3f, -423f)))
        Mockito.verify(this.mockListener, Mockito.only())?.onLight()
    }

    @Test
    fun detectOnLightWithLuxEqualsToCustomThreshold() {
        this.testDetector(9f).onSensorChanged(this.testLightEvent(floatArrayOf(9f)))
        Mockito.verify(this.mockListener, Mockito.only())?.onLight()
    }

    @Test
    fun detectOnLightWithLuxEqualsToDefaultThreshold() {
        this.testDetector().onSensorChanged(this.testLightEvent(floatArrayOf(3f)))
        Mockito.verify(this.mockListener, Mockito.only())?.onLight()
    }

    @Test
    fun detectOnLightWithLuxMoreThanCustomThreshold() {
        this.testDetector(9f).onSensorChanged(this.testLightEvent(floatArrayOf(12f)))
        Mockito.verify(this.mockListener, Mockito.only())?.onLight()
    }

    @Test
    fun detectOnLightWithLuxMoreThanDefaultThreshold() {
        this.testDetector().onSensorChanged(this.testLightEvent(floatArrayOf(10f)))
        Mockito.verify(this.mockListener, Mockito.only())?.onLight()
    }

    @Test(expected = ArrayIndexOutOfBoundsException::class)
    fun exceptionWithEmptyValues() {
        this.testDetector().onSensorChanged(this.testLightEvent(floatArrayOf()))
    }

    private fun testDetector(): LightDetector = LightDetector(this.mockListener)

    private fun testDetector(threshold: Float): LightDetector = LightDetector(threshold, this.mockListener)

    private fun testLightEvent(values: FloatArray): SensorEvent = SensorUtils.testSensorEvent(values, Sensor.TYPE_LIGHT)
}
