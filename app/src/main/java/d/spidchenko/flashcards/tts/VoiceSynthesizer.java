package d.spidchenko.flashcards.tts;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.util.Log;

import java.util.Locale;

public class VoiceSynthesizer {
    private static final String TAG = "VoiceSynthesizer.LOG_TAG";
    private static VoiceSynthesizer voiceSynthesizer;
    private final TextToSpeech tts;

    public static synchronized VoiceSynthesizer getInstance(Context context) {
        if (voiceSynthesizer == null) {
            voiceSynthesizer = new VoiceSynthesizer(context.getApplicationContext());
        }
        return voiceSynthesizer;
    }

    public boolean speak(String word) {
        int result = tts.speak(word, TextToSpeech.QUEUE_ADD, null, null);
        Log.d(TAG, "speak: " + word + " result=" + result);
        return result == TextToSpeech.SUCCESS;
    }

    private VoiceSynthesizer(Context context) {
        tts = new TextToSpeech(context, status -> {
            Log.d(TAG, "VoiceSynthesizer: INIT DONE! Status=" + status);
            setup();
        });
    }

    private void setup() {
        tts.setLanguage(new Locale("pl", "PL"));
        Log.d(TAG, "testSpeech: def voice:" + tts.getDefaultVoice());
        Voice currentVoice = tts.getVoice();
        Log.d(TAG, "testSpeech: Voice: name=" + currentVoice.getName() +
                "\nLocale:" + currentVoice.getLocale() +
                "\nQuality:" + currentVoice.getQuality() +
                "\nLatency:" + currentVoice.getLatency() +
                "\nNeed network:" + currentVoice.isNetworkConnectionRequired() +
                "\nFeatures" + currentVoice.getFeatures().toString()
        );
    }
}
