package d.spidchenko.flashcards.tts

import android.content.Context
import android.speech.tts.TextToSpeech
import d.spidchenko.flashcards.tts.VoiceSynthesizer
import android.speech.tts.Voice
import kotlin.jvm.Synchronized
import android.speech.tts.TextToSpeech.OnInitListener
import android.util.Log
import java.util.*

class VoiceSynthesizer private constructor(context: Context) {
    private val tts: TextToSpeech
    fun speak(word: String): Boolean {
        val result = tts.speak(word, TextToSpeech.QUEUE_ADD, null, null)
        Log.d(TAG, "speak: $word result=$result")
        return result == TextToSpeech.SUCCESS
    }

    private fun setup() {
        tts.language = Locale("pl", "PL")
        tts.setSpeechRate(VOICE_SPEED)
        Log.d(TAG, "testSpeech: def voice:" + tts.defaultVoice)
        val currentVoice = tts.voice
        Log.d(
            TAG, """
     testSpeech: Voice: name=${currentVoice.name}
     Locale:${currentVoice.locale}
     Quality:${currentVoice.quality}
     Latency:${currentVoice.latency}
     Need network:${currentVoice.isNetworkConnectionRequired}
     Features${currentVoice.features}
     """.trimIndent()
        )
    }

    companion object {
        private const val TAG = "VoiceSynthesizer.LOG_TAG"
        const val VOICE_SPEED = 0.7f
        private var voiceSynthesizer: VoiceSynthesizer? = null
        @JvmStatic
        @Synchronized
        fun getInstance(context: Context): VoiceSynthesizer? {
            if (voiceSynthesizer == null) {
                voiceSynthesizer = VoiceSynthesizer(context.applicationContext)
            }
            return voiceSynthesizer
        }
    }

    init {
        tts = TextToSpeech(context) { status: Int ->
            Log.d(TAG, "VoiceSynthesizer: INIT DONE! Status=$status")
            setup()
        }
    }
}