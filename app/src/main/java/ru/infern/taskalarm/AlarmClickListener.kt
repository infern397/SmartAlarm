package ru.infern.taskalarm

interface AlarmClickListener {
    fun onUpdateAlarm(alarm: Alarm)
    fun onItemClick(alarm: Alarm)
}