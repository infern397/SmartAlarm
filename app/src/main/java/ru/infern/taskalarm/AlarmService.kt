package ru.infern.taskalarm

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.os.PowerManager

class AlarmService : IntentService("AlarmService") {
    override fun onHandleIntent(intent: Intent?) {
        val broadcastIntent = Intent(applicationContext, AlarmReceiver::class.java)
        sendBroadcast(broadcastIntent)
    }
}