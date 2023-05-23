package ru.infern.taskalarm.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.infern.taskalarm.database.Alarm
import ru.infern.taskalarm.databinding.AlarmItemBinding


/**
 * Адаптер для списка будильников
 *
 */
class AlarmAdapter : RecyclerView.Adapter<AlarmAdapter.AlarmHolder>() {
    private val alarmList = ArrayList<Alarm>() // Список будильников
    private var alarmInteractionListener:
            AlarmInteractionListener? = null // слушатель взаимодействия с списком

    /**
     * ViewHolder для элемента списка будильников.
     *
     * @property binding привязка к элементу списка
     */
    inner class AlarmHolder(private val binding: AlarmItemBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        /**
         * привязка данных к элементу списка
         *
         * @param alarm будильник для привязки
         */
        fun bind(alarm: Alarm) {
            val formattedHours = if (alarm.hours < 10) "0${alarm.hours}" else alarm.hours.toString()
            val formattedMinutes = if (alarm.minutes < 10) "0${alarm.minutes}" else alarm.minutes.toString()
            val formattedTime = "$formattedHours:$formattedMinutes"
            binding.alarmTimeList.text = formattedTime
            binding.alarmNameList.text = alarm.name
            binding.alarmSwitch.isChecked = alarm.isEnable

            binding.alarmSwitch.setOnCheckedChangeListener { _, isChecked ->
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val changedAlarm = alarmList[position]
                    changedAlarm.isEnable = isChecked
                    alarmInteractionListener?.onUpdateAlarm(changedAlarm)
                }
            }
        }

        init {
            itemView.setOnClickListener(this)
        }

        /**
         * обработка нажатия на элемент списка.
         *
         * @param view нажатый элемент списка
         */
        override fun onClick(view: View) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val alarm = alarmList[position]
                alarmInteractionListener?.onItemClick(alarm)
            }
        }
    }

    /**
     * создание элемента списка
     *
     * @param parent родительская ViewGroup
     * @param viewType тип представления элемента списка
     * @return объект AlarmHolder, содержащий представление элемента списка
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmHolder {
        val binding = AlarmItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AlarmHolder(binding)
    }

    /**
     * геттер кол-ва элементов
     *
     * @return кол-во элементов
     */
    override fun getItemCount(): Int {
        return alarmList.size
    }

    /**
     * привязка данных конкретного элемента к элементу списка
     *
     * @param holder объект, содержащий представление элемента списка
     * @param position позиция элемента в списке
     */
    override fun onBindViewHolder(holder: AlarmHolder, position: Int) {
        val alarm = alarmList[position]
        holder.bind(alarm)
    }

    /**
     * добавление будильника
     *
     * @param alarm добавляемый будильник
     */
    fun addAlarm(alarm: Alarm) {
        alarmList.add(alarm)
        notifyItemInserted(alarmList.size - 1)
    }

    /**
     * удаление будильника
     *
     * @param position позиция удаляемого будильника
     */
    fun removeAlarm(position: Int) {
        alarmList.removeAt(position)
        notifyDataSetChanged()
    }

    /**
     * загрузка будильников
     *
     * @param list список будильников
     */
    fun setAlarm(list: Array<Alarm>) {
        alarmList.clear()
        alarmList.addAll(list)
        notifyDataSetChanged()
    }

    /**
     * получение будильника
     *
     * @param position позиция будильника в списке
     * @return будильник
     */
    fun getAlarm(position: Int) : Alarm {
        return alarmList[position]
    }

    /**
     * установка слушателя нажатий

     *
     * @param слушатель нажатий
     */
    fun setAlarmClickListener(listener: AlarmInteractionListener) {
        alarmInteractionListener = listener
    }
}