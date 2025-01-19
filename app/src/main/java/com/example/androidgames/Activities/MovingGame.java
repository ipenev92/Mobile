package com.example.androidgames.Activities;

import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidgames.R;

import java.util.Random;

public class MovingGame extends AppCompatActivity {

    private static final int GRID_SIZE = 4;
    private int[][] grid = new int[GRID_SIZE][GRID_SIZE];
    private int[][] previousGrid = new int[GRID_SIZE][GRID_SIZE]; // For undo feature
    private int previousScore = 0; // To store the score before the move

    private TextView[][] cells = new TextView[GRID_SIZE][GRID_SIZE];
    private GridLayout gridLayout;
    private TextView gameOverMessage;
    private TextView scoreView;
    private Handler handler;

    private boolean undoAvailable = true;
    private int movesSinceUndo = 0;
    private int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.moving_game);

        gridLayout = findViewById(R.id.grid_layout);
        gameOverMessage = findViewById(R.id.game_over_message);
        scoreView = findViewById(R.id.score_view);

        handler = new Handler();

        initializeGrid();
        spawnNumber();
        spawnNumber();
        updateUI();

        // Setup undo button
        Button undoButton = findViewById(R.id.button_undo);
        undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (undoAvailable) {
                    undo();
                    undoAvailable = false;
                    movesSinceUndo = 0;
                    updateUndoButtonState(undoButton); // Update button appearance
                    updateUI();
                }
            }
        });
    }

    private void updateUndoButtonState(Button undoButton) {
        if (undoAvailable) {
            // Button is enabled
            undoButton.setEnabled(true);
            undoButton.setText("Undo");
            undoButton.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_dark));
        } else {
            // Button is on cooldown
            undoButton.setEnabled(false);
            undoButton.setText("Undo (" + (5 - movesSinceUndo) + ")");
            undoButton.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        }
    }

    private void initializeGrid() {
        gridLayout.setRowCount(GRID_SIZE);
        gridLayout.setColumnCount(GRID_SIZE);

        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                TextView cell = new TextView(this);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 225;  // Cell width
                params.height = 225;  // Cell height
                params.setMargins(10, 10, 10, 10);  // Margins for thicker borders
                cell.setLayoutParams(params);

                cell.setGravity(android.view.Gravity.CENTER);
                cell.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                cell.setTextSize(32);
                cell.setBackgroundColor(getResources().getColor(android.R.color.white));

                cell.setPadding(0, 0, 0, 0);

                gridLayout.addView(cell);
                cells[i][j] = cell;
                grid[i][j] = 0;
            }
        }
    }

    private void saveGridState() {
        // Save the current grid state and score before the move
        for (int i = 0; i < GRID_SIZE; i++) {
            System.arraycopy(grid[i], 0, previousGrid[i], 0, GRID_SIZE);
        }
        previousScore = score;
    }

    private void spawnNumber() {
        Random random = new Random();
        int x, y;
        do {
            x = random.nextInt(GRID_SIZE);
            y = random.nextInt(GRID_SIZE);
        } while (grid[x][y] != 0);
        grid[x][y] = random.nextInt(10) == 0 ? 4 : 2;
    }

    private void updateUI() {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                int value = grid[i][j];
                cells[i][j].setText(value == 0 ? "" : String.valueOf(value));
            }
        }
        updateScoreUI();
    }

    private void updateScoreUI() {
        scoreView.setText("Score: " + score);
    }

    private boolean hasPossibleMoves() {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (grid[i][j] == 0) return true;
                if (i < GRID_SIZE - 1 && grid[i][j] == grid[i + 1][j]) return true;
                if (j < GRID_SIZE - 1 && grid[i][j] == grid[i][j + 1]) return true;
            }
        }
        return false;
    }

    private void showGameOverMessage() {
        gameOverMessage.setVisibility(View.VISIBLE);
    }

    private boolean swipe(int direction) {
        saveGridState(); // Save the grid and score state before the move
        boolean moved = false;
        switch (direction) {
            case KeyEvent.KEYCODE_DPAD_LEFT:
                moved = moveLeft();
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                rotateGrid();
                rotateGrid();
                moved = moveLeft();
                rotateGridBack();
                rotateGridBack();
                break;
            case KeyEvent.KEYCODE_DPAD_UP:
                rotateGridBack();
                moved = moveLeft();
                rotateGrid();
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                rotateGrid();
                moved = moveLeft();
                rotateGridBack();
                break;
        }
        if (moved) {
            spawnNumber();
            movesSinceUndo++; // Increment the move count
            if (movesSinceUndo >= 5) {
                undoAvailable = true;
            }
        }
        return moved;
    }

    private boolean moveLeft() {
        boolean moved = false;
        for (int i = 0; i < GRID_SIZE; i++) {
            int[] row = grid[i];
            int[] newRow = new int[GRID_SIZE];
            int writeIndex = 0;
            boolean[] merged = new boolean[GRID_SIZE];

            for (int j = 0; j < GRID_SIZE; j++) {
                if (row[j] != 0) {
                    if (writeIndex > 0 && newRow[writeIndex - 1] == row[j] && !merged[writeIndex - 1]) {
                        newRow[writeIndex - 1] *= 2;
                        score += newRow[writeIndex - 1]; // Add to score
                        merged[writeIndex - 1] = true;
                        moved = true;
                    } else {
                        newRow[writeIndex] = row[j];
                        if (writeIndex != j) {
                            moved = true;
                        }
                        writeIndex++;
                    }
                }
            }

            System.arraycopy(newRow, 0, row, 0, GRID_SIZE);
        }
        return moved;
    }

    private void rotateGrid() {
        int[][] newGrid = new int[GRID_SIZE][GRID_SIZE];
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                newGrid[j][GRID_SIZE - 1 - i] = grid[i][j];
            }
        }
        grid = newGrid;
    }

    private void rotateGridBack() {
        int[][] newGrid = new int[GRID_SIZE][GRID_SIZE];
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                newGrid[GRID_SIZE - 1 - j][i] = grid[i][j];
            }
        }
        grid = newGrid;
    }

    private void undo() {
        // Revert to previous grid state and previous score
        for (int i = 0; i < GRID_SIZE; i++) {
            System.arraycopy(previousGrid[i], 0, grid[i], 0, GRID_SIZE);
        }
        score = previousScore; // Revert the score
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean moved = swipe(keyCode); // Perform the move
        if (moved) {
            updateUI();

            // Track the movement for the undo cooldown
            Button undoButton = findViewById(R.id.button_undo);
            updateUndoButtonState(undoButton);

            if (!hasPossibleMoves()) {
                showGameOverMessage();
            }
        }

        // Call the parent class implementation for proper handling of the key event
        return super.onKeyDown(keyCode, event);
    }
}