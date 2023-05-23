package ru.infern.taskalarm.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

/**
 * Модель данных для будильника.
 *
 * @property hours часы
 * @property minutes минуты
 * @property name название
 * @property isEnable флаг активации
 * @property id идентификатор
 */
@Entity(tableName = "alarms")
data class Alarm(@ColumnInfo var hours: Int,
            @ColumnInfo var minutes: Int,
            @ColumnInfo var name: String = "",
            @ColumnInfo var isEnable: Boolean = true,
            @ColumnInfo(name = "id") @PrimaryKey
            val id: Int = System.currentTimeMillis().toInt() ) : Serializable