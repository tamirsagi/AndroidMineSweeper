


package com.minesweeper.BL.GameLogic;


import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * @author  Tamir Sagi
 * This class represents a game board
 */
public class Board {

    public final double ratioFlagsAndBombs = 0.8;           // number of flags are 80% of the bombs on board

    private Cell[][] cells; //game board
    private int rows;
    private int columns;
    private int numberOfBombs;
    private int numberOfFlags;
    private List<Cell> bombsOnBoard; //bomb on board
    private int remainedCells;
    private Cell lastClicked;

    public Board(int rows, int columns, int numberOfBombs) {
        initializeGameBoard(rows, columns, numberOfBombs);
    }

    public void initializeGameBoard(int rows, int columns, int numberOfBombs) {
        this.rows = rows;
        this.columns = columns;
        setNumberOfBombs(numberOfBombs);
        setRemainedCells(getBoardSize() - getNumberOfBombs());    //remained cells to be revealed
        numberOfFlags = (int) (ratioFlagsAndBombs * getNumberOfBombs());
        cells = new Cell[rows][columns];
        fillUpEmptyCells();

    }

    public void setFirstClickedCell(int row, int column) {
        cells[row][column].setCellType(Cell.CellType.EMPTY_FIRST_CLICKED);
    }

    public Cell[][] getGameBoard() {
        return cells;
    }

    public int getNumberOfRows() {
        return rows;
    }

    public int getNumberOfColumns() {
        return columns;
    }

    public int getNumberOfBombs() {
        return numberOfBombs;
    }

    public void setNumberOfBombs(int numberOfBombs) {
        this.numberOfBombs = numberOfBombs;
    }

    public int getBoardSize() {
        return getNumberOfRows() * getNumberOfColumns();
    }

    /**
     * Function set the board
     */
    public void setBoardAfterFirstClicked() {
        fillUpBombs();
        setNumberOfAdjacentMines();
    }

    /**
     * Function fills up board with bombs cells in random indexes
     */
    private void fillUpBombs() {
        bombsOnBoard = new LinkedList<Cell>();
        Random rand = new Random();
        int placedBombs = 0;
        while (placedBombs < numberOfBombs) {
            int row = rand.nextInt(rows);
            int col = rand.nextInt(columns);
            if (canPlaceBomb(row, col)) {
                Cell bomb = new BombCell(row, col);
                cells[row][col] = bomb;
                bombsOnBoard.add(bomb);
                placedBombs++;
            }
        }
    }

    /**
     * @return true if a mine can be placed within a current cell or false otherwise
     */
    private boolean canPlaceBomb(int row, int column) {
        return cells[row][column].getCellType() == Cell.CellType.EMPTY;
    }

