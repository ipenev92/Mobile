package com.example.androidgames.Activities.Balatro;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidgames.Activities.Balatro.Components.Ante;
import com.example.androidgames.Activities.Balatro.Components.GameData;
import com.example.androidgames.Activities.Balatro.Components.GameDataHolder;
import com.example.androidgames.Activities.Balatro.Components.Joker;
import com.example.androidgames.Activities.Balatro.Components.TarotCard;
import com.example.androidgames.R;

import java.util.List;
import java.util.Locale;

@SuppressLint("DiscouragedApi")
public class BalatroBlind extends AppCompatActivity {
    private GameData gameData;
    private String selectedDeck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.balatro_blind);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        if (GameDataHolder.gameData != null) {
            this.gameData = GameDataHolder.gameData;
            this.selectedDeck = gameData.getDeckName();
        } else {
            this.selectedDeck = getIntent().getStringExtra("deck");

            if (this.selectedDeck == null) {
                Log.e("BalatroBlind", "selectedDeck is null! Ensure it's passed correctly.");
                finish();
                return;
            }

            gameData = new GameData(selectedDeck);
            GameDataHolder.gameData = gameData;
        }

        setDeckImage();
        setBlindsData();
        setBossBlindImage();
        disableButtons();
        setButtonListeners();
        displayJokers();
        displayConsumables();
    }

    private void setDeckImage() {
        ImageView deckImage = findViewById(R.id.deck);
         int deckImageName = getResources().getIdentifier(this.selectedDeck,
                "drawable", getPackageName());
        deckImage.setImageResource(deckImageName);
    }

    private void setBossBlindImage() {
        ImageView bossBlindImage = findViewById(R.id.boss_blind_image);
        int bossImageName = getResources().getIdentifier(this.gameData.getAnte().getBossImage(),
                "drawable", getPackageName());
        bossBlindImage.setImageResource(bossImageName);
    }

    private void setBlindsData() {
        TextView handsText = findViewById(R.id.hands);
        TextView discardsText = findViewById(R.id.discards);
        TextView goldText = findViewById(R.id.gold);
        TextView anteText = findViewById(R.id.ante);
        TextView roundText = findViewById(R.id.round);
        TextView smallBlindPointsText = findViewById(R.id.small_blind_points);
        TextView bigBlindPointsText = findViewById(R.id.big_blind_points);
        TextView bossBlindName = findViewById(R.id.boss_blind_name);
        TextView bossBlindEffectText = findViewById(R.id.boss_blind_text);
        TextView bossBlindPointsText = findViewById(R.id.boss_blind_points);

        handsText.setText(gameData.getHands());
        discardsText.setText(gameData.getDiscards());
        goldText.setText(String.format("$%s", gameData.getGold()));
        anteText.setText(String.format(Locale.US, "%d/8", gameData.getAnte().getAnte()));
        roundText.setText(gameData.getRound());

        gameData.getAnte().updateBlinds();
        smallBlindPointsText.setText(gameData.getAnte().getSmallBlind());
        bigBlindPointsText.setText(gameData.getAnte().getBigBlind());
        bossBlindName.setText(gameData.getAnte().getBossName());
        bossBlindPointsText.setText(gameData.getAnte().getBossBlind());
        bossBlindEffectText.setText(gameData.getAnte().getBossEffectText());
    }

    private void disableButtons() {
        Ante ante = this.gameData.getAnte();

        Button smallBlindButton = findViewById(R.id.small_blind_button);
        Button bigBlindButton = findViewById(R.id.big_blind_button);
        Button bossBlindButton = findViewById(R.id.boss_blind_button);

        String select = "Select";
        String upcoming = "Upcoming...";
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

        View.OnClickListener blindButtonClickListener = v -> {
            Intent intent = new Intent(BalatroBlind.this, BalatroField.class);

            int stage = (v == smallBlindButton) ? 1 : (v == bigBlindButton) ? 2 : 3;

            gameData.getAnte().setStage(stage);
            gameData.getAnte().setRound(gameData.getAnte().getRound()+1);
            GameDataHolder.gameData = gameData;
            startActivity(intent);
        };

        smallBlindButton.setOnClickListener(blindButtonClickListener);
        bigBlindButton.setOnClickListener(blindButtonClickListener);
        bossBlindButton.setOnClickListener(blindButtonClickListener);
    }

    private void displayJokers() {
        LinearLayout jokersContainer = findViewById(R.id.jokers_container);

        if (jokersContainer == null || gameData == null) {
            return;
        }

        jokersContainer.removeAllViews();

        List<Joker> jokers = gameData.getJokers();

        for (Joker joker : jokers) {
            ImageView jokerImage = new ImageView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
            params.setMargins(5, 5, 5, 5);
            jokerImage.setLayoutParams(params);
            jokerImage.setScaleType(ImageView.ScaleType.FIT_CENTER);

            int drawableId = getResources().getIdentifier(joker.getName(), "drawable", getPackageName());
            if (drawableId != 0) {
                jokerImage.setImageResource(drawableId);
            }

            jokersContainer.addView(jokerImage);
        }

        jokersContainer.invalidate();
    }

    private void displayConsumables() {
        LinearLayout consumablesContainer = findViewById(R.id.consumables_container);

        consumablesContainer.removeAllViews();

        List<TarotCard> tarotCards = gameData.getTarotCards();

        for (TarotCard tarotCard : tarotCards) {
            ImageView consumableImage = new ImageView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
            params.setMargins(5, 5, 5, 5);
            consumableImage.setLayoutParams(params);
            consumableImage.setScaleType(ImageView.ScaleType.FIT_CENTER);

            int drawableId = getResources().getIdentifier(tarotCard.getName(), "drawable", getPackageName());
            if (drawableId != 0) {
                consumableImage.setImageResource(drawableId);
            }

            consumablesContainer.addView(consumableImage);
        }

        consumablesContainer.invalidate();
    }
}