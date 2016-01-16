package com.example.irtazasafi.mnemorizer;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class Login extends AppCompatActivity {
    public String serverResponse = "";
    public String serverURL = "http://192.168.10.6";//"http://192.168.10.6";//
    public volatile boolean respRecieved = false;
    public volatile boolean connectionError = false;
    ProgressBar spinner;
    public Context context = this;
   public SharedPreferences preferences;

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
                System.out.println(serverReply);
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

        @TargetApi(Build.VERSION_CODES.GINGERBREAD)
        @Override
        protected void onPostExecute(String result) {
            if(result.equals("error")) {
                showToast("Error,the connection timed out. Make sure you have a working internet connection");
                return;
            }
            String resp[] = result.split("-");
            int userID;
            System.out.println(resp[0]);
        if(resp[0].equals("Validated")){
            showToast("Login Successful");
            userID = Integer.valueOf(resp[1]);
            Intent main_deck_page = new Intent(context,main_deck_page.class);
            main_deck_page.putExtra("userID",userID);
            spinner.setVisibility(View.INVISIBLE);
            startActivity(main_deck_page);
           // fetchData();
        } else {
            showToast("Login Failed, either account doesn't exist or you entered invalid credentials");
        }
            if(resp[0].equals("json")){
                System.out.println("JSON RECIEVED");
                Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();


                String valSon = resp[1];
                VocabularyWord words [] = gson.fromJson(resp[1], VocabularyWord[].class);
                System.out.println("JSON is  " + valSon);

                DataManager globalData = new DataManager(1,"admin@admin.com");
                for (int i = 0 ; i < words.length;i++) {
                    globalData.vocabularyWords.add(words[i]);
                }

                Gson serializer = new Gson();

                String serializedData = serializer.toJson(globalData);

                System.out.println("SERIALIZED DATA IS :  " + serializedData);

                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("globalData", serializedData);
                editor.apply();
                showToast("DATA RECIEVED AND SAVED");

                String returnedData = preferences.getString("globalData","");

                System.out.println("RETURNED DATA IS  " + returnedData);

                DataManager returned = serializer.fromJson(returnedData,DataManager.class);

                System.out.println(returned.email);
                Intent main_deck_page = new Intent(context,main_deck_page.class);
                startActivity(main_deck_page);


            }
        }
    }



        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        spinner= (ProgressBar)findViewById(R.id.spinner);
        spinner.setVisibility(View.GONE);
            preferences = PreferenceManager.getDefaultSharedPreferences(this);
      //  Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);

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

    public void makeSynchronusRequest(final String url, final String method) {
        Thread requester = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpURLConnection koka = (HttpURLConnection) new URL(url).openConnection();
                    koka.setRequestMethod(method);
                    InputStream response = koka.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(response));
                    koka.setConnectTimeout(1000);
                    koka.setReadTimeout(1000);
                    for (String line; (line = reader.readLine()) != null; ) {
                        serverResponse = serverResponse + line;
                    }
                    System.out.println(serverResponse);
                    respRecieved = true;
                } catch (SocketTimeoutException e) {

                    connectionError = true;
                    e.printStackTrace();
                } catch(IOException e) {
                    connectionError = true;
                    e.printStackTrace();
                }
            }

        });
        requester.start();
    }


    public void makeAsynchronusRequest(String url) {
        RequestQueue queue = Volley.newRequestQueue(this);
        //final ArrayList<String> myResp = new ArrayList<String>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        System.out.println("RESPONSE RECEiVED");
                        serverResponse = response;
                        System.out.println("RESP   " + response);
                        respRecieved = true;
                      //  doNotify(responseWait);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.toString());
                connectionError = true;
                serverResponse = "ERROR";
                //respRecieved = true;
               // doNotify(responseWait);
            }
        });
        queue.add(stringRequest);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    public final static boolean isValidEmail(CharSequence target) {

        return target != null && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public void showToast(String text) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public void login(View view) throws Exception {
        EditText email = (EditText)findViewById(R.id.emailInLogin);

        if(!isValidEmail(email.getText().toString())){

            System.out.println("INVALID EMAIL ENTERED");
            showToast("INVALID EMAIL ENTERED");
            return;
        }
        EditText password = (EditText)findViewById(R.id.passwordIn);
        serverResponse ="";
        String loginRequest = serverURL+"/"+"-loginRequest-"+email.getText().toString()+"-"+
                HashPassword(password.getText().toString());
        spinner.setVisibility(View.VISIBLE);
        AsyncTaskRunner runner = new AsyncTaskRunner();
        runner.execute(loginRequest);

    }


    public void signup(View view) {
        Intent signupPage = new Intent(this,signup.class);
        startActivity(signupPage);
        //fetchData();

    }

    public void devAccess(View view) {
       // startActivity(new Intent(this,main_deck_page.class));
        AsyncTaskRunner runner = new AsyncTaskRunner();
        String apple = serverURL+"/-test";
        runner.execute(apple);
      //  fetchData();

    }

    public void fetchData() {
        Intent main_deck_page = new Intent(this,main_deck_page.class);
       // ProgressBar spinner = (ProgressBar)findViewById(R.id.spinner);
        spinner.setVisibility(View.VISIBLE);
//        for(int i = 0; i < 5000000;i++) {
//            System.out.println("FETCHING DATA");
//        }
      //  spinner.setVisibility(View.GONE);
       // startActivity(main_deck_page);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
