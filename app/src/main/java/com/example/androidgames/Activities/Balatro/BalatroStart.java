package com.example.androidgames.Activities.Balatro;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidgames.R;

public class BalatroStart extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.balatro_start);

        // Button: Play
        Button buttonPlay = findViewById(R.id.button_play);
        buttonPlay.setOnClickListener(v -> {
            Intent intent = new Intent(BalatroStart.this, BalatroDeckSelection.class);
            startActivity(intent);
        });

        // Button: How To
        Button buttonHowTo = findViewById(R.id.button_howTo);
        buttonHowTo.setOnClickListener(v -> {
            Intent intent = new Intent(BalatroStart.this, BalatroHowTo.class);
            startActivity(intent);
        });

        // Button: Exit
        Button buttonExit = findViewById(R.id.button_exit);
        buttonExit.setOnClickListener(v -> finish());
    }
}