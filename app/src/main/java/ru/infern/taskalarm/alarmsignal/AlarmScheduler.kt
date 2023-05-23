package ru.infern.taskalarm.alarmsignal

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import ru.infern.taskalarm.alarmsignal.AlarmHandler.Companion.EXTRA_ALARM_ID
import ru.infern.taskalarm.database.Alarm
import java.text.SimpleDateFormat
import java.util.*

/**
 * Класс AlarmScheduler для управления расписанием будильников.
 *
 * @property context Контекст приложения.
 */
class AlarmScheduler(private val context: Context) {
    private val alarmManager: AlarmManager =
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager // Менеджер будильников

    /**
     * Запланировать будильник.
     *
     * @param alarm Объект будильника.
     */
    fun scheduleAlarm(alarm: Alarm) {
        val alarmTime = getAlarmTime(alarm)
        val pendingIntent = getPendingIntent(alarm.id)
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            alarmTime.timeInMillis,
            pendingIntent
        )
        val currentTime = Calendar.getInstance().time
        val alarmTimeString = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(alarmTime.time)
        val currentTimeString = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(currentTime)
        Log.d("Alarm", "Current Time: $currentTimeString, Alarm Time: $alarmTimeString")
    }

    /**
     * Отменить запланированный будильник.
     *
     * @param alarm Объект будильника.
     */
    fun cancelAlarm(alarm: Alarm) {
        val pendingIntent = getPendingIntent(alarm.id)
        alarmManager.cancel(pendingIntent)
        pendingIntent.cancel()
    }

    /**
     * Получить время будильника в виде объекта Calendar.
     *
     * @param alarm Объект будильника.
     * @return Время будильника в виде объекта Calendar.
     */
    private fun getAlarmTime(alarm: Alarm): Calendar {
        val alarmTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, alarm.hours)
            set(Calendar.MINUTE, alarm.minutes)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (before(Calendar.getInstance())) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }
        return alarmTime
    }

    /**
     * Получить PendingIntent для передачи в AlarmManager.
     *
     * @param alarmId Идентификатор будильника.
     * @return PendingIntent для передачи в AlarmManager.
     */
    private fun getPendingIntent(alarmId: Int): PendingIntent {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra(EXTRA_ALARM_ID, alarmId)
        }
        intent.action = "com.project.action.ALERM"
        val flags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        return PendingIntent.getBroadcast(context, alarmId, intent, flags)
    }
}