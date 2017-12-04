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
import static org.mockito.Mockito.*;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import com.github.nisrulz.sensey.ProximityDetector.ProximityListener;
import org.junit.*;
import org.junit.runner.*;
import org.mockito.*;
import org.mockito.junit.*;

@RunWith(MockitoJUnitRunner.class)
public class ProximityDetectorTest {

    @Mock
    private ProximityListener mockListener;

    @Test
    public void detectOnFarWithExtraValues() {
        testDetector().onSensorChanged(testProximityEvent(new float[]{10, 0, 43, 3, -423}));
        verify(mockListener, only()).onFar();
    }

    @Test
    public void detectOnFarWithLuxMoreThanDefaultThreshold() {
        testDetector().onSensorChanged(testProximityEvent(new float[]{10}));
        verify(mockListener, only()).onFar();
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void exceptionWithEmptyValues() {
        testDetector().onSensorChanged(testProximityEvent(new float[]{}));
    }

    private ProximityDetector testDetector() {
        return new ProximityDetector(mockListener);
    }

    private SensorEvent testProximityEvent(float[] values) {
        return testSensorEvent(values, Sensor.TYPE_PROXIMITY);
    }
}
