package com.example.irtazasafi.mnemorizer;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;

public class addCustomMnemonic extends AppCompatActivity {

    // declare variables
    public SharedPreferences preferences;
    public SharedPreferences.Editor editor;
    public DataManager globalData = null;
    public Gson gson;
    public int creatorid;
    public int wordid;
    public Mnemonic createdMnemonic;
    public Context context = this;

        EditText mnemonicText;
        Button submit;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_add_custom_mnemonic);
            gson = new Gson();

            submit = (Button)findViewById(R.id.submitMnemonic);
            preferences = PreferenceManager.getDefaultSharedPreferences(this);
            editor = preferences.edit();
            globalData = gson.fromJson(preferences.getString("globalData", ""), DataManager.class);
            mnemonicText = (EditText)findViewById(R.id.mnemonicInput);
            creatorid = preferences.getInt("creatorid", 0);
            wordid = preferences.getInt("wordid",0);
        }



    public void submitMnemonic(View view) throws UnsupportedEncodingException {

        String data = mnemonicText.getText().toString();
        if(data.contains("-")){
            showToast("The Character '-' is not allowed, please remove it from your submission");
            return;
        }

        String serverURL = globalData.serverURL;
        GPSTracker gps = new GPSTracker(this);
        if(!gps.canGetLocation()) {
            showToast("PLEASE ENABLE GPS AND TRY AGAIN");
            gps.showSettingsAlert();
            return;
        }
        double latitude = gps.getLatitude();
        double longitude = gps.getLongitude();

        createdMnemonic = new Mnemonic(0,data,wordid,0,creatorid,latitude,longitude);


        String requestone = "-mnemonicSubmission-" +
        Integer.toString(creatorid)+"-"+Integer.toString(wordid)+"-"+data + "-"+latitude+"-"+longitude;

        String encoded = URLEncoder.encode(requestone,"UTF-8");

        String finalRequest = serverURL + "/"+encoded;




        //System.out.println("****************"+ request);


        AsyncTaskRunner runner = new AsyncTaskRunner();
        runner.execute(finalRequest);

    }


    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    protected void onPause() {
        super.onPause();

        String tosave = gson.toJson(globalData);
        editor.putString("globalData",tosave);
        editor.apply();



    }


    public void showToast(String text) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
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

            return makeHTTPRequest(params[0],"GET");
        }

        @Override
        protected void onPostExecute(String result) {

            String [] resVector = result.split("-");

            if(result.equals("error")){
                showToast("Error: Could not submit.The connection timed out. Make sure you have a working internet connection");
                return;
            }

            if(result.equals("dbQueryError")){
                showToast("Error Submitting to database");
                System.out.println("Error SUBMITTING TO DATABASE");
                return;
            }

            if(resVector[0].equals("success")){
                int id = Integer.parseInt(resVector[1]);
                createdMnemonic.id = id;
                globalData.putMnemonicforWord(wordid,createdMnemonic);
                showToast("Mnemonic Saved to Database");
                Intent intent = new Intent(context,meaning_mnemonic_page.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }


        }
    }


}
