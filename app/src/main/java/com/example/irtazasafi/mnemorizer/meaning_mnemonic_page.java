package com.example.irtazasafi.mnemorizer;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

public class meaning_mnemonic_page extends AppCompatActivity {
    public SharedPreferences preferences;
    public SharedPreferences.Editor editor;
    public DataManager globalData = null;
    public Gson gson;

    ArrayList<Mnemonic> mnemonics;

    public Button wordDisplay;
    public Button meaningDisplay;
    public Button iKnew;
    public Button iDidNotKnow;
    public Button leftButton;
    public Button rightButton;
    public Button mnemonicDisplay;
    public Button masteredDisplay;
    public Button rankDisplay;
    public int currWordID;
    public VocabularyWord currentWord;
    public int mnemonicIndex;

    public void showToast(String text) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meaning_mnemonic_page);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        // initialize variables

        wordDisplay = (Button)findViewById(R.id.wordDisplay);
        meaningDisplay = (Button)findViewById(R.id.meaningDisplay);
        iKnew = (Button)findViewById(R.id.iKnew);
        leftButton = (Button)findViewById(R.id.leftButton);
        rightButton = (Button)findViewById(R.id.rightButton);
        mnemonicDisplay = (Button)findViewById(R.id.mnemonicsDisplay);
        masteredDisplay = (Button)findViewById(R.id.masteredDisplay);
        masteredDisplay.setVisibility(View.INVISIBLE);
        rankDisplay = (Button)findViewById(R.id.rankDisplay);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();
        gson = new Gson();
        mnemonicIndex = 0;
        // initialize global Data and wordid





    }


    protected void onResume() {
        System.out.println("********** onResume in meaningMNEMONICPAGE CALLED");

        globalData = gson.fromJson(preferences.getString("globalData", ""), DataManager.class);
        currWordID = preferences.getInt("wordid", 0);
        currentWord = globalData.getWord(currWordID);
        mnemonics = currentWord.getMnemonics();

        if(currentWord.mastered == true) {

            masteredDisplay.setVisibility(View.VISIBLE);

        }

        if (mnemonics.size() == 0){
            mnemonicDisplay.setText("No Mnemonics Available");
            rankDisplay.setText("0");
        } else {
            mnemonicDisplay.setText(mnemonics.get(0).mnemonic);
            rankDisplay.setText("1");
        }

        // set initial view values

        wordDisplay.setText(currentWord.word);
        meaningDisplay.setText(currentWord.meaning);

        super.onResume();


    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    protected void onPause() {

        // save statechanges
        String data = gson.toJson(globalData);
        editor.putString("globalData", data);
        editor.apply();


        super.onPause();
    }

    public void iKnewThisWord(View v) {

        currentWord.changeHitScore(1);
        if(currentWord.hitScore >= 1){

            showToast("Word Mastered !");
            masteredDisplay.setVisibility(View.VISIBLE);
            currentWord.mastered = true;
        }

        Intent intent = new Intent(this,single_word_page.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        startActivity(intent);
    }

    public void iDidNotKnowThisWord(View v) {
        currentWord.hitScore = -2;
        currentWord.mastered = false;
        masteredDisplay.setVisibility(View.INVISIBLE);

        Intent intent = new Intent(this,single_word_page.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    public void rightClicked(View v) {
        if(mnemonics.size() == 0) {
            return;
        }
        int index = Math.abs((mnemonicIndex+1)%mnemonics.size());
        mnemonicIndex = index;
        mnemonicDisplay.setText(mnemonics.get(index).mnemonic);
        rankDisplay.setText(Integer.toString(index + 1));

    }

    public void leftClicked(View v) {
        if(mnemonics.size() == 0) {
            return;
        }
        int index = Math.abs((mnemonicIndex-1)%mnemonics.size());
        mnemonicIndex = index;
        mnemonicDisplay.setText(mnemonics.get(index).mnemonic);
        rankDisplay.setText(Integer.toString(index+1));
    }

    public void addCustomMnemonic(View v) {
        Intent intent = new Intent(this,addCustomMnemonic.class);
        startActivity(intent);
    }



}
