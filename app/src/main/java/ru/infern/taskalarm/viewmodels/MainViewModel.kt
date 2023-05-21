package ru.infern.taskalarm.viewmodels

import android.content.Context
import android.content.Intent
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.lifecycle.ViewModel
import ru.infern.taskalarm.ADD_ALARM_REQUEST
import ru.infern.taskalarm.AddEditAlarmActivity
import ru.infern.taskalarm.MainActivity

class MainViewModel(context: Context): ViewModel() {

    fun openAddEditAlarmActivity(context: Context) {
        val intent = Intent(context, AddEditAlarmActivity::class.java)
        intent.putExtra("requestCode", ADD_ALARM_REQUEST)
        startActivityForResult(intent, ADD_ALARM_REQUEST)
    }
}