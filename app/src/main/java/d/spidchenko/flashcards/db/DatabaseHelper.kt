package d.spidchenko.flashcards.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import d.spidchenko.flashcards.data.Word
import java.util.function.Consumer

class DatabaseHelper private constructor(context: Context) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION), DatabaseHandler {
    override fun onCreate(db: SQLiteDatabase) {
        val createWords = "CREATE TABLE " + TABLE_WORDS + "(" +
                KEY_ID + " INTEGER PRIMARY KEY," +
                KEY_RATE + " INTEGER, " +
                KEY_RU_WORD + " TEXT UNIQUE," +
                KEY_PL_WORD + " TEXT UNIQUE);"
        db.execSQL(createWords)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DELETE FROM $TABLE_WORDS")
    }

    override fun addWord(word: Word) {
//        Log.d(TAG, "addWord: " + word.getRuWord());
        try {
            writableDatabase.use { db ->
                val values = ContentValues()
                values.put(KEY_RATE, word.memoryRate)
                values.put(KEY_PL_WORD, word.plWord)
                values.put(KEY_RU_WORD, word.ruWord)
                db.insertWithOnConflict(TABLE_WORDS, null, values, SQLiteDatabase.CONFLICT_ABORT)
            }
        } catch (e: SQLiteConstraintException) {
            Log.d(TAG, "addWord: word{" + word.ruWord + "} already in DB")
        } catch (e: Exception) {
            Log.d(TAG, "addWord: Exception $e")
        }
    }

    override fun updateWord(word: Word) {
        try {
            writableDatabase.use { db ->
                val values = ContentValues()
                values.put(KEY_RATE, word.memoryRate)
                values.put(KEY_RU_WORD, word.ruWord)
                values.put(KEY_PL_WORD, word.plWord)
                val result = db.update(
                    TABLE_WORDS, values, "$KEY_ID =?", arrayOf(word.id.toString())
                )
                Log.d(TAG, "updateWord: Number of rows affected =$result")
            }
        } catch (e: Exception) {
            Log.d(TAG, "setRate: $e")
        }
    }

    override fun getAllWords(): List<Word>{
            val words = mutableListOf<Word>()
            try {
                readableDatabase.use { db ->
                    val cursor: Cursor =
                        db.query(TABLE_WORDS, null, null, null, null, null, KEY_RATE)
                    if (cursor.moveToFirst()) {
                        do {
                            val word = Word(
                                cursor.getInt(0),
                                cursor.getInt(1),
                                cursor.getString(2),
                                cursor.getString(3)
                            )
                            words.add(word)
                        } while (cursor.moveToNext())
                    }
                    cursor.close()
                }
            } catch (e: Exception) {
                Log.d(TAG, "getAllWords: $e")
            }
            return words
        }

    override fun addAllWords(words: List<Word>) {
        words.forEach { word -> this.addWord(word) }
    }

    companion object {
        private var databaseHelper: DatabaseHelper? = null
        private const val TAG = "DatabaseHelper.LOG_TAG"
        const val DB_VERSION = 3
        const val DB_NAME = "FlashCards"
        const val TABLE_WORDS = "words"
        const val KEY_ID = "id"
        const val KEY_RATE = "rate"
        const val KEY_RU_WORD = "ru_word"
        const val KEY_PL_WORD = "pl_word"

        @JvmStatic
        @Synchronized
        fun getInstance(context: Context): DatabaseHelper {
            if (databaseHelper == null) {
                databaseHelper = DatabaseHelper(context.applicationContext)
                //            Log.d(TAG, "getInstance: DBHelper");
            }
            return databaseHelper as DatabaseHelper
        }
    }
}