package ru.infern.taskalarm

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.infern.taskalarm.databinding.ActivityMainBinding
import ru.infern.taskalarm.viewmodels.MainViewModel


const val ADD_ALARM_REQUEST = 1
const val EDIT_ALARM_REQUEST = 2
const val EXTRA_REQUEST = "EXTRA_ALARM"


class MainActivity : AppCompatActivity(), AlarmClickListener {
    private lateinit var vm: MainViewModel

    private lateinit var alarmsRecyclerView: RecyclerView
    private lateinit var addTextView: TextView
    private lateinit var binding: ActivityMainBinding
    private lateinit var alarmModel: AlarmViewModel
    private lateinit var alarmHandler: AlarmHandler

    private val adapter = AlarmAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        vm = ViewModelProvider(this)[MainViewModel::class.java]

        initViews()
        initRecyclerView()
        setListeners()
        alarmModel = ViewModelProvider(this@MainActivity)[AlarmViewModel::class.java]
        alarmHandler = AlarmHandler(this)
        alarmModel.getAlarms()
        alarmModel.alarm.observe(this) { alarms ->
            if (alarms.isNotEmpty()) {
                adapter.setAlarm(alarms)
            }
        }
    }

    // Инициализация пользовательского интерфейса
    private fun initViews() {
        alarmsRecyclerView = binding.alarmsRecyclerView
        val itemTouchHelper = ItemTouchHelper(setTouchHelper())
        itemTouchHelper.attachToRecyclerView(alarmsRecyclerView)
        addTextView = findViewById(R.id.add_alarm_header_textView)
    }

    // Инициализация RecyclerView
    private fun initRecyclerView() {
        alarmsRecyclerView.layoutManager = LinearLayoutManager(this)
        alarmsRecyclerView.adapter = adapter
        adapter.setAlarmClickListener(this)
    }

    // Назначение обработчиков событий
    private fun setListeners() {
        addTextView.setOnClickListener {
            val intent = Intent(this@MainActivity, AddEditAlarmActivity::class.java)
            intent.putExtra("requestCode", ADD_ALARM_REQUEST)
            startActivityForResult(intent, ADD_ALARM_REQUEST)
        }
    }

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
                alarmModel.deleteAlarm(alarm)
                adapter.removeAlarm(viewHolder.adapterPosition)
                alarmHandler.cancelAlarm(alarm)
            }
        }
    }

    override fun onUpdateAlarm(alarm: Alarm) {
        alarmModel.updateAlarm(alarm)
        when (alarm.isEnable) {
            true -> alarmHandler.setAlarm(alarm)
            false -> alarmHandler.cancelAlarm(alarm)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val alarm = data?.extras?.getSerializable(EXTRA_REQUEST) as? Alarm
        if (requestCode == ADD_ALARM_REQUEST && resultCode == Activity.RESULT_OK) {
            alarm?.let {
                alarmModel.saveAlarm(alarm)
                adapter.addAlarm(alarm)
                alarmHandler.setAlarm(alarm)
            }
        } else if (requestCode == EDIT_ALARM_REQUEST && resultCode == Activity.RESULT_OK) {
            alarm?.let {
                alarmModel.updateAlarm(alarm)
                adapter.addAlarm(alarm)
                if (alarm.isEnable) {
                    alarmHandler.cancelAlarm(alarm)
                    alarmHandler.setAlarm(alarm)
                }
            }
        }
    }

    override fun onItemClick(alarm: Alarm) {
        val intent = Intent(this@MainActivity, AddEditAlarmActivity::class.java)
        intent.putExtra("requestCode", EDIT_ALARM_REQUEST)
        intent.putExtra("alarm", alarm)
        startActivityForResult(intent, EDIT_ALARM_REQUEST)
    }
}
