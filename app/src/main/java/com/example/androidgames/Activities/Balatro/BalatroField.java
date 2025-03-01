package com.example.androidgames.Activities.Balatro;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidgames.Activities.Balatro.Components.Ante;
import com.example.androidgames.Activities.Balatro.Components.CardSelectionListener;
import com.example.androidgames.Activities.Balatro.Components.GameData;
import com.example.androidgames.Activities.Balatro.Components.HandType;
import com.example.androidgames.Activities.Balatro.Components.PlayingCard;
import com.example.androidgames.Activities.Balatro.Components.PokerHandEvaluator;
import com.example.androidgames.R;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

@SuppressLint("DiscouragedApi")
public class BalatroField extends AppCompatActivity {
    private ArrayList<PlayingCard> deck;
    private ArrayList<PlayingCard> deckCopy;
    private GameData gameData;
    private final ArrayList<PlayingCard> selectedCards = new ArrayList<>();
    private static final int MAX_SELECTED_CARDS = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.balatro_field);

        this.gameData = getIntent().getParcelableExtra("gameData");

        this.deck = this.gameData.getDeck().getDeck();
        this.deckCopy = new ArrayList<>(deck);

        updateGameData();
        drawCardsToBoard();
        setButtonListeners();
    }

    private void updateGameData() {
        TextView nameText = findViewById(R.id.name);
        nameText.setText(this.getBlindName());

        setBlindImage(this.gameData.getAnte());

        TextView scoreMinText = findViewById(R.id.chipsNeeded);
        scoreMinText.setText(getChipsNeeded());

        TextView reward_text = findViewById(R.id.reward_text);
        reward_text.setText(getReward(this.gameData.getAnte().getStage()));

        TextView bossText = findViewById(R.id.boss_text);
        bossText.setText(this.gameData.getAnte().getStage() == 3 ?
                this.gameData.getAnte().getBossEffectText() : "");

        TextView handsText = findViewById(R.id.hands);
        TextView discardsText = findViewById(R.id.discards);
        TextView goldText = findViewById(R.id.gold);
        TextView anteText = findViewById(R.id.ante);
        TextView roundText = findViewById(R.id.round);

        handsText.setText(this.gameData.getHands());
        discardsText.setText(this.gameData.getDiscards());
        goldText.setText(this.gameData.getGold());
        anteText.setText(String.valueOf(this.gameData.getAnte().getAnte()));
        roundText.setText(String.valueOf(this.gameData.getAnte().getRound()));
    }

    private String getBlindName() {
        if (this.gameData.getAnte().getStage() == 1) {
            return "Small Blind";
        } else if (this.gameData.getAnte().getStage() == 2) {
            return "Big Blind";
        } else {
            return this.gameData.getAnte().getBossName();
        }
    }

    private String getChipsNeeded() {
        return this.gameData.getAnte().getStage() == 1 ? this.gameData.getAnte().getSmallBlind() :
                this.gameData.getAnte().getStage() == 2 ? this.gameData.getAnte().getBigBlind() :
                        this.gameData.getAnte().getBossBlind();
    }

    private String getReward(int stage) {
        return stage == 1 ? "Reward: $$$" : stage == 2 ? "Reward: $$$$" : "Reward: $$$$$";
    }

    private void setBlindImage(Ante ante) {
        String image;
        ImageView blindImage = findViewById(R.id.blind_image);

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

    private void drawCardsToBoard() {
        Random random = new Random();
        LinearLayout handLayout = findViewById(R.id.hand);

        // Clear UI first
        handLayout.removeAllViews();

        // Ensure PlayingField does not exceed 8 cards
        while (gameData.getPlayingField().size() < 8 && !deckCopy.isEmpty()) {
            int number = random.nextInt(deckCopy.size());
            PlayingCard selectedCard = deckCopy.remove(number);
            gameData.getPlayingField().add(selectedCard);

            // Debugging
            Log.d("BalatroField", "Drawing card: " + selectedCard.getName());
        }

        // If PlayingField is empty and deckCopy is empty, log an error
        if (gameData.getPlayingField().isEmpty()) {
            Log.e("BalatroField", "No cards in deck or playing field!");
            return;
        }

        // Draw all cards currently in PlayingField
        float weightPerCard = 1.0f / gameData.getPlayingField().size();
        for (PlayingCard card : gameData.getPlayingField()) {
            int id = getResources().getIdentifier(card.getName(), "drawable", getPackageName());

            if (id == 0) {
                Log.e("BalatroField", "Card image not found: " + card.getName());
                continue;
            }

            ImageView cardImageView = new ImageView(this);
            cardImageView.setImageResource(id);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    0, ViewGroup.LayoutParams.WRAP_CONTENT, weightPerCard);
            params.setMargins(10, 10, 10, 10);
            cardImageView.setLayoutParams(params);
            cardImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

            // Set tag for selection tracking
            cardImageView.setTag(card);
            cardImageView.setOnClickListener(view -> toggleCardSelection(view, card));

            // Add card to the hand layout
            handLayout.addView(cardImageView);
        }

        sortByRank(gameData.getPlayingField());
        updateDeck(deckCopy.size(), deck.size());
    }

    private void toggleCardSelection(View view, PlayingCard card) {
        if (selectedCards.contains(card)) {
            selectedCards.remove(card);
            view.setBackgroundResource(0);
        } else {
            if (selectedCards.size() < MAX_SELECTED_CARDS) {
                selectedCards.add(card);
                view.setBackgroundResource(R.drawable.card_selected_border); // Apply border effect
            } else {
                Toast.makeText(this, "You can only select up to 5 cards!",
                        Toast.LENGTH_SHORT).show();
            }
        }

        selectionListener.onSelectionChanged(gameData, selectedCards);
    }

    private final CardSelectionListener selectionListener = (gameData, cards) -> {
        TextView currentScoreText = findViewById(R.id.current_score);
        TextView chipsText = findViewById(R.id.chips);
        TextView multText = findViewById(R.id.mult);

        HandType hand = getHandType(cards);
        int chips = gameData.getPlanetData().getChipsByHand(hand);
        int mult = gameData.getPlanetData().getMultByHand(hand);
        int handLevel = gameData.getPlanetData().getLevelByHand(hand);

        if (hand == null) {
            currentScoreText.setText("");
            chipsText.setText("0");
            multText.setText("0");
        } else {
            currentScoreText.setText(String.format(Locale.US, "%s - Lvl %d", hand, handLevel));
            chipsText.setText(String.valueOf(chips));
            multText.setText(String.valueOf(mult));
        }
    };

    private HandType getHandType(ArrayList<PlayingCard> cards) {
        return PokerHandEvaluator.evaluateHand(cards);
    }

    private void updateDeck(int current, int max) {
        TextView playButton = findViewById(R.id.deck_size);
        playButton.setText(String.format("%s/%s", current, max));
    }

    private void setButtonListeners() {
        Button playButton = findViewById(R.id.play);
        Button discardButton = findViewById(R.id.discard);
        Button rankButton = findViewById(R.id.rank);
        Button suitButton = findViewById(R.id.suit);

        playButton.setOnClickListener(v -> {
            playHand(this.gameData.getPlayingField());
        });

        discardButton.setOnClickListener(v -> {
            discardHand();
        });

        rankButton.setOnClickListener(v -> {
            sortByRank(this.gameData.getPlayingField());
        });

        suitButton.setOnClickListener(v -> {
            sortBySuit(this.gameData.getPlayingField());
        });
    }

    private void playHand(ArrayList<PlayingCard> cards) {
        if (this.selectedCards.isEmpty()) {
            Toast.makeText(this, "Select at least one card to discard!", Toast.LENGTH_SHORT).show();
            return;
        }

        HandType hand = getHandType(cards);

        TextView chipsText = findViewById(R.id.chips);
        TextView multText = findViewById(R.id.mult);
        TextView currentScoreText = findViewById(R.id.current_score);
        TextView roundScoreText = findViewById(R.id.round_score);
        TextView handsText = findViewById(R.id.hands);

        int chips = this.gameData.getPlanetData().getChipsByHand(hand);
        int mult = this.gameData.getPlanetData().getMultByHand(hand);
        int handLevel = this.gameData.getPlanetData().getLevelByHand(hand);

        currentScoreText.setText(String.format(Locale.US, "%s - Lvl %d", hand, handLevel));

        for (PlayingCard card : cards) {
            chips += card.getChips();
            mult += card.getMult();

            chipsText.setText(String.valueOf(chips));
            multText.setText(String.valueOf(mult));
        }

        int finalScore = chips * mult;
        int roundScoreData = this.gameData.getRoundScore();
        this.gameData.setRoundScore(roundScoreData + finalScore);
        roundScoreText.setText(String.valueOf(this.gameData.getRoundScore()));
        currentScoreText.setText("");

        this.gameData.getPlayingField().removeAll(this.selectedCards);
        this.selectedCards.clear();
        drawCardsToBoard();

        int remainingHands = Integer.parseInt(this.gameData.getHands()) - 1;
        this.gameData.setHands(String.valueOf(remainingHands));
        handsText.setText(this.gameData.getHands());

        TextView blindPoints = findViewById(R.id.chipsNeeded);
        if (this.gameData.getRoundScore() >= Integer.parseInt(blindPoints.getText().toString())) {
            Log.e("Round passed", "passed");
        }
    }

    private void discardHand() {
        if (this.selectedCards.isEmpty()) {
            Toast.makeText(this, "Select at least one card to discard!", Toast.LENGTH_SHORT).show();
            return;
        }

        this.gameData.getPlayingField().removeAll(this.selectedCards);
        this.selectedCards.clear();
        drawCardsToBoard();

        int remainingDiscards = Integer.parseInt(this.gameData.getDiscards()) - 1;
        this.gameData.setDiscards(String.valueOf(remainingDiscards));
        TextView discardText = findViewById(R.id.discards);
        discardText.setText(this.gameData.getDiscards());
    }

    private void sortByRank(ArrayList<PlayingCard> playingField) {
        playingField.sort((card1, card2) -> {
            int rankComparison = Integer.compare(getRankValue(card2.getValue()), getRankValue(card1.getValue())); // Descending order
            if (rankComparison != 0) {
                return rankComparison;
            }
            return compareSuits(card1.getSuit(), card2.getSuit()); // Sort suits in the given order
        });

        refreshBoard();
    }

    private void sortBySuit(ArrayList<PlayingCard> playingField) {
        playingField.sort((card1, card2) -> {
            int suitComparison = compareSuits(card1.getSuit(), card2.getSuit());
            if (suitComparison != 0) {
                return suitComparison;
            }
            return Integer.compare(getRankValue(card2.getValue()), getRankValue(card1.getValue())); // Sort ranks in descending order within the same suit
        });

        refreshBoard();
    }

    private int getRankValue(String rank) {
        switch (rank.toLowerCase()) {
            case "ace": return 14;
            case "king": return 13;
            case "queen": return 12;
            case "jack": return 11;
            default: return Integer.parseInt(rank);
        }
    }

    private int compareSuits(String suit1, String suit2) {
        String[] suitOrder = {"spades", "hearts", "clubs", "diamonds"};
        int index1 = java.util.Arrays.asList(suitOrder).indexOf(suit1.toLowerCase());
        int index2 = java.util.Arrays.asList(suitOrder).indexOf(suit2.toLowerCase());
        return Integer.compare(index1, index2);
    }

    private void refreshBoard() {
        LinearLayout handLayout = findViewById(R.id.hand);
        handLayout.removeAllViews();

        for (PlayingCard card : this.gameData.getPlayingField()) {
            ImageView cardImageView = new ImageView(this);
            int id = getResources().getIdentifier(card.getName(), "drawable", getPackageName());
            cardImageView.setImageResource(id);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f / gameData.getPlayingField().size()
            );
            params.setMargins(10, 10, 10, 10);
            cardImageView.setLayoutParams(params);
            cardImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

            cardImageView.setTag(card);
            cardImageView.setOnClickListener(view -> toggleCardSelection(view, card));
            handLayout.addView(cardImageView);
        }
    }
}