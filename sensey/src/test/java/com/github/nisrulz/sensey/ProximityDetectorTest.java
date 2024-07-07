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

package com.github.nisrulz.sensey;

import static com.github.nisrulz.sensey.SensorUtils.testSensorEvent;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

import android.hardware.Sensor;
import android.hardware.SensorEvent;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ProximityDetectorTest {

    @Mock
    private ProximityDetector.ProximityListener mockListener;

    @Test
    public void detectOnFarWithExtraValues() {
        this.testDetector().onSensorChanged(this.testProximityEvent(new float[]{10, 0, 43, 3, -423}));
        verify(this.mockListener, only()).onFar();
    }

    @Test
    public void detectOnFarWithLuxMoreThanDefaultThreshold() {
        this.testDetector().onSensorChanged(this.testProximityEvent(new float[]{10}));
        verify(this.mockListener, only()).onFar();
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void exceptionWithEmptyValues() {
        this.testDetector().onSensorChanged(this.testProximityEvent(new float[]{}));
    }

    private ProximityDetector testDetector() {
        return new ProximityDetector(this.mockListener);
    }

    private SensorEvent testProximityEvent(final float[] values) {
        return testSensorEvent(values, Sensor.TYPE_PROXIMITY);
    }
}
