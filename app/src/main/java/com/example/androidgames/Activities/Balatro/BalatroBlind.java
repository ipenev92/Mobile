package com.example.androidgames.Activities.Balatro;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidgames.Activities.Balatro.Components.Ante;
import com.example.androidgames.Activities.Balatro.Components.Utils;
import com.example.androidgames.R;

public class BalatroBlind extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.balatro_blind);

        TextView anteText = findViewById(R.id.ante);
        Ante ante = new Ante(Integer.parseInt(anteText.getText().toString().split("/")[0]));

        String selectedDeck = getIntent().getStringExtra("deck");
        setDeckImage(selectedDeck);
        setBossBlindImage(ante);
        setBlindsData(selectedDeck, ante);
        disableButtons(ante);
        setButtonListeners();
    }

    private void setDeckImage(String deck) {
        ImageView deckImage = findViewById(R.id.deck);
        int deckImageName = getResources().getIdentifier(deck,
                "drawable", getPackageName());
        deckImage.setImageResource(deckImageName);
    }

    private void setBossBlindImage(Ante ante) {
        ImageView bossBlindImage = findViewById(R.id.boss_blind_image);
        int bossImageName = getResources().getIdentifier(ante.getBossImage(),
                "drawable", getPackageName());
        bossBlindImage.setImageResource(bossImageName);
    }

    private void setBlindsData(String deck, Ante ante) {
        TextView handsText = findViewById(R.id.hands);
        TextView discardsText = findViewById(R.id.discards);
        TextView goldText = findViewById(R.id.gold);
        TextView smallBlindPointsText = findViewById(R.id.small_blind_points);
        TextView bigBlindPointsText = findViewById(R.id.big_blind_points);
        TextView bossBlindName = findViewById(R.id.boss_blind_name);
        TextView bossBlindEffectText = findViewById(R.id.boss_blind_text);
        TextView bossBlindPointsText = findViewById(R.id.boss_blind_points);

        handsText.setText(Utils.getHands(deck));
        discardsText.setText(Utils.getDiscards(deck));
        goldText.setText(Utils.getGold(deck));

        smallBlindPointsText.setText(ante.getSmallBlind());
        bigBlindPointsText.setText(ante.getBigBlind());
        bossBlindName.setText(ante.getBossName());
        bossBlindPointsText.setText(ante.getBossBlind());
        bossBlindEffectText.setText(ante.getBossEffectText());
    }

    private void disableButtons(Ante ante) {
        Button smallBlindButton = findViewById(R.id.small_blind_button);
        Button bigBlindButton = findViewById(R.id.big_blind_button);
        Button bossBlindButton = findViewById(R.id.boss_blind_button);

        String upcoming = "Upcoming...";
        String select = "Select";
        smallBlindButton.setText(upcoming);
        bigBlindButton.setText(upcoming);
        bossBlindButton.setText(upcoming);
        smallBlindButton.setClickable(false);
        bigBlindButton.setClickable(false);
        bossBlindButton.setClickable(false);
        smallBlindButton.setBackgroundColor(Color.GRAY);
        bigBlindButton.setBackgroundColor(Color.GRAY);
        bossBlindButton.setBackgroundColor(Color.GRAY);

        if (ante.getStage() == 1) {
            smallBlindButton.setText(select);
            smallBlindButton.setClickable(true);
            smallBlindButton.setBackgroundColor(Color.BLUE);
        } else if (ante.getStage() == 2) {
            bigBlindButton.setText(select);
            bigBlindButton.setClickable(true);
            bigBlindButton.setBackgroundColor(Color.BLUE);
        } else if (ante.getStage() == 3) {
            bossBlindButton.setText(select);
            bossBlindButton.setClickable(true);
            bossBlindButton.setBackgroundColor(Color.BLUE);
        }
    }

    private void setButtonListeners() {
        Button smallBlindButton = findViewById(R.id.small_blind_button);
        Button bigBlindButton = findViewById(R.id.big_blind_button);
        Button bossBlindButton = findViewById(R.id.boss_blind_button);

        smallBlindButton.setOnClickListener(v -> {
            Intent intent = new Intent(BalatroBlind.this, BalatroField.class);
            intent.putExtra("deck", decks[currentDeckIndex]);
            startActivity(intent);
        });
    }
}