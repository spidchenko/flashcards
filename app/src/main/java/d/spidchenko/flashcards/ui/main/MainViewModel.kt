package d.spidchenko.flashcards.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import d.spidchenko.flashcards.data.Word
import d.spidchenko.flashcards.db.DatabaseHelper
import d.spidchenko.flashcards.tts.VoiceSynthesizer


class MainViewModel(
    private val mDatabaseHelper: DatabaseHelper,
    private val mVoiceSynthesizer: VoiceSynthesizer
) : ViewModel() {
    private lateinit var words: ArrayList<Word>
    private var currentIdx = 0
    private lateinit var currentWord: Word
    private val mCurrentTranslation = MutableLiveData<String>()
    private val mIsSpeechSynthesizerEnabled = MutableLiveData<Boolean>()
    val currentTranslation: LiveData<String>
        get() = mCurrentTranslation
    val isSpeechSynthesizerEnabled: LiveData<Boolean>
        get() = mIsSpeechSynthesizerEnabled

    fun nextWord() {
        if (currentIdx > words.size - 1) {
            currentIdx = 0
            words.sortWith(Comparator.comparingInt(Word::memoryRate))
        }
        currentWord = words[currentIdx++]
        Log.d(TAG, "getNextWord: idx=$currentIdx $currentWord")
        mCurrentTranslation.postValue(currentWord.ruWord)
    }

    fun translate() {
        if (mCurrentTranslation.value == currentWord.ruWord) {
            mCurrentTranslation.value = currentWord.plWord
            if (java.lang.Boolean.TRUE == mIsSpeechSynthesizerEnabled.value) {
                mVoiceSynthesizer.speak(currentWord.plWord)
            }
        } else {
            mCurrentTranslation.setValue(currentWord.ruWord)
        }
    }

    fun toggleSpeechSynthesizerState() {
        if (mIsSpeechSynthesizerEnabled.value != null) {
            mIsSpeechSynthesizerEnabled.value = !mIsSpeechSynthesizerEnabled.value!!
        }
    }

    fun increaseRate() {
        var rate = currentWord.memoryRate
        currentWord.memoryRate = ++rate
        updateWord(currentWord)
    }

    fun decreaseRate() {
        var rate = currentWord.memoryRate
        currentWord.memoryRate = --rate
        updateWord(currentWord)
    }

    private fun updateWord(word: Word) {
        Thread {
            mDatabaseHelper.updateWord(word)
            mDatabaseHelper.close()
        }.start()
    }

    private fun getAllWords() {
        Thread {
            val words: List<Word> = mDatabaseHelper.allWords
            mDatabaseHelper.close()
            this.words = ArrayList(words)
            shuffleWordsButSaveRateOrder()
            Log.d(TAG, "getAllWords: Total words in db " + words.size)
            nextWord()
        }.start()
    }

    private fun shuffleWordsButSaveRateOrder() {
        words.shuffle()
        words.sortWith(Comparator.comparingInt(Word::memoryRate))
    }

    private fun saveNewWord(word: Word) {
        Thread {
            mDatabaseHelper.addWord(word)
            Log.d(TAG, "saveNewWord: " + word.ruWord)
        }.start()
    }

    private fun saveAllWords(words: List<Word>) {
        Thread { mDatabaseHelper.addAllWords(words) }.start()
    }

//    private fun initDatabaseWithWords() {
//        val words: MutableList<Word> = Dictionary.
//        var i = 0
//        while (i < translations.length) {
//            if ("" == translations.get(i)) {
//                break
//            }
//            words.add(Word(translations.get(i), translations.get(i + 1)))
//            i += 2
//        }
//        saveAllWords(words)
//    }

    companion object {
        private const val TAG = "MainViewModel.LOG_TAG"
        const val KEY = "words"
    }

    init {
//        initDatabaseWithWords()
        getAllWords()
        mIsSpeechSynthesizerEnabled.value = true
    }
}