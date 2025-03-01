package com.example.androidgames.Activities.Balatro.Components;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayingCard extends Card implements Parcelable {
    private String value;
    private String suit;
    private Enhancement enhancement;
    private Seal seal;
    private int chips;
    private int mult;

    public PlayingCard(String name, String value, String suit, Enhancement enhancement,
                       Edition edition, Seal seal) {
        super(name, edition);
        this.value = value;
        this.suit = suit;
        this.enhancement = enhancement;
        this.seal = seal;
        this.chips = getCardChips();
        this.mult = 1;
    }

    private int getCardChips() {
        switch (this.getName()) {
            case "ace": return 11;
            case "king": case "queen": case "jack": case "10": return 10;
            case "9": return 9;
            case "8": return 8;
            case "7": return 7;
            case "6": return 6;
            case "5": return 5;
            case "4": return 4;
            case "3": return 3;
            case "2": return 2;
            default: return 0;
        }
    }

    /* ========== PARCELABLE ========== */
    protected PlayingCard(Parcel in) {
        super(in.readString(), (Edition) in.readSerializable()); // Read parent properties
        value = in.readString();
        suit = in.readString();
        enhancement = (Enhancement) in.readSerializable();
        seal = (Seal) in.readSerializable();
        chips = in.readInt();
        mult = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getName());
        dest.writeSerializable(getEdition());
        dest.writeString(value);
        dest.writeString(suit);
        dest.writeSerializable(enhancement);
        dest.writeSerializable(seal);
        dest.writeInt(chips);
        dest.writeInt(mult);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PlayingCard> CREATOR = new Creator<PlayingCard>() {
        @Override
        public PlayingCard createFromParcel(Parcel in) {
            return new PlayingCard(in);
        }

        @Override
        public PlayingCard[] newArray(int size) {
            return new PlayingCard[size];
        }
    };
}