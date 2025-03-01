package com.example.androidgames.Activities.Balatro.Components;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameData implements Parcelable {
    private String hands;
    private String discards;
    private String gold;
    private final String deckName;
    private String round;
    private int roundScore;

    private Ante ante;
    private Deck deck;
    private PlanetData planetData;
    private ArrayList<PlayingCard> playingField;
    private ArrayList<Joker> jokers;
    private ArrayList<Consumable> consumables;

    public GameData(String selectedDeck) {
        this.round = "0";
        this.roundScore = 0;
        this.deckName = selectedDeck;

        this.hands = getInitialHands(this.deckName);
        this.discards = getInitialDiscards(this.deckName);
        this.gold = getInitialGold(this.deckName);

        this.ante = new Ante(0);
        this.deck = new Deck(this.deckName);

        this.planetData = new PlanetData();
        this.playingField = new ArrayList<>();
        this.jokers = new ArrayList<>();
        this.consumables = new ArrayList<>();
    }

    public static String getInitialHands(String deck) {
        switch (deck) {
            case "deck_blue": return "5";
            case "deck_black": return "3";
            default: return "4";
        }
    }

    public static String getInitialDiscards(String deck) {
        return Objects.equals(deck, "deck_red") ? "4" : "3";
    }

    public static String getInitialGold(String deck) {
        return Objects.equals(deck, "deck_yellow") ? "$14" : "$4";
    }

    /* ========== PARCELABLE ========== */
    protected GameData(Parcel in) {
        hands = in.readString();
        discards = in.readString();
        gold = in.readString();
        deckName = in.readString();
        round = in.readString();
        roundScore = in.readInt();
        ante = in.readParcelable(Ante.class.getClassLoader());
        deck = in.readParcelable(Deck.class.getClassLoader());
        planetData = in.readParcelable(PlanetData.class.getClassLoader());
        playingField = in.createTypedArrayList(PlayingCard.CREATOR);
        jokers = in.createTypedArrayList(Joker.CREATOR);
        consumables = in.createTypedArrayList(Consumable.CREATOR);
    }

    public static final Creator<GameData> CREATOR = new Creator<GameData>() {
        @Override
        public GameData createFromParcel(Parcel in) {
            return new GameData(in);
        }

        @Override
        public GameData[] newArray(int size) {
            return new GameData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(hands);
        parcel.writeString(discards);
        parcel.writeString(gold);
        parcel.writeString(deckName);
        parcel.writeString(round);
        parcel.writeInt(roundScore);
        parcel.writeParcelable(ante, flags);
        parcel.writeParcelable(deck, flags);
        parcel.writeParcelable(planetData, flags);
        parcel.writeTypedList(jokers);
        parcel.writeTypedList(playingField);
        parcel.writeTypedList(consumables);
    }
}