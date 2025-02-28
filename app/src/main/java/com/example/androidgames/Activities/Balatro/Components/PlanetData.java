package com.example.androidgames.Activities.Balatro.Components;

import lombok.Getter;

@Getter
public class PlanetData {
    // Levels
    private int highCardLevel;
    private int pairLevel;
    private int twoPairLevel;
    private int threeOfAKindLevel;
    private int straightLevel;
    private int flushLevel;
    private int fullHouseLevel;
    private int fourOfAKindLevel;
    private int straightFlushLevel;
    private int fiveOfAKindLevel;
    private int flushHouseLevel;
    private int flushFiveLevel;

    // Chips
    private int highCardChips;
    private int pairChips;
    private int twoPairChips;
    private int threeOfAKindChips;
    private int straightChips;
    private int flushChips;
    private int fullHouseChips;
    private int fourOfAKindChips;
    private int straightFlushChips;
    private int fiveOfAKindChips;
    private int flushHouseChips;
    private int flushFiveChips;

    // Mult
    private int highCardMult;
    private int pairMult;
    private int twoPairMult;
    private int threeOfAKindMult;
    private int straightMult;
    private int flushMult;
    private int fullHouseMult;
    private int fourOfAKindMult;
    private int straightFlushMult;
    private int fiveOfAKindMult;
    private int flushHouseMult;
    private int flushFiveMult;

    // Played
    private int highCardPlayed;
    private int pairPlayed;
    private int twoPairPlayed;
    private int threeOfAKindPlayed;
    private int straightPlayed;
    private int flushPlayed;
    private int fullHousePlayed;
    private int fourOfAKindPlayed;
    private int straightFlushPlayed;
    private int fiveOfAKindPlayed;
    private int flushHousePlayed;
    private int flushFivePlayed;

    public PlanetData() {
        this.highCardLevel = 1;
        this.pairLevel = 1;
        this.twoPairLevel = 1;
        this.threeOfAKindLevel = 1;
        this.straightLevel = 1;
        this.flushLevel = 1;
        this.fullHouseLevel = 1;
        this.fourOfAKindLevel = 1;
        this.straightFlushLevel = 1;
        this.fiveOfAKindLevel = 1;
        this.flushHouseLevel = 1;
        this.flushFiveLevel = 1;

        this.highCardChips = 5;
        this.pairChips = 10;
        this.twoPairChips = 20;
        this.threeOfAKindChips = 30;
        this.straightChips = 30;
        this.flushChips = 35;
        this.fullHouseChips = 40;
        this.fourOfAKindChips = 60;
        this.straightFlushChips = 100;
        this.fiveOfAKindChips = 120;
        this.flushHouseChips = 140;
        this.flushFiveChips = 160;

        this.highCardMult = 1;
        this.pairMult = 2;
        this.twoPairMult = 2;
        this.threeOfAKindMult = 3;
        this.straightMult = 4;
        this.flushMult = 4;
        this.fullHouseMult = 4;
        this.fourOfAKindMult = 7;
        this.straightFlushMult = 8;
        this.fiveOfAKindMult = 12;
        this.flushHouseMult = 14;
        this.flushFiveMult = 16;

        this.highCardPlayed = 0;
        this.pairPlayed = 0;
        this.twoPairPlayed = 0;
        this.threeOfAKindPlayed = 0;
        this.straightPlayed = 0;
        this.flushPlayed = 0;
        this.fullHousePlayed = 0;
        this.fourOfAKindPlayed = 0;
        this.straightFlushPlayed = 0;
        this.fiveOfAKindPlayed = 0;
        this.flushHousePlayed = 0;
        this.flushFivePlayed = 0;
    }
}
