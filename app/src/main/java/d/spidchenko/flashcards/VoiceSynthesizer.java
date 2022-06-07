package d.spidchenko.flashcards;

import android.app.Application;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.speech.tts.Voice;
import android.util.Log;

import java.util.Locale;

public class VoiceSynthesizer {
    private static final String TAG = "VoiceSynthesizer.LOG_TAG";
    public static final String KEY_UTTERANCE_ID = "42";
    private Application application;
    private final TextToSpeech tts;

    public VoiceSynthesizer(Application application) {
        tts = new TextToSpeech(application, status -> {
            Log.d(TAG, "VoiceSynthesizer: INIT DONE! Status=" + status);
            testSpeech();
        });
    }

    private void testSpeech() {
        tts.setLanguage(new Locale("pl", "PL"));
        Log.d(TAG, "testSpeech: def voice:" + tts.getDefaultVoice());
        int result = tts.speak("interesujący", TextToSpeech.QUEUE_ADD, null, KEY_UTTERANCE_ID);
        Log.d(TAG, "testSpeech: Add to queue result=" + result);
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
