package project.gb.timer

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import project.gb.timer.databinding.FragmentTimerBinding
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch


class TimerFragment : Fragment() {

    private var _binding : FragmentTimerBinding? = null
    private val binding get() = _binding!!
    private var timerValue = 0
    private val handler = Handler(Looper.getMainLooper())
    private var timerRunnable: Runnable? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTimerBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Привязываем SeekBar к ProgressBar
        binding.progressBar.max = 100
        binding.seekBar.max = 100
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.progressBar.progress = progress
                binding.textViewTimer.text = progress.toString()
                // Записываем значение
                timerValue = progress

                binding.indicatorTimer.max = progress
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
        timerRunnable  = object : Runnable {
            override fun run() {
                binding.textViewTimer.text = timerValue.toString()
                val progressPercentage = (timerValue * 100 / binding.seekBar.max)
                binding.indicatorTimer.progress = progressPercentage
                timerValue--
                handler.postDelayed(this, 1000)
            }
        }
        handler.post(timerRunnable!!)
    }

    /**
     * Останавливаем таймер
     */
    private fun stopTimer() {
        timerRunnable?.let { handler.removeCallbacks(it) }
        timerValue = 0
        binding.textViewTimer.text = timerValue.toString()
        binding.progressBar.progress = 0
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}