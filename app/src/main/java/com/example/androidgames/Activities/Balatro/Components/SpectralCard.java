package com.example.androidgames.Activities.Balatro.Components;

import android.graphics.Bitmap;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpectralCard extends Consumable {
    public SpectralCard(String name, Bitmap image) {
        super(name, image);
    }
}