package com.example.irtazasafi.mnemorizer;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;

/**
 * Created by Irtaza Safi on 1/9/2016.
 */
public class mainDeckPageListAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;
    public SharedPreferences preferences;


    public mainDeckPageListAdapter(Context context, String[] values,SharedPreferences prefs) {
        super(context, R.layout.main_deck_page, values);
        this.context = context;
        this.values = values;
        preferences = prefs;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.main_deck_page, parent, false);
        final Button leftBut = (Button) rowView.findViewById(R.id.rightButton);
       // ProgressBar spinner = (ProgressBar)rowView.findViewById(R.id.spinner);
        //spinner.setVisibility(View.VISIBLE);


        leftBut.setText(values[position]);
        leftBut.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor editor = preferences.edit();

                if (leftBut.getText().toString().equals("No Mnemonics")) { //English Mnemonics","Native Language Mnemonics
                    editor.putInt("deckid", 1);
                } else if (leftBut.getText().toString().equals("English Mnemonics")) {
                    editor.putInt("deckid", 2);

                } else if(leftBut.getText().toString().equals("Sign Out")) {

                    editor.clear();
                    editor.apply();
                    Intent intent = new Intent(context,Login.class);
                    context.startActivity(intent);
                    return;

                } else {
                    editor.putInt("deckid", 3);
                }
                editor.apply();
                Intent intent = new Intent(v.getContext(), single_word_page.class);
                v.getContext().startActivity(intent);
                System.out.println(leftBut.getText().toString());

            }
        });
        return rowView;
    }
}
