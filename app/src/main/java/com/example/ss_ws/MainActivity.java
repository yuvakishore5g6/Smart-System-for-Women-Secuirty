package com.example.ss_ws;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
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
import com.google.android.material.navigation.NavigationView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final String MY_PREFERENCES = "com.example.ss_ws.Values";
    private static final String TAG = MainActivity.class.getName();
    private int CONST_PERMISSION = 1;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    private SpeechRecognizer mSpeechRecognizer;
    TextView responseText;
    private Handler mHandler = new Handler();
    Intent mSpeechIntent;
    double latitude, longitude;
    String[] appPermissions = {Manifest.permission.RECORD_AUDIO, Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.SEND_SMS,Manifest.permission.CALL_PHONE};
    boolean killCommanded = false;
    LocationManager locationManager;
    LocationListener listener;
    TextView tips,laws,emergency,instructions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        NavigationView navigationView = findViewById(R.id.nav_view);
        responseText =findViewById(R.id.responseText);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(MainActivity.this);
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

        if (checkAndRequestPermission()) {
            voice();
        }
    }

    private boolean checkAndRequestPermission() {
        List<String> listPermissionNeeded = new ArrayList<>();
        for (String per : appPermissions) {
            //check which permissions are granted
            if (ContextCompat.checkSelfPermission(this, per) != PackageManager.PERMISSION_GRANTED) {
                listPermissionNeeded.add(per);
            }
        }
        //Ask for non-granted permissions
        if (!listPermissionNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionNeeded.toArray(new String[listPermissionNeeded.size()]), CONST_PERMISSION);
            return false;
        }
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        int deniedCount = 0;
        HashMap<String, Integer> permissionResults = new HashMap<>();
        if (requestCode == CONST_PERMISSION) {

            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    permissionResults.put(permissions[i], grantResults[i]);
                    deniedCount++;
                }
            }
        }
        if (deniedCount == 0) {

        } else {
            for (Map.Entry<String, Integer> entry : permissionResults.entrySet()) {
                String perName = entry.getKey();
                Integer permResult = entry.getValue();
                //permission is denied for first time(when Never ask again is not checked)
                //so ask explaining the usage of permission
                //shouldShowRequestPermissionRationale() returns ture;
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, perName)) {
                    showDialog("", " This app needs location permission to work with out problems ", "Yes,Grant permissions",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    checkAndRequestPermission();
                                }
                            },
                            "No exit app", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    finish();
                                }
                            }, false);
                } else {
                    showDialog("", "You have denied some permissions,Allow all permissions at [setting] > [Permission]", "Go to settings",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", getPackageName(), null));
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                            }, "No Exit app", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    finish();
                                }
                            }, false);
                    break;
                }
            }
        }
    }

    public AlertDialog showDialog(String title, String message, String positiveText, DialogInterface.OnClickListener positiveonClickListener, String negativeText, DialogInterface.OnClickListener negativeonClickListener, boolean isCancelAble) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setCancelable(isCancelAble);
        builder.setMessage(message);
        builder.setPositiveButton(positiveText,positiveonClickListener);
        builder.setNegativeButton(negativeText,negativeonClickListener);
        AlertDialog alert = builder.create();
        alert.show();
        return alert;

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
        inflater.inflate(R.menu.menu, menu);
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
        if(result_message.contains("who")){
            if(result_message.contains("are you")){
                response="Smart system for your security";
            }
            if (result_message.contains("hello")){
                response = "Hello techie nice to meet you...!";

            }
        } else if (result_message.contains("help")){
            clickmeforloc();
            call();
            response = "Smart security system is online";

        }
        else if (result_message.contains("exit")){
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
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60000, 0, listener);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        if(id == R.id.home)
        {

        }
        if(id == R.id.add_contacts)
        {
            Intent intent = new Intent(this,Addcontacts.class);
            startActivity(intent);
        }
        if(id == R.id.profile)
        {
            Intent intent = new Intent(this,ActualProfile.class);
            startActivity(intent);
        }
        if(id == R.id.message)
        {
            Intent intent = new Intent(this,MessageActivity.class);
            startActivity(intent);
        }
        if(id == R.id.share)
        {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            String shareSubText = "SS_WS Great App";
            String shareBodyText = "https://play.google.com/store/apps/details?id=com.ss-ws&hl=en";
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, shareSubText);
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareBodyText);
            startActivity(Intent.createChooser(shareIntent, "Share With"));
        }
        if(id == R.id.rate)
        {
            Intent intent = new Intent(this,RatingActivity.class);
            startActivity(intent);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
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
                    if(!killCommanded) {
                        mSpeechRecognizer.startListening(mSpeechIntent);
                        mSpeechRecognizer.stopListening();
                    }
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

    }
}
