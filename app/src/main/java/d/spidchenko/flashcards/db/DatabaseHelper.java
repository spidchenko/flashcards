package d.spidchenko.flashcards.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import d.spidchenko.flashcards.Word;

public class DatabaseHelper extends SQLiteOpenHelper implements DatabaseHandler {

    private static DatabaseHelper databaseHelper;

    private static final String TAG = "DatabaseHelper.LOG_TAG";
    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "FlashCards";
    public static final String TABLE_WORDS = "words";
    public static final String KEY_ID = "id";
    public static final String KEY_RATE = "rate";
    public static final String KEY_RU_WORD = "ru_word";
    public static final String KEY_PL_WORD = "pl_word";

    public static synchronized DatabaseHelper getInstance(Context context) {

        if (databaseHelper == null) {
            databaseHelper = new DatabaseHelper(context.getApplicationContext());
//            Log.d(TAG, "getInstance: DBHelper");
        }
        return databaseHelper;
    }

    private DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createWords = "CREATE TABLE " + TABLE_WORDS + "(" +
                KEY_ID + " INTEGER PRIMARY KEY," +
                KEY_RATE + " INTEGER, " +
                KEY_RU_WORD + " TEXT UNIQUE," +
                KEY_PL_WORD + " TEXT UNIQUE);";
        db.execSQL(createWords);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void addWord(Word word) {
//        Log.d(TAG, "addWord: " + word.getRuWord());
        try (SQLiteDatabase db = getWritableDatabase()) {
            ContentValues values = new ContentValues();

            values.put(KEY_RATE, word.getMemoryRate());
            values.put(KEY_PL_WORD, word.getPlWord());
            values.put(KEY_RU_WORD, word.getRuWord());

            db.insert(TABLE_WORDS, null, values);
        } catch (Exception e) {
            Log.d(TAG, "addWord: " + e.toString());
        }
    }

    public void updateWord(Word word) {
        try (SQLiteDatabase db = getWritableDatabase()) {
            ContentValues values = new ContentValues();

            values.put(KEY_RATE, word.getMemoryRate());
            values.put(KEY_RU_WORD, word.getRuWord());
            values.put(KEY_PL_WORD, word.getPlWord());

            int result = db.update(TABLE_WORDS, values, KEY_ID + " =?",
                    new String[]{String.valueOf(word.getId())});

            Log.d(TAG, "updateWord: Number of rows affected =" + result);
        } catch (Exception e) {
            Log.d(TAG, "setRate: " + e);
        }
    }

    @Override
    public ArrayList<Word> getAllWords() {
        ArrayList<Word> words = new ArrayList<>();

        try (SQLiteDatabase db = getWritableDatabase()) {
            Cursor cursor;
            cursor = db.query(TABLE_WORDS, null, null, null, null, null, KEY_RATE);
            if (cursor.moveToFirst()) {
                do {
                    Word word = new Word(
                            cursor.getInt(0),
                            cursor.getInt(1),
                            cursor.getString(2),
                            cursor.getString(3));
                    words.add(word);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            Log.d(TAG, "getAllWords: " + e);
        }
        return words;
    }

    @Override
    public void addAllWords(List<Word> words) {
        words.forEach(this::addWord);
    }
}
