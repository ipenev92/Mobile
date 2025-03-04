package com.example.androidgames.Activities.Balatro;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidgames.R;

public class BalatroGameOver extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        Button restartButton = findViewById(R.id.restart_button);
        restartButton.setOnClickListener(v -> {
            Intent intent = new Intent(BalatroGameOver.this, BalatroStart.class);
            startActivity(intent);
        });
    }
}
