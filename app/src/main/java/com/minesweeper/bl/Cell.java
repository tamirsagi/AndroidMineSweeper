/**
 * This Application was created as part of academic course
 * Tamir Sagi
 */


package com.minesweeper.BL;

import android.util.Log;
import com.minesweeper.app.R;

/**
 * Class Represents a single cell within the board. it's responsible for,type and state
 */
public class Cell {

    private static int noImage = -1;
    private CellType cellType;
    private boolean revealed;
    private boolean clicked;
    private boolean flagged;
    public boolean visited; //use for open up cells
    private int numberOfAdjacentMines;
    private int row;
    private int column;

    public Cell() {
    }

    public Cell(CellType cellType, int row, int column) {
        this.cellType = cellType;
        this.row = row;
        this.column = column;
        flagged = false;
        revealed = false;
        visited = false;
        numberOfAdjacentMines = 0;
    }

    /**
     * EMPTY_FIRST_CLICKED - First Cell To be clicked prior game starts(uses for not placing a bomb on it)
     * EMPTY - If a cell is neither Bomb or Marked
     * BOMB - Contains Bomb
     * MARKED - Contains a number which indicates number of adjacent mines
     */
    public enum CellType {
        EMPTY_FIRST_CLICKED, EMPTY, BOMB, MARKED
    }

    public void setCellType(CellType newType) {
        cellType = newType;
    }

    public CellType getCellType() {
        return cellType;
    }


    public int getCellImage() {
        if (isRevealed()) {
            if (isFlagged())
                return R.drawable.flag_cell;
            return R.drawable.disabled_cell;
        }
        else if (isFlagged())
            return R.drawable.flag_cell;
        return R.drawable.empty_cell;
    }


    public boolean isRevealed() {
        return revealed;
    }

    public void setRevealed(boolean revealed) {
        this.revealed = revealed;
    }

    public boolean isEmpty() {
        return cellType == CellType.EMPTY || cellType == CellType.EMPTY_FIRST_CLICKED;
    }

    public boolean isCellMarked() {
        return cellType == CellType.MARKED;
    }

    public boolean isBomb() {
        return cellType == CellType.BOMB;
    }

    public boolean isFlagged() {
        return flagged;
    }

    public void setFlagged(boolean isFlagged) {
        flagged = isFlagged;
    }

    public boolean isClicked() {
        return clicked;
    }

    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }

    public int getRowNumber() {
        return row;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public int getColumnNumber() {
        return column;
    }

    public int getNumberOfAdjacentMines() {
        return numberOfAdjacentMines;
    }

    public void setNumberOfAdjacentMines(int value) {
        // change cell type only once despite we might change its value
        if (numberOfAdjacentMines == 0 && value > 0)
            setCellType(CellType.MARKED);
        numberOfAdjacentMines = value;
    }


}
