package d.spidchenko.flashcards;

import android.app.Application;

import d.spidchenko.flashcards.db.DatabaseHelper;
import d.spidchenko.flashcards.tts.VoiceSynthesizer;

public class MyApplication extends Application {

    public DatabaseHelper getDatabaseHelper() {
        return DatabaseHelper.getInstance(this);
    }

    public VoiceSynthesizer getVoiceSynthesizer() {
        return VoiceSynthesizer.getInstance(this);
    }
}
