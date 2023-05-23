package ru.infern.taskalarm.alarmsignal

import android.content.Context

import android.os.Vibrator

/**
 * Виброплеер для включения вибрации при срабатывании будильника.
 *
 */
class VibrationPlayer private constructor() {
    private var vibrator: Vibrator? = null // Системный вибратор

    /**
     * Запускает вибрацию.
     *
     * @param context контекст приложения
     */
    fun start(context: Context) {
        if (vibrator == null) {
            vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            // Define the vibration pattern and duration here
            val pattern = longArrayOf(0, 1000, 500)
            vibrator?.vibrate(pattern, 0)
        }
    }

    /**
     * Останавливает вибрацию.
     *
     */
    fun stop() {
        vibrator?.cancel()
        vibrator = null
    }

    /**
     * Singleton-паттерн для получения экземпляра виброплеера.
     */
    companion object {
        private var instance: VibrationPlayer? = null

        /**
         * Возвращает экземпляр виброплеера.
         *
         * @return экземпляр виброплеера
         */
        fun getInstance(): VibrationPlayer {
            return instance ?: VibrationPlayer().also { instance = it }
        }
    }
}