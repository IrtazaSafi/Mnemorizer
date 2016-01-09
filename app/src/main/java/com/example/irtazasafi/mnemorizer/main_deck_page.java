package com.example.irtazasafi.mnemorizer;

import android.app.ListActivity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;

public class main_deck_page extends ListActivity {

    public static String decks[] = {"No Mnemonics","English Mnemonics","Native Language Mnemonics","Idiot Mamo(do not click)","does it scroll?","add some more"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayAdapter<String> listMaker = new mainDeckPageListAdapter(this,decks);
        setListAdapter(listMaker);
    }



}
