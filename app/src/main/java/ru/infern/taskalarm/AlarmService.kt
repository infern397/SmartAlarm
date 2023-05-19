package ru.infern.taskalarm

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import java.util.*

class AlarmService : Service() {
    private var alarmManager: AlarmManager? = null
    private var notificationManager: NotificationManager? = null

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action
        when (action) {
            ACTION_SET_ALARM -> {
                val alarm = intent.getSerializableExtra(EXTRA_ALARM) as Alarm
                setAlarm(alarm)
            }
            ACTION_CANCEL_ALARM -> {
                val alarm = intent.getSerializableExtra(EXTRA_ALARM) as Alarm
                cancelAlarm(alarm)
            }
        }
        return START_STICKY
    }

    private fun setAlarm(alarm: Alarm) {
        val alarmTime = getAlarmTime(alarm)

        val intent = Intent(this, AlarmReceiver::class.java)
        intent.putExtra(EXTRA_ALARM, alarm)
        val pendingIntent =
            alarm.id?.let {
                PendingIntent.getBroadcast(this,
                    it, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
            }

        alarmManager?.setExact(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent)

        // Здесь вызываем метод startForeground()
        val notification = createNotification()
        startForeground(NOTIFICATION_ID, notification)
    }

    private fun cancelAlarm(alarm: Alarm) {
        val intent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent =
            alarm.id?.let {
                PendingIntent.getBroadcast(this,
                    it, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
            }

        alarmManager?.cancel(pendingIntent)
        pendingIntent?.cancel()

        alarm.id?.let { notificationManager?.cancel(it) }

        // Остановка службы, если нет активных будильников
        if (noActiveAlarms()) {
            stopForeground(true)
            stopSelf()
        }
    }

    private fun getAlarmTime(alarm: Alarm): Long {
        val calendar = Calendar.getInstance()

        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)

        calendar.set(Calendar.HOUR_OF_DAY, alarm.hours)
        calendar.set(Calendar.MINUTE, alarm.minutes)
        calendar.set(Calendar.SECOND, 0)

        if (alarm.hours < currentHour || (alarm.hours == currentHour && alarm.minutes <= currentMinute)) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        return calendar.timeInMillis
    }

    private fun createNotification(): Notification {
        // Создание канала уведомлений (требуется для версии Android 8.0 и выше)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "alarm_channel"
            val channelName = "Alarm Channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, channelName, importance)
            notificationManager?.createNotificationChannel(channel)
        }

        // Создание уведомления
        val notificationBuilder = NotificationCompat.Builder(this, "alarm_channel")
            .setContentTitle("Alarm")
            .setContentText("Alarm is set")
//            .setSmallIcon(R.drawable.ic_alarm)
            .setAutoCancel(true)

        return notificationBuilder.build()
    }

    private fun noActiveAlarms(): Boolean {
        // Здесь вам нужно реализовать логику проверки наличия активных будильников.
        // Например, можно проверить наличие записей в базе данных, содержащих активные будильники,
        // и вернуть соответствующее значение. В данном примере возвращаем просто `true`,
        // чтобы продемонстрировать, что метод выполняется без ошибок.
        return true
    }

    companion object {
        const val ACTION_SET_ALARM = "ru.infern.taskalarm.ACTION_SET_ALARM"
        const val ACTION_CANCEL_ALARM = "ru.infern.taskalarm.ACTION_CANCEL_ALARM"
        const val EXTRA_ALARM = "ru.infern.taskalarm.EXTRA_ALARM"
        private const val NOTIFICATION_ID = 1
    }
}
