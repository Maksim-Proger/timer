package project.gb.timer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import project.gb.timer.databinding.FragmentTimerBinding

class TimerFragment : Fragment() {

    private val viewModel: TimerViewModel by viewModels()
    private var _binding: FragmentTimerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTimerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Настройка слушателя изменений в SeekBar
        binding.progressBar.max = 100
        binding.seekBar.max = 100
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.indicatorTimer.max = progress
                binding.indicatorTimer.progress = progress

                binding.textViewTimer.text = progress.toString()
                viewModel.updateTimer(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Настройка слушателей для кнопок
        binding.buttonStart.setOnClickListener {
            viewModel.startTimer()
        }

        binding.buttonStop.setOnClickListener {
            viewModel.stopTimer()
        }

        // Наблюдение за LiveData для таймера
        viewModel.timer.observe(viewLifecycleOwner, Observer { time ->
            binding.textViewTimer.text = time.toString()
            val progressPercentage = (time * 100 / binding.seekBar.max)
            binding.indicatorTimer.progress = progressPercentage
        })

        // Наблюдение за LiveData для видимости кнопок
        viewModel.startButtonVisible.observe(viewLifecycleOwner, Observer { visible ->
            binding.buttonStart.visibility = if (visible) View.VISIBLE else View.INVISIBLE
        })

        viewModel.stopButtonVisible.observe(viewLifecycleOwner, Observer { visible ->
            binding.buttonStop.visibility = if (visible) View.VISIBLE else View.INVISIBLE
        })

        // Восстановление состояния кнопок при повороте экрана
        if (savedInstanceState != null) {
            val startButtonVisible = savedInstanceState.getBoolean("start_button_visible", true)
            val stopButtonVisible = savedInstanceState.getBoolean("stop_button_visible", false)
            viewModel.updateStartButtonVisibility(startButtonVisible)
            viewModel.updateStopButtonVisibility(stopButtonVisible)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.timer.value?.let {
            outState.putInt("timer_value", it)
        }
        outState.putBoolean("start_button_visible", binding.buttonStart.visibility == View.VISIBLE)
        outState.putBoolean("stop_button_visible", binding.buttonStop.visibility == View.VISIBLE)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.let { bundle ->
            if (bundle.getBoolean("start_button_visible", true)) {
                viewModel.updateStartButtonVisibility(true)
            } else {
                viewModel.updateStartButtonVisibility(false)
            }
            if (bundle.getBoolean("stop_button_visible", false)) {
                viewModel.updateStopButtonVisibility(true)
            } else {
                viewModel.updateStopButtonVisibility(false)
            }
            savedInstanceState.getInt("timer_value")?.let {
                viewModel.updateTimer(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}