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
import android.os.Process
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class SoundLevelDetector(
    private val trigger: SoundLevelTrigger,
    private val dispatcher: (SoundLevelEvent) -> Unit,
) {
    private val sampleRate = resolveSampleRate()
    private val bufferSize = resolveBufferSize(sampleRate)
    private var audioJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    fun start() {
        if (audioJob?.isActive == true) stop()
        audioJob = scope.launch(Dispatchers.IO) { captureAudio() }
    }

    private suspend fun captureAudio() {
        Process.setThreadPriority(Process.THREAD_PRIORITY_AUDIO)
        val audioRecord = createAudioRecord() ?: return
        try {
            audioRecord.startRecording()
        } catch (e: SecurityException) {
            Log.e(LOGTAG, "RECORD_AUDIO permission denied at runtime", e)
            audioRecord.release()
            return
        }
        processAudioStream(audioRecord)
    }

    fun stop() {
        audioJob?.cancel()
        audioJob = null
    }

    private fun createAudioRecord(): AudioRecord? {
        if (sampleRate == 0 || bufferSize == 0) {
            Log.e(LOGTAG, "Invalid sample rate or buffer size")
            return null
        }
        val actualBufferSize = if (bufferSize == AudioRecord.ERROR_BAD_VALUE) sampleRate * 2 else bufferSize
        val record =
            AudioRecord(AUDIO_SOURCE, sampleRate, AUDIO_CHANNEL, AUDIO_ENCODING, actualBufferSize)
        if (record.state != AudioRecord.STATE_INITIALIZED) {
            Log.e(LOGTAG, "AudioRecord could not be initialized")
            record.release()
            return null
        }
        return record
    }

    private suspend fun processAudioStream(audioRecord: AudioRecord) {
        val audioBuffer = ShortArray(bufferSize / 2)
        val floatBuffer = FloatArray(bufferSize / 2)

        try {
            while (currentCoroutineContext().isActive) {
                val samplesRead = audioRecord.read(audioBuffer, 0, audioBuffer.size)
                if (samplesRead <= 0) continue

                for (i in 0 until samplesRead) {
                    floatBuffer[i] = audioBuffer[i].toFloat()
                }

                val event = trigger.evaluate(floatBuffer.copyOfRange(0, samplesRead), System.currentTimeMillis())
                if (event != null) {
                    withContext(Dispatchers.Main) { dispatcher(event) }
                }
            }
        } finally {
            releaseAudioRecord(audioRecord)
        }
    }

    private fun releaseAudioRecord(audioRecord: AudioRecord) {
        try {
            audioRecord.stop()
        } catch (_: Exception) {
        } finally {
            audioRecord.release()
        }
    }

    companion object {
        private const val LOGTAG = "===SoundLevel==="
        private const val AUDIO_SOURCE = MediaRecorder.AudioSource.VOICE_RECOGNITION
        private const val AUDIO_CHANNEL = AudioFormat.CHANNEL_IN_MONO
        private const val AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT

        private fun resolveSampleRate(): Int {
            for (rate in SAMPLE_RATES) {
                val bufferSize = AudioRecord.getMinBufferSize(rate, AUDIO_CHANNEL, AUDIO_ENCODING)
                if (bufferSize > 0) return rate
            }
            return 0
        }

        private fun resolveBufferSize(sampleRate: Int): Int {
            for (size in BUFFER_SIZES) {
                val record =
                    AudioRecord(AUDIO_SOURCE, sampleRate, AUDIO_CHANNEL, AUDIO_ENCODING, size)
                val initialized = record.state == AudioRecord.STATE_INITIALIZED
                record.release()
                if (initialized) return size
            }
            return 0
        }

        private val SAMPLE_RATES = intArrayOf(8000, 11025, 16000, 22050, 44100, 48000)
        private val BUFFER_SIZES = intArrayOf(256, 512, 1024, 2048)
    }
}
