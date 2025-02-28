package com.example.androidgames.Activities.Balatro.Components;

import java.util.ArrayList;

import lombok.Getter;

@Getter
public class GameData {
    private int hands;
    private int discards;
    private int gold;
    private int round;
    private String deckName;

    private Ante ante;
    private Deck deck;
    private PlanetData planetData;
    private ArrayList<Joker> jokers;
    private ArrayList<Consumable> consumables;
}
