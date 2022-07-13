package d.spidchenko.flashcards.db.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "words", indices = [
        Index(value = arrayOf("ru_word"), unique = true),
        Index(value = arrayOf("pl_word"), unique = true),
    ]
)
data class Word(
    @ColumnInfo(name = "ru_word") val ruWord: String,
    @ColumnInfo(name = "pl_word") val plWord: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "rate") var memoryRate: Int = 0,
) {
    override fun toString(): String {
        return "Word{mMemoryRate=$memoryRate, RuWord='$ruWord', PlWord='$plWord'}"
    }
}