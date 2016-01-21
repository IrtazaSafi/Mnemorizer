package com.example.irtazasafi.mnemorizer;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;

public class meaning_mnemonic_page extends AppCompatActivity {
    public SharedPreferences preferences;
    public SharedPreferences.Editor editor;
    public DataManager globalData = null;
    public Gson gson;

    ArrayList<Mnemonic> mnemonics;

    public TextView wordDisplay;
    public TextView meaningDisplay;
    public Button iKnew;
    public Button iDidNotKnow;

    public Button leftButton;
    public Button rightButton;

    public Button mnemonicDisplay;
    public Button masteredDisplay;

    public Button rankDisplay;
    public Button likeCounter;

    public Button thumbsUp;
    public int currWordID;

    public VocabularyWord currentWord;
    public int mnemonicIndex;

    public Mnemonic currentMnemonic = null;

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

        wordDisplay = (TextView)findViewById(R.id.wordDisplay);
        meaningDisplay = (TextView)findViewById(R.id.meaningDisplay);
        iKnew = (Button)findViewById(R.id.iKnew);
        leftButton = (Button)findViewById(R.id.leftButton);
        rightButton = (Button)findViewById(R.id.rightButton);
        //rightButton.setBackgroundColor(Color.);

        mnemonicDisplay = (Button)findViewById(R.id.mnemonicsDisplay);
        masteredDisplay = (Button)findViewById(R.id.masteredDisplay);

        masteredDisplay.setVisibility(View.INVISIBLE);
        rankDisplay = (Button)findViewById(R.id.rankDisplay);
        likeCounter = (Button)findViewById(R.id.likesDisplay);
        thumbsUp = (Button)findViewById(R.id.thumbsUp);
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
        likeCounter.setText("0");

        if(currentWord.mastered == true) {

            masteredDisplay.setVisibility(View.VISIBLE);

        }

        if (mnemonics.size() == 0){
            mnemonicDisplay.setText("No Mnemonics Available");
            rankDisplay.setText("0");
        } else {
            mnemonicDisplay.setText(mnemonics.get(0).mnemonic);
            currentMnemonic = mnemonics.get(0);
            likeCounter.setText(Integer.toString(currentMnemonic.score));
            if(mnemonics.get(0).liked == true) {
                thumbsUp.setBackgroundResource(R.drawable.thumbsupblue);
            }
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
        if(mnemonics.get(index).liked == true) {
            thumbsUp.setBackgroundResource(R.drawable.thumbsupblue);
        } else {
            thumbsUp.setBackgroundResource(R.drawable.thumbsup);
        }
        mnemonicDisplay.setText(mnemonics.get(index).mnemonic);
        currentMnemonic = mnemonics.get(index);
        rankDisplay.setText(Integer.toString(index + 1));

    }

    public void leftClicked(View v) {
        if(mnemonics.size() == 0) {
            return;
        }

        int index = Math.abs((mnemonicIndex-1)%mnemonics.size());
        if(mnemonics.get(index).liked == true) {
            thumbsUp.setBackgroundResource(R.drawable.thumbsupblue);
        } else {
            thumbsUp.setBackgroundResource(R.drawable.thumbsup);
        }

        mnemonicIndex = index;
        mnemonicDisplay.setText(mnemonics.get(index).mnemonic);
        currentMnemonic = mnemonics.get(index);
        rankDisplay.setText(Integer.toString(index + 1));
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public void addCustomMnemonic(View v) {

        editor.putInt("wordid",currWordID);
        editor.putInt("creatorid",globalData.userID);

        editor.apply();

        Intent intent = new Intent(this,addCustomMnemonic.class);
        startActivity(intent);
    }

    public void thumbsUp(View view) {
        AsyncTaskRunner runner = new AsyncTaskRunner();
        if(mnemonics.size()==0){
            return;
        }
        if(currentMnemonic.liked == false) {
//            currentMnemonic.liked = true;
//            thumbsUp.setBackgroundResource(R.drawable.thumbsupblue);
            String query = globalData.serverURL+"/-ratingQuery-thumbsUp-"+currentMnemonic.id;
            runner.execute(query,"thumbsUp");
        } else {
//            currentMnemonic.liked = false;
//            thumbsUp.setBackgroundResource(R.drawable.thumbsup);
            String query = globalData.serverURL+"/-ratingQuery-thumbsDown-"+currentMnemonic.id;
            runner.execute(query, "thumbsDown");

        }

    }

    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        private String makeHTTPRequest(String url,String method){

            String serverReply ="";
            try {
                HttpURLConnection koka = (HttpURLConnection) new URL(url).openConnection();
                koka.setRequestMethod(method);
                InputStream response = koka.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(response));
                koka.setConnectTimeout(1000);
                koka.setReadTimeout(1000);
                for (String line; (line = reader.readLine()) != null; ) {
                    serverReply = serverReply+line;
                }
                //System.out.println(serverReply);
            } catch (SocketTimeoutException e) {
                e.printStackTrace();
                return "error";
            } catch(IOException e) {
                e.printStackTrace();
                return "error";
            }
            return serverReply;
        }

        @Override
        protected String doInBackground(String... params) {

            return makeHTTPRequest(params[0],"GET") + "-"+params[1];
        }

        @Override
        protected void onPostExecute(String result) {

            boolean upthumbs = false;

            System.out.println("********** RESULT IS  " + result);

            if(result.contains("thumbsUp")) {
                upthumbs = true;
            } else {
                upthumbs = false;
            }

            String [] resVector = result.split("-");

            if(resVector[0].equals("error")){
                showToast("Error: Could not submit.The connection timed out. Make sure you have a working internet connection");
//                if(upthumbs == true){
//                    thumbsUp.setBackgroundResource(R.drawable.thumbsup);
//                } else {
//                    thumbsUp.setBackgroundResource(R.drawable.thumbsupblue);
//                }

                return;
            }

            if(result.equals("dbQueryError")){
                showToast("Error Submitting to database");
                System.out.println("Error SUBMITTING TO DATABASE");
//                if(upthumbs == true){
//                    thumbsUp.setBackgroundResource(R.drawable.thumbsup);
//                } else {
//                    thumbsUp.setBackgroundResource(R.drawable.thumbsupblue);
//                }
                return;
            }

            if(resVector[0].equals("successLIKE")){
                currentMnemonic.score+=1;
                currentMnemonic.liked = true;
                likeCounter.setText(Integer.toString(currentMnemonic.score));
                thumbsUp.setBackgroundResource(R.drawable.thumbsupblue);


            } else if(resVector[0].equals("successUNLIKE")) {
                currentMnemonic.score-=1;
                currentMnemonic.liked = false;
                likeCounter.setText(Integer.toString(currentMnemonic.score));
                thumbsUp.setBackgroundResource(R.drawable.thumbsup);
            }


        }
    }



}
