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
public class Consumable implements Parcelable {
    private String name;
    private Bitmap image; // Usamos Bitmap en lugar de Image porque es Parcelable

    public Consumable(String name, Bitmap image) {
        this.name = name;
        this.image = image;
    }

    /* ========== PARCELABLE ========== */
    protected Consumable(Parcel in) {
        name = in.readString();
        image = in.readParcelable(Bitmap.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeParcelable(image, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Consumable> CREATOR = new Creator<Consumable>() {
        @Override
        public Consumable createFromParcel(Parcel in) {
            return new Consumable(in);
        }

        @Override
        public Consumable[] newArray(int size) {
            return new Consumable[size];
        }
    };
}