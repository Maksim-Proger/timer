package project.gb.timer

import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import project.gb.timer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var _binding : ActivityMainBinding? = null
    private val binding get() = _binding!!

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
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // TODO доработать
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // TODO доработать
            }
        })
    }
}