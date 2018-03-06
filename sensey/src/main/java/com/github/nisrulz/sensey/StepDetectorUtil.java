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

public class StepDetectorUtil {

    public static final int ACTIVITY_STILL = 0;

    public static final int ACTIVITY_WALKING = 1;

    public static final int ACTIVITY_RUNNING = 2;

    public static final int MALE = 0;

    public static final int FEMALE = 1;

    static float getDistanceCovered(long steps, int gender) {
    /*
      Using average distance covered in one step:  78cm for men and 70cm for women.
     */
        int averageStepDistance = 0;
        if (gender == MALE) {
            averageStepDistance = 78;
        } else if (gender == FEMALE) {
            averageStepDistance = 70;
        }

        float distanceCovered = (float) (steps * averageStepDistance) / (float) 100; // in meters
        return distanceCovered;
    }

    static int getStepActivityType(float distance, long timeDelta) {
        float speed = distance / timeDelta;
        int result = ACTIVITY_STILL;

        if (speed > 20) {
            // Running
            result = ACTIVITY_RUNNING;
        } else if (speed < 20 && speed > 0.3) {
            // Walking
            result = ACTIVITY_WALKING;
        }

        return result;
    }
}
