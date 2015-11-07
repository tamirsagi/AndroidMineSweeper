package com.minesweeper.bl;

/**
 * Class Represents a single cell within the board. it's responsible for,type and state
 */
public class Cell {

    private CellType cellType;
    private boolean enabled;
    private boolean revealed;
    private boolean flagged;
    public boolean visited; //use for open up cells
    private int numberOfAdjacentMines;
    private int row;
    private int col;


    public Cell(CellType cellType,int row,int column){
        this.cellType = cellType;
        this.row = row;
        this.col = column;
        enabled = true;
        flagged = false;
        revealed = false;
        visited = false;
        numberOfAdjacentMines = 0;
    }

    /**
     * EMPTY_FIRST_CLICKED - First Cell To be clicked prior game starts(uses for not placing a bomb on it)
     * EMPTY - If a cell is neither Bomb or Marked
     * BOMB - Contains Bomb
     * MARKED - Contain a number which indicates number of adjacent mines
     */
    public enum CellType{
        EMPTY_FIRST_CLICKED,EMPTY, BOMB, MARKED
    }


    public void setCellType(CellType newType){
        cellType = newType;
    }

    public CellType getCellType(){
        return cellType;
    }

    public boolean isEnabled(){
        return enabled;
    }

    public void setEnabled(boolean enabled){
            this.enabled = enabled;
    }

    public boolean isRevealed(){
        return revealed;
    }

    public void setRevealed(boolean revealed){
        this.revealed = revealed;
    }

    public boolean isEmpty(){
        return cellType == CellType.EMPTY || cellType == CellType.EMPTY_FIRST_CLICKED;
    }

    public void setCellMarked(){
        cellType = CellType.MARKED;
    }

    public boolean isCellMarked(){
        return cellType == CellType.MARKED;
    }

    public boolean isFlagged(){
        return flagged;
    }

    public void setFlagged(boolean isFlagged){
        flagged = isFlagged;
        enabled = flagged;
    }

    public int getRowNumber(){
        return row;
    }

    public boolean isVisited(){
        return visited;
    }

    public void setVisited(boolean visited){
        this.visited = visited;
    }

    public int getColumnNumber(){
        return col;
    }

    public int getNumberOfAdjacentMines(){
        return numberOfAdjacentMines;
    }

    public void setNumberOfAdjacentMines(int value){
        // change cell type only once despite we might change its value
        if(numberOfAdjacentMines == 0 && value > 0)
            setCellType(CellType.MARKED);
        numberOfAdjacentMines = value;
    }

    public boolean containsBomb(){
        return cellType == CellType.BOMB;
    }






}
