/**
 * This Application was created as part of academic course
 * Tamir Sagi
 */


package com.minesweeper.BL;
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
    private List<Cell> bombsOnBoard; //bomb on board
    private int cellsToReveal; //cells to reveal when click on an empty cell
    private int remainsCells;
    private Cell lastClicked;

    public Board(int rows,int columns,int numberOfBomb){
        this.rows = rows;
        this.columns = columns;
        this.numberOfBombs = numberOfBomb;
        remainsCells = this.rows * this.columns - numberOfBombs;    //remained cells to be revealed
        cellsToReveal = 0;
        initializeGameBoard(rows,columns);
    }

    private void initializeGameBoard(int rows,int columns){
        cells = new Cell[rows][columns];
        fillUpEmptyCells();
    }

    public void setFirstClickedCell(int row,int column){
        cells[row][column].setCellType(Cell.CellType.EMPTY_FIRST_CLICKED);
    }

    public Cell[][] getGameBoard(){
        return cells;
    }

    /**
     * Function set the board
     */
    public void setBoardForNewGame(){
        fillUpBombs();
        setNumberOfAdjacentMinesForEachCell();
    }


    /**
     * Function fills up board with bombs cells in random indexes
     */
    private void fillUpBombs(){
        bombsOnBoard = new LinkedList<Cell>();
        Random rand = new Random();
        int placedBombs = 0;
          while( placedBombs < numberOfBombs){
              int row = rand.nextInt(rows);
              int col = rand.nextInt(columns);
              if(canPlaceBomb(row,col)){
                  Cell bomb = new BombCell(row,col);
                  cells[row][col] = bomb;
                  bombsOnBoard.add(bomb);
                  placedBombs++;
            }
        }
    }

    /**
     *
     * @return true if a mine can be placed within a current cell or false otherwise
     */
    private boolean canPlaceBomb(int row, int column){
        return cells[row][column].getCellType() == Cell.CellType.EMPTY;
    }

    /**
     * Function fills up board with empty cells
     */
    public void fillUpEmptyCells(){
        for(int i = 0; i < rows; i++)
            for(int j = 0; j < columns; j++)
                cells[i][j] = new Cell(Cell.CellType.EMPTY,i,j);
    }

    /**
     * Function calculates the number Of adjacent mines for relevant cells only
     */
    private void setNumberOfAdjacentMinesForEachCell(){

        for(int i = 0; i < bombsOnBoard.size(); i++){
            Cell bomb = bombsOnBoard.get(i);
            int bombRow = bomb.getRowNumber(), bombColumn = bomb.getColumnNumber(), relevantRow,relevantColumn; // keep Adjacent Cell index
            //One Cell Up
            if((relevantRow = bombRow - 1) >= 0 && !cells[relevantRow][bombColumn].isBomb())
                cells[relevantRow][bombColumn].setNumberOfAdjacentMines(cells[relevantRow][bombColumn].getNumberOfAdjacentMines() + 1);
            //One Cell Bottom
            if((relevantRow = bombRow + 1) < rows && !cells[relevantRow][bombColumn].isBomb())
                cells[relevantRow][bombColumn].setNumberOfAdjacentMines(cells[relevantRow][bombColumn].getNumberOfAdjacentMines() + 1);
            //Left Cell
            if((relevantColumn = bombColumn - 1) >= 0 && !cells[bombRow][relevantColumn].isBomb())
                cells[bombRow][relevantColumn].setNumberOfAdjacentMines(cells[bombRow][relevantColumn].getNumberOfAdjacentMines() + 1);
            //Right Cell
            if ((relevantColumn = bombColumn + 1) < columns && !cells[bombRow][relevantColumn].isBomb())
                cells[bombRow][relevantColumn].setNumberOfAdjacentMines(cells[bombRow][relevantColumn].getNumberOfAdjacentMines() + 1);
            //Diagonal Top Right
            if((relevantRow = bombRow - 1) >= 0 && (relevantColumn = bombColumn + 1) < columns
                    && !cells[relevantRow][relevantColumn].isBomb())
                cells[relevantRow][relevantColumn].setNumberOfAdjacentMines(cells[relevantRow][relevantColumn].getNumberOfAdjacentMines() + 1);
            //Diagonal Bottom Right
            if((relevantRow = bombRow + 1) < rows && (relevantColumn = bombColumn + 1) < columns
                    && !cells[relevantRow][relevantColumn].isBomb())
                cells[relevantRow][relevantColumn].setNumberOfAdjacentMines(cells[relevantRow][relevantColumn].getNumberOfAdjacentMines() + 1);
            //Diagonal Top Left
            if((relevantRow = bombRow - 1) >= 0 && (relevantColumn = bombColumn - 1) >= 0
                    && !cells[relevantRow][relevantColumn].isBomb())
                cells[relevantRow][relevantColumn].setNumberOfAdjacentMines(cells[relevantRow][relevantColumn].getNumberOfAdjacentMines() + 1);
            //Diagonal Bottom Left
            if((relevantRow = bombRow + 1) < rows && (relevantColumn = bombColumn - 1) >= 0
                    && !cells[relevantRow][relevantColumn].isBomb())
                cells[relevantRow][relevantColumn].setNumberOfAdjacentMines(cells[relevantRow][relevantColumn].getNumberOfAdjacentMines() + 1);
        }
    }

    public boolean lost(){
        return getLastClikedCell().getCellType() == Cell.CellType.BOMB;
    }

    public boolean won(){
        return remainsCells == 0;
    }

    public void setBombCellsRevealed(){
        for (Cell c : bombsOnBoard)
            c.setRevealed(true);
    }

    public List<Cell> getBombsOnBoard(){
        return bombsOnBoard;
    }

    private Cell getLastClikedCell(){
        return lastClicked;
    }

    public void applyMove(int row,int column){
        if((lastClicked = cells[row][column]).getCellType() != Cell.CellType.BOMB) {
            cellsToReveal = 0;
            cellToRevealed(row, column);
            remainsCells -= cellsToReveal;
        }
        lastClicked.setClicked(true);
    }

    /**
     * Function picks cells to reveal(Marked and empty), If Cell Is empty we keep looking for other cells.
     * @param currentRow
     * @param currentColumn
     */
    private void cellToRevealed(int currentRow,int currentColumn){
        if(currentRow >= 0 && currentRow < rows && currentColumn >= 0 && currentColumn < columns &&
               !cells[currentRow][currentColumn].isBomb() && !cells[currentRow][currentColumn].isVisited()  ) {
            cells[currentRow][currentColumn].setVisited(true);
            cells[currentRow][currentColumn].setRevealed(true);
            cellsToReveal++;
            //if cell is empty check its adjacent cells
            if (cells[currentRow][currentColumn].isEmpty()){
                //Top Cell
                cellToRevealed(currentRow - 1,currentColumn);
                //Bottom Cell
                cellToRevealed(currentRow + 1,currentColumn);
                //left Cell
                cellToRevealed(currentRow,currentColumn - 1);
                //right Cell
                cellToRevealed(currentRow,currentColumn + 1);
                //Top right Cell
                cellToRevealed(currentRow - 1,currentColumn + 1);
                //Bottom right Cell
                cellToRevealed(currentRow + 1,currentColumn + 1);
                //Top Left Cell
                cellToRevealed(currentRow - 1,currentColumn - 1);
                //Bottom Left Cell
                cellToRevealed(currentRow + 1,currentColumn - 1);
            }
        }
    }

}
