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

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val musicPlayer = MusicPlayer.getInstance()
        val vibrationPlayer = VibrationPlayer.getInstance()

        musicPlayer.start(context)
        vibrationPlayer.start(context)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "alarm_channel_id"
            val channelName = "Alarm Channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, channelName, importance)
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        // Создание уведомления
        val notificationIntent = Intent(context, ExpressionActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context, 0, notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificationBuilder = NotificationCompat.Builder(context, "alarm_channel_id")
            .setContentTitle("Alarm")
            .setContentText("Alarm received!")
            .setSmallIcon(R.drawable.baseline_access_alarm_24)
            .setContentIntent(pendingIntent)
            .setDeleteIntent(getDismissIntent(context))

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notificationBuilder.build())
    }

    private fun getDismissIntent(context: Context): PendingIntent {
        val dismissIntent = Intent(context, AlarmReceiver::class.java)
        dismissIntent.action = "DISMISS_NOTIFICATION_ACTION"
        return PendingIntent.getBroadcast(
            context,
            0,
            dismissIntent,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}
