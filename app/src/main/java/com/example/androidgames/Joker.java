package com.example.androidgames;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Joker extends Card {
    private String rarity;

    public Joker(String name, Edition edition) {
        super(name, edition);
    }
}