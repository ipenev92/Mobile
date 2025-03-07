package com.example.androidgames.activities.balatro.components;

import java.util.Random;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Ante {
    private int stage;
    private int anteValue;
    private int round;
    private String smallBlind;
    private String bigBlind;
    private String bossBlind;
    private String bossName;
    private BossEffect bossEffect;
    private String bossEffectText;
    private String bossImage;

    private Random random;

    public Ante(int ante) {
        this.random = new Random();

        this.stage = 1;
        this.anteValue = ante;
        this.round = 0;
        this.bossEffect = getRandomBossEffect();
        this.bossName = getBossBlindName();
        this.bossEffectText = getBossBlindEffectText();
        this.bossImage = getBossBlindImage();

        this.smallBlind = getSmallBlindPoints();
        this.bigBlind = getBigBlindPoints();
        this.bossBlind = getBossBlindPoints(this.bossEffect);
    }

    private BossEffect getRandomBossEffect() {
        BossEffect[] effects = BossEffect.values();
        int randomNum = this.random.nextInt(effects.length);
        return effects[randomNum];
    }

    private String getBossBlindName() {
        switch (this.bossEffect) {
            case OX: return "The Ox";
            case HOUSE: return "The House";
            case WALL: return "The Wall";
            case WHEEL: return "The Wheel";
            case FISH: return "The Fish";
            case NEEDLE: return "The Needle";
            case MANACLE: return "The Manacle";
            case CLUB: return "The Club";
            case GOAD: return "The Goad";
            case HEAD: return "The Head";
            case WINDOW: return "The Window";
            case PLANT: return "The Plant";
        }
        return "";
    }

    private String getBossBlindEffectText() {
        switch (this.bossEffect) {
            case OX: return "Most common hand sets money to $0.";
            case HOUSE: return "First hand is drawn face down.";
            case WALL: return "Extra large blind.";
            case WHEEL: return "1 in 7 cards get drawn face down.";
            case FISH: return "Cards drawn face down after each hand is played.";
            case NEEDLE: return "Play only one hand.";
            case MANACLE: return "-1 Hand size.";
            case CLUB: return "Clubs are debuffed.";
            case GOAD: return "Spades are debuffed.";
            case HEAD: return "Hearts are debuffed.";
            case WINDOW: return "Diamonds are debuffed.";
            case PLANT: return "Face cards are debuffed.";
        }
        return "";
    }

    private String getBossBlindImage() {
        switch (this.bossEffect) {
            case OX: return "blind_boss_ox";
            case HOUSE: return "blind_boss_house";
            case WALL: return "blind_boss_wall";
            case WHEEL: return "blind_boss_wheel";
            case FISH: return "blind_boss_fish";
            case NEEDLE: return "blind_boss_needle";
            case MANACLE: return "blind_boss_manacle";
            case CLUB: return "blind_boss_club";
            case GOAD: return "blind_boss_goad";
            case HEAD: return "blind_boss_head";
            case WINDOW: return "blind_boss_window";
            case PLANT: return "blind_boss_plant";
        }
        return "blind_boss";
    }

    private String getSmallBlindPoints() {
        int[] small = {300, 800, 2000, 5000, 11000, 20000, 35000, 50000};
        return String.valueOf(small[this.anteValue]);
    }

    private String getBigBlindPoints() {
        int[] big = {450, 1200, 3000, 7500, 16500, 30000, 52500, 75000};
        return String.valueOf(big[this.anteValue]);
    }

    private String getBossBlindPoints(BossEffect effect) {
        int[] boss = {600, 1600, 4000, 10000, 22000, 40000, 70000, 100000};
        return String.valueOf(effect == BossEffect.WALL ? boss[this.anteValue] * 2 :
                boss[this.anteValue]);
    }

    public void updateBlinds() {
        this.bossEffect = getRandomBossEffect();
        this.bossName = getBossBlindName();
        this.bossEffectText = getBossBlindEffectText();
        this.bossImage = getBossBlindImage();

        this.smallBlind = getSmallBlindPoints();
        this.bigBlind = getBigBlindPoints();
        this.bossBlind = getBossBlindPoints(this.bossEffect);
    }
}