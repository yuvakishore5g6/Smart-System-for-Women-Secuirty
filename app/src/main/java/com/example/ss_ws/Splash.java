package com.example.ss_ws;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class Splash extends AppCompatActivity {

    private int SLEEP_TIMER = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        LogoLauncher logo = new LogoLauncher();
        logo.start();
    }
    private class LogoLauncher extends Thread{
        public void run()
        {
            try{
                sleep(1000 * SLEEP_TIMER);
            }
            catch(InterruptedException e) {
                e.printStackTrace();
            }
            Intent intent = new Intent(Splash.this, Login.class);
            startActivity(intent);
            Splash.this.finish();
            }
        }
    }

