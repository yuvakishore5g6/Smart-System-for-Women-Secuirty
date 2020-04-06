package com.example.ss_ws;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

public class RatingActivity extends AppCompatActivity {

    RatingBar ratingBar;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        ratingBar = findViewById(R.id.ratingBar);
        button = findViewById(R.id.rateBut);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float ratingValue = ratingBar.getRating();
                if(ratingValue >= 3)
                    Toast.makeText(RatingActivity.this, R.string.thank,Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(RatingActivity.this, R.string.working ,Toast.LENGTH_SHORT).show();
            }
        });
    }
}
