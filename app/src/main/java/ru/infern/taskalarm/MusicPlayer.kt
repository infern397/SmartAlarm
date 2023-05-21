package ru.infern.taskalarm

import android.content.Context
import android.media.MediaPlayer

class MusicPlayer private constructor() {
    private var mediaPlayer: MediaPlayer? = null

    fun start(context: Context) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context.applicationContext, R.raw.teryam)
            mediaPlayer?.isLooping = true
            mediaPlayer?.start()
        }
    }

    fun stop() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    companion object {
        private var instance: MusicPlayer? = null

        fun getInstance(): MusicPlayer {
            return instance ?: MusicPlayer().also { instance = it }
        }
    }
}