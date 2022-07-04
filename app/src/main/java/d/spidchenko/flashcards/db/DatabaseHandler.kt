package d.spidchenko.flashcards.db;

import java.util.ArrayList;
import java.util.List;

import d.spidchenko.flashcards.data.Word;

public interface DatabaseHandler {

    void addWord(Word word);

    void updateWord(Word word);

    ArrayList<Word> getAllWords();

    void addAllWords(List<Word> words);
}
