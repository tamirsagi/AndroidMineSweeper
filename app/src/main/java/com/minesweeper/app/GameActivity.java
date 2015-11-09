/**
 * This Application was created as part of academic course
 * Tamir Sagi
 */

package com.minesweeper.app;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.minesweeper.BL.GeneralGameProperties;
import com.minesweeper.BL.MineSweeperLogicManager;


public class GameActivity extends ActionBarActivity {

    public static final String ID_UPPER_MENU = "UpperGameScreen";
    public static final String ID_GAME_BAORD = "gameBoard";

    private int rows;
    private int columns;
    private int minesOnBoard;
    private String level;
    private String playerName;
    private MineSweeperLogicManager mineSweeperLogicManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setGameScreen();
        setContentView(R.layout.activity_game_board_layout);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public String getPlayerName(){
        return playerName;
    }

    public String getGameLevel(){
        return level;
    }

    public int getRows(){
        return rows;
    }

    public int getColumns(){
        return columns;
    }

    public int getMinesOnBoard(){
        return minesOnBoard;
    }

    public MineSweeperLogicManager getMineSweeperLogicManager(){
        return mineSweeperLogicManager;
    }


    private void setGameScreen(){
        setSettings();

        mineSweeperLogicManager = new MineSweeperLogicManager(level,rows,columns,minesOnBoard);


    }

    /**
     * Function set the game setting
     */

    private void setSettings(){
        Intent received = getIntent();
        Bundle extraData = received.getExtras();
        playerName = extraData.getString(GeneralGameProperties.KEY_PLAYER_FULL_NAME);
        level = extraData.getString(GeneralGameProperties.KEY_GAME_LEVEL);
        rows = extraData.getInt(GeneralGameProperties.KEY_GAME_BOARD_ROWS);
        columns = extraData.getInt(GeneralGameProperties.KEY_GAME_BOARD_COLUMNS);
        minesOnBoard = extraData.getInt(GeneralGameProperties.KEY_GAME_BOARD_MINES);

    }




}
