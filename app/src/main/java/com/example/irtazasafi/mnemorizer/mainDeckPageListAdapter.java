package com.example.irtazasafi.mnemorizer;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

/**
 * Created by Irtaza Safi on 1/9/2016.
 */
public class mainDeckPageListAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;

    public mainDeckPageListAdapter(Context context, String[] values) {
        super(context, R.layout.main_deck_page, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.main_deck_page, parent, false);
        final Button leftBut = (Button) rowView.findViewById(R.id.rightButton);

        leftBut.setText(values[position]);
        leftBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //System.out.println(leftBut.getText().toString());
                 Intent intent = new Intent(v.getContext(),single_word_page.class);
                  v.getContext().startActivity(intent);
                  System.out.println(leftBut.getText().toString());

            }
        });
        return rowView;
    }
}
