package com.example.androidgames;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayingCard extends Card {
    private String value;
    private String suit;
    private Enhancement enhancement;
    private Seal seal;

    public PlayingCard(String name, String value, String suit, Enhancement enhancement,
                       Edition edition, Seal seal) {
        super(name, edition);
        this.suit = suit;
        this.value = value;
        this.enhancement = enhancement;
        this.seal = seal;
    }
}