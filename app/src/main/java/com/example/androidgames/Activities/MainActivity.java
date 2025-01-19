package com.example.androidgames.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androidgames.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up button for 2048
        Button button2048 = findViewById(R.id.button_2048);
        button2048.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MovingGame.class);
                startActivity(intent);
            }
        });

        // Set up button for Balatro
        Button buttonBalatro = findViewById(R.id.button_balatro);
        buttonBalatro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Balatro.class);
                startActivity(intent);
            }
        });

        // Set up button for How to Play
        Button buttonHowToPlay = findViewById(R.id.button_how_to_play);
        buttonHowToPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HowTo.class);
                startActivity(intent);
            }
        });

        // Set up button for Exit
        Button buttonExit = findViewById(R.id.button_exit);
        buttonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Close the app
            }
        });
    }
}