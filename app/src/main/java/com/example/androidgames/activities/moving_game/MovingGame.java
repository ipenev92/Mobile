package com.example.androidgames.activities.moving_game;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.InputType;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import android.widget.Chronometer;

import com.example.androidgames.activities.MainActivity;
import com.example.androidgames.R;

import lombok.Getter;

@Getter
@SuppressLint("ClickableViewAccessibility")
public class MovingGame extends AppCompatActivity {
    private int gridSize;
    private GridLayout gridLayout;
    private Grid[][] grid;
    private Grid[][] undoGrid;
    private int score;
    private TextView scoreLabel;
    private String playerName;
    private Chronometer chronometer;
    private DatabaseManager databaseManager;
    private final Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.moving_game);

        databaseManager = DatabaseManager.getInstance(this);

        requestBoardSize();
        requestPlayerName();

        this.gridLayout = findViewById(R.id.gridLayout);
        this.scoreLabel = findViewById(R.id.scoreLabel);

        this.chronometer = findViewById(R.id.chronometer);
        this.chronometer.setBase(SystemClock.elapsedRealtime());
        this.chronometer.start();

        Button redo = findViewById(R.id.btn_turn_comeback);
        redo.setOnClickListener(v -> {
            if (this.undoGrid != null) {
                for (int i = 0; i < this.gridSize; i++) {
                    for (int j = 0; j < this.gridSize; j++) {
                        this.grid[i][j].setValue(this.undoGrid[i][j].getValue());
                        updateNumberView(this.grid[i][j]);
                    }
                }
                Toast.makeText(this, "Undone", Toast.LENGTH_SHORT).show();
            }
        });

        Button back = findViewById(R.id.btn_back_to_menu);
        back.setOnClickListener((v -> {
            Intent intent = new Intent(MovingGame.this, MainActivity.class);
            startActivity(intent);
        }));
    }

    private void requestBoardSize() {
        EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setHint("Example: 4");

        new AlertDialog.Builder(this).setTitle("Board Size")
                .setMessage("Input the board size (3-8):").setView(input)
                .setPositiveButton("Accept", (dialog, which) -> {
                    int size = Integer.parseInt(input.getText().toString());

                    if (size < 3) {
                        size = 3;
                        Toast.makeText(this, "Size set to 3.",
                                Toast.LENGTH_SHORT).show();
                    }

                    if (size > 8) {
                        size = 8;
                        Toast.makeText(this, "Size set to 8.",
                                Toast.LENGTH_SHORT).show();
                    }

                    this.gridSize = size;
                    startGame();
                })
                .setNegativeButton("Cancel", (dialog, which) ->
                        requestBoardSize()).setCancelable(false).show();
    }

    private void startGame() {
        this.gridLayout.removeAllViews();
        this.gridLayout.setColumnCount(this.gridSize);
        this.gridLayout.setRowCount(this.gridSize);

        this.grid = new Grid[this.gridSize][this.gridSize];
        initializeGameBoard();

        for (int i = 0; i < 2; i++) {
            addRandomNumbers();
        }
        setupGestures();
    }

    private void initializeGameBoard() {
        for (int i = 0; i < this.gridSize; i++) {
            for (int j = 0; j < this.gridSize; j++) {
                Grid board = new Grid(0, i, j);
                this.grid[i][j] = board;
                addNumberToGrid(board);
            }
        }
    }

    private void addNumberToGrid(Grid grid) {
        TextView tile = new TextView(this);
        tile.setGravity(Gravity.CENTER);
        tile.setTextSize(24);
        tile.setBackgroundColor(ContextCompat.getColor(this, R.color.tile_empty));
        tile.setText(grid.getValue() > 0 ? String.valueOf(grid.getValue()) : "");

        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = 0;
        params.height = 0;
        params.columnSpec = GridLayout.spec(grid.getCol(), 1, 1.0f);
        params.rowSpec = GridLayout.spec(grid.getRow(), 1, 1.0f);
        params.setMargins(8, 8, 8, 8);

        this.gridLayout.addView(tile, params);
    }

    private void updateNumberView(Grid grid) {
        TextView tile = (TextView) this.gridLayout.getChildAt(grid.getRow()
                * this.gridSize + grid.getCol());
        if (grid.getValue() == 0) {
            tile.setBackgroundColor(ContextCompat.getColor(this, R.color.tile_empty));
            tile.setText("");
        } else {
            tile.setBackgroundColor(ContextCompat.getColor(this, grid.getColor()));
            tile.setText(String.valueOf(grid.getValue()));
        }
    }

    private void addRandomNumbers() {
        List<Grid> emptySlots = new ArrayList<>();
        for (int i = 0; i < this.gridSize; i++) {
            for (int j = 0; j < this.gridSize; j++) {
                if (this.grid[i][j].getValue() == 0) {
                    emptySlots.add(this.grid[i][j]);
                }
            }
        }

        if (!emptySlots.isEmpty()) {
            Grid board = emptySlots.get(this.random.nextInt(emptySlots.size()));
            board.setValue(this.random.nextInt(10) < 9 ? 2 : 4);
            updateNumberView(board);
        }
    }

    private void updateScore(int points) {
        this.score += points;
        scoreLabel.setText(String.format(Locale.US, "Score: %d", score));
    }

    private void setupGestures() {
        this.gridLayout.setOnTouchListener(new OnSwipeTouchListener(this) {
            @Override
            public void onSwipeLeft() {
                move(0);
                verifyGameState();
                gridLayout.performClick();
            }

            @Override
            public void onSwipeRight() {
                move(1);
                verifyGameState();
                gridLayout.performClick();
            }
            @Override
            public void onSwipeUp() {
                move(2);
                verifyGameState();
                gridLayout.performClick();
            }

            @Override
            public void onSwipeDown() {
                move(3);
                verifyGameState();
                gridLayout.performClick();
            }
        });
    }

    private void move(int direction) {
        saveLastState();

        switch (direction) {
            case 0:
                moveLeft();
                break;
            case 1:
                flipBoardHorizontally();
                moveLeft();
                flipBoardHorizontally();
                break;
            case 2:
                transposeBoard();
                moveLeft();
                transposeBoard();
                break;
            case 3:
                transposeBoard();
                flipBoardHorizontally();
                moveLeft();
                flipBoardHorizontally();
                transposeBoard();
                break;
            default:
        }

        resetMergedStatus();
        addRandomNumbers();
    }

    private void moveLeft() {
        boolean moved = false;
        for (int row = 0; row < this.gridSize; row++) {
            for (int col = 1; col < this.gridSize; col++) {
                int value = this.grid[row][col].getValue();
                if (value != 0) {
                    int targetCol = findTargetColumn(row, col);
                    if (targetCol != col) {
                        if (this.grid[row][targetCol].getValue() == value
                                && !this.grid[row][targetCol].isMerged()) {
                            this.grid[row][targetCol].fuse(this.grid[row][col]);
                        } else {
                            this.grid[row][targetCol].setValue(value);
                            this.grid[row][col].setValue(0);
                        }
                        updateNumberView(this.grid[row][targetCol]);
                        updateNumberView(this.grid[row][col]);
                        moved = true;
                    }
                }
            }
        }
        if (moved) {
            addRandomNumbers();
        }
    }

    private int findTargetColumn(int row, int col) {
        int value = this.grid[row][col].getValue();
        int targetCol = col;
        for (int nextCol = col - 1; nextCol >= 0; nextCol--) {
            int nextValue = this.grid[row][nextCol].getValue();
            if (nextValue == 0) {
                targetCol = nextCol;
            } else {
                if (nextValue == value && !this.grid[row][nextCol].isMerged()) {
                    targetCol = nextCol;
                    updateScore(value * 2);
                }
                break;
            }
        }
        return targetCol;
    }


    // Right moves
    private void flipBoardHorizontally() {
        for (int i = 0; i < this.gridSize; i++) {
            for (int j = 0; j < this.gridSize / 2; j++) {
                Grid temp = this.grid[i][j];
                this.grid[i][j] = this.grid[i][this.gridSize - 1 - j];
                this.grid[i][this.gridSize - 1 - j] = temp;
            }
        }
    }

    // Up/Down moves)
    private void transposeBoard() {
        for (int i = 0; i < this.gridSize; i++) {
            for (int j = i + 1; j < this.gridSize; j++) {
                Grid temp = this.grid[i][j];
                this.grid[i][j] = this.grid[j][i];
                this.grid[j][i] = temp;
            }
        }
    }

    private void resetMergedStatus() {
        for (int i = 0; i < this.gridSize; i++) {
            for (int j = 0; j < this.gridSize; j++) {
                this.grid[i][j].resetFuse();
            }
        }
    }

    private void saveLastState() {
        this.undoGrid = new Grid[this.gridSize][this.gridSize];
        for (int i = 0; i < this.gridSize; i++) {
            for (int j = 0; j < this.gridSize; j++) {
                this.undoGrid[i][j] = new Grid(this.grid[i][j].getValue(), i, j);
            }
        }
    }


    private void verifyGameState() {
        long time = SystemClock.elapsedRealtime() - chronometer.getBase();

        if (checkVictory()) {
            endGame("You win!");
            databaseManager.insertPoints(this.playerName, this.score, time);
        } else if (checkGameOver()) {
            endGame("No possible movies. You Lose.");
            databaseManager.insertPoints(this.playerName, this.score, time);
        }
    }

    private boolean checkVictory() {
        boolean victory = false;
        int points = 2048;

        for (int i = 0; i < this.gridSize; i++) {
            for (int j = 0; j < this.gridSize; j++) {
                if (this.grid[i][j].getValue() == points) {
                    victory = true;
                    break;
                }
            }
        }
        return victory;
    }

    private boolean checkGameOver() {
        for (int i = 0; i < this.gridSize; i++) {
            for (int j = 0; j < this.gridSize; j++) {
                int current = this.grid[i][j].getValue();

                if (current == 0) {
                    return false;
                }

                if ((i > 0 && current == this.grid[i - 1][j].getValue()) ||
                        (i < this.gridSize - 1 && current == this.grid[i + 1][j].getValue()) ||
                        (j > 0 && current == this.grid[i][j - 1].getValue()) ||
                        (j < this.gridSize - 1 && current == this.grid[i][j + 1].getValue())) {
                    return false;
                }
            }
        }
        return true;
    }


    private void endGame(String message) {
        this.chronometer.stop();

        new AlertDialog.Builder(this)
                .setTitle("Game Over")
                .setMessage(message + "\nPlay again? \n")
                .setPositiveButton("Yes", (dialog, which) -> startGame())
                .setNegativeButton("No", (dialog, which) -> finish())
                .setCancelable(false)
                .show();
    }

    private void requestPlayerName() {
        EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint("Insert Name");

        new AlertDialog.Builder(this).setTitle("Player Name").setMessage("Insert Name:")
                .setView(input).setCancelable(false)
                .setPositiveButton("Accept", (dialog, which) -> {
                    String name = input.getText().toString().trim();
                    if (!name.isEmpty()) {
                        this.playerName = name;
                        Toast.makeText(MovingGame.this, "Welcome "
                                + this.playerName + "!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MovingGame.this,
                                "Name cannot be empty.", Toast.LENGTH_SHORT).show();
                        requestPlayerName();
                    }
                }).show();
    }
}