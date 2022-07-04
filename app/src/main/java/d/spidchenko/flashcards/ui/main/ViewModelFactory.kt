package d.spidchenko.flashcards.ui.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import d.spidchenko.flashcards.MyApplication;
import d.spidchenko.flashcards.db.DatabaseHelper;
import d.spidchenko.flashcards.tts.VoiceSynthesizer;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private Application application;
    private final VoiceSynthesizer voiceSynthesizer;
    private final DatabaseHelper databaseHelper;

    public ViewModelFactory(Application application) {
        databaseHelper = ((MyApplication) application).getDatabaseHelper();
        voiceSynthesizer = ((MyApplication) application).getVoiceSynthesizer();
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MainViewModel(databaseHelper , voiceSynthesizer);
    }
}
