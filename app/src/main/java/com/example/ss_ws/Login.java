package com.example.ss_ws;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Boolean.FALSE;

public class Login extends AppCompatActivity {

    TextView textView;
    Button button;
    private static final int MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 1;
    public static final String MY_PREFERENCES = "com.example.ss_ws.Values";

    String[] appPermissions = {Manifest.permission.RECORD_AUDIO,Manifest.permission.SEND_SMS};
    private final int CONST_PERMISSION = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle("Login");
        SharedPreferences preferences = getSharedPreferences(MY_PREFERENCES,MODE_PRIVATE);
        boolean check = preferences.getBoolean("save",FALSE);
        int flag  = 0;
        if(check)
        {
            flag = 1;
            finish();
            startActivity(new Intent(this,MainActivity.class));
        }
        else
        {
            if(flag == 0)
            {

            }
            else
               startActivity(new Intent(this,Profile.class));
        }

        if (ContextCompat.checkSelfPermission(Login.this,Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(Login.this,
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(Login.this,
                    Manifest.permission.RECORD_AUDIO) && ActivityCompat.shouldShowRequestPermissionRationale(Login.this,
                    Manifest.permission.SEND_SMS)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(Login.this,
                        new String[]{Manifest.permission.RECORD_AUDIO,Manifest.permission.SEND_SMS},MY_PERMISSIONS_REQUEST_RECORD_AUDIO);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        textView  = findViewById(R.id.editTextMobile);
        button  = findViewById(R.id.buttonContinue);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sms();
            }
        });
    }

    private void sms() {
        if (checkAndRequestPermission()) {
            String number = textView.getText().toString().trim();
            SecureRandom random = new SecureRandom();
            int num = random.nextInt(100000);
            String formatted = String.format("%05d", num);
            SharedPreferences.Editor editor = getSharedPreferences(MY_PREFERENCES, MODE_PRIVATE).edit();
            if(!number.equals("") && number.length() == 10)
            {
                editor.putString("mobile", number);
                editor.commit();
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(number, null, "OTP: " + formatted, null, null);
                Toast.makeText(Login.this, "Opt sent", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Login.this, Verify.class);
                intent.putExtra("opt", formatted);
                startActivity(intent);
                finish();
            }
            else
                Toast.makeText(Login.this, R.string.mobile_number,Toast.LENGTH_SHORT).show();
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
                    showDialog("", " This app needs permissions to work with out any issues ", "Yes,Grant permissions",
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
}
