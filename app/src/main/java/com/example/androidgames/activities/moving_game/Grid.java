package com.example.androidgames.activities.MovingGame;

import com.example.androidgames.R;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Grid {
    private int value;
    private int row;
    private int col;
    private boolean merged;

    public Grid(int value, int row, int col) {
        this.value = Math.max(0, value);
        this.row = row;
        this.col = col;
        this.merged = false;
    }

    public void fuse(Grid grid) {
        if (!this.merged && !grid.isMerged() && this.value == grid.getValue() && this.value > 0) {
            this.value *= 2;
            this.merged = true;
            grid.setValue(0);
        }
    }

    public void resetFuse() {
        this.merged = false;
    }

    public int getColor() {
        switch (value) {
            case 2: return R.color.tile_2;
            case 4: return R.color.tile_4;
            case 8: return R.color.tile_8;
            case 16: return R.color.tile_16;
            case 32: return R.color.tile_32;
            case 64: return R.color.tile_64;
            case 128: return R.color.tile_128;
            case 256: return R.color.tile_256;
            case 512: return R.color.tile_512;
            case 1024: return R.color.tile_1024;
            case 2048: return R.color.tile_2048;
            default: return R.color.tile_high;
        }
    }
}