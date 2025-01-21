package com.example.androidgames.Activities.Balatro;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidgames.Deck;
import com.example.androidgames.R;

public class BalatroField extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.balatro_field);

        Deck deck = new Deck(this);
        int[] cardImages = new int[deck.getDeck().size()];

        for (int i = 0; i < deck.getDeck().size(); i++) {
            String cardName = deck.getDeck().get(i).getName();
            int id = getResources().getIdentifier(cardName, "drawable", getPackageName());
            cardImages[i] = id;
        }

        FrameLayout handLayout = findViewById(R.id.hand);
        for (int i = 0; i < 8; i++) {
            ImageView cardImageView = new ImageView(this);
            cardImageView.setImageResource(cardImages[i]);

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            );

            params.leftMargin = i * 200;

            cardImageView.setLayoutParams(params);
            handLayout.addView(cardImageView);
        }
    }
}