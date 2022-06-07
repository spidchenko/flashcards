package d.spidchenko.flashcards.ui.main;

import static d.spidchenko.flashcards.Dictionary.translations;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import d.spidchenko.flashcards.Word;
import d.spidchenko.flashcards.db.DatabaseHelper;

public class MainViewModel extends AndroidViewModel {

    private static final String TAG = "MainViewModel.LOG_TAG";
    public static final String KEY = "words";
    private final Application mApplication;
    private ArrayList<Word> mWords;
    private int mCurrentIdx;
    private Word mCurrentWord;
    private final MutableLiveData<String> mCurrentTranslation = new MutableLiveData<>();
    private MutableLiveData<Boolean> mIsSpeechSynthesizerEnabled = new MutableLiveData<>();


    public MainViewModel(Application application) {
        super(application);
        this.mApplication = application;
        initDatabaseWithWords();
        getAllWords();
        mIsSpeechSynthesizerEnabled.setValue(true);
    }

    LiveData<String> getCurrentTranslation() {
        return mCurrentTranslation;
    }

    LiveData<Boolean> isSpeechSynthesizerEnabled(){
        return mIsSpeechSynthesizerEnabled;
    }

    public void nextWord() {
        if (mCurrentIdx > mWords.size() - 1) {
            mCurrentIdx = 0;
            mWords.sort(Comparator.comparingInt(Word::getMemoryRate));
        }
        mCurrentWord = mWords.get(mCurrentIdx++);
        Log.d(TAG, "getNextWord: idx=" + mCurrentIdx + " " + mCurrentWord);
        mCurrentTranslation.postValue(mCurrentWord.getRuWord());
    }

    public void translate() {
        if (Objects.equals(mCurrentTranslation.getValue(), mCurrentWord.getRuWord())) {
            mCurrentTranslation.setValue(mCurrentWord.getPlWord());
        } else {
            mCurrentTranslation.setValue(mCurrentWord.getRuWord());
        }
    }

    public void toggleSpeechSynthesizerState(){
        if (mIsSpeechSynthesizerEnabled.getValue() != null){
            mIsSpeechSynthesizerEnabled.setValue(!mIsSpeechSynthesizerEnabled.getValue());
        }
    }

    public void increaseRate() {
        if (mCurrentWord != null) {
            int rate = mCurrentWord.getMemoryRate();
            mCurrentWord.setMemoryRate(++rate);
            updateWord(mCurrentWord);
        }
    }

    public void decreaseRate() {
        if (mCurrentWord != null) {
            int rate = mCurrentWord.getMemoryRate();
            mCurrentWord.setMemoryRate(--rate);
            updateWord(mCurrentWord);
        }
    }

    private void updateWord(Word word) {
        new Thread(() -> {
            DatabaseHelper db = DatabaseHelper.getInstance(mApplication);
            db.updateWord(word);
            db.close();
        }).start();
    }

    private void getAllWords() {
        new Thread(() -> {
            DatabaseHelper db = DatabaseHelper.getInstance(mApplication);
            List<Word> words = db.getAllWords();
            db.close();
            mWords = new ArrayList<>(words);
            shuffleWordsButSaveRateOrder();
            Log.d(TAG, "getAllWords: Total words in db " + words.size());
            nextWord();
        }).start();
    }

    private void shuffleWordsButSaveRateOrder(){
        Collections.shuffle(mWords);
        mWords.sort(Comparator.comparingInt(Word::getMemoryRate));
    }

    private void saveNewWord(Word word) {
        new Thread(() -> {
            DatabaseHelper db = DatabaseHelper.getInstance(mApplication);
            db.addWord(word);
            Log.d(TAG, "saveNewWord: " + word.getRuWord());
        }).start();
    }

    private void saveAllWords(List<Word> words) {
        new Thread(() -> {
            DatabaseHelper db = DatabaseHelper.getInstance(mApplication);
            db.addAllWords(words);
//            Log.d(TAG, "saveAllWords: " + words);
        }).start();
    }

    private void initDatabaseWithWords() {
        List<Word> words = new ArrayList<>();
        for (int i = 0; i < translations.length; i += 2) {
//            Log.d(TAG, "initDatabaseWithWords: " +
//                    "ru{" + translations[i] + "} pl{" + translations[i + 1] + "}");
            if ("".equals(translations[i])) {
                break;
            }
            words.add(new Word(translations[i], translations[i + 1]));
        }
        saveAllWords(words);
    }

}