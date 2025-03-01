package com.example.androidgames.Activities.Balatro.Components;

import android.os.Parcel;
import android.os.Parcelable;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Joker extends Card implements Parcelable {
    private String rarity;

    public Joker(String name, Edition edition, String rarity) {
        super(name, edition);
        this.rarity = rarity;
    }

    /* ========== PARCELABLE ========== */
    protected Joker(Parcel in) {
        super(in.readString(), (Edition) in.readSerializable());
        this.rarity = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getName());
        dest.writeSerializable(getEdition());
        dest.writeString(rarity);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Joker> CREATOR = new Creator<Joker>() {
        @Override
        public Joker createFromParcel(Parcel in) {
            return new Joker(in);
        }

        @Override
        public Joker[] newArray(int size) {
            return new Joker[size];
        }
    };
}