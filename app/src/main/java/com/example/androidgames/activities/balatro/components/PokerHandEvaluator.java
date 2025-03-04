package com.example.androidgames.activities.balatro.components;

import java.util.*;

public class PokerHandEvaluator {
    private PokerHandEvaluator() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static HandType evaluateHand(List<PlayingCard> hand) {
        Map<String, Integer> valueCounts = new HashMap<>();
        Map<String, Integer> suitCounts = new HashMap<>();

        for (PlayingCard card : hand) {
            incrementMap(valueCounts, card.getValue());
            incrementMap(suitCounts, card.getSuit());
        }

        boolean isFlush = suitCounts.size() == 1;
        List<Integer> sortedValues = getSortedValues(valueCounts);
        boolean isStraight = sortedValues.size() == hand.size() && isSequential(sortedValues);

        if (hand.size() == 5) {
            HandType fiveCardType = evaluateFiveCardHand(valueCounts, isFlush, isStraight);
            if (fiveCardType != null) return fiveCardType;
        }

        if (valueCounts.containsValue(4))
            return HandType.FOUR_OF_A_KIND;

        if (isFullHouse(valueCounts))
            return HandType.FULL_HOUSE;

        if (isFlush && hand.size() >= 5)
            return HandType.FLUSH;

        if (isStraight && hand.size() >= 5)
            return HandType.STRAIGHT;

        if (valueCounts.containsValue(3))
            return HandType.THREE_OF_A_KIND;

        if (Collections.frequency(valueCounts.values(), 2) == 2)
            return HandType.TWO_PAIR;

        if (valueCounts.containsValue(2))
            return HandType.PAIR;

        return HandType.HIGH_CARD;
    }

    private static HandType evaluateFiveCardHand(Map<String, Integer> valueCounts,
                                                 boolean isFlush, boolean isStraight) {
        if (valueCounts.size() == 1 && isFlush)
            return HandType.FLUSH_FIVE;

        if (valueCounts.containsValue(5))
            return HandType.FIVE_OF_A_KIND;

        boolean fullHouse = isFullHouse(valueCounts);
        if (isFlush && fullHouse)
            return HandType.FLUSH_HOUSE;

        if (isFlush && isStraight)
            return HandType.STRAIGHT_FLUSH;

        return null;
    }

    private static void incrementMap(Map<String, Integer> map, String key) {
        map.compute(key, (k, current) -> (current != null ? current : 0) + 1);
    }

    private static List<Integer> getSortedValues(Map<String, Integer> valueCounts) {
        List<Integer> values = new ArrayList<>();
        for (String value : valueCounts.keySet()) {
            values.add(convertCardValue(value));
        }
        Collections.sort(values);
        return values;
    }

    private static int convertCardValue(String value) {
        switch (value.toLowerCase()) {
            case "ace":   return 14;
            case "king":  return 13;
            case "queen": return 12;
            case "jack":  return 11;
            default:      return Integer.parseInt(value);
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