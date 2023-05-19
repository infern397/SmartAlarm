package ru.infern.taskalarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import java.util.*

class AlarmHelper(private val context: Context) {
    fun setAlarm(alarm: Alarm) {
        val intent = Intent(context, AlarmService::class.java)
        intent.action = AlarmService.ACTION_SET_ALARM
        intent.putExtra(AlarmService.EXTRA_ALARM, alarm)
        ContextCompat.startForegroundService(context, intent)
    }

    fun cancelAlarm(alarmId: Long) {
        val intent = Intent(context, AlarmService::class.java)
        intent.action = AlarmService.ACTION_CANCEL_ALARM
        intent.putExtra(AlarmService.EXTRA_ALARM, alarmId)
        context.startService(intent)
    }
}