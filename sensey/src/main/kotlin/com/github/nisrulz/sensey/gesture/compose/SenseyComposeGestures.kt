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
package com.github.nisrulz.sensey.gesture.compose

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import com.github.nisrulz.sensey.SenseyPluginRegistry
import com.github.nisrulz.sensey.defaultSensey
import com.github.nisrulz.sensey.senseyRegister
import com.github.nisrulz.sensey.senseyStop

internal fun interface ComposeGestureProvider {
    suspend fun PointerInputScope.installGestures()
}

fun Modifier.senseyGestures(): Modifier {
    val sensey = defaultSensey ?: return this
    var combined = this
    for (provider in sensey.composeGestureProviders) {
        combined =
            combined.then(
                pointerInput(Unit) {
                    with(provider) { installGestures() }
                },
            )
    }
    return combined
}

@Composable
fun SenseyGestureEffect(
    lifecycle: Lifecycle,
    context: Context = LocalContext.current,
    sensorDataLoggingEnabled: Boolean = false,
    block: SenseyPluginRegistry.() -> Unit,
) {
    DisposableEffect(lifecycle) {
        context.senseyRegister(
            lifecycle = lifecycle,
            sensorDataLoggingEnabled = sensorDataLoggingEnabled,
            block = block,
        )
        onDispose { senseyStop() }
    }
}
