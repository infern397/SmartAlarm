package ru.infern.taskalarm

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe

class AlarmRepository(private val alarmDao: AlarmDao) {

    fun getAlarm(): Maybe<Array<Alarm>> {
        return alarmDao.getAlarms()
    }

    fun insertAlarm(alarm: Alarm): Completable {
        return alarmDao.insertAlarm(alarm)
    }

    fun updateAlarm(alarm: Alarm): Completable {
        return alarmDao.updateAlarm(alarm)
    }

    fun deleteAlarm(alarm: Alarm): Completable {
        return alarmDao.deleteAlarm(alarm)
    }
}