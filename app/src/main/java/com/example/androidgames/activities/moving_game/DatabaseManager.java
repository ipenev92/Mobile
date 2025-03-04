package com.example.androidgames.activities.MovingGame;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import lombok.Getter;

@Getter
public class DatabaseManager extends SQLiteOpenHelper {
    public static final String POINTS = "points";
    public static final String ID = "id";
    public static final String COL_PLAYER = "player";
    public static final String COL_POINTS = "points";
    public static final String COL_DATE = "date";
    public static final String COL_TIME = "time";
    private static DatabaseManager instance;

    private DatabaseManager(Context context) {
        super(context, "points.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + POINTS + " ("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_PLAYER + " TEXT NOT NULL, "
                + COL_POINTS + " INTEGER NOT NULL, "
                + COL_TIME + " INTEGER NOT NULL, "
                + COL_DATE + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
                + ");";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + POINTS);
        onCreate(db);
    }

    public static synchronized DatabaseManager getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseManager(context.getApplicationContext());
        }
        return instance;
    }

    public Cursor getAllPlayers(boolean ordered, boolean max) {
        SQLiteDatabase db = this.getReadableDatabase();
        String order = COL_POINTS + " DESC";
        if (ordered) {
            order = COL_PLAYER + " ASC";
        }
        if (max) {
            order += " LIMIT 10";
        }
        String query = "SELECT * FROM " + POINTS + " ORDER BY " + order ;
        return db.rawQuery(query, null);
    }

    public void insertPoints(String player, int points, Long time) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(POINTS,
                new String[]{ID, COL_POINTS},
                COL_PLAYER + " = ?",
                new String[]{player},
                null, null, null);

        ContentValues values = new ContentValues();
        values.put(COL_PLAYER, player);
        values.put(COL_POINTS, points);
        values.put(COL_TIME, time);

        db.insert(POINTS, null, values);
        if (cursor != null) {
            cursor.close();
        }
    }
}