package ru.infern.taskalarm.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.infern.taskalarm.database.Alarm

/**
 * Фабрика ViewModel для создания экземпляра AddEditViewModel с передачей начального будильника
 *
 * @property alarm начальный будильник, который будет использоваться в создаваемой ViewModel
 */
class AddEditViewModelFactory(val alarm: Alarm) : ViewModelProvider.Factory {
    /**
     * Переопределение метода create из интерфейса ViewModelProvider.Factory для создания ViewModel
     *
     * @param T тип ViewModel
     * @param modelClass класс ViewModel
     * @return экземпляр AddEditViewModel с переданным начальным будильником
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AddEditViewModel(alarm) as T
    }
}