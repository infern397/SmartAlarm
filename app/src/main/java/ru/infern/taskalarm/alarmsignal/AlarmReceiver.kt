package ru.infern.taskalarm.alarmsignal

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import ru.infern.taskalarm.R
import ru.infern.taskalarm.task.ExpressionActivity

/**
 * BroadcastReceiver для обработки сигнала будильника.
 *
 */
class AlarmReceiver : BroadcastReceiver() {

    companion object {
        private const val NOTIFICATION_ID = 0
        private const val CHANNEL_ID = "alarm_channel_id"
        private const val CHANNEL_NAME = "Alarm Channel"
        private const val DISMISS_ACTION = "DISMISS_NOTIFICATION_ACTION"
    }

    /**
     * Метод, вызываемый при получении сигнала будильника.
     *
     * @param context контекст приложения
     * @param intent полученный интент сигнала будильника
     */
    override fun onReceive(context: Context, intent: Intent) {
        startPlayers(context)

        createNotificationChannel(context)

        val notificationIntent = Intent(context, ExpressionActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("Alarm")
            .setContentText("Wake Up!")
            .setSmallIcon(R.drawable.baseline_access_alarm_24)
            .setContentIntent(pendingIntent)
            .setDeleteIntent(getDismissIntent(context))
            .build()

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    /**
     * Метод для запуска проигрывателей (музыки и вибрации).
     *
     * @param context контекст приложения
     */
    private fun startPlayers(context: Context) {
        val musicPlayer = MusicPlayer.getInstance()
        val vibrationPlayer = VibrationPlayer.getInstance()

        musicPlayer.start(context)
        vibrationPlayer.start(context)
    }

    /**
     * Метод для создания канала уведомлений для будильника.
     *
     * @param context контекст приложения
     */
    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance)
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     * Метод для получения интента отмены уведомления.
     *
     * @param context контекст приложения
     * @return PendingIntent для отмены уведомления
     */
    private fun getDismissIntent(context: Context): PendingIntent {
        val dismissIntent = Intent(context, AlarmReceiver::class.java)
        dismissIntent.action = DISMISS_ACTION
        return PendingIntent.getBroadcast(
            context,
            0,
            dismissIntent,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}
