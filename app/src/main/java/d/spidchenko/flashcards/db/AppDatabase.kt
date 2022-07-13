package d.spidchenko.flashcards.db

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import d.spidchenko.flashcards.db.dao.WordDao
import d.spidchenko.flashcards.db.models.Word

@Database(
    version = 4,
    entities = [Word::class],
)
abstract class AppDatabase() : RoomDatabase() {

    abstract fun wordDao(): WordDao


    companion object {

        private const val DB_NAME = "FlashCardsRoom"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance =
                    Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME)
                        .build()
                INSTANCE = instance

                instance
            }
        }

    }
}