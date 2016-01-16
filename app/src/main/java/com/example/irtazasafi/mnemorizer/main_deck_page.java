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
    public static String decks[] = {"No Mnemonics","English Mnemonics","Native Language Mnemonics","Idiot Mamo(do not click)","does it scroll?","add some more"};
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main_deck_page);

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



}
