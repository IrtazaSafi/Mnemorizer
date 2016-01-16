package com.example.irtazasafi.mnemorizer;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;

import java.util.ArrayList;

public class single_word_page extends AppCompatActivity {
    public SharedPreferences preferences;
    public SharedPreferences.Editor editor;
    public DataManager globalData;
    public Gson gson;
    public ArrayList<VocabularyWord> words;
    public AppCompatButton wordView;
    public Button viewMeaning;
    public int prevIndex;
    public int wordsMastered;
    public int wordsLearning;
    public int currwordID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        System.out.println("ON CREATE OF SINGLE_WORD_PAGE LAUNCHED");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_word_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //initialize views and buttons

        wordView = (AppCompatButton)findViewById(R.id.wordDisplay);
        viewMeaning = (Button)findViewById(R.id.viewMeaning);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();
        gson = new Gson();

        globalData = gson.fromJson(preferences.getString("globalData", ""), DataManager.class);
        int deckid = preferences.getInt("deckid",0);
        words = globalData.getWordsforDeck(deckid);

        System.out.println("********* DECK ID RETURNED IS " + deckid);
        System.out.println("*******SIZE OF WORDS is  " + words.size());

        wordView.setText(words.get(0).word);
        prevIndex = 0;
        currwordID = words.get(0).id;

    }


    protected void onPause() {


        super.onPause();
    }

    protected void onResume() {

        System.out.println("********** onResume in singeWORDPAGE CALLED");
        System.out.println("*********** VALUE OF PREVIOUS INDEX IS " + prevIndex);
        int nextwordIndex = (prevIndex+1)%words.size();
        prevIndex = prevIndex + 1;

        System.out.println("********** VALUE OF NEXT WORD INDEX IS  " + nextwordIndex);
        VocabularyWord nextWord = words.get(nextwordIndex);
        currwordID = nextWord.id;

        wordView.setText(nextWord.word);

        super.onResume();


    }


    protected void onDestroy() {



        super.onDestroy();
    }


    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public void tapToSeeMeaning(View v) {

        Intent intent = new Intent(this,meaning_mnemonic_page.class);
        // pass three things to nextActivity , wordid, globalData

        editor.putInt("wordid",currwordID);
        editor.apply();

        startActivity(intent);


    }

}
