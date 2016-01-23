package com.example.irtazasafi.mnemorizer;

import android.app.Application;
import android.provider.UserDictionary;

import java.util.ArrayList;

/**
 * Created by Irtaza Safi on 1/12/2016.
 */
public class DataManager {
    public int userID;
    public String email;
    public ArrayList<VocabularyWord> vocabularyWords;
    public String serverURL;
    public DataManager(int _userID,String _email) {
        userID = _userID;
        email = _email;
        vocabularyWords = new ArrayList<VocabularyWord>();
        serverURL = "http://192.168.10.7";
    }

    public ArrayList<VocabularyWord> getWordsforDeck(int _deckID){
        ArrayList<VocabularyWord> output = new ArrayList<VocabularyWord>();

        for (VocabularyWord word : vocabularyWords){
            if(word.deckid == _deckID){
                output.add(word);
            }
        }
        return output;
    }

    public VocabularyWord getWord(int _wordid) {
        for (VocabularyWord word : vocabularyWords){
            if(word.id == _wordid) {
                return word;
            }
        }
        return null;
    }

    public void putMnemonicforWord(int _wordid,Mnemonic mnemonic) {
        for (VocabularyWord word : vocabularyWords) {
            if (word.id == _wordid) {
                word.mnemonics.add(mnemonic);
            }
        }
    }

}
