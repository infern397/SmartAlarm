package ru.infern.taskalarm

import androidx.room.*
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe

@Dao
interface AlarmDao {

    @Query("SELECT * FROM alarms")
    fun getAlarms(): Maybe<Array<Alarm>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAlarm(users: Alarm): Completable

    @Delete
    fun deleteAlarm(user: Alarm): Completable

    @Update
    fun updateAlarm(user: Alarm): Completable
}