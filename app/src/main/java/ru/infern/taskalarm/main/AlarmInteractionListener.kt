package ru.infern.taskalarm.main

import ru.infern.taskalarm.database.Alarm

/**
 * Интерфейс для взаимодействия с элементами списка будильников.
 *
 */
interface AlarmInteractionListener {
    /**
     * Обновление данных выбранного будильника.
     *
     * @param alarm выбранный будильник
     */
    fun onUpdateAlarm(alarm: Alarm)

    /**
     * Обработка нажатия на элемент списка будильников.
     *
     * @param alarm выбранный будильник
     */
    fun onItemClick(alarm: Alarm)
}