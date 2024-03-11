package project.gb.timer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*

class TimerViewModel : ViewModel() {

    private var timerJob: Job? = null
    private val _timer = MutableLiveData<Int>()
    val timer: LiveData<Int> get() = _timer

    fun updateTimer(progress: Int) {
        _timer.value = progress
    }

    fun startTimer() {
        timerJob?.cancel() // Отменяем предыдущий запущенный таймер, если есть
        timerJob = viewModelScope.launch {
            val initialValue = _timer.value ?: return@launch // Получаем начальное значение таймера
            for (i in initialValue downTo 0) {
                _timer.value = i
                delay(1000)
            }
        }
    }

    fun stopTimer() {
        timerJob?.cancel()
        _timer.value = 0
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel() // Отменяем таймер при очистке ViewModel
    }
}
