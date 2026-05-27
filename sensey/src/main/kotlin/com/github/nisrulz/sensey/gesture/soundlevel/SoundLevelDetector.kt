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
package com.github.nisrulz.sensey.gesture.soundlevel

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.util.Log

class SoundLevelDetector(
    private val trigger: SoundLevelTrigger,
    private val dispatcher: (SoundLevelEvent) -> Unit,
) {
    private val sampleRate: Int = getValidSampleRate()
    private var bufferSize = getValidBufferSize(sampleRate)
    private var shouldContinueProcessing = false
    private var audioRecordingThread: Thread? = null

    private val audioRecordRunnable = Runnable {
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_AUDIO)

        if (sampleRate == 0 || bufferSize == 0) {
            Log.e(LOGTAG, "Invalid SampleRate/BufferSize! AudioRecord cannot be initialized. Exiting!")
            return@Runnable
        }

        if (bufferSize == AudioRecord.ERROR || bufferSize == AudioRecord.ERROR_BAD_VALUE) {
            bufferSize = sampleRate * 2
        }

        val audioBuffer = ShortArray(bufferSize / 2)
        val floats = FloatArray(bufferSize / 2)
        val audioRecord = AudioRecord(
            AUDIO_SOURCE,
            sampleRate,
            AUDIO_CHANNEL,
            AUDIO_ENCODING,
            bufferSize,
        )

        if (audioRecord.state != AudioRecord.STATE_INITIALIZED) {
            Log.e(LOGTAG, "AudioRecord could not be initialized. Exiting!")
            return@Runnable
        }

        audioRecord.startRecording()
        shouldContinueProcessing = true

        while (shouldContinueProcessing) {
            val numberOfShorts = audioRecord.read(audioBuffer, 0, audioBuffer.size)
            for (i in 0 until numberOfShorts) {
                floats[i] = audioBuffer[i].toFloat()
            }
            val event = trigger.evaluate(floats.copyOfRange(0, numberOfShorts), System.currentTimeMillis())
            event?.let(dispatcher)
        }

        try {
            audioRecord.stop()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            audioRecord.release()
        }
    }

    fun start() {
        if (audioRecordingThread == null) {
            audioRecordingThread = Thread(audioRecordRunnable)
            audioRecordingThread?.start()
        } else if (audioRecordingThread?.isAlive == true) {
            stopThreadAndProcessing()
            audioRecordingThread = Thread(audioRecordRunnable)
            audioRecordingThread?.start()
        }
    }

    fun stop() {
        stopThreadAndProcessing()
    }

    private fun stopThreadAndProcessing() {
        shouldContinueProcessing = false
        audioRecordingThread?.interrupt()
        audioRecordingThread = null
    }

    companion object {
        private const val LOGTAG = "SoundLevelDetector"
        private const val AUDIO_SOURCE = MediaRecorder.AudioSource.VOICE_RECOGNITION
        private const val AUDIO_CHANNEL = AudioFormat.CHANNEL_IN_MONO
        private const val AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT

        private fun getValidSampleRate(): Int {
            for (rate in intArrayOf(8000, 11025, 16000, 22050, 44100, 48000)) {
                val bufferSize = AudioRecord.getMinBufferSize(rate, AUDIO_CHANNEL, AUDIO_ENCODING)
                if (bufferSize > 0) return rate
            }
            return 0
        }

        private fun getValidBufferSize(sampleRate: Int): Int {
            for (bufferSize in intArrayOf(256, 512, 1024, 2048, 4096)) {
                val tempRecord = AudioRecord(
                    AUDIO_SOURCE, sampleRate,
                    AUDIO_CHANNEL, AUDIO_ENCODING, bufferSize,
                )
                if (tempRecord.state == AudioRecord.STATE_INITIALIZED) {
                    return bufferSize
                }
            }
            return 0
        }
    }
}
