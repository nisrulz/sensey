
package com.github.nisrulz.sensey.gesture.soundlevel

import com.github.nisrulz.sensey.internal.AudioCapture

internal class SoundLevelDetector(
    trigger: SoundLevelTrigger,
    dispatcher: (SoundLevelEvent) -> Unit,
) {
    private val capture = AudioCapture(trigger, dispatcher, LOGTAG, bufferMultiplier = 2)

    fun start() = capture.start()

    fun stop() = capture.stop()

    companion object {
        private const val LOGTAG = "===SoundLevel==="
    }
}
