package com.example.androidgames.Activities.Balatro.Components;

import java.util.*;

public class PokerHandEvaluator {
    public static HandType evaluateHand(List<PlayingCard> hand) {
        if (hand.isEmpty()) return null;
        if (hand.size() == 1) return HandType.HIGH_CARD; // Single card is always a High Card

        Map<String, Integer> valueCounts = new HashMap<>();
        Map<String, Integer> suitCounts = new HashMap<>();
        List<Integer> values = new ArrayList<>();

        // Count occurrences of each value and suit
        for (PlayingCard card : hand) {
            valueCounts.put(card.getValue(), valueCounts.getOrDefault(card.getValue(), 0) + 1);
            suitCounts.put(card.getSuit(), suitCounts.getOrDefault(card.getSuit(), 0) + 1);
        }

        boolean isFlush = suitCounts.size() == 1; // All same suit

        for (String value : valueCounts.keySet()) {
            values.add(convertCardValue(value));
        }

        Collections.sort(values);
        boolean isStraight = values.size() == hand.size() && isSequential(values);

        // Check for Flush Five (All same suit and value, and exactly 5 cards)
        if (hand.size() == 5 && valueCounts.size() == 1 && suitCounts.size() == 1)
            return HandType.FLUSH_FIVE;

        // Check for Five of a Kind (All same value, not necessarily same suit, and exactly 5 cards)
        if (hand.size() == 5 && valueCounts.containsValue(5))
            return HandType.FIVE_OF_A_KIND;

        // Check for Full House
        boolean isFullHouse = isFullHouse(valueCounts);

        // Check for Flush House (Full House but all same suit, and exactly 5 cards)
        if (hand.size() == 5 && isFlush && isFullHouse)
            return HandType.FLUSH_HOUSE;

        // Check for Straight Flush (Exactly 5 cards)
        if (hand.size() == 5 && isFlush && isStraight)
            return HandType.STRAIGHT_FLUSH;

        // Check for Four of a Kind (At least 4 matching values)
        if (valueCounts.containsValue(4))
            return HandType.FOUR_OF_A_KIND;

        // Check for Full House
        if (isFullHouse)
            return HandType.FULL_HOUSE;

        // Check for Flush (At least 5 cards of the same suit)
        if (isFlush && hand.size() >= 5)
            return HandType.FLUSH;

        // Check for Straight (At least 5 sequential values)
        if (isStraight && hand.size() >= 5)
            return HandType.STRAIGHT;

        // Check for Three of a Kind
        if (valueCounts.containsValue(3))
            return HandType.THREE_OF_A_KIND;

        // Check for Two Pair
        if (Collections.frequency(valueCounts.values(), 2) == 2)
            return HandType.TWO_PAIR;

        // Check for One Pair
        if (valueCounts.containsValue(2))
            return HandType.PAIR;

        return HandType.HIGH_CARD;
    }

    private static int convertCardValue(String value) {
        switch (value.toLowerCase()) {
            case "ace": return 14;
            case "king": return 13;
            case "queen": return 12;
            case "jack": return 11;
            default: return Integer.parseInt(value);
        }
    }

    private static boolean isSequential(List<Integer> values) {
        for (int i = 1; i < values.size(); i++) {
            if (values.get(i) != values.get(i - 1) + 1) {
                return false;
            }
        }
        return true;
    }

    private static boolean isFullHouse(Map<String, Integer> valueCounts) {
        return valueCounts.containsValue(3) && valueCounts.containsValue(2);
    }
}