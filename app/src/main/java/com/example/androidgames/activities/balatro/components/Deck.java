package com.example.androidgames.activities.balatro.components;

import android.util.Log;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Random;

import com.example.androidgames.R;

import lombok.Getter;

@Getter
public class Deck {
    private final String deckName;
    private final ArrayList<PlayingCard> deckList;
    private final Random random;

    public Deck(String selectedDeck) {
        this.deckName = selectedDeck;
        this.deckList = generateDeck();

         this.random = new Random();
    }

    private ArrayList<PlayingCard> generateDeck() {
        ArrayList<PlayingCard> deck;

        switch (this.deckName) {
            case "deck_abandoned": deck = this.generateAbandonedDeck(); break;
            case "deck_checkered": deck = this.generateCheckeredDeck(); break;
            case "deck_erratic": deck = this.generateErraticDeck(); break;
            default: deck = this.generateDefaultDeck();
        }

        return deck;
    }

    private ArrayList<PlayingCard> generateAbandonedDeck() {
        ArrayList<PlayingCard> normalDeck = this.generateDefaultDeck();
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
        ArrayList<PlayingCard> normalDeck = this.generateDefaultDeck();
        ArrayList<PlayingCard> deck = new ArrayList<>();

        for (PlayingCard card : normalDeck) {
            if (card.getSuit().equals("spades") || card.getSuit().equals("hearts")) {
                deck.add(card);
                deck.add(card);
            }
        }

        return deck;
    }

    private ArrayList<PlayingCard> generateErraticDeck() {
        ArrayList<PlayingCard> normalDeck = this.generateDeck();
        ArrayList<PlayingCard> deck = new ArrayList<>();

        for (int i = 0; i < normalDeck.size(); i++) {
            PlayingCard card = normalDeck.get(this.random.nextInt(52));
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

                    PlayingCard playingCard = new PlayingCard(name, parts[1], parts[3]);
                    deck.add(playingCard);
                }
            } catch (Exception e) {
                Log.e("Error", e.toString());
            }
        }

        return deck;
    }
}