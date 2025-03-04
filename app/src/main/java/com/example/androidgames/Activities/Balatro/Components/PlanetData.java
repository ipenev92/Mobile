package com.example.androidgames.Activities.Balatro.Components;

import java.util.EnumMap;
import java.util.Map;

import lombok.Getter;

@Getter
public class PlanetData {
    private final Map<HandType, Integer> levels = new EnumMap<>(HandType.class);
    private final Map<HandType, Integer> chips = new EnumMap<>(HandType.class);
    private final Map<HandType, Integer> multipliers = new EnumMap<>(HandType.class);
    private final Map<HandType, Integer> playedCounts = new EnumMap<>(HandType.class);

    public PlanetData() {
        initializeDefaultValues();
    }

    private void initializeDefaultValues() {
        for (HandType type : HandType.values()) {
            levels.put(type, 1);
            playedCounts.put(type, 0);
            chips.put(type, getDefaultChips(type));
            multipliers.put(type, getDefaultMultiplier(type));
        }
    }

    private int getDefaultChips(HandType type) {
        switch (type) {
            case HIGH_CARD: return 5;
            case PAIR: return 10;
            case TWO_PAIR: return 20;
            case THREE_OF_A_KIND: return 30;
            case STRAIGHT: return 30;
            case FLUSH: return 35;
            case FULL_HOUSE: return 40;
            case FOUR_OF_A_KIND: return 60;
            case STRAIGHT_FLUSH: return 100;
            case FIVE_OF_A_KIND: return 120;
            case FLUSH_HOUSE: return 140;
            case FLUSH_FIVE: return 160;
            default: return 1;
        }
    }

    private int getDefaultMultiplier(HandType type) {
        switch (type) {
            case HIGH_CARD: return 1;
            case PAIR: return 2;
            case TWO_PAIR: return 2;
            case THREE_OF_A_KIND: return 3;
            case STRAIGHT: return 4;
            case FLUSH: return 4;
            case FULL_HOUSE: return 4;
            case FOUR_OF_A_KIND: return 7;
            case STRAIGHT_FLUSH: return 8;
            case FIVE_OF_A_KIND: return 12;
            case FLUSH_HOUSE: return 14;
            case FLUSH_FIVE: return 16;
            default: return 1;
        }
    }

    public int getChipsByHand(HandType type) {
        return this.chips.getOrDefault(type, 1);
    }

    public int getMultByHand(HandType type) {
        return this.multipliers.getOrDefault(type, 1);
    }

    public int getLevelByHand(HandType type) {
        return this.levels.getOrDefault(type, 1);
    }

    public int getPlayedByHand(HandType type) {
        return this.playedCounts.getOrDefault(type, 1);
    }

    public void incrementChips(HandType type) {
        this.chips.put(type, this.chips.getOrDefault(type, 0) + 1);
    }

    public void incrementMult(HandType type) {
        this.multipliers.put(type, this.multipliers.getOrDefault(type, 0) + 1);
    }

    public void incrementLevel(HandType type) {
        this.levels.put(type, this.levels.getOrDefault(type, 0) + 1);
    }

    public void incrementPlayed(HandType type) {
        this.playedCounts.put(type, this.playedCounts.getOrDefault(type, 0) + 1);
    }
}