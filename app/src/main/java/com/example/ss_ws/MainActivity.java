package com.example.ss_ws;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.telephony.SmsManager;
import android.util.Log;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;



import java.util.ArrayList;



public class MainActivity extends AppCompatActivity {
    public static final String MY_PREFERENCES = "com.example.ss_ws.Values";
    private static final String TAG = MainActivity.class.getName();
    private static final int MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 1;
    private SpeechRecognizer mSpeechRecognizer;
    TextView responseText;
    private Handler mHandler = new Handler();
    Intent mSpeechIntent;
    double latitude, longitude;
    boolean killCommanded = false;
    LocationManager locationManager;
    LocationListener listener;
    TextView tips,laws,emergency,instructions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tips = findViewById(R.id.tvTips);
        laws = findViewById(R.id.tvLaw);
        emergency = findViewById(R.id.tvEmergency);
        instructions = findViewById(R.id.tvInstructions);
        tips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Tips.class);
                startActivity(intent);
            }
        });
        laws.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,LawsForwomen.class);
                startActivity(intent);
            }
        });
        emergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Emergency.class);
                startActivity(intent);
            }
        });
        instructions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Instructions.class);
                startActivity(intent);
            }
        });

        responseText =findViewById(R.id.responseText);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                String loc1 = "";
                SharedPreferences preferences = getSharedPreferences(MY_PREFERENCES, MODE_PRIVATE);
                String name = preferences.getString("userName", "");
                String number = preferences.getString("contact1", "");
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                loc1 = name+" need HELP " + "http://maps.google.com/maps?q=" + latitude + "," + longitude;
                SmsManager sms = SmsManager.getDefault();
                sms.sendTextMessage(number, null, loc1, null, null);
                Toast.makeText(MainActivity.this, "Message sent", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Toast.makeText(MainActivity.this, "Enable Gps", Toast.LENGTH_LONG).show();

            }

        };

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if ((ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.RECORD_AUDIO) &&
                    ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
            && ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.SEND_SMS) &&
                    ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CALL_PHONE))) {
            }
            else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.SEND_SMS,Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_RECORD_AUDIO);

            }
        } else {

        }
        voice();
    }


    public void voice() {
        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(MainActivity.this);
        SpeechListener mRecognitionListener = new SpeechListener();
        mSpeechRecognizer.setRecognitionListener(mRecognitionListener);
        mSpeechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        mSpeechIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,"com.example.continuous");

        // Given an hint to the recognizer about what the user is going to say
        mSpeechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        // Specify how many results you want to receive. The results will be sorted
        // where the first result is the one with higher confidence.
        mSpeechIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 20);
        mSpeechIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        mSpeechRecognizer.startListening(mSpeechIntent);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.volunteer)
        {
            Intent intent = new Intent(getApplicationContext(),Volunteer.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
            finish();

        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.drawer_menu, menu);
        return true;
    }

    @Override
    protected void onPause() {
        //kill the voice recognizer
        if(mSpeechRecognizer != null){
            mSpeechRecognizer.startListening(mSpeechIntent);
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if(mSpeechRecognizer != null){
            mSpeechRecognizer.stopListening();
            mSpeechRecognizer.cancel();
            mSpeechRecognizer.destroy();
            mSpeechRecognizer = null;
        }
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        if(mSpeechRecognizer != null){
            mSpeechRecognizer.startListening(mSpeechIntent);
        }
        super.onStop();
    }

    private void processCommand(ArrayList<String> matchStrings){
        String response = "I'm sorry, Yuvakishore. I'm afraid I can't do that.";
        String result_message = matchStrings.get(0);
        result_message = result_message.toLowerCase();
        if(result_message.indexOf("who") != -1){
            if(result_message.indexOf("are you") != -1){
                response="Smart system for your security";
            }
            if (result_message.indexOf("hello") != -1){
                response = "Hello techie nice to meet you...!";

            }
        } else if (result_message.indexOf("help") != -1){
            clickmeforloc();
            call();
            response = "Smart security system is online";

        }
        else if (result_message.indexOf("exit") != -1){
            killCommanded = true;
        }


        final String finalResponse = response;
        mHandler.post(new Runnable() {
            public void run() {
                responseText.setText(finalResponse);
            }
        });

    }
    private void call() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_USER_ACTION);
        SharedPreferences preferences = getSharedPreferences(MY_PREFERENCES, MODE_PRIVATE);
        String number = preferences.getString("contact1", "");
        callIntent.setData(Uri.parse("tel:" + number));
        startActivity(callIntent);
    }

    private void clickmeforloc() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0, listener);
    }

    class SpeechListener implements RecognitionListener {
        public void onBufferReceived(byte[] buffer) {
            Log.d(TAG, "buffer received ");
        }
        public void onError(int error) {
            //if critical error then exit
            if(error == SpeechRecognizer.ERROR_CLIENT || error == SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS){
                Log.d(TAG, "client error");
            }
            //else ask to repeats
            else{
                Log.d(TAG, "other error");
                mSpeechRecognizer.startListening(mSpeechIntent);
            }
        }
        public void onEvent(int eventType, Bundle params) {
            Log.d(TAG, "onEvent");
        }
        public void onPartialResults(Bundle partialResults) {
            Log.d(TAG, "partial results");
        }
        public void onReadyForSpeech(Bundle params) {
            Log.d(TAG, "on ready for speech");
        }
        public void onResults(Bundle results) {
            Log.d(TAG, "on results");
            ArrayList<String> matches = null;
            if(results != null){
                matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if(matches != null){
                    Log.d(TAG, "results are " + matches.toString());
                    final ArrayList<String> matchesStrings = matches;
                    processCommand(matchesStrings);
                    if(!killCommanded)
                        mSpeechRecognizer.startListening(mSpeechIntent);
                    else
                        finish();
                }
            }

        }
        public void onRmsChanged(float rmsdB) {
            //			Log.d(TAG, "rms changed");
        }
        public void onBeginningOfSpeech() {
            Log.d(TAG, "speech beginning");
        }
        public void onEndOfSpeech() {
            Log.d(TAG, "speech done");
        }

    };
}
