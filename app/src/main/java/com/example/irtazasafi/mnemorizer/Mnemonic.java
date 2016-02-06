package com.example.irtazasafi.mnemorizer;

import com.google.gson.annotations.Expose;

import java.util.Objects;

/**
 * Created by Irtaza Safi on 1/12/2016.
 */
public class Mnemonic implements Comparable<Mnemonic> {


    @Expose public int id;
    @Expose public String mnemonic;
    @Expose public int wordid;
    @Expose public int score;
    @Expose public int creatorid;
    @Expose public double latitude;
    @Expose public double longitude;

    public boolean liked;

    public Mnemonic(int _ID,String _mnemonic,int _wordid,int _score,int _creatorID,double _latitude, double _longitude){
        id = _ID;
        mnemonic = _mnemonic;
        wordid = _wordid;
        score = _score;
        creatorid = _creatorID;
        latitude = _latitude;
        longitude = _longitude;
        liked = false;
    }

    @Override
    public int compareTo(Mnemonic another) {
        if(this.score == another.score ) {
            return 0;
        } else if(this.score > another.score) {
            return -1;
        } else {
            return 1;
        }
    }
}
