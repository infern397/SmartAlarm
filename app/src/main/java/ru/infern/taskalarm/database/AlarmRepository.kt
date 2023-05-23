package ru.infern.taskalarm.database

import android.util.Log
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe

/**
 * Репозиторий для взаимодействия с таблицей будильников в базе данных.
 *
 * @property alarmDao объект доступа к данным (DAO) для таблицы будильников
 */
class AlarmRepository(private val alarmDao: AlarmDao) {

    /**
     * Получение списка всех будильников из базы данных.
     *
     * @return список всех будильников в виде RxJava Maybe
     */
    fun getAlarm(): Maybe<Array<Alarm>> {
        return alarmDao.getAlarms()
    }

    /**
     * Вставка нового будильника в базу данных или обновление существующего.
     *
     * @param alarm будильник для вставки или обновления
     * @return Completable для обозначения успешного завершения операции
     */
    fun insertAlarm(alarm: Alarm): Completable {
        return alarmDao.insertAlarm(alarm)
            .onErrorComplete { error ->
                Log.e("AlarmRepository", "Error inserting alarm: ${error.message}")
                true // Возвращаем true, чтобы операция была завершена успешно, не останавливая цепочку операций
            }
    }

    /**
     * Обновление существующего будильника в базе данных.
     *
     * @param alarm будильник для обновления
     * @return Completable для обозначения успешного завершения операции
     */
    fun updateAlarm(alarm: Alarm): Completable {
        return alarmDao.updateAlarm(alarm)
    }

    /**
     * Удаление существующего будильника из базы данных.
     *
     * @param alarm будильник для удаления
     * @return Completable для обозначения успешного завершения операции
     */
    fun deleteAlarm(alarm: Alarm): Completable {
        return alarmDao.deleteAlarm(alarm)
    }
}