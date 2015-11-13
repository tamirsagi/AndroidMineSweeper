/**
 * This Application was created as part of academic course
 * Tamir Sagi
 */

package com.minesweeper.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import com.minesweeper.BL.ButtonAdapter;
import com.minesweeper.BL.GeneralGameProperties;
import com.minesweeper.BL.MineSweeperLogicManager;


public class GameActivity extends AppCompatActivity {

    private int gameBoardRows;
    private int gameBoardColumns;
    private int minesOnBoard;
    private String level;
    private String playerName;
    private MineSweeperLogicManager mineSweeperLogicManager;
    private GridView gridView;
    private ButtonAdapter buttonAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_board_layout);
        setGameScreen();

    }

    public String getPlayerName() {
        return playerName;
    }

    public String getGameLevel() {
        return level;
    }

    public int getGameBoardRows() {
        return gameBoardRows;
    }

    public int getGameBoardColumns() {
        return gameBoardColumns;
    }

    public int getMinesOnBoard() {
        return minesOnBoard;
    }

    public MineSweeperLogicManager getMineSweeperLogicManager() {
        return mineSweeperLogicManager;
    }


    private void setGameScreen() {
        setSettings();
        mineSweeperLogicManager = new MineSweeperLogicManager(level, gameBoardRows, gameBoardColumns, minesOnBoard);
        setGridView();
    }

    /**
     * Function set the game setting
     */

    private void setSettings() {
        Intent received = getIntent();
        Bundle extraData = received.getExtras();
        playerName = extraData.getString(GeneralGameProperties.KEY_PLAYER_FULL_NAME);
        level = extraData.getString(GeneralGameProperties.KEY_GAME_LEVEL);
        gameBoardRows = extraData.getInt(GeneralGameProperties.KEY_GAME_BOARD_ROWS);
        gameBoardColumns = extraData.getInt(GeneralGameProperties.KEY_GAME_BOARD_COLUMNS);
        minesOnBoard = extraData.getInt(GeneralGameProperties.KEY_GAME_BOARD_MINES);
    }

    private void setGridView() {
        gridView = (GridView) findViewById(R.id.gameBoard);
        gridView.setNumColumns(gameBoardColumns);
        buttonAdapter =
                new ButtonAdapter(this, R.layout.row_grid, mineSweeperLogicManager.getBoard().getGameBoard());
        gridView.setAdapter(buttonAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
                Log.i("onItemClick", "" + position);
                int clickedRow = position % gameBoardRows;
                int clickedColumn = position / gameBoardRows;
                applyMove(clickedRow, clickedColumn);
            }
        });
    }


    public void applyMove(int row, int column) {
        Log.i("game activity", "applyMove clicked" + row + "," + column);
        mineSweeperLogicManager.makeMove(row,column);
        buttonAdapter.setGameBoard(mineSweeperLogicManager.getBoard().getGameBoard());
    }

    public void rematch(View view){
        mineSweeperLogicManager.rematch();
        buttonAdapter.setGameBoard(mineSweeperLogicManager.getBoard().getGameBoard());
    }


}
