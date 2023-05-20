package ru.infern.taskalarm

import android.app.IntentService
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.os.PowerManager

class AlarmService : Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val taskIntent = Intent(applicationContext, TaskActivity::class.java)
        taskIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(taskIntent)
        return START_NOT_STICKY
    }
}