package project.gb.timer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class TimerViewModel : ViewModel() {

    private var timerJob: Job? = null
    private val _timer = MutableLiveData<Int>()
    val timer: LiveData<Int> get() = _timer

    private val _startButtonVisible = MutableLiveData<Boolean>(true)
    val startButtonVisible: LiveData<Boolean> get() = _startButtonVisible

    private val _stopButtonVisible = MutableLiveData<Boolean>(false)
    val stopButtonVisible: LiveData<Boolean> get() = _stopButtonVisible

    fun updateTimer(progress: Int) {
        _timer.value = progress
    }

    fun startTimer() {
        timerJob?.cancel() // Отменяем предыдущий запущенный таймер, если есть
        timerJob = viewModelScope.launch {
            val initialValue = _timer.value ?: return@launch // Получаем начальное значение таймера
            _startButtonVisible.value = false
            _stopButtonVisible.value = true
            for (i in initialValue downTo 0) {
                _timer.value = i
                kotlinx.coroutines.delay(1000)
            }
            _startButtonVisible.value = true
            _stopButtonVisible.value = false
        }
    }

    fun stopTimer() {
        timerJob?.cancel()
        _timer.value = 0
        _startButtonVisible.value = true
        _stopButtonVisible.value = false
    }

    fun updateStartButtonVisibility(visible: Boolean) {
        _startButtonVisible.value = visible
    }

    fun updateStopButtonVisibility(visible: Boolean) {
        _stopButtonVisible.value = visible
    }


    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel() // Отменяем таймер при очистке ViewModel
    }
}


