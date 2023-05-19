package ru.infern.taskalarm

import android.util.Log
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe

class AlarmRepository(private val alarmDao: AlarmDao) {

    fun getAlarm(): Maybe<Array<Alarm>> {
        return alarmDao.getAlarms()
    }

    fun insertAlarm(alarm: Alarm): Completable {
        return alarmDao.insertAlarm(alarm)
            .onErrorComplete { error ->
                Log.e("AlarmRepository", "Error inserting alarm: ${error.message}")
                true // Возвращаем true, чтобы операция была завершена успешно, не останавливая цепочку операций
            }
    }

    fun updateAlarm(alarm: Alarm): Completable {
        return alarmDao.updateAlarm(alarm)
    }

    fun deleteAlarm(alarm: Alarm): Completable {
        return alarmDao.deleteAlarm(alarm)
    }
}