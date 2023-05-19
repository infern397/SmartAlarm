package ru.infern.taskalarm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class AlarmViewModel(application: Application) : AndroidViewModel(application) {

    private val compositeDisposable = CompositeDisposable()

    private val _alarm = MutableLiveData<Array<Alarm>>()
    var alarm: LiveData<Array<Alarm>> = _alarm

    private var alarmRepository: AlarmRepository =
        AlarmRepository(AlarmDatabase.getDatabase(application).alarmDao())

    fun getAlarms() {
        alarmRepository.getAlarm()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                it.sortBy { alarmItem: Alarm ->  alarmItem.hours * 60 + alarmItem.minutes }
                if (!it.isNullOrEmpty()) {
                    _alarm.postValue(it)
                } else {
                    _alarm.postValue(arrayOf())
                }
            }, {
            }).let {
                compositeDisposable.add(it)
            }
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        compositeDisposable.clear()
        super.onCleared()
    }

    fun updateAlarm(alarm: Alarm) {
        alarmRepository.updateAlarm(alarm)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                getAlarms()
            }, {
            }).let {
                compositeDisposable.add(it)
            }
    }

    fun saveAlarm(alarm: Alarm) {
        alarmRepository.insertAlarm(alarm)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                getAlarms()
            }, {
            }).let {
                compositeDisposable.add(it)
            }
    }

    fun deleteAlarm(alarm: Alarm) {
        alarmRepository.deleteAlarm(alarm)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                getAlarms()
            }, {
            }).let {
                compositeDisposable.add(it)
            }
    }
}