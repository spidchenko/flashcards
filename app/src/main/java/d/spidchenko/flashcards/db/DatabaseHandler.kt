package d.spidchenko.flashcards.db

import d.spidchenko.flashcards.data.Word

interface DatabaseHandler {
    fun addWord(word: Word)
    fun updateWord(word: Word)
    fun getAllWords(): List<Word>
    fun addAllWords(words: List<Word>)
}