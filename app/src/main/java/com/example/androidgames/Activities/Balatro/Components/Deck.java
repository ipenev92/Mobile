package com.example.androidgames.Activities.Balatro.Components;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Random;

import com.example.androidgames.R;

import lombok.Getter;

@Getter
public class Deck implements Parcelable {
    private final String deckName;
    private ArrayList<PlayingCard> deck;

    public Deck(String selectedDeck) {
        this.deckName = selectedDeck;
        this.deck = generateDeck();
    }

    private ArrayList<PlayingCard> generateDeck() {
        ArrayList<PlayingCard> deck;

        switch (this.deckName) {
            case "deck_abandoned": deck = generateAbandonedDeck(); break;
            case "deck_checkered": deck = generateCheckeredDeck(); break;
            case "deck_erratic": deck = generateErraticDeck(); break;
            default: deck = generateDefaultDeck();
        }

        return deck;
    }

    private ArrayList<PlayingCard> generateAbandonedDeck() {
        ArrayList<PlayingCard> normalDeck = generateDefaultDeck();
        ArrayList<PlayingCard> deck = new ArrayList<>();

        for (PlayingCard card : normalDeck) {
            if (!card.getValue().equals("jack") && !card.getValue().equals("queen") &&
                    !card.getValue().equals("king")) {
                deck.add(card);
            }
        }

        return deck;
    }

    private ArrayList<PlayingCard> generateCheckeredDeck() {
        ArrayList<PlayingCard> normalDeck = generateDefaultDeck();
        ArrayList<PlayingCard> deck = new ArrayList<>();

        for (PlayingCard card : normalDeck) {
            if (card.getSuit().equals("Spade") || card.getSuit().equals("Heart")) {
                deck.add(card);
                deck.add(card);
            }
        }

        return deck;
    }

    private ArrayList<PlayingCard> generateErraticDeck() {
        ArrayList<PlayingCard> normalDeck = generateDeck();
        ArrayList<PlayingCard> deck = new ArrayList<>();

        Random random = new Random();
        for (int i = 0; i < normalDeck.size(); i++) {
            PlayingCard card = normalDeck.get(random.nextInt(52));
            deck.add(card);
        }

        return deck;
    }

    private ArrayList<PlayingCard> generateDefaultDeck() {
        ArrayList<PlayingCard> deck = new ArrayList<>();

        Class<?> resClass = R.drawable.class;
        Field[] fields = resClass.getFields();

        for (Field field : fields) {
            try {
                String name = field.getName();
                if (name.startsWith("card_")) {
                    String[] parts = name.split("_");

                    PlayingCard playingCard = new PlayingCard(name, parts[1], parts[3],
                            Enhancement.BASE, Edition.BASE, Seal.BASE);
                    deck.add(playingCard);
                }


                deck.get(5).getName();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return deck;
    }

    /* ========== PARCELABLE ========== */
    protected Deck(Parcel in) {
        deckName = in.readString();
        deck = in.createTypedArrayList(PlayingCard.CREATOR);
    }

    public static final Creator<Deck> CREATOR = new Creator<Deck>() {
        @Override
        public Deck createFromParcel(Parcel in) {
            return new Deck(in);
        }

        @Override
        public Deck[] newArray(int size) {
            return new Deck[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(deckName);
        parcel.writeTypedList(deck);
    }
}
