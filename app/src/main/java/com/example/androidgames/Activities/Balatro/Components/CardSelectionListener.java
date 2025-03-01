package com.example.androidgames.Activities.Balatro.Components;

import java.util.ArrayList;

public interface CardSelectionListener {
    void onSelectionChanged(GameData gameData, ArrayList<PlayingCard> cards);
}