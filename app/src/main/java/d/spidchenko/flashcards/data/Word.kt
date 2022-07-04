package d.spidchenko.flashcards.data

class Word(val ruWord: String, val plWord: String) {
    enum class State {
        ORIGINAL, TRANSLATED
    }

    var id = 0
        private set
    var memoryRate = 0

    constructor(id: Int, mMemoryRate: Int, mRuWord: String, mPlWord: String) : this(
        mRuWord,
        mPlWord
    ) {
        memoryRate = mMemoryRate
        this.id = id
        //        Log.d(TAG, "Word: " + this);
    }

    override fun toString(): String {
        return "Word{mMemoryRate=$memoryRate, mRuWord='$ruWord', mPlWord='$plWord'}"
    }

    companion object {
        private const val TAG = "Word.LOG_TAG"
    }
}