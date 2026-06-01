
package com.github.nisrulz.sensey.internal

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Process
import android.util.Log
import com.github.nisrulz.sensey.contract.GestureTrigger
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class AudioCapture<T>(
    private val trigger: GestureTrigger<T>,
    private val dispatcher: (T) -> Unit,
    private val logTag: String,
    private val bufferMultiplier: Int = 2,
) {
    private val sampleRate = resolveSampleRate()
    private val bufferSize = resolveAudioBufferSize(sampleRate, bufferMultiplier)
    private var audioJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    fun start() {
        if (audioJob?.isActive == true) stop()
        audioJob = scope.launch(Dispatchers.IO) { captureAudio() }
    }

    fun stop() {
        audioJob?.cancel()
        audioJob = null
    }

    private suspend fun captureAudio() {
        try {
            Process.setThreadPriority(Process.THREAD_PRIORITY_AUDIO)
            val audioRecord = createAudioRecord() ?: return
            try {
                audioRecord.startRecording()
                if (audioRecord.recordingState != AudioRecord.RECORDSTATE_RECORDING) {
                    Log.e(logTag, "AudioRecord failed to start recording (state=${audioRecord.recordingState})")
                    audioRecord.release()
                    return
                }
            } catch (e: SecurityException) {
                Log.e(logTag, "RECORD_AUDIO permission denied at runtime", e)
                audioRecord.release()
                return
            }
            processAudioStream(audioRecord)
        } catch (e: CancellationException) {
            // Normal cancellation when stop() is called — no error log needed
        } catch (e: Exception) {
            Log.e(logTag, "Audio capture failed", e)
        }
    }

    private fun createAudioRecord(): AudioRecord? {
        if (sampleRate == 0 || bufferSize == 0) {
            Log.e(logTag, "Invalid sample rate ($sampleRate) or buffer size ($bufferSize)")
            return null
        }
        for (source in listOf(AUDIO_SOURCE_VOICE_RECOGNITION, AUDIO_SOURCE_MIC)) {
            val record = tryCreateAudioRecord(source) ?: continue
            return record
        }
        Log.e(logTag, "All audio sources failed to initialize")
        return null
    }

    private fun tryCreateAudioRecord(source: Int): AudioRecord? {
        val record =
            try {
                AudioRecord(source, sampleRate, AUDIO_CHANNEL, AUDIO_ENCODING, bufferSize)
            } catch (e: IllegalArgumentException) {
                Log.w(logTag, "AudioRecord($source) constructor failed", e)
                return null
            } catch (e: SecurityException) {
                Log.e(logTag, "RECORD_AUDIO permission denied in AudioRecord constructor", e)
                return null
            }
        if (record.state != AudioRecord.STATE_INITIALIZED) {
            Log.w(logTag, "AudioRecord($source) not initialized (state=${record.state})")
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
                if (samplesRead < 0) {
                    Log.e(logTag, "AudioRecord read error: $samplesRead")
                    break
                }
                if (samplesRead == 0) continue
                for (i in 0 until samplesRead) {
                    floatBuffer[i] = audioBuffer[i].toFloat()
                }
                val event = trigger.evaluate(floatBuffer.copyOfRange(0, samplesRead), System.currentTimeMillis())
                if (event != null) {
                    withContext(Dispatchers.Default) { dispatcher(event) }
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
        private const val AUDIO_SOURCE_VOICE_RECOGNITION = MediaRecorder.AudioSource.VOICE_RECOGNITION
        private const val AUDIO_SOURCE_MIC = MediaRecorder.AudioSource.MIC
        private const val AUDIO_CHANNEL = AudioFormat.CHANNEL_IN_MONO
        private const val AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT
        private val SAMPLE_RATES = intArrayOf(8000, 11025, 16000, 22050, 44100, 48000)

        private fun resolveSampleRate(): Int {
            for (rate in SAMPLE_RATES) {
                val bufferSize = AudioRecord.getMinBufferSize(rate, AUDIO_CHANNEL, AUDIO_ENCODING)
                if (bufferSize > 0) return rate
            }
            return 0
        }

        private fun resolveAudioBufferSize(
            sampleRate: Int,
            multiplier: Int,
        ): Int {
            if (sampleRate <= 0) return 0
            val minSize = AudioRecord.getMinBufferSize(sampleRate, AUDIO_CHANNEL, AUDIO_ENCODING)
            if (minSize <= 0) return 0
            return minSize * multiplier
        }
    }
}
