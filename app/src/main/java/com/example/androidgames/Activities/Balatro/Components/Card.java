package com.example.androidgames.Activities.Balatro.Components;

import android.media.Image;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Card {
    private String name;
    private Edition edition;
    private Image image;

    public Card(String name, Edition edition) {
        this.name = name;
        this.edition = edition;
    }
}