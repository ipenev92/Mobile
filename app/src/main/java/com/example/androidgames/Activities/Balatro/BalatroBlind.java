package com.example.androidgames.Activities.Balatro;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidgames.Activities.Balatro.Components.Ante;
import com.example.androidgames.Activities.Balatro.Components.GameData;
import com.example.androidgames.R;

public class BalatroBlind extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.balatro_blind);

        String selectedDeck = getIntent().getStringExtra("deck");
        GameData gameData = new GameData(selectedDeck);

        setDeckImage(selectedDeck);
        setBlindsData(gameData);
        setBossBlindImage(gameData.getAnte());
        disableButtons(gameData.getAnte());
        setButtonListeners(gameData);
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

    private void setBlindsData(GameData gameData) {
        TextView handsText = findViewById(R.id.hands);
        TextView discardsText = findViewById(R.id.discards);
        TextView goldText = findViewById(R.id.gold);
        TextView roundText = findViewById(R.id.round);
        TextView smallBlindPointsText = findViewById(R.id.small_blind_points);
        TextView bigBlindPointsText = findViewById(R.id.big_blind_points);
        TextView bossBlindName = findViewById(R.id.boss_blind_name);
        TextView bossBlindEffectText = findViewById(R.id.boss_blind_text);
        TextView bossBlindPointsText = findViewById(R.id.boss_blind_points);

        handsText.setText(gameData.getHands());
        discardsText.setText(gameData.getDiscards());
        goldText.setText(gameData.getGold());
        roundText.setText(gameData.getRound());

        smallBlindPointsText.setText(gameData.getAnte().getSmallBlind());
        bigBlindPointsText.setText(gameData.getAnte().getBigBlind());
        bossBlindName.setText(gameData.getAnte().getBossName());
        bossBlindPointsText.setText(gameData.getAnte().getBossBlind());
        bossBlindEffectText.setText(gameData.getAnte().getBossEffectText());
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

    private void setButtonListeners(GameData gameData) {
        Button smallBlindButton = findViewById(R.id.small_blind_button);
        Button bigBlindButton = findViewById(R.id.big_blind_button);
        Button bossBlindButton = findViewById(R.id.boss_blind_button);

        View.OnClickListener blindButtonClickListener = v -> {
            Intent intent = new Intent(BalatroBlind.this, BalatroField.class);

            int stage = (v == smallBlindButton) ? 1 : (v == bigBlindButton) ? 2 : 3;
            int ante = (v == smallBlindButton) ?
                    gameData.getAnte().getAnte()+1 : gameData.getAnte().getAnte();

            gameData.getAnte().setStage(stage);
            gameData.getAnte().setAnte(ante);
            gameData.getAnte().setRound(gameData.getAnte().getRound()+1);
            intent.putExtra("gameData", gameData);
            startActivity(intent);
        };

        smallBlindButton.setOnClickListener(blindButtonClickListener);
        bigBlindButton.setOnClickListener(blindButtonClickListener);
        bossBlindButton.setOnClickListener(blindButtonClickListener);
    }
}