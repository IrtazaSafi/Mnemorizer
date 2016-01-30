package com.example.irtazasafi.mnemorizer;

import android.app.ListActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;

import com.google.gson.Gson;

public class main_deck_page extends ListActivity {

    public SharedPreferences preferences;
    public static String decks[] = {"No Mnemonics","English Mnemonics","Native Language Mnemonics","Sign Out"};
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main_deck_page);

        //this.overridePendingTransition(R.anim.slide_right,R.anim.slide_left);

        // fetchData();
//        preferences = PreferenceManager.getDefaultSharedPreferences(this);
//
//        Gson gson = new Gson();
//
//        String returnedData = preferences.getString("globalData","");
//        DataManager returned = gson.fromJson(returnedData, DataManager.class);
//
//        System.out.println(returned.email);

        ArrayAdapter<String> listMaker = new mainDeckPageListAdapter(this,decks,PreferenceManager.getDefaultSharedPreferences(this));
        setListAdapter(listMaker);

    }

    protected void onStart() {

        System.out.println("****************************************************** ON START CALLED IN MAIN DECK PAGE ");

        super.onStart();
    }

    protected void onDestroy() {

        System.out.println("***************** ***********************************ON DESTROY CALLED IN MAIN DECK PAGE ");


        super.onDestroy();
    }

    protected void onPause() {
        System.out.println("***************** ***********************************ON PAUSE CALLED IN MAIN DECK PAGE ");
        super.onPause();


    }

    @Override
    public void onBackPressed() {

    }



}
