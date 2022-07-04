package d.spidchenko.flashcards.data;

import android.util.Log;

import androidx.annotation.NonNull;

public class Word {

    public enum State {ORIGINAL, TRANSLATED}

    private static final String TAG = "Word.LOG_TAG";
    private int id;
    private int mMemoryRate;
    private final String mRuWord;
    private final String mPlWord;

    public Word(String mRuWord, String mPlWord) {
        this.mRuWord = mRuWord;
        this.mPlWord = mPlWord;
    }

    public Word(int id, int mMemoryRate, String mRuWord, String mPlWord){
        this(mRuWord, mPlWord);
        this.mMemoryRate = mMemoryRate;
        this.id = id;
//        Log.d(TAG, "Word: " + this);
    }

    public String getRuWord() {
        return mRuWord;
    }

    public String getPlWord() {
        return mPlWord;
    }

    public int getMemoryRate() {
        return mMemoryRate;
    }

    public void setMemoryRate(int mMemoryRate) {
        this.mMemoryRate = mMemoryRate;
    }

    public int getId() {
        return id;
    }

    @NonNull
    @Override
    public String toString() {
        return "Word{" +
                "mMemoryRate=" + mMemoryRate +
                ", mRuWord='" + mRuWord + '\'' +
                ", mPlWord='" + mPlWord + '\'' +
                '}';
    }
}
