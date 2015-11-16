/**
 * This Application was created as part of academic course
 * Tamir Sagi
 */

package com.minesweeper.app;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.os.Handler;
import android.widget.*;
import com.minesweeper.BL.ButtonAdapter;
import com.minesweeper.BL.Cell;
import com.minesweeper.BL.GeneralGameProperties;
import com.minesweeper.BL.MineSweeperLogicManager;


public class GameActivity extends AppCompatActivity {

    private final int mil = 1000, secondsInAMinute = 60, delayed = 500;
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
    private Runnable timerJob;
    private long startedTime;
    private ImageButton bth_Rematch;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("onCreate","onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_board_layout);
        setGameScreen();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("onStop","onStop");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopTimer();
        Log.i("onDestroy", "onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("onPause","onPause");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("onStart","onStart");
    }

    @Override
    protected void onResume() {
        Log.i("onResume","onResume");
        super.onResume();
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
        setTimer();
    }

    /**
     * set general settings
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

        tv_Timer = (TextView) findViewById(R.id.timer);

        bth_Rematch = (ImageButton)findViewById(R.id.BTH_Restart_Game);
        changeRestartButtonState(false);
    }


    private void setMinesCounter() {
        tv_minesCounter.setText("" + minesOnBoard);
    }

    private void setGameLevel() {
        tv_gameLevel.setText(level.toString() + "(" + gameBoardRows + "X" + gameBoardColumns + ")");
    }

    private void setRemainedCells() {
        tv_remainedCell.setText("" + mineSweeperLogicManager.getBoard().getRemainedCells());
    }

    private void setRemainedFlags() {
        tv_RemainedFlags.setText("" + mineSweeperLogicManager.getBoard().getNumberOfFlags());
    }

    public void setTimer() {
        timerHandler = new Handler();
        timerJob = new Runnable() {
            @Override
            public void run() {
                long millis = System.currentTimeMillis() - startedTime;
                int seconds = (int) (millis / mil);             // get seconds from mili seconds
                int minutes = seconds / secondsInAMinute;      //  get minutes from seconds
                seconds = seconds % secondsInAMinute;         //   get updated seconds
                tv_Timer.setText(String.format("%02d:%02d", minutes, seconds));
                timerHandler.postDelayed(this, 0);
            }
        };
    }

    private void startTimer() {
        startedTime = System.currentTimeMillis();
        timerHandler.postDelayed(timerJob, 0);
    }

    private void stopTimer() {
        timerHandler.removeCallbacks(timerJob);
    }

    private void changeRestartButtonState(boolean show){
        if(show)
            bth_Rematch.setVisibility(View.VISIBLE);
        else
            bth_Rematch.setVisibility(View.INVISIBLE);
    }

    /**
     * set text to relevant text views on game window
     */
    private void setGameInfo() {
        setMinesCounter();
        setGameLevel();
        setRemainedCells();
        setRemainedFlags();
    }

    /**
     * set Grid view settings and listeners
     */
    private void setGridView() {
        gv_GameBoard = (GridView) findViewById(R.id.gameBoard);         //get grid view element from layout
        gv_GameBoard.setNumColumns(gameBoardColumns);                  //set grid view column number
        int cellSizeInGrid = setGridItemSize();
        buttonAdapter =
             new ButtonAdapter(this, R.layout.row_grid, mineSweeperLogicManager.getBoard().getGameBoard(),cellSizeInGrid);
        gv_GameBoard.setAdapter(buttonAdapter);
        gv_GameBoard.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
                Log.i("onItemClick", "" + position);
                if (!mineSweeperLogicManager.isGameOver()) {
                    int clickedRow = position % gameBoardRows;
                    int clickedColumn = position / gameBoardRows;
                    Cell clickedCell = mineSweeperLogicManager.getBoard().getGameBoard()[clickedRow][clickedColumn];
                    if (!clickedCell.isFlagged())
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
                if (!mineSweeperLogicManager.isGameOver() && !clickedCell.isRevealed()) {
                    clickedCell.setFlagged(!clickedCell.isFlagged());
                    int remainedFlags = mineSweeperLogicManager.getBoard().getNumberOfFlags();
                    if (clickedCell.isFlagged())
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


    /**
     * Function Measures the corresponded item size within the grid.
     * depends on the screen width and the number of columns in grid
     * @return the absolute size
     */
    private int setGridItemSize(){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;                                           //in px unit
        int columnSizeInPX = width / gameBoardColumns;               //column Size in Px units
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float logicalDensity = metrics.density;
        int columnSizeInDP = columnSizeInPX/ (int)logicalDensity;  //get column size in DP units
        int spaces = 4;                                           //padding between items
        int gridItemSizeInDP = columnSizeInDP - spaces;      //remove 2dp each side
        return gridItemSizeInDP;

    }

//    private void resizeButtonsInGrid(String gameLevel){
//        switch (gameLevel){
//            case GeneralGameProperties.
//        }
//    }


    /**
     * apply move on board
     * @param row
     * @param column
     */
    public void applyMove(int row, int column) {
        if (!mineSweeperLogicManager.isGameStarted())
            startTimer();
        mineSweeperLogicManager.makeMove(row, column);
        buttonAdapter.setGameBoard(mineSweeperLogicManager.getBoard().getGameBoard());
        setRemainedCells();
        if (mineSweeperLogicManager.isGameOver()) {
            stopTimer();
            changeRestartButtonState(true);

        }
    }

    /**
     * rematch the game
     * @param view
     */
    public void rematch(View view) {
        changeRestartButtonState(false);
        mineSweeperLogicManager.rematch();
        buttonAdapter.setGameBoard(mineSweeperLogicManager.getBoard().getGameBoard());
        stopTimer();
        setGameInfo();
        startedTime = 0;
        tv_Timer.setText("00:00");

    }



}
