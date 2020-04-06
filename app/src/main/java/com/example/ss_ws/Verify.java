package com.example.ss_ws;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class Verify extends AppCompatActivity {

    ProgressBar progressBar;
    EditText code;
    Button check;
    private static final String TAG ="Verify.java";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);
        progressBar = findViewById(R.id.progressbar);
        code = findViewById(R.id.editTextCode);
        check = findViewById(R.id.buttonSignIn);

       final int verify = Integer.parseInt(getIntent().getExtras().getString("opt"));
        Log.d(TAG, "onCreate: "+verify+" : "+verify);
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                int verify_code = Integer.parseInt(code.getText().toString().trim());
                if(verify_code == verify)
                {
                    Log.d(TAG, "onClick: valid");
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(Verify.this,"Verified Successfully",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),Profile.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                    finish();
                }
                else {
                    Log.d(TAG, "onClick: INVALID");
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(Verify.this,"Invalid PIN",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}
