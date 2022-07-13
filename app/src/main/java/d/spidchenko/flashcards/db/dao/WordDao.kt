package d.spidchenko.flashcards.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import d.spidchenko.flashcards.db.models.Word

@Dao
interface WordDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertAll(words: List<Word>)

    @Update
    fun updateWords(vararg word: Word)

    @Delete
    fun delete(word: Word)

    @Query("SELECT * FROM words")
    fun getAllWords(): List<Word>

    @Query("SELECT COUNT(*) FROM words WHERE rate = 0")
    fun getTotalNewWords(): LiveData<Int>
}