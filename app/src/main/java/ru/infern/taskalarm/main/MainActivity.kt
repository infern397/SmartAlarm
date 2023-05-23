package ru.infern.taskalarm.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.infern.taskalarm.R
import ru.infern.taskalarm.database.Alarm
import ru.infern.taskalarm.edit.AddEditAlarmActivity

/**
 * Главная активити приложения.
 *
 */
class MainActivity : AppCompatActivity(), AlarmInteractionListener {

    private lateinit var vm: MainViewModel // ViewModel

    private lateinit var alarmsRecyclerView: RecyclerView // RecyclerView для списка элементов
    private lateinit var addTextView: TextView // TextView для перехода в активити добавления

    private val adapter = AlarmAdapter() // Адаптер для списка

    /**
     * Метод onCreate вызывается при создании активити.
     *
     * @param savedInstanceState объект Bundle, содержащий состояние активити (если есть)
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Инициализация ViewModel
        vm = ViewModelProvider(this)[MainViewModel::class.java]

        initViews()
        initRecyclerView()
        setListeners()
        initViewModel()
    }

    /**
     * Инициализация элементов интерфейса.
     *
     */
    private fun initViews() {
        alarmsRecyclerView = findViewById(R.id.alarms_recycler_view)
        val itemTouchHelper = ItemTouchHelper(setTouchHelper())
        itemTouchHelper.attachToRecyclerView(alarmsRecyclerView)
        addTextView = findViewById(R.id.add_alarm_header_textView)
    }

    /**
     * Инициализация RecyclerView и его адаптера.
     *
     */
    private fun initRecyclerView() {
        alarmsRecyclerView.layoutManager = LinearLayoutManager(this)
        alarmsRecyclerView.adapter = adapter
        adapter.setAlarmClickListener(this)
    }

    /**
     * Инициализация ViewModel.
     *
     */
    private fun initViewModel() {
        vm.getAlarmsFromDatabase()
        vm.alarm.observe(this) { alarms ->
            if (alarms.isNotEmpty()) {
                adapter.setAlarm(alarms)
            }
        }
    }

    /**
     * Установка слушателей для элементов интерфейса.
     *
     */
    private fun setListeners() {
        addTextView.setOnClickListener {
            val intent = Intent(this@MainActivity, AddEditAlarmActivity::class.java)
            intent.putExtra("requestCode", ADD_ALARM_REQUEST)
            startActivityForResult(intent, ADD_ALARM_REQUEST)
        }
    }

    /**
     * Создание объекта ItemTouchHelper.SimpleCallback для обработки свайпа элементов списка.
     *
     * @return ItemTouchHelper.SimpleCallback
     */
    private fun setTouchHelper(): ItemTouchHelper.SimpleCallback {
        return object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val alarm = adapter.getAlarm(viewHolder.adapterPosition)
                adapter.removeAlarm(viewHolder.adapterPosition)
                vm.deleteAlarm(alarm)
            }
        }
    }

    /**
     * Метод onUpdateAlarm вызывается при обновлении данных будильника.
     *
     * @param alarm обновленные данные будильника
     */
    override fun onUpdateAlarm(alarm: Alarm) {
        vm.updateAlarmInDatabase(alarm)
        when (alarm.isEnable) {
            true -> vm.setAlarmSignal(alarm)
            false -> vm.cancelAlarmSignal(alarm)
        }
    }

    /**
     * Метод onActivityResult вызывается при получении результата от другой активити.
     *
     * @param requestCode  код запроса, который указывался при запуске активити
     * @param resultCode код результата, указывающий успешность операции
     * @param data данные, возвращенные из активити
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val alarm = data?.extras?.getSerializable(EXTRA_REQUEST) as? Alarm
        if (requestCode == ADD_ALARM_REQUEST && resultCode == Activity.RESULT_OK) {
            alarm?.let {
                adapter.addAlarm(alarm)
                vm.addAlarm(alarm)
            }
        } else if (requestCode == EDIT_ALARM_REQUEST && resultCode == Activity.RESULT_OK) {
            alarm?.let {
                adapter.addAlarm(alarm)
                vm.updateAlarm(alarm)
            }
        }
    }

    /**
     * Метод onItemClick вызывается при нажатии на элемент списка будильников.
     *
     * @param alarm выбранный будильник
     */
    override fun onItemClick(alarm: Alarm) {
        val intent = Intent(this@MainActivity, AddEditAlarmActivity::class.java)
        intent.putExtra("requestCode", EDIT_ALARM_REQUEST)
        intent.putExtra("alarm", alarm)
        startActivityForResult(intent, EDIT_ALARM_REQUEST)
    }

    companion object {
        const val ADD_ALARM_REQUEST = 1
        const val EDIT_ALARM_REQUEST = 2
        const val EXTRA_REQUEST = "EXTRA_ALARM"
    }
}
