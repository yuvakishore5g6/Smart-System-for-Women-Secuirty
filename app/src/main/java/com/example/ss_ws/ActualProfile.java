package com.example.ss_ws;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.ss_ws.Login.MY_PREFERENCES;
import static java.lang.Boolean.TRUE;

public class ActualProfile extends AppCompatActivity  {

    EditText address_act,contact1_act,contact2_act,name_act,mobile_act;

    Button but_save;
    ImageView img1,img2,img3;

    String nameACTFill,phone_Fill,addressFill,con1Fill,con2Fill;
    String xyz;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actual_profile);

        name_act = findViewById(R.id.etName_Act);
        mobile_act = findViewById(R.id.etMobileAct);
        address_act = findViewById(R.id.etAddressACT);
        contact1_act = findViewById(R.id.etContact1ACT);
        contact2_act = findViewById(R.id.etContact2ACT);
        textView = findViewById(R.id.tv_profile_heading);
        but_save = findViewById(R.id.butSaveACT);
        img1 =  findViewById(R.id.imageViewACT);
        img2 = findViewById(R.id.imageView1ACT);
        img3 =  findViewById(R.id.imageView2ACT);
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                address_act.setEnabled(true);
            }
        });
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contact1_act.setEnabled(true);
            }
        });
        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contact2_act.setEnabled(true);
            }
        });
        but_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveACT();
            }
        });
        setData();
    }

    private void setData() {
        SharedPreferences preferences = getSharedPreferences(MY_PREFERENCES,MODE_PRIVATE);
        nameACTFill = preferences.getString("userName","");
        if (nameACTFill != null) {
            xyz = "Hello, "+nameACTFill.toUpperCase();
        }
        textView.setText(xyz);
        name_act.setText(nameACTFill);
        name_act.setEnabled(false);
        phone_Fill  = preferences.getString("mobile","");
        mobile_act.setText(phone_Fill);
        mobile_act.setEnabled(false);
        addressFill = preferences.getString("Address","");
        address_act.setText(addressFill);
        address_act.setEnabled(false);
        con1Fill = preferences.getString("contact1","");
        contact1_act.setText(con1Fill);
        contact1_act.setEnabled(false);
        con2Fill = preferences.getString("contact2","");
        contact2_act.setText(con2Fill);
        contact2_act.setEnabled(false);

    }

    private void saveACT() {
        int see = 0;

        String addACT = address_act.getText().toString().trim();
        String con1ACT = contact1_act.getText().toString().trim();
        String con2ACT  = contact2_act.getText().toString().trim();
        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFERENCES,MODE_PRIVATE).edit();
        if(con1ACT.length() != 10 )
        {
            see =1;
            Toast.makeText(ActualProfile.this,"Enter a valid mobile number",Toast.LENGTH_SHORT).show();
            contact1_act.setError("contact 1 is required");
            contact1_act.requestFocus();
            return;
        }
        if( con1ACT.isEmpty())
        {
            see =1;
            Toast.makeText(ActualProfile.this,"Enter a  mobile number",Toast.LENGTH_SHORT).show();
            contact1_act.setError("contact 1 is required");
            contact1_act.requestFocus();
            return;
        }
        if(con2ACT.length() != 10)
        {
            see =1;
            Toast.makeText(ActualProfile.this,"Enter a valid mobile number",Toast.LENGTH_SHORT).show();
            contact2_act.setError("Contact 2 is required");
            contact2_act.requestFocus();
            return;
        }
        if(con2ACT.isEmpty())
        {
            see =1;
            Toast.makeText(ActualProfile.this,"Enter a  mobile number",Toast.LENGTH_SHORT).show();
            contact2_act.setError("Contact 2 is required");
            contact2_act.requestFocus();
            return;
        }
        if(con1ACT.equals(con2ACT))
        {
            see =1;
            contact2_act.setError("Numbers cannot be same");
            contact2_act.requestFocus();
            return;
        }
        if(con1ACT.equals(phone_Fill))
        {
            see =1;
            contact1_act.setError("Your number and Registered number cannot be same");
            contact1_act.requestFocus();
            return;
        }
        if(con2ACT.equals(phone_Fill))
        {
            see =1;
            contact2_act.setError("Your number and Registered number cannot be same");
            contact2_act.requestFocus();
            return;
        }
        if(see != 1) {
            editor.putString("Address", addACT);
            editor.putString("contact1", con1ACT);
            editor.putString("contact2", con2ACT);
            editor.putBoolean("save", TRUE);
            editor.apply();
            Toast.makeText(this,"Profile updated successfully",Toast.LENGTH_SHORT).show();
        }
    }


}
