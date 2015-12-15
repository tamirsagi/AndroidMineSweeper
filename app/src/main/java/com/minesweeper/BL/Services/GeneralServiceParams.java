package com.minesweeper.BL.Services;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

/**
 * @author Tamir Sagi
 * This Class hold shared parameters among the 2 services including static method to update acticity
 */
public class GeneralServiceParams {



    public static final String BUNDLE_ACTION = "ACTION";
    public static final String BUNDLE_DATA = "BUNDLE DATA";


    public enum ACTIONS {
        UPDATE_INITIAL_POSITION, UPDATE_CURRENT_POSITION, ADD_MINES_TO_GAME_BOARD,Go_TO_SETTING_WINDOW
    }


    /**
     * update game activity to change the board
     *
     * @param action - the action to take place where service is bonded
     */
    public static void sendMessageToActivity(Context context,String intentFilterNAme,String action, String data) {
        Intent intent = new Intent(intentFilterNAme);
        Bundle bundle = new Bundle();
        bundle.putString(GeneralServiceParams.BUNDLE_ACTION, action);
        bundle.putString(GeneralServiceParams.BUNDLE_DATA, data);
        intent.putExtras(bundle);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }



}
