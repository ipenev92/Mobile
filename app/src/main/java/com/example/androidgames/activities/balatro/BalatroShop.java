package com.example.androidgames.activities.balatro;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.androidgames.activities.balatro.components.ConsumableSelectionListener;
import com.example.androidgames.activities.balatro.components.GameData;
import com.example.androidgames.activities.balatro.components.GameDataHolder;
import com.example.androidgames.activities.balatro.components.HandType;
import com.example.androidgames.activities.balatro.components.Joker;
import com.example.androidgames.activities.balatro.components.PlanetCard;
import com.example.androidgames.activities.balatro.components.PlanetData;
import com.example.androidgames.activities.balatro.components.TarotCard;
import com.example.androidgames.activities.balatro.components.Voucher;
import com.example.androidgames.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Random;

@SuppressLint({"DiscouragedApi", "UseCompatLoadingForColorStateLists"})
public class BalatroShop extends AppCompatActivity implements ConsumableSelectionListener {
    private static final String DRAWABLE = "drawable";
    private static final String ERROR = "Error";
    private GameData gameData;
    private ConsumableSelectionListener selectionListener;
    private ImageView selectedCardView = null;
    private String selectedCard = null;
    private final Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.balatro_shop);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        this.gameData = GameDataHolder.gameData;
        this.selectionListener = this;

        this.updateGameData();
        this.generateJokers();
        this.generateVoucher();
        this.generateConsumables();
        this.setupButtonListeners();
        this.displayJokers();
        this.displayConsumables();
    }

    private void updateGameData() {
        TextView handsText = findViewById(R.id.hands);
        TextView discardsText = findViewById(R.id.discards);
        TextView goldText = findViewById(R.id.gold);
        TextView anteText = findViewById(R.id.ante);
        TextView roundText = findViewById(R.id.round);

        handsText.setText(this.gameData.getCurrentHands());
        discardsText.setText(this.gameData.getCurrentDiscards());
        goldText.setText(String.format("$%s", this.gameData.getCurrentGold()));
        anteText.setText(String.format(Locale.US, "%d/8",
                this.gameData.getAnte().getAnteValue()));
        roundText.setText(String.valueOf(this.gameData.getAnte().getRound()));

        setDeckImage(this.gameData.getDeckName());
    }

    private void setDeckImage(String deck) {
        ImageView deckImage = findViewById(R.id.deck);
        int deckImageName = getResources().getIdentifier(deck,
                DRAWABLE, getPackageName());
        deckImage.setImageResource(deckImageName);
    }

    private void generateJokers() {
        ArrayList<Joker> jokers = getJokers();

        if (jokers.size() >= 3) {
            HashSet<String> selectedJokers = new HashSet<>();

            while (selectedJokers.size() < this.gameData.getShopSize()) {
                String randomJoker = jokers.get(this.random.nextInt(jokers.size())).getName();
                selectedJokers.add(randomJoker);
            }

            runOnUiThread(() -> {
                LinearLayout jokersLayout = findViewById(R.id.jokers_section);
                jokersLayout.removeAllViews();

                for (String jokerName : selectedJokers) {
                    int drawableId = getResources().getIdentifier(jokerName,
                            DRAWABLE, getPackageName());

                    if (drawableId != 0) {
                        ImageView jokerImage = getImageView(jokerName, drawableId);
                        jokersLayout.addView(jokerImage);
                    }
                }
            });
        }
    }

    @NonNull
    private ImageView getImageView(String jokerName, int drawableId) {
        ImageView jokerImage = new ImageView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0,
                ViewGroup.LayoutParams.MATCH_PARENT, 1);
        params.setMargins(5, 0, 5, 0);
        jokerImage.setLayoutParams(params);
        jokerImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
        jokerImage.setImageResource(drawableId);

        jokerImage.setOnClickListener(v -> toggleSelection(jokerImage, jokerName));
        return jokerImage;
    }

    private void generateVoucher() {
        ArrayList<Voucher> vouchers = getVouchers();

        if (this.gameData.getAnte().getStage() == 1 && !vouchers.isEmpty()) {
            String randomVoucherName = vouchers.get(this.random.nextInt(vouchers.size())).getName();
            int drawableId = getResources().getIdentifier(randomVoucherName,
                    DRAWABLE, getPackageName());

            if (drawableId != 0) {
                runOnUiThread(() -> {
                    LinearLayout vouchersLayout = findViewById(R.id.vouchers_section);
                    vouchersLayout.removeAllViews();

                    ImageView voucherImage = new ImageView(this);
                    voucherImage.setLayoutParams(
                            new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT));
                    voucherImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    voucherImage.setImageResource(drawableId);

                    voucherImage.setOnClickListener(v -> toggleSelection(voucherImage,
                            randomVoucherName));
                    vouchersLayout.addView(voucherImage);
                });
            }
        }
    }

    private void generateConsumables() {
        ArrayList<PlanetCard> planets = getPlanets();
        ArrayList<TarotCard> tarotCards = getTarotCards();

        if (!planets.isEmpty() && !tarotCards.isEmpty()) {
            String randomPlanetName = planets.get(this.random.nextInt(planets.size())).getName();
            String randomTarotName = tarotCards.get(this.random.nextInt(tarotCards.size()))
                    .getName();

            int planetDrawableId = getResources().getIdentifier(randomPlanetName,
                    DRAWABLE, getPackageName());
            int tarotDrawableId = getResources().getIdentifier(randomTarotName,
                    DRAWABLE, getPackageName());

            if (planetDrawableId != 0 && tarotDrawableId != 0) {
                runOnUiThread(() -> {
                    LinearLayout consumablesLayout = findViewById(R.id.consumables_section);
                    consumablesLayout.removeAllViews();

                    ImageView planetImage = new ImageView(this);
                    planetImage.setLayoutParams(new LinearLayout.LayoutParams(0,
                            ViewGroup.LayoutParams.MATCH_PARENT, 1));
                    planetImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    planetImage.setImageResource(planetDrawableId);
                    planetImage.setOnClickListener(v -> toggleSelection(planetImage,
                            randomPlanetName));

                    ImageView tarotImage = new ImageView(this);
                    tarotImage.setLayoutParams(new LinearLayout.LayoutParams(0,
                            ViewGroup.LayoutParams.MATCH_PARENT, 1));
                    tarotImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    tarotImage.setImageResource(tarotDrawableId);
                    tarotImage.setOnClickListener(v -> toggleSelection(tarotImage,
                            randomTarotName));

                    consumablesLayout.addView(planetImage);
                    consumablesLayout.addView(tarotImage);
                });
            }
        }
    }

    private ArrayList<Joker> getJokers() {
        ArrayList<Joker> jokers = new ArrayList<>();

        Class<?> resClass = R.drawable.class;
        Field[] fields = resClass.getFields();

        for (Field field : fields) {
            try {
                String name = field.getName();
                if (name.startsWith("joker_")) {
                    Joker joker = new Joker(name);
                    jokers.add(joker);
                }
            } catch (Exception e) {
                Log.e(ERROR, e.toString());
            }
        }

        return jokers;
    }

    private ArrayList<Voucher> getVouchers() {
        ArrayList<Voucher> vouchers = new ArrayList<>();

        Class<?> resClass = R.drawable.class;
        Field[] fields = resClass.getFields();

        for (Field field : fields) {
            try {
                String name = field.getName();
                if (name.startsWith("voucher_")) {
                    Voucher voucher = new Voucher(name);
                    vouchers.add(voucher);
                }
            } catch (Exception e) {
                Log.e(ERROR, e.toString());
            }
        }

        return vouchers;
    }

    private ArrayList<PlanetCard> getPlanets() {
        ArrayList<PlanetCard> planets = new ArrayList<>();

        Class<?> resClass = R.drawable.class;
        Field[] fields = resClass.getFields();

        for (Field field : fields) {
            try {
                String name = field.getName();
                if (name.startsWith("planet_")) {
                    PlanetCard planet = new PlanetCard(name);
                    planets.add(planet);
                }
            } catch (Exception e) {
                Log.e(ERROR, e.toString());
            }
        }

        return planets;
    }

    private ArrayList<TarotCard> getTarotCards() {
        ArrayList<TarotCard> tarotCards = new ArrayList<>();

        Class<?> resClass = R.drawable.class;
        Field[] fields = resClass.getFields();

        for (Field field : fields) {
            try {
                String name = field.getName();
                if (name.startsWith("tarot_")) {
                    TarotCard tarotCard = new TarotCard(name);
                    tarotCards.add(tarotCard);
                }
            } catch (Exception e) {
                Log.e(ERROR, e.toString());
            }
        }

        return tarotCards;
    }

    private void toggleSelection(ImageView imageView, String cardName) {
        ImageView itemImageView = findViewById(R.id.item_image);
        TextView itemTextView = findViewById(R.id.item_text);
        TextView itemPriceView = findViewById(R.id.item_price);
        Button buyButton = findViewById(R.id.buy_item);

        if (isSameSelected(cardName)) {
            clearSelection(itemImageView, itemTextView, itemPriceView, buyButton);
        } else {
            updateSelection(imageView, cardName, itemImageView, itemTextView,
                    itemPriceView, buyButton);
        }

        notifySelectionListener();
    }

    private boolean isSameSelected(String cardName) {
        return this.selectedCard != null && this.selectedCard.equals(cardName);
    }

    private void clearSelection(ImageView itemImageView, TextView itemTextView,
                                TextView itemPriceView, Button buyButton) {
        this.selectedCard = null;
        if (this.selectedCardView != null) {
            this.selectedCardView.setBackgroundResource(0);
            this.selectedCardView = null;
        }
        if (itemImageView != null) {
            itemImageView.setImageDrawable(null);
        }
        itemTextView.setText("");
        itemPriceView.setText("");
        buyButton.setText("");
        buyButton.setEnabled(false);
        buyButton.setBackgroundTintList(ContextCompat
                .getColorStateList(this, R.color.dark_gray));
    }

    private void updateSelection(ImageView imageView, String cardName, ImageView itemImageView,
                                 TextView itemTextView, TextView itemPriceView, Button buyButton) {
        if (this.selectedCardView != null) {
            this.selectedCardView.setBackgroundResource(0);
        }

        this.selectedCard = cardName;
        this.selectedCardView = imageView;
        this.selectedCardView.setBackgroundResource(R.drawable.selected_border);

        Object selectedCardObject = getCardByName(cardName);
        if (selectedCardObject != null) {
            String cardText = getCardText(selectedCardObject);
            String cardPrice = getCardPrice(selectedCardObject);

            int drawableId = getResources().getIdentifier(this.selectedCard,
                    DRAWABLE, getPackageName());
            if (drawableId != 0 && itemImageView != null) {
                itemImageView.setImageResource(drawableId);
            }

            itemTextView.setText(cardText != null ? cardText : "No description available");
            itemPriceView.setText(String.format("$%s", cardPrice));

            updateBuyButton(buyButton, cardPrice);
            buyButton.setOnClickListener(v -> buyCard());
        }
    }

    private void updateBuyButton(Button buyButton, String cardPrice) {
        buyButton.post(() -> {
            buyButton.setText(String.format("Buy for $%s", cardPrice));
            int currentGold = Integer.parseInt(this.gameData.getCurrentGold());
            int price = Integer.parseInt(cardPrice);
            if (currentGold >= price) {
                buyButton.setEnabled(true);
                buyButton.setBackgroundTintList(ContextCompat
                        .getColorStateList(this, R.color.green));
            } else {
                buyButton.setEnabled(false);
                buyButton.setBackgroundTintList(ContextCompat
                        .getColorStateList(this, R.color.dark_gray));
            }
            buyButton.invalidate();
            buyButton.requestLayout();
        });
    }

    private void notifySelectionListener() {
        if (this.selectionListener != null) {
            ArrayList<String> selectedList = (this.selectedCard != null)
                    ? new ArrayList<>(Collections.singletonList(this.selectedCard))
                    : new ArrayList<>();
            try {
                this.selectionListener.onSelectionChanged(selectedList);
            } catch (Exception e) {
                Log.e(ERROR, "Error in onSelectionChanged()", e);
            }
        }
    }

    private void buyCard() {
        if (this.selectedCard == null) {
            Toast.makeText(this, "No card selected!", Toast.LENGTH_SHORT).show();
            return;
        }

        Object selectedCardObject = getCardByName(this.selectedCard);
        String cardPriceStr = getCardPrice(selectedCardObject);
        int cardPrice = Integer.parseInt(cardPriceStr);

        if (Integer.parseInt(this.gameData.getCurrentGold()) < cardPrice) {
            Toast.makeText(this, "Not enough gold!", Toast.LENGTH_SHORT).show();
            return;
        }

        int newGold = Integer.parseInt(this.gameData.getCurrentGold()) - cardPrice;
        this.gameData.setCurrentGold(String.valueOf(newGold));

        if (selectedCardObject instanceof Joker) {
            addJokerToCollection((Joker) selectedCardObject);
        } else if (selectedCardObject instanceof PlanetCard) {
            updatePlanetCard(this.selectedCard);
        } else if (selectedCardObject instanceof Voucher) {
            applyVoucher(this.selectedCard);
        } else if (selectedCardObject instanceof TarotCard) {
            addTarotToCollection((TarotCard) selectedCardObject);
        }

        if (this.selectedCardView != null) {
            ViewGroup parent = (ViewGroup) this.selectedCardView.getParent();
            if (parent != null) {
                parent.removeView(this.selectedCardView);
            }
        }

        this.updateGameData();
        this.clearItemDetails();

        this.selectedCard = null;
        this.selectedCardView = null;
    }

    private void addJokerToCollection(Joker joker) {
        LinearLayout jokersContainer = findViewById(R.id.jokers_container);

        if (jokersContainer != null) {
            if (jokersContainer.getChildCount() >= 5) {
                Toast.makeText(this, "Joker slots are full!",
                        Toast.LENGTH_SHORT).show();
                return;
            }

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
            jokersContainer.invalidate();

            this.gameData.getJokers().add(joker);
        }
    }

    private void addTarotToCollection(TarotCard tarotCard) {
        LinearLayout consumablesContainer = findViewById(R.id.consumables_container);

        if (consumablesContainer != null) {
            ImageView tarotImage = new ImageView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(5, 5, 5, 5);
            tarotImage.setLayoutParams(params);
            tarotImage.setScaleType(ImageView.ScaleType.FIT_CENTER);

            int drawableId = getResources().getIdentifier(tarotCard.getName(),
                    DRAWABLE, getPackageName());
            if (drawableId != 0) {
                tarotImage.setImageResource(drawableId);
            }

            consumablesContainer.addView(tarotImage);
            consumablesContainer.invalidate();

            this.gameData.getTarotCards().add(tarotCard);
        }
    }

    private void updatePlanetCard(String cardName) {
        PlanetData planetData = gameData.getPlanetData();
        HandType correspondingHandType = getHandTypeFromCardName(cardName);

        if (correspondingHandType != null) {
            planetData.incrementLevel(correspondingHandType);
        } else {
            Log.e(ERROR, "Unknown PlanetCard: " + cardName);
        }
    }

    private HandType getHandTypeFromCardName(String cardName) {
        if (cardName.contains("planet_pluto")) return HandType.HIGH_CARD;
        if (cardName.contains("planet_mercury")) return HandType.PAIR;
        if (cardName.contains("planet_uranus")) return HandType.TWO_PAIR;
        if (cardName.contains("planet_venus")) return HandType.THREE_OF_A_KIND;
        if (cardName.contains("planet_saturn")) return HandType.STRAIGHT;
        if (cardName.contains("planet_jupiter")) return HandType.FLUSH;
        if (cardName.contains("planet_earth")) return HandType.FULL_HOUSE;
        if (cardName.contains("planet_mars")) return HandType.FOUR_OF_A_KIND;
        if (cardName.contains("planet_neptune")) return HandType.STRAIGHT_FLUSH;

        return null;
    }

    private void applyVoucher(String card) {
        if (card.equals("voucher_grabber")) {
            int hands = Integer.parseInt(this.gameData.getHands()) + 1;
            this.gameData.setCurrentHands(String.valueOf(hands));
            this.gameData.setHands(String.valueOf(hands));

            runOnUiThread(() -> {
                TextView handsText = findViewById(R.id.hands);
                handsText.setText(String.valueOf(hands));
            });
        } else if (card.equals("voucher_wasteful")) {
            int discards = Integer.parseInt(this.gameData.getDiscards()) + 1;
            this.gameData.setCurrentDiscards(String.valueOf(discards));
            this.gameData.setDiscards(String.valueOf(discards));

            runOnUiThread(() -> {
                TextView discardsText = findViewById(R.id.discards);
                discardsText.setText(String.valueOf(discards));
            });
        } else {
            this.gameData.setShopSize(this.gameData.getShopSize() + 1);
        }
    }

    private void clearItemDetails() {
        ImageView itemImageView = findViewById(R.id.item_image);
        TextView itemTextView = findViewById(R.id.item_text);
        TextView itemPriceView = findViewById(R.id.item_price);
        Button buyButton = findViewById(R.id.buy_item);

        if (itemImageView != null) itemImageView.setImageDrawable(null);
        if (itemTextView != null) itemTextView.setText("");
        if (itemPriceView != null) itemPriceView.setText("");

        buyButton.setText("");
        buyButton.setEnabled(false);
        buyButton.setBackgroundTintList(ContextCompat
                .getColorStateList(this, R.color.dark_gray));
    }


    @Override
    public void onSelectionChanged(ArrayList<String> cards) {
        Log.i("selectedCard", cards.get(0));
    }

    private Object getCardByName(String cardName) {
        for (Joker joker : getJokers()) {
            if (joker.getName().equals(cardName)) {
                return joker;
            }
        }
        for (Voucher voucher : getVouchers()) {
            if (voucher.getName().equals(cardName)) {
                return voucher;
            }
        }
        for (PlanetCard planet : getPlanets()) {
            if (planet.getName().equals(cardName)) {
                return planet;
            }
        }
        for (TarotCard tarot : getTarotCards()) {
            if (tarot.getName().equals(cardName)) {
                return tarot;
            }
        }
        return null;
    }

    private String getCardText(Object card) {
        if (card instanceof Joker) {
            return ((Joker) card).getText();
        } else if (card instanceof Voucher) {
            return ((Voucher) card).getText();
        } else if (card instanceof PlanetCard) {
            return ((PlanetCard) card).getText();
        } else if (card instanceof TarotCard) {
            return ((TarotCard) card).getText();
        }
        return "";
    }

    private String getCardPrice(Object card) {
        if (card instanceof Joker) {
            return String.valueOf(((Joker) card).getPrice());
        } else if (card instanceof Voucher) {
            return String.valueOf(((Voucher) card).getPrice());
        } else if (card instanceof PlanetCard) {
            return String.valueOf(((PlanetCard) card).getPrice());
        } else if (card instanceof TarotCard) {
            return String.valueOf(((TarotCard) card).getPrice());
        }
        return "0";
    }

    private void setupButtonListeners() {
        Button nextRoundButton = findViewById(R.id.button_next_round);
        Button rerollButton = findViewById(R.id.button_reroll);

        if (nextRoundButton != null) {
            nextRoundButton.setOnClickListener(v -> {
                int stage = this.gameData.getAnte().getStage();
                if (stage == 3) {
                    this.gameData.getAnte().setStage(1);
                    this.gameData.getAnte().setAnteValue(this.gameData.getAnte().getAnteValue() + 1);
                } else {
                    this.gameData.getAnte().setStage(this.gameData.getAnte().getStage() + 1);
                }

                GameDataHolder.gameData = this.gameData;
                Intent intent = new Intent(BalatroShop.this, BalatroBlind.class);
                startActivity(intent);
            });
        }

        if (rerollButton != null) {
            rerollButton.setOnClickListener(v -> rerollJokers());
            updateRerollButton();
        }
    }

    private void rerollJokers() {
        int currentGold = Integer.parseInt(this.gameData.getCurrentGold());

        if (currentGold < 5) {
            Toast.makeText(this, "Not enough gold to reroll!",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        this.gameData.setCurrentGold(String.valueOf(currentGold - 5));
        updateGameData();

        generateJokers();
        updateRerollButton();
    }

    private void updateRerollButton() {
        Button rerollButton = findViewById(R.id.button_reroll);
        int currentGold = Integer.parseInt(this.gameData.getCurrentGold());

        if (rerollButton != null) {
            if (currentGold >= 5) {
                rerollButton.setEnabled(true);
                rerollButton.setBackgroundTintList(ContextCompat
                        .getColorStateList(this, R.color.green));
            } else {
                rerollButton.setEnabled(false);
                rerollButton.setBackgroundTintList(ContextCompat
                        .getColorStateList(this, R.color.dark_gray));
            }
        }
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

        List<TarotCard> tarotCards = gameData.getTarotCards();

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