package com.example.androidgames.Activities.Balatro;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
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
public class BalatroWin extends AppCompatActivity {
    private GameData gameData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.balatro_win);

        this.gameData = GameDataHolder.gameData;

        this.updateGameData();
        setButtonListener();
        displayJokers();
        displayConsumables();
    }

    private void updateGameData() {
        Button buttonContinueText = findViewById(R.id.button_continue);
        int reward = Integer.parseInt(this.gameData.getCurrentHands()) +
                getReward(this.gameData.getAnte().getStage());
        buttonContinueText.setText(String.format(Locale.US, "Cash Out: $%d", reward));

        TextView scoreMinText = findViewById(R.id.blind_chips);
        scoreMinText.setText(String.format(Locale.US,
                "Score at least \n %s", getChipsNeeded()));

        TextView rewardText = findViewById(R.id.blind_reward);
        rewardText.setText("$".repeat(getReward(this.gameData.getAnte().getStage())));

        TextView goldGainedHands = findViewById(R.id.gold_gained);
        goldGainedHands.setText(String.format("%s Remaining Hands ($1 each)",
                this.gameData.getCurrentHands()));

        TextView handRewardText = findViewById(R.id.hand_reward);
        handRewardText.setText("$".repeat(Integer.parseInt(this.gameData.getCurrentHands())));

        TextView handsText = findViewById(R.id.hands);
        TextView discardsText = findViewById(R.id.discards);
        TextView goldText = findViewById(R.id.gold);
        TextView anteText = findViewById(R.id.ante);
        TextView roundText = findViewById(R.id.round);

        handsText.setText(this.gameData.getCurrentHands());
        discardsText.setText(this.gameData.getCurrentDiscards());
        goldText.setText(String.format("$%s", this.gameData.getCurrentGold()));
        anteText.setText(String.valueOf(this.gameData.getAnte().getAnte()));
        roundText.setText(String.valueOf(this.gameData.getAnte().getRound()));

        setBlindImage(this.gameData.getAnte());
        setDeckImage(gameData.getDeckName());

        this.gameData.setCurrentHands(this.gameData.getHands());
        this.gameData.setCurrentDiscards(this.gameData.getCurrentDiscards());

        int currentGold = Integer.parseInt(this.gameData.getCurrentGold()) + reward;
        this.gameData.setCurrentGold(String.valueOf(currentGold));
    }

    private String getChipsNeeded() {
        return this.gameData.getAnte().getStage() == 1 ? this.gameData.getAnte().getSmallBlind() :
                this.gameData.getAnte().getStage() == 2 ? this.gameData.getAnte().getBigBlind() :
                        this.gameData.getAnte().getBossBlind();
    }

    private int getReward(int stage) {
        return stage == 1 ? 3 : stage == 2 ? 4 : 5;
    }

    private void setBlindImage(Ante ante) {
        String image;
        ImageView blindImage = findViewById(R.id.blind_icon);

        if (ante.getStage() == 1) {
            image = "blind_small";
        } else if (ante.getStage() == 2) {
            image = "blind_big";
        } else {
            image = ante.getBossImage();
        }

        int bossImageName = getResources().getIdentifier(image,
                "drawable", getPackageName());
        blindImage.setImageResource(bossImageName);
    }

    private void setDeckImage(String deck) {
        ImageView deckImage = findViewById(R.id.deck);
        int deckImageName = getResources().getIdentifier(deck,
                "drawable", getPackageName());
        deckImage.setImageResource(deckImageName);
    }

    private void setButtonListener() {
        Button buttonContinueText = findViewById(R.id.button_continue);

        buttonContinueText.setOnClickListener(v -> {
            GameDataHolder.gameData = this.gameData;
            Intent intent = new Intent(BalatroWin.this, BalatroShop.class);
            startActivity(intent);
        });
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