package d.spidchenko.flashcards.db

import d.spidchenko.flashcards.data.Word
import java.util.ArrayList

interface DatabaseHandler {
    fun addWord(word: Word?)
    fun updateWord(word: Word?)
    val allWords: ArrayList<Word?>?
    fun addAllWords(words: List<Word?>?)
}