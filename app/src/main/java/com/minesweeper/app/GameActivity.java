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
import android.widget.TextView;
import com.minesweeper.BL.ButtonAdapter;
import com.minesweeper.BL.Cell;
import com.minesweeper.BL.GeneralGameProperties;
import com.minesweeper.BL.MineSweeperLogicManager;

import java.util.logging.Handler;


public class GameActivity extends AppCompatActivity {

    private int gameBoardRows;
    private int gameBoardColumns;
    private int minesOnBoard;
    private String level;
    private String playerName;
    private MineSweeperLogicManager mineSweeperLogicManager;
    private GridView gv_GameBoard;
    private ButtonAdapter buttonAdapter;
    private TextView tv_minesCounter;
    private TextView tv_remainedCell;
    private TextView tv_gameLevel;
    private TextView tv_RemainedFlags;
    private Handler timerHandler;
    private TextView tv_Timer;


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
        setGameInfo();
        setGridView();
    }

    /**
     * Functions set the game setting
     */

    private void setSettings() {
        Intent received = getIntent();
        Bundle extraData = received.getExtras();
        playerName = extraData.getString(GeneralGameProperties.KEY_PLAYER_FULL_NAME);
        level = extraData.getString(GeneralGameProperties.KEY_GAME_LEVEL);
        gameBoardRows = extraData.getInt(GeneralGameProperties.KEY_GAME_BOARD_ROWS);
        gameBoardColumns = extraData.getInt(GeneralGameProperties.KEY_GAME_BOARD_COLUMNS);
        minesOnBoard = extraData.getInt(GeneralGameProperties.KEY_GAME_BOARD_MINES);

        tv_minesCounter = (TextView) findViewById(R.id.minesCounter);
        tv_gameLevel = (TextView) findViewById(R.id.gameLevel);
        tv_remainedCell = (TextView) findViewById(R.id.remainedCells);
        tv_RemainedFlags = (TextView) findViewById(R.id.remainedFlags);
    }


    private void setMinesCounter() {
        tv_minesCounter.setText("" + minesOnBoard);
    }

    private void setGameLevel() {
        tv_gameLevel.setText(level.toString() + "(" + gameBoardRows + "X" + gameBoardColumns + ")");
    }

    private void setRemainedCells() {
        tv_remainedCell.setText("" + mineSweeperLogicManager.getBoard().getRemainsCells());
    }

    private void setRemainedFlags() {
        tv_RemainedFlags.setText("" + mineSweeperLogicManager.getBoard().getNumberOfFlags());
    }


    private void setGameInfo() {
        setMinesCounter();
        setGameLevel();
        setRemainedCells();
        setRemainedFlags();
    }

    private void setGridView() {
        gv_GameBoard = (GridView) findViewById(R.id.gameBoard);
        gv_GameBoard.setNumColumns(gameBoardColumns);
        buttonAdapter =
                new ButtonAdapter(this, R.layout.row_grid, mineSweeperLogicManager.getBoard().getGameBoard());
        gv_GameBoard.setAdapter(buttonAdapter);
        gv_GameBoard.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
                Log.i("onItemClick", "" + position);
                if (mineSweeperLogicManager.getGameStatus() != MineSweeperLogicManager.GameStatus.OVER ) {
                    int clickedRow = position % gameBoardRows;
                    int clickedColumn = position / gameBoardRows;
                    Cell clickedCell = mineSweeperLogicManager.getBoard().getGameBoard()[clickedRow][clickedColumn];
                    if(!clickedCell.isFlagged())
                        applyMove(clickedRow, clickedColumn);
                }
            }
        });
        gv_GameBoard.setOnItemLongClickListener((new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                int clickedRow = position % gameBoardRows;
                int clickedColumn = position / gameBoardRows;
                Cell clickedCell = mineSweeperLogicManager.getBoard().getGameBoard()[clickedRow][clickedColumn];
                if (!mineSweeperLogicManager.isGameOver()  &&
                        !clickedCell.isRevealed()) {
                    clickedCell.setFlagged(!clickedCell.isFlagged());
                    int remainedFlags = mineSweeperLogicManager.getBoard().getNumberOfFlags();
                    if(clickedCell.isFlagged())
                        remainedFlags--;
                    else
                        remainedFlags++;
                    mineSweeperLogicManager.getBoard().setNumberOfFlags(remainedFlags);
                    setRemainedFlags();
                    buttonAdapter.setGameBoard(mineSweeperLogicManager.getBoard().getGameBoard());
                    return true;
                }
                return false;
            }
        }));
    }


    public void applyMove(int row, int column) {
        Log.i("game activity", "applyMove clicked" + row + "," + column);
        mineSweeperLogicManager.makeMove(row, column);
        buttonAdapter.setGameBoard(mineSweeperLogicManager.getBoard().getGameBoard());
        setRemainedCells();
    }

    public void rematch(View view) {
        mineSweeperLogicManager.rematch();
        buttonAdapter.setGameBoard(mineSweeperLogicManager.getBoard().getGameBoard());
        setGameInfo();
    }


}
