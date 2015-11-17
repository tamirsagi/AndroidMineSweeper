/**
 * This Application was created as part of academic course
 * Tamir Sagi
 */

package com.minesweeper.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.*;
import android.widget.PopupWindow;
import com.minesweeper.BL.GeneralGameProperties;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    public void onButtonOptionsClicked(View view) {
        Intent i = new Intent(this, OptionsActivity.class);
        startActivityForResult(i, 0);
    }

    public void onButtonPlayClicked(View view) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String firstName = sharedPrefs.getString(OptionsActivity.KEY_PREF_PLAYER_NAME, "NULL");//get player name
        String lastName = sharedPrefs.getString(OptionsActivity.KEY_PREF_PLAYER_LAST_NAME, "NULL");//get player last name
        String prefLevel = sharedPrefs.getString(OptionsActivity.KEY_PREF_GAME_LEVEL, "Intermediate"); //get the selected level
        String gameSettings = GeneralGameProperties.getGameSettings(prefLevel); //get corresponded game settings
        boolean playSound = sharedPrefs.getBoolean(GeneralGameProperties.KEY_Play_Sound,true);  //get play sound settings

        Intent gameActivity = new Intent(this, GameActivity.class);
        gameActivity.putExtra(GeneralGameProperties.KEY_PLAYER_FULL_NAME, firstName + " " + lastName);
        try {
            JSONObject settings = new JSONObject(gameSettings);
            int rows = settings.getInt(GeneralGameProperties.RowsInBoard);         //rows in board
            int columns = settings.getInt(GeneralGameProperties.ColumnsInBoard);   //columns in board
            int mines = settings.getInt(GeneralGameProperties.MinesOnBoard);      //mines on board


            //Save Data for next Activity
            gameActivity.putExtra(GeneralGameProperties.KEY_GAME_LEVEL, prefLevel);
            gameActivity.putExtra(GeneralGameProperties.KEY_GAME_BOARD_ROWS, rows);
            gameActivity.putExtra(GeneralGameProperties.KEY_GAME_BOARD_COLUMNS, columns);
            gameActivity.putExtra(GeneralGameProperties.KEY_GAME_BOARD_MINES, mines);
            gameActivity.putExtra(GeneralGameProperties.KEY_Play_Sound, playSound);

            //Chane activity
            startActivity(gameActivity);
        } catch (JSONException e) {
            Log.e("Main Activity - JSON", e.getMessage());
        }
    }


    public void onButtonAboutClicked(View v){
        View popupView = new View(MainActivity.this);
        final PopupWindow popupWindow = new PopupWindow(popupView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        Drawable drawable = getResources().getDrawable(R.drawable.about);

        popupWindow.setBackgroundDrawable(drawable);
        popupWindow.setHeight(400);
        popupWindow.setWidth(600);
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                popupWindow.dismiss();
                return false;
            }
        });
        popupWindow.showAtLocation(findViewById(R.id.BTH_options), Gravity.CENTER, 0, 0);
    }

}
