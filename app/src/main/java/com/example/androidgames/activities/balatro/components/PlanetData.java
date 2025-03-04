package com.example.androidgames.activities.balatro.components;

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
            this.levels.put(type, 1);
            this.playedCounts.put(type, 0);
            this.chips.put(type, getDefaultChips(type));
            this.multipliers.put(type, getDefaultMultiplier(type));
        }
    }

    private int getDefaultChips(HandType type) {
        switch (type) {
            case HIGH_CARD: return 5;
            case PAIR: return 10;
            case TWO_PAIR: return 20;
            case THREE_OF_A_KIND: case STRAIGHT: return 30;
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
            case PAIR: case TWO_PAIR: return 2;
            case THREE_OF_A_KIND: return 3;
            case STRAIGHT: case FLUSH: case FULL_HOUSE: return 4;
            case FOUR_OF_A_KIND: return 7;
            case STRAIGHT_FLUSH: return 8;
            case FIVE_OF_A_KIND: return 12;
            case FLUSH_HOUSE: return 14;
            case FLUSH_FIVE: return 16;
            case HIGH_CARD: default: return 1;
        }
    }

    public int getChipsByHand(HandType type) {
        Integer chipsValue = this.chips.getOrDefault(type, 1);
        return (chipsValue != null ? chipsValue : 1);
    }

    public int getMultByHand(HandType type) {
        Integer value = this.multipliers.getOrDefault(type, 1);
        return value != null ? value : 1;
    }

    public int getLevelByHand(HandType type) {
        Integer value = this.levels.getOrDefault(type, 1);
        return value != null ? value : 1;
    }

    public void incrementLevel(HandType type) {
        Integer currentLevel = this.levels.get(type);
        if (currentLevel == null) {
            currentLevel = 0;
        }
        this.levels.put(type, currentLevel + 1);
    }
}