package com.example.androidgames.Activities.Balatro.Components;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

@Getter
public class Joker {
    private final String name;
    private String text;
    private String price;

    private static final Map<String, JokerData> JOKER_MAP = new HashMap<>();

    static {
        JOKER_MAP.put("joker_abstract", new JokerData("+3 Mult for each Joker card",
                "4"));
        JOKER_MAP.put("joker_banner", new JokerData("+30 Chips for each remaining discard",
                "5"));
        JOKER_MAP.put("joker_clever", new JokerData("+80 Chips if played hand contains" +
                " a Two Pair", "4"));
        JOKER_MAP.put("joker_crafty", new JokerData("+80 Chips if played hand contains" +
                " a Flush", "4"));
        JOKER_MAP.put("joker_crazy", new JokerData("+12 Mult if played " +
                "hand contains a Straight", "4"));
        JOKER_MAP.put("joker_devious", new JokerData("+100 Chips if played hand contains" +
                " a Straight", "4"));
        JOKER_MAP.put("joker_droll", new JokerData("+10 Mult if played " +
                "hand contains a Flush", "4"));
        JOKER_MAP.put("joker_duo", new JokerData("X2 Mult if played hand contains a" +
                " Pair", "8"));
        JOKER_MAP.put("joker_family", new JokerData("X4 Mult if played hand contains" +
                " a Four of a Kind", "8"));
        JOKER_MAP.put("joker_gluttonous", new JokerData("Played cards with Club suit " +
                "give +3 Mult when scored", "5"));
        JOKER_MAP.put("joker_greedy", new JokerData("Played cards with Diamond suit " +
                "give +3 Mult when scored", "5"));
        JOKER_MAP.put("joker_joker", new JokerData("+4 Mult", "2"));
        JOKER_MAP.put("joker_jolly", new JokerData("+8 Mult if played hand " +
                "contains a Pair", "3"));
        JOKER_MAP.put("joker_lusty", new JokerData("Played cards with Heart suit " +
                "give +3 Mult when scored", "5"));
        JOKER_MAP.put("joker_mad", new JokerData("+10 Mult if played hand contains " +
                "a Two Pair", "4"));
        JOKER_MAP.put("joker_misprint", new JokerData("+0-23 Mult", "4"));
        JOKER_MAP.put("joker_mystic", new JokerData("+15 Mult when 0 discards" +
                " remaining", "5"));
        JOKER_MAP.put("joker_order", new JokerData("X3 Mult if played hand contains" +
                " a Straight", "8"));
        JOKER_MAP.put("joker_sly", new JokerData("+50 Chips if played hand contains" +
                " a Pair", "3"));
        JOKER_MAP.put("joker_tribe", new JokerData("X2 Mult if played hand contains" +
                " a Flush", "8"));
        JOKER_MAP.put("joker_trio", new JokerData("X3 Mult if played hand contains " +
                "a Three of a Kind", "8"));
        JOKER_MAP.put("joker_wily", new JokerData("+100 Chips if played hand contains" +
                " a Three of a Kind", "4"));
        JOKER_MAP.put("joker_wrathful", new JokerData("Played cards with Spade suit " +
                "give +3 Mult when scored", "5"));
        JOKER_MAP.put("joker_zany", new JokerData("+12 Mult if played hand contains" +
                " a Three of a Kind", "4"));
    }

    public Joker(String name) {
        this.name = name;
        updateJokers();
    }

    private void updateJokers() {
        JokerData data = JOKER_MAP.get(this.name);
        if (data != null) {
            this.text = data.getText();
            this.price = data.getPrice();
        }
    }

    @Getter
    private static class JokerData {
        private final String text;
        private final String price;

        public JokerData(String text, String price) {
            this.text = text;
            this.price = price;
        }
    }
}