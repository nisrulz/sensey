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

import static com.github.nisrulz.sensey.SensorUtils.testAccelerometerEvent;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ShakeDetectorTest {

    @Mock
    private ShakeDetector.ShakeListener mockListener;

    @Test
    public void detectNothingWithZeroGravity() {
        this.testDetector().onSensorChanged(testAccelerometerEvent(new float[]{0, 0, 0}));
        verifyNoMoreInteractions(this.mockListener);
    }

    @Test
    public void detectNothingWithZeroGravityForCustomThreshold() {
        this.testDetector(10, 1000).onSensorChanged(testAccelerometerEvent(new float[]{0, 0, 0}));
        verifyNoMoreInteractions(this.mockListener);
    }

    @Test
    public void detectShakeWithDoubleGravity() {
        this.testDetector().onSensorChanged(testAccelerometerEvent(new float[]{0, 0, 2 * 9.81f}));
        verify(this.mockListener, only()).onShakeDetected();
    }

    @Test
    public void detectShakeWithDoubleGravityForCustomThreshold() {
        this.testDetector(9, 1000).onSensorChanged(testAccelerometerEvent(new float[]{0, 0, 2 * 9.81f}));
        verify(this.mockListener, only()).onShakeDetected();
    }

    @Test
    public void detectShakeWithSeveralGravitySensors() {
        final ShakeDetector testDetector = this.testDetector();
        testDetector.onSensorChanged(testAccelerometerEvent(new float[]{0, 0, 2 * 9.81f}));
        testDetector.onSensorChanged(testAccelerometerEvent(new float[]{0, 0, 2 * -9.81f}));
        verify(this.mockListener, times(2)).onShakeDetected();
    }

    @Test
    public void detectShakeWithSeveralGravitySensorsForCustomThreshold() {
        final ShakeDetector testDetector = this.testDetector(9, 1000);
        testDetector.onSensorChanged(testAccelerometerEvent(new float[]{0, 0, 2 * 9.81f}));
        testDetector.onSensorChanged(testAccelerometerEvent(new float[]{0, 0, 2 * -9.81f}));
        verify(this.mockListener, times(1)).onShakeDetected();
    }

    @Test
    public void detectShakeWithSeveralStrongGravitySensorsForCustomThreshold() {
        final ShakeDetector testDetector = this.testDetector(9, 1000);
        testDetector.onSensorChanged(testAccelerometerEvent(new float[]{0, 0, 2 * 9.81f}));
        testDetector.onSensorChanged(testAccelerometerEvent(new float[]{0, 0, 3 * -9.81f}));
        verify(this.mockListener, times(2)).onShakeDetected();
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void exceptionWithLessThanThreeElements() {
        this.testDetector().onSensorChanged(testAccelerometerEvent(new float[]{2, 3}));
    }

    private ShakeDetector testDetector() {
        return new ShakeDetector(this.mockListener);
    }

    private ShakeDetector testDetector(final float threshold, final long timeBeforeDeclaringShakeStopped) {
        return new ShakeDetector(threshold, timeBeforeDeclaringShakeStopped, this.mockListener);
    }
}
