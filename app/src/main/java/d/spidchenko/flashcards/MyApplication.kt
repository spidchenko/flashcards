package d.spidchenko.flashcards

import android.app.Application
import d.spidchenko.flashcards.db.DatabaseHelper
import d.spidchenko.flashcards.tts.VoiceSynthesizer

class MyApplication : Application() {
    val databaseHelper: DatabaseHelper
        get() = DatabaseHelper.getInstance(this)
    val voiceSynthesizer: VoiceSynthesizer
        get() = VoiceSynthesizer.getInstance(this)
}