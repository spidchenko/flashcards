package d.spidchenko.flashcards.ui.main

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import d.spidchenko.flashcards.MyApplication
import d.spidchenko.flashcards.db.dao.WordDao
import d.spidchenko.flashcards.tts.VoiceSynthesizer

class ViewModelFactory(
    private val voiceSynthesizer: VoiceSynthesizer,
    private val wordDao: WordDao
) : ViewModelProvider.Factory {

//    private val wordDao = AppDatabase.

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(wordDao, voiceSynthesizer) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}