package com.example.irtazasafi.mnemorizer;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Login extends AppCompatActivity {
    public String serverResponse = "";
    public String serverURL = "http://192.168.10.7";
    public volatile boolean respRecieved = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("***************************************************************");
        setContentView(R.layout.activity_login);
        System.out.println("***************************************************************");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        System.out.println("***************************************************************");
        setSupportActionBar(toolbar);
        System.out.println("***************************************************************");
    }

    public void makeSynchronusRequest(final String url, final String method) throws Exception {
        Thread requester = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpURLConnection koka = (HttpURLConnection) new URL(url).openConnection();
                    koka.setRequestMethod(method);
                    InputStream response = koka.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(response));
                    for (String line; (line = reader.readLine()) != null; ) {
                        serverResponse = serverResponse + line;
                    }
                    System.out.println(serverResponse);
                    respRecieved = true;
                } catch (Exception e) {
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
                        //respRecieved = true;
                      //  doNotify(responseWait);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.toString());
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
        EditText email = (EditText)findViewById(R.id.emailIn);

        if(!isValidEmail(email.getText().toString())){

            System.out.println("INVALID EMAIL ENTERED");
            showToast("INVALID EMAIL ENTERED");
            return;
        }
        EditText password = (EditText)findViewById(R.id.passwordIn);

        serverResponse ="";

        String loginRequest = serverURL+"/"+"-loginRequest-"+email.getText().toString()+"-"+password.getText().toString();
        respRecieved = false;
        makeSynchronusRequest(loginRequest, "GET");
        while(respRecieved == false) {
            System.out.println("WAITING********");
        }
        respRecieved = false;
        System.out.println("SERVER RESPONSE IS :  " + serverResponse);

        String resp[] = serverResponse.split("-");
        int userID;
        System.out.println(resp[0]);
        if(resp[0].equals("Validated")){
            userID = Integer.valueOf(resp[1]);
            showToast("Login Successful");
        } else {
            showToast("Login Failed, either account doesn't exist or you entered invalid credentials");
        }

    }

    public void signup() {
        

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
