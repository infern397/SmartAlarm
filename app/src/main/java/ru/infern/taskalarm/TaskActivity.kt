package ru.infern.taskalarm

import android.media.MediaPlayer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class TaskActivity : AppCompatActivity() {
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm_call)

        // Initialize the MediaPlayer
        mediaPlayer = MediaPlayer.create(this, R.raw.teryam)

        // Set looping to true to play the sound in a loop
        mediaPlayer?.isLooping = true

        // Start playing the custom sound
        mediaPlayer?.start()
    }

    override fun onDestroy() {
        super.onDestroy()

        // Release the MediaPlayer resources
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}