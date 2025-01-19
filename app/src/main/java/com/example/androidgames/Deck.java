package com.example.androidgames;

import android.content.Context;
import android.content.res.Resources;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

@Getter
public class Deck {
    private final Context context;
    private final List<PlayingCard> deck;

    public Deck(Context context) {
        this.context = context;
        deck = new ArrayList<>();

        Class<?> resClass = R.drawable.class;
        Field[] fields = resClass.getFields();

        for (Field field : fields) {
            try {
                String name = field.getName();
                if (name.startsWith("card_")) {
                    String[] parts = name.split("_");

                    PlayingCard playingCard = new PlayingCard(name, parts[1], parts[3],
                            Enhancement.BASE, Edition.BASE, Seal.BASE);
                    deck.add(playingCard);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}