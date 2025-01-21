package com.example.androidgames.Activities.Balatro;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidgames.R;

public class BalatroStart extends AppCompatActivity {

    private final String[] decks = {"deck_red", "deck_blue", "deck_yellow", "deck_green"};
    private final String[] deckNames = {"Red Deck", "Blue Deck", "Yellow Deck", "Green Deck"};
    private final String[] deckDescriptions = {
            "+1 discard every round",
            "+1 hand every round",
            "Start with extra $10",
            "At end of each round:\n$2 per remaining Hand\n$1 per remaining Discard\nEarn no Interest"
    };
    private int currentDeckIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.balatro_start);

        ImageView image = findViewById(R.id.deckImage);
        TextView name = findViewById(R.id.deck_name);
        TextView description = findViewById(R.id.deck_description);

        ImageButton left = findViewById(R.id.button_left_deck);
        left.setOnClickListener(v -> showPreviousDeck(image, name, description));

        ImageButton right = findViewById(R.id.button_right_deck);
        right.setOnClickListener(v -> showNextDeck(image, name, description));

        Button button2048 = findViewById(R.id.button_play);
        button2048.setOnClickListener(v -> {
            Intent intent = new Intent(BalatroStart.this, BalatroBlind.class);
            intent.putExtra("deck", deckNames[currentDeckIndex]);
            startActivity(intent);
        });

        Button buttonBalatro = findViewById(R.id.button_howTo);
        buttonBalatro.setOnClickListener(v -> {
            Intent intent = new Intent(BalatroStart.this, BalatroHowTo.class);
            startActivity(intent);
        });

        Button buttonExit = findViewById(R.id.button_exit);
        buttonExit.setOnClickListener(v -> {
            finish();
        });

        updateDeckDisplay(image, name, description);
    }

    private void showPreviousDeck(ImageView deckImageView, TextView name, TextView description) {
        if (currentDeckIndex > 0) {
            currentDeckIndex--;
        } else {
            currentDeckIndex = decks.length - 1;
        }
        updateDeckDisplay(deckImageView, name, description);
    }

    private void showNextDeck(ImageView image, TextView name, TextView description) {
        if (currentDeckIndex < decks.length - 1) {
            currentDeckIndex++;
        } else {
            currentDeckIndex = 0;
        }
        updateDeckDisplay(image, name, description);
    }

    private void updateDeckDisplay(ImageView image, TextView name, TextView description) {
        String currentDeck = decks[currentDeckIndex];
        int imageResId = getResources().getIdentifier(currentDeck, "drawable", getPackageName());
        image.setImageResource(imageResId);

        name.setText(deckNames[currentDeckIndex]);
        description.setText(deckDescriptions[currentDeckIndex]);
    }
}