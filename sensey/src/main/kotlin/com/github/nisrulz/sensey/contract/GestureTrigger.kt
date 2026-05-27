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
package com.github.nisrulz.sensey.contract

interface GestureTrigger<T> {
    /**
     * Evaluates raw sensor or gesture data and returns an event if the
     * gesture threshold is met.
     *
     * ## Index semantics by trigger type
     *
     * | Trigger | values[0] | values[1] | values[2] | values[3] |
     * |---------|-----------|-----------|-----------|-----------|
     * | Chop | x | y | z | — |
     * | Flip | x | y | z | — |
     * | Light | light level | — | — | — |
     * | Movement | x | y | z | — |
     * | Orientation | x | y | z | — |
     * | PickupDevice | x | y | z | — |
     * | Proximity | cm distance | — | — | — |
     * | RotationAngle | x | y | z | — |
     * | Scoop | x | y | z | — |
     * | Shake | x | y | z | — |
     * | Step | x | y | z | — |
     * | TiltDirection | x | y | z | — |
     * | Wave | x | y | z | — |
     * | WristTwist | x | y | z | — |
     * | TouchType | deltaX | deltaY | velocityX | velocityY |
     * | SoundLevel | audio sample amplitude[n] | — | — | — |
     *
     * @param values Sensor readings or processed data. See table above for
     *   per-trigger index layout. For [SoundLevelTrigger] this is an array of
     *   amplitude values with no positional semantics.
     * @param timestamp Event timestamp in milliseconds.
     * @return A typed event if the gesture is detected, or `null` otherwise.
     */
    fun evaluate(values: FloatArray, timestamp: Long): T?
}
