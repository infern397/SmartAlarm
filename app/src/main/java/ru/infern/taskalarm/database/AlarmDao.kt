package ru.infern.taskalarm.database

import androidx.room.*
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe

/**
 * Data Access Object (DAO) для работы с таблицей будильников.
 *
 */
@Dao
interface AlarmDao {

    /**
     * Получение всех будильников.
     *
     * @return список будильников
     */
    @Query("SELECT * FROM alarms")
    fun getAlarms(): Maybe<Array<Alarm>>

    /**
     * Вставка или замена будильника.
     *
     * @param alarm будильник
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAlarm(alarm: Alarm): Completable

    /**
     * Удаление будильника.
     *
     * @param alarm будильник
     */
    @Delete
    fun deleteAlarm(alarm: Alarm): Completable

    /**
     * Обновление будильника.
     *
     * @param alarm будильник
     */
    @Update
    fun updateAlarm(alarm: Alarm): Completable
}