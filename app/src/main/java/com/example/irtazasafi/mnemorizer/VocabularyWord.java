package com.example.irtazasafi.mnemorizer;

import com.google.gson.annotations.Expose;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Irtaza Safi on 1/12/2016.
 */
public class VocabularyWord {

    @Expose public int id;
    @Expose  public int deckid;
    @Expose public String word;
    @Expose public String meaning;
    public  int hitScore;
    @Expose public ArrayList<Mnemonic> mnemonics;

    public VocabularyWord(String word_in,String meaning_in,int wordID_in,int deckID_in) {
        id = wordID_in;
        word = word_in;
        meaning = meaning_in;
        deckid = deckID_in;
        mnemonics = new ArrayList<Mnemonic>();
        hitScore = 1;
    }

    public void changeHitScore(int value) {
        hitScore = hitScore + value;
    }


    public ArrayList<Mnemonic> getMnemonics() {

        return mnemonics;
    }

}
