package d.spidchenko.flashcards.ui.main;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import d.spidchenko.flashcards.Word;
import d.spidchenko.flashcards.db.DatabaseHelper;

public class MainViewModel extends AndroidViewModel {

    private static final String TAG = "MainViewModel.LOG_TAG";
    public static final String KEY = "words";
    private Application application;
    private List<Word> mWords;
    private Word mCurrentWord;
    private MutableLiveData<String> mCurrentTranslation = new MutableLiveData<>();

    public MainViewModel(Application application) {
        super(application);
        this.application = application;
//        initDatabaseWithWords();
        getAllWords();
    }

    LiveData<String> getCurrentTranslation(){
        return mCurrentTranslation;
    }

    public void nextWord() {
        int randomIdx = new Random().nextInt(mWords.size());
        mCurrentWord = mWords.get(randomIdx);
        Log.d(TAG, "getNextWord: idx="+randomIdx);
        mCurrentTranslation.postValue(mCurrentWord.getPlWord());
    }

    public void translateToRussian() {
        mCurrentTranslation.setValue(mCurrentWord.getRuWord());
    }

    private void getAllWords() {
        new Thread(() -> {
            DatabaseHelper db = DatabaseHelper.getInstance(application);
            List<Word> words = db.getAllWords();
            db.close();
            mWords = words;
            Log.d(TAG, "getAllWords: " + words);
            nextWord();
        }).start();
    }

    private void saveNewWord(Word word) {
        new Thread(() -> {
            DatabaseHelper db = DatabaseHelper.getInstance(application);
            db.addWord(word);
            Log.d(TAG, "saveNewWord: " + word.getRuWord());
        }).start();
    }

    private void initDatabaseWithWords() {
        List<Word> words = new ArrayList<>();
        words.add(new Word("старый", "stary"));
        words.add(new Word("новый", "nowy"));
        words.add(new Word("дом", "dom"));
        words.add(new Word("синий", "granatowy"));

        words.forEach(this::saveNewWord);
    }
}