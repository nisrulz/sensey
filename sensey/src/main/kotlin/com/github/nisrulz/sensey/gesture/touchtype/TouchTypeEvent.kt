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
package com.github.nisrulz.sensey.gesture.touchtype

sealed interface TouchTypeEvent {
    data object DoubleTap : TouchTypeEvent
    data object LongPress : TouchTypeEvent
    data object SingleTap : TouchTypeEvent
    data class Swipe(val direction: Int) : TouchTypeEvent
    data class Scroll(val direction: Int) : TouchTypeEvent
    data object ThreeFingerSingleTap : TouchTypeEvent
    data object TwoFingerSingleTap : TouchTypeEvent
}
