
package com.github.nisrulz.sensey.gesture.audio.clap

import com.github.nisrulz.sensey.internal.AudioCapture

internal class ClapDetector(
    trigger: ClapTrigger,
    dispatcher: (ClapEvent) -> Unit,
) {
    private val capture = AudioCapture(trigger, dispatcher, LOGTAG, bufferMultiplier = 1)

    fun start() = capture.start()

    fun stop() = capture.stop()

    companion object {
        private const val LOGTAG = "===ClapDetector==="
    }
}
