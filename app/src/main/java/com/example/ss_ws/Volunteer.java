package com.example.ss_ws;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Volunteer extends AppCompatActivity {

    TextView raise,ambulance,exit;
    ImageView img;
    private MediaPlayer player;
    boolean isPlaying  = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer);
        raise = findViewById(R.id.tvRaise);
        ambulance = findViewById(R.id.tvAmbulance);
        exit = findViewById(R.id.tvExit);
        img = findViewById(R.id.help_img);

        raise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_USER_ACTION);
                callIntent.setData(Uri.parse("tel:" + 100));
                startActivity(callIntent);
            }
        });
        ambulance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_USER_ACTION);
                callIntent.setData(Uri.parse("tel:" + 108));
                startActivity(callIntent);
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isPlaying)
                    alert();
                else
                    player.stop();
            }
        });
    }

    private void alert() {
        if(player == null)
        {
            player = MediaPlayer.create(this,R.raw.police);
        }
        player.start();
    }
    @Override
    protected void onStop() {

        super.onStop();
        if(player != null)
        {
            player.release();
            player  = null;
        }
    }
}
