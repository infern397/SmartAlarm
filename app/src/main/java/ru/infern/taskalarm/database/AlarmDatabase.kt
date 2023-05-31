package ru.infern.taskalarm.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * База данных, содержащая таблицу будильников.
 *
 */
@Database(entities = [Alarm::class], version = 2, exportSchema = false)
abstract class AlarmDatabase : RoomDatabase() {

    /**
     * Получение объекта доступа к данным (DAO) для таблицы будильников.
     *
     * @return объект доступа к данным для таблицы будильников
     */
    abstract fun alarmDao(): AlarmDao

    /**
     * Компаньон объект для создания и получения экземпляра базы данных.
     */
    companion object {
        @Volatile
        private var INSTANCE: AlarmDatabase? = null // база данных

        /**
         * Получение экземпляра базы данных.
         *
         * @param context контекст приложения
         * @return экземпляр базы данных
         */
        fun getDatabase(context: Context): AlarmDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            return synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AlarmDatabase::class.java,
                    "app_database"
                )
                    .createFromAsset("alarm.db")
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
