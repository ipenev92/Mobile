package com.example.androidgames.Activities.MovingGame;

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

import com.example.androidgames.Activities.Balatro.BalatroField;
import com.example.androidgames.Activities.Balatro.BalatroWin;
import com.example.androidgames.Activities.MainActivity;
import com.example.androidgames.R;

@SuppressLint("ClickableViewAccessibility")
public class MovingGame extends AppCompatActivity {
    private int GRID_SIZE;

    private GridLayout gridLayout;
    private Grid[][] grid;
    private Grid[][] undoGrid;
    private int score;
    private TextView scoreLabel;
    private String playerName;
    private Chronometer chronometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.moving_game);

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
                for (int i = 0; i < this.GRID_SIZE; i++) {
                    for (int j = 0; j < this.GRID_SIZE; j++) {
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

                    this.GRID_SIZE = size;
                    startGame();
                })
                .setNegativeButton("Cancel", (dialog, which) ->
                        requestBoardSize()).setCancelable(false).show();
    }

    private void startGame() {
        this.gridLayout.removeAllViews();
        this.gridLayout.setColumnCount(this.GRID_SIZE);
        this.gridLayout.setRowCount(this.GRID_SIZE);

        this.grid = new Grid[this.GRID_SIZE][this.GRID_SIZE];
        initializeGameBoard();

        for (int i = 0; i < 2; i++) {
            addRandomNumbers();
        }
        setupGestures();
    }

    private void initializeGameBoard() {
        for (int i = 0; i < this.GRID_SIZE; i++) {
            for (int j = 0; j < this.GRID_SIZE; j++) {
                Grid grid = new Grid(0, i, j);
                this.grid[i][j] = grid;
                addNumberToGrid(grid);
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
                * this.GRID_SIZE + grid.getCol());
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
        for (int i = 0; i < this.GRID_SIZE; i++) {
            for (int j = 0; j < this.GRID_SIZE; j++) {
                if (this.grid[i][j].getValue() == 0) {
                    emptySlots.add(this.grid[i][j]);
                }
            }
        }

        if (!emptySlots.isEmpty()) {
            Grid grid = emptySlots.get(new Random().nextInt(emptySlots.size()));
            grid.setValue(new Random().nextInt(10) < 9 ? 2 : 4);
            updateNumberView(grid);
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
        }

        resetMergedStatus();
        addRandomNumbers();
    }

    private void moveLeft() {
        boolean moved = false;

        for (int row = 0; row < this.GRID_SIZE; row++) {
            for (int col = 1; col < this.GRID_SIZE; col++) {
                if (this.grid[row][col].getValue() != 0) {
                    int targetCol = col;

                    for (int nextCol = col - 1; nextCol >= 0; nextCol--) {
                        if (this.grid[row][nextCol].getValue() == 0) {
                            targetCol = nextCol;
                        } else if (this.grid[row][nextCol].getValue() ==
                                this.grid[row][col].getValue() && !this.grid[row][nextCol].isMerged()) {
                            targetCol = nextCol;
                            updateScore(this.grid[row][col].getValue() * 2);
                            break;
                        } else {
                            break;
                        }
                    }

                    if (targetCol != col) {
                        if (this.grid[row][targetCol].getValue() == this.grid[row][col].getValue()
                                && !this.grid[row][targetCol].isMerged()) {
                            this.grid[row][targetCol].fuse(this.grid[row][col]);
                        } else {
                            this.grid[row][targetCol].setValue(this.grid[row][col].getValue());
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

    // Right moves
    private void flipBoardHorizontally() {
        for (int i = 0; i < this.GRID_SIZE; i++) {
            for (int j = 0; j < this.GRID_SIZE / 2; j++) {
                Grid temp = this.grid[i][j];
                this.grid[i][j] = this.grid[i][this.GRID_SIZE - 1 - j];
                this.grid[i][this.GRID_SIZE - 1 - j] = temp;
            }
        }
    }

    // Up/Down moves)
    private void transposeBoard() {
        for (int i = 0; i < this.GRID_SIZE; i++) {
            for (int j = i + 1; j < this.GRID_SIZE; j++) {
                Grid temp = this.grid[i][j];
                this.grid[i][j] = this.grid[j][i];
                this.grid[j][i] = temp;
            }
        }
    }

    private void resetMergedStatus() {
        for (int i = 0; i < this.GRID_SIZE; i++) {
            for (int j = 0; j < this.GRID_SIZE; j++) {
                this.grid[i][j].resetFuse();
            }
        }
    }

    private void saveLastState() {
        this.undoGrid = new Grid[this.GRID_SIZE][this.GRID_SIZE];
        for (int i = 0; i < this.GRID_SIZE; i++) {
            for (int j = 0; j < this.GRID_SIZE; j++) {
                this.undoGrid[i][j] = new Grid(this.grid[i][j].getValue(), i, j);
            }
        }
    }


    private void verifyGameState() {
        if (checkVictory()) {
            endGame("You win!");
        } else if (checkGameOver()) {
            endGame("No possible movies. You Lose.");
        }
    }

    private boolean checkVictory() {
        boolean victory = false;
        int points = 2048;

        for (int i = 0; i < this.GRID_SIZE; i++) {
            for (int j = 0; j < this.GRID_SIZE; j++) {
                if (this.grid[i][j].getValue() == points) {
                    victory = true;
                    break;
                }
            }
        }
        return victory;
    }

    private boolean checkGameOver() {
        boolean loss = true;

        for (int i = 0; i < this.GRID_SIZE; i++) {
            for (int j = 0; j < this.GRID_SIZE; j++) {
                if (this.grid[i][j].getValue() == 0) {
                    loss = false;
                    break;
                }
            }
        }

        if (loss) {
            for (int i = 0; i < this.GRID_SIZE; i++) {
                for (int j = 0; j < this.GRID_SIZE; j++) {
                    int valorActual = this.grid[i][j].getValue();

                    if (i > 0 && valorActual == this.grid[i - 1][j].getValue()) {
                        loss = false;
                        break;
                    }
                    if (i < this.GRID_SIZE - 1 && valorActual == this.grid[i + 1][j].getValue()) {
                        loss = false;
                        break;
                    }

                    if (j > 0 && valorActual == this.grid[i][j - 1].getValue()) {
                        loss = false;
                        break;
                    }

                    if (j < this.GRID_SIZE - 1 && valorActual == this.grid[i][j + 1].getValue()) {
                        loss = false;
                        break;
                    }
                }
            }
        }

        return loss;
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