package com.example.androidgames.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androidgames.Activities.Balatro.BalatroStart;
import com.example.androidgames.Activities.MovingGame.MovingGame;
import com.example.androidgames.Activities.MovingGame.MovingGameRanking;
import com.example.androidgames.R;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button2048 = findViewById(R.id.button_2048);
        button2048.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MovingGame.class);
            startActivity(intent);
        });

        Button buttonRanking = findViewById(R.id.button_ranking_2048);
        buttonRanking.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MovingGameRanking.class);
            startActivity(intent);
        });

        Button buttonBalatro = findViewById(R.id.button_balatro);
        buttonBalatro.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, BalatroStart.class);
            startActivity(intent);
        });

        Button buttonExit = findViewById(R.id.button_exit);
        buttonExit.setOnClickListener(v -> finish());
    }
}