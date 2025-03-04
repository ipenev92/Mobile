package com.example.androidgames.Activities.Balatro.Components;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayingCard {
    String name;
    private String value;
    private String suit;

    private int chips;
    private int mult;

    public PlayingCard(String name, String value, String suit) {
        this.name = name;
        this.value = value;
        this.suit = suit;
        this.chips = getCardChips();
        this.mult = 0;
    }

    private int getCardChips() {
        switch (this.getValue()) {
            case "ace": return 11;
            case "king": case "queen": case "jack": case "10": return 10;
            case "9": return 9;
            case "8": return 8;
            case "7": return 7;
            case "6": return 6;
            case "5": return 5;
            case "4": return 4;
            case "3": return 3;
            case "2": return 2;
            default: return 0;
        }
    }
}