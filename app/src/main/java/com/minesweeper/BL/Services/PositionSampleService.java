package com.minesweeper.BL.Services;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

import java.text.DecimalFormat;

public class PositionSampleService extends Service implements SensorEventListener {

    public static final String TAG = "PositionSampleService";
    public static final String INTENT_FILTER_NAME = "SERVICE NOTIFIER";
    public static final String BUNDLE_ACTION = "ACTION";
    public static final String BUNDLE_DATA = "BUNDLE DATA";
    public static final String BUNDLE_DATA_ADD_MINES = "phone's angle was deviated for long time, mines added to board";
    public static final int Number_Of_AXIS = 3;

    private static final int MILI = 1000;
    private static final int SECONDS_MINUTE = 60;
    DecimalFormat decimalFormat = new DecimalFormat("#.##");

    private final int minAngleDeviation = 10;
    private final int minTimeToAngleDeviation = 90; // after 1.5 minutes

    private float[] initialValues = new float[Number_Of_AXIS];
    private double initialAngle;


    private boolean timerStarted = false;
    private boolean addMines = false;
    private long startTime;
    private long endTime;


    public enum ACTIONS {
        UPDATE_INITIAL_POSITION, UPDATE_CURRENT_POSITION, ADD_MINES_TO_GAME_BOARD
    }

    ;

    //Bind the Client which is the game Activity to the service
    //We use an Object for that
    private final IBinder positioningKeeper = new MyLocalBinder();

    private Sensor accelerometer;
    private SensorManager sensorManager;
    private boolean initial;

    public PositionSampleService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        setAccelerationSensor();
        return positioningKeeper;
    }

    private void setAccelerationSensor() {
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        initial = true;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        String position, action, angle;
        position = "X:" + decimalFormat.format(event.values[0]) + "\nY:" + decimalFormat.format(event.values[1]) +
                "\nZ:" + decimalFormat.format(event.values[2]);
        double radians = (Math.atan2(event.values[0], event.values[1])); //radians
        double vAngle = radians * (180 / Math.PI);                      //radians to degrees
        angle = "angle : " + decimalFormat.format(vAngle);
        if (initial) {
            initial = false;
            initialValues[0] = event.values[0];                         // X
            initialValues[1] = event.values[1];                         // Y
            initialValues[2] = event.values[2];                         // Z
            initialAngle = vAngle;
            position = "Initial\n" + position + "\n" + angle;
            action = ACTIONS.UPDATE_INITIAL_POSITION.toString();
        } else {
            position = "Current\n" + position + "\n" + angle;
            action = ACTIONS.UPDATE_CURRENT_POSITION.toString();
        }
        //update position
        sendMessageToActivity(action, position);

        //Check duration for deviation
        if (Math.abs(vAngle - initialAngle) >= minAngleDeviation) {
            if (!timerStarted) {
                timerStarted = true;
                startTime = System.currentTimeMillis() / MILI;
            }
            endTime = System.currentTimeMillis() / MILI;
            if (! addMines && endTime - startTime >= minTimeToAngleDeviation) {
                addMines = true;

            }

            if(addMines){
                action = ACTIONS.ADD_MINES_TO_GAME_BOARD.toString();
                sendMessageToActivity(action, "");
            }
        }
        else{
            timerStarted = false;
            startTime = 0;
            endTime = 0;
            addMines = false;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    //we need to extends Binder in order to make our object as a binder
    public class MyLocalBinder extends Binder {

        public PositionSampleService gerService() {
            return PositionSampleService.this;
        }

    }

    /**
     * update game activity to change the board
     *
     * @param action - the action to take place where service is bonded
     */
    private void sendMessageToActivity(String action, String data) {
        Intent intent = new Intent(INTENT_FILTER_NAME);
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_ACTION, action);
        bundle.putString(BUNDLE_DATA, data);
        intent.putExtras(bundle);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }


}

