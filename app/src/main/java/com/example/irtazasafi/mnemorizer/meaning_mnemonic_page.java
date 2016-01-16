package com.example.irtazasafi.mnemorizer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;

import java.util.ArrayList;

public class meaning_mnemonic_page extends AppCompatActivity {
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    DataManager globalData;
    Gson gson;

    ArrayList<Mnemonic> mnemonics;

    public Button wordDisplay;
    public Button meaningDisplay;
    public Button iKnew;
    public Button iDidNotKnow;
    public int currWordID;
    public VocabularyWord currentWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meaning_mnemonic_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // initialize variables

        wordDisplay = (Button)findViewById(R.id.wordDisplay);
        meaningDisplay = (Button)findViewById(R.id.meaningDisplay);
        iKnew = (Button)findViewById(R.id.iKnew);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();
        gson = new Gson();

        // initialize global Data and wordid





    }


    protected void onResume() {
        System.out.println("********** onResume in meaningMNEMONICPAGE CALLED");

        globalData = gson.fromJson(preferences.getString("globalData", ""), DataManager.class);
        currWordID = preferences.getInt("wordid",0);
        currentWord = globalData.getWord(currWordID);

        mnemonics = currentWord.getMnemonics();

        // set initial view values

        wordDisplay.setText(currentWord.word);
        meaningDisplay.setText(currentWord.meaning);

        super.onResume();


    }

    public void iKnewThisWord(View v) {

        Intent intent = new Intent(this,single_word_page.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);

        startActivity(intent);
    }

}
