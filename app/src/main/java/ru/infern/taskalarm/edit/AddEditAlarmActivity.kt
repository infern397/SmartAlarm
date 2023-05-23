package ru.infern.taskalarm.edit

import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ru.infern.taskalarm.databinding.ActivityAddEditAlarmBinding
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TimePicker
import androidx.lifecycle.ViewModelProvider
import ru.infern.taskalarm.database.Alarm

/**
 * Активити изменения данных будильника
 *
 */
class AddEditAlarmActivity : AppCompatActivity(), OnTimeSetListener {
    private lateinit var vm: AddEditViewModel // ViewModel

    private lateinit var binding: ActivityAddEditAlarmBinding // Привязка к элементам интерфейса

    private lateinit var timeTextView: TextView // TextView с временем
    private lateinit var nameEditText: EditText // TextView с названием будильника

    private lateinit var confirmTextView: TextView // TextView подтверждения

    private var requestCode: Int = 0 // код реквеста

    /**
     * Переопределение метода для создания activity
     *
     * @param savedInstanceState состояние
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        setListeners()

        requestCode = getRequestCode()
        vm = if (requestCode == 2) {
            ViewModelProvider(
                this,
                AddEditViewModelFactory(getAlarmFromInit())
            )[AddEditViewModel::class.java]
        } else {
            ViewModelProvider(
                this,
                AddEditViewModelFactory(Alarm(0, 0))
            )[AddEditViewModel::class.java]
        }
        vm.getAlarmLiveData().observe(this) { alarm ->
            timeTextView.text = vm.formatTime(alarm.hours, alarm.minutes)
            nameEditText.setText(alarm.name)
        }
    }

    /**
     * Инициализация объектов интерфейса.
     *
     */
    private fun initViews() {
        timeTextView = binding.timeTextView
        nameEditText = binding.nameEditText

        confirmTextView = binding.confirmTextView
    }

    /**
     * Установка слушателей.
     *
     */
    private fun setListeners() {
        timeTextView.setOnClickListener {
            val currentAlarm = vm.getAlarmLiveData().value ?: return@setOnClickListener
            TimePickerDialog(
                this, this, currentAlarm.hours, currentAlarm.minutes,
                true
            ).show()
        }
        confirmTextView.setOnClickListener {
            val intent = Intent().apply {
                putExtra("EXTRA_ALARM", vm.getAlarmLiveData().value)
            }
            setResult(RESULT_OK, intent)
            finish()
        }
        nameEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Ничего не делаем
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Вызываем метод обновления имени в ViewModel
                vm.setAlarmName(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
                // Ничего не делаем
            }
        })
    }

    /**
     * Переопределение метода для действий после выбора времени.
     *
     * @param view окно выбора времени
     * @param hourOfDay часы
     * @param minute минуты
     */
    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        vm.setAlarmTime(hourOfDay, minute)
    }

    /**
     * Получение requestCode из интента.
     *
     */
    private fun getRequestCode() = intent.getIntExtra("requestCode", 0)

    /**
     * Получение будильника из интента.
     *
     */
    private fun getAlarmFromInit() = intent.getSerializableExtra("alarm") as Alarm
}