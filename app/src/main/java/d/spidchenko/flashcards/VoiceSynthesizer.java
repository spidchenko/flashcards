package d.spidchenko.flashcards;

import android.app.Application;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.speech.tts.Voice;
import android.util.Log;

import java.util.Locale;
import java.util.Set;

public class VoiceSynthesizer extends UtteranceProgressListener {
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
        tts.setLanguage(new Locale("ru", "ru"));
//        tts.set
        int result = tts.speak("Polskiy robot azazazaz", TextToSpeech.QUEUE_ADD, null, KEY_UTTERANCE_ID);
        Log.d(TAG, "testSpeech: Add to queue result=" + result);
//        Set<Voice> availableVoices = tts.getVoices();
//        availableVoices.forEach(voice -> Log.d(TAG, "testSpeech: "+ voice));
//        Log.d(TAG, "testSpeech: Voices: " +tts.getVoices().toString());
        Voice currentVoice = tts.getVoice();
//        Log.d(TAG, "testSpeech: " + tts.getAvailableLanguages());
        Log.d(TAG, "testSpeech: Voice: name="+currentVoice.getName() +
                "\nLocale:" +currentVoice.getLocale() +
                "\nQuality:" + currentVoice.getQuality() +
                "\nLatency:" + currentVoice.getLatency() +
                "\nNeed network:" + currentVoice.isNetworkConnectionRequired() +
                "\nFeatures" + currentVoice.getFeatures().toString()
        );
    }

    @Override
    public void onStart(String utteranceId) {
        Log.d(TAG, "onStart: ");
    }

    @Override
    public void onDone(String utteranceId) {
        Log.d(TAG, "onDone: ");
    }

    @Override
    public void onError(String utteranceId) {
        Log.d(TAG, "onError: ");
    }
}
