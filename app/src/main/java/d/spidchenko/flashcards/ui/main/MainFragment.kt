package d.spidchenko.flashcards.ui.main

import d.spidchenko.flashcards.ui.main.MainViewModel
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import d.spidchenko.flashcards.R
import d.spidchenko.flashcards.ui.main.ViewModelFactory
import androidx.lifecycle.ViewModelProvider
import android.widget.TextView
import android.widget.ImageButton
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.Button
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import d.spidchenko.flashcards.ui.main.MainFragment

class MainFragment : Fragment() {
    private var mViewModel: MainViewModel? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val factory = ViewModelFactory(requireActivity().getApplication())
        mViewModel = ViewModelProvider(this, factory).get(MainViewModel::class.java)
        val tvWord: TextView = requireActivity().findViewById(R.id.tvCurrentWord)
        mViewModel!!.currentTranslation.observe(viewLifecycleOwner) { text: String? ->
            tvWord.text = text
        }
        tvWord.setOnClickListener { v: View? -> mViewModel!!.translate() }
        val btnShow: Button = requireActivity().findViewById(R.id.btnShow)
        btnShow.setOnClickListener { v: View? -> mViewModel!!.nextWord() }
        val btnIncreaseRate: ImageButton = requireActivity().findViewById(R.id.btnIncreaseRate)
        val btnDecreaseRate: ImageButton = requireActivity().findViewById(R.id.btnDecreaseRate)
        btnIncreaseRate.setOnClickListener { v: View? ->
            mViewModel!!.increaseRate()
            mViewModel!!.nextWord()
        }
        btnDecreaseRate.setOnClickListener { v: View? ->
            mViewModel!!.decreaseRate()
            mViewModel!!.nextWord()
        }
        //        VoiceSynthesizer voice = new VoiceSynthesizer(getActivity().getApplication());
        val btnTTSToggle: ImageButton = requireActivity().findViewById(R.id.btnTTSToggle)
        btnTTSToggle.setOnClickListener { v: View? -> mViewModel!!.toggleSpeechSynthesizerState() }
        mViewModel!!.isSpeechSynthesizerEnabled.observe(viewLifecycleOwner) { isEnabled: Boolean ->
            val icon: Drawable?
            icon = if (isEnabled) {
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