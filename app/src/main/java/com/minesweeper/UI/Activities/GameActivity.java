/**
 * @author Tamir Sagi
 * This Application was created as part of academic course
 */

package com.minesweeper.UI.Activities;

import android.content.*;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.os.Handler;
import android.view.animation.*;
import android.webkit.WebView;
import android.widget.*;
import com.minesweeper.BL.DB.DbManager;
import com.minesweeper.BL.GameLogic.ButtonAdapter;
import com.minesweeper.BL.GameLogic.Cell;
import com.minesweeper.BL.GameLogic.GeneralGameProperties;
import com.minesweeper.BL.GameLogic.MineSweeperLogicManager;
import com.minesweeper.UI.Animation.TileAnimation;
import com.minesweeper.UI.Fragments.DetailsDialog;

import com.minesweeper.BL.Services.*;

import java.util.HashMap;

public class GameActivity extends AppCompatActivity {
    public static final String TAG = "GameActivity";

    public static final String KEY_TABLE = "TABLE";
    public static final String KEY_ROUND_TIME = "ROUND_TIME";
    public static final String KEY_LOCATION_CITY = "CITY";
    public static final String KEY_LOCATION_COUNTRY = "COUNTRY";
    public static final String KEY_LOCATION_LATITUDE = "LATITUDE";
    public static final String KEY_LOCATION_LONGITUDE = "LONGITUDE";

    public static final String KEY_DATE = "DATE";


    private final int mil = 1000, mSecondsInAMinute = 60;
    private int mGameBoardRows;
    private int mGameBoardColumns;
    private int mMinesOnBoard;
    private String mLevel;
    private String mPlayerName;
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
    private MediaPlayer mp;
    private boolean playSound;
    private boolean playAnimation;
    private WebView mWebViewWinning;
    private WebView mWebViewLosing;
    private boolean isAnimationShown;


    //Service Params
    private PositionSampleService mPositionSampleService;
    private GPSTracker mGpsTrackerService;
    private boolean isBound = false;

    private TextView tv_InitialAccelerometer;
    private TextView tv_CurrentAccelerometer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("onCreate", "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_board_layout);
        setGameScreen();
        createBinnedGPSService();
        createBinnedPositionService();
    }

