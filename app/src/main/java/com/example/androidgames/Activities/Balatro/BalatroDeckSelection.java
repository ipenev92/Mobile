package com.example.androidgames.Activities.Balatro;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidgames.R;

public class BalatroDeckSelection extends AppCompatActivity {
    private final String[] decks = {
            "deck_red", "deck_blue", "deck_yellow", "deck_green", "deck_black", "deck_magic",
            "deck_nebula", "deck_ghost", "deck_abandoned", "deck_checkered", "deck_zodiac",
            "deck_painted", "deck_anaglyph", "deck_plasma", "deck_erratic"};
    private final String[] deckNames = {
            "Red Deck", "Blue Deck", "Yellow Deck", "Green Deck", "Black Deck", "Magic Deck",
            "Nebula Deck", "Ghost Deck", "Abandoned Deck", "Checkered Deck", "Zodiac Deck",
            "Painted Deck", "Anaglyph Deck", "Plasma Deck", "Erratic Deck"
    };
    private final String[] deckDescriptions = {
            "+1 discard every round",
            "+1 hand every round",
            "Start with extra $10",
            "At end of each round:\n$2 per remaining Hand\n$1 per remaining Discard\nEarn no Interest",
            "+1 Joker slot,\nbut -1 hand every round",
            "Start run with the\nCrystal Ball voucher\nand 2 copies of The Fool",
            "Start run with the\nTelescope voucher\nbut -1 consumable slot",
            "Spectral Cards may appear\nindividually in the shop,\nand you start with a Hex card",
            "Start run with no Face Cards\n(Jacks, Queens or Kings)\nin your deck",
            "Start run with 26 Spades\nand 26 Hearts in deck,\nand no Clubs or Diamonds",
            "Start the run with Tarot\nMerchant, Planet Merchant,\nand Overstock vouchers",
            "+2 Hand Size, -1 Joker Slot",
            "After defeating each Boss\nBlind, gain a Double Tag",
            "Balance Chips and Mult\nwhen calculating score\nfor played hand.\nx2 base Blind size",
            "All Ranks and Suits\nin deck are randomized"
    };
    private int currentDeckIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deck_selection);

        ImageView image = findViewById(R.id.deckImage);
        TextView name = findViewById(R.id.deck_name);
        TextView description = findViewById(R.id.deck_description);
        Button buttonPlay = findViewById(R.id.button_play);

        ImageButton left = findViewById(R.id.button_left_deck);
        left.setOnClickListener(v -> showPreviousDeck(image, name, description));

        ImageButton right = findViewById(R.id.button_right_deck);
        right.setOnClickListener(v -> showNextDeck(image, name, description));

        buttonPlay.setOnClickListener(v -> {
            Intent intent = new Intent(BalatroDeckSelection.this, BalatroBlind.class);
            intent.putExtra("deck", deckNames[currentDeckIndex]);
            startActivity(intent);
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