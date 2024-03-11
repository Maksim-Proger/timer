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

        binding.progressBar.max = 100
        binding.seekBar.max = 100
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.progressBar.progress = progress
                binding.indicatorTimer.progress = progress
                binding.textViewTimer.text = progress.toString()
                viewModel.updateTimer(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        binding.buttonStart.setOnClickListener {
            binding.buttonStop.visibility = View.VISIBLE
            binding.buttonStart.visibility = View.INVISIBLE
            viewModel.startTimer()
        }

        binding.buttonStop.setOnClickListener {
            binding.buttonStart.visibility = View.VISIBLE
            binding.buttonStop.visibility = View.INVISIBLE
            viewModel.stopTimer()
        }

        viewModel.timer.observe(viewLifecycleOwner, Observer { time ->
            binding.textViewTimer.text = time.toString()
            val progressPercentage = (time * 100 / binding.seekBar.max)
            binding.progressBar.progress = progressPercentage
            binding.indicatorTimer.progress = progressPercentage
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.timer.value?.let {
            outState.putInt("timer_value", it)
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.getInt("timer_value")?.let {
            viewModel.updateTimer(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


