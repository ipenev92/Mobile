package com.example.androidgames.Activities.Balatro.Components;

import android.graphics.Bitmap;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TarotCard extends Consumable {
    public TarotCard(String name, Bitmap image) {
        super(name, image);
    }
}