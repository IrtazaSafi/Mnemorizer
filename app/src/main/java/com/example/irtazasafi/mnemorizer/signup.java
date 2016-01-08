package com.example.irtazasafi.mnemorizer;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class signup extends AppCompatActivity  {

    public String serverResponse = "";
    public String serverURL = "http://10.130.2.78";//"http://192.168.10.7";
    public volatile boolean respRecieved = false;

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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void signUpAndLogin(View view) throws Exception {
        EditText email = (EditText) findViewById(R.id.emailInSignUp);
        EditText password = (EditText) findViewById(R.id.passwordInSignUp);
        EditText passwordconfirm = (EditText) findViewById(R.id.confirmPasswordSignUp);

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


        String query = serverURL+"/"+"-signUpRequest-"+email.getText().toString()+"-"+HashPassword(password.getText().toString())+
                "-"+String.valueOf(latitude)+"-"+String.valueOf(longitude);

        serverResponse ="";
        respRecieved= false;
        makeSynchronusRequest(query,"GET");
        while(!respRecieved){
            System.out.println("Waiting for Response");
        }
        String resp[] = serverResponse.split("-");
        System.out.println("Response is " + serverResponse);
        int userID = 0;
        if(resp[0].equals("Created")){
            userID = Integer.valueOf(resp[1]);
            showToast("Account Successfully Created");
        } else {
            showToast("Sign Up Failed, either account already exists or you entered invalid credentials");
        }
    }
}
