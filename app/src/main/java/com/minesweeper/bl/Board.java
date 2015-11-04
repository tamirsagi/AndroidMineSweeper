package com.minesweeper.bl;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * This class represents a game board
 */
public class Board {

    private Cell[][] cells; //game board
    private int rows;
    private int columns;
    private int numberOfBombs;
    private List<Cell> bombsOnBoard;



    public Board(int rows,int columns,int numberOfBomb){
        this.rows = rows;
        this.columns = columns;
        this.numberOfBombs = numberOfBomb;
        initializeGameBoard(rows,columns);
    }

    private void initializeGameBoard(int rows,int columns){
        cells = new Cell[rows][columns];
        fillUpEmptyCells();
    }


    public void setBoardForNewGame(){
        fillUpBombs();
        setNumberOfAdjacentMinesForEachCell();
    }

    /**
     * Function fill up board with bombs cells in random indexes
     */
    private void fillUpBombs(){
        bombsOnBoard = new LinkedList<Cell>();
        Random rand = new Random();
        int placedBombs = 0;
          while( placedBombs < numberOfBombs){
              int row = rand.nextInt(rows);
              int col = rand.nextInt(columns);
              if(cells[row][col] == null){
                  Cell bombCell = new Cell(Cell.CellType.MINE,row,col);
                  cells[row][col]= bombCell;
                  bombsOnBoard.add(bombCell);
                  placedBombs++;
            }
        }
    }

    /**
     * Function fill up board with empty cells
     */
    private void fillUpEmptyCells(){
        for(int i = 0; i < rows; i++)
            for(int j = 0; j < columns; j++)
                if(cells[i][j] == null)
                    cells[i][j] = new Cell(Cell.CellType.EMPTY,i,j);
    }

    /**
     * Function calculate for relevant cells only the number Of adjacent mines
     */
    private void setNumberOfAdjacentMinesForEachCell(){

        for(int i = 0; i < bombsOnBoard.size(); i++){
            Cell bomb = bombsOnBoard.get(i);
            int bombRow = bomb.getRowNumber(), bombColumn = bomb.getColumnNumber(), relevantRow,relevantColumn; // keep Adjacent Cell index
            //One Cell Up
            if((relevantRow = bombRow - 1) >= 0 && cells[relevantRow][bombColumn].getCellType() != Cell.CellType.MINE)
                cells[relevantRow][bombColumn].setNumberOfAdjacentMines(cells[relevantRow][bombColumn].getNumberOfAdjacentMines() + 1);
            //one Cell Bottom
            if((relevantRow = bombRow + 1) < rows && cells[relevantRow][bombColumn].getCellType() != Cell.CellType.MINE)
                cells[relevantRow][bombColumn].setNumberOfAdjacentMines(cells[relevantRow][bombColumn].getNumberOfAdjacentMines() + 1);
            //Left Cell
            if((relevantColumn = bombColumn - 1) >= 0 && cells[bombRow][relevantRow].getCellType() != Cell.CellType.MINE)
                cells[bombRow][relevantColumn].setNumberOfAdjacentMines(cells[bombRow][relevantColumn].getNumberOfAdjacentMines() + 1);
            //Right Cell
            if((relevantColumn = bombColumn + 1) < columns && cells[bombRow][relevantColumn].getCellType() != Cell.CellType.MINE)
                cells[bombRow][relevantColumn].setNumberOfAdjacentMines(cells[bombRow][relevantColumn].getNumberOfAdjacentMines() + 1);
            //Top Right Diagonal
            if((relevantRow = bombRow - 1) >= 0 && (relevantColumn = bombColumn + 1) < columns
                    && cells[relevantRow][relevantColumn].getCellType() != Cell.CellType.MINE)
                cells[relevantRow][relevantColumn].setNumberOfAdjacentMines(cells[relevantRow][relevantColumn].getNumberOfAdjacentMines() + 1);
            //Bottom Right Diagonal
            if((relevantRow = bombRow + 1) < rows && (relevantColumn = bombColumn + 1) <columns
                    && cells[relevantRow][relevantColumn].getCellType() != Cell.CellType.MINE)
                cells[relevantRow][relevantColumn].setNumberOfAdjacentMines(cells[relevantRow][relevantColumn].getNumberOfAdjacentMines() + 1);
            //Top Left Diagonal
            if((relevantRow = bombRow - 1) >= 0 && (relevantColumn = bombColumn - 1) >= 0
                    && cells[relevantRow][relevantColumn].getCellType() != Cell.CellType.MINE)
                cells[relevantRow][relevantColumn].setNumberOfAdjacentMines(cells[relevantRow][relevantColumn].getNumberOfAdjacentMines() + 1);
            //Bottom Left Diagonal
            if((relevantRow = bombRow + 1) < rows && (relevantColumn = bombColumn - 1) >= 0
                    && cells[relevantRow][relevantColumn].getCellType() != Cell.CellType.MINE)
                cells[relevantRow][relevantColumn].setNumberOfAdjacentMines(cells[relevantRow][relevantColumn].getNumberOfAdjacentMines() + 1);
        }
    }


    /**
     * function disabled all cells.
     */
    public void lockBoard(){
        for(int i = 0; i < rows; i++)
            for(int j = 0; j < columns; j++)
                cells[i][j].setEnabled(false);
    }

    public void applyMove(int row,int column){

    }

}
