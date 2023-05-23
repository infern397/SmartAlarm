package ru.infern.taskalarm.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.infern.taskalarm.database.Alarm

/**
 * Класс ViewModel для activity установки данных будильника
 *
 * @constructor
 * реализован через фабрику AddEditViewModelFactory
 *
 * @param alarm стартовый будильник
 */
class AddEditViewModel(alarm: Alarm) : ViewModel() {
    private val alarmLiveData = MutableLiveData(alarm)

    /**
     * Метод для получения форматированной строки времени
     *
     * @param hour часы
     * @param minute минуты
     * @return форматированная строка
     */
    fun formatTime(hour: Int, minute: Int): String {
        val formattedHours =
            if (hour < 10) "0${hour}" else hour.toString()
        val formattedMinutes =
            if (minute < 10) "0${minute}" else minute.toString()
        return "$formattedHours:$formattedMinutes"
    }

    /**
     * Метод для получения LiveData будильника
     *
     * @return лайвдата будильника
     */
    fun getAlarmLiveData(): LiveData<Alarm> {
        return alarmLiveData
    }

    /**
     * Метод для установки имени будильника
     *
     * @param имя
     */
    fun setAlarmName(name: String) {
        val currentAlarm = alarmLiveData.value ?: return
        if (currentAlarm.name != name) {
            currentAlarm.name = name
            alarmLiveData.postValue(currentAlarm)
        }
    }

    /**
     * Метод для установки времени будильника
     *
     * @param hour часы
     * @param minute минуты
     */
    fun setAlarmTime(hour: Int, minute: Int) {
        val currentAlarm = alarmLiveData.value ?: return
        currentAlarm.hours = hour
        currentAlarm.minutes = minute
        alarmLiveData.postValue(currentAlarm)
    }
}