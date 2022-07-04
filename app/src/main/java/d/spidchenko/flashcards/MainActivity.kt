package d.spidchenko.flashcards

import d.spidchenko.flashcards.ui.main.MainFragment.Companion.newInstance
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import d.spidchenko.flashcards.R
import d.spidchenko.flashcards.ui.main.MainFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, newInstance())
                .commitNow()
        }
    }
}