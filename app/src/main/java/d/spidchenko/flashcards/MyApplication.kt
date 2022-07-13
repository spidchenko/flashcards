package d.spidchenko.flashcards

import android.app.Application
import d.spidchenko.flashcards.db.AppDatabase
import d.spidchenko.flashcards.tts.VoiceSynthesizer

class MyApplication : Application() {
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
    val voiceSynthesizer: VoiceSynthesizer by lazy { VoiceSynthesizer.getInstance(this) }
}