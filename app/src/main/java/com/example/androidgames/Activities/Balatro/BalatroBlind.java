package com.example.androidgames.Activities.Balatro;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import com.example.androidgames.R;

public class BalatroBlind extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.balatro_blind);

        String selectedDeck = getIntent().getStringExtra("deck");
    }
}