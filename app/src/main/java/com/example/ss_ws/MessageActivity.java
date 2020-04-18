package com.example.ss_ws;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;

import static com.example.ss_ws.Login.MY_PREFERENCES;

public class MessageActivity extends AppCompatActivity {

    EditText editText;
    TextView textView;
    Button button;
    String text="";
    HashSet<String> name_set_mes = new HashSet<>();
    HashSet<String> phone_set_mes = new HashSet<>();
    HashSet<String> hSet = new HashSet<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        textView =findViewById(R.id.tv_message_regCon);
        editText = findViewById(R.id.et_MessageActivity);
        button = findViewById(R.id.but_msg_send);
        SharedPreferences preferences = getSharedPreferences(MY_PREFERENCES,MODE_PRIVATE);
        boolean set = preferences.getBoolean("set",false);

        if(set)
        {
            name_set_mes = (HashSet<String>) preferences.getStringSet("name_set", Collections.singleton(""));
            phone_set_mes = (HashSet<String>) preferences.getStringSet("phone_set", Collections.singleton(""));
            if (name_set_mes.size() > 0) {
                Iterator it = name_set_mes.iterator();
                Iterator it1 = phone_set_mes.iterator();
                while(it.hasNext() && it1.hasNext())
                {
                    text += it.next()+" : "+it1.next()+"\n";
                }
                textView.setText(text);
            }
        }
        else {
            String name = preferences.getString("contact1","");
            String name1 = preferences.getString("contact2","");
            text += "primary 1 : "+name+"\n"+"primary 2 : "+name1;
            textView.setText(text);
            Toast.makeText(this, "No contacts Selected:Add Contacts", Toast.LENGTH_SHORT).show();
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send();
            }
        });
    }

    private void send() {
        String message  = editText.getText().toString().trim();
        SharedPreferences preferences = getSharedPreferences(MY_PREFERENCES,MODE_PRIVATE);
        boolean set = preferences.getBoolean("set",false);
        if(set)
        {
            hSet = (HashSet<String>) preferences.getStringSet("phone_set",Collections.singleton(""));
        }

        if(set)
        {
            ArrayList<String> al = new ArrayList<>(hSet);
            int size = al.size();
            for(int i = 0 ; i < size;i++)
            {
                String number = al.get(i);
                SmsManager sms = SmsManager.getDefault();
                sms.sendTextMessage(number, null, message, null, null);
                Toast.makeText(MessageActivity.this, "Message sent to "+number, Toast.LENGTH_SHORT).show();
            }

        }
        else{
            SmsManager sms = SmsManager.getDefault();
            String number1 = preferences.getString("contact1","");
            String number2 = preferences.getString("contact2","");
            sms.sendTextMessage(number1, null, message, null, null);
            sms.sendTextMessage(number2, null, message, null, null);
            Toast.makeText(MessageActivity.this,"Messages Sent to Primary and secondary contacts",Toast.LENGTH_SHORT).show();
        }

    }
}
