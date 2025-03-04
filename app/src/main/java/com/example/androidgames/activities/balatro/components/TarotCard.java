package com.example.androidgames.Activities.Balatro.Components;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TarotCard {
    private final String name;
    private String text;
    private String price;

    private static final Map<String, TarotData> TAROT_MAP = new HashMap<>();

    static {
        TAROT_MAP.put("tarot_death", new TarotData("Select 2 cards, convert the " +
                "left card into the right card.", "3"));
        TAROT_MAP.put("tarot_emperor", new TarotData("Give", "3"));
        TAROT_MAP.put("tarot_hanged", new TarotData("Destroys up to 2 selected cards",
                "3"));
        TAROT_MAP.put("tarot_hermit", new TarotData("Doubles money (Max of $20)",
                "3"));
        TAROT_MAP.put("tarot_moon", new TarotData("Converts up to 3 selected cards to" +
                " Clubs", "3"));
        TAROT_MAP.put("tarot_priestess", new TarotData("Creates up to 2 random Planet" +
                " cards", "3"));
        TAROT_MAP.put("tarot_star", new TarotData("Converts up to 3 selected cards to" +
                " Diamonds", "3"));
        TAROT_MAP.put("tarot_strength", new TarotData("Increases rank of up to 2" +
                " selected cards by 1", "3"));
        TAROT_MAP.put("tarot_sun", new TarotData("Converts up to 3 selected cards to" +
                " Hearts", "3"));
        TAROT_MAP.put("tarot_world", new TarotData("Converts up to 3 selected cards to" +
                " Spades", "3"));
    }

    public TarotCard(String name) {
        this.name = name;
        updateTarot();
    }

    private void updateTarot() {
        TarotData data = TAROT_MAP.get(this.name);
        if (data != null) {
            this.text = data.getText();
            this.price = data.getPrice();
        }
    }

    @Getter
    private static class TarotData {
        private final String text;
        private final String price;

        public TarotData(String text, String price) {
            this.text = text;
            this.price = price;
        }
    }
}