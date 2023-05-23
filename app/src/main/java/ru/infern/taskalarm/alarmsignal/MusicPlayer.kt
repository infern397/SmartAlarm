package ru.infern.taskalarm.alarmsignal

import android.content.Context
import android.media.MediaPlayer
import ru.infern.taskalarm.R

/**
 * Медиаплеер для проигрывания музыки при срабатывании будильника.
 *
 */
class MusicPlayer private constructor() {
    private var mediaPlayer: MediaPlayer? = null // Системный медиаплеер

    /**
     * Запускает проигрывание музыки.
     *
     * @param context контекст приложения
     */
    fun start(context: Context) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context.applicationContext, R.raw.teryam)
            mediaPlayer?.isLooping = true
            mediaPlayer?.start()
        }
    }

    /**
     * Останавливает проигрывание музыки и освобождает ресурсы медиаплеера.
     *
     */
    fun stop() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    /**
     * Singleton-паттерн для получения экземпляра медиаплеера.
     */
    companion object {
        private var instance: MusicPlayer? = null

        /**
         * Возвращает экземпляр медиаплеера.
         *
         * @return экземпляр медиаплеера
         */
        fun getInstance(): MusicPlayer {
            return instance ?: MusicPlayer().also { instance = it }
        }
    }
}