package ru.infern.taskalarm.alarmsignal

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator

class VibrationPlayer private constructor() {
    private var vibrator: Vibrator? = null

    fun start(context: Context) {
        if (vibrator == null) {
            vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            // Define the vibration pattern and duration here
            val pattern = longArrayOf(0, 1000, 500)
            vibrator?.vibrate(pattern, 0)
        }
    }

    fun stop() {
        vibrator?.cancel()
        vibrator = null
    }

    companion object {
        private var instance: VibrationPlayer? = null

        fun getInstance(): VibrationPlayer {
            return instance ?: VibrationPlayer().also { instance = it }
        }
    }
}