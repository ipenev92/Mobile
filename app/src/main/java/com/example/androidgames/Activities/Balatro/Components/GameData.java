package com.example.androidgames.Activities.Balatro.Components;

import java.util.ArrayList;
import java.util.Objects;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameData {
    private String hands;
    private String currentHands;
    private String discards;
    private String currentDiscards;
    private String gold;
    private String currentGold;

    private final String deckName;
    private String round;
    private int roundScore;
    private String sort_by;
    private int shopSize;

    private Ante ante;
    private Deck deck;
    private PlanetData planetData;
    private ArrayList<PlayingCard> playingField;
    private ArrayList<Joker> jokers;
    private ArrayList<TarotCard> tarotCards;
    private ArrayList<Voucher> vouchers;

    public GameData(String selectedDeck) {
        this.round = "0";
        this.roundScore = 0;
        this.deckName = selectedDeck;
        this.sort_by = "rank";

        this.hands = getInitialHands(this.deckName);
        this.discards = getInitialDiscards(this.deckName);
        this.gold = getInitialGold(this.deckName);

        this.shopSize = 3;

        this.currentHands = this.hands;
        this.currentDiscards = this.discards;
        this.currentGold = this.gold;

        this.ante = new Ante(0);
        this.deck = new Deck(this.deckName);

        this.planetData = new PlanetData();
        this.playingField = new ArrayList<>();
        this.jokers = new ArrayList<>();
        this.tarotCards = new ArrayList<>();
        this.vouchers = new ArrayList<>();
    }

    public static String getInitialHands(String deck) {
        switch (deck) {
            case "deck_blue": return "5";
            case "deck_black": return "3";
            default: return "4";
        }
    }

    public static String getInitialDiscards(String deck) {
        return Objects.equals(deck, "deck_red") ? "4" : "3";
    }

    public static String getInitialGold(String deck) {
        return Objects.equals(deck, "deck_yellow") ? "14" : "40";
    }
}