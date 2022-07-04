package d.spidchenko.flashcards.ui.main

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import d.spidchenko.flashcards.R
import d.spidchenko.flashcards.ui.main.MainViewModel

class MainFragment : Fragment() {
    private lateinit var mViewModel: MainViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = ViewModelFactory(requireActivity().application)
        mViewModel = ViewModelProvider(this, factory).get(MainViewModel::class.java)
        val tvWord: TextView = view.findViewById(R.id.tvCurrentWord)
        mViewModel.currentTranslation.observe(viewLifecycleOwner) { text: String? ->
            tvWord.text = text
        }
        tvWord.setOnClickListener { mViewModel.translate() }
        val btnShow: Button = view.findViewById(R.id.btnShow)
        btnShow.setOnClickListener { mViewModel.nextWord() }
        val btnIncreaseRate: ImageButton = view.findViewById(R.id.btnIncreaseRate)
        val btnDecreaseRate: ImageButton = view.findViewById(R.id.btnDecreaseRate)
        btnIncreaseRate.setOnClickListener {
            mViewModel.increaseRate()
            mViewModel.nextWord()
        }
        btnDecreaseRate.setOnClickListener {
            mViewModel.decreaseRate()
            mViewModel.nextWord()
        }
        //        VoiceSynthesizer voice = new VoiceSynthesizer(getActivity().getApplication());
        val btnTTSToggle: ImageButton = view.findViewById(R.id.btnTTSToggle)
        btnTTSToggle.setOnClickListener { mViewModel.toggleSpeechSynthesizerState() }
        mViewModel.isSpeechSynthesizerEnabled.observe(viewLifecycleOwner) { isEnabled: Boolean ->
            val icon = if (isEnabled) {
                AppCompatResources.getDrawable(requireContext(), R.drawable.ic_volume_on)
            } else {
                AppCompatResources.getDrawable(requireContext(), R.drawable.ic_volume_off)
            }
            btnTTSToggle.setImageDrawable(icon)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(): MainFragment {
            return MainFragment()
        }
    }
}