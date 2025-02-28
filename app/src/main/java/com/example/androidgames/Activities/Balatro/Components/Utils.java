package com.example.androidgames.Activities.Balatro.Components;

import java.util.Objects;

public class Utils {
    public static String getHands(String deck) {
        switch (deck) {
            case "deck_blue": return "5";
            case "deck_black": return "3";
            default: return "4";
        }
    }

    public static String getDiscards(String deck) {
        return Objects.equals(deck, "deck_red") ? "4" : "3";
    }

    public static String getGold(String deck) {
        return Objects.equals(deck, "deck_yellow") ? "$14" : "$4";
    }
}
