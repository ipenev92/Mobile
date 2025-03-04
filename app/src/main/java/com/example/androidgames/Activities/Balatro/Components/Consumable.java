package com.example.androidgames.Activities.Balatro.Components;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.Parcel;
import android.os.Parcelable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Consumable {
    private String name;
    private Bitmap image; // Usamos Bitmap en lugar de Image porque es Parcelable

    public Consumable(String name, Bitmap image) {
        this.name = name;
        this.image = image;
    }
}