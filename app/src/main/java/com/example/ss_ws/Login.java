package com.example.ss_ws;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.security.SecureRandom;

import static java.lang.Boolean.FALSE;

public class Login extends AppCompatActivity {

    TextView textView;
    Button button;
    public static final String MY_PREFERENCES = "com.example.ss_ws.Values";


    private final int REQUEST_FOR_SMS = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle("Login");
        SharedPreferences preferences = getSharedPreferences(MY_PREFERENCES,MODE_PRIVATE);
        boolean check = preferences.getBoolean("save",FALSE);
        if(check)
        {
            finish();
            startActivity(new Intent(this,MainActivity.class));
        }
        else
        {
           String  mobile = preferences.getString("mobile","");
           if(mobile.equals(""))
           {

           }
           else {
               startActivity(new Intent(this,Profile.class));
           }
        }
        textView  = findViewById(R.id.editTextMobile);
        button  = findViewById(R.id.buttonContinue);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.SEND_SMS))
            {

            }
            else
            {
                ActivityCompat.requestPermissions(Login.this,new String[]{Manifest.permission.SEND_SMS},REQUEST_FOR_SMS);
            }
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = textView.getText().toString().trim();
                SecureRandom random = new SecureRandom();
                int num = random.nextInt(100000);
                String formatted = String.format("%05d", num);
                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFERENCES,MODE_PRIVATE).edit();
                editor.putString("mobile",number);
                editor.commit();
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(number,null,"OTP: "+formatted,null,null);
                Intent intent  = new Intent(Login.this,Verify.class);
                intent.putExtra("opt",formatted);
                startActivity(intent);
            }
        });
    }
}
