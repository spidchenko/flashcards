package d.spidchenko.flashcards.ui.main

import android.util.Log
import d.spidchenko.flashcards.data.Word.ruWord
import d.spidchenko.flashcards.data.Word.plWord
import d.spidchenko.flashcards.tts.VoiceSynthesizer.speak
import d.spidchenko.flashcards.data.Word.memoryRate
import d.spidchenko.flashcards.db.DatabaseHelper.updateWord
import d.spidchenko.flashcards.db.DatabaseHelper.allWords
import d.spidchenko.flashcards.db.DatabaseHelper.addWord
import d.spidchenko.flashcards.db.DatabaseHelper.addAllWords
import d.spidchenko.flashcards.db.DatabaseHelper
import d.spidchenko.flashcards.tts.VoiceSynthesizer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import d.spidchenko.flashcards.data.Word
import d.spidchenko.flashcards.ui.main.MainViewModel
import java.util.*

class MainViewModel(
    private val mDatabaseHelper: DatabaseHelper,
    private val mVoiceSynthesizer: VoiceSynthesizer
) : ViewModel() {
    private var mWords: ArrayList<Word?>? = null
    private var mCurrentIdx = 0
    private var mCurrentWord: Word? = null
    private val mCurrentTranslation = MutableLiveData<String>()
    private val mIsSpeechSynthesizerEnabled = MutableLiveData<Boolean?>()
    val currentTranslation: LiveData<String>
        get() = mCurrentTranslation
    val isSpeechSynthesizerEnabled: LiveData<Boolean?>
        get() = mIsSpeechSynthesizerEnabled

    fun nextWord() {
        if (mCurrentIdx > mWords!!.size - 1) {
            mCurrentIdx = 0
            mWords!!.sort(Comparator.comparingInt(Word::memoryRate))
        }
        mCurrentWord = mWords!![mCurrentIdx++]
        Log.d(TAG, "getNextWord: idx=$mCurrentIdx $mCurrentWord")
        mCurrentTranslation.postValue(mCurrentWord!!.ruWord)
    }

    fun translate() {
        if (mCurrentTranslation.value == mCurrentWord!!.ruWord) {
            mCurrentTranslation.setValue(mCurrentWord!!.plWord)
            if (java.lang.Boolean.TRUE == mIsSpeechSynthesizerEnabled.value) {
                mVoiceSynthesizer.speak(mCurrentWord!!.plWord)
            }
        } else {
            mCurrentTranslation.setValue(mCurrentWord!!.ruWord)
        }
    }

    fun toggleSpeechSynthesizerState() {
        if (mIsSpeechSynthesizerEnabled.value != null) {
            mIsSpeechSynthesizerEnabled.value = !mIsSpeechSynthesizerEnabled.value!!
        }
    }

    fun increaseRate() {
        if (mCurrentWord != null) {
            var rate = mCurrentWord!!.memoryRate
            mCurrentWord!!.memoryRate = ++rate
            updateWord(mCurrentWord!!)
        }
    }

    fun decreaseRate() {
        if (mCurrentWord != null) {
            var rate = mCurrentWord!!.memoryRate
            mCurrentWord!!.memoryRate = --rate
            updateWord(mCurrentWord!!)
        }
    }

    private fun updateWord(word: Word) {
        Thread {
            mDatabaseHelper.updateWord(word)
            mDatabaseHelper.close()
        }.start()
    }

    private val allWords: Unit
        private get() {
            Thread {
                val words: List<Word?> = mDatabaseHelper.allWords
                mDatabaseHelper.close()
                mWords = ArrayList(words)
                shuffleWordsButSaveRateOrder()
                Log.d(TAG, "getAllWords: Total words in db " + words.size)
                nextWord()
            }.start()
        }

    private fun shuffleWordsButSaveRateOrder() {
        Collections.shuffle(mWords)
        mWords!!.sort(Comparator.comparingInt(Word::memoryRate))
    }

    private fun saveNewWord(word: Word) {
        Thread {
            mDatabaseHelper.addWord(word)
            Log.d(TAG, "saveNewWord: " + word.ruWord)
        }.start()
    }

    private fun saveAllWords(words: List<Word?>) {
        Thread { mDatabaseHelper.addAllWords(words) }.start()
    }

    private fun initDatabaseWithWords() {
        val words: MutableList<Word?> = ArrayList()
        var i = 0
        while (i < translations.length) {
            if ("" == translations.get(i)) {
                break
            }
            words.add(Word(translations.get(i), translations.get(i + 1)))
            i += 2
        }
        saveAllWords(words)
    }

    companion object {
        private const val TAG = "MainViewModel.LOG_TAG"
        const val KEY = "words"
    }

    init {
        initDatabaseWithWords()
        allWords
        mIsSpeechSynthesizerEnabled.value = true
    }
}