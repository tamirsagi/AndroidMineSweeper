/**
 * This Application was created as part of academic course
 * Tamir Sagi
 */

package com.minesweeper.BL.GameLogic;


/**
 * This class is responsible to handle current Game
 */
public class MineSweeperLogicManager {

    private Level gameLevel;
    private GameStatus gameStatus; //keep current game status
    private GameResult gameResult;     //When game is over it keeps the result(Win/Lose)
    private Board gameBoard;


    public MineSweeperLogicManager(String level, int rows, int columns, int numberOfMines) {
        initializeBL(level, rows, columns, numberOfMines);
    }

    /**
     * Function prepare Game
     *
     * @param level
     * @param rows
     * @param columns
     * @param numberOfMines
     */
    private void initializeBL(String level, int rows, int columns, int numberOfMines) {
        setGameStatus(GameStatus.NOT_STARTED);
        setGameResult(GameResult.NONE);
        setGameLevel(Level.valueOf(level));
        gameBoard = new Board(rows, columns, numberOfMines);   //create new board
    }

    public enum Level {
        Beginner, Intermediate, Expert
    }

    public enum GameStatus {
        NOT_STARTED, STARTED, OVER
    }

    public enum GameResult {
        NONE, LOST, WON
    }

    ;

    public Board getBoard() {
        return gameBoard;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public Level getGameLevel() {
        return gameLevel;
    }

    public void setGameLevel(Level gameLevel) {
        this.gameLevel = gameLevel;
    }

    public void setGameStatus(GameStatus newStatus) {
        gameStatus = newStatus;
    }

    public void setGameResult(GameResult result) {
        gameResult = result;
    }

    public boolean isGameOver(){
        return gameResult != GameResult.NONE;
    }

    public boolean hasLost(){
        return gameResult == GameResult.LOST;
    }

    public boolean hasWon(){
        return gameResult == GameResult.WON;
    }

    public boolean isGameStarted(){
        return gameStatus ==  GameStatus.STARTED;
    }

    /**
     * Function makes move for current Cell
     *
     * @param row
     * @param column
     */
    public void makeMove(int row, int column) {
        if (getGameStatus() == GameStatus.NOT_STARTED) {
            setGameStatus(GameStatus.STARTED);
            gameBoard.setFirstClickedCell(row, column);
            gameBoard.setBoardAfterFirstClicked();
        }
        gameBoard.applyMove(row, column);
        if (gameBoard.lost()) {
            endGame(GameResult.LOST);
            gameBoard.setBombCellsRevealed();
        } else if (gameBoard.won()) {
            endGame(GameResult.WON);
        }
    }

    /**
     * Function ends the game
     * @param result
     */
    private void endGame(GameResult result) {
        setGameStatus(GameStatus.OVER);
        setGameResult(result);
    }

    /**
     * function prepares the board for a onButtonRematchClicked
     */
    public void rematch() {
        setGameStatus(GameStatus.NOT_STARTED);
        setGameResult(GameResult.NONE);
        int rows = getBoard().getNumberOfRows(), columns = getBoard().getNumberOfColumns(),
                bombs = getBoard().getNumberOfBombs();
        getBoard().initializeGameBoard(rows,columns,bombs);
    }


}
