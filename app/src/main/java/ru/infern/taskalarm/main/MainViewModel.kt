package ru.infern.taskalarm.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.infern.taskalarm.alarmsignal.AlarmHandler
import ru.infern.taskalarm.database.Alarm
import ru.infern.taskalarm.database.AlarmDatabase
import ru.infern.taskalarm.database.AlarmRepository

/**
 * ViewModel для главной активити.
 *
 * @param application ссылка на объект Application
 */
class MainViewModel(application: Application): AndroidViewModel(application) {
    private val compositeDisposable = CompositeDisposable() //

    private val _alarm = MutableLiveData<Array<Alarm>>() //
    var alarm: LiveData<Array<Alarm>> = _alarm //

    private var alarmRepository: AlarmRepository =
        AlarmRepository(AlarmDatabase.getDatabase(application).alarmDao()) //

    private var alarmHandler: AlarmHandler = AlarmHandler(application) //

    /**
     * Установка сигнала для будильника.
     *
     * @param alarm данные будильника
     */
    fun setAlarmSignal(alarm: Alarm) {
        alarmHandler.setAlarm(alarm)
    }

    /**
     * Отмена сигнала будильника.
     *
     * @param alarm данные будильника
     */
    fun cancelAlarmSignal(alarm: Alarm) {
        alarmHandler.cancelAlarm(alarm)
    }

    /**
     * Обновление сигнала будильника
     *
     * @param alarm данные будильника
     */
    private fun updateAlarmSignal(alarm: Alarm) {
        cancelAlarmSignal(alarm)
        setAlarmSignal(alarm)
    }

    /**
     * Получение списка будильников из базы данных.
     *
     */
    fun getAlarmsFromDatabase() {
        alarmRepository.getAlarm()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                it.sortBy { alarmItem: Alarm ->  alarmItem.hours * 60 + alarmItem.minutes }
                if (it.isNotEmpty()) {
                    _alarm.postValue(it)
                } else {
                    _alarm.postValue(arrayOf())
                }
            }, {
            }).let {
                compositeDisposable.add(it)
            }
    }

    /**
     * Метод onCleared вызывается при уничтожении ViewModel.
     * Здесь происходит очистка ресурсов и отмена всех подписок.
     */
    override fun onCleared() {
        compositeDisposable.dispose()
        compositeDisposable.clear()
        super.onCleared()
    }

    /**
     * Обновление данных будильника в базе данных.
     *
     * @param alarm обновленные данные будильника
     */
    fun updateAlarmInDatabase(alarm: Alarm) {
        alarmRepository.updateAlarm(alarm)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                getAlarmsFromDatabase()
            }, {
            }).let {
                compositeDisposable.add(it)
            }
    }

    /**
     * Сохранение данных будильника в базе данных.
     *
     * @param alarm данные будильника
     */
    private fun saveAlarmInDatabase(alarm: Alarm) {
        alarmRepository.insertAlarm(alarm)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                getAlarmsFromDatabase()
            }, {
            }).let {
                compositeDisposable.add(it)
            }
    }

    /**
     * Удаление данных будильника из базы данных.
     *
     * @param alarm данные будильника
     */
    private fun deleteAlarmInDatabase(alarm: Alarm) {
        alarmRepository.deleteAlarm(alarm)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                getAlarmsFromDatabase()
            }, {
            }).let {
                compositeDisposable.add(it)
            }
    }

    /**
     * Добавление нового будильника.
     *
     * @param alarm данные будильника
     */
    fun addAlarm(alarm: Alarm) {
        saveAlarmInDatabase(alarm)
        setAlarmSignal(alarm)
    }

    /**
     * Обновление существующего будильника.
     *
     * @param alarm обновленные данные будильника
     */
    fun updateAlarm(alarm: Alarm) {
        updateAlarmInDatabase(alarm)
        updateAlarmSignal(alarm)
    }

    /**
     * Удаление будильника.
     *
     * @param alarm данные будильника
     */
    fun deleteAlarm(alarm: Alarm) {
        deleteAlarmInDatabase(alarm)
        cancelAlarmSignal(alarm)
    }
}