package com.example.androidgames.activities.moving_game;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.androidgames.R;

import java.util.Locale;

@SuppressLint("Range")
public class MovingGameRanking extends AppCompatActivity {
    private TableLayout tableRanking;
    private DatabaseManager databaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        setTitle("Ranking");

        this.databaseManager = DatabaseManager.getInstance(this);
        this.tableRanking = findViewById(R.id.tableRanking);

        Button backToMenuButton = findViewById(R.id.btn_back_to_menu);

        loadRanking();

        backToMenuButton.setOnClickListener(v -> finish());
    }

    private void loadRanking() {
        Cursor cursor = this.databaseManager.getAllPlayers(false, true);
        if (cursor != null && cursor.moveToFirst()) {
            int childCount = this.tableRanking.getChildCount();
            if (childCount > 1) {
                this.tableRanking.removeViews(1, childCount - 1);
            }

            int pos = 1;
            do {
                if (pos > 10) break;

                String name = cursor.getString(cursor.getColumnIndex(DatabaseManager.COL_PLAYER));
                int points = cursor.getInt(cursor.getColumnIndex(DatabaseManager.COL_POINTS));
                long time = cursor.getLong(cursor.getColumnIndex(DatabaseManager.COL_TIME));

                int totalSeconds = (int) (time / 1000);
                int min = totalSeconds / 60;
                int sec = totalSeconds % 60;
                String formattedTime = String.format(Locale.US, "%02d:%02d", min, sec);

                TableRow row = new TableRow(this);
                row.setPadding(16, 16, 16, 16);

                TableRow.LayoutParams params = new TableRow.LayoutParams(
                        WRAP_CONTENT, WRAP_CONTENT);
                params.setMargins(50, 8, 50, 8);

                TextView nameView = new TextView(this);
                nameView.setText(String.format("%s. %s", pos, name));
                nameView.setTextSize(18);
                nameView.setTextColor(ContextCompat
                        .getColor(this, android.R.color.black));
                nameView.setLayoutParams(params);
                nameView.setGravity(Gravity.CENTER_VERTICAL);

                TextView pointsView = new TextView(this);
                pointsView.setText(String.valueOf(points));
                pointsView.setTextSize(18);
                pointsView.setTextColor(ContextCompat
                        .getColor(this, android.R.color.holo_blue_dark));
                pointsView.setLayoutParams(params);
                pointsView.setGravity(Gravity.CENTER_VERTICAL);

                TextView timeView = new TextView(this);
                timeView.setText(formattedTime);
                timeView.setTextSize(18);
                timeView.setTextColor(ContextCompat
                        .getColor(this, android.R.color.holo_green_dark));
                timeView.setLayoutParams(params);
                timeView.setGravity(Gravity.CENTER_VERTICAL);

                row.addView(nameView);
                row.addView(pointsView);
                row.addView(timeView);

                this.tableRanking.addView(row);

                pos++;
            } while (cursor.moveToNext());
            cursor.close();
        }
    }
}