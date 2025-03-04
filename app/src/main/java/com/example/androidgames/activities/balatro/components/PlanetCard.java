package com.example.androidgames.activities.balatro.components;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlanetCard {
    private final String name;
    private String text;
    private String price;

    private static final Map<String, PlanetData> PLANET_MAP = new HashMap<>();

    static {
        PLANET_MAP.put("planet_pluto", new PlanetData("High Card +1 Mult" +
                " and +10 Chips", "3"));
        PLANET_MAP.put("planet_mercury", new PlanetData("Pair +1 Mult" +
                " and +15 Chips", "3"));
        PLANET_MAP.put("planet_uranus", new PlanetData("Two Pair +2 Mult" +
                " and +20 Chips", "3"));
        PLANET_MAP.put("planet_venus", new PlanetData("Three of a Kind +3 Mult and +30 Chips",
                "3"));
        PLANET_MAP.put("planet_saturn", new PlanetData("Straight +3 Mult" +
                " and +30 Chips", "3"));
        PLANET_MAP.put("planet_jupiter", new PlanetData("Flush +2 Mult" +
                " and +15 Chips", "3"));
        PLANET_MAP.put("planet_earth", new PlanetData("Full House +2 Mult" +
                " and +25 Chips", "3"));
        PLANET_MAP.put("planet_mars", new PlanetData("Four of a Kind +3 Mult" +
                " and +30 Chips", "3"));
        PLANET_MAP.put("planet_neptune", new PlanetData("Straight Flush +4 Mult" +
                " and +40 Chips", "3"));
    }

    public PlanetCard(String name) {
        this.name = name;
        this.updatePlanet();
    }

    private void updatePlanet() {
        PlanetData data = PLANET_MAP.get(this.name);
        if (data != null) {
            this.text = data.getText();
            this.price = data.getPrice();
        }
    }

    @Getter
    private static class PlanetData {
        private final String text;
        private final String price;

        public PlanetData(String text, String price) {
            this.text = text;
            this.price = price;
        }
    }
}