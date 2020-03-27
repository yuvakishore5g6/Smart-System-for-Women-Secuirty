package com.example.ss_ws;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


import static com.example.ss_ws.Login.MY_PREFERENCES;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class Profile extends AppCompatActivity {

    EditText name,mobile,address,contact1,contact2;
    Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        SharedPreferences preferences = getSharedPreferences(MY_PREFERENCES,MODE_PRIVATE);
        String phone  = preferences.getString("mobile","");
        boolean check = preferences.getBoolean("save",FALSE);
        getSupportActionBar().setTitle("Profile");
        if(check)
        {
            finish();
            startActivity(new Intent(Profile.this,MainActivity.class));
        }
        name = findViewById(R.id.etName);
        mobile= findViewById(R.id.etMobile);
        mobile.setText(phone);
        mobile.setEnabled(false);
        address = findViewById(R.id.etAddress);
        contact1 = findViewById(R.id.etContact1);
        contact2 = findViewById(R.id.etContact2);
        save = findViewById(R.id.butSave);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    save_details();
            }
        });

    }

    private void save_details() {
        String userName = name.getText().toString().trim();
        String add = address.getText().toString().trim();
        String con1 = contact1.getText().toString().trim();
        String con2  = contact2.getText().toString().trim();
        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFERENCES,MODE_PRIVATE).edit();
        if(userName.isEmpty())
        {
            name.setError("Name is required");
            name.requestFocus();
            return;
        }
        if(add.isEmpty())
        {
            address.setError("Address is required");
            address.requestFocus();
            return;
        }
        if(con1.isEmpty())
        {
            contact1.setError("contact 1 is required");
            contact1.requestFocus();
            return;
        }
        if(con1.length() < 10)
        {
            Toast.makeText(Profile.this,"Enter a valid mobile number",Toast.LENGTH_SHORT).show();
            contact1.setError("contact 1 is required");
            contact1.requestFocus();
            return;
        }
        if(con2.isEmpty())
        {
            contact2.setError("Contact 2 is required");
            contact2.requestFocus();
            return;
        }
        if(con2.length() < 10)
        {
            Toast.makeText(Profile.this,"Enter a valid mobile number",Toast.LENGTH_SHORT).show();
            contact2.setError("contact 1 is required");
            contact2.requestFocus();
            return;
        }
        if(con1 == con2)
        {
            contact2.setError("Numbers cannot be same");
            contact2.requestFocus();
            return;
        }
        editor.putString("userName",userName);
        editor.putString("Address",add);
        editor.putString("contact1",con1);
        editor.putString("contact2",con2);
        editor.putBoolean("save",TRUE);
        editor.commit();
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        finish();

    }
}
