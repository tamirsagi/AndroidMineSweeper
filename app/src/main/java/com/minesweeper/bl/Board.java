/**
 * This Application was created as part of academic course
 * Tamir Sagi
 */


package com.minesweeper.BL;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
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
    private List<Cell> cellsToReveal; //cells to reveal when click on an empty cell
    private int remainsCells;
    private Cell.CellType lastClicked;

    public Board(int rows,int columns,int numberOfBomb){
        this.rows = rows;
        this.columns = columns;
        this.numberOfBombs = numberOfBomb;
        remainsCells = this.rows * this.columns - numberOfBombs;    //cells to be revealed
        cellsToReveal = new LinkedList<Cell>();
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
                  cells[row][col].setCellType(Cell.CellType.BOMB);
                  bombsOnBoard.add(cells[row][col]);
                  placedBombs++;
            }
        }
    }

    /**
     *
     * @return true if a mine can be placed within a current cell or false otherwise
     */
    private boolean canPlaceBomb(int row, int column){
        return !cells[row][column].containsBomb() && !cells[row][column].isCellMarked() &&
                cells[row][column].getCellType() != Cell.CellType.EMPTY_FIRST_CLICKED;
    }

    /**
     * Function fills up board with empty cells
     */
    private void fillUpEmptyCells(){
        for(int i = 0; i < rows; i++)
            for(int j = 0; j < columns; j++)
                if(cells[i][j] == null)
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
            if((relevantRow = bombRow - 1) >= 0 && !cells[relevantRow][bombColumn].containsBomb())
                cells[relevantRow][bombColumn].setNumberOfAdjacentMines(cells[relevantRow][bombColumn].getNumberOfAdjacentMines() + 1);
            //one Cell Bottom
            if((relevantRow = bombRow + 1) < rows && !cells[relevantRow][bombColumn].containsBomb())
                cells[relevantRow][bombColumn].setNumberOfAdjacentMines(cells[relevantRow][bombColumn].getNumberOfAdjacentMines() + 1);
            //Left Cell
            if((relevantColumn = bombColumn - 1) >= 0 && !cells[bombRow][relevantColumn].containsBomb())
                cells[bombRow][relevantColumn].setNumberOfAdjacentMines(cells[bombRow][relevantColumn].getNumberOfAdjacentMines() + 1);
            //Right Cell
            if ((relevantColumn = bombColumn + 1) < columns && !cells[bombRow][relevantColumn].containsBomb())
                cells[bombRow][relevantColumn].setNumberOfAdjacentMines(cells[bombRow][relevantColumn].getNumberOfAdjacentMines() + 1);
            //Top Right
            if((relevantRow = bombRow - 1) >= 0 && (relevantColumn = bombColumn + 1) < columns
                    && !cells[relevantRow][relevantColumn].containsBomb())
                cells[relevantRow][relevantColumn].setNumberOfAdjacentMines(cells[relevantRow][relevantColumn].getNumberOfAdjacentMines() + 1);
            //Bottom Right
            if((relevantRow = bombRow + 1) < rows && (relevantColumn = bombColumn + 1) < columns
                    && !cells[relevantRow][relevantColumn].containsBomb())
                cells[relevantRow][relevantColumn].setNumberOfAdjacentMines(cells[relevantRow][relevantColumn].getNumberOfAdjacentMines() + 1);
            //Top Left
            if((relevantRow = bombRow - 1) >= 0 && (relevantColumn = bombColumn - 1) >= 0
                    && !cells[relevantRow][relevantColumn].containsBomb())
                cells[relevantRow][relevantColumn].setNumberOfAdjacentMines(cells[relevantRow][relevantColumn].getNumberOfAdjacentMines() + 1);
            //Bottom Left
            if((relevantRow = bombRow + 1) < rows && (relevantColumn = bombColumn - 1) >= 0
                    && !cells[relevantRow][relevantColumn].containsBomb())
                cells[relevantRow][relevantColumn].setNumberOfAdjacentMines(cells[relevantRow][relevantColumn].getNumberOfAdjacentMines() + 1);
        }
    }

    public boolean lost(){
        return lastClicked == Cell.CellType.BOMB;
    }

    public boolean won(){
        return remainsCells == 0;
    }


//    /**
//     * function disables all cells.
//     */
//    public void lockBoard(){
//        for(int i = 0; i < rows; i++)
//            for(int j = 0; j < columns; j++)
//                cells[i][j].setEnabled(false);
//    }

    public void setBombCellsRevealed(){
        for (Cell c : bombsOnBoard)
            c.setRevealed(true);
    }

    public List<Cell> getBombsOnBoard(){
        return bombsOnBoard;
    }

    public List<Cell> getCellsToReveal(){
        return cellsToReveal;
    }

    public Cell.CellType getLastClikedCell(){
        return lastClicked;
    }

    public void applyMove(int row,int column){
        if((lastClicked = cells[row][column].getCellType()) != Cell.CellType.BOMB) {
            cellsToReveal.clear(); //clear last revealed cells list
            cellToRevealed(row, column);
            remainsCells -= cellsToReveal.size();
        }
    }

    /**
     * Function picks cells to reveal(Marked and empty), If Cell Is empty we keep looking for other cells.
     * @param currentRow
     * @param currentColumn
     */
    private void cellToRevealed(int currentRow,int currentColumn){
        if(currentRow >= 0 && currentRow < rows && currentColumn >= 0 && currentColumn < columns &&
               !cells[currentRow][currentColumn].containsBomb() && !cells[currentRow][currentColumn].isVisited()  ) {
            cells[currentRow][currentColumn].setVisited(true);
            cells[currentRow][currentColumn].setRevealed(true);
            cells[currentRow][currentColumn].setEnabled(false);
            cellsToReveal.add(cells[currentRow][currentColumn]);
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
