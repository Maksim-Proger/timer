package project.gb.timer

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import project.gb.timer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var _binding : ActivityMainBinding? = null
    private val binding get() = _binding!!
    private var timerValue = 0
    private val handler = Handler(Looper.getMainLooper())
    private var timerRunnable: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // TODO надо разобраться как меняется цвет у SeekBar и ProgressBar
        // Привязываем SeekBar к ProgressBar
        binding.progressBar.max = 100
        binding.seekBar.max = 100
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.progressBar.progress = progress
                binding.textViewTimer.text = progress.toString()

                // Записываем значение
                timerValue = progress

            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // TODO доработать
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // TODO доработать
            }
        })

        buttonsListener()
    }

    /**
     * Запускаем таймер
     */
    private fun startTimer(){
        val myRunnable = object : Runnable {
            override fun run() {
                binding.textViewTimer.text = timerValue.toString()
                timerValue--
                if (timerValue >= 0) {
                    handler.postDelayed(this, 1000)
                }
            }
        }
        handler.post(myRunnable)
    }

    /**
     * Останавливаем таймер
     */
    private fun stopTimer() {
        timerRunnable?.let { handler.removeCallbacks(it) }
        timerValue = 0
        binding.textViewTimer.text = timerValue.toString()
    }

    private fun buttonsListener() {

        binding.buttonStart.setOnClickListener {
            binding.buttonStop.isVisible = true
            binding.buttonStart.isVisible = false
            startTimer()
        }

        binding.buttonStop.setOnClickListener {
            binding.buttonStart.isVisible = true
            binding.buttonStop.isVisible = false
            stopTimer()
        }
    }

}