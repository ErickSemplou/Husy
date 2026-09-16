package com.example.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.sin
import kotlin.random.Random

object SoundEffectsManager {
    private val scope = CoroutineScope(Dispatchers.Default)

    private val _isSoundEnabled = MutableStateFlow(true)
    val isSoundEnabled: StateFlow<Boolean> = _isSoundEnabled.asStateFlow()

    fun toggleSound(): Boolean {
        _isSoundEnabled.value = !_isSoundEnabled.value
        return _isSoundEnabled.value
    }

    fun setSoundEnabled(enabled: Boolean) {
        _isSoundEnabled.value = enabled
    }

    /**
     * Plays a deep tribal drum beat (percussive acoustic wood/skin strike)
     */
    fun playTribalDrum() {
        if (!_isSoundEnabled.value) return
        scope.launch {
            val sampleRate = 22050
            val durationMs = 280
            val numSamples = (sampleRate * durationMs) / 1000
            val buffer = ShortArray(numSamples)

            val baseFreq = 95.0
            for (i in 0 until numSamples) {
                val t = i.toDouble() / sampleRate
                val decay = exp(-t * 18.0)
                val pitchEnvelope = baseFreq * (1.0 + 1.2 * exp(-t * 35.0))
                val wave = sin(2.0 * PI * pitchEnvelope * t)
                val noise = (Random.nextFloat() * 2f - 1f) * 0.12f * exp(-t * 40.0)
                val sample = ((wave + noise) * decay * 0.9 * Short.MAX_VALUE).toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt())
                buffer[i] = sample.toShort()
            }
            playPcmBuffer(buffer, sampleRate)
        }
    }

    /**
     * Plays whoosh of swinging branches in the forest canopy (Brachiation)
     */
    fun playBranchSwing() {
        if (!_isSoundEnabled.value) return
        scope.launch {
            val sampleRate = 22050
            val durationMs = 320
            val numSamples = (sampleRate * durationMs) / 1000
            val buffer = ShortArray(numSamples)

            for (i in 0 until numSamples) {
                val t = i.toDouble() / sampleRate
                // Band-limited whoosh with rising and falling pitch
                val freq = 220.0 + 350.0 * sin(PI * (t / (durationMs / 1000.0)))
                val env = sin(PI * (t / (durationMs / 1000.0)))
                val noise = (Random.nextFloat() * 2f - 1f) * 0.4
                val wave = sin(2.0 * PI * freq * t) * 0.6 + noise
                val sample = (wave * env * 0.7 * Short.MAX_VALUE).toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt())
                buffer[i] = sample.toShort()
            }
            playPcmBuffer(buffer, sampleRate)
        }
    }

    /**
     * Plays a crisp, heavy stone impact and nut crack (Nut Cracker)
     */
    fun playStoneCrack() {
        if (!_isSoundEnabled.value) return
        scope.launch {
            val sampleRate = 22050
            val durationMs = 240
            val numSamples = (sampleRate * durationMs) / 1000
            val buffer = ShortArray(numSamples)

            for (i in 0 until numSamples) {
                val t = i.toDouble() / sampleRate
                val decay = exp(-t * 32.0)
                // High frequency crack click + lower stone thud
                val click = (Random.nextFloat() * 2f - 1f) * exp(-t * 120.0) * 1.5
                val thud = sin(2.0 * PI * 160.0 * t) * exp(-t * 22.0) * 0.7
                val snap = sin(2.0 * PI * 680.0 * t) * exp(-t * 60.0) * 0.5
                val sample = ((click + thud + snap) * decay * 0.85 * Short.MAX_VALUE).toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt())
                buffer[i] = sample.toShort()
            }
            playPcmBuffer(buffer, sampleRate)
        }
    }

    /**
     * Plays savanna footstep rustle (Savannah Step)
     */
    fun playSavannahStep() {
        if (!_isSoundEnabled.value) return
        scope.launch {
            val sampleRate = 22050
            val durationMs = 180
            val numSamples = (sampleRate * durationMs) / 1000
            val buffer = ShortArray(numSamples)

            for (i in 0 until numSamples) {
                val t = i.toDouble() / sampleRate
                val decay = exp(-t * 25.0)
                val rustle = (Random.nextFloat() * 2f - 1f) * 0.7
                val stepThud = sin(2.0 * PI * 85.0 * t) * 0.4
                val sample = ((rustle + stepThud) * decay * 0.6 * Short.MAX_VALUE).toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt())
                buffer[i] = sample.toShort()
            }
            playPcmBuffer(buffer, sampleRate)
        }
    }

    /**
     * Plays rich ascending tribal victory chord (Quest Completed)
     */
    fun playQuestSuccess() {
        if (!_isSoundEnabled.value) return
        scope.launch {
            val sampleRate = 22050
            val durationMs = 550
            val numSamples = (sampleRate * durationMs) / 1000
            val buffer = ShortArray(numSamples)

            val notes = doubleArrayOf(261.63, 329.63, 392.00, 523.25) // C-E-G-C major pentatonic harmony
            for (i in 0 until numSamples) {
                val t = i.toDouble() / sampleRate
                var sum = 0.0
                notes.forEachIndexed { idx, freq ->
                    val noteStart = idx * 0.08
                    if (t >= noteStart) {
                        val noteT = t - noteStart
                        val env = exp(-noteT * 6.5)
                        sum += sin(2.0 * PI * freq * noteT) * env * 0.35
                    }
                }
                val sample = (sum * Short.MAX_VALUE).toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt())
                buffer[i] = sample.toShort()
            }
            playPcmBuffer(buffer, sampleRate)
        }
    }

    /**
     * Plays majestic, resonant prehistoric Horn & gong fanfare (Epoch Ascension)
     */
    fun playEvolutionFanfare() {
        if (!_isSoundEnabled.value) return
        scope.launch {
            val sampleRate = 22050
            val durationMs = 1200
            val numSamples = (sampleRate * durationMs) / 1000
            val buffer = ShortArray(numSamples)

            val root = 130.81 // Deep C3
            val fifth = 196.00 // G3
            val octave = 261.63 // C4
            val highThird = 329.63 // E4

            for (i in 0 until numSamples) {
                val t = i.toDouble() / sampleRate
                val gong = sin(2.0 * PI * 65.0 * t) * exp(-t * 2.5) * 0.4
                val horn1 = (sin(2.0 * PI * root * t) + 0.3 * sin(4.0 * PI * root * t)) * exp(-t * 2.0) * 0.35
                val horn2 = if (t > 0.25) sin(2.0 * PI * fifth * (t - 0.25)) * exp(-(t - 0.25) * 2.0) * 0.35 else 0.0
                val horn3 = if (t > 0.50) sin(2.0 * PI * octave * (t - 0.50)) * exp(-(t - 0.50) * 1.8) * 0.4 else 0.0
                val horn4 = if (t > 0.70) sin(2.0 * PI * highThird * (t - 0.70)) * exp(-(t - 0.70) * 1.5) * 0.45 else 0.0

                val sample = ((gong + horn1 + horn2 + horn3 + horn4) * 0.75 * Short.MAX_VALUE).toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt())
                buffer[i] = sample.toShort()
            }
            playPcmBuffer(buffer, sampleRate)
        }
    }

    /**
     * Plays quiz correct answer bell
     */
    fun playQuizCorrect() {
        if (!_isSoundEnabled.value) return
        scope.launch {
            val sampleRate = 22050
            val durationMs = 300
            val numSamples = (sampleRate * durationMs) / 1000
            val buffer = ShortArray(numSamples)

            val f1 = 587.33 // D5
            val f2 = 880.00 // A5
            for (i in 0 until numSamples) {
                val t = i.toDouble() / sampleRate
                val chime1 = sin(2.0 * PI * f1 * t) * exp(-t * 14.0) * 0.4
                val chime2 = if (t > 0.08) sin(2.0 * PI * f2 * (t - 0.08)) * exp(-(t - 0.08) * 10.0) * 0.5 else 0.0
                val sample = ((chime1 + chime2) * Short.MAX_VALUE).toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt())
                buffer[i] = sample.toShort()
            }
            playPcmBuffer(buffer, sampleRate)
        }
    }

    /**
     * Plays quiz incorrect answer low thud
     */
    fun playQuizWrong() {
        if (!_isSoundEnabled.value) return
        scope.launch {
            val sampleRate = 22050
            val durationMs = 260
            val numSamples = (sampleRate * durationMs) / 1000
            val buffer = ShortArray(numSamples)

            for (i in 0 until numSamples) {
                val t = i.toDouble() / sampleRate
                val freq = 130.0 - (t * 200.0) // falling pitch
                val wave = sin(2.0 * PI * freq * t) * exp(-t * 12.0) * 0.5
                val sample = (wave * Short.MAX_VALUE).toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt())
                buffer[i] = sample.toShort()
            }
            playPcmBuffer(buffer, sampleRate)
        }
    }

    private fun playPcmBuffer(buffer: ShortArray, sampleRate: Int) {
        try {
            val minBufferSize = AudioTrack.getMinBufferSize(
                sampleRate,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT
            )
            val bufferSize = maxOf(minBufferSize, buffer.size * 2)

            val audioTrack = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_GAME)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(sampleRate)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setBufferSizeInBytes(bufferSize)
                .setTransferMode(AudioTrack.MODE_STATIC)
                .build()

            audioTrack.write(buffer, 0, buffer.size)
            audioTrack.play()

            // Release track after playback
            audioTrack.setNotificationMarkerPosition(buffer.size)
            audioTrack.setPlaybackPositionUpdateListener(object : AudioTrack.OnPlaybackPositionUpdateListener {
                override fun onPeriodicNotification(track: AudioTrack?) {}
                override fun onMarkerReached(track: AudioTrack?) {
                    try {
                        track?.stop()
                        track?.release()
                    } catch (_: Exception) {}
                }
            })
        } catch (_: Exception) {
            // Graceful fallback if device audio is occupied
        }
    }
}
