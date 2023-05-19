package ru.infern.taskalarm

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import ru.infern.taskalarm.databinding.AlarmItemBinding


class AlarmAdapter : RecyclerView.Adapter<AlarmAdapter.AlarmHolder>() {
    private val alarmList = ArrayList<Alarm>()
    private var alarmClickListener: AlarmClickListener? = null

    inner class AlarmHolder(private val binding: AlarmItemBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

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
                    val alarm = alarmList[position]
                    alarm.isEnable = isChecked
                    alarmClickListener?.onUpdateAlarm(alarm)
                }
            }
        }

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val alarm = alarmList[position]
                alarmClickListener?.onItemClick(alarm)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmHolder {
        val binding = AlarmItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AlarmHolder(binding)
    }

    override fun getItemCount(): Int {
        return alarmList.size
    }

    override fun onBindViewHolder(holder: AlarmHolder, position: Int) {
        val alarm = alarmList[position]
        holder.bind(alarm)
    }

    fun addAlarm(alarm: Alarm) {
        alarmList.add(alarm)
        notifyItemInserted(alarmList.size - 1)
    }

    fun removeAlarm(position: Int) {
        alarmList.removeAt(position)
        notifyDataSetChanged()
    }

    fun setAlarm(list: Array<Alarm>) {
        alarmList.clear()
        alarmList.addAll(list)
        notifyDataSetChanged()
    }

    fun getAlarm(position: Int) : Alarm {
        return alarmList[position]
    }

    fun setAlarmClickListener(listener: AlarmClickListener) {
        alarmClickListener = listener
    }
}