    /**
     * Function fills up board with empty cells
     */
    private void fillUpEmptyCells() {
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < columns; j++)
                cells[i][j] = new Cell(Cell.CellType.EMPTY, i, j);
    }

    /**
     * Function calculates the number Of adjacent mines for relevant cells only
     */
    private void setNumberOfAdjacentMines() {
        for (int i = 0; i < bombsOnBoard.size(); i++) {
            Cell bomb = bombsOnBoard.get(i);
            setNumberOfAdjacentMinesForCell(bomb);
        }
    }

    /**
     * Function calculates the number Of adjacent per cell
     */
    private void setNumberOfAdjacentMinesForCell(Cell bomb) {

        int bombRow = bomb.getRowNumber(), bombColumn = bomb.getColumnNumber(), relevantRow, relevantColumn; // keep Adjacent Cell index
        //One Cell Up
        if ((relevantRow = bombRow - 1) >= 0 && !cells[relevantRow][bombColumn].isBomb())
            cells[relevantRow][bombColumn].setNumberOfAdjacentMines(cells[relevantRow][bombColumn].getNumberOfAdjacentMines() + 1);
        //One Cell Bottom
        if ((relevantRow = bombRow + 1) < rows && !cells[relevantRow][bombColumn].isBomb())
            cells[relevantRow][bombColumn].setNumberOfAdjacentMines(cells[relevantRow][bombColumn].getNumberOfAdjacentMines() + 1);
        //Left Cell
        if ((relevantColumn = bombColumn - 1) >= 0 && !cells[bombRow][relevantColumn].isBomb())
            cells[bombRow][relevantColumn].setNumberOfAdjacentMines(cells[bombRow][relevantColumn].getNumberOfAdjacentMines() + 1);
        //Right Cell
        if ((relevantColumn = bombColumn + 1) < columns && !cells[bombRow][relevantColumn].isBomb())
            cells[bombRow][relevantColumn].setNumberOfAdjacentMines(cells[bombRow][relevantColumn].getNumberOfAdjacentMines() + 1);
        //Diagonal Top Right
        if ((relevantRow = bombRow - 1) >= 0 && (relevantColumn = bombColumn + 1) < columns
                && !cells[relevantRow][relevantColumn].isBomb())
            cells[relevantRow][relevantColumn].setNumberOfAdjacentMines(cells[relevantRow][relevantColumn].getNumberOfAdjacentMines() + 1);
        //Diagonal Bottom Right
        if ((relevantRow = bombRow + 1) < rows && (relevantColumn = bombColumn + 1) < columns
                && !cells[relevantRow][relevantColumn].isBomb())
            cells[relevantRow][relevantColumn].setNumberOfAdjacentMines(cells[relevantRow][relevantColumn].getNumberOfAdjacentMines() + 1);
        //Diagonal Top Left
        if ((relevantRow = bombRow - 1) >= 0 && (relevantColumn = bombColumn - 1) >= 0
                && !cells[relevantRow][relevantColumn].isBomb())
            cells[relevantRow][relevantColumn].setNumberOfAdjacentMines(cells[relevantRow][relevantColumn].getNumberOfAdjacentMines() + 1);
        //Diagonal Bottom Left
        if ((relevantRow = bombRow + 1) < rows && (relevantColumn = bombColumn - 1) >= 0
                && !cells[relevantRow][relevantColumn].isBomb())
            cells[relevantRow][relevantColumn].setNumberOfAdjacentMines(cells[relevantRow][relevantColumn].getNumberOfAdjacentMines() + 1);
    }

    public boolean lost() {
        return getLastClikedCell().getCellType() == Cell.CellType.BOMB;
    }

    public boolean won() {
        return remainedCells == 0;
    }

    public void setBombCellsRevealed() {
        for (Cell c : bombsOnBoard)
            c.setRevealed(true);
    }

    public List<Cell> getBombsOnBoard() {
        return bombsOnBoard;
    }

    private Cell getLastClikedCell() {
        return lastClicked;
    }

    public int getRemainedCells() {
        return remainedCells;
    }

    public void setRemainedCells(int remainedCells) {
        this.remainedCells = remainedCells;
    }

    public int getNumberOfFlags() {
        return numberOfFlags;
    }

    public void setNumberOfFlags(int val) {
        numberOfFlags = val;
    }

    public void applyMove(int row, int column) {
        if ((lastClicked = cells[row][column]).getCellType() != Cell.CellType.BOMB) {
            cellsToReveal(row, column);
        }
        lastClicked.setClicked(true);
    }

    /**
     * Function picks cells to reveal(Marked and empty), If Cell Is empty we keep looking for other cells.
     *
     * @param currentRow
     * @param currentColumn
     */
    private void cellsToReveal(int currentRow, int currentColumn) {
        if (canVisitCell(currentRow, currentColumn)) {
            cells[currentRow][currentColumn].setVisited(true);
            cells[currentRow][currentColumn].setRevealed(true);
            setRemainedCells(getRemainedCells() - 1);
            //if cell is empty check its adjacent cells
            if (cells[currentRow][currentColumn].isEmpty()) {
                //Top Cell
                cellsToReveal(currentRow - 1, currentColumn);
                //Bottom Cell
                cellsToReveal(currentRow + 1, currentColumn);
                //left Cell
                cellsToReveal(currentRow, currentColumn - 1);
                //right Cell
                cellsToReveal(currentRow, currentColumn + 1);
                //Top right Cell
                cellsToReveal(currentRow - 1, currentColumn + 1);
                //Bottom right Cell
                cellsToReveal(currentRow + 1, currentColumn + 1);
                //Top Left Cell
                cellsToReveal(currentRow - 1, currentColumn - 1);
                //Bottom Left Cell
                cellsToReveal(currentRow + 1, currentColumn - 1);
            }
        }
    }

    /**
     * function checks whether the cell can ve visited when looking for cells to open
     *
     * @param currentRow
     * @param currentColumn
     * @return
     */
    private boolean canVisitCell(int currentRow, int currentColumn) {
        return currentRow >= 0 && currentRow < rows && currentColumn >= 0 &&
                currentColumn < columns && !cells[currentRow][currentColumn].isBomb() &&
                !cells[currentRow][currentColumn].isVisited() && !cells[currentRow][currentColumn].isFlagged();
    }


    /**
     * change board tiles correspondingly to a new bomb cell
     *
     * @param row
     * @param col
     */
    public void handleNewBombOnBoard(int row, int col) {
        //if the chosen cell is revealed, its replaced with a bomb so remained cell is reduced by one
        if(getGameBoard()[row][col].isRevealed())
            setRemainedCells(getRemainedCells() + 1);
        //set new cell for chosen position
        getGameBoard()[row][col] = new BombCell(row, col);
        Cell newBomb = getGameBoard()[row][col];
        //add it to bombs list
        bombsOnBoard.add(newBomb);
        //close adjacent cells around the new bomb cell
        closeAdjacentCells(newBomb);
        //re-evaluate each adjacent cell correspondingly the new bomb cell.
        setNumberOfAdjacentMinesForCell(newBomb);
        setNumberOfBombs(getNumberOfBombs() + 1);
        setRemainedCells(getRemainedCells() - 1);
        numberOfFlags = (int) (ratioFlagsAndBombs * getNumberOfBombs());
    }


    /**
     * Function closes adjacent tiles around a bomb
     */
    private void closeAdjacentCells(Cell bomb) {
        System.out.println("before closeAdjacentCells : " + getRemainedCells());
        int bombRow = bomb.getRowNumber(), bombColumn = bomb.getColumnNumber(), relevantRow, relevantColumn; // keep Adjacent Cell index
        //One Cell Up
        if ((relevantRow = bombRow - 1) >= 0 && cells[relevantRow][bombColumn].isRevealed()) {
            changeCellStates(relevantRow, bombColumn, false);
            setRemainedCells(getRemainedCells() + 1);
        }
        //One Cell Bottom
        if ((relevantRow = bombRow + 1) < rows && cells[relevantRow][bombColumn].isRevealed()) {
            changeCellStates(relevantRow, bombColumn, false);
            setRemainedCells(getRemainedCells() + 1);
        }
        //Left Cell
        if ((relevantColumn = bombColumn - 1) >= 0 && cells[bombRow][relevantColumn].isRevealed()) {
            changeCellStates(bombRow, relevantColumn, false);
            setRemainedCells(getRemainedCells() + 1);
        }
        //Right Cell
        if ((relevantColumn = bombColumn + 1) < columns && cells[bombRow][relevantColumn].isRevealed()) {
            changeCellStates(bombRow, relevantColumn, false);
            setRemainedCells(getRemainedCells() + 1);
        }
        //Diagonal Top Right
        if ((relevantRow = bombRow - 1) >= 0 && (relevantColumn = bombColumn + 1) < columns
                && cells[relevantRow][relevantColumn].isRevealed()) {
            changeCellStates(relevantRow, relevantColumn, false);
            setRemainedCells(getRemainedCells() + 1);
        }
        //Diagonal Bottom Right
        if ((relevantRow = bombRow + 1) < rows && (relevantColumn = bombColumn + 1) < columns
                && cells[relevantRow][relevantColumn].isRevealed()) {
            changeCellStates(relevantRow, relevantColumn, false);
            setRemainedCells(getRemainedCells() + 1);
        }
        //Diagonal Top Left
        if ((relevantRow = bombRow - 1) >= 0 && (relevantColumn = bombColumn - 1) >= 0
                && cells[relevantRow][relevantColumn].isRevealed()) {
            changeCellStates(relevantRow, relevantColumn, false);
            setRemainedCells(getRemainedCells() + 1);
        }
        //Diagonal Bottom Left
        if ((relevantRow = bombRow + 1) < rows && (relevantColumn = bombColumn - 1) >= 0
                && cells[relevantRow][relevantColumn].isRevealed()) {
            changeCellStates(relevantRow, relevantColumn, false);
            setRemainedCells(getRemainedCells() + 1);
        }
        System.out.println("after closeAdjacentCells : " + getRemainedCells());
    }

    private void changeCellStates(int row, int columns, boolean state) {
        cells[row][columns].setRevealed(state);
        cells[row][columns].setFlagged(state);
        cells[row][columns].setVisited(state);
    }

}
