package com.minesweeper.bl;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class is responsible to handle current Game
 */
public class MineSweeperLogicManager {

    private Level gameLevel;
    private Status gameStatus;
    private Board gameBoard;
    private String gameSetting;



    public MineSweeperLogicManager(String gameSetting){
        gameStatus = Status.NOT_STARTED;
        this.gameSetting = gameSetting;
        startGame();
    }

        public enum Level{
            BEGINNER, INTERMEDIATE,EXPERT
        }

        public enum Status{
            NOT_STARTED, STARTED,PAUSED ,OVER
    }

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
     * Function starts new game
     */
    public void startGame(){
        try {
            JSONObject settings = new JSONObject(gameSetting);
            String level = settings.getString(GeneralGameProperties.Level);     //get game level from json
            gameLevel = Level.valueOf(level);                                   // set game level
            int rows = settings.getInt(GeneralGameProperties.RowsInBoard);      //rows in board
            int columns = settings.getInt(GeneralGameProperties.RowsInBoard);   //columns in board
            int mines = settings.getInt(GeneralGameProperties.MineOnBoard);     //mines on board
            gameBoard = new Board(rows,columns,mines);                          //create new board
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void makeMove(int row,int column){
        if(getGameStatus() == Status.NOT_STARTED)
            setGameStatus(Status.STARTED);
            gameBoard.setBoardForNewGame();
    }

    public void endGame(){
        gameStatus = Status.OVER;
    }


}
