package ru.infern.taskalarm

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "alarms")
data class Alarm(@ColumnInfo var hours: Int,
            @ColumnInfo var minutes: Int,
            @ColumnInfo var name: String = "",
            @ColumnInfo var isEnable: Boolean = true,
            @ColumnInfo(name = "id") @PrimaryKey
            val id: Int? = null, ) : Serializable