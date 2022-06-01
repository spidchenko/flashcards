package d.spidchenko.flashcards.db;

import java.util.ArrayList;

import d.spidchenko.flashcards.Word;

public interface DatabaseHandler {

    void addWord(Word word);

    ArrayList<Word> getAllWords();
}
