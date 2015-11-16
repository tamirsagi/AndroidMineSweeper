/**
 * This Application was created as part of academic course
 * Tamir Sagi
 */



package com.minesweeper.app;

import android.os.Bundle;
import android.preference.PreferenceActivity;


public class OptionsActivity extends PreferenceActivity {

    public static final String KEY_PREF_PLAYER_NAME = "prefPlayerName";
    public static final String KEY_PREF_PLAYER_LAST_NAME = "prefPlayerLastName";
    public static final String KEY_PREF_GAME_LEVEL = "prefGameLevel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.game_settings);
    }


}
