package ru.infern.taskalarm.alarmsignal


import android.content.Context
import android.widget.Toast
import ru.infern.taskalarm.database.Alarm

/**
 * Класс AlarmHandler для управления будильниками.
 *
 * @property context Контекст приложения.
 */
class AlarmHandler(private val context: Context) {

    /**
     * Установить будильник.
     *
     * @param alarm Объект будильника.
     */
    fun setAlarm(alarm: Alarm) {
        val alarmScheduler = AlarmScheduler(context)
        alarmScheduler.scheduleAlarm(alarm)
        showToast("Будильник установлен")
    }

    /**
     * Отменить будильник.
     *
     * @param alarm Объект будильника.
     */
    fun cancelAlarm(alarm: Alarm) {
        val alarmScheduler = AlarmScheduler(context)
        alarmScheduler.cancelAlarm(alarm)
        showToast("Будильник отменен")
    }

    /**
     * Отобразить всплывающее сообщение.
     *
     * @param message Сообщение для отображения.
     */
    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    /**
     * Вспомогательный объект для передачи идентификатора будильника.
     */
    companion object {
        const val EXTRA_ALARM_ID = "extra_alarm_id"
    }
}
