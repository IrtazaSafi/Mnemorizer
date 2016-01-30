package com.example.irtazasafi.mnemorizer;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class signup extends AppCompatActivity  {

    public String serverResponse = "";
    public String serverURL = "";//http://192.168.10.4";//"http://ec2-54-191-246-47.us-west-2.compute.amazonaws.com"; //"http://10.130.2.78";//
    public volatile boolean respRecieved = false;
    public Context context = this;
    public SharedPreferences preferences;
    public SharedPreferences.Editor editor;
    DataManager globalData;

    LocationManager locationManager;

    public final static boolean isValidEmail(CharSequence target) {

        return target != null && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static String HashPassword(String plaintext) throws NoSuchAlgorithmException {
        MessageDigest m = MessageDigest.getInstance("MD5");
        m.reset();
        m.update(plaintext.getBytes());
        byte[] digest = m.digest();
        BigInteger bigInt = new BigInteger(1, digest);
        String hashtext = bigInt.toString(16);
        return hashtext;
    }

    public void showToast(String text) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        this.overridePendingTransition(R.anim.slide_right, R.anim.slide_left);


        globalData = new DataManager(0,"");
        serverURL = globalData.serverURL;

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void signUpAndLogin(View view) throws Exception {

        EditText email = (EditText) findViewById(R.id.emailInSignUp);
        EditText password = (EditText) findViewById(R.id.passInSignUp);
        EditText passwordconfirm = (EditText) findViewById(R.id.passConfirmSignUp);

        String pass = password.getText().toString();
        String confirm = passwordconfirm.getText().toString();

        if(!isValidEmail(email.getText().toString()) || !pass.equals(confirm)){
            showToast("Either Email was invalid or Passwords did not match");
            return;
        }
        GPSTracker gps = new GPSTracker(this);
        if(!gps.canGetLocation()) {
            showToast("PLEASE ENABLE GPS AND TRY AGAIN");
            gps.showSettingsAlert();
            return;
        }
        double latitude = gps.getLatitude();
        double longitude = gps.getLongitude();

        globalData.email = email.getText().toString();

        String query = serverURL+"/"+"-signUpRequest-"+email.getText().toString()+"-"+HashPassword(password.getText().toString())+
                "-"+String.valueOf(latitude)+"-"+String.valueOf(longitude);

        AsyncTaskRunner runner = new AsyncTaskRunner();

        runner.execute(query);

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

            System.out.println("****************REQUST SENT");
            return makeHTTPRequest(params[0],"GET");
        }

        @TargetApi(Build.VERSION_CODES.GINGERBREAD)
        @Override
        protected void onPostExecute(String result) {

            System.out.println("********** RESULT IS  " + result);

            String [] resVector = result.split("-");

            if(resVector[0].equals("error")) {

                if(resVector[1].equals("Exists")) {

                    showToast("This User Already Exists");
                } else {
                    showToast("Error with Database, please retry later");
                }

            } else if (resVector[0].equals("success")) {
                Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

                int userid = Integer.parseInt(resVector[1]);
                String valson = resVector[2];

                VocabularyWord [] words = gson.fromJson(valson,VocabularyWord[].class);

                globalData.userID = userid;

                for(int i = 0 ; i<words.length;i++) {

                    globalData.vocabularyWords.add(words[i]);
                }

                Gson serializer = new Gson();

                String serializedData = serializer.toJson(globalData);

                editor.putString("globalData", serializedData);

                editor.putBoolean("loggedIn", true);

                editor.apply();

                showToast("ACCOUNT SUCCESSFULLY CREATED");

                Intent intent = new Intent(context,main_deck_page.class);

                startActivity(intent);
            }


        }
    }




}
