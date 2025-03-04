package com.example.androidgames.activities.balatro;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.androidgames.activities.balatro.components.Ante;
import com.example.androidgames.activities.balatro.components.CardSelectionListener;
import com.example.androidgames.activities.balatro.components.GameData;
import com.example.androidgames.activities.balatro.components.GameDataHolder;
import com.example.androidgames.activities.balatro.components.HandType;
import com.example.androidgames.activities.balatro.components.Joker;
import com.example.androidgames.activities.balatro.components.PlayingCard;
import com.example.androidgames.activities.balatro.components.PokerHandEvaluator;
import com.example.androidgames.activities.balatro.components.TarotCard;
import com.example.androidgames.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

@SuppressLint("DiscouragedApi")
public class BalatroField extends AppCompatActivity {
    private static final String DRAWABLE = "drawable";
    private ArrayList<PlayingCard> deck;
    private ArrayList<PlayingCard> deckCopy;
    private GameData gameData;
    private final ArrayList<PlayingCard> selectedCards = new ArrayList<>();
    private static final int MAX_SELECTED_CARDS = 5;
    private final Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.balatro_field);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        this.gameData = GameDataHolder.gameData;
        this.deck = this.gameData.getDeck().getDeckList();
        this.deckCopy = new ArrayList<>(deck);

        this.updateGameData();
        this.drawCardsToBoard();
        this.setButtonListeners();
        this.displayJokers();
        this.displayConsumables();
    }

    private void updateGameData() {
        TextView nameText = findViewById(R.id.name);
        nameText.setText(this.getBlindName());

        setBlindImage(this.gameData.getAnte());

        TextView scoreMinText = findViewById(R.id.chipsNeeded);
        scoreMinText.setText(getChipsNeeded());

        TextView rewardText = findViewById(R.id.reward_text);
        rewardText.setText(getReward(this.gameData.getAnte().getStage()));

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
        goldText.setText(String.format("$%s", this.gameData.getGold()));
        anteText.setText(String.format(Locale.US, "%d/8",
                this.gameData.getAnte().getAnteValue()));
        roundText.setText(String.valueOf(this.gameData.getAnte().getRound()));

        setDeckImage(this.gameData.getDeckName());
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
        String bigBlind = this.gameData.getAnte().getStage() == 2 ?
                this.gameData.getAnte().getBigBlind() : this.gameData.getAnte().getBossBlind();
        return this.gameData.getAnte().getStage() == 1 ?
                this.gameData.getAnte().getSmallBlind() : bigBlind;
    }

    private void setDeckImage(String deck) {
        ImageView deckImage = findViewById(R.id.deck);
        int deckImageName = getResources().getIdentifier(deck,
                DRAWABLE, getPackageName());
        deckImage.setImageResource(deckImageName);
    }

    private String getReward(int stage) {
        String reward = stage == 2 ? "Reward: $$$$" : "Reward: $$$$$";
        return stage == 1 ? "Reward: $$$" : reward;
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
                DRAWABLE, getPackageName());
        blindImage.setImageResource(bossImageName);
    }

    private void drawCardsToBoard() {
        LinearLayout handLayout = findViewById(R.id.hand);
        handLayout.removeAllViews();

        while (this.gameData.getPlayingField().size() < 8 && !this.deckCopy.isEmpty()) {
            int number = this.random.nextInt(this.deckCopy.size());
            PlayingCard selectedCard = this.deckCopy.remove(number);
            this.gameData.getPlayingField().add(selectedCard);

        }

        if (this.gameData.getPlayingField().isEmpty()) {
            return;
        }

        float weightPerCard = 1.0f / this.gameData.getPlayingField().size();
        for (PlayingCard card : this.gameData.getPlayingField()) {
            int id = getResources().getIdentifier(card.getName(),
                    DRAWABLE, getPackageName());

            ImageView cardImageView = getImageView(card, id, weightPerCard);
            handLayout.addView(cardImageView);
        }

        if (this.gameData.getSortBy().equals("rank")) {
            this.sortByRank(this.gameData.getPlayingField());
        } else {
            this.sortBySuit(this.gameData.getPlayingField());
        }
        this.updateDeck(this.deckCopy.size(), this.deck.size());
    }

    private ImageView getImageView(PlayingCard card, int id, float weightPerCard) {
        ImageView cardImageView = new ImageView(this);
        cardImageView.setImageResource(id);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, weightPerCard);
        params.setMargins(10, 10, 10, 10);
        cardImageView.setLayoutParams(params);
        cardImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

        cardImageView.setTag(card);
        cardImageView.setOnClickListener(view -> toggleCardSelection(view, card));
        return cardImageView;
    }

    private void toggleCardSelection(View view, PlayingCard card) {
        if (this.selectedCards.contains(card)) {
            this.selectedCards.remove(card);
            view.setBackgroundResource(0);
        } else {
            if (this.selectedCards.size() < MAX_SELECTED_CARDS) {
                this.selectedCards.add(card);
                view.setBackgroundResource(R.drawable.card_selected_border);
            } else {
                Toast.makeText(this, "You can only select up to 5 cards!",
                        Toast.LENGTH_SHORT).show();
            }
        }

        this.selectionListener.onSelectionChanged(this.selectedCards);
    }

    private final CardSelectionListener selectionListener = cards -> {
        TextView currentScoreText = findViewById(R.id.current_score);
        TextView chipsText = findViewById(R.id.chips);
        TextView multText = findViewById(R.id.mult);

        HandType hand = getHandType(cards);
        int chips = this.gameData.getPlanetData().getChipsByHand(hand);
        int mult = this.gameData.getPlanetData().getMultByHand(hand);
        int handLevel = this.gameData.getPlanetData().getLevelByHand(hand);

        if (hand == null) {
            currentScoreText.setText("");
            chipsText.setText("0");
            multText.setText("0");
        } else {
            currentScoreText.setText(String.format(Locale.US,
                    "%s - Lvl %d", hand, handLevel));
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

        playButton.setOnClickListener(v -> playHand());
        discardButton.setOnClickListener(v -> discardHand());
        rankButton.setOnClickListener(v -> sortByRank(this.gameData.getPlayingField()));
        suitButton.setOnClickListener(v -> sortBySuit(this.gameData.getPlayingField()));
    }

    private void playHand() {
        if (this.selectedCards.isEmpty()) {
            Toast.makeText(this, "Select at least one card to play!",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        TextView currentScoreText = findViewById(R.id.current_score);
        TextView roundScoreText = findViewById(R.id.round_score);
        TextView handsText = findViewById(R.id.hands);
        TextView blindPoints = findViewById(R.id.chipsNeeded);

        HandType hand = getHandType(this.selectedCards);
        int chips = this.gameData.getPlanetData().getChipsByHand(hand);
        int mult = this.gameData.getPlanetData().getMultByHand(hand);
        int handLevel = this.gameData.getPlanetData().getLevelByHand(hand);

        currentScoreText.setText(String.format(Locale.US, "%s - Lvl %d", hand, handLevel));

        for (PlayingCard card : selectedCards) {
            chips += card.getChips();
            mult += card.getMult();
        }

        int finalScore = getFinalScore(chips, mult, hand);

        int roundScoreData = this.gameData.getRoundScore();
        this.gameData.setRoundScore(roundScoreData + finalScore);
        roundScoreText.setText(String.valueOf(this.gameData.getRoundScore()));
        currentScoreText.setText("");

        this.gameData.getPlayingField().removeAll(this.selectedCards);
        this.selectedCards.clear();
        drawCardsToBoard();

        int remainingHands = Integer.parseInt(this.gameData.getCurrentHands()) - 1;
        this.gameData.setCurrentHands(String.valueOf(remainingHands));
        handsText.setText(this.gameData.getCurrentHands());

        if (this.gameData.getRoundScore() >= Integer.parseInt(blindPoints.getText().toString())) {
            GameDataHolder.gameData = this.gameData;
            Intent intent = new Intent(BalatroField.this, BalatroWin.class);
            startActivity(intent);
        } else if (remainingHands <= 0) {
            Intent intent = new Intent(BalatroField.this, BalatroGameOver.class);
            startActivity(intent);
        }
    }

    private int getFinalScore(int chips, int mult, HandType hand) {
        for (Joker joker : this.gameData.getJokers()) {
            chips += getAdditionalChips(joker, chips, hand);
            mult += getAdditionalMult(joker, mult, hand);
            mult *= getMultiplicativeMult(joker, mult, hand);
        }

        TextView chipsText = findViewById(R.id.chips);
        TextView multText = findViewById(R.id.mult);
        chipsText.setText(String.valueOf(chips));
        multText.setText(String.valueOf(mult));

        return chips * mult;
    }

    private int getAdditionalChips(Joker joker, int chips, HandType hand) {
        int currentDiscards = Integer.parseInt(this.gameData.getCurrentDiscards());

        switch (joker.getName()) {
            case "joker_banner": chips += currentDiscards * 30; break;
            case "joker_clever":
                if (isHandAny(hand, HandType.TWO_PAIR, HandType.FULL_HOUSE)) {
                    chips += 80;
                }
                break;
            case "joker_crafty":
                if (hand == HandType.FLUSH) {
                    chips += 80;
                }
                break;
            case "joker_sly":
                if (isHandAny(hand, HandType.PAIR, HandType.THREE_OF_A_KIND,
                        HandType.TWO_PAIR, HandType.FULL_HOUSE,
                        HandType.FOUR_OF_A_KIND)) {
                    chips += 50;
                }
                break;
            case "joker_wily":
                if (isHandAny(hand, HandType.THREE_OF_A_KIND, HandType.FULL_HOUSE,
                        HandType.FOUR_OF_A_KIND)) {
                    chips += 100;
                }
                break;
            default:
        }

        return chips;
    }

    private int getAdditionalMult(Joker joker, int mult, HandType hand) {
        int jokerCount = this.gameData.getJokers().size();
        int currentDiscards = Integer.parseInt(this.gameData.getCurrentDiscards());

        switch (joker.getName()) {
            case "joker_abstract": mult += jokerCount * 3; break;
            case "joker_joker": mult += 4; break;
            case "joker_misprint": mult += this.random.nextInt(24); break;
            case "joker_crazy":
                if (hand == HandType.STRAIGHT) {
                    mult += 12;
                }
                break;
            case "joker_droll":
                if (hand == HandType.FLUSH) {
                    mult += 10;
                }
                break;
            case "joker_jolly":
                if (isHandAny(hand, HandType.PAIR, HandType.THREE_OF_A_KIND,
                        HandType.TWO_PAIR, HandType.FULL_HOUSE,
                        HandType.FOUR_OF_A_KIND)) {
                    mult += 8;
                }
                break;
            case "joker_mad":
                if (isHandAny(hand, HandType.TWO_PAIR, HandType.FULL_HOUSE)) {
                    mult += 10;
                }
                break;
            case "joker_mystic":
                if (currentDiscards == 0) {
                    mult += 15;
                }
                break;
            case "joker_zany":
                if (isHandAny(hand, HandType.THREE_OF_A_KIND, HandType.FULL_HOUSE,
                        HandType.FOUR_OF_A_KIND)) {
                    mult += 12;
                }
                break;
            default:
        }

        return mult;
    }

    private int getMultiplicativeMult(Joker joker, int mult, HandType hand) {
        switch (joker.getName()) {
            case "joker_duo":
                if (isHandAny(hand, HandType.PAIR, HandType.THREE_OF_A_KIND,
                        HandType.TWO_PAIR, HandType.FULL_HOUSE,
                        HandType.FOUR_OF_A_KIND)) {
                    mult *= 2;
                }
                break;
            case "joker_family":
                if (hand == HandType.FOUR_OF_A_KIND) {
                    mult *= 4;
                }
                break;

            case "joker_order":
                if (hand == HandType.STRAIGHT) {
                    mult *= 3;
                }
                break;
            case "joker_tribe":
                if (hand == HandType.FLUSH) {
                    mult *= 2;
                }
                break;
            case "joker_trio":
                if (isHandAny(hand, HandType.THREE_OF_A_KIND, HandType.FULL_HOUSE,
                        HandType.FOUR_OF_A_KIND)) {
                    mult *= 3;
                }
                break;
            default:
        }

        return mult;
    }

    private boolean isHandAny(HandType hand, HandType... types) {
        for (HandType type : types) {
            if (hand == type) {
                return true;
            }
        }
        return false;
    }


    private void discardHand() {
        if (this.selectedCards.isEmpty()) {
            Toast.makeText(this, "Select at least one card to discard!",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        this.gameData.getPlayingField().removeAll(this.selectedCards);
        this.selectedCards.clear();
        drawCardsToBoard();

        int remainingDiscards = Integer.parseInt(this.gameData.getCurrentDiscards()) - 1;
        this.gameData.setCurrentDiscards(String.valueOf(remainingDiscards));
        TextView discardText = findViewById(R.id.discards);
        discardText.setText(this.gameData.getCurrentDiscards());

        Button discardButton = findViewById(R.id.discard);
        if (Integer.parseInt(this.gameData.getCurrentDiscards()) <= 0) {
            discardButton.setClickable(false);
            discardButton.setBackgroundColor(ContextCompat.getColor(this,
                    android.R.color.darker_gray));

            Toast.makeText(this, "Out of discards!", Toast.LENGTH_SHORT).show();
        } else {
            discardButton.setClickable(true);
            discardButton.setBackgroundColor(ContextCompat.getColor(this,
                    android.R.color.holo_red_dark));
        }
    }

    private void sortByRank(ArrayList<PlayingCard> playingField) {
        playingField.sort((card1, card2) -> {
            int rankComparison = Integer.compare(getRankValue(card2.getValue()),
                    getRankValue(card1.getValue()));
            if (rankComparison != 0) {
                return rankComparison;
            }
            return compareSuits(card1.getSuit(), card2.getSuit());
        });

        this.gameData.setSortBy("rank");
        refreshBoard();
    }

    private void sortBySuit(ArrayList<PlayingCard> playingField) {
        playingField.sort((card1, card2) -> {
            int suitComparison = compareSuits(card1.getSuit(), card2.getSuit());
            if (suitComparison != 0) {
                return suitComparison;
            }
            return Integer.compare(getRankValue(card2.getValue()), getRankValue(card1.getValue()));
        });

        this.gameData.setSortBy("suit");
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
            int id = getResources().getIdentifier(card.getName(), DRAWABLE, getPackageName());
            cardImageView.setImageResource(id);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    0, ViewGroup.LayoutParams.WRAP_CONTENT,
                    1.0f / this.gameData.getPlayingField().size()
            );
            params.setMargins(10, 10, 10, 10);
            cardImageView.setLayoutParams(params);
            cardImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

            cardImageView.setTag(card);
            cardImageView.setOnClickListener(view -> toggleCardSelection(view, card));
            handLayout.addView(cardImageView);
        }
    }

    private void displayJokers() {
        LinearLayout jokersContainer = findViewById(R.id.jokers_container);

        if (jokersContainer == null || this.gameData == null) {
            return;
        }

        jokersContainer.removeAllViews();

        List<Joker> jokers = this.gameData.getJokers();

        for (Joker joker : jokers) {
            ImageView jokerImage = new ImageView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
            params.setMargins(5, 5, 5, 5);
            jokerImage.setLayoutParams(params);
            jokerImage.setScaleType(ImageView.ScaleType.FIT_CENTER);

            int drawableId = getResources().getIdentifier(joker.getName(),
                    DRAWABLE, getPackageName());
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
        List<TarotCard> tarotCards = this.gameData.getTarotCards();

        for (TarotCard tarotCard : tarotCards) {
            ImageView consumableImage = new ImageView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
            params.setMargins(5, 5, 5, 5);
            consumableImage.setLayoutParams(params);
            consumableImage.setScaleType(ImageView.ScaleType.FIT_CENTER);

            int drawableId = getResources().getIdentifier(tarotCard.getName(),
                    DRAWABLE, getPackageName());
            if (drawableId != 0) {
                consumableImage.setImageResource(drawableId);
            }

            consumablesContainer.addView(consumableImage);
        }

        consumablesContainer.invalidate();
    }
}