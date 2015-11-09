/**
 * This Application was created as part of academic course
 * Tamir Sagi
 */

package com.minesweeper.BL;


/**
 * This class is responsible to handle current Game
 */
public class MineSweeperLogicManager {

    private Level gameLevel;
    private Status gameStatus; //keep current game status
    private Result gameResult;     //When game is over it keeps the result(Win/Lose)
    private Board gameBoard;



    public MineSweeperLogicManager(String level,int rows,int columns,int numberOfMines){
        initializeBL(level,rows,columns,numberOfMines);
    }

    /**
     * Function prepare Game
     * @param level
     * @param rows
     * @param columns
     * @param numberOfMines
     */
    private void initializeBL(String level,int rows,int columns,int numberOfMines){
        gameStatus = Status.NOT_STARTED;
        gameResult = Result.NONE;
        gameLevel = Level.valueOf(level);
        gameBoard = new Board(rows,columns,numberOfMines);   //create new board
    }

    public enum Level{
        BEGINNER, INTERMEDIATE,EXPERT
    }

    public enum Status{
        NOT_STARTED, STARTED,PAUSED ,OVER
    }

    public enum Result{
        NONE,LOST , WIN
    };

    public Board getBoard(){
        return gameBoard;
    }

    public Status getGameStatus(){
        return gameStatus;
    }

    public Level getGameLevel(){
        return gameLevel;
    }

    public void setGameLevel(Level gameLevel){
        this.gameLevel = gameLevel;
    }

    public void setGameStatus(Status newStatus){
        gameStatus = newStatus;
    }


    /**
     * Function makes move for current Cell
     * @param row
     * @param column
     */
    public void makeMove(int row,int column){
        if (getGameStatus() == Status.NOT_STARTED) {
            setGameStatus(Status.STARTED);
            gameBoard.setFirstClickedCell(row, column);
            gameBoard.setBoardForNewGame();
        }
        gameBoard.applyMove(row, column);
        if(gameBoard.lost()) {
            endGame(Result.LOST);
            playerLost(gameBoard.getGameBoard()[row][column]);
        }
        else if(gameBoard.won()) {
                endGame(Result.WIN);
                playerWon();
            }
        }

    private void endGame(Result result) {
        gameStatus = Status.OVER;
        gameResult = result;
    }

    private void playerLost(Cell clickedMine){
        gameBoard.setBombCellsRevealed();
        /**
         * Should fire an event including the bomb cells
         */
    }

    private void playerWon(){
        /**
         * Should fire an event including cells to reveal
         */
    }


}