    @Override
    protected void onStop() {
        Log.i("onStop", "onStop");

        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("onDestroy", "onDestroy");
        stopTimer();
        if (mPositionSampleService != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageFromPositionService);
            unbindService(positionSampleConnection);
        }
        if (mGpsTrackerService != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageFromGPSService);
            unbindService(GPSTrackerServiceConnection);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPositionSampleService.unregisterListener();
        mGpsTrackerService.stopUsingGPS();
        Log.i("onPause", "onPause");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("onStart", "onStart");
    }

    @Override
    protected void onResume() {
        Log.i("onResume", "onResume");
        super.onResume();
        if(mPositionSampleService != null)
             mPositionSampleService.registerListener();
        if(mGpsTrackerService != null)
            mGpsTrackerService.startUsingGPS();
    }


    public String getPlayerName() {
        return mPlayerName;
    }

    public String getGameLevel() {
        return mLevel;
    }

    public int getGameBoardRows() {
        return mGameBoardRows;
    }

    public int getGameBoardColumns() {
        return mGameBoardColumns;
    }

    public int getMinesOnBoard() {
        return mMinesOnBoard;
    }

    public MineSweeperLogicManager getMineSweeperLogicManager() {
        return mineSweeperLogicManager;
    }


    private void setGameScreen() {
        setSettings();
        mineSweeperLogicManager = new MineSweeperLogicManager(mLevel, mGameBoardRows, mGameBoardColumns, mMinesOnBoard);
        setGameInfo();
        setGridView();
        setTimer();
        setGifWebView();
        setPositionsTextViews();

    }

    /**
     * set general settings
     */
    private void setSettings() {
        Intent received = getIntent();
        Bundle extraData = received.getExtras();
        mPlayerName = extraData.getString(GeneralGameProperties.KEY_PLAYER_FULL_NAME);
        mLevel = extraData.getString(GeneralGameProperties.KEY_GAME_LEVEL);
        mGameBoardRows = extraData.getInt(GeneralGameProperties.KEY_GAME_BOARD_ROWS);
        mGameBoardColumns = extraData.getInt(GeneralGameProperties.KEY_GAME_BOARD_COLUMNS);
        mMinesOnBoard = extraData.getInt(GeneralGameProperties.KEY_GAME_BOARD_MINES);
        playSound = extraData.getBoolean(GeneralGameProperties.KEY_Play_Sound);
        playAnimation = extraData.getBoolean(GeneralGameProperties.KEY_Play_Animation);

        tv_minesCounter = (TextView) findViewById(R.id.minesCounter);
        tv_gameLevel = (TextView) findViewById(R.id.gameLevel);
        tv_remainedCell = (TextView) findViewById(R.id.remainedCells);
        tv_RemainedFlags = (TextView) findViewById(R.id.remainedFlags);

        tv_Timer = (TextView) findViewById(R.id.timer);

        bth_Rematch = (ImageButton) findViewById(R.id.BTH_Restart_Game);
        changeRestartButtonState(false);

        mp = new MediaPlayer();
    }


    private void setMinesCounter() {
        mMinesOnBoard = mineSweeperLogicManager.getNumberOfBombs();
        tv_minesCounter.setText("" + mMinesOnBoard);
    }

    private void setGameLevel() {
        mGameBoardRows = mineSweeperLogicManager.getBoard().getNumberOfRows();
        mGameBoardColumns = mineSweeperLogicManager.getBoard().getNumberOfColumns();
        tv_gameLevel.setText(mLevel.toString() + "(" + mGameBoardRows + "X" + mGameBoardColumns + ")");
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
                int minutes = seconds / mSecondsInAMinute;      //  get minutes from seconds
                seconds = seconds % mSecondsInAMinute;         //   get updated seconds
                tv_Timer.setText(String.format("%02d:%02d", minutes, seconds));
                timerHandler.postDelayed(this, 0);
            }
        };
    }

    private void startTimer() {
        startedTime = System.currentTimeMillis();
        timerHandler.postDelayed(timerJob, 0);
        mPositionSampleService.startUpdatingCurrentAngle();
    }

    private void stopTimer() {
        timerHandler.removeCallbacks(timerJob);
        mPositionSampleService.stopUpdatingCurrentAngle();
    }

    private void changeRestartButtonState(boolean show) {
        if (show)
            bth_Rematch.setVisibility(View.VISIBLE);
        else
            bth_Rematch.setVisibility(View.INVISIBLE);
    }

    /**
     * set text to relevant text views on game window
     */
    private void setGameInfo() {
        setGameLevel();
        setRemainedCells();
        setMinesCounter();
        setRemainedFlags();

    }

    /**
     * set Grid view settings and listeners
     */
    private void setGridView() {
        gv_GameBoard = (GridView) findViewById(R.id.gameBoard);         //get grid view element from layout
        gv_GameBoard.setNumColumns(mGameBoardColumns);                  //set grid view column number
        int cellSizeInGrid = setGridItemSize();
        buttonAdapter =
                new ButtonAdapter(this, R.layout.row_grid, mineSweeperLogicManager.getBoard().getGameBoard(), cellSizeInGrid);
        gv_GameBoard.setAdapter(buttonAdapter);
        gv_GameBoard.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
                Log.i("onItemClick", "" + position);
                if (!mineSweeperLogicManager.isGameOver()) {
                    int clickedRow = position % mGameBoardRows;
                    int clickedColumn = position / mGameBoardRows;
                    Cell clickedCell = mineSweeperLogicManager.getBoard().getGameBoard()[clickedRow][clickedColumn];
                    if (!clickedCell.isFlagged())
                        applyMove(clickedRow, clickedColumn);

                }
            }
        });
        gv_GameBoard.setOnItemLongClickListener((new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                int clickedRow = position % mGameBoardRows;
                int clickedColumn = position / mGameBoardRows;
                Cell clickedCell = mineSweeperLogicManager.getBoard().getGameBoard()[clickedRow][clickedColumn];
                if (mineSweeperLogicManager.isGameStarted() && !clickedCell.isRevealed()) {
                    int remainedFlags = mineSweeperLogicManager.getBoard().getNumberOfFlags();
                    if (clickedCell.isFlagged()) {       //press on a cell where there are no available flags
                        clickedCell.setFlagged(false);
                        remainedFlags++;
                    } else if (remainedFlags > 0) {
                        clickedCell.setFlagged(true);
                        remainedFlags--;
                    }
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
     *
     * @return the absolute size
     */
    private int setGridItemSize() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;                                           //in px unit
        int columnSizeInPX = width / mGameBoardColumns;               //column Size in Px units
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float logicalDensity = metrics.density;
        int columnSizeInDP = columnSizeInPX / (int) logicalDensity;  //get column size in DP units
        int spaces = gv_GameBoard.getHorizontalSpacing();         //padding between items
        int gridItemSizeInDP = columnSizeInDP - spaces;         //remove 2dp each side
        return gridItemSizeInDP;

    }

    /**
     * apply move on board
     *
     * @param row
     * @param column
     */
    public void applyMove(int row, int column) {
        boolean won;
        if (!mineSweeperLogicManager.isGameStarted())
            startTimer();
        mineSweeperLogicManager.makeMove(row, column);
        buttonAdapter.setGameBoard(mineSweeperLogicManager.getBoard().getGameBoard());
        setRemainedCells();
        if (mineSweeperLogicManager.isGameOver()) {
            buttonAdapter.playAnimation();
            stopTimer();
            if (mineSweeperLogicManager.hasLost()) {
                won = false;
                mp = MediaPlayer.create(this, R.raw.granade);
            } else {
                won = true;
                mp = MediaPlayer.create(this, R.raw.victory);
                String tableInDB = getDbTableFromGameLevel(getGameLevel());
                String time = tv_Timer.getText().toString();
                if (DbManager.getInstance(this).shouldBeInserted(tableInDB, time)) {
                    showDialog(tableInDB);
                }
            }
            if (playAnimation)
                playAnimation(won);
            if (playSound)
                mp.start();
            changeRestartButtonState(true);
        }
    }

    /**
     * pops up the insertion dialog and pass relevant data
     *
     * @param tableInDB
     */
    private void showDialog(String tableInDB) {
        Bundle details = new Bundle();
        details.putString(KEY_ROUND_TIME, tv_Timer.getText().toString());
        if (mGpsTrackerService.getLastLocation() != null) {
            HashMap<String, String> locationValues = mGpsTrackerService.getLocationValues();
            details.putString(KEY_LOCATION_CITY, locationValues.get(KEY_LOCATION_CITY));
            details.putString(KEY_LOCATION_COUNTRY, locationValues.get(KEY_LOCATION_COUNTRY));
            details.putString(KEY_LOCATION_LATITUDE, locationValues.get(KEY_LOCATION_LATITUDE));
            details.putString(KEY_LOCATION_LONGITUDE, locationValues.get(KEY_LOCATION_LONGITUDE));
        } else {
            details.putString(KEY_LOCATION_CITY, "");
            details.putString(KEY_LOCATION_COUNTRY, "");
            details.putString(KEY_LOCATION_LATITUDE, "");
            details.putString(KEY_LOCATION_LONGITUDE, "");
        }
        details.putString(KEY_DATE, DbManager.getDate());
        details.putString(KEY_TABLE, tableInDB);
        DetailsDialog.showDialog(getFragmentManager(), details);
    }

    /**
     * Rematch the game
     *
     * @param view
     */
    public void onButtonRematchClicked(View view) {
        if (isAnimationShown)
            stopWebView();
        changeRestartButtonState(false);
        mp.release();
        mineSweeperLogicManager.rematch();
        gv_GameBoard.removeAllViewsInLayout();
        buttonAdapter.setGameBoard(mineSweeperLogicManager.getBoard().getGameBoard());
        gv_GameBoard.setAdapter(buttonAdapter);
        setGameInfo();
        startedTime = 0;
        tv_Timer.setText("00:00");
        tv_CurrentAccelerometer.setText(R.string.current_angle_text);
        mPositionSampleService.setGetUpdatedInitialAngle(true);
    }

    /**
     * @param level
     * @return corresponded db table by game mLevel
     */
    private String getDbTableFromGameLevel(String level) {
        switch (MineSweeperLogicManager.Level.valueOf(level)) {
            case Beginner:
                return DbManager.Tables.PLAYERS_RECORDS_BEGINNERS.toString();
            case Intermediate:
                return DbManager.Tables.PLAYERS_RECORDS_INTERMEDIATE.toString();
            case Expert:
                return DbManager.Tables.PLAYERS_RECORDS_EXPERT.toString();
            default:
                return "";
        }
    }


    /**
     * Sampling Phone Position Service Section
     */

    private void createBinnedPositionService() {
        Intent intent = new Intent(this, PositionSampleService.class);
        bindService(intent, positionSampleConnection, Context.BIND_AUTO_CREATE);
        isBound = true;
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageFromPositionService, new IntentFilter(PositionSampleService.INTENT_FILTER_NAME));

    }

    private ServiceConnection positionSampleConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            PositionSampleService.MyLocalBinder binder = (PositionSampleService.MyLocalBinder) service;
            mPositionSampleService = binder.gerService();
            mPositionSampleService.registerListener();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };

    private void setPositionsTextViews() {
        tv_InitialAccelerometer = (TextView) findViewById(R.id.initial_Acceleration);
        tv_CurrentAccelerometer = (TextView) findViewById(R.id.current_Acceleration);
    }


    /**
     * handle broadcast messages from position sampler service
     */
    private BroadcastReceiver mMessageFromPositionService = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            Bundle dataFromService = intent.getExtras();
            String action = dataFromService.getString(GeneralServiceParams.BUNDLE_ACTION);
            switch (GeneralServiceParams.ACTIONS.valueOf(action)) {
                case UPDATE_INITIAL_POSITION:
                    String initialValues = dataFromService.getString(GeneralServiceParams.BUNDLE_DATA);
                    tv_InitialAccelerometer.setText(initialValues);
                    break;
                case UPDATE_CURRENT_POSITION:
                    String currentValues = dataFromService.getString(GeneralServiceParams.BUNDLE_DATA);
                    tv_CurrentAccelerometer.setText(currentValues);
                    break;
                case ADD_MINES_TO_GAME_BOARD:
                    addMinesToGameBoard();
                    break;

            }
        }
    };

    /**
     * function add mines on board and updates relevant cells on board
     */
    private void addMinesToGameBoard() {
        //we add + 1 to leave one remained cell in the worth case, otherwise we wont be able to click
        // onClick is locked when game is over (remained cell == 0)
        if (mineSweeperLogicManager.getGameStatus() == MineSweeperLogicManager.GameStatus.STARTED &&
                mineSweeperLogicManager.getBoard().getNumberOfBombs() + 1 < mineSweeperLogicManager.getBoard().getBoardSize()) {
            mineSweeperLogicManager.addMinesToGameBoard();
            buttonAdapter.setGameBoard(mineSweeperLogicManager.getBoard().getGameBoard());
            mMinesOnBoard = mineSweeperLogicManager.getBoard().getNumberOfBombs();
            setGameInfo();
        }
    }

    /**
     * GPS LOCATION
     */

    private void createBinnedGPSService() {
        Intent intent = new Intent(this, GPSTracker.class);
        bindService(intent, GPSTrackerServiceConnection, Context.BIND_AUTO_CREATE);
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageFromGPSService, new IntentFilter(GPSTracker.INTENT_FILTER_NAME));
    }

    private ServiceConnection GPSTrackerServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            GPSTracker.MyLocalBinder gpsBinder = (GPSTracker.MyLocalBinder) service;
            mGpsTrackerService = gpsBinder.gerService();
            //my method, not override
            mGpsTrackerService.startUsingGPS();

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mGpsTrackerService.stopUsingGPS();
        }
    };

    /**
     * handle messages from service
     */
    private BroadcastReceiver mMessageFromGPSService = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle dataFromService = intent.getExtras();
            String action = dataFromService.getString(GeneralServiceParams.BUNDLE_ACTION);
            switch (GeneralServiceParams.ACTIONS.valueOf(action)) {
                case Go_TO_SETTING_WINDOW:
                    startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                    break;
            }
        }
    };


    /**
     * Animation Methods
     */


    /**
     * method add tiles to current layout and draw them
     */
    private void playAnimation(boolean won) {
//        loadGif(won);
//        if (!won)
//            startFallingTiles();
    }

    /**
     * load the falling tiles translation
     */
    private void startFallingTiles() {
        int numberOfTiles = 10;
        for (int i = 0; i < numberOfTiles; i++) {
            TileAnimation ta = new TileAnimation(this);
            ta.playAnimation();
        }
    }

    /**
     * load gid animation when game ends
     *
     * @param won
     */
    private void loadGif(boolean won) {
        if (won) {
            mWebViewWinning.loadUrl(GeneralGameProperties.DANCING_BANANA_ANIMATION_1_URL);
            mWebViewWinning.setVisibility(View.VISIBLE);
        } else {
            mWebViewLosing.loadUrl(GeneralGameProperties.EXPLOSION_1_URL);
            mWebViewLosing.setVisibility(View.VISIBLE);
        }
        isAnimationShown = true;
    }

    /**
     * Method creates 3  gif animations
     */
    private void setGifWebView() {
        mWebViewWinning = (WebView) findViewById(R.id.gifAnimation_Winning);
        mWebViewWinning.setBackgroundColor(0);
        mWebViewWinning.setVisibility(View.GONE);
        mWebViewWinning.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                stopWebView();
                return true;
            }
        });

        mWebViewLosing = (WebView) findViewById(R.id.gifAnimation_Losing);
        mWebViewLosing.setBackgroundColor(0);
        mWebViewLosing.setVisibility(View.GONE);
        mWebViewLosing.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                stopWebView();
                return true;
            }
        });
    }

    /**
     * stop web views
     */
    private void stopWebView() {
        if (mWebViewLosing.getVisibility() == View.VISIBLE) {
            mWebViewLosing.stopLoading();
            mWebViewLosing.setVisibility(View.GONE);
        } else {
            mWebViewWinning.stopLoading();
            mWebViewWinning.setVisibility(View.GONE);
        }
        isAnimationShown = false;
    }


}
