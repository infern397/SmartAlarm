package ru.infern.taskalarm

import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ru.infern.taskalarm.databinding.ActivityAddEditAlarmBinding
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Intent
import android.widget.EditText
import android.widget.TimePicker

class AddEditAlarmActivity : AppCompatActivity(), OnTimeSetListener {
    private lateinit var binding: ActivityAddEditAlarmBinding
    private lateinit var timeTextView: TextView
    private lateinit var nameEditText: EditText
    private lateinit var melodyTextView: TextView
    private lateinit var confirmTextView: TextView
    private lateinit var setAlarm: Alarm
    private var requestCode: Int = 0

    var hourOfDay: Int = 0
    var minute: Int = 0

    var savedHourOfDay: Int = 0
    var savedMinute: Int = 0

    var name: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
        setListeners()
        requestCode = getRequestCode()
        if (requestCode == 2) {
            setData()
        }
    }

    private fun initViews() {
        timeTextView = binding.timeTextView
        nameEditText = binding.nameEditText
        melodyTextView = binding.melodyTextView
        confirmTextView = binding.confirmTextView
    }

    private fun setListeners() {
        timeTextView.setOnClickListener {
            TimePickerDialog(this, this, hourOfDay, minute, true).show()
        }
        confirmTextView.setOnClickListener {
            val intent = Intent()
            if (requestCode == 1) {
                val alarmToSent = Alarm(savedHourOfDay, savedMinute, nameEditText.text.toString())
                intent.putExtra("EXTRA_ALARM", alarmToSent)
                setResult(RESULT_OK, intent)
            }
            else if (requestCode == 2) {
                setAlarm.name = nameEditText.text.toString()
                setAlarm.hours = savedHourOfDay
                setAlarm.minutes = savedMinute
                intent.putExtra("EXTRA_ALARM", setAlarm)
                setResult(RESULT_OK, intent)
            }

            finish()
        }
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        savedHourOfDay = hourOfDay
        savedMinute = minute
        timeTextView.text = formatTime(savedHourOfDay, savedMinute)
    }

    private fun formatTime(hour: Int, minute: Int ) : String {
        val formattedHours =
            if (hour < 10) "0${hour}" else hour.toString()
        val formattedMinutes =
            if (minute < 10) "0${minute}" else minute.toString()
        return "$formattedHours:$formattedMinutes"
    }

    private fun getRequestCode(): Int {
        return intent.getIntExtra("requestCode", 0)
    }

    private fun setData() {
        val alarm: Alarm = intent.getSerializableExtra("alarm") as Alarm
        savedHourOfDay = alarm.hours
        savedMinute = alarm.minutes
        timeTextView.text = formatTime(savedHourOfDay, savedMinute)
        nameEditText.setText(alarm.name)
        setAlarm = alarm
    }
}