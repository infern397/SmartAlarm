package ru.infern.taskalarm

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.infern.taskalarm.databinding.ActivityMainBinding


const val ADD_ALARM_REQUEST = 1
const val EDIT_ALARM_REQUEST = 2
const val EXTRA_REQUEST = "EXTRA_ALARM"


class MainActivity : AppCompatActivity(), AlarmClickListener {
    private lateinit var alarmsRecyclerView: RecyclerView
    private lateinit var addTextView: TextView
    private lateinit var binding: ActivityMainBinding
    private lateinit var alarmModel: AlarmViewModel
    private val adapter = AlarmAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
        initRecyclerView()
        setListeners()
        alarmModel = ViewModelProvider(this@MainActivity)[AlarmViewModel::class.java]
        alarmModel.getAlarms()
        alarmModel.alarm.observe(this) {
            if (it.isNotEmpty()) {
                adapter.setAlarm(it)
            }
        }
    }

    private fun initViews() {
        alarmsRecyclerView = binding.alarmsRecyclerView
        val itemTouchHelper = ItemTouchHelper(setTouchHelper())
        itemTouchHelper.attachToRecyclerView(alarmsRecyclerView)
        addTextView = findViewById(R.id.add_alarm_header_textView)
    }

    private fun initRecyclerView() {
        alarmsRecyclerView.layoutManager = LinearLayoutManager(this)
        alarmsRecyclerView.adapter = adapter
        adapter.setAlarmClickListener(this)
    }

    private fun setListeners() {
        addTextView.setOnClickListener {
            val intent = Intent(this@MainActivity, AddEditAlarmActivity::class.java)
            intent.putExtra("requestCode", ADD_ALARM_REQUEST)
            startActivityForResult(intent, ADD_ALARM_REQUEST)
        }
    }

    private fun setTouchHelper(): ItemTouchHelper.SimpleCallback {
        val itemTouchHelper = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                alarmModel.deleteAlarm(adapter.getAlarm(viewHolder.adapterPosition))
                adapter.removeAlarm(viewHolder.adapterPosition)
            }
        }
        return itemTouchHelper
    }

    override fun onUpdateAlarm(alarm: Alarm) {
        alarmModel.updateAlarm(alarm)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val alarm = data?.extras?.getSerializable(EXTRA_REQUEST) as? Alarm
        if (requestCode == ADD_ALARM_REQUEST && resultCode == Activity.RESULT_OK) {
            alarm?.let {
                alarmModel.saveAlarm(alarm)
                adapter.addAlarm(alarm)
            }
        } else if (requestCode == EDIT_ALARM_REQUEST && resultCode == Activity.RESULT_OK) {
            alarm?.let {
                alarmModel.updateAlarm(alarm)
                adapter.addAlarm(alarm)
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

