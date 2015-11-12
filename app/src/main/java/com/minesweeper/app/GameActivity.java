/**
 * This Application was created as part of academic course
 * Tamir Sagi
 */

package com.minesweeper.app;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import com.minesweeper.BL.GeneralGameProperties;
import com.minesweeper.BL.MineSweeperLogicManager;


public class GameActivity extends ActionBarActivity {

    private int gameBoardRows;
    private int gameBoardColumns;
    private int minesOnBoard;
    private String level;
    private String playerName;
    private MineSweeperLogicManager mineSweeperLogicManager;
    private TableLayout gameBoard;


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

    public int getGameBoardRows(){
        return gameBoardRows;
    }

    public int getGameBoardColumns(){
        return gameBoardColumns;
    }

    public int getMinesOnBoard(){
        return minesOnBoard;
    }

    public MineSweeperLogicManager getMineSweeperLogicManager(){
        return mineSweeperLogicManager;
    }


    private void setGameScreen(){
        setSettings();
        mineSweeperLogicManager = new MineSweeperLogicManager(level, gameBoardRows, gameBoardColumns,minesOnBoard);
        createGameBoard();


    }

    /**
     * Function set the game setting
     */

    private void setSettings(){
        Intent received = getIntent();
        Bundle extraData = received.getExtras();
        playerName = extraData.getString(GeneralGameProperties.KEY_PLAYER_FULL_NAME);
        level = extraData.getString(GeneralGameProperties.KEY_GAME_LEVEL);
        gameBoardRows = extraData.getInt(GeneralGameProperties.KEY_GAME_BOARD_ROWS);
        gameBoardColumns = extraData.getInt(GeneralGameProperties.KEY_GAME_BOARD_COLUMNS);
        minesOnBoard = extraData.getInt(GeneralGameProperties.KEY_GAME_BOARD_MINES);

    }

    private void createGameBoard()
    {
        gameBoard = (TableLayout)findViewById(R.id.gameBoard);
        final float scale = ((View)getWindow().getDecorView().findViewById(android.R.id.content)).getContext()
                .getResources().getDisplayMetrics().density;
        for (int row = 0; row < gameBoardRows; row++) {
            TableRow tableRow = new TableRow(this);
            for (int column = 0; column < gameBoardColumns; column++) {
                final ImageButton button = new ImageButton(this);
                int bthID = (row + 1) * (column + 1);
                button.setId(bthID);
                button.setTag("" + row + "," + column +"");   // row,col, it's the tag which indicates the position
                button.setBackgroundResource(R.drawable.emptyCell);
                button.setEnabled(false);
                button.setMaxWidth((int) (50 * scale));
                button.setMaxHeight((int) (50 * scale));
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String[] position = ((String)button.getTag()).split(",");
                        int row = Integer.parseInt(position[0]);
                        int col = Integer.parseInt(position[0]);
                        applyMove(row,col);
                    }
                });
                tableRow.addView(button);

            }
            gameBoard.addView(tableRow);
        }
    }


    public void applyMove(int row,int column){
        Log.i("game activity","applyMove clicked");
    }



}
