package d.spidchenko.flashcards.ui.main

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import d.spidchenko.flashcards.tts.VoiceSynthesizer
import d.spidchenko.flashcards.db.DatabaseHelper
import androidx.lifecycle.ViewModel
import d.spidchenko.flashcards.ui.main.MainViewModel
import d.spidchenko.flashcards.MyApplication

class ViewModelFactory(application: Application) : ViewModelProvider.Factory {
    private val voiceSynthesizer: VoiceSynthesizer
    private val databaseHelper: DatabaseHelper
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(databaseHelper, voiceSynthesizer) as T
    }

    init {
        databaseHelper = (application as MyApplication).databaseHelper
        voiceSynthesizer = application.voiceSynthesizer
    }
}